package de.inovex.fbuerkle.datamodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.inovex.fbuerkle.datamodel.Questions.CheckItem;
import de.inovex.fbuerkle.datamodel.Questions.ChecklistItem;
import de.inovex.fbuerkle.datamodel.Questions.DecisionItem;
import de.inovex.fbuerkle.datamodel.Questions.SelectionItem;


/**
 * Created by felix on 15/12/14.
 */
@Table(name = "Checklists")
public class Checklist extends Model implements Parcelable {
	@Column(name = "Name", index = true, unique = true)
	public String name;

	@Column(name = "Description")
	public String description;

	public Checklist(Parcel parcel) {
		this.name = parcel.readString();
		this.description = parcel.readString();
	}

	public List<ChecklistItem> items (){
		Class[] questionClasses = {CheckItem.class, DecisionItem.class, SelectionItem.class};

		List<ChecklistItem> checklist = new ArrayList<ChecklistItem>();
		for(Class c : questionClasses){
			checklist.addAll(getMany(c, "Checklist"));
		}
		Collections.sort(checklist);
		return checklist;
	}

	@Override
	public boolean equals(Object obj) {
		try{
			Checklist comp = (Checklist) obj;
			return comp.name.equals(this.name);
		} catch (ClassCastException e){
			return false;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.description);
	}

	public static final Parcelable.Creator<Checklist> CREATOR =
			new Parcelable.Creator<Checklist>(){

				@Override
				public Checklist createFromParcel(Parcel source) {
					return new Checklist(source);
				}

				@Override
				public Checklist[] newArray(int size) {
					return new Checklist[size];
				}
			};
}
