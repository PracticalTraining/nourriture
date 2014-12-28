package cn.edu.bjtu.nourriture.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;


public class SndCategoryActivity extends BaseActivity {

	private ListView catergory_listview;
	private LayoutInflater layoutInflater;
	private int type;
	private int superiorCategoryId;
	private HttpUtils httpUtils;
	private TextView tv_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		type = intent.getIntExtra("type", -1);
		superiorCategoryId = intent.getIntExtra("superiorCategoryId", 0);
		
		httpUtils = new HttpUtils();
		
		setContentView(R.layout.activity_snd_category);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		catergory_listview=(ListView)this.findViewById(R.id.catergory_listview);
		tv_title = (TextView) findViewById(R.id.textview_title);

		catergory_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int parent,
					final long id) {
				LogUtils.i(id + "");
				String url = Constants.MOBILE_SERVER_URL;
				url = type == 0 ? url + "foodCategory/getChildren" : url + "recipeCategory/getChildren";
				RequestParams params = new RequestParams();
				params.addQueryStringParameter("id", String.valueOf(id));
				httpUtils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							JSONArray jCategorys = null;
							if(type == 0)
							{
								jCategorys = new JSONObject(arg0.result).getJSONArray("foodCategorys");
							} else if(type == 1){
								jCategorys = new JSONObject(arg0.result).getJSONArray("recipeCategorys");
							}
							if(jCategorys.length() == 0){
								Intent intent = new Intent();
								intent.putExtra("categoryId", (int) id);
								if(type == 0){
									intent.setClass(SndCategoryActivity.this.getApplicationContext(), FoodListActivity.class);
								} else if(type == 1){
									intent.setClass(SndCategoryActivity.this.getApplicationContext(), RecipeListActivity.class);
								}
								startActivity(intent);
							} else {
								Intent intent = new Intent(SndCategoryActivity.this,SndCategoryActivity.class);
								intent.putExtra("type", type);
								intent.putExtra("superiorCategoryId", (int) id);
								startActivity(intent);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	@Override
	protected void initView() {
		String url = Constants.MOBILE_SERVER_URL;
		url = type == 0 ? url + "foodCategory/getChildren" : url + "recipeCategory/getChildren";
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("id", String.valueOf(superiorCategoryId));
		httpUtils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					JSONArray jCategorys = null;
					if(type == 0)
					{
						jCategorys = new JSONObject(arg0.result).getJSONArray("foodCategorys");
					} else if(type == 1){
						jCategorys = new JSONObject(arg0.result).getJSONArray("recipeCategorys");
					}
					List<JSONObject> list = new ArrayList<JSONObject>();
					for(int i = 0;i < jCategorys.length();i++){
						list.add(jCategorys.getJSONObject(i));
					}
					catergory_listview.setAdapter(new CatergorAdapter(list));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		if(superiorCategoryId == 0){
			if(type == 0){
				tv_title.setText(R.string.food);
			} else {
				tv_title.setText(R.string.recipe);
			}
		} else {
			url = Constants.MOBILE_SERVER_URL;
			url = type == 0 ? url + "foodCategory/" : url + "recipeCategory/";
			url += superiorCategoryId;
			httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					try {
						JSONObject jCategory = null;
						if(type ==0)
							jCategory = new JSONObject(arg0.result).getJSONObject("superiorFood");
						else 
							jCategory = new JSONObject(arg0.result).getJSONObject("superiorRecipe");
						tv_title.setText(jCategory.getString("name"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	private class CatergorAdapter extends BaseAdapter{
		private List<JSONObject> data;
		
		public CatergorAdapter(List<JSONObject> data) {
			super();
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
			try {
				return data.get(position).getInt("id");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return 0;
		}

		@SuppressWarnings("null")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder=new ViewHolder();
			JSONObject jCategory = data.get(position);
			
			layoutInflater = LayoutInflater.from(SndCategoryActivity.this);
			
			//组装数据
			if(convertView==null){
				convertView=layoutInflater.inflate(R.layout.activity_category_item, null);
				holder.title=(TextView) convertView.findViewById(R.id.catergoryitem_title);
				//使用tag存储数据
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			try {
				holder.title.setText(jCategory.getString("name"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return convertView;
		
		}
		
		
		
	}				

	public static class ViewHolder {
	    TextView title;
	}
	
}
