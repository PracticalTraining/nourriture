package cn.edu.bjtu.nourriture.ui;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.config.Constants;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;



public class SplashActivity extends BaseActivity {

	public static final String TAG = SplashActivity.class.getSimpleName();

	private ImageView mSplashItem_iv = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itau.tmall.ui.base.BaseActivity#findViewById()
	 */
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mSplashItem_iv = (ImageView) findViewById(R.id.splash_loading_item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Constants.SCREEN_DENSITY = metrics.density;
		Constants.SCREEN_HEIGHT = metrics.heightPixels;
		Constants.SCREEN_WIDTH = metrics.widthPixels;
		
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		analytics.setLocalDispatchPeriod(15);
		Tracker				my_tracker = analytics.newTracker(R.xml.app_tracker);
		
		my_tracker.setScreenName("SplashActivity");
		my_tracker.send(new HitBuilders.AppViewBuilder().build());
		mHandler = new Handler(getMainLooper());
		findViewById();
		initView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itau.jingdong.ui.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		Animation translate = AnimationUtils.loadAnimation(this,
				R.anim.splash_loading);
		translate.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				//启动homeactivty，相当于Intent
				openActivity(HomeActivity.class);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
				SplashActivity.this.finish();
			}
		});
		mSplashItem_iv.setAnimation(translate);
	}

}
