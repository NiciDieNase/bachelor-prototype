package de.inovex.fbuerkle.thesis_prototype.model;

import com.activeandroid.annotation.Column;

/**
 * Created by felix on 16.12.14.
 */
public class DecisionItem extends ChecklistItem {

    @Column
    public String greenOption;
    @Column
    public String redOption;

	public DecisionItem(){}

	public DecisionItem(String title, String greenOption, String redOption){
		this.title = title;
		this.greenOption = greenOption;
		this.redOption = redOption;
	}
}
