package de.inovex.fbuerkle.datamodel.model.Questions;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by felix on 16.12.14.
 */
@Table(name = "DecisionItems")
public class DecisionItem extends ChecklistItem {

    @Column
    public String greenOption;
    @Column
    public String redOption;

	public DecisionItem(){}

	public DecisionItem(String title){
		this(title,"Yes","No");
	}
	public DecisionItem(String title, String greenOption, String redOption){
		this.title = title;
		this.greenOption = greenOption;
		this.redOption = redOption;
	}
}
