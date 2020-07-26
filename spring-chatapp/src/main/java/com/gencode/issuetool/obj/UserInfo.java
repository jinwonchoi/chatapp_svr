package com.gencode.issuetool.obj;

import java.time.LocalDateTime;

import com.gencode.issuetool.etc.Utils;

public class UserInfo extends Pojo {

	protected long id;
	protected String loginId;
	protected String userName;
	protected String userEmail;
	protected String role;
	protected String agentId;
	protected String bizId;
	protected String groupId;
	protected String lang;
	protected String country;
	protected String firstName;
	protected String lastName;
	protected String officePhone;
	protected String cellPhone;
	protected String address;
	protected String passwd;
	protected String authKey;
	protected String useYn;
	protected String passwdUpdateDate;
	protected String userProfile;
	protected String confirmYn;
	protected String profileUrl;
	protected String accessToken;
	protected long noticeId;
	protected String registeredDtm;
	protected String createdDtm;
	protected String updatedDtm;
	
	public UserInfo() {}
	
	public UserInfo(String loginId, String passwd) {
		super();
		this.loginId = loginId;
		this.passwd = passwd;
	}
	
	public UserInfo(String loginId, String userName, String userEmail, String role, String agentId, String bizId,
			String groupId, String lang, String country, String firstName, String lastName, String officePhone,
			String cellPhone, String address, String passwd, String authKey, String useYn, String passwdUpdateDate,
			String userProfile, String confirmYn, String profileUrl, String accessToken, long noticeId) {
		super();
		this.loginId = loginId;
		this.userName = userName;
		this.userEmail = userEmail;
		this.role = role;
		this.agentId = agentId;
		this.bizId = bizId;
		this.groupId = groupId;
		this.lang = lang;
		this.country = country;
		this.firstName = firstName;
		this.lastName = lastName;
		this.officePhone = officePhone;
		this.cellPhone = cellPhone;
		this.address = address;
		this.passwd = passwd;
		this.authKey = authKey;
		this.useYn = useYn;
		this.passwdUpdateDate = passwdUpdateDate;
		this.userProfile = userProfile;
		this.confirmYn = confirmYn;
		this.profileUrl = profileUrl;
		this.accessToken = accessToken;
		this.noticeId = noticeId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getPasswdUpdateDate() {
		return passwdUpdateDate;
	}

	public void setPasswdUpdateDate(String passwdUpdateDate) {
		this.passwdUpdateDate = passwdUpdateDate;
	}

	public String getRegisteredDtm() {
		return registeredDtm;
	}

	public void setRegisteredDtm(String registeredDtm) {
		this.registeredDtm = registeredDtm;
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

	public String getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
	}

	public String getConfirmYn() {
		return confirmYn;
	}

	public void setConfirmYn(String confirmYn) {
		this.confirmYn = confirmYn;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(long noticeId) {
		this.noticeId = noticeId;
	}
}
