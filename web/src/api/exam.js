import rest from '@/api/rest';
import request from '@/utils/request';

/**
 * 考试接口名称。
 * @type {string}
 */
const API_NAME = 'exam';

export function pageExam(params) {
  return rest.findAll(API_NAME, params);
}

export function createExam(data) {
  return rest.create(API_NAME, data);
}

export function findExam(id, projection = '') {
  return rest.findOne(API_NAME, id, {projection});
}

export function editExam(id, data) {
  return rest.edit(API_NAME, id, data);
}

export function updateExam(id, data) {
  return rest.update(API_NAME, id, data);
}

export function deleteExam(id) {
  return rest.remove(API_NAME, id);
}

export function searchExam(path, params) {
  return rest.search(API_NAME, path, params);
}

export function updateExamTakers(id, data) {
  return request.put(`/${API_NAME}/${id}/takers`, data);
}

export function updateExamMarkers(id, data) {
  return request.put(`/${API_NAME}/${id}/markers`, data);
}

export function myExam(params) {
  // 注意：Pagination 组件默认从 1 开始作为第一页，所以 page 需要减 1 操作
  const config = {params: {...params}};
  if (params.page && params.page > 0) {
    config.params.page = params.page - 1;
  }
  return request.get(`/${API_NAME}/my-exam`, config);
}
