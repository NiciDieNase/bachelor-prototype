package de.inovex.fbuerkle.thesis_prototype;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.CardFragment;
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
		switch (row) {

			// 1st Row
			case 0:
				switch (col) {
					case 0:
						return new ChecklistItemConfirmFragment("Please Confirm");
					case 1:
						return CardFragment.create("Titel", mContext.getString(R.string.lorem_ipsum));
				}

				// 2nd Row
			case 1:
				switch (col) {
					case 0:
						return new ChecklistItemDecisionFragment("Everything okay?");
				}

				// 3rd Row
			case 2:
				switch (col) {
					case 0:
						return new DialpadInputFragment(0);
					case 1:
						return new DialpadInputFragment(1);
					case 2:
						return new DialpadInputFragment(2);
				}
		}
		return null;
	}

	@Override
	public int getRowCount() {
		return 3;
	}

	@Override
	public int getColumnCount(int i) {
		switch (i) {
			case 0:
				return 2;
			case 1:
				return 1;
			case 2:
				return 3;
			default:
				return 0;
		}
	}
}
