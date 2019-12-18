package com.jxj.jdoctorassistant.health;

import net.sf.json.JSONArray;
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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.BloodMeasureRecordAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.ControlCenterServiceThread;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.DateDialog;

public class BloodRecordActivity extends Activity {

	private NumberPicker mPicker;
	private int heartTime;
	@Bind(value = R.id.title_tv)
	TextView titleTv;
	@Bind(value = R.id.right_btn_igv)
	ImageView recordIgv;
	@Bind(R.id.recode_date_ll)
	LinearLayout recodeDateLl;
	@Bind(R.id.blood_date_tv)
	TextView bloodDateTv;
	@Bind(R.id.measure_recode_lv)
	ListView listview;
	@Bind(R.id.br_interval_tv)
	TextView brIntervalTv;
	@Bind(R.id.br_set_interval_rl)
	RelativeLayout setIntervalRl;

	@OnClick({ R.id.back_igv, R.id.right_btn_igv, R.id.blood_query_btn,R.id.bloodrecord_monitor_rl })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;
		case R.id.right_btn_igv:
			Intent intent = new Intent(new Intent(context, ChartActivity.class));
			intent.putExtra("type", AppConstant.CHART_BP);
			startActivity(intent);
			break;

		case R.id.blood_query_btn:
			query();
			break;


		case R.id.bloodrecord_monitor_rl:
			sendCmd(ApiConstant.bloodpressuremonitor);
			break;
		default:
			break;
		}
	}

	private JAssistantAPIThread thread, setBloodPressuperiodThread,
			getBloodPressuperiodThread;
	private ControlCenterServiceThread sendCmdThread;
	private Handler handler_getrecord;

	private Context context;
	private SharedPreferences sp;
//	private Editor editor;
	private String customerId;
	private String mProductModel;
	private int uId;
	private BloodMeasureRecordAdapter adapter;
	private JSONArray jsonarray = new JSONArray();
	private DateDialog dateDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_blood_record);
		context = this;
		ButterKnife.bind(this);
		initviews();
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
				.setTitle(getResources().getString(R.string.blood_title))
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
			brIntervalTv.setText(value + "");

			Handler handler = new Handler() {
				public void handleMessage(Message msg) {

					if (msg.what == 0x133) {
						String result = setBloodPressuperiodThread.getResult();
						if (UiUtil.isResultSuccess(context, result)) {
							JSONObject json = JSONObject.fromObject(result);
							int code = json.getInt("code");
							if (code == 200) {
								UiUtil.showToast(context,
										json.getString("message"));
								sendCmd(ApiConstant.openbloodpressure);
							}else {
								String message=json.getString("message");
								UiUtil.showToast(context,message);
							}
						}
					}

				};
			};

			setBloodPressuperiodThread = new JAssistantAPIThread(
					ApiConstant.SETBLOODPRESSUREPERIOD, handler, context);
			setBloodPressuperiodThread
					.setCustomerId(customerId);
			setBloodPressuperiodThread.setPeriod(value);
			setBloodPressuperiodThread.setuId(uId);
			setBloodPressuperiodThread.start();

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
					// queryHeartRateRecord();
				}
			}
		};
		sendCmdThread = new ControlCenterServiceThread(context,
				customerId, action, sendCmdHandler);
		sendCmdThread.setMethodName(ApiConstant.SENDCMD);
		sendCmdThread.start();

	}

	@SuppressLint("NewApi")
	private void initviews() {
//		sp = getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
		
		sp=getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
//		editor = sp.edit();
		customerId=sp.getString(AppConstant.USER_customerId, "");
		mProductModel = sp.getString(AppConstant.USER_jwotchModel, null);
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);
		titleTv.setText(getResources().getString(R.string.blood_measure_record));
		recordIgv.setImageResource(R.drawable.line_chart);

		bloodDateTv.setText(GetDate.lastDay());

		if(mProductModel.equals(AppConstant.JWOTCHMODEL_041)){
			setIntervalRl.setVisibility(View.VISIBLE);
			setIntervalRl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					setHeartTimeSet();
				}
			});
		}



		recodeDateLl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Handler handler=new Handler(){
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						if(msg.what==AppConstant.MSG_DATEDIALOG){
							bloodDateTv.setText(dateDialog.getDate());
						}
					}
				};
				dateDialog = new DateDialog(context,handler);
				dateDialog.setDate();
			}
		});

		adapter = new BloodMeasureRecordAdapter(jsonarray, context);
		listview.setAdapter(adapter);

//		listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1,
//					int position, long arg3) {
//				// TODO Auto-generated method stub
//				JSONObject jsonSend = jsonarray.getJSONObject(position);
//				String jsonSendStr = jsonSend.toString();
//				Intent intent = new Intent(context, PluseChartActivity.class);
//				intent.putExtra("pluseInfo", jsonSendStr);
//				intent.putExtra("jwotchModel",mProductModel);
//				startActivity(intent);
//			}
//		});

		query();

		getBPinterval();
	}

	private void getBPinterval() {
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {

				if (msg.what == 0x133) {
					String result = getBloodPressuperiodThread.getResult();
					System.out.println(result+"***********");
					if (UiUtil.isResultSuccess(context, result)) {
						JSONObject json = JSONObject.fromObject(result);
						int code = json.getInt("code");
						if (code == 200) {
							int period = json.getInt("BloodPressurePeriod");
//							System.out.println(period+"**********");
							brIntervalTv.setText(period + "");
						}
					}
				}

			};
		};

		getBloodPressuperiodThread = new JAssistantAPIThread(
				ApiConstant.GETBLOODPRESSUREPERIOD, handler, context);
		getBloodPressuperiodThread.setuId(uId);
		getBloodPressuperiodThread.setCustomerId(customerId);
		getBloodPressuperiodThread.start();
	}

	private void query() {
		handler_getrecord = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x133) {
					String mResult = thread.getResult();
					if (UiUtil.isResultSuccess(context, mResult)) {
						JSONObject jsonObj = JSONObject.fromObject(mResult);
						int code = jsonObj.getInt("code");
						if (code == 200) {
							mResult = jsonObj.getString("Data");
							try {
								jsonarray = JSONArray.fromObject(mResult);

								if (jsonarray.size() < 1) {
									UiUtil.showToast(context, getResources()
											.getString(R.string.the_date_no_data));

								}

							} catch (Exception e) {
								e.printStackTrace();
								jsonarray = null;
								UiUtil.showToast(
										context,
										getResources().getString(
												R.string.the_date_no_data));
							}
						}else{
							String message=jsonObj.getString("message");
							UiUtil.showToast(context, message);
							jsonarray=null;
						}
						
						adapter.setJsonarray(jsonarray);
						adapter.notifyDataSetChanged();

					}

				}
			}
		};

		String methodName = ApiConstant.GETCUSTOMERDATA_DAY;
		 if (mProductModel.equals("JXJ-HM041")) {
			methodName = ApiConstant.GETHM041BLOODPRESSUREBYDATE;
		 }
		thread = new JAssistantAPIThread(methodName, handler_getrecord,
				context);
		thread.setuId(uId);
		thread.setCustomerId(customerId);
		thread.setStartTime(bloodDateTv.getText().toString().trim());
		thread.start();
	}

}
