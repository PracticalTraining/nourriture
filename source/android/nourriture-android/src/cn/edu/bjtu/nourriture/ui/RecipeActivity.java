package cn.edu.bjtu.nourriture.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.bean.Recipe;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;

public class RecipeActivity extends BaseActivity {
	private LinearLayout lvCookingStep;
	private CookingStepAdapter cookingStepAdapter;
	private HttpUtils httpUtils;
	private BitmapUtils bitmapUtils;
	private Recipe recipe;
	private TextView tv_title;
	private ImageView iv_icon;
	private TextView tv_cusname;
	private TextView tv_des;
	private TextView tv_category;
	private TextView tv_ingredient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_recipe);
		
		httpUtils = new HttpUtils();
		bitmapUtils = new BitmapUtils(this);
		
		Intent intent = getIntent();
		recipe = new Recipe();
		try {
			JSONObject jRecipe = new JSONObject(intent.getStringExtra("recipe"));
			recipe.setId(jRecipe.getInt("id"));
			recipe.setName(jRecipe.getString("name"));
			recipe.setPicture(jRecipe.getString("picture"));
			recipe.setDescription(jRecipe.getString("description"));
			recipe.setIngredient(jRecipe.getString("ingredient"));
			recipe.setCatogeryId(jRecipe.getInt("catogeryId"));
			recipe.setCustomerId(jRecipe.getInt("customerId"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		findViewById();
		initView();
		setListeners();
	}

	@Override
	protected void findViewById() {
		lvCookingStep = (LinearLayout) findViewById(R.id.linearLayout_cooking_step);
		tv_title = (TextView) findViewById(R.id.textview_title);
		iv_icon = (ImageView) findViewById(R.id.imageview_icon);
		tv_cusname = (TextView) findViewById(R.id.textview_cusname);
		tv_des = (TextView) findViewById(R.id.textview_des);
		tv_category = (TextView) findViewById(R.id.textview_category);
		tv_ingredient = (TextView) findViewById(R.id.textview_ingredient);
	}

	@Override
	protected void initView() {
		tv_title.setText(recipe.getName());
		
		bitmapUtils.display(iv_icon, recipe.getPicture());
		
		String url = Constants.MOBILE_SERVER_URL + "customer/" + recipe.getCustomerId();
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				JSONObject jobj;
				try {
					jobj = new JSONObject(arg0.result);
					tv_cusname.setText(jobj.getJSONObject("customer").getString("name"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		tv_des.setText(recipe.getDescription());
		
		url = Constants.MOBILE_SERVER_URL + "recipeCategory/" + recipe.getCatogeryId();
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				JSONObject jobj;
				try {
					jobj = new JSONObject(arg0.result);
					tv_category.setText(jobj.getJSONObject("superiorRecipe").getString("name"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		tv_ingredient.setText(recipe.getIngredient());
		
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
