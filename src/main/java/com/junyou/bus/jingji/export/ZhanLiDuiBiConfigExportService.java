package com.junyou.bus.jingji.export;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ZhanLiDuiBiConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "ZhanLiDuiBi.jat";

	private Map<Integer, List<Integer>> configs = new TreeMap<>();

	private	List<Integer>  allIDs = new ArrayList<>();

	@Override
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			ChuanQiLog.error("ZhanLiDuiBi is null !");
			return;
		}
		Object[] dataList = GameConfigUtil.getResource(data);

		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null == tmp) {
				continue;
			}

			int id = CovertObjectUtil.object2int(tmp.get("id"));
			int maintype = CovertObjectUtil.object2int(tmp.get("maintype"));

			List<Integer> childs = null;
			if((childs = configs.get(maintype)) == null) {
				childs = new ArrayList<>();
				configs.put(maintype,childs);
			}
			childs.add(id);

			allIDs.add(id);
		}
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}

	public List<Integer> getChilds(int type){
		return configs.get(type);
	}

	public List<Integer> getChilds(){
		return allIDs;
	}

	public  Map<Integer, List<Integer>> getAllConfigs(){
		return this.configs;
	}
}
