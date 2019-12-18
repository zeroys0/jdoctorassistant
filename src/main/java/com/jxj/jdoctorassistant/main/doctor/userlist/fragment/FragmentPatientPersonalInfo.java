package com.jxj.jdoctorassistant.main.doctor.userlist.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.PatientHistoryAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;


public class FragmentPatientPersonalInfo extends Fragment {

	private DoctorSHThread getBasicInfoThread,getHealthInfoThread,getContactInfoThread,getMedicalHistoryThread;
	private String[] dataSex;
	private String[] dataIswork;
	private String[] dataIsmarried;
//	private String[] dataIdtype;
	private String customerId;
//	private int uId;
	private SharedPreferences preferences;

    @ViewInject(R.id.patient_name_tv)
    private TextView nameTv;
	@ViewInject(R.id.patient_sex_tv)
	private TextView sexTv;
	@ViewInject(R.id.patient_national_tv)
	private TextView nationalTv;
	@ViewInject(R.id.patient_marry_tv)
	private TextView marryTv;
	@ViewInject(R.id.patient_education_tv)
	private TextView educationTv;
	@ViewInject(R.id.patient_profession_tv)
	private TextView professionTv;
	@ViewInject(R.id.patient_birth_tv)
	private TextView birthTv;
	@ViewInject(R.id.patient_phone_tv)
	private TextView phoneTv;
	@ViewInject(R.id.patient_email_tv)
	private TextView emailTv;
	@ViewInject(R.id.patient_address_tv)
	private TextView addressTv;

	@ViewInject(R.id.patient_height_tv)
	private TextView heightTv;
	@ViewInject(R.id.patient_weight_tv)
	private TextView weightTv;
	@ViewInject(R.id.patient_waist_tv)
	private TextView waistTv;
	@ViewInject(R.id.patient_blood_type_tv)
	private TextView bloodTypeTv;
	@ViewInject(R.id.patient_ps_tv)
	private TextView psTv;
	@ViewInject(R.id.patient_pd_tv)
	private TextView pdTv;
//	@ViewInject(R.id.patient_history_lv)
//	private ListView historyLv;


	private Context context;
	private int doctorId;
	private SharedPreferences sp;
	private PatientHistoryAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_doctor_patient_info,container,false);
		ViewUtils.inject(this,view);
		context=getActivity();

		sp=context.getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
		doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
		customerId=sp.getString(AppConstant.USER_customerId,"");

		initView();
		getBasicInfo();
		getHealthInfo();
		getContactInfo();
		getMedicalHistory();


		return view;
	}

	/**
	 * 获取用户信息
	 */
	void getBasicInfo(){
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {

				if(msg.what==ApiConstant.MSG_API_HANDLER) {
						String result = getBasicInfoThread.getResult();
						if(UiUtil.isResultSuccess(context,result)) {
							JSONObject response = JSONObject.fromObject(result);
							int code = response.getInt("code");
							String message = response.getString("message");
							System.out.println(message);
							if (code == 200) {
								String data = response.getString("Data");
								JSONObject jsonObject = JSONObject.fromObject(data);
								birthTv.setText(jsonObject.getString("Birthday"));
								educationTv.setText(jsonObject.getString("Education"));

								int idMarried = jsonObject.getInt("IsMarried");
								marryTv.setText(dataIsmarried[idMarried]);
								nameTv.setText(jsonObject.getString("CName"));
								nationalTv.setText(jsonObject.getString("National"));
								professionTv.setText(jsonObject.getString("Professional"));
								int idSex = jsonObject.getInt("Sex");
								sexTv.setText(dataSex[idSex]);
							} else {
								UiUtil.showToast(context, message);
							}

						}
				}
			};
		};
		getBasicInfoThread = new DoctorSHThread(
				ApiConstant.GETCUSTOMERBASICINFO, handler, context);
		getBasicInfoThread.setDoctorId(doctorId);
		getBasicInfoThread.setCustomerId(customerId);
		getBasicInfoThread.start();
	}
	void getHealthInfo(){
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what==ApiConstant.MSG_API_HANDLER) {
					String result = getHealthInfoThread.getResult();
					if(UiUtil.isResultSuccess(context,result)) {
						JSONObject response = JSONObject.fromObject(result);
						int code = response.getInt("code");
						String message = response.getString("message");
						System.out.println(message);
						if (code == 200) {
							String data = response.getString("Data");
							JSONObject jsonObject = JSONObject.fromObject(data);
							bloodTypeTv.setText(jsonObject.getString("BloodType"));
							heightTv.setText(jsonObject.getString("Height"));
							pdTv.setText(jsonObject.getInt("PdBlood")+"");
							psTv.setText(""+jsonObject.getInt("PsBlood"));
							waistTv.setText(""+jsonObject.getInt("Waist"));
							weightTv.setText(""+jsonObject.getInt("Weight"));
						} else {
							UiUtil.showToast(context, message);
						}

					}
				}
			};
		};
		getHealthInfoThread = new DoctorSHThread(
				ApiConstant.GETCUSTOMERHEALTHINFO, handler, context);
		getHealthInfoThread.setDoctorId(doctorId);
		getHealthInfoThread.setCustomerId(customerId);
		getHealthInfoThread.start();
	}
	void getContactInfo(){
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what==ApiConstant.MSG_API_HANDLER) {
					String result = getContactInfoThread.getResult();
					if(UiUtil.isResultSuccess(context,result)) {
						JSONObject response = JSONObject.fromObject(result);
						int code = response.getInt("code");
						String message = response.getString("message");
						System.out.println(message);
						if (code == 200) {
							String data = response.getString("Data");
							JSONObject jsonObject = JSONObject.fromObject(data);
							addressTv.setText(jsonObject
									.getString("Address"));
							emailTv.setText(jsonObject
									.getString("Email"));
							phoneTv.setText(jsonObject
									.getString("TelPhone"));
						} else {
							UiUtil.showToast(context, message);
						}

					}
				}
			};
		};
		getContactInfoThread = new DoctorSHThread(
				ApiConstant.GETCUSTOMERCONTACTINFO, handler, context);
		getContactInfoThread.setDoctorId(doctorId);
		getContactInfoThread.setCustomerId(customerId);
		getContactInfoThread.start();
	}

	void getMedicalHistory(){
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if(msg.what==ApiConstant.MSG_API_HANDLER) {
					String result = getMedicalHistoryThread.getResult();
					if(UiUtil.isResultSuccess(context,result)) {
						JSONObject response = JSONObject.fromObject(result);
						int code = response.getInt("code");
						String message = response.getString("message");
						System.out.println(message);
						if (code == 200) {
							String data = response.getString("Data");
							JSONArray array=JSONArray.fromObject(data);

						} else {
							UiUtil.showToast(context, message);
						}

					}
				}
			};
		};
		getMedicalHistoryThread = new DoctorSHThread(
				ApiConstant.GETMEDICALHISTORY, handler, context);
		getMedicalHistoryThread.setDoctorId(doctorId);
		getMedicalHistoryThread.setCustomerId(customerId);
		getMedicalHistoryThread.setCulture("zh-cn");
		getMedicalHistoryThread.start();
	}

	private void initView() {

		preferences=context.getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
		customerId=preferences.getString(AppConstant.USER_customerId, "");
//		uId=preferences.getInt(AppConstant.ADMIN_userId, 0);
		System.out.println(customerId+"====customerId=====");
		Resources res = getResources();
		dataSex = res.getStringArray(R.array.sex_array);
		dataIswork = res.getStringArray(R.array.work_array);
		dataIsmarried = res.getStringArray(R.array.marry_array);
//		dataIdtype = res.getStringArray(R.array.id_type_array);
		adapter=new PatientHistoryAdapter(context);


	}
	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}
}
