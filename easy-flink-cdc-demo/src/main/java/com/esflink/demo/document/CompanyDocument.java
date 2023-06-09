package com.esflink.demo.document;

import cn.easyes.annotation.IndexName;
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
    private String id;

    /**
     * 公司名称
     */
    private String name;
    /**
     * 公司简称
     */
    private String nickName;
}
