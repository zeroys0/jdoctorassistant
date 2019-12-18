package com.jxj.jdoctorassistant.main.doctor.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.DoctorUserListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.main.doctor.userlist.ExceptionUserListActivity;
import com.jxj.jdoctorassistant.main.doctor.userlist.PatientInfoActivity;
import com.jxj.jdoctorassistant.main.doctor.userlist.SearchUserActivity;
import com.jxj.jdoctorassistant.main.doctor.userlist.SpecialUserActivity;
import com.jxj.jdoctorassistant.model.User;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public class FragmentUserList extends Fragment{
	@ViewInject(R.id.all_customer_num_tv)
	private TextView allCustomerNumTv;
	@ViewInject(R.id.exception_user_num_tv)
	private TextView exceptionUserNumTv;
	@ViewInject(R.id.special_user_num_tv)
	private TextView specialUserNumTv;
	@ViewInject(R.id.late_service_num_tv)
	private TextView lateServiceNumTv;
	@ViewInject(R.id.late_add_num_tv)
	private TextView lateAddNumTv;
	@ViewInject(R.id.late_browse_num_tv)
	private TextView lateBrowseNumTv;

	@ViewInject(R.id.doctor_user_list_lv)
	ListView userLv;


	private List<User> users;
	private Context context;
	private SharedPreferences preferences;
	private DoctorSHThread getCustomerCountsThread,getCustomerListThread;
	int doctorId;

	public static final String CUSTOMERTYPE="customer_type";
	public static final String CUSTOMERTITLE="customer_title";



	private JSONArray array;
	DoctorUserListAdapter adapter;
	private boolean isLive=false;

	@OnClick({R.id.doctor_user_list_search_igv,R.id.exception_user_rl,R.id.special_user_rl,R.id.late_service_rl,R.id.late_add_rl,R.id.late_browse_rl})
	void onClick(View view){
		switch (view.getId()){
			case R.id.doctor_user_list_search_igv:
				startActivity(new Intent(context, SearchUserActivity.class));
				break;
			case R.id.exception_user_rl:
				startActivity(new Intent(context, ExceptionUserListActivity.class));
				break;
			case R.id.special_user_rl:
				Intent intent=new Intent(context,SpecialUserActivity.class);
				intent.putExtra(CUSTOMERTYPE,AppConstant.CUSTOMER_SPECIAL);
				intent.putExtra(CUSTOMERTITLE,context.getResources().getString(R.string.special_care));
				startActivity(intent);
				break;
			case R.id.late_service_rl:
				intent=new Intent(context,SpecialUserActivity.class);
				intent.putExtra(CUSTOMERTYPE,AppConstant.CUSTOMER_LAST);
				intent.putExtra(CUSTOMERTITLE,context.getResources().getString(R.string.late_service));
				startActivity(intent);
				break;
			case R.id.late_add_rl:
				intent=new Intent(context,SpecialUserActivity.class);
				intent.putExtra(CUSTOMERTYPE,AppConstant.CUSTOMER_LAST_ADD);
				intent.putExtra(CUSTOMERTITLE,context.getResources().getString(R.string.late_add));
				startActivity(intent);
				break;
			case R.id.late_browse_rl:
				intent=new Intent(context,SpecialUserActivity.class);
				intent.putExtra(CUSTOMERTYPE,AppConstant.CUSTOMER_LAST_BROWSE);
				intent.putExtra(CUSTOMERTITLE,context.getResources().getString(R.string.late_browse));
				startActivity(intent);
				break;
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_doctor_user_list,container,false);
		ViewUtils.inject(this,view);
		context=getActivity();
		isLive=true;
		init();

		return view;
	}

	private void init() {
		preferences = context.getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_PRIVATE);
		doctorId = preferences.getInt(AppConstant.USER_doctor_id, 0);
		Log.d("test","医生id："+doctorId);

		adapter=new DoctorUserListAdapter(context);
		userLv.setAdapter(adapter);
		setListViewHeightBasedOnChildren(userLv);
		userLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				JSONObject object=array.getJSONObject(position);
//				String username=object.getString("Cname");
				String customerId=object.getString("CustomerId");
//				User user = users.get(i);
////		account=user.getUserName();
//				System.out.println(user.toString()+"======account======");
//				String customerId=user.getCustomerId()+"";
//				String jwotchModel=user.getJwotchModel();
////		preferences.edit().putString("customerId", customerId).commit();
////		String test1 = preferences.getString(AppConstant.USER_customerId, "");
////		System.out.println(test1+"11111111");
				preferences = context.getSharedPreferences(AppConstant.USER_sp_name, context.MODE_PRIVATE);
				preferences.edit().putString(AppConstant.USER_customerInfo, object.toString()).commit();
				preferences.edit().putString(AppConstant.USER_customerId,customerId).commit();
//				preferences.edit().putString(AppConstant.USER_jwotchModel, jwotchModel).commit();
				Intent intent = new Intent(context, PatientInfoActivity.class);

				startActivity(intent);
			}
		});

		getCustomerCounts();
		getCustomerList();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDestroyView() {
		isLive=false;
		super.onDestroyView();
	}

	void getCustomerCounts(){
		@SuppressLint("HandlerLeak") Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String result = getCustomerCountsThread.getResult();
				Log.d("test","获取签约用户列表 result："+result);
				if(UiUtil.isResultSuccess(context,result)){
					JSONObject jsonObject=JSONObject.fromObject(result);
					int code=jsonObject.getInt("code");
					if(code==200){
						JSONArray jsonArray=jsonObject.getJSONArray("Data");
						int[] nums=new int[6];
						for(int i=0;i<jsonArray.size();i++){
							JSONObject object=jsonArray.getJSONObject(i);
							nums[object.getInt("Category")]=object.getInt("Counts");
						}
						if(isLive) {
							allCustomerNumTv.setText(nums[0] + getResources().getString(R.string.people_unit));
							exceptionUserNumTv.setText(nums[1] + getResources().getString(R.string.people_unit));
							specialUserNumTv.setText(nums[2] + getResources().getString(R.string.people_unit));
							lateServiceNumTv.setText(nums[3] + getResources().getString(R.string.people_unit));
							lateAddNumTv.setText(nums[4] + getResources().getString(R.string.people_unit));
							lateBrowseNumTv.setText(nums[5] + getResources().getString(R.string.people_unit));
						}

					}else {
						UiUtil.showToast(context,jsonObject.getString("message"));

					}
				}else{

				}


			}
		};
		getCustomerCountsThread = new DoctorSHThread(
				ApiConstant.GETCUSTOMERCOUNTS, handler, context);
		getCustomerCountsThread.setDoctorId(doctorId);
		getCustomerCountsThread.start();
	}

	void getCustomerList(){
		@SuppressLint("HandlerLeak") Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String result = getCustomerListThread.getResult();
				Log.d("test","获取签约用户列表 result："+result);
				if(UiUtil.isResultSuccess(context,result)){
					JSONObject jsonObject=JSONObject.fromObject(result);
					int code=jsonObject.getInt("code");
					if(code==200){
						array=jsonObject.getJSONArray("Data");
						adapter.setArray(array);
					}else {
						UiUtil.showToast(context,jsonObject.getString("message"));
						adapter.setArray(null);
					}
				}else{
						adapter.setArray(null);
				}
				adapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(userLv);

			}
		};
		getCustomerListThread = new DoctorSHThread(
				ApiConstant.GETALLCUSTOMER, handler, context);
		getCustomerListThread.setDoctorId(doctorId);
		getCustomerListThread.setCategory(AppConstant.CUSTOMER_ALL);
		getCustomerListThread.setCondition("");
		getCustomerListThread.setStartDate(AppConstant.STARTDATE);
		getCustomerListThread.setEndDate(GetDate.currentDate());
		getCustomerListThread.start();
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if(listView == null) return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
