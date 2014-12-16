package de.inovex.fbuerkle.thesis_prototype.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felix on 15/12/14.
 */
public class Checklist extends Model {
	@Column
	public List<ChecklistItem> Items;

    public Checklist(){
        Items = new ArrayList<ChecklistItem>();
    }

    public ChecklistItem getFirstItem(){
        return Items.get(0);
    }
}
