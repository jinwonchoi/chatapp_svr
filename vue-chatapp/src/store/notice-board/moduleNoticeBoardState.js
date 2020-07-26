/*=========================================================================================
  File Name: moduleCalendarState.js
  Description: Calendar Module State
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/

export default {
  totalCount: 0,
  pageSize: 0,
  currentPage: 0,
  queryParam: {},
  notices: [],
  noticeTypes: [
    {"type":"N","name":"Normal"},
    {"type":"S","name":"Schedule"},
    {"type":"U","name":"Urgent"},
    {"type":"W","name":"Biz"},
  ],
}
