package com.jxj.jdoctorassistant.main.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.recycle.Department1Adapter;
import com.jxj.jdoctorassistant.adapter.recycle.Department2Adapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.ThirdApiThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DepartmentListActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.department_list_title)
    private TextView titleTv;

    @ViewInject(R.id.department_list_load_rl)
    private RelativeLayout loadRl;
    @ViewInject(R.id.department_1_rv)
    private RecyclerView department1Rv;
    @ViewInject(R.id.department_2_rv)
    private RecyclerView department2Rv;

    @ViewInject(R.id.department_et)
    private EditText departmentEt;

    @OnClick({R.id.back_igv,R.id.department_edit_btn})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.department_edit_btn:
                String department=departmentEt.getEditableText().toString().trim();
                if(department.length()>0){
                    Intent intent=new Intent();
                    intent.putExtra("department",department);
                    setResult(0x01,intent);
                    finish();
                }else {
                    UiUtil.showToast(context,getResources().getString(R.string.not_null));
                }

                break;
            default:
                break;
        }
    }

    private ThirdApiThread getDepartmentInfoThread;
    private Context context;
    private JSONArray array_1,array_2;//科室分类，详细分类
    private Department1Adapter adapter1;
    private Department2Adapter adapter2;
    public static int SELECTPOSITION=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_department_list);
        ViewUtils.inject(this);
        context=this;

        loadRl.setVisibility(View.GONE);
        department1Rv.setVisibility(View.GONE);
        department2Rv.setVisibility(View.GONE);
        titleTv.setText("");
//        setRecyclerView();
//        setRecyclerAdpater();
//        getDepartmentInfo();
    }
    void setRecyclerView(){
        department1Rv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        department1Rv.addItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
        department2Rv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        department2Rv.addItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
    }
    void setRecyclerAdpater(){
        adapter1=new Department1Adapter(context);
        department1Rv.setAdapter(adapter1);
        adapter1.setmOnItemClickListener(new Department1Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                SELECTPOSITION=position;
                adapter1.notifyDataSetChanged();
                JSONObject json=array_1.getJSONObject(position);
                String departments=json.getString("departments");
                array_2=JSONArray.fromObject(departments);
                adapter2.setArray(array_2);
                adapter2.notifyDataSetChanged();

            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });

        adapter2=new Department2Adapter(context);
        department2Rv.setAdapter(adapter2);
        adapter2.setmOnItemClickListener(new Department2Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent=new Intent();
                intent.putExtra("department",array_2.getJSONObject(position).getString("name"));
                setResult(0x01,intent);
                finish();
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });

    }



    void getDepartmentInfo(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getDepartmentInfoThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        loadRl.setVisibility(View.GONE);
                        JSONObject json=JSONObject.fromObject(result);
                        boolean status=json.getBoolean("status");
                        if(status){
                            String tngou=json.getString("tngou");

                            array_1=JSONArray.fromObject(tngou);
                            adapter1.setArray(array_1);
                            adapter1.notifyDataSetChanged();

                            JSONObject jsonObj=array_1.getJSONObject(0);
                            String departments=jsonObj.getString("departments");
                            array_2=JSONArray.fromObject(departments);
                            adapter2.setArray(array_2);
                            adapter2.notifyDataSetChanged();

                        }else {
                            UiUtil.showToast(context,getResources().getString(R.string.tgou_data_error));
                        }
                    }
                }
            }
        };
        getDepartmentInfoThread=new ThirdApiThread(handler,context);
        getDepartmentInfoThread.setUrl("http://www.tngou.net/api/department/all");
        getDepartmentInfoThread.setArgs("");
        getDepartmentInfoThread.start();
    }
}
