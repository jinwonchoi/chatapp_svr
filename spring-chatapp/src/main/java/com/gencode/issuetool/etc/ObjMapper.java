package com.gencode.issuetool.etc;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.gencode.issuetool.obj.ChatConsultDetails;
import com.gencode.issuetool.obj.CustomerInfo;
import com.gencode.issuetool.obj.NoticeBoard;
import com.gencode.issuetool.obj.UserInfo;

public class ObjMapper {

	static public ChatConsultDetails toChatConsultDetails(String prefix, ResultSet resultSet) throws SQLException {
		ChatConsultDetails obj = new ChatConsultDetails();
		if (resultSet.getLong(prefix+"ID")==0) return null;
		obj.setId(resultSet.getLong(prefix+"ID"));
		obj.setCustomerId(resultSet.getString(prefix+"CUSTOMER_ID"));
		obj.setAgentId(resultSet.getString(prefix+"AGENT_ID"));
		obj.setBizId(resultSet.getString(prefix+"BIZ_ID"));
		obj.setCountry(resultSet.getString(prefix+"COUNTRY"));
		obj.setLang(resultSet.getString(prefix+"LANG"));
		obj.setConsultStatus(resultSet.getString(prefix+"CONSULT_STATUS"));
		obj.setRefSessionId(resultSet.getLong(prefix+"REF_SESSION_ID"));
		obj.setPrevChatId(resultSet.getLong(prefix+"PREV_CHAT_ID"));
		obj.setConsultType(resultSet.getString(prefix+"CONSULT_TYPE"));
		obj.setChatComment(resultSet.getString(prefix+"CHAT_COMMENT"));
		obj.setUpdatedDtm(Utils.DtToStr(resultSet.getTimestamp(prefix+"UPDATED_DTM")));
		obj.setCreatedDtm(Utils.DtToStr(resultSet.getTimestamp(prefix+"CREATED_DTM")));

		return obj;
	}
	
	static public CustomerInfo toCustomerInfo(String prefix, ResultSet resultSet) throws SQLException {
		CustomerInfo obj = new CustomerInfo();
		if (resultSet.getLong(prefix+"ID")==0) return null;
		obj.setId(resultSet.getLong(prefix+"ID"));
		obj.setCustomerId(resultSet.getString(prefix+"CUSTOMER_ID"));
		obj.setIdType(resultSet.getString(prefix+"ID_TYPE"));
		obj.setBizId(resultSet.getString(prefix+"BIZ_ID"));
		obj.setCountry(resultSet.getString(prefix+"COUNTRY"));
		obj.setLang(resultSet.getString(prefix+"LANG"));
		obj.setCreatedDtm(Utils.DtToStr(resultSet.getTimestamp(prefix+"CREATED_DTM")));
		return obj;
	}

	static public UserInfo toUserInfo(String prefix, ResultSet resultSet) throws SQLException {
		UserInfo obj = new UserInfo();
		if (resultSet.getLong(prefix+"ID")==0) return null;
		obj.setId(resultSet.getLong(prefix+"ID"));
		obj.setLoginId(resultSet.getString(prefix+"LOGIN_ID"));
		obj.setUserName(resultSet.getString(prefix+"USER_NAME"));
		obj.setUserEmail(resultSet.getString(prefix+"USER_EMAIL"));
		obj.setRole(resultSet.getString(prefix+"ROLE"));
		obj.setAgentId(resultSet.getString(prefix+"AGENT_ID"));
		obj.setBizId(resultSet.getString(prefix+"BIZ_ID"));
		obj.setGroupId(resultSet.getString(prefix+"GROUP_ID"));
		obj.setLang(resultSet.getString(prefix+"LANG"));
		obj.setCountry(resultSet.getString(prefix+"COUNTRY"));
		obj.setFirstName(resultSet.getString(prefix+"FIRST_NAME"));
		obj.setLastName(resultSet.getString(prefix+"LAST_NAME"));
		obj.setOfficePhone(resultSet.getString(prefix+"OFFICE_PHONE"));
		obj.setCellPhone(resultSet.getString(prefix+"CELL_PHONE"));
		obj.setAddress(resultSet.getString(prefix+"ADDRESS"));
		//obj.setPasswd(resultSet.getString(prefix+"PASSWD"));
		//obj.setAuthKey(resultSet.getString(prefix+"AUTH_KEY"));
		obj.setUseYn(resultSet.getString(prefix+"USE_YN"));
		obj.setPasswdUpdateDate(resultSet.getString(prefix+"PASSWD_UPDATE_DATE"));
		obj.setUserProfile(resultSet.getString(prefix+"USER_PROFILE"));
		obj.setConfirmYn(resultSet.getString(prefix+"CONFIRM_YN"));
		obj.setProfileUrl(resultSet.getString(prefix+"PROFILE_URL"));
		obj.setAccessToken(resultSet.getString(prefix+"ACCESS_TOKEN"));
		obj.setNoticeId(resultSet.getLong(prefix+"NOTICE_ID"));		
		obj.setRegisteredDtm(Utils.DtToStr(resultSet.getTimestamp(prefix+"REGISTERED_DTM")));
		obj.setUpdatedDtm(Utils.DtToStr(resultSet.getTimestamp(prefix+"UPDATED_DTM")));
		obj.setCreatedDtm(Utils.DtToStr(resultSet.getTimestamp(prefix+"CREATED_DTM")));
		return obj;
	}
}
