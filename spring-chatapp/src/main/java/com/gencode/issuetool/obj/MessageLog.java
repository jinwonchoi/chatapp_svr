package com.gencode.issuetool.obj;

public class MessageLog extends Pojo {
	protected long messageId;
	protected String customerId;
	protected long chatId;
	protected String direction;
	protected String bizId;
	protected String country;
	protected String lang;
	protected String status;
	protected String message;
	protected String queCreatedDtm;
	protected String createdDtm;
	
	public MessageLog() {
		super();
	}
	
	public MessageLog(MessageQueue t) {
		super();
		this.messageId = t.getId();
		this.customerId = t.getCustomerId();
		this.chatId = t.getChatId();
		this.direction = t.getDirection();
		this.bizId = t.getBizId();
		this.country = t.getCountry();
		this.lang = t.getLang();
		this.status = t.getStatus();
		this.message = t.getMessage();
		this.queCreatedDtm = t.getCreatedDtm();
		this.createdDtm = t.getCreatedDtm();
	}
	
	public MessageLog(long messageId, String customerId, long chatId, String direction, String bizId, String country,
			String lang, String status, String message) {
		super();
		this.messageId = messageId;
		this.customerId = customerId;
		this.chatId = chatId;
		this.direction = direction;
		this.bizId = bizId;
		this.country = country;
		this.lang = lang;
		this.status = status;
		this.message = message;
	}
	
	public long getMessageId() {
		return messageId;
	}
	public void setMessageId(long messageId) {
		this.messageId = messageId;
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
	public String getQueCreatedDtm() {
		return queCreatedDtm;
	}
	public void setQueCreatedDtm(String queCreatedDtm) {
		this.queCreatedDtm = queCreatedDtm;
	}
	public String getCreatedDtm() {
		return createdDtm;
	}
	public void setCreatedDtm(String createdDtm) {
		this.createdDtm = createdDtm;
	}	
}
