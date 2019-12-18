package com.jxj.jdoctorassistant.health;

import net.sf.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.ControlCenterServiceThread;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.DateDialog;

public class HeartRateActivity extends Activity {

	private Context context;
	private NumberPicker mPicker;
	private int heartTime;
	private JAssistantAPIThread getHeartRecordThread,
			getSamplingPeriodThread, setSamplingPeriodThread;
	private ControlCenterServiceThread sendCmdThread;
//	private Handler handler_setSamPeriod;
	private SharedPreferences sp;
//	private Editor editor;
	private String customerId;
	private int uId;
	public static HeartRateActivity instance;
	// private int index = 0;
	private String jwotchModel;
	private DateDialog dateDialog;

	@Bind(value = R.id.title_tv)
	TextView titleTv;
	@Bind(value = R.id.right_btn_igv)
	ImageView recordIgv;
	@Bind(R.id.hr_interval_tv)
	TextView heartTimeSet;
	@Bind(R.id.hr_start_date_tv)
	TextView startDateTv;
	@Bind(R.id.hr_end_date_tv)
	TextView endDateTv;

	@OnClick({ R.id.back_igv, R.id.right_btn_igv, R.id.hr_set_interval_rl,
			R.id.hr_start_date_rl, R.id.hr_end_date_rl, R.id.hr_query_btn,
			R.id.heartrate_monitor_rl })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;
		case R.id.right_btn_igv:
			Intent intent = new Intent(new Intent(context, ChartActivity.class));
			intent.putExtra("type", AppConstant.CHART_HR);
			startActivity(intent);
			break;
		case R.id.hr_set_interval_rl:
			setHeartTimeSet();
			break;
		case R.id.hr_start_date_rl:
			setStartDate();
			break;
		case R.id.hr_end_date_rl:
			setEndDate();
			break;
		case R.id.hr_query_btn:
			query();
			break;
		case R.id.heartrate_monitor_rl:
			sendCmd(ApiConstant.heartratemonitor);
			break;
		default:
			break;
		}
	}

	private void sendCmd(String action) {
		// TODO Auto-generated method stub

		Handler sendCmdHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x134) {
					String mResult = sendCmdThread.getResult();
					if (UiUtil.isResultSuccess(context, mResult)) {
						UiUtil.showSendCmdResult(context, mResult);
					}
//					 queryHeartRateRecord();
				}
			}
		};
		sendCmdThread = new ControlCenterServiceThread(context,
				customerId + "", action, sendCmdHandler);
		sendCmdThread.setMethodName(ApiConstant.SENDCMD);
		sendCmdThread.start();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_heart_rate);
		ButterKnife.bind(this);
		context = this;
		initviews();
		instance = this;
	}

	private void initviews() {

		sp = getSharedPreferences(AppConstant.USER_sp_name,
				Context.MODE_PRIVATE);
//		editor = sp.edit();
		customerId = sp.getString(AppConstant.USER_customerId, null);
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);
		jwotchModel = sp.getString(AppConstant.USER_jwotchModel, null);
		System.out.println("************" + jwotchModel);
		titleTv.setText(getResources().getString(R.string.measure_pulse_txt));

		recordIgv.setImageResource(R.drawable.line_chart);

		startDateTv.setText(GetDate.lastDay());
		endDateTv.setText(GetDate.lastDay());

		// boolean isFirst=sp.getBoolean("heart_isfirst", true);
		// if (isFirst) {
		// GuideUtil guideUtil = null;
		// guideUtil = GuideUtil.getInstance();
		// guideUtil.initGuide(this, R.drawable.heart_rate_ins);
		// }
		//
		// editor.putBoolean("heart_isfirst", false);
		// editor.commit();

		getHRinterval();

	}

	private void getHRinterval() {
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {

				if (msg.what == 0x133) {
					String result = getSamplingPeriodThread.getResult();
					if (UiUtil.isResultSuccess(context, result)) {
						JSONObject json = JSONObject.fromObject(result);
						int code = json.getInt("code");
						if (code == 200) {
							int period = json.getInt("SamplingPeriod");
							heartTimeSet.setText(period + "");
						}
					}
				}

			};
		};

		getSamplingPeriodThread = new JAssistantAPIThread(
				ApiConstant.GETSAMPLINGPERIOD, handler, context);
		getSamplingPeriodThread.setuId(uId);
		getSamplingPeriodThread.setCustomerId(customerId);
		getSamplingPeriodThread.start();
	}

	@SuppressLint("NewApi")
	private void setHeartTimeSet() {
		View viewTimeSet = LayoutInflater.from(context).inflate(
				R.layout.view_number_picker, null);

		mPicker = (NumberPicker) viewTimeSet.findViewById(R.id.numpicker);
		mPicker.setMinValue(1);
		mPicker.setMaxValue(30);
		mPicker.setValue(heartTime);

		AlertDialog mAlertDialog = new AlertDialog.Builder(context)
				.setTitle(getResources().getString(R.string.heart_title))
				.setView(viewTimeSet)
				.setPositiveButton(getResources().getString(R.string.mycancel),
						null)
				.setNegativeButton(getResources().getString(R.string.ok),
						new ok()).create();
		mAlertDialog.show();
	}

	@SuppressLint("NewApi")
	private class ok implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			int value = mPicker.getValue();
			heartTimeSet.setText(value + "");

			Handler handler = new Handler() {
				public void handleMessage(Message msg) {

					if (msg.what == 0x133) {
						String result = setSamplingPeriodThread.getResult();
						if (UiUtil.isResultSuccess(context, result)) {
							JSONObject json = JSONObject.fromObject(result);
							int code = json.getInt("code");
							if (code == 200) {
								UiUtil.showToast(context,
										json.getString("message"));
								sendCmd(ApiConstant.openheartrate);
							}
						}
					}

				};
			};

			setSamplingPeriodThread = new JAssistantAPIThread(
					ApiConstant.SETSAMPLINGPERIOD, handler, context);
			setSamplingPeriodThread.setuId(uId);
			setSamplingPeriodThread.setCustomerId(customerId);
			setSamplingPeriodThread.setPeriod(value);
			setSamplingPeriodThread.start();
		}

	}

	private void setStartDate() {
		Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==AppConstant.MSG_DATEDIALOG){
					startDateTv.setText(dateDialog.getDate());
				}
			}
		};
		dateDialog = new DateDialog(context,handler);
		dateDialog.setDate();
	}

	private void setEndDate() {
		Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what==AppConstant.MSG_DATEDIALOG){
					endDateTv.setText(dateDialog.getDate());
				}
			}
		};
		dateDialog= new DateDialog(context,handler);
		dateDialog.setDate();
	}

	public void query() {

		final String startTime = startDateTv.getText().toString().trim() + " "
				+ "00:00:00";
		final String endTime = endDateTv.getText().toString().trim() + " "
				+ "23:59:59";
		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (msg.what == 0x133) {
					String result = getHeartRecordThread.getResult();
					System.out.println("041心率数据："+result);
					if (UiUtil.isResultSuccess(context, result)) {
						JSONObject json = JSONObject.fromObject(result);
						int code = json.getInt("code");
						if (code == 200) {

							String arrayString = json.getString("DataList");
							try {
								if (json.getInt("RecordCount")< 1) {
									UiUtil.showToast(context, getResources()
											.getString(R.string.show_date));
									return;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

							Intent intent = new Intent(context,
									HeartRecordActivity.class);
							intent.putExtra("arrayString", arrayString);
							intent.putExtra("starttime", startTime);
							intent.putExtra("endtime", endTime);
							startActivity(intent);

						} else {
							UiUtil.showToast(context, json.getString("message"));
						}
					}
				}

			}
		};
//		System.out.println("************" + jwotchModel);
		// if (jwotchModel.equals("JXJ-HM041")) {
		getHeartRecordThread = new JAssistantAPIThread(
				ApiConstant.GETHEARTRATEHM041, handler, context);
		// } else if (jwotchModel.equals("JXJ-HM032")) {
		// getHeartRecordThread = new iPreCareServiceThread(
		// AppConstant.GETHEARTRATEHM032, handler, context);
		// }
		getHeartRecordThread.setuId(uId);
		getHeartRecordThread.setCustomerId(customerId);
		getHeartRecordThread.setStartTime(startTime);
		getHeartRecordThread.setEndTime(endTime);
		getHeartRecordThread.setPageIndex(0);
		getHeartRecordThread.setPageSize(10);

		try {
			getHeartRecordThread.start();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
}
