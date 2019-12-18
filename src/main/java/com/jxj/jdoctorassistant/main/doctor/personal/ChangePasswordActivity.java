package com.jxj.jdoctorassistant.main.doctor.personal;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;


public class ChangePasswordActivity extends Activity {
	
	@ViewInject(value= R.id.title_tv,parentId=R.id.change_password_title)
	TextView titleTv;
	
	@OnClick({R.id.back_igv})
	private void onClick(View view){
		switch (view.getId()) {
		case R.id.back_igv:
			finish();
			break;

		default:
			break;
		}
	}

	private Context context;
	private SharedPreferences sp;
	private Editor editor;
	private int doctorId;
	private String puttingPassword, newPassword, newPassword2;
	boolean compareNP, compareOP, compareNOP;
	private DoctorSHThread resetPasswordThread;

	@ViewInject(R.id.original_password_edit)
	EditText originalPasswordEdt;
	@ViewInject(R.id.new_password_edit)
	EditText newPasswordEdt;
	@ViewInject(R.id.new_password_again_edit)
	EditText newPasswordAgainEdt;

	@OnClick({ R.id.back_igv, R.id.ok_to_change_btn })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;

		case R.id.ok_to_change_btn:
			resetPassword();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_change_password);
		ViewUtils.inject(this);
		context = this;
		initviews();

	}

	private void resetPassword() {

		// sp = getSharedPreferences("firstPasswordInfo",
		// context.MODE_WORLD_READABLE);
		// editor = sp.edit();

		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == ApiConstant.MSG_API_HANDLER) {
					String result = resetPasswordThread.getResult();
					// System.out.println("密码======" + password);
					// System.out.println(result);

					if (UiUtil.isResultSuccess(context, result)) {
						JSONObject json = JSONObject.fromObject(result);
						int code = json.getInt("code");
						if (code == 200) {

							UiUtil.showToast(
									context,
									getResources().getString(
											R.string.update_success));
							// System.out.println("========success=======");
							finish();
						} else {
							UiUtil.showToast(context, json.getString("message"));
						}

					}
				}
			}
		};

		// 开启线程之前先进行非空判断，考虑用户交互，如果只点按钮不输入内容会报错。
		newPassword = newPasswordEdt.getText().toString().trim();
		newPassword2 = newPasswordAgainEdt.getText().toString().trim();
		puttingPassword = originalPasswordEdt.getText().toString().trim();//记录输入密码
		
		

		if (StringUtils.isEmpty(newPassword)
				|| StringUtils.isEmpty(newPassword2)
				|| StringUtils.isEmpty(puttingPassword)) {
			UiUtil.showToast(context,
					getResources().getString(R.string.message_is_empty));
			return;

		}
		compareNP = newPassword.equals(newPassword2);// 输入新密码比较
//		compareOP = originalPassword.equals(puttingPassword);// 两个原密码比较
		if(!compareNP){
			UiUtil.showToast(context, getResources().getString(R.string.np_do_no_same));
			return;
		}
		
		resetPasswordThread = new DoctorSHThread(ApiConstant.UPDATESHDOCTORPASSWORD, handler, context);
		resetPasswordThread.setDoctorId(doctorId);
		resetPasswordThread.setCpwd(puttingPassword);
		resetPasswordThread.setNpwd(newPassword);
		resetPasswordThread.start();
	}

	private void initviews() {
		// TODO Auto-generated method stub
		titleTv.setText(getResources().getString(R.string.change_password));
		sp = getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
//		editor = sp.edit();
		doctorId = sp.getInt(AppConstant.USER_doctor_id, 0);
	}

}
