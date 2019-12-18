package com.jxj.jdoctorassistant.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.UserListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.main.disease.DiseaseInfoActivity;
import com.jxj.jdoctorassistant.model.User;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

public class UserListActivity extends Activity {

	private List<User> users;
	private Context context;
	private SharedPreferences preferences;
	private JAssistantAPIThread getCustomerListThread;
//	private Handler customerListHandler;
//	private JAssistantAPIThread customerLoginThread;
//	private Handler customerLoginHandler;
//	private String account;
	@ViewInject(R.id.user_list_lv)
	ListView userLv;
//	@ViewInject(R.id.menu_btn)
//	private Button mentBtn;
	
	@OnClick({R.id.disease_btn})
	private void onClick(View view){
		switch (view.getId()) {
		case R.id.disease_btn:
//			startActivity(new Intent(context, DiseaseInfoActivity.class));
			
			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		context = this;
		ViewUtils.inject(this);
		init();
//		loginForService();
	}

	@OnItemClick(R.id.user_list_lv)
	public void onUserListItemClicked(AdapterView<?> arg0, View arg1,
			int position, long id) {
		User user = users.get(position);
//		account=user.getUserName();
		System.out.println(user.toString()+"======account======");
		String customerId=user.getCustomerId()+"";
		String jwotchModel=user.getJwotchModel();
//		preferences.edit().putString("customerId", customerId).commit();
//		String test1 = preferences.getString(AppConstant.USER_customerId, "");
//		System.out.println(test1+"11111111");
		preferences = getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
		preferences.edit().putString(AppConstant.USER_customerId, customerId).commit();
		preferences.edit().putString(AppConstant.USER_jwotchModel, jwotchModel).commit();
		Intent intent = new Intent(context, UserFunctionActivity.class);
		
		startActivity(intent);
//		loginForService();
	}

	private void init() {
		preferences = getSharedPreferences(AppConstant.USER_sp_name,
				MODE_PRIVATE);
		int userId = preferences.getInt("userId", 0);
		Handler customerListHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String result = getCustomerListThread.getResult();
				try {
					JSONObject response = new JSONObject(result);
					int code = response.getInt("code");
					String message = response.getString("message");
					if (code == 200) {
						UiUtil.showToast(context, message);
						JSONArray arr = response.getJSONArray("DataList");
						users = new ArrayList<>();
						for (int i = 0; i < arr.length(); i++) {
							JSONObject person = arr.getJSONObject(i);
							User user = new User();
							user.setCustomerId(person.getInt("CustomerID"));
							user.setUserName(person.getString("UserName"));
							user.setcName(person.getString("Cname"));
							user.setcPhoneNumber(person
									.getString("CPhoneNumber"));
							user.setJwotchModel(person.getString("Model"));
							
							user.setPs(person.getInt("PS"));
							user.setPd(person.getInt("PD"));
							user.setHr(person.getInt("HR"));
							
							users.add(user);
						}
						
						
						UserListAdapter listAdapter = new UserListAdapter(
								context, R.layout.lv_item_user_list, users);
						userLv.setAdapter(listAdapter);
					} else {
						UiUtil.showToast(context, message);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.handleMessage(msg);
			}
		};
		getCustomerListThread = new JAssistantAPIThread(
				ApiConstant.GETCUSTOMERLIST, customerListHandler, context);
		getCustomerListThread.setuId(userId);
		getCustomerListThread.setPageIndex(0);
		getCustomerListThread.setPageSize(20);
		getCustomerListThread.start();
	}

	/**
	 * 登录方法
	 */
//	private void loginForService() {
//		Handler customerLoginHandler = new Handler() {
//			@Override
//			public void handleMessage(Message msg) {
//				if(msg.what==0x133){
//					try {
//						JSONObject response=new JSONObject(customerLoginThread.getResult());
////						System.out.println("loginForService::::"+response);
//						
//						JSONObject json = response.getJSONObject("CustomerInfo");
//						String customerId = json.getString("CustomerID");
//						preferences = getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
//						preferences.edit().putString(AppConstant.USER_customerId, customerId).commit();
//						Intent toGrid = new Intent(context, UserFunctionActivity.class);
//						
//						startActivity(toGrid);
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}else{
//					System.out.println("loginForService failed");
//				}
//				super.handleMessage(msg);
//			}
//		};
//		customerLoginThread = new JAssistantAPIThread(
//				AppConstant.CUSTOMERLOGIN, customerLoginHandler, context);
//		customerLoginThread.setAccount("13005466007");
//		customerLoginThread.setPassword(MD5DeCode.GetMd5("111111"));
//		customerLoginThread.start();
//		
//	}
}
