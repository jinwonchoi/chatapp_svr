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
  getCustomerInboundCount ({ commit }, payload) {
    return new Promise((resolve, reject) => {
        axios.post(`/dashboard/custInboundCount/${payload.bizId}`)
        .then((response) => {
                  //if (response.data.resultCode !== "0001") throw "No data"
        commit('SET_CUSTOMER_INBOUND_COUNT', { 
          'item':response.data.item })
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  getResponseCount ({ commit }, payload) {
    return new Promise((resolve, reject) => {
      //todo: bizId or agentId
      axios.post(`/dashboard/responseCountByBiz/${payload.bizId}`)
        .then((response) => {
                  //if (response.data.resultCode !== "0001") throw "No data"
        commit('SET_RESPONSE_COUNT', { 
          'item':response.data.item })
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  getResolvedCount ({ commit }, payload) {
    return new Promise((resolve, reject) => {
      //todo: bizId or agentId
      axios.post(`/dashboard/resolvedCountByBiz/${payload.bizId}`)
        .then((response) => {
                  //if (response.data.resultCode !== "0001") throw "No data"
        commit('SET_RESOLVED_COUNT', { 
          'item':response.data.item })
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  getResolvedGoal ({ commit }, payload) {
    return new Promise((resolve, reject) => {
      //todo: bizId or agentId
      axios.post(`/dashboard/resolvedGoal/${payload.bizId}`)
        .then((response) => {
                  //if (response.data.resultCode !== "0001") throw "No data"
        commit('SET_RESOLVED_GOAL', { 
          'item':response.data.item })
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },

}
