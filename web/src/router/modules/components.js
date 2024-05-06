/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout/Layout';

const componentsRouter = {
  path: '/components',
  component: Layout,
  redirect: 'noRedirect',
  name: 'ComponentDemo',
  meta: {
    title: 'components',
    icon: 'component',
    roles: ['ROLE_ADMIN']
  },
  children: [
    {
      path: 'tinymce',
      component: () => import('@/views/base/components-demo/tinymce'),
      name: 'TinymceDemo',
      meta: {
        title: 'tinymce',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'markdown',
      component: () => import('@/views/base/components-demo/markdown'),
      name: 'MarkdownDemo',
      meta: {
        title: 'markdown',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'json-editor',
      component: () => import('@/views/base/components-demo/json-editor'),
      name: 'JsonEditorDemo',
      meta: {
        title: 'jsonEditor',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'split-pane',
      component: () => import('@/views/base/components-demo/split-pane'),
      name: 'SplitpaneDemo',
      meta: {
        title: 'splitPane',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'avatar-upload',
      component: () => import('@/views/base/components-demo/avatar-upload'),
      name: 'AvatarUploadDemo',
      meta: {
        title: 'avatarUpload',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'dropzone',
      component: () => import('@/views/base/components-demo/dropzone'),
      name: 'DropzoneDemo',
      meta: {
        title: 'dropzone',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'sticky',
      component: () => import('@/views/base/components-demo/sticky'),
      name: 'StickyDemo',
      meta: {
        title: 'sticky',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'count-to',
      component: () => import('@/views/base/components-demo/count-to'),
      name: 'CountToDemo',
      meta: {
        title: 'countTo',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'mixin',
      component: () => import('@/views/base/components-demo/mixin'),
      name: 'ComponentMixinDemo',
      meta: {
        title: 'componentMixin',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'back-to-top',
      component: () => import('@/views/base/components-demo/back-to-top'),
      name: 'BackToTopDemo',
      meta: {
        title: 'backToTop',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'drag-dialog',
      component: () => import('@/views/base/components-demo/drag-dialog'),
      name: 'DragDialogDemo',
      meta: {
        title: 'dragDialog',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'drag-select',
      component: () => import('@/views/base/components-demo/drag-select'),
      name: 'DragSelectDemo',
      meta: {
        title: 'dragSelect',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'dnd-list',
      component: () => import('@/views/base/components-demo/dnd-list'),
      name: 'DndListDemo',
      meta: {
        title: 'dndList',
        roles: ['ROLE_ADMIN']
      }
    },
    {
      path: 'drag-kanban',
      component: () => import('@/views/base/components-demo/drag-kanban'),
      name: 'DragKanbanDemo',
      meta: {
        title: 'dragKanban',
        roles: ['ROLE_ADMIN']
      }
    }
  ]
};

export default componentsRouter;
