package de.inovex.fbuerkle.datamodel.Questions;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.android.gms.wearable.DataMap;

/**
 * Created by felix on 16.12.14.
 */
@Table(name = "DecisionItems")
public class DecisionItem extends ChecklistItem {

	@Column
	public String greenOption;
	@Column
	public String redOption;

	public DecisionItem() {
	}

	public DecisionItem(String title) {
		this(title, "Yes", "No");
	}

	public DecisionItem(String title, String yes, String no) {
		this.title = title;
		this.greenOption = yes;
		this.redOption = no;
	}

	public DecisionItem(DataMap map){
		super(map);
		this.greenOption = map.getString("greenOption");
		this.redOption = map.getString("redOption");
	}

	@Override
	public DataMap putToDataMap(DataMap map) {
		map = super.putToDataMap(map);
		map.putString("redOption",redOption);
		map.putString("greenOption",greenOption);
		return map;
	}
}
