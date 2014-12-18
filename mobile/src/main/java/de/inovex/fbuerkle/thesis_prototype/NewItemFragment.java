package de.inovex.fbuerkle.thesis_prototype;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.activeandroid.ActiveAndroid;

import de.inovex.fbuerkle.datamodel.model.Questions.CheckItem;
import de.inovex.fbuerkle.datamodel.model.Checklist;
import de.inovex.fbuerkle.datamodel.model.Questions.ChecklistItem;
import de.inovex.fbuerkle.datamodel.model.Questions.DecisionItem;

/**
 * Created by felix on 17/12/14.
 */
public class NewItemFragment extends DialogFragment {

	ItemTypes type = ItemTypes.Check;

	public NewItemFragment(ItemTypes type){
		this.type = type;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View layout = null;
		switch (this.type){
			case Check:
				layout = inflater.inflate(R.layout.create_checkitem, null);
				break;
			case Descision:
				layout = inflater.inflate(R.layout.create_decisionitem,null);
			case ValueSelect:
				// TODO implement new Value-Select
				break;
			case ItemSelect:
				// TODO implement new Item-Select
				break;
		}
		final View finalLayout = layout;
		builder.setView(finalLayout);

		builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				if(null != finalLayout){
					Checklist currentChecklist =((MainActivity) getActivity()).currentChecklist;
					ChecklistItem item = null;
					String title = "";
					switch (NewItemFragment.this.type){
						case Check:
							title = ((EditText) finalLayout.findViewById(R.id.editText_title)).getText().toString();
							item = new CheckItem();
							break;
						case Descision:
							String greenText, redText;
							title = ((EditText) finalLayout.findViewById(R.id.editText_title)).getText().toString();
							greenText = ((EditText) finalLayout.findViewById(R.id.editText_green)).getText().toString();
							redText =((EditText) finalLayout.findViewById(R.id.editText_red)).getText().toString();
							item = new DecisionItem(title, greenText, redText);
							break;
					}
					if(null != item){
						ActiveAndroid.beginTransaction();
						try{
							item.title = title;
							item.checklist = currentChecklist;
							item.save();
							ActiveAndroid.setTransactionSuccessful();
						} finally {
							ActiveAndroid.endTransaction();
						}
					}
				}
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

			}
		});

	return builder.create();
	}
}
