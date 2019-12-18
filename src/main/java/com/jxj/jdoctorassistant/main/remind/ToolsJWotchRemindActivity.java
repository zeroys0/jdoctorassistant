package com.jxj.jdoctorassistant.main.remind;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.ToolsRemindSetAdapter;
import com.jxj.jdoctorassistant.adapter.ToolsRemindSetAdapter.ToolsRemindSetDelete;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.ControlCenterServiceThread;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONArray;
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
import android.widget.ListView;
import android.widget.TextView;

public class ToolsJWotchRemindActivity extends Activity implements
		ToolsRemindSetDelete {

	private Context context;
	// 定时提醒线程
	private JAssistantAPIThread getRemindList, deleteRemind;
	private ControlCenterServiceThread sendCmdThread;

	private Handler  handler_getRemindlist, handler_deleteRemind;

	private String customerId;
	private int uId;
	private SharedPreferences sp;
//	private Editor editor;
	private ToolsRemindSetAdapter adapter;
	public static ToolsJWotchRemindActivity instance = null;
	private JSONArray remindJsonArr = null;
	@Bind(R.id.title_tv)
	TextView titleName;

	@Bind(R.id.remind_lv)
	ListView remindLv;
	

	@OnClick({ R.id.back_igv, R.id.remind_add_btn })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;
		case R.id.remind_add_btn:
			Intent intent = new Intent(context,
					ToolsJWotchRemindSetActivity.class);
			intent.putExtra("intentSize", 1);
			startActivity(intent);

			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tools_jwotch_remind);
		ButterKnife.bind(this);
		context = this;
		initviews();
		instance = this;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.setmDelegate(this);
		refresh();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		adapter.setmDelegate(null);
	}

	private void initviews() {
		titleName
				.setText(getResources().getString(R.string.jwotch_remind_list));
		sp = getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
		customerId=sp.getString(AppConstant.USER_customerId, "");
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);
		
		refresh();
		adapter = new ToolsRemindSetAdapter(context);
		remindLv.setAdapter(adapter);
		remindLv.setDividerHeight(0);
		
	}

	// 更新获取
	public void refresh() {
		
		
		handler_getRemindlist = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 0x133) {
					String result = getRemindList.getResult();
					remindJsonArr=null;
					if (UiUtil.isResultSuccess(context, result)) {

						System.out.println("提醒返回结果："+result);
						JSONObject json=JSONObject.fromObject(result);
						int code=json.getInt("code");
						if(code==200){
							String data=json.getString("Data");
							remindJsonArr = JSONArray.fromObject(data);
						}else{
							String message=json.getString("message");
							UiUtil.showToast(context, message);
						}
						



					}
					adapter.setJsonArray(remindJsonArr);
					adapter.notifyDataSetChanged();
				}

			}

		};

		getRemindList = new JAssistantAPIThread(ApiConstant.GETREMINDLIST,
				handler_getRemindlist, context);
		getRemindList.setuId(uId);
		getRemindList.setCustomerId(customerId);
		try {
			getRemindList.start();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 删除列表
	@Override
	public void deleteItem(int id) {
		handler_deleteRemind = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (msg.what == 0x133) {
					String mResult = deleteRemind.getResult();
					System.out.println("删除结果："+mResult);
					if (UiUtil.isResultSuccess(context, mResult)) {
						JSONObject json=JSONObject.fromObject(mResult);
						int code=json.getInt("code");
						if(code==200){
							refresh();
							SynRemind();
						}else{
							String message=json.getString("message");
							UiUtil.showToast(context, message);
						}
						
						
					}
				}

			}
		};

		deleteRemind = new JAssistantAPIThread(ApiConstant.DELETEREMIND,
				handler_deleteRemind, context);
		deleteRemind.setuId(uId);
		deleteRemind.setDataId(String.valueOf(id));
		try {
			deleteRemind.start();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// 修改列表
	@Override
	public void updateItem(int position, int id) {
		JSONObject jsonObject = remindJsonArr.getJSONObject(position);
		Intent intent = new Intent(context, ToolsJWotchRemindSetActivity.class);
		intent.putExtra("intentSize", 0);
		intent.putExtra("title", jsonObject.getString("Title"));
		String time=GetDate.convertTimeOrDate(jsonObject.getString("Hour"))+":"+GetDate.convertTimeOrDate(jsonObject.getString("Minute"));
		intent.putExtra("time",time);
		intent.putExtra("id", jsonObject.getInt("Id"));
		intent.putExtra("cycle", jsonObject.getInt("Type"));
		startActivity(intent);

	}

	private void SynRemind(){

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
		sendCmdThread = new ControlCenterServiceThread(context,String
				.valueOf(customerId), ApiConstant.refreshremindlist,
				sendCmdHandler);
		try {
			sendCmdThread.start();
			// threadOneKey.join();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
