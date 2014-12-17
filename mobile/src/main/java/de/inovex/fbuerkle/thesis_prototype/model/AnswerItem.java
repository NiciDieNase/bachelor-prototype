package de.inovex.fbuerkle.thesis_prototype.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by felix on 15/12/14.
 */
@Table(name = "AnswerItems")
public class AnswerItem extends Model{
	@Column(name = "Question")
	ChecklistItem question;
	@Column
	int sessionID;
	@Column
	String answer;
	@Column
	Date timestamp;
}
