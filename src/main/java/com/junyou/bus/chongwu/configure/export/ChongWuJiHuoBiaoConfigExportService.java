package com.junyou.bus.chongwu.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class ChongWuJiHuoBiaoConfigExportService extends AbsClasspathConfigureParser {

    private Map<Integer, ChongWuJiHuoBiaoConfig> configs = null;

    /**
     * configFileName
     */
    private static final String configureName = "ChongWuJiHuoBiao.jat";

    @SuppressWarnings("unchecked")
    @Override
    protected void configureDataResolve(byte[] data) {
        if( null == data) {
            ChuanQiLog.error("{} not found.", getConfigureName());
            return ;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if( null == dataList ) return ;
        ConfigMd5SignManange.addConfigSign(configureName, data);
        configs = new HashMap<Integer, ChongWuJiHuoBiaoConfig>();
        for (Object obj : dataList) {
            Map<String, Object> tmp = (Map<String, Object>) obj;
            if (null != tmp) {
                createChongWuJiHuoBiaoConfig(tmp);
            }
        }
    }

    private void createChongWuJiHuoBiaoConfig(Map<String, Object> tmp) {
        ChongWuJiHuoBiaoConfig config = new ChongWuJiHuoBiaoConfig();
        config.setId(CovertObjectUtil.object2int(tmp.get("id")));
        config.setNeeditem(CovertObjectUtil.object2String(tmp.get("needitem")));
        config.setName(CovertObjectUtil.object2String(tmp.get("name")));
        config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
        config.setSkill1(CovertObjectUtil.obj2StrOrNull(tmp.get("skill1")));
        config.setSkill2(CovertObjectUtil.obj2StrOrNull(tmp.get("skill2")));
        config.setSkill3(CovertObjectUtil.obj2StrOrNull(tmp.get("skill3")));
        config.setEquipOpen(CovertObjectUtil.object2int(tmp.get("equopen"))== 1);
        configs.put(config.getId(), config);
    }

    @Override
    protected String getConfigureName() {
        return configureName;
    }

    /**
     * 根据id获得配置
     * 
     * @param id
     * @return
     */
    public ChongWuJiHuoBiaoConfig loadById(int id) {
        return null == configs ? null : configs.get(id);
    }

}