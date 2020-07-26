package com.gencode.issuetool.unittest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.TransportRequest;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import com.gencode.issuetool.client.LogWrapper;
import com.gencode.issuetool.client.MyStompClient;
import com.gencode.issuetool.client.RestClient;
import com.gencode.issuetool.etc.Constant;
import com.gencode.issuetool.etc.ReturnCode;
import com.gencode.issuetool.etc.UserRole;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.ResultObj;
import com.gencode.issuetool.util.JsonUtils;
import com.gencode.issuetool.websocket.obj.StompMessage;
import com.google.gson.reflect.TypeToken;
import com.gencode.issuetool.obj.AuthUserInfo;
import com.gencode.issuetool.obj.ChatConsultDetails;
import com.gencode.issuetool.obj.ChatSessionStatus;
import com.gencode.issuetool.obj.ChatSessionStatusEx;
import com.gencode.issuetool.obj.MessageLog;
import com.gencode.issuetool.obj.MessageQueue;
import com.gencode.issuetool.obj.RoleDef;
import com.gencode.issuetool.obj.UserInfo;

import java.lang.reflect.Type;


import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource("classpath:application-dev.properties")
public class UnitTestRunnerForStomp {
	
	LogWrapper logger = new LogWrapper(UnitTestRunnerForStomp.class);
    TestData testData; 
    String token = null;
    String randomCustomerId = null;
    String bizId = "A01";
    String country = "kr";
    String lang = "ko";
    
	MyStompClient myStompClientForChat;
	StompSession stompSessionForChat;
	MyStompClient myStompClientForGroup;
	StompSession stompSessionForGroup;
	String strByeMsgForChat = "Bye Nexmo";
	String strByeMsgForGroup = "Bye My Group";

    @Before
    public void setup() {
    	testData = new TestData();
    	randomCustomerId = testData.getRandomPhoneNumber();
    }
    
    /**
     * test title:채팅창 열린 상태에서 nexmo 메시지 수신후 응답 정상처리
     * description:
     * 
     * test id:
     * test step:
     * 0. login하기
     * 1. chat queue 1:1 생성 대기
     * 2. chat queue group 생성 대기
     * 3. json으로 메시지 post처리
     *    - 1:1 로 메시지 send
     * 4. chat queue 1:1 메시지 확인
     * 5. chat queue group으로 메시지 send 
     * 6. chat queue group 메시지 확인
     * 7. chat connection 종료
     * 
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Test
    public void runReceiveNexmoMsgAndAnswerMsg() throws InterruptedException, ExecutionException, TimeoutException {

    	String queueIdForChat;//agentId
    	String queueIdForGroup;//bizId
    	String strNexmoMsg = "Hi, First Message From Nexmo";
    	String strGroupMsg = "Hi, A Message for my group";

    	//0. login하기
    	AuthUserInfo authUserInfo = _callLogin("0. login하기",testData.getUserInfoAdmin());
    	token = authUserInfo.getToken();
    	queueIdForChat = authUserInfo.getUserInfo().getAgentId();
    	queueIdForGroup = authUserInfo.getUserInfo().getBizId();
    	logger.assertResult("Login", "admin계정으로 로그인", !token.isEmpty());

    	MyStompClient myStompClient = new MyStompClient("admin", token);
    	StompSession stompSession = myStompClient.getSession();
        //1. chat queue 1:1 생성 대기
    	CompletableFuture<String> completableFutureForChat = myStompClient.subscribeQueue(stompSession, queueIdForChat, strByeMsgForChat);
    	//2. chat queue group 생성 대기
    	CompletableFuture<String> completableFutureForGroup = myStompClient.subscribeQueue(stompSession, queueIdForGroup, strByeMsgForGroup);

    	//3. json으로 메시지 post처리
		String url = testData.URL + "/chat/nexmomsg";
		//RestClient<String> restClient = new RestClient();
		StompMessage message = new StompMessage("userFromNexmo",strNexmoMsg,"sdfg", queueIdForChat);
		
		ResultObj<String> resultChat = (new RestClient<String>(token)).callJsonHttp(url, message);
		logger.debug("mockNexmoMsg message["+message+"]result["+resultChat+"]");

		//4. chat queue 1:1 메시지 확인
		logger.assertResult("receiveChat1to1","4. chat queue 1:1 메시지 확인", (completableFutureForChat.get(3,SECONDS).equals(strNexmoMsg)));

//        //5. chat queue group으로 메시지 send
//        myStompClient.sendMsg(stompSession, queueIdForGroup, strGroupMsg);
//        
//        //6. chat queue group 메시지 확인
//        logger.assertResult("receiveChatGroupMsg", "6. chat queue group 메시지 확인", (completableFutureForGroup.get(3,SECONDS).equals(strGroupMsg)));
//        //7. chat connection 종료
//        myStompClient.sendMsg(stompSession, queueIdForGroup, strByeMsgForGroup);
//        Thread.sleep(1000);
//        String strByeMsg = completableFutureForGroup.get(3,SECONDS);
//        logger.info(String.format("strByeMsg[%s]", strByeMsg));
//        if (completableFutureForGroup.get(3,SECONDS).equals(strByeMsgForGroup)) {
//            stompSession.disconnect();
//            logger.info("stompSession.disconnect()");
//            logger.assertResult("safeCloseChat", "7. chat connection 종료", true);
//        }
        Thread.sleep(1000);
    }
    
    @Test
    public void runAll() {
    	try {
        	//미지정메시지 인입 고객미등록
			runSendUnregChatMsg();
			//미지정메시지 상담선택
	    	runListChatSessionAndSelectChatMsg();
	    	//지정메시지 인입 처리 및 상담처리
	        runSendRegChatMsg();
	        //상담완료처리
	        runCompleteConsult();
	        //상담이관
	        //runPassOverConsult();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * test title:미지정메시지 인입 고객미등록
     * description: 미지정 메시지가 들어오면 
     * test step:
     * 0. login하기 - admin
     * 1. json으로 메시지 post처리
     *    - 메시지큐에 대기
     * 
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Test
    public void runSendUnregChatMsg() throws InterruptedException, ExecutionException, TimeoutException {
    	logger.describe("runSendUnregChatMsg", "미지정메시지 인입 고객미등록");
    	//0. login하기
    	AuthUserInfo authUserInfo = _callLogin("0. login하기",testData.getUserInfoAdmin());
    	token =  authUserInfo.getToken();
    	logger.assertResult("Login", "admin계정으로 로그인", !token.isEmpty());

    	//1. json으로 메시지 post처리
    	String bizId = authUserInfo.getUserInfo().getBizId();
		String url = testData.URL + "/chat/nexmoMsg/"+bizId;
    	String queueIdForChat = authUserInfo.getUserInfo().getAgentId();
    	String strNexmoMsg = "Hi, First Message From Nexmo";
    	strNexmoMsg = "It's worth noting that the keyHolder object will contain the auto-generated key return from th";
    	strNexmoMsg = "Hold your mouth";
		StompMessage message = new StompMessage(randomCustomerId,strNexmoMsg,"", queueIdForChat);
		
		ResultObj<String> resultChat = (new RestClient<String>(token)).callJsonHttp(url, message, new TypeToken<ResultObj<String>>() {}.getType());
		logger.debug("mockNexmoMsg message["+message+"]result["+resultChat+"]");
    }

    /**
     * test title:미지정메시지 상담선택
     * description: 세션조회후 미지정세션을 상담선택한다. 
     * test step:
     * 0. login하기- agent01
     * 1. 채팅세션상태 조회
     * 2. 미지정메시지선택
     * 3. 메시지조회
     * 4. 메시지상담선택
     * 
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Test
    public void runListChatSessionAndSelectChatMsg() throws InterruptedException, ExecutionException, TimeoutException {
    	logger.describe("runListChatSessionAndSelectChatMsg", "미지정메시지 상담선택");
    	//0. login하기
    	AuthUserInfo authUserInfo = _callLogin("0. login하기- agent01", testData.getUserInfoAgent());
    	token = authUserInfo.getToken();
    	
    	//1. 채팅세션현황 조회
		String url = testData.URL + "/chat/session/list";
		UserInfo userInfo = authUserInfo.getUserInfo();
		
		Type type = new TypeToken<ResultObj<List<ChatSessionStatus>>>() {}.getType();
		ResultObj<ArrayList<ChatSessionStatus>> resultSessionList = (new RestClient<ArrayList<ChatSessionStatus>>(token)).callJsonHttp(url, authUserInfo.getUserInfo(), type);
		logger.debug("chatSessionList result["+resultSessionList+"]");
    	logger.assertResult("chatSessionList", "1. 채팅세션현황 조회", resultSessionList.getItem().size()>0);

    	List<ChatSessionStatus> chatSessionList = resultSessionList.getItem();
    	ChatSessionStatus chatSession=null;
    	for ( ChatSessionStatus e : chatSessionList) {
    		if (e.getChatId()==0) {
    			chatSession=e;
    			break;
    		}
    	}
		chatSession.setAgentId(userInfo.getAgentId());
		//2. 미지정메시지조회
		_getWaitMsgList("2. 미지정메시지조회",chatSession.getCustomerId(), true);

		//3. 미지정메시지선택
		url = testData.URL + "/chat/take/waitsession";
		ResultObj<ChatConsultDetails> resultTakeWaitSession= (new RestClient<ChatConsultDetails>(token)).callJsonHttp(url, chatSession,
				new TypeToken<ResultObj<ChatConsultDetails>>() {}.getType());
		logger.debug("chatTakeWaitsession result["+resultTakeWaitSession+"]");
		long chatId = resultTakeWaitSession.getItem().getId();
    	logger.assertResult("chatTakeWaitsession", "미지정메시지선택", chatId>0);
		//4. 상담내역확인
    	url = testData.URL + "/chat/chatdetail/"+chatId;
    	
		ResultObj<ChatConsultDetails> resultChatConsultDetails = (new RestClient<ChatConsultDetails>(token)).callJsonHttp(url, "",
				new TypeToken<ResultObj<ChatConsultDetails>>() {}.getType());
		logger.debug("getChatConsultDetails result["+resultChatConsultDetails+"]");
    	logger.assertResult("getChatConsultDetails", "상담내역확인", resultChatConsultDetails.getItem().getId()>0);
	
    	/*
		ResultObj<List<MessageQueue>> resultMessageQueue = (new RestClient<List<MessageQueue>>(token)).callJsonHttp(url, chatSession,
				new TypeToken<ResultObj<List<MessageQueue>>>() {}.getType());
		logger.debug("chatTakeWaitsession result["+resultMessageQueue+"]");
		long chatId = resultMessageQueue.getItem().get(0).getChatId();
    	logger.assertResult("chatTakeWaitsession", "미지정메시지선택", chatId>0);
		//4. 상담내역확인
    	url = testData.URL + "/chat/chatdetail/"+chatId;
    	
		ResultObj<ChatConsultDetails> resultChatConsultDetails = (new RestClient<ChatConsultDetails>(token)).callJsonHttp(url, "",
				new TypeToken<ResultObj<ChatConsultDetails>>() {}.getType());
		logger.debug("getChatConsultDetails result["+resultChatConsultDetails+"]");
    	logger.assertResult("getChatConsultDetails", "상담내역확인", resultChatConsultDetails.getItem().getId()>0);
		 */
    }
    
    /**
	 * test title:지정메시지 인입 처리 및 상담처리
	 * description: 로그인후  
	 * test step:
	 * 0. login하기 - agent01
	 * 1. chat queue 1:1 생성 대기 
	 * 2. group queue 생성 대기
	 * 3. My채팅세션목록조회
	 * 4. 상담상세 조회
	 * 5. 채팅메시지 조회-큐대기포함
	 * 6. nexmo 메시지 전송
	 * 7. chat queue 1:1 메시지 확인
	 * 8. group queue 메시지 확인 - 체팅세션현황
	 * 9. chat메시지 전송
	 * 10. group queue 메시지 확인 - 체팅세션현황
	 * 11. chat queue 1:1 종료
	 * 12. group queue 종료
	 *  
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void runSendRegChatMsg() throws InterruptedException, ExecutionException, TimeoutException {
		
		logger.describe("runSendRegChatMsg", "지정메시지 인입 처리 및 상담처리");
		String strNexmoMsg = "Hi, First Message From Nexmo";
		strNexmoMsg = "It's worth noting that the keyHolder object will contain the auto-generated key return from th";
		
	
		//0. login하기 - agent01
		AuthUserInfo authUserInfo = _callLogin("0. login하기 - agent01",testData.getUserInfoAgent());
		token = authUserInfo.getToken();
		
		//1. chat queue 1:1 생성 대기 
		CompletableFuture<String> cFutureForChat = _connectChatStompClient("1. chat queue 1:1 생성 대기 ", authUserInfo.getUserInfo());
		//2. group queue 생성 대기
		CompletableFuture<String> cFutureForGroup = _connectGroupStompClient("2. group queue 생성 대기 ", authUserInfo.getUserInfo());
		//3. My채팅세션목록조회
		List<ChatSessionStatusEx> chatSessionStatusList = _callMyChatSessionList("3. My채팅세션목록조회", authUserInfo.getUserInfo(),true);
		//4. 상담상세 조회
		ChatConsultDetails detail = _callChatConsultDetails("4. 상담상세 조회", chatSessionStatusList.get(0), true);
		//5. 채팅메시지 조회-큐대기포함
		List<MessageLog> messageList = _callMessageListByChatConsultDetails("5. 채팅메시지 조회-큐대기포함", detail, true);
		String _customerId = messageList.get(0).getCustomerId();
		//6. nexmo 메시지 전송
		_invokeNexmoMsg("6. nexmo 메시지 전송",bizId, _customerId, strNexmoMsg);
		Thread.sleep(1000);
		//7. chat queue 1:1 메시지 확인
		_checkMessageReceived("7. chat queue 1:1 메시지 확인", cFutureForChat, strNexmoMsg);
		//8. group queue 메시지 확인 - 체팅세션현황
		_checkMessageReceived("8. group queue 메시지 확인 - 체팅세션현황", cFutureForGroup, strNexmoMsg);
	    //9. chat메시지 전송
		String strChatMsg = "thanks for your answer, and some notes for your answer: 1- This method has a processing load, 2- It has delay, 3- It needs to implement security 4- thanks again for your answer :).";
		_sendNexmoMsg("9. chat메시지 전송", detail, strChatMsg, true);
		//10. chat메시지 처리 확인
		_checkMessageReceived("10. chat메시지 처리 확인", cFutureForGroup, strChatMsg);
		//11. chat queue 1:1 종료
		_disconnectStompt("11. chat queue 종료", myStompClientForChat, stompSessionForChat, 
				cFutureForChat, authUserInfo.getUserInfo().getAgentId(), strByeMsgForChat);
		//12. chat queue 1:1 종료
		_disconnectStompt("12. group queue 종료", myStompClientForGroup, stompSessionForGroup, 
				cFutureForGroup, authUserInfo.getUserInfo().getBizId(), strByeMsgForGroup);
	}

	/**
     * test title:상담완료
     * description: 로그인후
     * 세션현황에서 정리하고 상담이력에서 완료처리  
     * test step:
     * 0. login하기 - agent01
     * 1. group queue 생성 대기
     * 2. My채팅세션목록조회
     * 3. 상담상세 조회
     * 4. 상담완료처리
     * 5. group queue 메시지 확인 - 체팅세션현황
     * 6. group queue 종료
     *  
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Test
    public void runCompleteConsult() throws InterruptedException, ExecutionException, TimeoutException {
    	
    	logger.describe("runCompleteConsult", "상담완료");

    	//0. login하기 - agent01
    	AuthUserInfo authUserInfo = _callLogin("0. login하기 - agent01",testData.getUserInfoAgent());
    	token = authUserInfo.getToken();
    	
    	//1. group queue 생성 대기
    	CompletableFuture<String> cFutureForGroup = _connectGroupStompClient("1. group queue 생성 대기 ", authUserInfo.getUserInfo());
    	//2. My채팅세션목록조회
    	List<ChatSessionStatusEx> chatSessionStatusList = _callMyChatSessionList("2. My채팅세션목록조회", authUserInfo.getUserInfo(),true);
    	//3. 상담상세 조회
    	ChatConsultDetails detail = _callChatConsultDetails("3. 상담상세 조회", chatSessionStatusList.get(0), true);
    	//4. 상담완료처리
    	detail.setChatComment("고객이 상담을 종료했습니다");
    	detail.setConsultStatus(Constant.CHAT_CONSULT_DETAILS_CONSULT_STATUS_CLOSE.get());
    	detail.setConsultType(Constant.CHAT_CONSULT_DETAILS_CONSULT_TYPE_COMPLAIN.get());
    	
    	_callCompleteConsult("4. 상담완료처리", detail, true);
    	//5. group queue 메시지 확인 - 체팅세션현황
    	_checkMessageReceived("5. group queue 메시지 확인 - 체팅세션현황", cFutureForGroup, "ChatSessionStatus.Delete");
    	//6. chat queue 1:1 종료
    	_disconnectStompt("6. group queue 종료", myStompClientForGroup, stompSessionForGroup, 
    			cFutureForGroup, authUserInfo.getUserInfo().getBizId(), strByeMsgForGroup);
    }    
    
    /**
     * test title:상담이관
     * description: 로그인후
     * 세션현황/상담이력에서 완료처리  
     * test step:
     * 0. login하기 - agent01
     * 1. group queue 생성 대기
     * 2. My채팅세션목록조회
     * 3. 상담상세 조회
     * 4. 상담원목록 조회
     * 5. 이관대상자 선택 및 상담이관처리
     * 6. group queue 메시지 확인 - 체팅세션현황
     * 7. group queue 종료
     *  
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    @Test
    public void runPassOverConsult() throws InterruptedException, ExecutionException, TimeoutException {
    	
    	logger.describe("runPassOverConsult", "상담이관");

    	//0. login하기 - agent01
    	AuthUserInfo authUserInfo = _callLogin("0. login하기 - agent01",testData.getUserInfoAgent());
    	token = authUserInfo.getToken();
    	
    	//1. group queue 생성 대기
    	CompletableFuture<String> cFutureForGroup = _connectGroupStompClient("1. group queue 생성 대기 ", authUserInfo.getUserInfo());
    	//2. My채팅세션목록조회
    	List<ChatSessionStatusEx> chatSessionStatusList = _callMyChatSessionList("2. My채팅세션목록조회", authUserInfo.getUserInfo(),true);
    	//3. 상담상세 조회
    	ChatConsultDetails detail = _callChatConsultDetails("3. 상담상세 조회", chatSessionStatusList.get(0), true);
    	//4. 상담원목록 조회
    	List<UserInfo> userList = _callAgentList("4. 상담원목록 조회", true);
    	//5. 이관대상자 선택 및 상담이관처리
    	UserInfo userPassOver = userList.stream().filter(e-> UserRole.AGENT.get().equals(e.getRole())).findFirst().get();
    	detail.setChatComment("매니저한테 이관처리");
    	detail.setConsultStatus(Constant.CHAT_CONSULT_DETAILS_CONSULT_STATUS_PASS.get());
    	detail.setConsultType(Constant.CHAT_CONSULT_DETAILS_CONSULT_TYPE_COMPLAIN.get());
    	
    	_callPassOverConsult("5. 이관대상자 선택 및 상담이관처리", userPassOver.getAgentId(), detail, true);
    	//5. group queue 메시지 확인 - 체팅세션현황
    	_checkMessageReceived("6. group queue 메시지 확인 - 체팅세션현황", cFutureForGroup, "ChatSessionStatus.Update");
    	//6. chat queue 1:1 종료
    	_disconnectStompt("7. group queue 종료", myStompClientForGroup, stompSessionForGroup, 
    			cFutureForGroup, authUserInfo.getUserInfo().getBizId(), strByeMsgForGroup);
    }      
    
    /**
	 * test title:상담내역 확인
	 * description: 선택된 상담내역을 확인한다. 
	 * test step:
	 * 0. login하기 -agent01
	 * 1. 메시지상담선택
	 * 
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void runGetChatConsultDetails() throws InterruptedException, ExecutionException, TimeoutException {
		logger.describe("runGetChatConsultDetails", "상담내역 확인");
		//0. login하기 -agent01
		AuthUserInfo authUserInfo = _callLogin("0. login하기 -agent01",testData.getUserInfoAgent());
		token = authUserInfo.getToken();
	
		//1. 상담내역확인
		long chatId = 5;
		String url = testData.URL + "/chat/chatdetail/"+chatId;
		ResultObj<ChatConsultDetails> resultChatConsultDetails = (new RestClient<ChatConsultDetails>(token)).callJsonHttp(url, "",
				new TypeToken<ResultObj<ChatConsultDetails>>() {}.getType());
		logger.debug("getChatConsultDetails result["+resultChatConsultDetails+"]");
		logger.assertResult("getChatConsultDetails", "상담내역확인", resultChatConsultDetails.getItem().getId()>0);
		
	}

    
    /**
	 * test title:상담지정된 nexmo메시지 발송
	 * description: 로그인후  
	 * test step:
	 * 1. nexmo 메시지 전송
	 *  
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void runInvokeRegChatMsg() throws InterruptedException, ExecutionException, TimeoutException {
		
		logger.describe("runInvokeRegChatMsg", "상담지정된 nexmo메시지 발송");
		String strNexmoMsg = "Hi, First Message From Nexmo";
		strNexmoMsg = "It's worth noting that the keyHolder object will contain the auto-generated key return from th";
		
		String _customerId = "7253632591";
		//6. nexmo 메시지 전송
		_invokeNexmoMsg("1. nexmo 메시지 전송",bizId, _customerId, strNexmoMsg);
	}
	
    @Test
    public void runListChatSession() throws InterruptedException, ExecutionException, TimeoutException {
    	logger.describe("runListChatSession", "미지정메시지 상담  조회");
    	//0. login하기
    	AuthUserInfo authUserInfo = _callLogin("0. login하기- agent01", testData.getUserInfoAgent());
    	token = authUserInfo.getToken();
    	
    	//1. 채팅세션현황 조회
		String url = testData.URL + "/chat/session/listex";
		UserInfo userInfo = authUserInfo.getUserInfo();
		
		Type type = new TypeToken<ResultObj<List<ChatSessionStatusEx>>>() {}.getType();
		ResultObj<ArrayList<ChatSessionStatusEx>> resultSessionList = (new RestClient<ArrayList<ChatSessionStatusEx>>(token)).callJsonHttp(url, authUserInfo.getUserInfo(), type);
		logger.debug("chatSessionList result["+resultSessionList+"]");
    	logger.assertResult("chatSessionList", "1. 채팅세션현황 조회", resultSessionList.getItem().size()>0);

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
    
    /**
     * nexmo메시지 발생시키기
     */
    boolean _invokeNexmoMsg(String testTitle, String bizId, String customerId, String nexmoMsg) {
		String url = testData.URL + "/nexmo/in/"+bizId;
		StompMessage message = new StompMessage(customerId,nexmoMsg,"", "");
		
		ResultObj<String> resultChat = (new RestClient<String>()).callJsonHttp(url, message);
		logger.debug("invokeNexmoMsg message["+message+"]result["+resultChat+"]");
    	logger.assertResult("invokeNexmoMsg", testTitle, ResultObj.isSuccess(resultChat.getResultCode()) );
		return true;
    }
    
    /**
     * chat메시지 전송
     */
    boolean _sendNexmoMsg(String testTitle, ChatConsultDetails c, String nexmoMsg, boolean flag) {
		String url = testData.URL + "/chat/message/send";
		MessageQueue msgQue = new MessageQueue(c.getCustomerId(), c.getId(), Constant.MESSAGE_QUEUE_DIRECTION_OUTBOUND.get(),
				c.getBizId(), country, lang, Constant.MESSAGE_QUEUE_STATUS_NON.get(), nexmoMsg);

		ResultObj<MessageLog> resultChat = (new RestClient<MessageLog>(token)).callJsonHttp(url, msgQue);
		logger.debug("_sendNexmo_Msg message["+msgQue+"]result["+resultChat+"]");
    	logger.assertResult("_sendNexmo_Msg", testTitle, flag && ResultObj.isSuccess(resultChat.getResultCode()) );
		return true;
    }

    List<MessageQueue> _getWaitMsgList(String testTitle, String customerId, boolean flag) {
		//2. 미지정메시지조회
		String url = testData.URL + "/chat/message/list";
		ChatSessionStatus chatSession = new ChatSessionStatus();
		chatSession.setCustomerId(customerId);
		ResultObj<List<MessageQueue>> result = (new RestClient<List<MessageQueue>>(token)).callJsonHttp(url, chatSession);
		logger.debug("getWaitMsgList result["+result+"]");
    	logger.assertResult("getWaitMsgList", testTitle, flag && result.getItem().size()>0);
    	return result.getItem();
    }

    List<ChatSessionStatusEx> _callMyChatSessionList(String testTitle, UserInfo userInfo, boolean flag) {
		//내 채팅세션목록조회
		String url = testData.URL + "/chat/session/mylist";
		Type type = new TypeToken<ResultObj<List<ChatSessionStatusEx>>>() {}.getType();
		ResultObj<List<ChatSessionStatusEx>> result = (new RestClient<List<ChatSessionStatusEx>>(token)).callJsonHttp(url, userInfo, type);
		logger.debug("callMyChatSessionList result["+result+"]");
    	logger.assertResult("callMyChatSessionList", testTitle, flag && result.getItem().size()>0);
    	return result.getItem();
    }
    
    ChatConsultDetails _callChatConsultDetails(String testTitle, ChatSessionStatusEx c, boolean flag) {
		//내 채팅상담내역 조회
		String url = testData.URL + "/chat/chatdetail/"+c.getChatConsultDetails().getId();
		Type type = new TypeToken<ResultObj<ChatConsultDetails>>() {}.getType();
		ResultObj<ChatConsultDetails> result = (new RestClient<ChatConsultDetails>(token)).callJsonHttp(url, "", type);
		logger.debug("_callChatConsultDetails result["+result+"]");
    	logger.assertResult("_callChatConsultDetails", testTitle, flag && ResultObj.isSuccess(result.getResultCode()) );
    	return result.getItem();
    }
    
    
    List<MessageLog> _callMessageListByChatConsultDetails(String testTitle, ChatConsultDetails c, boolean flag) {
		String url = testData.URL + "/chat/chatdetail/%d/messages";
		Type type = new TypeToken<PageResultObj<List<MessageLog>>>() {}.getType();
		Map<String, String> map = new HashMap<String, String>();
		map.put("chatId", Long.toString(c.getId()));
		Map<String, String> orMap = new HashMap<String, String>();
		PageRequest req = new PageRequest(1,10,0,map, orMap);
		url = String.format(url, c.getId());
		PageResultObj<List<MessageLog>> result = (PageResultObj<List<MessageLog>>) (new RestClient<List<MessageLog>>(token)).callJsonHttp(url, req, type);
		logger.debug("_callMessageListByChatConsultDetails result["+result+"]");
    	logger.assertResult("_callMessageListByChatConsultDetails", testTitle, flag && ResultObj.isSuccess(result.getResultCode()) );
    	return result.getItem();
    }
    
    boolean _callCompleteConsult(String testTitle, ChatConsultDetails c, boolean flag) {
		//내 채팅상담내역 조회
		String url = testData.URL + "/chat/chatdetail/complete";
		Type type = new TypeToken<ResultObj<String>>() {}.getType();
		ResultObj<String> result = (new RestClient<String>(token)).callJsonHttp(url, c, type);
		logger.debug("_callCompleteConsult result["+result+"]");
    	logger.assertResult("_callCompleteConsult", testTitle, flag && ResultObj.isSuccess(result.getResultCode()) );
    	return true;
    }
    
    
    List<UserInfo> _callAgentList(String testTitle, boolean flag) {
		String url = testData.URL + "/user/search";
		Type type = new TypeToken<ResultObj<List<UserInfo>>>() {}.getType();
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("role", "O");
		ResultObj<List<UserInfo>> result = (ResultObj<List<UserInfo>>) (new RestClient<List<UserInfo>>(token)).callJsonHttp(url, map, type);
		logger.debug("_callAgentList result["+result+"]");
    	logger.assertResult("_callAgentList", testTitle, flag && ResultObj.isSuccess(result.getResultCode()) );
    	return result.getItem();
    }
    
    boolean _callPassOverConsult(String testTitle, String passOverAgentId, ChatConsultDetails c, boolean flag) {
		//내 채팅상담내역 조회
		String url = testData.URL + "/chat/chatdetail/passover/"+passOverAgentId;
		Type type = new TypeToken<ResultObj<ChatConsultDetails>>() {}.getType();
		ResultObj<ChatConsultDetails> result = (new RestClient<ChatConsultDetails>(token)).callJsonHttp(url, c, type);
		logger.debug("_callPassOverConsult result["+result+"]");
    	logger.assertResult("_callPassOverConsult", testTitle, flag && ResultObj.isSuccess(result.getResultCode()) );
    	return true;
    }

    CompletableFuture<String> _connectChatStompClient(String testTitle, UserInfo userInfo) throws InterruptedException, ExecutionException {

    	myStompClientForChat = new MyStompClient(userInfo.getAgentId(), token);
    	stompSessionForChat = myStompClientForChat.getSession();
    	String queueId = userInfo.getAgentId();
        //1. chat queue 1:1 생성 대기
    	logger.assertResult("connectChatStompClient", testTitle, true);
    	return myStompClientForChat.subscribeQueue(stompSessionForChat, queueId, strByeMsgForChat);
    }
    
    CompletableFuture<String> _connectGroupStompClient(String testTitle, UserInfo userInfo) throws InterruptedException, ExecutionException {

    	myStompClientForGroup = new MyStompClient(userInfo.getBizId(), token);
    	stompSessionForGroup = myStompClientForGroup.getSession();
    	String queueId = userInfo.getBizId();
        //1. chat queue 1:1 생성 대기
    	logger.assertResult("_connectGroupStompClient", testTitle, true);
    	return myStompClientForGroup.subscribeQueue(stompSessionForGroup, queueId, strByeMsgForGroup);
    }
    
    boolean _checkMessageReceived(String testTitle, CompletableFuture<String> cFuture, String strMessage) throws InterruptedException, ExecutionException, TimeoutException {
		//4. chat queue 1:1 메시지 확인
    	String strFuture = cFuture.get(3,SECONDS);
    	logger.info(String.format("[%s]=>[%s]", strFuture, strMessage));
		logger.assertResult("checkMessageReceived",testTitle, (cFuture.get(3,SECONDS).contains(strMessage)));
		return true;
    }
    
    boolean _disconnectStompt(String testTitle, MyStompClient stompClient, StompSession stompSession, 
    		CompletableFuture<String> cFuture, String queueId, String strBye) throws InterruptedException, ExecutionException, TimeoutException {
        //7. chat connection 종료
        stompClient.sendMsg(stompSession, queueId, strBye);
        Thread.sleep(1000);
        String strByeMsg = cFuture.get(3,SECONDS);
        logger.info(String.format("strByeMsg[%s]==>[%s]", strByeMsg, strBye));
        if (strByeMsg.contains(strBye)) {
            stompSession.disconnect();
            logger.info("stompSession.disconnect()");
        } else {
            logger.assertResult("safeCloseChat", testTitle, false);
        }
        Thread.sleep(1000);
        return true;
    }
    
    /**
     * 시나리오:
     * 1. 미지정메시지 인입 고객미등록 
     * 2. 미지정메시지 인입 고객등록
     * 3. 지정메시지 인입 상담원오프라인
     * 4. 지정메시지 인입 상담원온라인
     */

}
