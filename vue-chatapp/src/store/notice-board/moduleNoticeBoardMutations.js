/*=========================================================================================
  File Name: moduleCalendarMutations.js
  Description: Calendar Module Mutations
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


export default {
  TEST_NOTICES (state) {
    state.totalPage+=1
    state.currentPage+=1
  },
  SET_NOTICES (state, payload) {
    state.pageSize=payload.pageSize
    state.totalCount=payload.totalCount
    state.totalPage=Math.ceil(payload.totalCount/payload.pageSize)
    state.currentPage=payload.pageNo
    state.notices=payload.notices
  },
  ADD_NOTICE (state, notice) {
    state.notices.push(notice)
  },
  // SET_NOTICE (state, notice) {
  //   state.notices = notice
  // },
  REMOVE_NOTICE (state, id) {
    const noticeIndex = state.notices.findIndex((u) => u.id === id)
    state.notices.splice(noticeIndex, 1)
  },
  UPDATE_NOTICE (state, notice) {
    const noticeIndex = state.notices.findIndex((u) => u.id === notice.id)
    Object.assign(state.notices[noticeIndex], notice)
  },
  SET_QUERY_PARAM (state, queryParam) {
    state.queryParam = queryParam
  }
}
