/*=========================================================================================
  File Name: moduleChatActions.js
  Description: Chat Module Actions
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/

import axios from '@/axios.js'

export default {
  setChatSearchQuery ({ commit }, query) {
    commit('SET_CHAT_SEARCH_QUERY', query)
  },
  updateAboutChat ({ commit, rootState }, value) {
    commit('UPDATE_ABOUT_CHAT', {
      rootState,
      value
    })
  },
  updateStatusChat ({ commit, rootState }, value) {
    commit('UPDATE_STATUS_CHAT', {
      rootState,
      value
    })
  },

  // API CALLS
  sendChatMessage ({ getters, commit, dispatch }, payload) {
    return new Promise((resolve, reject) => {
      axios.post('/api/apps/chat/msg', {payload})
        .then((response) => {
          payload.chatData = getters.chatDataOfUser(payload.id)
          if (!payload.chatData) { dispatch('fetchChatContacts') }
          commit('SEND_CHAT_MESSAGE', payload)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

  // Get contacts from server. Also change in store
  fetchContacts ({ commit }) {
    return new Promise((resolve, reject) => {
      axios.get('/api/apps/chat/contacts', {params: {q: ''}})
        .then((response) => {
          commit('UPDATE_CONTACTS', response.data)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

  // Get chat-contacts from server. Also change in store
  fetchChatContacts ({ commit }) {
    return new Promise((resolve, reject) => {
      axios.get('/api/apps/chat/chat-contacts', {params: {q: ''}})
        .then((response) => {
          commit('UPDATE_CHAT_CONTACTS', response.data)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

  // Get chats from server. Also change in store
  fetchChats ({ commit }) {
    return new Promise((resolve, reject) => {
      axios.get('/api/apps/chat/chats')
        .then((response) => {
          commit('UPDATE_CHATS', response.data)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

  markSeenAllMessages ({ getters, commit }, id) {

    return new Promise((resolve, reject) => {
      axios.post('/api/apps/chat/mark-all-seen', {id})
        .then((response) => {
          commit('MARK_SEEN_ALL_MESSAGES', {
            id,
            chatData: getters.chatDataOfUser(id)
          })
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

  toggleIsPinned ({ commit }, payload) {
    return new Promise((resolve, reject) => {
      axios.post('/api/apps/chat/set-pinned/', {contactId: payload.id,
        value: payload.value})
        .then((response) => {
          commit('TOGGLE_IS_PINNED', payload)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  ///////////////////////////////////////////////////////////////////////////////////////////////////////
  // 1. 채팅세션상태 조회 /chat/session/list
  // getChatSessionStatusByBizId(UserInfo)
  fetchChatSessionStatus ({ commit, rootState }) {
    return new Promise((resolve, reject) => {
      axios.post('/chat/session/listex', {"bizId":rootState.AppActiveUser.bizId})
        .then((response) => {
          if (response.data.resultCode === "0001") {
            commit('SET_CHAT_SESSIONS', response.data.item)
          }
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

  // 2. 미지정메시지 조회 /chat/message/list
  // getWaitingMessageQueue(ChatSessionStatus)
  fetchWaitingMessageQueue ({ commit }, payload) {
    return new Promise((resolve, reject) => {
      axios.post('/chat/message/list', { 'customerId': payload.chatSession.customerInfo.customerId})
        .then((response) => {
          commit('SET_WAITING_MESSAGES', response.data.item)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  // 3. 메시지 채팅창으로 띄워줌
  // 
  // 4. 미지정메시지 상담선택: /chat/take/waitsession
  //  -> 세션현황을 상담으로 등록
  // takeWaitingSessionForChat(ChatSessionStatus) ==> 세션현황 stomp sync
  takeWaitingSessionForChat ({ commit }, payload) {
    return new Promise((resolve, reject) => {
      axios.post('/chat/take/waitsession', { ...payload.chatSession, 'agentId':payload.agentId } )
        .then((response) => {
          const chatSession = payload.chatSession
          const chatConsultDetails = response.data.item
          commit('UPDATE_CHAT_SESSION', { 'chatSession':chatSession, 'agentId':payload.agentId, 'chatId':chatConsultDetails.id })
          commit('TAKE_WAITING_MESSAGES', chatConsultDetails)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

  setMessagesAlreadyRead ({ commit }, payload) {

    return new Promise((resolve, reject) => {
      axios.post('/chat/chatdetail/setAlreadyRead', {id:payload.chatId})
        .then((response) => {
          commit('SET_MESSAGES_ALREADY_READ', payload.chatId)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

  // 5. 상담메시지 전송 /chat/message/send
  // sendMessageToNexmo(MessageQueue) ==> 세션현황 stomp sync
  sendMessageToNexmo({}, payload) {
    return new Promise((resolve, reject) => {
      const chatConsultDetails = payload.chatConsultDetails
      axios.post('/chat/message/send', {
        customerId:chatConsultDetails.customerId,
        chatId: chatConsultDetails.id,
        direction: 'O',
        bizId: chatConsultDetails.bizId,
        country: chatConsultDetails.country,
        lang: chatConsultDetails.lang,
        status: 'N',
        message: payload.msg
      })
        .then((response) => {
          // msg추가는 stomp를 통해 받는다. -> chatSession, 
          // const msg = response.data.item
          // commit('ADD_CHAT_MESSAGE', { 'chatId':msg.chatId, 'msg': msg })
          if (response.data.resultCode !== "0001") throw "message send fail!"
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  // 6.1 상담내역확인 /chat/chatdetail/:chatId
  // getChatConsultDetails(String)
  fetchActiveChatConsultDetails({ getters, commit }, payload) {
    return new Promise((resolve, reject) => {
      axios.post(`/chat/chatdetail/${payload.chatId}`, {})
        .then((response) => {
          const chatConsultDetails = getters.chatConsultDetails(payload.chatId)
          if (chatConsultDetails) {
            commit('UPDATE_CHAT_CONSULT_DETAILS', response.data.item)
          } else {
            commit('SET_ACTIVE_CHAT_CONSULT_DETAILS', response.data.item)
          }
          resolve(response)
        })

        .catch((error) => { reject(error) })
    })
  },
  // 6.2 상담내역확인(이관이력포함) /chat/chatdetail/hist/:sessionId
  // getChatConsultDetails(String)
  fetchActiveChatConsultDetailsHist({ getters, commit }, payload) {
    return new Promise((resolve, reject) => {
      axios.post(`/chat/chatdetail/hist/${payload.sessionId}`, {})
        .then((response) => {
          commit('SET_ACTIVE_CHAT_CONSULT_DETAILS', response.data.item)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

  //고객기준 상담이력
  fetchChatConsultDetails ({ commit, rootState }, payload) {
    return new Promise((resolve, reject) => {
      const queryParam = { 
        pageNo: 1,
        pageSize: 200,
        lastOffset:0, 
        sortField: 'id',
        sortDir: 'ASC',
        searchMap: {'customerId':payload.customerId},
        searchByOrMap: {}
      }
      console.log(queryParam)
      axios.post(`/chat/consult/listex/${rootState.AppActiveUser.bizId}`, queryParam)
      .then((response) => {
        commit('SET_CHAT_CONSULT_DETAILS', response.data.item )
        resolve(response)
      })
      .catch((error) => { reject(error) })
    })
  },

  // 7. 특정상담 메시지내역 조회  /chat/chatdetail/:chatId/messages
  // getMessageListByChatConsultDetails(PageRequest)
  //payload: 
  //  chatId: long, 
  //  viewPrevChat: "yes"|"no", 
  //  morePage: true | false    
  //    
  getMessagesByChatConsultDetails({ getters, commit }, payload) {
    return new Promise((resolve, reject) => {
      
      const chatMessages = getters.chatMessagesOfChatId(payload.chatId)
      const _pageNo = (chatMessages)?((payload.morePage)?(chatMessages.pageNo+1):chatMessages.pageNo):1
      const _pageSize = (chatMessages)?chatMessages.pageSize:30

      if (chatMessages) return
      axios.post(`/chat/chatdetail/${payload.chatId}/messages/${payload.viewPrevChat}`, { 
        pageNo: _pageNo,
        pageSize: _pageSize,
        lastOffset:(_pageNo - 1) * _pageSize, 
        searchMap: {}
      })
        .then((response) => {
          if (response.data.resultCode !== "0001") throw "No data"
          if (chatMessages) {
            commit('FETCH_CHAT_MESSAGES', { 
              'chatId':payload.chatId,
              'pageSize':response.data.pageSize,
              'pageNo': response.data.pageNo,
              'msg': response.data.item })
          } else {
            commit('ADD_CHAT_MESSAGES', { 
              'chatId':payload.chatId,
              'pageSize':response.data.pageSize,
              'pageNo': response.data.pageNo,
              'msg': response.data.item })
          }
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  //TODO
  // getMoreMessagesByChatConsultDetails({}) {
  // },
  // 8. My채팅세션목록 조회 /chat/session/mylist
  // getChatSessionStatusByAgentId(UserInfo) 
  getChatSessionStatusByAgentId({ rootState }) {
    return new Promise((resolve, reject) => {
      axios.post("/chat/session/mylist", { agentId: rootState.AppActiveUser.agentId })
        .then((response) => {
          resolve(response)
        })

        .catch((error) => { reject(error) })
    })
  },
  // 11. 그룹공지 확인
  // 12. 상담완료처리 /chat/chatdetail/complete
  completeChatConsultDetails({ commit }, payload) {
    return new Promise((resolve, reject) => {
      //const chatConsultDetails = getters.chatConsultDetails(payload.chatId)
      axios.post("/chat/chatdetail/complete", payload.chatConsultDetails)
        .then((response) => {
          commit('REMOVE_CHAT_SESSION', payload.chatConsultDetails.id)
          commit('UPDATE_CHAT_CONSULT_DETAILS', payload.chatConsultDetails)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

  // 13. 상담원목록조회 /user/search
  // 14. 상담이관처리 /chatdetail/passover/{agentId}
  passOverChatConsultDetails({ getters, commit }, payload) {
    return new Promise((resolve, reject) => {
      //const chatConsultDetails = getters.chatConsultDetails(payload.chatId)
      axios.post(`/chat/chatdetail/passover/${payload.passoverAgentId}`, payload.chatConsultDetails)
        .then((response) => {
          const newChatConsultDetails = response.data.item
          const chatSession = getters.chatSessionOfChatId(payload.chatConsultDetails.id)
          // commit('UPDATE_CHAT_SESSION', { 
          //   'chatSession':{
          //     ...chatSession,
          //     agentId:payload.passoverAgentId,
          //     chatId:newChatConsultDetails.id
          //   }
          // })
          commit('UPDATE_CHAT_CONSULT_DETAILS', newChatConsultDetails)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

  // 15. 상담내역 변경
  updateChatConsultDetails({ getters, commit }, payload) {
    return new Promise((resolve, reject) => {
      //const chatConsultDetails = getters.chatConsultDetails(payload.chatId)
      axios.post('/chat/chatdetail/update', payload.chatConsultDetails)
        .then((response) => {
          const newChatConsultDetails = response.data.item
          const chatSession = getters.chatSessionOfChatId(payload.chatConsultDetails.id)
          commit('UPDATE_CHAT_SESSION', { 
            'chatSession':{
              ...chatSession,
              chatId: newChatConsultDetails.id,
              chatConsultDetails: newChatConsultDetails
            }
          })
          commit('REMOVE_CHAT_CONSULT_DETAILS', payload.chatConsultDetails.id)
          commit('ADD_CHAT_CONSULT_DETAILS', newChatConsultDetails)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

  // 1. 채팅세션상태 조회 /chat/session/list
  // getChatSessionStatusByBizId(UserInfo)
  // 2. 미지정메시지 조회 /chat/message/list
  // getWaitingMessageQueue(ChatSessionStatus)
  // 3. 메시지 채팅창으로 띄워줌
  // 
  // 4. 미지정메시지 상담선택: /chat/take/waitsession
  //  -> 세션현황을 상담으로 등록
  // takeWaitingSessionForChat(ChatSessionStatus) ==> 세션현황 stomp sync
  // 5. 상담메시지 전송 /chat/message/send
  // sendMessageToNexmo(MessageQueue) ==> 세션현황 stomp sync
  // 6. 상담내역확인 /chat/chatdetail/:chatId
  // getChatConsultDetails(String)
  // 7. 특정상담 메시지내역 조회  /chat/chatdetail/messages
  // getMessageListByChatConsultDetails(PageRequest)
  // 8. My채팅세션목록 조회 /chat/session/mylist
  // getChatSessionStatusByAgentId(UserInfo) 
  // 9. 상담상세 조회  /chat/chatdetail/:chatId
  // 10. 채팅메시지 조회-큐대기포함 /chat/chatdetail/messages/:chatId
  // 11. 그룹공지 확인
  // 12. 상담완료처리 /chat/chatdetail/complete
  // 13. 상담원목록조회 /user/search
  // 14. 상담이관처리 /chatdetail/passover/{agentId}

}
