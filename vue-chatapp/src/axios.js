// axios
import axios from 'axios'
import store from '@/store/store.js'
import authService from '@/auth/authService'


//const baseURL = ""
let isAlreadyFetchingAccessToken = false
let subscribers = []
const URL = process.env.NODE_ENV === 'production'?process.env.VUE_APP_PROD_URL: process.env.VUE_APP_DEV_URL
const vueURL = process.env.NODE_ENV === 'production'?process.env.VUE_APP_PROD_VUE_URL: process.env.VUE_APP_DEV_VUE_URL
function onAccessTokenFetched (access_token) {
  console.log("onAccessTokenFetched")
  subscribers = subscribers.filter(callback => callback(access_token))
}

function addSubscriber (callback) {
  console.log("addSubscriber")
  subscribers.push(callback)
}

console.log("=================================================")
console.log("Create Axios")
console.log("=================================================")
const axiosA = axios.create({
  baseURL: URL,
  withCredentials: true,
   headers: {
    "Access-Control-Allow-Origin": vueURL,
   }
})

if (authService.isAuthenticated ()) {
  axiosA.defaults.headers.common['Authorization'] = `Bearer ${localStorage.getItem('accessToken')}`
}

axiosA.interceptors.request.use(function (request) {
  console.log("Version: 2020-06-19 315")
  console.log("URL====>"+URL+request.url)
  console.log("vueURL====>"+vueURL)
  console.log("axiosA.interceptors.request")
  return request;
}, function (error) {
  // Any status codes that falls outside the range of 2xx cause this function to trigger
  // Do something with response error
  return Promise.reject(error);
});

axiosA.interceptors.response.use(function (response) {
  console.log("axiosA.interceptors.response")
  return response;
}, function (error) {
  // const { config, response: { status } } = error
  const { config, response } = error
  const originalRequest = config

  // if (status === 401) {
  if (response && response.status === 401) {
    console.log("401===>  isAlreadyFetchingAccessToken="+isAlreadyFetchingAccessToken)
    if (!isAlreadyFetchingAccessToken) {
      console.log("401====>2")
      isAlreadyFetchingAccessToken = true
      store.dispatch('auth/fetchAccessToken')
        .then((access_token) => {
          console.log("access_token="+onAccessTokenFetched)
          isAlreadyFetchingAccessToken = false
          onAccessTokenFetched(access_token)
        })
    } else {
      console.log(error)
      // if user is logged in via auth0
      authService.logOut()
    }

    const retryOriginalRequest = new Promise((resolve) => {
      addSubscriber(access_token => {
        originalRequest.headers.Authorization = `Bearer ${access_token}`
        resolve(axios(originalRequest))
      })
    }, (error)=> {
      console.log(error)
    })
    return retryOriginalRequest
  }
  return Promise.reject(error)
});

export default axiosA
