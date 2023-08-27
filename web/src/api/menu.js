import request from '@/utils/request';

/**
 * 列出所有菜单。
 *
 * 不分页，慎用，数据量大可能导致性能下降。
 *
 * 这个接口不是 RESTFul 风格，属于自定义接口。
 */
export const listAllMenu = () => {
  return request({
    url: `/menu/list`,
    method: 'get'
  });
};

/**
 * 接口名称。
 *
 * 作为与 Spring Boot Data REST 对接的模板，在这里提供名称，以便遵循 RESTFul 统一风格。
 */
const restApi = 'menu';

/**
 * 查询所有数据。支持分页、排序、投影（即 VO 对象）。
 *
 * 接口示例：
 *
 * /api/menu?{page=0&size=20&sort=createTime,desc&projection=noRoles}
 *
 * 其中 noRoles 代表后端定义的投影名称，可以设定多个投影适应不同的应用场景。
 */
export function findAll(param) {
  return request({
    url: `/api/${restApi}`,
    method: 'get',
    param: param
  });
}
