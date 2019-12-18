package com.jxj.jdoctorassistant.main.contact;

import net.sf.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

public class ToolsAddRelativesActivity extends Activity {

	private Context context;

	private String customerId;
	private int uId;
	private SharedPreferences sp;
//	private Editor editor;
	private JAssistantAPIThread addRelativesThread;
	private Handler handler_addRelatives;
	private ControlCenterServiceThread sendCmdThread;

	@ViewInject(R.id.title_tv)
	TextView titleName;
	@ViewInject(R.id.addrelatives_name_edt)
	EditText nameEdt;
	@ViewInject(R.id.addrelatives_phone_edt)
	EditText phoneEdt;
//	@ViewInject(R.id.addrelatives_position_edt)
//	EditText positionEdt;
	@ViewInject(R.id.addrelatives_position_sp)
	Spinner positionSp;

	@OnClick({ R.id.back_igv, R.id.relatives_save_btn })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;
		case R.id.relatives_save_btn:
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
		setContentView(R.layout.activity_tools_add_relatives);
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
		titleName.setText(getResources().getString(R.string.add_relatives));
		String[] dataNumber={"1","2","3","4"};
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_list_item_1, dataNumber);
		positionSp.setAdapter(dataAdapter);

	}

	private void save() {

		if (UiUtil.isEditTextNull(nameEdt) || UiUtil.isEditTextNull(phoneEdt)) {
			UiUtil.showToast(context,
					getResources().getString(R.string.not_null));
			return;

		}

		handler_addRelatives = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (msg.what == 0x133) {
					String result = addRelativesThread.getResult();
					if (UiUtil.isResultSuccess(context, result)) {
						JSONObject json = JSONObject.fromObject(result);
						UiUtil.showToast(context, json.getString("message"));
						if (json.getInt("code") == 200) {
							sendCmd();
							finish();
						}

					}

				}

			}
		};

		addRelativesThread = new JAssistantAPIThread(
				ApiConstant.SETRELATIVECONTACT, handler_addRelatives, context);
		addRelativesThread.setuId(uId);
		addRelativesThread.setCustomerId(String.valueOf(customerId));
		addRelativesThread.setPosition(positionSp.getSelectedItemPosition()+1);
		addRelativesThread.setUsername(nameEdt.getText().toString().trim());
		addRelativesThread.setPhone(phoneEdt.getText().toString().trim());
		addRelativesThread.start();

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

		sendCmdThread=new ControlCenterServiceThread(context,customerId+"", ApiConstant.refreshemergencecontactlist, sendCmdHandler);
		sendCmdThread.start();
		
	}
	
}
