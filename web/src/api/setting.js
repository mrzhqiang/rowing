import rest from '@/api/rest';

/**
 * 设置接口名称。
 */
const SETTING_API_NAME = 'setting';

export function pageSetting(params) {
  return rest.findAll(SETTING_API_NAME, params);
}

export function createSetting(data) {
  return rest.create(SETTING_API_NAME, data);
}

export function findSetting(id, projection = '') {
  return rest.findOne(SETTING_API_NAME, id, {projection});
}

export function editSetting(id, data) {
  return rest.edit(SETTING_API_NAME, id, data);
}

export function updateSetting(id, data) {
  return rest.update(SETTING_API_NAME, id, data);
}

export function deleteSetting(id) {
  return rest.remove(SETTING_API_NAME, id);
}

export function searchSetting(path, params) {
  return rest.search(SETTING_API_NAME, path, params);
}
