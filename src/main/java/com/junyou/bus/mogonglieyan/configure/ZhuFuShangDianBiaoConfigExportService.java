package com.junyou.bus.mogonglieyan.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * 
 * @Description 灵火祝福,祝福商店配置表
 * @Author Yang Gao
 * @Since 2016-10-20
 * @Version 1.1.0
 */
@Component
public class ZhuFuShangDianBiaoConfigExportService extends AbsClasspathConfigureParser {

    private Map<Integer, ZhuFuShangDianBiaoConfig> configs = null;
    /**
     * configFileName
     */
    private String configureName = "ZhuFuShangDianBiao.jat";

    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if (null == data) {
            ChuanQiLog.error("{} not found", getConfigureName());
            return;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            configs = new HashMap<Integer, ZhuFuShangDianBiaoConfig>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    createZhuFuShangDianBiaoConfig(tmp);
                }
            }
        }
    }

    private void createZhuFuShangDianBiaoConfig(Map<String, Object> tmp) {
        ZhuFuShangDianBiaoConfig config = new ZhuFuShangDianBiaoConfig();
        config.setOrder(CovertObjectUtil.object2int(tmp.get("order")));
        config.setMinLevel(CovertObjectUtil.object2int(tmp.get("minlevel")));
        config.setMzxLevel(CovertObjectUtil.object2int(tmp.get("maxlevel")));
        config.setNeedJinghua(CovertObjectUtil.obj2long(tmp.get("needjinghua")));
        config.setPercent(CovertObjectUtil.object2int(tmp.get("percent")));
        configs.put(config.getOrder(), config);
    }

    protected String getConfigureName() {
        return configureName;
    }

    public ZhuFuShangDianBiaoConfig loadById(Integer id) {
        return configs.get(id);
    }
    
    /**
     * 加载配置
     * @param level
     * @return
     */
    public ZhuFuShangDianBiaoConfig loadByLevel(int level) {
    	for (ZhuFuShangDianBiaoConfig config : configs.values()) {
    		if(config.getMinLevel() <= level && level <= config.getMzxLevel() ){
    			return config;
    		}
		}
    	return null;
    }
}