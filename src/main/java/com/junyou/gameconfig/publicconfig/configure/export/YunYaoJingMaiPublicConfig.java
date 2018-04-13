package com.junyou.gameconfig.publicconfig.configure.export;

/**
 * 
 * @Description 云瑶晶脉公共数据表
 * @Author Yang Gao
 * @Since 2016-11-1
 * @Version 1.1.0
 */
public class YunYaoJingMaiPublicConfig extends AdapterPublicConfig {

    /**
     * 最大挖矿次数
     */
    private int maxtimes;
    /**
     * 活动地图ID
     */
    private int map;
    /**
     * 活动开始时间:(时:分)
     */
    private int[] starttime;
    /**
     * 活动结束时间:(时:分)
     */
    private int[] endtime;
    /**
     * 活动进入的等级限制
     */
    private int needLevel;

    public int getMaxtimes() {
        return maxtimes;
    }

    public void setMaxtimes(int maxtimes) {
        this.maxtimes = maxtimes;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public int[] getStarttime() {
        return starttime;
    }

    public void setStarttime(int[] starttime) {
        this.starttime = starttime;
    }

    public int[] getEndtime() {
        return endtime;
    }

    public void setEndtime(int[] endtime) {
        this.endtime = endtime;
    }

    public int getNeedLevel() {
        return needLevel;
    }

    public void setNeedLevel(int needLevel) {
        this.needLevel = needLevel;
    }

}