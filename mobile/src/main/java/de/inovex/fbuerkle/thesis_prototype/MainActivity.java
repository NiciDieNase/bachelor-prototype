package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import de.inovex.fbuerkle.thesis_prototype.model.Checklist;


public class MainActivity extends Activity implements View.OnClickListener {

	private static final String TAG = "de.inovex.fbuerkle.checklist";
	public final String PATH_PREFIX = "/de.inovex/checklist/";
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
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_descisionitem).setOnClickListener(this);
		findViewById(R.id.btn_newcheckitem).setOnClickListener(this);

		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(new ChecklistAdapter(this,currentChecklist));

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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_newcheckitem:
				NewItemFragment cf = new NewItemFragment(ItemTypes.Check);
				cf.show(getFragmentManager(),"NewCheckItemFragment");
				break;
			case R.id.btn_descisionitem:
				NewItemFragment df = new NewItemFragment(ItemTypes.Descision);
				df.show(getFragmentManager(), "NewDescisionItemFragment");
				break;
			default:
				Log.d(TAG, "Unhandled Click");
				break;
		}
	}

}
