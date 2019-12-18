package com.jxj.jdoctorassistant.adapter;

import java.util.ArrayList;

import com.jxj.jdoctorassistant.R;

import butterknife.Bind;
import butterknife.ButterKnife;

import net.sf.json.JSONObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ToolsRelativesListAdapter extends BaseAdapter {

	public interface deleteRelativeInterface {
		public void deleteContact(int id);

	}

	class ViewHolder {

		@Bind(R.id.relative_tv)
		public TextView contactName;
		@Bind(R.id.relative_phone_tv)
		public TextView contactPhone;
		@Bind(R.id.relative_img)
		public ImageView relativeImg;
		@Bind(R.id.relative_delete_rl)
		public RelativeLayout contactDeleteRl;

	}

	private Context context;
	private LayoutInflater layoutinflater;
	private deleteRelativeInterface deleteInterface;
	private ArrayList<JSONObject> list=new ArrayList<JSONObject>();
//	private JSONArray jsonarray;
	private int[] imgs = { R.drawable.relative_one, R.drawable.relative_two,
			R.drawable.relative_three, R.drawable.relative_four, };

	public ToolsRelativesListAdapter(Context context) {
		this.context = context;
		this.layoutinflater = LayoutInflater.from(context);
	}

	public void setDeleteInterface(deleteRelativeInterface deleteInterface) {
		this.deleteInterface = deleteInterface;
	}

	public void setList(ArrayList<JSONObject> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		} else {
			return 0;
		}

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = layoutinflater.inflate(
					R.layout.lv_item_tools_relatives, null);
			viewHolder = new ViewHolder();
			ButterKnife.bind(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		
		JSONObject jsonobject = list.get(position);
		int pos = jsonobject.getInt("Position");
		final int id = jsonobject.getInt("Id");
		viewHolder.contactName.setText(jsonobject.getString("ContactString"));
		viewHolder.contactPhone.setText(jsonobject.getString("PhoneNumber"));
		viewHolder.relativeImg.setImageResource(imgs[pos-1]);
		viewHolder.contactDeleteRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				deleteInterface.deleteContact(id);

			}
		});

		return convertView;

	}

}
