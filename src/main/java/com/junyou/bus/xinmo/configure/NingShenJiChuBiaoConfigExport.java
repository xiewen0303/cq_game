/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * @Description 凝神培元基础表解析
 * @Author Yang Gao
 * @Since 2016-6-27
 * @Version 1.1.0
 */
@Component
public class NingShenJiChuBiaoConfigExport extends AbsClasspathConfigureParser {

    /* 配置文件名 */
    private static final String CONFIG_GURE_NAME = "NingShenJiChuBiao.jat";
    /* 基础数据列表 */
    private static Map<Integer, NingShenJiChuBiaoConfig> configMap = null;
    /* 基础数据边界值key=境界阶段;value={key:境界时期;value=每一个时期最大等级} */
    private static Map<Integer, Map<Integer, Integer>> boundsMaxValueMap = null;

    @SuppressWarnings("unchecked")
    @Override
    protected void configureDataResolve(byte[] data) {
        if (null == data) {
            ChuanQiLog.error("{} not found", getConfigureName());
            return;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            configMap = new HashMap<>();
            boundsMaxValueMap = new HashMap<>();
            for (Object obj : dataList)
                createConfig((Map<String, Object>) obj);
        }
    }

    /**
     * 解析配置
     * 
     * @param tmp
     */
    private void createConfig(Map<String, Object> tmp) {
        if (ObjectUtil.isEmpty(tmp)) {
            return;
        }
        int id = CovertObjectUtil.object2int(tmp.get("id"));
        int category = CovertObjectUtil.object2int(tmp.get("rank1"));
        int type = CovertObjectUtil.object2int(tmp.get("rank2"));
        int level = CovertObjectUtil.object2int(tmp.get("lv"));
        NingShenJiChuBiaoConfig config = new NingShenJiChuBiaoConfig();
        config.setId(id);
        config.setCategory(category);
        config.setType(type);
        config.setLevel(level);
        config.setMoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
        config.setProp(CovertObjectUtil.object2String(tmp.get("mallid")));
        config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
        config.setNeedType(CovertObjectUtil.object2int(tmp.get("valuetype")));
        config.setNeedVal(CovertObjectUtil.object2int(tmp.get("needvalue")));
        config.setAddVal(CovertObjectUtil.object2int(tmp.get("exp")));
        config.setGgCode(CovertObjectUtil.object2int(tmp.get("ggopen")));
        config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
        config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
        config.setXmAttrsMap(ConfigAnalysisUtils.setAttributeVal(tmp));
        configMap.put(id, config);

        Map<Integer, Integer> typeValueMap = boundsMaxValueMap.get(category);
        if (null == typeValueMap)
            typeValueMap = new HashMap<>();
        Integer maxLevel = typeValueMap.get(type);
        if (null == maxLevel || level > maxLevel.intValue()) {
            typeValueMap.put(type, level);
            boundsMaxValueMap.put(category, typeValueMap);
        }
    }

    @Override
    protected String getConfigureName() {
        return CONFIG_GURE_NAME;
    }

    /**
     * @Description 根据大类id获取物品id列表
     * @param id1
     * @return
     */
    public List<String> getConsumeIds(String id1) {
        return new ArrayList<>(BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(id1));
    }

    /**
     * 根据id获取配置对象
     * 
     * @param id
     * @return
     */
    public NingShenJiChuBiaoConfig loadById(int id) {
        return configMap.get(id);
    }

    /**
     * 根据境界阶段,时期,等级获取配置对象
     * 
     * @param category 阶段
     * @param type 时期
     * @param level 等级
     * @return
     */
    public NingShenJiChuBiaoConfig getConfig(int category, int type, int level) {
        NingShenJiChuBiaoConfig config = null;
        for (NingShenJiChuBiaoConfig tmp : configMap.values()) {
            if (tmp.getCategory() == category && tmp.getType() == type && tmp.getLevel() == level) {
                config = tmp;
                break;
            }
        }
        return config;
    }

    /**
     * 获取最大境界阶段
     * 
     * @return
     */
    public int getMaxCateGory() {
        return null == boundsMaxValueMap ? 0 : boundsMaxValueMap.size();
    }

    /**
     * 根据境界阶段获取最大境界时期
     * 
     * @param category
     * @return
     */
    public int getMaxTypeByCategory(int category) {
        if (null == boundsMaxValueMap) {
            return 0;
        } else {
            Map<Integer, Integer> typeValueMap = boundsMaxValueMap.get(category);
            return null == typeValueMap ? 0 : typeValueMap.size();
        }
    }

    /**
     * 根据境界阶段和时期获取最大等级
     * 
     * @param category
     * @param type
     * @return
     */
    public int getMaxLevelByCategoryAndType(int category, int type) {
        if (null == boundsMaxValueMap) {
            return 0;
        } else {
            Map<Integer, Integer> typeValueMap = boundsMaxValueMap.get(category);
            if (null == typeValueMap) {
                return 0;
            } else {
                Integer maxTypeValue = typeValueMap.get(type);
                return null == maxTypeValue ? 0 : maxTypeValue.intValue();
            }
        }
    }

    /**
     * 判断是否到达心魔最后一层 
     * @param id
     * @return
     */
    public boolean isXinmoLast(int id){
        NingShenJiChuBiaoConfig config = loadById(id);
        if (null == config) {
            return false;
        }
        int category = config.getCategory();
        int type = config.getType();
        int level = config.getLevel();
        return (category >= getMaxCateGory() && type >= getMaxTypeByCategory(category) && level >= getMaxLevelByCategoryAndType(category, type));
    }
    
}
