import axios from '../../axios-new'

class RealReq {
    isAlreadyFetchingAccessToken = false
    init () {
      axios.interceptors.response.use(function (response) {
          console.log("axios.interceptors.response")
        return response
      }, function (error) {
        // const { config, response: { status } } = error
        const { config, response } = error
        const originalRequest = config
  
        // if (status === 401) {
        if (response && response.status === 401) {
          if (!isAlreadyFetchingAccessToken) {
            isAlreadyFetchingAccessToken = true
            store.dispatch('auth/fetchAccessToken')
              .then((access_token) => {
                isAlreadyFetchingAccessToken = false
                onAccessTokenFetched(access_token)
              })
          }
  
          const retryOriginalRequest = new Promise((resolve) => {
            addSubscriber(access_token => {
              originalRequest.headers.Authorization = `Bearer ${access_token}`
              resolve(axios(originalRequest))
            })
          })
          return retryOriginalRequest
        }
        return Promise.reject(error)
      })
    }
    login (loginId, pwd) {
        console.log("real login")
      return axios.post((process.env.NODE_ENV === 'production')?process.env.VUE_APP_PROD_URL: process.env.VUE_APP_DEV_URL+'/auth/login', {
      //  return axios.post('/auth/login', {
            loginId:loginId,
        passwd: pwd
      })
    }
    // registerUser (name, email, pwd) {
    //   return axios.post('/api/auth/register', {
    //     displayName: name,
    //     email,
    //     password: pwd
    //   })
    // },
    // refreshToken () {
    //   return axios.post('/api/auth/refresh-token', {accessToken: localStorage.getItem('accessToKen')})
    // }
  
    
  }

  export default new RealReq()