package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.inovex.fbuerkle.datamodel.Checklist;


public class MainActivity extends Activity{

	private static final String TAG = "de.inovex.fbuerkle.checklist";

	public GoogleApiClient getmGoogleApiClient() {
		return mGoogleApiClient;
	}

	private GoogleApiClient mGoogleApiClient;

	public Checklist currentChecklist;

	public MainActivity(){
		super();
		this.currentChecklist = new Select()
				.from(Checklist.class)
				.where("Name = ?","Test")
				.executeSingle();

		if(currentChecklist == null){
			currentChecklist = new Checklist();
			currentChecklist.name = "Test";
			currentChecklist.save();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(	R.layout.activity_main);

		ListView listView = (ListView) findViewById(R.id.listView);
		ChecklistAdapter adapter = new ChecklistAdapter(this, currentChecklist);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(adapter);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle connectionHint) {
                    Log.d(TAG, "onConnected: " + connectionHint);
                    // Now you can use the Data Layer API
                }
                @Override
                public void onConnectionSuspended(int cause) {
                    Log.d(TAG, "onConnectionSuspended: " + cause);
                }
        })
        .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult result) {
                    Log.d(TAG, "onConnectionFailed: " + result);
                }
            })
        // Request access only to the Wearable API
        .addApi(Wearable.API)
        .build();
		mGoogleApiClient.connect();
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

		switch (id){
			case R.id.action_add_item:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Pick Item-Type")
						.setItems(R.array.item_types,new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int position) {
								switch (position){
									case 0:
										NewItemFragment cf = new NewItemFragment(ItemTypes.Check);
										cf.show(getFragmentManager(),"NewCheckItemFragment");
										break;
									case 1:
										NewItemFragment df = new NewItemFragment(ItemTypes.Decision);
										df.show(getFragmentManager(), "NewDescisionItemFragment");
										break;
									default:
										Toast.makeText(MainActivity.this,"Not Implemented",Toast.LENGTH_SHORT).show();
								}
							}
						});
				builder.show();
				return true;
			case R.id.action_sync:
				new AsyncTask(){
					@Override
					protected Object doInBackground(Object[] params) {
						List<Checklist> checklists =  new Select().from(Checklist.class).execute();
						ArrayList<String> listNames = new ArrayList<String>();

						for(Checklist list : checklists){
							PutDataMapRequest dataMap = PutDataMapRequest.create("/checklists/"+list.name.toLowerCase());
							dataMap.getDataMap().putString("name",list.name);
							dataMap.getDataMap().putString("description",list.description);
							PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient,request);
							listNames.add(list.name);
						}

						PutDataMapRequest dataMap = PutDataMapRequest.create("/checklists");
						dataMap.getDataMap().putStringArrayList("checklists",listNames);
						PutDataRequest request = dataMap.asPutDataRequest();
						PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient,request);
						return null;
					}

					@Override
					protected void onPostExecute(Object o) {
						super.onPostExecute(o);
						Toast.makeText(MainActivity.this,"Current Checklist Synced",Toast.LENGTH_SHORT).show();
					}
				}.execute();
				return true;
			case R.id.action_add_checklist:
				new NewChecklistFragment().show(getFragmentManager(),"newChecklistFragment");
				return true;
			case R.id.action_settings:
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
