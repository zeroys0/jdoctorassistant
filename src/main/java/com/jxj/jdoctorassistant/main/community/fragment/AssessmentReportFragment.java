package com.jxj.jdoctorassistant.main.community.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.community.AssessmentReportListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.main.community.activity.pinggu.QuerenPinguActivity;
import com.jxj.jdoctorassistant.thread.CommunityAssessThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 这个是记录  界面
 */
public class AssessmentReportFragment extends Fragment {
    @Bind(R.id.no_order_rl)
    RelativeLayout mNoOrderRl;

    @Bind(R.id.assessment_report_choose_time)
    TextView mAssessmentReportChooseTime;
    @Bind(R.id.assessment_report_choose_access_type)
    TextView mAssessmentReportChooseAccessType;
    /*有关 搜索*/
    private String[] mStrs = {"aaa", "bbb", "ccc", "airsaid"};
    private SearchView mSearchView;
    //private ListView mListView;
    private Context mContext;
    /*有关数据 */
    private PullToRefreshListView reportLv;
    private AssessmentReportListAdapter adapter;//这个适配器很重要
    private Context context;
    private CommunityAssessThread getApplicantThread, getCommutyThread;
    private String name, cardId;
    private int item1, communityId;
    private JSONArray array, commutyArray;

    private ImageView titleAdd;

    int addIndex = 10;
    int pageSize = 0;
    int type = -1;

    // 记录当前的时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;


    String currentTime;
    String startTime = "2017-01-01";


    public AssessmentReportFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        Log.v("qqq", "走到了第二个Fragment");
        View view = inflater.inflate(R.layout.fragment_assessment_report, container, false);
        // ViewUtils.inject(getActivity());
        //ViewUtils.inject(this,view);
        context = getActivity();
        titleAdd = (ImageView) view.findViewById(R.id.title_add);
        titleAdd.setVisibility(View.GONE);
        reportLv = (PullToRefreshListView) view.findViewById(R.id.assessment_report_lv);
        adapter = new AssessmentReportListAdapter(context);
        reportLv.setAdapter(adapter);


        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        updateDateDisplay();


        getAssessmentOrderList("", startTime, currentTime, type, pageSize, addIndex);


        mContext = getActivity().getApplicationContext();
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mSearchView.clearFocus();
        mSearchView.setQueryHint("请输入身份证号");
        mSearchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        Log.v("qqq", "下一步就是 ListView的子条目点击事件");
        //记录界面的ListView 的条目 点击事件
        reportLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //记录  进去 的就是 记录的详细情况
                Intent intent = new Intent(context, QuerenPinguActivity.class);
                intent.putExtra("array", array.get(position).toString());

                Log.v("qqqq", array.get(position).toString());
                intent.putExtra("intentSize", 0);
                startActivity(intent);


            }
        });


        reportLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = "更新于" + GetDate.currentFullTime();
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                addIndex = 10;

                getAssessmentOrderList("", startTime, currentTime, type, 0, addIndex);
                /*getApplicant("", "", level, shequId, 0, addIndex);*/

                reportLv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reportLv.onRefreshComplete();
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                addIndex = addIndex + 5;
                getAssessmentOrderList("", startTime, currentTime, type, 0, addIndex);

                /*getApplicant("", "", level, shequId, 0, addIndex);*/

                reportLv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        reportLv.onRefreshComplete();
                    }
                }, 1000);
            }

        });


       /* mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mStrs));
        mListView.setTextFilterEnabled(true);*/

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {//文字不为空            //判断 是姓名

                    getAssessmentOrderList(newText, startTime, currentTime, type, 0, addIndex);
                    adapter.notifyDataSetChanged();


                } else {
                    getAssessmentOrderList("", startTime, currentTime, type, 0, addIndex);
                    //mListView.clearTextFilter();
                }
                return false;
            }
        });


        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 更新日期显示(记得month要+1，因为DatePicker索引是0-11)
     */
    private void updateDateDisplay() {
        currentTime = new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).toString();
    }

    //  type 首次  持续 复检
    void getAssessmentOrderList(String keyString, String startTime, String endTime, int type, int pageIndex, int pageSize) {//这种调用 方式 为默认
        // 然后又一个上拉加载改变
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = getApplicantThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            array = jsonObject.getJSONArray("DataList");
                            int pageCount = jsonObject.getInt("PageCount");

                            if (pageCount == 0) {

                                mNoOrderRl.setVisibility(View.VISIBLE);

                            } else {
                                mNoOrderRl.setVisibility(View.GONE);
                                Log.v("qqq", "记录列表的到的数据  " + array.toString());
                                adapter.setJsonarray(array);
                                adapter.notifyDataSetChanged();

                                //mNoOrderRl

                            }


                        } else {
                            mNoOrderRl.setVisibility(View.VISIBLE);

                        }
                    }
                }
            }
        };
        getApplicantThread = new CommunityAssessThread(ApiConstant.GETPASSESSLIST, handler, context);
        getApplicantThread.setKeyString(keyString);// 查询 关键字
        getApplicantThread.setStartTime(startTime);//  查询范围 起始时间
        getApplicantThread.setEndTime(endTime);//     查询范围 结束时间
        getApplicantThread.setType(type);// 评估类型

        getApplicantThread.setPageIndex(pageIndex);//   当前页 页码
        getApplicantThread.setPageSize(pageSize);//    当前页 大小
        getApplicantThread.start();
    }


    private ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        String temp = " item";
        for (int i = 0; i < 20; i++) {
            data.add(i + temp);
        }

        return data;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /*弹出 时间选择框 与评估 类型  初次复检 持续 */
    @OnClick({R.id.assessment_report_choose_time, R.id.assessment_report_choose_access_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.assessment_report_choose_time:
                showTimePopwindow(mAssessmentReportChooseTime);


                break;
            case R.id.assessment_report_choose_access_type:
                showAccessTypePopwindow(mAssessmentReportChooseAccessType);


                break;


        }
    }


    private void showTimePopwindow(View v) {
        //根据 当前时间
        // 一周   开始时间（当前日期-7） 结束时间（当前日期）
        // 一月   开始时间（当前月份-1） 结束时间（当前日期）
        // 一年   开始时间（当前年-1） 结束时间（当前日期）

        View view = View.inflate(mContext, R.layout.shouye_popupwindow_choose_level, null);
        final PopupWindow mPopWindow = new PopupWindow(view);
        mPopWindow.setFocusable(true);//设置弹出窗体可点击
        int w = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        //        int h=getWindowManager().getDefaultDisplay().getHeight();
        //设置弹出窗体的高
        mPopWindow.setWidth(w);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //实例化一个ColorDrawable颜色明为半透
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        mPopWindow.setBackgroundDrawable(dw);
        //popupWindow.showAtLocation(chooseMonitoringScopeTV, Gravity.BOTTOM,0,0);//在父容器的位置
        mPopWindow.showAsDropDown(v, 0, 0);
        backgroundAlpha(0.8f);


        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        //设置各个控件的点击响应
        TextView tv1 = (TextView) view.findViewById(R.id.applicant_level_all);
        tv1.setText("全部");
        TextView tv2 = (TextView) view.findViewById(R.id.applicant_level_one);
        tv2.setText("一周");
        TextView tv3 = (TextView) view.findViewById(R.id.applicant_level_two);
        tv3.setText("一月");
        TextView tv4 = (TextView) view.findViewById(R.id.applicant_level_three);
        tv4.setText("一年");
        TextView tv5 = (TextView) view.findViewById(R.id.applicant_level_four);
        tv5.setVisibility(View.GONE);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了全部", Toast.LENGTH_SHORT).show();
                mAssessmentReportChooseTime.setText("全    部");
                mPopWindow.dismiss();
                //第三个参数  -1 是全部  1 2 3 4
                startTime = "2017-01-01";
                getAssessmentOrderList("", startTime, currentTime, type, 0, addIndex);

            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了正常", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                mAssessmentReportChooseTime.setText("一周");
                //startTime 为 currentTime -7

                startTime = GetDate.lastWeek();
                getAssessmentOrderList("", startTime, currentTime, type, 0, addIndex);

            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, "选择了轻度", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                mAssessmentReportChooseTime.setText("一月");
                startTime = GetDate.lastMonth();
                getAssessmentOrderList("", startTime, currentTime, type, 0, addIndex);
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了中度", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                mAssessmentReportChooseTime.setText("一年");
                startTime = GetDate.getDateBefore(365);
                getAssessmentOrderList("", startTime, currentTime, type, 0, addIndex);

            }
        });
        /*tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了重度", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                mAssessmentReportChooseTime.setText("重度");
                level = 4;
                getAssessmentOrderList("", startTime, currentTime, -1, 0, addIndex);

            }
        });*/
           /* //显示PopupWindow
            View rootview = LayoutInflater.from(context).inflate(R.layout.fragment_applicant, null);

            mPopWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);*/


    }

    private void showAccessTypePopwindow(View v) {

        View view = View.inflate(mContext, R.layout.shouye_popupwindow_choose_level, null);
        final PopupWindow mPopWindow = new PopupWindow(view);
        mPopWindow.setFocusable(true);//设置弹出窗体可点击
        int w = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        //        int h=getWindowManager().getDefaultDisplay().getHeight();
        //设置弹出窗体的高
        mPopWindow.setWidth(w);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //实例化一个ColorDrawable颜色明为半透
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        mPopWindow.setBackgroundDrawable(dw);
        //popupWindow.showAtLocation(chooseMonitoringScopeTV, Gravity.BOTTOM,0,0);//在父容器的位置
        mPopWindow.showAsDropDown(v, 0, 0);
        backgroundAlpha(0.8f);


        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        //设置各个控件的点击响应
        TextView tv1 = (TextView) view.findViewById(R.id.applicant_level_all);
        tv1.setText("全部");
        TextView tv2 = (TextView) view.findViewById(R.id.applicant_level_one);
        tv2.setText("初始");
        TextView tv3 = (TextView) view.findViewById(R.id.applicant_level_two);
        tv3.setText("复检");
        TextView tv4 = (TextView) view.findViewById(R.id.applicant_level_three);
        tv4.setText("持续");
        TextView tv5 = (TextView) view.findViewById(R.id.applicant_level_four);
        tv5.setVisibility(View.GONE);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了全部", Toast.LENGTH_SHORT).show();
                mAssessmentReportChooseAccessType.setText("全    部");
                mPopWindow.dismiss();
                //第三个参数  -1 是全部  1 2 3 4

                type = -1;
                getAssessmentOrderList("", startTime, currentTime, type, 0, addIndex);

            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了正常", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                mAssessmentReportChooseAccessType.setText("初始");
                type = 0;
                getAssessmentOrderList("", startTime, currentTime, type, 0, addIndex);

            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, "选择了轻度", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                mAssessmentReportChooseAccessType.setText("复检");
                type = 1;
                getAssessmentOrderList("", startTime, currentTime, type, 0, addIndex);
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了中度", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                mAssessmentReportChooseAccessType.setText("持续");
                type = 2;
                getAssessmentOrderList("", startTime, currentTime, type, 0, addIndex);

            }
        });
        /*tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了重度", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                mAssessmentReportChooseTime.setText("重度");
                level = 4;
                getAssessmentOrderList("", startTime, currentTime, -1, 0, addIndex);

            }
        });*/
           /* //显示PopupWindow
            View rootview = LayoutInflater.from(context).inflate(R.layout.fragment_applicant, null);

            mPopWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);*/


    }


    // 设置popwindow弹出的阴影效果
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

}
