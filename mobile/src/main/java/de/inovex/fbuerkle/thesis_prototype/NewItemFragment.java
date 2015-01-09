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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import de.inovex.fbuerkle.datamodel.Checklist;
import de.inovex.fbuerkle.datamodel.Questions.CheckItem;
import de.inovex.fbuerkle.datamodel.Questions.ChecklistItem;
import de.inovex.fbuerkle.datamodel.Questions.DecisionItem;

/**
 * Created by felix on 17/12/14.
 */
public class NewItemFragment extends DialogFragment {

	ItemTypes type = ItemTypes.Check;

	public NewItemFragment(ItemTypes type) {
		this.type = type;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View layout = null;
		switch (this.type) {
			case Check:
				layout = inflater.inflate(R.layout.create_checkitem, null);
				break;
			case Decision:
				layout = inflater.inflate(R.layout.create_decisionitem, null);
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
				if (null != finalLayout) {
					Checklist currentChecklist = ((MainActivity) getActivity()).currentChecklist;
					int position = currentChecklist.items().size();

					PutDataMapRequest dataMap = PutDataMapRequest.create("/checklist/" + currentChecklist.name.toLowerCase() + "/" + position);
					dataMap.getDataMap().putInt("position", position);

					ChecklistItem item = null;
					String title = "";
					switch (NewItemFragment.this.type) {
						case Check:
							title = ((EditText) finalLayout.findViewById(R.id.editText_title)).getText().toString();
							item = new CheckItem();

							dataMap.getDataMap().putString("type", "CheckItem");
							break;
						case Decision:
							String greenText, redText;
							title = ((EditText) finalLayout.findViewById(R.id.editText_title)).getText().toString();
							greenText = ((EditText) finalLayout.findViewById(R.id.editText_green)).getText().toString();
							redText = ((EditText) finalLayout.findViewById(R.id.editText_red)).getText().toString();
							item = new DecisionItem(title, greenText, redText);

							dataMap.getDataMap().putString("redText", redText);
							dataMap.getDataMap().putString("greenText", greenText);
							dataMap.getDataMap().putString("type", "DecisionItem");
							break;
						// TODO handle other item-types
					}
					if (null != item) {
						dataMap.getDataMap().putString("title", title);
						ActiveAndroid.beginTransaction();
						try {
							item.title = title;
							item.checklist = currentChecklist;
							item.position = position;
							item.save();
							item.checklist.length++;
							item.checklist.save();
							ActiveAndroid.setTransactionSuccessful();
						} finally {
							ActiveAndroid.endTransaction();

							PutDataRequest request = dataMap.asPutDataRequest();
							PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
									.putDataItem(((MainActivity) ((MainActivity) getActivity())).getmGoogleApiClient(), request);
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
