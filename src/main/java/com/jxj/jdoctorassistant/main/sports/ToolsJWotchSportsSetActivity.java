package com.jxj.jdoctorassistant.main.sports;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.ControlCenterServiceThread;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.DateDialog;
import com.jxj.jdoctorassistant.view.TimeDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class ToolsJWotchSportsSetActivity extends Activity{

	@ViewInject(R.id.title_tv)
	TextView titileTv;
	@ViewInject(R.id.right_btn_igv)
	ImageView saveIgv;


	@ViewInject(R.id.sport_jwotch_set_radiogroup)
	RadioGroup sportjWotchSetRg;
	@ViewInject(R.id.sport_jwotch_set_one)
	RadioButton sportjWotchSetOneRbn;
	@ViewInject(R.id.sport_jwotch_set_everyday)
	RadioButton sportjWotchSetEverydayRbn;
	@ViewInject(R.id.sport_jwotch_set_myself)
	RadioButton sportjWotchSetMyselfRbn;

	@ViewInject(R.id.sport_jwotch_set_myself_mon)
	CheckBox sportSetMyselfMonCbx;
	@ViewInject(R.id.sport_jwotch_set_myself_tue)
	CheckBox sportSetMyselfTueCbx;
	@ViewInject(R.id.sport_jwotch_set_myself_wed)
	CheckBox sportSetMyselfWedCbx;
	@ViewInject(R.id.sport_jwotch_set_myself_thu)
	CheckBox sportSetMyselfThuCbx;
	@ViewInject(R.id.sport_jwotch_set_myself_fri)
	CheckBox sportSetMyselfFriCbx;
	@ViewInject(R.id.sport_jwotch_set_myself_sat)
	CheckBox sportSetMyselfSatCbx;
	@ViewInject(R.id.sport_jwotch_set_myself_sun)
	CheckBox sportSetMyselfSunCbx;

	@ViewInject(R.id.sport_set_cycle_tv)
	TextView sportCycleTxt;

	@ViewInject(R.id.start_time_txt)
	TextView startTimeTxt;
	@ViewInject(R.id.end_time_txt)
	TextView endTimeTxt;
	@ViewInject(R.id.start_time_rl)
	RelativeLayout startTimeRl;
	@ViewInject(R.id.end_time_rl)
	RelativeLayout endTimeRl;
	@ViewInject(R.id.sport_add_cb)
	CheckBox sportAddCb;
	private int mCycleType, size, status,cycleType;

	private PopupWindow selfCycleSetPopWin;
	private View selfCycleSetPopWinView;
	private DateDialog dateDialog;

	private int customerId, id;
	private int uId;
	private SharedPreferences sp;
	private Context context;
	private JAssistantAPIThread addOrUpdateNewSportThread;
	private ControlCenterServiceThread sendCmdThread;
	private String arrayStr;
	private boolean isRepeat=false;
	private boolean isExcute=false;
//	private Handler handler_addNewSport, handler_updateSport;
	private Map sportPlanMap;
	private TimeDialog timedialog;
	private String date;
	public static ToolsJWotchSportsSetActivity instance = null;

	@OnClick({ R.id.back_igv, R.id.sport_jwotch_set_myself,
			R.id.right_btn_igv })
	public void click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;
			
		case R.id.sport_jwotch_set_myself:
			if (selfCycleSetPopWin.isShowing()) {
				selfCycleSetPopWin.dismiss();
			} else {
				showPop();
			}
		
		    break;


		case R.id.right_btn_igv:

			Intent intent = getIntent();
			Bundle data = intent.getExtras();
			size = data.getInt("size");
			if(data.containsKey("array")){
				arrayStr=data.getString("array");
			}
			addOrUpdate(size,arrayStr);

			break;

		default:
			break;
		}
	}



	private void addOrUpdate(int mark,String arrayString){
		isRepeat=false;
		String s1 = startTimeTxt.getText().toString().substring(0, 2);
		String s2 = startTimeTxt.getText().toString().substring(3, 5);
		String e1 = endTimeTxt.getText().toString().substring(0, 2);
		String e2 = endTimeTxt.getText().toString().substring(3, 5);
		JSONArray array=new JSONArray();
		if(arrayString!=null){
			array=JSONArray.fromObject(arrayString);
		}


		int s = (Integer.parseInt(s1)*60)+Integer.parseInt(s2);
		int e = (Integer.parseInt(e1)*60)+Integer.parseInt(e2);
		int sum = e - s;  //0 - 120min

		if(sum <= 0){
			UiUtil.showToast(context, getResources().getString(R.string.sm_time_hint1));
			return;
		}

		String mType=String.valueOf(mCycleType);
		if(mType.equals("0")){
			if(isPass(Integer.parseInt(s1),Integer.parseInt(s2),date)){
				UiUtil.showToast(context,getResources().getString(R.string.sm_time_hint3));
				return;
			}
		}

		StringBuffer mySb=new StringBuffer();
		String myCycleStr=Integer.toBinaryString(mCycleType);
		for(int r=0;r<7-myCycleStr.length();r++){
			mySb.append(0);
		}
		mySb.append(myCycleStr);
		char[] myChars=mySb.toString().toCharArray();
		int myStartHour=Integer.parseInt(startTimeTxt.getText().toString()
				.substring(0, 2));
		int myStartMinute=Integer.parseInt(startTimeTxt.getText().toString()
				.substring(3, 5));
		int myStopHour=Integer.parseInt(endTimeTxt.getText().toString()
				.substring(0, 2));
		int myStopMinute=Integer.parseInt(endTimeTxt.getText().toString()
				.substring(3, 5));

		for(int i=0;i<array.size()&&(!isRepeat);i++){//遍历计划列表
			isExcute=false;
			JSONObject json=array.getJSONObject(i);
			int mId=json.getInt("id");
			if(mId!=id){
//				Log.v("周期冲突","位置:"+i+" 修改判定 和自己的冲突");
				int cycle=json.getInt("CycleType");
				int startHour=json.getInt("StartTimeHour");
				int startMinute=json.getInt("StartTimeMinute");
				int stopHour=json.getInt("StopTimeHour");
				int stopMinute=json.getInt("StopTimeMinute");
				String uDate=null;
				if(json.containsKey("UpdateDate")){
					uDate=json.getString("UpdateDate");
				}else {
					uDate= GetDate.currentDate();
				}

				if(cycle==0){//遍历的计划周期为一次
					Log.d("周期测试","日期:"+uDate);
					if(isExecute(stopHour,stopMinute,uDate)){//已执行，不考虑周期冲突
						Log.v("周期冲突","已执行");
						isExcute=true;
//						return;
					}else {//未执行
						Log.v("周期冲突","未执行");
						if(mCycleType==0){//当前新建计划周期为一次
							if(uDate.equals(date)){
								if(isCover(convertMinute(myStartHour,myStartMinute),
										convertMinute(myStopHour,myStopMinute),
										convertMinute(startHour,startMinute),
										convertMinute(stopHour,stopMinute))){
									isRepeat=true;
									UiUtil.showToast(context,getResources().getString(R.string.sm_time_hint2));
									return;
								}
							}
						}else {//当前新建计划周期为其他重复类型
							Calendar cal = Calendar.getInstance();
							DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
							Date mDate=new Date();
							try {
								mDate = dateFormat.parse(uDate);
							}catch (Exception ee){
								ee.printStackTrace();
							}
							cal.setTime(mDate);
							int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
							Log.d("周期测试","周期位置:"+w);
							cycle=two(w);
							Log.d("周期测试","周期值:"+cycle);
						}

					}
				}else {//遍历的计划周期为多次重复
					if(mCycleType==0){//当前新建计划周期为一次
						Calendar cal = Calendar.getInstance();
						DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
						Date mDate=new Date();
						try {
							mDate = dateFormat.parse(date);
						}catch (Exception ee){
							ee.printStackTrace();
						}
						cal.setTime(mDate);
						int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
						Log.d("周期测试","周期位置:"+w);
						int cType=two(w);
						Log.d("周期测试","周期值:"+cType);
						myCycleStr=Integer.toBinaryString(cType);
						Log.d("周期测试","myCycleStr:"+myCycleStr);

						mySb.replace(7-myCycleStr.length(),7,myCycleStr);
//						for(int r=0;r<7-myCycleStr.length();r++){
//							mySb.replace()
//							mySb.append(0);
//						}
//						mySb.append(myCycleStr);
						myChars=mySb.toString().toCharArray();
					}

				}
				if(!isExcute){
					String cycleStr=Integer.toBinaryString(cycle);
					StringBuffer sb=new StringBuffer();
					int l=cycleStr.length();
					for(int j=0;j<7-l;j++){
						sb.append(0);
					}
					sb.append(cycleStr);
					Log.d("周期测试","对比周期："+sb+"/n"+"我的周期："+mySb);
					char[] chars=sb.toString().toCharArray();
					for(int ii=0;ii<7;ii++){
						if(chars[ii]=='1'&&myChars[ii]=='1'){
							if(isCover(convertMinute(myStartHour,myStartMinute),
									convertMinute(myStopHour,myStopMinute),
									convertMinute(startHour,startMinute),
									convertMinute(stopHour,stopMinute))){
								isRepeat=true;
								UiUtil.showToast(context,getResources().getString(R.string.sm_time_hint2));
								return;
							}
						}
					}
				}

			}

		}

		if(!isRepeat){
			Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					if (msg.what == 0x133) {
						String mResult = addOrUpdateNewSportThread.getResult();
						if (UiUtil.isResultSuccess(context, mResult)) {
							JSONObject j = JSONObject.fromObject(mResult);
								int code = j.getInt("code");
								if (code == 200) {
									UiUtil.showToast(context,
											j.getString("message"));
									SynSportplan();
									finish();
								}else {
									UiUtil.showToast(context,j.getString("message"));
								}

						}
					}
				}
			};

			sportPlanMap = new HashMap();
			sportPlanMap.put("CustomerID", customerId);
			sportPlanMap.put("UpdateDate",date);
			sportPlanMap.put("StartTimeHour", startTimeTxt.getText().toString()
					.substring(0, 2));
			sportPlanMap.put("StartTimeMinute", startTimeTxt.getText().toString()
					.substring(3, 5));
			sportPlanMap.put("StopTimeHour", endTimeTxt.getText().toString()
					.substring(0, 2));
			sportPlanMap.put("StopTimeMinute", endTimeTxt.getText().toString()
					.substring(3, 5));

			sportPlanMap.put("CycleType", String.valueOf(mCycleType));

			sportPlanMap.put("Status", sportAddCb.isChecked()?1:0);

			String methodName=null;
			if (mark == 0) {
				methodName= ApiConstant.INSERTSPORTPLAN;
			} else if (mark == 1) {
				sportPlanMap.put("id",id);
				methodName=ApiConstant.UPDATESPORTPLAN;
			}

			JSONObject json = JSONObject.fromObject(sportPlanMap);

			addOrUpdateNewSportThread = new JAssistantAPIThread(
					methodName, handler, context);
			addOrUpdateNewSportThread.setuId(uId);
			addOrUpdateNewSportThread.setCustomerId(String.valueOf(customerId));
			addOrUpdateNewSportThread.setSportPlan(json.toString());
			addOrUpdateNewSportThread.start();
		}
	}

	private int two(int i){
		if(i==0){
			return 1;
		}else if(i==1){
			return 2;
		}else {
			return 2*two(i-1);
		}
	}
	public int convertMinute(int hour,int minute){
		return hour*60+minute;
	}
	public boolean isCover(int myStartTime,int myStopTime,int startTime,int stopTime){
		if(myStopTime<startTime||myStartTime>stopTime){
			return false;
		}else{
			return true;
		}
	}
	//判断当前周期为一次时，时间设置有效（即执行时间为未来时间，而不是过去时间）
	public boolean isPass(int startHour, int startMinute,String date){
		//输出格式: 2006-01-01 01:00:00
		String time=date+" "+con(startHour)+":"+con(startMinute)+":00";
		System.out.println("是否有效 设置时间字符串："+time);
		DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date mDate=new Date();
		try {
			mDate=dateFormat.parse(time);
		}catch (Exception e){
			e.printStackTrace();
		}
		Date d=new Date();
		System.out.println("有效性 设置时间："+mDate.getTime()+" 当前时间："+d.getTime());
		return mDate.before(d);//mDate在d之后
	}

	private String con(int i){
		if(i<10){
			return "0"+i;
		}else {
			return String.valueOf(i);
		}
	}

	public boolean isExecute(int stopHour, int stopMinute,String date){
		//输出格式: 2006-01-01 01:00:00
		String time=date+" "+con(stopHour)+":"+con(stopMinute)+":00";
		System.out.println("是否已执行 设置时间字符串："+time);
		DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date mDate=new Date();
		try {
			mDate=dateFormat.parse(time);
		}catch (Exception e){
			e.printStackTrace();
		}
		Date d=new Date();
		System.out.println("是否已执行 设置时间："+mDate.getTime()+" 当前时间："+d.getTime());
		return mDate.before(d);//mDate在当前时间之后
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sports_jwotch_set);
		ViewUtils.inject(this);
		instance = this;
		context = this;
		initView();

	}

	@SuppressLint("NewApi")
	private void showPop() {

		String cycle=Integer.toBinaryString(cycleType);

		StringBuffer sb=new StringBuffer();
		int l=cycle.length();
		for(int i=0;i<7-l;i++){
			sb.append(0);
		}
		sb.append(cycle);
		char[] chars=sb.toString().toCharArray();
		if(chars[0]=='1'){
			sportSetMyselfSatCbx.setChecked(true);
		}
		if(chars[1]=='1'){
			sportSetMyselfFriCbx.setChecked(true);
		}
		if(chars[2]=='1'){
			sportSetMyselfThuCbx.setChecked(true);
		}
		if(chars[3]=='1'){
			sportSetMyselfWedCbx.setChecked(true);
		}
		if(chars[4]=='1'){
			sportSetMyselfTueCbx.setChecked(true);
		}
		if(chars[5]=='1'){
			sportSetMyselfMonCbx.setChecked(true);
		}
		if(chars[6]=='1'){
			sportSetMyselfSunCbx.setChecked(true);
		}


		selfCycleSetPopWin.showAsDropDown(sportjWotchSetRg);


		Button selfCycleSetBtn = (Button) selfCycleSetPopWinView
				.findViewById(R.id.view_jwotchset_sure_btn);
		selfCycleSetBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mCycleType = 0;
				if (sportSetMyselfMonCbx.isChecked()) {

					mCycleType += Integer.valueOf("00000010", 2).intValue();
				}
				if (sportSetMyselfTueCbx.isChecked()) {

					mCycleType += Integer.valueOf("00000100", 2).intValue();
				}
				if (sportSetMyselfWedCbx.isChecked()) {

					mCycleType += Integer.valueOf("00001000", 2).intValue();
				}
				if (sportSetMyselfThuCbx.isChecked()) {

					mCycleType += Integer.valueOf("00010000", 2).intValue();
				}
				if (sportSetMyselfFriCbx.isChecked()) {

					mCycleType += Integer.valueOf("00100000", 2).intValue();
				}
				if (sportSetMyselfSatCbx.isChecked()) {

					mCycleType += Integer.valueOf("01000000", 2).intValue();
				}
				if (sportSetMyselfSunCbx.isChecked()) {

					mCycleType += Integer.valueOf("00000001", 2).intValue();
				}

				sportCycleTxt.setText(getResources().getString(R.string.cycle_self));

				selfCycleSetPopWin.dismiss();
				backgroundAlpha(1f);
			}
		});

		backgroundAlpha(0.5f);

	}

	// 设置popwindow弹出的阴影效果
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha;
		getWindow().setAttributes(lp);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			backgroundAlpha(1f);
		}
	}

	@SuppressLint("NewApi")
	private void initView() {
		
		sp = getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);
		customerId = Integer.parseInt(sp.getString(AppConstant.USER_customerId, "0"));
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		size = data.getInt("size");
		titileTv.setText(getResources().getString(R.string.add_new_plan));
		saveIgv.setImageResource(R.drawable.define);

		if (size == 0) {
			startTimeTxt.setText(GetDate.currentTime());
			endTimeTxt.setText(GetDate.currentTime());

			sportjWotchSetOneRbn.setChecked(true);
			sportCycleTxt.setText(getResources().getString(R.string.cycle_one));
			date=GetDate.currentDate();
		} else if (size == 1) {
			startTimeTxt.setText((CharSequence) data.get("start"));
			endTimeTxt.setText((CharSequence) data.get("end"));

			id = data.getInt("id");
			status=data.getInt("status");
			cycleType=data.getInt("cycle");
			date=data.getString("date");
			mCycleType =cycleType;


			System.out.println("周期："+cycleType);

			if(status==1){
				sportAddCb.setChecked(true);
			}
			switch (cycleType){
				case 0:
					sportCycleTxt.setText(getResources().getString(R.string.cycle_one));
					sportjWotchSetOneRbn.setChecked(true);
					break;
				case 127:
					sportCycleTxt.setText(getResources().getString(R.string.cycle_day));
					sportjWotchSetEverydayRbn.setChecked(true);
					break;
				default:
					sportCycleTxt.setText(getResources().getString(R.string.cycle_self));
					sportjWotchSetMyselfRbn.setChecked(true);
					break;
			}
		}

		sportjWotchSetOneRbn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDateDialog();
			}
		});

		startTimeRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TimeDialog dialog = new TimeDialog(context);
				dialog.setTime(0, startTimeTxt);
			}
		});

		endTimeRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				TimeDialog dialog = new TimeDialog(context);
				dialog.setTime(0, endTimeTxt);
			}
		});

		sportjWotchSetRg
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						switch (arg1) {
						case R.id.sport_jwotch_set_one:

							mCycleType = 0;
							sportCycleTxt.setText(getResources().getString(R.string.cycle_one));
							break;
						case R.id.sport_jwotch_set_everyday:

							mCycleType = 127;
							sportCycleTxt.setText(getResources().getString(R.string.cycle_day));
							break;

						default:
							break;
						}
					}

				});


		LayoutInflater inflater = LayoutInflater.from(context);
		selfCycleSetPopWinView = inflater.inflate(R.layout.view_jwotchset_pop,
				null);
		int w= getWindowManager().getDefaultDisplay().getWidth();
		int h= getWindowManager().getDefaultDisplay().getHeight();
		int ww=200;
		if(h<1000){
			ww=220;
		}else  if(h<1500){
			ww=300;
		}else{
			ww=380;
		}
		selfCycleSetPopWin = new PopupWindow(selfCycleSetPopWinView,
				LayoutParams.MATCH_PARENT, ww, true);
		selfCycleSetPopWin.setFocusable(true);
		selfCycleSetPopWin.setBackgroundDrawable(new PaintDrawable(0));
		ViewUtils.inject(this, selfCycleSetPopWinView);

	}

	void showDateDialog(){
		Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==AppConstant.MSG_DATEDIALOG){
					date=dateDialog.getDate();
				}
			}
		};
		dateDialog=new DateDialog(context,handler);
		dateDialog.setDate();
	}

	
	private void SynSportplan() {

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
				String.valueOf(customerId), ApiConstant.refreshsportplanlist,
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
