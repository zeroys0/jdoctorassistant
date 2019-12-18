package com.jxj.jdoctorassistant.main.community.activity.questionnairesurvey;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.util.Log;
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

public class SurveyShijueActivity extends Activity {

    @Bind(R.id.back_igv)
    ImageView mBack;
    @Bind(R.id.title_tv)
    TextView mTitleTv;
    @Bind(R.id.right_btn_igv)
    ImageView mRightBtnIgv;
    @Bind(R.id.right_btn_tv)
    TextView mRightBtnTv;


    @Bind(R.id.shili_first_answer_a_rb)
    RadioButton mShiliFirstAnswerARb;
    @Bind(R.id.shili_first_answer_b_rb)
    RadioButton mShiliFirstAnswerBRb;
    @Bind(R.id.shili_first_answer_c_rb)
    RadioButton mShiliFirstAnswerCRb;
    @Bind(R.id.shili_first_answer_rg)
    RadioGroup mShiliFirstAnswerRg;


    @Bind(R.id.shijue_defen)
    TextView mShijueDefen;
    @Bind(R.id.shijue_jielun)
    TextView mShijueJielun;

    @Bind(R.id.shijue_dont_have_zhenduan_rb)
    RadioButton mShijueDontHaveZhenduanRb;
    @Bind(R.id.shijue_have_zhenduan_rb)
    RadioButton mShijueHaveZhenduanRb;
    @Bind(R.id.shijue_ishave_zhenduan_rg)
    RadioGroup mShijueIshaveZhenduanRg;

    @Bind(R.id.shijue_zhenduan_jieguo_et)
    EditText mShijueZhenduanJieguoEt;
    @Bind(R.id.shijue_zhenduan_shijian_tv)
    TextView mShijueZhenduanShijian;
    @Bind(R.id.shijue_yiyuan_name_et)
    EditText mShijueYiyuanNameEt;
    @Bind(R.id.survey_shijue_submit_btn)
    RelativeLayout mSurveyShijueSubmitBtn;

    int score1 =0;
    int isHaveZhenduan;
    int item1;
    private CommunityAssessThread updatePadl;

    private JSONObject array;

    String AssessID;
    int level =0;
    int allscore =0;

    // 记录当前的时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_shijue);
        ButterKnife.bind(this);
        mTitleTv.setText("评估");


        sp = getSharedPreferences(AppConstant.USER_sp_name, this.MODE_PRIVATE);
        AssessID=sp.getString("AssessID", "");


        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        updateDateDisplay();

        mShiliFirstAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mShiliFirstAnswerARb.getId() == checkedId) {
                    score1 = 0;
                    item1 = 1;
                    panduanLevel();

                } else if (mShiliFirstAnswerBRb.getId() == checkedId) {
                    score1 = 5;
                    item1 = 3;
                    panduanLevel();

                } else if (mShiliFirstAnswerCRb.getId() == checkedId) {
                    score1 = 10;
                    item1 = 4;
                    panduanLevel();

                }


            }
        });


        /*是否有医生诊断 */
        mShijueIshaveZhenduanRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mShijueDontHaveZhenduanRb.getId() == checkedId) {
                    isHaveZhenduan = 0;
                    panduanLevel();
                } else if (mShijueHaveZhenduanRb.getId() == checkedId) {
                    isHaveZhenduan = 1;
                    panduanLevel();

                }

            }
        });


    }


    private void panduanLevel() {
        allscore = score1;
        mShijueDefen.setText("" + allscore);

        if (allscore == 0) {
            mShijueJielun.setText("正常");
            level = 1;
        } else if (allscore == 5) {
            mShijueJielun.setText("中度障碍");
            level = 3;
        } else if (allscore == 10) {
            mShijueJielun.setText("重度障碍");
            level = 4;
        }

    }


    @OnClick({R.id.shijue_zhenduan_shijian_tv, R.id.survey_shijue_submit_btn, R.id.back_igv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shijue_zhenduan_shijian_tv:
                new DatePickerDialog(SurveyShijueActivity.this,
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
            case R.id.survey_shijue_submit_btn:

                Map map = new HashMap();

                //TODO  6个字段
                map.put("AssessID",AssessID);
                map.put("Item1", item1);
                map.put("DiagnosisTime", mShijueZhenduanShijian.getText().toString());
                map.put("HospitalName", mShijueYiyuanNameEt.getText().toString());
                map.put("DiagnosisResults", mShijueZhenduanJieguoEt.getText().toString());
                map.put("IsHaveHospitalDiagnosis", isHaveZhenduan);


                Log.v("qqq", "检验一下Map的值" + map.toString()); //值唯有问题

                JSONObject jsonObject=JSONObject.fromObject(map);
                addNewApplicant(jsonObject.toString());
                break;

            case R.id.back_igv:
                finish();
                break;

        }
    }

    /**
     * 更新日期显示(记得month要+1，因为DatePicker索引是0-11)
     */
    private void updateDateDisplay() {
        mShijueZhenduanShijian.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth + 1).append("-").append(mDay));
    }


    void addNewApplicant(String updatePadlJson) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = updatePadl.getResult();
                    if (UiUtil.isResultSuccess(SurveyShijueActivity.this, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            array = jsonObject.getJSONObject("Data");
                            //Toast.makeText(SurveyShijueActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

                            Intent intentTow = new Intent(SurveyShijueActivity.this, SurveyBeijingActivity.class);
                            sp.edit().putInt("Item14", level).commit();
                            sp.edit().putInt("Item14Score", allscore).commit();


                            startActivity(intentTow);
                            finish();
                        } else {
                            Toast.makeText(SurveyShijueActivity.this, "保存失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        };

        updatePadl = new CommunityAssessThread(ApiConstant.UPDATEPVISUAL, handler, this);
        updatePadl.setPvisual(updatePadlJson);
        updatePadl.start();


    }


}
