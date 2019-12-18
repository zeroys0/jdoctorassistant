package com.jxj.jdoctorassistant.main;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.main.doctor.personal.AddHospitalActivity;
import com.jxj.jdoctorassistant.main.doctor.userlist.DataInputActivity;
import com.jxj.jdoctorassistant.main.doctor.userlist.DataInputRecordInfoActivity;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.util.ValidateUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.TextView;

import static com.jxj.jdoctorassistant.main.doctor.personal.HospitalAddressListActivity.ADDRESSID;

public class WelcomeActivity extends Activity {
	@ViewInject(R.id.app_version_tv)
	private TextView appVersionTv;

	private SharedPreferences sp;
	private boolean isFirstRun;
	private Context context;
	private String account;
	private String password;
	private JAssistantAPIThread userLoginThread;
	private Handler handler_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		ViewUtils.inject(this);
		context = this;

		appVersionTv.setText("V"+ ValidateUtil.getVersionName(this));
		

		isFirstRun = true;

		final Intent intentMain = new Intent(this, UserFunctionActivity.class);
		final Intent intentLogin = new Intent(this, LoginActivity.class);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (isFirstRun) {
					startActivity(intentLogin);
					finish();
				} else {
					account = sp.getString("account", null);
					password = sp.getString("password", null);
					handler_login = new Handler(context.getMainLooper()) {
						public void handleMessage(Message msg) {
							if (msg.what == 0x133) {
								String result = userLoginThread.getResult();
								if (UiUtil.isResultSuccess(context, result)) {
									try {
										JSONObject json = new JSONObject(result);
										int code = json.getInt("code");
										if (code == 200) {
											UiUtil.showToast(context,
													json.getString("message"));
											startActivity(intentMain);
											finish();
										} else {
											UiUtil.showToast(context,
													json.getString("message"));
											startActivity(intentLogin);
											finish();
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}

							}

						};
					};
					userLoginThread = new JAssistantAPIThread(
							ApiConstant.DOCTORLOGIN, handler_login, context);
					userLoginThread.setAccount(account);
					userLoginThread.setPassword(password);
					try {
						userLoginThread.start();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
		};
		timer.schedule(task, 1500);
	}
}
