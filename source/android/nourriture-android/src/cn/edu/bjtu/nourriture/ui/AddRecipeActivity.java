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

public class AddRecipeActivity extends BaseActivity {

	private EditText ed_recipe_name;
	private EditText ed_recipe_description;
	private EditText ed_recipe_ingredient;
	private EditText ed_recipe_picture;
	private EditText ed_recipe_customerId;
	private EditText ed_recipe_catogeryId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_recipe);
		findViewById();
	}

	@Override
	protected void findViewById() {
		ed_recipe_name = (EditText) findViewById(R.id.ed_recipe_name);
		ed_recipe_description = (EditText) findViewById(R.id.ed_recipe_description);
		ed_recipe_ingredient = (EditText) findViewById(R.id.ed_recipe_ingredient);
		ed_recipe_picture = (EditText) findViewById(R.id.ed_recipe_picture);
		ed_recipe_customerId = (EditText) findViewById(R.id.ed_recipe_customerId);
		ed_recipe_catogeryId = (EditText) findViewById(R.id.ed_recipe_catogeryId);
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
	public void addRecipe(View view) {
		String name = ed_recipe_name.getText().toString().trim();
		String description = ed_recipe_description.getText().toString().trim();
		String ingredient = ed_recipe_ingredient.getText().toString().trim();
		String picture = ed_recipe_picture.getText().toString().trim();
		String customerId = ed_recipe_customerId.getText().toString().trim();
		String catogeryId = ed_recipe_catogeryId.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(AddRecipeActivity.this, "请输入食谱名称",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(description)) {
			Toast.makeText(AddRecipeActivity.this, "请输入食谱描述",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(ingredient)) {
			Toast.makeText(AddRecipeActivity.this, "请输入食谱原料",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(picture)) {
			Toast.makeText(AddRecipeActivity.this, "请输入食谱图片的路径",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(customerId)) {
			Toast.makeText(AddRecipeActivity.this, "请输入提供食谱顾客的ID",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(catogeryId)) {
			Toast.makeText(AddRecipeActivity.this, "请输入食谱种类的ID",
					Toast.LENGTH_SHORT).show();
			return;
		}
		HttpUtils httpUtils = new HttpUtils();
		// String id = EMobileTask.getCookie("userId");
		String url = Constants.MOBILE_SERVER_URL + "recipe";
		RequestParams params = new RequestParams();
		params.addBodyParameter("name", name);
		params.addBodyParameter("description", description);
		params.addBodyParameter("ingredient", ingredient);
		params.addBodyParameter("picture", picture);
		params.addBodyParameter("customerId", customerId);
		params.addBodyParameter("catogeryId", catogeryId);

		httpUtils.send(HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("onFailure");
						Toast.makeText(AddRecipeActivity.this, "添加失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						LogUtils.d("onSuccess");
						System.out.println(arg0.result.toString());
						Toast.makeText(AddRecipeActivity.this, "添加成功",
								Toast.LENGTH_SHORT).show();
						AddRecipeActivity.this.finish();
					}

				});
	}
}
