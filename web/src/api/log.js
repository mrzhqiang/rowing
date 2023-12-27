import rest from '@/api/rest';

/**
 * 异常日志接口名称。
 * @type {string}
 */
const EXCEPTION_LOG_API_NAME = 'exception-log';
/**
 * 操作日志接口名称。
 * @type {string}
 */
const ACTION_LOG_API_NAME = 'action-log';

export function searchExceptionLog(path, params) {
  return rest.search(EXCEPTION_LOG_API_NAME, path, params);
}

export function searchActionLog(path, params) {
  return rest.search(ACTION_LOG_API_NAME, path, params);
}
