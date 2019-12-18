package com.jxj.jdoctorassistant.adapter.community;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxj.jdoctorassistant.R;


/**
 * Created by liuli on 2017/6/2.
 */

public class PopuptAdapter extends BaseAdapter{
    private Context mContext;
    private String[] strs;
    public PopuptAdapter(Context mContext, String[] strs){
        this.mContext = mContext;
        this.strs =strs;
    }

    @Override
    public int getCount() {
        return strs.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView==null){
            convertView =  View.inflate(mContext, R.layout.item_button_choose_level,null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.button_text);
        textView.setText(strs[position]);

//        TranslateAnimation anim = new TranslateAnimation(0 ,0, -50 ,0); //左，右 50 从下到上 -50 从上到下
////        TranslateAnimation anim = new TranslateAnimation(50f, 50F, 50F, 80F);
//        // OvershootInterpolator    向前甩一定值后再回到原来位置
//        //DecelerateInterpolator 在动画开始的地方快然后慢
//        // AccelerateInterpolator  在动画开始的地方速率改变比较慢，然后开始加速
//        anim.setInterpolator(new AccelerateInterpolator());
//        anim.setRepeatCount(0); //Animation.INFINITE 无线循环
//        anim.setDuration(500);
//        textView.setAnimation(anim);
//        anim.setRepeatMode(Animation.REVERSE);//设置反方向执行  
//        anim.start();
        return convertView;
    }

}
