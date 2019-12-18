package com.jxj.jdoctorassistant.adapter.doctor;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.thread.DownloadPhotoThread;
import com.jxj.jdoctorassistant.thread.IprecareThread;
import com.jxj.jdoctorassistant.thread.PopularThread;
import com.jxj.jdoctorassistant.util.ImageUtil;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.RoundImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ApplySignAdapter extends BaseAdapter {

	private Context context;
	private JSONArray array;
//	private IprecareThread downloadPhotoThread;
	private String[] states={"请求签约","已取消","已同意","已拒绝"};

	public interface ApplySignDelegete{
		void refuse(int signId);
		void agree(int signId);
	}

	public ApplySignAdapter(Context context){
		this.context=context;
	}

	private ApplySignDelegete delegete;

	public void setDelegete(ApplySignDelegete delegete) {
		this.delegete = delegete;
	}

	public void setArray(JSONArray array) {
		this.array = array;
	}

	@Override
	public int getCount() {
		if(array!=null) {
			return array.size();
		}else {
			return 0;
		}
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
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_apply_sign, parent,false);
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
		final int signId=jsonObject.getInt("Id");
		int state=jsonObject.getInt("State");
		if(state==0){
			holder.agreeBtn.setVisibility(View.VISIBLE);
			holder.refuseBtn.setVisibility(View.VISIBLE);
			holder.stateTv.setVisibility(View.GONE);
		}else if(state<4){
			holder.agreeBtn.setVisibility(View.GONE);
			holder.refuseBtn.setVisibility(View.GONE);
			holder.stateTv.setVisibility(View.VISIBLE);
			holder.stateTv.setText(states[state]);
		}else {
			holder.agreeBtn.setVisibility(View.GONE);
			holder.refuseBtn.setVisibility(View.GONE);
			holder.stateTv.setVisibility(View.GONE);
		}

		String photo=jsonObject.getString("Photo");
		setPhoto(photo,holder.headRigv);

		holder.refuseBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				delegete.refuse(signId);
			}
		});

		holder.agreeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				delegete.agree(signId);
			}
		});
		return convertView;
	}

	void setPhoto(String photo,final RoundImageView igv){
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
		@ViewInject(R.id.apply_sign_head_rigv)
		RoundImageView headRigv;
		@ViewInject(R.id.apply_sign_name_tv)
		TextView nameTv;
		@ViewInject(R.id.apply_sign_info_tv)
		TextView infoTv;
		@ViewInject(R.id.apply_sign_refuse_btn)
		Button refuseBtn;
		@ViewInject(R.id.apply_sign_agree_btn)
		Button agreeBtn;
		@ViewInject(R.id.apply_sign_state_tv)
		TextView stateTv;

	}
	
}
