package com.jxj.jdoctorassistant.main.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;

public class JobTitleListActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.job_title_list_title)
    private TextView titleTv;
    @ViewInject(R.id.job_title_lv)
    private ListView jobTitleLv;

    private ArrayList<String> arrayList=new ArrayList<String>();
    private String[] arr;
    public final static int RESULTCODE=233;
    public final static String JOBTITLE="job_title";

    @OnClick({R.id.back_igv})
    void  onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            default:

                break;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_job_title_list);
        ViewUtils.inject(this);
        arr=getResources().getStringArray(R.array.job_title_arr);
        titleTv.setText("职称选择");
        jobTitleLv.setAdapter(new ArrayAdapter<String>(this,R.layout.lv_item_job_title,arr));
        jobTitleLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
                intent.putExtra(JOBTITLE,arr[i]);
                setResult(RESULTCODE,intent);
                finish();
            }
        });
    }

}
