package com.junyou.bus.equip.configure.export;

/**
 * @Description 宠物附属装备强化表配置
 * @Author Yang Gao
 * @Since 2016-9-6
 * @Version 1.1.0
 */
public class ChongwuFushuEquipQianghuaConfig {

    private Integer level;
    private int successrate;
    private float qhxs; // 强化属性=装备基础属性*强化系数 (取整数)
    private String needItemId;
    private int needItemCount;
    private int needMoney;

    private int gold;
    private int bgold;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public int getSuccessrate() {
        return successrate;
    }

    public void setSuccessrate(int successrate) {
        this.successrate = successrate;
    }

    public String getNeedItemId() {
        return needItemId;
    }

    public void setNeedItemId(String needItemId) {
        this.needItemId = needItemId;
    }

    public int getNeedItemCount() {
        return needItemCount;
    }

    public void setNeedItemCount(int needItemCount) {
        this.needItemCount = needItemCount;
    }

    public int getNeedMoney() {
        return needMoney;
    }

    public void setNeedMoney(int needMoney) {
        this.needMoney = needMoney;
    }

    public float getQhxs() {
        return qhxs;
    }

    public void setQhxs(float qhxs) {
        this.qhxs = qhxs;
    }

}
