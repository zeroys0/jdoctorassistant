package com.jxj.jdoctorassistant.main.community.activity.questionnairesurvey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.main.community.activity.pinggu.QuerenPinguActivity;
import com.jxj.jdoctorassistant.thread.CommunityAssessThread;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SurveyBeijingActivity extends Activity {

    @Bind(R.id.back_igv)
    ImageView mBackIgv;
    @Bind(R.id.title_tv)
    TextView mTitleTv;
    @Bind(R.id.right_btn_igv)
    ImageView mRightBtnIgv;
    @Bind(R.id.right_btn_tv)
    TextView mRightBtnTv;

    @Bind(R.id.beijing_first_answer_a_rb)
    RadioButton mBeijingFirstAnswerARb;
    @Bind(R.id.beijing_first_answer_b_rb)
    RadioButton mBeijingFirstAnswerBRb;
    @Bind(R.id.beijing_first_answer_c_rb)
    RadioButton mBeijingFirstAnswerCRb;
    @Bind(R.id.beijing_first_answer_d_rb)
    RadioButton mBeijingFirstAnswerDRb;
    @Bind(R.id.beijing_first_answer_rg)
    RadioGroup mBeijingFirstAnswerRg;

    @Bind(R.id.beijing_second_answer_a_rb)
    RadioButton mBeijingSecondAnswerARb;
    @Bind(R.id.beijing_second_answer_b_rb)
    RadioButton mBeijingSecondAnswerBRb;
    @Bind(R.id.beijing_second_answer_c_rb)
    RadioButton mBeijingSecondAnswerCRb;
    @Bind(R.id.beijing_second_answer_d_rb)
    RadioButton mBeijingSecondAnswerDRb;
    @Bind(R.id.beijing_second_answer_rg)
    RadioGroup mBeijingSecondAnswerRg;

    @Bind(R.id.beijing_third_answer_a_rb)
    RadioButton mBeijingThirdAnswerARb;
    @Bind(R.id.beijing_third_answer_b_rb)
    RadioButton mBeijingThirdAnswerBRb;
    @Bind(R.id.beijing_third_answer_c_rb)
    RadioButton mBeijingThirdAnswerCRb;
    @Bind(R.id.beijing_third_answer_d_rb)
    RadioButton mBeijingThirdAnswerDRb;
    @Bind(R.id.beijing_third_answer_rg)
    RadioGroup mBeijingThirdAnswerRg;

    @Bind(R.id.beijing_fourth_one_answer_a_rb)
    RadioButton mBeijingFourthOneAnswerARb;
    @Bind(R.id.beijing_fourth_one_answer_b_rb)
    RadioButton mBeijingFourthOneAnswerBRb;
    @Bind(R.id.beijing_fourth_one_answer_rg)
    RadioGroup mBeijingFourthOneAnswerRg;

    @Bind(R.id.beijing_fourth_two_answer_a_rb)
    RadioButton mBeijingFourthTwoAnswerARb;
    @Bind(R.id.beijing_fourth_two_answer_b_rb)
    RadioButton mBeijingFourthTwoAnswerBRb;
    @Bind(R.id.beijing_fourth_two_answer_rg)
    RadioGroup mBeijingFourthTwoAnswerRg;

    @Bind(R.id.beijing_fourth_third_answer_a_rb)
    RadioButton mBeijingFourthThirdAnswerARb;
    @Bind(R.id.beijing_fourth_third_answer_b_rb)
    RadioButton mBeijingFourthThirdAnswerBRb;
    @Bind(R.id.beijing_fourth_third_answer_c_rb)
    RadioButton mBeijingFourthThirdAnswerCRb;
    @Bind(R.id.beijing_fourth_third_answer_rg)
    RadioGroup mBeijingFourthThirdAnswerRg;


    @Bind(R.id.beijing_fourth_fourth_answer_a_rb)
    RadioButton mBeijingFourthFourthAnswerARb;
    @Bind(R.id.beijing_fourth_fourth_answer_b_rb)
    RadioButton mBeijingFourthFourthAnswerBRb;
    @Bind(R.id.beijing_fourth_fourth_answer_c_rb)
    RadioButton mBeijingFourthFourthAnswerCRb;
    @Bind(R.id.beijing_fouth_answer_rg)
    RadioGroup mBeijingFouthAnswerRg;


    @Bind(R.id.beijing_fifth_answer_a_rb)
    RadioButton mBeijingFifthAnswerARb;
    @Bind(R.id.beijing_fifth_answer_b_rb)
    RadioButton mBeijingFifthAnswerBRb;
    @Bind(R.id.beijing_fifth_answer_c_rb)
    RadioButton mBeijingFifthAnswerCRb;
    @Bind(R.id.beijing_fifth_answer_d_rb)
    RadioButton mBeijingFifthAnswerDRb;
    @Bind(R.id.beijing_fifth_answer_rg)
    RadioGroup mBeijingFifthAnswerRg;


    @Bind(R.id.beijing_sixth_zhongdajibing_et)
    EditText mBeijingSixthZhongdajibingEt;


    @Bind(R.id.survey_beijing_submit_btn)
    RelativeLayout mSurveyBeijingSubmitBtn;


    private CommunityAssessThread updatePadl;
    private JSONObject array;
    String AssessID;


    private SharedPreferences sp;
    int item1, item2, item3, item41, item42, item43, item44, item5, item6;
    int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_beijing);
        ButterKnife.bind(this);
        mTitleTv.setText("评估");


        sp = getSharedPreferences(AppConstant.USER_sp_name, this.MODE_PRIVATE);
        AssessID=sp.getString("AssessID", "");





        mBeijingFirstAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mBeijingFirstAnswerARb.getId() == checkedId) {

                    item1 = 0;


                } else if (mBeijingFirstAnswerBRb.getId() == checkedId) {

                    item1 = 1;


                } else if (mBeijingFirstAnswerCRb.getId() == checkedId) {

                    item1 = 2;

                } else if (mBeijingFirstAnswerDRb.getId() == checkedId) {

                    item1 = 3;


                }


            }
        });


        mBeijingSecondAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mBeijingSecondAnswerARb.getId() == checkedId) {

                    item2 = 0;


                } else if (mBeijingSecondAnswerBRb.getId() == checkedId) {

                    item2 = 1;


                } else if (mBeijingSecondAnswerCRb.getId() == checkedId) {

                    item2 = 2;

                } else if (mBeijingSecondAnswerDRb.getId() == checkedId) {

                    item2 = 3;


                }


            }
        });


        mBeijingThirdAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mBeijingThirdAnswerARb.getId() == checkedId) {

                    item3 = 0;


                } else if (mBeijingThirdAnswerBRb.getId() == checkedId) {

                    item3 = 1;


                } else if (mBeijingThirdAnswerCRb.getId() == checkedId) {

                    item3 = 2;

                } else if (mBeijingThirdAnswerDRb.getId() == checkedId) {

                    item3 = 3;


                }


            }
        });

        mBeijingFourthOneAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mBeijingFourthOneAnswerARb.getId() == checkedId) {

                    item41 = 0;


                } else if (mBeijingFourthOneAnswerBRb.getId() == checkedId) {

                    item41 = 1;


                }


            }
        });

        mBeijingFourthTwoAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mBeijingFourthTwoAnswerARb.getId() == checkedId) {

                    item42 = 0;


                } else if (mBeijingFourthTwoAnswerBRb.getId() == checkedId) {

                    item42 = 1;


                }


            }
        });

        mBeijingFourthThirdAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mBeijingFourthThirdAnswerARb.getId() == checkedId) {

                    item43 = 0;


                } else if (mBeijingFourthThirdAnswerBRb.getId() == checkedId) {

                    item43 = 1;


                } else if (mBeijingFourthThirdAnswerCRb.getId() == checkedId) {

                    item43 = 3;


                }


            }
        });

        mBeijingFouthAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mBeijingFourthFourthAnswerARb.getId() == checkedId) {

                    item44 = 0;


                } else if (mBeijingFourthFourthAnswerBRb.getId() == checkedId) {

                    item44 = 1;


                } else if (mBeijingFourthFourthAnswerCRb.getId() == checkedId) {

                    item44 = 3;


                }


            }
        });


        mBeijingFifthAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mBeijingFifthAnswerARb.getId() == checkedId) {

                    item5 = 0;


                } else if (mBeijingFifthAnswerBRb.getId() == checkedId) {

                    item5 = 1;


                } else if (mBeijingFifthAnswerCRb.getId() == checkedId) {

                    item5 = 2;

                } else if (mBeijingFifthAnswerDRb.getId() == checkedId) {

                    item5 = 3;


                }


            }
        });


    }


    @OnClick({R.id.back_igv, R.id.survey_beijing_submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;
            case R.id.survey_beijing_submit_btn:
                Map map = new HashMap();
                //TODO  10个字段
                map.put("AssessID", AssessID);
                map.put("Item1", item1);
                map.put("Item2", item2);
                map.put("Item3", item3);
                map.put("Item41", item41);
                map.put("Item42", item42);
                map.put("Item43", item43);
                map.put("Item44", item44);
                map.put("Item5", item5);
                map.put("Item6", mBeijingSixthZhongdajibingEt.getText().toString());
                JSONObject jsonObject=JSONObject.fromObject(map);
                addNewApplicant(jsonObject.toString());
                break;


        }


    }


    void addNewApplicant(String updatePadlJson) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = updatePadl.getResult();
                    if (UiUtil.isResultSuccess(SurveyBeijingActivity.this, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            array = jsonObject.getJSONObject("Data");
                           // Toast.makeText(SurveyBeijingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

                            Intent intentTow = new Intent(SurveyBeijingActivity.this, QuerenPinguActivity.class);
  /*展示用 0 编辑用1*/
                            intentTow.putExtra("intentSize", 1);


                            startActivity(intentTow);
                            finish();
                        } else {
                            Toast.makeText(SurveyBeijingActivity.this, "保存失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        };

        updatePadl = new CommunityAssessThread(ApiConstant.UPDATEPSCIALLIFE, handler, this);
        updatePadl.setPsciallife(updatePadlJson);
        updatePadl.start();


    }

}
