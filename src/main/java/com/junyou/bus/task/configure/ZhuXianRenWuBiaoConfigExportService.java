package com.junyou.bus.task.configure;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 主线任务表配置 
 *
 * @author LiNing
 * @date 2013-10-24 10:52:30
 */
@Component
public class ZhuXianRenWuBiaoConfigExportService extends AbsClasspathConfigureParser {
	
	private static Integer MIN = -1;
	
	/**
	  * configFileName
	 */
	private String configureName = "ZhuXianRenWuBiao.jat";
	
	private ConcurrentMap<Integer, ZhuXianRenWuBiaoConfig> configs;
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);
		ConcurrentMap<Integer, ZhuXianRenWuBiaoConfig> configs = new ConcurrentHashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZhuXianRenWuBiaoConfig config = createZhuXianRenWuBiaoConfig(tmp);
				
				if(MIN == -1 || MIN > config.getId()){
					MIN = config.getId();
				}
				configs.put(config.getId(), config);
				//注册需要解析的配置
				GoodsConfigChecker.registCheck(config);
			}
		}
		this.configs = configs;
	}
	
	public ZhuXianRenWuBiaoConfig createZhuXianRenWuBiaoConfig(Map<String, Object> tmp) {
		ZhuXianRenWuBiaoConfig config = new ZhuXianRenWuBiaoConfig();	
							
		config.setData1(CovertObjectUtil.object2String(tmp.get("data1")));
											
		config.setData2(CovertObjectUtil.object2int(tmp.get("data2")));
											
		config.setType(CovertObjectUtil.object2int(tmp.get("type")));
											
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
											
		config.setJiangexp(CovertObjectUtil.obj2long(tmp.get("jiangexp")));
											
		config.setMinlevel(CovertObjectUtil.object2int(tmp.get("minlevel")));
											
		config.setJiangmoney(CovertObjectUtil.obj2long(tmp.get("jiangmoney")));
		
		config.setZhenqi(CovertObjectUtil.obj2long(tmp.get("zhenqi")));
		
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		
		config.setRizhi(CovertObjectUtil.object2boolean(tmp.get("rizhi")));
					
		//奖励物品
		String tmpReward = CovertObjectUtil.object2String(tmp.get("jiangitem"));
		Map<String,Integer> publicRewardMap = ConfigAnalysisUtils.getConfigMap(tmpReward);
		config.setRewardItems(publicRewardMap);
		
		/**
		 * 专职物品
		 */
		
		Map<Byte,Map<String,Integer>> jobAward = new HashMap<>();
		
		//白子画
		String jobReward = CovertObjectUtil.object2String(tmp.get("jiangitem4"));
		Map<String,Integer> jobRewardMap = ConfigAnalysisUtils.getConfigMap(jobReward);
		if(jobRewardMap != null && jobRewardMap.size() > 0){
			jobAward.put(GameConstants.JOB_BZH, jobRewardMap);
		}
		
		//花千骨
		jobReward = CovertObjectUtil.object2String(tmp.get("jiangitem1"));
		jobRewardMap = ConfigAnalysisUtils.getConfigMap(jobReward);
		if(jobRewardMap != null && jobRewardMap.size() > 0){
			jobAward.put(GameConstants.JOB_HQG, jobRewardMap);
		}
		
		//杀阡陌
		jobReward = CovertObjectUtil.object2String(tmp.get("jiangitem2"));
		jobRewardMap = ConfigAnalysisUtils.getConfigMap(jobReward);
		if(jobRewardMap != null && jobRewardMap.size() > 0){
			jobAward.put(GameConstants.JOB_SQM, jobRewardMap);
		}
		
		//紫熏浅夏
		jobReward = CovertObjectUtil.object2String(tmp.get("jiangitem3"));
		jobRewardMap = ConfigAnalysisUtils.getConfigMap(jobReward);
		if(jobRewardMap != null && jobRewardMap.size() > 0){
			jobAward.put(GameConstants.JOB_ZXQX, jobRewardMap);
		}
		
		if(jobAward.size() > 0){
			config.setJobAward(jobAward);
		}
		
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ZhuXianRenWuBiaoConfig loadById(Integer id){
		if(id == null){
			id = MIN;
		}
		return configs.get(id);
	}
	
	public Integer getMin(){
		return this.MIN;
	}
	
	public ZhuXianRenWuBiaoConfig nextById(int id){
		for (int i = 1; i <= 5; i++) {
			ZhuXianRenWuBiaoConfig config = configs.get(id+i);
			if(config != null){
				return config;
			}
		}
		return null;
	}
}