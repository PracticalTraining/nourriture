package cn.edu.bjtu.nourriture.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.util.LogUtils;


public class FoodListActivity extends BaseActivity {

	private PullToRefreshListView pl_refresh;
	private LayoutInflater layoutInflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_food_list);
		findViewById();
		initView();
		setListeners();
	}

	@Override
	protected void findViewById() {
		pl_refresh = (PullToRefreshListView) this.findViewById(R.id.pl_refresh);
	}

	@Override
	protected void initView() {
		pl_refresh.setAdapter(new FoodListAdapter());

		// 设置PullRefreshListView上提加载时的加载提示
		pl_refresh.setMode(Mode.BOTH);
		pl_refresh.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载...");
		pl_refresh.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
		pl_refresh.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多...");

		// 设置PullRefreshListView下拉加载时的加载提示
		pl_refresh.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新...");
		pl_refresh.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
		pl_refresh.getLoadingLayoutProxy(true, false).setReleaseLabel("松开刷新...");
	}
	
	private void setListeners(){
		pl_refresh.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int parent,
					long id) {
				startActivity(new Intent(FoodListActivity.this,FoodActivity.class));
			}
		});
		pl_refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				LogUtils.i("onPullDownToRefresh");
				new GetDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				LogUtils.i("onPullUpToRefresh");
				new GetDataTask().execute();
			}

		});
	}
	
	private class FoodListAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 15;
		}

		@Override
		public Object getItem(int position) {
			return 0;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@SuppressWarnings("null")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder=new ViewHolder();
			layoutInflater=LayoutInflater.from(FoodListActivity.this);
			
			//组装数据
			if(convertView==null){
				convertView=layoutInflater.inflate(R.layout.item_food_list, null);
				//使用tag存储数据
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			
			return convertView;
		}
		
	}
			

	public static class ViewHolder {
		
	}
	
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
	    @Override
	    protected void onPostExecute(String[] result) {
	        // Call onRefreshComplete when the list has been refreshed.
	        pl_refresh.onRefreshComplete();
	        super.onPostExecute(result);
	    }

		@Override
		protected String[] doInBackground(Void... arg0) {
			return null;
		}
	}
}
