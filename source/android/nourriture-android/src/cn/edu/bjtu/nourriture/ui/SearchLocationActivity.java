package cn.edu.bjtu.nourriture.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;
import cn.edu.bjtu.nourriture.widgets.AutoClearEditText;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.lidroid.xutils.util.LogUtils;


public class SearchLocationActivity extends BaseActivity {
	private TextView tv_region;
	private ListView lv_location_list;
	private ImageButton bt_search;
	private AutoClearEditText et_search;
	private TextView tv_locate;
	private LinearLayout ly_option_op;
	private LocationListAdapter adapter;
	public static final String EXTRA_REGIONS = "EXTRA_REGIONS";
	private String[] regions;
	private PoiSearch mPoiSearch;
	private SuggestionSearch mSuggestionSearch;
	private LocationClient mLocationClient;
	private ArrayAdapter<String> sugAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_location);
		
		regions = getIntent().getStringArrayExtra(EXTRA_REGIONS);
		mPoiSearch = PoiSearch.newInstance();
		mSuggestionSearch = SuggestionSearch.newInstance();
		mLocationClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setAddrType("all");
		mLocationClient.setLocOption(option);
		
		findViewById();
		initView();
		setListeners();
	}

	@Override
	protected void findViewById() {
		tv_region = (TextView) findViewById(R.id.textview_region);
		lv_location_list = (ListView) findViewById(R.id.listview_location_list);
		bt_search = (ImageButton) findViewById(R.id.search_button);
		et_search = (AutoClearEditText) findViewById(R.id.search_edit);
		tv_locate = (TextView) findViewById(R.id.textview_locate);
		ly_option_op = (LinearLayout) findViewById(R.id.linearlayout_option_op);
	}

	@Override
	protected void initView() {
		StringBuffer sb = new StringBuffer();
		for(String region : regions){
			sb.append(region);
			sb.append("-");
		}
		if(regions.length > 0){
			sb.delete(sb.length() - 1, sb.length());
		}
		tv_region.setText(sb.toString());
		
		adapter = new LocationListAdapter(new ArrayList<JSONObject>());
		lv_location_list.setAdapter(adapter);
		
		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		et_search.setAdapter(sugAdapter);
	}
	
	@Override
	public void onBackPressed() {
		setResult(Activity.RESULT_CANCELED);
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			setResult(RESULT_OK, data);
			finish();
		}
	}

	private void setListeners() {
		et_search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
	
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() <= 0) {
					ly_option_op.setVisibility(View.VISIBLE);
					lv_location_list.setVisibility(View.GONE);
					return;
				}
				
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch
						.requestSuggestion(new SuggestionSearchOption()
								.keyword(s.toString()).city(regions[regions.length - 1]));
			}
		});
		mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
			
			@Override
			public void onGetPoiResult(PoiResult result) {
				adapter.clear();
				if (result == null
						|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
					Toast.makeText(SearchLocationActivity.this, R.string.activity_search_location_no_result
							, Toast.LENGTH_SHORT).show();
					ly_option_op.setVisibility(View.VISIBLE);
					lv_location_list.setVisibility(View.GONE);
					return;
				}
				if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

					//当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
					String strInfo = rs.getString(R.string.activity_search_location_other_city);
					for (CityInfo cityInfo : result.getSuggestCityList()) {
						strInfo += cityInfo.city;
						strInfo += ",";
					}
					Toast.makeText(SearchLocationActivity.this, strInfo, Toast.LENGTH_LONG)
							.show();
					ly_option_op.setVisibility(View.VISIBLE);
					lv_location_list.setVisibility(View.GONE);
					return;
				}
				
				List<PoiInfo> pois = result.getAllPoi();
				List<JSONObject> jPois = new ArrayList<JSONObject>();
				for(PoiInfo poi : pois){
					JSONObject jPoi = new JSONObject();
					try {
						jPoi.put("name", poi.name);
						jPoi.put("longitude", poi.location.longitude);
						jPoi.put("latitude", poi.location.latitude);
						jPois.add(jPoi);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				adapter.setData(jPois);
				if(pois.size() != 0){
					ly_option_op.setVisibility(View.GONE);
					lv_location_list.setVisibility(View.VISIBLE);
				} else {
					ly_option_op.setVisibility(View.VISIBLE);
					lv_location_list.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void onGetPoiDetailResult(PoiDetailResult result) {
				
			}
		});
		mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
			
			@Override
			public void onGetSuggestionResult(SuggestionResult result) {
				if (result == null || result.getAllSuggestions() == null) {
					return;
				}
				List<SuggestionInfo> infos = result.getAllSuggestions();
				for(SuggestionInfo info : infos){
					if (info.key != null)
						sugAdapter.add(info.key);
				}
				sugAdapter.notifyDataSetChanged();
			}
		});
		bt_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(et_search.getText())){
					Toast.makeText(SearchLocationActivity.this, R.string.activity_search_location_keyword, Toast.LENGTH_SHORT).show();
					return;
				}
				mPoiSearch.searchInCity((new PoiCitySearchOption())
						.city(regions[regions.length - 1])
						.keyword(et_search.getText().toString()).pageNum(0));
			}
		});
		tv_locate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mLocationClient != null && !mLocationClient.isStarted())  
		        {  
		            mLocationClient.requestLocation();  
		            mLocationClient.start();  
		        }  
			}
		});
		mLocationClient.registerLocationListener(new BDLocationListener() {
			
			@Override
			public void onReceivePoi(BDLocation arg0) {
				LogUtils.d("onReceivePoi");
			}
			
			@Override
			public void onReceiveLocation(BDLocation arg0) {
				 LogUtils.d("onReceiveLocation," + arg0.getAddrStr() + "," + arg0.getLongitude() +
						 "," + arg0.getLatitude());
				 if (arg0 == null)
						return;
				 et_search.setText(arg0.getAddrStr());
				 mLocationClient.stop();
				 Intent intent = new Intent(SearchLocationActivity.this,ConfirmLocationActivity.class);
				 intent.putExtra(ConfirmLocationActivity.EXTRA_LONGITUDE, arg0.getLongitude());
				 intent.putExtra(ConfirmLocationActivity.EXTRA_LATITUDE, arg0.getLatitude());
				 intent.putExtra(ConfirmLocationActivity.EXTRA_NAME, arg0.getAddrStr());
				 startActivityForResult(intent,1);
			}
		});
		lv_location_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				JSONObject jloc = (JSONObject) adapter.getItem(position);
				Intent intent = new Intent(SearchLocationActivity.this,ConfirmLocationActivity.class);
				try {
					intent.putExtra(ConfirmLocationActivity.EXTRA_LONGITUDE, jloc.getDouble("longitude"));
					intent.putExtra(ConfirmLocationActivity.EXTRA_LATITUDE, jloc.getDouble("latitude"));
					intent.putExtra(ConfirmLocationActivity.EXTRA_NAME, jloc.getString("name"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				startActivityForResult(intent,1);
			}
		});
	}
	
	private class LocationListAdapter extends BaseAdapter {
		private List<JSONObject> data;
		
		public LocationListAdapter(List<JSONObject> data) {
			this.data = data;
		}
		
		public void setData(List<JSONObject> data) {
			this.data = data;
			notifyDataSetChanged();
		}
		
		public void clear(){
			data.clear();
			notifyDataSetChanged();
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
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			
			if(convertView == null){
				convertView = LayoutInflater.from(SearchLocationActivity.this).inflate(R.layout.item_search_list, null);
				holder.his = (TextView) convertView.findViewById(R.id.textview_his);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
	
			try {
				holder.his.setText(data.get(position).getString("name"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}
	
	private class ViewHolder{
		TextView his;
	}
}
