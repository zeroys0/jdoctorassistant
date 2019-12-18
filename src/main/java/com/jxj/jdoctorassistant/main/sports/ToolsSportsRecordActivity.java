package com.jxj.jdoctorassistant.main.sports;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.ToolsSportsRecordListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.main.sports.ToolsSportsMoveTrailBMActivity;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;

import java.util.Locale;

public class ToolsSportsRecordActivity extends Activity {

	private Context context;
	private String customerId;
	private int uId;
	private SharedPreferences sp;
	private int index = 0;
	private JAssistantAPIThread getSportRecord;
	private ToolsSportsRecordListAdapter adapter;

	private JSONArray jsonarray = null;

	@Bind(R.id.title_tv)
	TextView titleTv;
	@Bind(R.id.sports_record_hint_tv)
	TextView hintTv;
	@Bind(R.id.sport_recode_lv)
	PullToRefreshListView listview;

	@OnClick({ R.id.back_igv })
	public void Click(View v) {
		switch (v.getId()) {
		case R.id.back_igv:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tools_sports_record);
		ButterKnife.bind(this);
		context = this;
		initviews();

		

	}

	private void initviews() {
		titleTv.setText(getResources().getString(R.string.sm_record));
		
		SpannableString styledText = new SpannableString(getResources().getString(R.string.sm_record_hint));
		String local= Locale.getDefault().getLanguage();

		if(local.equals("zh")){
			styledText.setSpan(new TextAppearanceSpan(this, R.style.style_sports_record_hint_gray), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			styledText.setSpan(new TextAppearanceSpan(this, R.style.style_sports_record_hint_blue), 2, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			styledText.setSpan(new TextAppearanceSpan(this, R.style.style_sports_record_hint_gray), 6, 31, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			styledText.setSpan(new TextAppearanceSpan(this, R.style.style_sports_record_hint_red), 30, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			styledText.setSpan(new TextAppearanceSpan(this, R.style.style_sports_record_hint_gray), 34, styledText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}else{
			styledText.setSpan(new TextAppearanceSpan(this, R.style.style_sports_record_hint_gray), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			styledText.setSpan(new TextAppearanceSpan(this, R.style.style_sports_record_hint_blue), 5, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			styledText.setSpan(new TextAppearanceSpan(this, R.style.style_sports_record_hint_gray), 23, 104, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			styledText.setSpan(new TextAppearanceSpan(this, R.style.style_sports_record_hint_red), 107, 126, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			styledText.setSpan(new TextAppearanceSpan(this, R.style.style_sports_record_hint_gray), 126, styledText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		hintTv.setText(styledText, TextView.BufferType.SPANNABLE);
		
		sp = getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
		customerId = sp.getString(AppConstant.USER_customerId, null);
		uId=sp.getInt(AppConstant.ADMIN_userId, 0);
		adapter = new ToolsSportsRecordListAdapter(context);
		listview.setAdapter(adapter);

		
		listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				String label = "更新于:" + GetDate.currentTime();
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				index=0;
				refresh();

				listview.onRefreshComplete();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {

				index++;

				Handler handler_getRecord = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						if (msg.what == 0x133) {
							String mResult = getSportRecord.getResult();
							if (UiUtil.isResultSuccess(context, mResult)) {
									JSONObject j = JSONObject
											.fromObject(mResult);
								int code=j.getInt("code");
								if(code==200) {
									String str = null;
									if (j.containsKey("DataList")) {
										str = j.getString("DataList");
									} else if (j.containsKey("sportRecordList")) {
										str = j.getString("sportRecordList");
									} else {
										str = "[]";
									}
									JSONArray myjsonarray = JSONArray
											.fromObject(str);

									for (int i = 0, h = myjsonarray.size(); i < h; i++) {
										jsonarray.add(myjsonarray.get(i));
									}
								}else {
									UiUtil.showToast(context,j.getString("message"));
								}
								adapter.setJsonarray(jsonarray);
								adapter.notifyDataSetChanged();
							}
						}
					}
				};
				getSportRecord = new JAssistantAPIThread(
						ApiConstant.GETSPORTRECORDLIST, handler_getRecord,
						context);
				getSportRecord.setuId(uId);
				getSportRecord.setCustomerId(customerId);
				getSportRecord.setPageIndex(index);
				getSportRecord.setPageSize(10);
				getSportRecord.start();

				listview.onRefreshComplete();
			}

		});

		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				JSONObject jsonObj =jsonarray.getJSONObject(position);
        		
        		Intent intent= new Intent(context,ToolsSportsMoveTrailBMActivity.class);
        		intent.putExtra("sportId",jsonObj.getInt("Id"));
        		intent.putExtra("calorie",jsonObj.getString("Calorie"));
        		intent.putExtra("totaltime",jsonObj.getString("TotalTime"));
        		intent.putExtra("distance",jsonObj.getString("Distance"));
        		startActivity(intent);	
			}
		});
		
		
		
		
		refresh();
	}

	private void refresh() {
		Handler handler_getRecord = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if (msg.what == 0x133) {
					String mResult = getSportRecord.getResult();
					System.out.println("运动记录结果："+mResult);
					if (UiUtil.isResultSuccess(context, mResult)) {
						JSONObject j = JSONObject.fromObject(mResult);
						int code=j.getInt("code");
						if(code==200){
							String str =null;
							if(j.containsKey("DataList")){
								str=j.getString("DataList");
							}else if(j.containsKey("sportRecordList")){
								str=j.getString("sportRecordList");
							}else {
								str="[]";
							}
							jsonarray = JSONArray.fromObject(str);
						}else {
							UiUtil.showToast(context,j.getString("message"));
							jsonarray=null;
						}
						adapter.setJsonarray(jsonarray);
						adapter.notifyDataSetChanged();
					}
				}
			}
		};
		getSportRecord = new JAssistantAPIThread(
				ApiConstant.GETSPORTRECORDLIST, handler_getRecord, context);
		getSportRecord.setuId(uId);
		getSportRecord.setCustomerId(String.valueOf(customerId));
		getSportRecord.setPageIndex(index);
		getSportRecord.setPageSize(10);
		getSportRecord.start();
	}

}
