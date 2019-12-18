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

public class IprecareThread extends Thread {

	String url = null;
	String IprecareAPI = null;
	String key = null;
	String methodName = null;
	String result = null;

	private Handler handler;
	private Context context;

	private String phone;
	private String imgToken;
	private String imgCode;

	private String fileName;

	private SharedPreferences sp;

	public IprecareThread(String methodName, Handler handler,
						  Context context) {
		super();
		this.methodName = methodName;
		this.handler = handler;
		this.context = context;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setImgToken(String imgToken) {
		this.imgToken = imgToken;
	}

	public void setImgCode(String imgCode) {
		this.imgCode = imgCode;
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
		IprecareAPI="http://"+api+"/iPreCareAPI.asmx";
		String str = url + "/" + methodName;
//		key = ApiConstant.KEY;
		SoapObject localSoapObject = new SoapObject(url, methodName);
		localSoapObject.addProperty("key", key);
		// 获取验证码图片
		if (methodName.equals(ApiConstant.GETIMAGECALIDATE)) {
		}
		else if(methodName.equals(ApiConstant.DOWNLOADPHOTO)){
			localSoapObject.addProperty("fileName",fileName);
		}
		// 发送验证码
		else if (methodName.equals(ApiConstant.SENDAUTHCODE)) {
			localSoapObject.addProperty("phone", phone);
			localSoapObject.addProperty("imgToken", imgToken);
			localSoapObject.addProperty("imgCode", imgCode);
		}

		System.out.println("iprecareWebService  localSoapObject:"
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
		System.out.println("iprecare 接口： "+methodName + "_result:" + result+" filename: "+fileName);
		return result;
	}
}
