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
import com.junyou.utils.common.ObjectUtil;

@Service
public class Huajuan2EquipConfigExportService extends AbsClasspathConfigureParser {
    /* 配置文件名 */
    private static final String CONFIG_GURE_NAME = "YunTuZhuangBeiBiao.jat";
    private Map<Integer, Huajuan2EquipBiaoConfig> configs = null;

    @Override
    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if (null == data) {
            ChuanQiLog.error("{} not found", getConfigureName());
            return;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            configs = new HashMap<Integer, Huajuan2EquipBiaoConfig>();
            for (Object dataObj : dataList) {
                createYunTuZhuangBeiBiaoConfig((Map<String, Object>) dataObj);
            }
        }
    }

    private void createYunTuZhuangBeiBiaoConfig(Map<String, Object> tmp) {
        if (ObjectUtil.isEmpty(tmp)){
            return;
        }
        Huajuan2EquipBiaoConfig config = new Huajuan2EquipBiaoConfig();
        config.setId(CovertObjectUtil.object2int(tmp.get("id")));
        config.setStar(CovertObjectUtil.object2int(tmp.get("xingji")));
        Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
        config.setAttrs(new ReadOnlyMap<>(attrs));
        configs.put(config.getId(), config);
    }

    @Override
    protected String getConfigureName() {
        return CONFIG_GURE_NAME;
    }

    public Huajuan2EquipBiaoConfig loadById(Integer id) {
        return configs.get(id);
    }

}
