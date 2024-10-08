import router from './router';
import store from './stores';
import {Message} from 'element-ui';
import NProgress from 'nprogress'; // progress bar
import 'nprogress/nprogress.css'; // progress bar style
import {getToken} from '@/utils/auth'; // get token from cookie
import getPageTitle from '@/utils/get-page-title';

NProgress.configure({showSpinner: false}); // NProgress Configuration

const whiteList = ['/login', '/register', '/auth-redirect']; // no redirect whitelist

router.beforeEach( (to, from, next) => {
  // start progress bar
  NProgress.start();

  // set page title
  document.title = getPageTitle(to.meta.title);

  // determine whether the user has logged in
  const hasToken = getToken();

  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({path: '/'});
      NProgress.done(); // hack: https://github.com/PanJiaChen/vue-element-admin/pull/2939
    } else {
      // determine whether the user has obtained his permission roles through getInfo
      const hasRoles = store.getters.roles && store.getters.roles.length > 0;
      if (hasRoles) {
        next();
      } else {
        try {
          store.dispatch('user/getInfo').then(userInfo => {
            // generate accessible routes map based on roles
            store.dispatch('permission/generateRoutes', userInfo.roles).then(accessRoutes => {
              // dynamically add accessible routes
              router.addRoutes(accessRoutes);
              // hack method to ensure that addRoutes is complete
              // set the replacement: true, so the navigation will not leave a history record
              next({...to, replace: true});
            });
          });
        } catch (error) {
          Message.error(error || 'Has Error');
          // remove token and go to login page to re-login
          store.dispatch('user/resetToken').then(() => {
            next(`/login?redirect=${to.path}`);
            NProgress.done();
          });
        }
      }
    }
  } else {
    /* has no token*/

    if (whiteList.includes(to.path)) {
      // in the free login whitelist, go directly
      next();
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`);
      NProgress.done();
    }
  }
});

router.afterEach(() => {
  // finish progress bar
  NProgress.done();
});
