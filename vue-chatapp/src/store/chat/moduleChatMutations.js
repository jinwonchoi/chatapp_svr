/*=========================================================================================
  File Name: moduleChatMutations.js
  Description: Chat Module Mutations
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


import Vue from 'vue'

export default {
  UPDATE_ABOUT_CHAT (state, obj) {
    obj.rootState.AppActiveUser.about = obj.value
  },
  UPDATE_STATUS_CHAT (state, obj) {
    obj.rootState.AppActiveUser.status = obj.value
  },


  // API AFTER
  SEND_CHAT_MESSAGE (state, payload) {
    if (payload.chatData) {
      // If there's already chat. Push msg to existing chat
      state.chats[Object.keys(state.chats).find(key => Number(key) === payload.id)].msg.push(payload.msg)
    } else {
      // Create New chat and add msg
      const chatId = payload.id
      Vue.set(state.chats, [chatId], { isPinned: payload.isPinned,
        msg: [payload.msg] })
    }
  },
  UPDATE_CONTACTS (state, contacts) {
    state.contacts = contacts
  },
  UPDATE_CHAT_CONTACTS (state, chatContacts) {
    state.chatContacts = chatContacts
  },
  UPDATE_CHATS (state, chats) {
    state.chats = chats
  },
  SET_CHAT_SEARCH_QUERY (state, query) {
    state.chatSearchQuery = query
  },
  MARK_SEEN_ALL_MESSAGES (state, payload) {
    payload.chatData.msg.forEach((msg) => {
      msg.isSeen = true
    })
  },
  TOGGLE_IS_PINNED (state, payload) {
    state.chats[Object.keys(state.chats).find(key => Number(key) === payload.id)].isPinned = payload.value
  },
  ////////////////////////////////////////////////////////
  // 추가 함수
  ////////////////////////////////////////////////////////
  SET_CHAT_SESSIONS(state, chatSessions) {
    chatSessions.forEach( (chatSession) => {
      chatSession.chatId = chatSession['chatConsultDetails']?chatSession['chatConsultDetails'].id:null
      chatSession.agentId = chatSession['agentInfo']?chatSession['agentInfo'].agentId:null
      chatSession.customerId = chatSession['customerInfo']?chatSession['customerInfo'].customerId:null
    })
    state.chatSessions = chatSessions
  },
  ADD_CHAT_SESSION(state, chatSession) {
    chatSession.chatId = chatSession['chatConsultDetails']?chatSession['chatConsultDetails'].id:null
    chatSession.agentId = chatSession['agentInfo']?chatSession['agentInfo'].agentId:null
    chatSession.customerId = chatSession['customerInfo']?chatSession['customerInfo'].customerId:null
    state.chatSessions.push(chatSession)
  },
  UPDATE_CHAT_SESSION(state, payload) { // chat session 변경
    
    var chatSession = payload.chatSession
    const chatSessionIndex = state.chatSessions.findIndex((c) => c.id == chatSession.id)
    if (payload.chatId) {
      chatSession.chatId = payload.chatId
    } else {
      chatSession.chatId = chatSession['chatConsultDetails']?chatSession['chatConsultDetails'].id:null
    }
    if (payload.agentId) {
      chatSession.agentId = payload.agentId
    } else {
      chatSession.agentId = chatSession['agentInfo']?chatSession['agentInfo'].agentId:null
    }
    Object.assign(state.chatSessions[chatSessionIndex], chatSession)
  },
  //{"type":"ChatSessionStatus.Update","sendDtm":"20200531213943678","payload":"{\"id\":1,\"chatId\":6,\"customerId\":\"7253632591\",\"agentId\":\"P060113\",\"lastMessageId\":25,\"lastMessage\":\"thanks for your answer, and some notes for your answer: 1- This method has a processing load, 2- It has delay, 3- It needs to implement security 4- thanks again for your answer :).\",\"direction\":\"I\",\"bizId\":\"A01\",\"country\":\"kr\",\"lang\":\"ko\",\"updatedDtm\":\"2020-05-31 21:39:42.545000000\",\"createdDtm\":\"2020-05-04 10:21:20.838000000\"}"}
  //{"type":"Test","sendDtm":"20200531213943693","payload":"Bye Nexmo"}
  REMOVE_CHAT_SESSION(state, id) { // chat session 삭제분 
    const chatSessionIndex = state.chatSessions.findIndex((c) => c.chatId === id)
    if (chatSessionIndex >= 0) {
      state.chatSessions.splice(chatSessionIndex, 1)
    }
  },
  SET_ACTIVE_SESSION(state, payload) {
    state.activeChatSession = payload.chatSession
  },
  // agent 목록관리
  SET_CHAT_AGENTS(state, chatAgents) {
    state.chatAgents = chatAgents
  },
  ADD_CHAT_AGENT(state, chatAgent) {
    state.chatAgents.push(chatAgent)
  },
  UPDATE_CHAT_AGENT(state, chatAgent) { // chat agent 변경
    const chatAgentIndex = state.chatAgents.findIndex((c) => c.id == chatAgent.id)
    Object.assign(state.chatAgentStatus[chatAgentIndex], chatAgent)
  },
  //{"type":"ChatSessionStatus.Update","sendDtm":"20200531213943678","payload":"{\"id\":1,\"chatId\":6,\"customerId\":\"7253632591\",\"agentId\":\"P060113\",\"lastMessageId\":25,\"lastMessage\":\"thanks for your answer, and some notes for your answer: 1- This method has a processing load, 2- It has delay, 3- It needs to implement security 4- thanks again for your answer :).\",\"direction\":\"I\",\"bizId\":\"A01\",\"country\":\"kr\",\"lang\":\"ko\",\"updatedDtm\":\"2020-05-31 21:39:42.545000000\",\"createdDtm\":\"2020-05-04 10:21:20.838000000\"}"}
  //{"type":"Test","sendDtm":"20200531213943693","payload":"Bye Nexmo"}
  REMOVE_CHAT_AGENT(state, agentId) { // chat session 삭제분 
    const chatAgentIndex = state.chatAgentStatus.findIndex((c) => c.id === agentId)
    state.chatAgents.splice(chatAgentIndex, 1)
  },

  // customer 목록관리
  SET_CHAT_CUSTOMERS(state, chatCustomers) {
    state.chatCustomerStatus = chatCustomers
  },
  ADD_CHAT_CUSTOMER(state, chatCustomer) {
    state.chatCustomerStatus.push(chatCustomer)
  },
  UPDATE_CHAT_CUSTOMER(state, chatCustomer) { // chat customer 변경
    const chatCustomerIndex = state.chatCustomerStatus.findIndex((c) => c.id == chatCustomer.id)
    Object.assign(state.chatCustomers[chatCustomerIndex], chatCustomer)
  },
  //{"type":"ChatSessionStatus.Update","sendDtm":"20200531213943678","payload":"{\"id\":1,\"chatId\":6,\"customerId\":\"7253632591\",\"agentId\":\"P060113\",\"lastMessageId\":25,\"lastMessage\":\"thanks for your answer, and some notes for your answer: 1- This method has a processing load, 2- It has delay, 3- It needs to implement security 4- thanks again for your answer :).\",\"direction\":\"I\",\"bizId\":\"A01\",\"country\":\"kr\",\"lang\":\"ko\",\"updatedDtm\":\"2020-05-31 21:39:42.545000000\",\"createdDtm\":\"2020-05-04 10:21:20.838000000\"}"}
  //{"type":"Test","sendDtm":"20200531213943693","payload":"Bye Nexmo"}
  REMOVE_CHAT_CUSTOMER(state, customerId) { // chat customer 삭제분 
    const chatCustomerIndex = state.chatCustomers.findIndex((c) => c.id === customerId)
    state.chatCustomers.splice(chatCustomerIndex, 1)
  },
  // chatConsultDetails 목록관리
  SET_ACTIVE_CHAT_CONSULT_DETAILS(state, chatConsultDetails) {
    state.activeChatConsultDetails = chatConsultDetails
  },
  SET_CHAT_CONSULT_DETAILS(state, chatConsultDetails) {
    state.chatConsultDetails = chatConsultDetails
  },
  ADD_CHAT_CONSULT_DETAILS(state, chatConsultDetail) {
    state.activeChatConsultDetails.push(chatConsultDetail)
    state.chatConsultDetails.push(chatConsultDetail)
  },
  UPDATE_CHAT_CONSULT_DETAILS(state, chatConsultDetail) { // chat customer 변경
    const chatConsultDetailIndex = state.chatConsultDetails.findIndex((c) => c.id == chatConsultDetail.id)
    Object.assign(state.chatConsultDetails[chatConsultDetailIndex], chatConsultDetail)
    const activeChatConsultDetailIndex = state.activeChatConsultDetails.findIndex((c) => c.id == chatConsultDetail.id)
    Object.assign(state.activeChatConsultDetails[activeChatConsultDetailIndex], chatConsultDetail)
  },
  REMOVE_CHAT_CONSULT_DETAILS(state, chatId) { // chat customer 삭제분 
    const chatConsultDetailIndex = state.chatConsultDetails.findIndex((c) => c.id === chatId)
    state.chatConsultDetails.splice(chatConsultDetailIndex, 1)
    const activeChatConsultDetailIndex = state.activeChatConsultDetails.findIndex((c) => c.id === chatId)
    state.activeChatConsultDetails.splice(activeChatConsultDetailIndex, 1)
  },
  // chat 내용관리

  SET_CHAT_MESSAGES(state, chatMessages) {
    state.chatMessages = chatMessages
  }, 
  ADD_CHAT_MESSAGE(state, payload) {
    const msg = payload.msg
    if (msg.messageId) {
      msg.id = msg.messageId
    }
    msg.isSent = msg.status === 'C'
    msg.isSeen = true
    msg.inbound= msg.direction==='I'

    if (payload.chatData) {
      // If there's already chat. Push msg to existing chat
      state.chatMessages[Object.keys(state.chatMessages).find(key => Number(key) === payload.chatId)].msg.push(payload.msg)
    } else {
      // Create New chat and add msg
      const chatId = payload.chatId
      Vue.set(state.chatMessages, [chatId], { isPinned: false,
        msg: payload.msg })
    }
  },
  //새로운 채팅목록을 추가
  ADD_CHAT_MESSAGES(state, payload) {
    const chatId = payload.chatId
    payload.msg.reverse().forEach((msg) => {
      if (msg.messageId) msg.id = msg.messageId
      msg.isSent = msg.status === 'C'
      msg.isSeen =true
      msg.inbound=msg.direction==='I'
    })
    Vue.set(state.chatMessages, [chatId], { isPinned: false,//payload.isPinned,
      pageNo: payload.pageNo,
      pageSize: payload.pageSize,
      msg: payload.msg })
  },
  //이전 채팅메시지를 채팅메시지목록에 추가
  FETCH_CHAT_MESSAGES(state, payload) {
    const chatMessages = state.chatMessages[Object.keys(state.chatMessages).find(key => Number(key) === payload.chatId)]
    payload.msg.forEach((msg) => {
      if (msg.messageId) msg.id = msg.messageId
      msg.isSent = msg.status === 'C'
      msg.isSeen =true
      msg.inbound=msg.direction==='I'
    })
    payload.msg.concat(chatMessages.msg)
    chatMessages.msg=payload.msg 
    chatMessages.pageNo=payload.pageNo,
    chatMessages.pageSize=payload.pageSize
  },

  //대기 메시지 
  SET_WAITING_MESSAGES(state, messages) {
    messages.forEach( (msg) => {
      msg.isSent = msg.status === 'C'
      msg.isSeen =true
      msg.inbound=msg.direction==='I'
    })
    state.waitingMessages = messages
  },
  TAKE_WAITING_MESSAGES(state, chatConsultDetails) {
    state.waitingMessages.forEach((msg) => {
      msg.chatId = chatConsultDetails.id
      msg.status = 'C'
      msg.isSent = true
      msg.isSeen = true
      msg.inbound= msg.direction==='I'
    })
    Vue.set(state.chatMessages, [chatConsultDetails.id], { isPinned: false,//payload.isPinned,
      pageNo: 1,
      pageSize: 50,
      msg: state.waitingMessages })
  },
  //대화창열때 session목록에서 안읽어본 건수를 0으로 설정
  SET_MESSAGES_ALREADY_READ(state, chatId) {
    state.chatSessions.find((session) => session.chatId === chatId).unreadCnt = 0
  }
}

