package cn.edu.bjtu.nourriture.ui;

import org.json.JSONException;
import org.json.JSONObject;

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

public class EditManuInfoActivity extends BaseActivity {
	private EditText ed_manu_company_name;
	private EditText ed_manu_company_description;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_manu_info);
		findViewById();
		
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0L);
		String url = Constants.MOBILE_SERVER_URL + "manuFacturer/" + EMobileTask.getCookie("userId");		
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				LogUtils.d("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				try {
					LogUtils.d(arg0.result);
					JSONObject jManuFacturer = new JSONObject(arg0.result).getJSONObject("manuFacturer");
					ed_manu_company_name.setText(jManuFacturer.getString("companyName"));
					ed_manu_company_description.setText(jManuFacturer.getString("description"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void findViewById() {
		ed_manu_company_name = (EditText) findViewById(R.id.ed_manu_company_name);
		ed_manu_company_description = (EditText) findViewById(R.id.ed_manu_company_description);
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
		String companyName = ed_manu_company_name.getText().toString().trim();
		String description = ed_manu_company_description.getText().toString()
				.trim();
		if (TextUtils.isEmpty(companyName)) {
			Toast.makeText(EditManuInfoActivity.this, R.string.activity_edit_manu_info_input_company_name,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(description)) {
			Toast.makeText(EditManuInfoActivity.this, R.string.activity_edit_manu_info_input_company_des,
					Toast.LENGTH_SHORT).show();
			return;
		}
		HttpUtils httpUtils = new HttpUtils();
		String id = EMobileTask.getCookie("userId");
		String url = Constants.MOBILE_SERVER_URL + "manuFacturer/" + id;
		RequestParams params = new RequestParams();
		// params.addBodyParameter("id", id);
		params.addBodyParameter("companyName", companyName);
		params.addBodyParameter("description", description);
		httpUtils.send(HttpMethod.PUT, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						LogUtils.d("onFailure");
						Toast.makeText(EditManuInfoActivity.this, R.string.activity_edit_manu_info_fail,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						LogUtils.d("onSuccess");
						Toast.makeText(EditManuInfoActivity.this, R.string.activity_edit_manu_info_success,
								Toast.LENGTH_SHORT).show();
						EditManuInfoActivity.this.finish();
					}

				});
	}
}
