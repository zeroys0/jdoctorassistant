package com.jxj.jdoctorassistant.thread;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;

public class DownloadPhotoThread extends Thread {

	String url = null;
	String IprecareAPI = null;
	String key = null;
	String methodName = ApiConstant.DOWNLOADPHOTO;
	String result = null;

	private Handler handler;
	private Context context;

	private String fileName;

	private SharedPreferences sp;

	public DownloadPhotoThread(Context context) {
		super();
		this.context = context;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void run() {
		sp = context.getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
		url=sp.getString(AppConstant.USER_url,"error_app_url");
		key=sp.getString(AppConstant.USER_key, "error_app_key");
		String api=sp.getString(AppConstant.USER_api, "error_app_api");
		IprecareAPI="http://"+api+"/iprecareapi.asmx";
		String str = url + "/" + methodName;
//		key = ApiConstant.KEY;
		SoapObject localSoapObject = new SoapObject(url, methodName);
		localSoapObject.addProperty("key", key);
		localSoapObject.addProperty("fileName",fileName);

		System.out.println("iprecareWebService  downloadphoto  localSoapObject:"
				+ localSoapObject.toString());
		SoapSerializationEnvelope localSoapSerializationEnvelope = new SoapSerializationEnvelope(
				110);
		localSoapSerializationEnvelope.dotNet = true;
		localSoapSerializationEnvelope.bodyOut = localSoapObject;
		localSoapSerializationEnvelope.setOutputSoapObject(localSoapObject);
		HttpTransportSE localHttpTransportSE = new HttpTransportSE(IprecareAPI,
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
		msg.what = 0x133;
		handler.sendMessage(msg);
	}

	public String getResult() {
//		System.out.println("iprecare 接口： "+methodName + "_result:" + result+" filename: "+fileName);
		return result;
	}
}
