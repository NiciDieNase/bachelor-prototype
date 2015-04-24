package de.inovex.fbuerkle.datamodel;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.android.gms.wearable.DataMap;

import java.util.ArrayList;
import java.util.List;

import de.inovex.fbuerkle.DataKeys;
import de.inovex.fbuerkle.datamodel.Questions.CheckItem;
import de.inovex.fbuerkle.datamodel.Questions.ChecklistItem;
import de.inovex.fbuerkle.datamodel.Questions.DecisionItem;
import de.inovex.fbuerkle.datamodel.Questions.SelectionItem;


/**
 * Created by felix on 15/12/14.
 */
@Table(name = "Checklists")
public class Checklist extends Model{
	@Column(name = "Name", index = true, unique = true)
	public String name;

	@Column(name = "Description")
	public String description;

	public Checklist() {
	}

	public Checklist(DataMap map) {
		this.name = map.getString(DataKeys.NAME);
		this.description = map.getString(DataKeys.DESCRIPTION);
	}

	public List<ChecklistItem> items() {
		Class[] questionClasses = {CheckItem.class, DecisionItem.class, SelectionItem.class};

		List<ChecklistItem> checklist = new ArrayList<ChecklistItem>();
		for (Class c : questionClasses) {
			checklist.addAll(getMany(c, "Checklist"));
		}
		return checklist;
	}

	public DataMap putToDataMap(DataMap map){
		map.putString(DataKeys.NAME,name);
		map.putString(DataKeys.DESCRIPTION,description);
		map.putInt(DataKeys.LENGTH, this.items().size());
		return map;
	}

	public static List<Checklist> getAll(){
		return new Select()
				.from(Checklist.class)
				.orderBy("Name ASC")
				.execute();
	}

}
