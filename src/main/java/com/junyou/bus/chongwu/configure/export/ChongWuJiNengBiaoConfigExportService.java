package com.junyou.bus.chongwu.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class ChongWuJiNengBiaoConfigExportService extends AbsClasspathConfigureParser {

    private static final String configureName = "ChongWuJiNengBiao.jat";

    private Map<String, ChongWuJiNengBiaoConfig> configs = null;
    
    private Map<String, Integer> maxlevel = null;

    @SuppressWarnings("unchecked")
    @Override
    protected void configureDataResolve(byte[] data) {
        if (null == data) {
            ChuanQiLog.error("{} not found", getConfigureName());
            return;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null == dataList) {
            return;
        }
        configs = new HashMap<String, ChongWuJiNengBiaoConfig>();
        maxlevel = new HashMap<String, Integer>();
        for (Object obj : dataList) {
            Map<String, Object> tmp = (Map<String, Object>) obj;
            if (null != tmp) {
                createChongWuJiHuoBiaoConfig(tmp);
            }
        }
    }

    public void createChongWuJiHuoBiaoConfig(Map<String, Object> tmp) {
        ChongWuJiNengBiaoConfig config = new ChongWuJiNengBiaoConfig();
        String id = CovertObjectUtil.obj2StrOrNull(tmp.get("skill"));
        int level = CovertObjectUtil.object2int(tmp.get("skilllevel"));
        config.setId(id);
        config.setLevel(level);
        config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
        config.setItemId(CovertObjectUtil.obj2StrOrNull(tmp.get("needitem1")));
        config.setItemNum(CovertObjectUtil.object2int(tmp.get("num")));
        config.setItemGold(CovertObjectUtil.object2int(tmp.get("needgold")));
        config.setItemBgold(CovertObjectUtil.object2int(tmp.get("needbgold")));
        config.setAttrMap(ConfigAnalysisUtils.setAttributeVal(tmp));
        configs.put(getKeyString(id, level), config);
        maxlevel.put(id, maxlevel.get(id) == null || level > maxlevel.get(id) ? level : maxlevel.get(id));
    }

    private String getKeyString(String id, int level) {
        return "id:" + id + ".level:" + level;
    }

    @Override
    protected String getConfigureName() {
        return configureName;
    }

    /**
     * 根据id和等级获得配置
     * 
     * @param id
     * @return
     */
    public ChongWuJiNengBiaoConfig loadByIdAndLevel(String id, int level) {
        return configs == null ? null : configs.get(getKeyString(id, level));
    }
    
    /**
     * 根据id获得配置的最大等级
     * 
     * @param id
     * @return
     */
    public Integer loadMaxLevelById(String id){
        return maxlevel == null ? null : maxlevel.get(id);
    }

}