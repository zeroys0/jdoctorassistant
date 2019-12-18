package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerTabStrip;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.main.doctor.userlist.fragment.FragmentPatientPersonalInfo;
import com.jxj.jdoctorassistant.main.doctor.userlist.fragment.FragmentRemark;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

public class PatientDetailInfoActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.patient_detail_info_title)
    private TextView titleTv;
    @ViewInject(value = R.id.right_btn_tv,parentId = R.id.patient_detail_info_title)
    private TextView recordTv;

    @ViewInject(R.id.patient_detail_info_rg)
    private RadioGroup infoRg;
//    @ViewInject(R.id.patient_detail_info_ll)
//    private LinearLayout infoLl;

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

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_patient_detail_info);
        ViewUtils.inject(this);

        context=this;
        initView();
        switchFragment(0);
    }

    void initView(){
        titleTv.setText(getResources().getString(R.string.patient_info));
//        recordTv.setText(getResources().getString(R.string.history_record));
//        recordTv.setVisibility(View.VISIBLE);
//        recordTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(context,RemarkRecordActivity.class));
//            }
//        });
        infoRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkId) {
                switch (checkId){
                    case R.id.patient_detail_info_personal_rb:
                        switchFragment(0);
                        break;
                    case R.id.patient_detail_info_remark_rb:
                        switchFragment(1);
                        break;
                    default:

                        break;
                }
            }
        });
    }

    void switchFragment(int i){
        Fragment fragment=null;

        switch (i){
            case 0:
                fragment=new FragmentPatientPersonalInfo();
                break;
            case 1:
                fragment=new FragmentRemark();
                break;
        }

        FragmentTransaction transaction=this.getFragmentManager().beginTransaction();
        transaction.replace(R.id.patient_detail_info_ll,fragment);
        transaction.commitAllowingStateLoss();
    }
}
