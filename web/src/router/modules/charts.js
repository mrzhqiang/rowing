/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/layout/Layout';

const chartsRouter = {
  path: '/charts',
  component: Layout,
  redirect: 'noRedirect',
  name: 'Charts',
  meta: {
    title: 'charts',
    icon: 'chart',
    roles: ['ROLE_ADMIN']
  },
  children: [
    {
      path: 'keyboard',
      component: () => import('@/views/base/charts/keyboard'),
      name: 'KeyboardChart',
      meta: {
        title: 'keyboardChart',
        noCache: true,
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'line',
      component: () => import('@/views/base/charts/line'),
      name: 'LineChart',
      meta: {
        title: 'lineChart',
        noCache: true,
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'mix-chart',
      component: () => import('@/views/base/charts/mix-chart'),
      name: 'MixChart',
      meta: {
        title: 'mixChart',
        noCache: true,
        roles: ['ROLE_ADMIN']
      }
    }
  ]
};

export default chartsRouter;
