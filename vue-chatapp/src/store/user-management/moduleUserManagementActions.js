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
  fetchUsers ({ commit }, payload) {
    const queryParam = { 
      pageNo: payload.pageNo,
      pageSize: payload.pageSize,
      lastOffset:(payload.pageNo - 1) * payload.pageSize, 
      sortField: (payload.sortField)?payload.sortField:'id',
      sortDir: (payload.sortDir)?payload.sortDir:'ASC',
      searchMap: (payload.searchMap)?payload.searchMap:{},
      searchByOrMap: (payload.searchByOrMap)?payload.searchByOrMap:{}
    }
    return new Promise((resolve, reject) => {
      axios.post('/user/page',queryParam)
        .then((response) => {
          commit('SET_USERS', { 
            'totalCount':response.data.totalCnt,
            'pageSize':response.data.pageSize,
            'pageNo': response.data.pageNo,
            'users': response.data.item })
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  fetchUser (context, userId) {
    return new Promise((resolve, reject) => {
      axios.get(`/user/${userId}`)
        .then((response) => {
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  deleteUser ({ commit }, userId) {
    return new Promise((resolve, reject) => {
      axios.delete(`/user/${userId}`)
        .then((response) => {
          commit('REMOVE_USER', userId)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  addUser({ commit }, item) {
    return new Promise((resolve, reject) => {
      axios.post("/user/add", {item: item})
        .then((response) => {
          commit('ADD_USER', Object.assign(item, {id: response.data.id}))
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
  updateUser({ commit }, item) {
    return new Promise((resolve, reject) => {
      axios.post("/user/update", {item: item})
        .then((response) => {
          commit('UPDATE_USER', item)
          resolve(response)
        })
        .catch((error) => { reject(error) })
    })
  },
}
