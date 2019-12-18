package com.jxj.jdoctorassistant.thread;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;

public class DoctorSHThread extends Thread {

	String url = null;
	String SHDoctorAPI = null;
	String key = null;
	String methodName = null;
	String result = null;

	private Handler handler;
	private Context context;

	private String phone;
	private String password;
	private String smsCode;
	private int doctorId;
	private String name;
	private String hospital;
	private String department;
	private String title;
	private String tel;//联系电话
	private String adept;
	private String resume;//简介
	private String license;//医生执照
	private String cpwd;//当前密码
	private String npwd;//新密码
	private String pwd;
	private String date;
	private String attention;
	private String datetime;

	private int type;
	private String beginTime;
	private String endTime;
	private int maxCount;
	private String address;

	private int pageIndex;
	private int pageSize;

	private String idList;
	private String customerId;

	private int communityId;
	private int addressId;
	private String image;
	private String remark;
	private String suggestion;
	private String images;

	private String startDate;
	private String endDate;
	private int category;
	private String condition;
	private String culture;
	private String inputData;

	private String account;
	private String content;
	private boolean top;
	private int totype;
	private String customerIdList;

	private String idCord;
	private String mobile;

	private boolean schedule;
	private boolean sign;
	private boolean newMsg;
	private boolean donotDisturb;

	private int signId;

	private String photo;
	private SharedPreferences sp;

	public DoctorSHThread(String methodName, Handler handler,
						  Context context) {
		super();
		this.methodName = methodName;
		this.handler = handler;
		this.context = context;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public void setNamee(String name) {
		this.name = name;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public void setAttention(String bool){this.attention = bool;}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public void setCpwd(String cpwd) {
		this.cpwd = cpwd;
	}

	public void setNpwd(String npwd) {
		this.npwd = npwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setIdList(String idList) {
		this.idList = idList;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setCulture(String culture) {
		this.culture = culture;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setTop(boolean top) {
		this.top = top;
	}

	public void setTotype(int totype) {
		this.totype = totype;
	}

	public void setCustomerIdList(String customerIdList) {
		this.customerIdList = customerIdList;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public void setIdCord(String idCord) {
		this.idCord = idCord;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setSchedule(boolean schedule) {
		this.schedule = schedule;
	}

	public void setSign(boolean sign) {
		this.sign = sign;
	}

	public void setNewMsg(boolean newMsg) {
		this.newMsg = newMsg;
	}

	public void setDonotDisturb(boolean donotDisturb) {
		this.donotDisturb = donotDisturb;
	}

	public void setSignId(int signId) {
		this.signId = signId;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public void setAdept(String adept) {
		this.adept = adept;
	}

	public void run() {
//		sp = context.getSharedPreferences(AppConstant.USER_sp_name,
//				context.MODE_WORLD_READABLE);
//		url=sp.getString(AppConstant.USER_url,"error_app_url");
	//	key=sp.getString(AppConstant.USER_key, "error_app_key");
		url="http://www.iprecare.com";
		key="58f1d615-ed3c-457a-98fe-320dcdf08b74";
//		String api=sp.getString(AppConstant.USER_api, "error_app_api");
//		String api="122.225.60.118:6280/PartnersAPI";
//		SHDoctorAPI="http://"+api+"/SHDoctorAPI.asmx";
		SHDoctorAPI="http://122.225.60.118:6280/PartnersAPI/V3/SHDoctorAPI.asmx";

		Log.v("aboutkey","url :"+ url+ "," +"key: "+key +"SHDoctorAPI :"+SHDoctorAPI);



		String str = url + "/" + methodName;
//		key = ApiConstant.KEY;
		SoapObject localSoapObject = new SoapObject(url, methodName);
		localSoapObject.addProperty("key", key);
		if(methodName.equals(ApiConstant.VERIFYPHONE)){
			localSoapObject.addProperty("phone",phone);
		}
		// 医生注册
		else if (methodName.equals(ApiConstant.REGISTERDOCTOR)) {
			// localSoapObject.addProperty("key", key);
			localSoapObject.addProperty("phone",phone);
			localSoapObject.addProperty("password", password);
			localSoapObject.addProperty("smsCode", smsCode);
		}
		// 医生登录
		else if (methodName.equals(ApiConstant.DOCTORLOGIN)) {

			Log.v("qqq","url :");

			localSoapObject.addProperty("phone", phone);
			localSoapObject.addProperty("password", password);
		}
		//医生验证码登录
		else if(methodName.equals(ApiConstant.DOCTOR_LOGIN_BY_CODE)){
			localSoapObject.addProperty("phone", phone);
			localSoapObject.addProperty("code", password);
		}
		//获取医生执照
		else if (methodName.equals(ApiConstant.GETDOCTORLICENSE)
				//获取签约用户
				||methodName.equals(ApiConstant.GETDOCTORADDRESSLIST)
				//根据医生id服务数据统计信息
				||methodName.equals(ApiConstant.GETDOCTORSCHEDULECOUNTS)
				||methodName.equals(ApiConstant.GETCUSTOMERCOUNTS)
				||methodName.equals(ApiConstant.GETSHMSGSETTINGS)
				){
			localSoapObject.addProperty("doctorId",doctorId);
		}
		// 医生账号唯一性的验证
//		else if (methodName.equals(ApiConstant.VERIFYACCOUNT)) {
//			// localSoapObject.addProperty("key", key);
//			localSoapObject.addProperty("account", account);
//		}
		// 医生更新信息
		else if (methodName.equals(ApiConstant.UPDATEDOCTORINFO)) {
			// localSoapObject.addProperty("key", key);
			localSoapObject.addProperty("doctorId", doctorId);
			localSoapObject.addProperty("name", name);
			localSoapObject.addProperty("hospital", hospital);
			localSoapObject.addProperty("department", department);
			localSoapObject.addProperty("title", title);
			localSoapObject.addProperty("tel", tel);
			localSoapObject.addProperty("adept",adept);
			localSoapObject.addProperty("resume", resume);
		}
		//设置医生执照
		else if(methodName.equals(ApiConstant.SETDOCTORLICENSE)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("license",license);
		}
		//获取当天日程安排
		else if(methodName.equals(ApiConstant.GETSHDOCTORPLANBYDATE)||
				 methodName.equals(ApiConstant.GETSHSCHEDULEC)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("date",date);
		}
		else if(methodName.equals(ApiConstant.ADDSHDOCTORPLAN)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("type",type);
			localSoapObject.addProperty("beginTime",beginTime);
			localSoapObject.addProperty("endTime",endTime);
			localSoapObject.addProperty("maxCount",maxCount);
			localSoapObject.addProperty("address",address);
		}
		else if(methodName.equals(ApiConstant.GETSHDOCTORPLANBYPAGE)||
				 methodName.equals(ApiConstant.GETSHSIGNRECORDLIST)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("pageIndex",pageIndex);
			localSoapObject.addProperty("pageSize",pageSize);
		}
		else if(methodName.equals(ApiConstant.DELETESHDOCTORPLAN)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("idList",idList);
		}
		else if(methodName.equals(ApiConstant.GETCUSTOMERBASICINFO)
				//获取用户基本信息
				|| methodName.equals(ApiConstant.GETCUSTOMERCONTACTINFO)
				//获取用户通讯信息
				|| methodName.equals(ApiConstant.GETCUSTOMERHEALTHINFO)
				//获取用户健康信息
				||methodName.equals(ApiConstant.GETCUSTOMERREMARK)
				){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("customerId",customerId);
		}
		else if(methodName.equals(ApiConstant.ADDDOCTORADDRESS)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("communityId",communityId);
			localSoapObject.addProperty("hospital",hospital);
			localSoapObject.addProperty("address",address);
		}
		else if(methodName.equals(ApiConstant.DELETEDOCTORADDRESS)
				||methodName.equals(ApiConstant.SETDEFAULTDOCTORADDRESS)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("addressId",addressId);
		}
//		else if(methodName.equals(ApiConstant.DOWNLOADIMAGE)){
//			localSoapObject.addProperty("image",image);
//		}
		else if(methodName.endsWith(ApiConstant.SETDOCTORPHOTO)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("photo",photo);
		}
//		分页查询客户指导建议列表，根据id倒序排序
		else if(methodName.equals(ApiConstant.GETSHSUGGESTIONLIST)||
				 methodName.equals(ApiConstant.GETSHSCHEDULEBYCUSTOMERID)||
				 methodName.equals(ApiConstant.GETABNORMALRECORDSBYCUSTOMERID)||
				 methodName.equals(ApiConstant.GETSHINPUTDATALIST)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("customerId",customerId);
			localSoapObject.addProperty("pageIndex",pageIndex);
			localSoapObject.addProperty("pageSize",pageSize);
		}
//		设置医生对客户的备注信息
		else if(methodName.equals(ApiConstant.SETCUSTOMERREMARK)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("customerId",customerId);
			localSoapObject.addProperty("remark",remark);
		}
		else if (methodName.equals(ApiConstant.SETCUSTOMERPAYATTENTION)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("customerId",customerId);
			localSoapObject.addProperty("payAttention",attention);
		}
//		医生提交指导建议
		else if(methodName.equals(ApiConstant.SUBMISSIONSHSUGGESTION)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("customerId",customerId);
			localSoapObject.addProperty("suggestion",suggestion);
			localSoapObject.addProperty("images",images);
		}
//		更新医生的诊所(医院)地址信息
		else if(methodName.equals(ApiConstant.UPDATEDOCTORADDRESS)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("addressId",addressId);
			localSoapObject.addProperty("communityId",communityId);
			localSoapObject.addProperty("hospital",hospital);
			localSoapObject.addProperty("address",address);
		}
		else if(methodName.equals(ApiConstant.GETALLCUSTOMER)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("category",category);
			localSoapObject.addProperty("condition",condition);
			localSoapObject.addProperty("bdate",startDate);
			localSoapObject.addProperty("edate",endDate);
		}
		else if(methodName.equals(ApiConstant.GETMEDICALHISTORY)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("customerId",customerId);
			localSoapObject.addProperty("culture",culture);
		}
		else if(methodName.equals(ApiConstant.INSERTINTOSHINPUTDATA)){
			localSoapObject.addProperty("inputData",inputData);
		}

		//分页查询查询医生所有服务记录，根据id倒序排序
		else if(methodName.equals(ApiConstant.GETSHSCHEDULELIST)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("condition",condition);
			localSoapObject.addProperty("pageIndex",pageIndex);
			localSoapObject.addProperty("pageSize",pageSize);
		}
		else if(methodName.equals(ApiConstant.GETSHNOTICELIST)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("pageIndex",pageIndex);
			localSoapObject.addProperty("pageSize",pageSize);
		}
		else if(methodName.equals(ApiConstant.RELEASESHNOTICE)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("title",title);
			localSoapObject.addProperty("content",content);
			localSoapObject.addProperty("images",images);
			localSoapObject.addProperty("top",top);
			localSoapObject.addProperty("totype",totype);
			localSoapObject.addProperty("customerIdList",customerIdList);
		}
		//便携设备数据
		else if(methodName.equals(ApiConstant.GETVHDHEARTRATEBYCUSTOMERID_TOP10)||
				 methodName.equals(ApiConstant.GETVHDBLOODPRESSUREBYCUSTOMERID_TOP10)||
				 methodName.equals(ApiConstant.GETVHDBLOODOXYGENBYCUSTOMERID_TOP10)||
				 methodName.equals(ApiConstant.GETVHDSTEPBYCUSTOMERID_TOP10)||
				 methodName.equals(ApiConstant.GETVHDCALORIEBYCUSTOMERID)||
				 methodName.equals(ApiConstant.GETVHDBLOODGLUCOSEBYCUSTOMERID_TOP10)||
				 methodName.equals(ApiConstant.GETVHDTEMPERATUREBYCUSTOMERID_TOP10)||
				 methodName.equals(ApiConstant.GETVHDWEIGHTLISTBYCUSTOMERID_TOP10)||
				 methodName.equals(ApiConstant.GETVHDBFRBYCUSTOMERID_TOP10)||
				 methodName.equals(ApiConstant.GETVHDMOISTUREBYCUSTOMERID_TOP10)||
				 methodName.equals(ApiConstant.GETVHDMUSCLEBYCUSTOMERID_TOP10)||
				 methodName.equals(ApiConstant.GETVHDBONEBYCUSTOMERID_TOP10)||
				 methodName.equals(ApiConstant.GETVHDBMBYCUSTOMERID_TOP10)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("customerId",customerId);
			localSoapObject.addProperty("datetime",datetime);
		}
		else if(methodName.equals(ApiConstant.GETHDDATABYCUSTOMERID_TOP3)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("customerId",customerId);
		}
		else if(methodName.equals(ApiConstant.GETHSBLOODGLUCOSE_TOP10)||
				methodName.equals(ApiConstant.GETHSBLOODOXYGENDATA_TOP10)||
				methodName.equals(ApiConstant.GETHSBLOODPRESSUREDATA_TOP10)||
				methodName.equals(ApiConstant.GETHSHEIGHTDATA_TOP10)||
				methodName.equals(ApiConstant.GETHSMINFATDATA_TOP10)||
				methodName.equals(ApiConstant.GETHSPEECGDATA_TOP10)||
				methodName.equals(ApiConstant.GETHSTEMPERATUREDATA_TOP10)||
				methodName.equals(ApiConstant.GETSHHEALTHDATACONTRAST)
				){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("idCord",idCord);
			localSoapObject.addProperty("mobile",mobile);
			localSoapObject.addProperty("datetime",datetime);
		}
		else if(methodName.equals(ApiConstant.GETSHHEALTHDATALIST)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("idCord",idCord);
			localSoapObject.addProperty("mobile",mobile);
			localSoapObject.addProperty("pageIndex",pageIndex);
			localSoapObject.addProperty("pageSize",pageSize);
		}
		else if(methodName.equals(ApiConstant.SETSHMSGSETTINGS)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("schedule",schedule);
			localSoapObject.addProperty("sign",sign);
			localSoapObject.addProperty("newMsg",newMsg);
			localSoapObject.addProperty("donotDisturb",donotDisturb);
		}
		else if(methodName.equals(ApiConstant.SUBMITSHDOCTORFEEDBACK)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("content",content);
			localSoapObject.addProperty("images",images);
			localSoapObject.addProperty("phone",phone);
		}
		else if(methodName.equals(ApiConstant.SHSIGNAGREE)||
				 methodName.equals(ApiConstant.SHSIGNREFUSE)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("signId",signId);
		}
		else if(methodName.equals(ApiConstant.CHANGESHDOCTORPHONE)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("phone",phone);
			localSoapObject.addProperty("smsCode",smsCode);
		}
		//主页消息数据
		else if(methodName.equals(ApiConstant.GETMSGMAININFO)){
			localSoapObject.addProperty("doctorId",doctorId);
		}
		//获取评论列表
		else if(methodName.equals(ApiConstant.GETEVALUATIONLIST)){
			localSoapObject.addProperty("doctorId",doctorId);
			localSoapObject.addProperty("pageIndex",pageIndex);
			localSoapObject.addProperty("pageSize",pageSize);
		}

//		// 上传医生头像
//		else if (methodName.equals(ApiConstant.UPLOADPHOTO)) {
//			localSoapObject.addProperty("stream", stream);
//			localSoapObject.addProperty("suffix", suffix);
//		}
//		// 设置医生头像
//		else if (methodName.equals(ApiConstant.SETDOCTORPHOTO)) {
//			// localSoapObject.addProperty("key", key);
//			localSoapObject.addProperty("doctorId", customerId);
//			localSoapObject.addProperty("photo", photo);
//		}
//		// 获取随访医生头像
//		else if (methodName.equals(ApiConstant.GETDOCTORPHOTO)) {
//			localSoapObject.addProperty("key", key);
//			localSoapObject.addProperty("doctorId", customerId);
//		}
//		// 下载头像
//		else if (methodName.equals(ApiConstant.DOWNLOADPHOTO)) {
//			localSoapObject.addProperty("fileName", photo);
//		}
		// 修改密码
		else if (methodName.equals(ApiConstant.UPDATESHDOCTORPASSWORD)) {
			localSoapObject.addProperty("doctorId", doctorId);
			localSoapObject.addProperty("cpwd", cpwd);
			localSoapObject.addProperty("npwd",npwd);
		}
		//重置用户登录密码
		else if(methodName.equals(ApiConstant.RESETSHDOCTORPASSWORD)){
			localSoapObject.addProperty("phone",phone);
			localSoapObject.addProperty("pwd",pwd);
			localSoapObject.addProperty("smsCode",smsCode);
		}
		System.out.println("DoctorSHWebService  localSoapObject:"
				+ localSoapObject.toString());

		SoapSerializationEnvelope localSoapSerializationEnvelope = new SoapSerializationEnvelope(
				110);
		localSoapSerializationEnvelope.dotNet = true;
		localSoapSerializationEnvelope.bodyOut = localSoapObject;
		localSoapSerializationEnvelope.setOutputSoapObject(localSoapObject);
		HttpTransportSE localHttpTransportSE = new HttpTransportSE(SHDoctorAPI,
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
		msg.what = ApiConstant.MSG_API_HANDLER;

		handler.sendMessage(msg);
	}

	public String getResult() {
		System.out.println("随护助手 医生模块 接口 "+methodName + "_result:" + result);
		return result;
	}
}
