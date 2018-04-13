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
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 *@Description  糖宝五行技能配置表解析
 *@Author Yang Gao
 *@Since 2016-6-13
 *@Version 1.1.0
 */
@Component
public class TangbaoMoShenJiNengBiaoConfigExportService extends AbsClasspathConfigureParser {

    /**
     * configFileName
     */
    private String configureName = "TangBaoMoShenJiNengBiao.jat";

    private Map<Integer, TangbaoMoShenJiNengBiaoConfig> configMap;
    
    private Map<String, Integer> configMaxLevelMap;

    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if(null == data){
            ChuanQiLog.error("{} not fond!", getConfigureName());
            return ;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            configMap = new HashMap<Integer, TangbaoMoShenJiNengBiaoConfig>();
            configMaxLevelMap = new HashMap<String, Integer>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    TangbaoMoShenJiNengBiaoConfig config = createTbMoShenJiNengBiaoConfig(tmp);
                    configMap.put(config.getId(), config);
                    String key = keyStr(config.getType(), config.getSeq());
                    Integer maxLevel = configMaxLevelMap.get(key); 
                    if(null == maxLevel || config.getLevel() > maxLevel.intValue()) 
                        configMaxLevelMap.put(key, config.getLevel());
                }
            }
        }
    }

    public TangbaoMoShenJiNengBiaoConfig createTbMoShenJiNengBiaoConfig(Map<String, Object> tmp) {
        TangbaoMoShenJiNengBiaoConfig config = new TangbaoMoShenJiNengBiaoConfig();
        config.setId(CovertObjectUtil.object2int(tmp.get("id")));
        config.setType(CovertObjectUtil.object2int(tmp.get("type")));
        config.setSeq(CovertObjectUtil.object2int(tmp.get("seq")));
        config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
        config.setLimit(CovertObjectUtil.object2int(tmp.get("limit")));
        config.setNeedItem(CovertObjectUtil.object2String(tmp.get("needitem")));
        config.setItemCount(CovertObjectUtil.object2int(tmp.get("itemcount")));
        config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
        config.setAttrMap(ConfigAnalysisUtils.setAttributeVal(tmp));
        return config;
    }

    protected String getConfigureName() {
        return configureName;
    }
    
    private String keyStr(Integer type, Integer seq){
        return "type:" + type + ".seq:" + seq;
    }

    public TangbaoMoShenJiNengBiaoConfig loadById(Integer id) {
        return configMap.get(id);
    }
    
    public int getTbWxSkillMaxLevel(Integer type, Integer seq){
        return configMaxLevelMap.get(keyStr(type, seq));
    }
    
    /**
     * @Description 根据大类id获取物品id列表
     * @param id1
     * @return                      
     */
    public List<String> getConsumeIds(String id1) {
        List<String> result = new ArrayList<>();
        if (id1 != null) {
            List<String> ids = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(id1);
            result.addAll(ids);
        }
        return result;
    }
}