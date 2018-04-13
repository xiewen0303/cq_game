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
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 
 * @Description 云瑶晶脉配置解析
 * @Author Yang Gao
 * @Since 2016-11-1
 * @Version 1.1.0
 */
@Component
public class YunYaoJingMaiConfigExportService extends AbsClasspathConfigureParser {

    /**
     * 配置文件名
     */
    private final String CONFIG_NAME = "YunYaoJingMaiBiao.jat";
    /**
     * 解析字段前缀常量
     */
    private final String[] FIELD_PRE_STR = { "item", "gailv" };
    /**
     * 配置数据
     */
    private Map<Integer, YunYaoJingMaiConfig> configMap;

    @Override
    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if (null == data) {
            ChuanQiLog.error("{} config file not found!!!", getConfigureName());
            return;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            configMap = new HashMap<Integer, YunYaoJingMaiConfig>();
            for (Object obj : dataList) {
                createYunYaoJingMaiConfig((Map<String, Object>) obj);
            }
        }
    }

    /**
     * 创建生成配置对象数据
     * 
     * @param obj
     */
    private void createYunYaoJingMaiConfig(Map<String, Object> tmp) {
        YunYaoJingMaiConfig config = new YunYaoJingMaiConfig();
        config.setId(CovertObjectUtil.object2int(tmp.get("id")));
        config.setCd(CovertObjectUtil.object2int(tmp.get("cd")));
        config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
        Map<String, Integer> itemMap = new HashMap<String, Integer>();
        for (String field : tmp.keySet()) {
            if (field.startsWith(FIELD_PRE_STR[0])) {
                String goodsId = CovertObjectUtil.object2String(tmp.get(field));
                if (!ObjectUtil.strIsEmpty(goodsId)) {
                    String idx = field.substring(FIELD_PRE_STR[0].length());
                    itemMap.put(goodsId, CovertObjectUtil.object2int(tmp.get(FIELD_PRE_STR[1] + idx)));
                }
            }
        }
        config.setItemMap(itemMap);
        List<Integer[]> zuobiao = new ArrayList<>();
        String zuobiaoStr = CovertObjectUtil.obj2StrOrNull(tmp.get("zuobiao"));
        if (!ObjectUtil.strIsEmpty(zuobiaoStr)) {
            String[] zuobiaoArr1 = zuobiaoStr.split(GameConstants.CONFIG_SPLIT_CHAR);
            if (null != zuobiaoArr1) {
                for (String zuobiaoStr2 : zuobiaoArr1) {
                    String[] zuobiaoArr2 = zuobiaoStr2.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
                    if (null != zuobiaoArr2) {
                        zuobiao.add(new Integer[] { Integer.parseInt(zuobiaoArr2[0]), Integer.parseInt(zuobiaoArr2[1]) });
                    }
                }
            }
        }
        config.setZuobiao(zuobiao);
        configMap.put(config.getId(), config);
    }

    @Override
    protected String getConfigureName() {
        return CONFIG_NAME;
    }

    public YunYaoJingMaiConfig loadById(Integer id) {
        return null == configMap ? null : configMap.get(id);
    }

    public List<YunYaoJingMaiConfig> loadAllConfig() {
        return null == configMap ? null : new ArrayList<>(configMap.values());
    }
}
