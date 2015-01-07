package cn.edu.bjtu.nourriture.ui;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.bean.Food;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;
import cn.edu.bjtu.nourriture.zxing.encoding.EncodeManager;

import com.google.zxing.WriterException;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

public class FoodActivity extends BaseActivity {
	private Food food;
	private Intent mIntent;
	private ImageView iv_picture;
	private TextView tv_title;
	private TextView tv_manuName;
	private TextView tv_price;
	private TextView tv_category;
	private TextView tv_flavour;
	private Button bt_producelc;
	private Button bt_buylc;
	private Button bt_qr_code;
	private Button bt_cmt;
	private HttpUtils HttpUtils;
	private BitmapUtils bitmapUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_food);
		
		HttpUtils = new HttpUtils();
		bitmapUtils = new BitmapUtils(this);
		
		mIntent = getIntent();
		food = new Food();
		try {
			JSONObject jFood = new JSONObject(mIntent.getStringExtra("food"));
			food.setId(jFood.getInt("id"));
			food.setName(jFood.getString("name"));
			food.setPicture(jFood.getString("picture"));
			food.setPrice(jFood.getDouble("price"));
			food.setFlavourId(jFood.getInt("flavourId"));
			food.setCategoryId(jFood.getInt("categoryId"));
			food.setBuyLocationId(jFood.getInt("buyLocationId"));
			food.setProduceLocationId(jFood.getInt("produceLocationId"));
			food.setManufacturerId(jFood.getInt("manufacturerId"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		findViewById();
		initView();
		setListeners();
	}

	@Override
	protected void findViewById() {
		iv_picture = (ImageView) findViewById(R.id.imageview_picture);
		tv_manuName = (TextView) findViewById(R.id.textview_manuName);
		tv_price = (TextView) findViewById(R.id.textview_price);
		tv_category = (TextView) findViewById(R.id.textview_category);
		tv_flavour = (TextView) findViewById(R.id.textview_flavour);
		bt_producelc = (Button) findViewById(R.id.button_producelc);
		bt_buylc = (Button) findViewById(R.id.button_buylc);
		tv_title = (TextView) findViewById(R.id.textview_title);
		bt_qr_code = (Button) findViewById(R.id.qr_code);
		bt_cmt = (Button) findViewById(R.id.button_cmt);
	}

	@Override
	protected void initView() {
		tv_title.setText(food.getName());
		
		bitmapUtils.display(iv_picture, food.getPicture());
		
		String url = Constants.MOBILE_SERVER_WS_URL + "manuFacturer/" + food.getManufacturerId();
		HttpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				JSONObject jobj;
				try {
					jobj = new JSONObject(arg0.result);
					tv_manuName.setText(jobj.getJSONObject("manuFacturer").getString("name"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		tv_price.setText(String.valueOf(food.getPrice()));
		
		url = Constants.MOBILE_SERVER_WS_URL + "foodCategory/" + food.getCategoryId();
		HttpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				JSONObject jobj;
				try {
					jobj = new JSONObject(arg0.result);
					tv_category.setText(jobj.getJSONObject("superiorFood").getString("name"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		url = Constants.MOBILE_SERVER_WS_URL + "flavour/" + food.getFlavourId();
		HttpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				JSONObject jobj;
				try {
					jobj = new JSONObject(arg0.result);
					tv_flavour.setText(jobj.getJSONObject("superiorFlavour").getString("name"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		new GetProduceLocationThread(food.getProduceLocationId()).start();
		new GetBuyLocationThread(food.getBuyLocationId()).start();
	}
	
	private void setListeners(){
		bt_qr_code.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Dialog dialog = new Dialog(FoodActivity.this, R.style.Transparent);
				dialog.setContentView(R.layout.dialog_qr_code);
				ImageView qr_code = (ImageView) dialog.findViewById(R.id.imageview_qr_code);
				try {
					JSONObject jObject = new JSONObject();
					jObject.put("type", "food");
					jObject.put("id", food.getId());
					qr_code.setImageBitmap(EncodeManager.Create2DCode(jObject.toString()));
				} catch (WriterException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				dialog.show();
			}
		});
		
		bt_cmt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FoodActivity.this,CmtActivity.class);
				intent.putExtra("type", CmtActivity.TYPE_FOOD);
				intent.putExtra("id", food.getId());
				startActivity(intent);
			}
		});
	}
	
	private String loadRegion(int id){
		String rUrl = Constants.MOBILE_SERVER_WS_URL + "region/" + id;
		try {
			String regionStr = HttpUtils.sendSync(HttpMethod.GET, rUrl).readString();
			JSONObject jRegion = new JSONObject(regionStr);
			int pRegionId = jRegion.getJSONObject("superiorRecipe").getInt("superiorRegionId");
			if(pRegionId > 0){
				return loadRegion(pRegionId) + jRegion.getJSONObject("superiorRecipe").getString("name");
			} else {
				return jRegion.getJSONObject("superiorRecipe").getString("name");
			}
		} catch (HttpException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	private class GetProduceLocationThread extends Thread{
		private int lId;

		public GetProduceLocationThread(int lId) {
			super();
			this.lId = lId;
		}

		@Override
		public void run() {
			try {
				String lUrl = Constants.MOBILE_SERVER_WS_URL + "location/" + lId;
				String locationStr = HttpUtils.sendSync(HttpMethod.GET, lUrl).readString();
				JSONObject jLocation = new JSONObject(locationStr);
				String locationName = jLocation.getString("detailAddress");
				int regionId = jLocation.getInt("regionId");
				String regionName = loadRegion(regionId);
				Message msg = getProduceLocationHandler.obtainMessage();
				Bundle bundle = new Bundle();
				bundle.putString("name", regionName + locationName);
				bundle.putDouble("longitude", jLocation.getDouble("longitude"));
				bundle.putDouble("latitude", jLocation.getDouble("latitude"));
				msg.setData(bundle);
				msg.sendToTarget();
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private class GetBuyLocationThread extends Thread{
		private int lId;

		public GetBuyLocationThread(int lId) {
			super();
			this.lId = lId;
		}

		@Override
		public void run() {
			try {
				String lUrl = Constants.MOBILE_SERVER_WS_URL + "location/" + lId;
				String locationStr = HttpUtils.sendSync(HttpMethod.GET, lUrl).readString();
				JSONObject jLocation = new JSONObject(locationStr);
				String locationName = jLocation.getString("detailAddress");
				int regionId = jLocation.getInt("regionId");
				String regionName = loadRegion(regionId);
				Message msg = getBuyLocationHandler.obtainMessage();
				Bundle bundle = new Bundle();
				bundle.putString("name", regionName + locationName);
				bundle.putDouble("longitude", jLocation.getDouble("longitude"));
				bundle.putDouble("latitude", jLocation.getDouble("latitude"));
				msg.setData(bundle);
				msg.sendToTarget();
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private Handler getProduceLocationHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			final String name = data.getString("name");
			final double longitude = data.getDouble("longitude");
			final double latitude = data.getDouble("latitude");
			bt_producelc.setText(name);
			if(longitude == 404 || latitude == 404){
				bt_producelc.setEnabled(false);
				bt_producelc.setCompoundDrawables(FoodActivity.this.getResources().getDrawable(R.drawable.local), null, null, null);
			} else {
				bt_producelc.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Uri uri = Uri.parse("geo:" + latitude + "," + longitude);  
						Intent it = new Intent(Intent.ACTION_VIEW,uri);  
						startActivity(it);  
					}
				});
			}
		}
		
	};
	
	private Handler getBuyLocationHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			final String name = data.getString("name");
			final double longitude = data.getDouble("longitude");
			final double latitude = data.getDouble("latitude");
			bt_buylc.setText(name);
			if(longitude == 404 || latitude == 404){
				bt_buylc.setEnabled(false);
				bt_buylc.setCompoundDrawables(FoodActivity.this.getResources().getDrawable(R.drawable.local), null, null, null);
			} else {
				bt_buylc.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Uri uri = Uri.parse("geo:" + latitude + "," + longitude);  
						Intent it = new Intent(Intent.ACTION_VIEW,uri);  
						startActivity(it);
					}
				});
			}
		}
		
	};

}
