/**
 *@Copyright Copyright (c) 2008 - 2100
 *@Company JunYou
 */
package com.junyou.bus.fuben.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.WuxingFubenConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;


/**
 *@Description 五行副本基础配置解析
 *@Author Yang Gao
 *@Since 2016-4-18 下午3:13:49
 *@Version 1.1.0
 */
@Service
public class WuxingFubenConfigService extends AbsClasspathConfigureParser {
    
    private final String configureName = "MoShenHuanJingBiao.jat";
    
    /*所有数据集合*/
    private Map<Integer, WuxingFubenConfig> configs;
    /*五行副本所有类型集合*/
    private Set<String> wxTypeList;

    /*怪物编号数据解析前缀*/
    private static final String PREFIX_STR1 = "monster";
    /*怪物出生坐标数据解析前缀*/
    private static final String PREFIX_STR2 = "zuobiao";
    
    @SuppressWarnings("unchecked")
    protected void configureDataResolve(byte[] data) {
        if(null == data){
            ChuanQiLog.error("{} not found", getConfigureName());
            return ;
        }
        Object[] dataList = GameConfigUtil.getResource(data);
        wxTypeList = new HashSet<String>();
        configs = new HashMap<Integer, WuxingFubenConfig>();
        for(Object dataObj : dataList){
            Map<String, Object> temp = (Map<String, Object>)dataObj;
            if(null != temp){
                WuxingFubenConfig wxConfig = createWuxingFubenConfig(temp);
                GoodsConfigChecker.registCheck(wxConfig);
                wxTypeList.add(wxConfig.getType());
                configs.put(wxConfig.getId(), wxConfig);
            }
        }
    }
    
    private WuxingFubenConfig createWuxingFubenConfig(Map<String, Object> tmp) {
        WuxingFubenConfig wxConfig = new WuxingFubenConfig();
        wxConfig.setId(CovertObjectUtil.object2int(tmp.get("id")));
        wxConfig.setType(CovertObjectUtil.object2String(tmp.get("type")));
        wxConfig.setLevel(CovertObjectUtil.object2int(tmp.get("lv")));
        wxConfig.setMapId(CovertObjectUtil.object2int(tmp.get("map")));
        wxConfig.setTime(CovertObjectUtil.object2int(tmp.get("time")));

        Map<String, List<Integer[]>> monsterMap = createWuxingMonsterMap(tmp);
        wxConfig.setMonsterMap(ObjectUtil.isEmpty(monsterMap) ? null : new ReadOnlyMap<>(monsterMap));
        
        Map<String, Long> wxAttrsMap = ConfigAnalysisUtils.setAttributeVal(tmp);
        wxConfig.setWxAttrsMap(ObjectUtil.isEmpty(wxAttrsMap) ? null : new ReadOnlyMap<>(wxAttrsMap));
        
        wxConfig.setMoney(CovertObjectUtil.obj2long(tmp.get("jiangmoney")));
        wxConfig.setExp(CovertObjectUtil.obj2long(tmp.get("jiangexp")));
        wxConfig.setZq(CovertObjectUtil.obj2long(tmp.get("jiangzhen")));

        Map<String, Integer> prop = ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("jiangitem")));
        wxConfig.setProp(ObjectUtil.isEmpty(prop) ? null : new ReadOnlyMap<>(prop));
        
        return wxConfig;
    }

    private Map<String, List<Integer[]>> createWuxingMonsterMap(Map<String, Object> temp) {
        Map<String, List<Integer[]>> monsterMap = new HashMap<>();
        for (String field : temp.keySet()) {
            if (field.startsWith(PREFIX_STR1)) {
                String monsterId = CovertObjectUtil.object2String(temp.get(field));
                String positionKey = PREFIX_STR2 + field.substring(PREFIX_STR1.length());
                String positionStr = CovertObjectUtil.object2String(temp.get(positionKey));
                if (!CovertObjectUtil.isEmpty(positionStr)) {
                    List<Integer[]> positionList = new ArrayList<>();
                    String[] strItem = positionStr.split(GameConstants.GOODS_CONFIG_SPLIT_CHAR);
                    for (String str : strItem) {
                        if (CovertObjectUtil.isEmpty(str)) {
                            continue;
                        }
                        String[] innerItem = str.split(GameConstants.GOODS_CONFIG_SUB_SPLIT_CHAR);
                        int len = innerItem == null ? 0 : innerItem.length;
                        if (len > 0) {
                            Integer[] positionInt = new Integer[len];
                            for (int i = 0; i < len; i++) {
                                positionInt[i] = Integer.parseInt(innerItem[i]);
                            }
                            positionList.add(positionInt);
                        }
                        monsterMap.put(monsterId, positionList);
                    }
                }
            }
        }
        return monsterMap;
    }
    
    @Override
    protected String getConfigureName() {
        return configureName;
    }
    
    public WuxingFubenConfig loadById(Integer id){
        return configs.get(id);
    }
    
    public Map<Integer, WuxingFubenConfig> loadAll(){
        return configs;
    }
    
    public Set<String> loadAllType(){
        return wxTypeList;
    }
    
}
