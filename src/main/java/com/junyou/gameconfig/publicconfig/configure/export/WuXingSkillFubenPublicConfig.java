/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.gameconfig.publicconfig.configure.export;

/**
 * @Description 五行技能副本公共数据配置信息
 * @Author Yang Gao
 * @Since 2016-5-3
 * @Version 1.1.0
 */
public class WuXingSkillFubenPublicConfig extends AdapterPublicConfig {

    /* 通关胜利时限 */
    private int time1;
    /* 完美胜利时限 */
    private int time2;
    /* BOSS坐标 */
    private Integer[] bossXyPoint;
    /* 地图ID */
    private int mapId;

    public int getTime1() {
        return time1;
    }

    public void setTime1(int time1) {
        this.time1 = time1;
    }

    public int getTime2() {
        return time2;
    }

    public void setTime2(int time2) {
        this.time2 = time2;
    }

    public Integer[] getBossXyPoint() {
        return bossXyPoint;
    }

    public void setBossXyPoint(Integer[] bossXyPoint) {
        this.bossXyPoint = bossXyPoint;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

}
