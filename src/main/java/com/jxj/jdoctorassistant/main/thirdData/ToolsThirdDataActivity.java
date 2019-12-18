package com.jxj.jdoctorassistant.main.thirdData;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.view.CircleLayout;

public class ToolsThirdDataActivity extends Activity {
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.right_btn_igv)
    ImageView thirdDataIgv;
    @Bind(R.id.third_data_ll)
    LinearLayout thirdDataLL;

    @OnClick({R.id.back_igv})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
            break;
        }
    }
    CircleLayout thirdDataCL;

    private Context context;

    private Dialog selectDialog;

    Display curDisplay ;
    int displayWidth,displayHeight;

    private int[] mItemImgs=new int[]{R.drawable.third_data_bg_bg,
            R.drawable.third_data_bo_bg,
            R.drawable.third_data_bp_bg,
            R.drawable.third_data_bf_bg,
            R.drawable.third_data_bt_bg,
    };

    private JAssistantAPIThread getThirdDataThread;
    private SharedPreferences sp;
    private String customerId;


    public static final String CUSTOMERID="customer_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tools_third_data);
        ButterKnife.bind(this);

        context=this;

        curDisplay = getWindowManager().getDefaultDisplay();//获取屏幕当前分辨�?
        displayWidth = curDisplay.getWidth();
        displayHeight = curDisplay.getHeight();

        thirdDataIgv.setVisibility(View.VISIBLE);
        thirdDataIgv.setImageResource(R.drawable.third_data);
        thirdDataIgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        customerId=sp.getString(AppConstant.USER_customerId,null);


        showDialog();
    }

    private void switchFragment(int i){

        titleTv.setText(getResources().getStringArray(R.array.third_data_array)[i]);

        Fragment fragment=null;
        Bundle bundle=new Bundle();
        bundle.putString(CUSTOMERID,customerId);


        switch(i){
            case 0:
                fragment=new FragmentThirdDataBg();
            break;
            case 1:
                fragment=new FragmentThirdDataBo();
                break;
            case 2:
                fragment=new FragmentThirdDataBp();
                break;
            case 3:
                fragment=new FragmentThirdDataBf();
                break;
            case 4:
                fragment=new FragmentThirdDataBt();
                break;
            default:
                break;

        }

        fragment.setArguments(bundle);
        FragmentTransaction transaction=this.getFragmentManager().beginTransaction();
        transaction.replace(R.id.third_data_ll,fragment);
        transaction.commitAllowingStateLoss();



    }

//    private void getThirdData(final int type){
//
//        titleTv.setText(getResources().getStringArray(R.array.third_data_array)[type]);
//
//        Handler handler=new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                if(msg.what== AppConstant.MSG_03){
//                    String result=getThirdDataThread.getResult();
//                    if(UiUtil.isResultSuccess(context,result)){
//                        JSONObject json=JSONObject.fromObject(result);
//                        int code=json.getInt("code");
//                        if(code==200){
//                            datalist=json.getString("DataList");
//                            System.out.println("三方数据:"+datalist);
//                            switchFragment(type);
//
//                        }
//
//                    }
//                }
//            }
//        };
//
//        getThirdDataThread=new iPreCareServiceThread(AppConstant.GETTHIRDPARTYDATA,handler,context);
//        getThirdDataThread.setCustomerId(String.valueOf(customerId));
//        getThirdDataThread.setPageIndex(0);
//        getThirdDataThread.setPageSize(10);
//        getThirdDataThread.setType(type+1);
//        getThirdDataThread.start();
//
//    }

    private void showDialog(){
        View view= LayoutInflater.from(context).inflate(R.layout.view_third_data_select,null);
//        view.setMinimumHeight((int)(displayHeight*3/5));
//        view.setMinimumWidth((int)(displayWidth*5/6));
        selectDialog=new Dialog(context);

//        selectDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        selectDialog.getWindow().setLayout((int)(displayWidth*2/3),(int)(displayHeight*3/5));
        selectDialog.setContentView(view);
        Window window=selectDialog.getWindow();
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        window.setGravity(Gravity.CENTER);
        layoutParams.height=(int)(displayHeight*3/5);
        layoutParams.width=(int)(displayWidth*5/6);
        window.setBackgroundDrawable(new ColorDrawable(0));


        String[] mItemTexts=getResources().getStringArray(R.array.third_data_array);

        thirdDataCL=(CircleLayout) view.findViewById(R.id.third_data_cl);
        thirdDataCL.setMenuItemIconsAndTexts(mItemImgs,mItemTexts);

        thirdDataCL.setOnMenuItemClickListener(new CircleLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                switchFragment(pos);
                selectDialog.dismiss();
            }

            @Override
            public void itemCenterClick(View view) {

            }
        });

        selectDialog.show();


    }
}
