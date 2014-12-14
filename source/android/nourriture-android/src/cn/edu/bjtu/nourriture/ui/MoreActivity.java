package cn.edu.bjtu.nourriture.ui;

import android.os.Bundle;
import android.view.Window;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;


public class MoreActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_more);
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

}
