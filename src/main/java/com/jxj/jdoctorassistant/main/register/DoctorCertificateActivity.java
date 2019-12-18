package com.jxj.jdoctorassistant.main.register;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.BitmapCompress;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.sf.json.JSONObject;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class DoctorCertificateActivity extends Activity {
    @ViewInject(value = R.id.title_tv)
    TextView titleTv;
    @ViewInject(R.id.doctor_certificate_igv)
    private ImageView certificateIgv;

    private PopupWindow popuPhoneW;
    private View popview;
    private Button btn_album,btn_photograph;
    private RelativeLayout rl_img;
    private ImageView img_certificate;
    @OnClick({R.id.back_igv,R.id.certificate_next_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.certificate_next_btn:
                if(file != null) {
                    submit();
                    startActivity(new Intent(context,ResultActivity.class));
                    finish();
                } else {
                    Toast.makeText(context, "请上传图片", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    private Context context;

    private File file;
//    private JAssistantAPIThread getPhotoThread, downloadPhotoThread;
//    private Handler handler_getPhoto, handler_downloadPhoto;
    private DoctorSHThread getLicenseThread;

    private String filename;

    //保存图片本地路径
    public static final String ACCOUNT_DIR = Environment.getExternalStorageDirectory().getPath()
            + "/jDoctorAssistant";
    public static final String ACCOUNT_MAINTRANCE_ICON_CACHE = "/cardImages/";
    private  String IMGPATH = ACCOUNT_DIR + ACCOUNT_MAINTRANCE_ICON_CACHE;
    private static final String IMAGE_FILE_NAME = "faceImage.jpeg";

    private int doctorId;
    private String fileStr;
    private byte [] bitmapByte;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_doctor_certificate);
        ViewUtils.inject(this);
        popu_head();
        context=this;
        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        titleTv.setText(getResources().getString(R.string.perfect_personal_info));

        certificateIgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popuPhoneW.showAtLocation(img_certificate, Gravity.CENTER, 0, 0);
                backgroundAlpha(0.5f);
            }
        });

        IMGPATH=IMGPATH+"/";

        String path = IMGPATH;
        File photofile = new File(path);
        if (!photofile.exists()) {
            photofile.mkdirs();
            return;
        }
        img_certificate = findViewById(R.id.img_certificate);
//        getPhotoName();
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
        getLicense();

}

    private void getLicense(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==ApiConstant.MSG_API_HANDLER){
                    String result=getLicenseThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            String data=jsonObject.getString("Data");
                            fileStr=data;
                            ByteArrayOutputStream baos=new ByteArrayOutputStream();
                            Bitmap bitmap=getBitmap(data);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            bitmapByte =baos.toByteArray();
                            // 显示图片
                            certificateIgv
                                    .setImageBitmap(getBitmap(data));
//                            certificateIgv.invalidate();
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }
                }

            }
        };
        getLicenseThread=new DoctorSHThread(ApiConstant.GETDOCTORLICENSE,handler,context);
        getLicenseThread.setDoctorId(doctorId);
        getLicenseThread.start();
    }


    public void submit (){
        OkGo.<String>post("http://122.225.60.118:6280/api/SHdoctorLicense/upload")
                .tag(this)
                .params("doctorId", "1000011")
                .params("key", "58f1d615-ed3c-457a-98fe-320dcdf08b74")
                .params("file1", file)
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

    // 将解密字符串转化为bitmap
    public Bitmap getBitmap(String bitmapString) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(bitmapString, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    private Bitmap bitmap = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 1:
                    Uri uri = data.getData();
                    bitmap = BitmapCompress.decodeUriBitmap(DoctorCertificateActivity.this, uri);
                    img_certificate.setImageBitmap(bitmap);
                    file = BitmapCompress.compressImage(bitmap);
                    break;
                case 2:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        bitmap = (Bitmap) bundle.get("data");
                        img_certificate.setImageBitmap(bitmap);
                        file = BitmapCompress.compressImage(bitmap);
                    }
                    break;
                case 3:

                    break;
                default:
                    break;
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void popu_head() {
        // TODO Auto-generated method stub
        popview = LayoutInflater.from(DoctorCertificateActivity.this).inflate(R.layout.popu_head, null);
        btn_album = (Button) popview.findViewById(R.id.btn_album);
        btn_photograph = (Button) popview.findViewById(R.id.btn_photograph);
        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popuPhoneW.dismiss();
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, 1);
            }
        });
        btn_photograph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popuPhoneW.dismiss();
                if (ContextCompat.checkSelfPermission(DoctorCertificateActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //当拒绝了授权后，为提升用户体验，可以以弹窗的方式引导用户到设置中去进行设置
                    new AlertDialog.Builder(DoctorCertificateActivity.this)
                            .setMessage("需要开启权限才能使用此功能")
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户到设置中去进行设置
                                    Intent intent = new Intent();
                                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                    startActivity(intent);

                                }
                            }).setNegativeButton("取消", null).create().show();
                } else {
                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent2, 2);
                }
            }
        });
        popuPhoneW = new PopupWindow(popview,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popuPhoneW.setFocusable(true);
        popuPhoneW.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popuPhoneW.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popuPhoneW.setOutsideTouchable(true);
        popuPhoneW.setBackgroundDrawable(new BitmapDrawable());
        popuPhoneW.setOnDismissListener(new poponDismissListener());
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            // Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }
    }
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
    }
}
