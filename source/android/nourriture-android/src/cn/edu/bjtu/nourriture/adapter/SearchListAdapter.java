package cn.edu.bjtu.nourriture.adapter;

import java.util.zip.Inflater;

import cn.edu.bjtu.nourriture.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class SearchListAdapter extends BaseAdapter {
	private Context context;
	
	public SearchListAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.item_search_list, null);
	}
}
