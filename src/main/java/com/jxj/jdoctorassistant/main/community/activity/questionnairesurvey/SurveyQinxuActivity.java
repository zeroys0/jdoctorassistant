package com.jxj.jdoctorassistant.main.community.activity.questionnairesurvey;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.jxj.jdoctorassistant.thread.CommunityAssessThread;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SurveyQinxuActivity extends Activity {

    @Bind(R.id.back_igv)
    ImageView mBackIgv;
    @Bind(R.id.title_tv)
    TextView mTitleTv;
    @Bind(R.id.right_btn_igv)
    ImageView mRightBtnIgv;
    @Bind(R.id.right_btn_tv)
    TextView mRightBtnTv;

    @Bind(R.id.qinxu_first_answer_a_rb)
    RadioButton mQinxuFirstAnswerARb;
    @Bind(R.id.qinxu_first_answer_b_rb)
    RadioButton mQinxuFirstAnswerBRb;
    @Bind(R.id.qinxu_first_answer_c_rb)
    RadioButton mQinxuFirstAnswerCRb;
    @Bind(R.id.qinxu_first_answer_d_rb)
    RadioButton mQinxuFirstAnswerDRb;
    @Bind(R.id.qinxu_first_answer_rg)
    RadioGroup mQinxuFirstAnswerRg;

    @Bind(R.id.qinxu_second_answer_a_rb)
    RadioButton mQinxuSecondAnswerARb;
    @Bind(R.id.qinxu_second_answer_b_rb)
    RadioButton mQinxuSecondAnswerBRb;
    @Bind(R.id.qinxu_second_answer_c_rb)
    RadioButton mQinxuSecondAnswerCRb;
    @Bind(R.id.qinxu_second_answer_d_rb)
    RadioButton mQinxuSecondAnswerDRb;
    @Bind(R.id.qinxu_second_answer_rg)
    RadioGroup mQinxuSecondAnswerRg;

    @Bind(R.id.qinxu_third_answer_a_rb)
    RadioButton mQinxuThirdAnswerARb;
    @Bind(R.id.qinxu_third_answer_b_rb)
    RadioButton mQinxuThirdAnswerBRb;
    @Bind(R.id.qinxu_third_answer_c_rb)
    RadioButton mQinxuThirdAnswerCRb;
    @Bind(R.id.qinxu_third_answer_rg)
    RadioGroup mQinxuThirdAnswerRg;


    @Bind(R.id.qinxu_all_score_tv)
    TextView mQinxuAllScoreTv;
    @Bind(R.id.qinxu_jielun_tv)
    TextView mQinxuJielunTv;

    @Bind(R.id.qinxu_dont_have_zhenduan_rb)
    RadioButton mQinxuDontHaveZhenduanRb;
    @Bind(R.id.qinxu_have_zhenduan_rb)
    RadioButton mQinxuHaveZhenduanRb;
    @Bind(R.id.qinxu_is_have_zhenduan_rg)
    RadioGroup mQinxuIsHaveZhenduanRg;


    @Bind(R.id.qinxu_zhenduan_jieguo_et)
    EditText mQinxuZhenduanJieguoEt;
    @Bind(R.id.qinxu_zhenduan_time_tv)
    TextView mQinxuZhenduanTimeTv;
    @Bind(R.id.qinxu_zhenduan_yiyuan_name_et)
    EditText mQinxuZhenduanYiyuanNameEt;
    @Bind(R.id.survey_qinxu_submit_btn)
    RelativeLayout mSurveyQinxuSubmitBtn;

    int score1, score2, score3;
    int item1, item2, item3;


    int isHaveZhenduan;
    // 记录当前的时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;

    private CommunityAssessThread updatePadl;
    private JSONObject array;
    int level =0;
    int allscore =0;
    String AssessID;
    String Item11,Item12;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_qinxu);
        ButterKnife.bind(this);
        mTitleTv.setText("评估");

        sp = getSharedPreferences(AppConstant.USER_sp_name, this.MODE_PRIVATE);
        AssessID=sp.getString("AssessID", "");






        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        updateDateDisplay();




        mQinxuFirstAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mQinxuFirstAnswerARb.getId() == checkedId) {
                    score1 = 0;
                    item1=1;
                    panduanLevel();

                } else if (mQinxuFirstAnswerBRb.getId() == checkedId) {
                    score1 = 2;
                    item1=2;
                    panduanLevel();

                } else if (mQinxuFirstAnswerCRb.getId() == checkedId) {
                    score1 = 6;
                    item1=3;
                    panduanLevel();

                } else if (mQinxuFirstAnswerDRb.getId() == checkedId) {
                    score1 = 10;
                    item1=4;
                    panduanLevel();

                }


            }
        });


        mQinxuSecondAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mQinxuSecondAnswerARb.getId() == checkedId) {
                    score2 = 0;
                    item2 =1;
                    panduanLevel();

                } else if (mQinxuSecondAnswerBRb.getId() == checkedId) {
                    score2 = 2;
                    item2 =2;
                    panduanLevel();

                } else if (mQinxuSecondAnswerCRb.getId() == checkedId) {
                    score2 = 6;
                    item2 =3;
                    panduanLevel();
                } else if (mQinxuSecondAnswerDRb.getId() == checkedId) {
                    score2 = 10;
                    item2 =4;
                    panduanLevel();
                }


            }
        });

        mQinxuThirdAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mQinxuThirdAnswerARb.getId() == checkedId) {
                    score3 = 0;
                    item3 =1;
                    panduanLevel();

                } else if (mQinxuThirdAnswerBRb.getId() == checkedId) {
                    score3 = 1;
                    item3 =3;
                    panduanLevel();

                } else if (mQinxuThirdAnswerCRb.getId() == checkedId) {
                    score3 = 1;
                    item3 =4;
                    panduanLevel();
                }
            }
        });


         /*是否有医生诊断 */
        mQinxuIsHaveZhenduanRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mQinxuDontHaveZhenduanRb.getId() == checkedId) {
                    isHaveZhenduan = 0;
                    panduanLevel();
                } else if (mQinxuHaveZhenduanRb.getId() == checkedId) {
                    isHaveZhenduan = 1;
                    panduanLevel();

                }

            }
        });




    }

    private void panduanLevel() {
       allscore = score1 + score2 + score3;
        mQinxuAllScoreTv.setText("" + allscore);

        if (allscore >= 0 && allscore <= 1) {
            mQinxuJielunTv.setText("情绪行为正常");
            level = 1;
        } else if (allscore >= 2 && allscore <= 5) {
            mQinxuJielunTv.setText("情绪行为轻度异常");
            level = 2;
        } else if (allscore >= 6 && allscore <= 14) {
            mQinxuJielunTv.setText("情绪行为中度异常");
            level = 3;
        } else if (allscore >= 15) {
            mQinxuJielunTv.setText("情绪行为重度异常");
            level = 4;
        }

    }

    @OnClick({R.id.back_igv, R.id.qinxu_zhenduan_time_tv, R.id.survey_qinxu_submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;
            case R.id.qinxu_zhenduan_time_tv:
                new DatePickerDialog(SurveyQinxuActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mYear = year;
                                mMonth = month;
                                mDay = dayOfMonth;
                                //更新Button上显示的日期
                                updateDateDisplay();

                            }
                        }, mYear, mMonth, mDay).show();

                break;


            case R.id.survey_qinxu_submit_btn:
                Map map = new HashMap();
                //TODO  9个字段
                map.put("AssessID", AssessID);
                map.put("Item1", item1);
                map.put("Item2", item2);
                map.put("Item3", item3);
                map.put("DiagnosisTime",mQinxuZhenduanTimeTv.getText().toString());
                map.put("HospitalName",mQinxuZhenduanYiyuanNameEt.getText().toString());
                map.put("DiagnosisResults",mQinxuZhenduanJieguoEt.getText().toString());
                map.put("Conclude", level);
                map.put("IsHaveHospitalDiagnosis",isHaveZhenduan);

                JSONObject jsonObject=JSONObject.fromObject(map);
                addNewApplicant(jsonObject.toString());


                break;


        }
    }


    /**
     * 更新日期显示(记得month要+1，因为DatePicker索引是0-11)
     */
    private void updateDateDisplay() {
        mQinxuZhenduanTimeTv.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth + 1).append("-").append(mDay));
    }


    void addNewApplicant(String updatePadlJson) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = updatePadl.getResult();
                    if (UiUtil.isResultSuccess(SurveyQinxuActivity.this, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            array = jsonObject.getJSONObject("Data");
                            //Toast.makeText(SurveyQinxuActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

                            Intent intentTow = new Intent(SurveyQinxuActivity.this, SurveyShijueActivity.class);

                            sp.edit().putInt("Item13", level).commit();
                            sp.edit().putInt("Item13Score", allscore).commit();


                            startActivity(intentTow);
                            finish();
                        } else {
                            Toast.makeText(SurveyQinxuActivity.this, "保存失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        };

        updatePadl = new CommunityAssessThread(ApiConstant.UPDATEPEB, handler, this);
        updatePadl.setPeb(updatePadlJson);
        updatePadl.start();


    }


}
