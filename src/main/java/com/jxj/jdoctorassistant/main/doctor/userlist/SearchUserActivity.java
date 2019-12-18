package com.jxj.jdoctorassistant.main.doctor.userlist;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.doctor.DoctorUserListAdapter;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.SearchListAdapter;
import com.jxj.jdoctorassistant.adapter.doctor.userlist.SearchUserAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.model.SearchWord;
import com.jxj.jdoctorassistant.thread.DeleteSqlThread;
import com.jxj.jdoctorassistant.thread.DoctorSHThread;
import com.jxj.jdoctorassistant.thread.InsertSqlThread;
import com.jxj.jdoctorassistant.thread.QuerySqlThread;
import com.jxj.jdoctorassistant.util.DatabaseHelper;
import com.jxj.jdoctorassistant.util.GetDate;
import com.jxj.jdoctorassistant.util.UiUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public class SearchUserActivity extends Activity implements SearchListAdapter.Delegete {
    @ViewInject(R.id.search_user_etv)
    private EditText etv;
//    @ViewInject(R.id.search_user_btn)
//    private Button btn;
    @ViewInject(R.id.search_user_lv)
    ListView userLv;

//    @ViewInject(R.id.auto)
//    private AutoCompleteTextView auto;

    @OnClick({R.id.back_igv,R.id.search_user_btn})
    void onClick(View view){
        switch (view.getId()){
            case R.id.back_igv:
                finish();
//                showAccountPop();
                break;
            case R.id.search_user_btn:
                if(TextUtils.isEmpty(etv.getEditableText().toString().toString())){
                    UiUtil.showToast(context,getResources().getString(R.string.search_user_hint));
                    return;
                }
                saveWord();
                getCustomerList();
                break;
            default:
                break;
        }
    }

    private Context context;
    private SearchUserAdapter adapter;
    private DoctorUserListAdapter userListAdapter;
    private JSONArray array;
    private DoctorSHThread getCustomerListThread;
//    private DoctorUserListAdapter adapter;
    private SharedPreferences sp;
    private int doctorId;

    private DatabaseHelper databasehelper;
//    private SQLiteDatabase db;
    private PopupWindow window;
    private SearchListAdapter searchAdapter;
    private List<SearchWord> searchWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_user);

        ViewUtils.inject(this);
        context=this;

        sp=getSharedPreferences(AppConstant.USER_sp_name,MODE_PRIVATE);
        doctorId=sp.getInt(AppConstant.USER_doctor_id,0);

        databasehelper = new DatabaseHelper(context);
//        db = databasehelper.getWritableDatabase();

//        adapter=new SearchUserAdapter(context);
        userListAdapter=new DoctorUserListAdapter(context);
        userLv.setAdapter(userListAdapter);
        userLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                JSONObject object=array.getJSONObject(position);
                String customerId=object.getString("CustomerId");
                sp.edit().putString(AppConstant.USER_customerInfo, object.toString()).commit();
                sp.edit().putString(AppConstant.USER_customerId,customerId).commit();
                startActivity(new Intent(context, PatientInfoActivity.class));
            }
        });

//        QuerySqlThread queryThread = new QuerySqlThread(context);
//        queryThread.start();
//        try {
//            queryThread.join();
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        List<SearchWord> list=queryThread.getList();
//    String[] histories=new String[list.size()];
//        for(int i=0;i<list.size();i++){
//            histories[i]=list.get(i).getSearchWord();
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, histories);
//        auto.setAdapter(userListAdapter);
//        auto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                AutoCompleteTextView view = (AutoCompleteTextView) v;
//
//                if (hasFocus) {
//
//                    view.showDropDown();
//
//                }
//            }
//        });
//        try {
//            Thread.sleep(1000);
//            showAccountPop();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        View.OnFocusChangeListener mFocusChangedListener;
//        mFocusChangedListener = new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    Toast.makeText(getApplicationContext(), "got focus: " + v.toString(), Toast.LENGTH_LONG).show();
//                    showAccountPop();
//                }else {
//                    Toast.makeText(getApplicationContext(), "lost focus: " + v.toString(), Toast.LENGTH_LONG).show();
//                }
//            }
//        };
//        TextWatcher textWatcher=new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                UiUtil.showToast(context,"beforeTextChanged");
//                showAccountPop();
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                UiUtil.showToast(context,"onTextChanged");
//                showAccountPop();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
////                UiUtil.showToast(context,"afterTextChanged");
////                showAccountPop();
//            }
//        };
//        etv.addTextChangedListener(textWatcher);
        etv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showAccountPop();
                }
//
//                if (hasFocus) {
//
//                    view.showDropDown();
//
//                }
            }
        });

    }

    @Override
    public void delete(String searchWord) {
        DeleteSqlThread thread = new DeleteSqlThread(searchWord, context);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        QuerySqlThread queryThread = new QuerySqlThread(context);
        queryThread.start();
        try {
            queryThread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        searchAdapter.setUserbean(queryThread.getList());
        searchAdapter.notifyDataSetChanged();
    }

    void saveWord(){
        SearchWord userbean = new SearchWord();
        userbean.setSearchWord(etv.getEditableText().toString().trim());
        userbean.setDate(GetDate.currentDate());
        userbean.setCount(0);
        InsertSqlThread thread = new InsertSqlThread(
                userbean, context);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void showAccountPop() {

        // 利用layoutInflater获得View
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_pop_search_history, null);
        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()
        window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, true);
        window.setBackgroundDrawable(new PaintDrawable(0));
        QuerySqlThread thread = new QuerySqlThread(context);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        searchWords = thread.getList();
        ImageView deleteIgv=(ImageView) view.findViewById(R.id.delete_igv);
        deleteIgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
                QuerySqlThread thread = new QuerySqlThread(context);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                searchWords =thread.getList();
                searchAdapter.setUserbean(searchWords);
                searchAdapter.notifyDataSetChanged();
            }
        });
        ListView listview = (ListView) view.findViewById(R.id.pop_account_lv);
        searchAdapter= new SearchListAdapter(searchWords, context);
        searchAdapter.setDelegete(this);
        listview.setAdapter(searchAdapter);
        window.setFocusable(true);
        window.showAsDropDown(etv);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                SearchWord user = searchWords.get(position);
                etv.setText(user.getSearchWord());

                window.dismiss();

            }
        });

        // 监听popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {

                searchAdapter.setDelegete(null);
            }
        });

    }

    void deleteAll(){
        DeleteSqlThread thread = new DeleteSqlThread("", context);
        thread.start();
        try {
            thread.join();

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void getCustomerList(){
        String str=etv.getEditableText().toString().trim();
        if(str.length()<1){
            UiUtil.showToast(context,getResources().getString(R.string.search_user_hint));
            return;
        }
        array=null;

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = getCustomerListThread.getResult();
                Log.d("test","获取签约用户列表 result："+result);
                if(UiUtil.isResultSuccess(context,result)){
                    JSONObject jsonObject=JSONObject.fromObject(result);
                    int code=jsonObject.getInt("code");
                    if(code==200){
                        array=jsonObject.getJSONArray("Data");
                    }else {
                        UiUtil.showToast(context,jsonObject.getString("message"));
                    }
                }
                userListAdapter.setArray(array);
                userListAdapter.notifyDataSetChanged();

            }
        };
        getCustomerListThread = new DoctorSHThread(
                ApiConstant.GETALLCUSTOMER, handler, context);
        getCustomerListThread.setDoctorId(doctorId);
        getCustomerListThread.setCategory(0);
        getCustomerListThread.setCondition(str);
        getCustomerListThread.setStartDate("");
        getCustomerListThread.setEndDate("");
        getCustomerListThread.start();
    }


}
