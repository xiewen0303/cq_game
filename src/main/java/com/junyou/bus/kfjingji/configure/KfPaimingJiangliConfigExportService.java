package com.junyou.bus.kfjingji.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyList;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-10-26 下午4:57:58
 */
@Service
public class KfPaimingJiangliConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "KuaFuPaiMingJiangLi.jat";
	private Map<Integer, KfPaimingJiangliConfig> configs;
	private List<Integer> mingciList;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, KfPaimingJiangliConfig> configs = new HashMap<>();
		List<Integer> mingciList = new ArrayList<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				KfPaimingJiangliConfig config = createKfPaimingJiangliConfig(tmp);
				configs.put(config.getRank(), config);
				mingciList.add(config.getRank());
			}
		}
		this.configs = configs;
		this.mingciList = new ReadOnlyList<>(mingciList);
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	private KfPaimingJiangliConfig createKfPaimingJiangliConfig(Map<String, Object> tmp){
		KfPaimingJiangliConfig config = new KfPaimingJiangliConfig();
		config.setRank(CovertObjectUtil.object2int(tmp.get("ranking")));
		config.setExp(CovertObjectUtil.object2int(tmp.get("jiangitem1")));
		config.setMoney(CovertObjectUtil.object2int(tmp.get("jiangitem2")));
		config.setShenhun(CovertObjectUtil.object2int(tmp.get("jiangitem")));
		config.setShow(CovertObjectUtil.object2int(tmp.get("show")));
		return config;
	}
	/**
	 * list不可修改
	 * @return
	 */
	public List<Integer> getMingCiList(){
		return mingciList;
	}
	
	public KfPaimingJiangliConfig loadById(Integer rank){
		return configs.get(rank);
	}
	public KfPaimingJiangliConfig loadByRank(Integer rank){
		for (Integer id : mingciList) {
			if(rank <= id){
				return configs.get(id);
			}
		}
		return null;
	}
	
	public int getMinRank(int rank){
		int min = 0;
		for (Integer mc : mingciList) {
			if(mc < rank){
				min = mc;
			}else{
				break;
			}
		}
		return min+1;
	}
}
