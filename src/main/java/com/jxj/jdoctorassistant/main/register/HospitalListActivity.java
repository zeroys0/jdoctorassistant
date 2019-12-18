package com.jxj.jdoctorassistant.main.register;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.recycle.CityAdapter;
import com.jxj.jdoctorassistant.adapter.recycle.ProvinceAdapter;
import com.jxj.jdoctorassistant.adapter.thirdData.HospitalListAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.ThirdApiThread;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.jxj.jdoctorassistant.view.CustomClipLoading;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HospitalListActivity extends Activity {
    @ViewInject(value = R.id.title_tv,parentId = R.id.hospital_list_title)
    private TextView titleTv;
    @ViewInject(value = R.id.right_btn_tv,parentId = R.id.hospital_list_title)
    private TextView cityTv;
    @ViewInject(R.id.hospital_lv)
    PullToRefreshListView hospitalLv;

    @ViewInject(R.id.hospital_et)
    private EditText hospitalEt;


    private PopupWindow cityWindow;
    private View cityView;
    private RecyclerView provinceRv;
    private RecyclerView cityRv;
    private CustomClipLoading loading;


    private Context context;
    private ThirdApiThread getCityListThread,getHospitalListThread;
    private JSONArray provinceArray,cityArray,hospitalArray;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;
    private HospitalListAdapter hospitalListAdapter;

    public static int SELECTPOSITION=0;
    private int id;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private int page=1;

    @OnClick({R.id.back_igv,R.id.hospital_edit_btn})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
                break;
            case R.id.hospital_edit_btn:
                String hospital=hospitalEt.getEditableText().toString().trim();
                if(hospital.length()>0){
                    Intent intent=new Intent();
                    intent.putExtra("hospitalName",hospital);
                    setResult(0x01,intent);
                    finish();
                }else{
                    UiUtil.showToast(context,getResources().getString(R.string.not_null));
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hospital_list);
        ViewUtils.inject(this);

        context=this;

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        editor=sp.edit();
//        setPopWindow();

        hospitalLv.setVisibility(View.GONE);
////        setHosiptalAdapter();
////        cityTv.setVisibility(View.VISIBLE);
////        cityTv.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                if(cityWindow==null||!cityWindow.isShowing()){
////                    setPopWindow();
////                }
////
////            }
////        });
//        if(sp.contains(AppConstant.USER_city_id)){
//            String city=sp.getString(AppConstant.USER_city_name,"");
//            id=sp.getInt(AppConstant.USER_city_id,0);
//            cityTv.setText(city);
//            getHospitalList(true);
//        }else {
//            showHintDialog();
//        }
        hospitalLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = getResources()
                        .getString(R.string.update_time)
                        + GetDate.currentTime();
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                hospitalArray=null;
                getHospitalList(true);
                hospitalLv.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getHospitalList(false);
                hospitalLv.onRefreshComplete();
            }
        });
    }
    void showHintDialog(){
        View viewNull = LayoutInflater.from(context)
                .inflate(R.layout.view_null, null);

        android.app.AlertDialog mAlertDialog = new android.app.AlertDialog.Builder(context)
                .setTitle(getResources().getString(R.string.set_hospital_hint))
                .setView(viewNull)
                .setPositiveButton(getResources().getString(R.string.ok),new ok())
                .create();

        mAlertDialog.show();
    }
   private class ok implements DialogInterface.OnClickListener{
       @Override
       public void onClick(DialogInterface dialogInterface, int i) {
           setPopWindow();
       }
   }

    void  setPopWindow(){

        cityView= LayoutInflater.from(context).inflate(R.layout.pop_window_city_list,null);

        int h=getWindowManager().getDefaultDisplay().getHeight();
        cityWindow=new PopupWindow(cityView, LayoutParams.MATCH_PARENT,h/2,true);
        cityWindow.setFocusable(true);
        cityWindow.setBackgroundDrawable(new PaintDrawable(0));
//        cityWindow=new PopupWindow(context, ViewGroup.LayoutParams.MATCH_PARENT,v)
//        if(cityWindow!=null&&cityWindow.isShowing())
//        cityWindow.setOutsideTouchable(true);
        provinceRv=(RecyclerView)cityView.findViewById(R.id.province_rv);
        cityRv=(RecyclerView)cityView.findViewById(R.id.city_rv);
        loading=(CustomClipLoading)cityView.findViewById(R.id.city_list_loading);
        setRecyclerView();
        setCityRecycleAdapter();
        loading.setVisibility(View.VISIBLE);
        getCityList();
        cityWindow.showAtLocation(cityView, Gravity.BOTTOM,0,0);
        backgroundAlpha(0.5f);
    }
    void getCityList(){
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    loading.setVisibility(View.GONE);
                    String result=getCityListThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject json=JSONObject.fromObject(result);
                        boolean status=json.getBoolean("status");
                        if(status){
                            String tngou=json.getString("tngou");
                            provinceArray=JSONArray.fromObject(tngou);
                            provinceAdapter.setArray(provinceArray);
                            provinceAdapter.notifyDataSetChanged();

                            JSONObject jsonObject=provinceArray.getJSONObject(0);
                            String citys=jsonObject.getString("citys");
                            cityArray=JSONArray.fromObject(citys);
                            cityAdapter.setArray(cityArray);
                            cityAdapter.notifyDataSetChanged();
                        }else {
                            UiUtil.showToast(context,getResources().getString(R.string.tgou_data_error));
                        }
                    }
                }
            }
        };

        getCityListThread=new ThirdApiThread(handler,context);
        getCityListThread.setUrl("http://www.tngou.net/api/area/province");
        getCityListThread.setArgs("type=all");
        getCityListThread.start();
    }
    void getHospitalList(final boolean isRefresh){
        if(isRefresh) {
            page=1;
        }else {
            page++;
        }
        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== ApiConstant.MSG_API_HANDLER){
//                    loading.setVisibility(View.GONE);
                    String result=getHospitalListThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject json=JSONObject.fromObject(result);
                        boolean status=json.getBoolean("status");
                        if(status){
                            String tngou=json.getString("tngou");
                            JSONArray array=JSONArray.fromObject(tngou);
                            if(isRefresh) {
                                hospitalArray=array;
                            }else {
                                hospitalArray.addAll(array);
                            }
                            hospitalListAdapter.setJsonarray(hospitalArray);
                            hospitalListAdapter.notifyDataSetChanged();
                        }else {
                            UiUtil.showToast(context,getResources().getString(R.string.tgou_data_error));
                        }
                    }
                }
            }
        };

        getHospitalListThread=new ThirdApiThread(handler,context);
        getHospitalListThread.setUrl("http://www.tngou.net/api/hospital/list");
        getHospitalListThread.setArgs("id="+id+"&row="+20+"&page="+page);
        getHospitalListThread.start();
    }
    void setRecyclerView(){
        provinceRv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        provinceRv.addItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
        cityRv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        cityRv.addItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
    }
    void setCityRecycleAdapter(){
        provinceAdapter=new ProvinceAdapter(context);
        provinceRv.setAdapter(provinceAdapter);
        provinceAdapter.setmOnItemClickListener(new ProvinceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                SELECTPOSITION=position;
                provinceAdapter.notifyDataSetChanged();
                JSONObject jsonObject=provinceArray.getJSONObject(position);
                String citys=jsonObject.getString("citys");
                cityArray=JSONArray.fromObject(citys);
                cityAdapter.setArray(cityArray);
                cityAdapter.notifyDataSetChanged();

            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
        cityAdapter=new CityAdapter(context);
        cityRv.setAdapter(cityAdapter);
        cityAdapter.setmOnItemClickListener(new CityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                JSONObject jsonObject=cityArray.getJSONObject(position);
                String city=jsonObject.getString("city");
                id=jsonObject.getInt("id");
                editor.putInt(AppConstant.USER_city_id,id);
                editor.putString(AppConstant.USER_city_name,city);
                editor.commit();
                cityTv.setText(city);
                getHospitalList(true);

                cityWindow.dismiss();
                backgroundAlpha(1f);
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
    }

    void setHosiptalAdapter(){
        hospitalListAdapter=new HospitalListAdapter(context);
        hospitalLv.setAdapter(hospitalListAdapter);
        hospitalLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent();
//                UiUtil.showToast(context,"第"+i+"位");
                intent.putExtra("hospitalName",hospitalArray.getJSONObject(i-1).getString("name"));
                setResult(0x01,intent);
                finish();
            }
        });
    }

    // 设置popwindow弹出的阴影效果
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            backgroundAlpha(1f);
        }
    }
}
