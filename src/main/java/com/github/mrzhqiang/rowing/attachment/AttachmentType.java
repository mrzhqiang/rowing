package com.github.mrzhqiang.rowing.attachment;

import com.github.mrzhqiang.rowing.util.Enums;

/**
 * 附件类型。
 */
public enum AttachmentType {

    /**
     * 有损压缩图片。
     */
    JPEG,
    /**
     * 无损压缩图片。
     */
    PNG,
    /**
     * 旧 Word 文档。
     */
    DOC,
    /**
     * 新 Word 文档。
     */
    DOCX,
    /**
     * 旧 Excel 表格。
     */
    XLS,
    /**
     * 新 Excel 表格。
     */
    XLSX,
    /**
     * 纯文本。
     */
    TXT,
    /**
     * 字符分割表格。
     */
    CSV,
    /**
     * 可携带文件。
     */
    PDF,
    ;

    public static AttachmentType of(String type) {
        return Enums.findByNameIgnoreCase(AttachmentType.class, type, TXT);
    }

}
