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
 * @Description 五行技能副本(镇妖塔)基础配置信息
 * @Author Yang Gao
 * @Since 2016-5-3
 * @Version 1.1.0
 */
public class WuxingSkillFubenConfig extends AbsFubenConfig implements IGoodsCheckConfig {

    /* boss怪物编号 */
    private String monsterId;
    /* 金钱奖励 */
    private long money;
    /* 经验奖励 */
    private long exp;
    /* 真气奖励 */
    private long zq;
    /* 物品奖励 */
    private ReadOnlyMap<String, Integer> prop;
    /* 加成buff值 */
    private int addBuffVal;
    /* 减伤buff值 */
    private int subBuffVal;
    /* boss怪物五行属性集合 */
    private ReadOnlyMap<String, Long> wxAttrsMap;

    @Override
    public int getMapType() {
        return MapType.WUXING_SKILL_FUBEN_MAP;
    }

    @Override
    public int getFubenType() {
        return GameConstants.FUBEN_TYPE_WUXING_SKILL;
    }

    @Override
    public short getExitCmd() {
        return ClientCmdType.WUXING_SKILL_FUBEN_EXIT;
    }

    @Override
    public boolean isAutoProduct() {
        return false;// 怪物生成自由添加，不使用地图自动刷怪
    }
    
    public String getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(String monsterId) {
        this.monsterId = monsterId;
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

    public int getAddBuffVal() {
        return addBuffVal;
    }

    public void setAddBuffVal(int addBuffVal) {
        this.addBuffVal = addBuffVal;
    }

    public int getSubBuffVal() {
        return subBuffVal;
    }

    public void setSubBuffVal(int subBuffVal) {
        this.subBuffVal = subBuffVal;
    }

    @Override
    public String getConfigName() {
        return "WuxingSkillFubenConfig" + getId();
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
}
