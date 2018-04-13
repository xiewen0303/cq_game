/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.configure;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 *@Description 天炉炼丹基础表解析
 *@Author Yang Gao
 *@Since 2016-7-13
 *@Version 1.1.0
 */
@Component
public class TianLuLianDanBiaoConfigExport extends AbsClasspathConfigureParser {

    /* 配置文件名 */
    private static final String CONFIG_GURE_NAME = "TianLuLianDanBiao.jat";
    /* 配置解析的常量 */
    private static final String[] CONFIG_CONSTANTS = {"bskuang", "gailv"};
    /* 基础数据列表 */
    private static Map<Integer, TianLuLianDanBiaoConfig> configMap = null;
    /*配置最大id值*/
    private static int MAX_CONFIG_ID = 0;

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
        TianLuLianDanBiaoConfig config = new TianLuLianDanBiaoConfig();
        int id = CovertObjectUtil.object2int(tmp.get("id"));
        config.setId(id);
        config.setNeedGold(CovertObjectUtil.object2int(tmp.get("needgold")));
        config.setGiveSolt(CovertObjectUtil.object2int(tmp.get("gewei")));
        config.setTiming(CovertObjectUtil.object2int(tmp.get("time")));
        Map<String, Integer> itemConfig = new HashMap<>();
        Set<String> keySet = tmp.keySet();
        for (String field : keySet) {
            if (field.startsWith(CONFIG_CONSTANTS[0])) {
                String goodsId = CovertObjectUtil.object2String(tmp.get(field));
                if(!ObjectUtil.strIsEmpty(goodsId)){
                    int valKey = Integer.parseInt(field.substring(CONFIG_CONSTANTS[0].length()));
                    String goodsValKey = CONFIG_CONSTANTS[1] + valKey;
                    int goodsVal = CovertObjectUtil.object2int(tmp.get(goodsValKey));
                    itemConfig.put(goodsId, goodsVal);
                }
            }
        }
        config.setItemConfig(itemConfig);
        configMap.put(id, config);
        if(id > MAX_CONFIG_ID) MAX_CONFIG_ID = id;
    }

    @Override
    protected String getConfigureName() {
        return CONFIG_GURE_NAME;
    }
    
    public TianLuLianDanBiaoConfig loadById(int id){
        return null == configMap ? null :  configMap.get(id);
    }
    
    public int getMaxConfigId(){
        return MAX_CONFIG_ID;
    }
}
