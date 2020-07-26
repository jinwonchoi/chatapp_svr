/*=========================================================================================
  File Name: moduleCalendarMutations.js
  Description: Calendar Module Mutations
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


export default {
  SET_CONSULTS (state, payload) {
    state.pageSize=payload.pageSize
    state.totalCount=payload.totalCount
    state.totalPage=Math.ceil(payload.totalCount/payload.pageSize)
    state.currentPage=payload.pageNo
    state.consults=payload.consults
  },
  UPDATE_CONSULT_TYPE_FILTER (state, filterName) {
    if (filterName==="all" || state.queryParam.searchMap['consultType']===filterName) {
      delete state.queryParam.searchMap['consultType']
    } else {
      state.queryParam.searchMap['consultType'] = filterName
    }
  },
  SET_QUERY_PARAM (state, queryParam) {
    state.queryParam = queryParam
  },
}
