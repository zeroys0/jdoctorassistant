package com.jxj.jdoctorassistant.main.community.activity.questionnairesurvey;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
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

public class SurveyRenzhiActivity extends Activity {

    @Bind(R.id.back_igv)
    ImageView mBackIgv;
    @Bind(R.id.title_tv)
    TextView mTitleTv;
    @Bind(R.id.renzhi_first_answer_a_rb)
    RadioButton mRenzhiFirstAnswerARb;
    @Bind(R.id.renzhi_first_answer_b_rb)
    RadioButton mRenzhiFirstAnswerBRb;
    @Bind(R.id.renzhi_first_answer_c_rb)
    RadioButton mRenzhiFirstAnswerCRb;
    @Bind(R.id.renzhi_first_answer_d_rb)
    RadioButton mRenzhiFirstAnswerDRb;
    @Bind(R.id.renzhi_first_answer_rg)
    RadioGroup mRenzhiFirstAnswerRg;

    @Bind(R.id.renzhi_second_answer_a_rb)
    RadioButton mRenzhiSecondAnswerARb;
    @Bind(R.id.renzhi_second_answer_b_rb)
    RadioButton mRenzhiSecondAnswerBRb;
    @Bind(R.id.renzhi_second_answer_c_rb)
    RadioButton mRenzhiSecondAnswerCRb;
    @Bind(R.id.renzhi_second_answer_rg)
    RadioGroup mRenzhiSecondAnswerRg;


    @Bind(R.id.renzhi_third_answer_a_rb)
    RadioButton mRenzhiThirdAnswerARb;
    @Bind(R.id.renzhi_third_answer_b_rb)
    RadioButton mRenzhiThirdAnswerBRb;
    @Bind(R.id.renzhi_third_answer_c_rb)
    RadioButton mRenzhiThirdAnswerCRb;
    @Bind(R.id.renzhi_third_answer_rg)
    RadioGroup mRenzhiThirdAnswerRg;


    @Bind(R.id.renzhi_fourth_answer_a_rb)
    RadioButton mRenzhiFourthAnswerARb;
    @Bind(R.id.renzhi_fourth_answer_b_rb)
    RadioButton mRenzhiFourthAnswerBRb;
    @Bind(R.id.renzhi_fourth_answer_c_rb)
    RadioButton mRenzhiFourthAnswerCRb;
    @Bind(R.id.renzhi_fourth_answer_rg)
    RadioGroup mRenzhiFourthAnswerRg;


    @Bind(R.id.survey_renzhi_submit_btn)
    RelativeLayout mSurveyRenzhiSubmitBtn;


    @Bind(R.id.renzhi_all_score_tv)
    TextView mRenzhiAllScoreTv;
    @Bind(R.id.renzhi_jielun_tv)
    TextView mRenzhiJielunTv;

    @Bind(R.id.renzhi_dont_have_zhenduan_rb)
    RadioButton mRenzhiDontHaveZhenduanRb;
    @Bind(R.id.renzhi_have_zhenduan_rb)
    RadioButton mRenzhiHaveZhenduanRb;
    @Bind(R.id.renzhi_is_have_zhenduan_rg)
    RadioGroup mRenzhiIsHaveZhenduanRg;

    @Bind(R.id.renzhi_zhenduan_jieguo_et)
    EditText mRenzhiZhenduanJieguoEt;
    @Bind(R.id.renzhi_zhenduan_time_tv)
    TextView mRenzhiZhenduanTimeTv;
    @Bind(R.id.renzhi_zhenduan_yiyuan_name_ed)
    EditText mRenzhiZhenduanYiyuanNameEd;

    String zhenduanJieguo, zhenduanTime, yiyuanName;

    private CommunityAssessThread updatePca;
    int score1, score2, score3, score4;
    int item1, item2, item3, item4;

    private Context context;
    int isHaveZhenduan;

    // 记录当前的时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;



    private JSONObject array;

    String AssessID;
    String Item11;
    String Item11Score;
    private SharedPreferences sp;
    int level =0;
    int allscore =0;

    /*  需求 在 获取上一个界面 得到的AssId  界面 填充 完数据
        看接口文档 需要多少个参数
        提交




    提交       */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_renzhi);
        ButterKnife.bind(this);
        mTitleTv.setText("评估");
        context = this;


        sp = getSharedPreferences(AppConstant.USER_sp_name, this.MODE_PRIVATE);
        AssessID=sp.getString("AssessID", "");






        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        updateDateDisplay();


        mRenzhiFirstAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mRenzhiFirstAnswerARb.getId() == checkedId) {
                    score1 = 0;
                    item1 = 0;
                    panduanLevel();

                } else if (mRenzhiFirstAnswerBRb.getId() == checkedId) {
                    score1 = 2;
                    item1 = 1;
                    panduanLevel();

                } else if (mRenzhiFirstAnswerCRb.getId() == checkedId) {
                    score1 = 5;
                    item1 = 2;
                    panduanLevel();
                } else if (mRenzhiFirstAnswerDRb.getId() == checkedId) {
                    score1 = 10;
                    item1 = 3;
                    panduanLevel();
                }


            }
        });


        mRenzhiSecondAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mRenzhiSecondAnswerARb.getId() == checkedId) {
                    score2 = 0;
                    item2 = 0;
                    panduanLevel();
                } else if (mRenzhiSecondAnswerBRb.getId() == checkedId) {
                    score2 = 5;
                    item2 = 1;
                    panduanLevel();
                } else if (mRenzhiSecondAnswerCRb.getId() == checkedId) {
                    score2 = 10;
                    item2 = 2;
                    panduanLevel();
                }

            }
        });

        mRenzhiThirdAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mRenzhiThirdAnswerARb.getId() == checkedId) {
                    score3 = 0;
                    item3 = 0;
                    panduanLevel();
                } else if (mRenzhiThirdAnswerBRb.getId() == checkedId) {
                    score3 = 5;
                    item3 = 1;
                    panduanLevel();
                } else if (mRenzhiThirdAnswerCRb.getId() == checkedId) {
                    score3 = 10;
                    item3 = 2;
                    panduanLevel();
                }

            }
        });

        mRenzhiFourthAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mRenzhiFourthAnswerARb.getId() == checkedId) {
                    score4 = 0;
                    item4 = 0;
                    panduanLevel();
                } else if (mRenzhiFourthAnswerBRb.getId() == checkedId) {
                    score4 = 5;
                    item4 = 1;
                    panduanLevel();
                } else if (mRenzhiFourthAnswerCRb.getId() == checkedId) {
                    score4 = 10;
                    item4 = 2;
                    panduanLevel();
                }

            }
        });
        /*是否有医生诊断 */
        mRenzhiIsHaveZhenduanRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mRenzhiDontHaveZhenduanRb.getId() == checkedId) {
                    isHaveZhenduan = 0;
                    panduanLevel();
                } else if (mRenzhiHaveZhenduanRb.getId() == checkedId) {
                    isHaveZhenduan = 1;
                    panduanLevel();

                }

            }
        });


        mRenzhiZhenduanTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(SurveyRenzhiActivity.this,
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

            }

        });


    }

    /**
     * 更新日期显示(记得month要+1，因为DatePicker索引是0-11)
     */
    private void updateDateDisplay() {
        mRenzhiZhenduanTimeTv.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth + 1).append("-").append(mDay));
    }

    private void panduanLevel() {
        allscore = score1 + score2 + score3 + score4;


        mRenzhiAllScoreTv.setText("" + allscore);

        if (allscore >= 0 && allscore <= 4) {
            mRenzhiJielunTv.setText("认知能力正常");
            level = 1;
        } else if (allscore >= 5 && allscore <= 14) {
            mRenzhiJielunTv.setText("认知能力轻度缺失");
            level = 2;
        } else if (allscore >= 15 && allscore <= 29) {
            mRenzhiJielunTv.setText("认知能力中度缺失");
            level = 3;
        } else if (allscore >= 30) {
            mRenzhiJielunTv.setText("认知能力重度度缺失");
            level = 4;
        }


    }


    @OnClick({R.id.back_igv, R.id.survey_renzhi_submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;
            case R.id.survey_renzhi_submit_btn:

                Map map = new HashMap();

                //TODO 10个字段
                map.put("AssessID",  AssessID);
                map.put("Item1", item1);
                map.put("Item2", item2);
                map.put("Item3", item3);
                map.put("Item4", item4);
                map.put("DiagnosisTime", mRenzhiZhenduanTimeTv.getText().toString());
                map.put("HospitalName", mRenzhiZhenduanYiyuanNameEd.getText().toString());
                map.put("DiagnosisResults", mRenzhiZhenduanJieguoEt.getText().toString());
                map.put("Conclude", level);
                map.put("IsHaveHospitalDiagnosis", isHaveZhenduan);



                JSONObject jsonObject=JSONObject.fromObject(map);
                addNewApplicant(jsonObject.toString());
                break;


        }
    }

    void addNewApplicant(String updatePcaJson) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = updatePca.getResult();
                    if (UiUtil.isResultSuccess(SurveyRenzhiActivity.this, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            array = jsonObject.getJSONObject("Data");
                            //Toast.makeText(SurveyRenzhiActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            Intent intentTow = new Intent(SurveyRenzhiActivity.this, SurveyQinxuActivity.class);
                            sp.edit().putInt("Item12", level).commit();
                            sp.edit().putInt("Item12Score", allscore).commit();
                            startActivity(intentTow);
                            finish();

                        } else {
                            Toast.makeText(SurveyRenzhiActivity.this, "保存失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        };

        updatePca = new CommunityAssessThread(ApiConstant.UPDATEPCA, handler, this);
        updatePca.setPca(updatePcaJson);
        updatePca.start();


    }


}
