package com.jxj.jdoctorassistant.app;

public class ApiConstant {
//	public static final String url = "http://www.iprecare.com";
//	public static final String KEY = "e8f0ba48-8599-456c-80cf-5c9fa2cbdddb";
//	public static final String urljdoctorassistant="http://api.iprecare.com:6280/JAssistantAPI.asmx";
//	public static final String urljdoctorassistant_test ="http://122.225.60.118:8081/partnersapi/JAssistantAPI.asmx";
//	public static final String urlShDoctor_test="http://122.225.60.118:8081/PartnersAPI/SHDoctorAPI.asmx";
//	public static final String urlShDoctor="http://api.iprecare.com:6280/SHDoctorAPI.asmx";
//	public static final String urlIprecare_test="http://122.225.60.118:8081/partnersapi/iprecareapi.asmx";
//	public static final String urlIprecare="http://api.iprecare.com:6280/iprecareapi.asmx";
//	public static final String urlCommunity_test="http://122.225.60.118:8081/PartnersAPI/CommunityAssessAPI.asmx";
//	public static final String urlCommunity="";
//	public static final String urlPopular_test="http://122.225.60.118:8081/PartnersAPI/JXJPopularAPI.asmx";
//	public static final String getUrlPopular="";

	//签约医生模块
	public static final String REGISTERDOCTOR = "RegisterDoctor";//注册随护助手医生账号
	public static final String DOCTORLOGIN = "DoctorLogin";//医生登录(账号密码)
	public static final String DOCTOR_LOGIN_BY_CODE = "DoctorLoginByCode";		//验证码登录
	public static final String GETDOCTORLICENSE="GetDoctorLicense";//获取随护助手医生执照图片
	public static final String RESETSHDOCTORPASSWORD="ResetSHDoctorPassword";//通过短信验证码重置医生登陆密码
	public static final String SETDOCTORLICENSE="SetDoctorLicense";//设置医生执照
	public static final String VERIFYPHONE = "VerifyPhone";
	public static final String CHANGESHDOCTORPHONE="ChangeSHDoctorPhone";
	public static final String UPDATEDOCTORINFO = "UpdateDoctorInfo";
	public static final String UPDATESHDOCTORPASSWORD="UpdateSHDoctorPassword";
	public static final String GETALLCUSTOMER="GetAllCustomer";		//获取用户列表
	public static final String GETSHDOCTORPLANBYDATE="GetSHDoctorPlanByDate";//根据日期获取当天的排班计划
	public static final String GETSHDOCTORPLANBYPAGE="GetSHDoctorPlanByPage";
	public static final String ADDSHDOCTORPLAN="AddSHDoctorPlan";
	public static final String DELETESHDOCTORPLAN="DeleteSHDoctorPlan";
	public static final String GETSHSCHEDULEC="GetSHScheduleC";	//获取当天日程安排
	public static final String GETMSGMAININFO = "GetMsgMainInfo";	//获取消息主页数据
	public static final String SETCUSTOMERPAYATTENTION = "SetCustomerPayAttention";	//设置特别关注;
	public static final String SHDSCHEDULECOMPLETE="SHDScheduleComplete";
	public static final String GETSHSCHEDULEBYCUSTOMERID="GetSHScheduleByCustomerId";
	public static final String SHSIGNAGREE="SHSignAgree";	//同意签约
	public static final String SHSIGNREFUSE="SHSignRefuse";
	public static final String GETSHSIGNRECORDLIST="GetSHSignRecordList";	//获取签约记录
    public static final String GETEVALUATIONLIST = "GetEvaluationList";     //获取评论列表

	public static final String ADDDOCTORADDRESS="AddDoctorAddress";
	public static final String DELETEDOCTORADDRESS="DeleteDoctorAddress";
	public static final String DOWNLOADIMAGE="DownloadImage";
	public static final String GETCUSTOMERREMARK="GetCustomerRemark";	//获取医生对用户的备注
	public static final String GETDOCTORADDRESSLIST="GetDoctorAddressList";
	public static final String GETSHSUGGESTIONLIST="GetSHSuggestionList";
	public static final String SETCUSTOMERREMARK="SetCustomerRemark";
	public static final String SETDEFAULTDOCTORADDRESS="SetDefaultDoctorAddress";
	public static final String SUBMISSIONSHSUGGESTION="SubmissionSHSuggestion";
	public static final String UPDATEDOCTORADDRESS="UpdateDoctorAddress";
	public static final String GETMEDICALHISTORY="GetMedicalHistory";
    public static final String INSERTINTOSHINPUTDATA="InsertIntoSHInputData";
	public static final String GETSHINPUTDATALIST="GetSHInputDataList";
	public static final String GETSHSCHEDULELIST="GetSHScheduleList";
	public static final String GETDOCTORSCHEDULECOUNTS="GetDoctorScheduleCounts";
	public static final String GETSHNOTICELIST="GetSHNoticeList";
	public static final String RELEASESHNOTICE="ReleaseSHNotice";

	public static final String GETABNORMALRECORDSBYCUSTOMERID="GetAbnormalRecordsByCustomerId";	//获取异常记录

	//数据
	public static final String GETHDDATABYCUSTOMERID_TOP3="GetHDDataByCustomerId_Top3";

	public static final String GETVHDHEARTRATEBYCUSTOMERID_TOP10="GetVHDHeartRateByCustomerId_Top10";
	public static final String GETVHDBLOODPRESSUREBYCUSTOMERID_TOP10="GetVHDBloodPressureByCustomerId_Top10";
	public static final String GETVHDBLOODOXYGENBYCUSTOMERID_TOP10="GetVHDBloodOxygenByCustomerId_Top10";
	public static final String GETVHDSTEPBYCUSTOMERID_TOP10="GetVHDStepByCustomerId_Top10";
	public static final String GETVHDCALORIEBYCUSTOMERID="GetVHDCalorieByCustomerId";
	public static final String GETVHDBLOODGLUCOSEBYCUSTOMERID_TOP10="GetVHDBloodGlucoseByCustomerId_Top10";
	public static final String GETVHDTEMPERATUREBYCUSTOMERID_TOP10="GetVHDTemperatureByCustomerId_Top10";
	public static final String GETVHDWEIGHTLISTBYCUSTOMERID_TOP10="GetVHDWeightListByCustomerId_Top10";
	public static final String GETVHDBFRBYCUSTOMERID_TOP10="GetVHDBFRByCustomerId_Top10";
	public static final String GETVHDMOISTUREBYCUSTOMERID_TOP10="GetVHDMoistureByCustomerId_Top10";
	public static final String GETVHDMUSCLEBYCUSTOMERID_TOP10="GetVHDMuscleByCustomerId_Top10";
	public static final String GETVHDBONEBYCUSTOMERID_TOP10="GetVHDBoneByCustomerId_Top10";
	public static final String GETVHDBMBYCUSTOMERID_TOP10="GetVHDBMByCustomerId_Top10";

	public static final String GETSHHEALTHDATALIST="GetSHHealthDataList";


	public static final String GETHSPEECGDATA_TOP10="GetHsPEEcgData_Top10";
	public static final String GETHSBLOODPRESSUREDATA_TOP10="GetHsBloodPressureData_Top10";
	public static final String GETHSBLOODOXYGENDATA_TOP10="GetHsBloodOxygenData_Top10";
	public static final String GETHSBLOODGLUCOSE_TOP10="GetHsBloodGlucose_Top10";
	public static final String GETHSTEMPERATUREDATA_TOP10="GetHsTemperatureData_Top10";
	public static final String GETHSHEIGHTDATA_TOP10="GetHsHeightData_Top10";//身高体重
	public static final String GETHSMINFATDATA_TOP10="GetHsMinFatData_Top10";//脂肪数据

	public static final String GETSHHEALTHDATACONTRAST="GetSHHealthDataContrast";

	public static final String GETCUSTOMERCOUNTS="GetCustomerCounts";

	public static final String SETSHMSGSETTINGS="SetSHMsgSettings";
	public static final String GETSHMSGSETTINGS="GetSHMsgSettings";
	public static final String SUBMITSHDOCTORFEEDBACK="SubmitSHDoctorFeedback";

	public static final String GETCITYLIST="GetCityList";
	public static final String GETDISTRICTLIST="GetDistrictList";
	public static final String GETPROVINCELIST="GetProvinceList";
	public static final String UPLOADIMAGE="UploadImage";


	//环信相关
	public static final String ADDEASEMOBACCOUNT="AddEasemobAccount";
	public static final String EDITEASEMOBPASSWORD="EditEasemobPassword";
	public static final String GETEASEMOBPASSWORD="GetEasemobPassword";


	public static final String SETDOCTORPHOTO = "SetDoctorPhoto";
	public static final String GETDOCTORPHOTO = "GetDoctorPhoto";
	public static final String SETDOCTORPASSWORD = "SetDoctorPassword";

//	public static final String GETWHBLOODOXYGENLIST = "GetWHBloodOxygenList";
//	public static final String GETWHBLOODPRESSURELIST = "GetWHBloodPressureList";
//	public static final String GETWHTEMPDATALIST = "GetWHTempDataList";

	/**
	 * 控制面板相关参数
	 */
	public static final String SENDCMD = "SendCmd";

	public static final String JDOCTORALRM = "JDOCTOR_ALARM";
	public static final String JDOCTORUAUTOCALLBACK = "JDOCTOR_AUTOCALLBACK";
	public static final String JDOCTORDOWNLOADREMINDLIST = "JDOCTOR_DOWNLOAD_REMIND_LIST";
	public static final String JDOCTORDOWNLOADCONTACT = "JDOCTOR_DOWNLOADCONTACT";
	public static final String JDOCTOROPENGPS = "JDOCTOR_OPENGPS";
	public static final String JDOCTORRESTART = "JDOCTOR_RESTART";
	public static final String JDOCTORSYNCHAUTOSTARTSTOPTIME = "JDOCTOR_SYNCH_AUTOSTARTSTOPTIME";
	public static final String JDOCTORSYNCHBASEINFO = "JDOCTOR_SYNCH_BASE_INFO";
	public static final String JDOCTORSYNCHCALIBRATIONDATA = "JDOCTOR_SYNCH_CALIBRATIONDATA";
	public static final String JDOCTORSYNCHEMERGENCYLIST = "JDOCTOR_SYNCH_EMERGENCY_LIST";
	public static final String JDOCTORSYNCHHEALTHNUMBER = "JDOCTOR_SYNCH_HEALTHNUMBER";
	public static final String JDOCTORSYNCHRELATIVESCONTACT = "JDOCTOR_SYNCH_RELATIVES_CONTACT";
	public static final String JDOCTORSYNCHREMINDERLIST = "JDOCTOR_SYNCH_REMINDER_LIST";
	public static final String JDOCTORSYNCHSPORTPLAN = "JDOCTOR_SYNCH_SPORT_PLAN";
	public static final String JDOCTORSYNCHSYSTEMTIME = "JDOCTOR_SYNCH_SYSTEMTIME";
	public static final String JDOCTORUPDATEJWOTCHSTATUS = "JDOCTOR_UPDATE_JWOTCH_STATUS";
	public static final String JDOCTORUPLOADDATA = "JDOCTOR_UPLOADDATA";
	public static final String JDOCTOROPENHEARTRATE = "JDOCTOR_OPEN_HEARTRATE";
	public static final String JDOCTORSYNCHHEARTRATEPERIOD = "JDOCTOR_SYNCH_HEARTRATE_PERIOD";
	public static final String JDOCTOROPENBLOODOXYGEN = "JDOCTOR_OPEN_BLOODOXYGEN";
	public static final String JDOCTORSYNCHBLOODOXYGENPERIOD = "JDOCTOR_SYNCH_BLOODOXYGEN_PERIOD";
	public static final String JDOCTOROPENBLOODPRESSURE = "JDOCTOR_OPEN_BLOODPRESSURE";
	public static final String JDOCTORSYNCHBLOODPRESSUREPERIOD = "JDOCTOR_SYNCH_BLOODPRESSURE_PERIOD";

	
	public static final String onekeylocation = "JDOCTOR_OPENGPS";// 打开GPS(定位命令)
	public static final String refreshremindlist = "JDOCTOR_DOWNLOAD_REMIND_LIST";// 下载定时提醒列表(新版)
	public static final String refreshcontactlist = "JDOCTOR_DOWNLOADCONTACT";// 下载通讯录
	public static final String refreshsportplanlist = "JDOCTOR_SYNCH_SPORT_PLAN";// 同步活动(运动)计划
	public static final String refreshrelativecontactlist = "JDOCTOR_SYNCH_EMERGENCY_LIST";// 同步紧急联系人(旧版)
	public static final String refreshemergencecontactlist = "JDOCTOR_SYNCH_RELATIVES_CONTACT";// 同步亲人号码列表(新版)

	public static final String heartratemonitor = "JDOCTOR_OPEN_HEARTRATE";//开始采集心率数据命令
	public static final String bloodoxygenmonitor = "JDOCTOR_OPEN_BLOODOXYGEN";//开始采集血氧数据命令
	public static final String bloodpressuremonitor="JDOCTOR_OPEN_BLOODPRESSURE";//命令手表开始采集血压数据

	public static final String openheartrate="JDOCTOR_SYNCH_HEARTRATE_PERIOD";//同步心率自动启动时间间隔
	public static final String openbloodoxygen="JDOCTOR_SYNCH_BLOODOXYGEN_PERIOD";//同步血氧自动启动时间间隔
	public static final String openbloodpressure="JDOCTOR_SYNCH_BLOODPRESSURE_PERIOD";//同步血压自动启动时间间隔

	public static final String synch_base_info="JDOCTOR_SYNCH_BASE_INFO";//同步基本信息
	
	public static final int userId = 120;
	// public static final String ckey="27fa7f32-9524-4b67-9e0a-a3074eee896b";
	public static final String SENDMSG="SendMessage";

	// public static final String
	// KEY_020="df191395-8533-4b9e-b975-675d5a9d5cb5";//老表key

	// public static final String KEY = "e8f0ba48-8599-456c-80cf-5c9fa2cbdddb";
	// public static final String urlPartnerServiceAPI =
	// "http://www.iprecare.com:6280/PartnerService.asmx";
	// public static final String urlControlCenterAPI =
	// "http://www.iprecare.com:6280/ControlCenter.asmx";
	// public static final String urlHealthReportServiceAPI =
	// "http://www.iprecare.com:6280/HealthReportService.asmx";

	public static final String GETJWOTCH = "GetJWotch";
	
	// 个人信息方法列表
	public static final String GETCUSTOMERBASICINFO = "GetCustomerBasicInfo";
	public static final String GETCUSTOMERCONTACTINFO = "GetCustomerContactInfo";	//获取联系方式
	public static final String GETCUSTOMERHEALTHINFO = "GetCustomerHealthInfo";

	public static final String UPDATECUSTOMERBASICINFO = "UpdateCustomerBasicInfo";
	public static final String UPDATECUSTOMERCONTACTINFO = "UpdateCustomerContactInfo";
	public static final String UPDATECUSTOMERHEALTHINFO = "UpdateCustomerHealthInfo";
	//登录/注册
	public static final String CUSTOMERLOGIN = "CustomerLogin";
	public static final String CUSTOMEREGISTER = "Customer_Register";
	// 查询041心率
	public static final String GETHEARTRATEHM041 = "GetHeartRateHM041";
	// 查询032心率
	public static final String GETHEARTRATEHM032 = "GetHeartRateHM032";
	//心率详细信息
	public static final String GETPRIMITIVEDATA = "GetPrimitiveData";
	
	public static final String HEALTHSTATE = "HealthState";
	// 获取,设置心率自动采样时间间隔
	public static final String GETSAMPLINGPERIOD = "GetSamplingPeriod";
	public static final String SETSAMPLINGPERIOD = "SetSamplingPeriod";
	// 用户头像模块
	public static final String SETCUSTOMERPHOTO = "SetCustomerPhoto";
	public static final String UPLOADPHOTO = "UploadPhoto";
	public static final String GETCUSTOMERPHOTO = "GetCustomerPhoto";
	public static final String DOWNLOADPHOTO = "DownloadPhoto";

	// 用户提醒列表
	public static final String GETREMINDLIST = "GetRemindList";
	public static final String GETREMINDBYID = "GetRemindById";
	public static final String ADDNEWREMIND = "AddNewRemind";
	public static final String UPDATEREMIND = "UpdateRemind";
	public static final String DELETEREMIND = "DeleteRemind";

	// 用户联系人列表
	public static final String GETCONTACTLISTBYCUSTOMERID = "GetContactListByCustomerId";
	public static final String DELETECONTACT = "DeleteContact";
	public static final String ADDCONTACT = "AddContact";
	public static final String UPDATECONTACT = "UpdateContact";

	public static final String GETRELATIVECONTACTLIST = "GetRelativeContactList";
	public static final String DELETERELATIVECONTACTBYID = "DeleteRelativeContactById";
	public static final String SETRELATIVECONTACT = "SetRelativeContact";

	public static final String GETURGENPEOPLE = "GetUrgentPeople";
	public static final String ADDURGENPEOPLE = "AddUrgentPeople";
	public static final String UPDATEURGENPEOPLE = "UpdateUrgentPeople";
	public static final String DELETEURGENPEOPLE = "DeleteUrgentPeople";

	// 定位信息
	public static final String GETTOPGPSLOCATION = "GetTopGpsLocation";
	public static final String GETDAILYGPSLOCATION = "GetDailyGpsLocation";
	// 注册
	public static final String CUSTOMERREGISTERFORAPP = "CustomerRegisterForApp";
	// 激活
	public static final String CUSTOMER_UPDATEBASICINFO = "Customer_UpdateBasicInfo";
	public static final String CUSTOMERBINDINGJWOTCH = "CustomerBindingJWotch";
	public static final String GETCUSTOMERBASICINFONEWBYID="GetCustomerBasicInfoNEWById";

	// 运动轨迹
	public static final String INSERTSPORTPLAN = "InsertSportPlan";
	public static final String UPDATESPORTPLAN = "UpdateSportPlan";
	public static final String DELETESPORTPLAN = "DeleteSportPlan";
	public static final String GETSPORTPLANLIST = "GetSportPlanList";
	public static final String GETSPORTPLANBYID = "GetSportPlanById";
	
	// 获取运动记录
	public static final String GETSPORTRECORDLIST = "GetSportRecordList";
	public static final String GETSPORTUNITGAP = "GetSportUnitGap";
	public static final String SETSPORTUNITGAP = "SetSportUnitGap";
	public static final String GETSPORTUNITLIST = "GetSportUnitList";

	// 卡路里
	public static final String GETCALORIE_WEEKORMONTH = "GetCalorie_weekOrMonth";

	// 查询041血压记录
	public static final String GETHM041BLOODPRESSUREBYDATE = "GetHM041BloodPressureByDate";

	// 查询031血压记录
	public static final String GETCUSTOMERDATA_DAY = "GetCustomerData_day";
	//查询031用户一段时间的血压心率记录
	public static final String GETCUSTOMERDATA_WEEKORMONTH="GetCustomerData_weekOrMonth";
	
	//查询用户一段时间血压
	public static final String GETBLOODPRESSURE_WEEKORMONTH="GetBloodPressure_weekOrMonth";
	//查询用户一段时间心率（脉搏）
	public static final String GETHEARTRATE_WEEKORMONTH="GetHeartRate_weekOrMonth";
	
	//健康报告界面	
//	public static final String GETQUESTIONNUMBERBYID = "GetQuestionNumberByID";
//	public static final String GETADVICE = "GetAdvice";
	//温度贴
	public static final String UPLOADBTTEMPERATURE = "UploadBTTemperature";
	public static final String GETBTTEMPERATURE = "GetBTTemperature";
	//充值
	public static final String FINALMONEY = "FinalMoney";
	public static final String ADDPAYMENTRECORD = "AddPaymentRecord";
//	public static final String UPDATEMONEY = "UpdateMoney";/
	
	//修改密码
	public static final String RESETCUSTOMERPASSWORD="ResetCustomerPassword";
	
	//检查版本
//	public static final String CHECKAPPVERSION = "CheckAppVersion";

	//健康建议
	public static final String GETLIFESUGGESTIONS="GetLifeSuggestions";
	//获取健康分析报告
	public static final String GETHEALTHREPORT="GetHealthReport";
	//密保手机
	public static final String GETIMAGECALIDATE="GetImageCalidate";
	public static final String SENDAUTHCODE="SendAuthCode";		//获取短信验证码
	public static final String VERIFYAUTHCODE="VerifyAuthCode";
	public static final String GETENCRYPTEDPHONE="GetEncryptedPhone";
	public static final String SETENCRYPTEDPHONE="SetEncryptedPhone";
	public static final String SENDAUTHCODEFORRESETPASSWORD="SendAuthCodeForResetPassword";
	public static final String RESETCUSTOMERPASSWORDBYCODE="ResetCustomerPasswordByCode";
	//血压上传间隔设置
	public static final String GETBLOODPRESSUREPERIOD="GetBloodPressurePeriod";
	public static final String SETBLOODPRESSUREPERIOD="SetBloodPressurePeriod";
	//积分
	public static final String GETTODAYSCORE="GetTodayScore";
    public static final String GETDAILYSCOREDETAIL="GetDailyScoreDetail";
	public static final String GETTODAYSCORERANKLIST="GetTodayScoreRanklist";
	//第三方数据
	public static final String GETTHIRDPARTYDATA="GetThirdPartyData";

	
	public static final String SETSAMPLINGPEROID = "SetSamplingPeriod";
	public static final String GETSAMPLINGPEROID = "GetSamplingPeriod";

	public static final String GETCALORIEWEEKORMONTH = "GetCalorie_weekOrMonth";
	public static final String GETBLOODOXYGENHM041 = "GetBloodOxygenHM041";
	public static final String GETBLOODOXYGENDETAIL = "GetBloodOxygenDetail";
	public static final String SETBLOODOXYGENPERIOD = "SetBloodOxygenPeriod";
	public static final String GETBLOODOXYGENPERIOD = "GetBloodOxygenPeriod";
	public static final String GETPOSITIONRECORD = "GetPositionRecord";

	public static final String ACCCALORIE = "AccCalorie";
	public static final String ACCSPORTTIME = "AccSportTime";
	public static final String ACCSTEPS = "AccSteps";

	public static final String CHARTPAGEINFO = "ChartPageInfo";
	public static final String CHECKAPPVERSION = "CheckAppVersion";
	public static final String CUSTOMER_REGISTER = "Customer_Register";
	public static final String DELETEURGENTPEOPLE = "DeleteUrgentPeople";
	public static final String GETQUESTIONNUMBERBYID = "GetQuestionNumberByID";

	public static final String GETTOPCUSTOMERDATA = "GetTopCustomerData";

	public static final String GETCUSTOMERLIST="GetCustomerList";

	public static final String GETTOPNGPST = "GetTopNGpsT";


//评估服务

	//获取评估记录列表
	//public static final String GETPASSESSLIST="GetPAssessList";
//获取社区目录列表
	public static final String GETCOMMUNITYLISTBYLEVEL="GetCommunityListByLevel";
	//评估服务
	//新增申请人
	public static final String ADDAPPLICANT="AddApplicant";
	//新增评估
	public static final String ADDPASSESS="AddPAssess";
	//根据申请人id获取申请人信息
	public static final String GETAPPLICANTBYID="GetApplicantByID";
	//获取申请人信息列表
	public static final String GETAPPLICANTLIST="GetApplicantList";
	//获取社区目录列表
	public static final String GETCOMMUNITYLIST="GetCommunityList";
	//通过评估编号获取本次评估的生活自理能力信息
	public static final String GETPADL="GetPADL";
	//获取评估记录列表
	public static final String GETPASSESSLIST="GetPAssessListEx";
	//通过id 获取评估记录  GetPAssessListByIdEx
	public static final String GETPASSESSLISTBYID="GetPAssessListByIdEx";

	//GetAppointmentList
	public static final String GETAPPOINTMENTLIST="GetAppointmentList";
	//AddAppointment
	public static final String ADDAPPOINTMENT="AddAppointment";
	//编辑预约评估
	public static final String EDITAPPOINTMENT="EditAppointment";
	//删除预约评估
	public static final String DELETEAPPOINTMENT="DeleteAppointment";

	//通过评估编号获取本次评估的认知能力信息
	public static final String GETPCA="GetPCA";
	//通过评估编号获取本次评估的情绪行为信息
	public static final String GETPEB="GetPEB";
	//通过评估编号获取本次评估的评估报告
	public static final String GETPREPORT="GetPReport";
	//通过评估编号获取本次评估的社会生活环境及患病情况信息
	public static final String GETPSCIALLIFE="GetPScialLife";
	//通过评估编号获取本次评估的视觉能力信息
	public static final String GETPVISUAL="GetPVisual";
	//编辑申请人信息
	public static final String UPDATEAPPLICANT="UpdateApplicant";
	//更新生活自理能力评估内容
	public static final String UPDATEPADL="UpdatePADL";
	//更新认知能力评估内容
	public static final String UPDATEPCA="UpdatePCA";
	//更新情绪行为评估内容
	public static final String UPDATEPEB="UpdatePEB";
	//更新评估报告内容
	public static final String UPDATEPREPORT="UpdatePReport";
	//更新社会生活环境及患病情况评估内容
	public static final String UPDATEPSCIALLIFE="UpdatePScialLife";
	//更新视觉能力评估内容
	public static final String UPDATEPVISUAL="UpdatePVisual";


	public static final String NE = "NETWORK_EXCEPTION";

	public static final String OE = "OTHER_EXCEPTION";

	public static final int MSG_API_HANDLER = 0x133;
	public static final int MSG_SEND_API_HANDLER=0x134;
	public static final int MSG_EASY_HANDLER=0x135;

}
