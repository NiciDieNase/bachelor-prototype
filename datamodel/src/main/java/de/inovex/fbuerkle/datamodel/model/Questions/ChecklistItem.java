package de.inovex.fbuerkle.datamodel.model.Questions;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

import de.inovex.fbuerkle.datamodel.model.Answers.AnswerItem;
import de.inovex.fbuerkle.datamodel.model.Checklist;

/**
 * Created by felix on 15/12/14.
 */
@Table(name = "ChecklistItems")
public class ChecklistItem extends Model implements Comparable {
	@Column (name = "Title")
	public String title;

	@Column(name = "Checklist")
	public Checklist checklist;

	@Column(name = "Position")
	public int position;

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
	public int compareTo(Object another) {
		ChecklistItem other = (ChecklistItem) another;
		return this.position - other.position;
	}
}
