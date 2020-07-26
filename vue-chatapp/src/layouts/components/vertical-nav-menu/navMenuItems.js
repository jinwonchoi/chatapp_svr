/*=========================================================================================
  File Name: sidebarItems.js
  Description: Sidebar Items list. Add / Remove menu items from here.
  ----------------------------------------------------------------------------------------
  Item Name: Vuexy - Vuejs, HTML & Laravel Admin Dashboard Template
  Author: Pixinvent
  Author URL: http://www.themeforest.net/user/pixinvent
==========================================================================================*/


export default [
  // {
  //   url: "/",
  //   name: "Home",
  //   slug: "home",
  //   icon: "HomeIcon",
  // },
  {
    url: '/dashboard/ecommerce',
    name: 'Dashboard',
    //tag: '2',
    tagColor: 'warning',
    icon: 'HomeIcon',
    i18n: 'Dashboard',
  },
  {
    header: 'Apps',
    icon: 'PackageIcon',
    i18n: 'Apps',
    items: [
      // {
      //   url: '/apps/email',
      //   name: 'Email',
      //   slug: 'email',
      //   icon: 'MailIcon',
      //   i18n: 'Email'
      // },
      {
        url: '/apps/chat',
        name: 'Chat',
        slug: 'chat',
        icon: 'MessageSquareIcon',
        i18n: 'Chat'
      },
      // {
      //   url: '/apps/todo',
      //   name: 'Todo',
      //   slug: 'todo',
      //   icon: 'CheckSquareIcon',
      //   i18n: 'Todo'
      // },
      // {
      //   url: '/apps/calendar/vue-simple-calendar',
      //   name: 'Calendar',
      //   slug: 'calendar-simple-calendar',
      //   icon: 'CalendarIcon',
      //   tagColor: 'success',
      //   i18n: 'Calendar'
      // },
      // {
      //   url: null,
      //   name: 'eCommerce',
      //   icon: 'ShoppingCartIcon',
      //   i18n: 'eCommerce',
      //   submenu: [
      //     {
      //       url: '/apps/eCommerce/shop',
      //       name: 'Shop',
      //       slug: 'ecommerce-shop',
      //       i18n: 'Shop'
      //     },
      //     {
      //       url: '/apps/eCommerce/item/',
      //       name: 'Item Details',
      //       slug: 'ecommerce-item-detail-view',
      //       i18n: 'ItemDetails'
      //     },
      //     {
      //       url: '/apps/eCommerce/wish-list',
      //       name: 'Wish List',
      //       slug: 'ecommerce-wish-list',
      //       i18n: 'WishList'
      //     },
      //     {
      //       url: '/apps/eCommerce/checkout',
      //       name: 'Checkout',
      //       slug: 'ecommerce-checkout',
      //       i18n: 'Checkout'
      //     }
      //   ]
      // },
      {
        url: null,
        name: 'User',
        icon: 'UserIcon',
        i18n: 'User',
        submenu: [
          {
            url: '/apps/user/user-list',
            name: 'List',
            slug: 'app-user-list',
            i18n: 'List'
          },
          {
            url: '/apps/user/user-view/268',
            name: 'View',
            slug: 'app-user-view',
          i18n: 'View'
          },
          {
            url: '/apps/user/user-edit/268',
            name: 'Edit',
            slug: 'app-user-edit',
            i18n: 'Edit'
          }
        ]
      },
      {
        url: '/apps/noticeboard/notice-list',
        name: 'Notice Board',
        icon: 'InfoIcon',
        slug: 'notice',
        i18n: 'NoticeBoard',
      },
      {
        url: '/apps/consult/consult-history',
        name: 'Consult History',
        icon: 'ListIcon',
        slug: 'app-consult-history',
        i18n: 'ConsultHistory',
      },

    ]
  },
  // {
  //   header: 'Pages',
  //   icon: 'FileIcon',
  //   i18n: 'Pages',
  //   items: [
  //     {
  //       url: '/pages/profile',
  //       slug: 'page-profile',
  //       name: 'Profile',
  //       icon: 'UserIcon',
  //       i18n: 'Profile'
  //     },
  //     {
  //       url: '/pages/user-settings',
  //       slug: 'page-user-settings',
  //       name: 'User Settings',
  //       icon: 'SettingsIcon',
  //       i18n: 'UserSettings'
  //     },

  //   ]
  // },
]
