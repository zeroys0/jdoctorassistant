package com.jxj.jdoctorassistant.main.contact;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.ToolsEmayContactListAdapter;
import com.jxj.jdoctorassistant.adapter.ToolsEmayContactListAdapter.deleteEmeyInterface;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.ControlCenterServiceThread;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.UiUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressLint("NewApi")
public class FragmentToolsEmeyContacts extends Fragment implements
		deleteEmeyInterface {

	private SharedPreferences sp;
//	private Editor editor;

	private Context context;
	private String customerId;
	private int uId;
	private JSONArray jsonarray;

	private ToolsEmayContactListAdapter adapter;
	private JAssistantAPIThread getUrgenThread, deleteThread;
	private ControlCenterServiceThread sendCmdThread;
	private Handler handler_getUrgen, handler_delete;
	@Bind(R.id.emey_contact_lv)
	ListView emeyContactLv;

	@OnClick({ R.id.emey_contact_btn })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.emey_contact_btn:
			Intent intent = new Intent(context,
					ToolsAddEmeyContactsActivity.class);
			intent.putExtra("size", 0);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_tools_emeycontacts,
				container, false);
		ButterKnife.bind(this, view);
		context = getActivity().getApplicationContext();
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initviews();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		refresh();
		adapter.setEmeyInterface(this);

	}

	private void initviews() {
		sp = getActivity().getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
//		editor = sp.edit();
		customerId = sp.getString(AppConstant.USER_customerId, null);
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);
		adapter = new ToolsEmayContactListAdapter(context);
		emeyContactLv.setAdapter(adapter);
		emeyContactLv.setDividerHeight(0);

		refresh();
	}

	private void refresh() {

		handler_getUrgen = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 0x133) {
					String result = getUrgenThread.getResult();
					jsonarray=null;
					if (UiUtil.isResultSuccess(context, result)) {
							JSONObject json=JSONObject.fromObject(result);
							int code=json.getInt("code");
							String message=json.getString("message");
							UiUtil.showToast(context, message);
							if(code==200){
								String data=json.getString("Data");
								jsonarray = JSONArray.fromObject(data);
							}
					}
					adapter.setJsonarray(jsonarray);
					adapter.notifyDataSetChanged();
				}

			}

		};

		getUrgenThread = new JAssistantAPIThread(ApiConstant.GETURGENPEOPLE,
				handler_getUrgen, context);
		getUrgenThread.setuId(uId);
		getUrgenThread.setCustomerId(String.valueOf(customerId));
		getUrgenThread.start();
	}

	@Override
	public void deleteContact(int id) {

		handler_delete = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (msg.what == 0x133) {
					String mResult = deleteThread.getResult();
					if (UiUtil.isResultSuccess(context, mResult)) {
						JSONObject jsonObject=JSONObject.fromObject(mResult);
						int code=jsonObject.getInt("code");
						if(code==200){
							refresh();
							sendCmd();
						}
						UiUtil.showToast(context,jsonObject.getString("message"));
					}
				}

			}
		};

		deleteThread = new JAssistantAPIThread(
				ApiConstant.DELETEURGENPEOPLE, handler_delete, context);
		deleteThread.setuId(uId);
		deleteThread.setDataId(String.valueOf(id));
		try {
			deleteThread.start();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void updateContact(int position, int id) {

		JSONObject jsonobject = jsonarray.getJSONObject(position);
		Intent intent = new Intent(context, ToolsAddEmeyContactsActivity.class);
		intent.putExtra("size", 1);
		intent.putExtra("name", jsonobject.getString("uName"));
		intent.putExtra("phone", jsonobject.getString("phone"));
		intent.putExtra("id", jsonobject.getInt("uID"));
		intent.putExtra("sex", jsonobject.getString("sex"));
		intent.putExtra("relation", jsonobject.getString("relation"));
		intent.putExtra("email", jsonobject.getString("email"));
		intent.putExtra("address", jsonobject.getString("address"));

		startActivity(intent);

	}

	private void sendCmd(){
		Handler sendCmdHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x134) {
					String mResult = sendCmdThread.getResult();
					if (UiUtil.isResultSuccess(context, mResult)) {
						UiUtil.showSendCmdResult(context, mResult);
					}
				}
			}
		};

		sendCmdThread=new ControlCenterServiceThread(context,customerId+"", ApiConstant.refreshemergencecontactlist, sendCmdHandler);
		sendCmdThread.start();

	}

}
