package de.inovex.fbuerkle.datamodel.Questions;

import android.os.Parcel;

import com.activeandroid.annotation.Table;

import de.inovex.fbuerkle.datamodel.Checklist;

/**
 * Created by felix on 15/12/14.
 */
@Table(name = "CheckItems")
public class CheckItem extends ChecklistItem {

	public CheckItem() {
	}

	public CheckItem(String title, Checklist checklist) {
		this.title = title;
		this.checklist = checklist;
	}

	public CheckItem(Parcel p) {
		this.title = p.readString();
		this.description = p.readString();
	}
}
