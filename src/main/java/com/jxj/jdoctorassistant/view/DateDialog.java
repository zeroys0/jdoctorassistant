package com.jxj.jdoctorassistant.view;

import java.util.Calendar;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.AppConstant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DateDialog {
	private Calendar calendar; // 通过Calendar获取系统时间
//	private int mYear;
//	private int mMonth;
//	private int mDay;
	private Context context;
	private Handler handler;
	private String date;

	public DateDialog(Context context,Handler handler) {
		this.context = context;
		this.handler=handler;
	}

	@SuppressLint("NewApi")
	public void setDate() {
		calendar = Calendar.getInstance();
		// 通过自定义控件AlertDialog实现
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		View view = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.dialog_date, null);
		final DatePicker datePicker = (DatePicker) view
				.findViewById(R.id.date_picker);
		// 设置日期简略显示 否则详细显示 包括:星期周
		datePicker.setCalendarViewShown(false);
		// 初始化当前日期
		calendar.setTimeInMillis(System.currentTimeMillis());
		datePicker.init(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH), null);
		// 设置date布局
		builder.setView(view);
		builder.setTitle(context.getResources().getString(R.string.date_set));
		builder.setNegativeButton(context.getResources().getString(R.string.cure),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 日期格式
						StringBuffer sb = new StringBuffer();
						sb.append(String.format("%d-%02d-%02d",
								datePicker.getYear(),
								datePicker.getMonth() + 1,
								datePicker.getDayOfMonth()));
						date=sb.toString();
						
						// 赋值后面闹钟使用
//						mYear = datePicker.getYear();
//						mMonth = datePicker.getMonth();
//						mDay = datePicker.getDayOfMonth();
						
						Message message=new Message();
						message.what=AppConstant.MSG_DATEDIALOG;
						handler.sendMessage(message);
						
						dialog.cancel();
					}
				});

		builder.setPositiveButton(context.getResources().getString(R.string.cancle),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}
	
	public String getDate() {
		return date;
	}
}
