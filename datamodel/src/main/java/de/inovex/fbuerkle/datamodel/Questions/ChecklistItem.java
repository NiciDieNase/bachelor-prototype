package de.inovex.fbuerkle.datamodel.Questions;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

import de.inovex.fbuerkle.datamodel.Answers.AnswerItem;
import de.inovex.fbuerkle.datamodel.Checklist;

/**
 * Created by felix on 15/12/14.
 */
@Table(name = "ChecklistItems")
public abstract class ChecklistItem extends Model implements Parcelable {
	@Column (name = "Title")
	public String title;

	@Column(name = "Checklist")
	public Checklist checklist;

	@Column(name = "Description")
	public String description;

	public ChecklistItem(){}

	public ChecklistItem(String title){
		this(title,null);
	}

	public ChecklistItem(Checklist checklist){
		this("",checklist);
	}

	public ChecklistItem(String title,Checklist list){
		this.title = title;
		this.checklist = list;
	}

	@Override
	public String toString(){
		return this.title;
	}

	public List<AnswerItem> answers (){
		return getMany(AnswerItem.class, "Question");
	}


	@Override
	public abstract int describeContents();

	@Override
	public abstract void writeToParcel(Parcel dest, int flags);
}
