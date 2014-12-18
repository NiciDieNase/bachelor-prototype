package de.inovex.fbuerkle.datamodel.model.Answers;

import com.activeandroid.annotation.Column;

/**
 * Created by felix on 18/12/14.
 */
public class DecisionAnswerItem extends AnswerItem {
	@Column(name = "Answer")
	public String answer;
}
