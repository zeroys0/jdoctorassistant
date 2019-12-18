package com.jxj.jdoctorassistant.main.location;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jxj.circlebutton.CircularProgressButton;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.ToolsLocationListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.ControlCenterServiceThread;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.DateDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ToolsLocationActivity extends Activity {

	@ViewInject(R.id.title_tv)
	TextView titleName;
	@ViewInject(R.id.right_btn_igv)
	ImageView queryIgv;

	@ViewInject(R.id.location_txt)
	TextView locationTv;
	@ViewInject(R.id.control_query_btn)
	CircularProgressButton oneKeyLocation;
	@ViewInject(R.id.location_lv)
	PullToRefreshListView listview;

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

	public static ToolsLocationActivity instance = null;
	private Context context;
	private ControlCenterServiceThread sendCmdThread;
	private JAssistantAPIThread threadGetGps, threadGetDailyGps;
	private Handler handler_getGps, handler_getDaily;
	private String customerId;
	private int uId;
	private SharedPreferences sp;
	private Editor editor;
	private double spLatitude;
	private double spLongtitude;
	private JSONArray jsonArrayGps;
	private ToolsLocationListAdapter adapter;
	private DateDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tools_location);
		instance = this;
		ViewUtils.inject(this);
		context = this;
		initviews();
	}

	@SuppressLint("NewApi")
	private void initviews() {
		sp = getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
		editor = sp.edit();

		customerId = sp.getString(AppConstant.USER_customerId, null);
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);
		titleName.setText(getResources().getString(R.string.tools_location));
		queryIgv.setImageResource(R.drawable.location_query);
		queryIgv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				Handler handler=new Handler(){
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						if(msg.what==AppConstant.MSG_DATEDIALOG){
							String date=dialog.getDate();
							locationTv.setText(date);
							getDailyGps(date);
							
						}
						
					}
				};
				dialog = new DateDialog(context,handler);
				dialog.setDate();
				
			}
		});

		adapter = new ToolsLocationListAdapter(context);
		listview.setAdapter(adapter);

		listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = ToolsLocationActivity.this.getResources()
						.getString(R.string.update_time)
						+ GetDate.currentTime();
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				getGps();
				listview.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getGps();
				listview.onRefreshComplete();
			}

		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				JSONObject gpsjson = jsonArrayGps.getJSONObject(position);

				spLatitude = Double.valueOf(gpsjson.getString("Latitude"));
				spLongtitude = Double.valueOf(gpsjson.getString("Longitude"));

				Intent intent = new Intent(context,
						ToolsLocationMapActivity.class);
				// intent.putExtra("latitude", spLatitude);
				// intent.putExtra("longtitude", spLongtitude);

				// 存入缓存
				editor.putFloat("latitude", (float) spLatitude);
				editor.putFloat("longtitude", (float) spLongtitude);
				editor.commit();

				startActivity(intent);
			}
		});

		oneKeyLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// TODO Auto-generated method stub
				if (oneKeyLocation.getProgress() == 0) {
					simulateSuccessProgress(oneKeyLocation);

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
					sendCmdThread = new ControlCenterServiceThread(context,
							customerId,
							ApiConstant.onekeylocation, sendCmdHandler);
					try {
						sendCmdThread.start();
						// threadOneKey.join();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});

		// 获取列表
		getGps();

	}

	public void getGps() {

		handler_getGps = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x133) {
					String mResult = threadGetGps.getResult();
					if (UiUtil.isResultSuccess(context, mResult)) {
						try {
							JSONObject json=JSONObject.fromObject(mResult);
							int code=json.getInt("code");
							if(code==200){
								String data=json.getString("Data");
								jsonArrayGps = JSONArray.fromObject(data);

								UiUtil.showToast(
										context,
										getResources().getString(
												R.string.already_refresh));
							}else{
								String message=json.getString("message");
								jsonArrayGps=null;
							}

							
							
						} catch (Exception e) {
							e.printStackTrace();
							jsonArrayGps=null;
						}
					}
					adapter.setJsonarray(jsonArrayGps);
					adapter.notifyDataSetChanged();
				}
			} 
		};

		threadGetGps = new JAssistantAPIThread(
				ApiConstant.GETTOPGPSLOCATION, handler_getGps, context);
		threadGetGps.setuId(uId);
		threadGetGps.setCustomerId(String.valueOf(customerId));
		threadGetGps.setAmount(50);
		threadGetGps.start();
	}

	// 获取制定日期定位列表
	public void getDailyGps(String date) {

		handler_getDaily = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x133) {
					String mResult = threadGetDailyGps.getResult();
					if (UiUtil.isResultSuccess(context, mResult)) {
						try {
							JSONObject json=JSONObject.fromObject(mResult);
							int code=json.getInt("code");
							if(code==200){
								String data=json.getString("Data");
								jsonArrayGps = JSONArray.fromObject(data);

								UiUtil.showToast(
										context,
										getResources().getString(
												R.string.already_refresh));
							}else{
								String message=json.getString("message");
								UiUtil.showToast(context, message);
								jsonArrayGps=null;
							}
							
							adapter.setJsonarray(jsonArrayGps);
							adapter.notifyDataSetChanged();
						} catch (Exception e) {
							adapter.setJsonarray(null);
							UiUtil.showToast(context,
									getResources()
											.getString(R.string.show_date));
							adapter.notifyDataSetChanged();

						}
					}

				}
			}
		};

		threadGetDailyGps = new JAssistantAPIThread(
				ApiConstant.GETDAILYGPSLOCATION, handler_getDaily, context);
		threadGetDailyGps.setuId(uId);
		threadGetDailyGps.setCustomerId(customerId);
		threadGetDailyGps.setStartTime(date);
		threadGetDailyGps.start();

	}

	@SuppressLint("NewApi")
	private void simulateSuccessProgress(final CircularProgressButton button) {
		ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
		widthAnimation.setDuration(8000);
		widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		widthAnimation
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						Integer value = (Integer) animation.getAnimatedValue();
						button.setProgress(value);
						if (value == 100) {
							oneKeyLocation.setProgress(0);
							// 刷新列表
							getGps();
						}

					}
				});
		widthAnimation.start();
	}

}
