package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.ImageListAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.WebUtils;

import java.util.ArrayList;

public class SuggestionInfoActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.suggestion_info_title)
    private TextView titleTv;

    @ViewInject(R.id.suggestion_time_tv)
    private TextView timeTv;
    @ViewInject(R.id.suggestion_content_tv)
    private TextView contentTv;
    @ViewInject(R.id.suggestion_image_lv)
    private ListView imageLv;

    @OnClick({R.id.back_igv})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
        }
    }

    private Context context;
    private ArrayList<String> arrImages=new ArrayList<String>();
    private ImageListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_suggestion_info);
        ViewUtils.inject(this);

        context=this;

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String object=bundle.getString(SuggestionRecordActivity.SUGGESTIONINFO);
        JSONObject jsonObject=JSONObject.fromObject(object);

        titleTv.setText("记录详情");
        timeTv.setText(jsonObject.getString("SubmissionTime"));
        contentTv.setText(jsonObject.getString("Suggestion"));

        String str=jsonObject.getString("Images");
        JSONArray jsonArray=null;
        adapter=new ImageListAdapter(context);
        imageLv.setAdapter(adapter);

        try {
            if(str!=null&&!str.equals("null")){
                jsonArray=JSONArray.fromObject(str);
                for(int i=0;i<jsonArray.size();i++){
                    arrImages.add(jsonArray.get(i).toString());
                }
                System.out.println("指导建议 图片列表： " + arrImages.toString());
                adapter.setImages(arrImages);
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }



    }

}
