/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.bus.fuben.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.WuxingSkillFubenConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * @Description 五行技能副本基础配置解析
 * @Author Yang Gao
 * @Since 2016-5-3
 * @Version 1.1.0
 */
@Service
public class WuxingSkillFubenConfigService extends AbsClasspathConfigureParser {

    private final String configureName = "ZhenYaoTaBiao.jat";

    /* 所有数据集合 */
    private Map<Integer, WuxingSkillFubenConfig> configs;
    /* 最大关卡层数  */
    private Integer maxLayer;

    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if(null == data){
            ChuanQiLog.error("{} not found", getConfigureName());
            return ;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        configs = new HashMap<Integer, WuxingSkillFubenConfig>();
        for (Object dataObj : dataList) {
            Map<String, Object> temp = (Map<String, Object>) dataObj;
            if (null != temp) {
                WuxingSkillFubenConfig wxSkillConfig = createWuxingSkillFubenConfig(temp);
                configs.put(wxSkillConfig.getId(), wxSkillConfig);
                if(null == maxLayer || wxSkillConfig.getId() > maxLayer.intValue()){
                    maxLayer = wxSkillConfig.getId();
                }
            }
        }
    }

    private WuxingSkillFubenConfig createWuxingSkillFubenConfig(Map<String, Object> tmp) {
        WuxingSkillFubenConfig wxSkillConfig = new WuxingSkillFubenConfig();
        wxSkillConfig.setId(CovertObjectUtil.object2int(tmp.get("lv")));
        wxSkillConfig.setMonsterId(CovertObjectUtil.object2String(tmp.get("monster1")));
        wxSkillConfig.setMoney(CovertObjectUtil.obj2long(tmp.get("jiangmoney")));
        wxSkillConfig.setExp(CovertObjectUtil.obj2long(tmp.get("jiangexp")));
        wxSkillConfig.setZq(CovertObjectUtil.obj2long(tmp.get("jiangzhen")));
        Map<String, Integer> prop = ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("jiangitem")));
        wxSkillConfig.setProp(ObjectUtil.isEmpty(prop) ? null : new ReadOnlyMap<>(prop));

        Map<String, Long> wxAttrsMap = ConfigAnalysisUtils.setAttributeVal(tmp);
        wxSkillConfig.setWxAttrsMap(ObjectUtil.isEmpty(wxAttrsMap) ? null : new ReadOnlyMap<>(wxAttrsMap));
        wxSkillConfig.setAddBuffVal(CovertObjectUtil.object2int(tmp.get("atkbuff")));
        wxSkillConfig.setSubBuffVal(CovertObjectUtil.object2int(tmp.get("defbuff")));
        return wxSkillConfig;
    }

    @Override
    protected String getConfigureName() {
        return configureName;
    }

    public WuxingSkillFubenConfig loadByLayer(Integer layer) {
        return configs.get(layer);
    }

    // 获取最大关卡层次
    public int findMaxLayer() {
        return maxLayer == null ? 0 : maxLayer.intValue();
    }

}
