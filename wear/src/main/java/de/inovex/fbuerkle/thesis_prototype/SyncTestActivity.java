package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.Wearable;

public class SyncTestActivity extends Activity implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks {

	private static final String TAG = "SyncTestActivity";
	private GoogleApiClient mGoogleApiClient;
	protected WearableStringListAdapter checklistAdapter;

	private WearableListView.ClickListener mClickListener = new WearableListView.ClickListener() {
		@Override
		public void onClick(WearableListView.ViewHolder viewHolder) {
			Log.d(TAG,"List Item clicked");
			PendingResult<DataItemBuffer> result
					= Wearable.DataApi.getDataItems(mGoogleApiClient, Uri.parse("wear:/CHECKLISTS"));
			result.setResultCallback(new ResultCallback<DataItemBuffer>() {
				@Override
				public void onResult(DataItemBuffer dataItems) {
					Log.d(TAG,dataItems.getStatus().toString());
					for(DataItem item:dataItems){
						Log.d(TAG, item.getUri().toString());
					}
				}
			});
		}

		@Override
		public void onTopEmptyRegionClick() {

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checklist);
		this.mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addApi(Wearable.API)
				.build();

		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				WearableListView listView = (WearableListView) findViewById(R.id.wearable_list);
				checklistAdapter = new WearableStringListAdapter(SyncTestActivity.this);
				listView.setAdapter(checklistAdapter);
				listView.setClickListener(mClickListener);
			}
		});
	}


	/*************
	 *
	 * DataApi Stuff
	 *
	 */

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
	public void onConnected(Bundle bundle) {
		Wearable.DataApi.addListener(mGoogleApiClient, this);
	}

	@Override
	public void onConnectionSuspended(int i) {
		Wearable.DataApi.removeListener(mGoogleApiClient, this);
	}
}