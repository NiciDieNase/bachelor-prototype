package de.inovex.fbuerkle.thesis_prototype.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

import java.util.List;

/**
 * Created by felix on 15/12/14.
 */

abstract public class ChecklistItem extends Model {
	@Column
	public ChecklistItem nextItem;
	@Column
	public List<AnswerItem> answers;
}