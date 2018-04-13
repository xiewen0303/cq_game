/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.equip.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsGroupFileAbleConfigureInit;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

/**
 *@Description 宠物附属装备强化表、升阶表配置表解析
 *@Author Yang Gao
 *@Since 2016-9-6
 *@Version 1.1.0
 */
@Component
public class ChongwuFushuEquipConfigExportService extends AbsGroupFileAbleConfigureInit {
    
    private static final String[] CONFIGURE_NAMES = new String[] { "ChongWuZhuangBeiQiangHuaBiao.jat", "ChongWuZhuangBeiShengJie.jat" };
    /* 宠物附属装备最大强化等级 */
    private static int max_qianghua_level;
    /* 宠物附属装备强化表数据集合 */
    private static Map<Integer,ChongwuFushuEquipQianghuaConfig> qianghua_configs = null;
    /* 宠物附属装备升阶表数据集合 */
    private static Map<Integer,ChongwuFushuEquipShengjiConfig> shengjie_configs = null;
    
    @Override
    protected void configureDataResolve(byte[] data, String configName) {
        if (null == data) {
            ChuanQiLog.error("{} not fond!", configName);
            return;
        }
        // 配置文件MD5值加入管理
        ConfigMd5SignManange.addConfigSign(configName, data);
        if (CONFIGURE_NAMES[0].equals(configName)) {
            loadEquipQianghuaConfig(data);
        } else if (CONFIGURE_NAMES[1].equals(configName)) {
            loadEquipShengjiConfig(data);
        }
    }
    
    /**
     * 加载附属装备升阶表数据
     * @param data
     */
    @SuppressWarnings("unchecked")
    private void loadEquipShengjiConfig(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            shengjie_configs = new HashMap<Integer,ChongwuFushuEquipShengjiConfig>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if(null != tmp){
                    ChongwuFushuEquipShengjiConfig config = new ChongwuFushuEquipShengjiConfig();
                    config.setLevel(CovertObjectUtil.object2int(tmp.get("zblevel"))); 
                    config.setItemCount(CovertObjectUtil.object2int(tmp.get("num")));
                    config.setItemId1(CovertObjectUtil.obj2StrOrNull(tmp.get("prop")));
                    config.setMoney(CovertObjectUtil.object2int(tmp.get("money")));
                    config.setSuccess(CovertObjectUtil.object2int(tmp.get("successrate")));
                    shengjie_configs.put(config.getLevel(), config);
                }
            }
        }
    }

    /**
     * 加载附属装备强化表数据
     * @param data
     */
    @SuppressWarnings("unchecked")
    private void loadEquipQianghuaConfig(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            qianghua_configs = new HashMap<Integer,ChongwuFushuEquipQianghuaConfig>(); 
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (null != tmp) {
                    ChongwuFushuEquipQianghuaConfig config=new ChongwuFushuEquipQianghuaConfig();
                    config.setLevel(CovertObjectUtil.object2int(tmp.get("level"))); 
                    config.setNeedItemCount(CovertObjectUtil.object2int(tmp.get("num")));
                    config.setNeedItemId(CovertObjectUtil.object2String(tmp.get("prop")));
                    config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("money")));
                    config.setQhxs(CovertObjectUtil.object2Float(tmp.get("qhxs")));
                    config.setSuccessrate(10000);//必定成功
                    config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
                    config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
                    qianghua_configs.put(config.getLevel(), config);
                    if(config.getLevel() >  max_qianghua_level) max_qianghua_level = config.getLevel();
                }
            }
        }
    }

    @Override
    protected String[] getGroupConfigureNames() {
        return CONFIGURE_NAMES;
    }

    public ChongwuFushuEquipQianghuaConfig loadQianghuaConfig(Integer qhLevel) {
        return null == qianghua_configs ? null : qianghua_configs.get(qhLevel);

    }
    
    public int getChongwuFushuEquipMaxQianghuaLevel(){
        return max_qianghua_level;
    }

    public ChongwuFushuEquipShengjiConfig loadShengjieConfig(Integer rank) {
        return null == shengjie_configs ? null : shengjie_configs.get(rank);
    }

    public Float getQhxs(int qhLevel) {
        ChongwuFushuEquipQianghuaConfig config = loadQianghuaConfig(qhLevel);
        if (null == config)
            return null;
        return config.getQhxs();
    }
}
