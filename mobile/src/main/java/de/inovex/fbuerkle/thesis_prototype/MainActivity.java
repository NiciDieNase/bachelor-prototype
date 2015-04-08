package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.inovex.fbuerkle.ChecklistManager;
import de.inovex.fbuerkle.datamodel.Checklist;


public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, DataApi.DataListener {

	private boolean mBound;

	private static final String TAG = "prototype.MainActivity";

	protected ChecklistManager mSyncService;

	public Checklist currentChecklist;
	private GoogleApiClient mGoogleApiClient;

	public MainActivity() {
		super();
		this.currentChecklist = new Select()
				.from(Checklist.class)
				.where("Name = ?", "Test")
				.executeSingle();

		if (currentChecklist == null) {
			currentChecklist = new Checklist();
			currentChecklist.name = "Test";
			currentChecklist.save();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
				.addConnectionCallbacks(this)
				.build();
		mGoogleApiClient.connect();

		setContentView(R.layout.activity_main);

		ListView listView = (ListView) findViewById(R.id.listView);
		ChecklistAdapter adapter = new ChecklistAdapter(this, currentChecklist);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(adapter);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
			case R.id.action_add_item:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Pick Item-Type")
						.setItems(R.array.item_types, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int position) {
								switch (position) {
									case 0:
										NewItemFragment cf = new NewItemFragment(ItemTypes.Check);
										cf.show(getFragmentManager(), "NewCheckItemFragment");
										break;
									case 1:
										NewItemFragment df = new NewItemFragment(ItemTypes.Decision);
										df.show(getFragmentManager(), "NewDescisionItemFragment");
										break;
									default:
										Toast.makeText(MainActivity.this, "Not Implemented", Toast.LENGTH_SHORT).show();
								}
							}
						});
				builder.show();
				return true;
			case R.id.action_sync:
				// TODO sync checklists
				// TODO publish checklists
				publishChecklists();
				return true;
			case R.id.action_add_checklist:
				new NewChecklistFragment().show(getFragmentManager(), "newChecklistFragment");
				publishChecklists();
				return true;
			case R.id.action_settings:
				return true;
			case R.id.action_test:
				PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/stringList");
				String[] strings1 = {"eggs", "bacon", "spam", "sausages"};
				ArrayList<String> strings = new ArrayList<String>(Arrays.asList(strings1));
				putDataMapRequest.getDataMap().putStringArrayList("stringList",strings );
				PutDataRequest request = putDataMapRequest.asPutDataRequest();
				if(mGoogleApiClient.isConnected()){
					PendingResult<DataApi.DataItemResult> pendingResult =
							Wearable.DataApi.putDataItem(mGoogleApiClient,request);
					pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
						@Override
						public void onResult(DataApi.DataItemResult dataItemResult) {
							Log.d(TAG,dataItemResult.toString());
						}
					});
				} else {
					Log.d(TAG,"GoogleAPI-Client not connected");
				}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void publishChecklists() {
		List<Checklist> checklists =  new Select().from(Checklist.class).execute();
		ArrayList<String> listNames = new ArrayList<>();
		for(Checklist list : checklists){
			listNames.add(list.name);
		}
		PutDataMapRequest dataMap = PutDataMapRequest.create("/checklists");
		dataMap.getDataMap().putStringArrayList("checklists",listNames);
		PendingResult<DataApi.DataItemResult> pendingResult =
				Wearable.DataApi.putDataItem(mGoogleApiClient,dataMap.asPutDataRequest());
		Log.d(TAG, "Publishing: " + listNames.toString());
		pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
			@Override
			public void onResult(DataApi.DataItemResult dataItemResult) {
				if(dataItemResult.getStatus().isSuccess()){
					Log.d(TAG,"Published: success");
				} else {
					Log.d(TAG,"Published: failure");
				}
			}
		});
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
}
