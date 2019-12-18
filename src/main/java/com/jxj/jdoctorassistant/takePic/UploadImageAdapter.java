package com.jxj.jdoctorassistant.takePic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.jxj.jdoctorassistant.R;

import java.io.InputStream;
import java.util.LinkedList;

/**
 * 多图上传,动态添加图片适配器
 */
public class UploadImageAdapter extends BaseAdapter {
	
	private LinkedList<String> imagePathList;
	private Context context;
	private boolean isAddData = true;
	private LayoutInflater inflater;
	/**
	 * 控制最多上传的图片数量
	 */
	private int imageNumber = 9;
	
	public UploadImageAdapter(Context context, LinkedList<String> imagePath) {
		this.context = context;
		this.imagePathList = imagePath;
		inflater = LayoutInflater.from(context);
	}
	
	public void update(LinkedList<String> imagePathList){
		this.imagePathList = imagePathList;
		//这里控制选择的图片放到前面,默认的图片放到最后面,
		if(isAddData){
			//集合中的总数量等于上传图片的数量加上默认的图片不能大于imageNumber + 1
			if(imagePathList.size() == imageNumber + 1){
				//移除默认的图片


				imagePathList.removeLast();
				isAddData = false;
			}
		}else{
			//添加默认的图片
			imagePathList.addLast(null);
			isAddData = true;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return imagePathList == null ? 0 : imagePathList.size();
	}

	@Override
	public Object getItem(int position) {
		return imagePathList == null ? null : imagePathList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return  position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//ImageView iv_image;


		ViewHolder viewHolder;

		if (convertView == null) {//创建ImageView

			convertView = inflater.inflate(R.layout.grid_view_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView=(ImageView) convertView.findViewById(R.id.itemImage);
			viewHolder.deleteImage=(ImageView) convertView.findViewById(R.id.item_delete);
			convertView.setTag(viewHolder);



			/*iv_image = new ImageView(context);
			iv_image.setLayoutParams(new AbsListView.LayoutParams(ImageUtils.getWidth(context) / 3 - 5, ImageUtils.getWidth(context) / 3 - 5) );
			iv_image.setScaleType(ImageButton.ScaleType.CENTER_CROP);
			convertView = iv_image ;
			*/


		}else{
			viewHolder = (ViewHolder) convertView.getTag();
			//iv_image = (ImageView) convertView;
		}


		if(getItem(position) == null ){//图片地址为空时设置默认图片
			//iv_image.setImageResource(R.drawable.upload);
			viewHolder.mImageView.setImageResource(R.drawable.upload);
			viewHolder.deleteImage.setVisibility(View.GONE);


		}else{
			//获取图片缩略图，避免OOM
			Bitmap bitmap = ImageUtils.getImageThumbnail((String)getItem(position), ImageUtils.getWidth(context) / 3 - 5, ImageUtils.getWidth(context) / 3 - 5);
			//iv_image.setImageBitmap(bitmap);
			viewHolder.mImageView.setImageBitmap(bitmap);
            viewHolder.deleteImage.setVisibility(View.VISIBLE);


		}
		return convertView;
	}


	class  ViewHolder{
		public ImageView mImageView;
		public ImageView deleteImage;


	}



}
