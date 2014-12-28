package cn.edu.bjtu.nourriture.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.task.EMobileTask;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

public class ChangePwdActivity extends BaseActivity {
	private EditText ed_old_pwd;
	private EditText ed_new_pwd;
	private EditText ed_repwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_change_pwd);
		findViewById();
	}

	@Override
	protected void findViewById() {
		ed_old_pwd = (EditText) findViewById(R.id.ed_old_pwd);
		ed_new_pwd = (EditText) findViewById(R.id.ed_new_pwd);
		ed_repwd = (EditText) findViewById(R.id.ed_repwd);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * 修改密码的事件处理
	 * 
	 * @param view
	 */
	public void changePwd(View view) {
		String old_pwd = ed_old_pwd.getText().toString().trim();
		String newpassword = ed_new_pwd.getText().toString().trim();
		String repwd = ed_repwd.getText().toString().trim();
		if (TextUtils.isEmpty(old_pwd)) {
			Toast.makeText(ChangePwdActivity.this, "请输入原始密码",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(newpassword)) {
			Toast.makeText(ChangePwdActivity.this, "请输入新密码", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (TextUtils.isEmpty(repwd)) {
			Toast.makeText(ChangePwdActivity.this, "请再次输入密码",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!newpassword.equals(repwd)) {
			Toast.makeText(ChangePwdActivity.this, "两次输入的密码不一致,请重新输入",
					Toast.LENGTH_SHORT).show();
			return;
		}
		HttpUtils httpUtils = new HttpUtils();
		String idendity = EMobileTask.getCookie("idendity");
		String id = EMobileTask.getCookie("userId");
		String url = null;
		if (idendity.equals("customer")) {
			url = Constants.MOBILE_SERVER_URL + "customer/password/" + id;
		} else if (idendity.equals("manu")) {
			url = Constants.MOBILE_SERVER_URL + "manuFacturer/password/" + id;
		}
		RequestParams params = new RequestParams();
		// params.addBodyParameter("name", name);

		params.addBodyParameter("id", id);
		params.addBodyParameter("newpassword", newpassword);

		httpUtils.send(HttpMethod.PUT, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						LogUtils.d("onFailure");
						Toast.makeText(ChangePwdActivity.this, "密码修改失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						LogUtils.d("onSuccess");
						System.out.println(arg0.result);
						Toast.makeText(ChangePwdActivity.this, "密码修改成功",
								Toast.LENGTH_SHORT).show();
						ChangePwdActivity.this.finish();
					}
				});
	}

}
