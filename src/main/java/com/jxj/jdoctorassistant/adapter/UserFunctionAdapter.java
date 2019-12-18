package com.jxj.jdoctorassistant.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.health.BloodRecordActivity;
import com.jxj.jdoctorassistant.health.ChartActivity;
import com.jxj.jdoctorassistant.health.HeartRateActivity;
import com.jxj.jdoctorassistant.main.ControlCenterActivity;
import com.jxj.jdoctorassistant.main.SendJDMessageActivity;
import com.jxj.jdoctorassistant.main.thirdData.ToolsThirdDataActivity;
import com.jxj.jdoctorassistant.main.contact.ToolsContactsActivity;
import com.jxj.jdoctorassistant.main.remind.ToolsJWotchRemindActivity;
import com.jxj.jdoctorassistant.main.sports.ToolsJwotchSportActivity;
import com.jxj.jdoctorassistant.main.location.ToolsLocationActivity;
import com.jxj.jdoctorassistant.main.userInfo.BasicInfoAvtivity;
import com.jxj.jdoctorassistant.main.userInfo.ContactInfoActivity;
import com.jxj.jdoctorassistant.main.userInfo.HealthInfoActivity;
import com.jxj.jdoctorassistant.main.userInfo.JWotchInfoActivity;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.MyGridView;

public class UserFunctionAdapter extends BaseAdapter {

	private Context context;
	private int resource;
	private String[] groups;
	private List<ArrayAdapter<String>> adapterList;
	private List<String[]> contentList;
	private String jwotchModel;

	public UserFunctionAdapter(Context context,
			String[] groups, List<ArrayAdapter<String>> adapterList,
			List<String[]> stringList) {

		this.context = context;
		this.groups = groups;
		this.adapterList = adapterList;
		this.contentList = stringList;
	}

//	public void setContentList(List<String[]> contentList) {
//		this.contentList = contentList;
//	}

	public void setJwotchModel(String jwotchModel) {
		this.jwotchModel = jwotchModel;
	}

	@Override
	public Object getItem(int i) {
		return i;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public int getCount() {
		return groups.length;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder holder = null;
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.infor_content_item, parent,
					false);
			holder = new ViewHolder();
			holder.itemText = (TextView) view.findViewById(R.id.infor_item_tv);
			holder.itemGrid = (MyGridView) view.findViewById(R.id.infor_item_grid);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.itemText.setText(groups[position]);
		holder.itemGrid.setAdapter(adapterList.get(position));
		final String[] contentString = contentList.get(position);
		holder.itemGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				startActivityByClick(contentString[position]);
			}
		});
		return view;
	}

	class ViewHolder {
		TextView itemText;
		MyGridView itemGrid;
	}

	/**
	 * 通过当前选中的子项跳转到对应的activity
	 */
	private void startActivityByClick(String type) {
		switch (type) {
		case "基本信息":
			context.startActivity(new Intent(context, 
					BasicInfoAvtivity.class));
			break;
		case "健康信息":
			context.startActivity(new Intent(context, 
					HealthInfoActivity.class));
			break;
		case "通讯信息":
			context.startActivity(new Intent(context, 
					ContactInfoActivity.class));
			break;
		case "腕表信息":
			context.startActivity(new Intent(context, 
					JWotchInfoActivity.class));
			break;
		case "血压":
			if(jwotchModel.equals(AppConstant.JWOTCHMODEL_032)){
				UiUtil.showToast(context,"JXJ-HM032用户并无血压模块");
			}else {
				context.startActivity(new Intent(context,
						BloodRecordActivity.class));
			}

			break;
		case "心率":
		{
			if (jwotchModel.equals(AppConstant.JWOTCHMODEL_031)){
				Intent intent=new Intent();
				intent.putExtra("type",AppConstant.CHART_HR);
				intent.setClass(context,ChartActivity.class);
				context.startActivity(intent);
			}else if(jwotchModel.equals(AppConstant.JWOTCHMODEL_032)||
					  jwotchModel.equals(AppConstant.JWOTCHMODEL_041)
					){
				context.startActivity(new Intent(context,
						HeartRateActivity.class));
			}

		}

			break;
		case "提醒":
			context.startActivity(new Intent(context,
					ToolsJWotchRemindActivity.class));
			break;
		case "定位":
			context.startActivity(new Intent(context,
					ToolsLocationActivity.class));
			break;
		case "运动轨迹":
			context.startActivity(new Intent(context,
					ToolsJwotchSportActivity.class));
			break;
		case "联系人":
			context.startActivity(new Intent(context,
					ToolsContactsActivity.class));
			break;
		case "发送信息":
			context.startActivity(new Intent(context,
					SendJDMessageActivity.class));
			break;
		case "卡路里":
			Intent intent=new Intent();
			intent.putExtra("type",AppConstant.CHART_CAL);
			intent.setClass(context,ChartActivity.class);
			context.startActivity(intent);
			break;
		case "控制面板":
			context.startActivity(new Intent(context,
					ControlCenterActivity.class));
			break;
		case "外设数据":
			context.startActivity(new Intent(context,
					ToolsThirdDataActivity.class));
			break;	
		default:
			break;
		}
	}
}
