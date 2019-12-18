package com.jxj.jdoctorassistant.main.community.activity.personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.community.ApplicantListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.thread.CommunityAssessThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/*
* 需求：
* 新增 预约评估进行 选择  申请人列表的
 * 点击事件 回到 新增评估 界面 传递一个 ID
*
*
*  实现 ：
*   获取数据     上拉 加载  下拉刷新
*
*
*   listview   适配  填充
  *
  *   点击条目
  *
  *
  *
*
*
*
* */
public class ChooseApplicantActivity extends Activity {


    @Bind(R.id.back_igv)
    ImageView mBackIgv;
    @Bind(R.id.title_tv)
    TextView mTitleTv;
    @Bind(R.id.activity_choose_applicant_lv)
    PullToRefreshListView mActivityChooseApplicantLv;


    private ApplicantListAdapter adapter;//这个适配器很重要
    Context context;
    private CommunityAssessThread getApplicantThread;
    private JSONArray array;

    /*有关数据 */
    JSONObject jsonObject;

    private int count;

    int level = -1;


    private int shequId = -1;
    int addIndex = 10;

    public static final String GETAPPLICANTINFO="get_applicant_info";

    public static final int RESULTCODE=0x112;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_applicant);
        ButterKnife.bind(this);
        mTitleTv.setText("选择申请人");

        context=this;

        adapter = new ApplicantListAdapter(context);

        mActivityChooseApplicantLv.setAdapter(adapter);



        getApplicant("", "", level, shequId, 0, addIndex);

        mActivityChooseApplicantLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = "更新于" + GetDate.currentFullTime();
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                addIndex=10;
                getApplicant("", "", level, shequId, 0, addIndex);

                mActivityChooseApplicantLv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mActivityChooseApplicantLv.onRefreshComplete();
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                addIndex = addIndex + 5;
                getApplicant("", "", level, shequId, 0, addIndex);

                mActivityChooseApplicantLv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mActivityChooseApplicantLv.onRefreshComplete();
                    }
                }, 1000);
            }

        });


        mActivityChooseApplicantLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //点击  把 array 写进 intent  {因为 上一个界面  用到 姓名 身份证号  联系方式 联系地址}

                Intent intent=new Intent();
                Log.v("mmm",array.getJSONObject(position).toString());
                intent.putExtra(GETAPPLICANTINFO,array.getJSONObject(position).toString());
                setResult(RESULTCODE,intent);
                finish();





            }
        });


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


                            if(pageSize!=10&&array.size()==count){
                                UiUtil.showToast(context,"没有更多数据了");
                            }else {
                                count=array.size();

                                adapter.setJsonarray(array);
                                adapter.notifyDataSetChanged();
                            }

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

    @OnClick(R.id.back_igv)
    public void onViewClicked() {

        finish();
    }
}
