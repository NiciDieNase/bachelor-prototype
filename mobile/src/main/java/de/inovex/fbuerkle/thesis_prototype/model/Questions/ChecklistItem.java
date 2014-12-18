package de.inovex.fbuerkle.thesis_prototype.model.Questions;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.util.List;

import de.inovex.fbuerkle.thesis_prototype.model.Answers.AnswerItem;
import de.inovex.fbuerkle.thesis_prototype.model.Checklist;

/**
 * Created by felix on 15/12/14.
 */

abstract public class ChecklistItem extends Model implements Comparable {
	@Column (name = "Title")
	public String title;

	@Column(name = "Checklist")
	public Checklist checklist;

	@Column(name = "Index")
	public int index;

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
		return this.index - other.index;
	}
}
