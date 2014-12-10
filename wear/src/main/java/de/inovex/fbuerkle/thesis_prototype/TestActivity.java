package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

public class TestActivity extends Activity implements ChecklistFragment.OnChecklistItemResultListener {

	private static final int CONFIRMATION = 1;
	private TextView mTextView;
	GridViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if(savedInstanceState != null && savedInstanceState.containsKey("CurrentRow")){
			Log.d("Checklist","Current Row: " + savedInstanceState.get("CurrentRow"));
		}

		// Keep App active
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		pager = (GridViewPager) findViewById(R.id.pager);
		TestGridPagerAdapter pagerAdapter = new TestGridPagerAdapter(getFragmentManager(), this);
		pager.setAdapter(pagerAdapter);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("CurrentRow",pager.getCurrentItem().y);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(savedInstanceState.containsKey("CurrentRow")){
			int row = savedInstanceState.getInt("CurrentRow");
			pager.setCurrentItem(row, pager.getAdapter().getCurrentColumnForRow(row,0));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// create Notification to restart/continue
		Intent notificationIntent = new Intent(this, TestActivity.class);
//		notificationIntent.putExtra("CurrentRow", pager.getCurrentItem().y);
		PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
		Notification.Action restart = new Notification.Action(android.R.drawable.ic_media_play,null,pendingIntent);

		Notification.Builder builder = new Notification.Builder(this)
				.setContentTitle("Checklist")
				.setContentText("continue")
				.setSmallIcon(R.drawable.generic_confirmation_00163)
				.addAction(restart)
			.extend(new Notification.WearableExtender()
					.setContentAction(0));

					NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
		notificationManagerCompat.notify(23, builder.build());
	}


	@Override
	public void onConfirm() {
		Intent i = new Intent(this, ConfirmationActivity.class).setAction(Intent.ACTION_MAIN);
		i.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
		i.putExtra("CurrentRow",pager.getCurrentItem().y);
		startActivityForResult(i, TestActivity.CONFIRMATION);
		Log.d("Checklist","Item Confirmed");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( requestCode == TestActivity.CONFIRMATION){
			if(data != null && data.getExtras() != null && data.getExtras().containsKey("CurrentRow")){
				Log.d("Checklist","Current Row: " + data.getExtras().get("CurrentRow"));
			}
			pager.setCurrentItem(1,0,true);
		}
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
