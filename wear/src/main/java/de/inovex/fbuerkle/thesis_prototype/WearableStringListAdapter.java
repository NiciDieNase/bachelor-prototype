package de.inovex.fbuerkle.thesis_prototype;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
* Created by felix on 28/01/15.
*/
class WearableStringListAdapter extends WearableListView.Adapter {
	private final LayoutInflater mInflater;

	private Context mContext;

	private String emptyListString = "loading items";
	private List<String> strings;

	public WearableStringListAdapter(Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
	}

	public WearableStringListAdapter(Context context, List<String> checklists) {
		this(context);
		this.strings = checklists;
	}

	public class MyViewHolder extends WearableListView.ViewHolder {
		private TextView textView;
		private ImageView imageView;

		public MyViewHolder(View itemView) {
			super(itemView);
			textView = (TextView) itemView.findViewById(R.id.item_text);
			imageView = (ImageView) itemView.findViewById(R.id.marker);
		}

	}

	@Override
	public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		return new MyViewHolder(mInflater.inflate(R.layout.list_item, null));
	}

	@Override
	public void onBindViewHolder(WearableListView.ViewHolder viewHolder, int position) {
		MyViewHolder holder = (MyViewHolder) viewHolder;
		if (null != strings) {
			holder.textView.setText(strings.get(position));
		} else {
			holder.textView.setText(emptyListString);
		}
		holder.itemView.setTag(position);
	}

	@Override
	public int getItemCount() {
		return null != strings ? strings.size() : 1;
	}

	public void updateStrings(List<String> checklists) {
		this.strings = checklists;
		this.notifyDataSetChanged();
	}

	public void setEmptyListString(String emptyListString) {
		this.emptyListString = emptyListString;
	}
}
