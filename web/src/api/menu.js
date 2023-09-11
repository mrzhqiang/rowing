import request from '@/utils/request';
import rest from '@/api/rest';

/**
 * 查询菜单路由。
 *
 * 排序，但不分页，一次性查询所有数据。
 */
export function findMenuRoutes() {
  return request.get('/menu/routes');
}

/**
 * 菜单接口名称。
 *
 * 作为与 Spring Boot Data REST 对接的模板，在这里提供名称，以便遵循 RESTFul 统一风格。
 */
const MENU_API_NAME = 'menu';
/**
 * 菜单资源接口名称。
 * @type {string}
 */
const MENU_RESOURCE_API_NAME = 'menu-resource';

export function pageMenu(params) {
  // 分页查询数据（菜单有层级关系，通常只需要查询顶级菜单列表，通过 search 接口可以实现）
  // 如果需要屏蔽某些字段，则在 params 中包含对应的 {projection: 'xxx'} 投影名称
  return rest.findAll(MENU_API_NAME, params);
}

export function createMenu(data) {
  // 创建新的数据
  return rest.create(MENU_API_NAME, data);
}

export function findMenu(id, projection = '') {
  // 查询数据详情
  return rest.findOne(MENU_API_NAME, id, {projection});
}

export function editMenu(id, data) {
  // 编辑部分字段
  return rest.edit(MENU_API_NAME, id, data);
}

export function updateMenu(id, data) {
  // 更新所有字段
  return rest.update(MENU_API_NAME, id, data);
}

export function deleteMenu(id) {
  // 删除已有数据
  return rest.remove(MENU_API_NAME, id);
}

export function searchMenu(path, params) {
  // 搜索数据
  // 如果需要屏蔽某些字段，则在 params 中包含对应的 {projection: 'xxx'} 投影名称
  return rest.search(MENU_API_NAME, path, params);
}

export function pageMenuResource(params) {
  return rest.findAll(MENU_RESOURCE_API_NAME, params);
}

export function createMenuResource(data) {
  return rest.create(MENU_RESOURCE_API_NAME, data);
}

export function findMenuResource(id, projection = '') {
  return rest.findOne(MENU_RESOURCE_API_NAME, id, {projection});
}

export function editMenuResource(id, data) {
  return rest.edit(MENU_RESOURCE_API_NAME, id, data);
}

export function updateMenuResource(id, data) {
  return rest.update(MENU_RESOURCE_API_NAME, id, data);
}

export function deleteMenuResource(id) {
  return rest.remove(MENU_RESOURCE_API_NAME, id);
}

export function searchMenuResource(path, params) {
  return rest.search(MENU_RESOURCE_API_NAME, path, params);
}
