package com.jxj.jdoctorassistant.main.userInfo;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;

public class ContactInfoActivity extends Activity {

	private Context context;
	private SharedPreferences preferences;
	private String customerId ;
	private int uId;
	private JAssistantAPIThread getContactInfoThread;
	private Handler handler_getContactInfo;
	@Bind(R.id.contact_address_txt)
	TextView contactAddressTxt;
	@Bind(R.id.contact_mobilephone_txt)
	TextView contactMobPhoneTxt;
	@Bind(R.id.contact_myemail_txt)
	TextView contactMyEmailTxt;
	@Bind(R.id.contact_myqq_txt)
	TextView contactMyQqTxt;
	@Bind(R.id.contact_otherphone_txt)
	TextView contactOtherPhoneTxt;
	@Bind(R.id.contact_phone_txt)
	TextView contactPhoneTxt;
	@Bind(R.id.contact_postcode_txt)
	TextView contactPostcodeTxt;

	@Bind(value = R.id.title_tv)
	TextView titleTv;

	@OnClick({ R.id.back_igv, R.id.right_btn_igv, })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_info);
		ButterKnife.bind(this);
		
		context = this;
		initData();
		
		
	}

	private void initData() {

		titleTv.setText("通讯信息");
		preferences = getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
		customerId=preferences.getString(AppConstant.USER_customerId, "");
		uId=preferences.getInt(AppConstant.ADMIN_userId, 0);
		handler_getContactInfo = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x133) {
					String result = getContactInfoThread.getResult();
					JSONObject json;
					try {
						json = new JSONObject(result);
						int code = json.getInt("code");
						if (code == 200) {
							String contactInfo = json.getString("Data");
							JSONObject contactJson = new JSONObject(contactInfo);
							contactAddressTxt.setText(contactJson
									.getString("Address"));
							contactMobPhoneTxt.setText(contactJson
									.getString("MobilePhone"));
							contactMyEmailTxt.setText(contactJson
									.getString("Email"));
							contactMyQqTxt.setText(contactJson
									.getString("QQNumber"));
							contactOtherPhoneTxt.setText(contactJson
									.getString("ElsePhone"));
							contactPhoneTxt.setText(contactJson
									.getString("TelPhone"));
							contactPostcodeTxt.setText(contactJson
									.getString("Postcode"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
		};
		// 获取通讯信息
		getContactInfoThread = new JAssistantAPIThread(
				ApiConstant.GETCUSTOMERCONTACTINFO, handler_getContactInfo,
				context);
		getContactInfoThread.setuId(uId);
		getContactInfoThread.setCustomerId(customerId);
		getContactInfoThread.start();
	}
}
