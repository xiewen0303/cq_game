/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xianqi.configure;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 *@Description  云遥仙洞配置解析
 *@Author Yang Gao
 *@Since 2016-10-25
 *@Version 1.1.0
 */
@Component
public class YunYaoXianDongConfigExportService extends AbsClasspathConfigureParser {

    private final String CONFIG_NAME = "YunYaoXianDongBiao.jat";
    
    private static int MAX_LEVEL;
    
    private Map<Integer, YunYaoXianDongConfig> configMap;
    
    @Override
    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if(null == data){
            ChuanQiLog.error("{} config file not found!!!", getConfigureName());
            return;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if(null != dataList){
            configMap = new HashMap<Integer, YunYaoXianDongConfig>();
            for(Object obj : dataList){
                createYunYaoXianDongConfig((Map<String, Object>) obj);
            }
        }
    }

    /**
     * 创建生成配置对象数据
     * @param obj
     */
    private void createYunYaoXianDongConfig(Map<String, Object> tmp) {
        YunYaoXianDongConfig config = new YunYaoXianDongConfig();
        config.setLv(CovertObjectUtil.object2int(tmp.get("lv")));
        config.setNeedexp(CovertObjectUtil.obj2long(tmp.get("needexp")));
        String item_str = CovertObjectUtil.obj2StrOrNull(tmp.get("needitem"));
        if(!ObjectUtil.strIsEmpty(item_str)){
            String[] item_arr = item_str.split(GameConstants.CONFIG_SPLIT_CHAR);
            if(null != item_arr){
                config.setNeeditem(Arrays.asList(item_arr));
            }
        }
        config.setAttrMap(ConfigAnalysisUtils.setAttributeVal(tmp));
        configMap.put(config.getLv(), config);
        if(config.getLv() > MAX_LEVEL){
            MAX_LEVEL = config.getLv();
        }
    }

    @Override
    protected String getConfigureName() {
        return CONFIG_NAME;
    }

    public YunYaoXianDongConfig loadByLvl(Integer lvl){
        return null == configMap ? null : configMap.get(lvl);
    }
    
    public int getMaxLevel(){
        return MAX_LEVEL;
    }
}
