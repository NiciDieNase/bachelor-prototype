package de.inovex.fbuerkle.datamodel.Questions;

import com.activeandroid.annotation.Table;
import com.google.android.gms.wearable.DataMap;

import de.inovex.fbuerkle.DataKeys;
import de.inovex.fbuerkle.datamodel.Checklist;

/**
 * Created by felix on 15/12/14.
 */
@Table(name = "CheckItems")
public class CheckItem extends ChecklistItem {

	public CheckItem() {
	}

	public CheckItem(String title) {
		this(title,"", null);
	}

	public CheckItem(String title, String description){
		this(title, description, null);
	}

	public CheckItem(Checklist checklist) {
		this("", "", checklist);
	}

	public CheckItem(String title, String description, Checklist list) {
		this.title = title;
		this.description = description;
		this.checklist = list;
	}


	public CheckItem(DataMap map){
		super(map);
	}

	@Override
	public DataMap putToDataMap(DataMap map) {
		super.putToDataMap(map);
		map.putString(DataKeys.TYPE,"check");
		return map;
	}
}
