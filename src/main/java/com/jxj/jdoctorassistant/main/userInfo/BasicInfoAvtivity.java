package com.jxj.jdoctorassistant.main.userInfo;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class BasicInfoAvtivity extends Activity {

	private Context context;
	private Handler userDataHandler;
	private JAssistantAPIThread userDataThread;
	private String[] dataSex;
	private String[] dataIswork;
	private String[] dataIsmarried;
	private String[] dataIdtype;
	private String customerId;
	private int uId;
	private SharedPreferences preferences;
	@Bind(R.id.basic_name_txt)
	TextView nameText;
	@Bind(R.id.basic_sex_txt)
	TextView sexText;
	@Bind(R.id.basic_national_txt)
	TextView nationalText;
	@Bind(R.id.basic_birth_txt)
	TextView birthText;
	@Bind(R.id.basic_edu_txt)
	TextView eduText;
	@Bind(R.id.basic_marry_txt)
	TextView marryText;
	@Bind(R.id.basic_pro_txt)
	TextView proText;
	@Bind(R.id.basic_crad_type_txt)
	TextView cardTypeText;
	@Bind(R.id.basic_card_id_txt)
	TextView typeIdText;
	@Bind(R.id.basic_insurance_id_txt)
	TextView insuranceText;
	@Bind(R.id.basic_work_txt)
	TextView workText;
	
	@Bind(value = R.id.title_tv)
	TextView titleTv;
	
	@OnClick({ R.id.back_igv, R.id.right_btn_igv})
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
		setContentView(R.layout.activity_basic_info);
		ButterKnife.bind(this);
		context=this;
		initUserData();
	}
	/**
	 * 获取用户信息
	 */
	private void initUserData() {
		
		titleTv.setText("基本信息");
		preferences=getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
		customerId=preferences.getString(AppConstant.USER_customerId, "");
		uId=preferences.getInt(AppConstant.ADMIN_userId, 0);
		System.out.println(customerId+"====customerId=====");
		Resources res = getResources();
		dataSex = res.getStringArray(R.array.sex_array);
		dataIswork = res.getStringArray(R.array.work_array);
		dataIsmarried = res.getStringArray(R.array.marry_array);
		dataIdtype = res.getStringArray(R.array.id_type_array);
		userDataHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0x133:
					String result = userDataThread.getResult();
					try {
						JSONObject response = new JSONObject(result);
						int code=response.getInt("code");
						String message=response.getString("message");
						System.out.println(message);
						if(code==200){
							String data=response.getString("Data");
							JSONObject resultJson=new JSONObject(data);
							birthText.setText(resultJson.getString("Birthday"));
							eduText.setText(resultJson.getString("Education"));
							typeIdText.setText(resultJson.getString("IdNumber"));
							int idType = resultJson.getInt("IdType");
							cardTypeText.setText(dataIdtype[idType]);
							insuranceText.setText(resultJson
									.getString("InsuranceID"));
							int idMarried = resultJson.getInt("IsMarried");
							marryText.setText(dataIsmarried[idMarried]);
							int idWork = resultJson.getInt("IsWorking");
							workText.setText(dataIswork[idWork]);
							nameText.setText(resultJson.getString("CName"));
							nationalText.setText(resultJson.getString("National"));
							proText.setText(resultJson.getString("Professional"));
							int idSex = resultJson.getInt("Sex");
							sexText.setText(dataSex[idSex]);
						}else{
							Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			};
		};
		userDataThread = new JAssistantAPIThread(
				ApiConstant.GETCUSTOMERBASICINFO, userDataHandler, context);
		userDataThread.setuId(uId);
		userDataThread.setCustomerId(customerId);
		userDataThread.start();
	}
}
