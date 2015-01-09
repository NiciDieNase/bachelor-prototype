package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.inovex.fbuerkle.ChecklistManager;
import de.inovex.fbuerkle.datamodel.Checklist;

public class ChecklistActivity extends Activity {

	private boolean mBound;
	private static final String TAG = "ChecklistActivity";
	private ChecklistManager mSyncService;

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
				ChecklistAdapter checklistAdapter = new ChecklistAdapter(ChecklistActivity.this);
				listView.setAdapter(checklistAdapter);
				listView.setClickListener(mClickListener);
				Log.i(TAG, "setting adapter and click listener");
			}
		});
	}

	private WearableListView.ClickListener mClickListener = new WearableListView.ClickListener() {

		@Override
		public void onClick(WearableListView.ViewHolder viewHolder) {
			// TODO start checklist

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
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBound = false;
		}
	};

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
