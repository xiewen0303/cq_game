/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.bus.fuben.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.junyou.bus.stagecontroll.MapType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.checker.IGoodsCheckConfig;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.ObjectUtil;

/**
 * @Description 五行试炼副本基础配置信息
 * @Author Yang Gao
 * @Since 2016-6-21
 * @Version 1.1.0
 */
public class WuxingShilianFubenConfig extends AbsFubenConfig implements IGoodsCheckConfig {
    /* 怪物集合 */
    private List<ShilianMonster> monsterList;
    /* 通关奖励:key=怪物击杀数量,value=物品奖励Map */
    private Map<Integer, Map<String, Integer>> rewardMap;

    // 创建副本怪物对象
    public void addMonster(String monsterId, Integer monsterNum, Integer[] xyPoint, Integer refreTime, ReadOnlyMap<String, Long> wxAttrsMap) {
        if (null == monsterList) {
            monsterList = new ArrayList<>();
        }
        monsterList.add(new ShilianMonster(monsterId, monsterNum, xyPoint, refreTime, wxAttrsMap));
    }

    public List<ShilianMonster> getMonsterList() {
        return monsterList;
    }

    public Map<Integer, Map<String, Integer>> getRewardMap() {
        return rewardMap;
    }

    public void setRewardMap(Map<Integer, Map<String, Integer>> rewardMap) {
        this.rewardMap = rewardMap;
    }

    @Override
    public int getMapType() {
        return MapType.WUXING_SHILIAN_FUBEN_MAP;
    }

    @Override
    public int getFubenType() {
        return GameConstants.FUBEN_TYPE_WUXING_SHILIAN;
    }

    @Override
    public short getExitCmd() {
        return InnerCmdType.S_WUXING_SHILIAN_FUBEN_FINISH;
    }

    @Override
    public boolean isAutoProduct() {
        return false;// 怪物生成自由添加，不使用地图自动刷怪
    }

    @Override
    public String getConfigName() {
        return "WuxingShilianFubenConfig" + getId();
    }

    @Override
    public List<Map<String, Integer>> getCheckMap() {
        // 校验配置的物品奖励信息prop
        List<Map<String, Integer>> checkList = null;
        if (!ObjectUtil.isEmpty(rewardMap)) {
            checkList = new ArrayList<>();
            for (Map<String, Integer> reward : rewardMap.values()) {
                checkList.add(reward);
            }
        }
        return checkList;
    }

    /**
     * @Description 试炼副本怪物
     * @Author Yang Gao
     * @Since 2016-6-21
     * @Version 1.1.0
     */
    public class ShilianMonster {
        /* 怪物编号 */
        private String monsterId;
        /* 怪物数量 */
        private Integer monsterNum;
        /* 怪物xy坐标 */
        private Integer[] xyPoint;
        /* 怪物刷新时间 */
        private Integer refreTime;
        /* 怪物五行属性 */
        private ReadOnlyMap<String, Long> wxAttrsMap;

        public ShilianMonster(String monsterId, Integer monsterNum, Integer[] xyPoint, Integer refreTime, ReadOnlyMap<String, Long> wxAttrsMap) {
            this.monsterId = monsterId;
            this.monsterNum = monsterNum;
            this.xyPoint = xyPoint;
            this.refreTime = refreTime;
            this.wxAttrsMap = wxAttrsMap;
        }

        public String getMonsterId() {
            return monsterId;
        }

        public Integer getMonsterNum() {
            return monsterNum;
        }

        public Integer[] getXyPoint() {
            return xyPoint;
        }

        public Integer getRefreTime() {
            return refreTime;
        }

        public ReadOnlyMap<String, Long> getWxAttrsMap() {
            return wxAttrsMap;
        }

    }
}
