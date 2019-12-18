package com.jxj.jdoctorassistant.main.community.activity.personal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.CommunityAssessThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.TimeDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import net.sf.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAppointmentActivity extends Activity {

    @Bind(R.id.back_igv)
    ImageView mBackIgv;
    @Bind(R.id.title_tv)
    TextView mTitleTv;
    @Bind(R.id.right_btn_tv)
    TextView mRightBtnTv;
    @Bind(R.id.add_order_name_tv)
    TextView mAddOrderNameTv;
    @Bind(R.id.add_order_name_rl)
    RelativeLayout mAddOrderNameRl;
    @Bind(R.id.add_order_idcard_num_tv)
    TextView mAddOrderIdcardNumTv;
    @Bind(R.id.add_order_phone_num_tv)
    TextView mAddOrderPhoneNumTv;
    @Bind(R.id.add_order_address_tv)
    TextView mAddOrderAddressTv;
    @Bind(R.id.add_order_date_time_tv)
    TextView mAddOrderDateTimeTv;
    @Bind(R.id.add_order_date_time_rl)
    RelativeLayout mAddOrderDateTimeRl;

    @Bind(R.id.add_order_date_date_tv)
    TextView mAddOrderDateDateTv;
    @Bind(R.id.add_order_date_date_rl)
    RelativeLayout mAddOrderDateDateRl;
    int from;
    JSONObject jsonObject;


    private int uId;
    private int cid = 0;

    //String addDateTime;


    private SharedPreferences sp;

    Context context;
    private CommunityAssessThread addOrderThread,editOrderThread;

    public final static int REQUESTCODE = 115;
    // 记录当前的时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;

    private int pinguID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);
        ButterKnife.bind(this);

        context = this;
        sp = getSharedPreferences(AppConstant.USER_sp_name, context.MODE_PRIVATE);
        uId = sp.getInt(AppConstant.ADMIN_userId, 0);

        mRightBtnTv.setText("保存");
        mRightBtnTv.setVisibility(View.VISIBLE);
        //  如果从 首页过来  显示 新增 ，从 预约界面 过来  显示 编辑

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        //有 传递 过来的值进行填充
        final Intent intent = getIntent();
        from = intent.getIntExtra("intentSize", 0);
        jsonObject = JSONObject.fromObject(intent.getStringExtra("array"));


        if (from == 0) {
            mTitleTv.setText("新增预约评估");

        } else if (from == 1) {
            mTitleTv.setText("编辑预约评估");


            cid = jsonObject.getInt("Cid");
            pinguID =jsonObject.getInt("Id");

            mAddOrderNameTv.setText(jsonObject.getString("CName"));

            String idCard = jsonObject.getString("CardId");
            if (idCard.isEmpty()){
                mAddOrderIdcardNumTv.setText("身份证号：暂无" );
            }else {
                mAddOrderIdcardNumTv.setText("身份证号：" + jsonObject.getString("CardId"));
            }


            String phoneNum =jsonObject.getString("MobilePhone");
            if (phoneNum.isEmpty()){
                mAddOrderPhoneNumTv.setText("联系方式：暂无");
            }else {
                mAddOrderPhoneNumTv.setText("联系方式：" + jsonObject.getString("MobilePhone"));
            }





           // mAddOrderPhoneNumTv.setText("联系方式：" + jsonObject.getString("MobilePhone"));
           /* JSONObject ResudentialAddress = jsonObject.getJSONObject("ResudentialAddress");*/

            String address =jsonObject.getString("Address");
            if (address.isEmpty()|| address.equals("////")){
                mAddOrderAddressTv.setText("联系地址：暂无");
            }else {


                String  tempaddress=address.replace("/"," ");
                mAddOrderAddressTv.setText("联系地址：" + tempaddress);
            }


            //mAddOrderAddressTv.setText("联系地址：" + jsonObject.getString("Address"));

            String AppDate =jsonObject.getString("AppDate");
            String[] dateTiem = AppDate.split(" ");
            mAddOrderDateTimeTv.setText(dateTiem[1]);
            mAddOrderDateDateTv.setText(dateTiem[0]);



        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            switch (resultCode) {
                case ChooseApplicantActivity.RESULTCODE:
                    Bundle bundle = data.getExtras(); // 填充 姓名  身份证号 手机号  地址
                    JSONObject jsonObject = JSONObject.fromObject(bundle.getString(ChooseApplicantActivity.GETAPPLICANTINFO));
                    cid = jsonObject.getInt("Id");
                    mAddOrderNameTv.setText(jsonObject.getString("Name"));


                    String idCard = jsonObject.getString("CardId");
                    if (idCard.isEmpty()){
                        mAddOrderIdcardNumTv.setText("身份证号：暂无" );
                    }else {
                        mAddOrderIdcardNumTv.setText("身份证号：" + jsonObject.getString("CardId"));
                    }


                    String phoneNum =jsonObject.getString("MobilePhone");
                    if (phoneNum.isEmpty()){
                        mAddOrderPhoneNumTv.setText("联系方式：暂无");
                    }else {


                        mAddOrderPhoneNumTv.setText("联系方式：" + jsonObject.getString("MobilePhone"));
                    }


                    JSONObject ResudentialAddress = jsonObject.getJSONObject("ResudentialAddress");
                    String address =ResudentialAddress.getString("PAddressStr");
                    if (address.isEmpty()|| address.equals("/////")){
                        mAddOrderAddressTv.setText("联系地址：暂无");
                    }else {

                        String  tempaddress=address.replace("/"," ");
                        mAddOrderAddressTv.setText("联系地址：" + tempaddress);
                    }



                  /*  mAddOrderIdcardNumTv.setText("身份证号：" + jsonObject.getString("CardId"));
                    mAddOrderPhoneNumTv.setText("联系方式：" + jsonObject.getString("MobilePhone"));

                    mAddOrderAddressTv.setText("联系地址：" + ResudentialAddress.getString("PAddressStr"));*/
                    break;

            }
        }


    }


    @OnClick({R.id.back_igv, R.id.right_btn_tv, R.id.add_order_name_rl, R.id.add_order_date_time_rl, R.id.add_order_date_date_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;
            case R.id.right_btn_tv:

                //Toast.makeText(context, "点击了保存", Toast.LENGTH_SHORT).show();
                String time = mAddOrderDateTimeTv.getText().toString();
                String date = mAddOrderDateDateTv.getText().toString();


                if (cid == 0 || time == null || date == null) {
                    Toast.makeText(context, "请选择申请人与 预约时间 日期", Toast.LENGTH_SHORT).show();


                    return;
                } else {
                    String addNewTime = date +" " +time;

                    int i = compare_date(addNewTime, GetDate.currentFullTime());
                    if (i == -1) {
                        Toast.makeText(context, "请选择一个 大于当前日期的时间", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (from == 0) {
                        //新增预约的接口
                        addOrderThread(uId, cid, addNewTime);

                    } else if (from == 1) {
                        editOrderThread(pinguID, addNewTime);
                    }



                }

                break;
            case R.id.add_order_name_rl: //跳转至 申请人列表 获取申请人
                // 新增 可以调整  编辑 不可跳转

                if (from == 0) {
                    Intent intent = new Intent(context, ChooseApplicantActivity.class);
               /* intent.putExtra("array", array.get(position).toString());
                Log.v("qqq", array.get(position).toString());*/
                    startActivityForResult(intent, REQUESTCODE);

                } else if (from == 1) {
                    Toast.makeText(context, "编辑预约评估不可更换申请人", Toast.LENGTH_SHORT).show();


                }

                break;
            case R.id.add_order_date_time_rl:
                //弹框 选择 时间
                TimeDialog dialog = new TimeDialog(context);
                dialog.setTime(0, mAddOrderDateTimeTv);

                break;

            case R.id.add_order_date_date_rl:
                //弹框 选择 日期  弹窗后的初始日期 设置为文本框内容
                new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mYear = year;
                                mMonth = month;
                                mDay = dayOfMonth;
                                //更新Button上显示的日期
                                mAddOrderDateDateTv.setText(new StringBuilder().append(mYear).append("-")
                                        .append(mMonth + 1).append("-").append(mDay));

                            }
                        }, mYear, mMonth, mDay).show();


                break;


        }
    }

    public static int compare_date(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");//填的日期 比 当前日期 大
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");//填的日期 比 当前日期 小
                return -1;
            } else {
                return 0;//填的日期 比 当前日期  一样
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }


    void addOrderThread(int uid, int cid, String dateTime) {//这种调用 方式 为默认
        // 然后又一个上拉加载改变
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = addOrderThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            Toast.makeText(context, "新增预约评估成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(context, "新增预约评估失败", Toast.LENGTH_SHORT).show();


                        }
                    } else {

                        Toast.makeText(context, "新增预约评估失败" + result, Toast.LENGTH_SHORT).show();

                    }
                }
            }
        };
        addOrderThread = new CommunityAssessThread(ApiConstant.ADDAPPOINTMENT, handler, context);
        addOrderThread.setUid(uid);// 查询 关键字
        addOrderThread.setCid(cid);//  查询范围 起始时间
        addOrderThread.setAddDate(dateTime);//   当前页 页码
        addOrderThread.start();
    }


    void editOrderThread(int pingguId, String dateTime) {//这种调用 方式 为默认
        // 然后又一个上拉加载改变
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = editOrderThread.getResult();
                    if (UiUtil.isResultSuccess(context, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            Toast.makeText(context, "编辑预约评估成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(context, "编辑预约评估失败", Toast.LENGTH_SHORT).show();


                        }
                    } else {

                        Toast.makeText(context, "新增预约评估失败" , Toast.LENGTH_SHORT).show();

                    }
                }
            }
        };
        editOrderThread = new CommunityAssessThread(ApiConstant.EDITAPPOINTMENT, handler, context);
        editOrderThread.setPingguId(pingguId);// 查询 关键字
        editOrderThread.setAddDate(dateTime);//   当前页 页码
        editOrderThread.start();
    }

}
