package com.jxj.jdoctorassistant.main.userInfo;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import com.jxj.jdoctorassistant.util.UiUtil;

public class HealthInfoActivity extends Activity {

	private Context context;
	private JAssistantAPIThread healthInfoThread;
	private Handler healthInfoHandler;
	private String customerId;
	private int uId;
	private SharedPreferences preferences;
	@Bind(R.id.health_blood_type_txt)
	TextView bloodTypeTxt;
	@Bind(R.id.health_height_txt)
	TextView heightTxt;
	@Bind(R.id.health_oxygen_txt)
	TextView oxyenTxt;
	@Bind(R.id.health_pd_txt)
	TextView pdTxt;
	@Bind(R.id.health_pr_txt)
	TextView prTxt;
	@Bind(R.id.health_ps_txt)
	TextView psTxt;
	@Bind(R.id.health_temperature_one_txt)
	TextView temperatureTxt1;
	@Bind(R.id.health_temperature_two_txt)
	TextView temperatureTxt2;
	@Bind(R.id.health_waist_txt)
	TextView waistTxt;
	@Bind(R.id.health_weight_txt)
	TextView weightTxt;
	
	@Bind(value = R.id.title_tv)
	TextView titleTv;
	
	@OnClick({ R.id.back_igv, R.id.right_btn_igv,})
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
		setContentView(R.layout.activity_healthinfo);
		ButterKnife.bind(this);
		context = this;
		
		initData();
	}

	@SuppressLint("HandlerLeak")
	private void initData() {
		
		titleTv.setText("健康信息");
		preferences = getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
		customerId=preferences.getString(AppConstant.USER_customerId, "");
		uId=preferences.getInt(AppConstant.ADMIN_userId, 0);
		healthInfoHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 0x133:
					String result = healthInfoThread.getResult();
					try {
						JSONObject responseJson=new JSONObject(result);
						int code=responseJson.getInt("code");
						if(code==200){
							JSONObject response=new JSONObject(responseJson.getString("Data"));
							bloodTypeTxt.setText(response.getString("BloodType"));
							heightTxt.setText(response.getString("Height"));
							pdTxt.setText(response.getInt("PdBlood")+"");
							psTxt.setText(""+response.getInt("PsBlood"));
							waistTxt.setText(""+response.getInt("Waist"));
							weightTxt.setText(""+response.getInt("Weight"));
						}else{
							UiUtil.showToast(context, responseJson.getString("message"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
		};
		healthInfoThread = new JAssistantAPIThread(
				ApiConstant.GETCUSTOMERHEALTHINFO, healthInfoHandler,
				context);
		healthInfoThread.setuId(uId);
		healthInfoThread.setCustomerId(customerId);
		healthInfoThread.start();
	}
}
