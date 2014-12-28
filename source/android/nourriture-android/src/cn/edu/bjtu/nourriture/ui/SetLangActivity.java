package cn.edu.bjtu.nourriture.ui;

import java.util.Locale;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.edu.bjtu.nourriture.AppManager;
import cn.edu.bjtu.nourriture.R;
import cn.edu.bjtu.nourriture.task.EMobileTask;
import cn.edu.bjtu.nourriture.ui.base.BaseActivity;


public class SetLangActivity extends BaseActivity {
	private RadioGroup rg_lang_group;
	private RadioButton rb_zh;
	private RadioButton rb_en;
	private RadioButton rb_sys;
	private Button ok;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set_lang);
		findViewById();
		setListeners();
	}
	

	@Override
	protected void onStart() {
		super.onStart();
		initView();
	}

	@Override
	protected void findViewById() {
		rg_lang_group = (RadioGroup) findViewById(R.id.radiogroup_lang_group);
		rb_zh = (RadioButton) findViewById(R.id.radiobutton_zh);
		rb_en = (RadioButton) findViewById(R.id.radiobutton_en);
		rb_sys = (RadioButton) findViewById(R.id.radiobutton_sys);
		ok = (Button) findViewById(R.id.login);
	}
	
	@Override
	protected void initView() {
		if(EMobileTask.getCookie("lang") == null || EMobileTask.getCookie("lang").equals("sys")){
			rb_sys.setChecked(true);
		} else if(EMobileTask.getCookie("lang").equals("zh_CN")){
			rb_zh.setChecked(true);
		} else if(EMobileTask.getCookie("lang").equals("en_US")) {
			rb_en.setChecked(true);
		}
	}
	
	private void setListeners(){
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int checkedId = rg_lang_group.getCheckedRadioButtonId();
				switch(checkedId){
					case R.id.radiobutton_sys:
						changeAppLanguage(getResources(), "sys");
						break;
					
					case R.id.radiobutton_zh:
						changeAppLanguage(getResources(), "zh_CN");
						break;
						
					case R.id.radiobutton_en:
						changeAppLanguage(getResources(), "en_US");
						break;
				}
				SetLangActivity.this.finish();
			}
		});
		
	}
	
	public void changeAppLanguage(Resources resources, String lanAtr) {
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (lanAtr.equals("zh_CN")) {
            config.locale = new Locale("zh", "CN");
            EMobileTask.addCookie("lang", "zh_CN");
        } else if (lanAtr.equals("en_US")) {
            config.locale = Locale.ENGLISH;
            EMobileTask.addCookie("lang", "en_US");
        } else {
            config.locale = Locale.getDefault();
            EMobileTask.addCookie("lang", "sys");
        }
        resources.updateConfiguration(config, dm);
        AppManager.getInstance().killAllActivity();
        Intent intent = new Intent(SetLangActivity.this,HomeActivity.class);
        intent.putExtra("currenttab", HomeActivity.TAB_PERSONAL);
        startActivity(intent);
    }
	
}
