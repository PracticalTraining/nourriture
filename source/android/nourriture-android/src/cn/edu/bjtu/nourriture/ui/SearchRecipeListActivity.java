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
import android.widget.ListView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.util.LogUtils;


public class SearchRecipeListActivity extends BaseActivity {

	private ListView pl_refresh;
	private LayoutInflater layoutInflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search_recipe_list);
		findViewById();
		initView();
		setListeners();
	}

	@Override
	protected void findViewById() {
		pl_refresh=(ListView) this.findViewById(R.id.pl_refresh);
	}

	@Override
	protected void initView() {
		pl_refresh.setAdapter(new RecipeListAdapter());
	}
	
	private void setListeners(){
		pl_refresh.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int parent,
					long id) {
				startActivity(new Intent(SearchRecipeListActivity.this,RecipeActivity.class));
			}
		});
	}
	
	private class RecipeListAdapter extends BaseAdapter{

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
			layoutInflater=LayoutInflater.from(SearchRecipeListActivity.this);
			
			//组装数据
			if(convertView==null){
				convertView=layoutInflater.inflate(R.layout.item_recipe_list, null);
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
}
