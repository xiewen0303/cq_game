package com.junyou.bus.xunbao.configure.export;

 
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.lottery.Lottery;
 

/**
 * 寻宝物品组包表
 * @author DaoZheng Yuan
 * 2015年6月2日 下午8:02:19
 */
@Component
public class XunBaoZuBiaoConfigExportService  extends AbsClasspathConfigureParser{
	/**
	 * 组包掉组包最多找5次,多余5次的,算做错误.
	 */
	private static final int maxFindCount = 5;
	/**
	  * configFileName
	 */
	private String configureName = "XunBaoZuBao.jat";
	
	private Map<String,XunBaoZuBiaoConfig> goodsGroupConfigs = new HashMap<>();
	

	
	protected String getConfigureName() {
		return configureName;
	}
	 
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data); 
	
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				XunBaoZuBiaoConfig config = createWuPinZuBiaoConfig(tmp); 
				if(config != null){
					goodsGroupConfigs.put(config.getId(), config);
				}
			}
		}
	}
	
	public XunBaoZuBiaoConfig createWuPinZuBiaoConfig(Map<String, Object> tmp) {
		String id = CovertObjectUtil.object2String(tmp.get("id"));
		if(CovertObjectUtil.isEmpty(id)){
			return null;
		}
		
		XunBaoZuBiaoConfig config = new XunBaoZuBiaoConfig();	
		config.setId(id);
		
		int allOdds = 0;
		int i=1;
		while(i<100){
			
			String drop = CovertObjectUtil.object2String(tmp.get(GameConstants.DROP+i));
			Integer dropCount=CovertObjectUtil.object2int(tmp.get(GameConstants.DROP+i+GameConstants.COUNT));
			
			if("".equals(drop) || dropCount == 0){
				break;
			}
			int dropOdds=CovertObjectUtil.object2int(tmp.get(GameConstants.DROP+i+GameConstants.ODDS));
			allOdds+=dropOdds;
			int index = drop.indexOf(GameConstants.ZB_FLAG);
			boolean isZB= index != -1 ? true:false;
			
			config.addGoodsOdds(new Object[]{drop,dropCount,isZB},dropOdds);  
			
			i++;
		}
		
		config.setAllOdds(allOdds);
		
		return config;
	}
 
	
	/**
	 * 根据组包roll规则  TODO
	 * @return goodsId,goodsCount
	 */
	public Map<String,Integer> componentRoll(String componentDropId) {
		XunBaoZuBiaoConfig zuBaoConfig = loadById(componentDropId);
		if(zuBaoConfig == null){
			return null;
		}
		
		Map<String,Integer> finalMap = new HashMap<String, Integer>();
		
		dropZubao(componentDropId, finalMap, 1);
		
		return finalMap;
	}
	
	public XunBaoZuBiaoConfig  loadById(String componentDropId) {
		return goodsGroupConfigs.get(componentDropId);
	}


	/**
	 * roll组包
	 * @param zubaoOddsList
	 * @param finalMap
	 * @param layer 层
	 */
	private void dropZubao(String dropId,Map<String,Integer> finalMap,int layer){
		
		/**
		 * 组包嵌套达到5层,自动停止,并且会给默认物品
		 */
		if(layer >= maxFindCount){
			finalMap.put("money", 1);
			return;
		}
		
		XunBaoZuBiaoConfig zuBaoConfig = goodsGroupConfigs.get(dropId);
		if(zuBaoConfig == null){
			finalMap.put("money", 1);
			return;
		}
		
		Map<Object[], Integer> zubaoOddsList = zuBaoConfig.getDatas();
		
		Object[] wuPinZuBaoValue = Lottery.getRandomKeyByInteger(zubaoOddsList, zuBaoConfig.getAllOdds());//RandomKey(zubaoOddsList);
		if(wuPinZuBaoValue != null){
			boolean isZB = (Boolean)wuPinZuBaoValue[2];
			String groupId=(String)wuPinZuBaoValue[0];
			int goodsCount =(Integer)wuPinZuBaoValue[1];
			
			if(isZB){
				
				for (int i = 1; i <= goodsCount; i++) {
					dropZubao(groupId, finalMap, ++layer);
				}
				
			}else{
				Integer oldCount = finalMap.get(groupId);
				finalMap.put(groupId,goodsCount+(oldCount==null?0:oldCount));
			}
			
		}
		
	} 
}