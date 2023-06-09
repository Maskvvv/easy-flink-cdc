package com.esflink.demo.document;

import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;
import cn.easyes.annotation.rely.Analyzer;
import cn.easyes.annotation.rely.FieldType;
import lombok.Data;

/**
 * ES数据模型: 公司
 **/
@Data
@IndexName("company")
public class CompanyDocument {
    /**
     * id
     */
    @IndexId
    private String id;

    /**
     * 公司名称
     */
    @IndexField(fieldType = FieldType.KEYWORD_TEXT, analyzer = Analyzer.IK_MAX_WORD)
    private String name;
    /**
     * 公司简称
     */
    private String nickName;
}
