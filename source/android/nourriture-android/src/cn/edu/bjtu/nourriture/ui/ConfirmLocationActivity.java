package cn.edu.bjtu.nourriture.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;


public class ConfirmLocationActivity extends BaseActivity {
	public static final String EXTRA_LONGITUDE = "EXTRA_LONGITUDE";
	public static final String EXTRA_LATITUDE = "EXTRA_LATITUDE";
	public static final String EXTRA_NAME = "EXTRA_NAME";
	private double longitude;
	private double latitude;
	private String name;
	private MapView mapview;
	private BaiduMap baiduMap;
	private TextView tv_name;
	private GeoCoder mSearch; // 搜索模块，也可去掉地图模块独立使用
	private TextView tv_confirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_location);
		
		Intent intent = getIntent();
		longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 404);
		latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 404);
		name = intent.getStringExtra(EXTRA_NAME);
		mSearch = GeoCoder.newInstance();
		
		findViewById();
		initView();
		setListeners();
	}

	@Override
	protected void findViewById() {
		mapview = (MapView) findViewById(R.id.mapview);
		baiduMap = mapview.getMap();
		tv_name = (TextView) findViewById(R.id.textview_name);
		tv_confirm = (TextView) findViewById(R.id.textview_confirm);
	}

	@Override
	protected void initView() {
		baiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
		baiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(17));
		tv_name.setText(name);
	}

	private void setListeners() {
		baiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
			
			@Override
			public void onMapStatusChangeStart(MapStatus status) {
				
			}
			
			@Override
			public void onMapStatusChangeFinish(MapStatus status) {
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
				.location(status.target));
			}
			
			@Override
			public void onMapStatusChange(MapStatus status) {
				
			}
		});
		mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult rs) {
				longitude = rs.getLocation().longitude;
				latitude = rs.getLocation().latitude;
				name = rs.getAddress();
				tv_name.setText(name);
			}
			
			@Override
			public void onGetGeoCodeResult(GeoCodeResult rs) {
				
			}
		});
		tv_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra(EXTRA_LONGITUDE, longitude);
				data.putExtra(EXTRA_LATITUDE, latitude);
				data.putExtra(EXTRA_NAME, name);
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}
}
