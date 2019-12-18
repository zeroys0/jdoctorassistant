package com.jxj.jdoctorassistant.adapter.recycle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by Administrator on 2017/2/14.
 */
public class Department2Adapter extends RecyclerView.Adapter<Department2Adapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    private JSONArray array;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
        void onItemLongClick(View v, int position);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public Department2Adapter(Context mContext) {
        this.mContext = mContext;
        inflater=LayoutInflater.from(mContext);
    }

    public void setArray(JSONArray array) {
        this.array = array;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.rv_item_department_2,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JSONObject object=array.getJSONObject(position);
        holder.tv.setText(object.getString("name"));

        setOnListener(holder);
    }

    protected void setOnListener(final RecyclerView.ViewHolder holder){
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int layoutPosition=holder.getPosition();
                    mOnItemClickListener.onItemClick(holder.itemView,layoutPosition);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    int layoutPosition=holder.getPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView,layoutPosition);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(array==null){
            return 0;
        }else{
            return array.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public ViewHolder(View itemView){
            super(itemView);
            tv=(TextView) itemView.findViewById(R.id.department_2_tv);

        }
    }
}
