package cn.edu.bjtu.nourriture.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.ui.SearchFoodListActivity.Food;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;


public class FoodListActivity extends BaseActivity {

	private PullToRefreshListView pl_refresh;
	private LayoutInflater layoutInflater;
	private int categoryId;
	private HttpUtils httpUtils;
	private BitmapUtils bitmapUtils;
	private TextView tv_title;
	private FoodListAdapter adapter;
	private int page;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_food_list);
		
		httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0L);
		bitmapUtils = new BitmapUtils(this);
		
		Intent intent = getIntent();
		categoryId = intent.getIntExtra("categoryId", -1);
		
		findViewById();
		initView();
		setListeners();
	}

	@Override
	protected void findViewById() {
		pl_refresh = (PullToRefreshListView) this.findViewById(R.id.pl_refresh);
		tv_title = (TextView) findViewById(R.id.textview_title);
	}

	@Override
	protected void initView() {
		adapter = new FoodListAdapter(new ArrayList<JSONObject>());
		pl_refresh.setAdapter(adapter);
		new InitDataTask().execute();

		//设置PullRefreshListView上提加载时的加载提示
		pl_refresh.setMode(Mode.BOTH);
		pl_refresh.getLoadingLayoutProxy(false, true).setPullLabel(rs.getString(R.string.pull_up_load));
		pl_refresh.getLoadingLayoutProxy(false, true).setRefreshingLabel(rs.getString(R.string.loading));
		pl_refresh.getLoadingLayoutProxy(false, true).setReleaseLabel(rs.getString(R.string.loosen_load_more));

		// 设置PullRefreshListView下拉加载时的加载提示
		pl_refresh.getLoadingLayoutProxy(true, false).setPullLabel(rs.getString(R.string.pull_down_refresh));
		pl_refresh.getLoadingLayoutProxy(true, false).setRefreshingLabel(rs.getString(R.string.refreshing));
		pl_refresh.getLoadingLayoutProxy(true, false).setReleaseLabel(rs.getString(R.string.loosen_refreshing));
		
		String url = Constants.MOBILE_SERVER_WS_URL + "foodCategory/" + categoryId;
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					JSONObject jCategory = new JSONObject(arg0.result).getJSONObject("superiorFood");
					tv_title.setText(jCategory.getString("name"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void setListeners(){
		pl_refresh.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int parent,
					long id) {
				Intent intent = new Intent(FoodListActivity.this,FoodActivity.class);
				intent.putExtra("food", adapter.getJsonStr((int) id));
				startActivity(intent);
			}
		});
		pl_refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

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
			layoutInflater=LayoutInflater.from(FoodListActivity.this);
			final JSONObject food = data.get(position);
			
			//组装数据
			if(convertView == null){
				convertView=layoutInflater.inflate(R.layout.item_food_list, null);
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
	
	private List<JSONObject> getData(){
		List<JSONObject> data = new ArrayList<JSONObject>();
		String url = Constants.MOBILE_SERVER_WS_URL + "food/getPage";
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("categoryId", String.valueOf(categoryId));
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
			

	public static class ViewHolder {
		ImageView icon;
		TextView name;
		TextView price;
		TextView flavour;
	}
	
	private class InitDataTask extends AsyncTask<Void, Void, List<JSONObject>> {
	    @Override
	    protected void onPostExecute(List<JSONObject> result) {
	       adapter.setData(result);
	       adapter.notifyDataSetChanged();
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
	       adapter.setData(result);
	       adapter.notifyDataSetChanged();
	       pl_refresh.onRefreshComplete();
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
	    		DisPlay(rs.getString(R.string.no_more));
	    		page--;
	    	}
	        for(JSONObject jObject : result){
	    	    adapter.addData(jObject);
	        }
	        adapter.notifyDataSetChanged();
	        pl_refresh.onRefreshComplete();
	    }

		@Override
		protected List<JSONObject> doInBackground(Void... p) {
			page++;
			return getData();
		}
	}
}
