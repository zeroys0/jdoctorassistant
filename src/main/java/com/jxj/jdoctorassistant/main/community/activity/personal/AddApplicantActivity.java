package com.jxj.jdoctorassistant.main.community.activity.personal;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.community.ChooseCommunityListAdapterYY;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.thread.CommunityAssessThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONArray;
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

public class AddApplicantActivity extends Activity {

    @Bind(R.id.back_igv)
    ImageView mBackIgv;
    @Bind(R.id.title_tv)
    TextView mTitleTv;
    @Bind(R.id.right_btn_tv)
    TextView mRightBtnTv;

    /*22个数据 分为3个模块 	1是 申请人  11个 	2是 联系信息 5个 	3是 代理人 6个*/
    /*地址*/
    @Bind(R.id.add_new_address_xian)
    TextView mAddNewAddressXian;
    @Bind(R.id.add_new_address_xiangzhen)
    TextView mAddNewAddressXiangzhen;
    @Bind(R.id.add_new_address_shequ)
    TextView mAddNewAddressShequ;

    /*7个可获得 */
    @Bind(R.id.add_new_name_et)
    EditText mAddNewNameEt;//姓名
    @Bind(R.id.add_new_idcard_num_et)
    EditText mAddNewIdcardNumEt;//身份证号
    @Bind(R.id.add_new_shebao_type)
    EditText mAddNewShebaoType;//社保类型
    @Bind(R.id.add_new_shebao_num_tv)
    TextView mAddNewShebaoNumTv;//社区编号
    @Bind(R.id.add_new_nation_et)
    EditText mAddNewNationEt;//民族
    @Bind(R.id.add_new_work)
    EditText mAddNewWork;//从事职业
    @Bind(R.id.add_new_pnative_place)
    EditText mAddNewPnativePlace;//籍贯


    //婚姻
    @Bind(R.id.add_new_pbirthday)
    TextView mAddNewPbirthday;//出生日期

    @Bind(R.id.add_new_sex_rg)
    RadioGroup mAddNewSexRg;
    @Bind(R.id.add_new_sex_man)
    RadioButton mAddNewSexMan;//男女
    @Bind(R.id.add_new_sex_woman)
    RadioButton mAddNewSexWoman;

    //联系信息
    @Bind(R.id.add_new_huji_address)
    TextView mAddNewHujiAddress;
    @Bind(R.id.add_new_juzhu_address)
    TextView mAddNewJuzhuAddress;
    @Bind(R.id.add_new_youbian)
    EditText mAddNewYoubian;
    @Bind(R.id.add_new_home_phone_num)
    EditText mAddNewHomePhoneNum;
    @Bind(R.id.add_new_telphone_num)
    EditText mAddNewTelphoneNum;

    //代理人
    @Bind(R.id.add_new_dailiren)
    EditText mAddNewDailiren;
    @Bind(R.id.add_new_dailiren_guanxi)
    EditText mAddNewDailirenGuanxi;
    @Bind(R.id.add_new_dailiren_address)
    EditText mAddNewDailirenAddress;
    @Bind(R.id.add_new_dailiren_youbian)
    EditText mAddNewDailirenYoubian;
    @Bind(R.id.add_new_dailiren_home_phone)
    EditText mAddNewDailirenHomePhone;
    @Bind(R.id.add_new_dailiren_telphone_num)
    EditText mAddNewDailirenTelphoneNum;

    @Bind(R.id.add_new_pmarita)
    RadioGroup mAddNewPmarita;
    @Bind(R.id.pmarita_weihun)
    RadioButton mPmaritaWeihun;
    @Bind(R.id.pmarita_yihun)
    RadioButton mPmaritaYihun;
    @Bind(R.id.pmarita_sanou)
    RadioButton mPmaritaSanou;
    @Bind(R.id.pmarita_lihun)
    RadioButton mPmaritaLihun;

    @Bind(R.id.add_new_edu_rg)
    RadioGroup mAddNewEduRg;
    @Bind(R.id.edu_chuzhong)
    RadioButton mEduChuzhong;
    @Bind(R.id.edu_gaozhong)
    RadioButton mEduGaozhong;
    @Bind(R.id.edu_daxue)
    RadioButton mEduDaxue;
    @Bind(R.id.edu_benkeyishang)
    RadioButton mEduBenkeyishang;


    @Bind(R.id.add_new_live_jijizhuangkuan_tuixiujing_rb)
    RadioButton mAddNewLiveJijizhuangkuanTuixiujingRb;
    @Bind(R.id.add_new_live_jijizhuangkuan_zinvbutie_rb)
    RadioButton mAddNewLiveJijizhuangkuanZinvbutieRb;
    @Bind(R.id.add_new_live_jijizhuangkuan_qinyouzizhu_rb)
    RadioButton mAddNewLiveJijizhuangkuanQinyouzizhuRb;
    @Bind(R.id.add_new_live_jijizhuangkuan_qitabutie_rb)
    RadioButton mAddNewLiveJijizhuangkuanQitabutieRb;
    @Bind(R.id.add_new_live_jijizhuangkuan_rg)
    RadioGroup mAddNewLiveJijizhuangkuanRg;


    @Bind(R.id.add_new_live_juzhuzhuangkuan_yuzinv_rb)
    RadioButton mAddNewLiveJuzhuzhuangkuanYuzinvRb;
    @Bind(R.id.add_new_live_juzhuzhuangkuan_yupeiou_rb)
    RadioButton mAddNewLiveJuzhuzhuangkuanYupeiouRb;
    @Bind(R.id.add_new_live_juzhuzhuangkuan_yuqinqi_rb)
    RadioButton mAddNewLiveJuzhuzhuangkuanYuqinqiRb;
    @Bind(R.id.add_new_live_juzhuzhuangkuan_duzishenghuo_rb)
    RadioButton mAddNewLiveJuzhuzhuangkuanDuzishenghuoRb;
    @Bind(R.id.add_new_live_juzhuzhuangkuan_rg)
    RadioGroup mAddNewLiveJuzhuzhuangkuanRg;


    @Bind(R.id.add_new_live_zhufangxingzhi_changquan_rb)
    RadioButton mAddNewLiveZhufangxingzhiChangquanRb;
    @Bind(R.id.add_new_live_zhufangxingzhi_zulin_rb)
    RadioButton mAddNewLiveZhufangxingzhiZulinRb;
    @Bind(R.id.add_new_live_zhufangxingzhi_liangzu_rb)
    RadioButton mAddNewLiveZhufangxingzhiLiangzuRb;
    @Bind(R.id.add_new_live_zhufangxingzhi_sifang_rb)
    RadioButton mAddNewLiveZhufangxingzhiSifangRb;
    @Bind(R.id.add_new_live_zhufangxingzhi_rg)
    RadioGroup mAddNewLiveZhufangxingzhiRg;


    @Bind(R.id.add_new_live_if_have_zhaogu_yes_rb)
    RadioButton mAddNewLiveIfHaveZhaoguYesRb;
    @Bind(R.id.add_new_live_if_have_zhaogu_no_rb)
    RadioButton mAddNewLiveIfHaveZhaoguNoRb;
    @Bind(R.id.add_new_live_if_have_zhaogu_rg)
    RadioGroup mAddNewLiveIfHaveZhaoguRg;


    @Bind(R.id.add_new_live_who_zhaogu_zinv_rb)
    RadioButton mAddNewLiveWhoZhaoguZinvRb;
    @Bind(R.id.add_new_live_who_zhaogu_peiou_rb)
    RadioButton mAddNewLiveWhoZhaoguPeiouRb;
    @Bind(R.id.add_new_live_who_zhaogu_qinyou_rb)
    RadioButton mAddNewLiveWhoZhaoguQinyouRb;
    @Bind(R.id.add_new_live_who_zhaogu_qita_rb)
    RadioButton mAddNewLiveWhoZhaoguQitaRb;
    @Bind(R.id.add_new_live_who_zhaogu_rg)
    RadioGroup mAddNewLiveWhoZhaoguRg;


    @Bind(R.id.add_new_live_jiuyifangshi_jiatingjiuyi_rb)
    RadioButton mAddNewLiveJiuyifangshiJiatingjiuyiRb;
    @Bind(R.id.add_new_live_jiuyifangshi_shequjiuyi_rb)
    RadioButton mAddNewLiveJiuyifangshiShequjiuyiRb;
    @Bind(R.id.add_new_live_jijizhuangkuan_waichujiuyi_rb)
    RadioButton mAddNewLiveJijizhuangkuanWaichujiuyiRb;
    @Bind(R.id.add_new_live_jiuyifangshi_rg)
    RadioGroup mAddNewLiveJiuyifangshiRg;


    @Bind(R.id.add_new_live_jiuyifangshi_yiyuan_name_et)
    EditText mAddNewLiveJiuyifangshiYiyuanNameEt;

    private String num1, num2;

    public final static int TYPE_02 = 0x16;
    private ChooseCommunityListAdapterYY communityListAdapter;


    private CommunityAssessThread addNewApplicantThread, getAddressNewApplicantThread;
    private JSONObject jsonObject;
    private JSONArray shequArray;
    private JSONObject array;
    int xiangzhengId = 0;
    int communityId = 0;

    int gender = 1;//性别
    int pmarita = 1;//婚姻
    int edu = 3;
    int Economic = 1;
    int Live = 1;
    int Housing = 1;
    int IsHelp = 1;
    int Helper = 1;
    int TreatmentMethod = 1;
    int onePrentId;

    int ifhaveCode;
    private boolean isClick;

    // 记录当前的时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private Calendar calendar;

    private int parentId_1, parentId_2;
    private boolean isParent_0, isParent_1;
    private Context context;

    private JSONObject censusRegister = new JSONObject();
    private JSONObject resudentialAddress = new JSONObject();

    public final static String RESADDRESS = "res_address";
    public final static String CESADDRESS = "ces_address";
    public final static int ADDRESSREQUEST_ces = 3; //户籍
    public final static int ADDRESSREQUEST_res = 4; //居住
    private String cesAddress, resAddress;


    private int id;

//    private boolean isCesAdd;
//    private boolean isResAdd;
////    public final static
//    public final static int ADD_ces=2;
//    public final static int ADD_res=3;
//    public final static int UPDATE=1;
//    public final static int UPDATE_res=12;
//    public final static int UPDATE_ces=13;
//    public int add;

//    private boolean isUpdate;
//    private boolean isCesUpdate;
//    private boolean isResUpdate;

    /*22个数据 分为3个模块 	1是 申请人  11个 	2是 联系信息 5个 	3是 代理人 6个*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_applicant);
        ButterKnife.bind(this);
        context = this;
        mRightBtnTv.setVisibility(View.VISIBLE);

        mRightBtnTv.setText("保存");
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        updateDateDisplay();

        communityListAdapter = new ChooseCommunityListAdapterYY(this);
        communityListAdapter.setType(TYPE_02);
        //有 传递 过来的值进行填充
        final Intent intent = getIntent();
        ifhaveCode = intent.getIntExtra("intentSize", 0);
        jsonObject = JSONObject.fromObject(intent.getStringExtra("array"));


        /*0 是首页过来的 ， 1 是从个人界面 过来的[就是]  */
        if (ifhaveCode == 1) {
            mTitleTv.setText("编辑");
            String arr = intent.getStringExtra("jsonObject");
            jsonObject = JSONObject.fromObject(arr);
            id = jsonObject.getInt("Id");
            setApplicantInfo();

        } else if (ifhaveCode == 0) {
            mTitleTv.setText("新增");
//            add=ADD;

        }


        mAddNewHujiAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setClass(context, PersonalAddressActivity.class);
                if (cesAddress != null && cesAddress.trim().length() > 2) {
                    intent1.putExtra("is_update", true);
                    intent1.putExtra(CESADDRESS, cesAddress);
                }

                startActivityForResult(intent1, ADDRESSREQUEST_ces);

            }
        });

        mAddNewJuzhuAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                intent1.setClass(context, PersonalAddressActivity.class);
                if (resAddress != null && resAddress.trim().length() > 2) {
                    intent1.putExtra("is_update", true);
                    intent1.putExtra(RESADDRESS, resAddress);
                }
                startActivityForResult(intent1, ADDRESSREQUEST_res);
            }
        });

        radioGroupListener();


    }

    private void radioGroupListener() {

        mAddNewLiveJijizhuangkuanRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mAddNewLiveJijizhuangkuanTuixiujingRb.getId() == checkedId) {
                    Economic = 1;

                } else if (mAddNewLiveJijizhuangkuanZinvbutieRb.getId() == checkedId) {
                    Economic = 2;
                } else if (mAddNewLiveJijizhuangkuanQinyouzizhuRb.getId() == checkedId) {
                    Economic = 3;
                } else if (mAddNewLiveJijizhuangkuanQitabutieRb.getId() == checkedId) {
                    Economic = 4;
                }
            }
        });

        mAddNewLiveJuzhuzhuangkuanRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mAddNewLiveJuzhuzhuangkuanYuzinvRb.getId() == checkedId) {
                    Live = 1;

                } else if (mAddNewLiveJuzhuzhuangkuanYupeiouRb.getId() == checkedId) {
                    Live = 2;
                } else if (mAddNewLiveJuzhuzhuangkuanYuqinqiRb.getId() == checkedId) {
                    Live = 3;
                } else if (mAddNewLiveJuzhuzhuangkuanDuzishenghuoRb.getId() == checkedId) {
                    Live = 4;
                }
            }
        });

        mAddNewLiveZhufangxingzhiRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mAddNewLiveZhufangxingzhiChangquanRb.getId() == checkedId) {
                    Housing = 1;

                } else if (mAddNewLiveZhufangxingzhiZulinRb.getId() == checkedId) {
                    Housing = 2;
                } else if (mAddNewLiveZhufangxingzhiLiangzuRb.getId() == checkedId) {
                    Housing = 3;
                } else if (mAddNewLiveZhufangxingzhiSifangRb.getId() == checkedId) {
                    Housing = 4;
                }
            }
        });


        mAddNewLiveIfHaveZhaoguRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mAddNewLiveIfHaveZhaoguYesRb.getId() == checkedId) {
                    IsHelp = 1;

                } else if (mAddNewLiveIfHaveZhaoguNoRb.getId() == checkedId) {
                    IsHelp = 2;
                }
            }
        });

        mAddNewLiveWhoZhaoguRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mAddNewLiveWhoZhaoguZinvRb.getId() == checkedId) {
                    Helper = 1;

                } else if (mAddNewLiveWhoZhaoguPeiouRb.getId() == checkedId) {
                    Helper = 2;
                } else if (mAddNewLiveWhoZhaoguQinyouRb.getId() == checkedId) {
                    Helper = 3;
                } else if (mAddNewLiveWhoZhaoguQitaRb.getId() == checkedId) {
                    Helper = 4;
                }
            }
        });


        mAddNewLiveJiuyifangshiRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mAddNewLiveJiuyifangshiJiatingjiuyiRb.getId() == checkedId) {
                    TreatmentMethod = 1;

                } else if (mAddNewLiveJiuyifangshiShequjiuyiRb.getId() == checkedId) {
                    TreatmentMethod = 2;
                } else if (mAddNewLiveJijizhuangkuanWaichujiuyiRb.getId() == checkedId) {
                    TreatmentMethod = 3;
                }

            }
        });


        mAddNewSexRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mAddNewSexMan.getId() == checkedId) {
                    gender = 1;

                } else if (mAddNewSexWoman.getId() == checkedId) {
                    gender = 2;
                }
            }
        });

        mAddNewEduRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mEduChuzhong.getId() == checkedId) {
                    edu = 3;
                } else if (mEduGaozhong.getId() == checkedId) {
                    edu = 4;
                } else if (mEduDaxue.getId() == checkedId) {
                    edu = 5;
                } else if (mEduBenkeyishang.getId() == checkedId) {
                    edu = 6;
                }
            }
        });

        mAddNewPmarita.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                if (mPmaritaWeihun.getId() == checkedId) {
                    pmarita = 1;
                } else if (mPmaritaYihun.getId() == checkedId) {
                    pmarita = 2;
                } else if (mPmaritaSanou.getId() == checkedId) {
                    pmarita = 3;
                } else if (mPmaritaLihun.getId() == checkedId) {
                    pmarita = 4;
                }
            }
        });


//        mAddNewJuzhuAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent();
//                intent1.setClass(context, PersonalAddressActivity.class);
//                intent1.putExtra("add", add);
//                startActivityForResult(intent1, ADDRESSREQUEST_res);
//            }
//        });
//
//
////            jsonObject.getJSONObject("CensusRegister").getString("PAddressStr").replace("/"," "));
//        mAddNewHujiAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent();
//                intent1.setClass(context, PersonalAddressActivity.class);
//                intent1.putExtra("add", add);//新增
//                //intent1.putExtra(CESADDRESS, censusRegister.toString());
//                startActivityForResult(intent1, ADDRESSREQUEST_ces);
//
//            }
//        });


    }

    private void setApplicantInfo() {

        mAddNewNameEt.setText(jsonObject.getString("Name"));
        mAddNewIdcardNumEt.setText(jsonObject.getString("CardId"));
        mAddNewShebaoType.setText(jsonObject.getString("SocialSecurity"));
        mAddNewShebaoNumTv.setText(jsonObject.getString("Number"));
        mAddNewNationEt.setText(jsonObject.getString("Nation"));
        mAddNewWork.setText(jsonObject.getString("Professional"));
        mAddNewPnativePlace.setText(jsonObject.getString("NativePlace"));
        mAddNewPbirthday.setText(jsonObject.getString("Birthday"));


        int sex = jsonObject.getInt("Gender");
        if (sex == 1) {
            mAddNewSexMan.setChecked(true);
        } else if (sex == 2) {
            mAddNewSexWoman.setChecked(true);
        }

       /* mAddNewJuzhuAddress.setText(toAddress(resudentialAddress));
        mAddNewHujiAddress.setText(toAddress(censusRegister));*/


        //TODO 户籍
        censusRegister = jsonObject.getJSONObject("CensusRegister");
        cesAddress = censusRegister.toString();
        mAddNewHujiAddress.setText(toAddress(censusRegister));
//        String str=toAddress(censusRegister);
//        if(str!=null&&str.trim().length()>0){
//            add=UPDATE;
//            isUpdate=true;
//        }else {
////            add=ADD;
//            isUpdate=false;
//        }
////        add=UPDATE;


        //TODO 现住地址
        resudentialAddress = jsonObject.getJSONObject("ResudentialAddress");
        resAddress = resudentialAddress.toString();
        mAddNewJuzhuAddress.setText(toAddress(resudentialAddress));
//        String strr=toAddress(resudentialAddress);
//        if(strr!=null&&strr.trim().length()>0){
//            add=UPDATE;
//            isUpdate=true;
//        }else {
////            add=ADD;
//            isUpdate=false;
//        }


//        mAddNewHujiAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent();
//                intent1.setClass(context, PersonalAddressActivity.class);
//                intent1.putExtra(CESADDRESS, censusRegister.toString());
//                startActivityForResult(intent1, ADDRESSREQUEST_ces);
//
//            }
//        });


        mAddNewYoubian.setText(jsonObject.getString("ZipCode"));
        mAddNewHomePhoneNum.setText(jsonObject.getString("HomePhone"));
        mAddNewTelphoneNum.setText(jsonObject.getString("MobilePhone"));
        mAddNewDailiren.setText(jsonObject.getString("AgentName"));
        mAddNewDailirenGuanxi.setText(jsonObject.getString("Relation"));
        mAddNewDailirenAddress.setText(jsonObject.getString("AgentAdress"));
        mAddNewDailirenYoubian.setText(jsonObject.getString("AgentZipCode"));
        mAddNewDailirenHomePhone.setText(jsonObject.getString("AgentHomePhone"));
        mAddNewDailirenTelphoneNum.setText(jsonObject.getString("AgentMobilePhone"));


        int jiehun = jsonObject.getInt("Marita");
        if (jiehun == 1) {
            mPmaritaWeihun.setChecked(true);
        } else if (jiehun == 2) {
            mPmaritaYihun.setChecked(true);
        } else if (jiehun == 3) {
            mPmaritaSanou.setChecked(true);
        } else if (jiehun == 4) {
            mPmaritaLihun.setChecked(true);
        }

        int Education = jsonObject.getInt("Education");
        if (Education <= 3) {
            mEduChuzhong.setChecked(true);
        } else if (Education == 4) {
            mEduGaozhong.setChecked(true);
        } else if (Education == 5) {
            mEduDaxue.setChecked(true);
        } else if (Education == 6) {
            mEduBenkeyishang.setChecked(true);

        }

        //上边22个

        //int Economic,Live,Housing,IsHelp,Helper,TreatmentMethod;

        int EconomicTemp =jsonObject.getInt("Economic");
        //Economic = jsonObject.getInt("Economic");
        if (EconomicTemp == 1) {
            mAddNewLiveJijizhuangkuanTuixiujingRb.setChecked(true);
        } else if (EconomicTemp == 2) {
            mAddNewLiveJijizhuangkuanZinvbutieRb.setChecked(true);
        } else if (EconomicTemp == 3) {
            mAddNewLiveJijizhuangkuanQinyouzizhuRb.setChecked(true);
        } else if (EconomicTemp == 4) {
            mAddNewLiveJijizhuangkuanQitabutieRb.setChecked(true);
        }


        int LiveTemp = jsonObject.getInt("Live");
        if (LiveTemp == 1) {
            mAddNewLiveJuzhuzhuangkuanYuzinvRb.setChecked(true);
        } else if (LiveTemp == 2) {
            mAddNewLiveJuzhuzhuangkuanYupeiouRb.setChecked(true);
        } else if (LiveTemp == 3) {
            mAddNewLiveJuzhuzhuangkuanYuqinqiRb.setChecked(true);
        } else if (LiveTemp == 4) {
            mAddNewLiveJuzhuzhuangkuanDuzishenghuoRb.setChecked(true);
        }

        int HousingTemp = jsonObject.getInt("Housing");
        if (HousingTemp == 1) {
            mAddNewLiveZhufangxingzhiChangquanRb.setChecked(true);
        } else if (HousingTemp == 2) {
            mAddNewLiveZhufangxingzhiZulinRb.setChecked(true);
        } else if (HousingTemp == 3) {
            mAddNewLiveZhufangxingzhiLiangzuRb.setChecked(true);
        } else if (HousingTemp == 4) {
            mAddNewLiveZhufangxingzhiSifangRb.setChecked(true);
        }

        int IsHelpTemp = jsonObject.getInt("IsHelp");
        if (IsHelpTemp == 1) {
            mAddNewLiveIfHaveZhaoguYesRb.setChecked(true);
        } else if (IsHelpTemp == 2) {
            mAddNewLiveIfHaveZhaoguNoRb.setChecked(true);
        }

        int  HelperTemp = jsonObject.getInt("Helper");
        if (HelperTemp == 1) {
            mAddNewLiveWhoZhaoguZinvRb.setChecked(true);
        } else if (HelperTemp == 2) {
            mAddNewLiveWhoZhaoguPeiouRb.setChecked(true);
        } else if (HelperTemp == 3) {
            mAddNewLiveWhoZhaoguQinyouRb.setChecked(true);
        } else if (HelperTemp == 4) {
            mAddNewLiveWhoZhaoguQitaRb.setChecked(true);
        }

        int  TreatmentMethodTemp = jsonObject.getInt("TreatmentMethod");
        if (TreatmentMethodTemp == 1) {
            mAddNewLiveJiuyifangshiJiatingjiuyiRb.setChecked(true);
        } else if (TreatmentMethodTemp == 2) {
            mAddNewLiveJiuyifangshiShequjiuyiRb.setChecked(true);
        } else if (TreatmentMethodTemp == 3) {
            mAddNewLiveJijizhuangkuanWaichujiuyiRb.setChecked(true);
        }

        mAddNewLiveJiuyifangshiYiyuanNameEt.setText(jsonObject.getString("Hospital"));

    }

    //新增 申请人
    void addNewApplicant(String setApplicantJson) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = getAddressNewApplicantThread.getResult();
                    if (UiUtil.isResultSuccess(AddApplicantActivity.this, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            AddApplicantActivity.this.jsonObject = jsonObject.getJSONObject("Data");
                            Toast.makeText(AddApplicantActivity.this, "保存成功", Toast.LENGTH_SHORT).show();

                            finish();
                        } else {
                            Toast.makeText(AddApplicantActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
//                            isClick=false;


                        }
                    }
                    isClick = false;
                }
            }
        };
        getAddressNewApplicantThread = new CommunityAssessThread(ApiConstant.ADDAPPLICANT, handler, this);
        getAddressNewApplicantThread.setApplicant(setApplicantJson);
        getAddressNewApplicantThread.start();


    }


    //编辑 申请人
    void updateApplicant(String setApplicantJson) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = getAddressNewApplicantThread.getResult();
                    if (UiUtil.isResultSuccess(AddApplicantActivity.this, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            AddApplicantActivity.this.jsonObject = jsonObject.getJSONObject("Data");


                            //Toast.makeText(AddApplicantActivity.this, "编辑成功", Toast.LENGTH_SHORT).show();

                            finish();
                        } else {
                            Toast.makeText(AddApplicantActivity.this, "编辑失败", Toast.LENGTH_SHORT).show();


                        }
                    }
                    isClick = false;
                }
            }
        };
        getAddressNewApplicantThread = new CommunityAssessThread(ApiConstant.UPDATEAPPLICANT, handler, this);
        getAddressNewApplicantThread.setApplicant(setApplicantJson);
        getAddressNewApplicantThread.start();


    }


    //通过 父id 循环获取 下一级 地址  晚上做
    void getAddress(int parentId) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ApiConstant.MSG_API_HANDLER) {
                    String result = addNewApplicantThread.getResult();
                    if (UiUtil.isResultSuccess(AddApplicantActivity.this, result)) {
                        JSONObject jsonObject = JSONObject.fromObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            shequArray = jsonObject.getJSONArray("Data");
                            Log.v("qqq", shequArray.toString() + "得到 社区地址的数据");


                        } else {
                            shequArray = null;
                        }

                        communityListAdapter.setArray(shequArray);
                        communityListAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
        addNewApplicantThread = new CommunityAssessThread(ApiConstant.GETCOMMUNITYLIST, handler, this);
        addNewApplicantThread.setParentId(parentId);
        addNewApplicantThread.start();


    }


    @OnClick({R.id.back_igv, R.id.right_btn_tv, R.id.add_new_address_xian,
            R.id.add_new_address_xiangzhen, R.id.add_new_address_shequ, R.id.add_new_pbirthday})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_igv:
                finish();
                break;
            case R.id.right_btn_tv: //TODO  保存按钮  如果 有数据才跳转，没有数据 不跳转



                if (isClick) {
                    return;
                }

                if (TextUtils.isEmpty(mAddNewNameEt.getEditableText().toString().trim())) {
                    UiUtil.showToast(context, "请输入姓名");
                    return;
                }

//                mRightBtnTv.setOnClickListener(null);
                /*29 个数据  */

                /*22个数据 分为3个模块 	1是 申请人(4个是选择的)  11个 	2是 联系信息 5个 	3是 代理人 6个*/
                Map map = new HashMap();

                notEmptySetMap(map, "Name", mAddNewNameEt.getText().toString());
                notEmptySetMap(map, "CardId", mAddNewIdcardNumEt.getText().toString());
                notEmptySetMap(map, "SocialSecurity", mAddNewShebaoType.getText().toString());
                notEmptySetMap(map, "Number", mAddNewShebaoNumTv.getText().toString());
                notEmptySetMap(map, "Nation", mAddNewNationEt.getText().toString());
                notEmptySetMap(map, "Professional", mAddNewWork.getText().toString());
                notEmptySetMap(map, "NativePlace", mAddNewPnativePlace.getText().toString());
                map.put("Gender", gender);//性别
                map.put("Education", edu);//教育程度
                map.put("Marita", pmarita);//婚姻状况

                int timeCompare = compare_date(mAddNewPbirthday.getText().toString(), GetDate.currentDate());
                if (timeCompare == 1) {
                    UiUtil.showToast(context, "请选择一个小于当前日期的生日");
                    return;
                }
                map.put("Birthday", mAddNewPbirthday.getText().toString());//出生日期

                //联系方式 5条
                if (censusRegister != null) {
                    map.put("CensusRegister", censusRegister);//
                }
                if (resudentialAddress != null) {
                    map.put("ResudentialAddress", resudentialAddress);//
                }


                notEmptySetMap(map, "ZipCode", mAddNewYoubian.getText().toString());
                notEmptySetMap(map, "HomePhone", mAddNewHomePhoneNum.getText().toString());
                notEmptySetMap(map, "MobilePhone", mAddNewTelphoneNum.getText().toString());


                /*代理人 6条 */
                notEmptySetMap(map, "AgentName", mAddNewDailiren.getText().toString());
                notEmptySetMap(map, "Relation", mAddNewDailirenGuanxi.getText().toString());
                notEmptySetMap(map, "AgentAdress", mAddNewDailirenAddress.getText().toString());
                notEmptySetMap(map, "AgentZipCode", mAddNewDailirenYoubian.getText().toString());
                notEmptySetMap(map, "AgentHomePhone", mAddNewDailirenHomePhone.getText().toString());
                notEmptySetMap(map, "AgentMobilePhone", mAddNewDailirenTelphoneNum.getText().toString());


                Log.v("qqq",Economic +"默认的 ");
                //生活状况有7个数据
                map.put("Economic", Economic);//
                map.put("Live", Live);//
                map.put("Housing", Housing);//
                map.put("IsHelp", IsHelp);
                map.put("Helper", Helper);
                map.put("TreatmentMethod", TreatmentMethod);
                notEmptySetMap(map, "Hospital", mAddNewLiveJiuyifangshiYiyuanNameEt.getText().toString());

                if (ifhaveCode == 1) {//更新
                    map.put("Id", id);
                    JSONObject jsonObject = JSONObject.fromObject(map);
                    //map.toString();
                    Log.v("zzzz", "修改申请人的数据  " + jsonObject.toString());
                    updateApplicant(jsonObject.toString());


                } else if (ifhaveCode == 0) {
                    JSONObject jsonObject = JSONObject.fromObject(map);
                    //map.toString();
                    Log.v("qqq", "新增申请人的数据  " + jsonObject.toString());
                    addNewApplicant(jsonObject.toString());

                }
                isClick = true;


                break;

            //时间选择节点
            case R.id.add_new_address_xian:
                //弹窗选择县
                showPopuwindow(mAddNewAddressXian, 0);
                break;
            case R.id.add_new_address_xiangzhen:
                //弹窗选择乡
                if (isParent_0) {
                    showPopuwindow(mAddNewAddressXiangzhen, 1);
                } else {
                    UiUtil.showToast(context, "请选择县/区");
                }

                break;
            case R.id.add_new_address_shequ:
                //弹窗选择社区
                if (isParent_1) {
                    showPopuwindow(mAddNewAddressShequ, 2);
                } else {
                    UiUtil.showToast(context, "请选择乡镇");
                }

                break;

            case R.id.add_new_pbirthday:
                new DatePickerDialog(AddApplicantActivity.this,
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


        }
    }


    public static int compare_date(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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


    /**
     * 更新日期显示(记得month要+1，因为DatePicker索引是0-11)
     */
    private void updateDateDisplay() {
        mAddNewPbirthday.setText(new StringBuilder().append(mYear).append("-")
                .append(mMonth + 1).append("-").append(mDay));
    }


    private void showPopuwindow(View view, final int addressLevel) {
        //不需要请求数据 只是选择一个 5个条目

        View contentView = LayoutInflater.from(this).inflate(R.layout.add_new_applicant_popupwindow, null, false);
        final ListView mListView = (ListView) contentView.findViewById(R.id.add_new_new_applicant_list_popupwindow_lv);

        mListView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        mListView.setAdapter(communityListAdapter);

        communityListAdapter.setArray(null);
        communityListAdapter.notifyDataSetChanged();
        //获取接口
        if (addressLevel == 0) {
            onePrentId = 0;


        } else if (addressLevel == 1) {
            onePrentId = parentId_1;

        } else if (addressLevel == 2) {
            onePrentId = parentId_2;
        }
        getAddress(onePrentId);

        final PopupWindow popupWindow = new PopupWindow(contentView);
        popupWindow.setFocusable(true);//设置弹出窗体可点击
        int w = getWindowManager().getDefaultDisplay().getWidth();
        //        int h=getWindowManager().getDefaultDisplay().getHeight();
        //设置弹出窗体的高
        popupWindow.setWidth(w / 3);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //实例化一个ColorDrawable颜色明为半透
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        popupWindow.setBackgroundDrawable(dw);

        //popupWindow.showAtLocation(chooseMonitoringScopeTV, Gravity.BOTTOM,0,0);//在父容器的位置

        popupWindow.showAsDropDown(view, 0, 0);
        backgroundAlpha(0.8f);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });


        //TODO 这里是条目的 点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  第一  给 文本 设置回显           //  第二 给  prentId  设置值
                if (addressLevel == 0) {
                    mAddNewAddressXian.setText(shequArray.getJSONObject(position).getString("CommunityName"));//选择后回填
                    parentId_1 = shequArray.getJSONObject(position).getInt("Id");
                    isParent_0 = true;
                    num1 = shequArray.getJSONObject(position).getString("CommunityNumber");
                    mAddNewShebaoNumTv.setText(num1 + "-");
                } else if (addressLevel == 1) {
                    mAddNewAddressXiangzhen.setText(shequArray.getJSONObject(position).getString("CommunityName"));//选择后回填
                    parentId_2 = shequArray.getJSONObject(position).getInt("Id");
                    isParent_1 = true;
                    num2 = shequArray.getJSONObject(position).getString("CommunityNumber");
                    mAddNewShebaoNumTv.setText(num1 + "-" + num2 + "-");
                } else if (addressLevel == 2) {
                    mAddNewAddressShequ.setText(shequArray.getJSONObject(position).getString("CommunityName"));//选择后回填
                    mAddNewShebaoNumTv.setText(num1 + "-" + num2 + "-" + shequArray.getJSONObject(position).getString("CommunityNumber"));
                }

//                onePrentId = shequArray.getJSONObject(position).getInt("Id");
                popupWindow.dismiss();


            }
        });


    }

    // 设置popwindow弹出的阴影效果
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    private String toAddress(JSONObject address) {
//        JSONObject j=JSONObject.fromObject(address);
        StringBuffer sb = new StringBuffer();
        sb.append(address.getString("District") + " ");
        sb.append(address.getString("Street") + " ");
        sb.append(address.getString("Road") + " ");
        sb.append(address.getString("Village") + " ");
        sb.append(address.getString("House") + " ");
        sb.append(address.getString("Room") + " ");
        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PersonalAddressActivity.BACKRESULTCODE) {
            Bundle bundle = data.getExtras();
            String str = bundle.getString(PersonalAddressActivity.BACKADDRESS);
            System.out.println("返回  内容 str:" + str);
            if (requestCode == ADDRESSREQUEST_ces) {
                censusRegister = JSONObject.fromObject(str);
                cesAddress = censusRegister.toString();
                mAddNewHujiAddress.setText(toAddress(censusRegister));
//                add=UPDATE;

            } else if (requestCode == ADDRESSREQUEST_res) {
                resudentialAddress = JSONObject.fromObject(str);
                resAddress = resudentialAddress.toString();
                mAddNewJuzhuAddress.setText(toAddress(resudentialAddress));
//                add=UPDATE;
            }
        }
    }

    void notEmptySetMap(Map map, String key, String value) {
        //TextUtils.isEmpty(value)
        if (value != null && value.trim().length() > 0) {
            map.put(key, value);
        }
    }


}
