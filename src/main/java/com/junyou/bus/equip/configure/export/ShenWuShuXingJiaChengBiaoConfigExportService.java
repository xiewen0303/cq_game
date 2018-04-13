package com.junyou.bus.equip.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @description 神武星铸属性加成配置表
 * @author Yang Gao
 * @date 2016-04-11 14:24:39
 */
@Component
public class ShenWuShuXingJiaChengBiaoConfigExportService extends AbsClasspathConfigureParser {
    /**
     * 全身神武装备强化
     */
    private static final int TYPE_QH_LEVEL = 1;

    /**
     * 所有配置数据集合
     */
    private static Map<Integer, ShenWuShuXingJiaChengBiaoConfig> configMap = new HashMap<>();;

    /**
     * configFileName
     */
    private String configureName = "ShenWuShuXingJiaChengBiao.jat";


    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if (data == null)
            return;
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            try {
                for (Object obj : dataList) {
                    Map<String, Object> tmp = (Map<String, Object>) obj;
                    if (null != tmp) {
                        createShenWuShuXingJiaChengBiaoConfig(tmp);
                    }
                }
            } catch (Exception e) {
                ChuanQiLog.error("ShenWuShuXingJiaChengBiao.jat解析有问题");
            }
        }
    }

    private void createShenWuShuXingJiaChengBiaoConfig(Map<String, Object> tmp) {
        int id = CovertObjectUtil.object2int(tmp.get("ID"));
        ShenWuShuXingJiaChengBiaoConfig config = new ShenWuShuXingJiaChengBiaoConfig();
        config.setId(id);
        config.setType(CovertObjectUtil.object2int(tmp.get("type")));
        config.setLv(CovertObjectUtil.object2int(tmp.get("lv")));
        Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
        config.setNeedNum(CovertObjectUtil.object2int(tmp.get("neednum")));
        config.setAttrs(new ReadOnlyMap<>(attrs));
        configMap.put(id, config);
    }

    protected String getConfigureName() {
        return configureName;
    }
    
    public List<ShenWuShuXingJiaChengBiaoConfig> getAllQhLevelConfig(){
        List<ShenWuShuXingJiaChengBiaoConfig> allConfig = new ArrayList<>();
        for(ShenWuShuXingJiaChengBiaoConfig config : configMap.values()){
            if(TYPE_QH_LEVEL == config.getType())
                allConfig.add(config);
        }
        return allConfig;
    }

}