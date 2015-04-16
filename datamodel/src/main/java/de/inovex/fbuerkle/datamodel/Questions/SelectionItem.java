package de.inovex.fbuerkle.datamodel.Questions;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by felix on 16.12.14.
 */
@Table(name = "SelectionItems")
public class SelectionItem extends ChecklistItem {
	@Column
	public String valueName;

	public SelectionItem() {
	}

}
