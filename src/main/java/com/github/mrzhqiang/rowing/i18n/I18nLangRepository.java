package com.github.mrzhqiang.rowing.i18n;

import com.github.mrzhqiang.rowing.domain.BaseRepository;

import java.util.Optional;

/**
 * 国际化语言标签仓库。
 */
public interface I18nLangRepository extends BaseRepository<I18nLang> {

    Optional<I18nLang> findByCode(String code);
}
