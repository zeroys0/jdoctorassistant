package com.jxj.jdoctorassistant.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.EasyConstant;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.util.List;

public class EasyThread extends Thread{

//	private static final String  url="http://www.iprecare.com";
//	private static final String urlApi="http://api.iprecare.com:6280/JAssistantAPI.asmx?op=CheckAppVersionForJAssistant";
//	private static final String key="e8f0ba48-8599-456c-80cf-5c9fa2cbdddb";
//
//	String result=null;
	Handler handler;
	Context context;

	String methodName;
	String contactName;//添加好友名字

	private boolean isSuccess;

	private List<String> userList;

	public EasyThread(Context context, Handler handler,String methodName) {
		super();
		this.context=context;
		this.handler=handler;
		this.methodName=methodName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			switch (methodName){
				case EasyConstant.ADDCONTACT:
					EMClient.getInstance().contactManager().addContact(contactName,"测试");
					isSuccess=true;
					break;
				case EasyConstant.GETCONTACT:
//					userList= EMClient.getInstance().contactManager().getAllContactsFromServer();
//					Log.e("环信信息：","好友数量："+userList.size());
					isSuccess=true;
					break;
				default:

					break;

			}



		}catch (HyphenateException e){
			e.printStackTrace();
			Log.e("错误信息:" ,"方法："+methodName+"  "+e.getMessage()+" 错误码："+e.getErrorCode());
			isSuccess=false;
		}

		Message msg = new Message();
		msg.what = ApiConstant.MSG_EASY_HANDLER;
		handler.sendMessage(msg);

	
	}

	public List<String> getUserList() {
		return userList;
	}

	public boolean isSuccess() {
		return isSuccess;
	}
}
