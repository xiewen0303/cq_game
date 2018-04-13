/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.bus.fuben.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.bus.stagecontroll.MapType;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.ObjectUtil;

/**
 * @Description 五行副本(魔神幻境)基础配置信息
 * @Author Yang Gao
 * @Since 2016-4-18 下午2:45:45
 * @Version 1.1.0
 */
public class WuxingFubenConfig extends AbsFubenConfig implements IGoodsCheckConfig {

    /* 类型 */
    private String type;
    /* 难度等级 */
    private int level;
    /* 地图编号 */
    private int mapId;
    /* 怪物信息 key=怪物编号;value=怪物坐标集合 */
    private ReadOnlyMap<String, List<Integer[]>> monsterMap;
    /* 怪物五行属性集合 */
    private ReadOnlyMap<String, Long> wxAttrsMap;
    /* 金钱奖励 */
    private long money;
    /* 经验奖励 */
    private long exp;
    /* 真气奖励 */
    private long zq;
    /* 物品奖励 */
    private ReadOnlyMap<String, Integer> prop;

    @Override
    public int getMapType() {
        return MapType.WUXING_FUBEN_MAP;
    }

    @Override
    public int getFubenType() {
        return GameConstants.FUBEN_TYPE_WUXING;
    }

    @Override
    public short getExitCmd() {
        return ClientCmdType.EXIT_WUXING_FUBEN;
    }

    @Override
    public boolean isAutoProduct() {
        return false;// 怪物生成自由添加，不使用地图自动刷怪
    }

    @Override
    public String getConfigName() {
        return "WuxingFubenConfig" + getId();
    }

    @Override
    public List<Map<String, Integer>> getCheckMap() {
        // 校验配置的物品奖励信息prop
        List<Map<String, Integer>> checkList = null;
        if (!ObjectUtil.isEmpty(prop)) {
            checkList = new ArrayList<>();
            checkList.add(prop);
        }
        return checkList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public ReadOnlyMap<String, List<Integer[]>> getMonsterMap() {
        return monsterMap;
    }

    public void setMonsterMap(ReadOnlyMap<String, List<Integer[]>> monsterMap) {
        this.monsterMap = monsterMap;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public long getZq() {
        return zq;
    }

    public void setZq(long zq) {
        this.zq = zq;
    }

    public void setZq(int zq) {
        this.zq = zq;
    }

    public ReadOnlyMap<String, Integer> getProp() {
        return prop;
    }

    public void setProp(ReadOnlyMap<String, Integer> prop) {
        this.prop = prop;
    }

    public ReadOnlyMap<String, Long> getWxAttrsMap() {
        return wxAttrsMap;
    }

    public void setWxAttrsMap(ReadOnlyMap<String, Long> wxAttrsMap) {
        this.wxAttrsMap = wxAttrsMap;
    }

}
