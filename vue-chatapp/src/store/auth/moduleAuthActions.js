/*=========================================================================================
  File Name: moduleAuthActions.js
  Description: Auth Module Actions
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/

import jwt from '../../http/requests/auth/jwt/index.js'


import router from '@/router.js'
import acl from '../../acl/acl'

export default {
  //TODO: 사용자 profile변경은 추후 정리
  updateUsername ({ commit }, payload) {
    //payload.user.updateProfile({
      //추후 updateProfile작업
    jwt.updateProfile({
      displayName: payload.displayName
    }).then(() => {

      // If username update is success
      // update in localstorage
      const newUserData = Object.assign({}, payload.user.providerData[0])
      newUserData.displayName = payload.displayName
      commit('UPDATE_USER_INFO', newUserData, {root: true})

      console.log("updateUsername")
      // If reload is required to get fresh data after update
      // Reload current page
      console.log("payload.isReloadRequired:"+JSON.stringify(payload))
      if (payload.isReloadRequired) {
        router.push(router.currentRoute.query.to || '/')
      }
      console.log("payload.isReloadRequired2:"+JSON.stringify(payload))
    }).catch((err) => {
      payload.notify({
        time: 8800,
        title: 'Error',
        text: err.message,
        iconPack: 'feather',
        icon: 'icon-alert-circle',
        color: 'danger'
      })
    })
  },
  // JWT
  loginJWT ({ commit }, payload) {

    return new Promise((resolve, reject) => {
      jwt.login(payload.userDetails.loginId, payload.userDetails.password)
        .then(response => {

          // If there's user data in response
          if (response.data.resultCode === "0001" && response.data.item.userInfo ) {
            var userInfo = response.data.item.userInfo
            var accessToken  = response.data.item.token
            console.log(response.data.item.userInfo)
            console.log(response.data.item.token)
            console.log(router.currentRoute)

            // Set accessToken
            localStorage.setItem('accessToken', accessToken)
            // Convert the JWT expiry time from seconds to milliseconds
            var tmLogin= Date.now()
            var tokenExpiry = new Date(tmLogin+3600*1000)
            localStorage.setItem('tokenExpiry', tokenExpiry)
            localStorage.setItem('loggedIn', 'true')

            const URL = process.env.NODE_ENV === 'production'?process.env.VUE_APP_PROD_URL: process.env.VUE_APP_DEV_URL

            // Update user details
            commit('UPDATE_USER_INFO', {
              uid: userInfo.id,
              userName: userInfo.userName,
              loginId: userInfo.loginId,
              displayName: `${userInfo.userName}(${userInfo.loginId})`, 
              userEmail: userInfo.userEmail,
              role: userInfo.role,
              agentId: userInfo.agentId,
              bizId: userInfo.bizId,
              groupId: userInfo.groupId,
              lang: userInfo.lang,
              country: userInfo.country,
              userProfile: userInfo.userProfile,
              about: userInfo.userProfile,
              photoURL: URL+userInfo.profileUrl,
              providerId: 'jwt',//this.profile.sub.substr(0, this.profile.sub.indexOf('|')),
              exp: tmLogin //60초
            }, {root: true})
            console.log("aa")
            // Set bearer token in axios
            commit('SET_BEARER', accessToken)
            console.log("bvb")
            // Navigate User to homepage
            router.push(router.currentRoute.query.to || '/')
            resolve(response)
          } else {
            reject({message: 'Wrong Login Id or Password'})
          }

        })
        .catch(error => { reject(error) })
    })
  },
  logout({commit}, payload) {
    return new Promise((resolve, reject) => {
      console.log("logout promise")
      acl.change('admin')
      resolve(response)
    })
  },
  registerUserJWT ({ commit }, payload) {


    const { displayName, loginId, password, confirmPassword } = payload.userDetails

    return new Promise((resolve, reject) => {

      // Check confirm password
      if (password !== confirmPassword) {
        reject({message: 'Password doesn\'t match. Please try again.'})
      }

      jwt.registerUser(displayName, loginId, password)
        .then(response => {
          // Redirect User
          router.push(router.currentRoute.query.to || '/')

          // Update data in localStorage
          localStorage.setItem('accessToken', response.data.accessToken)
          commit('UPDATE_USER_INFO', response.data.userData, {root: true})

          resolve(response)
        })
        .catch(error => { reject(error) })
    })
  },
  //
  fetchAccessToken () {
    return new Promise((resolve) => {
      jwt.refreshToken().then(response => { resolve(response) })
    })
  }
}
