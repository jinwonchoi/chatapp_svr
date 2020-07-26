/*=========================================================================================
  File Name: moduleAuthMutations.js
  Description: Auth Module Mutations
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/

//import axios from '../../http/axios/index.js'
import axios from '@/axios.js'
//import acl from '@/acl/acl'

export default {
  //https://stackoverflow.com/questions/59836005/receiving-null-authorization-header-in-spring-boot-from-requests-with-angular-7
  SET_BEARER (state, accessToken) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`
  },
  // SET_LOGOUT(state, logout) {
  //   acl.change('admin')
  //   //router.push('/pages/login').catch(() => {})
  // }

}
