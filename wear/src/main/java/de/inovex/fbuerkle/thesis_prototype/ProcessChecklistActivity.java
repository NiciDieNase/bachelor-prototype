package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

import de.inovex.fbuerkle.DataKeys;
import de.inovex.fbuerkle.datamodel.Checklist;
import de.inovex.fbuerkle.datamodel.Questions.CheckItem;
import de.inovex.fbuerkle.datamodel.Questions.ChecklistItem;
import de.inovex.fbuerkle.datamodel.Questions.DecisionItem;

/**
 * Created by felix on 07/01/15.
 */
public class ProcessChecklistActivity extends Activity {
	private static final String TAG = "de.inovex.fbuerkle.thesis_prototype.ProcessChecklistActivity";
	private Checklist currentChecklist;
	private List<ChecklistItem> currentChecklistItems = new ArrayList<ChecklistItem>();

	@Override
	public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.process_checklist);

		// TODO get actual checklist from Intent
	}

}
