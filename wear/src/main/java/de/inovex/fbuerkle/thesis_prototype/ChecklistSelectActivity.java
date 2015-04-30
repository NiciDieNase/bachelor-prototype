package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import de.inovex.fbuerkle.DataKeys;

/**
 * Created by felix on 16/04/15.
 */
public class ChecklistSelectActivity extends Activity implements ChecklistSelectFragment.OnChecklistSelectedListener {

	private static final String TAG = "ChecklistSelectActivity";
	private GoogleApiClient mGoogleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
				.build();
		mGoogleApiClient.connect();
		setContentView(R.layout.process_checklist);
		Bundle extras = this.getIntent().getExtras();
		startChecklistSelection();
		if (extras != null && extras.containsKey(DataKeys.CURRENT_ITEM)&& extras.containsKey(DataKeys.CHECKLIST)) {
			startChecklist(extras.getString(DataKeys.CHECKLIST),extras.getInt(DataKeys.CURRENT_ITEM));
		}
	}

	private void startChecklistSelection() {
		ChecklistSelectFragment selectFragment = new ChecklistSelectFragment(this, mGoogleApiClient);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_container, selectFragment, "fragment_container");
		transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
		transaction.commit();
	}

	@Override
	public void onChecklistSelected(String name) {
		startChecklist(name, -1);
	}

	private void startChecklist(String name, int index) {
		Log.d(TAG, "Start CHECKLIST: " + name);
		Intent startChecklist = new Intent(this,ChecklistProcessActivity.class);
		startChecklist.putExtra(DataKeys.CHECKLIST, name);
		if(index >= 0){
			startChecklist.putExtra(DataKeys.CURRENT_ITEM, index);
		}
		startActivity(startChecklist);
	}
}
