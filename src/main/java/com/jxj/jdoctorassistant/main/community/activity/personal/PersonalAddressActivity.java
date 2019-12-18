package com.jxj.jdoctorassistant.main.community.activity.personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PersonalAddressActivity extends Activity {
    @ViewInject(value = R.id.title_tv, parentId = R.id.address_title)
    private TextView titleTv;
    @ViewInject(value = R.id.right_btn_tv, parentId = R.id.address_title)
    private TextView saveTv;

    @ViewInject(R.id.district_etv)
    private EditText districtEtv;
    @ViewInject(R.id.street_etv)
    private EditText streetEtv;
    @ViewInject(R.id.road_etv)
    private EditText roadEtv;
    @ViewInject(R.id.village_etv)
    private EditText villageEtv;
    @ViewInject(R.id.house_etv)
    private EditText houseEtv;
    @ViewInject(R.id.room_etv)
    private EditText roomEtv;

    @OnClick({R.id.back_igv})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;
        }
    }

    public final static String BACKADDRESS = "back_address";
    public final static int BACKRESULTCODE = 5;

//    private String address;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_address);
        ViewUtils.inject(this);

        context = this;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String str = null;
        boolean isUpdate=false;
        if (bundle!=null&&bundle.containsKey("is_update")) {
           isUpdate=bundle.getBoolean("is_update");
        }
//        int add = bundle.getInt("add");
        if (isUpdate) {
//            int type=bundle.getInt("type");

            if (bundle.containsKey(AddApplicantActivity.RESADDRESS)) {
                str = bundle.getString(AddApplicantActivity.RESADDRESS);
                setValue(str);
            }

            if (bundle.containsKey(AddApplicantActivity.CESADDRESS)) {
                str = bundle.getString(AddApplicantActivity.CESADDRESS);
                setValue(str);
            }
//            System.out.println("拿到的type:"+type);
        }
//        System.out.println("拿到的add:"+add);
//        System.out.println("拿到的地址值:"+str);


//        String str=bundle.getString(AddApplicantActivity.ADDRESS);


        titleTv.setText("编辑地址");
        saveTv.setVisibility(View.VISIBLE);
        saveTv.setText(getResources().getString(R.string.save));
        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(districtEtv.getEditableText().toString().trim()) ||
                        TextUtils.isEmpty(streetEtv.getEditableText().toString().trim()) ||
                        TextUtils.isEmpty(roadEtv.getEditableText().toString().trim()) ||
                        TextUtils.isEmpty(villageEtv.getEditableText().toString().trim()) ||
                        TextUtils.isEmpty(houseEtv.getEditableText().toString().trim()) ||
                        TextUtils.isEmpty(roomEtv.getEditableText().toString().trim())) {
                    UiUtil.showToast(context, "请输入完整信息");
                    return;
                }

                Map map = new HashMap();
                map.put("District", districtEtv.getEditableText().toString().trim());
                map.put("Street", streetEtv.getEditableText().toString().trim());
                map.put("Road", roadEtv.getEditableText().toString().trim());
                map.put("Village", villageEtv.getEditableText().toString().trim());
                map.put("House", houseEtv.getEditableText().toString().trim());
                map.put("Room", roomEtv.getEditableText().toString().trim());
                JSONObject jsonObject = JSONObject.fromObject(map);

                Intent intent1 = new Intent();
                intent1.putExtra(BACKADDRESS, jsonObject.toString());
                setResult(BACKRESULTCODE, intent1);
                finish();
            }
        });


    }

    void setValue(String str) {
        JSONObject j = JSONObject.fromObject(str);
        districtEtv.setText(j.getString("District"));
        streetEtv.setText(j.getString("Street"));
        roadEtv.setText(j.getString("Road"));
        villageEtv.setText(j.getString("Village"));
        houseEtv.setText(j.getString("House"));
        roomEtv.setText(j.getString("Room"));
    }

}
