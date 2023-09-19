import rest from '@/api/rest';

/**
 * 考试接口名称。
 * @type {string}
 */
const EXAM_API_NAME = 'exam';

export function pageExam(params) {
  return rest.findAll(EXAM_API_NAME, params);
}

export function createExam(data) {
  return rest.create(EXAM_API_NAME, data);
}

export function findExam(id, projection = '') {
  return rest.findOne(EXAM_API_NAME, id, {projection});
}

export function editExam(id, data) {
  return rest.edit(EXAM_API_NAME, id, data);
}

export function updateExam(id, data) {
  return rest.update(EXAM_API_NAME, id, data);
}

export function deleteExam(id) {
  return rest.remove(EXAM_API_NAME, id);
}

export function searchExam(path, params) {
  return rest.search(EXAM_API_NAME, path, params);
}
