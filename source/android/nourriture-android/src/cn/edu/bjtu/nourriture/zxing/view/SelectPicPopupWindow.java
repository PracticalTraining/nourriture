package cn.edu.bjtu.nourriture.zxing.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import cn.edu.bjtu.nourriture.R;

public class SelectPicPopupWindow extends Activity implements OnClickListener {

	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private LinearLayout layout;
	private Intent intent;
	private String fileName;
	private final String SD_CARD_TEMP_DIR = "/nourriture/pictures";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
		intent = getIntent();
		btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);
		btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);
		btn_cancel = (Button) this.findViewById(R.id.btn_cancel);

		layout = (LinearLayout) findViewById(R.id.pop_layout);
		
		btn_cancel.setOnClickListener(this);
		btn_pick_photo.setOnClickListener(this);
		btn_take_photo.setOnClickListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != RESULT_OK){
			return;
		}
		if (requestCode == 1) {                
		     if (resultCode == Activity.RESULT_OK) {
		    	 compressImage(fileName);
		         File f = new File(fileName);
		         try {                            
		        	 Uri capturedImage = Uri.parse(
		        			 	 	 android.provider.MediaStore.Images.Media.insertImage(getContentResolver(),  
		        					 f.getAbsolutePath(), null, null)); 
		        	 f.delete();
		             intent.setData(capturedImage);
		         } catch (FileNotFoundException e) {                                          
		             e.printStackTrace();                        
		         } 
		     }
		}  
		if (data != null && data.getData()!= null)
			intent.setData(data.getData());
		if (data != null && data.getExtras() != null)
			intent.putExtras(data.getExtras());
		setResult(RESULT_OK, intent);
		finish();

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_take_photo:
			try {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				fileName = getFileName();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(fileName))); 
				startActivityForResult(intent, 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.btn_pick_photo:
			try {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 2);
			} catch (ActivityNotFoundException e) {

			}
			break;
		case R.id.btn_cancel:
			finish();
			break;
		default:
			break;
		}

	}
	
	private String getFileName() {  
        String saveDir = Environment.getExternalStorageDirectory() + SD_CARD_TEMP_DIR;  
        File dir = new File(saveDir);  
        if (!dir.exists()) {  
            dir.mkdirs(); // 创建文件夹  
        }  
        //用日期作为文件名，确保唯一性  
        Date date = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");  
        String fileName = saveDir + "/" + formatter.format(date) + ".PNG";  
  
        return fileName;  
    }
	
	private Bitmap compressImage(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
			baos.reset();//重置baos即清空baos
			image.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;//每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
	private void compressImage(String srcPath) {  
        BitmapFactory.Options newOpts = new BitmapFactory.Options();  
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;  
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空  
          
        newOpts.inJustDecodeBounds = false;  
        int w = newOpts.outWidth;  
        int h = newOpts.outHeight;  
        
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        float hh = width;//这里设置高度为800f  
        float ww = width / 2;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);  
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);  
        }  
        if (be <= 0)  
            be = 1;  
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);  
		try {
			FileOutputStream file = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, file);//压缩好比例大小后再进行质量压缩
			file.flush();
			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }  

}
