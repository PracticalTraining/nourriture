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

public class RegisterManuActivity extends BaseActivity {
	private EditText ed_register_manu_username;
	private EditText ed_register_manu_pwd;
	private EditText ed_register_manu_repwd;
	private EditText ed_register_manu_company_name;
	private EditText ed_register_manu_company_description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register_manu);
		findViewById();
	}

	@Override
	protected void findViewById() {
		ed_register_manu_username = (EditText) findViewById(R.id.ed_register_manu_username);
		ed_register_manu_pwd = (EditText) findViewById(R.id.ed_register_manu_pwd);
		ed_register_manu_repwd = (EditText) findViewById(R.id.ed_register_manu_repwd);
		ed_register_manu_company_name = (EditText) findViewById(R.id.ed_register_manu_company_name);
		ed_register_manu_company_description = (EditText) findViewById(R.id.ed_register_manu_company_description);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	/**
	 * 制造商注册的点击事件
	 * 
	 * @param view
	 */
	public void register(View view) {
		String name = ed_register_manu_username.getText().toString().trim();
		String password = ed_register_manu_pwd.getText().toString().trim();
		String repwd = ed_register_manu_repwd.getText().toString().trim();
		String companyName = ed_register_manu_company_name.getText().toString()
				.trim();
		String description = ed_register_manu_company_description.getText()
				.toString().trim();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(RegisterManuActivity.this, "用户名不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(RegisterManuActivity.this, "密码不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(repwd)) {
			Toast.makeText(RegisterManuActivity.this, "确认密码不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(companyName)) {
			Toast.makeText(RegisterManuActivity.this, "公司名不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(description)) {
			Toast.makeText(RegisterManuActivity.this, "公司描述不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!repwd.equals(password)) {
			Toast.makeText(RegisterManuActivity.this, "两次输入的密码不一致,请重新输入",
					Toast.LENGTH_SHORT).show();
			return;
		}
		HttpUtils httpUtils = new HttpUtils();
		String url = Constants.MOBILE_SERVER_URL + "manuFacturer";
		RequestParams params = new RequestParams();
		params.addBodyParameter("name", name);
		params.addBodyParameter("password", password);
		// params.addQueryStringParameter("sex", sex);
		params.addBodyParameter("companyName", companyName);
		params.addBodyParameter("description", description);
		// params.addQueryStringParameter(nameValuePair)
		httpUtils.send(HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						LogUtils.d("onFailure");
						Toast.makeText(RegisterManuActivity.this, "注册失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						LogUtils.d("onSuccess");
						System.out.println(arg0.result);
						Toast.makeText(RegisterManuActivity.this, "注册成功",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

}
