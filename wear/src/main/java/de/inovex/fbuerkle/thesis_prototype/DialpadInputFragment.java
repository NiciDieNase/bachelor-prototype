package de.inovex.fbuerkle.thesis_prototype;


import android.app.Fragment;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigInteger;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialpadInputFragment extends Fragment implements View.OnClickListener {

	static final int AUTO_LAYOUT = 0;
	static final int RECT_LAYOUT = 1;
	static final int ROUND_LAYOUT = 2;

	private int i;
	private BigInteger value = new BigInteger("0");
	private View layout;

	public DialpadInputFragment() {
		// Required empty public constructor
		i = 0;
	}

	public DialpadInputFragment(int i) {
		this.i = i;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		switch (this.i) {
			case AUTO_LAYOUT:
				layout = inflater.inflate(R.layout.number_input_dialpad, null);
				break;
			case RECT_LAYOUT:
				layout = inflater.inflate(R.layout.number_input_dialpad_square, null);
				break;
			case ROUND_LAYOUT:
				layout = inflater.inflate(R.layout.number_input_dialpad_round, null);
				break;
			default:
				layout = inflater.inflate(R.layout.number_input_dialpad, null);
				break;

		}
		if (this.i == AUTO_LAYOUT) {
			((WatchViewStub) layout.findViewById(R.id.watch_view_stub))
					.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
						@Override
						public void onLayoutInflated(WatchViewStub watchViewStub) {
							setClickListeners(watchViewStub);
						}
					});
		} else {
			setClickListeners(layout);
		}
		return layout;
	}

	private void setClickListeners(View v) {
		int[] buttons = {R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4,
				R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8,
				R.id.button_9, R.id.button_ok, R.id.button_del};
		for (int i : buttons) {
			(v.findViewById(i)).setOnClickListener(this);
		}
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_0:
				this.value = this.value.multiply(BigInteger.valueOf(10));
				this.showValue();
				break;
			case R.id.button_1:
				this.value = this.value.multiply(BigInteger.valueOf(10));
				this.value = this.value.add(BigInteger.valueOf(1));
				this.showValue();
				break;
			case R.id.button_2:
				this.value = this.value.multiply(BigInteger.valueOf(10));
				this.value = this.value.add(BigInteger.valueOf(2));
				this.showValue();
				break;
			case R.id.button_3:
				this.value = this.value.multiply(BigInteger.valueOf(10));
				this.value = this.value.add(BigInteger.valueOf(3));
				this.showValue();
				break;
			case R.id.button_4:
				this.value = this.value.multiply(BigInteger.valueOf(10));
				this.value = this.value.add(BigInteger.valueOf(4));
				this.showValue();
				break;
			case R.id.button_5:
				this.value = this.value.multiply(BigInteger.valueOf(10));
				this.value = this.value.add(BigInteger.valueOf(5));
				this.showValue();
				break;
			case R.id.button_6:
				this.value = this.value.multiply(BigInteger.valueOf(10));
				this.value = this.value.add(BigInteger.valueOf(6));
				this.showValue();
				break;
			case R.id.button_7:
				this.value = this.value.multiply(BigInteger.valueOf(10));
				this.value = this.value.add(BigInteger.valueOf(7));
				this.showValue();
				break;
			case R.id.button_8:
				this.value = this.value.multiply(BigInteger.valueOf(10));
				this.value = this.value.add(BigInteger.valueOf(8));
				this.showValue();
				break;
			case R.id.button_9:
				this.value = this.value.multiply(BigInteger.valueOf(10));
				this.value = this.value.add(BigInteger.valueOf(9));
				this.showValue();
				break;
			case R.id.button_ok:
				break;
			case R.id.button_del:
				this.value = this.value.divide(BigInteger.valueOf(10));
				this.showValue();
				break;
		}
	}

	private void showValue() {
		TextView valueView = (TextView) layout.findViewById(R.id.value_view);
		valueView.setText(value.toString());
	}
}
