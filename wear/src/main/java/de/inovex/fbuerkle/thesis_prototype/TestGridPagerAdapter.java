package de.inovex.fbuerkle.thesis_prototype;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;

/**
 * Created by felix on 05/12/14.
 */
public class TestGridPagerAdapter extends FragmentGridPagerAdapter {

	private final Context mContext;

	public TestGridPagerAdapter(FragmentManager fm, Context mContext) {
		super(fm);
		this.mContext = mContext;
	}

	@Override
	public Fragment getFragment(int row, int col) {
		if(row != 0){
			return null;
		}
		switch (col){
			case 0:
				return new ChecklistItemConfirmFragment("Please Confirm");
			case 1:
				return new ChecklistItemDecisionFragment("Everything okay?");
		}
	return null;
	}

	@Override
	public int getRowCount() {
		return 1;
	}

	@Override
	public int getColumnCount(int i) {
		return 2;
	}
}
