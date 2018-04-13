package com.junyou.bus.jewel.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.collection.ReadOnlyMap;
import com.junyou.utils.common.CovertObjectUtil;

/**宝石开孔表
 * @author lxn
 *
 */
@Component
public class JewelKaiKongConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "BaoShiKaiKong.jat";  
	
	private Map<Integer, JewelKaiKongConfig> configs;  

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if (data == null)
			return;
		Object[] dataList = GameConfigUtil.getResource(data);
		this.configs  =  new HashMap<>();
		try {
			for (Object obj : dataList) {
				Map<String, Object> tmp = (Map<String, Object>)obj;
				JewelKaiKongConfig vo=createData(tmp);
				if(vo!=null){
					this.configs.put(vo.getGeiWeiId(), vo);
				}
			}
		} catch (Exception e) {
			ChuanQiLog.error("BaoShiKaiKong表解析有问题");
		}
		
	}
	private JewelKaiKongConfig createData(Map<String, Object> tmp){
		JewelKaiKongConfig config = new JewelKaiKongConfig();
		 Map<Integer, Object[]> consumeMap  = new HashMap<>();
		Integer id = CovertObjectUtil.object2int(tmp.get("gewei"));
		for (int i = 2; i < 6; i++) {
			Integer kongId =  CovertObjectUtil.object2int(tmp.get("kong"+i));
			int needLevel =  CovertObjectUtil.object2int(tmp.get("needlevel"+i));
			int needMoney =  CovertObjectUtil.object2int(tmp.get("needmoney"+i));
			consumeMap.put(kongId, new Object[]{kongId,needLevel,needMoney});
		}
		config.setGeiWeiId(id);
		config.setConsumeMap(new ReadOnlyMap<>(consumeMap));
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
