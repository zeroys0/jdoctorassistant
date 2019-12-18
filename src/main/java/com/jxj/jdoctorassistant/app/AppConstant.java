package com.jxj.jdoctorassistant.app;

public class AppConstant {
	public static final int MAINVIEW_FINISH = 0x0010;
	public static final int HEALTHMEASUREITEM_FINISH = 0x0011;
	public static final int SAFEMONITORITEM_FINISH = 0x0102;
	public static final int MoreAbout_BACK = 0x0103;
	
	public static final String USER_sp_name="jwotch_sp_info";
	
	public static final String USER_isFirstLogin="isFirstLogin";

	public static final String Is_test="is_test";
	public static final String Is_test_server="is_test_server";

	public static final String ADMIN_userId="userId";
	public static final String USER_key="key";
	public static final String USER_api="api";
	public static final String USER_url="url";
	public static final String USER_code="code";
	
	public static final String USER_customerId="customer_id";
	public static final String USER_customerInfo="customer_info";
	public static final String USER_cname="cname";
	public static final String USER_jwotchModel="jwotch_model";
	public static final String USER_username="username";
	public static final String USER_jwotchNumber="jwotch_number";
	public static final String USER_password="password";
	public static final String USER_height="height";
	public static final String USER_weight="weight";
	public static final String USER_sex="sex";
	public static final String USER_age="age";
	public static final String USER_isSavePwd="is_save_pwd";
	public static final String USER_des_calorie="desCalorie";
	public static final String USER_city_id="city_id";
	public static final String USER_city_name="city_name";

	public static final String USER_doctor_id="doctor_id";
	public static final String USER_doctor_info="doctor_info";
//	public static final String USER_doctor_personal_info="doctor_personal_info";
	public static final String USER_doctor_username="doctor_username";
	public static final String USER_doctor_password="doctor_password";
	public static final String USER_doctor_community_id ="doctor_community_id";
	public static final String USER_doctor_ease_name="doctor_ease_name";
	public static final String LOGIN_state="login_state";
	public static final int DOCTOR_LOGIN=0x161;
	public static final int USER_LOGIN=0x162;
	
	public static final int CHART_CAL= 0x01;
	public static final int CHART_HR = 0x02;
	public static final int CHART_BP = 0x03;

	public static final int MSG_DATEDIALOG = 0x100;

	public static final String JWOTCHMODEL_031="JXJ-HM031";
	public static final String JWOTCHMODEL_032="JXJ-HM032";
	public static final String JWOTCHMODEL_041="JXJ-HM041";


	public static final String EASY_USER_INFO="easy_user_info";
	//0x00全部；0x01数据异常；0x02特别关心；0x03最近服务；0x04最近添加；0x05最近浏览；
	public static final int CUSTOMER_ALL=0x00;
	public static final int CUSTOMER_EXCEPTION=0x01;
	public static final int CUSTOMER_SPECIAL=0x02;
	public static final int CUSTOMER_LAST=0x03;
	public static final int CUSTOMER_LAST_ADD=0x04;
	public static final int CUSTOMER_LAST_BROWSE=0x05;

	public static final String STARTDATE="2017-10-01";

	public static final String JPUSHALIAS_DOCOTR="shzsys"; //医生
	public static final String JPUSHALIAS_USER="shzskf";   //客服

	//发送验证码
	public static final int USED_REGISTER = 0x01;	//注册新账号
	public static final int USED_LOGIN = 0x02;	//登录
	public static final int USED_RESET_PASSWORD = 0x03;	//重置密码
	public static final int USED_UPDATE_PHONENUMBER = 0x04;	//更换登录手机号

}
