package com.junyou.bus.huoyuedu.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;

/**
 * 活跃度内容表
 * 
 * @author lxn
 * 
 */
@Component
public class HuoYueDuContentConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "HuoYueDuNeiRong.jat";
	private Map<Integer, HuoYueDuContentConfig> configs;

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if (data == null)
			return;
		Object[] dataList = GameConfigUtil.getResource(data);
		this.configs = new HashMap<>();
		try {
			for (Object obj : dataList) {
				Map<String, Object> tmp = (Map<String, Object>) obj;
				HuoYueDuContentConfig vo = new HuoYueDuContentConfig();
				vo.setId((Integer) tmp.get("id"));
				vo.setJifen((Integer) tmp.get("jifen"));
				vo.setNeedNum((Integer) tmp.get("count"));
				Object object = tmp.get("data3");
				if(object!=null && !"".equals(object)){
					vo.setData((Integer)object);
				}
				this.configs.put(vo.getId(), vo);
			}
		} catch (Exception e) {
			ChuanQiLog.debug("HuoYueDuNeiRong表解析有问题");
		}
	}

	protected String getConfigureName() {
		return configureName;
	}

	@SuppressWarnings("unchecked")
	public <P> P loadPublicConfig(Integer key) {
		return (P) configs.get(key);
	}
}
