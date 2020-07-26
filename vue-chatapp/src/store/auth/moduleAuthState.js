/*=========================================================================================
  File Name: moduleAuthState.js
  Description: Auth Module State
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


import auth from '@/auth/authService'

export default {
  isUserLoggedIn: () => {
    console.log('isUserLoggedIn')
    return localStorage.getItem('userInfo') && auth.isAuthenticated() 
  }
}
