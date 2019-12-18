package com.jxj.jdoctorassistant.main.doctor.adapter;

import android.view.View;

public interface OnSignClickListener {
    void onItemClick(View view);
    void onConfirmClick(int position,int id);
    void onCancelClick(int position,int id);

}
