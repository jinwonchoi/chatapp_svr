package com.gencode.issuetool.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.gencode.issuetool.dao.ChatConsultDetailsDao;
import com.gencode.issuetool.dao.ChatSessionStatusDao;
import com.gencode.issuetool.dao.CustomerInfoDao;
import com.gencode.issuetool.dao.LoginUserDao;
import com.gencode.issuetool.dao.MessageLogDao;
import com.gencode.issuetool.dao.MessageQueueDao;
import com.gencode.issuetool.etc.ReturnCode;
import com.gencode.issuetool.etc.Utils;
import com.gencode.issuetool.io.StompObj;
import com.gencode.issuetool.obj.ChatConsultDetails;
import com.gencode.issuetool.obj.ChatSessionStatus;
import com.gencode.issuetool.obj.ChatSessionStatusEx;
import com.gencode.issuetool.obj.CustomerInfo;
import com.gencode.issuetool.obj.MessageLog;
import com.gencode.issuetool.obj.MessageQueue;
import com.gencode.issuetool.util.JsonUtils;
import com.gencode.issuetool.websocket.obj.StompMessage;

@Service
public class ChatService {
	private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    CustomerInfoDao customerInfoDao;
    @Autowired
    MessageQueueDao messageQueueDao;
    @Autowired
    MessageLogDao messageLogDao;
    @Autowired
    ChatConsultDetailsDao chatConsultDetailsDao;
    @Autowired
    ChatSessionStatusDao chatSessionStatusDao;
    @Autowired
    LoginUserDao loginUserDao;
    
    
	/**
	 * 기능테스트용
	 * 상담원에게 채팅메시지전송:
	 *  - 메시지를 파싱처리
	 *  - 해당고객번호로 
	 */
    public boolean sendMsgToChat(StompMessage msg) {
    	//boolean result = false;
		// 전달자
        msg.setSendTime(Utils.yyyyMMddHHmmss());
        template.convertAndSendToUser(msg.getReceiver(), "/chat", JsonUtils.toJson(msg));
        return true;
    }

    /**
     * 실사용 client 전송용 
     * @param msg
     * @return
     */
    public boolean sendMsgToChat(String receiver, String type, String item) {
        template.convertAndSendToUser(receiver, "/chat", JsonUtils.toJson(new StompObj(type, item)));
        return true;
    }

    /**
	 * 고객에게 채팅메시지전송:
     * nexmo메시지 전송하기
     * 1. messageQueue.insert
     * 2. sendNexmo
     * 3. messageLog.insert
     * 4. messageQueue.delete
     * 5. chatSessionStatus.update
     * @param msgQue
     * @return
     */
    @Transactional
    public Optional<MessageLog> sendMsgToNexmo(MessageQueue msgQue) {
    	long msgId = messageQueueDao.register(msgQue);
    	ChatConsultDetails detail = chatConsultDetailsDao.load(msgQue.getChatId()).get();
        callNexmoAPI(new StompMessage(detail.getAgentId(), msgQue.getMessage(), msgQue.getCreatedDtm(), msgQue.getCustomerId()));
        
        MessageQueue regMsgQue = messageQueueDao.load(msgId).get();
        messageLogDao.register(new MessageLog(regMsgQue));
        messageQueueDao.delete(msgId);
        ChatSessionStatus  c = chatSessionStatusDao.findByCustomerId(regMsgQue.getCustomerId()).get().get(0);
        c.setLastMessage(regMsgQue.getMessage());
        c.setLastMessageId(regMsgQue.getId());
        chatSessionStatusDao.update(c);
        ChatSessionStatusEx  cEx =chatSessionStatusDao.loadEx(c.getId()).get(); 
    	//채팅현황갱신
        sendMsgToChat(detail.getBizId(), "ChatSessionStatus.Update", JsonUtils.toJson(cEx));
        MessageLog regMsg = messageLogDao.load(msgId).get();
        //sendMsgToChat(detail.getAgentId(), "MessageLog.Insert", JsonUtils.toJson(regMsg));
      //TODO
        sendMsgToChat(detail.getBizId(), "MessageLog.Insert", JsonUtils.toJson(regMsg));
        return Optional.of(regMsg);
    }

    /**
     * nexmo메시지 전송
     * 
     * @param msg
     * @return
     */
    public boolean callNexmoAPI(StompMessage msg) {
    	log.info("sendMsgToNexmo"+ msg);
    	return true;
    }
    
    /**
     * Nexmo메시지 수신한경우
     * - customer id가 있는지 확인 없으면 등록 =>
     *  Customer.insert
     * - chat id가 있는지 확인.
     *  ChatSessionStatus.findByCustomerId
     *  없으면 생성
     *  ChatSessionStatus.insert
     *  있으면 업데이트
     *  
     *  MessageQue.insert
     * - 상담원이 onlin인지 확인. 
		loginUser.find
		Offline이면 대기
		6. 상담원이 online이면 전송
		ChatService.sendMsgToChat
		MessageLog.insert
		MessageQue.delete
		ChatSessionStatus.update
     * @param msg
     * @return
     */
    @Transactional
    public Optional<MessageLog> processNexmoMsg(MessageQueue msg) {
    	long msgId = 0;
    	boolean isInLog = false;
    	try {
	    	//고객이 신규이면 반영
	    	if (customerInfoDao.findByCustomerId(msg.getCustomerId()).get().size()==0) {
	    		CustomerInfo customerInfo = new CustomerInfo();
	    		customerInfo.setBizId(msg.getBizId());
	    		customerInfo.setCountry(msg.getCountry());
	    		customerInfo.setCustomerId(msg.getCustomerId());
	    		customerInfo.setLang(msg.getLang());
	    		customerInfo.setIdType("M");
	    		customerInfoDao.register(customerInfo);
	    	}
	    	//chatSession에 반영
	    	Optional<List<ChatSessionStatus>> chatSessionStatusList = chatSessionStatusDao.findByCustomerId(msg.getCustomerId());
	    	if (chatSessionStatusList.get().size()==0) {
		    	msgId = messageQueueDao.register(msg);

	    		ChatSessionStatus c = new ChatSessionStatus();
	    		c.setAgentId(null);
	    		c.setBizId(msg.getBizId());
	    		c.setCountry(msg.getCountry());
	    		c.setCustomerId(msg.getCustomerId());
	    		c.setLang(msg.getLang());
	    		c.setDirection(msg.getDirection());
	    		c.setLastMessageId(msgId);
	    		c.setLastMessage(msg.getMessage());
	    		long sessionId = chatSessionStatusDao.register(c);
	    		ChatSessionStatusEx c2  = chatSessionStatusDao.loadEx(sessionId).get();
	        	//채팅현황갱신
	            sendMsgToChat(c2.getBizId(), "ChatSessionStatus.Insert", JsonUtils.toJson(c2));
            	sendMsgToChat(c2.getBizId(), "MessageLog.Insert", JsonUtils.toJson(new MessageLog(messageQueueDao.load(msgId).get())));
	    	} else {
	    		ChatSessionStatus c = chatSessionStatusList.get().get(0);
	    		msg.setChatId(c.getChatId());
		    	msgId = messageQueueDao.register(msg);

	    		c.setDirection(msg.getDirection());
	    		c.setLastMessageId(msgId);
	    		c.setLastMessage(msg.getMessage());
	    		c.setUnreadCnt(c.getUnreadCnt()+1);
	    		chatSessionStatusDao.update(c);
	    		ChatSessionStatusEx c2 = chatSessionStatusDao.loadEx(c.getId()).get();
	        	//채팅현황갱신
	            //template.convertAndSendToUser(c.getBizId(), "/chat", JsonUtils.toJson(new StompObj("ChatSessionStatus.Update", c)));
	//            template.convertAndSendToUser(c.getAgentId(), "/chat", "hi agent");
	//            template.convertAndSendToUser(c.getBizId(), "/chat", "hi biz");
	//        	StompMessage stompMessage1 = new StompMessage(msg.getCustomerId(), JsonUtils.toJson(new StompObj("ChatSessionStatus.Update", c)), "", c.getAgentId());
	//        	sendMsgToChat(stompMessage1);
	//			StompMessage stompMessage2 = new StompMessage(msg.getCustomerId(),
	//					JsonUtils.toJson(new StompObj("ChatSessionStatus.Update", c))/* "hi biz" */, "", c.getBizId());
	//        	sendMsgToChat(stompMessage2);
	            sendMsgToChat(c2.getBizId(), "ChatSessionStatus.Update", JsonUtils.toJson(c2));
	    		
	        	//상담원이 온라인인지 확인
	    		
	            if (loginUserDao.contains(c.getAgentId())) {
	            	//StompMessage stompMessage = new StompMessage(msg.getCustomerId(), msg.getMessage(), "", c.getAgentId());
	            	//sendMsgToChat(stompMessage);
	        		MessageQueue _msg = messageQueueDao.load(msgId).get();
        			_msg.setBizId(c.getBizId());
        			_msg.setChatId(c.getChatId());
    				messageLogDao.register(new MessageLog(_msg));
    				messageQueueDao.delete(_msg.getId());
    				MessageLog regMsg = messageLogDao.load(_msg.getId()).get();
                	//sendMsgToChat(c.getAgentId(), "MessageLog.Insert", JsonUtils.toJson(regMsg));
                	sendMsgToChat(c.getBizId(), "MessageLog.Insert", JsonUtils.toJson(regMsg));
                	return Optional.of(regMsg); 
	        	} else {
                	sendMsgToChat(c.getBizId(), "MessageLog.Insert", JsonUtils.toJson(new MessageLog(messageQueueDao.load(msgId).get())));
	        	}
	    	}
    		return Optional.of(new MessageLog(messageQueueDao.load(msgId).get()));
    	} catch (Exception e) {
    		log.error("processNexmoMsg",e);
    		throw e;    		
    	}
    }
}
