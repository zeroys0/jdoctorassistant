package com.jxj.jdoctorassistant.main.community;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ListView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.community.ApplicantListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.thread.CommunityAssessThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ApplicantActivity extends Activity {

    @ViewInject(R.id.applicant_lv)
    private ListView applicantLv;

    private ApplicantListAdapter adapter;
    private Context context;
    private CommunityAssessThread getApplicantThread,getCommutyThread;
    private String name,cardId;
    private int item1,communityId;
    private JSONArray array,commutyArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_applicant);
        ViewUtils.inject(this);
        context=this;

        adapter=new ApplicantListAdapter(context);
        applicantLv.setAdapter(adapter);
        getApplicant("","",-1,-1);
    }

    void getApplicant(String mName,String mCardId,int mItem1,int mCommunityId){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getApplicantThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            array=jsonObject.getJSONArray("DataList");
                            adapter.setJsonarray(array);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        };
        getApplicantThread=new CommunityAssessThread(ApiConstant.GETAPPLICANTLIST,handler,context);
        getApplicantThread.setNamee(mName);
        getApplicantThread.setCardId(mCardId);
        getApplicantThread.setItem1(mItem1);
        getApplicantThread.setCommunityId(mCommunityId);
        getApplicantThread.setPageIndex(0);
        getApplicantThread.setPageSize(10);
        getApplicantThread.start();
    }

    void getCommuty(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==ApiConstant.MSG_API_HANDLER){
                    String result=getCommutyThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject jsonObject=JSONObject.fromObject(result);
                        int code=jsonObject.getInt("code");
                        if(code==200){
                            commutyArray=jsonObject.getJSONArray("Data");

                        }
                    }
                }
            }
        };

        getCommutyThread=new CommunityAssessThread(ApiConstant.GETCOMMUNITYLIST,handler,context);
        getCommutyThread.setParentId(0);
        getCommutyThread.start();
    }
}
