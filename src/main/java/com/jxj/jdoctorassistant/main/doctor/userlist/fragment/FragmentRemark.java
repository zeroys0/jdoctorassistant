package com.jxj.jdoctorassistant.main.doctor.userlist.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONObject;


public class FragmentRemark extends Fragment {

	@ViewInject(R.id.remark_etv)
	private EditText remarkEtv;
	@OnClick({R.id.remark_finish_btn})
	void onClick(View view){
		switch (view.getId()){
			case R.id.remark_finish_btn:
				submitRemark();
				break;
		}
	}

	private DoctorSHThread getRemarkThread,setRemarkThread;
	private String customerId;
	private int doctorId;
	private Context context;
	private SharedPreferences sp;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_remark,container,false);

		ViewUtils.inject(this,view);

		context=getActivity();
		sp=context.getSharedPreferences(AppConstant.USER_sp_name,Context.MODE_PRIVATE);
		doctorId=sp.getInt(AppConstant.USER_doctor_id,0);
		customerId=sp.getString(AppConstant.USER_customerId,null);
		getRemark();
		return view;
	}

	void submitRemark(){
		String remark=remarkEtv.getEditableText().toString().trim();
		if(remark.length()<1){
			return;
		}

		Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what== ApiConstant.MSG_API_HANDLER){
					String result=setRemarkThread.getResult();
					if(UiUtil.isResultSuccess(context,result)){
						JSONObject jsonObject=JSONObject.fromObject(result);
						int code=jsonObject.getInt("code");
						UiUtil.showToast(context,jsonObject.getString("message"));
					}
				}
			}
		};
		setRemarkThread=new DoctorSHThread(ApiConstant.SETCUSTOMERREMARK,handler,context);
		setRemarkThread.setDoctorId(doctorId);
		setRemarkThread.setCustomerId(customerId);
		setRemarkThread.setRemark(remark);
		setRemarkThread.start();
		
	}
	void getRemark(){
		Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what== ApiConstant.MSG_API_HANDLER){
					String result=getRemarkThread.getResult();
					if(UiUtil.isResultSuccess(context,result)){
						JSONObject jsonObject=JSONObject.fromObject(result);
						int code=jsonObject.getInt("code");
						if(code==200) {
							remarkEtv.setText(jsonObject.getString("Remark"));
						}else {
							UiUtil.showToast(context,jsonObject.getString("message"));
						}
					}
				}
			}
		};
		getRemarkThread=new DoctorSHThread(ApiConstant.GETCUSTOMERREMARK,handler,context);
		getRemarkThread.setDoctorId(doctorId);
		getRemarkThread.setCustomerId(customerId);
		getRemarkThread.start();
	}
	/**
	 * 获取用户信息
	 */
//	private void initUserData() {
		
//		titleTv.setText("基本信息");
//		preferences=getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
//		customerId=preferences.getString(AppConstant.USER_customerId, "");
//		uId=preferences.getInt(AppConstant.ADMIN_userId, 0);
//		System.out.println(customerId+"====customerId=====");
//		Resources res = getResources();
//		dataSex = res.getStringArray(R.array.sex_array);
//		dataIswork = res.getStringArray(R.array.work_array);
//		dataIsmarried = res.getStringArray(R.array.marry_array);
//		dataIdtype = res.getStringArray(R.array.id_type_array);
//		userDataHandler = new Handler() {
//			public void handleMessage(Message msg) {
//				switch (msg.what) {
//				case 0x133:
//					String result = userDataThread.getResult();
//					try {
//						JSONObject response = new JSONObject(result);
//						int code=response.getInt("code");
//						String message=response.getString("message");
//						System.out.println(message);
//						if(code==200){
//							String data=response.getString("Data");
//							JSONObject resultJson=new JSONObject(data);
//							birthText.setText(resultJson.getString("Birthday"));
//							eduText.setText(resultJson.getString("Education"));
//							typeIdText.setText(resultJson.getString("IdNumber"));
//							int idType = resultJson.getInt("IdType");
//							cardTypeText.setText(dataIdtype[idType]);
//							insuranceText.setText(resultJson
//									.getString("InsuranceID"));
//							int idMarried = resultJson.getInt("IsMarried");
//							marryText.setText(dataIsmarried[idMarried]);
//							int idWork = resultJson.getInt("IsWorking");
//							workText.setText(dataIswork[idWork]);
//							nameText.setText(resultJson.getString("CName"));
//							nationalText.setText(resultJson.getString("National"));
//							proText.setText(resultJson.getString("Professional"));
//							int idSex = resultJson.getInt("Sex");
//							sexText.setText(dataSex[idSex]);
//						}else{
//							Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//						}
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//					break;
//
//				default:
//					break;
//				}
//			};
//		};
//		userDataThread = new JAssistantAPIThread(
//				ApiConstant.GETCUSTOMERBASICINFOBYID, userDataHandler, context);
//		userDataThread.setuId(uId);
//		userDataThread.setCustomerId(customerId);
//		userDataThread.start();
//	}
}
