package com.junyou.stage.campwar.confiure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 阵营战奖励配置 
 *
 * @author LiNing
 * @date 2015-04-10 04:16:57
 */
@Component
public class ZhenYinZhanJiangLiConfigExportService extends AbsClasspathConfigureParser {
	
	/**
	  * configFileName
	 */
	private String configureName = "ZhenYinZhanJiangLi.jat";
	private Map<Integer, ZhenYinZhanJiangLiConfig> zyItemMap;
	private final int winPuItemId = -1;//胜方普奖的Id
	private final int losePuItemId = -2;//败方普奖的Id
	private Integer MAX = -1;//最大Id
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		// 配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		Map<Integer, ZhenYinZhanJiangLiConfig> tmpZyItemMap = new HashMap<>();
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZhenYinZhanJiangLiConfig config = createZhenYinZhanJiangLiConfig(tmp);
						
				if(MAX == -1 || MAX < config.getPaiming()){
					MAX = config.getPaiming();
				}
				
				tmpZyItemMap.put(config.getPaiming(), config);
			}
		}
		
		this.zyItemMap = tmpZyItemMap;
	}
	
	public ZhenYinZhanJiangLiConfig createZhenYinZhanJiangLiConfig(Map<String, Object> tmp) {
		ZhenYinZhanJiangLiConfig config = new ZhenYinZhanJiangLiConfig();	
							
		config.setJlItem(CovertObjectUtil.object2String(tmp.get("jiangli")));
		config.setPaiming(CovertObjectUtil.object2int(tmp.get("paiming")));
							
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	/**
	 * 获取排名的所有奖励配置
	 * @param id
	 * @return
	 */
	public Map<Integer, ZhenYinZhanJiangLiConfig> getRankConfig(){
		return zyItemMap;
	}
	
	/**
	 * 获取胜利普奖的奖励配置
	 * @return
	 */
	public Integer getRankWinPjId(){
		return winPuItemId;
	}
	/**
	 * 获取失败普奖的奖励配置
	 * @return
	 */
	public Integer getRankLosePjId(){
		return losePuItemId;
	}
	
	/**
	 * 获取最大的排名次数
	 * @return
	 */
	public Integer getMaxPaiming(){
		return MAX;
	}
}