package cn.edu.bjtu.nourriture.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.task.EMobileTask;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;
import cn.edu.bjtu.nourriture.widgets.LoadingWindow;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

public class CmtActivity extends BaseActivity {
	private PullToRefreshListView lv_cmt_list;
	private CmtAdapater adapter;
	private Button score1,score2,score3,score4,score5;
	private int score;
	private int type;
	private int id;
	private int page;
	public static final int TYPE_FOOD = 0;
	public static final int TYPE_RECIPE = 1;
	private HttpUtils httpUtils;
	private LinearLayout ly_no_cmt;
	private Button bt_cmt;
	private EditText et_cmt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cmt);
		
		httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0L);
		
		Intent intent = getIntent();
		type = intent.getIntExtra("type", -1);
		id = intent.getIntExtra("id", -1);
		
		findViewById();
		setListeners();
		initView();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void findViewById() {
		lv_cmt_list = (PullToRefreshListView) findViewById(R.id.listview_cmt_list);
		score1 = (Button) findViewById(R.id.button_score1);
		score2 = (Button) findViewById(R.id.button_score2);
		score3 = (Button) findViewById(R.id.button_score3);
		score4 = (Button) findViewById(R.id.button_score4);
		score5 = (Button) findViewById(R.id.button_score5);
		ly_no_cmt = (LinearLayout) findViewById(R.id.layout_login);
		bt_cmt = (Button) findViewById(R.id.button_cmt);
		et_cmt = (EditText) findViewById(R.id.edittext_cmt);
	}
	
	private void setListeners(){
		score1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				score1.setBackgroundResource(R.drawable.cang_red);
				score2.setBackgroundResource(R.drawable.cang);
				score3.setBackgroundResource(R.drawable.cang);
				score4.setBackgroundResource(R.drawable.cang);
				score5.setBackgroundResource(R.drawable.cang);
				score = 1;
			}
		});
		score2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				score1.setBackgroundResource(R.drawable.cang_red);
				score2.setBackgroundResource(R.drawable.cang_red);
				score3.setBackgroundResource(R.drawable.cang);
				score4.setBackgroundResource(R.drawable.cang);
				score5.setBackgroundResource(R.drawable.cang);
				score = 2;
			}
		});
		score3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				score1.setBackgroundResource(R.drawable.cang_red);
				score2.setBackgroundResource(R.drawable.cang_red);
				score3.setBackgroundResource(R.drawable.cang_red);
				score4.setBackgroundResource(R.drawable.cang);
				score5.setBackgroundResource(R.drawable.cang);
				score = 3;
			}
		});
		score4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				score1.setBackgroundResource(R.drawable.cang_red);
				score2.setBackgroundResource(R.drawable.cang_red);
				score3.setBackgroundResource(R.drawable.cang_red);
				score4.setBackgroundResource(R.drawable.cang_red);
				score5.setBackgroundResource(R.drawable.cang);
				score = 4;
			}
		});
		score5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				score1.setBackgroundResource(R.drawable.cang_red);
				score2.setBackgroundResource(R.drawable.cang_red);
				score3.setBackgroundResource(R.drawable.cang_red);
				score4.setBackgroundResource(R.drawable.cang_red);
				score5.setBackgroundResource(R.drawable.cang_red);
				score = 5;
			}
		});
		lv_cmt_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				LogUtils.i("onPullDownToRefresh");
				new RefreshDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				LogUtils.i("onPullUpToRefresh");
				new MoreDataTask().execute();
			}

		});
		bt_cmt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String userId = EMobileTask.getCookie("userId");
				String idendity = EMobileTask.getCookie("idendity");
				if(userId == null){
					Toast.makeText(CmtActivity.this, R.string.activity_no_login, Toast.LENGTH_SHORT).show();
					return;
				}
				if(idendity.equals("manu")){
					Toast.makeText(CmtActivity.this, R.string.activity_manu_not_support, Toast.LENGTH_SHORT).show();
					return;
				}
				if(et_cmt.getText() == null){
					Toast.makeText(CmtActivity.this, R.string.activity_manu_cmt_not_empty, Toast.LENGTH_SHORT).show();
					return;
				}
				
				final String cmt = et_cmt.getText().toString();
				et_cmt.setText("");
				String url = Constants.MOBILE_SERVER_WS_URL + "comments";
				RequestParams params = new RequestParams();
				params.addBodyParameter("score", String.valueOf(score));
				params.addBodyParameter("description", cmt);
				params.addBodyParameter("customerId", String.valueOf(userId));
				params.addBodyParameter("commentON", String.valueOf(type));
				params.addBodyParameter("refId", String.valueOf(id));
				final LoadingWindow l = EMobileTask.createLoaingWindow(CmtActivity.this);
				l.show();
				httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						l.dismiss();
						Toast.makeText(CmtActivity.this, R.string.activity_cmt_fail, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						l.dismiss();
						JSONObject jCmt = new JSONObject();
						try {
							jCmt.put("score", String.valueOf(score));
							jCmt.put("description", cmt);
							jCmt.put("customerName", EMobileTask.getCookie("username"));
							if(lv_cmt_list.getVisibility() == View.GONE){
								lv_cmt_list.setVisibility(View.VISIBLE);
								ly_no_cmt.setVisibility(View.GONE);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						adapter.addData(jCmt);
					}
				});
			}
		});
	}

	@Override
	protected void initView() {
		adapter = new CmtAdapater(new ArrayList<JSONObject>());
		lv_cmt_list.setAdapter(adapter);
		//设置PullRefreshListView上提加载时的加载提示
		lv_cmt_list.setMode(Mode.BOTH);
		lv_cmt_list.getLoadingLayoutProxy(false, true).setPullLabel(rs.getString(R.string.pull_up_load));
		lv_cmt_list.getLoadingLayoutProxy(false, true).setRefreshingLabel(rs.getString(R.string.loading));
		lv_cmt_list.getLoadingLayoutProxy(false, true).setReleaseLabel(rs.getString(R.string.loosen_load_more));
		// 设置PullRefreshListView下拉加载时的加载提示
		lv_cmt_list.getLoadingLayoutProxy(true, false).setPullLabel(rs.getString(R.string.pull_down_refresh));
		lv_cmt_list.getLoadingLayoutProxy(true, false).setRefreshingLabel(rs.getString(R.string.refreshing));
		lv_cmt_list.getLoadingLayoutProxy(true, false).setReleaseLabel(rs.getString(R.string.loosen_refreshing));
		
		new InitDataTask().execute();
	}
	
	private List<JSONObject> getData(){
		List<JSONObject> data = new ArrayList<JSONObject>();
		String url = Constants.MOBILE_SERVER_WS_URL;
		if(type == TYPE_FOOD){
			url += "comments/getFoodCmt";
		} else if(type == TYPE_RECIPE){
			url += "comments/getRecipeCmt";
		}
		RequestParams params = new RequestParams();
		if(type == TYPE_FOOD){
			params.addQueryStringParameter("foodId", String.valueOf(id));
		} else if(type == TYPE_RECIPE){
			params.addQueryStringParameter("recipeId", String.valueOf(id));
		}
		params.addQueryStringParameter("page", String.valueOf(page + 1));
		params.addQueryStringParameter("pageSize", String.valueOf(5));
		try {
			String cmtsStr = httpUtils.sendSync(HttpMethod.GET, url, params).readString();
			JSONArray jCmts = new JSONObject(cmtsStr).getJSONArray("comments");
			for(int i = 0;i < jCmts.length();i++){
				JSONObject jCmt = jCmts.getJSONObject(i);
				int customerId = jCmt.getInt("customerId");
				url = Constants.MOBILE_SERVER_WS_URL + "customer/" + customerId;
				String customerStr = httpUtils.sendSync(HttpMethod.GET, url).readString();
				JSONObject jCustomer = new JSONObject(customerStr).getJSONObject("customer");
				String name = jCustomer.getString("name");
				jCmt.put("customerName", name);
				data.add(jCmt);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			return data;
		}
	}

	private class CmtAdapater extends BaseAdapter{
		private List<JSONObject> data;
		
		public CmtAdapater(List<JSONObject> data) {
			super();
			this.data = data;
		}

		public void setData(List<JSONObject> data) {
			this.data = data;
		}
		
		public void addData(JSONObject jsonObject){
			data.add(jsonObject);
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			JSONObject jCmt = data.get(position);
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(CmtActivity.this).inflate(R.layout.item_cmt, null);
				holder.name = (TextView) convertView.findViewById(R.id.textview_name);
				holder.des = (TextView) convertView.findViewById(R.id.textview_des);
				holder.score_1 = (ImageView) convertView.findViewById(R.id.imageview_score1);
				holder.score_2 = (ImageView) convertView.findViewById(R.id.imageview_score2);
				holder.score_3 = (ImageView) convertView.findViewById(R.id.imageview_score3);
				holder.score_4 = (ImageView) convertView.findViewById(R.id.imageview_score4);
				holder.score_5 = (ImageView) convertView.findViewById(R.id.imageview_score5);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			try {
				holder.name.setText(jCmt.getString("customerName"));
				holder.des.setText(jCmt.getString("description"));
				switch (jCmt.getInt("score")) {
				case 0:
					holder.score_1.setImageResource(R.drawable.collection_grey);
					holder.score_2.setImageResource(R.drawable.collection_grey);
					holder.score_3.setImageResource(R.drawable.collection_grey);
					holder.score_4.setImageResource(R.drawable.collection_grey);
					holder.score_5.setImageResource(R.drawable.collection_grey);
					break;
				case 1:
					holder.score_1.setImageResource(R.drawable.collection_red_small);
					holder.score_2.setImageResource(R.drawable.collection_grey);
					holder.score_3.setImageResource(R.drawable.collection_grey);
					holder.score_4.setImageResource(R.drawable.collection_grey);
					holder.score_5.setImageResource(R.drawable.collection_grey);
					break;
				case 2:
					holder.score_1.setImageResource(R.drawable.collection_red_small);
					holder.score_2.setImageResource(R.drawable.collection_red_small);
					holder.score_3.setImageResource(R.drawable.collection_grey);
					holder.score_4.setImageResource(R.drawable.collection_grey);
					holder.score_5.setImageResource(R.drawable.collection_grey);	
					break;
				case 3:
					holder.score_1.setImageResource(R.drawable.collection_red_small);
					holder.score_2.setImageResource(R.drawable.collection_red_small);
					holder.score_3.setImageResource(R.drawable.collection_red_small);
					holder.score_4.setImageResource(R.drawable.collection_grey);
					holder.score_5.setImageResource(R.drawable.collection_grey);
					break;
				case 4:
					holder.score_1.setImageResource(R.drawable.collection_red_small);
					holder.score_2.setImageResource(R.drawable.collection_red_small);
					holder.score_3.setImageResource(R.drawable.collection_red_small);
					holder.score_4.setImageResource(R.drawable.collection_red_small);
					holder.score_5.setImageResource(R.drawable.collection_grey);
					break;
				case 5:
					holder.score_1.setImageResource(R.drawable.collection_red_small);
					holder.score_2.setImageResource(R.drawable.collection_red_small);
					holder.score_3.setImageResource(R.drawable.collection_red_small);
					holder.score_4.setImageResource(R.drawable.collection_red_small);
					holder.score_5.setImageResource(R.drawable.collection_red_small);
					break;

				default:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return convertView;
		}
		
	}
	
	private class ViewHolder{
		TextView name;
		TextView des;
		ImageView score_1;
		ImageView score_2;
		ImageView score_3;
		ImageView score_4;
		ImageView score_5;
	}
	
	private class InitDataTask extends AsyncTask<Void, Void, List<JSONObject>> {
	    @Override
	    protected void onPostExecute(List<JSONObject> result) {
	       if(result.size() == 0){
	    	   ly_no_cmt.setVisibility(View.VISIBLE);
	    	   lv_cmt_list.setVisibility(View.GONE);
	       } else {
	    	   ly_no_cmt.setVisibility(View.GONE);
	    	   lv_cmt_list.setVisibility(View.VISIBLE);
		       adapter.setData(result);
		       adapter.notifyDataSetChanged();
	       }
	    }

		@Override
		protected List<JSONObject> doInBackground(Void... p) {
			page = 0;
			return getData();
		}
	}

	private class RefreshDataTask extends AsyncTask<Void, Void, List<JSONObject>> {
	    @Override
	    protected void onPostExecute(List<JSONObject> result) {
	    	if(result.size() == 0){
	    	   ly_no_cmt.setVisibility(View.VISIBLE);
	    	   lv_cmt_list.setVisibility(View.GONE);
		    } else {
		       adapter.setData(result);
		       adapter.notifyDataSetChanged();
		       ly_no_cmt.setVisibility(View.GONE);
	    	   lv_cmt_list.setVisibility(View.VISIBLE);
		       lv_cmt_list.onRefreshComplete();
		    }
	    }

		@Override
		protected List<JSONObject> doInBackground(Void... p) {
			page = 0;
			return getData();
		}
	}
	
	private class MoreDataTask extends AsyncTask<Void, Void, List<JSONObject>> {
	    @Override
	    protected void onPostExecute(List<JSONObject> result) {
	    	if(result.size() == 0){
	    		DisPlay(rs.getString(R.string.no_more));
	    		page--;
	    	}
	        for(JSONObject jObject : result){
	    	    adapter.addData(jObject);
	        }
	        adapter.notifyDataSetChanged();
	        lv_cmt_list.onRefreshComplete();
	    }

		@Override
		protected List<JSONObject> doInBackground(Void... p) {
			page++;
			return getData();
		}
	}
}
