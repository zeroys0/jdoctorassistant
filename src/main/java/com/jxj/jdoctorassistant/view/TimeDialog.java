package com.jxj.jdoctorassistant.view;

import java.util.Calendar;

import com.jxj.jdoctorassistant.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;




public class TimeDialog {
	private Calendar calendar; // 通过Calendar获取系统时间
	private int hour;
	private int minute;
	private Context context;

	public TimeDialog(Context context) {
		this.context = context;

	}

	@SuppressLint("NewApi")
	public void setTime(final int type,final TextView text) {
		
		
		calendar = Calendar.getInstance();
		// 自定义控件
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View view = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.dialog_time, null);
		final TimePicker timePicker = (TimePicker) view
				.findViewById(R.id.time_picker);
		// 初始化时间
		calendar.setTimeInMillis(System.currentTimeMillis());
		timePicker.setIs24HourView(false);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		// 设置time布局
		builder.setView(view);
		builder.setTitle(context.getString(R.string.timedialog_title));
		builder.setNegativeButton(context.getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						hour = timePicker.getCurrentHour();
						minute = timePicker.getCurrentMinute();
						// 时间小于10的数字 前面补0 如01:12:00
						if(type==2){
							text.setText(new StringBuilder()
									.append(hour < 10 ? "0" + hour : hour)
									.append(":")
									.append(minute < 10 ? "0" + minute : minute));
						}else {
							text.setText(new StringBuilder()
									.append(hour < 10 ? "0" + hour : hour)
									.append(":")
									.append(minute < 10 ? "0" + minute : minute)
									.append(":00"));
						}

						dialog.cancel();
						
						
					}
				});
		builder.setPositiveButton(context.getString(R.string.mycancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}
}
