package com.jxj.jdoctorassistant.main.doctor.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.app.MyApplication;
import com.jxj.jdoctorassistant.bean.CommentBean;
import com.jxj.jdoctorassistant.main.doctor.adapter.CommentAdapter;
import com.jxj.jdoctorassistant.main.doctor.personal.DoctorPersonalInfoActivity;
import com.jxj.jdoctorassistant.main.doctor.personal.DoctorPersonalSetActivity;
import com.jxj.jdoctorassistant.main.doctor.personal.DoctorQrcodeActivity;
import com.jxj.jdoctorassistant.main.doctor.personal.NoticeListActivity;
import com.jxj.jdoctorassistant.main.doctor.personal.ServiceListActivity;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.PopularThread;
import com.jxj.jdoctorassistant.util.ImageUtil;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.RoundImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentPersonal extends Fragment {

    @ViewInject(R.id.fragment_doctor_personal_head_igv)
    private RoundImageView headIgv;
    @ViewInject(R.id.fragment_doctor_personal_score_tv)
    private TextView scoreTv;
    @ViewInject(R.id.fragment_doctor_personal_name_tv)
    private TextView nameTv;
    @ViewInject(R.id.fragment_doctor_personal_hospital_tv)
    private TextView hospitalTv;
    @ViewInject(R.id.fragment_doctor_personal_jobtitle_tv)
    private TextView jobTitleTv;
    @ViewInject(R.id.fragment_doctor_personal_info_tv)
    private TextView infoTv;
    @ViewInject(R.id.fragment_doctor_personal_evaluate_lv)
    private RecyclerView fragment_doctor_personal_evaluate_lv;
    

    @OnClick({R.id.qrcode_igv,R.id.set_igv,R.id.service_num_ll,R.id.notice_num_ll})
    void onClick(View view){
        switch (view.getId()){
            case R.id.qrcode_igv:
                startActivity(new Intent(context, DoctorQrcodeActivity.class));
                break;
            case R.id.set_igv:
                startActivity(new Intent(context, DoctorPersonalSetActivity.class));
                break;
            case R.id.service_num_ll:
                startActivity(new Intent(context, ServiceListActivity.class));
                break;
            case R.id.notice_num_ll:
                startActivity(new Intent(context, NoticeListActivity.class));
                break;
            default:

                break;
        }
    }

    private Context context;
    private SharedPreferences sp;
    private String info;
    private PopularThread downloadImageThread;
    CommentAdapter commentAdapter;
    private DoctorSHThread commentThread;
    List<CommentBean> list = new ArrayList<>();

    // 保存图片本地路径
//    public static final String ACCOUNT_DIR = Environment
//            .getExternalStorageDirectory().getPath() + "/jDcotorAssistant/";
//    public static final String ACCOUNT_MAINTRANCE_ICON_CACHE = "cardImages/";
//    private  String IMGPATH = ACCOUNT_DIR
//            + ACCOUNT_MAINTRANCE_ICON_CACHE;
//
//    private static final String IMAGE_FILE_NAME = "faceImage.jpeg";
//    // private static final String TMP_IMAGE_FILE_NAME = "tmp_faceImage.jpg";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_doctor_personal, container, false);
        ViewUtils.inject(this,view);

        context=getActivity();
        sp=context.getSharedPreferences(AppConstant.USER_sp_name,Context.MODE_PRIVATE);
        info=sp.getString(AppConstant.USER_doctor_info,null);
        initView();
        fragment_doctor_personal_evaluate_lv= view.findViewById(R.id.fragment_doctor_personal_evaluate_lv);
        initData();
        return view;
    }

    void initView(){

        if(info!=null){
            JSONObject jsonObject=JSONObject.fromObject(info);
            String photo=jsonObject.getString("Photo");
            downloadImage(photo);
            nameTv.setText(jsonObject.getString("Name"));
            hospitalTv.setText(jsonObject.getString("Hospital"));
            jobTitleTv.setText(jsonObject.getString("Title"));
            infoTv.setText("擅长："+jsonObject.getString("Resume"));
            scoreTv.setText(jsonObject.getInt("Score")+"分");

        }

        headIgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, DoctorPersonalInfoActivity.class));
            }
        });

    }
    //获取评论列表
    public void initData(){
        @SuppressLint("HandlerLeak") Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result= commentThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            JSONArray jsonArray = jsonObject.getJSONArray("DataList");
                            Gson gson = new Gson();
                            list = gson.fromJson(jsonArray.toString(),new TypeToken<List<CommentBean>>(){}.getType());
                            commentAdapter = new CommentAdapter(list,context);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                            fragment_doctor_personal_evaluate_lv.setLayoutManager(layoutManager);
                            fragment_doctor_personal_evaluate_lv.setAdapter(commentAdapter);
                        }

                    }
                }
            }
        };
        commentThread = new DoctorSHThread(ApiConstant.GETEVALUATIONLIST,handler,context);
        commentThread.setDoctorId(MyApplication.Doctor_ID);
        commentThread.setPageIndex(0);
        commentThread.setPageSize(10);
        commentThread.start();
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
                        }

                    }
                }
            }
        };
        downloadImageThread=new PopularThread(ApiConstant.DOWNLOADIMAGE,handler,context);
        downloadImageThread.setImage(image);
        downloadImageThread.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == 1) {
//			System.out.println("***********返回我的界面");
            switch (requestCode) {
                case 1:
//				Bundle data = intent.getExtras();
//				Bitmap bitmap = (Bitmap) data.get("myBitmap");
////				filename = data.getString("filename");
////
////				System.out.println("***********" + filename);
//
//				myHeadPicture.setImageBitmap(bitmap);
//
////				int mark=data.getInt("mark");
////				if(mark==5){
//                    getPhotoName();
//				}
                    break;

                default:
                    break;
            }
        }
    }
}
