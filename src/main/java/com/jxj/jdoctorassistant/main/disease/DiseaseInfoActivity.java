package com.jxj.jdoctorassistant.main.disease;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.recycle.DiseaseClassAdapter;
import com.jxj.jdoctorassistant.adapter.recycle.DiseaseInfoAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.ThirdApiThread;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiseaseInfoActivity extends Activity{
	
	@ViewInject(value=R.id.title_tv,parentId=R.id.disease_info_title)
	private TextView titleTv;

	@ViewInject(R.id.disease_info_load_rl)
	private RelativeLayout loadRl;

	@OnClick({R.id.back_igv})
	private void onClick(View view){
		switch (view.getId()){
			case R.id.back_igv:
				finish();
				break;
		}
	}

//	@ViewInject(R.id.disease_class_rv);
	private RecyclerView diseaseClassRv;
	private RecyclerView diseaseInfoRv;


	private ThirdApiThread getDiseaseClassThread;
	private JSONArray array;

	private DiseaseClassAdapter diseaseClassAdapter =null;
	private DiseaseInfoAdapter diseaseInfoAdapter=null;
	private Context context;
	
//	private PopupWindow window;
	public static int SELECTPOSITION=0;
	private int diseaseClassImg[]={R.drawable.body_1,R.drawable.body_2,R.drawable.body_3,R.drawable.body_4,R.drawable.body_5,R.drawable.body_6,R.drawable.body_7,R.drawable.body_8,R.drawable.body_9,R.drawable.body_10,R.drawable.body_11,R.drawable.body_12,R.drawable.body_13,R.drawable.body_14,R.drawable.body_15,R.drawable.body_16,R.drawable.body_17,R.drawable.body_18};
	private int diseaseClassCheckedImg[]={R.drawable.body_checked_1,R.drawable.body_checked_2,R.drawable.body_checked_3,R.drawable.body_checked_4,R.drawable.body_checked_5,R.drawable.body_checked_6,R.drawable.body_checked_7,R.drawable.body_checked_8,R.drawable.body_checked_9,R.drawable.body_checked_10,R.drawable.body_checked_11,R.drawable.body_checked_12,R.drawable.body_checked_13,R.drawable.body_checked_14,R.drawable.body_checked_15,R.drawable.body_checked_16,R.drawable.body_checked_17,R.drawable.body_checked_18};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_disease_info);
		
		ViewUtils.inject(this);
		
		context=this;

		titleTv.setText(getResources().getString(R.string.disease_info));


		diseaseClassRv=(RecyclerView)findViewById(R.id.disease_class_rv);
		diseaseInfoRv=(RecyclerView)findViewById(R.id.disease_info_rv);
		setRecyclerView();
		setDiseaseClassAdapter();
		setDiseaseInfoAdapter();
		getDiseaseClass();
		
		
	}

	private void setRecyclerView(){
		diseaseClassRv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
		diseaseClassRv.addItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
		diseaseInfoRv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
		diseaseInfoRv.addItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
	}

	private void setDiseaseClassAdapter(){
		diseaseClassAdapter=new DiseaseClassAdapter(context);
		diseaseClassRv.setAdapter(diseaseClassAdapter);
		diseaseClassAdapter.setmOnItemClickListener(new DiseaseClassAdapter.OnItemClickListener(){
			@Override
			public void onItemClick(View v, int position) {
				SELECTPOSITION=position;
				diseaseClassAdapter.notifyDataSetChanged();
				JSONObject object=array.getJSONObject(position);
				String places=object.getString("places");
				JSONArray arr=JSONArray.fromObject(places);
				diseaseInfoAdapter.setArray(arr);
				diseaseInfoAdapter.notifyDataSetChanged();
			}

			@Override
			public void onItemLongClick(View v, int position) {

			}
		});
	}

	private void setDiseaseInfoAdapter(){
		diseaseInfoAdapter=new DiseaseInfoAdapter(context);
		diseaseInfoRv.setAdapter(diseaseInfoAdapter);
		diseaseInfoAdapter.setmOnItemClickListener(new DiseaseInfoAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View v, int position) {
				JSONObject object=array.getJSONObject(SELECTPOSITION);
				String places=object.getString("places");
				JSONArray arr=JSONArray.fromObject(places);
				JSONObject obj=arr.getJSONObject(position);
				int id=obj.getInt("id");

				Intent intent=new Intent();
				intent.putExtra("id",id);
				intent.setClass(context,DiseaseListActivity.class);
				startActivity(intent);
			}

			@Override
			public void onItemLongClick(View v, int position) {

			}
		});
	}

	
	private void getDiseaseClass(){
		Handler handler =new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what== ApiConstant.MSG_API_HANDLER){
					String result=getDiseaseClassThread.getResult();

					if(UiUtil.isResultSuccess(context, result)){
						loadRl.setVisibility(View.GONE);
						JSONObject json=JSONObject.fromObject(result);
						boolean status=json.getBoolean("status");
						if(status){

							String tngou=json.getString("tngou");
							array=JSONArray.fromObject(tngou);
							diseaseClassAdapter.setArray(array);
							diseaseClassAdapter.setShowImg(diseaseClassImg);
							diseaseClassAdapter.setHideImg(diseaseClassCheckedImg);
							diseaseClassAdapter.notifyDataSetChanged();

							JSONObject object=array.getJSONObject(SELECTPOSITION);
							String places=object.getString("places");
							JSONArray arr=JSONArray.fromObject(places);
							diseaseInfoAdapter.setArray(arr);
							diseaseInfoAdapter.notifyDataSetChanged();

						}else {
							UiUtil.showToast(context,getResources().getString(R.string.tgou_data_error));
						}
						
					}
				}
			}
		};
		
		getDiseaseClassThread=new ThirdApiThread(handler, context);
		getDiseaseClassThread.setUrl("http://www.tngou.net/api/place/all");
		getDiseaseClassThread.setArgs("");
		getDiseaseClassThread.start();
	}

}
