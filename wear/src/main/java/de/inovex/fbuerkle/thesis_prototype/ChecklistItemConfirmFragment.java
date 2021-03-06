package de.inovex.fbuerkle.thesis_prototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.inovex.fbuerkle.datamodel.Questions.CheckItem;

/**
 * Created by felix on 04/12/14.
 */
public class ChecklistItemConfirmFragment extends ChecklistFragment implements View.OnClickListener {

	private String text;

	public ChecklistItemConfirmFragment() {
		this("");
	}

	public ChecklistItemConfirmFragment(String text) {
		this.text = text;
	}

	public ChecklistItemConfirmFragment(CheckItem item){
		this.text = item.title;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View layout = inflater.inflate(R.layout.check_item_layout_2, container, false);

		TextView confirmText = (TextView) layout.findViewById(R.id.text_decision);
		confirmText.setText(text);
		layout.findViewById(R.id.button_confirm).setOnClickListener(ChecklistItemConfirmFragment.this);

//		final WatchViewStub stub = (WatchViewStub) layout.findViewById(R.id.watch_view_stub);
//		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//			@Override
//			public void onLayoutInflated(WatchViewStub watchViewStub) {
//				TextView confirmText = (TextView) layout.findViewById(R.id.text_decision);
//				confirmText.setText(text);
//				layout.findViewById(R.id.button_confirm).setOnClickListener(ChecklistItemConfirmFragment.this);
//			}
//		});
		return layout;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_confirm:
				this.mListener.onConfirm();
				break;
		}
	}

	public void setText(String text) {
		this.text = text;
	}
}
