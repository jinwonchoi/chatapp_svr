package com.gencode.issuetool.obj;

public class ChatConsultDetailsEx extends ChatConsultDetails {
	protected CustomerInfo customerInfo;
	protected UserInfo agentInfo;
	protected ChatConsultDetails prevChat;
	
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
	public ChatConsultDetails getPrevChat() {
		return prevChat;
	}
	public void setPrevChat(ChatConsultDetails prevChat) {
		this.prevChat = prevChat;
	}
	
}
