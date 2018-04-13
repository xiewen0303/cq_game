package com.junyou.bus.jingji.export;

import com.junyou.bus.daytask.configure.BeginEnd;
import com.junyou.bus.jingji.entity.MingCiZhanShiConfig;
import com.junyou.bus.jingji.entity.RankShowConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyList;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.lottery.RandomUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RankShowConfigExportService extends AbsClasspathConfigureParser{

	private String configureName = "RankRefresh.jat";
	private Map<Integer, RankShowConfig> configs;
	private RankShowConfig defauftConfig;

	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, RankShowConfig> configs = new HashMap<>();

		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				RankShowConfig config = createMingCiZhanShiConfig(tmp);
				configs.put(config.getId(), config);
				defauftConfig = config;
			}
		}
		this.configs = configs;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	private RankShowConfig createMingCiZhanShiConfig(Map<String, Object> tmp){
		RankShowConfig config = new RankShowConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setBeginEnd(new BeginEnd(CovertObjectUtil.object2int(tmp.get("min")),CovertObjectUtil.object2int(tmp.get("max"))));

		String pain1 = CovertObjectUtil.obj2StrOrNull(tmp.get("paim1"));
		if(pain1 != null  ){
			String[] pain1s = pain1.split(";");
			if(pain1s.length >= 2){
				config.setRank1Begin(CovertObjectUtil.object2int(pain1s[0]));
				config.setRank1End(CovertObjectUtil.object2int(pain1s[1]));
			}
		}

		String pain2 = CovertObjectUtil.obj2StrOrNull(tmp.get("paim2"));
		if(pain2 != null  ){
			String[] pain2s = pain2.split(";");
			if(pain2s.length >= 2){
				config.setRank2Begin(CovertObjectUtil.object2int(pain2s[0]));
				config.setRank2End(CovertObjectUtil.object2int(pain2s[1]));
			}
		}

		String pain3 = CovertObjectUtil.obj2StrOrNull(tmp.get("paim3"));
		if(pain3 != null  ){
			String[] pain3s = pain3.split(";");
			if(pain3s.length >= 2){
				config.setRank3Begin(CovertObjectUtil.object2int(pain3s[0]));
				config.setRank3End(CovertObjectUtil.object2int(pain3s[1]));
			}
		}
		return config;
	}

	public int[] getRanks(int rank){
		if(configs == null){
			ChuanQiLog.error("MingCiZhanShi config is null !");
			return null;
		}

		int[] result = new int[3];

		RankShowConfig rankShowConfig = null;
		for (RankShowConfig element : configs.values()) {
			if(element.getBeginEnd().isContain(rank)){
				rankShowConfig = element;
				break;
			}
		}

		if(rankShowConfig == null) {
			rankShowConfig = defauftConfig;
		}

		result[0] = RandomUtil.getRondom(rank - rankShowConfig.getRank1Begin(),rank - rankShowConfig.getRank1End());
		result[1] = RandomUtil.getRondom(rank - rankShowConfig.getRank2Begin(),rank - rankShowConfig.getRank2End());
		result[2] = RandomUtil.getRondom(rank - rankShowConfig.getRank3Begin(),rank - rankShowConfig.getRank3End());
		return result;
	}
}
