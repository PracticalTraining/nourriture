package cn.edu.bjtu.nourriture.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;


public class CategoryActivity extends BaseActivity {

	private ListView catergory_listview;
	private LayoutInflater layoutInflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mTitleValues[0] = rs.getString(R.string.food);
		mTitleValues[1] = rs.getString(R.string.recipe);
		
		setContentView(R.layout.activity_category);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		catergory_listview=(ListView)this.findViewById(R.id.catergory_listview);

		catergory_listview.setAdapter(new CatergorAdapter());
		catergory_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view, int parent,
					long id) {
				Intent intent = new Intent(CategoryActivity.this,SndCategoryActivity.class);
				intent.putExtra("type", (int) id);
				intent.putExtra("superiorCategoryId", 0);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void initView() {

	}
	
	private class CatergorAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mTitleValues.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressWarnings("null")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder=new ViewHolder();
			layoutInflater=LayoutInflater.from(CategoryActivity.this);
			
			//组装数据
			if(convertView==null){
				convertView=layoutInflater.inflate(R.layout.activity_category_item, null);
				holder.title=(TextView) convertView.findViewById(R.id.catergoryitem_title);
				//使用tag存储数据
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.title.setText(mTitleValues[position]);
			
			return convertView;
		
		}
		
		
		
	}
	
	
	//给照片添加文字显示(Title)
	private String[] mTitleValues = new String[2];		

	public static class ViewHolder {
		TextView title;
	}
	
	

}
