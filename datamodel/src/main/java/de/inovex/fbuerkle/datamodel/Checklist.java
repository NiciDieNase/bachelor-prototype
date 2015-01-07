package de.inovex.fbuerkle.datamodel;

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
public class Checklist extends Model {
	@Column(name = "Name", index = true, unique = true)
	public String name;

	@Column(name = "Description")
	public String description;

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
}
