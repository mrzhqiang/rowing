import rest from '@/api/rest';

const API_NAME = 'exam-question-option';

export function pageExamQuestionOption(params) {
  return rest.findAll(API_NAME, params);
}

export function createExamQuestionOption(data) {
  return rest.create(API_NAME, data);
}

export function findExamQuestionOption(id, projection = '') {
  return rest.findOne(API_NAME, id, {projection});
}

export function editExamQuestionOption(id, data) {
  return rest.edit(API_NAME, id, data);
}

export function updateExamQuestionOption(id, data) {
  return rest.update(API_NAME, id, data);
}

export function deleteExamQuestionOption(id) {
  return rest.remove(API_NAME, id);
}

export function searchExamQuestionOption(path, params) {
  return rest.search(API_NAME, path, params);
}
