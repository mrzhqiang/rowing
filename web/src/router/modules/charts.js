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
    roles: ['ADMIN']
  },
  children: [
    {
      path: 'keyboard',
      component: () => import('@/views/charts/keyboard'),
      name: 'KeyboardChart',
      meta: {
        title: 'keyboardChart',
        noCache: true,
        roles: ['ADMIN']
      }
    },
    {
      path: 'line',
      component: () => import('@/views/charts/line'),
      name: 'LineChart',
      meta: {
        title: 'lineChart',
        noCache: true,
        roles: ['ADMIN']
      }
    },
    {
      path: 'mix-chart',
      component: () => import('@/views/charts/mix-chart'),
      name: 'MixChart',
      meta: {
        title: 'mixChart',
        noCache: true,
        roles: ['ADMIN']
      }
    }
  ]
};

export default chartsRouter;
