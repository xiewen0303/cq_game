/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.wuxing.configure.export.jingpo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsGroupFileAbleConfigureInit;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * @Description 魔神精魄配置文件组解析
 * @Author Yang Gao
 * @Since 2016-5-10
 * @Version 1.1.0
 */
@Component
public class MoshenJingpoConfigExportService extends AbsGroupFileAbleConfigureInit {

    private static final String PREFIX_PRO = "pro_";
    /** 精魄基础配置集合 **/
    private Map<Integer, MoshenJingpoConfig> jpConfigMap;
    /** 精魄兑换配置集合 **/
    private Map<Integer, MoshenJingpoDuiHuanConfig> jpDuiHuanConfigMap;
    /** 精魄背包孔位配置集合 **/
    private Map<Integer, MoshenJingpoKongWeiConfig> jpBagKWConfigMap;
    /** 魔神身上孔位配置集合 **/
    private Map<Integer, MoshenJingpoKongWeiConfig> jpBodyKWConfigMap;
    /** 精魄刷新配置集合 **/
    private Map<Integer, MoshenJingpoShuaXinConfig> jpShuaXinConfigMap;
    /** 精魄掉落配置集合 **/
    private Map<Integer, MoshenJingpoDiaoLuoConfig> jpDiaoLuoConfigMap;

    private String[] configureNames = new String[] { 
            "MoShenJingPoBiao.jat", 
            "JingPoDuiHuanBiao.jat", 
            "JingPoKongWeiBiao.jat", 
            "JingPoShuaXinBiao.jat", 
            "JingPoDiaoLuoBiao.jat" 
            };

    @Override
    protected void configureDataResolve(byte[] data, String configName) {
        if(null == data){
            ChuanQiLog.error("{} not found", getConfigureName());
            return ;
        }
        // 配置文件MD5值加入管理
        ConfigMd5SignManange.addConfigSign(configName, data);

        if (configureNames[0].equals(configName)) {
            loadMoshenJingpo(data);
        } else if (configureNames[1].equals(configName)) {
            loadJingpoDuiHuan(data);
        } else if (configureNames[2].equals(configName)) {
            loadJingpoKongWei(data);
        } else if (configureNames[3].equals(configName)) {
            loadJingpoShuaXin(data);
        } else if (configureNames[4].equals(configName)) {
            loadJingpoDiaoLuo(data);
        }

    }

    /**
     * @Description 解析魔神精魄掉落的配置
     * @param data
     */
    @SuppressWarnings("unchecked")
    private void loadJingpoDiaoLuo(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            jpDiaoLuoConfigMap = new HashMap<>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    int idKey = CovertObjectUtil.object2int(tmp.get("level"));
                    MoshenJingpoDiaoLuoConfig jpDiaoLuoConfig = new MoshenJingpoDiaoLuoConfig();
                    jpDiaoLuoConfig.setId(idKey);
                    jpDiaoLuoConfig.setJinghua(CovertObjectUtil.object2int(tmp.get("jinghua")));
                    jpDiaoLuoConfig.setNeedMoney(CovertObjectUtil.obj2long(tmp.get("needmoney")));
                    Map<Integer, Integer> proMap = new HashMap<>();
                    for (String field : tmp.keySet()) {
                        if (field.startsWith(PREFIX_PRO)) {
                            int proVal = CovertObjectUtil.object2int(tmp.get(field));
                            if(proVal > 0){
                                proMap.put(Integer.parseInt(field.substring(PREFIX_PRO.length())), proVal);
                            }
                        }
                    }
                    jpDiaoLuoConfig.setProMap(proMap);
                    jpDiaoLuoConfigMap.put(idKey, jpDiaoLuoConfig);

                }
            }
        }
    }

    /**
     * @Description 解析魔神精魄刷新配置
     * @param data
     */
    @SuppressWarnings("unchecked")
    private void loadJingpoShuaXin(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            jpShuaXinConfigMap = new HashMap<>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    int idKey = CovertObjectUtil.object2int(tmp.get("id"));
                    MoshenJingpoShuaXinConfig jpShuaXinConfig = new MoshenJingpoShuaXinConfig();
                    jpShuaXinConfig.setId(idKey);
                    Map<Integer, Integer> proMap = new HashMap<>();
                    for (String field : tmp.keySet()) {
                        int proVal = CovertObjectUtil.object2int(tmp.get(field));
                        if (0 < proVal && field.startsWith(PREFIX_PRO)) {
                            proMap.put(Integer.parseInt(field.substring(PREFIX_PRO.length())), proVal);
                        }
                    }
                    jpShuaXinConfig.setProMap(proMap);
                    jpShuaXinConfigMap.put(idKey, jpShuaXinConfig);

                }
            }
        }
    }

    /**
     * @Description 解析魔神精魄孔位配置
     * @param data
     */
    @SuppressWarnings("unchecked")
    private void loadJingpoKongWei(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            jpBagKWConfigMap = new HashMap<>();
            jpBodyKWConfigMap = new HashMap<>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    int type = CovertObjectUtil.object2int(tmp.get("kongtype"));
                    int kong = CovertObjectUtil.object2int(tmp.get("kong"));
                    
                    MoshenJingpoKongWeiConfig jpKongWeiConfig = new MoshenJingpoKongWeiConfig();
                    jpKongWeiConfig.setType(type);
                    jpKongWeiConfig.setKong(kong);
                    jpKongWeiConfig.setNeedType(CovertObjectUtil.object2int(tmp.get("needtype")));
                    jpKongWeiConfig.setNeedCount(CovertObjectUtil.object2int(tmp.get("needcount")));
                    if(GameConstants.WX_JP_SLOT_OPEN_LEVEL == type){
                        jpBodyKWConfigMap.put(kong, jpKongWeiConfig);
                    }
                    else if(GameConstants.WX_JP_SLOT_OPEN_GOLD == type){
                        jpBagKWConfigMap.put(kong, jpKongWeiConfig);
                    }
                }
            }
        }
    }

    /**
     * @Description 解析魔神精魄兑换配置
     * @param data
     */
    @SuppressWarnings("unchecked")
    private void loadJingpoDuiHuan(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            jpDuiHuanConfigMap = new HashMap<>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    MoshenJingpoDuiHuanConfig jpDuiHuanConfig = new MoshenJingpoDuiHuanConfig();
                    int idKey = CovertObjectUtil.object2int(tmp.get("id"));
                    jpDuiHuanConfig.setId(idKey);
                    jpDuiHuanConfig.setOrder(CovertObjectUtil.object2int(tmp.get("order")));
                    jpDuiHuanConfig.setNeedjinghua(CovertObjectUtil.object2int(tmp.get("needjinghua")));
                    jpDuiHuanConfigMap.put(idKey, jpDuiHuanConfig);
                }
            }
        }
    }

    /**
     * @Description 解析魔神精魄基础配置
     * @param data
     */
    @SuppressWarnings("unchecked")
    private void loadMoshenJingpo(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            jpConfigMap = new HashMap<>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    int idKey = CovertObjectUtil.object2int(tmp.get("id"));
                    MoshenJingpoConfig msjpConfig = new MoshenJingpoConfig();
                    msjpConfig.setId(idKey);
                    msjpConfig.setType(CovertObjectUtil.object2int(tmp.get("type")));
                    msjpConfig.setAttrType(CovertObjectUtil.object2int(tmp.get("attrtype")));
                    msjpConfig.setQuality(CovertObjectUtil.object2int(tmp.get("rarelevel")));
                    msjpConfig.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
                    msjpConfig.setAttr(ConfigAnalysisUtils.setAttributeVal(tmp));
                    msjpConfig.setExp(CovertObjectUtil.object2int(tmp.get("exp")));
                    msjpConfig.setNeedExp(CovertObjectUtil.object2int(tmp.get("needexp")));
                    jpConfigMap.put(idKey, msjpConfig);
                }
            }
        }
    }

    @Override
    protected String[] getGroupConfigureNames() {
        return configureNames;
    }
    
    public MoshenJingpoConfig loadMsJpById(int id){
        return jpConfigMap.get(id);
    }
    
    /**
     * @Description 根据品质和等级获取精魄数据
     * @param quality
     * @param level
     * @return
     */
    public MoshenJingpoConfig getWxJpByQualityAndLevel(int quality, int level, int attrType) {
        for (MoshenJingpoConfig jpConfig : jpConfigMap.values()) {
            if (quality == jpConfig.getQuality() && level == jpConfig.getLevel() && attrType == jpConfig.getAttrType()) {
                return jpConfig;
            }
        }
        return null;
    }
    
    public Map<Integer, MoshenJingpoConfig> getWxJpConfigMap(){
        return jpConfigMap;
    }
    
    public MoshenJingpoKongWeiConfig loadMsJpBodyKwBySlot(int slot){
        return jpBodyKWConfigMap.get(slot);
    }
    
    public Map<Integer, MoshenJingpoKongWeiConfig> getWxJpBagKwMap(){
        return jpBagKWConfigMap;
    }

    public Map<Integer, MoshenJingpoKongWeiConfig> getWxJpBodyKwMap(){
        return jpBodyKWConfigMap;
    }
    
    public MoshenJingpoDiaoLuoConfig loadMsJpDialLuoByLevel(int level){
        return jpDiaoLuoConfigMap.get(level);
    }
    
    public MoshenJingpoShuaXinConfig loadMsJpShuaXinByLevel(int level){
        return jpShuaXinConfigMap.get(level);
    }
    
    public MoshenJingpoDuiHuanConfig loadMsJpDuiHuanById(int id){
        return jpDuiHuanConfigMap.get(id);
    }
}
