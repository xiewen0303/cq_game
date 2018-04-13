package com.junyou.bus.kuafuarena1v1.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

@Component
public class GeRenPaiHangConfigExportService extends
		AbsClasspathConfigureParser {

	private Map<Integer, GeRenPaiHangConfig> configs = null;

	private int maxJifen = 0;
	private int maxDuan = 0;
	/**
	 * configFileName
	 */
	private String configureName = "GeRenPaiHang.jat";

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {

		ConfigMd5SignManange.addConfigSign(configureName, data);

		configs = new HashMap<Integer, GeRenPaiHangConfig>();

		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				GeRenPaiHangConfig config = createGeRenPaiHangConfig(tmp);
				configs.put(config.getDuan(), config);
				if (config.getJifen() > maxJifen) {
					maxJifen = config.getJifen();
					maxDuan = config.getDuan();
				}
			}
		}
	}

	public GeRenPaiHangConfig createGeRenPaiHangConfig(Map<String, Object> tmp) {
		GeRenPaiHangConfig config = new GeRenPaiHangConfig();
		config.setDuan(CovertObjectUtil.object2int(tmp.get("id")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setJifen(CovertObjectUtil.object2int(tmp.get("jifen")));
		config.setWingx(CovertObjectUtil.object2int(tmp.get("wingx")));
		config.setWinjf(CovertObjectUtil.object2int(tmp.get("winjf")));
		config.setLosejf(CovertObjectUtil.object2int(tmp.get("losejf")));
		config.setJiangcs(CovertObjectUtil.object2int(tmp.get("jiangcs")));
		config.setGongxun(CovertObjectUtil.object2int(tmp.get("gongxun")));
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
	public GeRenPaiHangConfig loadById(int duan) {
		return configs.get(duan);
	}

	public int getDuanByJifen(int jifen) {
		if (jifen >= maxJifen) {
			return maxDuan;
		}
		for (int i = maxDuan; i > 1; i--) {
			GeRenPaiHangConfig preConfig = configs.get(i - 1);
			GeRenPaiHangConfig config = configs.get(i);
			if (config.getJifen() > jifen && preConfig.getJifen() <= jifen) {
				return preConfig.getDuan();
			}
		}
		ChuanQiLog.error("jifen error when get duan");
		throw new RuntimeException("jifen error when get duan");
	}

	public int getMaxDuan() {
		return maxDuan;
	}

}
