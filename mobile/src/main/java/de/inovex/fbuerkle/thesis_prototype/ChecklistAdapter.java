package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import de.inovex.fbuerkle.datamodel.Checklist;
import de.inovex.fbuerkle.datamodel.Questions.CheckItem;
import de.inovex.fbuerkle.datamodel.Questions.ChecklistItem;
import de.inovex.fbuerkle.datamodel.Questions.DecisionItem;


/**
 * Created by felix on 17/12/14.
 */
public class ChecklistAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

	private Checklist checklist;
	private Context mContext;

	static class ViewHolder {

		TextView titelView;
		TextView typeView;
		TextView optionsView;
	}

	public ChecklistAdapter(Context mContext, Checklist checklist) {
		this.mContext = mContext;
		this.checklist = checklist;
	}

	@Override
	public int getCount() {
		return checklist.items().size();
	}

	@Override
	public Object getItem(int position) {
		return checklist.items().get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (null == convertView) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.titelView = (TextView) convertView.findViewById(R.id.tv_title);
			viewHolder.typeView = (TextView) convertView.findViewById(R.id.tv_type);
			viewHolder.optionsView = (TextView) convertView.findViewById(R.id.tv_options);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String type = "";
		String options = "";
		ChecklistItem item = checklist.items().get(position);
		if (item.getClass().equals(DecisionItem.class)) {
			type = "Decision";
			options = ((DecisionItem) item).greenOption + " / " + ((DecisionItem) item).redOption;
		} else if (item.getClass().equals(CheckItem.class)) {
			type = "Check";
		}

		viewHolder.titelView.setText(item.title);
		viewHolder.typeView.setText(type);
		viewHolder.optionsView.setText(options);
		return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage(mContext.getString(R.string.do_you_want_to_delete_this_item))
				.setTitle(mContext.getString(R.string.confirm_delete))
				.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ChecklistItem item = checklist.items().get(position);
						checklist.items().remove(position);
						item.checklist.length--;
						item.checklist.save();
						item.delete();
						ChecklistAdapter.this.notifyDataSetChanged();
						// TODO delete DataItem
					}
				})
				.setNegativeButton("Cancel", null);
		builder.show();
	}
}
