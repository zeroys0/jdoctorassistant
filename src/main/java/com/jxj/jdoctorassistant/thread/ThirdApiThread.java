package com.jxj.jdoctorassistant.thread;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class ThirdApiThread extends Thread{
	private String key;
	private String url;
	private String args;
	
	private Handler handler;
	private Context context;
	private String result;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setArgs(String args) {
		this.args = args;
	}
	public ThirdApiThread(Handler handler, Context context) {
		super();
		this.handler = handler;
		this.context = context;
	}
	
	public String getResult() {
		return result;
	}
	
//	public static String request(String mUrl,String mArgs){
//		BufferedReader reader=null;
//		String mResult=null;
//		StringBuffer sbf=new StringBuffer();
//		mUrl=mUrl+"?"+mArgs;
//
//		try{
//			URL url=new URL(mUrl);
//			HttpURLConnection connection=(HttpURLConnection) url.openConnection();
//			connection.setRequestMethod("GET");
//			InputStream is=connection.getInputStream();
//			reader=new BufferedReader(new InputStreamReader(is,"UTF-8"));
//			String read=null;
//			while((read=reader.readLine())!=null){
//				sbf.append(read);
//				sbf.append("\r\n");
//			}
//			reader.close();
//			mResult=sbf.toString();
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		return mResult;
//
//	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		BufferedReader reader=null;
		StringBuffer sbf=new StringBuffer();
		String mUrl=url+"?"+args;
		System.out.println("天狗云访问接口: "+mUrl);
		try{
			URL url=new URL(mUrl);
			HttpURLConnection connection=(HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			InputStream is=connection.getInputStream();
			reader=new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String read=null;
			while((read=reader.readLine())!=null){
				sbf.append(read);
				sbf.append("\r\n");
			}
			reader.close();
			result=sbf.toString();
			
		}catch(Exception e){
			e.printStackTrace();
			result=ApiConstant.NE;
		}
		
		System.out.println("天狗云数据："+result);
		Message message=new Message();
		message.what= ApiConstant.MSG_API_HANDLER;
		handler.sendMessage(message);
	}
	
	

}
