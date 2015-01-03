package cn.edu.bjtu.nourriture.ui;

import com.lidroid.xutils.util.LogUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;
import cn.edu.bjtu.nourriture.zxing.view.SelectPicPopupWindow;

public class AddFoodActivity extends BaseActivity {
	private ImageView iv_picture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_food);
		findViewById();
		setLisenters();
		initView();
	}

	@Override
	protected void findViewById() {
		iv_picture = (ImageView) findViewById(R.id.imageview_picture);
	}
	
	private void setLisenters(){
		iv_picture.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				int width = iv_picture.getWidth();
				int rmdHeight = width / 2;
				LinearLayout.LayoutParams lp = (LayoutParams) iv_picture.getLayoutParams();
				lp.height = rmdHeight;
				iv_picture.setLayoutParams(lp);
			}
		});
		iv_picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(AddFoodActivity.this,
						SelectPicPopupWindow.class), 1);
			}
		});
	}

	@Override
	protected void initView() {
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (resultCode) {
		case 1:
			if (data != null) {
				Uri mImageCaptureUri = data.getData();
				//LogUtils.d(mImageCaptureUri.toString());
				if (mImageCaptureUri != null) {
					Bitmap image;
					try {
						image = MediaStore.Images.Media.getBitmap(
								this.getContentResolver(), mImageCaptureUri);
						if (image != null) {
							LinearLayout.LayoutParams lp = (LayoutParams) iv_picture.getLayoutParams();
							lp.height = LayoutParams.WRAP_CONTENT;
							iv_picture.setLayoutParams(lp);
							iv_picture.setImageBitmap(image);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap image = extras.getParcelable("data");
						if (image != null) {
							LinearLayout.LayoutParams lp = (LayoutParams) iv_picture.getLayoutParams();
							lp.height = LayoutParams.WRAP_CONTENT;
							iv_picture.setLayoutParams(lp);
							iv_picture.setImageBitmap(image);
						}
					}
				}

			}
			break;
		default:
			break;

		}
	}
}
