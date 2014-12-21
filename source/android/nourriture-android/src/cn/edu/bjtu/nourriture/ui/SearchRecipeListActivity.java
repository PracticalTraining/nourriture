package cn.edu.bjtu.nourriture.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.util.LogUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;


public class SearchRecipeListActivity extends BaseActivity {

	private ListView pl_refresh;
	private LayoutInflater layoutInflater;
	private List<SearchRecipeListActivity.Recipe> data;
	private RecipeListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search_recipe_list);
		
		data = new ArrayList<SearchRecipeListActivity.Recipe>();
		Intent intent = getIntent();
		String jsonStr = intent.getStringExtra("recipes");
		try {
			JSONArray jRecipes = new JSONArray(jsonStr);
			for(int i = 0;i < jRecipes.length();i++){
				JSONObject jRecipe = jRecipes.getJSONObject(i);
				SearchRecipeListActivity.Recipe recipe = new SearchRecipeListActivity.Recipe();
				recipe.name = jRecipe.getString("name");
				recipe.ingredient = jRecipe.getString("ingredient");
				recipe.url = jRecipe.getString("picture");
				data.add(recipe);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
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
		adapter = new RecipeListAdapter();
		pl_refresh.setAdapter(adapter);
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
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("null")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder=new ViewHolder();
			layoutInflater=LayoutInflater.from(SearchRecipeListActivity.this);
			
			//组装数据
			if(convertView==null){
				convertView=layoutInflater.inflate(R.layout.item_recipe_list, null);
				holder.name = (TextView) convertView.findViewById(R.id.textview_name);
				holder.ingredient = (TextView) convertView.findViewById(R.id.textview_ingredient);
				holder.icon = (ImageView) findViewById(R.id.imageview_icon);
				//使用tag存储数据
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			
			SearchRecipeListActivity.Recipe recipe = data.get(position);
			holder.name.setText(recipe.name);
			holder.ingredient.setText(recipe.ingredient);
			BitmapUtils bitmapUtils = new BitmapUtils(SearchRecipeListActivity.this);
			bitmapUtils.display(holder.icon, recipe.url);
			LogUtils.i(recipe.url);
			
			return convertView;
		}
		
	}
			

	public static class ViewHolder {
		ImageView icon;
		TextView name;
		TextView ingredient;
	}
	
	public static class Recipe{
		String url;
		String name;
		String ingredient;
	}
}
