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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.AppManager;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;


public class SelRegionActivity extends BaseActivity {

	private ListView lv_sel_listview;
	private LayoutInflater layoutInflater;
	private HttpUtils httpUtils;
	private TextView tv_title;
	private int superiorId;
	public static final String EXTRA_REGION = "EXTRA_REGION";
	private RegionAdapter adapter;
	private int type;
	public static final String EXTRA_TYPE= "EXTRA_TYPE";
	public static final int TYPE_PRODUCE = 1;
	public static final int TYPE_BUY= 2;
	private List<String> regions;
	public static final int REQUEST_CODE_LOCATION = 1;
	private int selectedRegionId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0L);
		
		type = getIntent().getIntExtra(EXTRA_TYPE, 0);
		
		setContentView(R.layout.activity_sel);
		
		regions = new ArrayList<String>();
		
		findViewById();
		setListeners();
		initView();
	}

	@Override
	protected void findViewById() {
		lv_sel_listview=(ListView)this.findViewById(R.id.sel_listview);
		tv_title = (TextView) findViewById(R.id.textview_title);
	}

	private void setListeners(){
		lv_sel_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JSONObject jFlavour = (JSONObject) adapter.getItem(position);
				try {
					regions.add(jFlavour.getString("name"));
					int childrenNum = jFlavour.getInt("childrenNum");
					if(childrenNum > 0)
					{
						superiorId = (int) id;
						initView();
					} else {
						selectedRegionId = (int) id;
						Intent intent = new Intent(SelRegionActivity.this,SearchLocationActivity.class);
						String[] regionArys = new String[regions.size()];
						for(int i = 0;i < regions.size();i++){
							regionArys[i] = regions.get(i);
						}
						intent.putExtra(SearchLocationActivity.EXTRA_REGIONS, regionArys);
						startActivityForResult(intent,REQUEST_CODE_LOCATION);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_CANCELED){
			superiorId = 0;
			initView();
		} else if(resultCode == Activity.RESULT_OK) {
			data.putExtra(AddFoodActivity.EXTRA_REGION_ID, selectedRegionId);
			setResult(RESULT_OK, data);
			finish();
		}
	}

	@Override
	protected void initView() {
		if(superiorId != 0){
			String url = Constants.MOBILE_SERVER_URL + "region/" + superiorId;
			httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					try {
						JSONObject jRegion = new JSONObject(arg0.result).getJSONObject("superiorRecipe");
						tv_title.setText(jRegion.getString("name"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			tv_title.setText(rs.getString(R.string.activity_sel_region_title));
			regions.clear();
			selectedRegionId = 0;
		}
		
		adapter = new RegionAdapter(new ArrayList<JSONObject>());
		lv_sel_listview.setAdapter(adapter);
		
		new GetFlavourTask().execute();
	}
	
	private class RegionAdapter extends BaseAdapter{
		private List<JSONObject> data;
		
		public RegionAdapter(List<JSONObject> data) {
			super();
			this.data = data;
		}
		
		public void setData(List<JSONObject> data) {
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
			JSONObject jFlavour = data.get(position);
			
			layoutInflater = LayoutInflater.from(SelRegionActivity.this);
			
			//组装数据
			if(convertView==null){
				convertView = layoutInflater.inflate(R.layout.item_flavour, null);
				holder.title=(TextView) convertView.findViewById(R.id.textview_item_title);
				holder.next = (ImageView) convertView.findViewById(R.id.imageview_next);
				//使用tag存储数据
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			
			try {
				holder.title.setText(jFlavour.getString("name"));
				int childrenNum = jFlavour.getInt("childrenNum");
				if(childrenNum == 0){
					holder.next.setVisibility(View.GONE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return convertView;
		
		}
		
	}				

	public static class ViewHolder {
	    TextView title;
	    ImageView next;
	}
	
	private class GetFlavourTask extends AsyncTask<Void, Integer, List<JSONObject>>{

		@Override
		protected List<JSONObject> doInBackground(Void... params) {
			String url = Constants.MOBILE_SERVER_URL + "region/" + superiorId + "/getChildren";
			try {
				String rs = httpUtils.sendSync(HttpMethod.GET, url).readString();
				JSONArray jRegions = new JSONObject(rs).getJSONArray("regions");
				List<JSONObject> data = new ArrayList<JSONObject>();
				for(int i = 0;i < jRegions.length();i++){
					JSONObject jRegion = jRegions.getJSONObject(i);
					url = Constants.MOBILE_SERVER_URL + "region/" + jRegion.getInt("id") + "/getChildren";
					rs = httpUtils.sendSync(HttpMethod.GET, url).readString();
					int childrenNum = new JSONObject(rs).getJSONArray("regions").length();
					jRegion.put("childrenNum", childrenNum);
					data.add(jRegion);
				}
				return data;
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return new ArrayList<JSONObject>();
			
		}

		@Override
		protected void onPostExecute(List<JSONObject> result) {
			adapter.setData(result);
			adapter.notifyDataSetChanged();
		}
		
	}
	
}
