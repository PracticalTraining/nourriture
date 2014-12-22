package cn.edu.bjtu.nourriture.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.adapter.SearchListAdapter;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.bean.SearchHistory;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;
import cn.edu.bjtu.nourriture.widgets.AutoClearEditText;
import cn.edu.bjtu.nourriture.widgets.LoadingWindow;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class SearchActivity extends BaseActivity {

	private AutoClearEditText mEditText = null;
	private ImageButton mImageButton = null;
	private ListView mSearchListView;
	private SearchListAdapter mSearchListAdapter;
	private LoadingWindow l;
	private LinearLayout lyNoHistory;
	private View line1;
	private View line2;
	private RelativeLayout clearHistory;
	private List<String> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		findViewById();
		initView();
		setListeners();

		data = new ArrayList<String>();
		mSearchListAdapter = new SearchListAdapter(this, data);
		mSearchListView.setAdapter(mSearchListAdapter);
	}

	@Override
	protected void findViewById() {
		mEditText = (AutoClearEditText) findViewById(R.id.search_edit);
		mImageButton = (ImageButton) findViewById(R.id.search_button);
		mSearchListView = (ListView) findViewById(R.id.search_list);
		lyNoHistory = (LinearLayout) findViewById(R.id.layout_login);
		line1 = findViewById(R.id.line1);
		line2 = findViewById(R.id.line2);
		clearHistory = (RelativeLayout) findViewById(R.id.relativelayout_clear_history);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mEditText.requestFocus();
		mImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mEditText.getText() == null)
					search(null);
				else
					search(mEditText.getText().toString().trim());
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (l != null && l.isShowing()) {
			l.dismiss();
		}

		DbUtils dbUtils = DbUtils.create(this);
		try {
			SqlInfo sqlInfo = new SqlInfo(
					"select * from search_history order by time desc");
			List<DbModel> list = dbUtils.findDbModelAll(sqlInfo);
			if (list.size() > 0) {
				mSearchListView.setVisibility(View.VISIBLE);
				line1.setVisibility(View.VISIBLE);
				clearHistory.setVisibility(View.VISIBLE);
				line2.setVisibility(View.VISIBLE);
				lyNoHistory.setVisibility(View.GONE);
				data.clear();
				for (int i = 0; i < list.size(); i++) {
					data.add(list.get(i).getString("keyword"));
					mSearchListAdapter.notifyDataSetChanged();
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	private void setListeners() {
		mSearchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				search((String) mSearchListAdapter.getItem(position));
			}
		});
		clearHistory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DbUtils dbUtils = DbUtils.create(SearchActivity.this);
				try {
					dbUtils.execNonQuery("delete from search_history");
					mSearchListView.setVisibility(View.GONE);
					line1.setVisibility(View.GONE);
					clearHistory.setVisibility(View.GONE);
					line2.setVisibility(View.GONE);
					lyNoHistory.setVisibility(View.VISIBLE);
					data.clear();
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private final void search(String keyword) {

		if (l != null && l.isShowing()) {
			return;
		}
		if (TextUtils.isEmpty(keyword)) {
			DisPlay("关键词不能为空");
			return;
		}

		l = new LoadingWindow(SearchActivity.this, R.style.Transparent);
		l.show();
		new SearchThread(keyword).start();

		SearchHistory searchHistory = new SearchHistory();
		searchHistory.setKeyword(keyword);
		searchHistory.setTime(new Date().getTime());
		DbUtils dbUtils = DbUtils.create(SearchActivity.this);
		try {
			dbUtils.createTableIfNotExist(SearchHistory.class);
			SqlInfo sqlInfo = new SqlInfo("select * from search_history where keyword = ?");
			sqlInfo.addBindArg(keyword);
			List<DbModel> list = dbUtils.findDbModelAll(sqlInfo);
			if (list.size() == 0)
				dbUtils.save(searchHistory);
			else {
				int id = list.get(0).getInt("id");
				sqlInfo = new SqlInfo(
						"update search_history set time = ? where id = ?");
				sqlInfo.addBindArg(new Date().getTime());
				sqlInfo.addBindArg(id);
				dbUtils.execNonQuery(sqlInfo);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	private class SearchThread extends Thread {
		private String keyWord;

		public SearchThread(String keyWord) {
			this.keyWord = keyWord;
		}

		@Override
		public void run() {
			HttpUtils httpUtils = new HttpUtils();

			String foodUrl = Constants.MOBILE_SERVER_URL + "food/searchByName";
			String recipeUrl = Constants.MOBILE_SERVER_URL
					+ "recipe/searchByName";
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("name", keyWord);
			try {
				String foodsStr = httpUtils.sendSync(HttpMethod.GET, foodUrl,
						params).readString();
				String recipesStr = httpUtils.sendSync(HttpMethod.GET,
						recipeUrl, params).readString();
				Message message = mSearchHandler.obtainMessage();
				Bundle bundle = new Bundle();
				bundle.putString("foods", new JSONObject(foodsStr)
						.getJSONArray("foods").toString());
				bundle.putString("recipes", new JSONObject(recipesStr)
						.getJSONArray("recipes").toString());
				message.setData(bundle);
				message.sendToTarget();
			} catch (final HttpException e1) {
				e1.printStackTrace();
				SearchActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						DisPlay("搜索失败:" + e1.getMessage());
						l.dismiss();
					}
				});
			} catch (final IOException e) {
				e.printStackTrace();
				SearchActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						DisPlay("搜索失败:" + e.getMessage());
						l.dismiss();
					}
				});
			} catch (final JSONException e) {
				e.printStackTrace();
				SearchActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						DisPlay("搜索失败:" + e.getMessage());
						l.dismiss();
					}
				});
			}
		}

	}

	private Handler mSearchHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Intent intent = new Intent(SearchActivity.this,
					SearchResultActivity.class);
			intent.putExtra("foods", msg.getData().getString("foods"));
			intent.putExtra("recipes", msg.getData().getString("recipes"));
			startActivity(intent);
		}

	};
}
