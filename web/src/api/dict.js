import rest from '@/api/rest';

const DICT_API = 'dict';
const DICT_ITEM_API = 'dict-item';

export const DICT_CODES = {
  logic: 'LOGIC'
};

export function pageDict(params) {
  return rest.findAll(DICT_API, params);
}

export function createDict(data) {
  return rest.create(DICT_API, data);
}

export function findDict(id, projection = null) {
  return rest.findOne(DICT_API, id, {projection});
}

export function editDict(id, data) {
  return rest.edit(DICT_API, id, data);
}

export function updateDict(id, data) {
  return rest.update(DICT_API, id, data);
}

export function deleteDict(id) {
  return rest.remove(DICT_API, id);
}

export function searchDict(path, params) {
  return rest.search(DICT_API, path, params);
}

export function pageDictItem(params) {
  return rest.findAll(DICT_ITEM_API, params);
}

export function createDictItem(data) {
  return rest.create(DICT_ITEM_API, data);
}

export function findDictItem(id, projection = null) {
  return rest.findOne(DICT_ITEM_API, id, {projection});
}

export function editDictItem(id, data) {
  return rest.edit(DICT_ITEM_API, id, data);
}

export function updateDictItem(id, data) {
  return rest.update(DICT_ITEM_API, id, data);
}

export function deleteDictItem(id) {
  return rest.remove(DICT_ITEM_API, id);
}

export function searchDictItem(path, params) {
  return rest.search(DICT_ITEM_API, path, params);
}
