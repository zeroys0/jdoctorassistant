package com.jxj.jdoctorassistant.main.community.activity.pinggu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.CommunityAssessThread;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*就是 对评估结果  的确认，是评估界面的 下一个界面*/
public class QuerenPinguActivity extends Activity {

    @Bind(R.id.back_igv)
    ImageView mBackIgv;
    @Bind(R.id.title_tv)
    TextView mTitleTv;
    @Bind(R.id.queren_zongfen)
    TextView mQuerenZongfen;
    @Bind(R.id.level_iv)
    ImageView mLevelIv;

    @Bind(R.id.queren_ziji_fenshu)
    TextView mQuerenZijiFenshu;
    @Bind(R.id.queren_ziji_level_tv)
    TextView mQuerenZijiLevelTv;

    @Bind(R.id.queren_renzhi_fenshu)
    TextView mQuerenRenzhiFenshu;
    @Bind(R.id.queren_renzhi_level_tv)
    TextView mQuerenRenzhiLevelTv;

    @Bind(R.id.queren_qingxu_fenshu)
    TextView mQuerenQingxuFenshu;
    @Bind(R.id.queren_qingxu_level_tv)
    TextView mQuerenQingxuLevelTv;

    @Bind(R.id.queren_shijue_fenshu)
    TextView mQuerenShijueFenshu;
    @Bind(R.id.queren_shijue_level_tv)
    TextView mQuerenShijueLevelTv;

    @Bind(R.id.queren_zongfen_submit)
    RelativeLayout mQuerenZongfenSubmit;

    String AssessID;
    int Item11, Item12, Item13, Item14;
    int Item11Score, Item12Score, Item13Score, Item14Score;

    String Conclude;
    int allscore;
    int level;
    @Bind(R.id.queren_miaoshu_et)
    EditText mQuerenMiaoshuEt;
    @Bind(R.id.queren_fuwu_advice_et)
    EditText mQuerenFuwuAdviceEt;
    @Bind(R.id.show_score_detail)
    ImageView mShowScoreDetail;
    @Bind(R.id.close_igv)
    ImageView mCloseIgv;
    @Bind(R.id.show_score_compare_rl)
    RelativeLayout mShowScoreCompareRl;
    private SharedPreferences sp;

    @Bind(R.id.queren_riqi_tv)
    TextView mQuerenRiqiTv;
    private CommunityAssessThread updatePReport;

    private JSONObject array;

    private int uId;


    // 记录当前的时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;
    /*
    UpdatePReport
    有16 个数据 9个数据
     *
     *  1,id 传过来的
     * 5,item 4个传过来的
     *
     *
     * 6,等级算出来的
     * 7描述
     * 8,服务建议
     *  9,评估员编号
     *
     *  */
    int intentSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queren_pingu);
        ButterKnife.bind(this);

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        updateDateDisplay();

        mTitleTv.setText("评估报表");

        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        intentSize = data.getInt("intentSize");
        /*展示用 0 编辑用1*/
        if (intentSize == 0) {// 是用来展示的
            array = JSONObject.fromObject(data.getString("array"));
            JSONObject user = array.getJSONObject("Report");

            Log.v("querenpinggu",user.toString());

            // 得到的是 评估等级 不是评估分数
            Item11 = user.getInt("Item11");
            Item12 = user.getInt("Item12");
            Item13 = user.getInt("Item13");
            Item14 = user.getInt("Item14");




            //level =user.getInt("Conclude");
            mQuerenMiaoshuEt.setInputType(InputType.TYPE_NULL);
            mQuerenFuwuAdviceEt.setInputType(InputType.TYPE_NULL);

            String  item21 =user.getString("Item21");
            if (item21.equals("null")){
                mQuerenMiaoshuEt.setText("暂无");
            }else {
                mQuerenMiaoshuEt.setText(user.getString("Item21"));

            }



            String  item22 =user.getString("Item21");
            if (item22.equals("null")){
                mQuerenFuwuAdviceEt.setText("暂无");
            }else {
                mQuerenFuwuAdviceEt.setText(user.getString("Item22"));
            }





            mQuerenZongfenSubmit.setVisibility(View.GONE);





        } else if (intentSize == 1) {// 是用来编辑 提交的

            sp = getSharedPreferences(AppConstant.USER_sp_name, this.MODE_PRIVATE);
            uId = sp.getInt(AppConstant.ADMIN_userId, 0);
            AssessID = sp.getString("AssessID", "");
            Item11 = sp.getInt("Item11", 0);
            Item12 = sp.getInt("Item12", 0);
            Item13 = sp.getInt("Item13", 0);
            Item14 = sp.getInt("Item14", 0);

            mQuerenMiaoshuEt.setHint("点击输入项目描述");
            mQuerenFuwuAdviceEt.setHint("点击输入服务建议");

        }



        int[] i1={0,0,6,18,30};
        Item11Score=i1[Item11];

        int[] i2={0,0,3,9,15};
        Item12Score=i2[Item12];



        int[] i3={0,0,1,3,5};
        Item13Score=i3[Item13];


        int[] i4={0,0,-1,6,50};
        Item14Score=i4[Item14];
        allscore = Item11Score + Item12Score + Item13Score + Item14Score;
        mQuerenZongfen.setText(allscore + "分");
        if (allscore >= 0 && allscore <= 5) {
            level = 1;
            mLevelIv.setImageResource(R.drawable.level_one);
        } else if (allscore >= 6 && allscore <= 17) {
            level = 2;
            mLevelIv.setImageResource(R.drawable.level_two);
        } else if (allscore >= 18 && allscore <= 30) {
            level = 3;
            mLevelIv.setImageResource(R.drawable.level_three);
        } else if (allscore >= 30) {
            level = 4;
            mLevelIv.setImageResource(R.drawable.level_four);
        }

        //生活自理能力等级

        mQuerenZijiFenshu.setText(Item11Score + " ");

        if (Item11 == 1) {
            mQuerenZijiLevelTv.setText("正常");
        } else if (Item11 == 2) {

            mQuerenZijiLevelTv.setText("轻度");
        } else if (Item11 == 3) {

            mQuerenZijiLevelTv.setText("中度");
        } else if (Item11 == 4) {

            mQuerenZijiLevelTv.setText("重度");
        }

        //认知能力等级
        mQuerenRenzhiFenshu.setText(Item12Score + " ");
        if (Item12 == 1) {
            mQuerenRenzhiLevelTv.setText("正常");
        } else if (Item12 == 2) {
            mQuerenRenzhiLevelTv.setText("轻度");
        } else if (Item12 == 3) {
            mQuerenRenzhiLevelTv.setText("中度");
        } else if (Item12 == 4) {
            mQuerenRenzhiLevelTv.setText("重度");
        }

        //情绪能力等级
        mQuerenQingxuFenshu.setText(Item13Score + " ");
        if (Item13 == 1) {
            mQuerenQingxuLevelTv.setText("正常");
        } else if (Item13 == 2) {
            mQuerenQingxuLevelTv.setText("轻度");
        } else if (Item13 == 3) {
            mQuerenQingxuLevelTv.setText("中度");
        } else if (Item13 == 4) {
            mQuerenQingxuLevelTv.setText("重度");
        }


        //视觉能力等级
        mQuerenShijueFenshu.setText(Item14Score + " ");
        if (Item14 == 1) {
            mQuerenShijueLevelTv.setText("正常");
        } else if (Item14 == 3) {
            mQuerenShijueLevelTv.setText("中度");
        } else if (Item14 == 4) {
            mQuerenShijueLevelTv.setText("重度");
        }


    }

    /**
     * 更新日期显示(记得month要+1，因为DatePicker索引是0-11)
     */
    private void updateDateDisplay() {
        mQuerenRiqiTv.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth + 1).append("-").append(mDay));
    }

    @OnClick({R.id.back_igv, R.id.queren_zongfen_submit, R.id.show_score_detail, R.id.queren_riqi_tv,R.id.close_igv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;

            case R.id.queren_riqi_tv:
                new DatePickerDialog(QuerenPinguActivity.this,
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

            case R.id.show_score_detail:

                if(mShowScoreCompareRl.getVisibility()==View.GONE){
                    mShowScoreCompareRl.setVisibility(View.VISIBLE);
                }


                break;

            case R.id.close_igv:

                if(mShowScoreCompareRl.getVisibility()==View.VISIBLE){
                    mShowScoreCompareRl.setVisibility(View.GONE);
                }else {

                }

                break;


            case R.id.queren_zongfen_submit:
                //h回到 主页  把之前的界面 销毁

                Map map = new HashMap();
                //TODO  10个字段
                map.put("AssessID", AssessID);
                map.put("Item11", Item11);
                map.put("Item12", Item12);
                map.put("Item13", Item13);
                map.put("Item14", Item14);


                map.put("Conclude", level);
                map.put("Item21", mQuerenMiaoshuEt.getText().toString());
                map.put("Item22", mQuerenFuwuAdviceEt.getText().toString());
                map.put("Uid1", uId);

                JSONObject jsonObject = JSONObject.fromObject(map);
                addNewApplicant(jsonObject.toString());


                break;


        }


    }

    String isNull(String str) {
        if (str != null && !str.equals("null")) {
            return " ";
        } else {
            return str;
        }
    }


    void addNewApplicant(String updatePadlJson) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = updatePReport.getResult();
                    if (UiUtil.isResultSuccess(QuerenPinguActivity.this, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            array = jsonObject.getJSONObject("Data");
                            Toast.makeText(QuerenPinguActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(QuerenPinguActivity.this, "保存失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        };

        updatePReport = new CommunityAssessThread(ApiConstant.UPDATEPREPORT, handler, this);
        updatePReport.setReport(updatePadlJson);
        updatePReport.start();


    }



}
