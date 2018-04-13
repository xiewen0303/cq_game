package com.junyou.bus.assign.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.checker.GoodsConfigChecker;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyList;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 签到
 * @author jy
 *
 */
@Component
public class QianDaoJiangLiConfigExportService extends AbsClasspathConfigureParser {
	 
	
	/**
	  * configFileName
	 */
	private String configureName = "QianDaoJiangLi.jat";
	
	private Map<Integer,QianDaoJiangLiConfig> assignConfigs;
	
	/**
	 * 签到配置id有序
	 */
	private List<Integer> QD_IDS;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			ChuanQiLog.errorConfig("QianDaoJiangLi.jat is null!");
			return;
		}
		
		Map<Integer,QianDaoJiangLiConfig> configs = new HashMap<Integer, QianDaoJiangLiConfig>();
		List<Integer> list = new ArrayList<>();
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				QianDaoJiangLiConfig config = createAssignConfig(tmp);
				configs.put(config.getId(), config);
				
				//加入有序id
				list.add(config.getId());
				GoodsConfigChecker.registCheck(config);
			}
		}
		
		//加入有序id
		QD_IDS = new ReadOnlyList<>(list);
		this.assignConfigs = configs;
	}
	
	/**
	 * @param tmp
	 * @return
	 */
	public QianDaoJiangLiConfig createAssignConfig(Map<String, Object> tmp) {
		QianDaoJiangLiConfig config = new QianDaoJiangLiConfig();	
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setCount(CovertObjectUtil.object2int(tmp.get("cishu")));
		Map<String,Integer> rewardMap = ConfigAnalysisUtils.getConfigMap(CovertObjectUtil.object2String(tmp.get("jiangitem1")));
		config.setAwards(new ReadOnlyMap<>(rewardMap));
		return config;
	}
		
	protected String getConfigureName() {
		return configureName;
	}
	
	public QianDaoJiangLiConfig loadById(Integer id){
		return assignConfigs.get(id);
	}
	
	/**
	 * 获取所有配置id列表
	 * @return
	 */
	public List<Integer> getAllConfigIds(){
		return QD_IDS;
	}
	
}