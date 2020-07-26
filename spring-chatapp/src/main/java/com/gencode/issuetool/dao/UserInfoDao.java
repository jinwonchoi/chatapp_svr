package com.gencode.issuetool.dao;

import java.util.Optional;

import com.gencode.issuetool.obj.UserInfo;

public interface UserInfoDao extends Dao<UserInfo> {
 	Optional<UserInfo> login(String loginId);
	void activate(UserInfo userInfo);
}
