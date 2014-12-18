package de.inovex.fbuerkle.thesis_prototype.model.Answers;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

import de.inovex.fbuerkle.thesis_prototype.model.Questions.ChecklistItem;

/**
 * Created by felix on 15/12/14.
 */
@Table(name = "AnswerItems")
public class AnswerItem extends Model{
	@Column(name = "Question")
	public ChecklistItem question;
	@Column
	public int sessionID;
	@Column
	public Date timestamp;

	public void setCurrentTime(){
		this.timestamp = new Date();
	}
}
