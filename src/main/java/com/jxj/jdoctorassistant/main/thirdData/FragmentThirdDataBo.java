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
import butterknife.Bind;
import butterknife.ButterKnife;

import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.adapter.thirdData.ThirdDataBoAdapter;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;
import com.jxj.jdoctorassistant.thread.JAssistantAPIThread;
import com.jxj.jdoctorassistant.util.UiUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentThirdDataBo extends Fragment {
    @Bind(R.id.third_data_bo_lv)
    ListView lv;

    private JAssistantAPIThread getThirdDataThread;
    private ThirdDataBoAdapter adapter;
    private Context context;
    private int customerId;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_third_data_bo, container, false);
        ButterKnife.bind(this,view);

        context=getActivity();

        adapter=new ThirdDataBoAdapter(context);
        lv.setAdapter(adapter);

        Bundle bundle=getArguments();
        customerId=Integer.parseInt(bundle.getString(ToolsThirdDataActivity.CUSTOMERID));
        getThirdData(1);

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
