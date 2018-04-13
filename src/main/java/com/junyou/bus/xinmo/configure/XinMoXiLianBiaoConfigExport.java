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
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.lottery.Lottery;

/**
 * @Description 心魔洗练基础表解析
 * @Author Yang Gao
 * @Since 2016-8-15
 * @Version 1.1.0
 */
@Component
public class XinMoXiLianBiaoConfigExport extends AbsClasspathConfigureParser {

    /* 配置文件名 */
    private static final String CONFIG_GURE_NAME = "XinMoXiLianBiao.jat";
    /* 配置解析的常量 */
    private static String[] CONFIG_CONSTANTS = null;
    /* 数据列表key=基础数据对象;value=数据随机权值 */
    private static Map<XinMoXiLianBiaoConfig, Integer> configMap = null;

    @SuppressWarnings("unchecked")
    @Override
    protected void configureDataResolve(byte[] data) {
        if (null == data) {
            ChuanQiLog.error("{} not found", getConfigureName());
            return;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            configMap = new HashMap<XinMoXiLianBiaoConfig, Integer>();
            CONFIG_CONSTANTS = new String[] { "rank", "odds", "area" };
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
        int typeOdds = CovertObjectUtil.object2int(tmp.get("typeodds"));
        XinMoXiLianBiaoConfig config = new XinMoXiLianBiaoConfig();
        config.setId(CovertObjectUtil.object2int(tmp.get("id")));
        config.setAttrType(CovertObjectUtil.object2String(tmp.get("type")));
        config.setTypeOdds(typeOdds);
        Map<Object[], Integer> infoMap = new HashMap<>();
        Set<String> keySet = tmp.keySet();
        for (String field : keySet) {
            if (field.startsWith(CONFIG_CONSTANTS[0])) {
                int rank = Integer.parseInt(field.substring(CONFIG_CONSTANTS[0].length()));
                int rank_odds = CovertObjectUtil.object2int(tmp.get(CONFIG_CONSTANTS[1] + rank));
                String attr_val_str = CovertObjectUtil.object2String(tmp.get(CONFIG_CONSTANTS[2] + rank));
                String[] attr_val_array = attr_val_str.split(GameConstants.CONFIG_SPLIT_CHAR);
                /* 属性信息(key=[0=属性品质类型,1=属性值随机最小值,2=属性值随机最大值],value=属性值品质随机权值) */
                infoMap.put(new Object[] { rank, Integer.parseInt(attr_val_array[0]), Integer.parseInt(attr_val_array[1]) }, rank_odds);
            }
        }
        config.setInfoMap(infoMap);
        configMap.put(config, typeOdds);
    }

    @Override
    protected String getConfigureName() {
        return CONFIG_GURE_NAME;
    }

    public boolean configNotExist(){
        return configMap == null;
    }
    
    public Integer createRandomConfigId(){
        return configMap == null ? null : Lottery.getRandomKeyByInteger(configMap).getId();
    }
    
    public XinMoXiLianBiaoConfig getConfigById(Integer id){
        if(id == null || configMap == null){
            return null;
        }
        for(XinMoXiLianBiaoConfig config : configMap.keySet()){
            if(id.equals(config.getId())){
                return config;
            }
        }
        return null;
    }
    

}
