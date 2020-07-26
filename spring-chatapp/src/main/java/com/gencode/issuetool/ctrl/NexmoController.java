package com.gencode.issuetool.ctrl;

import com.gencode.issuetool.etc.Constant;
import com.gencode.issuetool.etc.ReturnCode;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.ResultObj;
import com.gencode.issuetool.obj.ChatConsultDetails;
import com.gencode.issuetool.obj.ChatSessionStatus;
import com.gencode.issuetool.obj.MessageLog;
import com.gencode.issuetool.obj.MessageQueue;
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

@RequestMapping("/nexmo")
@RestController
public class NexmoController {
	private static final Logger logger = LoggerFactory.getLogger(NexmoController.class);

    @Autowired
    private SimpMessagingTemplate template;
    private String receiver;

    @Autowired
    private ChatService chatService;
    
    @Autowired
    private ChatSessionService chatSessionStatusService;


    /**
     * nexmo에서 메시지 수신
     *  
     */
	@RequestMapping(method=RequestMethod.POST, value="/in/{bizId}")
    public ResultObj<MessageLog> getNexmoMsg(@RequestBody StompMessage msg, @PathVariable(name="bizId") String bizId){
        try {
        	MessageQueue msgQue = new MessageQueue();
        	msgQue.setBizId(bizId);
        	msgQue.setCountry("kr");
        	msgQue.setLang("ko");
        	msgQue.setMessage(msg.getContent());
        	msgQue.setCustomerId(msg.getUsername());
        	msgQue.setDirection(Constant.MESSAGE_QUEUE_DIRECTION_INBOUND.get());

        	Optional<MessageLog> result = chatService.processNexmoMsg(msgQue);
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
	 * 특정상담 메시지내역 조회
	 * @param cStatus
	 * @return
	 */
	@RequestMapping(method=RequestMethod.POST, value="/messages/{customerId}")
    public ResultObj<List<MessageLog>> getMessageListByCustomerId(@PathVariable(name="customerId") String customerId) {
        try {
        	Optional<List<MessageLog>> result =  chatSessionStatusService.loadMessageListByCustomerId(customerId);
        	if (result.isPresent()) {
    			return new ResultObj<List<MessageLog>>(ReturnCode.SUCCESS, result.get());
        	} else {
        		return new ResultObj<List<MessageLog>>(ReturnCode.DATA_NOT_FOUND, null);
        	}
		} catch (Exception e) {
			logger.error("normal error", e);
			return ResultObj.errorUnknown();
		}
	}
}
