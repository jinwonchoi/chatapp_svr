/*=========================================================================================
  File Name: moduleChatState.js
  Description: Chat Module State
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


export default {
  // 사업부 사용자 인입 건수    
  customerInBound :{
    series: [
      {
        name: 'inbound calls',
        data: []
      }
    ],
    analyticsData: {
      callCount: 0
    }
  },
  // 응대 건수  
  responseCalls: {
    series: [
      {
        name: 'response calls',
        data: []
      }
    ],
    analyticsData: {
      callCount: 0
    }
  },
  // 해결 건수    
  resolvedCalls: {
    series: [
      {
        name: 'resolved calls',
        data: []
      }
    ],
    analyticsData: {
      callCount: 0
    }
  },
  // 해결건 집계 (Goal Overview)
  resolvedGoal: {
    analyticsData: {
      resolvedCount: 0,
      totalCount: 0
    },
    series: [0]
  },
  // 좌측 상담현황 
  chatSessionStatus: [],
  // 우측 공지사항
  notices: [],
  // 좌측 상담요청종류
  consultTypeRatePerInBoundNo: {},
  // 우측 상담결과종류
  consultStatusRatePerInBoundNo: {}, 
  
}
