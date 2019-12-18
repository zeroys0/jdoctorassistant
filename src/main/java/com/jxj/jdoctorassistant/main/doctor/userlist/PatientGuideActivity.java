package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.takePic.BaseActivity;
import com.jxj.jdoctorassistant.takePic.ImageUtils;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.UploadImageThread;
import com.jxj.jdoctorassistant.util.ImageUtil;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.TimeDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;

import java.util.ArrayList;

public class PatientGuideActivity extends BaseActivity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.patient_guide_title)
    private TextView titleTv;
    @ViewInject(value = R.id.right_btn_tv,parentId = R.id.patient_guide_title)
    private TextView recordTv;

    @ViewInject(R.id.patient_guide_etv)
    private EditText etv;

    @OnClick({R.id.back_igv,R.id.patient_guide_save_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.patient_guide_save_btn:
                subSuggestion();
                break;
        }
    }

    private DoctorSHThread subSuggestionThread;
    private Context context;
    private SharedPreferences sp;
    private int doctorId;
    private String customerId;

    private ArrayList<String> imagesList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_patient_guide);
        ViewUtils.inject(this);
        context=this;

        titleTv.setText(getResources().getString(R.string.suggestion_guide));

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        customerId=sp.getString(AppConstant.USER_customerId,null);

        recordTv.setVisibility(View.VISIBLE);
        recordTv.setText(getResources().getString(R.string.history_record));
        recordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,SuggestionRecordActivity.class));
            }
        });


    }

    void subSuggestion(){
        String suggestion=etv.getEditableText().toString().trim();
        if(suggestion.length()<1){
            return;
        }

        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==ApiConstant.MSG_API_HANDLER){
                    String result=subSuggestionThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        UiUtil.showToast(context,jsonObject.getString("message"));
                    }
                }
            }
        };
        subSuggestionThread=new DoctorSHThread(ApiConstant.SUBMISSIONSHSUGGESTION,handler,context);
        subSuggestionThread.setDoctorId(doctorId);
        subSuggestionThread.setCustomerId(customerId);
        subSuggestionThread.setSuggestion(suggestion);
        subSuggestionThread.setImages(imagesList.toString());
        subSuggestionThread.start();
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
            uploadImages(bitmap);
            mOnFragmentResult.onResult(imagePath);


        }
    }

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
//                            "\""+s1+"\""
                            imagesList.add("\""+photo+"\"");
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
}
