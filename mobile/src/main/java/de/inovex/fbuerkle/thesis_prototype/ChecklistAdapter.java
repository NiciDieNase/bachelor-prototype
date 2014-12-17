package de.inovex.fbuerkle.thesis_prototype;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import de.inovex.fbuerkle.thesis_prototype.model.CheckItem;
import de.inovex.fbuerkle.thesis_prototype.model.Checklist;
import de.inovex.fbuerkle.thesis_prototype.model.ChecklistItem;
import de.inovex.fbuerkle.thesis_prototype.model.DecisionItem;


/**
 * Created by felix on 17/12/14.
 */
public class ChecklistAdapter extends BaseAdapter {

	private Checklist checklist;
	private Context mContext;

	static class ViewHolder{
		TextView titelView;
		TextView typeView;
	}

	public ChecklistAdapter(Context mContext, Checklist checklist){
		this.mContext = mContext;
		this.checklist = checklist;
	}
	@Override
	public int getCount() {
		return checklist.items.size();
	}

	@Override
	public Object getItem(int position) {
		return checklist.items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if(null == convertView){
			LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_item,parent,false);
			viewHolder = new ViewHolder();
			viewHolder.titelView = (TextView) convertView.findViewById(R.id.tv_title);
			viewHolder.typeView = (TextView) convertView.findViewById(R.id.tv_type);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String type = "";
		ChecklistItem item = checklist.items.get(position);
		if(item.getClass().equals(DecisionItem.class)){
			type = "Descision";
		} else if(item.getClass().equals(CheckItem.class)){
			type = "Check";
		}

		viewHolder.titelView.setText(item.title);
		viewHolder.typeView.setText(type);
		return convertView;
	}
}
