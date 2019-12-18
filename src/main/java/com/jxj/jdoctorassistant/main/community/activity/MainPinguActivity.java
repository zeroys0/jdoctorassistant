package com.jxj.jdoctorassistant.main.community.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.main.community.fragment.ApplicantFragment;
import com.jxj.jdoctorassistant.main.community.fragment.AssessmentOrderFragment;
import com.jxj.jdoctorassistant.main.community.fragment.AssessmentReportFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;


/*---时间线-----------------------------------------------------------------------*/
//2017年10月19日11:31:03   今天把 框架搭出来   【完成】
//2017年10月20日08:51:53    今天把 三个界面 的展示 完成

//2017年10月24日16:38:12     下拉选择框 与  ListView 的上拉加载
//2017年10月25日17:45:09     除了问卷调查的  界面 填充


/*现在后台 有  19 个接口   */

/*----主要描述----------------------------------------------------------------------*/
/* 这个是主界面  有3个 Fragment   动态添加

        申请人  评估记录  预约评估       */
public class MainPinguActivity extends FragmentActivity {

    private Context context;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    /**
     * 底部四个按钮
     */
    private LinearLayout mTabBtnApplicant;
    private LinearLayout mTabBtnAssessmentReport;
    private LinearLayout mTabBtnAssessmentOrder;


    ApplicantFragment applicantFragment;
    AssessmentReportFragment assessmentReportFragment;
    AssessmentOrderFragment assessmentOrderFragment;


    /* private FriendFragment mFriend;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pingu_yy);
        context = this;
        ViewUtils.inject(this);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        initView();
        //主要是适配器的 功劳     两个 方法 一个是返回数量 一个是返回 条目（即Fragment的实例）
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mFragments.get(arg0);
            }


        };

        mViewPager.setAdapter(mAdapter);

        // mViewPager.setOnPageChangeListener(null);
        //ViewPager 滑动的监听事件 主要是 用于转换图标
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int currentIndex;

            @Override
            public void onPageSelected(int position) {
                resetTabBtn();
                switch (position) {
                    case 0:
                        ((ImageButton) mTabBtnApplicant.findViewById(R.id.btn_tab_bottom_applicant))
                                .setImageResource(R.drawable.tab_applicant_pressed);
                        break;
                    case 1:
                        ((ImageButton) mTabBtnAssessmentReport.findViewById(R.id.btn_tab_bottom_assessment_report))
                                .setImageResource(R.drawable.tab_assessment_report_pressed);
                        break;

                    case 2:
                        ((ImageButton) mTabBtnAssessmentOrder.findViewById(R.id.btn_tab_bottom_assessment_order))
                                .setImageResource(R.drawable.tab_assessment_order_pressed);
                        break;


                }

                currentIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }


        });


    }

    protected void resetTabBtn() {
        ((ImageButton) mTabBtnApplicant.findViewById(R.id.btn_tab_bottom_applicant))
                .setImageResource(R.drawable.tab_applicant_normal);
        ((ImageButton) mTabBtnAssessmentReport.findViewById(R.id.btn_tab_bottom_assessment_report))
                .setImageResource(R.drawable.tab_assessment_report_normal);
        ((ImageButton) mTabBtnAssessmentOrder.findViewById(R.id.btn_tab_bottom_assessment_order))
                .setImageResource(R.drawable.tab_assessment_order_normal);


    }


    /* 这里用来创建 几个 Fragment 并把它 添加到 fragment的集合中  */
    private void initView() {

        mTabBtnApplicant = (LinearLayout) findViewById(R.id.id_tab_bottom_applicant);
        mTabBtnAssessmentReport = (LinearLayout) findViewById(R.id.id_tab_bottom_assessment_report);
        mTabBtnAssessmentOrder = (LinearLayout) findViewById(R.id.id_tab_bottom_assessment_order);


        applicantFragment = new ApplicantFragment();
        assessmentReportFragment = new AssessmentReportFragment();
        assessmentOrderFragment = new AssessmentOrderFragment();

        mViewPager.setCurrentItem(0);

        mFragments.add(applicantFragment);
        mFragments.add(assessmentReportFragment);
        mFragments.add(assessmentOrderFragment);


    }

    @OnClick({R.id.btn_tab_bottom_applicant, R.id.btn_tab_bottom_assessment_report, R.id.btn_tab_bottom_assessment_order})
    public void Click(View view) {
        switch (view.getId()) {
            case R.id.btn_tab_bottom_applicant:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.btn_tab_bottom_assessment_report:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.btn_tab_bottom_assessment_order:
                mViewPager.setCurrentItem(2);
                break;

        }
    }


}
