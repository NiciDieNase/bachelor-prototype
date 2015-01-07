package de.inovex.fbuerkle.datamodel.Questions;

import com.activeandroid.annotation.Table;

import de.inovex.fbuerkle.datamodel.Checklist;

/**
 * Created by felix on 15/12/14.
 */
@Table(name = "CheckItems")
public class CheckItem extends ChecklistItem {

	public CheckItem(){}

	public CheckItem(String title, Checklist checklist) {
		this.title = title;
		this.checklist = checklist;
	}
}