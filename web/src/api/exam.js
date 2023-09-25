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

export function prepareExam(id) {
  return request.post(`/${API_NAME}/${id}/prepare`);
}

export function takerExam(params) {
  // 注意：Pagination 组件默认从 1 开始作为第一页，所以 page 需要减 1 操作
  const config = {params: {...params}};
  if (params.page && params.page > 0) {
    config.params.page = params.page - 1;
  }
  return request.get(`/${API_NAME}/for-taker`, config);
}

export function markerExam(params) {
  // 注意：Pagination 组件默认从 1 开始作为第一页，所以 page 需要减 1 操作
  const config = {params: {...params}};
  if (params.page && params.page > 0) {
    config.params.page = params.page - 1;
  }
  return request.get(`/${API_NAME}/for-marker`, config);
}

export function checkTakerPaperStatus(id) {
  return request.get(`/${API_NAME}/${id}/taker-paper-status`);
}

export function findTakerPaper(id) {
  return request.get(`/${API_NAME}/${id}/taker-paper`);
}

export function findMarkerPaper(id) {
  return request.get(`/${API_NAME}/${id}/marker-paper`);
}

export function saveExamPaperAnswer(id, data) {
  return request.patch(`/${API_NAME}/${id}/paper-answer`, data);
}

export function saveExamPaperMarker(id, data) {
  return request.patch(`/${API_NAME}/${id}/paper-marker`, data);
}

export function commitExamPaper(id, data) {
  return request.post(`/${API_NAME}/${id}/commit-paper`, data);
}

export function finishExamPaper(id, data) {
  return request.post(`/${API_NAME}/${id}/finish-paper`, data);
}
