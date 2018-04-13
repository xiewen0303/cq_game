package com.junyou.bus.qiling.configure.export;

 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 器灵升阶表 
 *
 * @author wind
 * @date 2015-03-31 19:07:18
 */
@Component
public class QiLingJiChuConfigExportService extends AbsClasspathConfigureParser  {
	
	/**
	 * 器灵结束等级
	 */
	private int endLevel;
	
	private Map<Integer,QiLingJiChuConfig> configs = null;
	
	private String czdId =null;
	
	/**
	  * configFileName
	 */
	private String configureName = "QiLingJiChuBiao.jat";
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		Map<Integer,QiLingJiChuConfig>	configsTemp = new HashMap<Integer, QiLingJiChuConfig>();
		endLevel = 0;
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				QiLingJiChuConfig config = createYuJianJiChuConfig(tmp);
				if(czdId == null){
					czdId = config.getCzdid();
				}
				configsTemp.put(config.getId(), config);
				
				endLevel = endLevel < config.getId()?config.getId():endLevel; 
			}
		}
		
		configs= configsTemp;
	}
	
	public int getMaxJjLevel(){
		return endLevel;
	}
	
	public QiLingJiChuConfig createYuJianJiChuConfig(Map<String, Object> tmp) {
		QiLingJiChuConfig config = new QiLingJiChuConfig();	
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		
		Map<String,Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		
//		int moveAttrVal = attrs.remove(EffectType.x20.name());
//		config.setMoveAttrVal(moveAttrVal);
		
		config.setAttrs(new ReadOnlyMap<>(attrs));
		
		List<String> consumeItems =new ArrayList<>();
		
		String id1s = CovertObjectUtil.object2String(tmp.get("id1"));
		String[] id1Arrary =id1s.split("\\|");
		for (String id1 : id1Arrary) {
			consumeItems.add(id1);  
		}
		
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		
		config.setConsumeItems(consumeItems);
		
		config.setCzdopen(CovertObjectUtil.object2int(tmp.get("czdopen")));
							
		config.setCzdid(CovertObjectUtil.object2String(tmp.get("czdid")));
											
		config.setQndmax(CovertObjectUtil.object2int(tmp.get("qndmax")));
											
		config.setValue1(CovertObjectUtil.obj2StrOrNull(tmp.get("value1")));
											
		config.setLevelmax(CovertObjectUtil.object2int(tmp.get("levelmax")));
											
		config.setGgid(CovertObjectUtil.obj2StrOrNull(tmp.get("ggid")));
											
		config.setName(CovertObjectUtil.obj2StrOrNull(tmp.get("name")));
											
		config.setMoney(CovertObjectUtil.object2int(tmp.get("money")));
											
		config.setCztime(CovertObjectUtil.obj2float(tmp.get("cztime")));
											
		config.setGgopen(CovertObjectUtil.object2boolean(tmp.get("ggopen")));
											
		config.setZfzmin(CovertObjectUtil.object2int(tmp.get("zfzmin")));
											
		config.setZfzmin3(CovertObjectUtil.object2int(tmp.get("zfzmin3")));
											
		config.setZfzmin2(CovertObjectUtil.object2int(tmp.get("zfzmin2")));
											
		config.setQndid(CovertObjectUtil.object2String(tmp.get("qndid")));
											
		config.setZfztime(CovertObjectUtil.object2int(tmp.get("zfztime")) == 1);
											
		config.setNumber(CovertObjectUtil.object2int(tmp.get("count")));
											
		config.setCzdmax(CovertObjectUtil.object2int(tmp.get("czdmax")));
											
		config.setQndopen(CovertObjectUtil.object2int(tmp.get("qndopen")));
											
		config.setPro(CovertObjectUtil.object2int(tmp.get("pro")));
		
		config.setZfzmax(CovertObjectUtil.object2int(tmp.get("zfzmax")));
											
		config.setJinengge(CovertObjectUtil.object2int(tmp.get("jinengge")));
											
		config.setMallid(CovertObjectUtil.object2String(tmp.get("mallid")));
		
		config.setGold(CovertObjectUtil.object2int(tmp.get("gold")));
		config.setBgold(CovertObjectUtil.object2int(tmp.get("bgold")));
		 
		return config;
	}
	
	public List<String> getConsumeIds(List<String> id1s) {
		List<String> result = new ArrayList<>();
		if(id1s != null){
			for (String id1 : id1s) {
				List<String> ids = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(id1);
				if(ids != null){
					result.addAll(ids);
				}else{
					ChuanQiLog.error("");
				}
			}
		}
		return result; 
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public QiLingJiChuConfig loadById(int id){
		return configs.get(id);
	}
	public String getCzdId(){
		return czdId;
	}
}