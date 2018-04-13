package com.junyou.bus.hongbao.configure;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

/**红包表
 * @author lxn
 */
@Component
public class HongbaoConfigExportService extends AbsClasspathConfigureParser {

	private String configureName = "HongBao.jat";  
	private Map<Integer, HongbaoConfig> configs; 

	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		if (data == null)
			return;
		Object[] dataList = GameConfigUtil.getResource(data);
		try {
			configs  = new HashMap<>();
			for (Object obj : dataList) {
				Map<String, Object> tmp = (Map<String, Object>)obj;
				HongbaoConfig hongbaoConfig = createData(tmp);
				if(hongbaoConfig!=null){
					configs.put(hongbaoConfig.getId(),hongbaoConfig);
				}
			}
		} catch (Exception e) {
			ChuanQiLog.error("HongBao表解析有问题");
		}
		
	}
	private HongbaoConfig createData(Map<String, Object> tmp){
		HongbaoConfig hongbaoConfig  = new HongbaoConfig();
		int id = CovertObjectUtil.object2int(tmp.get("id"));
		hongbaoConfig.setId(id);
		if(id!=-1){
			int type = CovertObjectUtil.object2int(tmp.get("type"));
			int count  = 0 ; //总和
			for (int i = 1; i <= tmp.size(); i++) {
				int value = CovertObjectUtil.object2int(tmp.get("item"+i+"odds"));
				count+=value;
			}
			Map<String,Integer>  rankMap  = new HashMap<>();
			for (int i = 1; i <= tmp.size(); i++) {
				String item = CovertObjectUtil.object2String(tmp.get("item"+i));
				if(item==null||item.length()==0){
					continue;
				}
				int odds = CovertObjectUtil.object2int(tmp.get("item"+i+"odds"));
				if(rankMap.get(item)!=null){
					//容错处理：处理key相同的情况
					odds+=rankMap.get(item);
				}
				rankMap.put(item,odds);
			}
			hongbaoConfig.setCount(count);
			hongbaoConfig.setDataMap(rankMap);
			hongbaoConfig.setType(type);
		}else{
			int gold = CovertObjectUtil.object2int(tmp.get("gold"));
			int gailv = CovertObjectUtil.object2int(tmp.get("gailv"));
			hongbaoConfig.setGailv(gailv);
			hongbaoConfig.setGold(gold);
		}
		return hongbaoConfig;
	}
	protected String getConfigureName() {
		return configureName;
	}
	@SuppressWarnings("unchecked")
	public <P> P loadPublicConfig(Integer type) {
		return (P) configs.get(type);
	}
}
