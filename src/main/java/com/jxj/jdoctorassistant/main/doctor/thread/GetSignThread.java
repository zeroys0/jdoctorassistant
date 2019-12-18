package com.jxj.jdoctorassistant.main.doctor.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.MyApplication;
import com.jxj.jdoctorassistant.bean.Urls;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;

//获取签约记录
public class GetSignThread extends Thread {
    String url;

    String SHDoctorAPI = null;
    String methodName = null;
    Handler handler;
    Context context;
    String result = null;
    int doctor_id;
    int pageIndex = 0;
    int pageSize = 10;
    int key;

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public GetSignThread(String methodName, Handler handler,
                         Context context) {
        super();
        this.methodName = methodName;
        this.handler = handler;
        this.context = context;
    }

    public void setDoctor_id(int id) {
        this.doctor_id = id;
    }
    public void run(){
        url= Urls.URL;
        SHDoctorAPI=Urls.API;
        String str = url + "/" + methodName;
        SoapObject localSoapObject = new SoapObject(url, methodName);

        localSoapObject.addProperty("key", MyApplication.key);
        localSoapObject.addProperty("doctorId", doctor_id);
        localSoapObject.addProperty("pageIndex", pageIndex);
        localSoapObject.addProperty("pageSize", pageSize);

        SoapSerializationEnvelope localSoapSerializationEnvelope = new SoapSerializationEnvelope(
                110);
        localSoapSerializationEnvelope.dotNet = true;
        localSoapSerializationEnvelope.bodyOut = localSoapObject;
        localSoapSerializationEnvelope.setOutputSoapObject(localSoapObject);
        HttpTransportSE localHttpTransportSE = new HttpTransportSE(SHDoctorAPI,
                3000);
        try {
            localHttpTransportSE.call(str, localSoapSerializationEnvelope);
            result = ((SoapObject) localSoapSerializationEnvelope.bodyIn)
                    .getProperty(0).toString();
        } catch (SocketTimeoutException socketTimeoutException) {
            socketTimeoutException.printStackTrace();
            result = ApiConstant.NE;
        } catch (Exception exception) {
            exception.printStackTrace();
            result = ApiConstant.OE;
        }

        Message msg = new Message();
        msg.what = ApiConstant.MSG_API_HANDLER;
        handler.sendMessage(msg);
    }

    public String getResult() {
        System.out.println("随护助手 医生模块 接口 "+methodName + "_result:" + result);
        return result;
    }
}
