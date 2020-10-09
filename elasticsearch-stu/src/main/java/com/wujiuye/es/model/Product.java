package com.wujiuye.es.model;

import com.wujiuye.es.config.EsField;

public class Product {

    /**
     * 商品id
     */
    @EsField(type = "integer")
    private Integer leId;
    /**
     * 1：新品上架 2：补货清单 3：今日折扣
     */
    @EsField(type = "integer")
    private Integer type;
    /**
     * 1：全球直购 2：境内发货
     */
    @EsField(type = "integer")
    private Integer labelId;
    /**
     * 一级菜单
     */
    @EsField(type = "integer")
    private Integer fistLetId;
    /**
     * 一级菜单名称
     */
    private String fistLetName;
    /**
     * 产品id
     */
    @EsField(type = "integer")
    private Integer brandId;
    /**
     * 产品名称
     */
    private String brandName;
    /**
     * 分类排序
     */
    private Integer letOrder;
    /**
     * 日期
     */
    private String date;

    public Integer getLeId() {
        return leId;
    }

    public void setLeId(Integer leId) {
        this.leId = leId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public Integer getFistLetId() {
        return fistLetId;
    }

    public void setFistLetId(Integer fistLetId) {
        this.fistLetId = fistLetId;
    }

    public String getFistLetName() {
        return fistLetName;
    }

    public void setFistLetName(String fistLetName) {
        this.fistLetName = fistLetName;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getLetOrder() {
        return letOrder;
    }

    public void setLetOrder(Integer letOrder) {
        this.letOrder = letOrder;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Product{" +
                "leId=" + leId +
                ", type=" + type +
                ", labelId=" + labelId +
                ", fistLetId=" + fistLetId +
                ", fistLetName='" + fistLetName + '\'' +
                ", brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                ", letOrder=" + letOrder +
                ", date='" + date + '\'' +
                '}';
    }

}
