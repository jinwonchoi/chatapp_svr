/**
 * 일단 auth0을 사용하지 않고 직접 서버접속으로 login하는 기능을 적용
 * auth0사용은 authServiceUsingAuth0.js파일 사용
 */
import EventEmitter from 'events'
//import authConfig from '@/../auth_config.json'

import store from '@/store/store.js'
import router from '@/router'
import jwt from '../http/requests/auth/jwt/index.js'

// 'loggedIn' is used in other parts of application. So, Don't forget to change there also
const localStorageKey = 'loggedIn'

const tokenExpiryKey = 'tokenExpiry'
const loginEvent = 'loginEvent'

//const redirectUri = `${window.location.origin + process.env.BASE_URL}callback`
// const webAuth = new auth0.WebAuth({
//   domain: authConfig.domain,
//   redirectUri: `${window.location.origin + process.env.BASE_URL}callback`,
//   clientID: authConfig.clientId,
//   responseType: 'id_token',
//   scope: 'openid profile email'
// })
/**
 * 로그세션유지 방식:
 * 최초 화면열때 App.vue에서 isUserLoggedIn 확인
 * if isUserLoggedIn
 *    renewToken
 * else
 *    login화면
 * 
 * isUserLoggedIn:
 * - storage에 없거나 로그인주기 만료 여부
 * 
 * logout:
 * - storage삭제
 * - state삭제
 * 
 * login:
 * - invalidateSession
 * - 로그인 및 storage 추가
 * 
 * renewToken:
 * - storage업데이트
 * - 
 *  
 *   
 */
class AuthService extends EventEmitter {
    idToken = null;
    profile = null;
    tokenExpiry = null;

    // Starts the user login flow//
    // 로그인 페이지로 이동
    // login (customState) {
    //   //TODO
    // }

    // Handles the callback request from Auth0
    //TODO
    handleAuthentication () {
    //   return new Promise((resolve, reject) => {
    //     webAuth.parseHash((err, authResult) => {
    //       if (err) {
    //         alert(`${err.error}. Detailed error can be found in console.`)
    //         reject(err)
    //       } else {
    //         this.localLogin(authResult)
    //         resolve(authResult.idToken)
    //       }
    //     })
    //   })
    }

    /**
     * @param {*} authResult 
     * authResult.idToken 토큰 사용안함
     * authResult.idTokenPayLoad 사용자 정보
     */
    /**
     * 로그인정보 local에 저장하고 store에 user정보 반영
     * logingEvent로 등록된 handler에 로그인정보 전달.
     * @param {*} authResult 
     */
    localLogin (authResult) {
      this.profile = authResult.payload

      // Convert the JWT expiry time from seconds to milliseconds
      this.tokenExpiry = new Date(this.profile.exp+3600*1000)
      localStorage.setItem(tokenExpiryKey, this.tokenExpiry)
      localStorage.setItem(localStorageKey, 'true')

      const URL = process.env.NODE_ENV === 'production'?process.env.VUE_APP_PROD_URL: process.env.VUE_APP_DEV_URL

      store.commit('UPDATE_USER_INFO', {
        displayName: this.profile.userName,
        email: this.profile.loginId,
        photoURL: URL+this.profile.profileUrl,
        providerId: 'jwt',//this.profile.sub.substr(0, this.profile.sub.indexOf('|')),
        uid: this.profile.id
      })

      this.emit(loginEvent, {
        loggedIn: true,
        profile: authResult.payload,
        state: authResult.appState || {}
      })
    }

    /**
     * 로그인처리후 토큰 갱신처리
     */
    renewTokens () {
      //TODO
      // reject can be used as parameter in promise for using reject
      return new Promise((resolve, reject) => {
        if (localStorage.getItem(localStorageKey) !== 'true') {
           return reject("Not logged in");
        }
        let localUserInfo = JSON.parse(localStorage.getItem("userInfo"))
        if (!localUserInfo) return;

        //axios call 로 변경
        jwt.refreshToken({loginId:localUserInfo.loginId})
        .then(response => {
          // If there's user data in response
          if (response.data.resultCode === "0001" && response.data.item.userInfo ) {
            var userInfo = response.data.item.userInfo
            console.log(response.data.item.userInfo)
            console.log(response.data.item.token)
            // Navigate User to homepage
            // Set accessToken
            // localStorage.setItem('accessToken', accessToken)

            userInfo.exp = new Date()
            var authResult = { payload : userInfo }
            this.localLogin(authResult)
            resolve(response)
        } else {
          reject({message: 'Wrong Login Id: failed to refresh token'})
          this.logOut ()
        }
      })
      .catch(error => { reject(error) 
        this.logOut ()
        })

        // webAuth.checkSession({}, (err, authResult) => {
        //   if (err) {
        //      //reject(err);
        //   } else {

        //   }
        // })
      })
    }

    cleanseLoginInfo() {
      
      localStorage.removeItem(localStorageKey)
      localStorage.removeItem(tokenExpiryKey)
      localStorage.removeItem('userInfo')
      localStorage.removeItem('accessToken')
      this.tokenExpiry = null
      this.profile = null
    }

    logOut () {
      console.log("logOut")
      this.cleanseLoginInfo()
      this.emit(loginEvent, { loggedIn: false })
      //store.commit('auth/SET_LOGOUT', {})
      //store.dispatch('auth/logout', {})
      router.push('/pages/login').catch(() => {})
    }

    /**
     * Date.now() --> 1590010036432
     * 60초 -> 60000
     */
    isAuthenticated () {
      var result = new Date(Date.now()) < new Date(localStorage.getItem(tokenExpiryKey)+3600*1000) &&
      localStorage.getItem(localStorageKey) === 'true'
      console.log(`isAuthenticated result: ${new Date(Date.now()) < new Date(localStorage.getItem(tokenExpiryKey)+3600*1000)} ${localStorage.getItem(localStorageKey)}`)
      return result
    }
}

export default new AuthService()
