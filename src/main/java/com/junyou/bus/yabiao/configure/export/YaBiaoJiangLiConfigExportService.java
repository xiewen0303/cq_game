package com.junyou.bus.yabiao.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 押镖奖励配置 
 *
 * @author LiNing
 * @date 2015-03-13 17:05:04
 */
@Component
public class YaBiaoJiangLiConfigExportService extends AbsClasspathConfigureParser{
	/**
	  * configFileName
	 */
	private String configureName = "YaBiaoJiangLi.jat";
	private Map<Integer, List<YaBiaoJiangLiConfig>> ybJiangliMap;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer, List<YaBiaoJiangLiConfig>> tmpYbJiangliMap = new HashMap<>();
		
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				YaBiaoJiangLiConfig config = createYaBiaoJiangLiConfig(tmp);
				
				List<YaBiaoJiangLiConfig> list = tmpYbJiangliMap.get(config.getId());
				if(list == null){
					list = new ArrayList<>();
				}
				list.add(config);
				tmpYbJiangliMap.put(config.getId(), list);
			}
		}
		this.ybJiangliMap = tmpYbJiangliMap;
	}
	
	public YaBiaoJiangLiConfig createYaBiaoJiangLiConfig(Map<String, Object> tmp) {
		YaBiaoJiangLiConfig config = new YaBiaoJiangLiConfig();	
							
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setMaxlevel(CovertObjectUtil.object2int(tmp.get("maxlevel")));
		config.setJiangexp(CovertObjectUtil.object2int(tmp.get("jiangexp")));
		config.setMinlevel(CovertObjectUtil.object2int(tmp.get("minlevel")));
		config.setJiangmoney(CovertObjectUtil.object2int(tmp.get("jiangmoney")));	
		config.setJiangzhenqi(CovertObjectUtil.object2int(tmp.get("jiangzhenqi")));
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	/**
	 * 根据镖车id和玩家等级获取押镖奖励配置
	 * @param id
	 * @param level
	 * @return
	 */
	public YaBiaoJiangLiConfig loadById(Integer id, Integer level){
		List<YaBiaoJiangLiConfig> list = ybJiangliMap.get(id);
		if(list == null || list.size() <= 0 || level == null){
			return null;
		}
		
		for (YaBiaoJiangLiConfig config : list) {
			//玩家等级区间判定：minlevel≤玩家等级≤maxlevel
			if(config.getMinlevel() <= level && level <= config.getMaxlevel()){
				return config;
			}
		}
		return null;
	}
}