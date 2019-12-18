package com.jxj.jdoctorassistant.main.doctor.personal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.PopularThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class AddHospitalActivity extends Activity {
    StringBuffer sb = new StringBuffer();
    private String city;
    private String province;
    @ViewInject(value = R.id.title_tv,parentId = R.id.add_hospital_title)
    private TextView titleTv;
    @ViewInject(value = R.id.right_btn_tv,parentId = R.id.add_hospital_title)
    private TextView right_btn_tv;


    @ViewInject(R.id.hospital_name_etv)
    private EditText nameEtv;
//    @ViewInject(R.id.province_tv)
//    private TextView provinceTv;
//    @ViewInject(R.id.city_tv)
//    private TextView cityTv;
//    @ViewInject(R.id.local_area_tv)
//    private TextView areaTv;
    @ViewInject(R.id.detail_address_etv)
    private EditText addressEtv;
    @ViewInject(R.id.rl_address)
    private RelativeLayout rl_address;
    @ViewInject(R.id.tv_address)
    private TextView tv_address;

    @OnClick({R.id.back_igv,R.id.right_btn_tv})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.right_btn_tv:
                Toast.makeText(context, "您保存", Toast.LENGTH_SHORT).show();
                hospital=nameEtv.getEditableText().toString().trim();
                if(hospital.length()<1){
                    UiUtil.showToast(context,getResources().getString(R.string.doctor_hospital_hint));
                    return;
                }
                String detailAddress=addressEtv.getEditableText().toString().trim();
                if(((isSetProvince&&isSetCity)&&isSetArea)&&(detailAddress.length()>0)){
                    address=tv_address.getText().toString().trim()+ detailAddress;
                    if(addressId!=0){
                        updateAddress();
                    }else {
                        addAddress();
                    }
                }else {
                    UiUtil.showToast(context,"请完成地址的设置");
                }

                break;
        }
    }

    private Context context;
    private int doctorId;
    private int addressId;
    private int communityId;
    private String hospital;
    private String address;
    private DoctorSHThread addAddressThread,updateAddressThread;
    private PopularThread getProvinceThread,getCityThread,getAreaThread;
    private SharedPreferences sp;

    private String[] arrProvince;
    private String[] arrCity;
    private String[] arrArea;

    private boolean isSetProvince;
    private boolean isSetCity;
    private boolean isSetArea;

    private final static int typeProvince=101;
    private final static int typeCity=102;
    private final static int typeArea=103;

    private ArrayAdapter<String> arrayAdapter;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_hospital);
        ViewUtils.inject(this);

        context=this;
        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        communityId=sp.getInt(AppConstant.USER_doctor_community_id,0);

        titleTv.setText(getResources().getString(R.string.add_hospital));
        right_btn_tv.setText("保存");
        right_btn_tv.setVisibility(View.VISIBLE);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        addressId=bundle.getInt(HospitalAddressListActivity.ADDRESSID,0);
        if(addressId==0){

        }else {
            String addressInfo=bundle.getString(HospitalAddressListActivity.ADDRESSINFO);
            JSONObject jsonObject=JSONObject.fromObject(addressInfo);
            nameEtv.setText(jsonObject.getString("Hospital"));
        }

        rl_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.delete(0,sb.length());
                getProvince();
            }
        });


//        provinceTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getProvince();
//            }
//        });
//
//        cityTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isSetProvince){
//                    getCity();
//                }else {
//
//                }
//
//            }
//        });
//
//        areaTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(isSetCity){
//                    getArea();
//                }else {
//
//                }
//
//            }
//        });

    }

    void addAddress(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=addAddressThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            finish();
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        addAddressThread=new DoctorSHThread(ApiConstant.ADDDOCTORADDRESS,handler,context);
        addAddressThread.setDoctorId(doctorId);
        addAddressThread.setCommunityId(communityId);
        addAddressThread.setHospital(hospital);
        addAddressThread.setAddress(address);
        addAddressThread.start();
    }
    void updateAddress(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=updateAddressThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            finish();
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        updateAddressThread=new DoctorSHThread(ApiConstant.UPDATEDOCTORADDRESS,handler,context);
        updateAddressThread.setDoctorId(doctorId);
        updateAddressThread.setAddressId(addressId);
        updateAddressThread.setCommunityId(communityId);
        updateAddressThread.setHospital(hospital);
        updateAddressThread.setAddress(address);
        updateAddressThread.start();
    }

    void getProvince(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getProvinceThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            JSONArray array=jsonObject.getJSONArray("Data");
                            arrProvince=new String[array.size()];
                            for(int i=0;i<array.size();i++){
                                arrProvince[i]=array.get(i).toString();
                            }
                            showListDialog(arrProvince,tv_address,typeProvince);
//                            System.out.println("@@@@@数组长度："+array.size());
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        getProvinceThread=new PopularThread(ApiConstant.GETPROVINCELIST,handler,context);
        getProvinceThread.start();
    }

    void getCity(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getCityThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            JSONArray array=jsonObject.getJSONArray("Data");
                            arrProvince=new String[array.size()];
                            for(int i=0;i<array.size();i++){
                                arrProvince[i]=array.get(i).toString();
                            }
                            showListDialog(arrProvince,tv_address,typeCity);
                            System.out.println("@@@@@数组长度："+array.size());
//                            arrProvince=jsonObject.getString("Data");
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        getCityThread=new PopularThread(ApiConstant.GETCITYLIST,handler,context);
        getCityThread.setProvince(province);
        getCityThread.start();
    }

    void getArea(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getAreaThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            JSONArray array=jsonObject.getJSONArray("Data");
                            arrProvince=new String[array.size()];
                            for(int i=0;i<array.size();i++){
                                arrProvince[i]=array.get(i).toString();
                            }
                            showListDialog(arrProvince,tv_address,typeArea);
//                            arrProvince=jsonObject.getString("Data");
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }
            }
        };
        getAreaThread=new PopularThread(ApiConstant.GETDISTRICTLIST,handler,context);
        getAreaThread.setProvince(province);
        getAreaThread.setCity(city);
        getAreaThread.start();
    }

    void showListDialog(final String[] arr, final TextView tv, final int type){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        Dialog dialog=null;
        View view = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.dialog_list, null);
        ListView lv=(ListView) view.findViewById(R.id.dialog_lv);
        arrayAdapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,arr);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                sb.append(arr[position]);
                tv_address.setText(sb+" >");
//                tv.setText(arr[position]);
                switch (type){
                    case typeProvince:
                        isSetProvince=true;
//                        cityTv.setText(getResources().getString(R.string.city));
//                        areaTv.setText(getResources().getString(R.string.local_area));
                        isSetCity=false;
                        isSetArea=false;
                        province = arr[position];
                        getCity();
                        break;
                    case typeCity:
                        isSetCity=true;
//                        areaTv.setText(getResources().getString(R.string.local_area));
                        isSetArea=false;
                        city = arr[position];
                        getArea();
                        break;
                    case typeArea:
                        isSetArea=true;
                        break;
                }
                dialog.dismiss();
            }
        });
        // 设置date布局
        String title=null;
        builder.setView(view);
        switch (type){
            case typeProvince:
                title="设置"+getResources().getString(R.string.province);
                break;
            case typeCity:
                title="设置"+getResources().getString(R.string.city);
                break;
            case typeArea:
                title="设置"+getResources().getString(R.string.local_area);
                break;
        }
        builder.setTitle(title);
//        builder.setNegativeButton(context.getResources().getString(R.string.cure),
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 日期格式
//                        StringBuffer sb = new StringBuffer();
//                        sb.append(String.format("%d-%02d-%02d",
//                                datePicker.getYear(),
//                                datePicker.getMonth() + 1,
//                                datePicker.getDayOfMonth()));
//                        date=sb.toString();
//
//                        // 赋值后面闹钟使用
////						mYear = datePicker.getYear();
////						mMonth = datePicker.getMonth();
////						mDay = datePicker.getDayOfMonth();
//
//                        Message message=new Message();
//                        message.what=AppConstant.MSG_DATEDIALOG;
//                        handler.sendMessage(message);
//
//                        dialog.cancel();
//                    }
//                });

        builder.setPositiveButton(context.getResources().getString(R.string.cancle),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
//        builder.create().show();
        dialog = builder.create();
        dialog.show();

    }


}
