package com.github.mrzhqiang.rowing.dict;

/**
 * 字典服务。
 */
public interface DictService {

    /**
     * 字典组的 Sheet 名称。
     */
    String GROUP_SHEET_NAME = "group";
    /**
     * 字典项的 Sheet 名称。
     */
    String ITEM_SHEET_NAME = "item";

    /**
     * 同步字典。
     */
    void sync();
}
