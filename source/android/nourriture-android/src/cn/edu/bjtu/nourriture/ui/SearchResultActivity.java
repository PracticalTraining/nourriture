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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search_result);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
//		mobile = (EditText) this.findViewById(R.id.edit_mobile);
//		checkBox = (ImageButton) this.findViewById(R.id.checkBox);
//		access_password = (Button) this.findViewById(R.id.access_password);
		food = (Button) this.findViewById(R.id.food);
		recipe = (Button) findViewById(R.id.recipe);
	}

	@Override
	protected void initView() {
//		tools = new CommonTools();
//		registerNum = mobile.getText().toString();
//		// 判断是否是手机
//		tools.isMobileNO(registerNum);
//		if (flag == false) {
//			DisPlay("您输入的手机号不合法");
//		}
//
//		checkBox.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if (flag == false) {
//					access_password.setTextColor(Color.BLACK);
//					flag = true;
//				}
//
//				else {
//					access_password.setTextColor(Color.WHITE);
//				}
//
//			}
//		});
		
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
