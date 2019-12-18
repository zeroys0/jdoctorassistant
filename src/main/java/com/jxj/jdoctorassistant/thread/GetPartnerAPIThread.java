package com.jxj.jdoctorassistant.thread;

import android.os.Handler;
import android.os.Message;

import com.jxj.jdoctorassistant.app.ApiConstant;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;

public class GetPartnerAPIThread extends Thread{

	String url="http://www.iprecare.com";
//	String urlPartnerServiceAPI="http://www.iprecare.com:8083/Partner.asmx";
	String urlPartnerServiceAPI="http://api.iprecare.com:8083/Partner.asmx";
	String partnerCode;
	String result=null;
	Handler handler;
	public GetPartnerAPIThread(String partnerCode, Handler handler) {
		super();
		this.partnerCode=partnerCode;
		this.handler=handler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

		try
	    {
			String str = url + "/" +"GetPartnerAPIURL";
		    SoapObject localSoapObject = new SoapObject(url,"GetPartnerAPIURL");
		   
		    localSoapObject.addProperty("partnerCode",partnerCode);
		    
		    System.out.println("PartnerAPIService  localSoapObject:"
					+ localSoapObject.toString());
  
		    SoapSerializationEnvelope localSoapSerializationEnvelope = new SoapSerializationEnvelope(110);
		    localSoapSerializationEnvelope.dotNet = true;
		    localSoapSerializationEnvelope.bodyOut = localSoapObject;
		    localSoapSerializationEnvelope.setOutputSoapObject(localSoapObject);
		    HttpTransportSE localHttpTransportSE = new HttpTransportSE(urlPartnerServiceAPI,5000);
		    
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
	    	result= ApiConstant.OE;
	    }
		System.out.println("系统编码返回结果："+result);
		
		Message msg=new Message();
		msg.what=0x135;
		handler.sendMessage(msg);
	
	}
	public String getResult() {
		return result;
	}
	


}
