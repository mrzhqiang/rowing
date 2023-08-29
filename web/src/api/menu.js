import request from '@/utils/request';
import {findAll, create, findOne, update, remove, search} from '@/api/rest';

/**
 * 查询菜单路由。
 *
 * 排序，但不分页，一次性查询所有数据。
 */
export const findMenuRoutes = () => {
  return request.get('/menu/routes');
};

/**
 * 接口名称。
 *
 * 作为与 Spring Boot Data REST 对接的模板，在这里提供名称，以便遵循 RESTFul 统一风格。
 */
const API_NAME = 'menu';

export const findAllMenu = (params) => {
  return findAll(API_NAME, params);
};

export const createMenu = (data) => {
  return create(API_NAME, data);
};

export const findOneMenu = (id) => {
  // 如果需要屏蔽某些字段，则传递对应的 {projection: 'xxx'} 投影名称
  return findOne(API_NAME, id);
};

export const updateMenu = (id, data) => {
  return update(API_NAME, id, data);
};

export const removeMenu = (id) => {
  return remove(API_NAME, id);
};

export const searchMenu = (path, params) => {
  return search(API_NAME, path, params);
};
