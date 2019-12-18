package com.jxj.jdoctorassistant.main.location;

import com.jxj.jdoctorassistant.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ToolsLocationMapActivity extends Activity {

	private Context context;
	private SharedPreferences sp;

	@Bind(R.id.location_radiogroup)
	RadioGroup radioBtnGroup;
	@Bind(R.id.baidu_btn)
	RadioButton baiduBtn;
	@Bind(R.id.google_btn)
	RadioButton googleBtn;

	@OnClick({ R.id.location_backbtn })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.location_backbtn:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_tools_location_map);
		ButterKnife.bind(this);
		context = this;
		initviews();

	}

	private void initviews() {
		radioBtnGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@SuppressLint("NewApi")
					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub

						switch (arg1) {

						case R.id.baidu_btn:
							setbtnTextColor();
							baiduBtn.setTextColor(getResources().getColor(
									R.color.login_loginBtn_bgColor));
							switchFragment(0);
							break;

						case R.id.google_btn:
							setbtnTextColor();
							googleBtn.setTextColor(getResources().getColor(
									R.color.login_loginBtn_bgColor));
							switchFragment(1);
							break;

						default:
							break;
						}
					}

				});
		switchFragment(0);
	}

	public void switchFragment(int i) {
		Fragment fragment = null;

		switch (i) {
		case 0:
			fragment = new FragmentToolsBaiduMap();
			break;
		case 1:
			fragment = new FragmentToolsGoogleMap();
			break;

		default:
			break;
		}

		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
		ft.replace(R.id.location_ll, fragment);
		ft.commitAllowingStateLoss();
	}

	private void setbtnTextColor() {
		baiduBtn.setTextColor(getResources().getColor(R.color.white_bg));
		googleBtn.setTextColor(getResources().getColor(R.color.white_bg));
	}
}
