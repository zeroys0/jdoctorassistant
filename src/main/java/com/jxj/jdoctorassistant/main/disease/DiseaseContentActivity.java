package com.jxj.jdoctorassistant.main.disease;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;

public class DiseaseContentActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId=R.id.disease_content_title)
    private TextView titleTv;

    @ViewInject(R.id.disease_name_tv)
    private TextView nameTv;
    @ViewInject(R.id.disease_ins_wv)
    private WebView insWv;
    @ViewInject(R.id.disease_symptom_tv)
    private TextView symptomTv;
    @ViewInject(R.id.disease_symptomtext_wv)
    private WebView symptomTextWv;
    @ViewInject(R.id.disease_care_wv)
    private WebView careWv;
    @ViewInject(R.id.disease_cause_wv)
    private WebView causeWv;
    @ViewInject(R.id.disease_drug_wv)
    private WebView drugWv;
    @ViewInject(R.id.disease_food_wv)
    private WebView foodWv;
    @ViewInject(R.id.disease_check_wv)
    private WebView checkWv;

    @ViewInject(R.id.disease_igv)
    private ImageView igv;



    @OnClick({R.id.back_igv})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            default:
                break;
        }
    }

    private Context context;
    private final static String imgSrc="http://tnfs.tngou.net/image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_disease_content);
        ViewUtils.inject(this);

        context=this;

        titleTv.setText(getResources().getString(R.string.disease_detail_info));
        Bundle bundle=getIntent().getExtras();
        String disease=bundle.getString("disease");
//        System.out.println("2--疾病信息："+disease);
        JSONObject json=JSONObject.fromObject(disease);
        getDiseaseInfo(json);


    }
    private void getDiseaseInfo(JSONObject json){
        String name=json.getString("name");
        String imgUrl=json.getString("img");
        String symptom=json.getString("symptom");
        String diseaseText=json.getString("diseasetext");
        String careText=json.getString("caretext");
        String symptomText=json.getString("symptomtext");
        String food=json.getString("food");
        String foodText=json.getString("foodtext");
        String drug=json.getString("drug");
        String drugText=json.getString("drugtext");
        String causeText=json.getString("causetext");
        String checks=json.getString("checks");
        String checkText=json.getString("checktext");

        nameTv.setText(name);
//        BitmapUtils utils=new BitmapUtils();
//        utils.

        symptomTv.setText(symptom);
        BitmapUtils utils=new BitmapUtils(context);
        utils.display(igv,imgSrc+imgUrl);

        insWv.setVerticalScrollBarEnabled(false);
        insWv.loadDataWithBaseURL("",diseaseText,"text/html","utf-8","");

        careWv.setVerticalScrollBarEnabled(false);
        careWv.loadDataWithBaseURL("",careText,"text/html","utf-8","");
        symptomTextWv.setVerticalScrollBarEnabled(false);
        symptomTextWv.loadDataWithBaseURL("",symptomText,"text/html","utf-8","");
        foodWv.setVerticalScrollBarEnabled(false);
        foodWv.loadDataWithBaseURL("",food+foodText,"text/html","utf-8","");
        drugWv.setVerticalScrollBarEnabled(false);
        drugWv.loadDataWithBaseURL("",drug+drugText,"text/html","utf-8","");
        checkWv.setVerticalScrollBarEnabled(false);
        checkWv.loadDataWithBaseURL("",checks+checkText,"text/html","utf-8","");

//        insTv.setText(diseaseText);
//        careTv.setText(careText);
//        symptomTv.setText(symptomText);
//        foodTv.setText(food);
//        drugTv.setText(drug);

        causeWv.setVerticalScrollBarEnabled(false);
        causeWv.loadDataWithBaseURL("",causeText,"text/html","utf-8","");



    }
}
