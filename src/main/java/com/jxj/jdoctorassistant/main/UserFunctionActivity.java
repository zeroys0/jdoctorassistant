package com.jxj.jdoctorassistant.main;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.UserFunctionAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;

public class UserFunctionActivity extends Activity {
//	private JAssistantAPIThread getJWotchThread;
//	private Handler handler_getJWotch;
	private SharedPreferences preferences;
//	private String customerId;
	private String jwotchModel;
	private Context context;
	
	
	@Bind(R.id.title_tv)
	TextView titleTv;
	@Bind(R.id.content_list_lv)
	ListView contentList;
	
	@OnClick(R.id.back_igv)
	public void onBackClick(View view) {
		finish();
	}
	private List<ArrayAdapter<String>> adapterList = new ArrayList<ArrayAdapter<String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_user_function);
		
		ButterKnife.bind(this);
		context = this;
		initView();
	}

	@SuppressLint("HandlerLeak")
	private void initView() {
		titleTv.setText("随访助手");
		preferences = getSharedPreferences(AppConstant.USER_sp_name, MODE_PRIVATE);
		jwotchModel=preferences.getString(AppConstant.USER_jwotchModel, "");
		initData(jwotchModel);
	}



	/**
	 * 初始化数据
	 */
	private void initData(String type) {
		UserFunctionAdapter adapter=null;
		String[] groups=getResources().getStringArray(R.array.user_function_list_group);
		List<String[]> stringList = new ArrayList<String[]>();
		String[] s1 = { "基本信息", "健康信息", "通讯信息", "腕表信息" };
		initAdapterList(initAdapter(initList(s1)));
		String[] s2 = { "血压","心率", "卡路里","外设数据"};
		initAdapterList(initAdapter(initList(s2)));
		String[] s3 = { "提醒", "定位", "运动轨迹", "联系人" };
		initAdapterList(initAdapter(initList(s3)));
		String[] s4 = { "控制面板" };
		initAdapterList(initAdapter(initList(s4)));
		String[] s5 = { "发送信息" };
		stringList.add(s1);
		stringList.add(s2);
		stringList.add(s3);
		stringList.add(s4);
		stringList.add(s5);
		initAdapterList(initAdapter(initList(s5)));

		adapter = new UserFunctionAdapter(
					context, groups,
					adapterList, stringList);
		adapter.setJwotchModel(jwotchModel);
		contentList.setAdapter(adapter);
	}

	/**
	 * 生成每个grid的adapter的方法
	 */
	public ArrayAdapter<String> initAdapter(List<String> lists) {
		ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
				R.layout.infor_content_item_item, R.id.grid_item_content, lists);
		return adapter;
	}

	/**
	 * 生成list的方法
	 */
	public List<String> initList(String[] content) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < content.length; i++) {
			list.add(content[i]);
		}
		return list;
	}

	/**
	 * 生成adapter列表的方法
	 */
	public void initAdapterList(ArrayAdapter<String> adapter) {
		adapterList.add(adapter);
	}
}
