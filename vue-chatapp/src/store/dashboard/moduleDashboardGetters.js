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
  // chatDataOfUser: state => id => {
  //   return state.chats[Object.keys(state.chats).find(key => Number(key) === id)]
  // },
  // chatContacts: (state, getters) => {
  //   const chatContacts = state.chatContacts.filter((contact) => contact.displayName.toLowerCase().includes(state.chatSearchQuery.toLowerCase()))

  //   chatContacts.sort((x, y) => {
  //     const timeX = getters.chatLastMessaged(x.uid).time
  //     const timeY = getters.chatLastMessaged(y.uid).time
  //     return new Date(timeY) - new Date(timeX)
  //   })

  //   return chatContacts.sort((x, y) => {
  //     const chatDataX = getters.chatDataOfUser(x.uid)
  //     const chatDataY = getters.chatDataOfUser(y.uid)
  //     if (chatDataX && chatDataY) return chatDataY.isPinned - chatDataX.isPinned
  //     else return 0
  //   })
  // },

}
