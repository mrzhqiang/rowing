import {createApp} from 'vue';
import App from './App.vue';

import {setupPinia} from 'src/stores';
import {createRouter} from './router';
import titleMixin from './util/title';
import ProgressBar from './components/ProgressBar.vue';

const app = createApp(App);

const router = store();

app.use(store);
app.use(router);

setupPinia(app);

import Cookies from 'js-cookie';

import 'normalize.css/normalize.css'; // a modern alternative to CSS resets

import Element from 'element-plus';
import './styles/element-variables.scss';

import '@/styles/index.scss'; // global css

import i18n from './lang'; // internationalization
import './icons'; // icon
import './routes'; // router config
import './utils/error-log'; // error-log
import './directive'; // directive

// global progress bar
const bar = createApp(ProgressBar).mount('#progress-container')
document.body.appendChild(bar.$el)
app.config.globalProperties.$bar = bar

// mixin for handling title
app.mixin(titleMixin)

// a global mixin that calls `asyncData` when a route component's params change
app.mixin({
  beforeRouteUpdate(to, from, next) {
    const {asyncData} = this.$options
    if (asyncData) {
      asyncData({
        store: this.$store,
        route: to
      })
        .then(next)
        .catch(next)
    } else {
      next()
    }
  }
});

function getMatchedComponents(route) {
  return route.matched.flatMap(record => Object.values(record.components))
}

router.beforeResolve((to, from, next) => {
  const matched = getMatchedComponents(to)
  const prevMatched = getMatchedComponents(from)
  let diffed = false
  const activated = matched.filter((c, i) => {
    return diffed || (diffed = prevMatched[i] !== c)
  })
  const asyncDataHooks = activated.map((c) => c.asyncData).filter((_) => _)
  if (!asyncDataHooks.length) {
    return next()
  }
  bar.start()
  Promise.all(asyncDataHooks.map((hook) => hook({store, route: to})))
    .then(() => {
      bar.finish()
      next()
    })
    .catch(next);
});

Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value),
});

Vue.config.productionTip = false;

// actually mount to DOM
app.mount('#app');
