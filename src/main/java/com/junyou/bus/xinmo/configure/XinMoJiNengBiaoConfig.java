/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.configure;

import java.util.Map;

/**
 * @Description 心魔技能配置
 * @Author Yang Gao
 * @Since 2016-8-2
 * @Version 1.1.0
 */
public class XinMoJiNengBiaoConfig {

    /* 编号 */
    private int id;
    /* 序号 */
    private int seq;
    /* 等级 */
    private int level;
    /* 所属心魔类型 */
    private int xinmoType;
    /* 技能类型:1=普通被动技能;2=噬体被动技能 */
    private int skillType;
    /* 心魔阶级要求 */
    private int xinmoRank;
    /* 消耗道具大类id */
    private String itemId;
    /* 消耗道具数量 */
    private int itemCount;
    /* 消耗金币 */
    private int needMoney;
    /* 获得属性集合 */
    private Map<String, Long> attrsMap;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXinmoType() {
        return xinmoType;
    }

    public void setXinmoType(int xinmoType) {
        this.xinmoType = xinmoType;
    }

    public int getSkillType() {
        return skillType;
    }

    public void setSkillType(int skillType) {
        this.skillType = skillType;
    }

    public int getXinmoRank() {
        return xinmoRank;
    }

    public void setXinmoRank(int xinmoRank) {
        this.xinmoRank = xinmoRank;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getNeedMoney() {
        return needMoney;
    }

    public void setNeedMoney(int needMoney) {
        this.needMoney = needMoney;
    }

    public Map<String, Long> getAttrsMap() {
        return attrsMap;
    }

    public void setAttrsMap(Map<String, Long> attrsMap) {
        this.attrsMap = attrsMap;
    }

}
