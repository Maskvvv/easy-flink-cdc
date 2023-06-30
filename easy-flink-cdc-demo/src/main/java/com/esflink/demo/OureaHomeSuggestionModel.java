package com.esflink.demo;

/**
 * <p> </p>
 *
 * @author zhouhongyin
 * @since 2023/6/29 11:04
 */
public class OureaHomeSuggestionModel {

    /**
     * id
     */
    private String id;

    /**
     * 职位/公司名称
     */
    private String name;

    /**
     * 职位：状态 0未提交 1未审核 2已通过 3已驳回
     * 公司：状态 0未认证 1待审核 2已认证 3未通过
     */
    private Integer status;

    /**
     * 1 企业 2 职位
     */
    private Integer type;

    /**
     * 是否置顶
     */
    private Integer top;

    /**
     * 职位是否上线 1上线 0下线
     */
    private Integer onlined;

    /**
     * 排序
     */
    private Double sequence;

    private String highlight;

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

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
