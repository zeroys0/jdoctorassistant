package com.jxj.jdoctorassistant.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5DeCode
{
	private static StringBuffer buf = new StringBuffer("");
	public static String GetMd5(String plainText) 
	{
		try { 
	    buf = new StringBuffer("");//加这句后代码每次运行时清除之前的存值
		MessageDigest md = MessageDigest.getInstance("MD5"); 
		md.update(plainText.getBytes()); 
		byte b[] = md.digest(); 
		int i=0;  
		for (int offset = 0; offset < b.length; offset++) 
		{
			i = b[offset]; 
		if(i<0) 
			i+= 256; 
		if(i<16) 
			buf.append("0");
		buf.append(Integer.toHexString(i));
		} 
		
		} catch (NoSuchAlgorithmException e) { 
		// TODO Auto-generated catch block 
		e.printStackTrace();
		}
		return buf.toString(); 
		} 
}

