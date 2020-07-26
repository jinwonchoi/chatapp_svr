// http 가짜호출: 실제 호출을 하지 않고 
//https://github.com/ctimmerm/axios-mock-adapter
//
import axios from '@/axios.js'
const MockAdapter = require('axios-mock-adapter')
const mock = new MockAdapter(axios) // This sets the mock adapter on the default instance
//const mock = axios // This sets the mock adapter on the default instance

export default mock
