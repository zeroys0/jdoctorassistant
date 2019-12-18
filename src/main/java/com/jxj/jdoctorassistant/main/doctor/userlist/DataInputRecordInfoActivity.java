package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.ImageListAdapter;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.TopDataAdapter;
import com.jxj.jdoctorassistant.view.MyGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;

public class DataInputRecordInfoActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.data_input_record_info_title)
    private TextView titleTv;

    @ViewInject(R.id.date_tv)
    private TextView dateTv;
    @ViewInject(R.id.source_tv)
    private TextView sourceTv;
    @ViewInject(R.id.data_info_tv)
    private TextView infoTv;
    @ViewInject(R.id.data_record_gv)
    private MyGridView recordGv;
    @ViewInject(R.id.image_lv)
    private ListView imageLv;

    @OnClick({R.id.back_igv})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
        }
    }


    public static final String[] DATATYPE={"血压","心率","血氧","血糖","计步","卡路里","体温","体重","脂肪率","水分率","肌肉率","骨量","基础代谢"};
    public static final String[] TYPE={"PS","PR","BloodOxygen","BloodGlucose","Step","Calorie","Temperature","Weight","FatRate","Moisture","Muscle","Bone","BM"};
    public static final String[] UNIT={"mmHg","次/分","mmol/L","mmol/L","步数","kcal","℃","kg","%","%","%","%","%"};

    private String[] dataUnit;
    private Context context;
    private TopDataAdapter topDataAdapter;
    private JSONArray array=new JSONArray();
    private ImageListAdapter adapter;
    private ArrayList<String> arrImages=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_data_input_record_info);
        ViewUtils.inject(this);

        context=this;

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String object=bundle.getString(DataInputRecordActivity.DATAINFO);
        JSONObject jsonObject=JSONObject.fromObject(object);

        titleTv.setText("记录详情");
        dateTv.setText(jsonObject.getString("TestDate"));
        sourceTv.setText(getResources().getStringArray(R.array.arr_data_source_record)[jsonObject.getInt("Type")]);
        dataUnit=getResources().getStringArray(R.array.data_unit);

        StringBuffer stringBuffer=new StringBuffer();
        for(int i=0;i<TYPE.length;i++){
            JSONObject json=new JSONObject();
            if(jsonObject.has(TYPE[i])){
                if(jsonObject.has("PD")){
                    json.put("Values",jsonObject.getString(TYPE[i])+"/"+jsonObject.getString("PD"));
                    json.put("Type",i);
                }else {
                    json.put("Values",jsonObject.getString(TYPE[i]));
                    json.put("Type",i);
                }
//                stringBuffer.append(DATATYPE[i]+":"+jsonObject.getString(TYPE[i])+UNIT[i]+"  ");
            }else {
                json.put("Values","--");
                json.put("Type",i);
            }
            array.add(i,json);
        }
        infoTv.setText(stringBuffer.toString());


        topDataAdapter=new TopDataAdapter(context);
        topDataAdapter.setUnit(dataUnit);
        topDataAdapter.setArray(array);

        recordGv.setAdapter(topDataAdapter);
//        setListViewHeightBasedOnChildren(recordGv);


        String str=jsonObject.getString("Images");
        Log.d("onSuccess:显示图片 ",str);
        JSONArray jsonArray=null;
        adapter=new ImageListAdapter(context);
        try {
            if(str!=null&&!str.equals("null")){
                jsonArray= JSONArray.fromObject(str);
                for(int i=0;i<jsonArray.size();i++){
                    arrImages.add(jsonArray.get(i).toString());
                }
                System.out.println("数据输入 图片列表： " + arrImages.toString());
                adapter.setImages(arrImages);
                adapter.notifyDataSetChanged();
//                setListViewHeightBasedOnChildren(imageLv);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        imageLv.setAdapter(adapter);


    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        adapter.setDelegete(this);
//    }

//    @Override
//    public void loaded() {
//        setListViewHeightBasedOnChildren(imageLv);
//    }
}
