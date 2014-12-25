package cn.edu.bjtu.nourriture.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.SearchFoodListActivity.ViewHolder;

import com.lidroid.xutils.util.LogUtils;

public class RmdFoodListFragment extends Fragment {
	private IndexActivity context;
	private ListView lv_rmd_food_list;
	private FoodListAdapter adapter;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = (IndexActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils.d("onCreateView");
		View view = inflater.inflate(R.layout.fragment_rmd_food, null);
		findViews(view);
		setListeners();
		initView();
		return view;
	}
	
	private void findViews(View root){
		lv_rmd_food_list = (ListView) root.findViewById(R.id.listview_rmd_food_list);
	}
	
	private void setListeners(){
		lv_rmd_food_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
			
		});
	}
	
	private void initView(){
		adapter = new FoodListAdapter(new ArrayList<JSONObject>());
		lv_rmd_food_list.setAdapter(adapter);
	}
	
	private class FoodListAdapter extends BaseAdapter{
		private List<JSONObject> data;
		
		public FoodListAdapter(List<JSONObject> data) {
			super();
			this.data = data;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("null")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			
			//组装数据
			if(convertView==null){
				convertView=layoutInflater.inflate(R.layout.item_food_list, null);
				//使用tag存储数据
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			return convertView;
		}
		
	}

}
