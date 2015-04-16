package de.inovex.fbuerkle.datamodel.Questions;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.android.gms.wearable.DataMap;

import java.util.List;

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
		this(title, null);
	}

	public ChecklistItem(Checklist checklist) {
		this("", checklist);
	}

	public ChecklistItem(String title, Checklist list) {
		this.title = title;
		this.checklist = list;
	}

	public ChecklistItem(DataMap map){
		this.title = map.getString("title");
		this.description = map.getString("description");
	}

	@Override
	public String toString() {
		return this.title;
	}

	public List<AnswerItem> answers() {
		return getMany(AnswerItem.class, "Question");
	}

	public DataMap putToDataMap(DataMap map){
		map.putString("title",title);
		map.putString("description",description);
		return map;
	}
}
