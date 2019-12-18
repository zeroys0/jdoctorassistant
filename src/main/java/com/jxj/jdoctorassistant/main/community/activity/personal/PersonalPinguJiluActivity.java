package com.jxj.jdoctorassistant.main.community.activity.personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.community.AssessmentReportListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.main.community.activity.pinggu.QuerenPinguActivity;
import com.jxj.jdoctorassistant.thread.CommunityAssessThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*用于 记录 个人的 评估记录 */
public class PersonalPinguJiluActivity extends Activity {


    @Bind(R.id.personal_pinggu_jilu_lv)
    ListView mPersonalPingguJiluLv;
    @Bind(R.id.back_igv)
    ImageView mBackIgv;
    @Bind(R.id.title_tv)
    TextView mTitleTv;

    /*适配器 */
    private AssessmentReportListAdapter adapter;//
    private CommunityAssessThread getApplicantThread, getCommutyThread;
    private Context context;
    private JSONArray array, commutyArray;

    private JSONObject object;
    // 记录当前的时间

    private String sendTime;

    private int id;


    /* 调用 第二个 fragment 的线程 只是 参数  是固定的 人
        *
        *
        *
        * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_pingu_jilu);
        ButterKnife.bind(this);


        mTitleTv.setText("个人评估记录");

        context = getBaseContext();
        adapter = new AssessmentReportListAdapter(this);
        mPersonalPingguJiluLv.setAdapter(adapter);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();

        object = JSONObject.fromObject(data.getString("array"));
        int id = object.getInt("Id");


        getAssessmentOrderList(id, 0, 40);

        mPersonalPingguJiluLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(PersonalPinguJiluActivity.this, QuerenPinguActivity.class);
                intent.putExtra("intentSize", 0);
                //array 传值
                intent.putExtra("array", array.get(position).toString());
                startActivity(intent);

            }
        });

    }


    @OnClick({R.id.back_igv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;

        }
    }

    void getAssessmentOrderList(int cid, int pageIndex, int pageSize) {//这种调用 方式 为默认
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
                            //array 赋值
                            adapter.setJsonarray(array);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        };
        getApplicantThread = new CommunityAssessThread(ApiConstant.GETPASSESSLISTBYID, handler, context);
        getApplicantThread.setCid(cid);// 评估类型
        getApplicantThread.setPageIndex(pageIndex);//   当前页 页码
        getApplicantThread.setPageSize(pageSize);//    当前页 大小
        getApplicantThread.start();
    }


}
