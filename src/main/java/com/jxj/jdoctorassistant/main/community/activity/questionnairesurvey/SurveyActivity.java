package com.jxj.jdoctorassistant.main.community.activity.questionnairesurvey;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*问卷调查 是这个APP的重中之重  除了展示 就是 评估了（即问卷调查）
*       TODO
*       先把这个界面 模拟出来  可以往下跳转
*       一步一步往下走
*
*
* */
public class SurveyActivity extends Activity {

    @Bind(R.id.back_igv)
    ImageView mBackIgv;
    @Bind(R.id.title_tv)
    TextView mTitleTv;
    @Bind(R.id.survey_submit_btn)
    RelativeLayout mSurveySubmitBtn;

    @Bind(R.id.right_btn_igv)
    ImageView mRightBtnIgv;
    @Bind(R.id.right_btn_tv)
    TextView mRightBtnTv;


    @Bind(R.id.second_answer_a_rb)
    RadioButton mSecondAnswerARb;
    @Bind(R.id.second_answer_b_rb)
    RadioButton mSecondAnswerBRb;
    @Bind(R.id.second_answer_c_rb)
    RadioButton mSecondAnswerCRb;
    @Bind(R.id.second_answer_rg)
    RadioGroup mSecondAnswerRg;

    @Bind(R.id.third_answer_a_rb)
    RadioButton mThirdAnswerARb;
    @Bind(R.id.third_answer_b_rb)
    RadioButton mThirdAnswerBRb;
    @Bind(R.id.third_answer_rg)
    RadioGroup mThirdAnswerRg;

    @Bind(R.id.fourth_answer_a_rb)
    RadioButton mFourthAnswerARb;
    @Bind(R.id.fourth_answer_b_rb)
    RadioButton mFourthAnswerBRb;
    @Bind(R.id.fourth_answer_c_rb)
    RadioButton mFourthAnswerCRb;
    @Bind(R.id.fourth_answer_rg)
    RadioGroup mFourthAnswerRg;

    @Bind(R.id.fifth_answer_a_rb)
    RadioButton mFifthAnswerARb;
    @Bind(R.id.fifth_answer_b_rb)
    RadioButton mFifthAnswerBRb;
    @Bind(R.id.fifth_answer_c_rb)
    RadioButton mFifthAnswerCRb;
    @Bind(R.id.fifth_answer_rg)
    RadioGroup mFifthAnswerRg;

    @Bind(R.id.sixth_answer_a_rb)
    RadioButton mSixthAnswerARg;
    @Bind(R.id.sixth_answer_b_rb)
    RadioButton mSixthAnswerBRg;
    @Bind(R.id.sixth_answer_c_rb)
    RadioButton mSixthAnswerCRg;
    @Bind(R.id.sixth_answer_d_rb)
    RadioButton mSixthAnswerDRg;
    @Bind(R.id.sixth_answer_rg)
    RadioGroup mSixthAnswerRg;

    @Bind(R.id.seventh_answer_a_rb)
    RadioButton mSeventhAnswerARb;
    @Bind(R.id.seventh_answer_b_rb)
    RadioButton mSeventhAnswerBRb;
    @Bind(R.id.seventh_answer_c_rb)
    RadioButton mSeventhAnswerCRb;
    @Bind(R.id.seventh_answer_d_rb)
    RadioButton mSeventhAnswerDRb;
    @Bind(R.id.seventh_answer_rg)
    RadioGroup mSeventhAnswerRg;

    @Bind(R.id.eighth_answer_a_rb)
    RadioButton mEighthAnswerARb;
    @Bind(R.id.eighth_answer_b_rb)
    RadioButton mEighthAnswerBRb;
    @Bind(R.id.eighth_answer_c_rb)
    RadioButton mEighthAnswerCRb;
    @Bind(R.id.eighth_answer_rg)
    RadioGroup mEighthAnswerRg;

    @Bind(R.id.ninth_answer_rg)
    RadioGroup mNinethAnswerRg;
    @Bind(R.id.ninth_answer_a_rb)
    RadioButton mNinthAnswerARb;
    @Bind(R.id.ninth_answer_b_rb)
    RadioButton mNinthAnswerBRb;
    @Bind(R.id.ninth_answer_c_rb)
    RadioButton mNinthAnswerCRb;

    @Bind(R.id.tenth_answer_a_rb)
    RadioButton mTenthAnswerARb;
    @Bind(R.id.tenth_answer_b_rb)
    RadioButton mTenthAnswerBRb;
    @Bind(R.id.tenth_answer_rg)
    RadioGroup mTenthAnswerRg;

    @Bind(R.id.all_score_tv)
    TextView mAllScoreTv;
    @Bind(R.id.zilinengli_jielun_tv)
    TextView mZilinengliJielunTv;

    int item1, item2, item3, item4, item5, item6, item7, item8, item9, item10;
    int score1, score2, score3, score4, score5, score6, score7, score8, score9, score10;
    int level =0;
    int allscore =0;
    @Bind(R.id.first_answer_a_rb)
    RadioButton mFirstAnswerARb;
    @Bind(R.id.first_answer_b_rb)
    RadioButton mFirstAnswerBRb;
    @Bind(R.id.first_answer_c_rb)
    RadioButton mFirstAnswerCRb;
    @Bind(R.id.first_answer_rg)
    RadioGroup mFirstAnswerRg;

    private CommunityAssessThread updatePadl;


    private JSONObject array;

    String AssessID;
    private SharedPreferences sp;
    /*  需求 在 获取上一个界面 得到的AssId  界面 填充 完数据
        看接口文档 需要多少个参数
        提交




    提交       */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ButterKnife.bind(this);
        mTitleTv.setText("评估");


        sp = getSharedPreferences(AppConstant.USER_sp_name, this.MODE_PRIVATE);
        AssessID=sp.getString("AssessID", "");

        //10个问题
        mFirstAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {

                if (mFirstAnswerARb.getId() == checkedId) {
                    score1 = 0;
                    item1 = 0;
                    panduanLevel();

                } else if (mFirstAnswerBRb.getId() == checkedId) {
                    score1 = 5;
                    item1 = 1;
                    panduanLevel();

                } else if (mFirstAnswerCRb.getId() == checkedId) {
                    score1 = 10;
                    item1 = 2;
                    panduanLevel();

                }

            }
        });


        mSecondAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mSecondAnswerARb.getId() == checkedId) {
                    score2 = 0;
                    item2 = 0;
                    panduanLevel();
                } else if (mSecondAnswerBRb.getId() == checkedId) {
                    score2 = 5;
                    item2 = 1;
                    panduanLevel();
                } else if (mSecondAnswerCRb.getId() == checkedId) {
                    score2 = 10;
                    item2 = 2;
                    panduanLevel();
                }

            }
        });


        mThirdAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mThirdAnswerARb.getId() == checkedId) {
                    score3 = 0;
                    item3 = 0;
                    panduanLevel();
                } else if (mThirdAnswerBRb.getId() == checkedId) {
                    score3 = 5;
                    item3 = 1;
                    panduanLevel();
                }

            }
        });

        mFourthAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mFourthAnswerARb.getId() == checkedId) {
                    score4 = 0;
                    item4 = 0;
                    panduanLevel();
                } else if (mFourthAnswerBRb.getId() == checkedId) {
                    score4 = 5;
                    item4 = 1;
                    panduanLevel();
                } else if (mFourthAnswerCRb.getId() == checkedId) {
                    score4 = 10;
                    item4 = 2;
                    panduanLevel();
                }


            }
        });


        mFifthAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mFifthAnswerARb.getId() == checkedId) {
                    score5 = 0;
                    item5 = 0;
                    panduanLevel();
                } else if (mFifthAnswerBRb.getId() == checkedId) {
                    score5 = 5;
                    item5 = 1;
                    panduanLevel();
                } else if (mFifthAnswerCRb.getId() == checkedId) {
                    score5 = 10;
                    item5 = 2;
                    panduanLevel();
                }

            }
        });

        mSixthAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mSixthAnswerARg.getId() == checkedId) {
                    score6 = 0;
                    item6 = 0;
                    panduanLevel();
                } else if (mSixthAnswerBRg.getId() == checkedId) {
                    score6 = 5;
                    item6 = 1;
                    panduanLevel();
                } else if (mSixthAnswerCRg.getId() == checkedId) {
                    score6 = 10;
                    item6 = 2;
                    panduanLevel();
                } else if (mSixthAnswerDRg.getId() == checkedId) {
                    score6 = 15;
                    item6 = 3;
                    panduanLevel();
                }

            }
        });


        mSeventhAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mSeventhAnswerARb.getId() == checkedId) {
                    score7 = 0;
                    item7 = 0;
                    panduanLevel();
                } else if (mSeventhAnswerBRb.getId() == checkedId) {
                    score7 = 5;
                    item7 = 1;
                    panduanLevel();
                } else if (mSeventhAnswerCRb.getId() == checkedId) {
                    score7 = 10;
                    item7 = 2;
                    panduanLevel();
                } else if (mSeventhAnswerDRb.getId() == checkedId) {
                    score7 = 15;
                    item7 = 3;
                    panduanLevel();
                }

            }
        });

        mEighthAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mEighthAnswerARb.getId() == checkedId) {
                    score8 = 0;
                    item8 = 0;
                    panduanLevel();
                } else if (mEighthAnswerBRb.getId() == checkedId) {
                    score8 = 5;
                    item8 = 1;
                    panduanLevel();
                } else if (mEighthAnswerCRb.getId() == checkedId) {
                    score8 = 10;
                    item8 = 2;
                    panduanLevel();
                }

            }
        });


        mNinethAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mNinthAnswerARb.getId() == checkedId) {
                    score9 = 0;
                    item9 = 0;
                    panduanLevel();
                } else if (mNinthAnswerBRb.getId() == checkedId) {
                    score9 = 5;
                    item9 = 1;
                    panduanLevel();
                } else if (mNinthAnswerCRb.getId() == checkedId) {
                    score9 = 10;
                    item9 = 2;
                    panduanLevel();
                }

            }
        });

        mTenthAnswerRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mTenthAnswerARb.getId() == checkedId) {
                    score10 = 0;
                    item10 = 0;
                    panduanLevel();
                } else if (mTenthAnswerBRb.getId() == checkedId) {
                    score10 = 5;
                    item10 = 1;
                    panduanLevel();
                }

            }
        });


    }

    private void panduanLevel() {
        allscore = score1 + score2 + score3 + score4 + score5 + score6 + score7 + score8 + score9 + score10;
        mAllScoreTv.setText(allscore+"");
        if (allscore >= 0 && allscore <= 45) {
            mZilinengliJielunTv.setText("重度功能缺陷");
            level = 4;
        } else if (allscore >= 50 && allscore <= 70) {
            mZilinengliJielunTv.setText("中度功能缺陷");
            level = 3;
        } else if (allscore >= 75 && allscore <= 95) {
            mZilinengliJielunTv.setText("轻度功能缺陷");
            level = 2;
        } else if (allscore == 100) {
            mZilinengliJielunTv.setText("自理");
            level = 1;
        }


    }

    void addNewApplicant(String updatePadlJson) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = updatePadl.getResult();
                    if (UiUtil.isResultSuccess(SurveyActivity.this, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            array = jsonObject.getJSONObject("Data");
                            //Toast.makeText(SurveyActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

                            Intent intentTow = new Intent(SurveyActivity.this, SurveyRenzhiActivity.class);

                            sp.edit().putInt("Item11", level).commit();
                            sp.edit().putInt("Item11Score", allscore).commit();


                            //level
                            startActivity(intentTow);
                            finish();

                        } else {
                            Toast.makeText(SurveyActivity.this, "保存失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        };

        updatePadl = new CommunityAssessThread(ApiConstant.UPDATEPADL, handler, this);
        updatePadl.setPadl(updatePadlJson);
        updatePadl.start();


    }

    @OnClick({R.id.back_igv, R.id.survey_submit_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;
            case R.id.survey_submit_btn:
                Map map = new HashMap();

                //TODO  12个参数
                map.put("AssessID", AssessID);
                map.put("Item1", item1);
                map.put("Item2", item2);
                map.put("Item3", item3);
                map.put("Item4", item4);
                map.put("Item5", item5);
                map.put("Item6", item6);
                map.put("Item7", item7);
                map.put("Item8", item8);
                map.put("Item9", item9);
                map.put("Item10", item10);
                map.put("Conclude", level);
                JSONObject jsonObject = JSONObject.fromObject(map);

                Log.v("qqq",jsonObject.toString()+"zijinnengli ");

                addNewApplicant(jsonObject.toString());


                break;

        }
    }


}
