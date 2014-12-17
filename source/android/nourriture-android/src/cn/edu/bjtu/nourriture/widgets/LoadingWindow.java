package cn.edu.bjtu.nourriture.widgets;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.bjtu.nourriture.R;

public class LoadingWindow extends Dialog{
	private Context context;
	private ImageView mLoadingItem;
	private TextView mProgressTextView;
	private TranslateAnimation t;
	
	public LoadingWindow(Context context) {
		super(context);
		this.context = context;
	}
	
	public LoadingWindow(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);
		
		mLoadingItem = (ImageView) findViewById(R.id.splash_loading_item);
		mProgressTextView = (TextView) findViewById(R.id.textview_progress);
	
		setOnShowListener(new OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				init();
			}
		});
		setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				mLoadingItem.clearAnimation();
			}
		});
	}
	
	private void init(){
		t = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.6f, 
				Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
		t.setDuration(1000);
		t.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mLoadingItem.startAnimation(t);
			}
		});
		mLoadingItem.startAnimation(t);
	}
	
}
