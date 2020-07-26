package com.gencode.issuetool.unittest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;

import com.gencode.issuetool.client.LogWrapper;
import com.gencode.issuetool.client.RestClient;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.ResultObj;
import com.gencode.issuetool.io.SortDirection;
import com.gencode.issuetool.obj.AuthUserInfo;
import com.gencode.issuetool.obj.NoticeBoard;
import com.gencode.issuetool.obj.NoticeBoardEx;
import com.gencode.issuetool.obj.StatsPerDay;
import com.gencode.issuetool.obj.UserInfo;
import com.gencode.issuetool.websocket.obj.StompMessage;
import com.google.gson.reflect.TypeToken;

public class UnitTestRunnerDashBoard {
	LogWrapper logger = new LogWrapper(UnitTestRunnerDashBoard.class);
	
    TestData testData; 
    String token;
    
	public UnitTestRunnerDashBoard() {
	}

	@Before 
	public void setUp() throws Exception {
		testData = new TestData();
	}
	
    /**
     * test title:로그인한 후 정상인증처리하고 token가져오기
     * description:
     * 
     * test id:
     * test step:
     * 1. admin계정으로 로그인
     * 2. token으로 사용자목록 가져오기
     * 
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Test
    public void runLoginAndGetCustInboundCount() {
    	try {
        	RestClient<AuthUserInfo> restClient = new RestClient<AuthUserInfo>();
        	Type type = new TypeToken<ResultObj<AuthUserInfo>>() {}.getType();
        	ResultObj<AuthUserInfo> result = restClient.callJsonHttp(testData.URL + "/auth/login", testData.getUserInfoAdmin(), type);
        	
        	logger.info(result.getResultCode());
        	logger.info("[["+result.getItem().toString());
        	logger.assertResult("Login", "admin계정으로 로그인", !result.getItem().getToken().isEmpty());
        	AuthUserInfo authUserInfo = (AuthUserInfo)result.getItem();
        	token =  authUserInfo.getToken();
        	
        	RestClient<List<StatsPerDay>> restClient2 = new RestClient<List<StatsPerDay>>(token);
        	ResultObj<List<StatsPerDay>> list = restClient2.callJsonHttp(testData.URL 
        			+ "/dashboard/custInboundCount/"+authUserInfo.getUserInfo().getBizId(), "");
        	logger.assertResult("GetCustInboundCount", "token으로 사용자인입건 가져오기", list.getItem().toArray().length>0);
    		
        	RestClient<List<StatsPerDay>> restClient3 = new RestClient<List<StatsPerDay>>(token);
        	ResultObj<List<StatsPerDay>> list3 = restClient3.callJsonHttp(testData.URL 
        			+ "/dashboard/responseCountByBiz/"+authUserInfo.getUserInfo().getBizId(), "");
        	logger.assertResult("GetResponseCountByBiz", "token으로 법인별 응답건 가져오기", list3.getItem().toArray().length>0);

        	RestClient<List<StatsPerDay>> restClient4 = new RestClient<List<StatsPerDay>>(token);
        	ResultObj<List<StatsPerDay>> list4 = restClient4.callJsonHttp(testData.URL 
        			+ "/dashboard/responseCountByAgent/"+authUserInfo.getUserInfo().getAgentId(), "");
        	logger.assertResult("GetResponseCountByAgent", "token으로 상담원별 응답건 가져오기", list3.getItem().toArray().length>0);

    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
    /**
	 * 로그인처리
	 * @param loginUser
	 * @return
	 */
	AuthUserInfo _callLogin(String testTitle, UserInfo loginUser) {
		RestClient<AuthUserInfo> restClient = new RestClient<AuthUserInfo>();
		Type type = new TypeToken<ResultObj<AuthUserInfo>>() {}.getType();
		ResultObj<AuthUserInfo> result = restClient.callJsonHttp(testData.URL + "/auth/login", loginUser, type);
		
		logger.info(result.getResultCode());
		logger.info("[["+result.getItem().toString());
		AuthUserInfo authUserInfo = (AuthUserInfo)result.getItem();
		logger.assertResult("Login", testTitle, ResultObj.isSuccess(result.getResultCode()));
	
		return authUserInfo;
	}
//
//	List<StatsPerDay> _callAgentList(String testTitle, boolean flag) {
//		String url = testData.URL + "/user/search";
//		Type type = new TypeToken<ResultObj<List<UserInfo>>>() {}.getType();
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("role", "O");
//		ResultObj<List<UserInfo>> result = (ResultObj<List<UserInfo>>) (new RestClient<List<UserInfo>>(token)).callJsonHttp(url, map, type);
//		logger.debug("_callAgentList result["+result+"]");
//		logger.assertResult("_callAgentList", testTitle, flag && ResultObj.isSuccess(result.getResultCode()) );
//		return result.getItem();
//	}
//    
    
}


