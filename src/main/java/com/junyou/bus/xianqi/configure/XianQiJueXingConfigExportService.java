/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xianqi.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @Description 仙器觉醒配置解析
 * @Author Yang Gao
 * @Since 2016-10-25
 * @Version 1.1.0
 */
@Component
public class XianQiJueXingConfigExportService extends AbsClasspathConfigureParser {

    private final String CONFIG_NAME = "XianQiJueXingBiao.jat";

    private Map<String, XianQiJueXingConfig> configMap;
    /**
     * key=仙器类型
     * value=最大等级
     */
    private Map<Integer, Integer> maxLevelMap;

    @Override
    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if (null == data) {
            ChuanQiLog.error("{} config file not found!!!", getConfigureName());
            return;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            configMap = new HashMap<String, XianQiJueXingConfig>();
            maxLevelMap = new HashMap<Integer, Integer>();
            for (Object obj : dataList) {
                createXianQiJueXingConfig((Map<String, Object>) obj);
            }
        }
    }

    /**
     * 创建生成配置对象数据
     * 
     * @param obj
     */
    private void createXianQiJueXingConfig(Map<String, Object> tmp) {
        XianQiJueXingConfig config = new XianQiJueXingConfig();
        config.setId(CovertObjectUtil.object2int(tmp.get("id")));
        config.setType(CovertObjectUtil.object2int(tmp.get("type")));
        config.setLevel(CovertObjectUtil.object2int(tmp.get("lv")));
        List<String> itemIdList = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : tmp.entrySet()) {
            if (entry.getKey().startsWith("needitem")) {
                itemIdList.add(CovertObjectUtil.obj2StrOrNull(entry.getValue()));
            }
        }
        config.setItemIdList(itemIdList);
        config.setAttrMap(ConfigAnalysisUtils.setAttributeVal(tmp));
        configMap.put(getConfigKey(config.getType(), config.getLevel()), config);
        if(maxLevelMap.get(config.getType()) == null || config.getLevel() > maxLevelMap.get(config.getType())){
            maxLevelMap.put(config.getType(), config.getLevel());
        }
    }

    @Override
    protected String getConfigureName() {
        return CONFIG_NAME;
    }

    /**
     * 获取配置map键值
     * @param type
     * @param lvl
     * @return
     */
    private String getConfigKey(Integer type, Integer lvl) {
        return "type=" + type + "_" + "level=" + lvl;
    }

    /**
     * 根据类型和等级获取仙器觉醒配置数据
     * 
     * @param type
     * @param lvl
     * @return
     */
    public XianQiJueXingConfig loadByTypeAndLvl(Integer type, Integer lvl) {
        return null == configMap ? null : configMap.get(getConfigKey(type, lvl));
    }

    /**
     * 根据仙器类型获取最大等级
     * 
     * @param type
     * @return
     */
    public int getMaxByType(Integer type){
        return null == maxLevelMap ? 0 : maxLevelMap.get(type);
    }
}
