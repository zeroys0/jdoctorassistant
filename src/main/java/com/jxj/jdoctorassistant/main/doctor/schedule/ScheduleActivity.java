package com.jxj.jdoctorassistant.main.doctor.schedule;

import android.app.Activity;


import android.content.Context;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.main.doctor.adapter.SchedulePagerAdapter;
import com.jxj.jdoctorassistant.main.doctor.fragment.FragmentMessage;
import com.jxj.jdoctorassistant.main.doctor.fragment.FragmentMsg;
import com.jxj.jdoctorassistant.main.doctor.fragment.FragmentSchedule;
import com.jxj.jdoctorassistant.main.doctor.fragment.FragmentSign;
import com.jxj.jdoctorassistant.main.doctor.fragment.FragmentToProcessed;
import com.jxj.jdoctorassistant.view.NoSlideViewPager;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends FragmentActivity implements View.OnClickListener {


    private ViewPager viewpager_schedule;
FragmentSign fragmentSign;
FragmentToProcessed fragmentToProcessed;
FragmentMsg fragmentMsg;
    SchedulePagerAdapter schedulePagerAdapter;
    TabLayout tab_schedule;
    List<Fragment> listfragment = new ArrayList<>();
    ImageView back_igv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule);
        initView();
    }
    public void initView(){
        viewpager_schedule = findViewById(R.id.viewpager_schedule);
        tab_schedule = findViewById(R.id.tab_schedule);
        back_igv = findViewById(R.id.back_igv);
        back_igv.setOnClickListener(this);
        tab_schedule.addTab(tab_schedule.newTab().setText("日程待办"));
        tab_schedule.addTab(tab_schedule.newTab().setText("申请签约"));
        tab_schedule.addTab(tab_schedule.newTab().setText("聊天消息"));
        tab_schedule.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager_schedule.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        FragmentManager fm = getSupportFragmentManager();
        if (fragmentToProcessed == null) {
            fragmentToProcessed = new FragmentToProcessed();
            listfragment.add(fragmentToProcessed);
        }
        if (fragmentSign == null) {
            fragmentSign = new FragmentSign();
            listfragment.add(fragmentSign);
        }
        if (fragmentMsg == null) {
            fragmentMsg = new FragmentMsg();
            listfragment.add(fragmentMsg);
        }
        schedulePagerAdapter = new SchedulePagerAdapter(fm, listfragment);
        viewpager_schedule.setAdapter(schedulePagerAdapter);
        viewpager_schedule.setCurrentItem(0);
        viewpager_schedule.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                tab_schedule.getTabAt(position).select();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_igv:
                finish();
                break;
                default:
                    break;
        }
    }
}
