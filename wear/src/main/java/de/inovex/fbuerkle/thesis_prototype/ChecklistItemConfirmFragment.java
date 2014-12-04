package de.inovex.fbuerkle.thesis_prototype;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by felix on 04/12/14.
 */
public class ChecklistItemConfirmFragment extends Fragment{

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		return inflater.inflate(R.layout.check_item_layout,container,false);
		return inflater.inflate(R.layout.check_item_layout_2,container,false);
	}
}
