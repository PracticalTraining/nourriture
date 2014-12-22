package cn.edu.bjtu.nourriture.ui;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

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


public class SndCategoryActivity extends BaseActivity {

	private ListView catergory_listview;
	private LayoutInflater layoutInflater;
	private int type;
	private int superiorCategoryId;
	private HttpUtils httpUtils;
	
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

		catergory_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int parent,
					long id) {
				//startActivity(new Intent(SndCategoryActivity.this,FoodListActivity.class));
				startActivity(new Intent(SndCategoryActivity.this,RecipeListActivity.class));
			}
		});
	}

	@Override
	protected void initView() {
		String url = Constants.MOBILE_SERVER_URL;
		url = type == 1 ? url + "foodCategory/getChilren" : url + "recipeCategory/getChilren";
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("id", String.valueOf(superiorCategoryId));
		httpUtils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				
			}
		});
		catergory_listview.setAdapter(new CatergorAdapter(null));
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
			
			layoutInflater=LayoutInflater.from(SndCategoryActivity.this);
			
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
