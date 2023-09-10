import request from '@/utils/request';
import rest from '@/api/rest';

/**
 * 注册账户。
 */
export function registerAccount(account, user) {
  return request.post('/account/register', {account, user});
}

/**
 * 账户接口名称。
 */
const ACCOUNT_API_NAME = 'account';

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
