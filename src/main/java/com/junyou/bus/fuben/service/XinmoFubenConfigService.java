/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.bus.fuben.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.XinmoFubenConfig;
import com.junyou.bus.fuben.entity.XinmoFubenFuHuaConfig;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsGroupFileAbleConfigureInit;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * @Description 心魔副本基础配置解析
 * @Author Yang Gao
 * @Since 2016-7-26
 * @Version 1.1.0
 */
@Service
public class XinmoFubenConfigService extends AbsGroupFileAbleConfigureInit {
    private static String[] CONFIGURE_NAMES = { "TiaoZhanXinMoBiao.jat", "FuHuaJianShaoBiao.jat" };

    /* 心魔副本配置集合 */
    private static Map<Integer, XinmoFubenConfig> fubenConfigs;
    /* 心魔副本腐化度配置集合 */
    private static Map<Integer, XinmoFubenFuHuaConfig> fuhuaConfigs;

    @Override
    protected void configureDataResolve(byte[] data, String configName) {
        if (null == data) {
            ChuanQiLog.error("{} not fond!", configName);
            return;
        }
        // 配置文件MD5值加入管理
        ConfigMd5SignManange.addConfigSign(configName, data);
        if (CONFIGURE_NAMES[0].equals(configName)) {
            createWuxingFubenConfig(data);
        } else if (CONFIGURE_NAMES[1].equals(configName)) {
            createWuxingFubenFuhuaConfig(data);
        }
    }

    @SuppressWarnings("unchecked")
    private void createWuxingFubenConfig(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            fubenConfigs = new HashMap<>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    XinmoFubenConfig config = new XinmoFubenConfig();
                    config.setId(CovertObjectUtil.object2int(tmp.get("id")));
                    config.setMapId(CovertObjectUtil.object2int(tmp.get("map")));
                    String monster_id_str = CovertObjectUtil.object2String(tmp.get("monster"));
                    String monster_xy_str = CovertObjectUtil.object2String(tmp.get("zuobiao"));
                    if (!ObjectUtil.strIsEmpty(monster_id_str) && !ObjectUtil.strIsEmpty(monster_xy_str)) {
                        Map<String, Integer[]> monsterMap = new HashMap<>();
                        String[] monster_id_arr = monster_id_str.split(GameConstants.CONFIG_SPLIT_CHAR);
                        String[] monster_xy_arr = monster_xy_str.split(GameConstants.CONFIG_SPLIT_CHAR);
                        for (int idx = 0; idx < monster_id_arr.length; idx++) {
                            String[] xy_str = monster_xy_arr[idx].split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
                            monsterMap.put(monster_id_arr[idx], new Integer[] { Integer.parseInt(xy_str[0]), Integer.parseInt(xy_str[1]) });
                        }
                        config.setMonsterMap(monsterMap);
                    }
                    config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
                    config.setFuhuaVal(CovertObjectUtil.object2int(tmp.get("fuhua")));
                    config.setItemMap(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("sitem"))));
//                    config.setFuhuo(true);
                    fubenConfigs.put(config.getId(), config);
                    GoodsConfigChecker.registCheck(config);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void createWuxingFubenFuhuaConfig(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            fuhuaConfigs = new HashMap<>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    XinmoFubenFuHuaConfig config = new XinmoFubenFuHuaConfig();
                    config.setXinmoRank(CovertObjectUtil.object2int(tmp.get("rank")));
                    config.setTiming(CovertObjectUtil.object2int(tmp.get("time")));
                    config.setFuhuaVal(CovertObjectUtil.object2int(tmp.get("val")));
                    fuhuaConfigs.put(config.getXinmoRank(), config);
                }
            }
        }
    }

    @Override
    protected String[] getGroupConfigureNames() {
        return CONFIGURE_NAMES;
    }

    public XinmoFubenConfig loadFubenConfigById(Integer id) {
        return fubenConfigs.get(id);
    }

    public XinmoFubenFuHuaConfig loadFuHuaConfigById(Integer rank) {
        return fuhuaConfigs.get(rank);
    }

    public Map<Integer, XinmoFubenConfig> loadAll() {
        return fubenConfigs;
    }

}
