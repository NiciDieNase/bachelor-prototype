package de.inovex.fbuerkle.thesis_prototype.model;

import android.support.annotation.ColorRes;

import com.activeandroid.annotation.Column;

/**
 * Created by felix on 16.12.14.
 */
public class DecisionItem extends ChecklistItem {
    @Column
    public String question;
    @Column
    public String yesOption;
    @Column
    public String noOption;
}
