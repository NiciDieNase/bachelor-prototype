package de.inovex.fbuerkle.thesis_prototype;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import de.inovex.fbuerkle.datamodel.Checklist;

/**
 * Created by felix on 07/01/15.
 */
public class NewChecklistFragment extends DialogFragment {

	private static final String TAG = "NewChecklistFragment";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		final View layout = inflater.inflate(R.layout.new_checklist, null);
		builder.setView(layout);

		builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String name = ((EditText) layout.findViewById(R.id.editText_name)).getText().toString();
				String description = ((EditText) layout.findViewById(R.id.editText_description)).getText().toString();

				Checklist checklist = new Checklist();
				checklist.name = name;
				checklist.description = description;
				checklist.save();
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d(TAG, "Canceled creation of new checklist");
			}
		});
		return builder.create();
	}
}
