package cn.edu.bjtu.nourriture.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.task.EMobileTask;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RmdRecipeListFragment extends Fragment {
	private IndexActivity context;
	private PullToRefreshListView lv_rmd_recipe_list;
	private RecipeListAdapter adapter;
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
		View view = inflater.inflate(R.layout.fragment_rmd_recipe, null);
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
			tv_info.setText("您还没有登陆");
			lv_rmd_recipe_list.setVisibility(View.GONE);
		} else if(EMobileTask.getCookie("idendity").equals("manu")){
			ly_login.setVisibility(View.VISIBLE);
			tv_info.setText("厂商暂时不支持推荐");
			lv_rmd_recipe_list.setVisibility(View.GONE);
		} else {
			ly_login.setVisibility(View.GONE);
			lv_rmd_recipe_list.setVisibility(View.VISIBLE);
			lv_rmd_recipe_list.getRefreshableView().setVisibility(View.VISIBLE);
		}
		
		if(!TextUtils.isEmpty(EMobileTask.getCookie("userId")) && EMobileTask.getCookie("idendity").equals("customer")){
			page = 0;
			new InitDataTask().execute();
		}
	}
	
	private void findViews(View root){
		lv_rmd_recipe_list = (PullToRefreshListView) root.findViewById(R.id.listview_rmd_recipe_list);
		tv_info = (TextView) root.findViewById(R.id.textview_info);
		ly_login = (LinearLayout) root.findViewById(R.id.layout_login);
	}
	
	private void setListeners(){
		lv_rmd_recipe_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int parent,
					long id) {
				Intent intent = new Intent(context,RecipeActivity.class);
				intent.putExtra("recipe", adapter.getJsonStr((int) id));
				startActivity(intent);
			}
		});
		lv_rmd_recipe_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

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
		adapter = new RecipeListAdapter(new ArrayList<JSONObject>());
		lv_rmd_recipe_list.setAdapter(adapter);
		//设置PullRefreshListView上提加载时的加载提示
		lv_rmd_recipe_list.setMode(Mode.BOTH);
		lv_rmd_recipe_list.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载...");
		lv_rmd_recipe_list.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
		lv_rmd_recipe_list.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多...");
		// 设置PullRefreshListView下拉加载时的加载提示
		lv_rmd_recipe_list.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新...");
		lv_rmd_recipe_list.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
		lv_rmd_recipe_list.getLoadingLayoutProxy(true, false).setReleaseLabel("松开刷新...");
	}
	
	private List<JSONObject> getData(){
		List<JSONObject> data = new ArrayList<JSONObject>();
		String url = Constants.MOBILE_SERVER_URL + "recipe/recommend";
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("customerId", EMobileTask.getCookie("userId"));
		params.addQueryStringParameter("page", String.valueOf(page));
		try {
			String foodsStr = httpUtils.sendSync(HttpMethod.GET, url, params).readString();
			JSONArray jFoods = new JSONObject(foodsStr).getJSONArray("recipes");
			for(int i = 0;i < jFoods.length();i++){
				JSONObject jFood = jFoods.getJSONObject(i);
				jFood.put("jsonStr", jFood.toString());
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
	
	private class RecipeListAdapter extends BaseAdapter{

		private List<JSONObject> data;
		
		public RecipeListAdapter(List<JSONObject> data) {
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

			ViewHolder holder=new ViewHolder();
			LayoutInflater layoutInflater=LayoutInflater.from(context);
			final JSONObject recipe = data.get(position);
			
			//组装数据
			if(convertView==null){
				convertView=layoutInflater.inflate(R.layout.item_recipe_list, null);
				holder.icon = (ImageView) convertView.findViewById(R.id.imageview_icon);
				holder.name = (TextView) convertView.findViewById(R.id.textview_name);
				holder.ingredient = (TextView) convertView.findViewById(R.id.textview_ingredient);
				//使用tag存储数据
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			
			try {
				holder.name.setText(recipe.getString("name"));
				holder.ingredient.setText(recipe.getString("ingredient"));
				bitmapUtils.display(holder.icon, recipe.getString("picture"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return convertView;
		}
		
	}
			

	public static class ViewHolder {
		ImageView icon;
		TextView name;
		TextView ingredient;
	}
	
	private class InitDataTask extends AsyncTask<Void, Void, List<JSONObject>> {
	    @Override
	    protected void onPostExecute(List<JSONObject> result) {
	       if(result.size() == 0){
	    	    ly_login.setVisibility(View.VISIBLE);
				tv_info.setText("用户信息不完整，暂时没有推荐");
				lv_rmd_recipe_list.setVisibility(View.GONE);
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
				tv_info.setText("用户信息不完整，暂时没有推荐");
				lv_rmd_recipe_list.setVisibility(View.GONE);
	    	} else {
		        adapter.setData(result);
		        adapter.notifyDataSetChanged();
		        lv_rmd_recipe_list.onRefreshComplete();
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
	    		Toast.makeText(context, "没有更多了", Toast.LENGTH_SHORT).show();
	    		page--;
	    	}
	        for(JSONObject jObject : result){
	    	    adapter.addData(jObject);
	        }
	        adapter.notifyDataSetChanged();
	        lv_rmd_recipe_list.onRefreshComplete();
	    }

		@Override
		protected List<JSONObject> doInBackground(Void... p) {
			page++;
			return getData();
		}
	}

}
