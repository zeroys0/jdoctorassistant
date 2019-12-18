package com.jxj.jdoctorassistant.thread;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;


public class DoctorLoginThread extends Thread {

	private SharedPreferences sp;
	// 必选参数

	private Context context;
	private String url;
	private String urlJdoctorAssistantAPI ;
	private String key;
	private String methodName;
	private Handler handler;
	private String result = null;
	private String account;
	private String password;


	public DoctorLoginThread(String methodName, Handler handler,
                             Context context) {
		super();
		this.methodName = methodName;
		this.handler = handler;
		this.context = context;
	}


	public void setAccount(String account) {
		this.account = account;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void run() {

		sp = context.getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
		url=sp.getString(AppConstant.USER_url,"error_app_url");
		key=sp.getString(AppConstant.USER_key, "error_app_key");
		String api=sp.getString(AppConstant.USER_api, "error_app_api");
		urlJdoctorAssistantAPI="http://"+api+"/JAssistantAPI.asmx";
		methodName=ApiConstant.DOCTORLOGIN;
		String str = url + "/" + methodName;
		SoapObject localSoapObject = new SoapObject(url, methodName);
		localSoapObject.addProperty("key", key);
		localSoapObject.addProperty("account", account);
		localSoapObject.addProperty("pwd", password);

		System.out.println("JAssistantAPIThread 医生登录 localSoapObject:"
				+ localSoapObject.toString());

		SoapSerializationEnvelope localSoapSerializationEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		localSoapSerializationEnvelope.dotNet = true;
		localSoapSerializationEnvelope.bodyOut = localSoapObject;
		localSoapSerializationEnvelope.setOutputSoapObject(localSoapObject);
		HttpTransportSE localHttpTransportSE = new HttpTransportSE(
				urlJdoctorAssistantAPI, 3000);

		try {
			localHttpTransportSE.call(str, localSoapSerializationEnvelope);
			result = ((SoapObject) localSoapSerializationEnvelope.bodyIn)
					.getProperty(0).toString();
		} catch (SocketTimeoutException socketTimeoutException) {
			socketTimeoutException.printStackTrace();
			result = ApiConstant.NE;
		} catch (Exception localException) {
			localException.printStackTrace();
			result = ApiConstant.OE;
		}

		Message msg = new Message();
		msg.what = ApiConstant.MSG_API_HANDLER;
		handler.sendMessage(msg);
	}

	public String getResult() {
		return result;

	}

}
