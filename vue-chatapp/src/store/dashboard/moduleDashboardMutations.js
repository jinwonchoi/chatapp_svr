/*=========================================================================================
  File Name: moduleChatMutations.js
  Description: Chat Module Mutations
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/

export default {
  SET_CUSTOMER_INBOUND_COUNT (state, payload) {
    let _data = []
    let _total=0
    payload.item.forEach((e)=> { 
      _data.push(e.cnt)
      _total += e.cnt
    })
    // state.customerInBound = {
    //   series: [
    //     {
    //       name: 'inbound calls',
    //       data: _data
    //     }
    //   ],
    //   analyticsData: {
    //     callCount: _total
    //   }
    // }
    state.customerInBound.series[0].data = _data
    state.customerInBound.analyticsData.callCount= _total
  },
  SET_RESPONSE_COUNT (state, payload) {
    let _data = []
    let _total=0
    payload.item.forEach((e)=> { 
      _data.push(e.cnt)
      _total += e.cnt
    })
    state.responseCalls.series[0].data = _data
    state.responseCalls.analyticsData.callCount= _total
  },
  SET_RESOLVED_COUNT (state, payload) {
    let _data = []
    let _total=0
    payload.item.forEach((e)=> { 
      _data.push(e.cnt)
      _total += e.cnt
    })
    state.resolvedCalls.series[0].data = _data
    state.resolvedCalls.analyticsData.callCount= _total
  },
  SET_RESOLVED_GOAL (state, payload) {
    state.resolvedGoal= {
      analyticsData: {
        resolvedCount: payload.item.resolvedCount,
        totalCount: payload.item.totalCount
      },
      series: [payload.item.statsRate*100]
    }
  },
  // UPDATE_STATUS_CHAT (state, obj) {
  //   obj.rootState.AppActiveUser.status = obj.value
  // },


//   analyticsData: [
//     {
//       device: 'Dekstop',
//       icon: 'MonitorIcon',
//       color: 'primary',
//       sessionsPercentage: 58.6,
//       comparedResultPercentage: 2
//     },
//     {
//       device: 'Mobile',
//       icon: 'SmartphoneIcon',
//       color: 'warning',
//       sessionsPercentage: 34.9,
//       comparedResultPercentage: 8
//     },
//     {
//       device: 'Tablet',
//       icon: 'TabletIcon',
//       color: 'danger',
//       sessionsPercentage: 6.5,
//       comparedResultPercentage: -5
//     }
//   ],
//   series: [58.6, 34.9, 6.5]
// }
}

