package com.jxj.jdoctorassistant.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {
	
	// 版本名
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	// 版本号
	public static int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;
		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pi;
	}

	/*************************************************************************** 
     * 判断当前系统版本
     *  
     * @return 
     */  
	public static int getAndroidSDKVersion() {
		   int version = 0;
		   try {
		     version = Integer.valueOf(android.os.Build.VERSION.SDK);
		   } catch (NumberFormatException e) {
		     
		   }
		   return version;
		}
	/*************************************************************************** 
     * 验证电话号码 后可接分机号 区号3位或者4位 电话7位或者8位后 后面可加3位或者4位分机号 
     *  
     * @param telephoeNo 电话号码字符串 
     * @return 
     */  
    public static boolean isValidTelephoeNo(String telephoeNo)  
    {  
        // 1、\\d{3,4} 区号 3位或者4位的匹配  
        // 2、\\d{7,8} 号码 7味或者8位的匹配  
        // 3、(\\d{3,4})? 分机号3位或者4位的匹配 ？可匹配一次或者两次  
        boolean flag = false;  
        Pattern p = Pattern.compile("^\\d{3,4}\\d{7,8}(\\d{3,4})?$");  
        Matcher match = p.matcher(telephoeNo);  
        if (telephoeNo != null)  
        {  
            flag = match.matches();  
        }  
        return flag;  
    }

    /**
     * 验证手机号码
     *
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147、182
     * 联通号码段:130、131、132、136、185、186、145
     * 电信号码段:133、153、180、189、177
     *
     * @param mobileNo
     * @return
     */

    public static boolean isValidMobileNo(String mobileNo)  
    {  
        // 1、(13[0-9])|(15[02789])|(18[679]) 13段 或者15段 18段的匹配  
        // 2、\\d{8} 整数出现8次  
        boolean flag = false;  
        // Pattern p = Pattern.compile("^(1[358][13567890])(\\d{8})$");  
        Pattern p = Pattern  
                .compile("^((13[0-9])|(14[5|7])|(15([0-9]))|(18[0-9])|(17[0-9]))\\d{8}$");
        Matcher match = p.matcher(mobileNo);  
        if (mobileNo != null)  
        {  
            flag = match.matches();  
        }  
        return flag;  
    }  
  
    /*************************************************************************** 
     * 验证是否是正确的邮箱格式 
     *  
     * @param email 
     * @return true表示是正确的邮箱格式,false表示不是正确邮箱格式 
     */  
    public static boolean isValidEmail(String email)  
    {  
        // 1、\\w+表示@之前至少要输入一个匹配字母或数字或下划线 \\w 单词字符：[a-zA-Z_0-9]  
        // 2、(\\w+\\.)表示域名. 如新浪邮箱域名是sina.com.cn  
        // {1,3}表示可以出现一次或两次或者三次.  
        String reg = "\\w+@(\\w+\\.){1,3}\\w+";  
        Pattern pattern = Pattern.compile(reg);  
        boolean flag = false;  
        if (email != null)  
        {  
            Matcher matcher = pattern.matcher(email);  
            flag = matcher.matches();  
        }  
        return flag;  
    }  
}
