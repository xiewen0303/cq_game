package com.junyou.bus.wuxing.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @Description 糖宝五行魔神基础信息解析
 * @Author Yang Gao
 * @Since 2016-6-7
 * @Version 1.1.0
 */
@Component
public class TangbaoMoShenBaseConfigExportService extends AbsClasspathConfigureParser {

    private Map<Integer, TangbaoMoShenBaseConfig> configs = new HashMap<Integer, TangbaoMoShenBaseConfig>();

    private String configureName = "TangBaoMoShenJiChuBiao.jat";

    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if(null == data){
            ChuanQiLog.error("{} not fond!", getConfigureName());
            return ;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        for (Object obj : dataList) {
            Map<String, Object> tmp = (Map<String, Object>) obj;
            if (null != tmp) {
                TangbaoMoShenBaseConfig config = createTangbaoMoShenBaseConfig(tmp);

                configs.put(config.getId(), config);
            }
        }
    }

    public TangbaoMoShenBaseConfig createTangbaoMoShenBaseConfig(Map<String, Object> tmp) {
        TangbaoMoShenBaseConfig config = new TangbaoMoShenBaseConfig();

        config.setCount(CovertObjectUtil.object2int(tmp.get("count")));

        config.setType(CovertObjectUtil.object2int(tmp.get("type")));

        config.setId(CovertObjectUtil.object2int(tmp.get("id")));

        config.setZfzmin(CovertObjectUtil.object2int(tmp.get("zfzmin")));

        config.setMoney(CovertObjectUtil.object2int(tmp.get("money")));

        config.setCztime(CovertObjectUtil.obj2float(tmp.get("cztime")));

        config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));

        config.setGgopen(CovertObjectUtil.object2int(tmp.get("ggopen")) == 1);

        config.setLevel(CovertObjectUtil.object2int(tmp.get("dengjie")));

        config.setZfzmin3(CovertObjectUtil.object2int(tmp.get("zfzmin3")));

        config.setZfzmin2(CovertObjectUtil.object2int(tmp.get("zfzmin2")));

        config.setItem(CovertObjectUtil.object2String(tmp.get("id1")));

        config.setZfztime(CovertObjectUtil.object2int(tmp.get("zfztime")) == 1);

        config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));

        config.setPro(CovertObjectUtil.object2int(tmp.get("pro")));

        config.setZfzmax(CovertObjectUtil.object2int(tmp.get("zfzmax")));

        Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
        config.setAttrs(new ReadOnlyMap<>(attrs));

        return config;
    }

    public List<String> getConsumeIds(String id1) {
        List<String> result = new ArrayList<>();
        List<String> ids = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(id1);
        result.addAll(ids);
        return result;
    }

    /**
     * 根据类型和等级获取配置
     * 
     * @param type
     * @param level
     * @return
     */
    public TangbaoMoShenBaseConfig getTbWuXingConfigByTypeAndLevel(Integer type, Integer level) {
        for (Integer id : configs.keySet()) {
            TangbaoMoShenBaseConfig config = configs.get(id);
            if (config.getType() == type.intValue() && config.getLevel() == level.intValue()) {
                return config;
            }

        }
        return null;
    }

    protected String getConfigureName() {
        return configureName;
    }

    public TangbaoMoShenBaseConfig loadById(Integer id) {
        return configs.get(id);
    }
}