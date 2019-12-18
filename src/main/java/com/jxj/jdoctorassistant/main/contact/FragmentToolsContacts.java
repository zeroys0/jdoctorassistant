package com.jxj.jdoctorassistant.main.contact;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.ToolsContactsListAdapter;
import com.jxj.jdoctorassistant.adapter.ToolsContactsListAdapter.deleteContactInterface;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
public class FragmentToolsContacts extends Fragment implements
		deleteContactInterface {

	private SharedPreferences sp;
//	private Editor editor;
	private Context context;
	private String customerId;
	private int uId;
	private ToolsContactsListAdapter adapter;
	private JAssistantAPIThread getContactThread, deleteContactThread;
	private Handler handler_getContact, handler_deleteContact;
	private JSONArray jsonarray = null;
	@ViewInject(R.id.contacts_lv)
	ListView contactsLv;

//	@OnClick({ R.id.contacts_add_btn })
//	public void Click(View v) {
//		switch (v.getId()) {
//		case R.id.contacts_add_btn:
//			Intent intent = new Intent(context, ToolsAddContactsActivity.class);
//			intent.putExtra("size", 0);
//			startActivity(intent);
//			break;
//
//		default:
//			break;
//		}
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_tools_contacts,
				container, false);
		ViewUtils.inject(this,view);
//		ButterKnife.bind(this, view);
		context = getActivity().getApplicationContext();

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

//		initviews();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

//		refresh();
//		adapter.setDeleteInterface(this);

	}

	private void initviews() {
		sp = getActivity().getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_PRIVATE);
//		editor = sp.edit();
		customerId = sp.getString(AppConstant.USER_customerId,null);
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);
		adapter = new ToolsContactsListAdapter(context);
		contactsLv.setAdapter(adapter);
		contactsLv.setDividerHeight(0);
//		refresh();
	}

	private void refresh() {
		handler_getContact = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 0x133) {
					String result = getContactThread.getResult();
					if (UiUtil.isResultSuccess(context, result)) {

						try {
							jsonarray = JSONArray.fromObject(result);
							adapter.setJsonarray(jsonarray);
							adapter.notifyDataSetChanged();

						} catch (Exception e) {
							adapter.setJsonarray(null);
							adapter.notifyDataSetChanged();
							UiUtil.showToast(context,
									getResources()
											.getString(R.string.show_date));
						}

					}
				}
			}

		};

		getContactThread = new JAssistantAPIThread(
				ApiConstant.GETCONTACTLISTBYCUSTOMERID, handler_getContact,
				context);
		getContactThread.setuId(uId);
		getContactThread.setCustomerId(String.valueOf(customerId));
		getContactThread.start();
	}

	@Override
	public void deleteContact(int id) {
		handler_deleteContact = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (msg.what == 0x133) {
					String mResult = deleteContactThread.getResult();
					if (UiUtil.isResultSuccess(context, mResult)) {
//						refresh();
						UiUtil.showToast(
								context,
								"删除成功");
					}
				}

			}
		};

		deleteContactThread = new JAssistantAPIThread(
				ApiConstant.DELETECONTACT, handler_deleteContact, context);
		deleteContactThread.setuId(uId);
		deleteContactThread.setRemindID(id);
		try {
			deleteContactThread.start();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void updateContact(int position, int id) {

		JSONObject jsonobject = jsonarray.getJSONObject(position);
		Intent intent = new Intent(context, ToolsAddContactsActivity.class);
		intent.putExtra("size", 1);
		intent.putExtra("name", jsonobject.getString("name"));
		intent.putExtra("phone", jsonobject.getString("phone"));
		intent.putExtra("id", jsonobject.getInt("id"));
		startActivity(intent);

	}

}
