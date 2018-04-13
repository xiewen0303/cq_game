package com.junyou.bus.huajuan2.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

@Service
public class JuanZhouXinXiConfigExportService extends AbsClasspathConfigureParser {
    /* 配置文件名 */
    private static final String CONFIG_GURE_NAME = "JuanZhouXinXi.jat";
    /* 卷轴配置信息数据 */
    private Map<Integer, JuanZhouXinXiConfig> configs = null;
    private static Integer MIN_LEVEL = null;
    private static Integer MAX_LEVEL = null;
    /* 最小经验值 */
    private static Long MIN_EXP = null;
    /* 最大经验值 */
    private static Long MAX_EXP = null;

    @Override
    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if (null == data) {
            ChuanQiLog.error("{} not found", getConfigureName());
            return;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            configs = new HashMap<Integer, JuanZhouXinXiConfig>();
            for (Object dataObj : dataList) {
                createJuanZhouXinXiConfig((Map<String, Object>) dataObj);
            }
        }
    }

    private void createJuanZhouXinXiConfig(Map<String, Object> tmp) {
        if (null == tmp) {
            return;
        }
        JuanZhouXinXiConfig config = new JuanZhouXinXiConfig();
        config.setLevel(CovertObjectUtil.object2int(tmp.get("id")));
        config.setMinExp(CovertObjectUtil.obj2long(tmp.get("minexp")));
        config.setMaxExp(CovertObjectUtil.obj2long(tmp.get("needexp")));
        config.setPercent(CovertObjectUtil.object2int(tmp.get("tisheng")));
        configs.put(config.getLevel(), config);
        if (null == MIN_LEVEL || config.getLevel() <= MIN_LEVEL)   MIN_LEVEL = config.getLevel();
        if (null == MAX_LEVEL || config.getLevel() >= MAX_LEVEL)   MAX_LEVEL = config.getLevel();
        if (null == MIN_EXP || config.getMinExp() <= MIN_EXP)   MIN_EXP = config.getMinExp() ;
        if (null == MAX_EXP || config.getMaxExp() >= MAX_EXP)   MAX_EXP = config.getMaxExp();
    }

    @Override
    protected String getConfigureName() {
        return CONFIG_GURE_NAME;
    }

    /**
     * 计算画卷2卷轴等级
     * 
     * @param exp
     * @return
     */
    public Integer calcLevel(long exp) {
        if(exp <= MIN_EXP){
            return MIN_LEVEL;
        }
        if(exp >= MAX_EXP){
            return MAX_LEVEL;
        }
        if (!ObjectUtil.isEmpty(configs)) {
            for (JuanZhouXinXiConfig config : configs.values()) {
                if (config.getMinExp() <= exp && exp < config.getMaxExp()) {
                    return config.getLevel();
                }
            }
        }
        return null;
    }

    public JuanZhouXinXiConfig loadById(Integer id) {
        return ObjectUtil.isEmpty(configs) ? null : configs.get(id);
    }

    public Long getMaxExp() {
        return MAX_EXP;
    }
}
