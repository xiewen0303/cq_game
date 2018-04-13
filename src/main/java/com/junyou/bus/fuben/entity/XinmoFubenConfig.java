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
 * @Description <b>挑战心魔</b> 基础配置信息
 * @Author Yang Gao
 * @Since 2016-4-18 下午2:45:45
 * @Version 1.1.0
 */
public class XinmoFubenConfig extends AbsFubenConfig implements IGoodsCheckConfig {

    /* 地图编号 */
    private int mapId;
    /* key=怪物编号;value=xy坐标集合 */
    private Map<String, Integer[]> monsterMap;
    /* 挑战增加的腐化值 */
    private int fuhuaVal;
    /* 怪物boss掉落物品集合 */
    private Map<String, Integer> itemMap;

    @Override
    public String getConfigName() {
        return "XinmoFubenConfig" + getId();
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
        return MapType.XINMO_FUBEN_MAP;
    }

    @Override
    public int getFubenType() {
        return 0;
    }

    @Override
    public short getExitCmd() {
        return ClientCmdType.XM_FUBEN_EXIT;
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

    public Map<String, Integer[]> getMonsterMap() {
        return monsterMap;
    }

    public void setMonsterMap(Map<String, Integer[]> monsterMap) {
        this.monsterMap = monsterMap;
    }

    public int getFuhuaVal() {
        return fuhuaVal;
    }

    public void setFuhuaVal(int fuhuaVal) {
        this.fuhuaVal = fuhuaVal;
    }

    public Map<String, Integer> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, Integer> itemMap) {
        this.itemMap = itemMap;
    }

}
