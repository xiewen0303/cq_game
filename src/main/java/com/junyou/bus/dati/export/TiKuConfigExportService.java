package com.junyou.bus.dati.export;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.junyou.bus.dati.configure.TiMuConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class TiKuConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "TiKu.jat";
	private Map<Integer, TiMuConfig> tiKuConfig;

	@Override
	protected void configureDataResolve(byte[] data) {
		ConcurrentMap<Integer, TiMuConfig> configs = new ConcurrentHashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>) obj;
			if (null != tmp) {
				TiMuConfig config = createTiMu(tmp);
				configs.put(config.getId(), config);
			}
		}
		this.tiKuConfig = configs;

	}

	private TiMuConfig createTiMu(Map<String, Object> tmp) {
		TiMuConfig timu=new TiMuConfig();
		timu.setId(CovertObjectUtil.object2int(tmp.get("id")));
		timu.setTitle(String.valueOf(tmp.get("neirong")));
		timu.setRight(CovertObjectUtil.object2int(tmp.get("right")));
		timu.getAnswers().add( String.valueOf(tmp.get("answer1")));
		timu.getAnswers().add( String.valueOf(tmp.get("answer2")));
		timu.getAnswers().add( String.valueOf(tmp.get("answer3")));
		timu.getAnswers().add( String.valueOf(tmp.get("answer4")));
		return timu;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public Map<Integer, TiMuConfig> getTiKuConfig() {
		return tiKuConfig;
	}



}
