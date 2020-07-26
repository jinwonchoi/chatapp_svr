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
  // addItem({ commit }, item) {
  //   return new Promise((resolve, reject) => {
  //     axios.post("/api/data-list/products/", {item: item})
  //       .then((response) => {
  //         commit('ADD_ITEM', Object.assign(item, {id: response.data.id}))
  //         resolve(response)
  //       })
  //       .catch((error) => { reject(error) })
  //   })
  // },
  fetchNotices ({ commit }, payload) {
    return new Promise((resolve, reject) => {
      const queryParam = { 
        pageNo: payload.pageNo,
        pageSize: payload.pageSize,
        lastOffset:(payload.pageNo - 1) * payload.pageSize, 
        sortField: (payload.sortField)?payload.sortField:'id',
        sortDir: (payload.sortDir)?payload.sortDir:'ASC',
        searchMap: (payload.searchMap)?payload.searchMap:{},
        searchByOrMap: (payload.searchByOrMap)?payload.searchByOrMap:{}
      }
      commit('SET_QUERY_PARAM', queryParam)      
      axios.post(`/notice-board/search`, queryParam)
      .then((response) => {
        //if (response.data.resultCode !== "0001") throw "No data"
        commit('SET_NOTICES', { 
          'totalCount':response.data.totalCnt,
          'pageSize':response.data.pageSize,
          'pageNo': response.data.pageNo,
          'notices': response.data.item })
        resolve(response)
      })
      .catch((error) => { reject(error) })
    })
  },
  fetchNotice (id) {
    return new Promise((resolve, reject) => {
      axios.get(`/notice-board/${id}`)
        .then((response) => {
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  deleteNotice ({}, id) {
    return new Promise((resolve, reject) => {
      axios.post(`/notice-board/delete/${id}`)
        .then((response) => {
          //commit('REMOVE_NOTICE', id)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  addNotice({}, item) {
    return new Promise((resolve, reject) => {
      axios.post("/notice-board/add", item)
        .then((response) => {
          // commit('ADD_NOTICE', Object.assign(item, {id: response.data.id
          //             , updatedDtm: response}))
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  updateNotice({}, item) {
    return new Promise((resolve, reject) => {
      axios.post("/notice-board/update", item)
        .then((response) => {
          //commit('UPDATE_NOTICE', item)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
}
