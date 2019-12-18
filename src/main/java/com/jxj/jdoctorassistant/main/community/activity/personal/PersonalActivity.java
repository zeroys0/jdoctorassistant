package com.jxj.jdoctorassistant.main.community.activity.personal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.main.community.activity.questionnairesurvey.SurveyActivity;
import com.jxj.jdoctorassistant.thread.CommunityAssessThread;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
* 在这个界面 中 通过id 再次 获取一下 评估报告中的内容
*   GetPReport
*   查询 评估报告的 内容
*
*
*   解决 住宅电话的  问题 GETAPPLICANTBYID
*
*   在 onResume的 方法 重新获取一遍 数据 通过 GETAPPLICANTBYID  然后填充 JsonObject
*
*   传递到下一界面  的界面 的 就是完整的
*
* */
public class PersonalActivity extends Activity implements View.OnClickListener {

    @Bind(R.id.back_igv)
    ImageView mBackIgv;
    /*@Bind(R.id.title_tv)
    TextView mTitleTv;*/
   /* @Bind(R.id.right_btn_igv)
    ImageView mRightBtnIgv;*/

    @Bind(R.id.personal_username_tv)
    TextView mPersonalUsernameTv;
    @Bind(R.id.personal_assess_number_tv)
    TextView mPersonalAssessNumberTv;
    @Bind(R.id.personal_item1_tv)
    TextView mPersonalItem1Tv;
    @Bind(R.id.personal_native_tv)
    TextView mPersonalNativeTv;
    @Bind(R.id.evaluate_order_btn)
    Button mEvaluateOrderBtn;
    @Bind(R.id.recheck_result_btn)
    Button mRecheckResultBtn;
    /*--------------*/
    /*@Bind(R.id.community_pname)
    TextView mCommunityPname;*/

    @Bind(R.id.community_pid)
    TextView mCommunityPid;
    @Bind(R.id.community_psex)
    TextView mCommunityPsex;
    @Bind(R.id.community_pcommunity_type)
    TextView mCommunityPcommunityType;
    @Bind(R.id.community_pnumber)
    TextView mCommunityPnumber;
    @Bind(R.id.community_pnational)
    TextView mCommunityPnational;
    @Bind(R.id.community_peducation)
    TextView mCommunityPeducation;
    @Bind(R.id.community_pbirthday)
    TextView mCommunityPbirthday;
    @Bind(R.id.community_pprofessional)
    TextView mCommunityPprofessional;
    @Bind(R.id.community_pnative_place)
    TextView mCommunityPnativePlace;
    @Bind(R.id.community_pmarita)
    TextView mCommunityPmarita;

    @Bind(R.id.right_btn_tv)
    TextView mRightBtnTv;
    @Bind(R.id.textView2)
    TextView mTextView2;
    @Bind(R.id.community_pname)
    TextView mCommunityPname;

    Context context;
    @Bind(R.id.choose_personal_info)
    RadioButton mChoosePersonalInfo;
    @Bind(R.id.choose_personnal_life_situation)
    RadioButton mChoosePersonnalLifeSituation;

    @Bind(R.id.personal_info_add_huji_address)
    TextView mPersonalInfoAddHujiAddress;
    @Bind(R.id.personal_info_add_jizhu_address)
    TextView mPersonalInfoAddJizhuAddress;
    @Bind(R.id.personal_info_add_post_code)
    TextView mPersonalInfoAddPostCode;
    @Bind(R.id.personal_info_add_house_phone_number)
    TextView mPersonalInfoAddHousePhoneNumber;
    @Bind(R.id.personal_info_add_telphone_number)
    TextView mPersonalInfoAddTelphoneNumber;

    @Bind(R.id.life_sitution_dailiren_name)
    TextView mLifeSitutionDailirenName;
    @Bind(R.id.life_sitution_dailiren_relation)
    TextView mLifeSitutionDailirenRelation;
    @Bind(R.id.life_sitution_dailiren_address)
    TextView mLifeSitutionDailirenAddress;
    @Bind(R.id.life_sitution_dailiren_postcode)
    TextView mLifeSitutionDailirenPostcode;
    @Bind(R.id.life_sitution_dailiren_home_phone)
    TextView mLifeSitutionDailirenHomePhone;
    @Bind(R.id.life_sitution_dailiren_telphone)
    TextView mLifeSitutionDailirenTelphone;

    @Bind(R.id.life_sitution_financial_situation)
    TextView mLifeSitutionFinancialSituation;
    @Bind(R.id.life_sitution_living_situation)
    TextView mLifeSitutionLivingSituation;
    @Bind(R.id.life_sitution_living_type)
    TextView mLifeSitutionLivingType;
    @Bind(R.id.life_sitution_if_have_care)
    TextView mLifeSitutionIfHaveCare;
    @Bind(R.id.life_sitution_by_who_care)
    TextView mLifeSitutionByWhoCare;
    @Bind(R.id.life_sitution_doctore_type)
    TextView mLifeSitutionDoctoreType;
    @Bind(R.id.life_sitution_usual_hospital)
    TextView mLifeSitutionUsualHospital;


    @Bind(R.id.life_sitution)
    LinearLayout mLifeSitution;
    @Bind(R.id.personal_info)
    LinearLayout mPersonalInfo;

    private int applicantId;
    private int uId;
    private SharedPreferences sp;
    int pingguCishu;
    private Dialog dialog;
    private PopupWindow mPopWindow;
    private CommunityAssessThread addPinggu, getApplicantById;
    private JSONArray sendArray;
    private JSONObject jsonObject;

    private JSONObject tempJsonObject;

    private CommunityAssessThread getApplicantThread;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        ButterKnife.bind(this);
        context = this;
        sp = getSharedPreferences(AppConstant.USER_sp_name, context.MODE_PRIVATE);
        uId = sp.getInt(AppConstant.ADMIN_userId, 0);
        // mRightBtnTv.setVisibility(View.VISIBLE);
        // mRightBtnTv.setText("编辑");

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        tempJsonObject = JSONObject.fromObject(data.getString("array"));
        applicantId = tempJsonObject.getInt("Id");


    }

    private void initeView() {

        /*上方数据  */
        name = jsonObject.getString("Name");
        mPersonalUsernameTv.setText(name);
        mPersonalAssessNumberTv.setText("共" + jsonObject.getString("AssessNumber") + "次评估");
       /* if (jsonObject.getInt("Item1") == 0) {
            mPersonalItem1Tv.setText("正常");
        } else if (jsonObject.getInt("Item1") == 1) {
            mPersonalItem1Tv.setText("轻度");
        } else if (jsonObject.getInt("Item1") == 2) {
            mPersonalItem1Tv.setText("中度");
        } else if (jsonObject.getInt("Item1") == 3) {
            mPersonalItem1Tv.setText("重度");
        } else {
            mPersonalItem1Tv.setText("服务器没有返回结果");

        }*/


        /*个人信息  11   +5   条  */


        mCommunityPname.setText(jsonObject.getString("Name"));//姓名
        mCommunityPid.setText(jsonObject.getString("CardId"));//身份证号
        if (jsonObject.getInt("Gender") == 1) {//性别
            mCommunityPsex.setText("男");
        } else if (jsonObject.getInt("Gender") == 2) {
            mCommunityPsex.setText("女");
        } else {
            mCommunityPsex.setText("暂无");
        }

        mCommunityPcommunityType.setText(jsonObject.getString("SocialSecurity"));//社保类型
        mCommunityPnumber.setText(jsonObject.getString("Number"));//社区编号
        mCommunityPnational.setText(jsonObject.getString("Nation"));//民族


        if (jsonObject.getInt("Education") == 3) {//学历
            mCommunityPeducation.setText("初中及以下");
        } else if (jsonObject.getInt("Education") == 4) {
            mCommunityPeducation.setText("高中");
        } else if (jsonObject.getInt("Education") == 5) {
            mCommunityPeducation.setText("大学");
        } else if (jsonObject.getInt("Education") == 6) {
            mCommunityPeducation.setText("本科及以上");
        } else {
            mCommunityPeducation.setText("暂无");
        }

        mCommunityPbirthday.setText(jsonObject.getString("Birthday"));//出生日期
        mCommunityPprofessional.setText(jsonObject.getString("Professional"));//职业
        mCommunityPnativePlace.setText(jsonObject.getString("NativePlace"));//籍贯
        if (jsonObject.getInt("Marita") == 1) {//婚姻
            mCommunityPmarita.setText("未婚");
        } else if (jsonObject.getInt("Marita") == 2) {
            mCommunityPmarita.setText("已婚");
        } else if (jsonObject.getInt("Marita") == 3) {
            mCommunityPmarita.setText("丧偶");
        } else if (jsonObject.getInt("Marita") == 4) {
            mCommunityPmarita.setText("离婚");
        } else {
            mCommunityPmarita.setText("暂无");
        }


        /* 下方5条 */
        Log.v("qqqm", jsonObject.toString());

        JSONObject address = jsonObject.getJSONObject("CensusRegister");//户籍地址
        String paddressStr =address.getString("PAddressStr");
        if (paddressStr.equals("/////")){
            mPersonalInfoAddHujiAddress.setText("暂无");
        }else {
            String  tempaddresshuji=paddressStr.replace("/"," ");

            mPersonalInfoAddHujiAddress.setText(tempaddresshuji);
        }





        JSONObject juZhuAddress = jsonObject.getJSONObject("ResudentialAddress");//居住地址

        String  paddressStrResudential =juZhuAddress.getString("PAddressStr");

        if (paddressStrResudential.equals("/////")){

            mPersonalInfoAddJizhuAddress.setText("暂无");
        }else {
            String  tempaddressjuzhu=paddressStrResudential.replace("/"," ");
            mPersonalInfoAddJizhuAddress.setText(tempaddressjuzhu);
        }






        //邮 编 ZipCode
        // 住宅电话 移动电话
        mPersonalInfoAddPostCode.setText(jsonObject.getString("ZipCode"));

        mPersonalInfoAddHousePhoneNumber.setText(jsonObject.getString("HomePhone"));

        mPersonalInfoAddTelphoneNumber.setText(jsonObject.getString("MobilePhone"));

        /*-代理人   6  +   7条 数据  -----------------------*/
        mLifeSitutionDailirenName.setText(jsonObject.getString("AgentName"));
        mLifeSitutionDailirenRelation.setText(jsonObject.getString("Relation"));
        mLifeSitutionDailirenAddress.setText(jsonObject.getString("AgentAdress"));
        mLifeSitutionDailirenPostcode.setText(jsonObject.getString("AgentZipCode"));
        mLifeSitutionDailirenHomePhone.setText(jsonObject.getString("AgentHomePhone"));
        mLifeSitutionDailirenTelphone.setText(jsonObject.getString("AgentMobilePhone"));


        int economic = jsonObject.getInt("Economic");
        if (economic == 1) {
            mLifeSitutionFinancialSituation.setText("退休金");
        } else if (economic == 2) {
            mLifeSitutionFinancialSituation.setText("子女补贴");
        } else if (economic == 3) {
            mLifeSitutionFinancialSituation.setText("亲友资助");
        } else if (economic == 4) {
            mLifeSitutionFinancialSituation.setText("其他补贴");
        } else {
            mLifeSitutionFinancialSituation.setText("暂无");
        }


        int live = jsonObject.getInt("Live");
        if (live == 1) {
            mLifeSitutionLivingSituation.setText("与子女生活");
        } else if (live == 2) {
            mLifeSitutionLivingSituation.setText("与配偶生活");
        } else if (live == 3) {
            mLifeSitutionLivingSituation.setText("与亲戚朋友生活");
        } else if (live == 4) {
            mLifeSitutionLivingSituation.setText("独自生活");
        } else {
            mLifeSitutionLivingSituation.setText("暂无");

        }

        int housing = jsonObject.getInt("Housing");
        if (housing == 1) {
            mLifeSitutionLivingType.setText("产权房");
        } else if (housing == 2) {
            mLifeSitutionLivingType.setText("租赁房");
        } else if (housing == 3) {
            mLifeSitutionLivingType.setText("廉住房");
        } else if (housing == 4) {
            mLifeSitutionLivingType.setText("私房");
        } else {
            mLifeSitutionLivingType.setText("暂无");
        }

        int isHelp = jsonObject.getInt("IsHelp");
        if (isHelp == 1) {
            mLifeSitutionIfHaveCare.setText("是");
        } else if (isHelp == 2) {
            mLifeSitutionIfHaveCare.setText("否");
        } else {
            mLifeSitutionIfHaveCare.setText("暂无");
        }


        //mLifeSitutionByWhoCare
        int helper = jsonObject.getInt("Helper");
        if (helper == 1) {
            mLifeSitutionByWhoCare.setText("子女");
        } else if (helper == 2) {
            mLifeSitutionByWhoCare.setText("配偶");
        } else if (helper == 3) {
            mLifeSitutionByWhoCare.setText("亲友");
        } else if (helper == 4) {
            mLifeSitutionByWhoCare.setText("其他");
        } else {
            mLifeSitutionByWhoCare.setText("暂无");
        }


        int treatmentMethod = jsonObject.getInt("TreatmentMethod");
        if (treatmentMethod == 1) {
            mLifeSitutionDoctoreType.setText("家庭病房");
        } else if (treatmentMethod == 2) {
            mLifeSitutionDoctoreType.setText("社区生活");
        } else if (treatmentMethod == 3) {
            mLifeSitutionDoctoreType.setText("外出就诊");
        } else if (treatmentMethod == 4) {
            mLifeSitutionDoctoreType.setText("其他");
        } else {
            mLifeSitutionDoctoreType.setText("暂无");
        }


        mLifeSitutionUsualHospital.setText(jsonObject.getString("Hospital"));

    }


    @OnClick({R.id.evaluate_order_btn, R.id.recheck_result_btn, R.id.back_igv, R.id.right_btn_tv, R.id.choose_personal_info, R.id.choose_personnal_life_situation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;

            case R.id.right_btn_tv:
                Intent intentToXiugai = new Intent(this, AddApplicantActivity.class);
                intentToXiugai.putExtra("intentSize", 1);// 1 是编辑
                Log.v("给编辑页面传递的值", jsonObject.toString());
                intentToXiugai.putExtra("jsonObject", jsonObject.toString());
                startActivity(intentToXiugai);
                break;

            case R.id.evaluate_order_btn: //评估 记录
                Intent intent = new Intent(this, PersonalPinguJiluActivity.class);
                intent.putExtra("array", jsonObject.toString());
                startActivity(intent);
                break;

            case R.id.recheck_result_btn:// 进入评估
                showMyDialog();
                break;
            case R.id.choose_personal_info:
                mLifeSitution.setVisibility(View.GONE);
                mPersonalInfo.setVisibility(View.VISIBLE);
                break;

            case R.id.choose_personnal_life_situation:
                mLifeSitution.setVisibility(View.VISIBLE);
                mPersonalInfo.setVisibility(View.GONE);
                break;

        }

    }

    private void showMyDialog() {
        View contentView = LayoutInflater.from(PersonalActivity.this).inflate(R.layout.layout_popupwindow_choose_level, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);

        mPopWindow.setOutsideTouchable(true);


        //实例化一个ColorDrawable颜色明为半透
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        //popupWindow.setBackgroundDrawable(dw);
        mPopWindow.setBackgroundDrawable(dw);
        //mPopWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        backgroundAlpha(0.8f);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        mPopWindow.showAsDropDown(mRecheckResultBtn, 0, 0);
        //TODO
        //设置各个控件的点击响应
        TextView tv1 = (TextView) contentView.findViewById(R.id.popupView);
        TextView tv2 = (TextView) contentView.findViewById(R.id.popupView_second);
        TextView tv3 = (TextView) contentView.findViewById(R.id.popupView_third);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);


        //显示PopupWindow
        View rootview = LayoutInflater.from(PersonalActivity.this).inflate(R.layout.activity_personal, null);
        mPopWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.popupView: {

                //Toast.makeText(this, "选择了首次评估", Toast.LENGTH_SHORT).show();
                pingguCishu = 0;
                mPopWindow.dismiss();
                gotoPinggu(pingguCishu);
            }
            break;
            case R.id.popupView_second: {
                //Toast.makeText(this, "选择了复选评估", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                pingguCishu = 1;
                gotoPinggu(pingguCishu);
            }
            break;
            case R.id.popupView_third: {
                //Toast.makeText(this, "选择了持续评估", Toast.LENGTH_SHORT).show();
                mPopWindow.dismiss();
                pingguCishu = 2;
                gotoPinggu(pingguCishu);
            }
            break;
        }
    }

    //新增评估
    private void gotoPinggu(int updatePadlJson) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = addPinggu.getResult();
                    if (UiUtil.isResultSuccess(PersonalActivity.this, result)) {
                        JSONObject json = JSONObject.fromObject(result);
                        int code = json.getInt("code");
                        if (code == 200) {
                            JSONObject object = json.getJSONObject("Data");
                            //Toast.makeText(PersonalActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            Intent intentTow = new Intent(PersonalActivity.this, SurveyActivity.class);
                            String AssessID = object.getString("AssessID");

                            Log.v("qqq", AssessID + "个人界面写入sp 的值 ");
                            sp.edit().putString("AssessID", AssessID).commit();


                            startActivity(intentTow);

                        } else {
                            Toast.makeText(PersonalActivity.this, "保存失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        };


        addPinggu = new CommunityAssessThread(ApiConstant.ADDPASSESS, handler, this);
        int cid = Integer.parseInt(jsonObject.getString("Id"));
        addPinggu.setAssessType(updatePadlJson);
        addPinggu.setCid(cid);//申请人id
        addPinggu.setUid(uId);//评估id
        addPinggu.start();


    }

    private void getApplicantById(int applicantId) {

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = getApplicantById.getResult();
                    if (UiUtil.isResultSuccess(PersonalActivity.this, result)) {
                        JSONObject json = JSONObject.fromObject(result);
                        int code = json.getInt("code");
                        if (code == 200) {
                            jsonObject = json.getJSONObject("Data");
                            initeView();

                            Log.v("从首页传过来的id获取的值", jsonObject.toString());


                        } else {
                            //Toast.makeText(PersonalActivity.this, "保存失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        };


        getApplicantById = new CommunityAssessThread(ApiConstant.GETAPPLICANTBYID, handler, this);
        getApplicantById.setApplicantId(applicantId);
        getApplicantById.start();


    }

    @Override
    protected void onResume() {
        super.onResume();
        getApplicantById(applicantId);


    }

    // 设置popwindow弹出的阴影效果
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }





}
