package com.jxj.jdoctorassistant.main.thirdData;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.thirdData.ThirdDataBfAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentThirdDataBf extends Fragment {
    @Bind(R.id.third_data_bf_lv)
    ListView lv;

    @Bind(R.id.bf_age_tv)
    TextView ageTv;
    @Bind(R.id.bf_height_tv)
    TextView heightTv;
    @Bind(R.id.bf_sex_tv)
    TextView sexTv;

    private JAssistantAPIThread getThirdDataThread;
    private Context context;
    private ThirdDataBfAdapter adapter;
    private int customerId;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_third_data_bf, container, false);
        ButterKnife.bind(this,view);

        context=getActivity();

        adapter=new ThirdDataBfAdapter(context);
        lv.setAdapter(adapter);

        Bundle bundle=getArguments();
        customerId=Integer.parseInt(bundle.getString(ToolsThirdDataActivity.CUSTOMERID));
        getThirdData(3);

        return view;
    }

    private void getThirdData(final int type){


        Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what== ApiConstant.MSG_API_HANDLER){
                    String result=getThirdDataThread.getResult();
                    if(UiUtil.isResultSuccess(context,result)){
                        JSONObject json=JSONObject.fromObject(result);
                        int code=json.getInt("code");
                        if(code==200){
                            JSONArray array=json.getJSONArray("DataList");

                            if(array.size()>0){
                                JSONObject jsonObject=array.getJSONObject(0);
                                ageTv.setText(String.valueOf(jsonObject.getInt("Age")));
                                heightTv.setText(String.valueOf(jsonObject.getInt("Height")));
                                sexTv.setText(jsonObject.getInt("Gender")==0?
                                        getResources().getString(R.string.female):
                                        getResources().getString(R.string.male));

                            }
                            adapter.setArray(array);
                            adapter.notifyDataSetChanged();

                        }

                    }
                }
            }
        };

        getThirdDataThread=new JAssistantAPIThread(ApiConstant.GETTHIRDPARTYDATA,handler,context);
        getThirdDataThread.setCustomerId(String.valueOf(customerId));
        getThirdDataThread.setPageIndex(0);
        getThirdDataThread.setPageSize(10);
        getThirdDataThread.setType(type+1);
        getThirdDataThread.start();

    }
}
