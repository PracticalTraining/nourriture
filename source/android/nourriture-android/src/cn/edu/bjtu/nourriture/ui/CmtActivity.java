package cn.edu.bjtu.nourriture.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;


public class CmtActivity extends BaseActivity {
	private ListView lv_cmt_list;
	private CmtAdapater adapter;
	private Button score1,score2,score3,score4,score5;
	private int score;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cmt);
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
		lv_cmt_list = (ListView) findViewById(R.id.listview_cmt_list);
		score1 = (Button) findViewById(R.id.button_score1);
		score2 = (Button) findViewById(R.id.button_score2);
		score3 = (Button) findViewById(R.id.button_score3);
		score4 = (Button) findViewById(R.id.button_score4);
		score5 = (Button) findViewById(R.id.button_score5);
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
	}

	@Override
	protected void initView() {
		adapter = new CmtAdapater();
		lv_cmt_list.setAdapter(adapter);
	}

	private class CmtAdapater extends BaseAdapter{

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				holder = new ViewHolder();
				convertView = LayoutInflater.from(CmtActivity.this).inflate(R.layout.item_cmt, null);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}
		
	}
	
	private class ViewHolder{
		
	}
}
