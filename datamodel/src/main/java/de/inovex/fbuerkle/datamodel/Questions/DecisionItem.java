package de.inovex.fbuerkle.datamodel.Questions;

import android.os.Parcel;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

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

	public DecisionItem(Parcel p) {
		this.title = p.readString();
		this.description = p.readString();
		this.redOption = p.readString();
		this.greenOption = p.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.title);
		dest.writeString(this.description);
		dest.writeString(this.redOption);
		dest.writeString(this.greenOption);
	}

	public DecisionItem(String title, String greenOption, String redOption) {
		this.title = title;
		this.greenOption = greenOption;
		this.redOption = redOption;
	}

	public static final Creator<DecisionItem> CREATOR = new Creator<DecisionItem>() {
		@Override
		public DecisionItem createFromParcel(Parcel source) {
			return new DecisionItem(source);
		}

		@Override
		public DecisionItem[] newArray(int size) {
			return new DecisionItem[size];
		}
	};
}
