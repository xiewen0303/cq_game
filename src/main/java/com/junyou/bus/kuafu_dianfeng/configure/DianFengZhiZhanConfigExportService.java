package com.junyou.bus.kuafu_dianfeng.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsGroupFileAbleConfigureInit;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

/**
 * @Description 巅峰之战配置表解析处理类
 * @Author Yang Gao
 * @Since 2016-5-19
 * @Version 1.1.0
 */
@Component
public class DianFengZhiZhanConfigExportService extends AbsGroupFileAbleConfigureInit {

    private Map<Integer, DianFengZhiZhanConfig> dfzzConfigMap;

    private Map<Integer, DianFengZhiZhanJiangLiConfig> dfzzjlConfigMap;

    private String[] configureNames = new String[] { "DianFengZhiZhan.jat", "DianFengZhiZhanJiangLi.jat" };

    @Override
    protected void configureDataResolve(byte[] data, String configName) {
        if(null == data){
            ChuanQiLog.error("{} not found", configName);
            return ;
        }
        // 配置文件MD5值加入管理
        ConfigMd5SignManange.addConfigSign(configName, data);
        if (configureNames[0].equals(configName)) {
            loadDianFengZhiZhanConfig(data);
        } else if (configureNames[1].equals(configName)) {
            loadDianFengZhiZhanJiangLiConfig(data);
        }

    }

    // 加载巅峰之战奖励数据到内存
    @SuppressWarnings("unchecked")
    private void loadDianFengZhiZhanJiangLiConfig(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            dfzzjlConfigMap = new HashMap<>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    DianFengZhiZhanJiangLiConfig config = new DianFengZhiZhanJiangLiConfig();
                    config.setId(CovertObjectUtil.object2int(tmp.get("id")));
                    config.setRank(CovertObjectUtil.object2int(tmp.get("mingci")));
                    config.setRewardId(CovertObjectUtil.object2int(tmp.get("jiangitem")));
                    dfzzjlConfigMap.put(config.getId(), config);
                }
            }
        }
    }

    // 加载巅峰之战活动数据到内存
    @SuppressWarnings("unchecked")
    public void loadDianFengZhiZhanConfig(byte[] data) {
        Object[] dataList = GameConfigUtil.getResource(data);
        if (null != dataList) {
            dfzzConfigMap = new HashMap<>();
            for (Object obj : dataList) {
                Map<String, Object> tmp = (Map<String, Object>) obj;
                if (!ObjectUtil.isEmpty(tmp)) {
                    DianFengZhiZhanConfig config = new DianFengZhiZhanConfig();

                    config.setLoop(CovertObjectUtil.object2int(tmp.get("id")));

                    config.setWeek(CovertObjectUtil.object2int(tmp.get("week")));

                    String beginStr = CovertObjectUtil.object2String(tmp.get("starttime"));
                    String[] beginTmp = beginStr.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
                    if (null != beginTmp) {
                        config.setBegintime(new Integer[] { Integer.parseInt(beginTmp[0]), Integer.parseInt(beginTmp[1]), Integer.parseInt(beginTmp[2]) });
                    }
                    String endStr = CovertObjectUtil.object2String(tmp.get("endtime"));
                    String[] endTmp = endStr.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
                    if (null != endTmp) {
                        config.setEndtime(new Integer[] { Integer.parseInt(endTmp[0]), Integer.parseInt(endTmp[1]), Integer.parseInt(endTmp[2]) });
                    }
                    config.setFighttime(CovertObjectUtil.object2int(tmp.get("time1")));

                    config.setFightBeforeTime(CovertObjectUtil.object2int(tmp.get("time3")));

                    config.setResultShowTime(CovertObjectUtil.object2int(tmp.get("time4")));

                    config.setFightcount(CovertObjectUtil.object2int(tmp.get("changci")));

                    config.setWinfightcount(CovertObjectUtil.object2int(tmp.get("winchangci")));

                    config.setMapId(CovertObjectUtil.object2int(tmp.get("map")));

                    String zbStr = CovertObjectUtil.object2String(tmp.get("zuobiao1"));
                    String[] tmpZbStr = zbStr.split(GameConstants.CONFIG_SPLIT_CHAR);
                    if (null != tmpZbStr) {
                        for (String rzb : tmpZbStr) {
                            String[] tmpRzb = rzb.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
                            config.setZuobiao1(new Integer[] { Integer.parseInt(tmpRzb[0]), Integer.parseInt(tmpRzb[1]) });
                        }
                    }

                    String zbStr2 = CovertObjectUtil.object2String(tmp.get("zuobiao2"));
                    String[] tmpZbStr2 = zbStr2.split(GameConstants.CONFIG_SPLIT_CHAR);
                    if (null != tmpZbStr2) {
                        for (String rzb2 : tmpZbStr2) {
                            String[] tmpRzb2 = rzb2.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
                            config.setZuobiao2(new Integer[] { Integer.parseInt(tmpRzb2[0]), Integer.parseInt(tmpRzb2[1]) });
                        }
                    }
                    dfzzConfigMap.put(config.getLoop(), config);
                }
            }
        }
    }

    @Override
    protected String[] getGroupConfigureNames() {
        return configureNames;
    }

    /**
     * 根据编号获取配置
     * 
     * @param loop
     * @return
     */
    public DianFengZhiZhanConfig loadByLoop(Integer loop) {
        return dfzzConfigMap.get(loop);
    }

    /**
     * 获取所有巅峰之战活动数据
     */
    public Map<Integer, DianFengZhiZhanConfig> getAllDianFengConfigMap() {
        return dfzzConfigMap;
    }

    /**
     * 根据轮次获取奖励礼包编号
     * 
     * @param loop
     * @return
     */
    public int getRewardIdByLoop(int loop) {
        DianFengZhiZhanJiangLiConfig jlConfig = dfzzjlConfigMap.get(loop);
        return jlConfig == null ? 0 : jlConfig.getRewardId();
    }
}