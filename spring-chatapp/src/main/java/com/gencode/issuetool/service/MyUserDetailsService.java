package com.gencode.issuetool.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.NotSupportedException;

//import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gencode.issuetool.dao.UserInfoDao;
import com.gencode.issuetool.etc.ReturnCode;
import com.gencode.issuetool.etc.Utils;
import com.gencode.issuetool.exception.ApplicationException;
import com.gencode.issuetool.exception.TooManyRowException;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
//import com.gencode.issuetool.mail.EmailService;
import com.gencode.issuetool.obj.UserInfo;

@Service
public class MyUserDetailsService implements UserDetailsService {
	private final static Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
	@Autowired
	private UserInfoDao userInfoDao;
	
	//@Autowired
	//private EmailService emailService;
		
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		logger.info("loadUserByUsername:loingId="+loginId);
		Optional<UserInfo> userInfo = userInfoDao.login(loginId);
		if (!userInfo.isPresent()) {
			throw new UsernameNotFoundException("User not found by loginId:"+loginId);
		}
		return toUserDetails(userInfo.get());
	}

	private UserDetails toUserDetails(UserInfo userInfo) {
		return org.springframework.security.core.userdetails.User.withUsername(userInfo.getAgentId())
				.password(userInfo.getPasswd())
				.authorities(userInfo.getRole()).build();
	}
	
	/**
	 * 1. 
	 * @param user
	 * @throws Exception 
	 */
	@Transactional
	public void register(UserInfo userInfo) throws Exception {
		try { 
			//email등록된것이 있으면 오류
			if (hasLoginId(userInfo.getLoginId())) {
				throw new ApplicationException(ReturnCode.ALREADY_EXISTS_USERID);
			}
			
			if (hasUserEmail(userInfo.getUserEmail())) {
				throw new ApplicationException(ReturnCode.ALREADY_EXISTS_EMAIL);
			}
			logger.info(userInfo.getPasswd());
			userInfo.setPasswd(passwordEncoder.encode(userInfo.getPasswd()));
			logger.info(userInfo.getPasswd());
			userInfo.setAuthKey(Utils.getRandomPassword());
			userInfo.setConfirmYn("N");
			userInfoDao.register(userInfo);
			//emailService.sendRegActivationMail(user.getEmail(), user.getUsername(), user.getUserid(),user.getEmailAuthKey());
		} catch (Exception e) {
			logger.error("register fail:"+userInfo, e);
			throw e;
		}
		
	}
	
	public boolean hasLoginId(String loginId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginId", loginId);
		return (userInfoDao.search(map).get().size() > 0);
	}
	
	public boolean hasUserEmail(String userEmail) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userEmail", userEmail);		
		return (userInfoDao.search(map).get().size() > 0);
	}
	
	@Transactional
	public boolean activate(UserInfo userInfo) {
		try {
			throw new NotSupportedException();
//			userInfo.setConfirmYn("Y");
//			userInfoDao.activate(userInfo);
//			return true;
		} catch (Exception e) {
			logger.error("activate fail:"+userInfo, e);
			return false;
		}
	}

	public void sendUseridNotification(String email) throws Exception {
//		Map<String, String> map = new HashMap<String, String>();
//		try {
//			map.put("email", email);		
//			Optional<List<User>> userList = userDao.search(map);
//			User user = userList.get().get(0);
//			//emailService.sendUseridNotification(user.getEmail(), user.getUserid(), user.getUsername());
//		} catch (Exception e) {
//			logger.error("userid notification fail:"+email, e);
//			throw e;
//		}
	}
	
	public void sendPasswordNotification(String userid, String email) throws Exception {
//		Map<String, String> map = new HashMap<String, String>();
//		User user = null;
//		try {
//			map.put("userid", userid);		
//			map.put("email", email);		
//			Optional<List<User>> userList = userDao.search(map);
//			user = userList.get().get(0);
//			String password = Utils.getRandomPassword();
//			user.setPassword(passwordEncoder.encode(password));
//			logger.debug(user.toString());
//			userDao.update(user);
//			//emailService.sendPasswordNotification(user.getEmail(), password, user.getUsername());
//		} catch (Exception e) {
//			logger.error("password notification fail:"+user, e);
//			throw e;
//		}
	}

	public Optional<UserInfo> load(long id) {
		return userInfoDao.load(id);
	}
	
	public Optional<UserInfo> load(String loginId) {
		return userInfoDao.login(loginId);
	}
	
	@Transactional
	public void delete(long id) {
		userInfoDao.delete(id);
	}

	@Transactional
	public void update(UserInfo userInfo) throws Exception {
		if (hasUserEmail(userInfo.getUserEmail())) {
			throw new ApplicationException(ReturnCode.ALREADY_EXISTS_EMAIL);
		}
		if (userInfo.getPasswd()!=null&&!userInfo.getPasswd().trim().equals("")) {
			userInfo.setPasswd(passwordEncoder.encode(userInfo.getPasswd()));
		}
		userInfoDao.update(userInfo);
	}
	
	public Optional<List<UserInfo>> loadAll() {
		return userInfoDao.loadAll();
	}
	
	public Optional<List<UserInfo>> search(Map<String, String> map) {
		return userInfoDao.search(map);
	}
	
	
	/**
	 * 
	 * @param page
	 * @return
	 */
	public Optional<PageResultObj<List<UserInfo>>> search(PageRequest req) {
		return userInfoDao.search(req);
	}
}
