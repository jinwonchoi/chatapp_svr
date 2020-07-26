// axios
import axios from 'axios'

const baseURL = ""

export default axios.create({
  baseURL: (process.env.NODE_ENV === 'production')?process.env.VUE_APP_PROD_URL: process.env.VUE_APP_DEV_URL//"http://localhost:8090/issuetool"
  // You can add your headers here
})

