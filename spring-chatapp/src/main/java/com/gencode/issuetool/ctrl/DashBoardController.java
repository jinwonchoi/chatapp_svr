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

import com.gencode.issuetool.service.DashBoardService;
import com.gencode.issuetool.service.MyUserDetailsService;
import com.gencode.issuetool.config.JwtTokenProvider;
import com.gencode.issuetool.etc.ReturnCode;
import com.gencode.issuetool.exception.ApplicationException;
import com.gencode.issuetool.exception.TooManyRowException;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.ResultObj;
import com.gencode.issuetool.obj.GroupSum;
import com.gencode.issuetool.obj.StatsGoal;
import com.gencode.issuetool.obj.StatsPerDay;
import com.gencode.issuetool.obj.UserInfo;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "${cors_url}")
public class DashBoardController {

	private final static Logger logger = LoggerFactory.getLogger(DashBoardController.class);
	
	@Autowired
	private DashBoardService dashBoardService;
	
	
	/* 사업부 사용자 인입 건수
	 * @return StatsPerDay 
	 *
	 */    
	@RequestMapping("/custInboundCount/{bizId}") 
	ResultObj<List<StatsPerDay>> getCustomerInboundCount(@PathVariable(name="bizId") String bizId) {
		try {
			Optional<List<StatsPerDay>> statsPerDay = dashBoardService.getCustomerInboundCount(bizId);
			return new ResultObj<List<StatsPerDay>>(ReturnCode.SUCCESS, statsPerDay.get());
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}

	/* 
	 * 법인 응대 건수 
	 * @return StatsPerDay
	 */
	@RequestMapping("/responseCountByBiz/{bizId}") 
	ResultObj<List<StatsPerDay>> getResponseCount(@PathVariable(name="bizId") String bizId) {
		try {
			Optional<List<StatsPerDay>> statsPerDay = dashBoardService.getResponseCountByBiz(bizId);
			return new ResultObj<List<StatsPerDay>>(ReturnCode.SUCCESS, statsPerDay.get());
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}

	/* 
	 * 상담원 응대 건수 
	 * @return StatsPerDay
	 */
	@RequestMapping("/responseCountByAgent/{agentId}") 
	ResultObj<List<StatsPerDay>> getResponseCountByAgent(@PathVariable(name="agentId") String agentId) {
		try {
			Optional<List<StatsPerDay>> statsPerDay = dashBoardService.getResponseCountByAgent(agentId);
			return new ResultObj<List<StatsPerDay>>(ReturnCode.SUCCESS, statsPerDay.get());
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}

	/*
	 * 법인 해결 건수
	 * @return StatsPerDay 
	 */
	@RequestMapping("/resolvedCountByBiz/{bizId}") 
	ResultObj<List<StatsPerDay>> getResolvedCount(@PathVariable(name="bizId") String bizId) {
		try {
			Optional<List<StatsPerDay>> statsPerDay = dashBoardService.getResolvedCountByBiz(bizId);
			return new ResultObj<List<StatsPerDay>>(ReturnCode.SUCCESS, statsPerDay.get());
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}

	/*
	 * 상담원 해결 건수
	 * @return StatsPerDay 
	 */
	@RequestMapping("/resolvedCountByAgent/{agentId}") 
	ResultObj<List<StatsPerDay>> getResolvedCountByAgent(@PathVariable(name="agentId") String agentId) {
		try {
			Optional<List<StatsPerDay>> statsPerDay = dashBoardService.getResolvedCountByAgent(agentId);
			return new ResultObj<List<StatsPerDay>>(ReturnCode.SUCCESS, statsPerDay.get());
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}
	
	/* 
	 * 해결건 집계 (Goal Overview)
	 * @return StatsGoal 
	 */
	@RequestMapping("/resolvedGoal/{bizId}") 
	ResultObj<StatsGoal> getResolvedGoal(@PathVariable(name="bizId") String bizId) {
		try {
			Optional<StatsGoal> statsGoal = dashBoardService.getResolvedGoal(bizId);
			return new ResultObj<StatsGoal>(ReturnCode.SUCCESS, statsGoal.get());
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}
	
	/* 
	 * 좌측 상담현황
	 * @return  
	 */ 
	// chat/session/listex
	/* 
	 * 우측 공지사항
	 * @return 
	 */
	// notice-board/search

	/* 
	 * 상담요청종류
	 * @return GroupSum 
	 */
	@RequestMapping("/consultTypeCount/{bizId}") 
	ResultObj<List<GroupSum>> getConsultTypeCount(@PathVariable(name="bizId") String bizId) {
		try {
			Optional<List<GroupSum>> consultTypeCount = dashBoardService.getConsultTypeCount(bizId);
			return new ResultObj<List<GroupSum>>(ReturnCode.SUCCESS, consultTypeCount.get());
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}
	
	/* 
	 * 상담결과종류
	 * @return GroupSum
	 */
	@RequestMapping("/consultStatusCount/{bizId}") 
	ResultObj<List<GroupSum>> getConsultStatusCount(@PathVariable(name="bizId") String bizId) {
		try {
			Optional<List<GroupSum>> consultTypeCount = dashBoardService.getConsultStatusCount(bizId);
			return new ResultObj<List<GroupSum>>(ReturnCode.SUCCESS, consultTypeCount.get());
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}

	
//	@RequestMapping("/{id}") 
//	ResultObj<UserInfo> getUser(@PathVariable(name="id") String id) {
//		try {
//			Optional<UserInfo> userInfo = userService.load(Long.parseLong(id));
//			return new ResultObj<UserInfo>(ReturnCode.SUCCESS, userInfo.get());
//		} catch (Exception e) {
//			logger.error("normal error", e);
//			return ResultObj.errorUnknown();
//		}
//	}
//
//	@RequestMapping("/list") 
//	ResultObj<List<UserInfo>> getList() {
//		try {
//			logger.debug("getList");
//			return userService.loadAll().map(t->(t.size()>0)
//					?ResultObj.<List<UserInfo>>success(t)
//							:ResultObj.<List<UserInfo>>error(ReturnCode.USER_NOT_FOUND)).get();
//		} catch (Exception e) {
//			logger.error("normal error", e);
//			return ResultObj.errorUnknown();
//		}
//	}
//	
//	@RequestMapping(method=RequestMethod.POST, value="/search")
//	public ResultObj<List<UserInfo>> searchUser(@RequestBody HashMap<String, String> map) {
//		try {
//			System.out.println(map.toString());
//			Optional<List<UserInfo>> list = userService.search(map);
//			if (list.get().size() == 0) {
//				return new ResultObj<List<UserInfo>>(ReturnCode.USER_NOT_FOUND, null);
//			} else {
//				return new ResultObj<List<UserInfo>>(ReturnCode.SUCCESS, list.get());
//			}
//		} catch (Exception e) {
//			logger.error("normal error", e);
//			return null;
//		}
//	}
//
//	@RequestMapping(method=RequestMethod.POST, value="/page")
//	public PageResultObj<List<UserInfo>> pageUser(@RequestBody PageRequest pageRequest) {
//		try {
//			logger.info(pageRequest.toString());
//			Optional<PageResultObj<List<UserInfo>>> list = userService.search(pageRequest);
//			if (list.get().getItem().size() == 0) {
//				return new PageResultObj<List<UserInfo>>(ReturnCode.USER_NOT_FOUND, null);
//			} else {
//				return new PageResultObj<List<UserInfo>>(ReturnCode.SUCCESS, list.get());
//			}
//		} catch (Exception e) {
//			logger.error("normal error", e);
//			return PageResultObj.<List<UserInfo>>errorUnknown();
//		}
//	}
//	
//	
//	/**
//	 * 
//	 * @param user
//	 * @return
//	 */
//	@RequestMapping(method=RequestMethod.POST, value="/update")
//	ResultObj<String> updateUser(@RequestBody UserInfo user) {
//		try {
//			userService.update(user);
//			return ResultObj.success();
//		} catch (Exception e) {
//			logger.error("normal error", e);
//			return ResultObj.errorUnknown();
//		}
//	}
//	/**
//	 * 
//	 * @param user
//	 * @return
//	 */
//	@RequestMapping(method=RequestMethod.POST, value="/add")
//	ResultObj<String> addUser(@RequestBody UserInfo user) {
//		try {
//			userService.register(user);
//			return ResultObj.success();
//		} catch (Exception e) {
//			logger.error("normal error", e);
//			return ResultObj.errorUnknown();
//		}
//	}
//
//	/**
//	 * 
//	 * @param user
//	 * @return
//	 */
//	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
//	ResultObj<String> deleteUser(@PathVariable(name="id") String id) {
//		try {
//			userService.delete(Long.parseLong(id));
//			return ResultObj.success();
//		} catch (Exception e) {
//			logger.error("normal error", e);
//			return ResultObj.errorUnknown();
//		}
//	}
//	
}
