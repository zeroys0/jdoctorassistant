package com.jxj.jdoctorassistant.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.model.JdMessage;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

	class ViewHolder {
		@ViewInject(R.id.elv_group_arrow_img)
		ImageView groupImg;
		@ViewInject(R.id.textView01)
		TextView groupTv;
	}

	class ViewHolderChild {
		@ViewInject(R.id.childTo)
		TextView childTv;
	}

	private Context context;

	// 定义两个List用来控制Group和Child中的String;
	// private List<String> groupArray;// 组列表
	// private List<List<String>> childArray;// 子列表
	private List<JdMessage> list;
	private LayoutInflater layoutinflater;

	public ExpandableListViewAdapter(Context context, List<JdMessage> list) {
		this.context = context;
		this.layoutinflater = LayoutInflater.from(context);
		// this.groupArray = groupArray;
		// this.childArray = childArray;

		this.list = list;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		// return childArray.get(groupPosition).get(childPosition);
		return list.get(groupPosition).getContent()[childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	// 获取二级列表的View对象
	@Override
	public View getChildView(int parent_pos, int child_pos, boolean arg2,
			View childView, ViewGroup arg4) {
		ViewHolderChild viewHolderChild;

		if (childView == null) {
			// String str = childArray.get(arg0).get(arg1);
			childView = layoutinflater.inflate(R.layout.elv_item_child, null);
//		}
		viewHolderChild = new ViewHolderChild();
		ViewUtils.inject(viewHolderChild, childView);
		childView.setTag(viewHolderChild);
		 } else {
		 viewHolderChild = (ViewHolderChild) childView.getTag();
		 }
		viewHolderChild.childTv
				.setText(list.get(parent_pos).getContent()[child_pos]);

		return childView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0).getContent().length;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return getGroup(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int position, boolean isSelected, View parentView,
			ViewGroup arg3) {
		ViewHolder viewHolder;

		if (parentView == null) {
			// String str = groupArray.get(arg0);
			parentView = layoutinflater.inflate(R.layout.elv_item_group, null);
//		}
		viewHolder = new ViewHolder();
		ViewUtils.inject(viewHolder, parentView);
		parentView.setTag(viewHolder);

		 } else {
		 viewHolder = (ViewHolder) parentView.getTag();
		 }
		viewHolder.groupTv.setText(list.get(position).getName());
		// 需要把判断的语句写到判断arg2的外面，否则箭头不会改变
		if (!isSelected) {
			viewHolder.groupImg
					.setBackgroundResource(R.drawable.msg_btn_unclicked);
		} else {
			viewHolder.groupImg
					.setBackgroundResource(R.drawable.msg_btn_clicked);
		}
		return parentView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}