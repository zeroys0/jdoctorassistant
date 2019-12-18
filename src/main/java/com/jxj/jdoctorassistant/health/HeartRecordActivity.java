package com.jxj.jdoctorassistant.health;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.HeartRecordAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;

public class HeartRecordActivity extends Activity {

	@Bind(value = R.id.title_tv)
	TextView titleTv;
	@Bind(R.id.heart_record_lv)
	PullToRefreshListView heartRecordLv;

	private Context context;
	private JSONArray jsonarray = new JSONArray();
	private HeartRecordAdapter adapter;
	private JAssistantAPIThread getHeartRecordThread;
	private Handler handler_getRecord;
	private SharedPreferences sp;
	private Editor editor;
	private String customerId;
	private int uId;
	private int index = 0;
	private String startTime;
	private String endTime;

	private String jwotchModel;

	@OnClick({ R.id.back_igv })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_heart_record);
		ButterKnife.bind(this);
		context = this;
		initviews();

	}

	private void initviews() {
		titleTv.setText(getResources().getString(R.string.heart_record));
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String arrayString = (String) bundle.get("arrayString");
		startTime =  bundle.getString("starttime");
		endTime = bundle.getString("endtime");
		sp = getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_PRIVATE);
		editor = sp.edit();
		customerId = sp.getString(AppConstant.USER_customerId, "");
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);
		jwotchModel = sp.getString(AppConstant.USER_jwotchModel, null);

		adapter = new HeartRecordAdapter(jsonarray, context);
		heartRecordLv.setAdapter(adapter);
		showLocation(arrayString);

		heartRecordLv.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				String label = getResources().getString(R.string.update_time)
						+ GetDate.currentTime();
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				handler_getRecord = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						if (msg.what == 0x133) {
							String result = getHeartRecordThread.getResult();
							if (UiUtil.isResultSuccess(context, result)) {
								JSONObject json = JSONObject.fromObject(result);
								int code = json.getInt("code");
								if (code == 200) {
									String arrayString = json
											.getString("DataList");
									try {
										if (json.getInt("RecordCount")< 1) {
											UiUtil.showToast(
													context,
													getResources().getString(
															R.string.show_date));
											return;
										}
										JSONArray jsonArray = JSONArray
												.fromObject(arrayString);
										for (int j = 0; j < jsonArray.size(); j++) {

											if (jsonarray.get(j).equals(
													jsonArray.get(j))) {
												UiUtil.showToast(
														context,
														getResources()
																.getString(
																		R.string.show_date));
												return;
											}
										}
										jsonarray.clear();
										for (int i = 0; i < jsonArray.size(); i++) {
											jsonarray.add(jsonArray.get(i));
											adapter.setJsonarray(jsonarray);
											adapter.notifyDataSetChanged();
										}

									} catch (Exception e) {
										e.printStackTrace();
									}
									UiUtil.showToast(context,
											json.getString("message"));
								} else {
									UiUtil.showToast(context,
											json.getString("message"));
								}
							}

						}
					}

				};

				if (jwotchModel.equals("JXJ-HM041")) {
					getHeartRecordThread = new JAssistantAPIThread(
							ApiConstant.GETHEARTRATEHM041, handler_getRecord,
							context);
				} else if (jwotchModel.equals("JXJ-HM032")) {
					getHeartRecordThread = new JAssistantAPIThread(
							ApiConstant.GETHEARTRATEHM032, handler_getRecord,
							context);
				}
				getHeartRecordThread.setuId(uId);
				getHeartRecordThread.setCustomerId(customerId);
				getHeartRecordThread.setStartTime(startTime);
				getHeartRecordThread.setEndTime(endTime);
				getHeartRecordThread.setPageIndex(0);
				getHeartRecordThread.setPageSize(10);

				try {
					getHeartRecordThread.start();
				} catch (Exception e) {

				}

				heartRecordLv.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 这里写上拉加载更多的任务
				String label = getResources().getString(R.string.update_time)
						+ GetDate.currentTime();
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				index++;

				handler_getRecord = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						if (msg.what == 0x133) {
							String result = getHeartRecordThread.getResult();
							if (UiUtil.isResultSuccess(context, result)) {
								JSONObject json = JSONObject.fromObject(result);
								int code = json.getInt("code");
								if (code == 200) {
									String arrayString = json
											.getString("DataList");
									try {
										if (json.getInt("DataList")< 1) {
											UiUtil.showToast(
													context,
													getResources().getString(
															R.string.show_date));
											return;
										}
										showLocation(arrayString);
									} catch (Exception e) {
										e.printStackTrace();
									}
//									UiUtil.showToast(context,
//											json.getString("message"));
								} else {
									UiUtil.showToast(context,
											json.getString("message"));
								}
							}

						}
					}

				};
				setQueryObject();
				heartRecordLv.onRefreshComplete();

			}

		});

	}

	// 显示加载数据
	private void showLocation(String string) {
		try {
			JSONArray jsonArray = JSONArray.fromObject(string);
			for (int i = 0; i < jsonArray.size(); i++) {
				jsonarray.add(jsonArray.get(i));
			}

		} catch (Exception e) {
			UiUtil.showToast(context,
					getResources().getString(R.string.show_date));
		}

		adapter.setJsonarray(jsonarray);
		adapter.notifyDataSetChanged();

	}

	// 设置查询数据
	private void setQueryObject() {

		if (jwotchModel.equals("JXJ-HM041")) {
			getHeartRecordThread = new JAssistantAPIThread(
					ApiConstant.GETHEARTRATEHM041, handler_getRecord, context);
		} else if (jwotchModel.equals("JXJ-HM032")) {
			getHeartRecordThread = new JAssistantAPIThread(
					ApiConstant.GETHEARTRATEHM032, handler_getRecord, context);
		}
		getHeartRecordThread.setuId(uId);
		getHeartRecordThread.setCustomerId(String.valueOf(100001189));
		getHeartRecordThread.setStartTime(startTime);
		getHeartRecordThread.setEndTime(endTime);
		getHeartRecordThread.setPageIndex(index);
		getHeartRecordThread.setPageSize(10);
		getHeartRecordThread.start();
	}

}
