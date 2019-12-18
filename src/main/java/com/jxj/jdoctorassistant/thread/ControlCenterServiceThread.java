package com.jxj.jdoctorassistant.thread;

import java.net.SocketTimeoutException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

public class ControlCenterServiceThread extends Thread {
	private SharedPreferences sp;

	String url ;
	String urlControlServiceAPI = null;
	String key = null;
	int userId;
	String customerId;
	String action;
	String result = null;
	String methodName;
	String msg;
	Handler handler;
	Context context;

	public ControlCenterServiceThread(Context context, String customerId,
			String action, Handler handler) {
		super();
		this.context = context;
		this.customerId = customerId;
		this.action = action;
		this.handler = handler;
		this.methodName=ApiConstant.SENDCMD;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public void run() {
		sp = context.getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
		url=sp.getString(AppConstant.USER_url,"error_app_url");
		key=sp.getString(AppConstant.USER_key, "error_app_key");
		String api=sp.getString(AppConstant.USER_api, "error_app_api");
		urlControlServiceAPI="http://"+api+"/ControlCenter.asmx";

		try {
			String str = url + "/" + methodName;
			SoapObject localSoapObject = new SoapObject(url, methodName);
			localSoapObject.addProperty("key", key);
			localSoapObject.addProperty("userId", userId);
			if (methodName.equals(ApiConstant.SENDCMD)) {
				localSoapObject.addProperty("customerId", customerId);
				localSoapObject.addProperty("action", action);
			} else if (methodName.equals(ApiConstant.SENDMSG)) {
				localSoapObject.addProperty("customerId", customerId);
				localSoapObject.addProperty("msg", msg);
			}
			System.out.println("ControlCenterServiceThread localSoapObject:" + localSoapObject.toString());
			SoapSerializationEnvelope localSoapSerializationEnvelope = new SoapSerializationEnvelope(
					110);
			localSoapSerializationEnvelope.dotNet = true;
			localSoapSerializationEnvelope.bodyOut = localSoapObject;
			localSoapSerializationEnvelope.setOutputSoapObject(localSoapObject);
			HttpTransportSE localHttpTransportSE = new HttpTransportSE(
					urlControlServiceAPI, 5000);

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
		System.out.println("控制接口返回结果：" + result);

		Message msg = new Message();
		msg.what = ApiConstant.MSG_SEND_API_HANDLER;
		handler.sendMessage(msg);

	}

	public String getResult() {
		return result;
	}

}
