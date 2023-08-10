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
    roles: ['ADMIN']
  },
  children: [
    {
      path: 'dynamic-table',
      component: () => import('@/views/table/dynamic-table/index'),
      name: 'DynamicTable',
      meta: {
        title: 'dynamicTable',
        roles: ['ADMIN']
      }
    },
    {
      path: 'drag-table',
      component: () => import('@/views/table/drag-table'),
      name: 'DragTable',
      meta: {
        title: 'dragTable',
        roles: ['ADMIN']
      }
    },
    {
      path: 'inline-edit-table',
      component: () => import('@/views/table/inline-edit-table'),
      name: 'InlineEditTable',
      meta: {
        title: 'inlineEditTable',
        roles: ['ADMIN']
      }
    },
    {
      path: 'complex-table',
      component: () => import('@/views/table/complex-table'),
      name: 'ComplexTable',
      meta: {
        title: 'complexTable',
        roles: ['ADMIN']
      }
    }
  ]
};

export default tableRouter;
