package com.gencode.issuetool.obj;

public class CustomerInfo extends Pojo {

	protected long id;
	protected String customerId;
	protected String idType;
	protected String bizId;
	protected String country;
	protected String lang;
	protected String createdDtm;
	public CustomerInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomerInfo(long id, String customerId, String idType, String bizId, String country, String lang) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.idType = idType;
		this.bizId = bizId;
		this.country = country;
		this.lang = lang;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getCreatedDtm() {
		return createdDtm;
	}
	public void setCreatedDtm(String createdDtm) {
		this.createdDtm = createdDtm;
	}

	
}
