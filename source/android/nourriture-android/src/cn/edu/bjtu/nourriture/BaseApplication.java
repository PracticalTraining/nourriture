package cn.edu.bjtu.nourriture;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import cn.edu.bjtu.nourriture.config.Constants;
import cn.edu.bjtu.nourriture.image.ImageLoaderConfig;


public class BaseApplication extends Application {
	private static Context context;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this;
		ImageLoaderConfig.initImageLoader(this, Constants.BASE_IMAGE_CACHE);
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	public static Context getContext() {
		return context;
	}		

}
