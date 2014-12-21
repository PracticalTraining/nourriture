package cn.edu.bjtu.nourriture.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
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

public class AddFoodActivity extends BaseActivity {

	private EditText ed_food_name;
	private EditText ed_food_price;
	private EditText ed_food_picture;
	private EditText ed_food_categoryId;
	private EditText ed_food_flavourId;
	private EditText ed_food_manufacturerId;
	private EditText ed_food_produceLocationId;
	private EditText ed_food_buyLocationId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_food);
		findViewById();
	}

	@Override
	protected void findViewById() {
		ed_food_name = (EditText) findViewById(R.id.ed_food_name);
		ed_food_price = (EditText) findViewById(R.id.ed_food_price);
		ed_food_picture = (EditText) findViewById(R.id.ed_food_picture);
		ed_food_categoryId = (EditText) findViewById(R.id.ed_food_categoryId);
		ed_food_flavourId = (EditText) findViewById(R.id.ed_food_flavourId);
		ed_food_manufacturerId = (EditText) findViewById(R.id.ed_food_manufacturerId);
		ed_food_produceLocationId = (EditText) findViewById(R.id.ed_food_produceLocationId);
		ed_food_buyLocationId = (EditText) findViewById(R.id.ed_food_buyLocationId);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	/**
	 * 确认修改的事件处理
	 * 
	 * @param view
	 */
	public void addFood(View view) {
		String name = ed_food_name.getText().toString().trim();
		String price = ed_food_price.getText().toString().trim();
		String picture = ed_food_picture.getText().toString().trim();
		String categoryId = ed_food_categoryId.getText().toString().trim();
		String flavourId = ed_food_flavourId.getText().toString().trim();
		String manufacturerId = ed_food_manufacturerId.getText().toString()
				.trim();
		String produceLocationId = ed_food_produceLocationId.getText()
				.toString().trim();
		String buyLocationId = ed_food_buyLocationId.getText().toString()
				.trim();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(AddFoodActivity.this, "请输入食物名称", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (TextUtils.isEmpty(price)) {
			Toast.makeText(AddFoodActivity.this, "请输入食物价格", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (TextUtils.isEmpty(picture)) {
			Toast.makeText(AddFoodActivity.this, "请输入食物图片路径",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(categoryId)) {
			Toast.makeText(AddFoodActivity.this, "请输入食物类别ID",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(flavourId)) {
			Toast.makeText(AddFoodActivity.this, "请输入食物口味ID",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(manufacturerId)) {
			Toast.makeText(AddFoodActivity.this, "请输入食物制造商ID",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(produceLocationId)) {
			Toast.makeText(AddFoodActivity.this, "请输入食物产地ID",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(buyLocationId)) {
			Toast.makeText(AddFoodActivity.this, "请输入食物购买地", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		HttpUtils httpUtils = new HttpUtils();
		// String id = EMobileTask.getCookie("userId");
		String url = Constants.MOBILE_SERVER_URL + "food";
		RequestParams params = new RequestParams();
		params.addBodyParameter("name", name);
		params.addBodyParameter("price", price);
		params.addBodyParameter("picture", picture);
		params.addBodyParameter("categoryId", categoryId);
		params.addBodyParameter("flavourId", flavourId);
		params.addBodyParameter("manufacturerId", manufacturerId);
		params.addBodyParameter("produceLocationId", produceLocationId);
		params.addBodyParameter("buyLocationId", buyLocationId);

		httpUtils.send(HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("onFailure");
						Toast.makeText(AddFoodActivity.this, "添加失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						LogUtils.d("onSuccess");
						System.out.println(arg0.result.toString());
						Toast.makeText(AddFoodActivity.this, "添加成功",
								Toast.LENGTH_SHORT).show();
						AddFoodActivity.this.finish();
					}

				});
	}
}
