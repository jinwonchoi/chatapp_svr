/*=========================================================================================
  File Name: moduleCalendarGetters.js
  Description: Calendar Module Getters
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


export default {
  totalPages: (state) => state.totalPage | 0,
  getConsult: (state) => consultId => state.consults.find((consult) => consult.id === consultId),
  getConsultStatusFilter: (state) => { 
    let ret = state.queryParam.searchMap['consultStatus']
    return ret?ret:''
  },
}
