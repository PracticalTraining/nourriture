package cn.edu.bjtu.nourriture.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ToggleButton;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.task.EMobileTask;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;
import cn.edu.bjtu.nourriture.widgets.LoadingWindow;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private static final String Tag = "LoginActivity";
	private LoginActivity loginActivity = null;
	private ImageView loginLogo, login_more;
	private EditText loginaccount, loginpassword;
	private ToggleButton isShowPassword;
	private boolean isDisplayflag = false;// 是否显示密码
	private String getpassword;
	private Button loginBtn, register;
	private RadioGroup mRG_Indendity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		loginActivity = LoginActivity.this;
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		loginLogo = (ImageView) this.findViewById(R.id.logo);
		// login_more=(ImageView)this.findViewById(R.id.login_more);
		loginaccount = (EditText) this.findViewById(R.id.loginaccount);
		loginpassword = (EditText) this.findViewById(R.id.loginpassword);

		isShowPassword = (ToggleButton) this.findViewById(R.id.isShowPassword);
		loginBtn = (Button) this.findViewById(R.id.login);
		register = (Button) this.findViewById(R.id.register);
		mRG_Indendity = (RadioGroup) findViewById(R.id.radiogroup_idendity);

		getpassword = loginpassword.getText().toString();
	}

	@Override
	protected void initView() {
		register.setOnClickListener(this);
		isShowPassword
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						Log.i(Tag, "开关按钮状态=" + isChecked);

						// if(getpassword.equals("")||getpassword.length()<=0){
						// DisPlay("密码不能为空");
						// }

						if (isChecked) {
							// 隐藏
							loginpassword.setInputType(0x90);
							// loginpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
						} else {
							// 明文显示
							loginpassword.setInputType(0x81);
							// loginpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
						}
						Log.i("togglebutton", "" + isChecked);
						// loginpassword.postInvalidate();
					}
				});

		loginBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register:
			Intent mIntent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(mIntent);
			break;

		case R.id.login:
			userlogin();
			break;

		default:
			break;
		}

	}

	private void userlogin() {
		final String username = loginaccount.getText().toString().trim();
		final String password = loginpassword.getText().toString().trim();
		final int identity = mRG_Indendity.getCheckedRadioButtonId();

		if (username.equals("")) {
			DisplayToast("用户名不能为空!");
			return;
		}
		if (password.equals("")) {
			DisplayToast("密码不能为空!");
			return;
		}
		if (identity == -1) {
			DisplayToast("请选择登陆身份");
			return;
		}

		final LoadingWindow l = EMobileTask.createLoaingWindow(this);
		l.show();

		HttpUtils httpUtils = new HttpUtils();
		String url = null;
		if (identity == R.id.radiobutton_customer) {
			url = Constants.MOBILE_SERVER_URL + "customer/login";
		} else {
			url = Constants.MOBILE_SERVER_URL + "manuFacturer/login";
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("name", username);
		params.addQueryStringParameter("password", password);
		httpUtils.send(HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("onFailure");

						l.dismiss();
						DisPlay("登陆失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						LogUtils.d("onSuccess");

						l.dismiss();
						try {
							JSONObject json = new JSONObject(arg0.result);
							int userId = json.getInt("id");
							EMobileTask.addCookie("userId",
									String.valueOf(userId));
							EMobileTask.addCookie("username", username);
							EMobileTask
									.addCookie(
											"idendity",
											identity == R.id.radiobutton_customer ? "普通用户"
													: "厂商");
							DisPlay("登陆成功");
							LoginActivity.this.finish();
						} catch (JSONException e) {
							DisPlay("登陆失败");
							e.printStackTrace();
						}
					}

				});
	}

}
