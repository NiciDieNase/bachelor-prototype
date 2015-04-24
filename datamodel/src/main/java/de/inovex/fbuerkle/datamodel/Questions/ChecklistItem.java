package de.inovex.fbuerkle.datamodel.Questions;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.android.gms.wearable.DataMap;

import java.util.List;

import de.inovex.fbuerkle.DataKeys;
import de.inovex.fbuerkle.datamodel.Answers.AnswerItem;
import de.inovex.fbuerkle.datamodel.Checklist;

/**
 * Created by felix on 15/12/14.
 */
@Table(name = "ChecklistItems")
public abstract class ChecklistItem extends Model{
	@Column(name = "Title")
	public String title;

	@Column(name = "Checklist")
	public Checklist checklist;

	@Column(name = "Description")
	public String description;

	public ChecklistItem() {
	}

	public ChecklistItem(String title) {
		this(title,"", null);
	}

	public ChecklistItem(String title, String description){
		this(title, description, null);
	}

	public ChecklistItem(Checklist checklist) {
		this("", "", checklist);
	}

	public ChecklistItem(DataMap map){
		this(map.getString(DataKeys.title), map.getString(DataKeys.description), null);
	}

	public ChecklistItem(String title, String description, Checklist list) {
		this.title = title;
		this.description = description;
		this.checklist = list;
	}

	@Override
	public String toString() {
		return this.title;
	}

	public List<AnswerItem> answers() {
		return getMany(AnswerItem.class, "Question");
	}

	public DataMap putToDataMap(DataMap map){
		map.putString(DataKeys.title,title);
		map.putString(DataKeys.description,description);
		map.putString(DataKeys.checklist,checklist.name);
		return map;
	}
}
