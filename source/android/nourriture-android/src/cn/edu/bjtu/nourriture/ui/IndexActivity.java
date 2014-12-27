package cn.edu.bjtu.nourriture.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseFragmentActivity;
import cn.edu.bjtu.nourriture.zxing.CaptureActivity;

public class IndexActivity extends BaseFragmentActivity implements OnClickListener{
	public static final String TAG = IndexActivity.class.getSimpleName();
	
	//=============中部导航栏模块=====
	private Intent mIntent;
	private EditText mSearchBox = null;
	private ImageButton mCamerButton = null;
	private RadioButton rb_food;
	private RadioButton rb_recipe;
	private RadioGroup rg_rmd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);

		findViewById();
		setListeners();
		initView();
	}

	@Override
	protected void findViewById() {
		mSearchBox = (EditText) findViewById(R.id.index_search_edit);
		mCamerButton = (ImageButton) findViewById(R.id.index_camer_button);
		rb_food = (RadioButton) findViewById(R.id.radiobutton_food);
		rb_recipe = (RadioButton) findViewById(R.id.radiobutton_recipe);
		rg_rmd = (RadioGroup) findViewById(R.id.radiogroup_rmd);
	}
	
	@Override
	protected void initView() {
		// ======= 初始化ViewPager ========

		mCamerButton.setOnClickListener(this);
		mSearchBox.setOnClickListener(this);

		mSearchBox.setInputType(InputType.TYPE_NULL);
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.framelayout_rmd, new RmdFoodListFragment());
		ft.commit();
	}
	
	private void setListeners(){
		rb_food.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					buttonView.setTextColor(IndexActivity.this.getResources().getColor(R.color.white));
				} else {
					buttonView.setTextColor(IndexActivity.this.getResources().getColor(R.color.black));
				}
			}
			
		});
		rb_recipe.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					buttonView.setTextColor(IndexActivity.this.getResources().getColor(R.color.white));
				} else {
					buttonView.setTextColor(IndexActivity.this.getResources().getColor(R.color.black));
				}
			}
			
		});
		rg_rmd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				FragmentManager fm = getSupportFragmentManager();
				switch (checkedId) {
				case R.id.radiobutton_food:
					FragmentTransaction ft = fm.beginTransaction();
					ft.replace(R.id.framelayout_rmd, new RmdFoodListFragment());
					ft.commit();
					break;
			
				case R.id.radiobutton_recipe:
					fm = getSupportFragmentManager();
					ft = fm.beginTransaction();
					ft.replace(R.id.framelayout_rmd, new RmdRecipeListFragment());
					ft.commit();
					break;

				default:
					break;
				}
			}
			
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.index_camer_button:
			mIntent=new Intent(IndexActivity.this, CaptureActivity.class);
			startActivity(mIntent);
			break;

		case R.id.index_search_edit:
			openActivity(SearchActivity.class);
			break;

		default:
			break;
		}
	}
}
