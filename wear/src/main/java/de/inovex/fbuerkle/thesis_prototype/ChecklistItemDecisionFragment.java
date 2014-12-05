package de.inovex.fbuerkle.thesis_prototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by felix on 04/12/14.
 */
public class ChecklistItemDecisionFragment extends ChecklistFragment implements View.OnClickListener{

	private String text;

	public ChecklistItemDecisionFragment(){
		this("");
	}

	public ChecklistItemDecisionFragment(String text){
		this.text = text;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.decision_item_layout, container, false);
		TextView decisionText = (TextView) layout.findViewById(R.id.text_decision);
		decisionText.setText(this.text);
		layout.findViewById(R.id.button_ok).setOnClickListener(this);
		layout.findViewById(R.id.button_cancel).setOnClickListener(this);
		return layout;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.button_ok:
				this.mListener.onOK();
				break;
			case R.id.button_cancel:
				this.mListener.onCancel();
				break;
		}
	}

	public void setText(String text){
		this.text = text;
	}
}
