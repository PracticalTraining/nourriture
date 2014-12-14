package cn.edu.bjtu.nourriture.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;
import cn.edu.bjtu.nourriture.utils.CommonTools;
import cn.edu.bjtu.nourriture.widgets.AutoClearEditText;


public class SearchActivity extends BaseActivity {

	private AutoClearEditText mEditText = null;
	private ImageButton mImageButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mEditText = (AutoClearEditText) findViewById(R.id.search_edit);
		mImageButton = (ImageButton) findViewById(R.id.search_button);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mEditText.requestFocus();
		mImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommonTools.showShortToast(SearchActivity.this, "亲，该功能暂未开放");
			}
		});
	}
}
