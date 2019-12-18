package com.jxj.jdoctorassistant.adapter.doctor.userlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.model.SearchWord;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class SearchListAdapter extends BaseAdapter {

	public interface Delegete {
		void delete(String searchWord);
	}

	private class ViewHolder {

		@ViewInject(R.id.search_word_tv)
		public TextView wordTv;
		@ViewInject(R.id.delete_igv)
		public ImageView deleteIgv;

	}

	private Context context;
	private List<SearchWord> userbean;
	private LayoutInflater layoutinflater;
	private Delegete delegete;

	public SearchListAdapter(List userbean, Context context) {
		this.context = context;
		this.layoutinflater = LayoutInflater.from(context);
		this.userbean = userbean;
	}

	public void setDelegete(Delegete delegete) {
		this.delegete = delegete;
	}

	public void setUserbean(List<SearchWord> userbean) {
		this.userbean = userbean;
	}

	@Override
	public int getCount() {
		if (userbean!=null) {
			return userbean.size();
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
			convertView = layoutinflater
					.inflate(R.layout.lv_item_search, null);
			viewHolder = new ViewHolder();
			ViewUtils.inject(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		SearchWord user = userbean.get(position);
		final String searchWord=user.getSearchWord();
		viewHolder.wordTv.setText(searchWord);
		viewHolder.deleteIgv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				delegete.delete(searchWord);

			}
		});

		return convertView;

	}

}
