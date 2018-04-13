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
import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.common.ObjectUtil;

/**
 * @Description <b>心魔深渊</b> 基础配置信息
 * @Author Yang Gao
 * @Since 2016-8-8
 * @Version 1.1.0
 */
public class XinmoShenyuanFubenConfig extends AbsFubenConfig implements IGoodsCheckConfig {

    /* 地图编号 */
    private int mapId;
    /* key=boss类型;value=怪物编号 */
    private Map<Integer, String> monsterMap;
    /* xy坐标集合 */
    private Integer[] xyPoint;
    /* 难度系数 */
    private float multiplier;
    /* 金钱奖励 */
    private long money;
    /* 经验奖励 */
    private long exp;
    /* 真气奖励 */
    private long zq;
    /* 副本物品奖励集合 */
    private Map<String, Integer> itemMap;

    public String getMonsterId(Integer type){
        return null == monsterMap ? null : monsterMap.get(type);
    }
    
    
    @Override
    public String getConfigName() {
        return "XinmoShenyuanFubenConfig" + getId();
    }

    @Override
    public List<Map<String, Integer>> getCheckMap() {
        // 校验配置的物品奖励信息prop
        List<Map<String, Integer>> checkList = null;
        if (!ObjectUtil.isEmpty(itemMap)) {
            checkList = new ArrayList<>();
            checkList.add(itemMap);
        }
        return checkList;
    }

    @Override
    public int getMapType() {
        return MapType.XINMO_SHENYUAN_FUBEN_MAP;
    }

    @Override
    public int getFubenType() {
        return 0;
    }

    @Override
    public short getExitCmd() {
        return ClientCmdType.XM_SHENYUAN_EXIT;
    }

    @Override
    public boolean isAutoProduct() {
        return false;
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public Integer[] getXyPoint() {
        return xyPoint;
    }

    public void setXyPoint(Integer[] xyPoint) {
        this.xyPoint = xyPoint;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
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

    public Map<String, Integer> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, Integer> itemMap) {
        this.itemMap = itemMap;
    }

    public Map<Integer, String> getMonsterMap() {
        return monsterMap;
    }

    public void setMonsterMap(Map<Integer, String> monsterMap) {
        this.monsterMap = monsterMap;
    }

}
