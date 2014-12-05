package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity implements ChecklistFragment.OnChecklistItemResultListener {

	private TextView mTextView;
	GridViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		pager = (GridViewPager) findViewById(R.id.pager);
		TestGridPagerAdapter pagerAdapter = new TestGridPagerAdapter(getFragmentManager(), this);
		pager.setAdapter(pagerAdapter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// create Notification to restart/continue
	}

	@Override
	public void onConfirm() {
		Log.d("Checklist","Item Confirmed");
		//pager.setCurrentItem(0,1,true);
	}

	@Override
	public void onOK() {
		Log.d("Checklist","Item Okay");
//		pager.setCurrentItem(0,0,true);
	}

	@Override
	public void onCancel() {
		Log.d("Checklist","Item Canceled");
//		pager.setCurrentItem(0,0,true);
	}

	@Override
	public void onValueSelected() {
		Log.d("Checklist","Value Selected");
	}
}
