package com.jxj.jdoctorassistant.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.bean.Urls;
import com.jxj.jdoctorassistant.thread.DownloadImageThread;
import com.jxj.jdoctorassistant.util.ImageUtil;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import net.sf.json.JSONObject;

import java.util.ArrayList;

public class ImageListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> images=new ArrayList<String>();

	public ImageListAdapter(Context context){
		this.context=context;
	}

//	public interface Delegete{
//		void loaded();
//	}
//
//	public Delegete delegete;
//
//	public void setDelegete(Delegete delegete) {
//		this.delegete = delegete;
//	}

	@Override
	public int getCount() {
			return images.size();
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_image, parent,false);
			holder=new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
//		setPhoto(images.get(position),holder.igv);
		Glide.with(context).load(Urls.INPUTDATAIMAGES+images.get(position)+".bmp").into(holder.igv);
		return convertView;
	}

	void setPhoto(String photo,final ImageView igv){
		final DownloadImageThread downloadImageThread=new DownloadImageThread(context);

		Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what== ApiConstant.MSG_API_HANDLER){
					String result=downloadImageThread.getResult();
					if(UiUtil.isResultSuccess(context,result)){
						JSONObject jsonObject=JSONObject.fromObject(result);
						int code=jsonObject.getInt("code");
						if(code==200){
							String data=jsonObject.getString("Data");
							igv.setImageBitmap(ImageUtil.getBitmap(data));
						}else {
							igv.setImageDrawable(context.getResources().getDrawable(R.drawable.load_fail));
						}
					}else {
						igv.setImageDrawable(context.getResources().getDrawable(R.drawable.load_fail));
					}
//					delegete.loaded();
				}
			}
		};
		downloadImageThread.setHandler(handler);
		downloadImageThread.setImage(photo);
		downloadImageThread.start();
	}
	
	class ViewHolder{
		@ViewInject(R.id.igv)
		ImageView igv;
	}
	
}
