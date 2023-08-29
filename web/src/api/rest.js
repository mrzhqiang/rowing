import request from '@/utils/request';

/* 按照 Spring Boot Data REST 框架实现的接口工具。*/

/**
 * 查询对应资源和参数相关的所有数据。
 *
 * 接口示例：
 *
 * GET http(s)://your-domain/api/${resource}?page=0&size=20&sort=($property-name,)+[asc,desc]&sort=...
 *
 * 接口优先级：
 *
 * findAll(Pageable) -- Page<T> -- page=&size=&sort=
 *
 * findAll(Sort) -- Iterable<T> -- sort=
 *
 * findAll() -- List<T> -- 注意：这个只是作为备用方法，实际上后端会默认分页，即设置 page=0&size=20 参数
 *
 * 另外：如果后端创建了对应实体的投影接口，则可以额外使用 projection=($projection-name) 参数，这表示对返回的数据进行投影，
 * 即适当选择返回的字段，而不是一次性返回所有字段。
 * 对于 findAll 接口来说，可以设置一个默认投影，表示预览数据列表，点击每条数据的 _links.self.href 可以查看详情。
 *
 * @param resource rest 资源名称。
 * @param params 参数对象。这个对象将自动展开为 URL 中的请求参数。
 * @returns {AxiosPromise<any>} 查询请求。
 */
export function findAll(resource, params = {page: 0, size: 20, sort: 'created,desc'}) {
  return request.get(`/api/${resource}`, {params: params});
}

/**
 * 创建对应资源的数据。
 *
 * @param resource 资源名称。
 * @param data 需要创建的数据对象。
 * @returns {AxiosPromise<any>} 创建请求。
 * 响应 201 状态码表示创建成功，通常不会返回任何数据，但通过后端可以配置为返回新创建的数据。
 */
export function create(resource, data) {
  return request.post(`/api/${resource}`, {data: data});
}

/**
 * 查询对应资源的数据详情。
 *
 * @param resource 资源名称。
 * @param id 资源 ID。
 * @param params 查询参数对象。默认情况下为空字符串，也可以传递投影名称表示仅返回投影相关的数据。
 * @returns {AxiosPromise<any>} 查询请求。
 * 如果后端在此接口上配置 @RestResource(exported = false) 则响应 405 状态码表示方法不允许。
 */
export function findOne(resource, id, params = '') {
  return request.get(`/api/${resource}/${id}`, {params: params});
}

/**
 * 更新对应资源的数据。
 *
 * @param resource 资源名称。
 * @param id 资源 ID。
 * @param data 需要更新的数据对象。
 * @returns {AxiosPromise<any>} 更新请求。
 * 响应 204 状态码表示更新成功，但不返回任何数据；如果通过后端设置返回数据，则响应 200 状态码。
 * 如果后端在此接口上配置 @RestResource(exported = false) 则响应 405 状态码表示方法不允许。
 */
export function update(resource, id, data) {
  return request.put(`/api/${resource}/${id}`, data);
}

/**
 * 删除对应资源的数据。
 *
 * @param resource 资源名称。
 * @param id 资源 ID。
 * @returns {AxiosPromise<any>} 删除请求。
 * 如果后端在此接口上配置 @RestResource(exported = false) 则响应 405 状态码表示方法不允许。
 */
export function remove(resource, id) {
  return request.delete(`/api/${resource}/${id}`);
}

/**
 * 搜索对应资源和参数相关的所有数据。
 *
 * 接口示例：
 *
 * GET http(s)://your-domain/api/${resource}?page=0&size=20&sort=($property-name,)+[asc,desc]&sort=...
 *
 * 注意：只有自定义的查询接口才能使用此函数，并且 path 必须匹配 @RestResource(path = 'xxx') 中的路径。
 *
 * @param resource 资源名称。
 * @param path 搜索路径。
 * @param params 搜索参数对象。
 * @return {AxiosPromise<any>} 搜索请求。
 */
export function search(resource, path, params = {page: 0, size: 20, sort: 'created,desc'}) {
  return request.get(`/api/${resource}/search/${path}`, {params: params});
}

/**
 * 访问资源链接。
 *
 * 资源链接：
 *
 * self 表示自身；
 * next 表示分页中的下一页；
 * prev 表示分页中的上一页；
 * first 表示分页中的第一页；
 * last 表示分页中的最后一页；
 * profile 表示当前资源的属性；
 * search 表示当前资源的自定义查询方法；
 *
 * @param href 链接。
 * @return {AxiosPromise<any>} 链接请求。
 */
export function link(href) {
  return request.get(href);
}
