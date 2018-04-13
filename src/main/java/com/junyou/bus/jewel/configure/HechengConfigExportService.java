package com.junyou.bus.jewel.configure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 合成表
 * @author lxn
 *
 */
@Component
public class HechengConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "HeChengBiao.jat";  
	private Map<String, HechengConfig> configs; //{ 道具id1,HechengConfig}
	private List<String> id1List  = new ArrayList<>(); 
	

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if (data == null)
			return;
		Object[] dataList = GameConfigUtil.getResource(data);
		this.configs  =  new HashMap<>();
		try {
			for (Object obj : dataList) {
				Map<String, Object> tmp = (Map<String, Object>)obj;
				HechengConfig vo=createData(tmp);
				if(vo!=null){
					this.configs.put(vo.getId1(), vo);
				}
			}
			
		} catch (Exception e) {
			ChuanQiLog.error("HeChengBiao表解析有问题");
		}
		
	}
	public List<String> getId1List() {
		return id1List;
	}
	public void setId1List(List<String> id1List) {
		this.id1List = id1List;
	}
	private HechengConfig createData(Map<String, Object> tmp){
		HechengConfig config = new HechengConfig();
		String id1 = CovertObjectUtil.object2String(tmp.get("id1"));
		String nextid = CovertObjectUtil.object2String(tmp.get("nextid"));
		int needmoney = CovertObjectUtil.object2int(tmp.get("needmoney"));
		int neednub = CovertObjectUtil.object2int(tmp.get("neednub"));
		config.setId1(id1);
		config.setNextLevelid(nextid);
		config.setNeedmoney(needmoney);
		config.setNeednum(neednub);
		id1List.add(id1);
		return  config;
	}
	protected String getConfigureName() {
		return configureName;
	}
	@SuppressWarnings("unchecked")
	public <P> P loadPublicConfig(String id1) {
		return (P) configs.get(id1);
	}
}
