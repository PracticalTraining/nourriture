package cn.edu.bjtu.nourriture.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;
import cn.edu.bjtu.nourriture.utils.CommonTools;
import cn.edu.bjtu.nourriture.widgets.HomeSearchBarPopupWindow;
import cn.edu.bjtu.nourriture.widgets.HomeSearchBarPopupWindow.onSearchBarItemClickListener;
import cn.edu.bjtu.nourriture.zxing.CaptureActivity;

public class IndexActivity extends BaseActivity implements OnClickListener,
		onSearchBarItemClickListener {
	public static final String TAG = IndexActivity.class.getSimpleName();
	
	//=============中部导航栏模块=====
	private Intent mIntent;
	private HomeSearchBarPopupWindow mBarPopupWindow = null;
	private EditText mSearchBox = null;
	private ImageButton mCamerButton = null;
	private LinearLayout mTopLayout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);

		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		mSearchBox = (EditText) findViewById(R.id.index_search_edit);
		mCamerButton = (ImageButton) findViewById(R.id.index_camer_button);
		mTopLayout = (LinearLayout) findViewById(R.id.index_top_layout);
	}
	
	@Override
	protected void initView() {
		// ======= 初始化ViewPager ========

		mBarPopupWindow = new HomeSearchBarPopupWindow(this,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mBarPopupWindow.setOnSearchBarItemClickListener(this);

		mCamerButton.setOnClickListener(this);
		mSearchBox.setOnClickListener(this);

		mSearchBox.setInputType(InputType.TYPE_NULL);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.index_camer_button:
			int height = mTopLayout.getHeight()
					+ CommonTools.getStatusBarHeight(this);
			mBarPopupWindow.showAtLocation(mTopLayout, Gravity.TOP, 0, height);
			break;

		case R.id.index_search_edit:
			openActivity(SearchActivity.class);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBarCodeButtonClick() {
		mIntent=new Intent(IndexActivity.this, CaptureActivity.class);
		startActivity(mIntent);
	}

	@Override
	public void onCameraButtonClick() {
		CommonTools.showShortToast(this, "拍照购");
	}

	@Override
	public void onColorButtonClick() {
		CommonTools.showShortToast(this, "颜色购");
	}
}
