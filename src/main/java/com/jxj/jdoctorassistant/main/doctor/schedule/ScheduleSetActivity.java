package com.jxj.jdoctorassistant.main.doctor.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.ScheduleSetAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.main.doctor.personal.HospitalAddressListActivity;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.CalendarUtil;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.MyGridView;
import com.jxj.jdoctorassistant.view.TimeDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScheduleSetActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.schedule_set_title)
    private TextView titleTv;
    @ViewInject(value = R.id.right_btn_igv,parentId = R.id.schedule_set_title)
    private ImageView rightIgv;

    @ViewInject(R.id.schedule_set_calendar_gv)
    private MyGridView calendarGv;

    @ViewInject(R.id.schedule_set_start_time_tv)
    private TextView startTimeTv;
    @ViewInject(R.id.schedule_set_end_time_tv)
    private TextView endTimeTv;
    @ViewInject(R.id.schedule_set_num_etv)
    private EditText numEtv;
    @ViewInject(R.id.schedule_type_rg)
    private RadioGroup typeRg;
    @ViewInject(R.id.schedule_set_address_tv)
    private TextView addressTv;

    @ViewInject(R.id.schedule_ins_rl)
    private RelativeLayout scheduleInsRl;

    @OnClick({R.id.back_igv,R.id.schedule_set_save_btn,R.id.close_igv})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.schedule_set_save_btn:
                addPlan();
                break;
            case R.id.close_igv:
                if(scheduleInsRl.getVisibility()==View.VISIBLE){
                    scheduleInsRl.setVisibility(View.GONE);
                }else {

                }
                break;
        }
    }


    private Context context;
    private ScheduleSetAdapter adapter;
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
    private TimeDialog timeDialog;

    private DoctorSHThread addThread, getHospitalListThread;
    private int scheduleType;
    private int doctorId;
    private static String DATEERROR="date_error";

    public final static int    REQUESTCODE=0x122;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule_set);

        ViewUtils.inject(this);
        context=this;
        initDate();
        initView();
    }

    void initView(){
        doctorId=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE).getInt(AppConstant.USER_doctor_id,0);
        titleTv.setText(getResources().getString(R.string.schedule_set));
        rightIgv.setVisibility(View.VISIBLE);
        rightIgv.setImageDrawable(getResources().getDrawable(R.drawable.schedule_ins));
        rightIgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scheduleInsRl.getVisibility()==View.GONE){
                    scheduleInsRl.setVisibility(View.VISIBLE);
                }
            }
        });

//        scheduleInsRl.setVisibility(View.GONE);

        startTimeTv.setText(GetDate.currentHM());
        endTimeTv.setText(GetDate.currentHM());

        adapter=new ScheduleSetAdapter(context);
        adapter.setType(0);
        adapter.setYear(year);
        adapter.setMonth(month);
        adapter.setDay(day);
        adapter.setDayOfWeek(dayOfWeek);
        adapter.setDateNum(dateNum);
        calendarGv.setAdapter(adapter);
        calendarGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                adapter.setSelected(position);
//                adapter.notifyDataSetChanged();
                int monthDays= CalendarUtil.getDaysOfMonth(CalendarUtil.isLeapYear(year),month);
                int temp=day+21-monthDays;
                if(dateNum[position]>temp&&(dateNum[position]-7)<day){
                        setDate=DATEERROR;
                    adapter.setSelected(position,false);
                }else {
                    adapter.setSelected(position,true);


                    if(dateNum[position]>day){
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
                adapter.notifyDataSetChanged();
//                titleTv.setText(setDate);


            }
        });


        typeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {
                switch (checkId){
                    case R.id.schedule_type_pic_rb:
                        scheduleType=0;
                        break;
                    case R.id.schedule_type_video_rb:
                        scheduleType=1;
                        break;
                    case R.id.schedule_type_hospital_rb:
                        scheduleType=2;
                        break;
                    case R.id.schedule_type_visit_rb:
                        scheduleType=3;
                        break;
                    default:

                        break;
                }

            }
        });
        timeDialog=new TimeDialog(context);

        startTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog.setTime(2,startTimeTv);
            }
        });

        endTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog.setTime(2,endTimeTv);
            }
        });

        addressTv.setText("无");
        addressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(context,HospitalAddressListActivity.class),REQUESTCODE);
//                startActivity(new Intent(context, HospitalAddressListActivity.class));
            }
        });
        getHospitalList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUESTCODE){
            if(resultCode==HospitalAddressListActivity.RESULTCODE){
                String hospiatl=data.getStringExtra(HospitalAddressListActivity.HOSPITALNAME);
                addressTv.setText(hospiatl);
            }else {
                getHospitalList();
            }
        }
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
        dateNum=new int[14];
        for(int i=0;i<14;i++){
            int tempDay=day-dayOfWeek+1+i+7;
            if(tempDay> CalendarUtil.getDaysOfMonth(CalendarUtil.isLeapYear(year),month)){
                dateNum[i]=tempDay-CalendarUtil.getDaysOfMonth(CalendarUtil.isLeapYear(year),month);
            }else {
                dateNum[i]=tempDay;
            }

        }
    }
    private String convertMonth(int m){
        if(m>9){
            return String.valueOf(m);
        }else {
            return "0"+String.valueOf(m);
        }
    }

    void addPlan(){
        if(setDate.equals(DATEERROR)){
            UiUtil.showToast(context,"当前日期不可用，请重新选择日期");
            return;
        }
        String peopleNum=numEtv.getEditableText().toString().trim();
        if(peopleNum.length()<1){
            UiUtil.showToast(context,"请设置人数");
            return;
        }
        String address=addressTv.getText().toString().trim();
        if(address.length()<1){
            UiUtil.showToast(context,"请设置医院");
            return;
        }

        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=addThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            startActivity(new Intent(context,ScheduleRecordActivity.class));
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }
            }
        };

        addThread=new DoctorSHThread(ApiConstant.ADDSHDOCTORPLAN,handler,context);
        addThread.setDoctorId(doctorId);
        addThread.setType(scheduleType);
        addThread.setBeginTime(setDate+" "+startTimeTv.getText().toString().trim()+":00");
        addThread.setEndTime(setDate+" "+endTimeTv.getText().toString().trim()+":00");
        addThread.setMaxCount(Integer.parseInt(peopleNum));
        addThread.setAddress(address);
        addThread.start();
    }

    void getHospitalList(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result= getHospitalListThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            JSONArray array=jsonObject.getJSONArray("Data");
                            for(int i=0;i<array.size();i++){
                                JSONObject object=array.getJSONObject(i);
                                boolean isDefault=object.getBoolean("IsDefault");
                                if(isDefault){
                                    addressTv.setText(object.getString("Hospital"));
                                    break;
                                }
                            }

                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        getHospitalListThread =new DoctorSHThread(ApiConstant.GETDOCTORADDRESSLIST,handler,context);
        getHospitalListThread.setDoctorId(doctorId);
        getHospitalListThread.start();

    }


}
