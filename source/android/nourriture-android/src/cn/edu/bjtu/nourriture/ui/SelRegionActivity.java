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
	public static final String EXTRA_FLAVOUR = "EXTRA_FLAVOUR";
	private FlavourAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0L);
		
		setContentView(R.layout.activity_sel);
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
					int childrenNum = jFlavour.getInt("childrenNum");
					if(childrenNum > 0)
					{
						superiorId = (int) id;
						initView();
					} else {
						Intent intent = new Intent();
						intent.putExtra(EXTRA_FLAVOUR, jFlavour.toString());
						setResult(AddFoodActivity.REQUEST_CODE_FLAVOUR,intent);
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void initView() {
		if(superiorId != 0){
			String url = Constants.MOBILE_SERVER_URL + "flavour/" + superiorId;
			httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					try {
						JSONObject jFlavour = new JSONObject(arg0.result).getJSONObject("superiorFlavour");
						tv_title.setText(jFlavour.getString("name"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		adapter = new FlavourAdapter(new ArrayList<JSONObject>());
		lv_sel_listview.setAdapter(adapter);
		
		new GetFlavourTask().execute();
	}
	
	private class FlavourAdapter extends BaseAdapter{
		private List<JSONObject> data;
		
		public FlavourAdapter(List<JSONObject> data) {
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
			String url = Constants.MOBILE_SERVER_URL + "flavour/" + superiorId + "/getChildren";
			try {
				String rs = httpUtils.sendSync(HttpMethod.GET, url).readString();
				JSONArray jFlavours = new JSONObject(rs).getJSONArray("flavours");
				List<JSONObject> data = new ArrayList<JSONObject>();
				for(int i = 0;i < jFlavours.length();i++){
					JSONObject jFlavour = jFlavours.getJSONObject(i);
					url = Constants.MOBILE_SERVER_URL + "flavour/" + jFlavour.getInt("id") + "/getChildren";
					rs = httpUtils.sendSync(HttpMethod.GET, url).readString();
					int childrenNum = new JSONObject(rs).getJSONArray("flavours").length();
					jFlavour.put("childrenNum", childrenNum);
					data.add(jFlavour);
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
