package com.junyou.bus.huoyuedu.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;

/**活跃度奖励表
 * @author lxn
 *
 */
@Component
public class HuoYueDuAwardConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "HuoYueDuJiangLi.jat"; //活跃度奖励表
	private Map<Integer, HuoYueDuAwardConfig> configs; //id:{奖励}

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if (data == null)
			return;
		Object[] dataList = GameConfigUtil.getResource(data);
		this.configs  =  new HashMap<>();
		try {
			for (Object obj : dataList) {
				Map<String, Object> tmp = (Map<String, Object>)obj;
				HuoYueDuAwardConfig vo = new HuoYueDuAwardConfig();
				vo.setId((Integer)tmp.get("id"));
				vo.setItems(ConfigAnalysisUtils.getConfigMap((String)tmp.get("item")));
				vo.setJifen((Integer)tmp.get("jifen"));
				this.configs.put(vo.getId(), vo);
			}
		} catch (Exception e) {
			ChuanQiLog.debug("HuoYueDuJiangLi表解析有问题");
		}
		
	}

	protected String getConfigureName() {
		return configureName;
	}

	public  Map<Integer, HuoYueDuAwardConfig> getAllData(){
		
		return configs;
	}
	
	@SuppressWarnings("unchecked")
	public <P> P loadPublicConfig(Integer key) {
		return (P) configs.get(key);
	}
}
