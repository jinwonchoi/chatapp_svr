/*=========================================================================================
  File Name: moduleCalendarMutations.js
  Description: Calendar Module Mutations
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


export default {
  ADD_USER (state, user) {
    state.users.push(user)
  },
  SET_USERS (state, payload) {
    state.pageSize=payload.pageSize
    state.totalCount=payload.totalCount
    state.totalPage=Math.ceil(payload.totalCount/payload.pageSize)
    state.currentPage=payload.pageNo
    state.users=payload.users
  },
  REMOVE_USER (state, userId) {
    const userIndex = state.users.findIndex((u) => u.id === userId)
    state.users.splice(userIndex, 1)
  },
  UPDATE_USER (state, user) {
    const userIndex = state.users.findIndex((u) => u.id === user.id)
    Object.assign(state.users[userIndex], user)
  },
  SET_QUERY_PARAM (state, queryParam) {
    state.queryParam = queryParam
  }
}
