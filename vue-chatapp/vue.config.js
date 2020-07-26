/*=========================================================================================
  File Name: vue.config.js
  Description: configuration file of vue
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


module.exports = {
  publicPath: '/issuetool/chatapp',
  transpileDependencies: [
    'vue-echarts',
    'resize-detector'
  ],
  configureWebpack: {
    optimization: {
      splitChunks: {
        chunks: 'all'
      }
    }
  },
  devServer: {
    port: 8082,
  }
}
// proxy: {
//   '/issuetool': {
//   target: 'http://192.168.0.104:8090',
//     ws: true,
//     changeOrigin: true
//   }
// }

//      target: 'http://192.168.0.104:8090',
//      target: 'http://13.125.27.90:8090',
// 
// spring boot - vue js간 cors 문제 해결
//https://awesomeopensource.com/project/jonashackt/spring-boot-vuejs#http-calls-from-vuejs-to-spring-boot-rest-backend