package cn.edu.bjtu.nourriture.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.bean.Constants;
import cn.edu.bjtu.nourriture.bean.Recipe;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;
import cn.edu.bjtu.nourriture.zxing.encoding.EncodeManager;

import com.google.zxing.WriterException;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class RecipeActivity extends BaseActivity {
	private Intent mIntent;
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
	private TextView tv_step_loading;
	private Button bt_cmt;
	private Button bt_qr_code;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_recipe);
		
		httpUtils = new HttpUtils();
		bitmapUtils = new BitmapUtils(this);
		
		mIntent = getIntent();
		recipe = new Recipe();
		try {
			JSONObject jRecipe = new JSONObject(mIntent.getStringExtra("recipe"));
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
		tv_step_loading = (TextView) findViewById(R.id.textview_step_loading);
		bt_cmt = (Button) findViewById(R.id.button_cmt);
		bt_qr_code = (Button) findViewById(R.id.qr_code);
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
		
		url = Constants.MOBILE_SERVER_URL + "cookingStep/getRecipeSteps";
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("rId", String.valueOf(recipe.getId()));
		httpUtils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				JSONArray jCookingSteps;
				List<JSONObject> data = new ArrayList<JSONObject>();
				try {
					jCookingSteps = new JSONObject(arg0.result).getJSONArray("cookingSteps");
					for(int i = 0;i < jCookingSteps.length();i++){
						data.add(jCookingSteps.getJSONObject(i));
					}
					tv_step_loading.setVisibility(View.GONE);
					cookingStepAdapter = new CookingStepAdapter(data);
					for(int i = 0;i < cookingStepAdapter.getCount();i++){
						lvCookingStep.addView(cookingStepAdapter.getView(i, null, null));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void setListeners(){
		bt_cmt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecipeActivity.this,CmtActivity.class);
				startActivity(intent);
			}
		});
		bt_qr_code.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Dialog dialog = new Dialog(RecipeActivity.this, R.style.Transparent);
				dialog.setContentView(R.layout.dialog_qr_code);
				ImageView qr_code = (ImageView) dialog.findViewById(R.id.imageview_qr_code);
				try {
					JSONObject jObject = new JSONObject();
					jObject.put("type", "recipe");
					jObject.put("id", recipe.getId());
					qr_code.setImageBitmap(EncodeManager.Create2DCode(jObject.toString()));
				} catch (WriterException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				dialog.show();
			}
		});
	}
	
	
	private class CookingStepAdapter extends BaseAdapter{
		private List<JSONObject> data;
		
		public CookingStepAdapter(List<JSONObject> data) {
			super();
			this.data = data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			return data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater layoutInflater = LayoutInflater.from(RecipeActivity.this);
			
			JSONObject jCookingStep = data.get(position);
			//组装数据
			convertView = layoutInflater.inflate(R.layout.item_cooking_step, null);
			
			TextView label = (TextView) convertView.findViewById(R.id.textview_label);
			ImageView icon = (ImageView) convertView.findViewById(R.id.imageview_icon);
			TextView des = (TextView) convertView.findViewById(R.id.textview_des);
			
			label.setText(String.valueOf(position + 1));
			try {
				bitmapUtils.display(icon,jCookingStep.getString("picture"));
				des.setText(jCookingStep.getString("description"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return convertView;
		}
		
	}

}
