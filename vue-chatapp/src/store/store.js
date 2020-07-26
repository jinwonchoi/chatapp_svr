/*=========================================================================================
  File Name: store.js
  Description: Vuex store
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


import Vue from 'vue'
import Vuex from 'vuex'

import state from "./state"
import getters from "./getters"
import mutations from "./mutations"
import actions from "./actions"

Vue.use(Vuex)

import moduleTodo from './todo/moduleTodo.js'
import moduleCalendar from './calendar/moduleCalendar.js'
import moduleChat from './chat/moduleChat.js'
//import moduleEmail from './email/moduleEmail.js'
import moduleAuth from './auth/moduleAuth.js'
import moduleNoticeBoard from './notice-board/moduleNoticeBoard.js'
import moduleDashboard from './dashboard/moduleDashboard.js'
import moduleConsult from './consult/moduleConsult'

export default new Vuex.Store({
    getters,
    mutations,
    state,
    actions,
    modules: {
       todo: moduleTodo,
       calendar: moduleCalendar,
       chat: moduleChat,
      // email: moduleEmail,
      auth: moduleAuth,
      consult: moduleConsult,
      noticeBoard: moduleNoticeBoard,
      dashboard: moduleDashboard
    },
      strict: process.env.NODE_ENV !== 'production'
})
