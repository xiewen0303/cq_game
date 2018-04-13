package com.junyou.bus.shenqi.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.chibang.configure.export.ChiBangJiChuConfig;
import com.junyou.bus.chibang.configure.export.ChiBangJiChuConfigExportService;
import com.junyou.bus.shenqi.ShenQiConstants;
import com.junyou.bus.wuqi.configure.export.XinShengJianJiChuConfig;
import com.junyou.bus.wuqi.configure.export.XinShengJianJiChuConfigExportService;
import com.junyou.bus.xianjian.configure.export.XianJianJiChuConfig;
import com.junyou.bus.xianjian.configure.export.XianJianJiChuConfigExportService;
import com.junyou.bus.zhanjia.configure.export.ZhanJiaJiChuConfig;
import com.junyou.bus.zhanjia.configure.export.ZhanJiaJiChuConfigExportService;
import com.junyou.bus.zuoqi.configure.export.YuJianJiChuConfig;
import com.junyou.bus.zuoqi.configure.export.YuJianJiChuConfigExportService;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class ShuXingZhuFuConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, Map<Integer, ShuXingZhuFuConfig>> configs;

	private Map<Integer, Integer> leixingMinLevelMap;

	private Map<Integer, Integer> leixingMaxLevelMap;

	/**
	 * configFileName
	 */
	private String configureName = "ShuXingZhuFu.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, Map<Integer, ShuXingZhuFuConfig>>();
		leixingMinLevelMap = new HashMap<Integer, Integer>();
		leixingMaxLevelMap = new HashMap<Integer, Integer>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ShuXingZhuFuConfig config = createShuXingZhuFuConfig(tmp);
				Map<Integer, ShuXingZhuFuConfig> map = configs.get(config
						.getLeixing());
				if (map == null) {
					map = new HashMap<Integer, ShuXingZhuFuConfig>();
					configs.put(config.getLeixing(), map);
				}
				map.put(config.getLevel(), config);

				Integer minLevel = leixingMinLevelMap.get(config.getLeixing());
				if (minLevel == null) {
					leixingMinLevelMap.put(config.getLeixing(),
							config.getLevel());
				} else {
					if (minLevel > config.getLevel()) {
						leixingMinLevelMap.put(config.getLeixing(),
								config.getLevel());
					}
				}

				Integer maxLevel = leixingMaxLevelMap.get(config.getLeixing());
				if (maxLevel == null) {
					leixingMaxLevelMap.put(config.getLeixing(),
							config.getLevel());
				} else {
					if (maxLevel < config.getLevel()) {
						leixingMaxLevelMap.put(config.getLeixing(),
								config.getLevel());
					}
				}
			}
		}
	}

	public ShuXingZhuFuConfig createShuXingZhuFuConfig(Map<String, Object> tmp) {
		ShuXingZhuFuConfig config = new ShuXingZhuFuConfig();

		config.setLeixing(CovertObjectUtil.object2int(tmp.get("leixing")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));

		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));

		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	/**
	 * 根据祝福类型获得最低的祝福等级
	 * 
	 * @param type
	 * @return
	 */
	public int getMinZhufuLevel(int type) {
		return leixingMinLevelMap.get(type);
	}

	/**
	 * 根据祝福类型获得最高的祝福等级
	 * 
	 * @param type
	 * @return
	 */
	public int getMaxZhufuLevel(int type) {
		return leixingMaxLevelMap.get(type);
	}
	/**
	 * 根据祝福类型，跟祝福等级获得
	 * @param type
	 * @param configId
	 * @return
	 */
	public ShuXingZhuFuConfig getByTypeAndId(int type, int configId) {
		int level = 0;
		switch(type){
		case ShenQiConstants.ZHUFU_TYPE_WUQI:
			XinShengJianJiChuConfig config5 = xinShengJianConfigService.loadById(configId);
			if(config5 != null){
				level = config5.getLevel();
			}
			break;
		case ShenQiConstants.ZHUFU_TYPE_ZHANJIA:
			ZhanJiaJiChuConfig  config4 = zhanJiaJiChuConfigExportService.loadById(configId);
			if(config4 != null){
				level = config4.getLevel();
			}
			break;
		case ShenQiConstants.ZHUFU_TYPE_XIANJIAN:
			XianJianJiChuConfig  config3 = xianJianJiChuConfigExportService.loadById(configId);
			if(config3 != null){
				level = config3.getLevel();
			}
			break;
		case ShenQiConstants.ZHUFU_TYPE_CHIBANG:
			ChiBangJiChuConfig  config2 = chiBangJiChuConfigExportService.loadById(configId);
			if(config2 != null){
				level = config2.getLevel();
			}
			break;
		case ShenQiConstants.ZHUFU_TYPE_ZUOQI:
			YuJianJiChuConfig  config1 = yuJianJiChuConfigExportService.loadById(configId);
			if(config1 != null){
				level = config1.getLevel();
			}
			break;
			default:
				ChuanQiLog.error("getByTypeAndLevel type is exist, type="+type);
				
		}
		return configs.get(type) == null?null:configs.get(type).get(level);
	}
	
	@Autowired
	private XinShengJianJiChuConfigExportService xinShengJianConfigService;
	@Autowired
	private ZhanJiaJiChuConfigExportService zhanJiaJiChuConfigExportService;
	@Autowired
	private XianJianJiChuConfigExportService xianJianJiChuConfigExportService;
	@Autowired
	private ChiBangJiChuConfigExportService chiBangJiChuConfigExportService;
	@Autowired
	private YuJianJiChuConfigExportService yuJianJiChuConfigExportService;
	
	/**
	 * 判断等级是否达到此类型祝福的范围内
	 * @param type
	 * @param level 服务器御剑，翅膀，仙剑 存的是从0 开始
	 */
	public boolean isZhufuActivated(int type,int level){
		int minLevel = getMinZhufuLevel(type);
		int maxLevel = getMaxZhufuLevel(type);
		if(level+1 <= maxLevel && level+1 >= minLevel){
			return true;
		}
		return false;
	}
}
