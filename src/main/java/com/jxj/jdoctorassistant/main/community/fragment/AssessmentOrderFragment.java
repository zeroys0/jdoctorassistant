package com.jxj.jdoctorassistant.main.community.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.community.AssessmentOrderListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.main.community.activity.personal.AddAppointmentActivity;
import com.jxj.jdoctorassistant.thread.CommunityAssessThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 2017-12-1 09:50:21
 * 预约评估
 */
public class AssessmentOrderFragment extends Fragment {

    /*有关 搜索*/
    private SearchView mSearchView;
    private Context context;
    /*有关数据 */
    private PullToRefreshListView orderLv;
    private AssessmentOrderListAdapter adapter;//这个适配器很重要
    private CommunityAssessThread getAppointmentListThread, getCommutyThread;
    private String name, cardId;
    private int item1, communityId;
    private JSONArray array, commutyArray;
    private ImageView titleAdd;

    private int uId;
    private SharedPreferences sp;
    int addIndex = 10;

    public AssessmentOrderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assessment_order, container, false);
        context = this.getActivity();
        sp = getActivity().getSharedPreferences(AppConstant.USER_sp_name, context.MODE_PRIVATE);
        uId = sp.getInt(AppConstant.ADMIN_userId, 0);
        titleAdd = (ImageView) view.findViewById(R.id.title_add);
        titleAdd.setVisibility(View.GONE);
        orderLv = (PullToRefreshListView) view.findViewById(R.id.assessment_order_lv);
        adapter = new AssessmentOrderListAdapter(getContext());
        orderLv.setAdapter(adapter);

        getAssessmentOrderList(uId, "", 0, addIndex);


        /* 这里有一个权限问题  只能看到自己社区的 评估记录*/
        // Log.v("mmm",uId +"  55555555555555555555555");
        orderLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getContext(), AddAppointmentActivity.class);
                intent.putExtra("intentSize", 1);// 1是编辑
                intent.putExtra("array", array.get(position).toString());
                Log.v("qqq", array.get(position).toString());
                startActivity(intent);
            }
        });


        orderLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = "更新于" + GetDate.currentFullTime();
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                addIndex = 10;


                getAssessmentOrderList(uId, "", 0, addIndex);
                orderLv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        orderLv.onRefreshComplete();
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                addIndex = addIndex + 5;
                getAssessmentOrderList(uId, "", 0, addIndex);

                orderLv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        orderLv.onRefreshComplete();
                    }
                }, 1000);
            }

        });

        /*TODO  滑动   */

        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mSearchView.clearFocus();
       /*   mListView = (ListView)view. findViewById(R.id.listView);

        mListView.setTextFilterEnabled(true);
*/

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
                if (!TextUtils.isEmpty(newText)) {
                    getAssessmentOrderList(uId, newText, 0, addIndex);
                    // mListView.setFilterText(newText);
                } else {
                    // mListView.clearTextFilter();
                    getAssessmentOrderList(uId, "", 0, addIndex);
                }
                return false;
            }
        });


        return view;


    }


    void getAssessmentOrderList(int uid, String terms, int pageIndex, int pageSize) {//这种调用 方式 为默认
        // 然后又一个上拉加载改变
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = getAppointmentListThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            array = jsonObject.getJSONArray("DataList");
                            adapter.setJsonarray(array);
                            // mAdapter.setJsonarray(array);

                            Log.v("qqq", " 得到的评估预约 接口是：" + array.toString());
                            adapter.notifyDataSetChanged();
                            // mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        };
        getAppointmentListThread = new CommunityAssessThread(ApiConstant.GETAPPOINTMENTLIST, handler, context);
        getAppointmentListThread.setUid(uid);// 查询 关键字
        getAppointmentListThread.setTerms(terms);//  查询范围 起始时间
        getAppointmentListThread.setPageIndex(pageIndex);//   当前页 页码
        getAppointmentListThread.setPageSize(pageSize);//    当前页 大小
        getAppointmentListThread.start();
    }


    @Override
    public void onResume() {
        super.onResume();
        getAssessmentOrderList(uId, "", 0, addIndex);


    }
}
