import rest from '@/api/rest';

const API_NAME = 'exam-question';

export function pageExamQuestion(params) {
  return rest.findAll(API_NAME, params);
}

export function createExamQuestion(data) {
  return rest.create(API_NAME, data);
}

export function findExamQuestion(id, projection = '') {
  return rest.findOne(API_NAME, id, {projection});
}

export function editExamQuestion(id, data) {
  return rest.edit(API_NAME, id, data);
}

export function updateExamQuestion(id, data) {
  return rest.update(API_NAME, id, data);
}

export function deleteExamQuestion(id) {
  return rest.remove(API_NAME, id);
}

export function searchExamQuestion(path, params) {
  return rest.search(API_NAME, path, params);
}
