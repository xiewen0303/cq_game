package com.junyou.gameconfig.goods.configure.export;
 
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;
 

/**
 * 道具限制配置表  
  *@author: wind
  *@email: 18221610336@163.com
  *@version: 2014-12-18下午2:43:38
  *@Description:
 */
@Component
public class DaoJuXzConfigExportService{
	 
	 
	/**
	  * configFileName
	 */
	private String configureName = "DaoJuXianZhiBiao.jat";
	
	
	private Map<Integer,DaoJuXzConfig> daojuXzConfigs=new HashMap<Integer, DaoJuXzConfig>();
	
	public DaoJuXzConfig createDaoJuXzConfig(Map<String, Object> tmp) {
		DaoJuXzConfig config = new DaoJuXzConfig();
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		
		Map<Integer,Integer> data=new HashMap<Integer, Integer>();
		for (int i = 0; i <= 20; i++) {
			//每日使用次数限制：  0或不填：无限制  >0的整数：限制次数 
			int value = CovertObjectUtil.object2int(tmp.get("x"+i));
			if(value != 0){
				data.put(i, value);
			}
		}
		config.addUserTimes(new ReadOnlyMap<>(data));
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	/**
	 * 若位null 表示配置中不存在【道具限制组id】（goodsIdType） 为0表示不限制使用次数
	 * @param limitId：道具限制组id
	 * @param vipLevel
	 * @return
	 */
	public Integer getLimitUseCount(int limitId,int vipLevel){
		DaoJuXzConfig daoJuXzConfig=daojuXzConfigs.get(limitId);
		if(daoJuXzConfig==null){
			return null;
		}
		Integer count=daoJuXzConfig.getUseTimes(vipLevel);
		if(count==null){
			return 0;
		}
		return count;
	}

//	@Override
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				DaoJuXzConfig config = createDaoJuXzConfig(tmp);
								
				daojuXzConfigs.put(config.getId(), config);
			}
		}
	}

//	@Override
	protected String[] getGroupConfigureNames() {
		return new String[]{configureName};
	}
}