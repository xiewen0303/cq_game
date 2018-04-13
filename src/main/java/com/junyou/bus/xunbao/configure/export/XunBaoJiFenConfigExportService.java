package com.junyou.bus.xunbao.configure.export;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.loader.ConfigMd5SignManange;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 寻宝积分配置配置表 
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-2-5 下午2:36:38
 */
@Component
public class XunBaoJiFenConfigExportService extends AbsClasspathConfigureParser   {
	 
	
	/**
	  * configFileName
	 */
	private String configureName = "XunBaoJiFenBiao.jat";
	
	private Map<Integer,XunBaoJiFenConfig> xunBaoJiFenBiaoConfigs = new HashMap<Integer, XunBaoJiFenConfig>();
	
	private Map<Integer,Object[]> xunBaoJfGroups = new HashMap<>();
	
	private Object[] xunBaoJFenGroups;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		
		//配置文件MD5值加入管理
		ConfigMd5SignManange.addConfigSign(configureName, data);
		List<Object[]> xunBaoJiFenfzGroups = new ArrayList<>();
		Map<Integer,List<XunBaoJiFenConfig>> xunBaoJiFenGroupsMap = new HashMap<>();
		
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				XunBaoJiFenConfig config = createXunBaoJiFenBiaoConfig(tmp);
				xunBaoJiFenBiaoConfigs.put(config.getId(), config);
				
				List<XunBaoJiFenConfig>  fenzhuJFs = xunBaoJiFenGroupsMap.get(config.getFenzu());
				if(fenzhuJFs == null){
					 fenzhuJFs = new ArrayList<>();
					 xunBaoJiFenGroupsMap.put(config.getFenzu(), fenzhuJFs);
					 //寻宝积分分组页签
					 xunBaoJiFenfzGroups.add(new Object[]{config.getFenzu(),config.getName()});
				 }
				 fenzhuJFs.add(config);
			}
		}
		
		if(xunBaoJiFenfzGroups.size() > 0){
			xunBaoJFenGroups = xunBaoJiFenfzGroups.toArray();
		}
		
		//数据转成数据组，业务用的时候，直接用不用再转换
		if(xunBaoJiFenGroupsMap.size() > 0){
			for (Map.Entry<Integer, List<XunBaoJiFenConfig>> entery : xunBaoJiFenGroupsMap.entrySet()) {
				
				List<XunBaoJiFenConfig> list = entery.getValue();
				Object[] arr = new Object[list.size()];
				for (int i = 0; i < list.size(); i++) {
					arr[i] = list.get(i).getConfigData();
				}
				
				xunBaoJfGroups.put(entery.getKey(), arr);
			}
		}
	}
	
	public XunBaoJiFenConfig createXunBaoJiFenBiaoConfig(Map<String, Object> tmp) {
		XunBaoJiFenConfig config = new XunBaoJiFenConfig();	
		
		config.setId(CovertObjectUtil.object2int(tmp.get("id")));
		config.setName(CovertObjectUtil.object2String(tmp.get("name")));
		config.setNeeditem(CovertObjectUtil.object2String(tmp.get("needitem")));
		config.setNeedxbjifen(CovertObjectUtil.object2int(tmp.get("needxbjifen")));
		config.setItemid(CovertObjectUtil.object2String(tmp.get("itemid")));
		config.setFenzu(CovertObjectUtil.object2int(tmp.get("fenzu")));
							
		return config;
	} 
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public XunBaoJiFenConfig loadById(Integer id){
		return xunBaoJiFenBiaoConfigs.get(id);
	}
	
	/**
	 * 寻宝积分信息表
	 * @param fzId
	 * @return
	 */
	public Object[] getXunBaoJiFenBiaoConfigList(Integer fzId) {
		return xunBaoJfGroups.get(fzId); 
	}

	public Object[] getXunBaoJiFenFZConfig() {
		return xunBaoJFenGroups;
	}
}