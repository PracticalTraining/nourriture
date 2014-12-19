package cn.edu.bjtu.nourriture.ui;

import org.json.JSONArray;
import org.json.JSONException;

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

	private Button food,recipe;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search_result);
		findViewById();
		initView();
		
		intent = getIntent();
		String foodsStr = intent.getStringExtra("foods");
		String recipesStr = intent.getStringExtra("recipes");
		try {
			JSONArray foods = new JSONArray(foodsStr);
			JSONArray recipes = new JSONArray(recipesStr);
			int foodNum = foods.length();
			int recipeNum = recipes.length();
			food.setText("食物 " + foodNum +"个搜索结果");
			recipe.setText("食谱 " + recipeNum +"个搜索结果");
			if(foodNum == 0){
				food.setEnabled(false);
			}
			if(recipeNum == 0){
				recipe.setEnabled(false);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			food.setText("食物 0个搜索结果");
			recipe.setText("食谱 0个搜索结果");
			food.setEnabled(false);
			recipe.setEnabled(false);
		}
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
