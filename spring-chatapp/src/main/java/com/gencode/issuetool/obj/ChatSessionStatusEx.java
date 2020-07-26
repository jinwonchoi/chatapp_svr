package com.gencode.issuetool.obj;

public class ChatSessionStatusEx extends ChatSessionStatus{
	protected ChatConsultDetails chatConsultDetails;
	protected CustomerInfo customerInfo;
	protected UserInfo agentInfo;
	public ChatSessionStatusEx() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ChatSessionStatusEx(long id, ChatConsultDetails chatConsultDetails, CustomerInfo customerInfo, UserInfo agentInfo, long lastMessageId,
			String lastMessage, int unreadCnt, String direction, String bizId, String country, String lang) {
		super();
		this.id = id;
		this.chatId = chatConsultDetails.id;
		this.chatConsultDetails = chatConsultDetails;
		this.customerId = customerInfo.customerId;
		this.customerInfo = customerInfo;
		this.agentId = agentInfo.agentId;
		this.agentInfo = agentInfo;
		this.lastMessageId = lastMessageId;
		this.lastMessage = lastMessage;
		this.unreadCnt = unreadCnt;
		this.direction = direction;
		this.bizId = bizId;
		this.country = country;
		this.lang = lang;
		
	}
	public ChatConsultDetails getChatConsultDetails() {
		return chatConsultDetails;
	}
	public void setChatConsultDetails(ChatConsultDetails chatConsultDetails) {
		this.chatConsultDetails = chatConsultDetails;
	}
	public CustomerInfo getCustomerInfo() {
		return customerInfo;
	}
	public void setCustomerInfo(CustomerInfo customerInfo) {
		this.customerInfo = customerInfo;
	}
	public UserInfo getAgentInfo() {
		return agentInfo;
	}
	public void setAgentInfo(UserInfo agentInfo) {
		this.agentInfo = agentInfo;
	}
}
