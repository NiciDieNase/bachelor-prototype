package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import de.inovex.fbuerkle.datamodel.Checklist;

public class ChecklistActivity extends Activity implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, WearableListView.ClickListener {

	private static final String TAG = "ChecklistActivity";
	private GoogleApiClient mGoogleApiClient;
	private String[] checklistList;
	private List<Checklist> checklists = new ArrayList<Checklist>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checklist);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
				.addConnectionCallbacks(this)
				.build();

		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				WearableListView listView = (WearableListView) findViewById(R.id.wearable_list);
				ChecklistAdapter checklistAdapter = new ChecklistAdapter(ChecklistActivity.this);
				listView.setAdapter(checklistAdapter);
				listView.setClickListener(ChecklistActivity.this);
				Log.i(TAG,"setting adapter and click listener");
			}
		});

		updateChecklists();


	}

	private void updateChecklists() {
		PendingResult<DataItemBuffer> result = Wearable.DataApi.getDataItems(mGoogleApiClient, Uri.parse("wear:/checklists"));
		result.setResultCallback(new ResultCallback<DataItemBuffer>() {
			@Override
			public void onResult(DataItemBuffer dataItems) {
				if(dataItems.getCount() != 0){
					DataMapItem mapItem = DataMapItem.fromDataItem(dataItems.get(0));
					Log.d(TAG, mapItem.getUri().toString());

					List<String> checklists = mapItem.getDataMap().getStringArrayList("checklists");

					WearableListView listView = (WearableListView) findViewById(R.id.wearable_list);
					((ChecklistAdapter)listView.getAdapter()).updateChecklists(checklists);
					dataItems.release();
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
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
		updateChecklists();

	}

	@Override
	public void onConnected(Bundle bundle) {
		Wearable.DataApi.addListener(mGoogleApiClient, this);
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onClick(WearableListView.ViewHolder viewHolder) {
		// TODO start checklist
	}

	@Override
	public void onTopEmptyRegionClick() {

	}

	private class ChecklistAdapter extends WearableListView.Adapter {

			private final LayoutInflater mInflater;
			private Context mContext;
			private List<String> checklists;

			public ChecklistAdapter(Context context) {
				this.mContext = context;
				this.mInflater = LayoutInflater.from(mContext);
			}

			public ChecklistAdapter(Context context,List<String> checklists) {
				this(context);
				this.checklists = checklists;
			}

			public class MyViewHolder extends WearableListView.ViewHolder{
				private TextView textView;
				private ImageView imageView;
				public MyViewHolder(View itemView) {
					super(itemView);
					textView = (TextView) itemView.findViewById(R.id.item_text);
					imageView = (ImageView) itemView.findViewById(R.id.marker);
				}
			}

			@Override
			public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
				return new MyViewHolder(mInflater.inflate(R.layout.list_item,null));
			}

			@Override
			public void onBindViewHolder(WearableListView.ViewHolder viewHolder, int position) {
				MyViewHolder holder = (MyViewHolder) viewHolder;
				if(null != checklists) {
					holder.textView.setText(checklists.get(position));
				} else {
					holder.textView.setText("waiting for Items ...");
				}
				holder.itemView.setTag(position);
			}

			@Override
			public int getItemCount() {
				return null != checklists ? checklists.size() : 1 ;
			}

			public void updateChecklists(List<String> checklists) {
				this.checklists = checklists;
				this.notifyDataSetChanged();
			}
	}
}
