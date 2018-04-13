/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.configure;

import java.util.Map;

/**
 * @Description 心魔洗练配置类
 * @Author Yang Gao
 * @Since 2016-8-15
 * @Version 1.1.0
 */
public class XinMoXiLianBiaoConfig {

    /* 配置id */
    private int id;
    /* 属性类型 */
    private String attrType;
    /* 属性权值 */
    private int typeOdds;
    /* 属性信息(key=[0=属性品质类型,1=属性值随机最小值,2=属性值随机最大值],value=属性值品质随机权值) */
    private Map<Object[], Integer> infoMap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public int getTypeOdds() {
        return typeOdds;
    }

    public void setTypeOdds(int typeOdds) {
        this.typeOdds = typeOdds;
    }

    public Map<Object[], Integer> getInfoMap() {
        return infoMap;
    }

    public void setInfoMap(Map<Object[], Integer> infoMap) {
        this.infoMap = infoMap;
    }

}
