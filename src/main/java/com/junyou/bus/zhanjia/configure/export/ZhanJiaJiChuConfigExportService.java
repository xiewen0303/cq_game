package com.junyou.bus.zhanjia.configure.export;

 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 仙剑基础表 
 *
 * @author wind
 * @date 2015-03-31 19:07:18
 */
@Component
public class ZhanJiaJiChuConfigExportService extends AbsClasspathConfigureParser  {
	
	/**
	 * 仙剑结束等级
	 */
	private int maxConfigId;
	
	/**
	 * 最大等级
	 */
	private int maxLevel;
	/**
	 * level - showId
	 */
	private Map<Integer,Integer> showIds = new HashMap<>();
	
	private Map<Integer,ZhanJiaJiChuConfig> configs = null;
	
	/**
	  * configFileName
	 */
//	private String configureName = "TangBaoZhanJia.jat";
	private String configureName = "TangBaoZhanJiaFenXingBiao.jat";
	
	
	private String czdId =null;
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		configs = new HashMap<Integer, ZhanJiaJiChuConfig>();
		maxConfigId = 0;
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				ZhanJiaJiChuConfig config = createYuJianJiChuConfig(tmp);
								
				configs.put(config.getId(), config);
				if(czdId == null){
					czdId = config.getCzdid();
				}
				
				maxConfigId = maxConfigId < config.getId()?config.getId():maxConfigId;
				maxLevel = maxLevel < config.getLevel()? config.getLevel():maxLevel;
			}
		}
	}
	
	public int getMaxConfigId(){
		return maxConfigId;
	}
	
	public ZhanJiaJiChuConfig createYuJianJiChuConfig(Map<String, Object> tmp) {
		ZhanJiaJiChuConfig config = new ZhanJiaJiChuConfig();	
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		if(config.getId() <0 ){
			return config;
		}
		Map<String,Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		
		config.setAttrs(new ReadOnlyMap<>(attrs));
		
		List<String> consumeItems =new ArrayList<>();
		
		String id1s = CovertObjectUtil.object2String(tmp.get("id1"));
		String[] id1Arrary =id1s.split("\\|");
		for (String id1 : id1Arrary) {
			consumeItems.add(id1);  
		}
		
		config.setConsumeItems(consumeItems);
		
		config.setLevel(CovertObjectUtil.object2int(tmp.get("level")));
		
		config.setCzdopen(CovertObjectUtil.object2int(tmp.get("czdopen")));
							
		config.setCzdid(CovertObjectUtil.object2String(tmp.get("czdid")));
											
		config.setQndmax(CovertObjectUtil.object2int(tmp.get("qndmax")));
											
		config.setValue1(CovertObjectUtil.obj2StrOrNull(tmp.get("value1")));
											
//		config.setLevelmax(CovertObjectUtil.object2int(tmp.get("levelmax")));
											
//		config.setGgid(CovertObjectUtil.obj2StrOrNull(tmp.get("ggid")));
											
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
		
		
		int odds1 =  CovertObjectUtil.object2int(tmp.get("odds1"));
		int baoji1 = CovertObjectUtil.object2int(tmp.get("baoji1"));
		int odds2 = CovertObjectUtil.object2int(tmp.get("odds2"));
		int baoji2 = CovertObjectUtil.object2int(tmp.get("baoji2"));
		
		config.getWeights().put(new int[]{GameConstants.RATIO_1,baoji1}, odds1);
		config.getWeights().put(new int[]{GameConstants.RATIO_2,baoji2}, odds2);
		config.getWeights().put(new int[]{GameConstants.RATIO_3,1}, 10000 - odds1 - odds2);
		config.setStar(CovertObjectUtil.object2int(tmp.get("star")));
		
		if(config.getStar() == 0){
			showIds.put(config.getLevel(),config.getId());
		}
		config.setShowId(CovertObjectUtil.object2int(showIds.get(config.getLevel())));
		
		this.tempZfz += config.getZfzmax();
		config.setRealNeedZfz(this.tempZfz);

		return config;
	}
	
	private int tempZfz = 0;
	
	public List<String> getConsumeIds(List<String> id1s,boolean isUseWND) {
		List<String> result = new ArrayList<>();
		if(id1s != null){
			for (String id1 : id1s) {
				if(isUseWND && GameConstants.WND_UPJIE.equals(id1)){
					continue;
				}
				List<String> ids = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(id1);
				result.addAll(ids);
			}
		}
		return result; 
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ZhanJiaJiChuConfig loadById(int id){
		return configs.get(id);
	}
	
	public String getCzdId(){
		return czdId;
	}
	
	public int getMaxLevel(){
		return maxLevel;
	}
	
	public Integer getShowId(int level){
		return showIds.get(level);
	}
}