package com.jxj.jdoctorassistant.main.contact;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.ControlCenterServiceThread;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts.Intents.UI;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class ToolsAddContactsActivity extends Activity {

	private Context context;
	private int size;
	private int id;

	private String customerId;
	private int uId;
	private SharedPreferences sp;

	private JAssistantAPIThread addContactThread, updateContactThread;
	private Handler handler_addContact, handler_updateContact;
	private ControlCenterServiceThread sendCmdThread;

	@Bind(R.id.title_tv)
	TextView titleName;
	@Bind(R.id.addcontact_name_edt)
	EditText nameEdt;
	@Bind(R.id.addcontact_phone_edt)
	EditText phoneEdt;

	@OnClick({ R.id.back_igv, R.id.contacts_save_btn })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;
		case R.id.contacts_save_btn:
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
		setContentView(R.layout.activity_tools_add_contacts);
		ButterKnife.bind(this);
		context = this;
		initviews();
	}

	private void initviews() {
		sp = getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
		
		customerId = sp.getString(AppConstant.USER_customerId, null);
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);
		titleName.setText(getResources().getString(R.string.add_newcontacts));

		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		size = data.getInt("size");
		if (size == 0) {

		} else if (size == 1) {
			nameEdt.setText(data.getString("name"));
			phoneEdt.setText(data.getString("phone"));
			id = data.getInt("id");
		}

	}

	private void save() {

		if (UiUtil.isEditTextNull(nameEdt) || UiUtil.isEditTextNull(phoneEdt)) {
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

							UiUtil.showToast(
									context,
									"添加成功");
							sendCmd();
							finish();

						}

					}

				}
			};

			addContactThread = new JAssistantAPIThread(
					ApiConstant.ADDCONTACT, handler_addContact, context);
			addContactThread.setuId(uId);
			addContactThread.setCustomerId(customerId);
			addContactThread.setUsername(nameEdt.getText().toString().trim());
			addContactThread.setPhone(phoneEdt.getText().toString().trim());
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

							UiUtil.showToast(
									context,
									"修改成功");
							sendCmd();
							finish();

						}

					}

				}
			};

			Map map = new HashMap();
			map.put("customerId", customerId);
			map.put("id", id);
			map.put("name", nameEdt.getText().toString().trim());
			map.put("phone", phoneEdt.getText().toString().trim());
			JSONObject jsonobject = JSONObject.fromObject(map);
			updateContactThread = new JAssistantAPIThread(
					ApiConstant.UPDATECONTACT, handler_updateContact, context);
			updateContactThread.setuId(uId);
			updateContactThread.setContactInfo(jsonobject.toString());
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

		sendCmdThread=new ControlCenterServiceThread(context,customerId+"", ApiConstant.refreshcontactlist, sendCmdHandler);
		sendCmdThread.start();
		
	}
}
