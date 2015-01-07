package cn.edu.bjtu.nourriture.ui;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.bean.Food;
import cn.edu.bjtu.nourriture.bean.Location;
import cn.edu.bjtu.nourriture.task.EMobileTask;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;
import cn.edu.bjtu.nourriture.widgets.LoadingWindow;
import cn.edu.bjtu.nourriture.zxing.view.SelectPicPopupWindow;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class AddFoodActivity extends BaseActivity {
	private ImageView iv_picture;
	private TextView tv_sel_flavour;
	private TextView tv_sel_food_category;
	private Button bt_produce_lc;
	private Button bt_buy_lc;
	private Button bt_ok;
	private EditText et_name;
	private EditText et_price;
	private LoadingWindow l;
	public static final int REQUEST_CODE_PICTURE = 1;
	public static final int REQUEST_CODE_FLAVOUR = 2;
	public static final int REQUEST_CODE_FOOD_CATEGORY = 3;
	public static final int REQUEST_CODE_PRODUCE_LOCATION = 4;
	public static final int REQUEST_CODE_BUY_LOCATION = 5;
	public static final String EXTRA_REGION_ID = "EXTRA_REGION_ID";
	private HttpUtils httpUtils;
	private Food food;
	private Location produce_loc;
	private Location buy_loc;
	private String imagePath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_food);
		
		httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0L);
		food = new Food();
		food.setManufacturerId(Integer.valueOf(EMobileTask.getCookie("userId")));
		produce_loc = new Location();
		buy_loc = new Location();
		
		findViewById();
		setLisenters();
		initView();
	}

	@Override
	protected void findViewById() {
		iv_picture = (ImageView) findViewById(R.id.imageview_picture);
		tv_sel_flavour = (TextView) findViewById(R.id.textview_sel_flavour);
		tv_sel_food_category = (TextView) findViewById(R.id.textview_sel_food_category);
		bt_produce_lc = (Button) findViewById(R.id.button_producelc);
		bt_buy_lc = (Button) findViewById(R.id.button_buylc);
		bt_ok = (Button) findViewById(R.id.button_ok);
		et_name = (EditText) findViewById(R.id.edittext_name);
		et_price = (EditText) findViewById(R.id.edittext_price);
	}
	
	private void setLisenters(){
		iv_picture.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				int width = iv_picture.getWidth();
				int rmdHeight = width / 2;
				LinearLayout.LayoutParams lp = (LayoutParams) iv_picture.getLayoutParams();
				lp.height = rmdHeight;
				iv_picture.setLayoutParams(lp);
			}
		});
		iv_picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(AddFoodActivity.this,
						SelectPicPopupWindow.class), REQUEST_CODE_PICTURE);
			}
		});
		tv_sel_flavour.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddFoodActivity.this,SelFlavourActivity.class);
				startActivityForResult(intent, REQUEST_CODE_FLAVOUR);
			}
		});
		tv_sel_food_category.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddFoodActivity.this,SelFoodCategoryActivity.class);
				startActivityForResult(intent, REQUEST_CODE_FOOD_CATEGORY);
			}
		});
		bt_produce_lc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddFoodActivity.this,SelRegionActivity.class);
				intent.putExtra(SelRegionActivity.EXTRA_TYPE, SelRegionActivity.TYPE_PRODUCE);
				startActivityForResult(intent, REQUEST_CODE_PRODUCE_LOCATION);
			}
		});
		bt_buy_lc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddFoodActivity.this,SelRegionActivity.class);
				intent.putExtra(SelRegionActivity.EXTRA_TYPE, SelRegionActivity.TYPE_BUY);
				startActivityForResult(intent, REQUEST_CODE_BUY_LOCATION);
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(imagePath)){
					Toast.makeText(AddFoodActivity.this, R.string.activity_add_food_no_image, Toast.LENGTH_SHORT).show();
					return;
				}
				
				Editable name = et_name.getText();
				if(TextUtils.isEmpty(name)){
					Toast.makeText(AddFoodActivity.this, R.string.activity_add_food_no_name, Toast.LENGTH_SHORT).show();
					return;
				}
				
				Editable price = et_price.getText();
				if(TextUtils.isEmpty(price)){
					Toast.makeText(AddFoodActivity.this, R.string.activity_add_food_no_price, Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(food.getFlavourId() <= 0){
					Toast.makeText(AddFoodActivity.this, R.string.activity_add_food_no_flavour, Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(food.getCategoryId() <= 0){
					Toast.makeText(AddFoodActivity.this, R.string.activity_add_food_no_category, Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(TextUtils.isEmpty(produce_loc.getDetailAddress())){
					Toast.makeText(AddFoodActivity.this, R.string.activity_add_food_no_produce_loc, Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(TextUtils.isEmpty(buy_loc.getDetailAddress())){
					Toast.makeText(AddFoodActivity.this, R.string.activity_add_food_no_buy_loc, Toast.LENGTH_SHORT).show();
					return;
				}
				
				l = EMobileTask.createLoaingWindow(AddFoodActivity.this);
				l.show();
				mAddFoodTask.execute();
			}
		});
	}

	@Override
	protected void initView() {
		
	}
	
	private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != RESULT_OK){
			return;
		}
		switch (requestCode) {
			case REQUEST_CODE_PICTURE:
				if (data != null) {
					Uri mImageCaptureUri = data.getData();
					if (mImageCaptureUri != null) {
						try {
							Bitmap image = MediaStore.Images.Media.getBitmap(
									this.getContentResolver(), mImageCaptureUri);
							if (image != null) {
								this.imagePath = getRealPathFromURI(mImageCaptureUri);
								LinearLayout.LayoutParams lp = (LayoutParams) iv_picture.getLayoutParams();
								lp.height = LayoutParams.WRAP_CONTENT;
								iv_picture.setLayoutParams(lp);
								iv_picture.setImageBitmap(image);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} 
//					else {
//						LogUtils.d("else");
//						Bundle extras = data.getExtras();
//						if (extras != null) {
//							Bitmap image = extras.getParcelable("data");
//							if (image != null) {
//								LinearLayout.LayoutParams lp = (LayoutParams) iv_picture.getLayoutParams();
//								lp.height = LayoutParams.WRAP_CONTENT;
//								iv_picture.setLayoutParams(lp);
//								iv_picture.setImageBitmap(image);
//							}
//						}
//					}
				}
				break;
			case REQUEST_CODE_FLAVOUR:
				try {
					JSONObject jFlavour = new JSONObject(data.getStringExtra(SelFlavourActivity.EXTRA_FLAVOUR));
					tv_sel_flavour.setText(jFlavour.getString("name"));
					food.setFlavourId(jFlavour.getInt("id"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case REQUEST_CODE_FOOD_CATEGORY:
				try {
					JSONObject jFoodCategory = new JSONObject(data.getStringExtra(SelFoodCategoryActivity.EXTRA_FOOD_CATEGORY));
					tv_sel_food_category.setText(jFoodCategory.getString("name"));
					food.setCategoryId(jFoodCategory.getInt("id"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case REQUEST_CODE_PRODUCE_LOCATION:
				bt_produce_lc.setText(data.getStringExtra(ConfirmLocationActivity.EXTRA_NAME));
				produce_loc.setDetailAddress(data.getStringExtra(ConfirmLocationActivity.EXTRA_NAME));
				produce_loc.setLongitude(data.getDoubleExtra(ConfirmLocationActivity.EXTRA_LONGITUDE,404));
				produce_loc.setLatitude(data.getDoubleExtra(ConfirmLocationActivity.EXTRA_LONGITUDE,404));
				produce_loc.setRegionId(data.getIntExtra(EXTRA_REGION_ID, 0));
				break;
			case REQUEST_CODE_BUY_LOCATION:
				bt_buy_lc.setText(data.getStringExtra(ConfirmLocationActivity.EXTRA_NAME));
				buy_loc.setDetailAddress(data.getStringExtra(ConfirmLocationActivity.EXTRA_NAME));
				buy_loc.setLongitude(data.getDoubleExtra(ConfirmLocationActivity.EXTRA_LONGITUDE,404));
				buy_loc.setLatitude(data.getDoubleExtra(ConfirmLocationActivity.EXTRA_LONGITUDE,404));
				buy_loc.setRegionId(data.getIntExtra(EXTRA_REGION_ID, 0));
				break;
			default:
				break;

		}
	}
	
	private AsyncTask<Void, Integer, Void> mAddFoodTask = new AsyncTask<Void, Integer, Void>(){

		@Override
		protected Void doInBackground(Void... p) {
			String url = Constants.MOBILE_SERVER_URL + "upload.action";
			RequestParams params = new RequestParams();
			params.addBodyParameter("file", new File(imagePath));
			try {
				httpUtils.sendSync(HttpMethod.POST, url, params);
			} catch (HttpException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			l.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
		
	};
}
