package cn.edu.bjtu.nourriture.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.task.EMobileTask;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;
import cn.edu.bjtu.nourriture.utils.ExitView;
import cn.edu.bjtu.nourriture.widgets.CustomScrollView;

import com.lidroid.xutils.util.LogUtils;

public class PersonalActivity extends BaseActivity implements OnClickListener {

	private ImageView mBackgroundImageView = null;
	private Button mLoginButton, mMoreButton, mExitButton;
	private CustomScrollView mScrollView = null;
	private Intent mIntent = null;
	private ExitView exit;
	private LinearLayout Ly_login, Ly_Other;
	private RelativeLayout Ly_personalInfo, ly_changePwd, ly_changeInfo,
			ly_setLang, ly_add_food, ly_add_recipe;
	private TextView username, jobtitle;
	private int LOGIN_CODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		findViewById();
		initView();
	}

	@Override
	protected void onStart() {
		LogUtils.d("onStart");
		super.onStart();
		if (EMobileTask.getCookie("userId") == null) {
			Ly_personalInfo.setVisibility(View.GONE);
			Ly_login.setVisibility(View.VISIBLE);
			mExitButton.setVisibility(View.GONE);
		} else {
			Ly_personalInfo.setVisibility(View.VISIBLE);
			Ly_login.setVisibility(View.GONE);
			mExitButton.setVisibility(View.VISIBLE);
			username.setText(EMobileTask.getCookie("username"));
			jobtitle.setText(EMobileTask.getCookie("idendity"));
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mBackgroundImageView = (ImageView) findViewById(R.id.personal_background_image);
		mLoginButton = (Button) findViewById(R.id.personal_login_button);
		mScrollView = (CustomScrollView) findViewById(R.id.personal_scrollView);
		mExitButton = (Button) this.findViewById(R.id.personal_exit);

		Ly_login = (LinearLayout) findViewById(R.id.login);
		Ly_personalInfo = (RelativeLayout) findViewById(R.id.personal);
		Ly_Other = (LinearLayout) findViewById(R.id.other_layout);
		username = (TextView) findViewById(R.id.username);
		jobtitle = (TextView) findViewById(R.id.jobtitle);
		ly_changePwd = (RelativeLayout) findViewById(R.id.relativelayout_change_pwd);
		ly_changeInfo = (RelativeLayout) findViewById(R.id.relativelayout_change_info);
		ly_setLang = (RelativeLayout) findViewById(R.id.relativelayout_set_lang);
		ly_add_food = (RelativeLayout) findViewById(R.id.relativelayout_add_food);
		ly_add_recipe = (RelativeLayout) findViewById(R.id.relativelayout_add_recipe);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mScrollView.setImageView(mBackgroundImageView);

		mLoginButton.setOnClickListener(this);
		mExitButton.setOnClickListener(this);
		ly_changePwd.setOnClickListener(this);
		ly_changeInfo.setOnClickListener(this);
		ly_setLang.setOnClickListener(this);
		ly_add_food.setOnClickListener(this);
		ly_add_recipe.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// CommonTools.showShortToast(PersonalActivity.this, "稍后开放");
		switch (v.getId()) {
		case R.id.personal_login_button:
			mIntent = new Intent(PersonalActivity.this, LoginActivity.class);

			startActivityForResult(mIntent, LOGIN_CODE);
			break;

		case R.id.personal_exit:

			// 实例化SelectPicPopupWindow
			exit = new ExitView(PersonalActivity.this, itemsOnClick);
			// 显示窗口
			exit.showAtLocation(
					PersonalActivity.this.findViewById(R.id.layout_personal),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

			break;

		case R.id.relativelayout_change_pwd:
			if (EMobileTask.getCookie("userId") == null) {
				startActivity(new Intent(this, LoginActivity.class));
			} else {
				startActivity(new Intent(this, ChangePwdActivity.class));
			}
			break;

		case R.id.relativelayout_set_lang:
			startActivity(new Intent(this, SetLangActivity.class));
			break;

		case R.id.relativelayout_change_info:
			String id = EMobileTask.getCookie("userId");
			if (id == null) {
				startActivity(new Intent(this, LoginActivity.class));
			} else if (EMobileTask.getCookie("idendity").equals("普通用户")) {
				startActivity(new Intent(this, EditBormalInfoActivity.class));
			} else {
				startActivity(new Intent(this, EditManuInfoActivity.class));
			}
			break;
		case R.id.relativelayout_add_food:
			if (EMobileTask.getCookie("userId") == null) {
				startActivity(new Intent(this, LoginActivity.class));
			} else if (EMobileTask.getCookie("idendity").equals("普通用户")) {
				DisplayToast("普通用户不能添加食物");
			} else {
				// 制造商添加食物
				Intent intent = new Intent(PersonalActivity.this,
						AddFoodActivity.class);
				startActivity(intent);
				// startActivity(new Intent(this,EditBormalInfoActivity.class));
			}
			break;
		case R.id.relativelayout_add_recipe:
			if (EMobileTask.getCookie("userId") == null) {
				startActivity(new Intent(this, LoginActivity.class));
			} else if (EMobileTask.getCookie("idendity").equals("普通用户")) {
				// startActivity(new Intent(this,EditBormalInfoActivity.class));
				Intent intent = new Intent(PersonalActivity.this,
						AddRecipeActivity.class);
				startActivity(intent);
			} else {
				DisplayToast("厂商不能添加食谱");
			}
			break;
		default:
			break;
		}

	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.btn_exit:
				EMobileTask.remove("userId");
				EMobileTask.remove("username");
				EMobileTask.remove("idendity");
				Ly_personalInfo.setVisibility(View.GONE);
				Ly_login.setVisibility(View.VISIBLE);
				mExitButton.setVisibility(View.GONE);
				exit.dismiss();
				break;
			case R.id.btn_cancel:
				PersonalActivity.this.dismissDialog(R.id.btn_cancel);
				break;
			default:
				break;
			}
		}
	};

}
