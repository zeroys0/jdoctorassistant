package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.ScheduleListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleListActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.schedule_list_title)
    private TextView titleTv;

    @ViewInject(R.id.schedule_customer_lv)
    private ListView lv;

    @OnClick({R.id.back_igv})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            default:

                break;
        }
    }


    private Context context;
    private SharedPreferences sp;
    private int doctorId;
    private String customerId;
    private ScheduleListAdapter adapter;
    private JSONArray array;
    private DoctorSHThread getScheduleThread;
    private TextView tv_start_time,tv_end_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule_list);
        ViewUtils.inject(this);

        context=this;
        titleTv.setText(getResources().getString(R.string.schedule_list));

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        customerId=sp.getString(AppConstant.USER_customerId,null);
        adapter=new ScheduleListAdapter(context);
        lv.setAdapter(adapter);

        getSchedule();
        init();
    }
    public void init(){
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String format = "yyyy-MM-dd hh:mm:ss";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                long begintime = 0;
                long endtime = 0;
                try {
                    begintime = simpleDateFormat.parse(array.getJSONObject(position).getString("BeginDatetime")).getTime()/1000;
                    endtime = simpleDateFormat.parse(array.getJSONObject(position).getString("EndDatetime")).getTime()/1000;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tv_start_time.setText(getHourByTimeStamp(begintime)+" : "+getMinuteByTimeStamp(begintime));
                tv_end_time.setText(getHourByTimeStamp(endtime)+" : "+getMinuteByTimeStamp(endtime));

            }
        });
    }

    void getSchedule(){

        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getScheduleThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            array=jsonObject.getJSONArray("DataList");
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }

                    adapter.setArray(array);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        getScheduleThread=new DoctorSHThread(ApiConstant.GETSHSCHEDULEBYCUSTOMERID,handler,context);
        getScheduleThread.setDoctorId(doctorId);
        getScheduleThread.setCustomerId(customerId);
        getScheduleThread.setPageIndex(0);
        getScheduleThread.setPageSize(10);
        getScheduleThread.start();
    }

    public static int getHourByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String hour = date.substring(11, 13);
        return Integer.parseInt(hour);
    }

    public static int getMinuteByTimeStamp(long timeStamp){
        String date = timeStampToDate(timeStamp);
        String minute = date.substring(14, 16);
        return Integer.parseInt(minute);
    }

    public static String timeStampToDate(long timeStamp){
        Date date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }


}
