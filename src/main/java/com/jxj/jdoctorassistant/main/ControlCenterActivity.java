package com.jxj.jdoctorassistant.main;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.ControlCenterAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.model.ControlCenterItem;
import com.jxj.jdoctorassistant.thread.ControlCenterServiceThread;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONObject;


public class ControlCenterActivity extends Activity {

	private Context context;
	private String[] arrayString;
	private String customerId;
	private int uId;
	private SharedPreferences preferences;
	private ControlCenterServiceThread controlCenterThread;
	private Handler controlCenterHandler;

	@Bind(R.id.control_lv)
	ListView controlLv;
	@Bind(R.id.title_tv)
	TextView titleTv;

	@OnClick(R.id.back_igv)
	public void onclick(View view) {
		finish();
	}

	@OnItemClick(R.id.control_lv)
	public void onItemClick(View view, int position, long id) {

			String[] cmdArray = getResources().getStringArray(
					R.array.control_center_array_cmd);
			controlCenterThread = new ControlCenterServiceThread(context,
					customerId, cmdArray[position], controlCenterHandler);
			controlCenterThread.setMethodName(ApiConstant.SENDCMD);
			controlCenterThread.setUserId(uId);
			controlCenterThread.start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_center);
		ButterKnife.bind(this);
		context = this;
		titleTv.setText("控制面板");
		init();
	}

	@SuppressLint("HandlerLeak")
	private void init() {
		preferences = getSharedPreferences(AppConstant.USER_sp_name,
				MODE_PRIVATE);
		customerId = preferences.getString(AppConstant.USER_customerId, null);
		uId=preferences.getInt(AppConstant.ADMIN_userId,0);
		List<ControlCenterItem> list = new ArrayList<>();
		arrayString = getResources().getStringArray(
				R.array.control_center_array);
		for (int i = 0; i < arrayString.length; i++) {
			ControlCenterItem item = new ControlCenterItem();
			List<Object> resourceList = initResouceList();
			item.setItemString(arrayString[i]);
			item.setResourceId((int) resourceList.get(i));
			list.add(item);
		}
		ControlCenterAdapter adapter = new ControlCenterAdapter(context,
				R.layout.lv_item_control_center, list);
		controlLv.setAdapter(adapter);
		controlCenterHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ApiConstant.MSG_SEND_API_HANDLER:
					try {
						String result=controlCenterThread.getResult();
						if(UiUtil.isResultSuccess(context,result)){
							JSONObject jsonObject=JSONObject.fromObject(result);
							String message=jsonObject.getString("message");
							UiUtil.showToast(context,message);
						}else {
							UiUtil.showNetworkException(context);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
				}
			}
		};
	}

	private List<Object> initResouceList() {
		List<Object> resourceList = new ArrayList<>();
		resourceList.add(R.drawable.jdoctor_01_alarm);
		resourceList.add(R.drawable.jdoctor_02_autocallback);
		resourceList.add(R.drawable.jdoctor_03_download_remind_list);
		resourceList.add(R.drawable.jdoctor_04_downloadcontact);
		resourceList.add(R.drawable.jdoctor_05_opengps);
		resourceList.add(R.drawable.jdoctor_06_restart);
		resourceList.add(R.drawable.jdoctor_07_synch_autostartstoptime);
		resourceList.add(R.drawable.jdoctor_08_synch_base_info);
		resourceList.add(R.drawable.jdoctor_09_synch_healthnumber);
		resourceList.add(R.drawable.jdoctor_10_synchrelatives_contact);
		resourceList.add(R.drawable.jdoctor_11_synch_sport_plan);
		resourceList.add(R.drawable.jdoctor_12_open_heartrate);
		resourceList.add(R.drawable.jdoctor_13_synch_heartrate_period);
		resourceList.add(R.drawable.jdoctor_14_open_bloodoxygen);
		resourceList.add(R.drawable.jdoctor_15_synch_bloodoxygen_period);
		resourceList.add(R.drawable.jdoctor_16_open_bloodpressure);
		resourceList.add(R.drawable.jdoctor_17_synch_bloodpressure_period);
		return resourceList;
	}
}
