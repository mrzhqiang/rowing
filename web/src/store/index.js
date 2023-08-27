import Vue from 'vue';
import Vuex from 'vuex';
import getters from './getters';
/* 改为手动导入模块 */
import app from '@/store/modules/app';
import errorLog from '@/store/modules/errorLog';
import permission from '@/store/modules/permission';
import settings from '@/store/modules/settings';
import tagsView from '@/store/modules/tagsView';
import user from '@/store/modules/user';

Vue.use(Vuex);

/* 以下方式无法进行代码跳转，影响阅读体验以及问题查找
// https://webpack.js.org/guides/dependency-management/#requirecontext
const modulesFiles = require.context('./modules', true, /\.js$/);

// you do not need `import app from './modules/app'`
// it will auto require all vuex module from modules file
const modules = modulesFiles.keys().reduce((modules, modulePath) => {
  // set './app.js' => 'app'
  const moduleName = modulePath.replace(/^\.\/(.*)\.\w+$/, '$1');
  const value = modulesFiles(modulePath);
  modules[moduleName] = value.default;
  return modules;
}, {});
*/

const store = new Vuex.Store({
  modules: {
    app,
    errorLog,
    permission,
    settings,
    tagsView,
    user,
  },
  getters
});

export default store;
