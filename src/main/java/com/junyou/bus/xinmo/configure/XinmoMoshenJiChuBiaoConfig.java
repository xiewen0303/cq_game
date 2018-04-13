/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.configure;

import java.util.Map;

/**
 * @Description 心魔魔神基础表
 * @Author Yang Gao
 * @Since 2016-7-18
 * @Version 1.1.0
 */
public class XinmoMoshenJiChuBiaoConfig {

    /** 配置编号id **/
    private int id;
    /** 类型 **/
    private int type;
    /** 等阶 **/
    private int rank;
    /** 凝神境界等阶 **/
    private int ningshenRank;
    /** 道具大类Id **/
    private String itemId;
    /** 道具数量 **/
    private int itemCount;
    /** 升阶需要银两 **/
    private int money;
    /** 兑换道具元宝 **/
    private int gold;
    /** 兑换道具绑定元宝 **/
    private int bgold;
    /** 升阶祝福值最小值 **/
    private int blessMinVal;
    /** 升阶祝福值最大值 **/
    private int blessMaxVal;
    /** 升阶成功的概率 **/
    private int successRatio;
    /** 升阶失败获得最小祝福值 **/
    private int addBlessMinVal;
    /** 升阶失败获得最大祝福值 **/
    private int addBlessMaxVal;
    /** 是否需要重置祝福值 **/
    private boolean isReset;
    /** 祝福值重置时间:单位小时 **/
    private int resetHour;
    /** 升阶成功是否需要公告 **/
    private int noticeCode;
    /** 永久加成的属性集合 **/
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

    public int getNingshenRank() {
        return ningshenRank;
    }

    public void setNingshenRank(int ningshenRank) {
        this.ningshenRank = ningshenRank;
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

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getBgold() {
        return bgold;
    }

    public void setBgold(int bgold) {
        this.bgold = bgold;
    }

    public int getBlessMinVal() {
        return blessMinVal;
    }

    public void setBlessMinVal(int blessMinVal) {
        this.blessMinVal = blessMinVal;
    }

    public int getBlessMaxVal() {
        return blessMaxVal;
    }

    public void setBlessMaxVal(int blessMaxVal) {
        this.blessMaxVal = blessMaxVal;
    }

    public int getSuccessRatio() {
        return successRatio;
    }

    public void setSuccessRatio(int successRatio) {
        this.successRatio = successRatio;
    }

    public int getAddBlessMinVal() {
        return addBlessMinVal;
    }

    public void setAddBlessMinVal(int addBlessMinVal) {
        this.addBlessMinVal = addBlessMinVal;
    }

    public int getAddBlessMaxVal() {
        return addBlessMaxVal;
    }

    public void setAddBlessMaxVal(int addBlessMaxVal) {
        this.addBlessMaxVal = addBlessMaxVal;
    }

    public boolean isReset() {
        return isReset;
    }

    public void setReset(boolean isReset) {
        this.isReset = isReset;
    }

    public int getResetHour() {
        return resetHour;
    }

    public void setResetHour(int resetHour) {
        this.resetHour = resetHour;
    }

    public int getNoticeCode() {
        return noticeCode;
    }

    public void setNoticeCode(int noticeCode) {
        this.noticeCode = noticeCode;
    }

    public Map<String, Long> getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Map<String, Long> attrMap) {
        this.attrMap = attrMap;
    }

}
