package com.jxj.jdoctorassistant.model;

public class User {
	private int customerId;
	private String userName;
	private String cName;
	private String cPhoneNumber;
	private String cPassword;
	private String jwotchModel;
	
	private int ps,pd,hr;
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getcPhoneNumber() {
		return cPhoneNumber;
	}
	public void setcPhoneNumber(String cPhoneNumber) {
		this.cPhoneNumber = cPhoneNumber;
	}
	public String getcPassword() {
		return cPassword;
	}
	public void setcPassword(String cPassword) {
		this.cPassword = cPassword;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	
	public void setJwotchModel(String jwotchModel) {
		this.jwotchModel = jwotchModel;
	}
	public String getJwotchModel() {
		return jwotchModel;
	}
	public int getPs() {
		return ps;
	}
	public void setPs(int ps) {
		this.ps = ps;
	}
	public int getPd() {
		return pd;
	}
	public void setPd(int pd) {
		this.pd = pd;
	}
	public int getHr() {
		return hr;
	}
	public void setHr(int hr) {
		this.hr = hr;
	}
	
	
}
