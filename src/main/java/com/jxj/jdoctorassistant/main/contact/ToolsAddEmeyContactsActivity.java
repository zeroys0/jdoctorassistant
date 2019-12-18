package com.jxj.jdoctorassistant.main.contact;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.jxj.jdoctorassistant.thread.ControlCenterServiceThread;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

//import com.baidu.platform.comapi.map.C;

public class ToolsAddEmeyContactsActivity extends Activity {

	private Context context;
	private int size;
	private int id;

	private String customerId;
	private int uId;
	private SharedPreferences sp;
//	private Editor editor;

	private JAssistantAPIThread addContactThread, updateContactThread;
	private Handler handler_addContact, handler_updateContact;
	private ControlCenterServiceThread sendCmdThread;

	@ViewInject(R.id.title_tv)
	TextView titleName;
	
	@ViewInject(R.id.emey_name_edt)
	EditText nameEdt;
	@ViewInject(R.id.emey_sex_edt)
	EditText sexEdt;
	@ViewInject(R.id.emey_relations_edt)
	EditText relationEdt;
	@ViewInject(R.id.emey_phone_edt)
	EditText phoneEdt;
	@ViewInject(R.id.emey_email_edt)
	EditText emailEdt;
	@ViewInject(R.id.emey_address_edt)
	EditText addressEdt;

	@OnClick({ R.id.back_igv, R.id.ememy_save_btn })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;
		case R.id.ememy_save_btn:
			save();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tools_add_urgent);
		ViewUtils.inject(this);
		context = this;
		initviews();
	}

	private void initviews() {
		sp = getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
//		editor = sp.edit();
		customerId = sp.getString(AppConstant.USER_customerId, null);
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);
		titleName.setText(getResources().getString(R.string.add_emey_relatives));

		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		size = data.getInt("size");
		if (size == 0) {

		} else if (size == 1) {
			nameEdt.setText((CharSequence) data.get("name"));
			phoneEdt.setText((CharSequence) data.get("phone"));
			sexEdt.setText((CharSequence) data.get("sex"));
			relationEdt.setText((CharSequence) data.get("relation"));
			emailEdt.setText((CharSequence) data.get("email"));
			addressEdt.setText((CharSequence) data.get("address"));
			id = data.getInt("id");
		}

	}

	private void save() {

		if (UiUtil.isEditTextNull(nameEdt) || UiUtil.isEditTextNull(phoneEdt)
				|| UiUtil.isEditTextNull(addressEdt)
				|| UiUtil.isEditTextNull(emailEdt)
				|| UiUtil.isEditTextNull(relationEdt)
				|| UiUtil.isEditTextNull(sexEdt)) {
			UiUtil.showToast(context,
					getResources().getString(R.string.not_null));
			return;

		}
		if (size == 0) {

			handler_addContact = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);

					if (msg.what == 0x133) {
						String result = addContactThread.getResult();
						if (UiUtil.isResultSuccess(context, result)) {
							JSONObject jsonObject=JSONObject.fromObject(result);
							int code=jsonObject.getInt("code");
							if(code==200){
								sendCmd();
								finish();
							}
							UiUtil.showToast(
									context,
									jsonObject.getString("message"));


						}

					}

				}
			};

			addContactThread = new JAssistantAPIThread(
					ApiConstant.ADDURGENPEOPLE, handler_addContact, context);
			Map map = new HashMap();
			map.put("customerId", customerId);
			map.put("uName", nameEdt.getText().toString().trim());
			map.put("phone", phoneEdt.getText().toString().trim());
			map.put("sex", sexEdt.getText().toString().trim());
			map.put("relation", relationEdt.getText().toString().trim());
			map.put("email", emailEdt.getText().toString().trim());
			map.put("address", addressEdt.getText().toString().trim());
			map.put("age", "0");
			map.put("priority", 0);
			map.put("tel", " ");
			JSONObject jsonobject = JSONObject.fromObject(map);

			addContactThread.setUrgentPeople(jsonobject.toString());
			addContactThread.setuId(uId);
			addContactThread.start();

		} else if (size == 1) {

			handler_updateContact = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);

					if (msg.what == 0x133) {
						String result = updateContactThread.getResult();
						if (UiUtil.isResultSuccess(context, result)) {
							JSONObject jsonObject=JSONObject.fromObject(result);
							int code=jsonObject.getInt("code");
							if(code==200){
								sendCmd();
								finish();
							}
							UiUtil.showToast(
									context,
									jsonObject.getString("message"));


						}

					}

				}
			};

			Map map = new HashMap();
			map.put("customerId", customerId);
			map.put("uID", id);
			map.put("uName", nameEdt.getText().toString().trim());
			map.put("phone", phoneEdt.getText().toString().trim());
			map.put("sex", sexEdt.getText().toString().trim());
			map.put("relation", relationEdt.getText().toString().trim());
			map.put("email", emailEdt.getText().toString().trim());
			map.put("address", addressEdt.getText().toString().trim());
			map.put("age", "0");
			map.put("priority", 0);
			map.put("tel", " ");

			JSONObject jsonobject = JSONObject.fromObject(map);
			updateContactThread = new JAssistantAPIThread(
					ApiConstant.UPDATEURGENPEOPLE, handler_updateContact,
					context);
			updateContactThread.setuId(uId);
			updateContactThread.setUrgentPeople(jsonobject.toString());
			updateContactThread.start();

		}
	}
	
	private void sendCmd(){
		Handler sendCmdHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x134) {
					String mResult = sendCmdThread.getResult();
					if (UiUtil.isResultSuccess(context, mResult)) {
						UiUtil.showSendCmdResult(context, mResult);
					}
				}
			}
		};

		sendCmdThread=new ControlCenterServiceThread(context,customerId+"", ApiConstant.refreshrelativecontactlist, sendCmdHandler);
		sendCmdThread.start();
		
	}
}
