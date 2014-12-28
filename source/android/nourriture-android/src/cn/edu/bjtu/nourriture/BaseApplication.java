package cn.edu.bjtu.nourriture;

import java.util.Locale;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import cn.edu.bjtu.nourriture.config.Constants;
import cn.edu.bjtu.nourriture.image.ImageLoaderConfig;
import cn.edu.bjtu.nourriture.task.EMobileTask;


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
		Resources resources = getResources();
		Configuration config = resources.getConfiguration();
	    DisplayMetrics dm = resources.getDisplayMetrics();
	    if(EMobileTask.getCookie("lang") == null){
	    	config.locale = Locale.getDefault();
	    } else if (EMobileTask.getCookie("lang").equals("zh_CN")) {
            config.locale = new Locale("zh", "CN");
        } else if (EMobileTask.getCookie("lang").equals("en_US")) {
            config.locale = Locale.ENGLISH;
        } else {
            config.locale = Locale.getDefault();
        }
	    resources.updateConfiguration(config, dm);
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
