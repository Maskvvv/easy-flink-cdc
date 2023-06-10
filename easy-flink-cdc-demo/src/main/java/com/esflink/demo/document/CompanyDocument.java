package com.esflink.demo.document;

import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;
import cn.easyes.annotation.rely.Analyzer;
import cn.easyes.annotation.rely.FieldType;
import cn.easyes.annotation.rely.IdType;
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
    @IndexId(type = IdType.CUSTOMIZE)
    private String id;

    /**
     * 公司名称
     */
    @IndexField
    private String name;

    /**
     * 公司简称
     */
    @IndexField
    private String nickName;

    /**
     * 公司名称
     */
    @IndexField
    private String keyword;

}
