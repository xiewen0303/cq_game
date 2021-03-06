package com.junyou.bus.shenmo.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.shenmo.configure.ShenMoPaiHangConfig;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class ShenMoPaiHangConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, ShenMoPaiHangConfig> configs = null;

	private int maxJifen = 0;
	private int maxDuan = 0;
	/**
	 * configFileName
	 */
	private String configureName = "ShenMoDuanWei.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		Map<Integer, ShenMoPaiHangConfig> configs = new HashMap<>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				ShenMoPaiHangConfig config = createShenMoPaiHangConfig(tmp);
				configs.put(config.getDuan(), config);
				if (config.getJifen() > maxJifen) {
					maxJifen = config.getJifen();
					maxDuan = config.getDuan();
				}
			}
		}
		
		this.configs = configs;
	}

	public ShenMoPaiHangConfig createShenMoPaiHangConfig(Map<String, Object> tmp) {
		ShenMoPaiHangConfig config = new ShenMoPaiHangConfig();
		config.setDuan(CovertObjectUtil.object2int(tmp.get("id")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setJifen(CovertObjectUtil.object2int(tmp.get("jifen")));
		config.setWingx(CovertObjectUtil.object2int(tmp.get("wingx")));
		config.setWinjf(CovertObjectUtil.object2int(tmp.get("winjf")));
		config.setLosejf(CovertObjectUtil.object2int(tmp.get("losejf")));
		config.setJiangcs(CovertObjectUtil.object2int(tmp.get("jiangcs")));
		config.setGongxun(CovertObjectUtil.object2int(tmp.get("gongxun")));
		config.setWinExp(CovertObjectUtil.object2int(tmp.get("winexp")));
		config.setLoseExp(CovertObjectUtil.object2int(tmp.get("loseexp")));
		return config;
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
	public ShenMoPaiHangConfig loadById(int duan) {
		return configs.get(duan);
	}

	public int getDuanByJifen(int jifen) {
		if (jifen >= maxJifen) {
			return maxDuan;
		}
		for (int i = maxDuan; i > 1; i--) {
			ShenMoPaiHangConfig preConfig = configs.get(i - 1);
			ShenMoPaiHangConfig config = configs.get(i);
			if (config.getJifen() > jifen && preConfig.getJifen() <= jifen) {
				return preConfig.getDuan();
			}
		}
		if(jifen<configs.get(1).getJifen()){
			return 1;
		}
		ChuanQiLog.error("shenmo jifen error when get duan");
		throw new RuntimeException("shenmo jifen error when get duan");
	}

	public int getMaxDuan() {
		return maxDuan;
	}

}
