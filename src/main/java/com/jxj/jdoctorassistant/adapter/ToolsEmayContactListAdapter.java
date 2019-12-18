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

public class ToolsEmayContactListAdapter extends BaseAdapter {

	public interface deleteEmeyInterface {
		public void deleteContact(int id);

		public void updateContact(int position, int id);
	}

	class ViewHolder {

		@Bind(R.id.emey_name_tv)
		public TextView name;
		@Bind(R.id.emey_sex_tv)
		public TextView sex;
		@Bind(R.id.emey_relation_tv)
		public TextView relation;
		@Bind(R.id.emey_phone_tv)
		public TextView phone;
		@Bind(R.id.emey_email_tv)
		public TextView email;
		@Bind(R.id.emey_address_tv)
		public TextView address;
		@Bind(R.id.emey_update_rl)
		public RelativeLayout contactUpdateRl;
		@Bind(R.id.emey_delete_rl)
		public RelativeLayout contactDeleteRl;

	}

	private Context context;
	private LayoutInflater layoutinflater;
	private deleteEmeyInterface emeyInterface;
	private JSONArray jsonarray;

	public ToolsEmayContactListAdapter(Context context) {
		this.context = context;
		this.layoutinflater = LayoutInflater.from(context);
	}

	public void setEmeyInterface(deleteEmeyInterface emeyInterface) {
		this.emeyInterface = emeyInterface;
	}

	public void setJsonarray(JSONArray jsonarray) {
		this.jsonarray = jsonarray;
	}

	@Override
	public int getCount() {
		if (jsonarray != null) {
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
					R.layout.lv_item_tools_emeycontact, null);
			viewHolder = new ViewHolder();
			ButterKnife.bind(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		JSONObject jsonobject = jsonarray.getJSONObject(position);

		final int id = jsonobject.getInt("uID");
		final int pos = position;
		viewHolder.name.setText(jsonobject.getString("uName"));
		viewHolder.sex.setText(jsonobject.getString("sex"));
		viewHolder.relation.setText(jsonobject.getString("relation"));
		viewHolder.phone.setText(jsonobject.getString("phone"));
		viewHolder.email.setText(jsonobject.getString("email"));
		viewHolder.address.setText(jsonobject.getString("address"));
		viewHolder.contactDeleteRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				emeyInterface.deleteContact(id);

			}
		});

		viewHolder.contactUpdateRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				emeyInterface.updateContact(pos, id);
			}
		});

		return convertView;

	}

}
