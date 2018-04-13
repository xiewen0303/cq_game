/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.gameconfig.publicconfig.configure.export;

/**
 * 
 * @Description 心魔-斗场副本公共配置数据
 * @Author Yang Gao
 * @Since 2016-8-23
 * @Version 1.1.0
 */
public class XinmoDouchangFubenPublicConfig extends AdapterPublicConfig {
    /* 副本地图编号 */
    private int mapId;
    /* 副本挑战时长:单位秒 */
    private int time;
    /* 副本每日挑战次数 */
    private int count;
    /* 购买副本挑战次数需要元宝数量 */
    private int needGold;

    public XinmoDouchangFubenPublicConfig() {
    }

    public XinmoDouchangFubenPublicConfig(int mapId, int time, int count, int needGold) {
        super();
        this.mapId = mapId;
        this.time = time;
        this.count = count;
        this.needGold = needGold;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getNeedGold() {
        return needGold;
    }

    public void setNeedGold(int needGold) {
        this.needGold = needGold;
    }

}
