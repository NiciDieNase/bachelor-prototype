package de.inovex.fbuerkle.datamodel.Questions;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.android.gms.wearable.DataMap;

import de.inovex.fbuerkle.DataKeys;

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
		this.greenOption = map.getString(DataKeys.GREEN_TEXT);
		this.redOption = map.getString(DataKeys.RED_TEXT);
	}

	@Override
	public DataMap putToDataMap(DataMap map) {
		map = super.putToDataMap(map);
		map.putString(DataKeys.TYPE,DataKeys.TYPE_DECISION);
		map.putString(DataKeys.RED_TEXT,redOption);
		map.putString(DataKeys.GREEN_TEXT,greenOption);
		return map;
	}
}
