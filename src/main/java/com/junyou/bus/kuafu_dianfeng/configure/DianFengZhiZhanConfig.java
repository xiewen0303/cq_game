package com.junyou.bus.kuafu_dianfeng.configure;

import com.kernel.data.dao.AbsVersion;

/**
 * @Description 巅峰之战配置表
 * @Author Yang Gao
 * @Since 2016-5-19
 * @Version 1.1.0
 */
public class DianFengZhiZhanConfig extends AbsVersion {

    /** 轮次 **/
    private Integer loop;
    /** 开始时间 **/
    private Integer[] begintime;
    /** 结束时间 **/
    private Integer[] endtime;
    /** 活动开始时间所在周 **/
    private Integer week;
    /** 地图出生坐标1 **/
    private Integer[] zuobiao1;
    /** 地图出生坐标2 **/
    private Integer[] zuobiao2;
    /** 地图编号 **/
    private Integer mapId;
    /** 每场战斗时间:秒 **/
    private int fighttime;
    /** 每轮战斗场数 **/
    private int fightcount;
    /** 每轮战斗胜利需要赢的场数 **/
    private int winfightcount;
    /** 每轮战斗开始前的准备时间:单位秒 **/
    private int fightBeforeTime;
    /** 每轮战斗结果展示时间 **/
    private int resultShowTime;

    public Integer getLoop() {
        return loop;
    }

    public void setLoop(Integer loop) {
        this.loop = loop;
    }

    public Integer[] getBegintime() {
        return begintime;
    }

    public void setBegintime(Integer[] begintime) {
        this.begintime = begintime;
    }

    public Integer[] getEndtime() {
        return endtime;
    }

    public void setEndtime(Integer[] endtime) {
        this.endtime = endtime;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer[] getZuobiao1() {
        return zuobiao1;
    }

    public void setZuobiao1(Integer[] zuobiao1) {
        this.zuobiao1 = zuobiao1;
    }

    public Integer[] getZuobiao2() {
        return zuobiao2;
    }

    public void setZuobiao2(Integer[] zuobiao2) {
        this.zuobiao2 = zuobiao2;
    }

    public Integer getMapId() {
        return mapId;
    }

    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }

    public int getFighttime() {
        return fighttime;
    }

    public void setFighttime(int fighttime) {
        this.fighttime = fighttime;
    }

    public int getFightcount() {
        return fightcount;
    }

    public void setFightcount(int fightcount) {
        this.fightcount = fightcount;
    }

    public int getWinfightcount() {
        return winfightcount;
    }

    public void setWinfightcount(int winfightcount) {
        this.winfightcount = winfightcount;
    }

    public int getFightBeforeTime() {
        return fightBeforeTime;
    }

    public void setFightBeforeTime(int fightBeforeTime) {
        this.fightBeforeTime = fightBeforeTime;
    }

    public int getResultShowTime() {
        return resultShowTime;
    }

    public void setResultShowTime(int resultShowTime) {
        this.resultShowTime = resultShowTime;
    }

}
