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

public class CommunityAssessThread extends Thread {

	String url = null;
	String SHDoctorAPI = null;
	String key = null;
	String methodName = null;
	String result = null;

	private Handler handler;
	private Context context;

	private String applicant;//申请人信息
	private int cid;//申请人id
	private int uid;//评估员id
	private int pingguId;
	private String terms;

	private String addDate;
	private int orderID;

	private int assessType;//评估员类型
	private int applicantId;//申请人id
	private String name;//姓名
	private String cardId;//身份证号
	private int item1;//评估结论
	private int communityId;//社区id
	private int pageIndex;//当前页码
	private int pageSize;//当前页大小
	private int parentId;//目录父节点id
	private int assessID;//评论编号
	private String keyString;//查询关键字
	private String startTime;//开始日期时间 yyyy-MM-dd
	private String endTime;//结束日期时间
	private int type;//评估类型
	private String padl;//生活自理能力评估数据
	private String pca;//认知能力评估数据
	private String peb;//情绪行为数据
	private String report;//评估报告数据
	private String psciallife;//社会生活环境及患病情况数据
	private String pvisual;//视觉能力数据

	private int level;

	private SharedPreferences sp;

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public void setAssessType(int assessType) {
		this.assessType = assessType;
	}

	public void setApplicantId(int applicantId) {
		this.applicantId = applicantId;
	}

	public void setNamee(String name) {
		this.name = name;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public void setItem1(int item1) {
		this.item1 = item1;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public void setAssessID(int assessID) {
		this.assessID = assessID;
	}

	public void setKeyString(String keyString) {
		this.keyString = keyString;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setPadl(String padl) {
		this.padl = padl;
	}

	public void setPca(String pca) {
		this.pca = pca;
	}

	public void setPeb(String peb) {
		this.peb = peb;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public void setPsciallife(String psciallife) {
		this.psciallife = psciallife;
	}

	public void setPvisual(String pvisual) {
		this.pvisual = pvisual;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}

	public void setPingguId(int pingguId) {
		this.pingguId = pingguId;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public CommunityAssessThread(String methodName, Handler handler,
								 Context context) {
		super();
		this.methodName = methodName;
		this.handler = handler;
		this.context = context;
	}




	public void run() {
		sp = context.getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
		url=sp.getString(AppConstant.USER_url,"error_app_url");
		key=sp.getString(AppConstant.USER_key, "error_app_key");
		String api=sp.getString(AppConstant.USER_api, "error_app_api");
		SHDoctorAPI="http://"+api+"/CommunityAssessAPI.asmx";

		String str = url + "/" + methodName;
//		key = ApiConstant.KEY;
		SoapObject localSoapObject = new SoapObject(url, methodName);
		localSoapObject.addProperty("key", key);
		if(methodName.equals(ApiConstant.ADDAPPLICANT)){
			localSoapObject.addProperty("applicant",applicant);
		}
		else if(methodName.equals(ApiConstant.ADDPASSESS)){
			localSoapObject.addProperty("cid",cid);
			localSoapObject.addProperty("uid",uid);
			localSoapObject.addProperty("assessType",assessType);
		}
		else if(methodName.equals(ApiConstant.GETAPPLICANTBYID)){
			localSoapObject.addProperty("applicantId",applicantId);
		}
		else if(methodName.equals(ApiConstant.GETAPPLICANTLIST)){
			localSoapObject.addProperty("name",name);
			localSoapObject.addProperty("cardId",cardId);
			localSoapObject.addProperty("item1",item1);
			localSoapObject.addProperty("communityId",communityId);
			localSoapObject.addProperty("pageIndex",pageIndex);
			localSoapObject.addProperty("pageSize",pageSize);
		}
		else if(methodName.equals(ApiConstant.GETCOMMUNITYLIST)){
			localSoapObject.addProperty("parentId",parentId);
		}
		else if(methodName.equals(ApiConstant.GETPADL)){
			localSoapObject.addProperty("assessID",assessID);
		}
		else if(methodName.equals(ApiConstant.GETPASSESSLIST)){
			localSoapObject.addProperty("keyString",keyString);
			localSoapObject.addProperty("startTime",startTime);
			localSoapObject.addProperty("endTime",endTime);
			localSoapObject.addProperty("type",type);
			localSoapObject.addProperty("pageIndex",pageIndex);
			localSoapObject.addProperty("pageSize",pageSize);
		}

		//
		else if(methodName.equals(ApiConstant.GETPASSESSLISTBYID)){
			localSoapObject.addProperty("cid",cid);
			localSoapObject.addProperty("pageIndex",pageIndex);
			localSoapObject.addProperty("pageSize",pageSize);
		}
		else if(methodName.equals(ApiConstant.GETAPPOINTMENTLIST)){
			localSoapObject.addProperty("uid",uid);
			localSoapObject.addProperty("terms",terms);
			localSoapObject.addProperty("pageIndex",pageIndex);
			localSoapObject.addProperty("pageSize",pageSize);
		}


		else if(methodName.equals(ApiConstant.GETPCA)||
				methodName.equals(ApiConstant.GETPEB)||
				methodName.equals(ApiConstant.GETPREPORT)||
				methodName.equals(ApiConstant.GETPSCIALLIFE)||
				methodName.equals(ApiConstant.GETPVISUAL)){
			localSoapObject.addProperty("assessID",assessID);
		}

		else if(methodName.equals(ApiConstant.ADDAPPOINTMENT)){
			localSoapObject.addProperty("uid",uid);
			localSoapObject.addProperty("cid",cid);
			localSoapObject.addProperty("appDate",addDate);

		}

		else if(methodName.equals(ApiConstant.EDITAPPOINTMENT)){
			localSoapObject.addProperty("id",pingguId);
			localSoapObject.addProperty("appDate",addDate);

		}

		else if(methodName.equals(ApiConstant.DELETEAPPOINTMENT)){
			localSoapObject.addProperty("id",orderID);

		}

//		else if(methodName.equals(ApiConstant.GETPEB)){
//			localSoapObject.addProperty("assessID",assessID);
//		}
//		else if(methodName.equals(ApiConstant.GETPREPORT)){
//			localSoapObject.addProperty("assessID",assessID);
//		}
//		else if(methodName.equals(ApiConstant.GETPSCIALLIFE)){
//			localSoapObject.addProperty("assessID",assessID);
//		}
//		else if(methodName.equals(ApiConstant.GETPVISUAL)){
//			localSoapObject.addProperty("assessID",assessID);
//		}
		else if(methodName.equals(ApiConstant.UPDATEAPPLICANT)){
			localSoapObject.addProperty("applicant",applicant);
		}
		else if(methodName.equals(ApiConstant.UPDATEPADL)){
			localSoapObject.addProperty("padl",padl);
		}
		else if(methodName.equals(ApiConstant.UPDATEPCA)){
			localSoapObject.addProperty("pca",pca);
		}
		else if(methodName.equals(ApiConstant.UPDATEPEB)){
			localSoapObject.addProperty("peb",peb);
		}
		else if(methodName.equals(ApiConstant.UPDATEPREPORT)){
			localSoapObject.addProperty("report",report);
		}
		else if(methodName.equals(ApiConstant.UPDATEPSCIALLIFE)){
			localSoapObject.addProperty("psciallife",psciallife);
		}
		else if(methodName.equals(ApiConstant.UPDATEPVISUAL)){
			localSoapObject.addProperty("pvisual",pvisual);
		}
		else if(methodName.equals(ApiConstant.GETCOMMUNITYLISTBYLEVEL)){
			localSoapObject.addProperty("level",level);
		}
		System.out.println("CommunityAssessWebService  localSoapObject:"+ localSoapObject.toString());

		SoapSerializationEnvelope localSoapSerializationEnvelope = new SoapSerializationEnvelope(110);
		localSoapSerializationEnvelope.dotNet = true;
		localSoapSerializationEnvelope.bodyOut = localSoapObject;
		localSoapSerializationEnvelope.setOutputSoapObject(localSoapObject);
		HttpTransportSE localHttpTransportSE = new HttpTransportSE(SHDoctorAPI,3000);


		try {
			localHttpTransportSE.call(str, localSoapSerializationEnvelope);
			result = ((SoapObject) localSoapSerializationEnvelope.bodyIn)					.getProperty(0).toString();
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
