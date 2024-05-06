/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout/Layout';

const tableRouter = {
  path: '/table',
  component: Layout,
  redirect: '/table/complex-table',
  name: 'Table',
  meta: {
    title: 'Table',
    icon: 'table',
    roles: ['ROLE_ADMIN']
  },
  children: [
    {
      path: 'dynamic-table',
      component: () => import('@/views/base/table/DynamicTable'),
      name: 'DynamicTable',
      meta: {
        title: 'dynamicTable',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'drag-table',
      component: () => import('@/views/base/table/drag-table'),
      name: 'DragTable',
      meta: {
        title: 'dragTable',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'inline-edit-table',
      component: () => import('@/views/base/table/inline-edit-table'),
      name: 'InlineEditTable',
      meta: {
        title: 'inlineEditTable',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'complex-table',
      component: () => import('@/views/base/table/complex-table'),
      name: 'ComplexTable',
      meta: {
        title: 'complexTable',
        roles: ['ROLE_ADMIN']
      }
    }
  ]
};

export default tableRouter;
