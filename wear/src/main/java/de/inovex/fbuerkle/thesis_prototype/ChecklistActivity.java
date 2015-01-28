package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.inovex.fbuerkle.ChecklistManager;
import de.inovex.fbuerkle.datamodel.Checklist;

public class ChecklistActivity extends Activity {

	private boolean mBound;
	private static final String TAG = "ChecklistActivity";
	private ChecklistManager mSyncService;
	protected WearableStringListAdapter checklistAdapter;

	private String[] checklistNames;

	private List<Checklist> checklists = new ArrayList<Checklist>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checklist);

		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub stub) {
				WearableListView listView = (WearableListView) findViewById(R.id.wearable_list);
				checklistAdapter = new WearableStringListAdapter(ChecklistActivity.this);
				if(mBound){
					checklistAdapter.updateStrings(mSyncService.getChecklists());
				}else {
					Log.d(TAG,"Couldn't update list because service is not bound");
				}
				listView.setAdapter(checklistAdapter);
				listView.setClickListener(mClickListener);
				Log.i(TAG, "setting adapter and click listener");
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent i = new Intent(this, ChecklistManager.class);
		bindService(i,mConnection,Context.BIND_AUTO_CREATE);
	}

//	@Override
//	protected void onStop() {
//		super.onStop();
//		if(mBound){
//			unbindService(mConnection);
//			mBound = false;
//		}
//	}

	private WearableListView.ClickListener mClickListener = new WearableListView.ClickListener() {

		@Override
		public void onClick(WearableListView.ViewHolder viewHolder) {
			// TODO start checklist

			if(mBound){
				Log.d(TAG,"updating checklists");
				checklistAdapter.updateStrings(mSyncService.getChecklists());
			} else {
				Log.d(TAG, "can't update, not bound");
			}
		}

		@Override
		public void onTopEmptyRegionClick() {

		}
	};

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ChecklistManager.SyncBinder binder = (ChecklistManager.SyncBinder) service;
			mSyncService = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBound = false;
		}
	};

}
