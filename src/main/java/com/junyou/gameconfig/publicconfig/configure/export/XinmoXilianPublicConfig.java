/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.gameconfig.publicconfig.configure.export;

/**
 * @Description 心魔-洗练公共配置数据
 * @Author Yang Gao
 * @Since 2016-8-16
 * @Version 1.1.0
 */
public class XinmoXilianPublicConfig extends AdapterPublicConfig {
    /*无锁定消耗道具id和数量*/
    private String item1;
    /*锁定1个属性洗练消耗道具id和数量*/
    private String item2;
    /*锁定2个属性洗练消耗道具id和数量*/
    private String item3;
    /*洗练消耗的银两*/
    private int money;

    public XinmoXilianPublicConfig() {}

    public XinmoXilianPublicConfig(String item1, String item2, String item3, int money) {
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.money = money;
    }
    
    /*根据锁定属性个数获取对应的洗练道具消耗*/
    public String getItemByCheckNum(int checkNum) {
        String item = null;
        switch (checkNum) {
        case 0:
            item = this.item1;
            break;
        case 1:
            item = this.item2;
            break;
        case 2:
            item = this.item3;
            break;
        default:
            break;
        }
        return item;
    }

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

}
