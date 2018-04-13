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
 * @description 五行技能配置表
 * @author Yang Gao
 * @date 2016-04-25 18:04:34
 */
@Component
public class MoShenJiNengBiaoConfigExportService extends AbsClasspathConfigureParser {

    /**
     * configFileName
     */
    private String configureName = "MoShenJiNengBiao.jat";

    private Map<Integer, MoShenJiNengBiaoConfig> configMap;
    
    private Map<String, Integer> configMaxLevelMap;

    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if(null == data){
            ChuanQiLog.error("{} not found", getConfigureName());
            return ;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            configMap = new HashMap<Integer, MoShenJiNengBiaoConfig>();
            configMaxLevelMap = new HashMap<String, Integer>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    MoShenJiNengBiaoConfig config = createMoShenJiNengBiaoConfig(tmp);
                    configMap.put(config.getId(), config);
                    String key = keyStr(config.getType(), config.getSeq());
                    Integer maxLevel = configMaxLevelMap.get(key); 
                    if(null == maxLevel || config.getLevel() > maxLevel.intValue()) 
                        configMaxLevelMap.put(key, config.getLevel());
                }
            }
        }
    }

    public MoShenJiNengBiaoConfig createMoShenJiNengBiaoConfig(Map<String, Object> tmp) {
        MoShenJiNengBiaoConfig config = new MoShenJiNengBiaoConfig();
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

    public MoShenJiNengBiaoConfig loadById(Integer id) {
        return configMap.get(id);
    }
    
    public int getWxSkillMaxLevel(Integer type, Integer seq){
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