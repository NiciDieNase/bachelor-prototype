package de.inovex.fbuerkle.thesis_prototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.wearable.view.WatchViewStub;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.inovex.fbuerkle.datamodel.Questions.DecisionItem;

/**
 * Created by felix on 04/12/14.
 */
public class ChecklistItemDecisionFragment extends ChecklistFragment implements View.OnClickListener {

	String titleText;
	String okayText = "yes";
	String cancelText = "no";

	public ChecklistItemDecisionFragment() {
		this("");
	}

	public ChecklistItemDecisionFragment(String text) {
		this.titleText = text;
	}

	public ChecklistItemDecisionFragment(DecisionItem item) {
		this.titleText = item.title;
		this.okayText = item.greenOption;
		this.cancelText = item.redOption;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View layout = inflater.inflate(R.layout.decision_item_layout, container, false);
		final WatchViewStub stub = (WatchViewStub) layout.findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
			@Override
			public void onLayoutInflated(WatchViewStub watchViewStub) {
				TextView decisionText = (TextView) layout.findViewById(R.id.text_decision);
				decisionText.setText(ChecklistItemDecisionFragment.this.titleText);
				Button okButton = (Button) layout.findViewById(R.id.button_ok);
				okButton.setOnClickListener(ChecklistItemDecisionFragment.this);
				okButton.setText(ChecklistItemDecisionFragment.this.okayText);
				Button cancelButton = (Button) layout.findViewById(R.id.button_cancel);
				cancelButton.setOnClickListener(ChecklistItemDecisionFragment.this);
				cancelButton.setText(ChecklistItemDecisionFragment.this.cancelText);
			}
		});


		return layout;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_ok:
				this.mListener.onOK();
				break;
			case R.id.button_cancel:
				this.mListener.onCancel();
				break;
		}
	}

	public void setTitle(String text) {
		this.titleText = text;
	}
}
