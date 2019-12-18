package com.jxj.jdoctorassistant.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.jxj.jdoctorassistant.app.ApiConstant;


import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;

public class UpdateVersionThread extends Thread{

	private static final String  url="http://www.iprecare.com";
	private static final String urlApi="http://api.iprecare.com:6280/JAssistantAPI.asmx?op=CheckAppVersionForJAssistant";
	private static final String key="e8f0ba48-8599-456c-80cf-5c9fa2cbdddb";

	String result=null;
	Handler handler;
	Context context;

	public UpdateVersionThread(Context context, Handler handler) {
		super();
		this.context=context;
		this.handler=handler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub


		try
	    {
			String str = url + "/" + ApiConstant.CHECKAPPVERSION;
		    SoapObject localSoapObject = new SoapObject(url,ApiConstant.CHECKAPPVERSION);
		    localSoapObject.addProperty("key",key);
		    SoapSerializationEnvelope localSoapSerializationEnvelope = new SoapSerializationEnvelope(110);
		    localSoapSerializationEnvelope.dotNet = true;
		    localSoapSerializationEnvelope.bodyOut = localSoapObject;
		    localSoapSerializationEnvelope.setOutputSoapObject(localSoapObject);
		    HttpTransportSE localHttpTransportSE = new HttpTransportSE(urlApi,5000);
		    
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
		msg.what=0x136;
		handler.sendMessage(msg);
	
	}
	public String getResult() {
		return result;
	}
	
}
