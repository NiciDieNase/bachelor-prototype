package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

import de.inovex.fbuerkle.DataKeys;
import de.inovex.fbuerkle.Paths;
import de.inovex.fbuerkle.datamodel.Checklist;
import de.inovex.fbuerkle.datamodel.Questions.CheckItem;
import de.inovex.fbuerkle.datamodel.Questions.ChecklistItem;
import de.inovex.fbuerkle.datamodel.Questions.DecisionItem;

public class ChecklistProcessActivity extends Activity implements ChecklistFragment.OnChecklistItemResultListener{

	private static final int NOTIFICATION_ID = 42;
	private static final String TAG = "ChecklistActivity";
	protected WearableStringListAdapter checklistAdapter;
	private GoogleApiClient mGoogleApiClient;

	private int currentListItem = -1;
	List<ChecklistItem> listItems;
	private Checklist mChecklist;
	private int listLenght = -1;
	private boolean done = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
				.build();
		mGoogleApiClient.connect();
		setContentView(R.layout.process_checklist);
		listItems = new ArrayList<ChecklistItem>();
		Bundle extras = this.getIntent().getExtras();
		if(extras != null && extras.containsKey(DataKeys.CHECKLIST)){
			// load CHECKLIST
			if(extras.containsKey(DataKeys.CURRENT_ITEM)){
				// resume list
				this.currentListItem = extras.getInt(DataKeys.CURRENT_ITEM);
			} else {
				// start at beginning
				currentListItem = 0;
			}
			this.startChecklist(extras.getString(DataKeys.CHECKLIST));
		} else {
			// no list to process
			this.finish();
		}

	}

	private Fragment getFragmentForItem(int item){
		ChecklistItem checklistItem = listItems.get(item);
		if(checklistItem == null){
			return null;
		} else {
			if(CheckItem.class.equals(checklistItem.getClass())){
				return new ChecklistItemConfirmFragment((CheckItem) checklistItem);
			} else if (DecisionItem.class.equals(checklistItem.getClass())){
				return new ChecklistItemDecisionFragment((DecisionItem) checklistItem);
			} else {
				Log.e(TAG, "Unknown Item TYPE");
				return null;
			}
		}
	}

	private void startChecklist(final String name) {
		String path = Paths.PREFIX + Paths.CHECKLIST + name.toLowerCase().replace(" ","");
		Log.d(TAG, "loading: " + path);
		Wearable.DataApi.getDataItems(mGoogleApiClient, Uri.parse("wear:" + path))
			.setResultCallback(new ResultCallback<DataItemBuffer>() {
				@Override
				public void onResult(final DataItemBuffer dataItems) {
					if(dataItems.getCount() != 0){
						DataMapItem mapItem = DataMapItem.fromDataItem(dataItems.get(0));
						listLenght = mapItem.getDataMap().getInt(DataKeys.LENGTH);
						mChecklist = new Checklist(mapItem.getDataMap());
						if(listLenght == 0){
							finish();
						}
					}
					dataItems.release();
					loadChecklistItem(currentListItem)
							.setResultCallback(new NextItemCallback(currentListItem));
				}
			});
	}

	private void nextItem(){
		currentListItem++;
		if( currentListItem < listLenght){
			loadChecklistItem(currentListItem).setResultCallback(new NextItemCallback(currentListItem));
		} else {
			this.done=true;
			finish();
		}
	}

	private PendingResult loadChecklistItem(final int index){
		String itemPath = Paths.PREFIX + Paths.CHECKLIST + mChecklist.name.toLowerCase().replace(" ","") + Paths.ITEMS + index;
		Log.d(TAG, "Loading " + itemPath);
		return Wearable.DataApi.getDataItems(mGoogleApiClient,Uri.parse("wear:" + itemPath));
	}

	@Override
	public void onConfirm() {
		nextItem();
	}

	@Override
	public void onOK() {
		nextItem();
	}

	@Override
	public void onCancel() {
		nextItem();
	}

	@Override
	public void onValueSelected() {
		nextItem();
	}

	private void loadFragment(int i){
		Fragment nextItemFragment = this.getFragmentForItem(i);
		if(nextItemFragment != null){
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.fragment_container, nextItemFragment,"fragment_container");
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			transaction.commit();
		} else {
			this.done = true;
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(!done){

			// create Notification to restart/continue
			Intent notificationIntent = new Intent(this, ChecklistSelectActivity.class);
			notificationIntent.putExtra(DataKeys.CHECKLIST, this.mChecklist.name);
			notificationIntent.putExtra(DataKeys.CURRENT_ITEM, this.currentListItem);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			Notification.Action restart = new Notification.Action(R.drawable.transparent_icon, null, pendingIntent);

			Notification.Builder builder = new Notification.Builder(this)
					.setContentTitle(mChecklist.name)
					.setContentText("continue")
					.setSmallIcon(R.drawable.transparent_icon)
					.addAction(restart)
					.setAutoCancel(true)
					.extend(new Notification.WearableExtender()
									.setContentAction(0)
									.setHintHideIcon(true)
					);

			NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Notification notification = builder.build();
			notification.flags &= Notification.FLAG_AUTO_CANCEL;
			nm.notify(NOTIFICATION_ID,notification);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Bundle extras = this.getIntent().getExtras();
		if(extras != null && extras.containsKey("CURRENT_ITEM")){
			this.currentListItem = extras.getInt("CURRENT_ITEM")-1;
			this.loadFragment(currentListItem);
		}
		((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).cancelAll();
	}

	private class NextItemCallback implements ResultCallback<DataItemBuffer> {

		private int index;

		public NextItemCallback(int index){
			this.index = index;
		}

		@Override
		public void onResult(final DataItemBuffer dataItems) {
			while (listItems.size()-1 < index){
				listItems.add(null);
			}
			if( dataItems.getCount() > 0 ){
					DataMap map = DataMapItem.fromDataItem(dataItems.get(0)).getDataMap();
					switch(map.getString(DataKeys.TYPE)){
						case DataKeys.TYPE_CHECK:
							CheckItem checkItem = new CheckItem(map);
							checkItem.checklist = mChecklist;
							listItems.add(index,checkItem);
							break;
						case DataKeys.TYPE_DECISION:
							DecisionItem decisionItem = new DecisionItem(map);
							decisionItem.checklist = mChecklist;
							listItems.add(index,decisionItem);
							break;
						default:
							//unknown Type
							throw new RuntimeException("Unknown Item-TYPE");
					}
				loadFragment(index);
			}
			dataItems.release();
		}
	}
}
