package de.inovex.fbuerkle.thesis_prototype.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felix on 15/12/14.
 */
public class Checklist extends Model {
	@Column(name = "items")
	public List<ChecklistItem> items;

	@Column(name = "Name")
	public String name;

    public Checklist(){
        items = new ArrayList<ChecklistItem>();
    }

    public ChecklistItem getFirstItem(){
        return items.get(0);
    }

	public void addCheck (ChecklistItem item){
		this.items.add(item);
	}
}
