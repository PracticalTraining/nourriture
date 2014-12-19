package cn.edu.bjtu.nourriture.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.adapter.SearchListAdapter;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;
import cn.edu.bjtu.nourriture.utils.CommonTools;
import cn.edu.bjtu.nourriture.widgets.AutoClearEditText;


public class SearchActivity extends BaseActivity {

	private AutoClearEditText mEditText = null;
	private ImageButton mImageButton = null;
	private ListView mSearchListView;
	private SearchListAdapter mSearchListAdapter;
	private String foodsStr;
	private String recipesStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		findViewById();
		initView();
		setListeners();
		
		mSearchListAdapter = new SearchListAdapter(this);
		mSearchListView.setAdapter(mSearchListAdapter);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mEditText = (AutoClearEditText) findViewById(R.id.search_edit);
		mImageButton = (ImageButton) findViewById(R.id.search_button);
		mSearchListView = (ListView) findViewById(R.id.search_list);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mEditText.requestFocus();
		mImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(mEditText.getText())){
					DisPlay("关键词不能为空");
					return;
				}
				
				String keyword = mEditText.getText().toString().trim();
				
				HttpUtils httpUtils = new HttpUtils();
				
				String foodUrl = Constants.MOBILE_SERVER_URL + "food/searchByName";
				RequestParams params = new RequestParams();
				params.addQueryStringParameter("name", keyword);
				httpUtils.send(HttpMethod.GET, foodUrl, params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						DisPlay("服务器异常，请稍后重试");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							JSONObject json = new JSONObject(arg0.result);
							foodsStr = json.getJSONArray("foods").toString();
						} catch (JSONException e) {
							DisPlay("搜索失败");
							e.printStackTrace();
						}
					}
				});
				
				String recipeUrl = Constants.MOBILE_SERVER_URL + "recipe/searchByName";
				params = new RequestParams();
				params.addQueryStringParameter("name", keyword);
				httpUtils.send(HttpMethod.GET, recipeUrl, params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						DisPlay("服务器异常，请稍后重试");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						try {
							JSONObject json = new JSONObject(arg0.result);
							recipesStr = json.getJSONArray("recipes").toString();
						} catch (JSONException e) {
							DisPlay("搜索失败");
							e.printStackTrace();
						}
					}
				});
				
				Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
				intent.putExtra("foods", foodsStr);
				intent.putExtra("recipes", recipesStr);
				startActivity(intent);
			}
		});
	}
	
	private void setListeners(){
		mSearchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(SearchActivity.this,SearchResultActivity.class));
			}
			
		});
	}
}
