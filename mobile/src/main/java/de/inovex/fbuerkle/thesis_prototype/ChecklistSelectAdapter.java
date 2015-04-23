package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.util.List;

import de.inovex.fbuerkle.datamodel.Checklist;

/**
 * Created by felix on 23/04/15.
 */
public class ChecklistSelectAdapter extends BaseAdapter {

	private List<Checklist> checklists;
	private final Context mContext;

	static class ViewHolder {
		TextView nameView;
	}

	public ChecklistSelectAdapter(Context context){
		mContext = context;
		checklists =  new Select().from(Checklist.class).execute();
	}

	@Override
	public int getCount() {
		return checklists.size();
	}

	@Override
	public Object getItem(int position) {
		return checklists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return checklists.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (null == convertView) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.drawer_layout, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.nameView = (TextView) convertView.findViewById(R.id.textViewName);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.nameView.setText(checklists.get(position).name);
		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		checklists =  new Select().from(Checklist.class).execute();
		super.notifyDataSetChanged();
	}
}
