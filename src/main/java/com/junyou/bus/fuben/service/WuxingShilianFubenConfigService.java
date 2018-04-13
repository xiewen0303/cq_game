/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.bus.fuben.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.WuxingShilianFubenConfig;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsGroupFileAbleConfigureInit;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * @Description 五行试炼副本基础配置解析
 * @Author Yang Gao
 * @Since 2016-6-21
 * @Version 1.1.0
 */
@Service
public class WuxingShilianFubenConfigService extends AbsGroupFileAbleConfigureInit {

    private String[] configureNames = new String[] { "WuXingShiLianBiao.jat", "WuXingShiLianJiangLiBiao.jat" };

    private static WuxingShilianFubenConfig config = new WuxingShilianFubenConfig();

    @Override
    protected void configureDataResolve(byte[] data, String configName) {
        if (null == data) {
            ChuanQiLog.error("{} not fond!", configName);
            return;
        }
        // 配置文件MD5值加入管理
        ConfigMd5SignManange.addConfigSign(configName, data);
        if (configureNames[0].equals(configName)) {
            loadWuXingShiLianBiaoConfig(data, config);
        } else if (configureNames[1].equals(configName)) {
            loadWuXingShiLianJiangLiBiaoConfig(data, config);
        }
    }

    /**
     * 加载五行试炼奖励配置
     * 
     * @param data
     */
    @SuppressWarnings("unchecked")
    private void loadWuXingShiLianJiangLiBiaoConfig(byte[] data, WuxingShilianFubenConfig config) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            Map<Integer, Map<String, Integer>> rewardMap = new HashMap<>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    Integer key = CovertObjectUtil.object2int(tmp.get("count1"));
                    String reward_str = CovertObjectUtil.object2String(tmp.get("jiangitem"));
                    Map<String, Integer> reward = ConfigAnalysisUtils.getConfigMap(reward_str);
                    rewardMap.put(key, reward);
                }
            }
            config.setRewardMap(rewardMap);
        }
    }

    /**
     * 加载五行试炼配置
     * 
     * @param data
     */
    @SuppressWarnings("unchecked")
    private void loadWuXingShiLianBiaoConfig(byte[] data, WuxingShilianFubenConfig config) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {            
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    String monsterId = CovertObjectUtil.object2String(tmp.get("monster"));
                    Integer monsterNum = CovertObjectUtil.object2int(tmp.get("num"));
                    Integer refreTime = CovertObjectUtil.object2int(tmp.get("shuaxin"));
                    Map<String, Long> attrMap = ConfigAnalysisUtils.setAttributeVal(tmp);
                    ReadOnlyMap<String, Long> wxAttrsMap = ObjectUtil.isEmpty(attrMap) ? null : new ReadOnlyMap<>(attrMap);
                    String point_str = CovertObjectUtil.object2String(tmp.get("zuobiao"));
                    if (!CovertObjectUtil.isEmpty(point_str)) {
                        String[] point_array = point_str.split(GameConstants.GOODS_CONFIG_SPLIT_CHAR);
                        if (null != point_array) {
                            for (String point : point_array) {
                                String[] xy_point = point.split(GameConstants.GOODS_CONFIG_SUB_SPLIT_CHAR);
                                if (null != xy_point) {
                                    Integer[] xyPoint = { Integer.parseInt(xy_point[0]), Integer.parseInt(xy_point[1]) };
                                    config.addMonster(monsterId, monsterNum, xyPoint, refreTime, wxAttrsMap);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected String[] getGroupConfigureNames() {
        return configureNames;
    }
    
    public WuxingShilianFubenConfig loadByConfig(){
        return config;
    }

    
}

