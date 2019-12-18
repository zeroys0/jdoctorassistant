package com.jxj.jdoctorassistant.main.doctor.personal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.HospitalAddressListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Calendar;

public class HospitalAddressListActivity extends Activity implements HospitalAddressListAdapter.AdapterDelegete{
    @ViewInject(value = R.id.title_tv,parentId = R.id.hospital_address_title)
    private TextView titleTv;

    @ViewInject(R.id.hospital_address_lv)
    private ListView hosptalLv;

    @OnClick({R.id.back_igv,R.id.add_hospital_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.add_hospital_btn:
                Intent intent=new Intent(context,AddHospitalActivity.class);
                intent.putExtra(ADDRESSID,0);
                startActivity(intent);
                break;
        }
    }

    private Context context;
    private HospitalAddressListAdapter adapter;
    private int doctorId;
    private DoctorSHThread getHospitalListThread,deleteAddressThread,setDefaultThread;
    private JSONArray array;
    private SharedPreferences sp;

    public static final String ADDRESSID="address_id";
    public static final String ADDRESSINFO="address_info";
    public static final int RESULTCODE=0x112;
    public static final String HOSPITALNAME="hospital_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hospital_address_list);
        ViewUtils.inject(this);

        context=this;
        initView();
    }
    void initView(){
        titleTv.setText(getResources().getString(R.string.hospital_address));
        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        adapter=new HospitalAddressListAdapter(context);

        hosptalLv.setAdapter(adapter);
        hosptalLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent=new Intent();
                intent.putExtra(HOSPITALNAME,array.getJSONObject(position).getString("Hospital"));
                setResult(RESULTCODE,intent);
                finish();
            }
        });

//        getHospitalList();
    }

    void getHospitalList(){
        array=null;
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result= getHospitalListThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            array=jsonObject.getJSONArray("Data");

                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                    adapter.setArray(array);
                    adapter.notifyDataSetChanged();
                }
            }
        };
        getHospitalListThread =new DoctorSHThread(ApiConstant.GETDOCTORADDRESSLIST,handler,context);
        getHospitalListThread.setDoctorId(doctorId);
        getHospitalListThread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setDelegete(this);
        getHospitalList();
    }

    void showDeleteDialog(final int addressId){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.view_null, null);
        builder.setView(view);
        builder.setTitle("确认要删除吗？");
        builder.setNegativeButton(context.getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Handler handler=new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                if(msg.what== ApiConstant.MSG_API_HANDLER){
                                    String result=deleteAddressThread.getResult();
                                    if(UiUtil.isResultSuccess(context,result)){
                                        JSONObject jsonObject=JSONObject.fromObject(result);
                                        int code=jsonObject.getInt("code");
                                        if(code==200){
                                            getHospitalList();
                                        }else {
                                            UiUtil.showToast(context,jsonObject.getString("message"));
                                        }
                                    }
                                }
                            }
                        };
                        deleteAddressThread=new DoctorSHThread(ApiConstant.DELETEDOCTORADDRESS,handler,context);
                        deleteAddressThread.setDoctorId(doctorId);
                        deleteAddressThread.setAddressId(addressId);
                        deleteAddressThread.start();
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

    @Override
    public void delete(int addressId) {
        showDeleteDialog(addressId);
    }

    @Override
    public void edit(int position,int addressId) {
        Intent intent=new Intent(context,AddHospitalActivity.class);
        JSONObject object=array.getJSONObject(position);
        String str=object.toString();
        intent.putExtra(ADDRESSID,addressId);
        intent.putExtra(ADDRESSINFO,str);
        startActivity(intent);
    }

    @Override
    public void setDefalut(int addressId) {
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=setDefaultThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            getHospitalList();
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        setDefaultThread=new DoctorSHThread(ApiConstant.SETDEFAULTDOCTORADDRESS,handler,context);
        setDefaultThread.setDoctorId(doctorId);
        setDefaultThread.setAddressId(addressId);
        setDefaultThread.start();
    }
}
