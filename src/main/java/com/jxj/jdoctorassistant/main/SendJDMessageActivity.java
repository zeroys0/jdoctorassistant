package com.jxj.jdoctorassistant.main;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.ExpandableListViewAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.model.JdMessage;
import com.jxj.jdoctorassistant.thread.ControlCenterServiceThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class  SendJDMessageActivity extends Activity {

	@ViewInject(R.id.title_tv)
	TextView titleTv;
	@ViewInject(R.id.send_msg_etv_01)
	EditText sendMsgEtv;
	@ViewInject(R.id.send_msg_elv)
	ExpandableListView sendMsgElv;

	private ControlCenterServiceThread sendMsgThread;
	private Context context;
	private SharedPreferences sp;
	private String customerId;
	private ExpandableListViewAdapter eAdapter;

	private boolean isLastChild = false;

	// 定义两个List用来控制Group和Child中的String;
	// private List<String> groupArray;// 组列表
	// private List<List<String>> childArray;// 子列表
	private List<JdMessage> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_jd_message);
		
	    ViewUtils.inject(this);
		initData();

	}

	@OnClick({ R.id.send_msg_btn_01, R.id.back_igv })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_msg_btn_01:
			sendMsg();
			break;
		case R.id.back_igv:
			finish();
			break;

		default:
			break;
		}
	}

	private void sendMsg() {
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String result = sendMsgThread.getResult();
				try {
					JSONObject json = JSONObject.fromObject(result);
					int code = json.getInt("code");
					String message = json.getString("message");
					UiUtil.showToast(context, message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		sendMsgThread = new ControlCenterServiceThread(context, customerId, "",
				handler);
		sendMsgThread.setMethodName(ApiConstant.SENDMSG);
		sendMsgThread.setMsg(sendMsgEtv.getText().toString().trim());
		sendMsgThread.start();
	}

	private void initData() {

		
		context = this;
		sp = getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
		customerId = sp.getString(AppConstant.USER_customerId, "");
		titleTv.setText("发送短信");
		// groupArray = new ArrayList<String>();
		// childArray = new ArrayList<List<String>>();
		list = new ArrayList<JdMessage>();

		initdate();
//		for (int i = 0; i < list.size(); i++) {
//
//			System.out.println("短信类别："+list.get(i).getName()+list.get(i).getContent().toString());
//		}
		eAdapter = new ExpandableListViewAdapter(context, list);
		sendMsgElv.setAdapter(eAdapter);

		childClick(sendMsgElv, eAdapter);
	}

	// 二级条目监听事件
	private void childClick(ExpandableListView elv,
			final ExpandableListViewAdapter adapter) {

		elv.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int arg2, int arg3, long arg4) {

				int gourpsSum = adapter.getGroupCount();
				for (int i = 0; i < gourpsSum; i++) {
					int childSum = adapter.getChildrenCount(i);
					for (int j = 0; j < childSum; j++) {

						if (i == (gourpsSum - 1)) {
							isLastChild = true;
						}

					}
				}

				Object childString = adapter.getChild(arg2, arg3);
//				System.out.println(childString + "===childString");
				sendMsgEtv.setText(childString.toString());

				return true;
			}
		});

	}

	private void initdate() {
		String[] group = getResources().getStringArray(R.array.message_class);

		// for(int i=0;i<group.length;i++){
		// JdMessage msg=new JdMessage();
		// msg.setName(group[i]);
		//
		// }
		String[] child1 = getResources().getStringArray(
				R.array.holiday_greetings);
		String[] child2 = getResources().getStringArray(
				R.array.birthday_blessing);
		String[] child3 = getResources().getStringArray(R.array.health_tips);
		String[] child4 = getResources()
				.getStringArray(R.array.weather_concern);

		JdMessage msg = new JdMessage();

		msg.setName(group[0]);
		msg.setContent(child1);
		list.add(msg);
		
		
		msg = new JdMessage();
		msg.setName(group[1]);
		msg.setContent(child2);
		list.add(msg);
		
		msg = new JdMessage();
		msg.setName(group[2]);
		msg.setContent(child3);
		list.add(msg);

		msg = new JdMessage();
		msg.setName(group[3]);
		msg.setContent(child4);
		list.add(msg);

	}

	
}
