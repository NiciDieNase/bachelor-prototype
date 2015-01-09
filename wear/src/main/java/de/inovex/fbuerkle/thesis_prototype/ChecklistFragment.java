package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.app.Fragment;

/**
 * Created by felix on 05/12/14.
 */
public class ChecklistFragment extends Fragment {

	OnChecklistItemResultListener mListener;

	public interface OnChecklistItemResultListener {
		public void onConfirm();

		public void onOK();

		public void onCancel();

		public void onValueSelected();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnChecklistItemResultListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnChecklistItemResultListener.");
		}
	}
}
