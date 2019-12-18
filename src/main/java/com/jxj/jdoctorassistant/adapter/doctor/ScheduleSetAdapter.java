package com.jxj.jdoctorassistant.adapter.doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.util.CalendarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;

public class ScheduleSetAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;
	private int selected=-1;
	private boolean isAble;
	private int[] dateNum;
	private int year;
	private int month;
	private int day;
	private int dayOfWeek;
	private int type;

	public ScheduleSetAdapter(Context context){
		this.context=context;
	}

	public void setSelected(int selected,boolean isAble) {
		this.selected = selected;
		this.isAble=isAble;
	}

	public void setDateNum(int[] dateNum) {
		this.dateNum = dateNum;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public int getCount() {
		return dateNum.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.gv_item_schedule_set_calendar, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		holder.dateTv.setText(String.valueOf(dateNum[position]));


		if(position==selected){
			if(isAble){
				holder.dateTv.setTextColor(context.getResources().getColor(R.color.app_blue));
				holder.dateTv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.white_circle_bg));
			}else {
				holder.dateTv.setTextColor(context.getResources().getColor(R.color.white));
				holder.dateTv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.white_circle_blue_bg));
			}
//			holder.dateTv.setTextColor(context.getResources().getColor(R.color.app_blue));
//			holder.dateTv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.white_circle_bg));
		}else {
			switch (type){
				case 0://排班设置界面
					int monthDays= CalendarUtil.getDaysOfMonth(CalendarUtil.isLeapYear(year),month);
					int temp=day+21-monthDays;
					if(dateNum[position]>temp&&(dateNum[position]-7)<day){
						holder.dateTv.setTextColor(context.getResources().getColor(R.color.white_60));
//					holder.dateTv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.white_corner_blue_circle_bg));
					}else {
                        holder.dateTv.setTextColor(context.getResources().getColor(R.color.white));
					}
					break;
				case 1://日程安排界面
					int lastMonthDays;
					if(month>1){
						lastMonthDays=CalendarUtil.getDaysOfMonth(CalendarUtil.isLeapYear(year),month-1);
					}else {
						lastMonthDays=31;
					}
					int tempp=day+7-lastMonthDays;
					System.out.println("上月天数："+lastMonthDays);
					System.out.println("条目日期："+dateNum[position]+"  tempp:"+tempp+"  day:"+day);
					if(dateNum[position]>tempp&&dateNum[position]<day){
						holder.dateTv.setTextColor(context.getResources().getColor(R.color.white_60));
						System.out.println("未选择情况下的不可用日期");
//					holder.dateTv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.white_corner_blue_circle_bg));

					}else{
						holder.dateTv.setTextColor(context.getResources().getColor(R.color.white));
						System.out.println("未选择情况下的可用日期");
//					holder.dateTv.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.white_corner_blue_circle_bg));

					}

					break;
			}
//			holder.dateTv.setTextColor(context.getResources().getColor(R.color.white));
			holder.dateTv.setBackgroundDrawable(context.getResources().getDrawable(R.color.blue_27));
		}


		return convertView;
	}
	
	class ViewHolder{
		@ViewInject(R.id.schedule_set_calendar_tv)
		TextView dateTv;
//		@ViewInject(R.id.patient_function_lv_tv)
//		TextView tv;
	}
	
}
