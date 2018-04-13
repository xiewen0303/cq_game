/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.configure;

import java.util.Map;

/**
 * @Description 心魔-魔神噬体配置表
 * @Author Yang Gao
 * @Since 2016-7-18
 * @Version 1.1.0
 */
public class XinmoMoshenShitiBiaoConfig {

    /** 配置编号id **/
    private int id;
    /** 魔神类型 **/
    private int type;
    /** 魔神等阶 **/
    private int rank;
    /** 增加属性集合 **/
    private Map<String, Long> attrMap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Map<String, Long> getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map<String, Long> attrMap) {
        this.attrMap = attrMap;
    }

}
