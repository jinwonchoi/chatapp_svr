package com.gencode.issuetool.ctrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gencode.issuetool.service.MyUserDetailsService;
import com.gencode.issuetool.config.JwtTokenProvider;
import com.gencode.issuetool.etc.ReturnCode;
import com.gencode.issuetool.exception.ApplicationException;
import com.gencode.issuetool.exception.TooManyRowException;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.ResultObj;
import com.gencode.issuetool.obj.UserInfo;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "${cors_url}")
public class UserController {

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private MyUserDetailsService userService;
	
	@RequestMapping("/{id}") 
	ResultObj<UserInfo> getUser(@PathVariable(name="id") String id) {
		try {
			Optional<UserInfo> userInfo = userService.load(Long.parseLong(id));
			return new ResultObj<UserInfo>(ReturnCode.SUCCESS, userInfo.get());
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}

	@RequestMapping("/list") 
	ResultObj<List<UserInfo>> getList() {
		try {
			logger.debug("getList");
			return userService.loadAll().map(t->(t.size()>0)
					?ResultObj.<List<UserInfo>>success(t)
							:ResultObj.<List<UserInfo>>error(ReturnCode.USER_NOT_FOUND)).get();
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/search")
	public ResultObj<List<UserInfo>> searchUser(@RequestBody Map<String, String> map) {
		try {
			System.out.println(map.toString());
			Optional<List<UserInfo>> list = userService.search(map);
			if (list.get().size() == 0) {
				return new ResultObj<List<UserInfo>>(ReturnCode.USER_NOT_FOUND, null);
			} else {
				return new ResultObj<List<UserInfo>>(ReturnCode.SUCCESS, list.get());
			}
		} catch (Exception e) {
			logger.error("normal error", e);
			return null;
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="/page")
	public PageResultObj<List<UserInfo>> pageUser(@RequestBody PageRequest pageRequest) {
		try {
			logger.info(pageRequest.toString());
			Optional<PageResultObj<List<UserInfo>>> list = userService.search(pageRequest);
			if (list.get().getItem().size() == 0) {
				return new PageResultObj<List<UserInfo>>(ReturnCode.USER_NOT_FOUND, null);
			} else {
				return new PageResultObj<List<UserInfo>>(ReturnCode.SUCCESS, list.get());
			}
		} catch (Exception e) {
			logger.error("normal error", e);
			return PageResultObj.<List<UserInfo>>errorUnknown();
		}
	}
	
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/update")
	ResultObj<String> updateUser(@RequestBody UserInfo user) {
		try {
			userService.update(user);
			return ResultObj.success();
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}
	/**
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/add")
	ResultObj<String> addUser(@RequestBody UserInfo user) {
		try {
			userService.register(user);
			return ResultObj.success();
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}

	/**
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	ResultObj<String> deleteUser(@PathVariable(name="id") String id) {
		try {
			userService.delete(Long.parseLong(id));
			return ResultObj.success();
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}
	
//	/** 
//	 * file만 업로드하고 url을 리턴
//	 * @param user
//	 * @return
//	 */
//	@RequestMapping(value="/profile/upload", method=RequestMethod.POST
//			,  consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
//	ResultObj<String> upload(@RequestPart("upfile") MultipartFile multipartFile) {
//		try {
//			
//			logger.debug(multipartFile.getOriginalFilename());
//			logger.debug(multipartFile.getSize()+"");
//			String uploadFileName = userProfileService.uploadFile(multipartFile);
//			ResultObj<String>  resultObj = ResultObj.success();
//			resultObj.setItem(uploadFileName);
//			return resultObj;
//		} catch (Exception e) {
//			logger.error("normal error", e);
//			return ResultObj.errorUnknown();
//		}
//	}

//	/**
//	 * 프로파일을 리턴 
//	 * @param user
//	 * @return
//	 */
//	@RequestMapping(value="/profile/{userid}", method=RequestMethod.GET)
//	ResultObj<UserProfile> getProfile(@PathVariable(name="userid") String userid) {
//		try {			
//			logger.debug(userid);
//			Optional<UserProfile> userProfile = userProfileService.load(userid);
//			if (userProfile.isPresent()) {
//				return new ResultObj<UserProfile>(ReturnCode.SUCCESS, userProfile.get());
//			} else {
//				return ResultObj.dataNotFound();
//			}
//			
//		} catch (Exception e) {
//			logger.error("normal error", e);
//			return ResultObj.errorUnknown();
//		}
//	}
//
//	/**
//	 * 프로파일을 등록 또는 업데이트 
//	 * @param user
//	 * @return
//	 */
//	@RequestMapping(value="/profile/register", method=RequestMethod.POST)
//	ResultObj<String> registerProfile(@RequestBody UserProfile userProfile) {
//		try {			
//			logger.debug(userProfile.toString());
//			userProfileService.register(userProfile);
//			return ResultObj.success();
//		} catch (Exception e) {
//			logger.error("normal error", e);
//			return ResultObj.errorUnknown();
//		}
//	}
//
//	/** 
//	 * todo: JSON, file 한꺼번에 업로드 할경우
//	 * @param user
//	 * @return
//	 */
//	@RequestMapping(value="/profile/upload/register", method=RequestMethod.POST
//			,  consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
//	ResultObj<String> registerProfile(@RequestPart("json") UserProfile userProfile, @RequestPart("upfile") MultipartFile multipartFile) {
//		try {
//			logger.debug(userProfile.toString());
//			logger.debug(multipartFile.getOriginalFilename());
//			logger.debug(multipartFile.getSize()+"");
//			userProfileService.uploadAndRegister(userProfile, multipartFile);
//			return ResultObj.success();
//		} catch (Exception e) {
//			logger.error("normal error", e);
//			return ResultObj.errorUnknown();
//		}
//	}
}
