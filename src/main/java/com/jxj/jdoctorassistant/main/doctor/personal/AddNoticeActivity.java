package com.jxj.jdoctorassistant.main.doctor.personal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.AddNoticeUserGridAdapter;
import com.jxj.jdoctorassistant.adapter.doctor.AddNoticeUserSelectListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.takePic.BaseActivity;
import com.jxj.jdoctorassistant.takePic.ImageUtils;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.UploadImageThread;
import com.jxj.jdoctorassistant.util.ImageUtil;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.MyGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddNoticeActivity extends BaseActivity implements AddNoticeUserGridAdapter.Delegete{
    @ViewInject(value = R.id.title_tv,parentId = R.id.add_notice_title)
    private TextView titleTv;

    @ViewInject(R.id.add_notice_title_etv)
    private EditText titleEtv;
    @ViewInject(R.id.add_notice_content_etv)
    private EditText contentEtv;

    @ViewInject(R.id.add_notice_select_user_rg)
    private RadioGroup userRg;
    @ViewInject(R.id.add_notice_select_one_rb)
    private RadioButton oneRb;

    @ViewInject(R.id.add_notice_istop_cb)
    private CheckBox isTopCb;

//    @ViewInject(R.id.add_notice_user_gv)
//    private MyGridView userGv;


    @OnClick({R.id.back_igv,R.id.add_notice_finish_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.add_notice_finish_btn:
                addNotice();
            default:
                break;
        }
    }

    private Context context;
    private SharedPreferences sp;
    private int doctorId;
    private DoctorSHThread getUserListThread,addNoticeThread;
    private AddNoticeUserGridAdapter adapter;
    private AddNoticeUserSelectListAdapter selectAdapter;
//    private ArrayAdapter<String> arrayAdapter;
    private List<String> customerList;
    private JSONArray array;

    private boolean[] arrIsSelect;

    private final static int USER_ALL=0x01;
    private final static int USER_SC=0x02;
    private final static int USER_DE=0x03;
    private final static int USER_ONE=0x04;

    private int totype;

    private List<String> imagesList=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_notice);
        ViewUtils.inject(this);

        context=this;
        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);

        initView();
    }

    void initView(){
        titleTv.setText("公共发布");

        userRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int id) {
                switch (id){
                    case R.id.add_notice_select_all_rb:
                        totype=USER_ALL;

                        break;
                    case R.id.add_notice_select_sc_rb:
                        totype=USER_SC;

                        break;
                    case R.id.add_notice_select_de_rb:
                        totype=USER_DE;

                        break;
                    case R.id.add_notice_select_one_rb:
//                        showListDialog();
                        break;
                }
            }
        });

        oneRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totype=USER_ONE;
                showListDialog();
            }
        });

        adapter=new AddNoticeUserGridAdapter(context);

        selectAdapter=new AddNoticeUserSelectListAdapter(context);

        getUserList();
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

    void getUserList(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getUserListThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            array=jsonObject.getJSONArray("Data");
                            arrIsSelect=new boolean[array.size()];
                            selectAdapter.setArrIsSelect(arrIsSelect);
                            selectAdapter.setArray(array);
//                            selectAdapter.notifyDataSetChanged();
                        }else {
                            UiUtil.showToast(context,jsonObject.getString("message"));
                        }
                    }

                }
            }
        };
        getUserListThread=new DoctorSHThread(ApiConstant.GETALLCUSTOMER,handler,context);
        getUserListThread.setDoctorId(doctorId);
        getUserListThread.setCategory(AppConstant.CUSTOMER_ALL);
        getUserListThread.setCondition("");
        getUserListThread.setStartDate("");
        getUserListThread.setEndDate("");
        getUserListThread.start();
    }

    void addNotice(){
        String title=titleEtv.getEditableText().toString().trim();
        if(title.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.notice_title));
            return;
        }

        String content=contentEtv.getEditableText().toString().trim();
        if(content.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.notice_content));
            return;
        }


        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=addNoticeThread.getResult();
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
        addNoticeThread=new DoctorSHThread(ApiConstant.RELEASESHNOTICE,handler,context);
        addNoticeThread.setDoctorId(doctorId);
        addNoticeThread.setTitle(title);
        addNoticeThread.setContent(content);
        addNoticeThread.setImages(imagesList.toString());
        addNoticeThread.setTop(isTopCb.isChecked());
        addNoticeThread.setTotype(totype);
        addNoticeThread.setCustomerIdList("");
        addNoticeThread.start();
    }

    @Override
    public void delete(int position) {

    }

    void showListDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        Dialog dialog=null;
        View view = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.dialog_list, null);
        ListView lv=(ListView) view.findViewById(R.id.dialog_lv);
        lv.setAdapter(selectAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                arrIsSelect[position] = !arrIsSelect[position];

                selectAdapter.setArrIsSelect(arrIsSelect);
                selectAdapter.notifyDataSetChanged();
            }
        });
        // 设置date布局
        String title=null;
        builder.setView(view);
        builder.setTitle(title);
        builder.setNegativeButton(context.getResources().getString(R.string.cure),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.cancel();
                    }
                });

        builder.setPositiveButton(context.getResources().getString(R.string.cancle),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }
}
