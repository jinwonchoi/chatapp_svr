import store from '@/store/store.js'

class Utils {
  capitalize (str) {
      return str.slice(0, 1).toUpperCase() + str.slice(1, str.length)
    }
  toLocale(theTime) {
    return new Date(theTime).toLocaleString()
  }
  userImg(userInfo) {
    const URL = process.env.NODE_ENV === 'production'?process.env.VUE_APP_PROD_URL: process.env.VUE_APP_DEV_URL
    let resultUrl = ""
    if (userInfo) {
      resultUrl =URL+userInfo.profileUrl
    } else {
      resultUrl =URL+require("@/assets/images/portrait/small/default.jpg")
    }
    return resultUrl
  }

  // consult_type: {
  //   get () {
  //     let _consult_type = this.consult_type_choices.find( e => e.value === this.chatConsultDetails.consultType)
  //     return { label: _consult_type?_consult_type.label:"", value: this.chatConsultDetails.consultType }
  //   },
  //   set (obj) {
  //     this.chatConsultDetails.consultType = obj.value
  //   }
  // },
  // consult_status: {
  //   get () {
  //     let _consult_status = this.consult_status_choices.find( e => e.value === this.chatConsultDetails.consultStatus)
  //     return { label: _consult_status?_consult_status.label:"", value: this.chatConsultDetails.consultStatus }
  //   },
  //   set (obj) {
  //     this.chatConsultDetails.consultStatus = obj.value
  //   }
  // },
}
export default new Utils()


