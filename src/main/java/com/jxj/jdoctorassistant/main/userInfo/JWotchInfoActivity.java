package com.jxj.jdoctorassistant.main.userInfo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;

public class JWotchInfoActivity extends Activity {
	private Context context;
	private JAssistantAPIThread getJWotchThread;
	private Handler handler_getJWotch;
	private SharedPreferences preferences ;
	private String customerId ;
	private int uId;
	
	@ViewInject(R.id.jwotch_id_tv)
	TextView idTv;
	@ViewInject(R.id.jwotch_phone_number_tv)
	TextView phoneNumberTv;
	@ViewInject(R.id.jwotch_product_model_tv)
	TextView productModelTv;
	@ViewInject(R.id.jwotch_serial_number_tv)
	TextView serialNumberTv;
	@ViewInject(R.id.jwotch_status_tv)
	TextView statusTv;

	@ViewInject(value = R.id.title_tv)
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
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_jwotch_info);
		ViewUtils.inject(this);
		context = this;
		initview();

	}

	private void initview() {
		titleTv.setText("腕表信息");
		preferences = getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
		customerId=preferences.getString(AppConstant.USER_customerId, "");
		uId=preferences.getInt(AppConstant.ADMIN_userId, 0);
		handler_getJWotch = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0x133) {
					String result = getJWotchThread.getResult();
					if(UiUtil.isResultSuccess(context,result)){
						try {
							JSONObject json = JSONObject.fromObject(result);
							int code=json.getInt("code");
							if(code==200){
								String data=json.getString("Data");
								JSONObject obj=JSONObject.fromObject(data);

								idTv.setText(obj.getString("JWotchID"));
								phoneNumberTv.setText(obj.getString("PhoneNumber"));
								productModelTv.setText(obj.getString("ProductModel"));
								serialNumberTv.setText(obj.getString("SerialNumber"));
								int status=obj.getInt("Status");
								statusTv.setText(status==1?"启用":"停用");
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		
		getJWotchThread = new JAssistantAPIThread(ApiConstant.GETJWOTCH, handler_getJWotch, context);
		getJWotchThread.setuId(uId);
		getJWotchThread.setCustomerId(customerId);
		getJWotchThread.start();
	}
}
