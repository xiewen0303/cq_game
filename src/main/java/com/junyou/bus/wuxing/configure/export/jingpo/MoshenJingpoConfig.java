/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.wuxing.configure.export.jingpo;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * @Description 精魄基础配置
 * @Author Yang Gao
 * @Since 2016-5-10
 * @Version 1.1.0
 */
public class MoshenJingpoConfig extends AbsVersion {
    /** 编号 **/
    private int id;
    /** 精魄类型 **/
    private int type;
    /** 品阶 **/
    private int quality;
    /** 等级 **/
    private int level;
    /** 属性类型 **/
    private int attrType;
    /** 加成属性 **/
    private Map<String, Long> attr;
    /** 基础经验 **/
    private int exp;
    /** 升级经验 **/
    private int needExp;

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

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAttrType() {
        return attrType;
    }

    public void setAttrType(int attrType) {
        this.attrType = attrType;
    }

    public Map<String, Long> getAttr() {
        return attr;
    }

    public void setAttr(Map<String, Long> attr) {
        this.attr = attr;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getNeedExp() {
        return needExp;
    }

    public void setNeedExp(int needExp) {
        this.needExp = needExp;
    }

}
