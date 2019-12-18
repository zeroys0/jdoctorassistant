package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.DataInputGridAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.app.MyApplication;
import com.jxj.jdoctorassistant.takePic.BaseActivity;
import com.jxj.jdoctorassistant.takePic.ImageUtils;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.IprecareThread;
import com.jxj.jdoctorassistant.thread.UploadImageThread;
import com.jxj.jdoctorassistant.util.BitmapCompress;
import com.jxj.jdoctorassistant.util.ImageUtil;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.DateDialog;
import com.jxj.jdoctorassistant.view.MyGridView;
import com.jxj.jdoctorassistant.view.TimeDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import net.sf.json.JSONObject;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataInputActivity extends BaseActivity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.data_input_title)
    private TextView titleTv;
    @ViewInject(value = R.id.right_btn_tv,parentId = R.id.data_input_title)
    private TextView recordTv;

    @ViewInject(R.id.data_type_gv)
    MyGridView dataTypeGv;
    @ViewInject(R.id.time_tv)
    private TextView timeTv;
    @ViewInject(R.id.data_type_ll)
    private LinearLayout dataInputLL;
//    @ViewInject(R.id.data_input_heart_rate_ll)
//    private LinearLayout heartRateLL;
//    @ViewInject(R.id.data_input_heart_rate_etv)
//    private EditText heartRateEtv;
//    @ViewInject(R.id.data_input_blood_pressure_ll)
//    private LinearLayout bloodPressureLL;
//    @ViewInject(R.id.data_input_blood_pressure_etv)
//    private EditText bloodPressureEtv;
//    @ViewInject(R.id.data_input_blood_oxygen_ll)
//    private LinearLayout bloodOxygenLL;
//    @ViewInject(R.id.data_input_blood_oxygen_etv)
//    private EditText bloodOxygenEtv;
//    @ViewInject(R.id.data_input_step_ll)
//    private LinearLayout stepLL;
//    @ViewInject(R.id.data_input_step_etv)
//    private EditText stepEtv;
//    @ViewInject(R.id.data_input_calorie_ll)
//    private LinearLayout calorieLL;
//    @ViewInject(R.id.data_input_calorie_etv)
//    private EditText calorieEtv;
//    @ViewInject(R.id.data_input_blood_sugar_ll)
//    private LinearLayout bloodSugarLL;
//    @ViewInject(R.id.data_input_blood_sugar_etv)
//    private EditText bloodSugarEtv;
//    @ViewInject(R.id.data_input_temperature_ll)
//    private LinearLayout temperatureLL;
//    @ViewInject(R.id.data_input_temperature_etv)
//    private EditText temperatureEtv;
//    @ViewInject(R.id.data_input_weight_ll)
//    private LinearLayout weightLL;
//    @ViewInject(R.id.data_input_weight_etv)
//    private EditText weightEtv;
//    @ViewInject(R.id.data_input_fat_ll)
//    private LinearLayout fatLL;
//    @ViewInject(R.id.data_input_fat_etv)
//    private EditText fatEtv;
//    @ViewInject(R.id.data_input_water_ll)
//    private LinearLayout waterLL;
//    @ViewInject(R.id.data_input_water_etv)
//    private EditText waterEtv;
//    @ViewInject(R.id.data_input_muscle_ll)
//    private LinearLayout muscleLL;
//    @ViewInject(R.id.data_input_muscle_etv)
//    private EditText muscleEtv;
//    @ViewInject(R.id.data_input_bones_ll)
//    private LinearLayout bonesLL;
//    @ViewInject(R.id.data_input_bones_etv)
//    private EditText bonesEtv;
//    @ViewInject(R.id.data_input_metabolism_ll)
//    private LinearLayout metabolismLL;
//    @ViewInject(R.id.data_input_metabolism_etv)
//    private EditText metabolismEtv;

    @ViewInject(R.id.data_type_tv)
    private TextView dataTypeTv;
    @OnClick({R.id.back_igv,R.id.data_input_save_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.data_input_save_btn:
                saveData();
                break;
        }
    }

    private Context context;
    private DataInputGridAdapter adapter;

    private boolean[] arrBoolean;
    private String[] arrType;
    private String[] arrDataType=new String[14];
    private LinearLayout[] arrLL=new LinearLayout[13];
    private EditText[] arrEtv=new EditText[14];
    private StringBuffer typeSb=new StringBuffer();
    private List<String> typeList=new ArrayList<String>();
    private DoctorSHThread insertSHDataThread;
    private SharedPreferences sp;
    private String customerId;
    private int doctorId;
    private List<String> imagesList=new ArrayList<String>();
    private DateDialog dateDialog;
    private List<File> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_data_input);

        ViewUtils.inject(this);
        context=this;

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        customerId=sp.getString(AppConstant.USER_customerId,null);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);

        titleTv.setText(getResources().getStringArray(R.array.patient_function_arr)[2]);
        recordTv.setVisibility(View.VISIBLE);
        recordTv.setText(getResources().getString(R.string.history_record));
        recordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,DataInputRecordActivity.class));
            }
        });
        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        if(msg.what==AppConstant.MSG_DATEDIALOG){
                            timeTv.setText(dateDialog.getDate());
                        }
                    }
                };
                dateDialog=new DateDialog(context,handler);
                dateDialog.setDate();
            }
        });

        initLayout();
        initView();
    }
    void initLayout(){
        arrDataType[0]="PR";
        arrDataType[1]="PS";
        arrDataType[2]="PD";
        arrDataType[3]="BloodOxygen";
        arrDataType[4]="Step";
        arrDataType[5]="Calorie";
        arrDataType[6]="BloodGlucose";
        arrDataType[7]="Temperature";
        arrDataType[8]="Weight";
        arrDataType[9]="FatRate";
        arrDataType[10]="Moisture";
        arrDataType[11]="Muscle";
        arrDataType[12]="Bone";
        arrDataType[13]="BM";


        arrLL[0]=(LinearLayout)findViewById(R.id.data_input_heart_rate_ll);
        arrLL[1]=(LinearLayout)findViewById(R.id.data_input_blood_pressure_ll);
        arrLL[2]=(LinearLayout)findViewById(R.id.data_input_blood_oxygen_ll);
        arrLL[3]=(LinearLayout)findViewById(R.id.data_input_step_ll);
        arrLL[4]=(LinearLayout)findViewById(R.id.data_input_calorie_ll);
        arrLL[5]=(LinearLayout)findViewById(R.id.data_input_blood_sugar_ll);
        arrLL[6]=(LinearLayout)findViewById(R.id.data_input_temperature_ll);
        arrLL[7]=(LinearLayout)findViewById(R.id.data_input_weight_ll);
        arrLL[8]=(LinearLayout)findViewById(R.id.data_input_fat_ll);
        arrLL[9]=(LinearLayout)findViewById(R.id.data_input_water_ll);
        arrLL[10]=(LinearLayout)findViewById(R.id.data_input_muscle_ll);
        arrLL[11]=(LinearLayout)findViewById(R.id.data_input_bones_ll);
        arrLL[12]=(LinearLayout)findViewById(R.id.data_input_metabolism_ll);

        arrEtv[0]=(EditText) findViewById(R.id.data_input_heart_rate_etv);
        arrEtv[1]=(EditText) findViewById(R.id.data_input_ps_etv);
        arrEtv[2]=(EditText) findViewById(R.id.data_input_pd_etv);
        arrEtv[3]=(EditText) findViewById(R.id.data_input_blood_oxygen_etv);
        arrEtv[4]=(EditText) findViewById(R.id.data_input_step_etv);
        arrEtv[5]=(EditText) findViewById(R.id.data_input_calorie_etv);
        arrEtv[6]=(EditText) findViewById(R.id.data_input_blood_sugar_etv);
        arrEtv[7]=(EditText) findViewById(R.id.data_input_temperature_etv);
        arrEtv[8]=(EditText) findViewById(R.id.data_input_weight_etv);
        arrEtv[9]=(EditText) findViewById(R.id.data_input_fat_etv);
        arrEtv[10]=(EditText) findViewById(R.id.data_input_water_etv);
        arrEtv[11]=(EditText) findViewById(R.id.data_input_muscle_etv);
        arrEtv[12]=(EditText) findViewById(R.id.data_input_bones_etv);
        arrEtv[13]=(EditText) findViewById(R.id.data_input_metabolism_etv);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        //这里是赋予  image 的图片地址的地方

        String imagePath = "";
        if (requestCode == SELECT_IMAGE_RESULT_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {//有数据返回直接使用返回的图片地址






                imagePath = ImageUtils.getFilePathByFileUri(this, data.getData());

            } else {//无数据使用指定的图片路径
                imagePath = mImagePath;
            }

            Bitmap bitmap = ImageUtils.getImage(imagePath);
//            uploadImages(bitmap);

            mOnFragmentResult.onResult(imagePath);
            File file = BitmapCompress.compressImage(bitmap);
            list.add(file);

        }
    }


        //delete
    void uploadImages(Bitmap bitmap){
        final UploadImageThread uploadPhotoThread=new UploadImageThread(context);
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = uploadPhotoThread.getResult();
//							Log.e("webservice result",result);
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject json = JSONObject.fromObject(result);
                        int code = json.getInt("code");
                        if (code == 200) {
                            String photo = json.getString("Data");
//                            imagesList.add("\""+photo+"\"");
                            imagesList.add(photo);
                        } else {
                            UiUtil.showToast(context,
                                    json.getString("message"));
                        }
                    }

                }

            };
        };

        uploadPhotoThread.setHandler(handler);
        uploadPhotoThread.setImage(ImageUtil.getStream(bitmap));
        uploadPhotoThread.start();



    }

    void initView(){

        arrType =getResources().getStringArray(R.array.data_input);
        adapter=new DataInputGridAdapter(context);
        adapter.setArr(arrType);
        arrBoolean =new boolean[arrType.length];
        adapter.setBoolArr(arrBoolean);
        dataTypeGv.setAdapter(adapter);
        dataTypeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(arrBoolean[position]){
                    arrBoolean[position]=false;
                    arrLL[position].setVisibility(View.GONE);
                    if(typeList.size()>0&&typeList.contains(arrType[position])){
                        typeList.remove(arrType[position]);
                    }

                }else{
                    arrBoolean[position]=true;
                    arrLL[position].setVisibility(View.VISIBLE);
                    typeList.add(arrType[position]);
                }
                if(typeList.size()>0){
                    dataInputLL.setVisibility(View.VISIBLE);
                }else {
                    dataInputLL.setVisibility(View.GONE);
                }



//                switch (position){
//                    case 0:
//                        if(arrBoolean[position]){
//                            heartRateLL.setVisibility(View.VISIBLE);
//                        }else {
//                            heartRateLL.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 1:
//                        if(arrBoolean[position]){
//                            bloodPressureLL.setVisibility(View.VISIBLE);
//                        }else {
//                            bloodPressureLL.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 2:
//                        if(arrBoolean[position]){
//                            bloodOxygenLL.setVisibility(View.VISIBLE);
//                        }else {
//                            bloodOxygenLL.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 3:
//                        if(arrBoolean[position]){
//                            temperatureLL.setVisibility(View.VISIBLE);
//                        }else {
//                            temperatureLL.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 4:
//                        if(arrBoolean[position]){
//                            weightLL.setVisibility(View.VISIBLE);
//                        }else {
//                            weightLL.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 5:
//                        if(arrBoolean[position]){
//                            fatLL.setVisibility(View.VISIBLE);
//                        }else {
//                            fatLL.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 6:
//                        if(arrBoolean[position]){
//                            heartRateLL.setVisibility(View.VISIBLE);
//                        }else {
//                            heartRateLL.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 0:
//                        if(arrBoolean[position]){
//                            heartRateLL.setVisibility(View.VISIBLE);
//                        }else {
//                            heartRateLL.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 0:
//                        if(arrBoolean[position]){
//                            heartRateLL.setVisibility(View.VISIBLE);
//                        }else {
//                            heartRateLL.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 0:
//                        if(arrBoolean[position]){
//                            heartRateLL.setVisibility(View.VISIBLE);
//                        }else {
//                            heartRateLL.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 0:
//                        if(arrBoolean[position]){
//                            heartRateLL.setVisibility(View.VISIBLE);
//                        }else {
//                            heartRateLL.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 0:
//                        if(arrBoolean[position]){
//                            heartRateLL.setVisibility(View.VISIBLE);
//                        }else {
//                            heartRateLL.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 0:
//                        if(arrBoolean[position]){
//                            heartRateLL.setVisibility(View.VISIBLE);
//                        }else {
//                            heartRateLL.setVisibility(View.GONE);
//                        }
//                        break;
//                }
                adapter.setBoolArr(arrBoolean);
                adapter.notifyDataSetChanged();
                int length=typeList.toString().length();
                if(length>1){
                    dataTypeTv.setText(typeList.toString().substring(1,length-1));
                }else {
                    dataTypeTv.setText("没有选择数据");
                }

            }
        });
    }

    void saveData(){
        for(int i=0;i<imagesList.size();i++){
            System.out.println("<<"+i+">> 图片名称："+imagesList.get(i));
        }
        System.out.println("图片数组:"+imagesList.toString());

        Map map=new HashMap();

        map.put("CustomerId",customerId);
        map.put("DoctorId",doctorId+"");
        map.put("TestDate",timeTv.getText().toString().trim());
        for(int i=0;i<arrBoolean.length;i++){
            if(arrBoolean[i]){
                if(i==0){
                    map.put(arrDataType[i],arrEtv[i].getEditableText().toString().trim());
                }
                else if (i==1){
                    map.put(arrDataType[i],arrEtv[i].getEditableText().toString().trim());
                    map.put(arrDataType[i+1],arrEtv[i+1].getEditableText().toString().trim());
                }else {
                    map.put(arrDataType[i+1],arrEtv[i+1].getEditableText().toString().trim());
                }

            }
        }
        map.put("images",imagesList);
        JSONObject object=JSONObject.fromObject(map);

        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=insertSHDataThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        UiUtil.showToast(context,jsonObject.getString("message"));
                    }else {

                    }
                }
            }
        };
        insertSHDataThread=new DoctorSHThread(ApiConstant.INSERTINTOSHINPUTDATA,handler,context);
        insertSHDataThread.setInputData(object.toString());
        insertSHDataThread.start();
        HttpParams paramsf = new HttpParams();
        int i = 1;
        for (File file:list) {
            paramsf.put("file"+i,file);
            i++;
        }
//        paramsf.putFileParams("file",list);

        OkGo.<String>post("http://122.225.60.118:6280/api/inputdata/upload")
                .tag(this)
                .params("customerId", customerId)
                .params("key", MyApplication.key)
                .params(paramsf)
                .isMultipart(true)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            org.json.JSONObject json = new org.json.JSONObject(response.body());
                            Log.d("onSuccess:上传图片 ",json.getString("message"));
                            Toast.makeText(context, json.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
