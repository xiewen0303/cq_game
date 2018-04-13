package com.junyou.bus.equip.configure.export;
 
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 随机属性表 
 *
 * @author wind
 * @date 2015-01-10 15:09:16
 */
@Component
public class SuiJiShuXingConfigExportService extends AbsClasspathConfigureParser {
 
	/**
	  * configFileName
	 */
	private String configureName = "SuiJiShuXingBiao.jat";
	Map<Integer,SuiJiShuXingConfig> datas=new HashMap<>(); 
	
	//groupId =<id=odds>      （所属分组=<id=概率>）
	Map<Integer,Map<Integer,Float>> groups=new HashMap<Integer, Map<Integer,Float>>();
	
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if(data==null){
			return;
		}
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		
		Object[] dataList = GameConfigUtil.getResource(data);
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				SuiJiShuXingConfig config = createSuiJiShuXingConfig(tmp);
				if(config == null){
					continue;
				}
				Integer fenZu=config.getFenzu();
				Map<Integer, Float> idOdds=groups.get(fenZu);
				if(idOdds==null){
					idOdds=new HashMap<>();
					groups.put(fenZu, idOdds);
				}
				idOdds.put(config.getId(), config.getOdds());
				datas.put(config.getId(), config);
			}
		}
	}
	
	private SuiJiShuXingConfig createSuiJiShuXingConfig(Map<String, Object> tmp) {
		int id = CovertObjectUtil.object2int(tmp.get("id"));
		if(id < 1){
			return null;
		}
		SuiJiShuXingConfig config = new SuiJiShuXingConfig();	
		config.setId(id);
		config.setFenzu(CovertObjectUtil.object2int(tmp.get("fenzu")));		
		config.setNextId(CovertObjectUtil.object2Integer(tmp.get("lvid")));
		config.setTpId(CovertObjectUtil.object2Integer(tmp.get("tpid")));
		config.setLevel(CovertObjectUtil.object2int(tmp.get("lv")));
		config.setOdds(CovertObjectUtil.obj2float(tmp.get("odds")));
		
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		config.setAttrs(attrs);
		
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public Map<String,Long> getAttrsById(Integer id){
		SuiJiShuXingConfig  config = datas.get(id);
		return config.getAttrs();
	}
	
	public Map<Integer,Float> getRandomGroup(int groupId){
		return groups.get(groupId);
	}
	
	public SuiJiShuXingConfig getConfig(Integer id){
		return datas.get(id);
	}
	
	public int getMaxSize(){
		return datas.size();
	}
}