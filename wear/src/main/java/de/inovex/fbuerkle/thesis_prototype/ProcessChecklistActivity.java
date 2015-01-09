package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

import de.inovex.fbuerkle.DataKeys;
import de.inovex.fbuerkle.datamodel.Checklist;
import de.inovex.fbuerkle.datamodel.Questions.CheckItem;
import de.inovex.fbuerkle.datamodel.Questions.ChecklistItem;
import de.inovex.fbuerkle.datamodel.Questions.DecisionItem;

/**
 * Created by felix on 07/01/15.
 */
public class ProcessChecklistActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, DataApi.DataListener {
	private static final String TAG = "de.inovex.fbuerkle.thesis_prototype.ProcessChecklistActivity";
	private GoogleApiClient mGoogleApiClient;
	private Checklist currentChecklist;
	private List<ChecklistItem> currentChecklistItems = new ArrayList<ChecklistItem>();

	@Override
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.process_checklist);
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
				.addConnectionCallbacks(this)
				.build();

		// TODO get actual checklist from Intent
		String checklist = "wear:/checklists/test";

		PendingResult<DataItemBuffer> getChecklist = Wearable.DataApi.getDataItems(mGoogleApiClient, Uri.parse(checklist));
		getChecklist.setResultCallback(new ResultCallback<DataItemBuffer>() {
			@Override
			public void onResult(DataItemBuffer dataItems) {
				if (dataItems.getCount() != 0) {
					Log.d(TAG, DataMapItem.fromDataItem(dataItems.get(0)).getUri().toString());

					DataMap mapItem = DataMapItem.fromDataItem(dataItems.get(0)).getDataMap();
					Checklist mChecklist = new Checklist();
					mChecklist.name = mapItem.getString(DataKeys.name);
					mChecklist.description = mapItem.getString(DataKeys.description);
					mChecklist.length = mapItem.getInt(DataKeys.length);
					ProcessChecklistActivity.this.currentChecklist = mChecklist;

					dataItems.release();
				}
			}
		});
		getChecklist.await();
		for (int i = 0; i < currentChecklist.length; i++) {
			PendingResult<DataItemBuffer> getItem = Wearable.DataApi.getDataItems(mGoogleApiClient, Uri.parse(checklist + "/" + i));
			getChecklist.setResultCallback(new ResultCallback<DataItemBuffer>() {
				@Override
				public void onResult(DataItemBuffer dataItems) {
					if (dataItems.getCount() != 0) {
						Log.d(TAG, DataMapItem.fromDataItem(dataItems.get(0)).getUri().toString());

						DataMap mapItem = DataMapItem.fromDataItem(dataItems.get(0)).getDataMap();
						Checklist mChecklist = new Checklist();
						mChecklist.name = mapItem.getString(DataKeys.name);
						mChecklist.description = mapItem.getString(DataKeys.description);
						mChecklist.length = mapItem.getInt(DataKeys.length);
						ProcessChecklistActivity.this.currentChecklist = mChecklist;

						dataItems.release();
					}
				}
			});
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

	private ChecklistItem getChecklistItemFromDataMap(DataMap dataMap) {
		ChecklistItem item;
		switch (dataMap.getString("type")) {
			case "CheckItem":
				item = new CheckItem();
			case "DecisionItem":
				item = new DecisionItem();
				item.title = dataMap.getString(DataKeys.title);
				((DecisionItem) item).redOption = dataMap.getString(DataKeys.redText);
				((DecisionItem) item).greenOption = dataMap.getString(DataKeys.greenText);
				break;

			// TODO handle other item-types
			default:
				item = new CheckItem();
				break;
		}
		;
		item.position = dataMap.getInt(DataKeys.position);
		item.title = dataMap.getString(DataKeys.title);
		item.description = dataMap.getString(DataKeys.description);
		return item;
	}
}
