import request from '@/utils/request';

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    params: data
  });
}

export function getInfo(token) {
  return request({
    url: '/api/user/info',
    method: 'get',
    params: {token}
  });
}

export function logout() {
  return request({
    url: '/api/user/logout',
    method: 'post'
  });
}
