/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.gameconfig.publicconfig.configure.export;

/**
 * @Description 五行试炼副本公共数据配置信息
 * @Author Yang Gao
 * @Since 2016-6-21
 * @Version 1.1.0
 */
public class WuXingShilianFubenPublicConfig extends AdapterPublicConfig {
    /* 活动地图 */
    private Integer mapId;
    /* 每日挑战次数 */
    private Integer maxFightNum;
    /* 副本战斗时间:单位秒 */
    private Integer timeSeconds;

    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }

    public Integer getMaxFightNum() {
        return maxFightNum;
    }

    public void setMaxFightNum(Integer maxFightNum) {
        this.maxFightNum = maxFightNum;
    }

    public Integer getTimeSeconds() {
        return timeSeconds;
    }

    public void setTimeSeconds(Integer timeSeconds) {
        this.timeSeconds = timeSeconds;
    }

}
