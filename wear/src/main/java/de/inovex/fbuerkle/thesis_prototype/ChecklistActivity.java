package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

import de.inovex.fbuerkle.ChecklistManager;
import de.inovex.fbuerkle.datamodel.Checklist;
import de.inovex.fbuerkle.datamodel.Questions.CheckItem;
import de.inovex.fbuerkle.datamodel.Questions.ChecklistItem;
import de.inovex.fbuerkle.datamodel.Questions.DecisionItem;

public class ChecklistActivity extends Activity implements ChecklistFragment.OnChecklistItemResultListener, GoogleApiClient.ConnectionCallbacks, DataApi.DataListener {

	private static final int NOTIFICATION_ID = 42;
	private boolean mBound;
	private static final String TAG = "ChecklistActivity";
	private ChecklistManager mSyncService;
	protected WearableStringListAdapter checklistAdapter;
	private int currentListItem = -1;
	List<ChecklistItem> listItems;

	private String[] checklistNames;

	private List<Checklist> checklists = new ArrayList<Checklist>();
	private GoogleApiClient mGoogleApiClient;

	public ChecklistActivity(){
		this.listItems = new ArrayList<ChecklistItem>();
		listItems.add(new CheckItem("Hello",null));
		listItems.add(new DecisionItem("Is everything okay?"));
		listItems.add(new DecisionItem("Did you like the talk?"));
		listItems.add(new DecisionItem("Was the\ntalk too long?"));
		listItems.add(new CheckItem("Goodbye",null));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
				.addConnectionCallbacks(this)
				.build();
		mGoogleApiClient.connect();

//		setContentView(R.layout.process_checklist);
//		Bundle extras = this.getIntent().getExtras();
//		if(extras != null && extras.containsKey("currentItem")){
//			this.currentListItem = extras.getInt("currentItem");
//		}
//		this.nextFragment();

		setContentView(R.layout.activity_checklist);
		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				WearableListView listView = (WearableListView) findViewById(R.id.wearable_list);
				checklistAdapter = new WearableStringListAdapter(ChecklistActivity.this);
				updateChecklists();
				listView.setAdapter(checklistAdapter);
				listView.setClickListener(mClickListener);
				Log.i(TAG, "setting adapter and click listener");
			}
		});
	}

	private void updateChecklists() {
		Uri uri = Uri.parse("wear:/checklists");
		PendingResult<DataItemBuffer> result = Wearable.DataApi.getDataItems(mGoogleApiClient, uri);
		result.setResultCallback(new ResultCallback<DataItemBuffer>() {
					@Override
					public void onResult(DataItemBuffer dataItems) {
						if (dataItems.getCount() > 0) {
							DataMapItem mapItem = DataMapItem.fromDataItem(dataItems.get(0));
							Log.d(TAG, "Handling DataItem: " + mapItem.getUri().toString());
							ArrayList<String> checklists = mapItem.getDataMap().getStringArrayList("checklists");
							checklistAdapter.updateStrings(checklists);
							dataItems.release();
						}
					}
				});
	}


	private WearableListView.ClickListener mClickListener = new WearableListView.ClickListener() {

		@Override
		public void onClick(WearableListView.ViewHolder viewHolder) {
			Log.d(TAG,"item click");
			updateChecklists();
		}

		@Override
		public void onTopEmptyRegionClick() {
			Log.d(TAG, "empty region click");
			updateChecklists();
		}
	};


	private Fragment getNextItemFragment(){
		if(currentListItem>=listItems.size()-1){
			return null;
		} else {
			currentListItem++;
			if(CheckItem.class.equals(listItems.get(currentListItem).getClass())){
				return new ChecklistItemConfirmFragment((CheckItem) listItems.get(currentListItem));
			} else if (DecisionItem.class.equals(listItems.get(currentListItem).getClass())){
				return new ChecklistItemDecisionFragment((DecisionItem) listItems.get(currentListItem));
			} else {
				Log.e(TAG, "Unknown Item type");
				return null;
			}
		}
	}

	@Override
	public void onConfirm() {
		this.nextFragment();
	}

	@Override
	public void onOK() {
		this.nextFragment();
	}

	@Override
	public void onCancel() {
		this.nextFragment();
	}

	@Override
	public void onValueSelected() {
		this.nextFragment();
	}

	private void nextFragment(){
		Fragment nextItemFragment = this.getNextItemFragment();
		if(nextItemFragment != null){
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.fragment_container, nextItemFragment,"fragment_container");
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			transaction.commit();
		} else {
			NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(NOTIFICATION_ID);
			finish();
//			Toast.makeText(this, "Done", Toast.LENGTH_SHORT);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// create Notification to restart/continue
		Intent notificationIntent = new Intent(this, ChecklistActivity.class);
		notificationIntent.putExtra("currentItem", this.currentListItem);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification.Action restart = new Notification.Action(R.drawable.transparent_icon, null, pendingIntent);

		Notification.Builder builder = new Notification.Builder(this)
				.setContentTitle("Checklist")
				.setContentText("continue")
				.setSmallIcon(R.drawable.transparent_icon)
				.addAction(restart)
				.extend(new Notification.WearableExtender()
						.setContentAction(0)
						.setHintHideIcon(true)
				);

		NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
		Notification notification = builder.build();
		notification.flags &= Notification.FLAG_AUTO_CANCEL;
		notificationManagerCompat.notify(NOTIFICATION_ID, notification);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Bundle extras = this.getIntent().getExtras();
		if(extras != null && extras.containsKey("currentItem")){
			this.currentListItem = extras.getInt("currentItem")-1;
			this.nextFragment();
		}
		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
	}

	@Override
	public void onConnected(Bundle bundle) {
		Wearable.DataApi.addListener(mGoogleApiClient,this);
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onDataChanged(DataEventBuffer dataEvents) {
		for (DataEvent event : dataEvents) {
			Log.d(TAG,"DataEvent: " + event.toString() + " URI: " + event.getDataItem().getUri());
		}
	}
}
