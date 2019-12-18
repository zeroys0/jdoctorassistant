package com.jxj.jdoctorassistant.main.doctor.personal;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.PopularThread;
import com.jxj.jdoctorassistant.util.ImageUtil;
import com.jxj.jdoctorassistant.util.QRCodeUtil;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class DoctorQrcodeActivity extends Activity {
    @ViewInject(R.id.title_tv)
    private TextView titleTv;

    @ViewInject(R.id.qrcode_igv)
    private ImageView qrCodeIgv;

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

//     保存图片本地路径
//    public static final String ACCOUNT_DIR = Environment
//            .getExternalStorageDirectory().getPath() + "/jDcotorAssistant/QrCode";

    private Context context;
    private SharedPreferences sp;
    private int doctorId;
    private String name;
    private PopularThread downloadImageThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_doctor_qrcode);
        ViewUtils.inject(this);

        context=this;

        titleTv.setText("我的二维码");

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        Log.d( "医生id: ",doctorId+"");
        if(sp.contains(AppConstant.USER_doctor_info)){
            String str=sp.getString(AppConstant.USER_doctor_info,null);
            if(str!=null){
                JSONObject json=JSONObject.fromObject(str);
                String photo=json.getString("Photo");
                name=json.getString("Name");

                try {
                    Bitmap bitmap=QRCodeUtil.createQRCode(name,200);
                    qrCodeIgv.setImageBitmap(bitmap);

                }catch (Exception e){
                    e.printStackTrace();
                }
//                downloadImage(photo);

            }
        }

//        QRCodeUtil.createQRImage("10000",200,200,null,ACCOUNT_DIR);


    }

//    void downloadImage(String image){
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
//                            try {
//                            Bitmap bitmap=QRCodeUtil.createQRCode(name,200);
//                            qrCodeIgv.setImageBitmap(bitmap);
//
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//
//
////                            headIgv.setImageBitmap(ImageUtil.getBitmap(data));
//                        }else {
//                            UiUtil.showToast(context,jsonObject.getString("message"));
////                            headIgv.setImageDrawable(getResources().getDrawable(R.drawable.head_picture_man));
//                        }
//
//                    }
//                }
//            }
//        };
//        downloadImageThread=new PopularThread(ApiConstant.DOWNLOADIMAGE,handler,context);
//        downloadImageThread.setImage(image);
//        downloadImageThread.start();
//    }

}
