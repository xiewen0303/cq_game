package com.junyou.bus.kfjingji.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyList;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-10-26 下午4:57:58
 */
@Service
public class KfJingjiMingciZhanshiConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "KuaFuMingCiZhanShi.jat";
	private Map<Integer, KfJingjiMingciZhanshiConfig> configs;
	private List<Integer> mingciList;
	private int maxRank;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, KfJingjiMingciZhanshiConfig> configs = new HashMap<>();
		List<Integer> mingciList = new ArrayList<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				KfJingjiMingciZhanshiConfig config = createKfJingjiMingciZhanshiConfig(tmp);
				configs.put(config.getPaiming(), config);
				mingciList.add(config.getPaiming());
				maxRank = config.getPaiming();
			}
		}
		this.configs = configs;
		this.mingciList = new ReadOnlyList<>(mingciList);
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	private KfJingjiMingciZhanshiConfig createKfJingjiMingciZhanshiConfig(Map<String, Object> tmp){
		KfJingjiMingciZhanshiConfig config = new KfJingjiMingciZhanshiConfig();
		config.setPaiming(CovertObjectUtil.object2int(tmp.get("paim")));
		List<int[]> list = new ArrayList<>();
		for (int i = 1; i < 5; i++) {
			String pm = CovertObjectUtil.obj2StrOrNull(tmp.get("paim"+i));
			if(pm == null){
				break;
			}
			String[] pmInfo = pm.split(GameConstants.CONFIG_SPLIT_CHAR);
			int rank = CovertObjectUtil.object2int(pmInfo[0]);
			int rank2 = 0;
			if(pmInfo.length > 1){
				rank2 = rank - CovertObjectUtil.object2int(pmInfo[1]) + 1;
			}
			list.add(new int[]{rank,rank2});
		}
		config.setOther(list);
		return config;
	}
	/**
	 * list不可修改
	 * @return
	 */
	public List<Integer> getMingCiList(){
		return mingciList;
	}
	
	public KfJingjiMingciZhanshiConfig loadById(Integer rank){
		return configs.get(rank);
	}
	public KfJingjiMingciZhanshiConfig loadByRank(Integer rank){
		for (Integer id : mingciList) {
			if(rank <= id){
				return configs.get(id);
			}
		}
		return null;
	}

	public int getMaxRank() {
		return maxRank;
	}
}
