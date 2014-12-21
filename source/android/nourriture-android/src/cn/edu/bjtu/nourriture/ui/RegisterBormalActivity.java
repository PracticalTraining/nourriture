package cn.edu.bjtu.nourriture.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
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

public class RegisterBormalActivity extends BaseActivity {
	private EditText ed_register_username;
	private EditText ed_register_pwd;
	private EditText ed_register_repwd;
	private EditText ed_register_age;
	private RadioGroup rd_register_sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register_normal);
		findViewById();

	}

	@Override
	protected void findViewById() {
		ed_register_username = (EditText) findViewById(R.id.ed_register_username);
		ed_register_pwd = (EditText) findViewById(R.id.ed_register_pwd);
		ed_register_repwd = (EditText) findViewById(R.id.ed_register_repwd);
		ed_register_age = (EditText) findViewById(R.id.ed_register_age);
		rd_register_sex = (RadioGroup) findViewById(R.id.rd_register_sex);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	/**
	 * 用户点击注册的时间处理
	 * 
	 * @param view
	 */
	public void register(View view) {
		String name = ed_register_username.getText().toString().trim();
		String password = ed_register_pwd.getText().toString().trim();
		String repwd = ed_register_repwd.getText().toString().trim();
		String ageStr = ed_register_age.getText().toString().trim();
		int age = Integer.parseInt(ageStr);
		int sex = 0;
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(RegisterBormalActivity.this, "用户名不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(RegisterBormalActivity.this, "密码不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(repwd)) {
			Toast.makeText(RegisterBormalActivity.this, "确认密码不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (age == 0) {
			Toast.makeText(RegisterBormalActivity.this, "年龄不能为空",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!password.equals(repwd)) {
			Toast.makeText(RegisterBormalActivity.this, "两次用户名和密码不一致,请重新输入",
					Toast.LENGTH_SHORT).show();
			return;
		}
		int checkedRadioButtonId = rd_register_sex.getCheckedRadioButtonId();
		if (checkedRadioButtonId == R.id.rb_boy) {
			sex = 0;
		} else if (checkedRadioButtonId == R.id.rb_girl) {
			sex = 1;
		}
		HttpUtils httpUtils = new HttpUtils();
		String url = Constants.MOBILE_SERVER_URL + "customer";
		RequestParams params = new RequestParams();
		params.addBodyParameter("name", name);
		params.addBodyParameter("password", password);
		params.addBodyParameter("sex", String.valueOf(sex));
		params.addBodyParameter("age", String.valueOf(age));
		// params.addHeader("name", name);
		// params.addHeader("password", password);
		// params.addHeader("sex", sex + "");
		// params.addHeader("age", age + "");
		// List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		// NameValuePair username = new BasicNameValuePair("name", name);
		// NameValuePair pwd = new BasicNameValuePair("password", password);
		// NameValuePair sex1 = new BasicNameValuePair("sex", sex + "");
		// NameValuePair age1 = new BasicNameValuePair("age", age + "");
		// parameters.add(username);
		// parameters.add(pwd);
		// parameters.add(sex1);
		// parameters.add(age1);
		httpUtils.send(HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						LogUtils.d("onFailure");
						Toast.makeText(RegisterBormalActivity.this, "注册失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						LogUtils.d("onSuccess");
						System.out.println(arg0.result);
						Toast.makeText(RegisterBormalActivity.this, "注册成功",
								Toast.LENGTH_SHORT).show();
					}
				});
	}
}
