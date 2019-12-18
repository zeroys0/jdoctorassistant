package com.jxj.jdoctorassistant.main.sports;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.ToolsSportSetAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ToolsJwotchSportActivity extends Activity implements
		ToolsSportSetAdapter.ToolsSportSetDelete {

	private Context context;

	@ViewInject(R.id.title_tv)
	TextView titleName;
	@ViewInject(R.id.right_btn_igv)
	ImageView recordIgv;

	@ViewInject(R.id.sport_lv)
	ListView listview;

	private ToolsSportSetAdapter adapter;
	private int customerId;
	private int uId;
	private SharedPreferences sp;
//	private Editor editor;

	private JAssistantAPIThread getSportListThread, deleteSportListThread;
	private Handler handler_getSport, handler_deleteSport;
	private JSONArray jsonarray=new JSONArray();

	@OnClick({ R.id.back_igv, R.id.sprot_add_btn, R.id.right_btn_igv })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;

		case R.id.sprot_add_btn:
			Intent intent = new Intent(context, ToolsJWotchSportsSetActivity.class);
			intent.putExtra("size", 0);
			if(jsonarray!=null){
				intent.putExtra("array",jsonarray.toString());
			}
			startActivity(intent);

			break;

		case R.id.right_btn_igv:
			startActivity(new Intent(context, ToolsSportsRecordActivity.class));

			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tools_jwotch_sport);
		context = this;
		ViewUtils.inject(this);
		initviews();
	}

	@SuppressLint("NewApi")
	private void initviews() {

		titleName.setText(getResources().getString(
				R.string.sport_jwotch_set_list));
		recordIgv.setImageResource(R.drawable.record);
		sp = getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
//		editor = sp.edit();
		customerId = Integer.parseInt(sp.getString(AppConstant.USER_customerId, "0"));
		uId=sp.getInt(AppConstant.ADMIN_userId,0);
		adapter = new ToolsSportSetAdapter(context);
		listview.setAdapter(adapter);
		listview.setDividerHeight(0);
		// 更新数据
//		refresh();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.setmDelegate(this);
		refresh();
	}

	private void refresh() {

		handler_getSport = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 0x133) {
					String result = getSportListThread.getResult();
					if (UiUtil.isResultSuccess(context, result)) {
						JSONObject jsonObject = JSONObject.fromObject(result);
						int code=jsonObject.getInt("code");
						if(code==200){
							String str=null;
							System.out.println();
							if(jsonObject.containsKey("Data")){
								str=jsonObject.getString("Data");
							}else if(jsonObject.containsKey("sportPlanList")){
								str=jsonObject.getString("sportPlanList");
							}else {
								str="[]";
							}
//							String str = jsonObject.getString("Data");
							jsonarray = JSONArray.fromObject(str);
						}else {
							UiUtil.showToast(context,jsonObject.getString("message"));
							jsonarray=null;
						}

					}
					adapter.setJsonArray(jsonarray);
					adapter.notifyDataSetChanged();
				}

			}

		};

		getSportListThread = new JAssistantAPIThread(
				ApiConstant.GETSPORTPLANLIST, handler_getSport, context);
		getSportListThread.setuId(uId);
		getSportListThread.setCustomerId(String.valueOf(customerId));
		getSportListThread.start();
	}

	@Override
	public void deleteItem(int id) {

		handler_deleteSport = new Handler() {
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 0x133) {
					String result = deleteSportListThread.getResult();
					if (UiUtil.isResultSuccess(context, result)) {

						JSONObject json = JSONObject.fromObject(result);
						UiUtil.showToast(context, json.getString("message"));
						if (json.getInt("code") == 200) {
							refresh();
						}

					}

				}

			}
		};

		deleteSportListThread = new JAssistantAPIThread(
				ApiConstant.DELETESPORTPLAN, handler_deleteSport, context);
		deleteSportListThread.setCustomerId(String.valueOf(customerId));
		deleteSportListThread.setId(id);
		deleteSportListThread.start();

	}

	@Override
	public void updateItem(int position, int id) {

		JSONObject jsonObject = jsonarray.getJSONObject(position);
		System.out.println("运动监测修改:"+jsonObject.toString());
		Intent intent = new Intent(context, ToolsJWotchSportsSetActivity.class);
		intent.putExtra("size", 1);
		intent.putExtra("start", GetDate.convertTimeOrDate(jsonObject.getString("StartTimeHour")) + ":"
				+ GetDate.convertTimeOrDate(jsonObject.getString("StartTimeMinute")));
		intent.putExtra("end", GetDate.convertTimeOrDate(jsonObject.getString("StopTimeHour")) + ":"
				+ GetDate.convertTimeOrDate(jsonObject.getString("StopTimeMinute")));
		intent.putExtra("id", jsonObject.getInt("id"));
		intent.putExtra("status", jsonObject.getInt("Status"));
		intent.putExtra("cycle",jsonObject.getInt("CycleType"));
		intent.putExtra("date",jsonObject.containsKey("UpdateDate")?jsonObject.getString("UpdateDate"):GetDate.currentDate());
		intent.putExtra("array",jsonarray.toString());
		startActivity(intent);

	}
}
