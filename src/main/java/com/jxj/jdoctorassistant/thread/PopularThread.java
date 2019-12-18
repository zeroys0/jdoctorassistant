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

public class PopularThread extends Thread{

	private String  url=null;
	private String PopularAPi =null;

	String key=null;
	String result=null;
	String methodName = null;

	Handler handler;
	Context context;

	private String account;
	private String password;
	private String image;
	private int used;

	private String province;
	private String city;

	private String phone;
	private String imgToken;
	private int imgCode;

	private SharedPreferences sp;

	public PopularThread(String methodName,Handler handler,Context context) {
		super();
		this.methodName=methodName;
		this.context=context;
		this.handler=handler;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setImgCode(int imgCode) {
		this.imgCode = imgCode;
	}

	public void setKey(String imgToken) {
		this.imgToken = imgToken;
	}
	public void setUsed(int user){
		this.used = used;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		sp = context.getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
//		url=sp.getString(AppConstant.USER_url,"error_app_url");
//		key=sp.getString(AppConstant.USER_key, "error_app_key");
		url="http://www.iprecare.com";
		key="58f1d615-ed3c-457a-98fe-320dcdf08b74";
//		String api=sp.getString(AppConstant.USER_api, "error_app_api");
		PopularAPi="http://122.225.60.118:6280/PartnersAPI/V3/SHDoctorAPI.asmx";

		String str = url + "/" + methodName;
//		key = ApiConstant.KEY;
		SoapObject localSoapObject = new SoapObject(url, methodName);
		localSoapObject.addProperty("key", key);
		//环信相关
		if(methodName.equals(ApiConstant.ADDEASEMOBACCOUNT)||
				methodName.equals(ApiConstant.EDITEASEMOBPASSWORD)){
			localSoapObject.addProperty("account",account);
			localSoapObject.addProperty("password",password);
		}
		else if(methodName.equals(ApiConstant.GETEASEMOBPASSWORD)){
			localSoapObject.addProperty("account",account);
		}
		else if(methodName.equals(ApiConstant.DOWNLOADIMAGE)||
				methodName.equals(ApiConstant.UPLOADIMAGE)){
			localSoapObject.addProperty("image",image);
		}
		else if(methodName.equals(ApiConstant.GETCITYLIST)){
			localSoapObject.addProperty("province",province);
		}
		else if(methodName.equals(ApiConstant.GETDISTRICTLIST)){
			localSoapObject.addProperty("province",province);
			localSoapObject.addProperty("city",city);
		}
		else if(methodName.equals(ApiConstant.GETPROVINCELIST)||
				 methodName.endsWith(ApiConstant.GETIMAGECALIDATE)){
		}
		else if(methodName.equals(ApiConstant.SENDAUTHCODE)){
			localSoapObject.addProperty("phone",phone);
			localSoapObject.addProperty("key",imgToken);
			localSoapObject.addProperty("used",used);
		}


		System.out.println("PopularWebService  localSoapObject:"
				+ localSoapObject.toString());
		SoapSerializationEnvelope localSoapSerializationEnvelope = new SoapSerializationEnvelope(
				110);
		localSoapSerializationEnvelope.dotNet = true;
		localSoapSerializationEnvelope.bodyOut = localSoapObject;
		localSoapSerializationEnvelope.setOutputSoapObject(localSoapObject);
		HttpTransportSE localHttpTransportSE = new HttpTransportSE(PopularAPi,
				3000);
		try
	    {
		    localHttpTransportSE.call(str, localSoapSerializationEnvelope);
		    result=((SoapObject)localSoapSerializationEnvelope.bodyIn).getProperty(0).toString();
	    }
		catch (SocketTimeoutException socketTimeoutException)
	    {
	    	socketTimeoutException.printStackTrace();
	    	result=ApiConstant.NE;
	    }
	    catch (Exception localException)
	    {
	    	localException.printStackTrace();
	    	result=ApiConstant.OE;
	    }
		
		Message msg=new Message();
		msg.what=ApiConstant.MSG_API_HANDLER;
		handler.sendMessage(msg);
	
	}
	public String getResult() {
		System.out.println("随护助手 通用 接口 "+methodName + "_result:" + result);
		return result;
	}
	
}
