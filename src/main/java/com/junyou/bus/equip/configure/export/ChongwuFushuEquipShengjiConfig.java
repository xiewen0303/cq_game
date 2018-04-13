package com.junyou.bus.equip.configure.export;

/**
 * @Description 宠物附属装备升阶配置表
 * @Author Yang Gao
 * @Since 2016-9-6
 * @Version 1.1.0
 */
public class ChongwuFushuEquipShengjiConfig {
    private int level;
    private int success;
    private String itemId1;// 大类id
    private int itemCount;
    private int money;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getItemId1() {
        return itemId1;
    }

    public void setItemId1(String itemId1) {
        this.itemId1 = itemId1;
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

}
