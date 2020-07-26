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
  pageSize: 7,
  currentPage: 0,
  queryParam: { 
    pageNo: 1,
    pageSize: 7,
    lastOffset:0, 
    sortField: 'id',
    sortDir: 'ASC',
    searchMap: {},
    searchByOrMap: {}
  },
  consults: [],
}
