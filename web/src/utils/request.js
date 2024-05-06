import store from '@/store';
import {getToken} from '@/utils/auth';
import axios from 'axios';
import {Message, MessageBox} from 'element-ui';

// 自定义参数序列化函数
const paramsSerializer = (params) => {
  // 将数组参数转换为逗号分隔的字符串
  return Object.entries(params)
    .map(([key, value]) => {
      if (Array.isArray(value)) {
        const encodedValues = value.map(v => encodeURIComponent(v));
        return `${encodeURIComponent(key)}=${encodedValues.join(',')}`;
      }
      return `${encodeURIComponent(key)}=${encodeURIComponent(value)}`;
    })
    .join('&');
};

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 10000, // request timeout
  paramsSerializer
});

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    config.headers['Accept-Language'] = store.getters.language || 'zh-CN,zh';

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['X-Token'] = getToken();
    }
    return config;
  },
  error => {
    // do something with request error
    console.log(error); // for debug
    return Promise.reject(error);
  }
);

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    return response.data;
  },
  error => {
    console.error(error); // for debug

    let message = error.message;
    // 如果存在响应，表示这是一个非 2xx 的错误
    if (error.response) {
      message = error.response.data.message || message;
      if (error.response.status === 403) {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload();
          });
        });
      }
    }
    Message({
      message,
      type: 'error',
      duration: 5 * 1000
    });
    return Promise.reject(error);
  }
);

export default service;
