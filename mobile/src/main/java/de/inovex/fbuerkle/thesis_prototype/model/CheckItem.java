package de.inovex.fbuerkle.thesis_prototype.model;

import com.activeandroid.annotation.Table;

/**
 * Created by felix on 15/12/14.
 */
@Table(name = "CheckItems")
public class CheckItem extends ChecklistItem {

	public CheckItem(String title) {
		this.title = title;
	}
}
