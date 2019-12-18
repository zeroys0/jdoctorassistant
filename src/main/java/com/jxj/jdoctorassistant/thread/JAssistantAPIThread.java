package com.jxj.jdoctorassistant.thread;

import java.net.SocketTimeoutException;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.jxj.jdoctorassistant.app.ApiConstant;
import com.jxj.jdoctorassistant.app.AppConstant;


public class JAssistantAPIThread extends Thread {
	
	private SharedPreferences sp;
	// 必选参数

	private Context context;
	private String url;
	private String urlJdoctorAssistantAPI ;
	private String key;
	private String methodName;
	private Handler handler;
	private String result = null;

	private int uId;//登录用户ID
	// 可选参数
	private String customerId;
	private String username;
	private String account;
	private String password;
	private String startTime;
	private String endTime;
	private int pageIndex;
	private int pageSize;
	private String updateBasicString;
	private String updateHealthString;
	private String updateContactString;
	private int period;
	private String stream;
	private String suffix;
	private String photo;
	private int remindID;
	private String remindStr;
	private String phone;
	private String contactInfo;
	private int position;
	private String urgentPeople;
	private String uid;
	private int amount;
	private String company;
	private String customerBasicInfoNEWString;
	private String jwotchName;
	private String IMEI;
	private int id;
	private String sportPlan;
	
	private String patchId;
	private String temperature;
	private String datetime;
	
	private String orderNumber, channel, paymentAmount, cphoneNumber;
	
	private String dataId;
	
	private int type;

	private String pwd;
	
	public JAssistantAPIThread(String methodName, Handler handler,
			Context context) {
		super();
		this.methodName = methodName;
		this.handler = handler;
		this.context = context;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setUpdateBasicString(String updateBasicString) {
		this.updateBasicString = updateBasicString;
	}

	public void setUpdateHealthString(String updateHealthString) {
		this.updateHealthString = updateHealthString;
	}

	public void setUpdateContactString(String updateContactString) {
		this.updateContactString = updateContactString;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public void setRemindID(int remindID) {
		this.remindID = remindID;
	}

	public void setRemindStr(String remindStr) {
		this.remindStr = remindStr;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setUrgentPeople(String urgentPeople) {
		this.urgentPeople = urgentPeople;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setCustomerBasicInfoNEWString(String customerBasicInfoNEWString) {
		this.customerBasicInfoNEWString = customerBasicInfoNEWString;
	}

	public void setJwotchName(String jwotchName) {
		this.jwotchName = jwotchName;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSportPlan(String sportPlan) {
		this.sportPlan = sportPlan;
	}
	
	public void setPatchId(String patchId) {
		this.patchId = patchId;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public void setCphoneNumber(String cphoneNumber) {
		this.cphoneNumber = cphoneNumber;
	}
	
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public void setuId(int uId) {
		this.uId = uId;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void run() {

		sp = context.getSharedPreferences(AppConstant.USER_sp_name,
				context.MODE_WORLD_READABLE);
		url=sp.getString(AppConstant.USER_url,"error_app_url");
		key=sp.getString(AppConstant.USER_key, "error_app_key");
		String api=sp.getString(AppConstant.USER_api, "error_app_api");
		urlJdoctorAssistantAPI="http://"+api+"/JAssistantAPI.asmx";
		String str = url + "/" + methodName;
		SoapObject localSoapObject = new SoapObject(url, methodName);
		localSoapObject.addProperty("key", key);
		localSoapObject.addProperty("uId",uId);
		if (methodName.equals(ApiConstant.GETCUSTOMERBASICINFO)
				//获取用户基本信息
				|| methodName.equals(ApiConstant.GETCUSTOMERCONTACTINFO)
				//获取用户通讯信息
				|| methodName.equals(ApiConstant.GETCUSTOMERHEALTHINFO)
				//获取用户健康信息
				|| methodName.equals(ApiConstant.GETCUSTOMERBASICINFONEWBYID)
				|| methodName.equals(ApiConstant.HEALTHSTATE)
				|| methodName.equals(ApiConstant.GETSAMPLINGPERIOD)
				|| methodName.equals(ApiConstant.GETBLOODPRESSUREPERIOD)
				|| methodName.equals(ApiConstant.GETREMINDLIST)
				|| methodName.equals(ApiConstant.GETCONTACTLISTBYCUSTOMERID)
				|| methodName.equals(ApiConstant.GETRELATIVECONTACTLIST)
				|| methodName.equals(ApiConstant.GETURGENPEOPLE)
				|| methodName.equals(ApiConstant.FINALMONEY)
				|| methodName.equals(ApiConstant.GETSPORTPLANLIST)
				|| methodName.equals(ApiConstant.GETJWOTCH)
				// 获取用户头像
				|| methodName.equals(ApiConstant.GETCUSTOMERPHOTO)) {

			localSoapObject.addProperty("customerId", customerId);
		}
		// 医生登录
		else if (methodName.equals(ApiConstant.DOCTORLOGIN)) {
			SoapObject localSoapObject1 = new SoapObject(url, methodName);
			localSoapObject1.addProperty("key", key);
			localSoapObject1.addProperty("account", account);
			localSoapObject1.addProperty("pwd", pwd);
		}
		else if(methodName.equals(ApiConstant.GETCUSTOMERLIST)){
			localSoapObject.addProperty("pageNo",pageIndex);
			localSoapObject.addProperty("pageSize",pageSize);
		}
		else if (methodName.equals(ApiConstant.CUSTOMERLOGIN)) {
			localSoapObject.addProperty("account", account);
			localSoapObject.addProperty("password", password);
		} else if (methodName.equals(ApiConstant.CUSTOMEREGISTER)) {
			localSoapObject.addProperty("userName", username);
			localSoapObject.addProperty("password", password);
		} else if (methodName.equals(ApiConstant.GETHEARTRATEHM041)
				|| methodName.equals(ApiConstant.GETHEARTRATEHM032)
				|| methodName.equals(ApiConstant.GETBTTEMPERATURE)) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("startTime", startTime);
			localSoapObject.addProperty("endTime", endTime);
			localSoapObject.addProperty("pageIndex", pageIndex);
			localSoapObject.addProperty("pageSize", pageSize);
		} else if (methodName.equals(ApiConstant.UPDATECUSTOMERBASICINFO)) {

			localSoapObject.addProperty("customerBasicInfoString",
					updateBasicString);

		} else if (methodName.equals(ApiConstant.UPDATECUSTOMERCONTACTINFO)) {

			localSoapObject.addProperty("customerContactInfoString",
					updateContactString);

		} else if (methodName.equals(ApiConstant.UPDATECUSTOMERHEALTHINFO)) {

			localSoapObject.addProperty("customerHealthInfoString",
					updateHealthString);

		} else if (methodName.equals(ApiConstant.SETSAMPLINGPERIOD)
				|| methodName.equals(ApiConstant.SETBLOODPRESSUREPERIOD)
				) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("period", period);
		}
		// 上传用户头像
		else if (methodName.equals(ApiConstant.UPLOADPHOTO)) {
			localSoapObject.addProperty("stream", stream);
			localSoapObject.addProperty("suffix", suffix);
		}
		// 设置用户头像
		else if (methodName.equals(ApiConstant.SETCUSTOMERPHOTO)) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("photo", photo);
		}
		// 下载头像
		else if (methodName.equals(ApiConstant.DOWNLOADPHOTO)) {
			localSoapObject.addProperty("fileName", photo);
		}
		// 增加，修改用户定时提醒
		else if (methodName.equals(ApiConstant.ADDNEWREMIND)
				|| methodName.equals(ApiConstant.UPDATEREMIND)) {
			localSoapObject.addProperty("remind", remindStr);
		}
		else if (methodName.equals(ApiConstant.GETREMINDBYID)
				) {
			localSoapObject.addProperty("id", remindID);
		}

		// 修改通讯录

		else if (methodName.equals(ApiConstant.UPDATECONTACT)) {
			localSoapObject.addProperty("customerAddressBook", contactInfo);
		}

		// 增加通讯录
		else if (methodName.equals(ApiConstant.ADDCONTACT)) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("name", username);
			localSoapObject.addProperty("phone", phone);
		}

		// 添加亲人号码
		else if (methodName.equals(ApiConstant.SETRELATIVECONTACT)) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("position", position);
			localSoapObject.addProperty("contact", username);
			localSoapObject.addProperty("phone", phone);
		}

		// 修改，添加紧急联系人号码
		else if (methodName.equals(ApiConstant.UPDATEURGENPEOPLE)
				|| methodName.equals(ApiConstant.ADDURGENPEOPLE)) {
			localSoapObject.addProperty("urgentPeople", urgentPeople);
		}

		// 删除紧急联系人,亲人号码，定时提醒,运动计划
		else if (methodName.equals(ApiConstant.DELETEURGENPEOPLE)
				|| methodName.equals(ApiConstant.DELETERELATIVECONTACTBYID)
				|| methodName.equals(ApiConstant.DELETEREMIND)
				||methodName.equals(ApiConstant.DELETESPORTPLAN)
				//获取脉搏波形数据
				||methodName.equals(ApiConstant.GETPRIMITIVEDATA)
				) {
			localSoapObject.addProperty("dataId", dataId);
		}

		// 获取定位信息
		else if (methodName.equals(ApiConstant.GETTOPGPSLOCATION)) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("amount", amount);

		}

		// 获取指定日期信息
		else if (methodName.equals(ApiConstant.GETDAILYGPSLOCATION)) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("dtime", startTime);

		}

		// 用户注册
		else if (methodName.equals(ApiConstant.CUSTOMERREGISTERFORAPP)) {
			localSoapObject.addProperty("userName", username);
			localSoapObject.addProperty("password", password);
			localSoapObject.addProperty("name", account);
			localSoapObject.addProperty("mobilePhone", phone);
			localSoapObject.addProperty("company", company);

		}

		// 激活界面
		else if (methodName.equals(ApiConstant.CUSTOMER_UPDATEBASICINFO)) {
			localSoapObject.addProperty("customerBasicInfoNEWString",
					customerBasicInfoNEWString);

		} else if (methodName.equals(ApiConstant.CUSTOMERBINDINGJWOTCH)) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("IMEI", IMEI);
			localSoapObject.addProperty("phoneNumber", jwotchName);
		}

		// 增加修改运动计划
		else if (methodName.equals(ApiConstant.INSERTSPORTPLAN)
				|| methodName.equals(ApiConstant.UPDATESPORTPLAN)) {
			localSoapObject.addProperty("sportPlan", sportPlan);
		}
		// 获取运动记录

		else if (methodName.equals(ApiConstant.GETSPORTRECORDLIST)) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("pageNo", pageIndex);
			localSoapObject.addProperty("pageSize", pageSize);
		}

		// 获取运动记录点
		else if (methodName.equals(ApiConstant.GETSPORTUNITLIST)
				|| methodName.equals(ApiConstant.UPDATESPORTPLAN)) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("sportId", id);
		}
		// 卡路里周月查询
		else if (methodName.equals(ApiConstant.GETCALORIE_WEEKORMONTH)
				//血压周月查询
				||methodName.equals(ApiConstant.GETBLOODPRESSURE_WEEKORMONTH)
				//心率周月查询
				||methodName.equals(ApiConstant.GETHEARTRATE_WEEKORMONTH)
				) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("startDate", startTime);
			localSoapObject.addProperty("endDate", endTime);
		}

		// 设置密保手机
		else if (methodName.equals(ApiConstant.SETENCRYPTEDPHONE)) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("phone", phone);
		}

		// 获取031与041的血压记录

		else if (methodName.equals(ApiConstant.GETCUSTOMERDATA_DAY)
				|| methodName.equals(ApiConstant.GETHM041BLOODPRESSUREBYDATE)) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("dateTime", startTime);
		}
		
		//查询用户一段时间的血压
		
		else if (methodName.equals(ApiConstant.GETCUSTOMERDATA_WEEKORMONTH)
				) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("startDate", startTime);
			localSoapObject.addProperty("endDate", endTime);
		}
		//添加支付记录
		else if (methodName.equals(ApiConstant.ADDPAYMENTRECORD)) {
			localSoapObject.addProperty("orderNumber", orderNumber);
			localSoapObject.addProperty("channel", channel);
			localSoapObject.addProperty("paymentAmount", paymentAmount);
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("cphoneNumber", cphoneNumber);
		}
		
		//重置用户密码
		else if (methodName.equals(ApiConstant.RESETCUSTOMERPASSWORD)) {
			localSoapObject.addProperty("customerId", customerId);
			localSoapObject.addProperty("password", password);
		}
		//版本更新
		else if (methodName.equals(ApiConstant.CHECKAPPVERSION)) {
		} 

		//分页查询第三方设备数据
		else if(methodName.equals(ApiConstant.GETTHIRDPARTYDATA)){
			localSoapObject.addProperty("customerId",customerId);
			localSoapObject.addProperty("pageNo",pageIndex);
			localSoapObject.addProperty("pageSize",pageSize);
			localSoapObject.addProperty("type",type);
		}

		
		System.out.println("JAssistantAPIThread  localSoapObject:"
				+ localSoapObject.toString());

		SoapSerializationEnvelope localSoapSerializationEnvelope = new SoapSerializationEnvelope(
				110);
		localSoapSerializationEnvelope.dotNet = true;
		localSoapSerializationEnvelope.bodyOut = localSoapObject;
		localSoapSerializationEnvelope.setOutputSoapObject(localSoapObject);
		HttpTransportSE localHttpTransportSE = new HttpTransportSE(
				urlJdoctorAssistantAPI, 3000);

		try {
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

		Message msg = new Message();
		msg.what = ApiConstant.MSG_API_HANDLER;
		handler.sendMessage(msg);
	}

	public String getResult() {
		return result;

	}

}
