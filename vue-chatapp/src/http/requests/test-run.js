import realReq from './real-req.js';
//import  from './stomp.js';

export default {
    runLogin() {
        realReq.login('admin','passwd')        
        .then(response => {
            console.log(response)
            if (response.data.resultCode === "0001") {
                console.log(response.data.item.userInfo)
                console.log(response.data.item.token)
            }
            // If there's user data in response
            if (response.data.item.userInfo) {
              // Navigate User to homepage
              //router.push(router.currentRoute.query.to || '/')
  
              // Set accessToken
              localStorage.setItem('accessToken', response.data.item.token)
  
              // Update user details
              //commit('UPDATE_USER_INFO', response.data.userData, {root: true})
  
              // Set bearer token in axios
              //commit('SET_BEARER', response.data.accessToken)
  
              //resolve(response)
            } else {
              //reject({message: 'Wrong Email or Password'})
            }
  
          })
          .catch(error => { 
            console.log(error)  
            //reject(error)
         })
    },
    rungetUsers() {
      
    },
    runStomp() {
        console.log('runStomp')
        stomp.init

    }
}