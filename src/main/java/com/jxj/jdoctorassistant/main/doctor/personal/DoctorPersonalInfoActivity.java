package com.jxj.jdoctorassistant.main.doctor.personal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.main.register.JobTitleListActivity;
import com.jxj.jdoctorassistant.takePic.BaseActivity;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.PopularThread;
import com.jxj.jdoctorassistant.util.ImageUtil;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.RoundImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.cache.LruDiskCache;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;

import java.io.File;


public class DoctorPersonalInfoActivity extends BaseActivity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.doctor_personal_info_title)
    private TextView titleTv;
    @ViewInject(value = R.id.right_btn_tv,parentId = R.id.doctor_personal_info_title)
    private TextView save_btn;
    @ViewInject(R.id.name_et)
    private EditText nameEtv;
    @ViewInject(R.id.hospital_tv)
    private TextView hospitalTv;
    @ViewInject(R.id.department_etv)
    private EditText departmentEtv;
    @ViewInject(R.id.job_title_tv)
    private TextView jobTitleTv;
    @ViewInject(R.id.phone_et)
    private EditText phoneEtv;
    @ViewInject(R.id.expert_etv)
    private EditText expertEtv;
    @ViewInject(R.id.introduction_et)
    private EditText introductonEtv;



    @ViewInject(R.id.head_igv)
    private RoundImageView headIgv;

    @OnClick({R.id.back_igv,R.id.save_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.save_btn:
                perfectInfo();
                break;
        }
    }

    private Context context;
    private Uri uri;
    private String path;
    private int doctorId;
    private SharedPreferences sp;
//    private String photo;

    //图片裁剪
    private static final int PHOTO_CROP_CODE = 3;

//    private final static int hospitalCode=0x01;
//    private final static int departmentCode=0x02;
    private final static int jobTitleCode=0x03;
    private final static int REQUESTCODE=205;
    private DoctorSHThread updateDoctorInfoThread,setPhotoThread;
    private PopularThread downloadImageThread,uploadImageThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_doctor_personal_info);
        ViewUtils.inject(this);

        context=this;

        titleTv.setText(getResources().getString(R.string.personal_information));
        save_btn.setVisibility(View.VISIBLE);
        save_btn.setText("编辑");
        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        if(sp.contains(AppConstant.USER_doctor_info)){
            String str=sp.getString(AppConstant.USER_doctor_info,null);
            if(str!=null){
                JSONObject json=JSONObject.fromObject(str);
                String photo=json.getString("Photo");
                downloadImage(photo);
                nameEtv.setText(json.getString("Name"));
                hospitalTv.setText(json.getString("Hospital"));
                departmentEtv.setText(json.getString("Department"));
                jobTitleTv.setText(json.getString("Title"));
                phoneEtv.setText(json.getString("Tel"));
                expertEtv.setText(json.getString("Adept"));
                introductonEtv.setText(json.getString("Resume"));
            }
        }

        headIgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDailog();
            }
        });

        hospitalTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(context,HospitalAddressListActivity.class),REQUESTCODE);
            }
        });


        jobTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(context,JobTitleListActivity.class);
                startActivityForResult(intent,jobTitleCode);
            }
        });

    }

    void uploadImage(Bitmap bitmap){
        Handler handler= new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = uploadImageThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject json = JSONObject
                                .fromObject(result);
                        int code = json.getInt("code");
                        String photo = json.getString("Data");
                        if (code == 200) {
                            setPhoto(photo);
                        } else {
                            UiUtil.showToast(context,
                                    json.getString("message"));
                        }
                    }

                }

            };
        };

        uploadImageThread = new PopularThread(
                ApiConstant.UPLOADIMAGE, handler,
                context);
        uploadImageThread.setImage(ImageUtil.getStream(bitmap));
        uploadImageThread.start();
    }
    void setPhoto(final String photo){
        // 设置医生头像
        Handler handler= new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0x133) {
                    String result = setPhotoThread.getResult();
                    // System.out.println(result);
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject json = JSONObject
                                .fromObject(result);
                        int code = json.getInt("code");
                        if (code == 200) {
                            UiUtil.showToast(context,
                                    json.getString("message"));
                            // 得到上传的图片，并且保存显示
                            downloadImage(photo);

                        }
                    }

                }
            };
        };

        setPhotoThread = new DoctorSHThread(
                ApiConstant.SETDOCTORPHOTO,
                handler, context);
        setPhotoThread.setDoctorId(doctorId);
        setPhotoThread.setPhoto(photo);
        setPhotoThread.start();
    }
    void downloadImage(String image){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result= downloadImageThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            String data=jsonObject.getString("Data");
                            headIgv.setImageBitmap(ImageUtil.getBitmap(data));
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                            headIgv.setImageDrawable(getResources().getDrawable(R.drawable.head_picture_man));
                        }

                    }
                }
            }
        };
        downloadImageThread=new PopularThread(ApiConstant.DOWNLOADIMAGE,handler,context);
        downloadImageThread.setImage(image);
        downloadImageThread.start();
    }

//    void uploadImage(String image){
//        Handler handler=new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                if(msg.what== ApiConstant.MSG_API_HANDLER){
//                    String result= downloadImageThread.getResult();
//                    if(UiUtil.isResultSuccess(context,result)){
//                        JSONObject jsonObject=JSONObject.fromObject(result);
//                        int code=jsonObject.getInt("code");
//                        if(code==200){
//                            String data=jsonObject.getString("Data");
//                            headIgv.setImageBitmap(ImageUtil.getBitmap(data));
//                        }
//
//                    }
//                }
//            }
//        };
//        uploadImageThread=new PopularThread(ApiConstant.UPLOADIMAGE,handler,context);
//        uploadImageThread.setImage(image);
//        uploadImageThread.start();
//    }

    void perfectInfo(){
        String name=nameEtv.getEditableText().toString().trim();
        if(name.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.doctor_name_hint));
            return;
        }
        String hospital=hospitalTv.getText().toString().trim();
        if(hospital.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.doctor_hospital_hint));
            return;
        }
        String department=departmentEtv.getText().toString().trim();
        if(department.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.doctor_department_hint));
            return;
        }
        String title=jobTitleTv.getText().toString().trim();
        if(title.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.doctor_title_hint));
            return;
        }
        String tel=phoneEtv.getEditableText().toString().trim();
        if(tel.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.doctor_phone_hint));
            return;
        }
        String adept=expertEtv.getEditableText().toString().trim();
        if(adept.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.doctor_adept_hint));
            return;
        }
        String rusume=introductonEtv.getEditableText().toString().trim();
        if(rusume.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.doctor_resume_hint));
            return;
        }
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=updateDoctorInfoThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
//                        if(code==200){
//                            startActivity(new Intent(context,DoctorCertificateActivity.class));
//                            loaded();
//                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
//                        }
                    }
                }
            }
        };
        updateDoctorInfoThread=new DoctorSHThread(ApiConstant.UPDATEDOCTORINFO,handler,context);
        updateDoctorInfoThread.setDoctorId(doctorId);
        updateDoctorInfoThread.setNamee(name);
        updateDoctorInfoThread.setHospital(hospital);
        updateDoctorInfoThread.setDepartment(department);
        updateDoctorInfoThread.setTitle(title);
        updateDoctorInfoThread.setTel(tel);
        updateDoctorInfoThread.setAdept(adept);
        updateDoctorInfoThread.setResume(rusume);
        updateDoctorInfoThread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("requestCode:"+requestCode+"  resultCode:"+resultCode);
        //这里是赋予  image 的图片地址的地方
        if(requestCode==REQUESTCODE){
            if(resultCode==HospitalAddressListActivity.RESULTCODE){
                String hospiatl=data.getStringExtra(HospitalAddressListActivity.HOSPITALNAME);
                hospitalTv.setText(hospiatl);
            }
        }
        if(resultCode==JobTitleListActivity.RESULTCODE){
            Bundle bundle=data.getExtras();
            switch (requestCode){
//                case hospitalCode:
//                    String hospital=bundle.getString("hospitalName");
//                    hospitalTv.setText(hospital);
//                    break;
//                case departmentCode:
//                    String department=bundle.getString("department");
//                    departmentTv.setText(department);
//                    break;
                case jobTitleCode:
                    String jobTitle=bundle.getString(JobTitleListActivity.JOBTITLE);
                    jobTitleTv.setText(jobTitle);
                    break;
                default:
                    break;
            }
        }
//        String imagePath = "";
        if (requestCode == SELECT_IMAGE_RESULT_CODE && resultCode == RESULT_OK) {
//            System.out.println("选择图片及拍照  返回结果 data:"+data.getData());
            if (data != null && data.getData() != null) {//有数据返回直接使用返回的图片地址
                uri=data.getData();
                path=uriToFilePath(uri);
//                startPhotoZoom(uri,PHOTO_CROP_CODE);

//                imagePath = ImageUtils.getFilePathByFileUri(this, data.getData());

            } else {//无数据使用指定的图片路径
                uri=Uri.fromFile(new File(mImagePath));
                path=mImagePath;
//                startPhotoZoom(uri,PHOTO_CROP_CODE);
//                imagePath = mImagePath;
            }
            startPhotoZoom(uri,PHOTO_CROP_CODE);
//            startPhotoZoom(data.getData(),PHOTO_CROP_CODE);

//            Bitmap bitmap = ImageUtils.getImage(imagePath);
//            headIgv.setImageBitmap(bitmap);
//            uploadImages(bitmap);
//            mOnFragmentResult.onResult(imagePath);


        }else if (requestCode == PHOTO_CROP_CODE&& resultCode == RESULT_OK) {
            System.out.println("裁剪图片  返回结果 data:"+data.getData()+"图片路径 ："+path);
//            if (data != null && data.getData() != null) {//有数据返回直接使用返回的图片地址
//
//
//                imagePath = ImageUtils.getFilePathByFileUri(this, data.getData());
//
//            } else {//无数据使用指定的图片路径
//                imagePath = mImagePath;
//            }
            if(uri!=null) {
                System.out.println("图片路径 uri：" + uri);
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (bitmap != null) {
                    //这里可以把图片进行上传到服务器操作
                    headIgv.setImageBitmap(bitmap);
                    uploadImage(bitmap);
                }
            }
        }
    }

    private void startPhotoZoom(Uri uri, int REQUE_CODE_CROP) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // 去黑边
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // aspectX aspectY 是宽高的比例，根据自己情况修改
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高像素
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        //取消人脸识别功能
        intent.putExtra("noFaceDetection", true);
        //设置返回的uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //设置为不返回数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUE_CODE_CROP);
    }

    /**
     * @param
     * @description 把Uri转换为文件路径
     * @author ldm
     * @time 2016/11/30 15:22
     */
    private String uriToFilePath(Uri uri) {
        //获取图片数据
        String[] proj = {MediaStore.Images.Media.DATA};
        //查询
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        //获得用户选择的图片的索引值
        int image_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        //返回图片路径
        return cursor.getString(image_index);
    }

}
