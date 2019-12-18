package com.jxj.jdoctorassistant.adapter;

import com.jxj.jdoctorassistant.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ToolsContactsListAdapter extends BaseAdapter {

	public interface deleteContactInterface {
		public void deleteContact(int id);

		public void updateContact(int position, int id);
	}

	class ViewHolder {

		@Bind(R.id.contact_tv)
		public TextView contactName;
		@Bind(R.id.contact_phone_tv)
		public TextView contactPhone;
		@Bind(R.id.contact_update_rl)
		public RelativeLayout contactUpdateRl;
		@Bind(R.id.contact_delete_rl)
		public RelativeLayout contactDeleteRl;

	}

	private Context context;
	private LayoutInflater layoutinflater;
	private deleteContactInterface deleteInterface;
	private JSONArray jsonarray;

	public ToolsContactsListAdapter(Context context) {
		this.context = context;
		this.layoutinflater = LayoutInflater.from(context);
	}




	public void setDeleteInterface(deleteContactInterface deleteInterface) {
		this.deleteInterface = deleteInterface;
	}



	public void setJsonarray(JSONArray jsonarray) {
		this.jsonarray = jsonarray;
	}

	@Override
	public int getCount() {
		if (jsonarray!= null) {
			return jsonarray.size();
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
					R.layout.lv_item_tools_contact, null);
			viewHolder = new ViewHolder();
			ButterKnife.bind(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		JSONObject jsonobject = jsonarray.getJSONObject(position);

		final int id = jsonobject.getInt("id");
		final int pos = position;
		viewHolder.contactName.setText(jsonobject.getString("name"));
		viewHolder.contactPhone.setText(jsonobject.getString("phone"));
		viewHolder.contactDeleteRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				deleteInterface.deleteContact(id);

			}
		});

		viewHolder.contactUpdateRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				deleteInterface.updateContact(pos, id);
			}
		});

		return convertView;

	}

}
