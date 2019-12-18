package com.jxj.jdoctorassistant.main.remind;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.ControlCenterServiceThread;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.TimeDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


public class ToolsJWotchRemindSetActivity extends Activity {

	private Context context;

	@ViewInject(R.id.title_tv)
	TextView titleName;
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

	@ViewInject(R.id.remind_set_time_tv)
	TextView remindSettimeTv;
	@ViewInject(R.id.remind_set_cycle_tv)
	TextView remindSetCycleTv;
	@ViewInject(R.id.remind_lv)
	ListView remindLv;
	@ViewInject(R.id.remind_set_theme_edt)
	EditText remindTitleEt;

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

	private String customerId;
	private int uId;
	private int remindId;
	private int cycleType;
	private SharedPreferences sp;
	// 定时提醒线程
	private JAssistantAPIThread addNewRemind,updateRemind;
	private ControlCenterServiceThread sendCmdThread;

	Handler handler_addRemind, handler_getRemindlist, handler_getRemind,
			handler_updateRemind, handler_deleteRemind;
	private PopupWindow selfCycleSetPopWin;
	private View selfCycleSetPopWinView;
	private int intentSize;
	private int typeId;

	@OnClick({ R.id.back_igv, R.id.sport_jwotch_set_myself,
			R.id.right_btn_igv, R.id.remind_set_time_tv })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;
		case R.id.sport_jwotch_set_myself: {
			if (selfCycleSetPopWin.isShowing()) {
				selfCycleSetPopWin.dismiss();
			} else {
				showPop();
				remindSetCycleTv.setText(getResources().getString(R.string.cycle_self));
			}
		}
			break;
		case R.id.right_btn_igv:
			saveRemind();
			break;
		case R.id.remind_set_time_tv:
			TimeDialog timedialog = new TimeDialog(context);
			timedialog.setTime(0, remindSettimeTv);

			break;

		default:
			break;
		}
	}

	private void saveRemind() {
		if(UiUtil.isEditTextNull(remindTitleEt)){
			UiUtil.showToast(context, getResources().getString(R.string.remind_theme));
			return;
		}
		
		String time=remindSettimeTv.getText().toString().trim();
		
		Calendar cal = Calendar.getInstance();
		int year=cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH)+1;
		int day=cal.get(Calendar.DAY_OF_MONTH);
		
		String[] timeArr=time.split(":");
		int hour=Integer.parseInt(timeArr[0]);
		int minute=Integer.parseInt(timeArr[1]);
		
//		String type = String.valueOf(typeId);
		Map map = new HashMap();
		map.put("CustomerId", customerId);
//		map.put("DateString", DateString);
		map.put("Title", remindTitleEt.getText().toString().trim());
		map.put("Type", typeId);
		map.put("VoiceId", 1);
		map.put("State", true);
		
		map.put("Year", year);
		map.put("Month", month);
		map.put("Day", day);
		map.put("Hour", hour);
		map.put("Minute", minute);
		
		if (intentSize == 1) {

			handler_addRemind = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					if (msg.what == 0x133) {
						String result = addNewRemind.getResult();
						if (UiUtil.isResultSuccess(context, result)) {
							JSONObject json=JSONObject.fromObject(result);
							int code=json.getInt("code");
							if (code==200) {
								SynRemind();
								finish();
							}
							UiUtil.showToast(context, json.getString("message"));
						}
					}
				}
			};

			
			JSONObject json = JSONObject.fromObject(map);
			addNewRemind = new JAssistantAPIThread(ApiConstant.ADDNEWREMIND,
					handler_addRemind, context);
			addNewRemind.setuId(uId);
			addNewRemind.setRemindStr(json.toString());
			try {
				addNewRemind.start();
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else if (intentSize == 0) {
			
			handler_updateRemind = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					if (msg.what == 0x133) {
						String result = updateRemind.getResult();
						if (UiUtil.isResultSuccess(context, result)) {
							JSONObject json=JSONObject.fromObject(result);
							int code=json.getInt("code");
							if (code==200) {
								SynRemind();
								finish();
							}
							UiUtil.showToast(context,json.getString("message"));
						}
					}
				}
			};
			
			map.put("Id", remindId);
			
			JSONObject json = JSONObject.fromObject(map);
			updateRemind = new JAssistantAPIThread(ApiConstant.UPDATEREMIND,
					handler_updateRemind, context);
			updateRemind.setuId(uId);
			updateRemind.setRemindStr(json.toString());
			try {
				updateRemind.start();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private void showPop() {
		
		String cycle=Integer.toBinaryString(cycleType);
		char[] chars=cycle.toCharArray();
		if(chars[0]=='1'){
			sportSetMyselfMonCbx.setChecked(true);
		}
		if(chars.length>1&&chars[1]=='1'){
			sportSetMyselfTueCbx.setChecked(true);
		}
		if(chars.length>2&&chars[2]=='1'){
			sportSetMyselfWedCbx.setChecked(true);
		}
		if(chars.length>3&&chars[3]=='1'){
			sportSetMyselfThuCbx.setChecked(true);
		}
		if(chars.length>4&&chars[4]=='1'){
			sportSetMyselfFriCbx.setChecked(true);
		}
		if(chars.length>5&&chars[5]=='1'){
			sportSetMyselfSatCbx.setChecked(true);
		}
		if(chars.length>6&&chars[6]=='1'){
			sportSetMyselfSunCbx.setChecked(true);
		}
		
		selfCycleSetPopWin.showAsDropDown(sportjWotchSetRg);
		Button selfCycleSetBtn = (Button) selfCycleSetPopWinView
				.findViewById(R.id.view_jwotchset_sure_btn);
		selfCycleSetBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				typeId=0;
				if (sportSetMyselfMonCbx.isChecked()) {

					typeId += Integer.valueOf("00000010", 2).intValue();
				}
				if (sportSetMyselfTueCbx.isChecked()) {

					typeId += Integer.valueOf("00000100", 2).intValue();
				}
				if (sportSetMyselfWedCbx.isChecked()) {

					typeId += Integer.valueOf("00001000", 2).intValue();
				}
				if (sportSetMyselfThuCbx.isChecked()) {

					typeId += Integer.valueOf("00010000", 2).intValue();
				}
				if (sportSetMyselfFriCbx.isChecked()) {

					typeId += Integer.valueOf("00100000", 2).intValue();
				}
				if (sportSetMyselfSatCbx.isChecked()) {

					typeId += Integer.valueOf("01000000", 2).intValue();
				}
				if (sportSetMyselfSunCbx.isChecked()) {

					typeId += Integer.valueOf("00000001", 2).intValue();
				}

				remindSetCycleTv.setText(getResources().getString(R.string.cycle_self));
				selfCycleSetPopWin.dismiss();
				backgroundAlpha(1f);
			}
		});
		backgroundAlpha(0.5f);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tools_jwotch_remind_set);
		context = this;
		ViewUtils.inject(this);
		initviews();

	}

	private void initviews() {

		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		intentSize = data.getInt("intentSize");

		if (intentSize == 1) {
			remindSettimeTv.setText(GetDate.currentTime());
			remindSetCycleTv.setText(getResources().getString(R.string.cycle_one));
			sportjWotchSetOneRbn.setChecked(true);
		} else if (intentSize == 0) {

			remindSettimeTv.setText((CharSequence) data.get("time"));

			remindTitleEt.setText((CharSequence) data.get("title"));
			remindId = data.getInt("id");
			cycleType=data.getInt("cycle");
			switch (cycleType){
				case 0:
					sportjWotchSetOneRbn.setChecked(true);
					remindSetCycleTv.setText(getResources().getString(R.string.cycle_one));
					break;
				case 127:
					sportjWotchSetEverydayRbn.setChecked(true);
					remindSetCycleTv.setText(getResources().getString(R.string.cycle_day));
					break;
				default:
					sportjWotchSetMyselfRbn.setChecked(true);
					remindSetCycleTv.setText(getResources().getString(R.string.cycle_self));
					break;
			}

		}

		sp = getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);

		customerId = sp.getString(AppConstant.USER_customerId, null);
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);

		titleName.setText(getResources().getString(R.string.add_newmind_txt));
		saveIgv.setImageResource(R.drawable.define);

//		remindSetCycleTv.setText(getResources().getString(R.string.cycle_one));
//		sportjWotchSetOneRbn.setChecked(true);
		sportjWotchSetRg
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {

						switch (arg1) {
						case R.id.sport_jwotch_set_one:

							typeId = 0;
							remindSetCycleTv.setText(getResources().getString(R.string.cycle_one));
							break;
						case R.id.sport_jwotch_set_everyday:

							typeId = 127;
							remindSetCycleTv.setText(getResources().getString(R.string.cycle_day));
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
//		selfCycleSetPopWin.setOutsideTouchable(true);
		ViewUtils.inject(this, selfCycleSetPopWinView);


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
