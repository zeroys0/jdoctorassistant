package com.jxj.jdoctorassistant.main.doctor.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.constraint.solver.SolverVariable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.ScheduleAdapter;
import com.jxj.jdoctorassistant.adapter.doctor.ScheduleSetAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.main.doctor.adapter.OnConfirmListener;
import com.jxj.jdoctorassistant.main.doctor.schedule.ScheduleRecordActivity;
import com.jxj.jdoctorassistant.main.doctor.schedule.ScheduleSetActivity;
import com.jxj.jdoctorassistant.main.doctor.userlist.PatientGuideActivity;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.CalendarUtil;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.MyGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FragmentSchedule extends Fragment implements OnConfirmListener {
    @ViewInject(R.id.schedule_title_rg)
    RadioGroup titleRg;
    @ViewInject(R.id.fragment_schedule_rl)
    RelativeLayout todayRl;
    @ViewInject(R.id.fragment_schedule_ll)
    LinearLayout weekLl;
    @ViewInject(R.id.schedule_calendar_gv)
    MyGridView calendarGv;
    @ViewInject(R.id.schedule_patient_num_tv)
    TextView numTv;
    @ViewInject(R.id.schedule_lv)
    ListView scheduleLv;
    @ViewInject(R.id.schedule_null_rl)
    RelativeLayout nullRl;
    @ViewInject(R.id.work_setting)
    TextView work_setting;
    @ViewInject(R.id.work_record)
    TextView work_record;


    @OnClick({R.id.work_setting,R.id.work_record})
    void onClick(View view){
        switch (view.getId()){
            case R.id.work_setting:
                startActivity(new Intent(context,ScheduleSetActivity.class));
                break;
            case R.id.work_record:
                startActivity(new Intent(context,ScheduleRecordActivity.class));
                break;
            default:

                break;
        }
    }

    private Context context;
    private ScheduleAdapter adapter;
    private JSONArray array;
//    private DoctorSHThread thread;
    private int doctorId;

    private ScheduleSetAdapter setAdapter;
    public int selected;
    private int[] dateNum;

    private String sysDate = "";

    private String sys_year = "";
    private String sys_month = "";
    private String sys_day = "";

    private int year=0;
    private int month=0;
    private int day=0;

    private int dayOfWeek=0;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String setDate;

    private static String DATEERROR="date_error";
    private DoctorSHThread getScheduleThread;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_schedule,container,false);
        ViewUtils.inject(this,view);
        context=getActivity();

        initDate();
        initView();

        return view;
    }

    void initView(){
        doctorId=context.getSharedPreferences(AppConstant.USER_sp_name,context.MODE_PRIVATE).getInt(AppConstant.USER_doctor_id,0);

        titleRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {
                switch (checkId){
                    case R.id.schedule_title_today_rb:

                        todayRl.setVisibility(View.VISIBLE);
                        weekLl.setVisibility(View.GONE);
                        work_record.setVisibility(View.GONE);
                        work_setting.setVisibility(View.GONE);
                        getSchedule(sysDate);
                        break;
                    case R.id.schedule_title_week_rb:

                        work_record.setVisibility(View.VISIBLE);
                        work_setting.setVisibility(View.VISIBLE);
                        todayRl.setVisibility(View.GONE);
                        weekLl.setVisibility(View.VISIBLE);
                        getSchedule(sysDate);
                        break;
                }

            }
        });

        work_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ScheduleSetActivity.class));
            }
        });

        work_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ScheduleRecordActivity.class));
            }
        });

        adapter=new ScheduleAdapter(context,this);
        String[] typeArr={context.getResources().getString(R.string.pic_text),
                context.getResources().getString(R.string.video_audio),
                context.getResources().getString(R.string.hospital_treatment),
                context.getResources().getString(R.string.visit_service)};
        adapter.setTypeArr(typeArr);
        scheduleLv.setEmptyView(nullRl);
        scheduleLv.setAdapter(adapter);

        setAdapter=new ScheduleSetAdapter(context);
        setAdapter.setType(1);
        setAdapter.setYear(year);
        setAdapter.setMonth(month);
        setAdapter.setDay(day);
        setAdapter.setDayOfWeek(dayOfWeek);
        setAdapter.setDateNum(dateNum);
        calendarGv.setAdapter(setAdapter);
        calendarGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                setAdapter.setSelected(position);
//                setAdapter.notifyDataSetChanged();
                int lastMonthDays;
                if(month>1){
                    lastMonthDays=CalendarUtil.getDaysOfMonth(CalendarUtil.isLeapYear(year),month-1);
                }else {
                    lastMonthDays=31;
                }
                System.out.println("上月天数:"+lastMonthDays+" 今天几号："+day+" 位置："+position+"点击的几号："+dateNum[position]);
//                int monthDays= CalendarUtil.getDaysOfMonth(CalendarUtil.isLeapYear(year),month);
                int temp=day+7-lastMonthDays;
                if(dateNum[position]>temp&&dateNum[position]<day){
                    setAdapter.setSelected(position,false);
                    setDate=DATEERROR;
                }else if(dateNum[position]>(lastMonthDays+1-dayOfWeek)){
                    setAdapter.setSelected(position,false);
                    setDate=DATEERROR;
                }else {
                    setAdapter.setSelected(position,true);
                    if(dateNum[position]>day||dateNum[position]==day){
//                        titleTv.setText(sys_year+"-"+sys_month+"-"+convertMonth(dateNum[position]));
                        setDate=sys_year+"-"+sys_month+"-"+convertMonth(dateNum[position]);
                    }else {
                        if(month>11){
//                            titleTv.setText((year+1)+"-01-"+convertMonth(dateNum[position]));
                            setDate=(year+1)+"-01-"+convertMonth(dateNum[position]);
                        }else {
//                            titleTv.setText(sys_year+"-"+convertMonth(month+1)+"-"+convertMonth(dateNum[position]));
                            setDate=sys_year+"-"+convertMonth(month+1)+"-"+convertMonth(dateNum[position]);
                        }

                    }
                }
                setAdapter.notifyDataSetChanged();
//                UiUtil.showToast(context,setDate);
//                titleTv.setText(setDate);
                getSchedule(setDate);

            }
        });

        setAdapter.setSelected(dayOfWeek-1,true);
        setAdapter.notifyDataSetChanged();

        getSchedule(sysDate);

    }

    void initDate(){
        setDate=DATEERROR;

        Date date =new Date();
        sysDate = sdf.format(date); // 当期日期
        System.out.println("当前日期："+sysDate);

        sys_year = sysDate.split("-")[0];
        sys_month = sysDate.split("-")[1];
        sys_day = sysDate.split("-")[2];

        year=Integer.parseInt(sys_year);
        month = Integer.parseInt(sys_month);
        day=Integer.parseInt(sys_day);

        Calendar calendar=Calendar.getInstance();
//        month=12;
//        day=day+7;
//        String s=year+"-"+(month<10?("0"+month):month)+"-"+(day<10?("0"+day):day);
////        String s=year+"-"+month+"-"+day;
//        System.out.println("设置日期："+s);
//        date=new Date(s);
//        calendar.setTime(date);
//        day=13;
        calendar.set(year,month-1,day);
        dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK)-1;
        System.out.println("一周的第几天："+dayOfWeek);
        dateNum=new int[7];
        for(int i=0;i<7;i++){
            int tempDay=day-dayOfWeek+1+i;
            if(tempDay> CalendarUtil.getDaysOfMonth(CalendarUtil.isLeapYear(year),month)){
                dateNum[i]=tempDay-CalendarUtil.getDaysOfMonth(CalendarUtil.isLeapYear(year),month);
            }else {
                dateNum[i]=tempDay;
            }

        }
    }

    void getSchedule(String date){
        if(date==DATEERROR){
            UiUtil.showToast(context,"当前日期不可用");
            return;
        }
        array=null;
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==ApiConstant.MSG_API_HANDLER){
                    String result=getScheduleThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            array=jsonObject.getJSONArray("Data");
                            if(array!=null&&array.size()>0){
                                numTv.setText(String.valueOf(array.size()));
                            }else {
                                numTv.setText("0");
                            }
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));

                        }
                    }else {

                    }
                }
                adapter.setArray(array);
                adapter.notifyDataSetChanged();
            }
        };
        getScheduleThread=new DoctorSHThread(ApiConstant.GETSHSCHEDULEC,handler,context);
        getScheduleThread.setDoctorId(doctorId);
        getScheduleThread.setDate(date);
        getScheduleThread.start();
    }


    private String convertMonth(int m){
        if(m>9){
            return String.valueOf(m);
        }else {
            return "0"+String.valueOf(m);
        }
    }


    @Override
    public void onConfirm(View view) {
        Intent intent = new Intent(context, PatientGuideActivity.class);
        startActivity(intent);
    }
}
