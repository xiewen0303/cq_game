package com.junyou.bus.wuxing.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * @description 五行技能配置表
 * @author Yang Gao
 * @date 2016-04-25 18:04:34
 */
public class MoShenJiNengBiaoConfig extends AbsVersion {

    private Integer id;
    private Integer type;
    private Integer seq;
    private Integer level;
    private Integer limit;
    private String needItem;
    private Integer itemCount;
    private Integer needMoney;
    private Map<String, Long> attrMap;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getNeedItem() {
        return needItem;
    }

    public void setNeedItem(String needItem) {
        this.needItem = needItem;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getNeedMoney() {
        return needMoney;
    }

    public void setNeedMoney(Integer needMoney) {
        this.needMoney = needMoney;
    }

    public Map<String, Long> getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map<String, Long> attrMap) {
        this.attrMap = attrMap;
    }

}
