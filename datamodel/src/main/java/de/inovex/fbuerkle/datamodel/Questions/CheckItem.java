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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.title);
		dest.writeString(this.description);
	}

	public static final Creator<CheckItem> CREATOR = new Creator<CheckItem>() {
		@Override
		public CheckItem createFromParcel(Parcel source) {
			return new CheckItem(source);
		}

		@Override
		public CheckItem[] newArray(int size) {
			return new CheckItem[size];
		}
	};
}
