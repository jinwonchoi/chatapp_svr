package com.gencode.issuetool.obj;

public class MessageQueue extends Pojo {
	protected long id;
	protected String customerId;
	protected long chatId;
	protected String direction;
	protected String bizId;
	protected String country;
	protected String lang;
	protected String status;
	protected String message;
	protected String createdDtm;
	public MessageQueue() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public MessageQueue(String customerId, long chatId, String direction, String bizId,
			String country, String lang, String status, String message) {
		super();
		this.customerId = customerId;
		this.chatId = chatId;
		this.direction = direction;
		this.bizId = bizId;
		this.country = country;
		this.lang = lang;
		this.status = status;
		this.message = message;
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
	public long getChatId() {
		return chatId;
	}
	public void setChatId(long chatId) {
		this.chatId = chatId;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCreatedDtm() {
		return createdDtm;
	}
	public void setCreatedDtm(String createdDtm) {
		this.createdDtm = createdDtm;
	}
}
