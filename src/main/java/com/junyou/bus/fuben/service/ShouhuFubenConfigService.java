package com.junyou.bus.fuben.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.ShouhuFubenConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyList;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;

@Service
public class ShouhuFubenConfigService extends AbsClasspathConfigureParser {
	private final String configureName = "ShouWeiFuBen.jat";
	private Map<Integer,ShouhuFubenConfig> configs;
	
	/**
	 * key:配置id,value:奖励位索引
	 */
	private Map<Integer,Integer> guanIndex;
	
	private List<Integer> guanKaIds;
	
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		Map<Integer,ShouhuFubenConfig> configs = new HashMap<>();
		Map<Integer,Integer> guanIndex = new HashMap<>();
		List<Integer> tmpKaIds = new ArrayList<>();
		
		int index = 0;
		for (Object obj : dataList) {
			@SuppressWarnings("unchecked")
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ShouhuFubenConfig config = createShouhuFubenConfig(tmp);
				configs.put(config.getId(), config);
				if(!ObjectUtil.strIsEmpty(config.getItemId())){
					guanIndex.put(config.getId(),index++);
					
					//有序的关卡配置id
					tmpKaIds.add(config.getId());
				}
				

				//注册需要解析的配置
				GoodsConfigChecker.registCheck(config);
			}
		}
		
		this.guanKaIds = new ReadOnlyList<>(tmpKaIds);
		this.configs = configs;
		this.guanIndex = guanIndex;
	}

	private ShouhuFubenConfig createShouhuFubenConfig(Map<String, Object> tmp){
		ShouhuFubenConfig config = new ShouhuFubenConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
//		config.setFuhuo(false);
		config.setMoney(CovertObjectUtil.object2int(tmp.get("jiangmoney")));
		config.setExp(CovertObjectUtil.object2int(tmp.get("jiangexp")));
		config.setZhenqi(CovertObjectUtil.object2int(tmp.get("jiangzhen")));
		config.setItemId(CovertObjectUtil.obj2StrOrNull(tmp.get("shoucijiangli")));
		config.setTime(CovertObjectUtil.object2int(tmp.get("time")));
		config.setNeedMoney(CovertObjectUtil.object2int(tmp.get("needmoney")));
		String monster = CovertObjectUtil.object2String(tmp.get("monster"));
		if(!ObjectUtil.strIsEmpty(monster)){
			String[] monsterInfo = monster.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			if(monsterInfo.length < 2){
				ChuanQiLog.error("ShouhuFubenConfig is err.id:" + config.getId());
			}
			config.setMonsterId(monsterInfo[0]);
			config.setMonsterCount(Integer.parseInt(monsterInfo[1]));
			Map<String,Integer> map = new HashMap<>();
			map.put(config.getMonsterId(), config.getMonsterCount());
			config.setWantedMap(map);
		}
		
		return config;
	}
	
	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public ShouhuFubenConfig loadById(Integer id){
		return configs.get(id);
	}
			
	public Integer getGiftIndexById(Integer id){
		return guanIndex.get(id);
	}
	
	
	/**
	 * 获取关卡id列表
	 * @return
	 */
	public List<Integer> getGuanKaIds(){
		return guanKaIds;
	}

}
