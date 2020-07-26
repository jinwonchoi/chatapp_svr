/*=========================================================================================
  File Name: router.js
  Description: Routes for vue-router. Lazy loading is enabled.
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


import Vue from 'vue'
import Router from 'vue-router'
import auth from '@/auth/authService'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  scrollBehavior () {
      return { x: 0, y: 0 }
  },
  routes: [
    {
    // =============================================================================
    // MAIN LAYOUT ROUTES
    // =============================================================================
      path: '',
      component: () => import('./layouts/main/Main.vue'),
      children: [
        // =============================================================================
        // Theme Routes
        // =============================================================================
        {
          path: '/',
          redirect: '/dashboard/ecommerce'
          // name: 'home',
          // component: () => import('./views/Home.vue'),
          // meta: {
          //   rule: 'agent',
          //   no_scroll: true
          // }

        },
        {
          path: '/page2',
          name: 'page-2',
          component: () => import('./views/Page2.vue'),
          meta: {
            rule: 'agent',
            no_scroll: true
          }
        },
        {
          path: '/dashboard/ecommerce',
          name: 'dashboard-ecommerce',
          component: () => import('./views/DashboardECommerce.vue'),
          meta: {
            rule: 'agent'
          }
        },
        // =============================================================================
        // Application Routes
        // =============================================================================
        {
          path: '/apps/todo',
          redirect: '/apps/todo/all',
          name: 'todo'
        },
        {
          path: '/apps/todo/:filter',
          component: () => import('./views/apps/todo/Todo.vue'),
          meta: {
            rule: 'agent',
            parent: 'todo',
            no_scroll: true
          }
        },
        {
          path: '/apps/chat',
          name: 'chat',
          component: () => import('./views/apps/chat/Chat.vue'),
          meta: {
            rule: 'agent',
            no_scroll: true
          }
        },
      
        {
          path: '/apps/user/user-list',
          name: 'app-user-list',
          component: () => import('@/views/apps/user/user-list/UserList.vue'),
          meta: {
            breadcrumb: [
              { title: 'Home', url: '/' },
              { title: 'User' },
              { title: 'List', active: true }
            ],
            pageTitle: 'User List',
            rule: 'admin'
          }
        },
        {
          path: '/apps/user/user-view/:userId',
          name: 'app-user-view',
          component: () => import('@/views/apps/user/UserView.vue'),
          meta: {
            breadcrumb: [
              { title: 'Home', url: '/' },
              { title: 'User' },
              { title: 'View', active: true }
            ],
            pageTitle: 'User View',
            rule: 'admin'
          }
        },
        {
          path: '/apps/user/user-edit/:userId',
          name: 'app-user-edit',
          component: () => import('@/views/apps/user/user-edit/UserEdit.vue'),
          meta: {
            breadcrumb: [
              { title: 'Home', url: '/' },
              { title: 'User' },
              { title: 'Edit', active: true }
            ],
            pageTitle: 'User Edit',
            rule: 'admin'
          }
        },              
        {
          path: '/apps/noticeboard/notice-list',
          name: 'app-notice-list',
          component: () => import('@/views/apps/noticeboard/notice-list/NoticeList.vue'),
          meta: {
            breadcrumb: [
              { title: 'Home', url: '/' },
              { title: 'Notice Updates' },
              { title: 'List', active: true }
            ],
            pageTitle: 'Notice Updates',
            rule: 'admin'
          }
        },
        {
          path: '/apps/noticeboard/notice-view/:id',
          name: 'app-notice-view',
          component: () => import('@/views/apps/noticeboard/NoticeView.vue'),
          meta: {
            breadcrumb: [
              { title: 'Home', url: '/' },
              { title: 'Notice Updates' },
              { title: 'View', active: true }
            ],
            pageTitle: 'Notice View',
            rule: 'admin'
          }
        },
        {
          path: '/apps/noticeboard/notice-edit/:id',
          name: 'app-notice-edit',
          component: () => import('@/views/apps/noticeboard/NoticeEdit.vue'),
          meta: {
            breadcrumb: [
              { title: 'Home', url: '/' },
              { title: 'Notice Updates' },
              { title: 'Edit', active: true }
            ],
            pageTitle: 'Notice Edit',
            rule: 'admin'
          }
        },       
        /////////////////////////////////////////////////////////
        {
          path: '/apps/consult/consult-history',
          name: 'app-consult-history',
          redirect: '/apps/consult/consult-history/all'
        },
        {
          path: '/apps/consult/consult-history/:filter',
          component: () => import('@/views/apps/consult/ConsultHistory.vue'),
          meta: {
            // breadcrumb: [
            //   { title: 'Home', url: '/' },
            //   { title: 'Chat Consult' },
            //   { title: 'Consult History', active: true }
            // ],
            //pageTitle: 'Consult History',
            parent: 'app-consult-history',
            rule: 'admin'
          }
        },
        // {
        //   path: '/apps/consult/consult-view/:chatId',
        //   name: 'app-consult-view',
        //   component: () => import('@/views/apps/consult/ConsultView.vue'),
        //   meta: {
        //     breadcrumb: [
        //       { title: 'Home', url: '/' },
        //       { title: 'Chat Consult' },
        //       { title: 'Consult View', active: true }
        //     ],
        //     pageTitle: 'Consult View',
        //     rule: 'admin'
        //   }
        // },
        // {
        //   path: '/apps/consult/consult-edit/:chatId',
        //   name: 'app-consult-edit',
        //   component: () => import('@/views/apps/consult/ConsultEdit.vue'),
        //   meta: {
        //     breadcrumb: [
        //       { title: 'Home', url: '/' },
        //       { title: 'Chat Consult' },
        //       { title: 'Consult Edit', active: true }
        //     ],
        //     pageTitle: 'Consult Edit',
        //     rule: 'admin'
        //   }
        // },  
        // =============================================================================
        // Pages Routes
        // =============================================================================
        {
          path: '/pages/profile',
          name: 'page-profile',
          component: () => import('@/views/pages/Profile.vue'),
          meta: {
            breadcrumb: [
              { title: 'Home', url: '/' },
              { title: 'Pages' },
              { title: 'Profile', active: true }
            ],
            pageTitle: 'Profile',
            rule: 'agent'
          }
        },
        {
          path: '/pages/user-settings',
          name: 'page-user-settings',
          component: () => import('@/views/pages/user-settings/UserSettings.vue'),
          meta: {
            breadcrumb: [
              { title: 'Home', url: '/' },
              { title: 'Pages' },
              { title: 'User Settings', active: true }
            ],
            pageTitle: 'Settings',
            rule: 'agent'
          }
        },
        {
          path: '/pages/search',
          name: 'page-search',
          component: () => import('@/views/pages/Search.vue'),
          meta: {
            breadcrumb: [
              { title: 'Home', url: '/' },
              { title: 'Pages' },
              { title: 'Search', active: true }
            ],
            pageTitle: 'Search',
            rule: 'agent'
          }
        },
      ]
    },
    // =============================================================================
    // FULL PAGE LAYOUTS
    // =============================================================================
      {
        path: '',
        component: () => import('@/layouts/full-page/FullPage.vue'),
        children: [
          // =============================================================================
          // PAGES
          // =============================================================================
          {
            path: '/callback',
            name: 'auth-callback',
            component: () => import('@/views/Callback.vue'),
            meta: {
              rule: 'agent'
            }
          },
          {
            path: '/pages/login',
            name: 'page-login',
            component: () => import('@/views/pages/Login.vue'),
            meta: {
              rule: 'agent'
            }
          },
          {
            path: '/pages/forgot-password',
            name: 'page-forgot-password',
            component: () => import('@/views/pages/ForgotPassword.vue'),
            meta: {
              rule: 'agent'
            }
          },
          {
            path: '/pages/reset-password',
            name: 'page-reset-password',
            component: () => import('@/views/pages/ResetPassword.vue'),
            meta: {
              rule: 'agent'
            }
          },
          {
            path: '/pages/lock-screen',
            name: 'page-lock-screen',
            component: () => import('@/views/pages/LockScreen.vue'),
            meta: {
              rule: 'agent'
            }
          },
          {
            path: '/pages/error-404',
            name: 'page-error-404',
            component: () => import('@/views/pages/Error404.vue')
          },
          {
            path: '/pages/not-authorized',
            name: 'page-not-authorized',
            component: () => import('@/views/pages/NotAuthorized.vue'),
            meta: {
              rule: 'agent'
            }
          },
          {
            path: '/pages/maintenance',
            name: 'page-maintenance',
            component: () => import('@/views/pages/Maintenance.vue'),
            meta: {
              rule: 'agent'
            }
          }
        ]
      },
      // Redirect to 404 page, if no match found
      {
          path: '*',
          redirect: '/pages/error-404'
      }
    ]
})

router.afterEach(() => {
  // Remove initial loading
  const appLoading = document.getElementById('loading-bg')
  if (appLoading) {
    appLoading.style.display = "none";
  }
})


router.beforeEach((to, from, next) => {
  console.log(`beforeEach `)
  console.log(`beforeEach to:${to.path}`)
  console.log(`beforeEach from:${from.path}`)
  
  if (
      to.path === "/pages/login" ||
      to.path === "/pages/forgot-password" ||
      to.path === "/pages/error-404" ||
      to.path === "/pages/error-500" ||
      to.path === "/pages/register" ||
      to.path === "/callback" ||
      to.path === "/pages/comingsoon" ||
      auth.isAuthenticated()
  ) {
      return next();
  }

  // If auth required, check login. If login fails redirect to login page
  if (to.meta.rule) {
    if (!auth.isAuthenticated()) {
      console.log("router.push({ path: '/pages/login', query: { to: to.path } })")
      router.push({ path: '/pages/login', query: { to: to.path, from: from } })
    }
  }

  return next()
  // Specify the current path as the customState parameter, meaning it
  // will be returned to the application after auth
  // auth.login({ target: to.path });
})

export default router
