package com.jxj.jdoctorassistant.main.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.ToolsRelativesListAdapter;
import com.jxj.jdoctorassistant.adapter.ToolsRelativesListAdapter.deleteRelativeInterface;
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
public class FragmentToolsRelatives extends Fragment implements
		deleteRelativeInterface {

	private SharedPreferences sp;
//	private Editor editor;

	private Context context;
	private String customerId;
	private int uId;
	private JAssistantAPIThread getRelativeThread, deleteRelativeThread;
	private ControlCenterServiceThread sendCmdThread;
	private ArrayList<JSONObject> list = new ArrayList<JSONObject>();
	private ToolsRelativesListAdapter adapter;

	@Bind(R.id.relatives_lv)
	ListView relativesLv;
//	@Bind(R.id.relatives_add_btn)
//	RelativeLayout relatiAddRl;

	@OnClick({ R.id.relatives_add_btn })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.relatives_add_btn:
			startActivity(new Intent(context, ToolsAddRelativesActivity.class));
			break;

		default:
			break;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_tools_relatives,
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

	private void initviews() {
		sp = getActivity().getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
//		editor = sp.edit();
		customerId = sp.getString(AppConstant.USER_customerId, null);
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);
		adapter = new ToolsRelativesListAdapter(context);
		relativesLv.setAdapter(adapter);
		relativesLv.setDividerHeight(0);

		refresh();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refresh();
		adapter.setDeleteInterface(this);
	}

	private void refresh() {
		Handler handler = new Handler() {
			@SuppressLint("ResourceAsColor")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 0x133) {
					String result = getRelativeThread.getResult();
					list.clear();
					if (UiUtil.isResultSuccess(context, result)) {
						JSONObject jsonobject = JSONObject.fromObject(result);
						if (jsonobject.getInt("code") == 200) {
							String data = jsonobject.getString("Data");
							JSONArray jsonarray = JSONArray.fromObject(data);

							Map<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
							for (int i = 0, j = jsonarray.size(); i < j; i++) {
								JSONObject json = jsonarray
											.getJSONObject(i);
								map.put(json.getInt("Position"), json);

							}
							for (int l = 1; l < 5; l++) {
									if (map.get(l) != null) {
										JSONObject json = map.get(l);
										list.add(json);
									}
							}

						}

					}
					adapter.setList(list);
					adapter.notifyDataSetChanged();
				}

			}
		};

		getRelativeThread = new JAssistantAPIThread(
				ApiConstant.GETRELATIVECONTACTLIST, handler,
				context);
		getRelativeThread.setuId(uId);
		getRelativeThread.setCustomerId(String.valueOf(customerId));
		getRelativeThread.start();
	}

	@Override
	public void deleteContact(int id) {

		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (msg.what == 0x133) {
					String mResult = deleteRelativeThread.getResult();
					if (UiUtil.isResultSuccess(context, mResult)) {
						JSONObject json = JSONObject.fromObject(mResult);
						if (json.getInt("code") == 200) {
							refresh();
							sendCmd();
						}
						UiUtil.showToast(context, json.getString("message"));
					}
				}

			}
		};

		deleteRelativeThread = new JAssistantAPIThread(
				ApiConstant.DELETERELATIVECONTACTBYID, handler,
				context);
		deleteRelativeThread.setuId(uId);
		deleteRelativeThread.setDataId(String.valueOf(id));
		try {
			deleteRelativeThread.start();
		} catch (Exception e) {
			// TODO: handle exception
		}

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
