import Vue from 'vue'
import { AclInstaller, AclCreate, AclRule } from 'vue-acl'
import router from '@/router'

// AclInstaller 플러그인을 설치
Vue.use(AclInstaller)

let initialRole = 'admin'

const userInfo = JSON.parse(localStorage.getItem('userInfo'))
if (userInfo && userInfo.userRole) initialRole = userInfo.userRole

export default new AclCreate({
  initial  : initialRole,
  notfound : '/pages/not-authorized',
  router,
  acceptLocalRules : true,
  globalRules: {
    admin  : new AclRule('admin').generate(),
    manager  : new AclRule('manager').or('admin').generate(),
    agent : new AclRule('agent').or('manager').or('admin').generate()
  }
})

