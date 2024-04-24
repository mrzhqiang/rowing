import request from '@/utils/request';
import rest from '@/api/rest';

/**
 * 注册账户。
 */
export function register(form) {
  let url = '/register';
  if (form.kaptcha) {
    url = url + '?kaptcha=' + form.kaptcha;
  }
  return request.post(url, form);
}

/**
 * 账户接口名称。
 */
const ACCOUNT_API_NAME = 'account';
const USER_API_NAME = 'user';

export function pageAccount(params) {
  return rest.findAll(ACCOUNT_API_NAME, params);
}

export function createAccount(data) {
  return rest.create(ACCOUNT_API_NAME, data);
}

export function findAccount(id, projection = '') {
  return rest.findOne(ACCOUNT_API_NAME, id, {projection});
}

export function editAccount(id, data) {
  return rest.edit(ACCOUNT_API_NAME, id, data);
}

export function updateAccount(id, data) {
  return rest.update(ACCOUNT_API_NAME, id, data);
}

export function deleteAccount(id) {
  return rest.remove(ACCOUNT_API_NAME, id);
}

export function searchAccount(path, params) {
  return rest.search(ACCOUNT_API_NAME, path, params);
}

export function pageUser(params) {
  return rest.findAll(USER_API_NAME, params);
}

export function createUser(data) {
  return rest.create(USER_API_NAME, data);
}

export function findUser(id, projection = '') {
  return rest.findOne(USER_API_NAME, id, {projection});
}

export function editUser(id, data) {
  return rest.edit(USER_API_NAME, id, data);
}

export function updateUser(id, data) {
  return rest.update(USER_API_NAME, id, data);
}

export function deleteUser(id) {
  return rest.remove(USER_API_NAME, id);
}

export function searchUser(path, params) {
  return rest.search(USER_API_NAME, path, params);
}
