package com.jxj.jdoctorassistant.main;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.util.UiUtil;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class UserFeedbackActivity extends Activity{
	@Bind(value=R.id.title_tv)
	TextView titleTv;
	@Bind(R.id.user_feedback_et)
	EditText userFeedbackEt;
	
	@OnClick({R.id.back_igv,R.id.user_feedback_submit_btn})
	public void onClick(View view){
		switch (view.getId()) {
		case R.id.back_igv:
			finish();
			break;
		case R.id.user_feedback_submit_btn:
			if(!UiUtil.isEditTextNull(userFeedbackEt)){
				String content = "反馈信息："
						+ userFeedbackEt.getEditableText().toString()
								.trim();
				sendSMS(context, content, "18822432603");
				UiUtil.showToast(context, getResources().getString(R.string.send_success));
				finish();
			}else{
				UiUtil.showToast(context, getResources().getString(R.string.user_feedback_hint));
			}			
			break;

		default:
			break;
		}
	}
	
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_user_feedback);
		ButterKnife.bind(this);
		context=this;
		
		initView();
		
	}

	private void initView(){
		titleTv.setText(getResources().getString(R.string.user_feedback));
	}
	
	// 发送短信
		private void sendSMS(Context context, String content, String mobile) {
			SmsManager smsManager = SmsManager.getDefault();
			PendingIntent sentIntent = PendingIntent.getBroadcast(context, 0,
					new Intent(), 0);
			smsManager.sendTextMessage(mobile, null, content, sentIntent, null);
		}
}
