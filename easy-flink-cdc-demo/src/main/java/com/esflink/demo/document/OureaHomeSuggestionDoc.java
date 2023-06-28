package com.esflink.demo.document;

import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;
import cn.easyes.annotation.rely.IdType;

/**
 * <p> 优加就业首页 职位/公司 搜索补全 </p>
 *
 * @author zhouhongyin
 * @since 2023/6/27 11:19
 */
@IndexName("ourea-home-suggestion")
public class OureaHomeSuggestionDoc {

    /**
     * id
     */
    @IndexId(type = IdType.CUSTOMIZE)
    private String id;

    /**
     * 职位/公司名称
     */
    @IndexField
    private String name;

    /**
     * 职位：状态 0未提交 1未审核 2已通过 3已驳回
     * 公司：状态 0未认证 1待审核 2已认证 3未通过
     */
    @IndexField
    private Integer status;

    /**
     * 1 企业 2 职位
     */
    @IndexField
    private Integer type;

    /**
     * 是否置顶
     */
    @IndexField
    private Integer top;

    /**
     * 职位是否上线 1上线 0下线
     */
    @IndexField
    private Integer onlined;

    /**
     * 排序
     */
    @IndexField
    private Double sequence;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getOnlined() {
        return onlined;
    }

    public void setOnlined(Integer onlined) {
        this.onlined = onlined;
    }

    public Double getSequence() {
        return sequence;
    }

    public void setSequence(Double sequence) {
        this.sequence = sequence;
    }
}
