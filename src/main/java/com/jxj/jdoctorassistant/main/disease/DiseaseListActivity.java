package com.jxj.jdoctorassistant.main.disease;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.DiseaseDetailLvAdapter;
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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiseaseListActivity extends Activity{
	
	@ViewInject(value=R.id.title_tv,parentId=R.id.disease_detail_title)
	private TextView titleTv;
	@ViewInject(R.id.disease_lv)
	private ListView diseaseDetailLv;
	@ViewInject(R.id.disease_load_rl)
	private RelativeLayout loadRl;

    @OnClick({R.id.back_igv})
	private void onClick(View view){
		switch (view.getId()){
			case R.id.back_igv:
				finish();
				break;
		}
	}
	
	private ThirdApiThread getDiseaseDetailThread;
	private JSONArray array;
	
	private DiseaseDetailLvAdapter lvAdapter;
	
	private Context context;
	
	private PopupWindow window;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_disease_detail);
		
		ViewUtils.inject(this);
		
		context=this;

		titleTv.setText(getResources().getString(R.string.disease_list));

		Bundle bundle=getIntent().getExtras();
		int id=bundle.getInt("id");

		lvAdapter=new DiseaseDetailLvAdapter(context);
		diseaseDetailLv.setAdapter(lvAdapter);
		diseaseDetailLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				JSONObject json=array.getJSONObject(position);
				String disease=json.toString();
//				System.out.println("1--疾病信息："+disease);
				Intent intent=new Intent();
				intent.putExtra("disease",disease);
				intent.setClass(context,DiseaseContentActivity.class);
				startActivity(intent);

			}
		});

//		diseaseClassGv.setNumColumns(3);
//		diseaseClassGv.setAdapter(adapter);
//		diseaseClassGv.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int positon,
//					long arg3) {
//				// TODO Auto-generated method stub
//				JSONObject object=array.getJSONObject(positon);
//				String places=object.getString("places");
//				JSONArray array=JSONArray.fromObject(places);
//				showWindow(array);
//			}
//		});
		
		
		getDiseaseDetail(id);
		
		
	}
	
//	private void showWindow(JSONArray array){
//
//
//		View view=getLayoutInflater().inflate(R.layout.pop_window_disease_list, null,false);
//		ListView lv=(ListView) view.findViewById(R.id.disease_info_lv);
//		lv.setAdapter(lvAdapter);
//		lvAdapter.setArray(array);
//		lvAdapter.notifyDataSetChanged();
//
//		window=new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
//		window.setAnimationStyle(R.style.AnimationFade);
//	    window.showAsDropDown(titleTv);
//	    view.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				if(window!=null && window.isShowing()){
//					window.dismiss();
//					window=null;
//				}
//				return false;
//			}
//		});
//	}
	
	private void getDiseaseDetail(int id){
		Handler handler =new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				if(msg.what== ApiConstant.MSG_API_HANDLER){
					String result= getDiseaseDetailThread.getResult();

					if(UiUtil.isResultSuccess(context, result)){
						loadRl.setVisibility(View.GONE);
						try {
							JSONObject json=JSONObject.fromObject(result);

							String list = json.getString("list");
							array = JSONArray.fromObject(list);
							lvAdapter.setArray(array);
							lvAdapter.notifyDataSetChanged();
						}catch (Exception e) {
							e.printStackTrace();
							UiUtil.showToast(context,getResources().getString(R.string.tgou_data_error));
						}
					}
				}
			}
		};
		
		getDiseaseDetailThread =new ThirdApiThread(handler, context);
		getDiseaseDetailThread.setUrl("http://www.tngou.net/api/disease/place");
		getDiseaseDetailThread.setArgs("id="+id);
		getDiseaseDetailThread.start();
	}

}
