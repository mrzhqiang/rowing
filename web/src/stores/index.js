import {createPinia} from 'pinia';
import piniaPersistedStatePlugin from 'pinia-plugin-persistedstate';

const pinia = createPinia();
pinia.use(piniaPersistedStatePlugin);

export const setupPinia = (app) => {
  app.use(pinia);
};

export default pinia;
