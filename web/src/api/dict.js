import rest from '@/api/rest';

const DICT_API = 'dict';
const DICT_ITEM_API = 'dict-item';
const DICT_GBT_2260_API = 'dict-gbt-2260';
const DICT_ISO_639_API = 'dict-iso-639';
const DICT_ISO_3166_API = 'dict-iso-3166';

export const DICT_CODES = {
  logic: 'LOGIC',
  taskType: 'TASK_TYPE',
  taskStatus: 'TASK_STATUS',
  accountType: 'ACCOUNT_TYPE',
  gender: 'GENDER',
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

export function pageDictGBT2260(params) {
  return rest.findAll(DICT_GBT_2260_API, params);
}

export function createDictGBT2260(data) {
  return rest.create(DICT_GBT_2260_API, data);
}

export function findDictGBT2260(id, projection = null) {
  return rest.findOne(DICT_GBT_2260_API, id, {projection});
}

export function editDictGBT2260(id, data) {
  return rest.edit(DICT_GBT_2260_API, id, data);
}

export function updateDictGBT2260(id, data) {
  return rest.update(DICT_GBT_2260_API, id, data);
}

export function deleteDictGBT2260(id) {
  return rest.remove(DICT_GBT_2260_API, id);
}

export function searchDictGBT2260(path, params) {
  return rest.search(DICT_GBT_2260_API, path, params);
}

export function pageDictISO639(params) {
  return rest.findAll(DICT_ISO_639_API, params);
}

export function createDictISO639(data) {
  return rest.create(DICT_ISO_639_API, data);
}

export function findDictISO639(id, projection = null) {
  return rest.findOne(DICT_ISO_639_API, id, {projection});
}

export function editDictISO639(id, data) {
  return rest.edit(DICT_ISO_639_API, id, data);
}

export function updateDictISO639(id, data) {
  return rest.update(DICT_ISO_639_API, id, data);
}

export function deleteDictISO639(id) {
  return rest.remove(DICT_ISO_639_API, id);
}

export function searchDictISO639(path, params) {
  return rest.search(DICT_ISO_639_API, path, params);
}

export function pageDictISO3166(params) {
  return rest.findAll(DICT_ISO_3166_API, params);
}

export function createDictISO3166(data) {
  return rest.create(DICT_ISO_3166_API, data);
}

export function findDictISO3166(id, projection = null) {
  return rest.findOne(DICT_ISO_3166_API, id, {projection});
}

export function editDictISO3166(id, data) {
  return rest.edit(DICT_ISO_3166_API, id, data);
}

export function updateDictISO3166(id, data) {
  return rest.update(DICT_ISO_3166_API, id, data);
}

export function deleteDictISO3166(id) {
  return rest.remove(DICT_ISO_3166_API, id);
}

export function searchDictISO3166(path, params) {
  return rest.search(DICT_ISO_3166_API, path, params);
}
