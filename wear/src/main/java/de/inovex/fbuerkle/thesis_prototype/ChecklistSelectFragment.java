package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * Created by felix on 16/04/15.
 */
public class ChecklistSelectFragment extends Fragment implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks {

	private static final String TAG = "ChecklistSelectFragment";
	OnChecklistSelectedListener mListener;
	private WearableStringListAdapter checklistAdapter;
	private Context mContext;
	private GoogleApiClient mGoogleApiClient;
	private ArrayList<String> checklists;
	private WearableListView.ClickListener mClickListener = new WearableListView.ClickListener() {

		@Override
		public void onClick(WearableListView.ViewHolder viewHolder) {
			int i = (int) viewHolder.itemView.getTag();
			Log.d(TAG, "Item clicked: " + i);
			mListener.onChecklistSelected(checklists.get(i));
		}

		@Override
		public void onTopEmptyRegionClick() {

		}
	};


	ChecklistSelectFragment(Context mContext, GoogleApiClient mGoogleApiClient){
		this.mContext = mContext;
		this.mGoogleApiClient = mGoogleApiClient;
	};

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.activity_checklist,null);
		final WatchViewStub stub = (WatchViewStub) view.findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				WearableListView listView = (WearableListView) view.findViewById(R.id.wearable_list);
				checklistAdapter = new WearableStringListAdapter(mContext);
				listView.setAdapter(checklistAdapter);
				listView.setClickListener(mClickListener);
				PendingResult<DataItemBuffer> result = Wearable.DataApi.getDataItems(mGoogleApiClient, Uri.parse("wear:/checklists/"));
				result.setResultCallback(new ResultCallback<DataItemBuffer>() {
					@Override
					public void onResult(DataItemBuffer dataItems) {
						if (dataItems.getCount() != 0) {
							DataMapItem mapItem = DataMapItem.fromDataItem(dataItems.get(0));
							Log.d(TAG, "Handling DataItem: " + mapItem.getUri().toString());
							checklists = mapItem.getDataMap().getStringArrayList("checklists");
							Log.d(TAG, "Got list: " + checklists.toString());
							checklistAdapter.updateStrings(checklists);
							dataItems.release();
						}
					}
				});
			}
		});
		return view;
	}

	public interface OnChecklistSelectedListener{
		public void onChecklistSelected(String name);
	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			mListener = (OnChecklistSelectedListener) activity;
		} catch (ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement OnChecklistSelectedListener.");
		}
	}

	@Override
	public void onDataChanged(DataEventBuffer dataEvents) {
		for(DataEvent event:dataEvents){
			if(event.getDataItem().getUri().getPath().startsWith("/checklists")){
				DataMapItem mapItem = DataMapItem.fromDataItem(event.getDataItem());
				checklistAdapter.updateStrings(mapItem.getDataMap().getStringArrayList("checklists"));
			}
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		Wearable.DataApi.addListener(mGoogleApiClient,this);
	}

	@Override
	public void onConnectionSuspended(int i) {
		Wearable.DataApi.removeListener(mGoogleApiClient,this);
	}
}