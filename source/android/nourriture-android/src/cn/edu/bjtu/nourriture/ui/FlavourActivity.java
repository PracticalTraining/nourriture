package cn.edu.bjtu.nourriture.ui;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;


public class FlavourActivity extends BaseActivity {

	private ListView lv_flavour_listview;
	private LayoutInflater layoutInflater;
	private HttpUtils httpUtils;
	private TextView tv_title;
	private int superiorFlavourId;
	public static final String EXTRA_SP_FLAVOUR_ID = "superiorFlavourId";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		superiorFlavourId = intent.getIntExtra(EXTRA_SP_FLAVOUR_ID, 0);
		httpUtils = new HttpUtils();
		
		setContentView(R.layout.activity_flavour);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		lv_flavour_listview=(ListView)this.findViewById(R.id.flavour_listview);
		tv_title = (TextView) findViewById(R.id.textview_title);
	}

	@Override
	protected void initView() {
		if(superiorFlavourId != 0){
			String url = Constants.MOBILE_SERVER_URL + "flavour/" + superiorFlavourId;
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
		
		String url = Constants.MOBILE_SERVER_URL + "";
		//httpUtils.send(HttpMethod.GET, url, params, callBack);
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
			
			layoutInflater = LayoutInflater.from(FlavourActivity.this);
			
			//组装数据
			if(convertView==null){
				convertView = layoutInflater.inflate(R.layout.item_flavour, null);
				holder.title=(TextView) convertView.findViewById(R.id.textview_item_title);
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
