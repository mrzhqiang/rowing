import rest from '@/api/rest';

/**
 * 初始化任务接口名称。
 * @type {string}
 */
const INIT_TASK_API_NAME = 'init-task';
/**
 * 初始化任务日志接口名称。
 * @type {string}
 */
const INIT_TASK_LOG_API_NAME = 'init-task-log';

export function pageInitTask(params) {
  return rest.findAll(INIT_TASK_API_NAME, params);
}

export function createInitTask(data) {
  return rest.create(INIT_TASK_API_NAME, data);
}

export function findInitTask(id, projection = '') {
  return rest.findOne(INIT_TASK_API_NAME, id, {projection});
}

export function editInitTask(id, data) {
  return rest.edit(INIT_TASK_API_NAME, id, data);
}

export function updateInitTask(id, data) {
  return rest.update(INIT_TASK_API_NAME, id, data);
}

export function deleteInitTask(id) {
  return rest.remove(INIT_TASK_API_NAME, id);
}

export function searchInitTask(path, params) {
  return rest.search(INIT_TASK_API_NAME, path, params);
}

export function pageInitTaskLog(params) {
  return rest.findAll(INIT_TASK_LOG_API_NAME, params);
}

export function createInitTaskLog(data) {
  return rest.create(INIT_TASK_LOG_API_NAME, data);
}

export function findInitTaskLog(id, projection = '') {
  return rest.findOne(INIT_TASK_LOG_API_NAME, id, {projection});
}

export function editInitTaskLog(id, data) {
  return rest.edit(INIT_TASK_LOG_API_NAME, id, data);
}

export function updateInitTaskLog(id, data) {
  return rest.update(INIT_TASK_LOG_API_NAME, id, data);
}

export function deleteInitTaskLog(id) {
  return rest.remove(INIT_TASK_LOG_API_NAME, id);
}

export function searchInitTaskLog(path, params) {
  return rest.search(INIT_TASK_LOG_API_NAME, path, params);
}
