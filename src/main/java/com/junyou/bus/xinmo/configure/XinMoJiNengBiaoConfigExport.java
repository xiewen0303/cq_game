/**
 *@Copyright:Copyright (c) 2008 - 2100
 *@Company:JunYou
 */
package com.junyou.bus.xinmo.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * @Description 心魔技能配置解析类
 * @Author Yang Gao
 * @Since 2016-8-2
 * @Version 1.1.0
 */
@Service
public class XinMoJiNengBiaoConfigExport extends AbsClasspathConfigureParser {

    private static final String CONFIGURE_NAME = "XinMoJiNengBiao.jat";

    private Map<Integer, XinMoJiNengBiaoConfig> configMap;

    private Map<String, Integer> configMaxLevelMap;

    @Override
    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if (null == data) {
            ChuanQiLog.error("{} not found", getConfigureName());
            return;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            configMap = new HashMap<Integer, XinMoJiNengBiaoConfig>();
            configMaxLevelMap = new HashMap<String, Integer>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    XinMoJiNengBiaoConfig config = createXinMoJiNengBiaoConfig(tmp);
                    configMap.put(config.getId(), config);
                    String key = keyStr(config.getXinmoType(), config.getSeq());
                    Integer maxLevel = configMaxLevelMap.get(key);
                    if (null == maxLevel || config.getLevel() > maxLevel.intValue())
                        configMaxLevelMap.put(key, config.getLevel());
                }
            }
        }
    }

    private String keyStr(int xinmoType, int seq) {
        return "xinmoType:" + xinmoType + ".seq:" + seq;
    }

    /**
     * 构造生成配置数据对象
     * 
     * @param tmp
     * @return
     */
    private XinMoJiNengBiaoConfig createXinMoJiNengBiaoConfig(Map<String, Object> tmp) {
        XinMoJiNengBiaoConfig config = new XinMoJiNengBiaoConfig();
        config.setId(CovertObjectUtil.object2int(tmp.get("id")));
        config.setSeq(CovertObjectUtil.object2int(tmp.get("seq")));
        config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
        config.setXinmoType(CovertObjectUtil.object2int(tmp.get("type")));
        config.setSkillType(CovertObjectUtil.object2int(tmp.get("skilltype")));
        config.setXinmoRank(CovertObjectUtil.object2int(tmp.get("limit")));
        config.setItemId(CovertObjectUtil.object2String(tmp.get("needitem")));
        config.setItemCount(CovertObjectUtil.object2int(tmp.get("itemcount")));
        config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
        config.setAttrsMap(ConfigAnalysisUtils.setAttributeVal(tmp));
        return config;
    }

    @Override
    protected String getConfigureName() {
        return CONFIGURE_NAME;
    }

    public XinMoJiNengBiaoConfig loadById(Integer id) {
        return configMap.get(id);
    }

    public int getXmSkillMaxLevel(Integer type, Integer seq) {
        return configMaxLevelMap.get(keyStr(type, seq));
    }

    /**
     * @Description 根据大类id获取物品id列表
     * @param id1
     * @return
     */
    public List<String> getConsumeIds(String id1) {
        List<String> result = new ArrayList<>();
        if (id1 != null) {
            List<String> ids = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(id1);
            result.addAll(ids);
        }
        return result;
    }

}
