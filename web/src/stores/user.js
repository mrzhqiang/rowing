import { defineStore } from "pinia";
import {getInfo, login, logout} from '@/api/user.js';
import {getToken, removeToken, setToken} from '@/utils/auth.js';
import router, {resetRouter} from '@/router/index.js';
import {encrypt} from '@/utils/security.js';

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  roles: []
};

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token;
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction;
  },
  SET_NAME: (state, name) => {
    state.name = name;
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar;
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles;
  }
};

const actions = {
  login({commit}, userInfo) {
    const {username} = userInfo;
    const password = encrypt(userInfo.password);
    return new Promise((resolve, reject) => {
      login({username: username.trim(), password: password}).then(response => {
        commit('SET_TOKEN', response.token);
        setToken(response.token);
        resolve();
      }).catch(error => {
        reject(error);
      });
    });
  },

  getInfo({commit, state}) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const {nickname, avatar, gender, introduction, roles} = response;

        // roles must be a non-empty array
        if (!roles || roles.length <= 0) {
          reject('getInfo: roles must be a non-null array!');
        }

        const avatarUrl = avatar || (gender && gender === 'FEMALE'
          ? require('@/assets/images/def_avatar_female.png') : require('@/assets/images/def_avatar_male.png'));
        commit('SET_ROLES', roles);
        commit('SET_NAME', nickname);
        commit('SET_AVATAR', avatarUrl);
        commit('SET_INTRODUCTION', introduction);
        resolve(response);
      }).catch(error => {
        reject(error);
      });
    });
  },

  // user logout
  logout({commit, state, dispatch}) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '');
        commit('SET_ROLES', []);
        removeToken();
        resetRouter();

        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, {root: true});

        resolve();
      }).catch(error => {
        reject(error);
      });
    });
  },

  // remove token
  resetToken({commit}) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '');
      commit('SET_ROLES', []);
      removeToken();
      resolve();
    });
  },

  // dynamically modify permissions
  async changeRoles({commit, dispatch}, role) {
    const token = role + '-token';

    commit('SET_TOKEN', token);
    setToken(token);

    const {roles} = await dispatch('getInfo');

    resetRouter();

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, {root: true});
    // dynamically add accessible routes
    router.addRoutes(accessRoutes);

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, {root: true});
  }
};

export default {
  namespaced: true,
  state,
  mutations,
  actions
};
