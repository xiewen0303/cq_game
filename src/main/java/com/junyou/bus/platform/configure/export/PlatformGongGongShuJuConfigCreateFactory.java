package com.junyou.bus.platform.configure.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.junyou.bus.platform._37.configure.export._37ShoujilbPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqBaoZidengjiPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqGameEveryDayPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqGameLevelPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqGameXinShouPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqHZchengzhangPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqHZdengjiPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqHZnianfeiPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqHZxinshouPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqLZKaiTongPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqLZXuFeiPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqLZchengzhangPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqLZdengjiPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqLZnianfeiPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqLZxinshouPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqQDCountPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqQDGoodPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqQDJiaGePublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqQzoneEveryDayPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqQzoneLevelPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqQzoneXinShouPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqRechargePublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqShuJiaPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqTgpDuiHuanPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqTgpDuiHuanXHPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqTgpNengLiangPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqTgpPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqTgpZhuanPanPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqTgpchengzhangPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqWeiDuanPublicConfig;
import com.junyou.bus.platform.qq.constants.QqGameConstants;
import com.junyou.bus.platform.qq.constants.QqQzoneConstants;
import com.junyou.bus.platform.yuenan.confiure.export.YueNanYaoQingPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 平台公共数据创建工厂
 * 
 * @author dsh
 * @date 2015-6-10
 */
public class PlatformGongGongShuJuConfigCreateFactory {

	public static AdapterPublicConfig createPublicConfig(String mId, Map<String, Object> tmp) {
		
		AdapterPublicConfig config = null;
		if (mId.equals(PlatformPublicConfigConstants._37_SHOUJI_LB)) {
			config = create_37ShoujilbPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_HZ_XINSHOU)) {
			config = createQqHZxinshouPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_LZ_XINSHOU)) {
			config = createQqLZxinshouPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_HZ_DENGJI)) {
			config = createQqHZdengjiPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_GUANJIA)) {
			config = getPublicConfigData(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_LZ_DENGJI)) {
			config = createQqLZdengjiPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_LZ_KAITONG)) {
			config = createQqLZKaiTongPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_HZ_NIANFEI)) {
			config = createQqHZnianfeiPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_LZ_NIANFEI)) {
			config = createQqLZnianfeiPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_HZ_CHENGZHANG)) {
			config = createQqHZchengzhangPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_LZ_CHENGZHANG)) {
			config = createQqLZchengzhangPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_LZ_XUFEI)) {
			config = createQqLZXuFeiPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_3366_LEVEL)) {
			config = createQq3366dengjiPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QDIAN_ZHIGOU)) {
			config = createQqQDGoodPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QDIAN_JIAGE)) {
			config = createQqQDJiaGePublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QDIAN_COUNT)) {
			config = createQqQDCountPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_RECHARGE)) {
			config = createQqRechargePublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_SHUJIA)) {
			config = createQqShuJiaPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_WEIDUAN)) {
			config = createQqWeiDuanPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_TGP)) {
			config = createQqTgpPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_TGP_LOGIN)) {
			config = createQqTgpLoginPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.QQ_TGP_CHENGZHANG)) {
			config = createQqTgpchengzhangPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.TGP_NL_CFG)) {
			config = createQqTgpNengLiangPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.TGP_NL_ZUANPAN)) {
			config = createQqTgpZhuanPanPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.TGP_NL_DUIHUAN)) {
			config = createQqTgpDuiHuanPublicConfig(tmp);
		}else if (mId.equals(PlatformPublicConfigConstants.TGP_NL_DUIHUAN_XH)) {
			config = createQqTgpDuiHuanXHPublicConfig(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants._37_LEVEL_LB)){
			config = getPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants._37_LINGPAI_LB)){
			config = getPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants._37_VPLAN_DAY_LB)){
			config = getPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants._37_VPLAN_ROLE_LEVEL_LB)){
			config = getVLevelUpgradeLbPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants._37_VPLAN_LJXF_LB)){
			config = getVLevelXfLbPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants._360_JIASUQIU_LB)){
			config = getPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants._360_YOUXIDATING_LB)){
			config = getPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants._360_TEQUAN_LB)){
			config = getPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants._360_TEQUAN_LEVEL)){
			config = getPublicConfigData2(tmp); 
		}else if(mId.equals(PlatformPublicConfigConstants._360_TEQUAN_MEIRI_SWITCH)){
			config = getPublicConfigData2(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants._360_VPLAN_DAY_LB)){
			config = getPublicConfigData(tmp); 
		}else if(mId.equals(PlatformPublicConfigConstants._360_VPLAN_TG_LB)){
			config = getPublicConfigData(tmp); 
		}else if(mId.equals(PlatformPublicConfigConstants._360_VPLAN_LEVEL_LB)){
			config = getVLevelUpgradeLbPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants._360_VPLAN_XF_LB)){
			config = getVLevelXfLbPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants._360_TEQUAN_SHARE_LB)){
			config = getPublicConfigData(tmp);    
		}else if(mId.equals(PlatformPublicConfigConstants.SOGOU_DATING_LB)){
			config = getPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants.SOGOU_PIFU_LB)){
			config = getPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants.XUNLEI_BOX_LB)){
			config = getPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants.XUNLEI_TEQUAN_LB)){
			config = getPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants.XUNLEI_VIP_LB)){
			config = getPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants.SHUNWANG_VIP_LB)){
			config = getPublicConfigData(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants.YUENAN_YAOQING_HAOYOU)){
			config = createYueNanYaoQingPublicConfig(tmp);
		}else if(mId.equals(PlatformPublicConfigConstants.QQ_GAME_XINSHOU) || mId.equals(PlatformPublicConfigConstants.QQ_GAME_LEVEL) || mId.equals(PlatformPublicConfigConstants.QQ_GAME_EVERY_DAY)){
		    config = createQqGameRewardPublicConfig(tmp,mId);
		}else if(mId.equals(PlatformPublicConfigConstants.QQ_QZONE_XINSHOU) || mId.equals(PlatformPublicConfigConstants.QQ_QZONE_LEVEL) || mId.equals(PlatformPublicConfigConstants.QQ_QZONE_EVERY_DAY)){
            config = createQqQzoneRewardPublicConfig(tmp,mId);
        }
		
		if (config != null) {
			config.setId(mId);
		}
		return config;
	}
	/**
	 * 手机礼包奖励
	 * 
	 * @param tmp
	 * @return
	 */
	private static _37ShoujilbPublicConfig create_37ShoujilbPublicConfig(Map<String, Object> tmp) {
		_37ShoujilbPublicConfig config = new _37ShoujilbPublicConfig();
		String item = CovertObjectUtil.object2String(tmp.get("item"));
		Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
		config.setItem(new ReadOnlyMap<>(itemMap));
		return config;
	}
	
	/**
	 * qq黄钻新手
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqHZxinshouPublicConfig createQqHZxinshouPublicConfig(Map<String, Object> tmp) {
		QqHZxinshouPublicConfig config = new QqHZxinshouPublicConfig();
		String item = CovertObjectUtil.object2String(tmp.get("xinshou"));
		Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
		config.setXinshou(new ReadOnlyMap<>(itemMap));
		return config;
	}
	/**
	 * qq蓝钻新手
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqLZxinshouPublicConfig createQqLZxinshouPublicConfig(Map<String, Object> tmp) {
		QqLZxinshouPublicConfig config = new QqLZxinshouPublicConfig();
		String item = CovertObjectUtil.object2String(tmp.get("xinshou"));
		Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
		config.setXinshou(new ReadOnlyMap<>(itemMap));
		return config;
	}
	/**
	 * qq黄钻等级
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqHZdengjiPublicConfig createQqHZdengjiPublicConfig(Map<String, Object> tmp) {
		QqHZdengjiPublicConfig config = new QqHZdengjiPublicConfig();
		Map<Integer, Map<String, Integer>> items = new HashMap<Integer,Map<String,Integer>>();
		for (String key : tmp.keySet()) {
			if(key.startsWith("hzlv")){
				String level = key.substring("hzlv".length(), key.length());
				String item = CovertObjectUtil.object2String(tmp.get(key));
				Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
				items.put(Integer.valueOf(level), itemMap);
			}
		}
		config.setItems(items);
		return config;
	}
	/**
	 * 3366包子等级礼包
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqBaoZidengjiPublicConfig createQq3366dengjiPublicConfig(Map<String, Object> tmp) {
		QqBaoZidengjiPublicConfig config = new QqBaoZidengjiPublicConfig();
		Map<Integer, Map<String, Integer>> items = new HashMap<Integer,Map<String,Integer>>();
		for (String key : tmp.keySet()) {
			if(key.startsWith("3366lv")){
				String level = key.substring("3366lv".length(), key.length());
				String item = CovertObjectUtil.object2String(tmp.get(key));
				Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
				items.put(Integer.valueOf(level), itemMap);
			}
		}
		config.setItems(items);
		return config;
	}
	/**
	 * Q点直购物品
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqQDGoodPublicConfig createQqQDGoodPublicConfig(Map<String, Object> tmp) {
		QqQDGoodPublicConfig config = new QqQDGoodPublicConfig();
		Map<String, String> items = new HashMap<>();
		for (String key : tmp.keySet()) {
			if(key.startsWith("zhigou")){
				String level = key.substring("zhigou".length(), key.length());
				String item = CovertObjectUtil.object2String(tmp.get(key));
				items.put("zhigou"+level, item);
			}
		}
		config.setItems(items);
		return config;
	}
	/**
	 * Q点直购价格
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqQDJiaGePublicConfig createQqQDJiaGePublicConfig(Map<String, Object> tmp) {
		QqQDJiaGePublicConfig config = new QqQDJiaGePublicConfig();
		Map<String, String> items = new HashMap<>();
		for (String key : tmp.keySet()) {
			if(key.startsWith("zhigou")){
				String level = key.substring("zhigou".length(), key.length());
				String item = CovertObjectUtil.object2String(tmp.get(key));
				items.put("zhigou"+level, item);
			}
		}
		config.setItems(items);
		return config;
	}
	/**
	 * Q点直购次数
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqQDCountPublicConfig createQqQDCountPublicConfig(Map<String, Object> tmp) {
		QqQDCountPublicConfig config = new QqQDCountPublicConfig();
		Map<String, Integer> items = new HashMap<>();
		for (String key : tmp.keySet()) {
			if(key.startsWith("zhigou")){
				String level = key.substring("zhigou".length(), key.length());
				Integer count = CovertObjectUtil.object2int(tmp.get(key));
				items.put("zhigou"+level, count);
			}
		}
		config.setItems(items);
		return config;
	}
	/**
	 * qq蓝钻等级
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqLZdengjiPublicConfig createQqLZdengjiPublicConfig(Map<String, Object> tmp) {
		QqLZdengjiPublicConfig config = new QqLZdengjiPublicConfig();
		Map<Integer, Map<String, Integer>> items = new HashMap<Integer,Map<String,Integer>>();
		for (String key : tmp.keySet()) {
			if(key.startsWith("lzlv")){
				String level = key.substring("lzlv".length(), key.length());
				String item = CovertObjectUtil.object2String(tmp.get(key));
				Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
				items.put(Integer.valueOf(level), itemMap);
			}
		}
		config.setItems(items);
		return config;
	}
	/**
	 * qq蓝钻
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqLZKaiTongPublicConfig createQqLZKaiTongPublicConfig(Map<String, Object> tmp) {
		QqLZKaiTongPublicConfig config = new QqLZKaiTongPublicConfig();
		
		config.setItems(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("jiangitem"))));
		return config;
	}
	/**
	 * qq黄钻年费
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqHZnianfeiPublicConfig createQqHZnianfeiPublicConfig(Map<String, Object> tmp) {
		QqHZnianfeiPublicConfig config = new QqHZnianfeiPublicConfig();
		String item = CovertObjectUtil.object2String(tmp.get("nianfei"));
		Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
		config.setNianfei(new ReadOnlyMap<>(itemMap));
		return config;
	}
	/**
	 * qq蓝钻年费
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqLZnianfeiPublicConfig createQqLZnianfeiPublicConfig(Map<String, Object> tmp) {
		QqLZnianfeiPublicConfig config = new QqLZnianfeiPublicConfig();
		String item = CovertObjectUtil.object2String(tmp.get("nianfei"));
		Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
		config.setNianfei(new ReadOnlyMap<>(itemMap));
		String hitem = CovertObjectUtil.object2String(tmp.get("haohua"));
		Map<String, Integer> hitemMap = ConfigAnalysisUtils.getConfigMap(hitem);
		config.setHaohua(new ReadOnlyMap<>(hitemMap));
		return config;
	}
	/**
	 * qq续费蓝钻
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqLZXuFeiPublicConfig createQqLZXuFeiPublicConfig(Map<String, Object> tmp) {
		QqLZXuFeiPublicConfig config = new QqLZXuFeiPublicConfig();
		String item = CovertObjectUtil.object2String(tmp.get("libao"));
		Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
		config.setXufei(new ReadOnlyMap<>(itemMap));
		return config;
	}
	/**
	 * qq充值
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqRechargePublicConfig createQqRechargePublicConfig(Map<String, Object> tmp) {
		QqRechargePublicConfig config = new QqRechargePublicConfig();
		Map<String, String> map = new HashMap<>();
		
		for(int i=1;i<100;i++){
			String gold1 = CovertObjectUtil.object2String(tmp.get("gold"+i));
			String icon1 = CovertObjectUtil.object2String(tmp.get("icon"+i));
			String name1 = CovertObjectUtil.object2String(tmp.get("name"+i));
			String des1 = CovertObjectUtil.object2String(tmp.get("des"+i));
			if(gold1 == ""){
				break;
			}
			map.put("gold"+i, gold1);
			map.put("icon"+i, icon1);
			map.put("name"+i, name1);
			map.put("des"+i, des1);
		}
		
		String goldBi = CovertObjectUtil.object2String(tmp.get("goldbi"));
		map.put("goldbi", goldBi);
		
		config.setChargeMap(new ReadOnlyMap<>(map));
		return config;
	}
	/**
	 * qq暑假活动
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqShuJiaPublicConfig createQqShuJiaPublicConfig(Map<String, Object> tmp) {
		QqShuJiaPublicConfig config = new QqShuJiaPublicConfig();
		Map<String, String> map = new HashMap<>();
		
		String id1 = CovertObjectUtil.object2String(tmp.get("up"));
		String id2 = CovertObjectUtil.object2String(tmp.get("30"));
		String id3 = CovertObjectUtil.object2String(tmp.get("40"));
		String id4 = CovertObjectUtil.object2String(tmp.get("50"));
		map.put("up", id1);
		map.put("30", id2);
		map.put("40", id3);
		map.put("50", id4);
		
		config.setRenwuMap(new ReadOnlyMap<>(map));
		return config;
	}
	/**
	 * 腾讯TGP奖励配置
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqTgpPublicConfig createQqTgpPublicConfig(Map<String, Object> tmp) {
		QqTgpPublicConfig config = new QqTgpPublicConfig();
		Map<String, Integer> map = new HashMap<>();
		
		String jitem = CovertObjectUtil.object2String(tmp.get("item"));
		map = ConfigAnalysisUtils.getConfigMap(jitem);
		
		config.setTgpMap(new ReadOnlyMap<>(map));
		return config;
	}
	/**
	 * 腾讯TGP能量兑换配置
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqTgpDuiHuanPublicConfig createQqTgpDuiHuanPublicConfig(Map<String, Object> tmp) {
		QqTgpDuiHuanPublicConfig config = new QqTgpDuiHuanPublicConfig();
		Map<String, Map<String, Integer>> tgpMap = new HashMap<>();
		
		for (int i = 1; i < 10; i++) {
			String jitem = CovertObjectUtil.object2String(tmp.get("item"+i));
			if(jitem == null || "".equals(jitem)){
				break;
			}
			tgpMap.put(i+"", ConfigAnalysisUtils.getConfigMap(jitem));
		}
		
		config.setTgpMap(new ReadOnlyMap<>(tgpMap));
		
		return config;
	}
	/**
	 * 腾讯TGP能量兑换消耗配置
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqTgpDuiHuanXHPublicConfig createQqTgpDuiHuanXHPublicConfig(Map<String, Object> tmp) {
		QqTgpDuiHuanXHPublicConfig config = new QqTgpDuiHuanXHPublicConfig();
		Map<String, Integer> tgpMap = new HashMap<>();
		
		for (int i = 1; i < 10; i++) {
			Integer jitem = CovertObjectUtil.object2int(tmp.get("item"+i));
			if(jitem == null || jitem <= 0){
				break;
			}
			tgpMap.put(i+"", jitem);
		}
		
		config.setTgpMap(new ReadOnlyMap<>(tgpMap));
		
		return config;
	}
	/**
	 * 腾讯TGP能量公共配置
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqTgpNengLiangPublicConfig createQqTgpNengLiangPublicConfig(Map<String, Object> tmp) {
		QqTgpNengLiangPublicConfig config = new QqTgpNengLiangPublicConfig();
		Map<String, Integer> tgpMap = new HashMap<>();
		
		tgpMap.put("add", CovertObjectUtil.object2int(tmp.get("add")));//每日可增加点数
		tgpMap.put("add1", CovertObjectUtil.object2int(tmp.get("add1")));//每日可增加点数的条件
		tgpMap.put("jieshou", CovertObjectUtil.object2int(tmp.get("jieshou")));//每日最多可以接受次数
		tgpMap.put("zhuanpan", CovertObjectUtil.object2int(tmp.get("zhuanpan")));//转盘转一次要消耗的
		
		config.setTgpMap(new ReadOnlyMap<>(tgpMap));
		
		return config;
	}
	/**
	 * 腾讯TGP转盘配置
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqTgpZhuanPanPublicConfig createQqTgpZhuanPanPublicConfig(Map<String, Object> tmp) {
		QqTgpZhuanPanPublicConfig config = new QqTgpZhuanPanPublicConfig();
		Map<Map<String, Integer>,Integer> tgpMap = new HashMap<>();
		Map<Map<String, Integer>,Integer> geziMap = new HashMap<>();
		
		for (int i = 1; i < 10; i++) {
			String jitem = CovertObjectUtil.object2String(tmp.get("item"+i));
			if(jitem == null || "".equals(jitem)){
				break;
			}
			Integer quan = CovertObjectUtil.object2int(tmp.get("quan"+i));
			tgpMap.put(ConfigAnalysisUtils.getConfigMap(jitem), quan);
			geziMap.put(ConfigAnalysisUtils.getConfigMap(jitem), i);
		}
		
		config.setTgpMap(new ReadOnlyMap<>(tgpMap));
		config.setGeziMap(new ReadOnlyMap<>(geziMap));
		
		return config;
	}
	
	/**
	 * 腾讯TGP登录奖励配置
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqTgpPublicConfig createQqTgpLoginPublicConfig(Map<String, Object> tmp) {
		QqTgpPublicConfig config = new QqTgpPublicConfig();
		Map<String, Integer> map = new HashMap<>();
		
		String jitem = CovertObjectUtil.object2String(tmp.get("up"));
		map = ConfigAnalysisUtils.getConfigMap(jitem);
		
		config.setTgpMap(new ReadOnlyMap<>(map));
		return config;
	}
	/**
	 * 腾讯微端奖励配置
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqWeiDuanPublicConfig createQqWeiDuanPublicConfig(Map<String, Object> tmp) {
		QqWeiDuanPublicConfig config = new QqWeiDuanPublicConfig();
		Map<Integer, Map<String, Integer>> map = new HashMap<>();
		
		String jitem1 = CovertObjectUtil.object2String(tmp.get("1"));
		String jitem2 = CovertObjectUtil.object2String(tmp.get("2"));
		String jitem3 = CovertObjectUtil.object2String(tmp.get("3"));
		map.put(1, ConfigAnalysisUtils.getConfigMap(jitem1));
		map.put(2, ConfigAnalysisUtils.getConfigMap(jitem2));
		map.put(3, ConfigAnalysisUtils.getConfigMap(jitem3));
		
		config.setWeiDuanMap(new ReadOnlyMap<>(map));
		return config;
	}
	/**
	 * qq黄钻成长
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqHZchengzhangPublicConfig createQqHZchengzhangPublicConfig(Map<String, Object> tmp) {
		QqHZchengzhangPublicConfig config = new QqHZchengzhangPublicConfig();
		Map<Integer, Map<String, Integer>> items = new HashMap<Integer,Map<String,Integer>>();
		Map<Integer , Integer> levels = new HashMap<Integer ,Integer>();
		List<Integer> nList = new ArrayList<Integer>();
		for (String key : tmp.keySet()) {
			if(key.startsWith("lv")){
				String level = CovertObjectUtil.object2String(tmp.get(key));
				String paramSequence = key.substring("lv".length(), key.length());
				nList.add(Integer.parseInt(paramSequence));
				levels.put(Integer.parseInt(paramSequence), Integer.parseInt(level));
				String itemKey = "item"+paramSequence;
				String item = CovertObjectUtil.object2String(tmp.get(itemKey));
				Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
				items.put(Integer.valueOf(paramSequence), itemMap);
			}
		}
		Collections.sort(nList);
		config.setnList(nList);
		config.setLevels(levels);
		config.setItems(items);
		return config;
	}
	/**
	 * qq蓝钻成长
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqLZchengzhangPublicConfig createQqLZchengzhangPublicConfig(Map<String, Object> tmp) {
		QqLZchengzhangPublicConfig config = new QqLZchengzhangPublicConfig();
		Map<Integer, Map<String, Integer>> items = new HashMap<Integer,Map<String,Integer>>();
		Map<Integer , Integer> levels = new HashMap<Integer ,Integer>();
		List<Integer> nList = new ArrayList<Integer>();
		for (String key : tmp.keySet()) {
			if(key.startsWith("lv")){
				String level = CovertObjectUtil.object2String(tmp.get(key));
				String paramSequence = key.substring("lv".length(), key.length());
				nList.add(Integer.parseInt(paramSequence));
				levels.put(Integer.parseInt(paramSequence), Integer.parseInt(level));
				String itemKey = "item"+paramSequence;
				String item = CovertObjectUtil.object2String(tmp.get(itemKey));
				Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
				items.put(Integer.valueOf(paramSequence), itemMap);
			}
		}
		Collections.sort(nList);
		config.setnList(nList);
		config.setLevels(levels);
		config.setItems(items);
		return config;
	}
	/**
	 * TGP成长等级礼包
	 * 
	 * @param tmp
	 * @return
	 */
	private static QqTgpchengzhangPublicConfig createQqTgpchengzhangPublicConfig(Map<String, Object> tmp) {
		QqTgpchengzhangPublicConfig config = new QqTgpchengzhangPublicConfig();
		Map<Integer, Map<String, Integer>> items = new HashMap<Integer,Map<String,Integer>>();
		Map<Integer , Integer> levels = new HashMap<Integer ,Integer>();

		for (int i = 1; i < 10; i++) {
			Integer level = CovertObjectUtil.object2int(tmp.get("lv"+i));
			if(level == null){
				break;
			}
			levels.put(i, level);
			String item = CovertObjectUtil.object2String(tmp.get("item"+i));
			if(item == null || "".equals(item)){
				break;
			}
			items.put(level, ConfigAnalysisUtils.getConfigMap(item));
		}
		config.setLevels(levels);
		config.setItems(items);
		return config;
	}
	
	/**
	 * 越南邀请好友
	 */
	private static YueNanYaoQingPublicConfig createYueNanYaoQingPublicConfig(Map<String, Object> tmp){
		YueNanYaoQingPublicConfig config = new YueNanYaoQingPublicConfig();
		config.setItemMap(ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("jiangitem"))));
		config.setLingQuCount(CovertObjectUtil.object2int(tmp.get("jiangnum")));
		config.setYaoqingCount(CovertObjectUtil.object2int(tmp.get("yqfriends")));
		
		return config;
	}
	/**
	 * 360|37 V计划 消费礼包
	 */
	private static PtCommonPublicConfig getVLevelXfLbPublicConfigData(Map<String, Object> tmp) {
		PtCommonPublicConfig config = new PtCommonPublicConfig();
		Map<String, Object> levelMap  = new HashMap<>();
		int len = tmp.size()/2+1;
		for (int i = 1; i <len; i++) {//只有5个档位
			Integer needGold =  CovertObjectUtil.object2Integer(tmp.get("gold"+i));//消费等级
			Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap((String) tmp.get("item"+i));
			config.setItems(i+"", itemMap);//建立一个奖励的map
			levelMap.put(i+"", needGold);
			config.setInfo(levelMap);//建立一个消费等级的map
		}
		return config;
	}
	/**
	 * 360|37 V计划等级升级礼包
	 */
	private static PtCommonPublicConfig getVLevelUpgradeLbPublicConfigData(Map<String, Object> tmp) {
		PtCommonPublicConfig config = new PtCommonPublicConfig();
		Map<String, Object> levelMap  = new HashMap<>();
		int len = tmp.size()/2+1;
		for (int i = 1; i <len; i++) {//只有四个档位
			String level =  CovertObjectUtil.object2String(tmp.get("lv"+i));//等级
			Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap((String) tmp.get("item"+i));
			config.setItems(i+"", itemMap);//建立一个奖励的map
			levelMap.put(i+"", level);
			config.setInfo(levelMap);//建立一个等级的map
		}
		return config;
	}
	/**
	 * 360 v计划年，月会员属性奖励
	 */
	private static _360VplanAttrConfig getVmonthAttrPublicConfigData(Map<String, Object> tmp,int type) {
		_360VplanAttrConfig config = new _360VplanAttrConfig();
		String str  = type==1?"yue":"nian";
		for (int i = 1; i <6; i++) {
			Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap((String) tmp.get(str+i));
			Map<String, Long> attrMap  = new HashMap<>();
			for (Entry<String, Integer> entry : itemMap.entrySet()) {
				attrMap.put(entry.getKey(), Long.valueOf(entry.getValue()));
			} 
			config.addAttrMap(i+"", attrMap);//月会员等级=attrMap
		}
		return config;
	}
	/**
	 * 组装数据公共方法
	 * 没特殊礼包配置，数据统一在这里面创建
	 * @return
	 */
	private static PtCommonPublicConfig getPublicConfigData(Map<String, Object> tmp) {
		PtCommonPublicConfig config = new PtCommonPublicConfig();
		Set<Map.Entry<String, Object>> entryseSet = tmp.entrySet();
		for (Map.Entry<String, Object> entry : entryseSet) {
			//针对有道具的奖励解析
			Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap((String) entry.getValue());
			config.setItems(entry.getKey(),new ReadOnlyMap<>(itemMap)); //{key:{itemId:num}}
		}
		return config;
	}
	/**
	 * 组装数据公共方法
	 * 这里不解析具体道具  直接返回配置的value。
	 * 如：不解析1001:2|1003:2|1101:2，直接返回这个字符串
	 * @return
	 */
	private static PtCommonPublicConfig getPublicConfigData2(Map<String, Object> tmp) {
		PtCommonPublicConfig config = new PtCommonPublicConfig();
		Set<Map.Entry<String, Object>> entryseSet = tmp.entrySet();
		Map<String, Object> infoMap = new HashMap<>();
		for (Map.Entry<String, Object> entry : entryseSet) {
			//直接表里填什么存什么
			infoMap.put(entry.getKey(), (Integer)entry.getValue());
			config.setInfo(new ReadOnlyMap<>(infoMap));
		}
		return config;
	}

	/**
     * qq游戏大厅特权配置解析
     * @param tmp 解析数据源
     * @param mId 特权key
     * @return
     */
    private static AdapterPublicConfig createQqGameRewardPublicConfig(Map<String, Object> tmp, String mId) {
        if(mId.equals(PlatformPublicConfigConstants.QQ_GAME_XINSHOU)){
            QqGameXinShouPublicConfig config = new QqGameXinShouPublicConfig();
            String item = CovertObjectUtil.object2String(tmp.get(QqGameConstants.XINSHOU_REWARD_KEY));
            Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
            config.setXinShouItem(itemMap);
            return config;
        }else if(mId.equals(PlatformPublicConfigConstants.QQ_GAME_LEVEL)){
            QqGameLevelPublicConfig config = new QqGameLevelPublicConfig();
            Map<Integer, Map<String, Integer>> levelItem = new HashMap<Integer,Map<String,Integer>>();
            String lv_prefix = QqGameConstants.LEVEL_REWARD_LV_PREFIX;
            String item_prefix = QqGameConstants.LEVEL_REWARD_ITEM_PREFIX;
            Set<String> ketSet = tmp.keySet();
            for (String key : ketSet) {
                if(key.startsWith(lv_prefix)){
                    int level =CovertObjectUtil.object2int(tmp.get(key));
                    String suffix = key.substring(lv_prefix.length(), key.length());
                    String item = CovertObjectUtil.object2String(tmp.get(item_prefix+suffix));
                    Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
                    levelItem.put(level, itemMap);
                }
            }
            config.setLevelItem(levelItem);
            return config;
        }else if(mId.equals(PlatformPublicConfigConstants.QQ_GAME_EVERY_DAY)){
            QqGameEveryDayPublicConfig config = new QqGameEveryDayPublicConfig();
            String item = CovertObjectUtil.object2String(tmp.get(QqGameConstants.EVERY_DAY_REWARD_KEY));
            Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
            config.setEveryDayItem(itemMap);
            return config;
        }
        return null;
    }
    
    /**
     * qq空间特权配置解析
     * @param tmp 解析数据源
     * @param mId 特权key
     * @return
     */
    private static AdapterPublicConfig createQqQzoneRewardPublicConfig(Map<String, Object> tmp, String mId) {
        if(mId.equals(PlatformPublicConfigConstants.QQ_QZONE_XINSHOU)){
            QqQzoneXinShouPublicConfig config = new QqQzoneXinShouPublicConfig();
            String item = CovertObjectUtil.object2String(tmp.get(QqQzoneConstants.XINSHOU_REWARD_KEY));
            Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
            config.setXinShouItem(itemMap);
            return config;
        }else if(mId.equals(PlatformPublicConfigConstants.QQ_QZONE_LEVEL)){
            QqQzoneLevelPublicConfig config = new QqQzoneLevelPublicConfig();
            Map<Integer, Map<String, Integer>> levelItem = new HashMap<Integer,Map<String,Integer>>();
            String lv_prefix = QqQzoneConstants.LEVEL_REWARD_LV_PREFIX;
            String item_prefix = QqQzoneConstants.LEVEL_REWARD_ITEM_PREFIX;
            Set<String> ketSet = tmp.keySet();
            for (String key : ketSet) {
                if(key.startsWith(lv_prefix)){
                    int level =CovertObjectUtil.object2int(tmp.get(key));
                    String suffix = key.substring(lv_prefix.length(), key.length());
                    String item = CovertObjectUtil.object2String(tmp.get(item_prefix+suffix));
                    Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
                    levelItem.put(level, itemMap);
                }
            }
            config.setLevelItem(levelItem);
            return config;
        }else if(mId.equals(PlatformPublicConfigConstants.QQ_QZONE_EVERY_DAY)){
            QqQzoneEveryDayPublicConfig config = new QqQzoneEveryDayPublicConfig();
            String item = CovertObjectUtil.object2String(tmp.get(QqQzoneConstants.EVERY_DAY_REWARD_KEY));
            Map<String, Integer> itemMap = ConfigAnalysisUtils.getConfigMap(item);
            config.setEveryDayItem(itemMap);
            return config;
        }
        return null;
    }

}
