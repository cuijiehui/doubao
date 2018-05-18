package com.xinspace.csevent.monitor.bean;

public class DeviceBean {

	private String devSn = null;
	private String devMac = null;
	private int devType;
	private int privilege;
	private int openType;
	private int verified;
	private String startDate = null;
	private String endDate = null;
	private int useCount;
	private String eKey = null;
	private int encrytion = 0x00;
	
	public String getDevSn() {
		return devSn;
	}
	public void setDevSn(String devSn) {
		this.devSn = devSn;
	}
	
	public String getDevMac() {
		return devMac;
	}
	public void setDevMac(String devMac) {
		this.devMac = devMac;
	}
	
	public int getDevType() {
		return devType;
	}
	public void setDevType(int devType) {
		this.devType = devType;
	}
	
	public int getPrivilege() {
		return privilege;
	}
	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}
	
	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	
	public int getVerified() {
		return verified;
	}
	public void setVerified(int verified) {
		this.verified = verified;
	}
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public int getUseCount() {
		return useCount;
	}
	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}
	
	public String geteKey() {
		return eKey;
	}
	public void seteKey(String eKey) {
		this.eKey = eKey;
	}
	
	public int getEncrytion() {
		return encrytion;
	}
	public void setEncrytion(int encrytion) {
		this.encrytion = encrytion;
	}
	
}
