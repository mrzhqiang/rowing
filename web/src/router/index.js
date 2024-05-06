import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

/* Layout */
import Layout from '@/layout/Layout';

/* Router Modules */
import componentsRouter from './modules/components';
import chartsRouter from './modules/charts';
import tableRouter from './modules/table';

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['ADMIN','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/base/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/login/register.vue'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/base/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/base/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: {title: 'dashboard', icon: 'dashboard', affix: true}
      }
    ]
  },
  {
    path: '/guide',
    component: Layout,
    redirect: '/guide/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/base/guide/index'),
        name: 'Guide',
        meta: {title: 'guide', icon: 'guide', noCache: true}
      }
    ]
  },
  {
    path: '/profile',
    component: Layout,
    redirect: '/profile/index',
    hidden: true,
    children: [
      {
        path: 'index',
        component: () => import('@/views/profile/index'),
        name: 'Profile',
        meta: {title: 'profile', icon: 'user', noCache: true}
      }
    ]
  },
  {
    path: '/api-explorer',
    component: Layout,
    children: [
      {
        path: 'http://localhost:8888/api/explorer',
        meta: {
          title: '接口工具',
          icon: 'link',
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  }
];

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  {
    path: '/permission',
    component: Layout,
    redirect: '/permission/page',
    alwaysShow: true, // will always show the root menu
    name: 'Permission',
    meta: {
      title: 'permission',
      icon: 'lock',
      roles: ['ROLE_ADMIN'] // you can set roles in root nav
    },
    children: [
      {
        path: 'page',
        component: () => import('@/views/base/permission/page'),
        name: 'PagePermission',
        meta: {
          title: 'pagePermission',
          roles: ['ROLE_ADMIN'] // or you can only set roles in sub nav
        }
      },
      {
        path: 'directive',
        component: () => import('@/views/base/permission/directive'),
        name: 'DirectivePermission',
        meta: {
          title: 'directivePermission',
          roles: ['ROLE_ADMIN']
          // if do not set roles, means: this page does not require permission
        }
      },
      {
        path: 'role',
        component: () => import('@/views/base/permission/role'),
        name: 'RolePermission',
        meta: {
          title: 'rolePermission',
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  },

  {
    path: '/icon',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/base/icons/index'),
        name: 'Icons',
        meta: {
          title: 'icons',
          icon: 'icon',
          noCache: true,
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  },

  /** when your routing map is too long, you can split it into small modules **/
  componentsRouter,
  chartsRouter,
  tableRouter,

  {
    path: '/tab',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/base/tab/index'),
        name: 'Tab',
        meta: {
          title: 'tab',
          icon: 'tab',
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  },

  {
    path: '/error',
    component: Layout,
    redirect: 'noRedirect',
    name: 'ErrorPages',
    meta: {
      title: 'errorPages',
      icon: '404',
      roles: ['ROLE_ADMIN']
    },
    children: [
      {
        path: '401',
        component: () => import('@/views/base/error-page/401'),
        name: 'Page401',
        meta: {
          title: 'page401',
          noCache: true,
          roles: ['ROLE_ADMIN']
        }
      },
      {
        path: '404',
        component: () => import('@/views/base/error-page/404'),
        name: 'Page404',
        meta: {
          title: 'page404',
          noCache: true,
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  },

  {
    path: '/error-log',
    component: Layout,
    children: [
      {
        path: 'log',
        component: () => import('@/views/base/error-log/index'),
        name: 'ErrorLog',
        meta: {
          title: 'errorLog',
          icon: 'bug',
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  },

  {
    path: '/excel',
    component: Layout,
    redirect: '/excel/export-excel',
    name: 'Excel',
    meta: {
      title: 'excel',
      icon: 'excel',
      roles: ['ROLE_ADMIN']
    },
    children: [
      {
        path: 'export-excel',
        component: () => import('@/views/base/excel/export-excel'),
        name: 'ExportExcel',
        meta: {
          title: 'exportExcel',
          roles: ['ROLE_ADMIN']
        }
      },
      {
        path: 'export-selected-excel',
        component: () => import('@/views/base/excel/select-excel'),
        name: 'SelectExcel',
        meta: {
          title: 'selectExcel',
          roles: ['ROLE_ADMIN']
        }
      },
      {
        path: 'export-merge-header',
        component: () => import('@/views/base/excel/merge-header'),
        name: 'MergeHeader',
        meta: {
          title: 'mergeHeader',
          roles: ['ROLE_ADMIN']
        }
      },
      {
        path: 'upload-excel',
        component: () => import('@/views/base/excel/upload-excel'),
        name: 'UploadExcel',
        meta: {
          title: 'uploadExcel',
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  },

  {
    path: '/zip',
    component: Layout,
    redirect: '/zip/download',
    alwaysShow: true,
    name: 'Zip',
    meta: {
      title: 'zip',
      icon: 'zip',
      roles: ['ROLE_ADMIN']
    },
    children: [
      {
        path: 'download',
        component: () => import('@/views/base/zip/index'),
        name: 'ExportZip',
        meta: {
          title: 'exportZip',
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  },

  {
    path: '/pdf',
    component: Layout,
    redirect: '/pdf/index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/base/pdf/index'),
        name: 'PDF',
        meta: {
          title: 'pdf',
          icon: 'pdf',
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  },
  {
    path: '/pdf/download',
    component: () => import('@/views/base/pdf/download'),
    hidden: true
  },

  {
    path: '/theme',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/base/theme/index'),
        name: 'Theme',
        meta: {
          title: 'theme',
          icon: 'theme',
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  },

  {
    path: '/clipboard',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/base/clipboard/index'),
        name: 'ClipboardDemo',
        meta: {
          title: 'clipboardDemo',
          icon: 'clipboard',
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  },

  {
    path: '/i18n',
    component: Layout,
    children: [
      {
        path: 'index',
        component: () => import('@/views/base/i18n-demo/index'),
        name: 'I18n',
        meta: {
          title: 'i18n',
          icon: 'international',
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  },

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://github.com/PanJiaChen/vue-element-admin',
        meta: {
          title: 'externalLink',
          icon: 'link',
          roles: ['ROLE_ADMIN']
        }
      }
    ]
  }
];

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({y: 0}),
  routes: constantRoutes
});

const router = createRouter();

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher; // reset router
}

export default router;
