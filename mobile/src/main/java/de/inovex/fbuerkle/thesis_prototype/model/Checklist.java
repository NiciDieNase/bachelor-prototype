package de.inovex.fbuerkle.thesis_prototype.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.util.Collections;
import java.util.List;

import de.inovex.fbuerkle.thesis_prototype.model.Questions.ChecklistItem;

/**
 * Created by felix on 15/12/14.
 */
public class Checklist extends Model {
	@Column(name = "Name")
	public String name;

	public List<ChecklistItem> items (){
		List<ChecklistItem> checklist = getMany(ChecklistItem.class, "Checklist");
		Collections.sort(checklist);
		return checklist;
	}
}
