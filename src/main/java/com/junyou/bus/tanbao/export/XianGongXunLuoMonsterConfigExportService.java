package com.junyou.bus.tanbao.export;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.bus.tanbao.entity.XianGongXunLuoMonsterConfig;
import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.stage.model.core.stage.aoi.AoiPoint;
import com.junyou.stage.model.core.stage.aoi.AoiPointManager;
import com.junyou.utils.collection.ReadOnlyList;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * @author LiuYu
 * 2015-6-17 下午10:15:14
 */
@Service
public class XianGongXunLuoMonsterConfigExportService extends AbsClasspathConfigureParser{
	
	private String configureName = "XunLuoGuai.jat";
	private List<XianGongXunLuoMonsterConfig> configs = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Override
	protected void configureDataResolve(byte[] data) {
		Object[] listData = GameConfigUtil.getResource(data);
		List<XianGongXunLuoMonsterConfig> configs = new ArrayList<>();
		for (Object obj:listData) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			XianGongXunLuoMonsterConfig config = createXianGongXunLuoMonsterConfig(tmp);
			if(config != null){
				configs.add(config);
			}
		}
		this.configs = new ReadOnlyList<>(configs);
	}
	
	private XianGongXunLuoMonsterConfig createXianGongXunLuoMonsterConfig(Map<String, Object> tmp){
		XianGongXunLuoMonsterConfig config = new XianGongXunLuoMonsterConfig();
		String id = CovertObjectUtil.obj2StrOrNull(tmp.get("id"));
		if(id == null){
			return null;
		}
		config.setMonsterId(id);
		
		List<AoiPoint> lujing = new ArrayList<>();
		String zuobiao = CovertObjectUtil.obj2StrOrNull(tmp.get("zuobiao"));
		for (String point : zuobiao.split(GameConstants.CONFIG_SPLIT_CHAR)) {
			String[] info = point.split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
			lujing.add(AoiPointManager.getAoiPoint(Integer.parseInt(info[0]),Integer.parseInt(info[1])));
		}
		config.setLujing(lujing);
		return config;
	}

	@Override
	protected String getConfigureName() {
		return configureName;
	}
	
	public List<XianGongXunLuoMonsterConfig> getAllConfigs(){
		return configs;
	}
	
}
