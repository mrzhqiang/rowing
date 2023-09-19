import rest from '@/api/rest';

const API_NAME = 'exam-question-bank';

export function pageExamQuestionBank(params) {
  return rest.findAll(API_NAME, params);
}

export function createExamQuestionBank(data) {
  return rest.create(API_NAME, data);
}

export function findExamQuestionBank(id, projection = '') {
  return rest.findOne(API_NAME, id, {projection});
}

export function editExamQuestionBank(id, data) {
  return rest.edit(API_NAME, id, data);
}

export function updateExamQuestionBank(id, data) {
  return rest.update(API_NAME, id, data);
}

export function deleteExamQuestionBank(id) {
  return rest.remove(API_NAME, id);
}

export function searchExamQuestionBank(path, params) {
  return rest.search(API_NAME, path, params);
}
