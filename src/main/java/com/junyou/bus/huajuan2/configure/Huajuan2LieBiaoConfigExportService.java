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
public class Huajuan2LieBiaoConfigExportService extends AbsClasspathConfigureParser {

    /* 配置文件名 */
    private static final String CONFIG_GURE_NAME = "YunTuLieBiao.jat";

    private Map<Integer, Huajuan2LieBiaoConfig> configs = null;

    @Override
    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if (null == data) {
            ChuanQiLog.error("{} not found", getConfigureName());
            return;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            configs = new HashMap<Integer, Huajuan2LieBiaoConfig>();
            for (Object dataObj : dataList) {
                createYunTuLieBiaoConfig((Map<String, Object>) dataObj);
            }
        }
    }

    private void createYunTuLieBiaoConfig(Map<String, Object> tmp) {
        if (null == tmp) {
            return;
        }
        Huajuan2LieBiaoConfig config = new Huajuan2LieBiaoConfig();
        config.setId(CovertObjectUtil.object2int(tmp.get("id")));
        config.setNeedLevel(CovertObjectUtil.object2int(tmp.get("needLevel")));
        config.setAttrs(new ReadOnlyMap<>(ConfigAnalysisUtils.setAttributeVal(tmp)));
        configs.put(config.getId(), config);
    }

    @Override
    protected String getConfigureName() {
        return CONFIG_GURE_NAME;
    }

    public Huajuan2LieBiaoConfig loadById(Integer id) {
        return configs.get(id);
    }

}
