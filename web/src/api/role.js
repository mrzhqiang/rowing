import rest from '@/api/rest';

/**
 * 角色接口名称。
 * @type {string}
 */
const ROLE_API_NAME = 'role';

export function pageRole(params) {
  return rest.findAll(ROLE_API_NAME, params);
}

export function createRole(data) {
  return rest.create(ROLE_API_NAME, data);
}

export function findRole(id, projection = '') {
  return rest.findOne(ROLE_API_NAME, id, {projection});
}

export function editRole(id, data) {
  return rest.edit(ROLE_API_NAME, id, data);
}

export function updateRole(id, data) {
  return rest.update(ROLE_API_NAME, id, data);
}

export function deleteRole(id) {
  return rest.remove(ROLE_API_NAME, id);
}

export function searchRole(path, params) {
  return rest.search(ROLE_API_NAME, path, params);
}

export function deleteRoleAccount(id) {
  return rest.remove(`${ROLE_API_NAME}/accounts/`, id);
}

export function deleteRoleMenu(id) {
  return rest.remove(`${ROLE_API_NAME}/menus/`, id);
}

export function deleteRoleMenuResource(id) {
  return rest.remove(`${ROLE_API_NAME}/menu-resources/`, id);
}
