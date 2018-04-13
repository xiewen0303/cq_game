package com.junyou.bus.jewel.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**宝石表
 * @author lxn
 *
 */
@Component
public class JewelConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "BaoShiBiao.jat";  
	private Map<Integer, JewelConfig> configs; //{宝石id,JewelConfig}

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if (data == null)
			return;
		Object[] dataList = GameConfigUtil.getResource(data);
		this.configs  =  new HashMap<>();
		try {
			for (Object obj : dataList) {
				Map<String, Object> tmp = (Map<String, Object>)obj;
				JewelConfig vo=createData(tmp);
				if(vo!=null){
					this.configs.put(vo.getId(), vo);
				}
			}
		} catch (Exception e) {
			ChuanQiLog.error("BaoShiBiao表解析有问题");
		}
		
	}
	private JewelConfig createData(Map<String, Object> tmp){
		JewelConfig config = new JewelConfig();
		Integer id = CovertObjectUtil.object2int(tmp.get("id"));
		int level = CovertObjectUtil.object2int(tmp.get("level"));
		int type = CovertObjectUtil.object2int(tmp.get("type"));
		Integer nextid = CovertObjectUtil.object2int(tmp.get("nextid"));
		int needmoney = CovertObjectUtil.object2int(tmp.get("needmoney"));
		int neednub = CovertObjectUtil.object2int(tmp.get("neednub"));
		config.setId(id);
		config.setLevel(level);
		config.setType(type);
		config.setNextLevelid(nextid);
		config.setNeedmoney(needmoney);
		config.setNeednum(neednub);
		Map<String, Long> attrs = ConfigAnalysisUtils.setAttributeVal(tmp);
		if(attrs!=null){
			config.setAttrMap(new ReadOnlyMap<>(attrs));
		}
		return  config;
	}
	protected String getConfigureName() {
		return configureName;
	}
	@SuppressWarnings("unchecked")
	public <P> P loadPublicConfig(Integer key) {
		return (P) configs.get(key);
	}
}
