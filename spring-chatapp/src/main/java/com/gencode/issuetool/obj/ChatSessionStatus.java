package com.gencode.issuetool.obj;

public class ChatSessionStatus {
	protected long id;
	protected long chatId;
	protected String customerId;
	protected String agentId;
	protected long lastMessageId;
	protected String lastMessage;
	protected int unreadCnt;
	protected String direction;
	protected String bizId;
	protected String country;
	protected String lang;
	protected String updatedDtm;
	protected String createdDtm;
	public ChatSessionStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChatSessionStatus(long id, long chatId, String customerId, String agentId, long lastMessageId,
			String lastMessage, int unreadCnt, String direction, String bizId, String country, String lang) {
		super();
		this.id = id;
		this.chatId = chatId;
		this.customerId = customerId;
		this.agentId = agentId;
		this.lastMessageId = lastMessageId;
		this.lastMessage = lastMessage;
		this.unreadCnt = unreadCnt;
		this.direction = direction;
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
	public long getChatId() {
		return chatId;
	}
	public void setChatId(long chatId) {
		this.chatId = chatId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public long getLastMessageId() {
		return lastMessageId;
	}
	public void setLastMessageId(long lastMessageId) {
		this.lastMessageId = lastMessageId;
	}
	public String getLastMessage() {
		return lastMessage;
	}
	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}
	public int getUnreadCnt() {
		return unreadCnt;
	}
	public void setUnreadCnt(int unreadCnt) {
		this.unreadCnt = unreadCnt;
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
	public String getUpdatedDtm() {
		return updatedDtm;
	}
	public void setUpdatedDtm(String updatedDtm) {
		this.updatedDtm = updatedDtm;
	}
	public String getCreatedDtm() {
		return createdDtm;
	}
	public void setCreatedDtm(String createdDtm) {
		this.createdDtm = createdDtm;
	}

}
