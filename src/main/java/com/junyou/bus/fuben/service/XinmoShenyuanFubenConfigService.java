/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.bus.fuben.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.XinmoShenyuanFubenConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @Description 心魔深渊副本基础配置解析
 * @Author Yang Gao
 * @Since 2016-8-10
 * @Version 1.1.0
 */
@Service
public class XinmoShenyuanFubenConfigService extends AbsClasspathConfigureParser {

    private static String CONFIGURE_NAME = "ShenYuanFuBenBiao.jat";

    /* 心魔深渊副本配置集合 */
    private static Map<Integer, XinmoShenyuanFubenConfig> fubenConfigs;
    /* 心魔深渊副本最大难度关卡编号 */
    private static Integer maxFubenId;

    @SuppressWarnings("unchecked")
    @Override
    protected void configureDataResolve(byte[] data) {
        if (null == data) {
            ChuanQiLog.error("{} not fond!", getConfigureName());
            return;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        fubenConfigs = new HashMap<Integer, XinmoShenyuanFubenConfig>();
        for (Object dataObj : dataList) {
            Map<String, Object> temp = (Map<String, Object>) dataObj;
            if (null != temp) {
                createXinmoShenyuanFubenConfig(temp);
            }
        }
    }

    /**
     * 
     * @param temp
     */
    private void createXinmoShenyuanFubenConfig(Map<String, Object> temp) {
        XinmoShenyuanFubenConfig config = new XinmoShenyuanFubenConfig();
        int id = CovertObjectUtil.object2int(temp.get("ID"));
        config.setId(id);

        config.setMapId(CovertObjectUtil.object2int(temp.get("map")));

        String[] xyPoint_str = CovertObjectUtil.object2String(temp.get("zuobiao")).split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
        config.setXyPoint(new Integer[] { Integer.parseInt(xyPoint_str[0]), Integer.parseInt(xyPoint_str[1]) });

        Map<Integer, String> monsterMap = new HashMap<Integer, String>();
        String[] monster_strArr = CovertObjectUtil.object2String(temp.get("monster")).split(GameConstants.CONFIG_SPLIT_CHAR);
        for (String monster_str : monster_strArr) {
            String[] monster_arr = monster_str.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
            monsterMap.put(Integer.parseInt(monster_arr[0]), monster_arr[1]);
        }
        config.setMonsterMap(monsterMap);

        config.setTime(CovertObjectUtil.object2int(temp.get("time")));

        config.setMultiplier(CovertObjectUtil.obj2float(temp.get("multiplier")));

        config.setExp(CovertObjectUtil.obj2long(temp.get("jiangexp")));

        config.setZq(CovertObjectUtil.obj2long(temp.get("jiangzhenqi")));

        config.setMoney(CovertObjectUtil.obj2long(temp.get("jiangmoney")));

        config.setItemMap(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(temp.get("jiangitem"))));

//        config.setFuhuo(true);
        fubenConfigs.put(id, config);
        if (null == maxFubenId || id > maxFubenId.intValue()) {
            maxFubenId = id;
        }
    }

    @Override
    protected String getConfigureName() {
        return CONFIGURE_NAME;
    }

    public XinmoShenyuanFubenConfig loadById(Integer id) {
        return null == fubenConfigs ? null : fubenConfigs.get(id);
    }

    public int getMaxFubenId() {
        return maxFubenId == null ? 0 : maxFubenId.intValue();
    }

}
