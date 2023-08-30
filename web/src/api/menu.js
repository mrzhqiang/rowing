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
 * 接口名称。
 *
 * 作为与 Spring Boot Data REST 对接的模板，在这里提供名称，以便遵循 RESTFul 统一风格。
 */
const API_NAME = 'menu';

export function findAllMenu(params) {
  return rest.findAll(API_NAME, params);
}

export function createMenu(data) {
  return rest.create(API_NAME, data);
}

export function findOneMenu(id) {
  // 如果需要屏蔽某些字段，则传递对应的 {projection: 'xxx'} 投影名称
  return rest.findOne(API_NAME, id);
}

export function updateMenu(id, data) {
  return rest.update(API_NAME, id, data);
}

export function removeMenu(id) {
  return rest.remove(API_NAME, id);
}

export function searchMenu(path, params) {
  return rest.search(API_NAME, path, params);
}
