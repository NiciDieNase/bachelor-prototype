package de.inovex.fbuerkle.thesis_prototype;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.support.wearable.view.WearableListView.OnCenterProximityListener;

/**
 * Created by felix on 10/10/14.
 */
public class WearableListItemLayout extends LinearLayout implements OnCenterProximityListener {

	private final float mFadedText;
	private final int inactiveCircleColor;
	private final int activeCircleColor;
	Context mContext;
	private float mScale;
	private ImageView mMarker;
	private TextView mText;
	private WearableListItemLayout mItem;

	public WearableListItemLayout(Context context) {
		this(context, null);
	}

	public WearableListItemLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WearableListItemLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScale = 1.5f;
		mFadedText = 0.5f;
		inactiveCircleColor = getResources().getColor(R.color.icon_active);
		activeCircleColor = getResources().getColor(R.color.icon_active);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

//		mItem = (WearableListItemLayout) findViewById(R.id.list_item);
		mMarker = (ImageView) findViewById(R.id.marker);
		mText = (TextView) findViewById(R.id.item_text);
	}

	@Override
	public void onCenterPosition(boolean b) {
		mText.setAlpha(1f);
		((GradientDrawable)mMarker.getDrawable()).setColor(activeCircleColor);
		((GradientDrawable)mMarker.getDrawable()).setAlpha(255);
		mMarker.setScaleX(mScale);
		mMarker.setScaleY(mScale);
	}

	@Override
	public void onNonCenterPosition(boolean b) {
		mText.setAlpha(mFadedText);
		((GradientDrawable)mMarker.getDrawable()).setColor(inactiveCircleColor);
		((GradientDrawable)mMarker.getDrawable()).setAlpha(120);
		mMarker.setScaleX(1f);
		mMarker.setScaleY(1f);
	}
}
