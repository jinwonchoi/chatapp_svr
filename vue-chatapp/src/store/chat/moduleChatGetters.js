/*=========================================================================================
  File Name: moduleChatGetters.js
  Description: Chat Module Getters
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


// import contacts from '@/views/apps/chat/contacts'

export default {
  chatDataOfUser: state => id => {
    return state.chats[Object.keys(state.chats).find(key => Number(key) === id)]
  },
  chatContacts: (state, getters) => {
    const chatContacts = state.chatContacts.filter((contact) => contact.displayName.toLowerCase().includes(state.chatSearchQuery.toLowerCase()))

    chatContacts.sort((x, y) => {
      const timeX = getters.chatLastMessaged(x.uid).time
      const timeY = getters.chatLastMessaged(y.uid).time
      return new Date(timeY) - new Date(timeX)
    })

    return chatContacts.sort((x, y) => {
      const chatDataX = getters.chatDataOfUser(x.uid)
      const chatDataY = getters.chatDataOfUser(y.uid)
      if (chatDataX && chatDataY) return chatDataY.isPinned - chatDataX.isPinned
      else return 0
    })
  },
  contacts: (state) => state.contacts.filter((contact) => contact.displayName.toLowerCase().includes(state.chatSearchQuery.toLowerCase())),
  contact: (state) => contactId => state.contacts.find((contact) => contact.uid === contactId),
  chats: (state) => state.chats,
  chatUser: (state, getters, rootState) => id => state.contacts.find((contact) => contact.uid === id) || rootState.AppActiveUser,

  chatLastMessaged: (state, getters) => id => {
    if (getters.chatDataOfUser(id)) return getters.chatDataOfUser(id).msg.slice(-1)[0]
    else return false
  },
  chatUnseenMessages: (state, getters) => id => {
    let unseenMsg = 0
    const chatData = getters.chatDataOfUser(id)
    if (chatData) {
      chatData.msg.map((msg) => {
        if (!msg.isSeen && !msg.isSent) unseenMsg++
      })
    }
    return unseenMsg
  },
  ///////////////////////////////////////////////////
  // 추가 내용
  ///////////////////////////////////////////////////
  //특정 chat id의 대화내용
  chatMessagesOfChatId: state => id => {
    return state.chatMessages[Object.keys(state.chatMessages).find(key => Number(key) === id)]
  },
  // chat 전체데이터
  chatMessages: (state) => state.chatMessages,
  chatAgent: (state) => id => state.chatAgents.find((agent) => agent.id === id),
  chatSession: (state) => id => state.chatSessions.find((chatSession) => chatSession.id === id),
  chatSessions: (state) => state.chatSessions,
  chatSessionOfChatId: (state) => id => state.chatSessions.find((chatSession) => chatSession.chatId === id),
  chatSessionAgent: (state) => id => {
    const details = state.activeChatConsultDetails.find((e) => e.id === id)
    return (details)?details.agentInfo:null
  },
  chatConsultDetails: (state) => id =>  state.chatConsultDetails.find((chatConsultDetail) => chatConsultDetail.id === id),
  activeChatConsultDetails: (state) => id =>  state.activeChatConsultDetails.find((chatConsultDetail) => chatConsultDetail.id === id),
  waitingMessages: (state) => state.waitingMessages
  // is_seen 
  // is_sent 
}
