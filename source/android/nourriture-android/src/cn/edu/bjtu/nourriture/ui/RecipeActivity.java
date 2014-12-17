package cn.edu.bjtu.nourriture.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;

public class RecipeActivity extends BaseActivity {
	private LinearLayout lvCookingStep;
	private CookingStepAdapter cookingStepAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_recipe);
		findViewById();
		initView();
		setListeners();
	}

	@Override
	protected void findViewById() {
		lvCookingStep = (LinearLayout) findViewById(R.id.linearLayout_cooking_step);
	}

	@Override
	protected void initView() {
		cookingStepAdapter = new CookingStepAdapter();
		for(int i = 0;i < cookingStepAdapter.getCount();i++){
			lvCookingStep.addView(cookingStepAdapter.getView(i, null, null));
		}
	}
	
	private void setListeners(){
			
	}
	
	
	private class CookingStepAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Object getItem(int arg0) {
			return 0;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater layoutInflater = LayoutInflater.from(RecipeActivity.this);
			
			//组装数据
			convertView = layoutInflater.inflate(R.layout.item_cooking_step, null);
			
			TextView label = (TextView) convertView.findViewById(R.id.textview_label);
			
			label.setText(String.valueOf(position + 1));
			return convertView;
		}
		
	}

}
