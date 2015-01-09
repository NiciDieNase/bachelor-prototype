package de.inovex.fbuerkle.datamodel.Questions;

import android.os.Parcel;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by felix on 16.12.14.
 */
@Table(name = "SelectionItems")
public class SelectionItem extends ChecklistItem{
    @Column
    public String valueName;

	public SelectionItem(){}

	public SelectionItem(Parcel parcel){
		this.title = parcel.readString();
		this.description = parcel.readString();
		this.valueName = parcel.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.title);
		dest.writeString(this.description);
		dest.writeString(this.valueName);
	}

	public static final Creator<SelectionItem> CREATOR = new Creator<SelectionItem>() {
		@Override
		public SelectionItem createFromParcel(Parcel source) {
			return new SelectionItem(source);
		}

		@Override
		public SelectionItem[] newArray(int size) {
			return new SelectionItem[size];
		}
	};
}
