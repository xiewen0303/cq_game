package com.junyou.bus.equip.configure.export;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.bus.equip.configure.export.ShenWuXingZhuBiaoConfigExportService;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.log.ChuanQiLog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.junyou.bus.equip.configure.export.ShenWuXingZhuBiaoConfig;

/**
 * @description 神武星铸配置表
 * @author Yang Gao
 * @date 2016-03-28 14:59:05
 */
@Component
public class ShenWuXingZhuBiaoConfigExportService extends AbsClasspathConfigureParser {

    private int maxQhLevel = 0;

    private Map<Integer, ShenWuXingZhuBiaoConfig> configMap;

    /**
     * configFileName
     */
    private String configureName = "ShenWuXingZhuBiao.jat";

    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if (data == null)
            return;
        Object[] dataList = GameConfigUtil.getResource(data);
        try {
            configMap = new HashMap<Integer, ShenWuXingZhuBiaoConfig>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (null != tmp) {
                    ShenWuXingZhuBiaoConfig config = createShenWuXingZhuBiaoConfig(tmp);
                    int qhLevel = config.getLevel();
                    configMap.put(qhLevel, config);
                    if(qhLevel > maxQhLevel){
                        maxQhLevel = qhLevel;
                    }
                }
            }
        } catch (Exception e) {
            ChuanQiLog.error("ShenWuXingZhuBiao.jat解析有问题");
        }
    }

    public ShenWuXingZhuBiaoConfig createShenWuXingZhuBiaoConfig(Map<String, Object> tmp) {
        ShenWuXingZhuBiaoConfig config = new ShenWuXingZhuBiaoConfig();
        config.setLevel(CovertObjectUtil.object2int(tmp.get("level"))); 
        config.setNeedItemCount(CovertObjectUtil.object2int(tmp.get("num")));
        config.setNeedItemId(CovertObjectUtil.object2String(tmp.get("prop")));
        config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("money")));
        config.setQhxs(CovertObjectUtil.object2Float(tmp.get("qhxs")));
        config.setSuccessrate(CovertObjectUtil.object2int(tmp.get("successrate")));
        config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
        config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
        return config;
    }

    public List<String> getGoodsIdsById1(String id1) {
        return BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(id1);
    }

    protected String getConfigureName() {
        return configureName;
    }

    public ShenWuXingZhuBiaoConfig getQHConfig(int level) {
        return null == configMap ? null : configMap.get(level);
    }

    public Float getQhxs(int qhLevel) {
        ShenWuXingZhuBiaoConfig config = getQHConfig(qhLevel);
        if (null == config)
            return null;
        return config.getQhxs();
    }
    
    public int getMaxQhLevel() {
        return this.maxQhLevel;
    }

}