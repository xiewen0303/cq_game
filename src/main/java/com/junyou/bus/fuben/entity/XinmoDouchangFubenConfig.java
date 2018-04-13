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
 * 
 * @Description 心魔斗场副本基础配置信息
 * @Author Yang Gao
 * @Since 2016-8-23
 * @Version 1.1.0
 */
public class XinmoDouchangFubenConfig extends AbsFubenConfig implements IGoodsCheckConfig {

    /*
     * 副本怪物集合List<Object[]>
     * object[]{
     * 0=(String)怪物编号
     * 1=(Integer[x,y]怪物坐标)
     * 2=(int)怪物类型
     * 3=(String)怪物死亡触发buff的编号}
     */
    private List<Object[]> monsterList;
    /*
     * 通关奖励:Map<Intger,Object[]>
     * key=怪物击杀数量
     * value=物品奖励
     *    Object[]{
     *        0=(long)经验奖励
     *        1=(long)银两奖励
     *        2=(long)真气奖励
     *        3=(Map<String,Integer>
     *              key=奖励物品编号
     *              value=物品数量
     *           )
     *   }
     */
    private Map<Integer, Object[]> rewardMap;

    public List<Object[]> getMonsterList() {
        return monsterList;
    }

    public void setMonsterList(List<Object[]> monsterList) {
        this.monsterList = monsterList;
    }

    public Map<Integer, Object[]> getRewardMap() {
        return rewardMap;
    }

    public void setRewardMap(Map<Integer, Object[]> rewardMap) {
        this.rewardMap = rewardMap;
    }

    @Override
    public String getConfigName() {
        return this.getClass().getSimpleName();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Integer>> getCheckMap() {
        // 校验配置的物品奖励信息prop
        List<Map<String, Integer>> checkList = null;
        if (!ObjectUtil.isEmpty(rewardMap)) {
            checkList = new ArrayList<>();
            for (Object[] rewardData : rewardMap.values()) {
                if(rewardData != null && rewardData.length > 3 && rewardData[3] instanceof Map){
                    Map<String, Integer> reward = (Map<String, Integer>) rewardData[3];
                    checkList.add(reward);
                }
            }
        }
        return checkList;
    }

    @Override
    public int getMapType() {
        return MapType.XINMO_DOUCHANG_FUBEN_MAP;
    }

    @Override
    public int getFubenType() {
        return 0;
    }

    @Override
    public short getExitCmd() {
        return ClientCmdType.XM_DOUCHANG_EXIT;
    }

    @Override
    public boolean isAutoProduct() {
        return false;
    }

    @Override
    public boolean canFuhuo() {
        return true;
    }
    
    

}
