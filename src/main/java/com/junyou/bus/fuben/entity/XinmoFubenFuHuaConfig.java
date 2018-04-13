/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.bus.fuben.entity;

/**
 * @Description <b>挑战心魔</b> 腐化度基础配置信息
 * @Author Yang Gao
 * @Since 2016-4-18 下午2:45:45
 * @Version 1.1.0
 */
public class XinmoFubenFuHuaConfig {
    /* 元神境界等阶 */
    private int xinmoRank;
    /* 每次减少腐化度的时间戳 */
    private int timing;
    /* 每次减少腐化度的数值 */
    private int fuhuaVal;

    public int getXinmoRank() {
        return xinmoRank;
    }

    public void setXinmoRank(int xinmoRank) {
        this.xinmoRank = xinmoRank;
    }

    public int getTiming() {
        return timing;
    }

    public void setTiming(int timing) {
        this.timing = timing;
    }

    public int getFuhuaVal() {
        return fuhuaVal;
    }

    public void setFuhuaVal(int fuhuaVal) {
        this.fuhuaVal = fuhuaVal;
    }

}
