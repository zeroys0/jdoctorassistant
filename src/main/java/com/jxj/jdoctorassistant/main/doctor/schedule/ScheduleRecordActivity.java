package com.jxj.jdoctorassistant.main.doctor.schedule;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.ScheduleRecordAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScheduleRecordActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.schedule_record_title)
    private TextView titleTv;
    @ViewInject(value = R.id.right_btn_tv,parentId = R.id.schedule_record_title)
    private TextView editTv;

    @ViewInject(R.id.schedule_record_lv)
    ListView recordLv;
    @ViewInject(R.id.schedule_record_edit_ll)
    private LinearLayout editLL;

    @OnClick({R.id.back_igv,R.id.schedule_record_all_select_btn,R.id.schedule_record_delete_btn})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.schedule_record_all_select_btn:
                if(array.size()>0) {
                    if (isAllSeclect) {
                        for (int i = 0; i < array.size(); i++) {
                            checkArr[i] = false;
                        }
                        idArr.clear();
                        isAllSeclect = false;
                    } else {
                        for (int i = 0; i < array.size(); i++) {
                            int currentCount = array.getJSONObject(i).getInt("CurrentCount");
                            if (currentCount > 0) {
                                UiUtil.showToast(context, "当前预约人数大于0，不能被删除");
                                checkArr[i] = false;
                            } else {
                                checkArr[i] = true;
                                int scheduleId = array.getJSONObject(i).getInt("Id");
                                idArr.add(Integer.valueOf(scheduleId));
                            }

                        }
                        isAllSeclect = true;
                    }
                    adapter.setCheckArr(checkArr);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.schedule_record_delete_btn:
                deleteScheduleRecord();
                break;

        }
    }

    private Context context;
    private ScheduleRecordAdapter adapter;
    private JSONArray array;
    private DoctorSHThread getRecordThread,deleteRecordThread;
    private int doctorId;
    private boolean[] checkArr;
    private boolean isAllSeclect=false;
    private boolean isDelete=false;
    private List<Integer> idArr;
//    private int selectId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule_record);
        ViewUtils.inject(this);

        context=this;

        titleTv.setText(getResources().getString(R.string.schedule_record));
        editTv.setVisibility(View.VISIBLE);
        editTv.setText(getResources().getString(R.string.edit));
        String[] typeArr={getResources().getString(R.string.pic_text),
                getResources().getString(R.string.video_audio),
                getResources().getString(R.string.hospital_treatment),
                getResources().getString(R.string.visit_service)};
        idArr=new ArrayList<Integer>();

        editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDelete){
                    isDelete=false;
                    editTv.setText(getResources().getString(R.string.edit));
                    editLL.setVisibility(View.GONE);
                }else {
                    isDelete=true;
                    editTv.setText(getResources().getString(R.string.mycancel));
                    editLL.setVisibility(View.VISIBLE);
                }
                adapter.setDelete(isDelete);
                adapter.notifyDataSetChanged();
            }
        });
        doctorId=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE).getInt(AppConstant.USER_doctor_id,0);

        adapter=new ScheduleRecordAdapter(context);
        adapter.setTypeArr(typeArr);
        recordLv.setAdapter(adapter);
        recordLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int scheduleId=array.getJSONObject(position).getInt("Id");
                System.out.println("排班id："+scheduleId);
                int currentCount=array.getJSONObject(position).getInt("CurrentCount");
                if(checkArr[position]==true){
                        checkArr[position]=false;
                        if(idArr.size()>0&&idArr.contains(Integer.valueOf(scheduleId))){
                            idArr.remove(Integer.valueOf(scheduleId));
                        }
                }else {
                        if(currentCount>0){
                            UiUtil.showToast(context,"当前预约人数大于0，不能被删除");
                        }else {
                            checkArr[position]=true;
                            idArr.add(Integer.valueOf(scheduleId));
                        }

                }
//                adapter.setSelectId(position);

                adapter.setCheckArr(checkArr);
                adapter.notifyDataSetChanged();

                System.out.println("排班id数组："+idArr.toString());
            }
        });

        getScheduleRecord();
    }

    void getScheduleRecord(){
            array=null;
            isAllSeclect=false;
            idArr.clear();
            Handler handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what== ApiConstant.MSG_API_HANDLER){
                        String result=getRecordThread.getResult();
                        if(UiUtil.isResultSuccess(context,result)){
                            JSONObject jsonObject=JSONObject.fromObject(result);
                            int code=jsonObject.getInt("code");
                            if(code==200){
                                array=jsonObject.getJSONArray("DataList");
                                checkArr=new boolean[array.size()];

                            }else {
                                UiUtil.showToast(context,jsonObject.getString("message"));
                            }
                        }
                        adapter.setArray(array);
                        adapter.setCheckArr(checkArr);
                        adapter.notifyDataSetChanged();
                    }
                }
            };

        getRecordThread=new DoctorSHThread(ApiConstant.GETSHDOCTORPLANBYPAGE,handler,context);
        getRecordThread.setDoctorId(doctorId);
        getRecordThread.setPageIndex(0);
        getRecordThread.setPageSize(10);
        getRecordThread.start();
        }

    void deleteScheduleRecord(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=deleteRecordThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            getScheduleRecord();

                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                    adapter.setArray(array);
                    adapter.setCheckArr(checkArr);
                    adapter.notifyDataSetChanged();
                }
            }
        };

        deleteRecordThread=new DoctorSHThread(ApiConstant.DELETESHDOCTORPLAN,handler,context);
        deleteRecordThread.setDoctorId(doctorId);
        deleteRecordThread.setIdList(idArr.toString());
        deleteRecordThread.start();
    }


}
