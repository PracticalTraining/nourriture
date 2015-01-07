package cn.edu.bjtu.nourriture.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.task.EMobileTask;
import cn.edu.bjtu.nourriture.ui.SearchFoodListActivity.ViewHolder;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

public class RmdFoodListFragment extends Fragment {
	private IndexActivity context;
	private PullToRefreshListView lv_rmd_food_list;
	private FoodListAdapter adapter;
	private HttpUtils httpUtils;
	private BitmapUtils bitmapUtils;
	private TextView tv_info;
	private LinearLayout ly_login;
	private int page;
	
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
		httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0L);
		bitmapUtils = new BitmapUtils(context);
		findViews(view);
		setListeners();
		initView();
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(TextUtils.isEmpty(EMobileTask.getCookie("userId"))){
			ly_login.setVisibility(View.VISIBLE);
			tv_info.setText(R.string.activity_rmd_list_no_login);
			lv_rmd_food_list.setVisibility(View.GONE);
		} else if(EMobileTask.getCookie("idendity").equals("manu")){
			ly_login.setVisibility(View.VISIBLE);
			tv_info.setText(R.string.activity_rmd_list_manu_not_support);
			lv_rmd_food_list.setVisibility(View.GONE);
		} else {
			ly_login.setVisibility(View.GONE);
			lv_rmd_food_list.setVisibility(View.VISIBLE);
			lv_rmd_food_list.getRefreshableView().setVisibility(View.VISIBLE);
		}
		
		if(!TextUtils.isEmpty(EMobileTask.getCookie("userId")) && EMobileTask.getCookie("idendity").equals("customer")){
			page = 0;
			new InitDataTask().execute();
		}
	}

	private void findViews(View root){
		lv_rmd_food_list = (PullToRefreshListView) root.findViewById(R.id.listview_rmd_food_list);
		tv_info = (TextView) root.findViewById(R.id.textview_info);
		ly_login = (LinearLayout) root.findViewById(R.id.layout_login);
	}
	
	private void setListeners(){
		lv_rmd_food_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int parent,
					long id) {
				Intent intent = new Intent(context,FoodActivity.class);
				intent.putExtra("food", adapter.getJsonStr((int) id));
				startActivity(intent);
			}
		});
		lv_rmd_food_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				LogUtils.i("onPullDownToRefresh");
				new RefreshDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				LogUtils.i("onPullUpToRefresh");
				new MoreDataTask().execute();
			}

		});
	}
	
	private void initView(){
		adapter = new FoodListAdapter(new ArrayList<JSONObject>());
		lv_rmd_food_list.setAdapter(adapter);
		//设置PullRefreshListView上提加载时的加载提示
		lv_rmd_food_list.setMode(Mode.BOTH);
		lv_rmd_food_list.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_up_load));
		lv_rmd_food_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
		lv_rmd_food_list.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.loosen_load_more));

		// 设置PullRefreshListView下拉加载时的加载提示
		lv_rmd_food_list.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_down_refresh));
		lv_rmd_food_list.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
		lv_rmd_food_list.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.loosen_refreshing));
	}
	
	private List<JSONObject> getData(){
		List<JSONObject> data = new ArrayList<JSONObject>();
		String url = Constants.MOBILE_SERVER_WS_URL + "food/recommend";
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("customerId", EMobileTask.getCookie("userId"));
		params.addQueryStringParameter("page", String.valueOf(page));
		try {
			String foodsStr = httpUtils.sendSync(HttpMethod.GET, url, params).readString();
			JSONArray jFoods = new JSONObject(foodsStr).getJSONArray("foods");
			for(int i = 0;i < jFoods.length();i++){
				JSONObject jFood = jFoods.getJSONObject(i);
				jFood.put("jsonStr", jFood.toString());
				int flavourId = jFood.getInt("flavourId");
				url = Constants.MOBILE_SERVER_WS_URL + "flavour/" + flavourId;
				String flavourStr = httpUtils.sendSync(HttpMethod.GET, url).readString();
				JSONObject jFlavour = new JSONObject(flavourStr).getJSONObject("superiorFlavour");
				String name = jFlavour.getString("name");
				jFood.put("flavour", name);
				data.add(jFood);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			return data;
		}
	}
	
	private class FoodListAdapter extends BaseAdapter{
		private List<JSONObject> data;
		
		public FoodListAdapter(List<JSONObject> data) {
			super();
			this.data = data;
		}

		public void setData(List<JSONObject> data) {
			this.data = data;
		}
		
		public void addData(JSONObject object){
			data.add(object);
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
			try {
				return data.get(position).getInt("id");
			} catch (JSONException e) {
				e.printStackTrace();
				return 0;
			}
		}
		
		public String getJsonStr(int id){
			for(int i = 0;i < data.size();i++){
				try {
					int dId = data.get(i).getInt("id");
					if(dId == id){
						return data.get(i).getString("jsonStr");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return "";
		}

		@SuppressWarnings("null")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			final JSONObject food = data.get(position);
			
			//组装数据
			if(convertView == null){
				convertView = layoutInflater.inflate(R.layout.item_food_list, null);
				holder.icon = (ImageView) convertView.findViewById(R.id.imageview_icon);
				holder.name = (TextView) convertView.findViewById(R.id.textview_name);
				holder.price = (TextView) convertView.findViewById(R.id.textview_price);
				holder.flavour = (TextView) convertView.findViewById(R.id.textview_flavour);
				//使用tag存储数据
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			try {
				holder.name.setText(food.getString("name"));
				holder.price.setText(food.getString("price"));
				bitmapUtils.display(holder.icon, food.getString("picture"));
			    String flavour = food.getString("flavour");
			    if(!TextUtils.isEmpty(flavour))
			    	holder.flavour.setText(flavour);
			} catch (JSONException e) {
				e.printStackTrace();
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
	
	private class InitDataTask extends AsyncTask<Void, Void, List<JSONObject>> {
	    @Override
	    protected void onPostExecute(List<JSONObject> result) {
	       if(result.size() == 0){
	    	    ly_login.setVisibility(View.VISIBLE);
				tv_info.setText(R.string.activity_rmd_list_user_info_part);
				lv_rmd_food_list.setVisibility(View.GONE);
	       } else {
		        adapter.setData(result);
		        adapter.notifyDataSetChanged();
	       }
	    }

		@Override
		protected List<JSONObject> doInBackground(Void... p) {
			page = 0;
			return getData();
		}
	}

	private class RefreshDataTask extends AsyncTask<Void, Void, List<JSONObject>> {
	    @Override
	    protected void onPostExecute(List<JSONObject> result) {
	    	if(result.size() == 0){
	    		ly_login.setVisibility(View.VISIBLE);
				tv_info.setText(R.string.activity_rmd_list_user_info_part);
				lv_rmd_food_list.setVisibility(View.GONE);
	    	} else {
		        adapter.setData(result);
		        adapter.notifyDataSetChanged();
		        lv_rmd_food_list.onRefreshComplete();
	    	}
	    }

		@Override
		protected List<JSONObject> doInBackground(Void... p) {
			page = 0;
			return getData();
		}
	}
	
	private class MoreDataTask extends AsyncTask<Void, Void, List<JSONObject>> {
	    @Override
	    protected void onPostExecute(List<JSONObject> result) {
	    	if(result.size() == 0){
	    		Toast.makeText(context, R.string.no_more, Toast.LENGTH_SHORT).show();
	    		page--;
	    	}
	        for(JSONObject jObject : result){
	    	    adapter.addData(jObject);
	        }
	        adapter.notifyDataSetChanged();
	        lv_rmd_food_list.onRefreshComplete();
	    }

		@Override
		protected List<JSONObject> doInBackground(Void... p) {
			page++;
			return getData();
		}
	}

}
