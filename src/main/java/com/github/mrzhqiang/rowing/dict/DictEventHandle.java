package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.dict.gbt.DictGBT2260;
import com.github.mrzhqiang.rowing.dict.gbt.DictGBT2260Repository;
import com.github.mrzhqiang.rowing.dict.iso.DictISO3166;
import com.github.mrzhqiang.rowing.dict.iso.DictISO3166Repository;
import com.github.mrzhqiang.rowing.dict.iso.DictISO639;
import com.github.mrzhqiang.rowing.dict.iso.DictISO639Repository;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * 字典事件处理器。
 */
@RepositoryEventHandler
@Component
public class DictEventHandle {

    private final DictGroupRepository groupRepository;
    private final DictItemRepository itemRepository;
    private final DictGBT2260Repository gbt2260Repository;
    private final DictISO639Repository iso639Repository;
    private final DictISO3166Repository iso3166Repository;

    public DictEventHandle(DictGroupRepository groupRepository,
                           DictItemRepository itemRepository,
                           DictGBT2260Repository gbt2260Repository,
                           DictISO639Repository iso639Repository,
                           DictISO3166Repository iso3166Repository) {
        this.groupRepository = groupRepository;
        this.itemRepository = itemRepository;
        this.gbt2260Repository = gbt2260Repository;
        this.iso639Repository = iso639Repository;
        this.iso3166Repository = iso3166Repository;
    }

    @HandleBeforeCreate
    public void onBeforeCreate(DictGroup dictGroup) {
    }

    @HandleBeforeCreate
    public void onBeforeCreate(DictItem dictItem) {
    }

    @HandleBeforeCreate
    public void onBeforeCreate(DictGBT2260 dictGBT2260) {
    }

    @HandleBeforeCreate
    public void onBeforeCreate(DictISO639 dictISO639) {
    }

    @HandleBeforeCreate
    public void onBeforeCreate(DictISO3166 dictISO3166) {
    }

    @HandleBeforeSave
    public void onBeforeSave(DictGroup dictGroup) {
    }

    @HandleBeforeSave
    public void onBeforeSave(DictItem dictItem) {
    }

    @HandleBeforeSave
    public void onBeforeSave(DictGBT2260 dictGBT2260) {
    }

    @HandleBeforeSave
    public void onBeforeSave(DictISO639 dictISO639) {
    }

    @HandleBeforeSave
    public void onBeforeSave(DictISO3166 dictISO3166) {
    }

    @HandleBeforeDelete
    public void onBeforeDelete(DictGroup dictGroup) {
    }

    @HandleBeforeDelete
    public void onBeforeDelete(DictItem dictItem) {
    }

    @HandleBeforeDelete
    public void onBeforeDelete(DictGBT2260 dictGBT2260) {
    }

    @HandleBeforeDelete
    public void onBeforeDelete(DictISO639 dictISO639) {
    }

    @HandleBeforeDelete
    public void onBeforeDelete(DictISO3166 dictISO3166) {
    }

}
