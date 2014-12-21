package cn.edu.bjtu.nourriture.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;

public class SearchListAdapter extends BaseAdapter {
	private Context context;
	private List<String> data;
	
	public SearchListAdapter(Context context,List<String> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_search_list, null);
			holder.his = (TextView) convertView.findViewById(R.id.textview_his);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.his.setText(data.get(position));
		return convertView;
	}
	
	private static class ViewHolder{
		TextView his;
	}
}
