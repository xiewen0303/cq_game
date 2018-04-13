package com.junyou.gameconfig.publicconfig.configure.export;

/**
 * 
 * @Description 魔宫烈焰公共数据表
 * @Author Yang Gao
 * @Since 2016-10-19
 * @Version 1.1.0
 */
public class MoGongLieYanPublicConfig extends AdapterPublicConfig {

    /**
     * 御魔护盾最大值
     */
    private int yumo;
    /**
     * 活动地图id
     */
    private int map;
    /**
     * 场景中间隔<code>time</code>时间增加经验和真气,减少一定御魔值,单位秒
     */
    private int time;
    /**
     * 场景中间隔一定时间获得的经验,最终获得的计算公式=角色等级*角色等级*exp
     */
    private int exp;
    /**
     * 场景中间隔一定时间获得的真气,最终获得的计算公式=角色等级*zhenqi
     */
    private int zhenqi;
    /**
     * 击杀普通怪减少的御魔值
     */
    private int jianyumo1;
    /**
     * 击杀boss怪减少的御魔值
     */
    private int jianyumo2;
    /**
     * 角色死亡减少的御魔值
     */
    private int jianyumo3;
    /**
     * 在场景中单位时间减少的御魔值
     */
    private int jianyumo4;

    /**
     * 场景中御魔值为0,延迟踢人时间:单位秒
     */
    private int delaytime;

    public int getYumo() {
        return yumo;
    }

    public void setYumo(int yumo) {
        this.yumo = yumo;
    }

    public int getMap() {
        return map;
    }

    public void setMap(int map) {
        this.map = map;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getZhenqi() {
        return zhenqi;
    }

    public void setZhenqi(int zhenqi) {
        this.zhenqi = zhenqi;
    }

    public int getJianyumo1() {
        return jianyumo1;
    }

    public void setJianyumo1(int jianyumo1) {
        this.jianyumo1 = jianyumo1;
    }

    public int getJianyumo2() {
        return jianyumo2;
    }

    public void setJianyumo2(int jianyumo2) {
        this.jianyumo2 = jianyumo2;
    }

    public int getJianyumo3() {
        return jianyumo3;
    }

    public void setJianyumo3(int jianyumo3) {
        this.jianyumo3 = jianyumo3;
    }

    public int getJianyumo4() {
        return jianyumo4;
    }

    public void setJianyumo4(int jianyumo4) {
        this.jianyumo4 = jianyumo4;
    }

    public int getDelaytime() {
        return delaytime;
    }

    public void setDelaytime(int delaytime) {
        this.delaytime = delaytime;
    }

}