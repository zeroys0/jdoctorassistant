package com.jxj.jdoctorassistant.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;


import com.jxj.jdoctorassistant.R;
import com.jxj.jdoctorassistant.app.ApiConstant;


import net.sf.json.JSONObject;

public class UiUtil {

	public static boolean isOpenNetwork(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}
		
		UiUtil.showToast(context, context.getResources().getString(R.string.network_is_not_connected));
		
		return false;
	}
	
	public static boolean isResultSuccess(Context context, String result) {
		if (result == null || result.equals(ApiConstant.OE)) {
//			showOtherException(context);
			return false;
		} else if (result.equals(ApiConstant.NE)) {
			showNetworkException(context);
			return false;
		} else {
			return true;
		}

	}
	public static boolean isEditTextNull(EditText e) {  
		if (TextUtils.isEmpty(e.getText().toString().trim())) {
			return true;
		} else {
			return false;
		}
	}

	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	public static void showSendCmdResult(Context context,String result){
//		String[] arr=result.split("\\|");
		String msg=" ";
		try{
			JSONObject json=JSONObject.fromObject(result);
			msg=json.getString("message");
		}catch(Exception e){
			e.printStackTrace();
			String[] arr=result.split("\\|");
			msg=arr[arr.length-1];
		}
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void showNetworkException(Context context) {
		Toast.makeText(
				context,
				context.getResources().getString(R.string.network_exception), Toast.LENGTH_SHORT)
				.show();
	}
	
//	public static void showOtherException(Context context) {
//		Toast.makeText(
//				context,
//				context.getResources().getString(R.string.other_exception), Toast.LENGTH_SHORT)
//				.show();
//	}
}
