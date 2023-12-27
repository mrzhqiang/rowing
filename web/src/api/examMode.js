import rest from '@/api/rest';

const API_NAME = 'exam-mode';

export function pageExamMode(params) {
  return rest.findAll(API_NAME, params);
}

export function createExamMode(data) {
  return rest.create(API_NAME, data);
}

export function findExamMode(id, projection = '') {
  return rest.findOne(API_NAME, id, {projection});
}

export function editExamMode(id, data) {
  return rest.edit(API_NAME, id, data);
}

export function updateExamMode(id, data) {
  return rest.update(API_NAME, id, data);
}

export function deleteExamMode(id) {
  return rest.remove(API_NAME, id);
}

export function searchExamMode(path, params) {
  return rest.search(API_NAME, path, params);
}
