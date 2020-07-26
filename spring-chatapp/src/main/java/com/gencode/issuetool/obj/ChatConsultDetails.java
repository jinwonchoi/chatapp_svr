package com.gencode.issuetool.obj;

import java.time.LocalDateTime;

import com.gencode.issuetool.etc.Constant;

public class ChatConsultDetails {
	protected long id;
	protected String customerId;
	protected String agentId;
	protected String bizId;
	protected String country;
	protected String lang;
	protected String consultStatus;
	protected long refSessionId;
	protected long prevChatId;
	protected String consultType;
	protected String chatComment;
	protected String updatedDtm;
	protected String createdDtm;

	public ChatConsultDetails() {
		super();
	}

	public ChatConsultDetails(ChatSessionStatus t) {
		super();
		this.customerId = t.getCustomerId();
		this.agentId = t.getAgentId();
		this.bizId = t.getBizId();
		this.country = t.getCountry();
		this.lang = t.getLang();
		this.consultStatus = Constant.CHAT_CONSULT_DETAILS_CONSULT_STATUS_OPEN.get();
		this.refSessionId = t.getId();
		//this.prevChatId = prevChatId;
		this.consultType = Constant.CHAT_CONSULT_DETAILS_CONSULT_TYPE_BASIC.get();
	}
	
	public ChatConsultDetails(String customerId, String agentId, String bizId, String country, String lang,
			String consultStatus, long refSessionId, long prevChatId, String consultType, String chatComment) {
		super();
		this.customerId = customerId;
		this.agentId = agentId;
		this.bizId = bizId;
		this.country = country;
		this.lang = lang;
		this.consultStatus = consultStatus;
		this.refSessionId = refSessionId;
		this.prevChatId = prevChatId;
		this.consultType = consultType;
		this.chatComment = chatComment;
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

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
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

	public String getConsultStatus() {
		return consultStatus;
	}

	public void setConsultStatus(String consultStatus) {
		this.consultStatus = consultStatus;
	}

	public long getRefSessionId() {
		return refSessionId;
	}

	public void setRefSessionId(long refSessionId) {
		this.refSessionId = refSessionId;
	}

	public long getPrevChatId() {
		return prevChatId;
	}

	public void setPrevChatId(long prevChatId) {
		this.prevChatId = prevChatId;
	}

	public String getConsultType() {
		return consultType;
	}

	public void setConsultType(String consultType) {
		this.consultType = consultType;
	}

	public String getChatComment() {
		return chatComment;
	}

	public void setChatComment(String chatComment) {
		this.chatComment = chatComment;
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
