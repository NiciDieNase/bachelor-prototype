package de.inovex.fbuerkle;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.activeandroid.query.Select;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;
import java.util.List;

import de.inovex.fbuerkle.datamodel.Checklist;
import de.inovex.fbuerkle.datamodel.Questions.ChecklistItem;

/**
 * Created by felix on 09/01/15.
 */
public class ChecklistManager extends WearableListenerService {
	private static final String TAG = "de.inovex.fbuerkle.ChecklistManager";
	private final IBinder mBinder = new SyncBinder();
	private GoogleApiClient mGoogleApiClient;
	private ArrayList<String> checklists;

	public static final String KEY_CHECKLISTS = "checklists";
	public static final String LIST_OF_CHECKLIST = "/checklists";
	public static final String CHECKLIST = "/checklist/"; // + checklist-name
	//public final String ITEMS = "/items/"; // /checklist/[name]/items/[n]

	@Override
	public void onDataChanged(DataEventBuffer dataEvents) {
		for (DataEvent event : dataEvents) {
			if (event.getType() == DataEvent.TYPE_DELETED) {
				Log.d(TAG, "DataItem deleted: " + event.getDataItem().getUri());
			} else if (event.getType() == DataEvent.TYPE_CHANGED) {
				Log.d(TAG, "DataItem changed: " + event.getDataItem().getUri());

				String path = event.getDataItem().getUri().getPath();

				// List of Checklists
				if(path.matches(LIST_OF_CHECKLIST)){
					Log.d(TAG,"list of checklists changed");
					DataMap dataMap = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
					this.checklists = dataMap.getStringArrayList(KEY_CHECKLISTS);
				}
				// A Checklist
				else if(path.startsWith(CHECKLIST) && !path.endsWith("/items")){
					// TODO handle checklist change
					String name = path.split("/")[1];
					Log.d(TAG,"a checklist changed: " + name);
				}
				// Items of a checklist
				else if(path.startsWith(CHECKLIST) && path.endsWith("/items")){
					// TODO handle items change
					String name = path.split("/")[1];
					Log.d(TAG,"checklist-items changed: " + name);
				}
			}
}
	}

	public class SyncBinder extends Binder {
		public ChecklistManager getService() {
			return ChecklistManager.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
				.build();
	}


	public List<String> getChecklists() {
		if(null == checklists){
		}
		this.updateChecklists();
		return checklists;
	}

	private void updateChecklists() {
		PendingResult<DataApi.DataItemResult> result = Wearable.DataApi.getDataItem(mGoogleApiClient, Uri.parse("wear:/checklists"));
		result.setResultCallback(
				new ResultCallback<DataApi.DataItemResult>() {
					@Override
					public void onResult(DataApi.DataItemResult dataItemResult) {
						DataMapItem mapItem = DataMapItem.fromDataItem(dataItemResult.getDataItem());
						ChecklistManager.this.checklists = mapItem.getDataMap().getStringArrayList("checklists");
					}
				});
	}




	public Checklist getChecklist(String name) {
		// TODO
		return null;
	}

	public void syncChecklist(Checklist checklist) {
		PutDataMapRequest metaChecklist = PutDataMapRequest.create("/checklists/" + checklist.name.toLowerCase().replace(" ","_"));
		DataMap dataMap = metaChecklist.getDataMap();

		dataMap.putString(DataKeys.name, checklist.name);
		dataMap.putString(DataKeys.description, checklist.description);

		PendingResult<DataApi.DataItemResult> metaChecklistResult = Wearable.DataApi
				.putDataItem(this.mGoogleApiClient, metaChecklist.asPutDataRequest());
		metaChecklistResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
			@Override
			public void onResult(DataApi.DataItemResult dataItemResult) {
				Log.d(TAG,dataItemResult.getStatus().toString());
			}
		});

		PutDataMapRequest checklistItem = PutDataMapRequest.create("/checklists/" + checklist.name.toLowerCase() + "/items");
		Bundle bundle = new Bundle();
		List<ChecklistItem> items = checklist.items();
//		bundle.putParcelableArrayList("items",items);
		bundle.putInt("size", items.size());
		for (int i = 0; i < items.size(); i++) {
			bundle.putParcelable(String.valueOf(i), items.get(i));
		}
		checklistItem.getDataMap().putAll(DataMap.fromBundle(bundle));
		PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
				.putDataItem(this.mGoogleApiClient, checklistItem.asPutDataRequest());
		pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
			@Override
			public void onResult(DataApi.DataItemResult dataItemResult) {
				Log.d(TAG,dataItemResult.getStatus().toString());
			}
		});
	}

	public void publishListOfChecklists(){
		List<Checklist> checklists =  new Select().from(Checklist.class).execute();
		ArrayList<String> listNames = new ArrayList<String>();
		for(Checklist list : checklists){
			listNames.add(list.name);
		}
		PutDataMapRequest dataMap = PutDataMapRequest.create(LIST_OF_CHECKLIST);
		dataMap.getDataMap().putStringArrayList(KEY_CHECKLISTS,listNames);
		PutDataRequest request = dataMap.asPutDataRequest();
		PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient,request);
		pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
			@Override
			public void onResult(DataApi.DataItemResult dataItemResult) {
				Log.d(TAG, dataItemResult.getStatus().toString());
			}
		});
	}
}
