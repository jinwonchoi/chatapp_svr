package com.gencode.issuetool.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.gencode.issuetool.dao.ChatConsultDetailsDao;
import com.gencode.issuetool.dao.ChatSessionStatusDao;
import com.gencode.issuetool.dao.MessageLogDao;
import com.gencode.issuetool.dao.MessageQueueDao;
import com.gencode.issuetool.etc.Constant;
import com.gencode.issuetool.io.PageRequest;
import com.gencode.issuetool.io.PageResultObj;
import com.gencode.issuetool.io.SortDirection;
import com.gencode.issuetool.io.StompObj;
import com.gencode.issuetool.obj.ChatConsultDetails;
import com.gencode.issuetool.obj.ChatConsultDetailsEx;
import com.gencode.issuetool.obj.ChatSessionStatus;
import com.gencode.issuetool.obj.ChatSessionStatusEx;
import com.gencode.issuetool.obj.MessageLog;
import com.gencode.issuetool.obj.MessageQueue;
import com.gencode.issuetool.obj.UserInfo;
import com.gencode.issuetool.util.JsonUtils;

@Service
public class ChatSessionService {

    @Autowired
    private SimpMessagingTemplate template;
	@Autowired
	ChatSessionStatusDao chatSessionStatusDao;
	@Autowired 
	ChatConsultDetailsDao chatConsultDetailsDao;
	@Autowired
	MessageQueueDao messageQueueDao;
	@Autowired
	MessageLogDao messageLogDao;
	
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
	 * 채팅세션상태 현황 조회
	 * 소속법인기준으로 조회
	 * @param t
	 * @return
	 */
	public Optional<List<ChatSessionStatus>> selectByBizId(UserInfo t) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("bizId", t.getBizId());
		return chatSessionStatusDao.search(map);
	}

	public Optional<List<ChatSessionStatusEx>> findByBizId(UserInfo t) {
		return chatSessionStatusDao.findByBizId(t.getBizId());
	}

	/**
	 * 상담선택 처리
	 * - 미지정메시지 선택  MessageQue.findByCustomerId=>해당 고객의 메시지를 창으로 띄워 보여줌. 상담선택여부
	 * - 상담선택
	 *   ChatMessage내려받음 ==> 사전동작
	 *   /--> 여기서부터 처리과정
	 *   MessageQue.getList
	 *   ChatConsultDetails.insert
		MessageLog.insert
		MessageQue.delete
		ChatSessionStatus.update

	 * @return
	 */
	@Transactional
	public Optional<ChatConsultDetails> registerChatConsult(ChatSessionStatus t) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("customerId", t.getCustomerId());
		Optional<List<MessageQueue>> msgList = messageQueueDao.search(map);
		ChatConsultDetails chatConsultDetails = new ChatConsultDetails(t);
		long chatId = chatConsultDetailsDao.register(chatConsultDetails);
		msgList.ifPresent(list -> {
			list.forEach(e->{
				e.setBizId(t.getBizId());
				e.setChatId(chatId);
				messageLogDao.register(new MessageLog(e));
				messageQueueDao.delete(e.getId());
			});
		});
		t.setChatId(chatId);
		chatSessionStatusDao.update(t);
		chatConsultDetails.setId(chatId);
		
    	//채팅현황갱신
		ChatSessionStatusEx  cEx =chatSessionStatusDao.loadEx(t.getId()).get();
		sendMsgToChat(t.getBizId(), "ChatSessionStatus.Update", JsonUtils.toJson(cEx));
		//TODO
		//세션현황 sync
		return Optional.of(chatConsultDetails);
	}
/*
	@Transactional
	public Optional<List<MessageQueue>> selectChatConsult(ChatSessionStatus t) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("customerId", t.getCustomerId());
		Optional<List<MessageQueue>> msgList = messageQueueDao.search(map);
		ChatConsultDetails chatConsultDetails = new ChatConsultDetails(t);
		long chatId = chatConsultDetailsDao.register(chatConsultDetails);
		msgList.ifPresent(list -> {
			list.forEach(e->{
				e.setBizId(t.getBizId());
				e.setChatId(chatId);
				messageLogDao.register(new MessageLog(e));
				messageQueueDao.delete(e.getId());
			});
		});
		t.setChatId(chatId);
		chatSessionStatusDao.update(t);
		//TODO
		//세션현황 sync
		return msgList;
	}
	
	 */
	public Optional<List<MessageQueue>> selectWaitingMessages(ChatSessionStatus t) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("customerId", t.getCustomerId());
		return messageQueueDao.search(map);
	}

	public Optional<List<ChatSessionStatusEx>> findByAgentId(UserInfo t) {
		return chatSessionStatusDao.findByAgentId(t.getAgentId());
	}
	
	/**
	 * 상담완료처리
	 * 
	 * chatSessionStatus.delete
	 * chatConsultDetails.update
	 * 
	 * @param t
	 * @return
	 */
	@Transactional
	public boolean completeChatConsult(ChatConsultDetails t) {
		chatSessionStatusDao.deleteByChatId(t.getId());
		t.setConsultStatus(Constant.CHAT_CONSULT_DETAILS_CONSULT_STATUS_CLOSE.get());
		chatConsultDetailsDao.update(t);
		template.convertAndSendToUser(t.getBizId(), "/chat", JsonUtils.toJson(new StompObj("ChatSessionStatus.Delete", Long.toString(t.getId()))));
		return true;
	}

	/**
	 * 상담이관처리
	 * 내가 다른 상담원에게 이관
	 * - 기존 상담이력 이관처리
	 * - 신규 상담이력 생성
	 * - 세션정보 갱신
	 * @param t
	 * @return
	 */
	@Transactional
	public Optional<ChatConsultDetails> passOverChatConsult(ChatConsultDetails t, String passOverAgentId) {
		
		//기존 상담이력 이관처리
		t.setConsultStatus(Constant.CHAT_CONSULT_DETAILS_CONSULT_STATUS_PASS.get());
		//t.setConsultType(Constant.CHAT_CONSULT_DETAILS_CONSULT_TYPE_COMPLAIN.get());
		chatConsultDetailsDao.update(t);
		//신규 상담이력 생성(이관대상 agentId)
		t.setPrevChatId(t.getId());
		t.setAgentId(passOverAgentId);
		t.setConsultStatus(Constant.CHAT_CONSULT_DETAILS_CONSULT_STATUS_OPEN.get());
		long chatId = chatConsultDetailsDao.register(t);
		t.setId(chatId);
		//세션정보 갱신
		Map<String, String> map = new HashMap<String,String>();
		map.put("chatId", Long.toString(t.getPrevChatId()));
        ChatSessionStatus  c = chatSessionStatusDao.search(map).get().get(0);
        c.setAgentId(passOverAgentId);
        c.setChatId(t.getId());
        chatSessionStatusDao.update(c);
        ChatSessionStatusEx c2 = chatSessionStatusDao.loadEx(c.getId()).get();
    	//채팅현황갱신
        template.convertAndSendToUser(c2.getBizId(), "/chat", JsonUtils.toJson(new StompObj("ChatSessionStatus.Update", JsonUtils.toJson(c2))));
        //template.convertAndSendToUser(c2.getBizId(), "/chat", JsonUtils.toJson(new StompObj("ChatSessionStatus.Update", c2)));
        return chatConsultDetailsDao.load(t.getId());
	}
	
	@Transactional
	public Optional<ChatConsultDetails> updateChatConsult(ChatConsultDetails t) {
		
		//기존 상담이력 이관처리
		chatConsultDetailsDao.update(t);
        ChatSessionStatusEx c2 = chatSessionStatusDao.loadExByChatId(t.getId()).get();
    	//채팅현황갱신
        template.convertAndSendToUser(c2.getBizId(), "/chat", JsonUtils.toJson(new StompObj("ChatSessionStatus.Update", JsonUtils.toJson(c2))));
        //template.convertAndSendToUser(c2.getBizId(), "/chat", JsonUtils.toJson(new StompObj("ChatSessionStatus.Update", c2)));
        return chatConsultDetailsDao.load(t.getId());
	}
	
	public Optional<PageResultObj<List<ChatConsultDetailsEx>>> loadChatConsultDetailsExByBrId(String bizId, PageRequest req) {
        return chatConsultDetailsDao.searchEx(bizId, req);
	}
		
	public Optional<List<ChatSessionStatus>> loadAll() {
		return chatSessionStatusDao.loadAll();
	}

	public Optional<List<ChatSessionStatus>> search(Map<String, String> map) {
		return chatSessionStatusDao.search(map);
	}
	
	@Transactional
	public void add(ChatSessionStatus t) {
		chatSessionStatusDao.register(t);
	}
	
	@Transactional
	public void update(ChatSessionStatus t) {
		chatSessionStatusDao.update(t);
	}

	public Optional<ChatSessionStatus> load(long id) {
		return chatSessionStatusDao.load(id);
	}
	
	@Transactional
	public void delete(long id) {
		chatSessionStatusDao.delete(id);
	}
	
	public Optional<ChatConsultDetails> loadChatConsultDetails(long id) {
		return chatConsultDetailsDao.load(id);
	}
	
	public Optional<ChatConsultDetailsEx> loadChatConsultDetailsEx(long id) {
		return chatConsultDetailsDao.loadEx(id);
	}
	
	public Optional<List<ChatConsultDetailsEx>> loadChatConsultDetailsExBySessionId(long id) {
		return chatConsultDetailsDao.findBySessionId(id);
	}
	
	@Transactional
	public Optional<PageResultObj<List<MessageLog>>> loadMessageListByChatConsultDetails(PageRequest req) {
		messageQueueDao.search(req).ifPresent(page -> {
			page.getItem().forEach(e->{
				messageLogDao.register(new MessageLog(e));
				messageQueueDao.delete(e.getId());
			});
		});
		return messageLogDao.search(req);
	}

	@Transactional
	public Optional<PageResultObj<List<MessageLog>>> loadMessageListByChatConsultDetails(long chatId, boolean viewPrevChat, PageRequest req) {
		messageQueueDao.loadByChatId(chatId, req).ifPresent(page -> {
			page.getItem().forEach(e->{
				messageLogDao.register(new MessageLog(e));
				messageQueueDao.delete(e.getId());
			});
		});
		req.setSortField("message_id");
		req.setSortDir(SortDirection.DESC);
		return messageLogDao.loadByChatId(chatId, viewPrevChat, req);
	}
	
	/**
	 * 상담원이 아직 읽어보지 않은 채팅 메시지를 읽은 상태로 변경
	 * 1. messageLog에 해당 채팅의 메시지 status변경
	 * 2. chat session의 unread건수 0으로 변경
	 * @param c
	 */
	@Transactional
	public void setMessageAlreadyRead(ChatConsultDetails c) {
		messageLogDao.updateByChatId(c.getId(), Constant.MESSAGE_QUEUE_STATUS_COMPLETE.get());
		chatSessionStatusDao.resetUnreadCnt(c.getId());
	}
	
	public Optional<List<MessageLog>> loadMessageListByCustomerId(String customerId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("customerId", customerId);
		List<MessageLog> resultList = messageLogDao.search(map).get();
		messageQueueDao.search(map).ifPresent(list -> {
			list.forEach(e -> {
				resultList.add(new MessageLog(e));
			});
		});

		return Optional.of(resultList);
	}
}
