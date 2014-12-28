package cn.edu.bjtu.nourriture.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class EditBormalInfoActivity extends BaseActivity {
	private RadioGroup rdg_sex;
	private EditText ed_age;
	private RadioButton rb_boy;
	private RadioButton rb_girl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_normal_info);
		findViewById();
		
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0L);
		String url = Constants.MOBILE_SERVER_URL + "customer/" + EMobileTask.getCookie("userId");		
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.d("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					LogUtils.d(arg0.result);
					JSONObject jCustomer = new JSONObject(arg0.result).getJSONObject("customer");
					
					int sex = jCustomer.getInt("sex");
					if(sex == 0){
						rb_boy.setChecked(true);
					} else {
						rb_girl.setChecked(true);
					}
					
					int age = jCustomer.getInt("age");
					ed_age.setText(String.valueOf(age));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void findViewById() {
		rdg_sex = (RadioGroup) findViewById(R.id.rdg_sex);
		ed_age = (EditText) findViewById(R.id.ed_age);
		rb_boy = (RadioButton) findViewById(R.id.rb_boy);
		rb_girl = (RadioButton) findViewById(R.id.rb_girl);
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
	public void confirmUpdate(View view) {
		String age = ed_age.getText().toString().trim();
		if (TextUtils.isEmpty(age)) {
			Toast.makeText(EditBormalInfoActivity.this, R.string.activity_edit_bormal_info_input_age,
					Toast.LENGTH_SHORT).show();
			return;
		}
		int checkedRadioButtonId = rdg_sex.getCheckedRadioButtonId();
		int sex = 0;
		if (checkedRadioButtonId == R.id.rb_boy) {
			sex = 0;
		} else if (checkedRadioButtonId == R.id.rb_girl) {
			sex = 1;
		}
		HttpUtils httpUtils = new HttpUtils();
		String id = EMobileTask.getCookie("userId");
		String url = Constants.MOBILE_SERVER_URL + "customer/" + id;
		RequestParams params = new RequestParams();
		params.addBodyParameter("id", id);
		params.addBodyParameter("age", age);
		params.addBodyParameter("sex", sex + "");
		httpUtils.send(HttpMethod.PUT, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("onFailure");
						Toast.makeText(EditBormalInfoActivity.this, rs.getString(R.string.activity_edit_bormal_info_fail),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						LogUtils.d("onSuccess");
						System.out.println(arg0.result.toString());
						Toast.makeText(EditBormalInfoActivity.this, rs.getString(R.string.activity_edit_bormal_info_success),
								Toast.LENGTH_SHORT).show();
						EditBormalInfoActivity.this.finish();
					}

				});
	}
}
