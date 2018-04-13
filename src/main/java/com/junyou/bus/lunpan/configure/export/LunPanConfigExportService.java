package com.junyou.bus.lunpan.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuZhanJiaGroupConfig;
import com.junyou.bus.rfbactivity.configure.export.AbstractRfbConfigService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.configure.export.IRfbConfigTemplateService;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.json.JsonUtils;
import com.junyou.utils.md5.Md5Utils;
import com.junyou.utils.md5.ThreeDesEncryptAndEncrypt;

/**
 * @Description 轮盘数据解析
 * @Author Yang Gao
 * @Since 2016-6-1
 * @Version 1.1.0
 */
@Service
public class LunPanConfigExportService extends AbstractRfbConfigService {

	@Override
	public Object getChildData(int subId) {
		LunPanConfigGroup config = this.lunpanConfigMap.get(subId);
		return config != null ? config.getPic() : null;
	}
	
    private static final LunPanConfigExportService INSTANCE = new LunPanConfigExportService();

    /** key:子活动id value:子活动数据 **/
    private Map<Integer, LunPanConfigGroup> lunpanConfigMap = new HashMap<>();

    private LunPanConfigExportService() {
    }

    /**
     * 获取对象实例
     * 
     * @return
     */
    public static LunPanConfigExportService getInstance() {
        return INSTANCE;
    }

    public Map<Integer, LunPanConfigGroup> getAllConfig() {
        return lunpanConfigMap;
    }

    public LunPanConfig loadByKeyId(int subId, Integer id) {
        LunPanConfigGroup config = lunpanConfigMap.get(subId);
        if (config != null) {
            return config.getConfigMap().get(id);
        }
        return null;
    }

    public LunPanConfigGroup loadByMap(int subId) {
        return lunpanConfigMap.get(subId);
    }

    /**
     * 解析数据
     * 
     * @param subId
     * @param data
     */
    public void analysisConfigureDataResolve(int subId, byte[] data) {
        if (data == null) {
            ChuanQiLog.error(" LunPan 1 data is error! ");
        }

        byte[] data1 = ThreeDesEncryptAndEncrypt.getDecryptResourceHandle(data);
        JSONArray json = JsonUtils.getJsonArrayByBytes(data1);
        if (json == null || json.isEmpty()) {
            ChuanQiLog.error(" LunPan 2 data is error! ");
            return;
        }

        // 版本号比对
        String md5Value = Md5Utils.md5Bytes(data);
        LunPanConfigGroup group = lunpanConfigMap.get(subId);
        if (group != null) {

            if (md5Value.equals(group.getMd5Version())) {
                // 版本号一致，不处理下面的业务直接跳出
                ChuanQiLog.error(" LunPan subid={} version is same md5Value={}", subId, md5Value);
                return;
            }
        }
        group = new LunPanConfigGroup();
        group.setMd5Version(md5Value);

        // 处理子活动的版本号
        ActivityConfigSon configSon = ActivityAnalysisManager.getInstance().loadByZiId(subId);
        if (configSon != null) {
            configSon.setClientVersion(configSon.getClientVersion() + 1);
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < json.size(); i++) {
            JSONArray json1 = json.getJSONArray(i);
            Map<String, Object> map = new HashMap<String, Object>();
            for (int j = 0; j < json1.size(); j++) {
                @SuppressWarnings("unchecked")
                Map<String, Object> aa = (Map<String, Object>) JSONObject.parse(json1.getString(j));
                map.putAll(aa);
            }
            list.add(map);
        }

        int ge = 0;
        List<Object[]> duiHuanList = new ArrayList<>();
        Map<Integer, Integer> zpMap = new HashMap<>();
        Map<Integer, LunPanConfig> tmpConfig = new HashMap<Integer, LunPanConfig>();
        for (Map<String, Object> map : list) {
            if (null != map) {
                Integer id = CovertObjectUtil.object2int(map.get("id"));
                // id为-1处理活动说明和底图处理
                if (id == -1) {
                    group.setPic(CovertObjectUtil.object2String(map.get("item1")));
                } else if (id == -2) {
                    continue;
                } else if (id == -3) {
                    group.setGold(CovertObjectUtil.object2int(map.get("gold")));
                    group.setCount(CovertObjectUtil.object2int(map.get("counts")));
                } else if (id == -4) {
                    int count = CovertObjectUtil.object2int(map.get("type"));
                    if (count == 0) {
                        count = 9999;
                    }
                    group.setMaxCount(count);
                } else {
                    LunPanConfig config = createShenMiShangDianConfig(map, duiHuanList, zpMap);
                    tmpConfig.put(config.getId(), config);
                    if (config.getType().intValue() == 1) {
                        ge++;
                    }
                }
            }
        }
        group.setZpMap(zpMap);
        group.setDuiHuanData(duiHuanList);
        group.setMaxGe(ge);
        group.setConfigMap(tmpConfig);
        // 最终根据子活动ID记录，当前子活动ID的首充活动数据
        lunpanConfigMap.put(subId, group);
    }

    public LunPanConfig createShenMiShangDianConfig(Map<String, Object> tmp, List<Object[]> duiHuanList, Map<Integer, Integer> zpMap) {
        LunPanConfig config = new LunPanConfig();

        config.setId(CovertObjectUtil.object2int(tmp.get("id")));

        config.setJifen(CovertObjectUtil.object2int(tmp.get("needjifen")));

        config.setQuan(CovertObjectUtil.object2int(tmp.get("odds")));

        config.setType(CovertObjectUtil.object2int(tmp.get("type")));

        if (config.getType() == 1) {
            Map<String, Integer> itemMap = new HashMap<>();
            for (int i = 1; i < 50; i++) {
                String item = CovertObjectUtil.object2String(tmp.get("item" + i));
                if (item != null && !"".equals(item)) {
                    Integer quan = CovertObjectUtil.object2int(tmp.get("item" + i + "odds"));
                    itemMap.put(item, quan);
                } else {
                    break;
                }
            }
            config.setItemMap(itemMap);

            zpMap.put(config.getId(), config.getQuan());
        }
        if (config.getType() == 2) {
            Map<String, Integer> itemMap = new HashMap<>();
            String item = CovertObjectUtil.object2String(tmp.get("item1"));
            if (item != null && !"".equals(item)) {
                String[] str = item.split(":");

                itemMap.put(str[0], Integer.parseInt(str[1]));
                config.setDuiHuanMap(itemMap);
                config.setGoodId(str[0]);
                config.setGoodCount(Integer.parseInt(str[1]));
                duiHuanList.add(new Object[] { config.getId(), str[0], str[1], config.getJifen() });
            }
        }

        return config;
    }

}
