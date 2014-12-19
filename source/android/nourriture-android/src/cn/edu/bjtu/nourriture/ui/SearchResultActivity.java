package cn.edu.bjtu.nourriture.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;
import cn.edu.bjtu.nourriture.utils.CommonTools;


public class SearchResultActivity extends BaseActivity {

	private EditText mobile;
	private String registerNum;
	private ImageButton checkBox;
	private Button access_password, food,recipe;
	private CommonTools tools;
	private boolean flag = false;
	private Intent mIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search_result);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		food = (Button) this.findViewById(R.id.food);
		recipe = (Button) findViewById(R.id.recipe);
	}

	@Override
	protected void initView() {
		
		/**
		 * 跳转到普通注册*/
		food.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SearchResultActivity.this, SearchFoodListActivity.class));
			}
		});
		
		/**
		 * 跳转到厂商注册*/
		recipe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SearchResultActivity.this, SearchRecipeListActivity.class));
			}
		});

	}

}
