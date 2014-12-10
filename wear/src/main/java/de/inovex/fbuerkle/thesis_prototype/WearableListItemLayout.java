package de.inovex.fbuerkle.thesis_prototype;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by felix on 10/10/14.
 */
public class WearableListItemLayout extends LinearLayout implements WearableListView.Item {

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
	public float getProximityMinValue() {
		return 1.0f;
	}

	@Override
	public float getProximityMaxValue() {
		return 1.5f;
	}

	@Override
	public float getCurrentProximityValue() {
		return mScale;
	}

	@Override
	public void setScalingAnimatorValue(float v) {
		mScale = v;

//		mItem.setScaleY(mScale);
		mMarker.setScaleX(mScale);
		mMarker.setScaleY(mScale);
	}

	@Override
	public void onScaleUpStart() {
		mText.setAlpha(1.0f);
		((GradientDrawable)mMarker.getDrawable()).setColor(activeCircleColor);
	}

	@Override
	public void onScaleDownStart() {
		((GradientDrawable)mMarker.getDrawable()).setColor(inactiveCircleColor);
		mText.setAlpha(mFadedText);

	}
}
