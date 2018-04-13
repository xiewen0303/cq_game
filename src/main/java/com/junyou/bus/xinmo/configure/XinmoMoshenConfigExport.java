/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsGroupFileAbleConfigureInit;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

/**
 *@Description 心魔魔神基础表解析
 *@Author Yang Gao
 *@Since 2016-7-18
 *@Version 1.1.0
 */
@Component
public class XinmoMoshenConfigExport extends AbsGroupFileAbleConfigureInit {

    private static final String[] CONFIGURE_NAMES = new String[] { "XinMoJiChuBiao.jat", "XinMoShiTiBiao.jat" };
    /** key=魔神类型;value=最大阶级 **/
    private static Map<Integer, Integer> rankMaxMap;
    /** key=魔神编号id;value=基础配置对象 **/
    private static Map<Integer, XinmoMoshenJiChuBiaoConfig> jichuConfigMap;
    /** key=魔神编号id;value=噬体配置对象 **/
    private static Map<Integer, XinmoMoshenShitiBiaoConfig> shitiConfigMap;

    @Override
    protected void configureDataResolve(byte[] data, String configName) {
        if (null == data) {
            ChuanQiLog.error("{} not fond!", configName);
            return;
        }
        // 配置文件MD5值加入管理
        ConfigMd5SignManange.addConfigSign(configName, data);
        if (CONFIGURE_NAMES[0].equals(configName)) {
            loadJichuConfig(data);
        } else if (CONFIGURE_NAMES[1].equals(configName)) {
            loadShitiConfig(data);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadShitiConfig(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            shitiConfigMap = new HashMap<>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                int id = CovertObjectUtil.object2int(tmp.get("id"));
                XinmoMoshenShitiBiaoConfig config = new XinmoMoshenShitiBiaoConfig();
                config.setId(id);
                config.setType(CovertObjectUtil.object2int(tmp.get("type")));
                config.setRank(CovertObjectUtil.object2int(tmp.get("dengjie")));
                config.setAttrMap(ConfigAnalysisUtils.setAttributeVal(tmp));
                shitiConfigMap.put(id, config);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadJichuConfig(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            rankMaxMap = new HashMap<>();
            jichuConfigMap = new HashMap<>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                int id = CovertObjectUtil.object2int(tmp.get("id"));
                int type = CovertObjectUtil.object2int(tmp.get("type"));
                int rank = CovertObjectUtil.object2int(tmp.get("dengjie"));
                XinmoMoshenJiChuBiaoConfig config = new XinmoMoshenJiChuBiaoConfig();
                config.setId(id);
                config.setType(type);
                config.setRank(rank);
                config.setNingshenRank(CovertObjectUtil.object2int(tmp.get("needrank")));
                config.setItemId(CovertObjectUtil.object2String(tmp.get("id1")));
                config.setItemCount(CovertObjectUtil.object2int(tmp.get("count")));
                config.setMoney(CovertObjectUtil.object2int(tmp.get("money")));
                config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
                config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
                config.setBlessMinVal(CovertObjectUtil.object2int(tmp.get("zfzmin")));
                config.setBlessMaxVal(CovertObjectUtil.object2int(tmp.get("zfzmax")));
                config.setSuccessRatio(CovertObjectUtil.object2int(tmp.get("pro")));
                config.setAddBlessMinVal(CovertObjectUtil.object2int(tmp.get("zfzmin2")));
                config.setAddBlessMaxVal(CovertObjectUtil.object2int(tmp.get("zfzmin3")));
                config.setReset(CovertObjectUtil.object2boolean(tmp.get("zfztime")));
                config.setResetHour(CovertObjectUtil.object2int(tmp.get("cztime")));
                config.setNoticeCode(CovertObjectUtil.object2int(tmp.get("ggopen")));
                config.setAttrMap(ConfigAnalysisUtils.setAttributeVal(tmp));
                jichuConfigMap.put(id, config);
                Integer maxRank = rankMaxMap.get(type);
                if (null == maxRank || rank > maxRank.intValue())
                    rankMaxMap.put(type, rank);
            }
        }
    }

    @Override
    protected String[] getGroupConfigureNames() {
        return CONFIGURE_NAMES;
    }

    /*根据心魔-魔神编号获取基础配置信息对象*/
    public XinmoMoshenJiChuBiaoConfig loadJichuConfigById(int id){
        return null == jichuConfigMap ? null : jichuConfigMap.get(id);
    }
    
    /*根据心魔-魔神的类型和阶级获取基础配置信息对象*/
    public XinmoMoshenJiChuBiaoConfig loadJichuConfigByTypeAndRank(int type, int rank){
        if(null == jichuConfigMap){
            return null;
        }
        for(XinmoMoshenJiChuBiaoConfig config : jichuConfigMap.values()){
            if(type == config.getType() && rank == config.getRank()){
                return config;
            }
        }
        return null;
    }

    /*根据魔神编号获取魔神噬体基础配置信息对象*/
    public XinmoMoshenShitiBiaoConfig loadShitiConfigById(int id){
        return null == shitiConfigMap ? null : shitiConfigMap.get(id);
    }
    
    /*根据心魔-魔神类型获取最大阶级*/
    public int getXinmoMoshenMaxRankByType(int type) {
        return null == rankMaxMap ? null : rankMaxMap.get(type);
    }
    
}
