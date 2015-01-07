package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

public class ChecklistActivity extends Activity implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks {

	private static final String TAG = "ChecklistActivity";
	private TextView mTextView;
	private GoogleApiClient mGoogleApiClient;
	private String text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checklist);
		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				mTextView = (TextView) stub.findViewById(R.id.text);
			}
		});
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
				.addConnectionCallbacks(this)
				.build();

	}



	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();

		Wearable.DataApi.getDataItems(mGoogleApiClient, Uri.parse("wear:/de.inovex/checklist/item"))
				.setResultCallback(new ResultCallback<DataItemBuffer>() {
					@Override
					public void onResult(DataItemBuffer dataItems) {
						if(dataItems.getStatus().isSuccess()){

							for(DataItem dataItem:dataItems){
								String s = DataMapItem.fromDataItem(dataItem).getDataMap().getString("item");
								mTextView.setText(s);
							}
						}
					}
				});

	}

	@Override
	protected void onStop() {
		if (null != mGoogleApiClient && mGoogleApiClient.isConnected()) {
			Wearable.DataApi.removeListener(mGoogleApiClient, this);
			mGoogleApiClient.disconnect();
		}
		super.onStop();
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
	public void onConnected(Bundle bundle) {
		Wearable.DataApi.addListener(mGoogleApiClient,this);
	}

	@Override
	public void onConnectionSuspended(int i) {

	}
}
