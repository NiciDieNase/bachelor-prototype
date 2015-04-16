package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by felix on 16/04/15.
 */
public class ChecklistSelectActivity extends Activity implements ChecklistSelectFragment.OnChecklistSelectedListener {

	private GoogleApiClient mGoogleApiClient;
	private int currentListItem;
	private String currentChecklist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
//				.addConnectionCallbacks(this)
				.build();
		mGoogleApiClient.connect();
		setContentView(R.layout.process_checklist);
		Bundle extras = this.getIntent().getExtras();
		if (extras != null && extras.containsKey("currentItem")&& extras.containsKey("currentChecklist")) {
			this.currentListItem = extras.getInt("currentItem");
			this.currentChecklist = extras.getString("currentChecklist");
		} else {
			// Start checklist selection
			ChecklistSelectFragment selectFragment = new ChecklistSelectFragment(this, mGoogleApiClient);
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.fragment_container, selectFragment, "fragment_container");
			transaction.setTransition(FragmentTransaction.TRANSIT_NONE);
			transaction.commit();
		}
	}

	@Override
	public void onChecklistSelected(String name) {
		//TODO:  start checklist processing
	}
}
