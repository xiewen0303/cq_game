package com.junyou.bus.huajuan2.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class Huajuan2XinXiBiaoConfigExportService extends AbsClasspathConfigureParser {
    /* 配置文件名 */
    private static final String CONFIG_GURE_NAME = "YunTuXinXiBiao.jat";
    private Map<Integer, Huajuan2XinXiBiaoConfig> configs = null;

    private Map<Integer, Integer> group = null;

    @Override
    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if (null == data) {
            ChuanQiLog.error("{} not found", getConfigureName());
            return;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            configs = new HashMap<Integer, Huajuan2XinXiBiaoConfig>();
            group = new HashMap<Integer, Integer>();
            Huajuan2XinXiBiaoConfig tmpConfig = null;
            for (Object dataObj : dataList) {
                Map<String, Object> temp = (Map<String, Object>) dataObj;
                if (null != temp) {
                    Huajuan2XinXiBiaoConfig config = createYunTuXinXiBiaoConfig(temp);
                    if (tmpConfig != null && config.getMonsterId().equals(tmpConfig.getMonsterId())) {
                        tmpConfig.setNextConfig(config);
                    }
                    tmpConfig = config;
                    configs.put(config.getId(), config);
                    group.put(config.getLiebiaoId(), null == group.get(config.getLiebiaoId()) ? 1 : group.get(config.getLiebiaoId()) + 1);
                }
            }
        }
    }

    private Huajuan2XinXiBiaoConfig createYunTuXinXiBiaoConfig(Map<String, Object> tmp) {
        Huajuan2XinXiBiaoConfig config = new Huajuan2XinXiBiaoConfig();
        config.setId(CovertObjectUtil.object2int(tmp.get("id")));
        config.setLiebiaoId(CovertObjectUtil.object2int(tmp.get("liebiaoid")));
        config.setStar(CovertObjectUtil.object2int(tmp.get("xingji")));
        config.setMonsterId(CovertObjectUtil.object2String(tmp.get("monsterid")));
        config.setNeeditem(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("needitem"))));
        Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
        config.setAttrs(new ReadOnlyMap<>(attrs));
        return config;
    }

    @Override
    protected String getConfigureName() {
        return CONFIG_GURE_NAME;
    }

    public Huajuan2XinXiBiaoConfig loadById(Integer id) {
        return null == configs ? null : configs.get(id);
    }

    public int getGroupCount(Integer groupId) {
        return null == group ? null : group.get(groupId);
    }
}