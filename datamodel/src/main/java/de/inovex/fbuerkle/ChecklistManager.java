package de.inovex.fbuerkle;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

import de.inovex.fbuerkle.datamodel.Checklist;
import de.inovex.fbuerkle.datamodel.Questions.ChecklistItem;

/**
 * Created by felix on 09/01/15.
 */
public class ChecklistManager extends Service implements GoogleApiClient.ConnectionCallbacks, DataApi.DataListener {
	private static final String TAG = "de.inovex.fbuerkle.ChecklistManager";
	private final IBinder mBinder = new SyncBinder();
	private GoogleApiClient mGoogleApiClient;
	private ArrayList<String> checklists;

	@Override
	public IBinder onBind(Intent intent) {
		updateChecklists();
		return mBinder;
	}

	public class SyncBinder extends Binder {
		public ChecklistManager getService() {
			return ChecklistManager.this;
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		Wearable.DataApi.addListener(mGoogleApiClient, this);
	}

	@Override
	public void onConnectionSuspended(int i) {
		Wearable.DataApi.removeListener(mGoogleApiClient, this);
	}

	@Override
	public void onDataChanged(DataEventBuffer dataEvents) {
		for (DataEvent event : dataEvents) {
			if (event.getType() == DataEvent.TYPE_DELETED) {
				Log.d(TAG, "DataItem deleted: " + event.getDataItem().getUri());
			} else if (event.getType() == DataEvent.TYPE_CHANGED) {
				Log.d(TAG, "DataItem changed: " + event.getDataItem().getUri());
			}
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
				.addConnectionCallbacks(this)
				.build();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public List<String> getChecklists() {
		if(null == checklists){
			this.updateChecklists();
		}
		return checklists;
	}

	private void updateChecklists() {
		ChecklistsUpdate update = new ChecklistsUpdate();
		update.execute();
	}

	private class ChecklistsUpdate extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			PendingResult<DataItemBuffer> result = Wearable.DataApi.getDataItems(mGoogleApiClient, Uri.parse("wear:/checklists/"));
			result.setResultCallback(new ResultCallback<DataItemBuffer>() {
				@Override
				public void onResult(DataItemBuffer dataItems) {
					if (dataItems.getCount() != 0) {
						DataMapItem mapItem = DataMapItem.fromDataItem(dataItems.get(0));
						Log.d(TAG, "Handling DataItem: " + mapItem.getUri().toString());

						ChecklistManager.this.checklists = mapItem.getDataMap().getStringArrayList("checklists");
						Log.d(TAG, "Got list: " + ChecklistManager.this.checklists.toString());

						dataItems.release();
						Toast.makeText(ChecklistManager.this,"Updated List",Toast.LENGTH_LONG).show();
					}
				}
			});
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			Toast.makeText(ChecklistManager.this,"update finished",Toast.LENGTH_SHORT).show();
		}
	}

	public Checklist getChecklist(String name) {
		return null;
	}

	public void syncChecklist(Checklist checklist) {
		PutDataMapRequest metaChecklist = PutDataMapRequest.create("/checklists/" + checklist.name.toLowerCase());
		DataMap dataMap = metaChecklist.getDataMap();

		dataMap.putString(DataKeys.name, checklist.name);
		dataMap.putString(DataKeys.description, checklist.description);

		PendingResult<DataApi.DataItemResult> metaChecklistResult = Wearable.DataApi
				.putDataItem(this.mGoogleApiClient, metaChecklist.asPutDataRequest());

		List<ChecklistItem> items = checklist.items();
		Bundle bundle;
		for (int i = 0; i < items.size(); i++) {
			PutDataMapRequest checklistItem = PutDataMapRequest.create("/checklists/" + checklist.name.toLowerCase() + "/" + i);
			bundle = new Bundle();
//			bundle.putParcelableArrayList("items",items);
			bundle.putParcelable(String.valueOf(i), items.get(i));
//			bundle.putInt("size", items.size());
			checklistItem.getDataMap().putAll(DataMap.fromBundle(bundle));
			Wearable.DataApi.putDataItem(this.mGoogleApiClient, checklistItem.asPutDataRequest());
		}
	}

	public void publishListOfChecklists(){
		List<Checklist> checklists =  new Select().from(Checklist.class).execute();
		ArrayList<String> listNames = new ArrayList<String>();
		for(Checklist list : checklists){
			listNames.add(list.name);
		}
		PutDataMapRequest dataMap = PutDataMapRequest.create("/checklists/");
		dataMap.getDataMap().putStringArrayList("checklists",listNames);
		PutDataRequest request = dataMap.asPutDataRequest();
		PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient,request);
		Log.d(TAG,"Published: " + listNames.toString());
	}
}
