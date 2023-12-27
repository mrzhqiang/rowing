import rest from '@/api/rest';

const API_NAME = 'exam-rule';

export function pageExamRule(params) {
  return rest.findAll(API_NAME, params);
}

export function createExamRule(data) {
  return rest.create(API_NAME, data);
}

export function findExamRule(id, projection = '') {
  return rest.findOne(API_NAME, id, {projection});
}

export function editExamRule(id, data) {
  return rest.edit(API_NAME, id, data);
}

export function updateExamRule(id, data) {
  return rest.update(API_NAME, id, data);
}

export function deleteExamRule(id) {
  return rest.remove(API_NAME, id);
}

export function searchExamRule(path, params) {
  return rest.search(API_NAME, path, params);
}
