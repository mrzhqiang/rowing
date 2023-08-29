import {asyncRoutes, constantRoutes} from '@/router';
import {findMenuRoutes} from '@/api/menu';
import Layout from '@/layout/Layout';

/**
 * Use meta.role to determine if the current user has permission
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role));
  } else {
    return true;
  }
}

/**
 * Filter asynchronous routing tables by recursion
 */
function filterAsyncRoutes(routes, roles) {
  const res = [];

  routes.forEach(route => {
    const tmp = {...route};
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles);
      }
      res.push(tmp);
    }
  });

  return res;
}

const parseComponent = (view) => { // 路由懒加载
  return (resolve) => require([`@/views/${view}`], resolve);
};

function parseRoutes(menus) {
  if (!menus || !menus.length) {
    return [];
  }

  const routes = [];
  menus.forEach(menu => {
    const route = {
      path: menu.path,
      hidden: menu.hidden
    };
    if (menu.name) {
      route.name = menu.name;
    }
    if (menu.parentId && menu.component) {
      route.component = parseComponent(menu.component);
    } else {
      route.component = Layout;
    }
    if (menu.redirect) {
      route.redirect = menu.redirect;
    }
    if (menu.alwaysShow) {
      route.alwaysShow = menu.alwaysShow;
    }
    if (menu.meta) {
      route.meta = parseMenuMeta(menu.meta);
    }
    if (menu.children && menu.children.length) {
      route.children = parseRoutes(menu.children);
    }
    routes.push(route);
  });
  return routes;
}

function parseMenuMeta(data) {
  const meta = {};
  if (data.title) {
    meta.title = data.title;
  }
  if (data.icon) {
    meta.icon = data.icon;
  }
  if (data.noCache) {
    meta.noCache = data.noCache;
  }
  if (data.affix) {
    meta.affix = data.affix;
  }
  if (data.breadcrumb) {
    meta.breadcrumb = data.breadcrumb;
  }
  if (data.activeMenu) {
    meta.activeMenu = data.activeMenu;
  }
  if (data.roles && data.roles.length) {
    meta.roles = data.roles;
  }
  return meta;
}

const state = {
  routes: [],
  addRoutes: []
};

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes;
    state.routes = constantRoutes.concat(routes);
  }
};

const actions = {
  generateRoutes({commit}, roles) {
    return new Promise(resolve => {
      findMenuRoutes().then(menus => {
        let accessedRoutes = parseRoutes(menus);
        if (roles.includes('ADMIN')) {
          accessedRoutes = (accessedRoutes || []).concat(asyncRoutes);
        } else {
          accessedRoutes = filterAsyncRoutes(accessedRoutes, roles);
        }
        // 404 page must be placed at the end !!!
        accessedRoutes.push({path: '*', redirect: '/404', hidden: true});
        commit('SET_ROUTES', accessedRoutes);
        resolve(accessedRoutes);
      });
    });
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions
};
