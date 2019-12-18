package com.jxj.jdoctorassistant.adapter.doctor;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.thread.DownloadPhotoThread;
import com.jxj.jdoctorassistant.thread.IprecareThread;
import com.jxj.jdoctorassistant.util.ImageUtil;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.RoundImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DoctorUserListAdapter extends BaseAdapter {

	private Context context;
//	private int resourceId;
//	private List<User> userList;
	private JSONArray array;
	private LayoutInflater inflater;

//	private IprecareThread downloadPhotoThread;

	public DoctorUserListAdapter(Context context){
		this.context=context;
		inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		if(array!=null){
			return array.size();
		}else {
			return 0;
		}
	}

	public void setArray(JSONArray array) {
		this.array = array;
	}

	@Override
	public Object getItem(int position) {
		return array.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_doctor_user_list, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		JSONObject jsonObject=array.getJSONObject(position);
		holder.nameTv.setText(jsonObject.getString("Cname"));
		StringBuffer stringBuffer=new StringBuffer();
		if(jsonObject.getString("Gender").equals("M")){
			stringBuffer.append(context.getResources().getString(R.string.male)+"|");
			holder.headRigv.setImageDrawable(context.getResources().getDrawable(R.drawable.head_picture_man));
		}else {
			stringBuffer.append(context.getResources().getString(R.string.female)+"|");
			holder.headRigv.setImageDrawable(context.getResources().getDrawable(R.drawable.head_picture_woman));
		}
		stringBuffer.append(jsonObject.getInt("Age")+"|");
		stringBuffer.append(jsonObject.getString("Insurance"));

		holder.infoTv.setText(stringBuffer);
		String photo=jsonObject.getString("Photo");
//		ImageUtil.getBitmap();
//		setPhoto(photo,holder.headRigv);
		boolean isPay=jsonObject.getBoolean("PayAttention");
		if(isPay){
			holder.isPayIgv.setVisibility(View.VISIBLE);
		}else {
			holder.isPayIgv.setVisibility(View.GONE);
		}

		holder.symptomTv.setText(jsonObject.getString("Suggestion"));
		return convertView;
	}

	void setPhoto(String photo,final ImageView igv){
		final DownloadPhotoThread downloadPhotoThread=new DownloadPhotoThread(context);

		Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what== ApiConstant.MSG_API_HANDLER){
					String result=downloadPhotoThread.getResult();
					if(UiUtil.isResultSuccess(context,result)){
						JSONObject jsonObject=JSONObject.fromObject(result);
						int code=jsonObject.getInt("code");
						if(code==200){
							String data=jsonObject.getString("Data");
							igv.setImageBitmap(ImageUtil.getBitmap(data));
						}else {

						}
					}
				}
			}
		};
		downloadPhotoThread.setHandler(handler);
		downloadPhotoThread.setFileName(photo);
		downloadPhotoThread.start();
	}


	
	class ViewHolder{
		@ViewInject(R.id.doctor_user_rigv)
		RoundImageView headRigv;
		@ViewInject(R.id.doctor_user_name_tv)
		TextView nameTv;
		@ViewInject(R.id.doctor_user_info_tv)
		TextView infoTv;
		@ViewInject(R.id.doctor_user_symptom_tv)
		TextView symptomTv;
		@ViewInject(R.id.doctor_user_ispay_igv)
		ImageView isPayIgv;
	}
	
}
