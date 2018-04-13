/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.bus.fuben.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.XinmoDouchangFubenConfig;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsGroupFileAbleConfigureInit;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 
 *@Description 心魔斗场副本配置解析
 *@Author Yang Gao
 *@Since 2016-8-23
 *@Version 1.1.0
 */
@Service
public class XinmoDouchangFubenConfigService extends AbsGroupFileAbleConfigureInit {

    private String[] configureNames = new String[] { "XinMoDouChangBiao.jat", "DouChangJiangLiBiao.jat" };

    private static XinmoDouchangFubenConfig config = null;

    @Override
    protected void configureDataResolve(byte[] data, String configName) {
        if (null == data) {
            ChuanQiLog.error("{} not fond!", configName);
            return;
        }
        // 配置文件MD5值加入管理
        ConfigMd5SignManange.addConfigSign(configName, data);
        if(null == config) config = new XinmoDouchangFubenConfig();
        if (configureNames[0].equals(configName)) {
            loadXinMoDouChangBiaoConfig(data);
        } else if (configureNames[1].equals(configName)) {
            loadDouChangJiangLiBiaoConfig(data);
        }
    }

    /**
     * 加载心魔斗场副本奖励配置
     * @param data
     * @param config2
     */
    @SuppressWarnings("unchecked")
    private void loadDouChangJiangLiBiaoConfig(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {            
            Map<Integer, Object[]> rewardMap = new HashMap<Integer, Object[]>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    int killNum = CovertObjectUtil.object2int(tmp.get("num"));
                    long exp = CovertObjectUtil.obj2long(tmp.get("jiangexp"));
                    long money = CovertObjectUtil.obj2long(tmp.get("jiangmoney"));
                    long zq = CovertObjectUtil.obj2long(tmp.get("jiangzhenqi"));
                    Map<String, Integer> reward = ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("jiangitem")));
                    rewardMap.put(killNum, new Object[]{exp, money, zq, reward});
                }
            }
            config.setRewardMap(rewardMap);
        }
    }

    /**
     * 加载心魔斗场副本基本配置
     * @param data
     * @param config2
     */
    @SuppressWarnings("unchecked")
    private void loadXinMoDouChangBiaoConfig(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            List<Object[]> monsterList = new ArrayList<Object[]>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    String monsterId = CovertObjectUtil.object2String(tmp.get("monster"));
                    int monsterType = CovertObjectUtil.object2int(tmp.get("monstertype"));
                    String buffId = CovertObjectUtil.object2String(tmp.get("buffid"));
                    String point_str = CovertObjectUtil.object2String(tmp.get("zuobiao"));
                    Integer[] point = null;
                    if (!CovertObjectUtil.isEmpty(point_str)) {
                        String[] point_array = point_str.split(GameConstants.GOODS_CONFIG_SPLIT_CHAR);
                        point = new Integer[]{Integer.parseInt(point_array[0]),Integer.parseInt(point_array[1])};
                    }
                    monsterList.add(new Object[]{monsterId, point, monsterType, buffId});
                }
            }
            config.setMonsterList(monsterList);
        }
    }


    @Override
    protected String[] getGroupConfigureNames() {
        return configureNames;
    }
    
    public XinmoDouchangFubenConfig loadByConfig(){
        return config;
    }

    
}

