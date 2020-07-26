package com.gencode.issuetool.ctrl;

import com.gencode.issuetool.etc.Constant;
import com.gencode.issuetool.etc.ReturnCode;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.ResultObj;
import com.gencode.issuetool.obj.ChatConsultDetails;
import com.gencode.issuetool.obj.ChatConsultDetailsEx;
import com.gencode.issuetool.obj.ChatSessionStatus;
import com.gencode.issuetool.obj.ChatSessionStatusEx;
import com.gencode.issuetool.obj.MessageLog;
import com.gencode.issuetool.obj.MessageQueue;
import com.gencode.issuetool.obj.NoticeBoardEx;
import com.gencode.issuetool.obj.User;
import com.gencode.issuetool.obj.UserInfo;
import com.gencode.issuetool.service.ChatService;
import com.gencode.issuetool.service.ChatSessionService;
import com.gencode.issuetool.util.JsonUtils;
import com.gencode.issuetool.websocket.obj.StompMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/chat")
@RestController
@CrossOrigin(origins = "${cors_url}")
public class ChatController {
	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private SimpMessagingTemplate template;
    private String receiver;

    @Autowired
    private ChatService chatService;
    
    @Autowired
    private ChatSessionService chatSessionStatusService;

    /**
     * 기능테스트용.
     * nexmo에서 메시지 수신
     *  
     */
	@RequestMapping(method=RequestMethod.POST, value="/nexmomsg")
    public ResultObj<String> getNexmoMsg(@RequestBody StompMessage msg){
		ResultObj<String> result = new ResultObj<String>();
		// 전달자
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        //Message msg = JsonUtils.toObject(message, Message.class);
        try {
            msg.setSendTime(sdf.format(new Date()));
            template.convertAndSendToUser(msg.getReceiver(), "/chat", JsonUtils.toJson(msg));
            //template.convertAndSendToUser(msg.getReceiver(), "/chat", JsonUtils.toJson(msg));
            template.convertAndSendToUser("A01", "/chat", JsonUtils.toJson(msg));
			result.setResultCode(ReturnCode.SUCCESS.get());
			result.setResultMsg(ReturnCode.STR_SUCCESS.get());
			result.setItem("ok");
        } catch (Exception e) {

			result.setResultCode(ReturnCode.ERROR_UNKNOWN.get());
			result.setResultMsg(ReturnCode.STR_ERROR_UNKNOWN.get());
        }
        return result;
	}


    /**
     * nexmo에서 메시지 수신
     *  
     */
	@RequestMapping(method=RequestMethod.POST, value="/nexmoMsg/{bizId}")
    public ResultObj<String> getNexmoMsg(@RequestBody StompMessage msg, @PathVariable(name="bizId") String bizId){
		ResultObj<String> result = new ResultObj<String>();
        try {
        	MessageQueue msgQue = new MessageQueue();
        	msgQue.setBizId(bizId);
        	msgQue.setCountry("kr");
        	msgQue.setLang("ko");
        	msgQue.setMessage(msg.getContent());
        	msgQue.setCustomerId(msg.getUsername());
        	msgQue.setDirection(Constant.MESSAGE_QUEUE_DIRECTION_INBOUND.get());

        	chatService.processNexmoMsg(msgQue);
			ResultObj<String> resultObj = ResultObj.success();
			resultObj.setItem("ok");
			return resultObj;
        } catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
        }
	}
	
    /**
     * 기능테스트용
     * 상담창에 채팅. 대상은 상담원, 팀, 법인 모두 해당
     * @param principal
     * @param message
     */
    @MessageMapping("/sendmsg")
    public void sendToCustomer(Principal principal, String message) {
    	logger.info(message);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        StompMessage msg = JsonUtils.toObject(message, StompMessage.class);
        try {
            msg.setSendTime(sdf.format(new Date()));
        } catch (Exception e) {
        }
        chatService.sendMsgToChat(msg.getReceiver(), "Test", msg.getContent());
        //template.convertAndSendToUser(msg.getReceiver(), "/chat", JsonUtils.toJson(msg));
    }

    /**
     * 채팅메시지 전송
     * @param 
     * @return
     */
	@RequestMapping(method=RequestMethod.POST, value="/message/send")
    public ResultObj<MessageLog> sendMessageToNexmo(@RequestBody MessageQueue msgQue){
        try {
        	Optional<MessageLog> result = chatService.sendMsgToNexmo(msgQue);
        	if (result.isPresent()) {
    			return new ResultObj<MessageLog>(ReturnCode.SUCCESS, result.get());
        	} else {
        		return new ResultObj<MessageLog>(ReturnCode.DATA_NOT_FOUND, null);
        	}
        } catch (Exception e) {
        	logger.error("normal error", e);
			return ResultObj.errorUnknown();
        }
	}

    
    /**
     * 법인 채팅세션현황 조회
     * @param userInfo
     * @return
     */
	@RequestMapping(method=RequestMethod.POST, value="/session/list")
    public ResultObj<List<ChatSessionStatus>> getChatSessionStatusByBizId(@RequestBody UserInfo userInfo){
        try {
        	return chatSessionStatusService.selectByBizId(userInfo)
        	.map(t->(t.size()>0)
					?ResultObj.<List<ChatSessionStatus>>success(t)
							:ResultObj.<List<ChatSessionStatus>>error(ReturnCode.DATA_NOT_FOUND)).get();
        } catch (Exception e) {
        	logger.error("normal error", e);
			return ResultObj.errorUnknown();
        }
	}

	@RequestMapping(method=RequestMethod.POST, value="/session/listex")
    public ResultObj<List<ChatSessionStatusEx>> getChatSessionStatusExByBizId(@RequestBody UserInfo userInfo){
        try {
        	return chatSessionStatusService.findByBizId(userInfo)
        	.map(t->(t.size()>0)
					?ResultObj.<List<ChatSessionStatusEx>>success(t)
							:ResultObj.<List<ChatSessionStatusEx>>error(ReturnCode.DATA_NOT_FOUND)).get();
        } catch (Exception e) {
        	logger.error("normal error", e);
			return ResultObj.errorUnknown();
        }
	}

	@RequestMapping(method=RequestMethod.POST, value="/consult/listex/{bizId}")
    public PageResultObj<List<ChatConsultDetailsEx>> getChatConsultDetailsExByBizId(@PathVariable(name="bizId") String bizId, @RequestBody PageRequest req){
        try {
        	System.out.println(req.toString());
			Optional<PageResultObj<List<ChatConsultDetailsEx>>> list = chatSessionStatusService.loadChatConsultDetailsExByBrId(bizId, req);
			if (list.isPresent()) {
				return new PageResultObj<List<ChatConsultDetailsEx>>(ReturnCode.SUCCESS, list.get());
			} else {
				return PageResultObj.<List<ChatConsultDetailsEx>>dataNotFound();
			}
		} catch (Exception e) {
			logger.error("normal error", e);
			return PageResultObj.<List<ChatConsultDetailsEx>>errorUnknown();        }
	}

	/**
     * 내 채팅세션현황 조회
     * @param userInfo
     * @return
     */
	@RequestMapping(method=RequestMethod.POST, value="/session/mylist")
    public ResultObj<List<ChatSessionStatusEx>> getChatSessionStatusByAgentId(@RequestBody UserInfo userInfo){
        try {
        	return chatSessionStatusService.findByAgentId(userInfo)
        	.map(t->(t.size()>0)
					?ResultObj.<List<ChatSessionStatusEx>>success(t)
							:ResultObj.<List<ChatSessionStatusEx>>error(ReturnCode.DATA_NOT_FOUND)).get();
        } catch (Exception e) {
        	logger.error("normal error", e);
			return ResultObj.errorUnknown();
        }
	}

	/**
	 * 미지정메시지 조회
	 * @param cStatus
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/message/list")
    public ResultObj<List<MessageQueue>> getWaitingMessageQueue(@RequestBody ChatSessionStatus cStatus){
        try {
        	return chatSessionStatusService.selectWaitingMessages(cStatus)
        	.map(t->(t.size()>0)
					?ResultObj.<List<MessageQueue>>success(t)
							:ResultObj.<List<MessageQueue>>error(ReturnCode.DATA_NOT_FOUND)).get();
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}

	
	/**
	 * 미지정메시지 상담선택
	 */
	@RequestMapping(method=RequestMethod.POST, value="/take/waitsession")
    public ResultObj<ChatConsultDetails> takeWaitingSessionForChat(@RequestBody ChatSessionStatus cStatus){
        try {
        	Optional<ChatConsultDetails> result =  chatSessionStatusService.registerChatConsult(cStatus);
        	if (result.isPresent()) {
    			return new ResultObj<ChatConsultDetails>(ReturnCode.SUCCESS, result.get());
        	} else {
        		return new ResultObj<ChatConsultDetails>(ReturnCode.DATA_NOT_FOUND, null);
        	}

		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}
	/*
    public ResultObj<List<MessageQueue>> takeWaitingSessionForChat(@RequestBody ChatSessionStatus cStatus){
        try {
        	return chatSessionStatusService.selectChatConsult(cStatus)
        	.map(t->(t.size()>0)
					?ResultObj.<List<MessageQueue>>success(t)
							:ResultObj.<List<MessageQueue>>error(ReturnCode.DATA_NOT_FOUND)).get();
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}
	 */
	
	/**
	 * 열어본창읽기 완료
	 */
	@RequestMapping(method=RequestMethod.POST, value="/chatdetail/setAlreadyRead")
    public ResultObj<String> setMessageAlreadyRead(@RequestBody ChatConsultDetails c){
        try {
        	chatSessionStatusService.setMessageAlreadyRead(c);
        	return ResultObj.<String>success();
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}
	
	
	/**
	 * 상담내역검색
	 * @param cStatus
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/chatdetail/{id}")
    public ResultObj<ChatConsultDetailsEx> getChatConsultDetails(@PathVariable(name="id") String id){
        try {
        	Optional<ChatConsultDetailsEx> result =  chatSessionStatusService.loadChatConsultDetailsEx(Long.parseLong(id));
        	if (result.isPresent()) {
    			return new ResultObj<ChatConsultDetailsEx>(ReturnCode.SUCCESS, result.get());
        	} else {
        		return new ResultObj<ChatConsultDetailsEx>(ReturnCode.DATA_NOT_FOUND, null);
        	}
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}


	/**
	 * 이관이력 포함 상담내역검색
	 * @param cStatus
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/chatdetail/hist/{id}")
    public ResultObj<List<ChatConsultDetailsEx>> getChatConsultDetailsHistory(@PathVariable(name="id") String id){
        try {
        	Optional<List<ChatConsultDetailsEx>> result =  chatSessionStatusService.loadChatConsultDetailsExBySessionId(Long.parseLong(id));
        	if (result.isPresent()) {
    			return new ResultObj<List<ChatConsultDetailsEx>>(ReturnCode.SUCCESS, result.get());
        	} else {
        		return new ResultObj<List<ChatConsultDetailsEx>>(ReturnCode.DATA_NOT_FOUND, null);
        	}
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}

	/**
	 * 특정상담 메시지내역 조회
	 * @param cStatus
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/chatdetail/{chatId}/messages/{viewPrevChat}")
    public PageResultObj<List<MessageLog>> getMessageListByChatConsultDetails(@PathVariable(name="chatId") String chatId,
    		@PathVariable(name="viewPrevChat") String viewPrevChat,
    		@RequestBody PageRequest req) {
        try {
        	Optional<PageResultObj<List<MessageLog>>> result =  chatSessionStatusService.loadMessageListByChatConsultDetails(Long.parseLong(chatId), viewPrevChat.equals("y"), req);
        	if (result.isPresent()) {
    			return new PageResultObj<List<MessageLog>>(ReturnCode.SUCCESS, result.get());
        	} else {
        		return new PageResultObj<List<MessageLog>>(ReturnCode.DATA_NOT_FOUND, null);
        	}
		} catch (Exception e) {
			logger.error("normal error", e);
			return PageResultObj.errorUnknown();
		}
	}

	/**
	 * 상담완료처리
	 * @param cStatus
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/chatdetail/complete")
    public ResultObj<String> completeChatConsultDetails(@RequestBody ChatConsultDetails c){
        try {
        	chatSessionStatusService.completeChatConsult(c);
        	return ResultObj.<String>success();
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.<String>errorUnknown();
		}
	}

	/**
	 * 상담이관처리
	 * @param cStatus
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/chatdetail/passover/{agentId}")
    public ResultObj<ChatConsultDetails> passOverChatConsultDetails(@PathVariable(name="agentId") String agentId,
    		@RequestBody ChatConsultDetails c){
        try {
        	Optional<ChatConsultDetails> result = chatSessionStatusService.passOverChatConsult(c, agentId);
        	return ResultObj.<ChatConsultDetails>success(result.get());
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.<ChatConsultDetails>errorUnknown();
		}
	}
	
	/**
	 * 상담변경
	 * @param cStatus
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/chatdetail/update")
    public ResultObj<ChatConsultDetails> updateChatConsultDetails(@RequestBody ChatConsultDetails c){
        try {
        	Optional<ChatConsultDetails> result = chatSessionStatusService.updateChatConsult(c);
        	return ResultObj.<ChatConsultDetails>success(result.get());
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.<ChatConsultDetails>errorUnknown();
		}
	}

}
