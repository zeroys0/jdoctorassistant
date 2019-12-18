package com.jxj.jdoctorassistant.util;

/**
 * Created by Administrator on 2017/11/28 0028.
 */

public class DataUtil {
    public static boolean isEmpty(String str){
        if(str==null||str.equals("null")){
            return true;
        }else {
            return false;
        }
    }

//    public static String isNull(String str){
//        if(str==null||str.equals("null")){
//            return "--";
//        }else {
//            return str;
//        }
//    }
}
