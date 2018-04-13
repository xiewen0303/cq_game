package com.junyou.gameconfig.publicconfig.configure.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @Description 跨服云宫之战公共配置表
 * @Author Yang Gao
 * @Since 2016-9-23
 * @Version 1.1.0
 */
public class KuafuYunGongPublicConfig extends AdapterPublicConfig {
    /* 活动时间 */
    private int week;// 活动开启日期,单位:周(0-6周日到周六)
    private int[] readytime;// 活动准备时间
    private int[] starttime;// 活动开始时间
    private int[] endtime;// 活动结束时间
    /* 活动结束后,满足活动时间内停留时间达到一定条件才发送的奖励 */
    private int time2;// 需在活动地图停留超过一定时间获得奖励
    private int zhenqi2;// 活动结束后，停留超过一定时间获得真气
    private int jingyan2;// 活动结束后，停留超过一定时间获得经验
    private int winBg;// 胜方奖励帮贡
    private int winExp;// 胜方奖励经验(计算公式=exp*level*level)
    private String chenhao1;// 获胜公会门主称号奖励道具id
    private String chenhao2;// 获胜公会门主配偶称号奖励道具id
    private int loseBg;// 败方奖励帮贡
    private int loseExp;// 败方奖励经验(计算公式=exp*level*level)
    private Map<String, Integer> jiangitem;// 胜方公会全体官员物品奖励
    /* 活动开始后,满足条件的玩家才能进入地图 */
    private int map;// 活动地图编号
    private int time;// 活动获得胜利停留一定时间后清空场景 :秒
    private int needTime;// 活动获得胜利所需停留时间:秒
    private long needZhanli;// 进入地图需要的最低战斗力
    private int needLevel;// 进入地图需要的最低等级
    private int groupNumber;// 一个跨服服务器分多少个公会
    /* 活动时间内,满足一定条件发送奖励 */
    private int delay;// 活动时间内间隔时间:单位秒
    private long exp;// 活动时间内每隔一定时间获得经验(计算公式=exp*level*level)
    private long zhenqi;// 活动时间内每一定时间获得真气(计算公式=zhenqi*level)
    /* 活动时间内奖励 */
    private float addexp;// 胜方打坐经验加成比例
    private float addzhenqi;// 胜方打坐真气加成比例
    /* 活动开始后,创建场景的资源数据 */
    private int ziyuanid;// 旗子资源id
    private int[] zuobiao;// 旗子的初始坐标
    private int needgong;// 拔旗所需帮派资金
    private String kqbuff;// 扛旗buff
    private String longt;// 图腾id
    private int shanghai;// 固定伤害值（每次攻击伤害为100点）
    private List<Integer[]> gongfuhuo;// 攻方复活点
    private List<Integer[]> shoufuhuo;// 守方复活点
    /* 活动公告 */
    private int gonggao1;// 活动准备公告
    private int gonggao2;// 活动开始公告
    private int gonggao3;// 活动地图内结束公告
    private int gonggao4;// 活动结束公告
    private int gonggao5;// 活动地图内拔旗公告

    private List<Integer[]> allFuhuo;// 所有复活坐标集合

    /**
     * 获取所有复活点(出生点)坐标集合
     */
    public List<Integer[]> getAllFuhuoPointList() {
        if (null == allFuhuo) {
            allFuhuo = new ArrayList<Integer[]>();
            if (null != gongfuhuo)
                allFuhuo.addAll(gongfuhuo);
            if (null != shoufuhuo)
                allFuhuo.addAll(shoufuhuo);
        }
        return allFuhuo;
    }

    public int getWinBg() {
        return winBg;
    }

    public void setWinBg(int winBg) {
        this.winBg = winBg;
    }

    public int getWinExp() {
        return winExp;
    }

    public void setWinExp(int winExp) {
        this.winExp = winExp;
    }

    public String getChenhao1() {
        return chenhao1;
    }

    public void setChenhao1(String chenhao1) {
        this.chenhao1 = chenhao1;
    }

    public String getChenhao2() {
        return chenhao2;
    }

    public void setChenhao2(String chenhao2) {
        this.chenhao2 = chenhao2;
    }

    public float getAddexp() {
        return addexp;
    }

    public void setAddexp(float addexp) {
        this.addexp = addexp;
    }

    public float getAddzhenqi() {
        return addzhenqi;
    }

    public void setAddzhenqi(float addzhenqi) {
        this.addzhenqi = addzhenqi;
    }

    public Map<String, Integer> getJiangitem() {
        return jiangitem;
    }

    public void setJiangitem(Map<String, Integer> jiangitem) {
        this.jiangitem = jiangitem;
    }

    public int getLoseBg() {
        return loseBg;
    }

    public void setLoseBg(int loseBg) {
        this.loseBg = loseBg;
    }

    public int getLoseExp() {
        return loseExp;
    }

    public void setLoseExp(int loseExp) {
        this.loseExp = loseExp;
    }

    public int getTime2() {
        return time2;
    }

    public void setTime2(int time2) {
        this.time2 = time2;
    }

    public int getZhenqi2() {
        return zhenqi2;
    }

    public void setZhenqi2(int zhenqi2) {
        this.zhenqi2 = zhenqi2;
    }

    public int getJingyan2() {
        return jingyan2;
    }

    public void setJingyan2(int jingyan2) {
        this.jingyan2 = jingyan2;
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

    public int getNeedTime() {
        return needTime;
    }

    public void setNeedTime(int needTime) {
        this.needTime = needTime;
    }

    public long getNeedZhanli() {
        return needZhanli;
    }

    public void setNeedZhanli(long needZhanli) {
        this.needZhanli = needZhanli;
    }

    public int getNeedLevel() {
        return needLevel;
    }

    public void setNeedLevel(int needLevel) {
        this.needLevel = needLevel;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public long getZhenqi() {
        return zhenqi;
    }

    public void setZhenqi(long zhenqi) {
        this.zhenqi = zhenqi;
    }

    public int getZiyuanid() {
        return ziyuanid;
    }

    public void setZiyuanid(int ziyuanid) {
        this.ziyuanid = ziyuanid;
    }

    public int[] getZuobiao() {
        return zuobiao;
    }

    public void setZuobiao(int[] zuobiao) {
        this.zuobiao = zuobiao;
    }

    public int getNeedgong() {
        return needgong;
    }

    public void setNeedgong(int needgong) {
        this.needgong = needgong;
    }

    public String getKqbuff() {
        return kqbuff;
    }

    public void setKqbuff(String kqbuff) {
        this.kqbuff = kqbuff;
    }

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }

    public int getShanghai() {
        return shanghai;
    }

    public void setShanghai(int shanghai) {
        this.shanghai = shanghai;
    }

    public List<Integer[]> getGongfuhuo() {
        return gongfuhuo;
    }

    public void setGongfuhuo(List<Integer[]> gongfuhuo) {
        this.gongfuhuo = gongfuhuo;
    }

    public List<Integer[]> getShoufuhuo() {
        return shoufuhuo;
    }

    public void setShoufuhuo(List<Integer[]> shoufuhuo) {
        this.shoufuhuo = shoufuhuo;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int[] getReadytime() {
        return readytime;
    }

    public void setReadytime(int[] readytime) {
        this.readytime = readytime;
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

    public int getGonggao1() {
        return gonggao1;
    }

    public void setGonggao1(int gonggao1) {
        this.gonggao1 = gonggao1;
    }

    public int getGonggao2() {
        return gonggao2;
    }

    public void setGonggao2(int gonggao2) {
        this.gonggao2 = gonggao2;
    }

    public int getGonggao3() {
        return gonggao3;
    }

    public void setGonggao3(int gonggao3) {
        this.gonggao3 = gonggao3;
    }

    public int getGonggao4() {
        return gonggao4;
    }

    public void setGonggao4(int gonggao4) {
        this.gonggao4 = gonggao4;
    }

    public int getGonggao5() {
        return gonggao5;
    }

    public void setGonggao5(int gonggao5) {
        this.gonggao5 = gonggao5;
    }

}
