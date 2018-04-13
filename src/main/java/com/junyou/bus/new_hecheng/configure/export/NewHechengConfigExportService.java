package com.junyou.bus.new_hecheng.configure.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

@Service
public class NewHechengConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "XinHeChengBiao.jat";
	private Map<Integer, NewHechengConfig> configs = new HashMap<>();

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if (data == null)
			return;
		Object[] dataList = GameConfigUtil.getResource(data);

		try {
			for (Object obj : dataList) {
				Map<String, Object> tmp = (Map<String, Object>) obj;
				NewHechengConfig vo = createData(tmp);
				if (vo != null) {
					this.configs.put(vo.getId(), vo);
				}
			}

		} catch (Exception e) {
			ChuanQiLog.error(configureName + "解析有问题");
		}

	}

	private NewHechengConfig createData(Map<String, Object> tmp) {
		NewHechengConfig config = new NewHechengConfig();
		config.setId(CovertObjectUtil.object2Integer(tmp.get("id")));
		config.setFromItem(CovertObjectUtil.object2String(tmp.get("hechengitem")));
		config.setClientGailv(CovertObjectUtil.object2Float(tmp.get("gailv")));
		config.setToItem(CovertObjectUtil.object2String(tmp.get("nextid")));
		config.setRealGaiLv(CovertObjectUtil.object2Float((tmp.get("zhenshigv"))));
		return config;
	}

	protected String getConfigureName() {
		return configureName;
	}

	@SuppressWarnings("unchecked")
	public <P> P loadPublicConfig(int id) {
		return (P) configs.get(id);
	}
}
