/*=========================================================================================
  File Name: moduleCalendarActions.js
  Description: Calendar Module Actions
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/

//import axios from '@/axios.js'
//import axios from '../../http/axios/index.js'
import axios from '@/axios.js'
export default {
  fetchConsults ({ commit, rootState, state }, payload) {
    return new Promise((resolve, reject) => {
      const queryParam = { 
        pageNo: payload.pageNo,
        pageSize: payload.pageSize,
        lastOffset:(payload.pageNo - 1) * payload.pageSize, 
        sortField: (payload.sortField)?payload.sortField:'id',
        sortDir: (payload.sortDir)?payload.sortDir:'ASC',
        searchMap: state.queryParam.searchMap,
        searchByOrMap: (payload.searchByOrMap)?payload.searchByOrMap:{}
      }
      commit('SET_QUERY_PARAM', queryParam)      
      axios.post(`/chat/consult/listex/${rootState.AppActiveUser.bizId}`, queryParam)
      .then((response) => {
        commit('SET_CONSULTS', { 
          'totalCount':response.data.totalCnt,
          'pageSize':response.data.pageSize,
          'pageNo': response.data.pageNo,
          'consults': response.data.item })
        resolve(response)
      })
      .catch((error) => { reject(error) })
    })
  },
  fetchConsult (id) {
    return new Promise((resolve, reject) => {
      axios.get(`/chat/chatdetail/${id}`)
        .then((response) => {
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

}
