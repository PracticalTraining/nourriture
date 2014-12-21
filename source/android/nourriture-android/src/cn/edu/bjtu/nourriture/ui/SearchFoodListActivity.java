package cn.edu.bjtu.nourriture.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;


public class SearchFoodListActivity extends BaseActivity {

	private ListView pl_refresh;
	private LayoutInflater layoutInflater;
	private List<SearchFoodListActivity.Food> data;
	private FoodListAdapter adapter;
	private int startIndex;
	private int endIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search_food_list);
		
		data = new ArrayList<SearchFoodListActivity.Food>();
		Intent intent = getIntent();
		String jsonStr = intent.getStringExtra("foods");
		try {
			JSONArray jFoods = new JSONArray(jsonStr);
			for(int i = 0;i < jFoods.length();i++){
				JSONObject jFood = jFoods.getJSONObject(i);
				SearchFoodListActivity.Food food = new SearchFoodListActivity.Food();
				food.url = jFood.getString("picture");
				food.name = jFood.getString("name");
				food.price = jFood.getDouble("price");
				food.flavourId = jFood.getInt("flavourId");
				data.add(food);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		findViewById();
		initView();
		setListeners();
		
	}

	@Override
	protected void findViewById() {
		pl_refresh=(ListView) this.findViewById(R.id.pl_refresh);
	}

	@Override
	protected void initView() {
		adapter = new FoodListAdapter();
		pl_refresh.setAdapter(adapter);
	}
	
	private void setListeners(){
		pl_refresh.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int parent,
					long id) {
				startActivity(new Intent(SearchFoodListActivity.this,FoodActivity.class));
			}
		});
		pl_refresh.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				LogUtils.d("onScrollStateChanged");
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
					pageLoad(startIndex,endIndex);
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				LogUtils.d("onScroll");
				startIndex = firstVisibleItem;  
	            endIndex = firstVisibleItem + visibleItemCount;  
	            if (startIndex >= totalItemCount) {  
	            	endIndex = totalItemCount - 1;  
	            }  
			}
		});
		pl_refresh.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				pageLoad(pl_refresh.getFirstVisiblePosition(), pl_refresh.getLastVisiblePosition() + 1);
			}
		});
	}
	
	private void pageLoad(int startIndex,int endIndex){
		for(int i = startIndex;i < endIndex;i++){
			final Food food = data.get(i);
			int flavourId = data.get(i).flavourId;
			String flavour = data.get(i).flavour;
			if(!TextUtils.isEmpty(flavour)){
				return;
			}
			HttpUtils httpUtils = new HttpUtils();
			String url = Constants.MOBILE_SERVER_URL + "flavour/" + flavourId;
			httpUtils.send(HttpMethod.GET, url , new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					try {
						JSONObject jFlavour = new JSONObject(arg0.result).getJSONObject("superiorFlavour");
						String name = jFlavour.getString("name");
						food.flavour = name;
						adapter.notifyDataSetChanged();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	private class FoodListAdapter extends BaseAdapter{
		
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

		@SuppressWarnings("null")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder=new ViewHolder();
			layoutInflater=LayoutInflater.from(SearchFoodListActivity.this);
			Food food = data.get(position);
			
			//组装数据
			if(convertView==null){
				convertView=layoutInflater.inflate(R.layout.item_food_list, null);
				holder.icon = (ImageView) convertView.findViewById(R.id.imageview_icon);
				holder.name = (TextView) convertView.findViewById(R.id.textview_name);
				holder.price = (TextView) convertView.findViewById(R.id.textview_price);
				holder.flavour = (TextView) convertView.findViewById(R.id.textview_flavour);
				//使用tag存储数据
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			
			holder.name.setText(food.name);
			holder.price.setText(String.valueOf(food.price));
			BitmapUtils bitmapUtils = new BitmapUtils(SearchFoodListActivity.this);
			LogUtils.d(food.url);
			bitmapUtils.display(holder.icon, food.url);
			if(!TextUtils.isEmpty(food.flavour)){
				holder.flavour.setText(food.flavour);
			}
			
			return convertView;
		}
		
	}
			

	public static class ViewHolder {
		ImageView icon;
		TextView name;
		TextView price;
		TextView flavour;
	}
	
	public static class Food{
		String url;
		String name;
		double price;
		int flavourId;
		String flavour;
	}
}
