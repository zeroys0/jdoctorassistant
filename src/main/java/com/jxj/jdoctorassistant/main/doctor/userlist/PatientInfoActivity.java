package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.util.ICUUncheckedIOException;
import android.net.Uri;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.EaseConstant;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.MachineDataAdapter;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.PatientFunctionAdapter;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.TopDataAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.app.MyApplication;
import com.jxj.jdoctorassistant.main.easymob.ChatActivity;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.IprecareThread;
import com.jxj.jdoctorassistant.util.ImageUtil;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.MyGridView;
import com.jxj.jdoctorassistant.view.RoundImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PatientInfoActivity extends Activity implements MachineDataAdapter.Delegete{
    @ViewInject(value = R.id.title_tv,parentId = R.id.patient_info_title)
    private TextView titleTv;

    @ViewInject(R.id.patient_head_rigv)
    private RoundImageView headRigv;
    @ViewInject(R.id.patient_name_tv)
    private TextView nameTv;
    @ViewInject(R.id.patient_info_tv)
    private TextView infoTv;
    @ViewInject(R.id.patient_ispay_igv)
    private ImageView isPayIgv;

    @ViewInject(R.id.patient_function_gv)
    private MyGridView functionGv;
    @ViewInject(R.id.patient_top_data_gv)
    private MyGridView topDataGv;
    @ViewInject(R.id.patient_machine_data_lv)
    private ListView machineDataLv;

    private Context context;
    PatientFunctionAdapter adapter;
    TopDataAdapter topDataAdapter;
    MachineDataAdapter machineDataAdapter;

    private JSONArray array;
    private JSONArray machineDataArray;
    private DoctorSHThread getBasicInfoThread,getContactThread,getHdDataTop3Thread,getMachineDataThread;
    private IprecareThread downloadPhotoThread;

    private SharedPreferences sp;
    private int doctorId;
    private String customerId;
    private String userName;
    private String cname;
    private String phone;

    private String idCord;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_AUDIO=2;
    private static String[] PERMISSIONS_STORAGE = {	Manifest.permission.READ_EXTERNAL_STORAGE,	Manifest.permission.WRITE_EXTERNAL_STORAGE	};
    private static String[] PERMISSIONS_AUDIO={Manifest.permission.RECORD_AUDIO,Manifest.permission.MODIFY_AUDIO_SETTINGS};

    public static final String RECORDDATE="record_date";
    public static final String IDCORD="idcord";
    public static final String PHONE="phone";
    DoctorSHThread payAttentionThread;
    private boolean[] isVisables;
    private String attention;

    @OnClick({R.id.back_igv,R.id.patient_head_rigv})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.patient_head_rigv:
                startActivity(new Intent(context,PatientDetailInfoActivity.class));
                break;
//            case R.id.machine_data_record_btn:
//                startActivity(new Intent(context,MachineDataActivity.class));
//                break;
            default:

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_patient_info);
        ViewUtils.inject(this);

        context=this;

        titleTv.setText(getResources().getString(R.string.patient_info));

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        String customerInfo=sp.getString(AppConstant.USER_customerInfo,null);
        JSONObject customerObject=JSONObject.fromObject(customerInfo);
        customerId=sp.getString(AppConstant.USER_customerId,null);
        userName=customerObject.getString("UserName");
        cname=customerObject.getString("Cname");
        String photo=customerObject.getString("Photo");
        nameTv.setText(cname);
        StringBuffer stringBuffer=new StringBuffer();
        if(customerObject.getString("Gender").equals("M")){
            stringBuffer.append(context.getResources().getString(R.string.male)+"|");
            headRigv.setImageDrawable(context.getResources().getDrawable(R.drawable.head_picture_man));
        }else {
            stringBuffer.append(context.getResources().getString(R.string.female)+"|");
            headRigv.setImageDrawable(context.getResources().getDrawable(R.drawable.head_picture_woman));
        }
        stringBuffer.append(customerObject.getInt("Age")+"|");
        stringBuffer.append(customerObject.getString("Insurance"));

        infoTv.setText(stringBuffer);
        boolean isPay=customerObject.getBoolean("PayAttention");
        if(isPay){
            attention = "false";
            isPayIgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    payAttention(attention);        //取消关注
                }
            });
        }else {
            attention = "true";
            isPayIgv.setImageResource(R.drawable.img_heart_empty);
            isPayIgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    payAttention(attention);    //关注
                }
            });
        }

        adapter=new PatientFunctionAdapter(context);
        int[] img={R.drawable.patient_function_message,R.drawable.img_video,R.drawable.patient_function_call,R.drawable.patient_function_data_input,R.drawable.patient_function_schedule,R.drawable.patient_function_suggest,R.drawable.patient_function_exception,R.drawable.patient_function_report};
        adapter.setName(getResources().getStringArray(R.array.patient_function_arr));
        adapter.setImg(img);
        functionGv.setAdapter(adapter);
        functionGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0: //发消息
//                        Intent intent=new Intent();
//                        intent.putExtra(EaseConstant.EXTRA_USER_ID, userName);
//                        intent.putExtra(AppConstant.USER_cname,cname);
//                        intent.setClass(context,ChatActivity.class);
//                        startActivity(intent);
                        break;
//                    case 1: //视频
//
//                        break;
                    case 2: //电话
                        if(phone!=null){
                            onCall();
                        }else {
                            UiUtil.showToast(context,getResources().getString(R.string.phone_null));
                        }
                        break;
                    case 3: //数据录入
                        startActivity(new Intent(context,DataInputActivity.class));
                        break;
                    case 4: //预约咨询
                        startActivity(new Intent(context,ScheduleListActivity.class));
                        break;
                    case 5: //建议指导
                        startActivity(new Intent(context,SuggestionRecordActivity.class));
                        break;
                    case 6: //异常记录
                        startActivity(new Intent(context,ExceptionRecordActivity.class));
                        break;
                    case 7: //健康报告
//                        startActivity(new Intent(context,HealthReportActivity.class));
                        Intent intent = new Intent(context,WatchDataActivity.class);
                        intent.putExtra("id",customerId);
                        startActivity(intent);
                        break;
                    default:

                        break;
                }
            }
        });

        topDataAdapter=new TopDataAdapter(context);
        String[] dataUnit=getResources().getStringArray(R.array.data_unit);
        topDataAdapter.setUnit(dataUnit);
        topDataGv.setAdapter(topDataAdapter);
        topDataGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                JSONObject jsonObject=array.getJSONObject(position);
                int type=Integer.parseInt(jsonObject.getString("Type"));
//                int type=position;
                Intent intent=new Intent(context,HdDataActivity.class);
                intent.putExtra("type",type);
                startActivity(intent);
            }
        });

        machineDataAdapter=new MachineDataAdapter(context);
        machineDataLv.setAdapter(machineDataAdapter);
        setListViewHeightBasedOnChildren(machineDataLv);

        verifyAudioPermissions(this);

        setPhoto(photo);
        getBasicInfo();
        getContactInfo();
        getHdDataTop3();
    }

    @Override
    protected void onResume() {
        super.onResume();
        machineDataAdapter.setDelegete(this);
    }

    /*6.0 写 内存 的 权限  */
    public static void verifyAudioPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,	Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_AUDIO,REQUEST_AUDIO);
        }

    }

    void setPhoto(String photo){
        @SuppressLint("HandlerLeak") Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=downloadPhotoThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            String data=jsonObject.getString("Data");
                            headRigv.setImageBitmap(ImageUtil.getBitmap(data));
                        }else {

                        }
                    }
                }
            }
        };
        downloadPhotoThread=new IprecareThread(ApiConstant.DOWNLOADPHOTO,handler,context);
        downloadPhotoThread.setFileName(photo);
        downloadPhotoThread.start();
    }

    void getHdDataTop3(){
        array=null;

        @SuppressLint("HandlerLeak") Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getHdDataTop3Thread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            array=jsonObject.getJSONArray("Data");

                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                    topDataAdapter.setArray(array);
                    topDataAdapter.notifyDataSetChanged();

                }
            }
        };
        getHdDataTop3Thread=new DoctorSHThread(ApiConstant.GETHDDATABYCUSTOMERID_TOP3,handler,context);
        getHdDataTop3Thread.setDoctorId(doctorId);
        getHdDataTop3Thread.setCustomerId(customerId);
        getHdDataTop3Thread.start();
    }

    /**
     * 获取用户信息
     */
    void getBasicInfo(){
        @SuppressLint("HandlerLeak") Handler handler = new Handler() {
            public void handleMessage(Message msg) {

                if(msg.what==ApiConstant.MSG_API_HANDLER) {
                    String result = getBasicInfoThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)) {
                        JSONObject response = JSONObject.fromObject(result);
                        int code = response.getInt("code");
                        String message = response.getString("message");
                        System.out.println(message);
                        if (code == 200) {
                            String data = response.getString("Data");
                            JSONObject jsonObject = JSONObject.fromObject(data);

                            idCord = jsonObject.getString("IdNumber");
//                            getMachineData();
                                getContactInfo();

                        } else {
                            UiUtil.showToast(context, message);
                        }

                    }
                }
            };
        };
        getBasicInfoThread = new DoctorSHThread(
                ApiConstant.GETCUSTOMERBASICINFO, handler, context);
        getBasicInfoThread.setDoctorId(doctorId);
        getBasicInfoThread.setCustomerId(customerId);
        getBasicInfoThread.start();
    }
    void getContactInfo(){
        @SuppressLint("HandlerLeak") Handler handler = new Handler() {
            public void handleMessage(Message msg) {

                if(msg.what==ApiConstant.MSG_API_HANDLER) {
                    String result = getContactThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)) {
                        JSONObject response = JSONObject.fromObject(result);
                        int code = response.getInt("code");
                        String message = response.getString("message");
                        System.out.println(message);
                        if (code == 200) {
                            String data = response.getString("Data");
                            JSONObject jsonObject = JSONObject.fromObject(data);
//                            phone=jsonObject.getString("TelPhone");
//                            if(phone.length()<1){
                                phone=jsonObject.getString("MobilePhone");

//                                if(phone.length()<1){
//                                    phone=jsonObject.getString("ElsePhone");
//                                }else {
//                                    phone=null;
//                                }
//                            }
                                getMachineData();

                        } else {
                            UiUtil.showToast(context, message);
                        }

                    }
                }
            };
        };
        getContactThread = new DoctorSHThread(
                ApiConstant.GETCUSTOMERCONTACTINFO, handler, context);
        getContactThread.setDoctorId(doctorId);
        getContactThread.setCustomerId(customerId);
        getContactThread.start();
    }

    void getMachineData(){
        machineDataArray=null;

        @SuppressLint("HandlerLeak") Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getMachineDataThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            machineDataArray=jsonObject.getJSONArray("DataList");
                            isVisables=new boolean[machineDataArray.size()];
                            if(machineDataArray.size()>0){
                                isVisables[0]=true;
                            }
                            machineDataAdapter.setIsVisables(isVisables);
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }

                    machineDataAdapter.setArray(machineDataArray);
                    machineDataAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(machineDataLv);
                }
            }
        };
        getMachineDataThread=new DoctorSHThread(ApiConstant.GETSHHEALTHDATALIST,handler,context);
        getMachineDataThread.setDoctorId(doctorId);
        getMachineDataThread.setIdCord(idCord);
        getMachineDataThread.setMobile(phone);
        getMachineDataThread.setPageIndex(0);
        getMachineDataThread.setPageSize(10);
        getMachineDataThread.start();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void onCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    Integer.parseInt(phone));
        } else {
            startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:"+phone)));
        }
    }

    @Override
    public void contrast(int pos) {
        JSONObject jsonObject=machineDataArray.getJSONObject(pos);
        String time=jsonObject.getString("MeasureTime");
        Intent intent=new Intent(context,ContrastDataActivity.class);
        intent.putExtra(RECORDDATE,time.split(" ")[0]);
        intent.putExtra(IDCORD,idCord);
        intent.putExtra(PHONE,phone);
        startActivity(intent);
    }

    @Override
    public void record(int pos) {
        JSONObject jsonObject=machineDataArray.getJSONObject(pos);
        String time=jsonObject.getString("MeasureTime");
        Intent intent=new Intent(context,MachineDataActivity.class);
        intent.putExtra(RECORDDATE,time.split(" ")[0]);
        intent.putExtra(IDCORD,idCord);
        intent.putExtra(PHONE,phone);
        startActivity(intent);
    }

    @Override
    public void changeState(int pos) {
            isVisables[pos]=!isVisables[pos];
            machineDataAdapter.setIsVisables(isVisables);
            machineDataAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(machineDataLv);
    }

    public void payAttention(final String payAttention){
        @SuppressLint("HandlerLeak") Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=payAttentionThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            if (payAttention.equals("true")){
                                isPayIgv.setImageResource(R.drawable.img_heart);
                                attention = "false";
                            } else {
                                isPayIgv.setImageResource(R.drawable.img_heart_empty);
                                attention = "true";
                            }
                        }else {

                        }
                    }
                }
            }
        };
        payAttentionThread  = new DoctorSHThread(ApiConstant.SETCUSTOMERPAYATTENTION,handler,context);
        payAttentionThread.setDoctorId(MyApplication.Doctor_ID);
        payAttentionThread.setCustomerId(customerId);
        payAttentionThread.setAttention(payAttention);
        payAttentionThread.start();
    }
}
