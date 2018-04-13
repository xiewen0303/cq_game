package com.junyou.bus.xianqi.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class XianYuanFeiHuaConfigExportService extends AbsClasspathConfigureParser {

	private Map<Integer, XianYuanFeiHuaConfig> configs = null;
	private String configureName = "XianYuanFeiHuaBiao.jat";

	private int maxLevel;

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		// 配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		configs = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				XianYuanFeiHuaConfig config = createConfig(tmp);
				if (config.getLevel() > getMaxLevel()) {
					setMaxLevel(config.getLevel());
				}
				configs.put(config.getLevel(), config);
			}
		}

	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public  void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	private XianYuanFeiHuaConfig createConfig(Map<String, Object> tmp) {
		XianYuanFeiHuaConfig config = new XianYuanFeiHuaConfig();
		config.setLevel(CovertObjectUtil.object2int(tmp.get("lv")));
		config.setCount(CovertObjectUtil.object2int(tmp.get("count")));
		config.setMoney(CovertObjectUtil.object2int(tmp.get("money")));
		config.setGoodsId(CovertObjectUtil.object2String(tmp.get("id1")));
		config.setMallId(CovertObjectUtil.object2String(tmp.get("mallid")));
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
		config.setZfzMin(CovertObjectUtil.object2int(tmp.get("zfzmin")));
		config.setZfzMax(CovertObjectUtil.object2int(tmp.get("zfzmax")));
		config.setSuccessRate(CovertObjectUtil.object2int(tmp.get("pro")));
		config.setZfzMinAdd(CovertObjectUtil.object2int(tmp.get("zfzmin2")));
		config.setZfzMaxAdd(CovertObjectUtil.object2int(tmp.get("zfzmin3")));
		config.setResetZfzTime(CovertObjectUtil.object2boolean(tmp.get("zfztime")));
		config.setCzTime(CovertObjectUtil.object2int(tmp.get("cztime")));
		config.setSendNotice(CovertObjectUtil.object2boolean(tmp.get("ggopen")));

		config.setNeedLevel(CovertObjectUtil.object2int(tmp.get("needlevel")));
		config.setNeedQianghua(CovertObjectUtil.object2int(tmp.get("needqianghua")));
		config.setNeedYujian(CovertObjectUtil.object2int(tmp.get("needyujian")));
		config.setNeedChibang(CovertObjectUtil.object2int(tmp.get("needchibang")));
		config.setNeedQiling(CovertObjectUtil.object2int(tmp.get("needqiling")));
		config.setNeedTiangong(CovertObjectUtil.object2int(tmp.get("needtiangong")));
		config.setNeedTianshang(CovertObjectUtil.object2int(tmp.get("needtianshang")));
		config.setNeedTianyu(CovertObjectUtil.object2int(tmp.get("needtianyu")));
		config.setNeedWuxing(CovertObjectUtil.object2int(tmp.get("needwuxing")));
		config.setNeedXinmo(CovertObjectUtil.object2int(tmp.get("needxinmo")));
		config.setNeedXianjue(CovertObjectUtil.object2int(tmp.get("needxianjue")));
		config.setNeedVip(CovertObjectUtil.object2int(tmp.get("needvip")));

		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(new ReadOnlyMap<>(attrs));
		return config;
	}

	public XianYuanFeiHuaConfig loadByLevel(int level) {
		if(configs ==null){
			return null;
		}
		return configs.get(level);
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public List<String> getConsumeIds(String id1s) {
		List<String> result = new ArrayList<>();
		if(id1s != null){
				List<String> ids = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(id1s);
				if(ids != null){
					result.addAll(ids);
				}else{
					ChuanQiLog.error("仙缘飞化配置错误 :没有找到大类{}对应的物品配置信息",id1s);
				}
		}
		return result; 
	}

}
