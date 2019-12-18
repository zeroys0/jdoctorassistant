package com.jxj.jdoctorassistant.main.community.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.community.ApplicantListAdapter;
import com.jxj.jdoctorassistant.adapter.community.ChooseCommunityListAdapterYY;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.main.community.activity.personal.AddApplicantActivity;
import com.jxj.jdoctorassistant.main.community.activity.personal.AddAppointmentActivity;
import com.jxj.jdoctorassistant.main.community.activity.personal.PersonalActivity;
import com.jxj.jdoctorassistant.thread.CommunityAssessThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
/* 数据有两部分
*       一部分是 ： ListView 的适配器
*       一部分是 ： ListView 的条目点击事件 Intent 传值
*
*   搜索 的 结果为
*           把获取的 填充进 主ListView 的获取 数据  然后刷新适配器
*
*   页面构成
        *   主页面的  ListView以及点击事件
        *
        *   搜索实现   2017-11-6 17:04:53  今天完成 搜索
        *
        *   下拉列表

*/

public class ApplicantFragment extends Fragment {

    public static final int TYPE_1 = 0x15;
    @Bind(R.id.no_applicant_rl)
    RelativeLayout mNoApplicantRl;

    /*有关 搜索*/

    private SearchView mSearchView;

    private Context mContext;


    /*有关数据 */
    JSONObject jsonObject;

    private PullToRefreshListView applicantLv;
    /*适配器 主适配器  与下拉社区 适配器*/
    private ApplicantListAdapter adapter;//这个适配器很重要
    private ChooseCommunityListAdapterYY communityListAdapter;

    /*有关标题栏  title_add*/
    private ImageView titleAdd;
    private ImageButton orderMessage;
    /*有关  下拉菜单*/
    private TextView chooseLevel;
    private TextView chooseApplicantAddress;
    private PopupWindow chooseLevelPopupwindow;
    private String[] chooseLevelStrs = {"全部", "正常", "轻度", "中度", "重度"};

    private Context context;
    private CommunityAssessThread getApplicantThread, getCommutyThread, getAddressThread;

    private JSONArray array;

    int addIndex = 10;
    private JSONArray shequArray;

    int level = -1;


    private int shequId = -1;

    private int count;

    public ApplicantFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applicant, container, false);
        context = getActivity();


        communityListAdapter = new ChooseCommunityListAdapterYY(context);
        applicantLv = (PullToRefreshListView) view.findViewById(R.id.applicant_lv);
        adapter = new ApplicantListAdapter(context);

        mContext = getActivity().getApplicationContext();
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mSearchView.clearFocus();
        titleAdd = (ImageView) view.findViewById(R.id.title_add);

        chooseLevel = (TextView) view.findViewById(R.id.applicant_level);
        chooseApplicantAddress = (TextView) view.findViewById(R.id.applicant_address);


        applicantLv.setAdapter(adapter);
        getApplicant("", "", level, shequId, 0, addIndex);

        //  需要 7个参数（key现在不用考虑 ） 其中一个 name 与一个 cardId(身份证号) 抽出来写在总 类中  3个


        /*TODO 主 ListView 条目的点击事件   */
        applicantLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(getContext(), PersonalActivity.class);
                intent.putExtra("array", array.get(position).toString());
                Log.v("qqq", array.get(position).toString());
                startActivity(intent);
            }
        });


        applicantLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = "更新于" + GetDate.currentFullTime();
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                addIndex = 10;
                getApplicant("", "", level, shequId, 0, addIndex);

                applicantLv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        applicantLv.onRefreshComplete();
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                addIndex = addIndex + 5;
                getApplicant("", "", level, shequId, 0, addIndex);

                applicantLv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        applicantLv.onRefreshComplete();
                    }
                }, 1000);
            }

        });


      /*  mListView.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, mStrs));
        mListView.setTextFilterEnabled(true);*/


        //选择程度
        chooseLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出 选择框4种  正常 轻度 中度 重度
                //showPopuwindow(chooseLevel);
                showPuTongPopuwindow(chooseLevel);
                //

            }
        });
        //选择社区 这个需要请求数据
        chooseApplicantAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里需要生成 一个ListView
                //获取社区列表  然后 选择点击

                showPopuwindow(chooseApplicantAddress);


            }
        });


        //设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {// 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)) {//文字不为空            //判断 是姓名
                    Pattern pattern = Pattern.compile("[0-9]*");
                    Matcher isNum = pattern.matcher(newText);
                    if (!isNum.matches()) {//不是数字


                        getApplicant(newText, "", level, shequId, 0, addIndex);

                        adapter.notifyDataSetChanged();
                    } else {


                        getApplicant("", newText, level, shequId, 0, addIndex);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    //mListView.clearTextFilter();
                }
                return false;
            }

        });


        //弹框 选择 两个  一个是 新增 申请人 一个是  新增 预约评估
        titleAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseAddWhatPopuwindow(titleAdd);

            }
        });


        ButterKnife.bind(this, view);
        return view;
    }

    private void chooseAddWhatPopuwindow(View v) {
        View view = View.inflate(mContext, R.layout.yy_shouye_popupwindow_choose_add_what, null);
        final PopupWindow mPopWindow = new PopupWindow(view);
        mPopWindow.setFocusable(true);//设置弹出窗体可点击
        int w = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        //        int h=getWindowManager().getDefaultDisplay().getHeight();
        //设置弹出窗体的高
        mPopWindow.setWidth(w / 2);
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
        TextView tv1 = (TextView) view.findViewById(R.id.applicant_add_applicant);
        TextView tv2 = (TextView) view.findViewById(R.id.applicant_add_order);


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了新增申请人", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AddApplicantActivity.class);
                intent.putExtra("intentSize", 0);// 0是展示
                startActivity(intent);

                mPopWindow.dismiss();


            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了新增预约", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AddAppointmentActivity.class);
                intent.putExtra("intentSize", 0);// 0是展示
                startActivity(intent);
                mPopWindow.dismiss();


            }
        });


    }

    private void showPopuwindow(View view) {
        //请求数据 选择社区 所有

        View contentView = LayoutInflater.from(context).inflate(R.layout.add_new_applicant_popupwindow, null, false);
        final ListView mListView = (ListView) contentView.findViewById(R.id.add_new_new_applicant_list_popupwindow_lv);

        mListView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mListView.setAdapter(communityListAdapter);
        communityListAdapter.setType(TYPE_1);


        getAddress(3);
        //getPrivateTemplate();

        final PopupWindow popupWindow = new PopupWindow(contentView);
        popupWindow.setFocusable(true);//设置弹出窗体可点击
        int w = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        //        int h=getWindowManager().getDefaultDisplay().getHeight();
        //设置弹出窗体的高
        popupWindow.setWidth(w);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //实例化一个ColorDrawable颜色明为半透
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        popupWindow.setBackgroundDrawable(dw);

        //popupWindow.showAtLocation(chooseMonitoringScopeTV, Gravity.BOTTOM,0,0);//在父容器的位置

        popupWindow.showAsDropDown(view, w / 2, 0);
        backgroundAlpha(0.8f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });


        //TODO 这里是条目的 点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
//                    shequId = shequArray.getJSONObject(position - 1).getInt("Id");
//                    chooseApplicantAddress.setText(shequArray.getJSONObject(position).getString("CommunityName"));
                    chooseApplicantAddress.setText("全部社区");
                    shequId = -1;
                    getApplicant("", "", level, shequId, 0, addIndex);

                } else {
                    shequId = shequArray.getJSONObject(position - 1).getInt("Id");
                    chooseApplicantAddress.setText(shequArray.getJSONObject(position - 1).getString("CommunityName"));

                    getApplicant("", "", level, shequId, 0, addIndex);


                }
                popupWindow.dismiss();

            }
        });


    }


    private void showPuTongPopuwindow(View v) {
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
        tv2.setText("正常");
        TextView tv3 = (TextView) view.findViewById(R.id.applicant_level_two);
        tv3.setText("轻度");
        TextView tv4 = (TextView) view.findViewById(R.id.applicant_level_three);
        tv4.setText("中度");
        TextView tv5 = (TextView) view.findViewById(R.id.applicant_level_four);
        tv5.setText("重度");


        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了全部", Toast.LENGTH_SHORT).show();
                chooseLevel.setText("全    部");
                mPopWindow.dismiss();
                //第三个参数  -1 是全部  1 2 3 4

                level = -1;
                getApplicant("", "", level, shequId, 0, addIndex);

            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了正常", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                chooseLevel.setText("正常");
                level = 1;
                getApplicant("", "", level, shequId, 0, addIndex);

            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(context, "选择了轻度", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                chooseLevel.setText("轻度");
                level = 2;
                getApplicant("", "", level, shequId, 0, addIndex);
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了中度", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                chooseLevel.setText("中度");
                level = 3;
                getApplicant("", "", level, shequId, 0, addIndex);

            }
        });
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "选择了重度", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                chooseLevel.setText("重度");
                level = 4;
                getApplicant("", "", level, shequId, 0, addIndex);

            }
        });
           /* //显示PopupWindow
            View rootview = LayoutInflater.from(context).inflate(R.layout.fragment_applicant, null);

            mPopWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);*/


    }


    //通过 父id 循环获取 下一级 地址  晚上做
    void getAddress(int parentId) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = getAddressThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            shequArray = jsonObject.getJSONArray("Data");
                            Log.v("qqq", shequArray.toString() + "得到 社区地址的数据");


                        } else {
                            shequArray = null;
                        }

                        communityListAdapter.setArray(shequArray);
                        communityListAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
        getAddressThread = new CommunityAssessThread(ApiConstant.GETCOMMUNITYLISTBYLEVEL, handler, context);
        getAddressThread.setLevel(parentId);
        getAddressThread.start();


    }


    // 设置popwindow弹出的阴影效果
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }


    // 姓名 身份证号   社区ID 程度
    void getApplicant(String mName, String mCardId, int mItem1, int mCommunityId, int pageIndex, final int pageSize) {//这种调用 方式 为默认
        // 然后又一个上拉加载改变
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = getApplicantThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {
                        jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            array = jsonObject.getJSONArray("DataList");

                            int pageCount = jsonObject.getInt("PageCount");

                            if (pageCount == 0) {

                                mNoApplicantRl.setVisibility(View.VISIBLE);
                            } else {
                                if (pageSize != 10 && array.size() == count) {
                                    UiUtil.showToast(context, "没有更多数据了");
                                } else {
                                    mNoApplicantRl.setVisibility(View.GONE);
                                    count = array.size();

                                    adapter.setJsonarray(array);
                                    adapter.notifyDataSetChanged();


                                }

                            }


                        } else {
                            mNoApplicantRl.setVisibility(View.VISIBLE);
                        }
                    }
                }


            }
        };
        getApplicantThread = new CommunityAssessThread(ApiConstant.GETAPPLICANTLIST, handler, context);
        getApplicantThread.setNamee(mName);
        getApplicantThread.setCardId(mCardId);
        getApplicantThread.setItem1(mItem1);
        getApplicantThread.setCommunityId(mCommunityId);
        getApplicantThread.setPageIndex(pageIndex);
        getApplicantThread.setPageSize(pageSize);
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
    public void onResume() {
        getApplicant("", "", -1, -1, 0, addIndex);

        super.onResume();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }




}
