package com.junyou.gameconfig.export;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.RoleBasePublicConfig;
import com.junyou.gameconfig.utils.ConfigAnalysisUtils;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.CovertObjectUtil;

/**
 * 
 * @description 全局经验表配置 
 *
 * @author DaoZhen Yuan
 * @date 2013-08-08 20:38:59
 */
@Component
public class ExpConfigExportService extends AbsClasspathConfigureParser{
	
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	
	/**
	  * configFileName
	 */
	private String configureName = "ShengJiJingYanBiao.jat";
	private int ROLE_MAX_LEVEL = 0;
	private long speed = 0;
	
	private Map<Integer, ExpConfig> configs;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Object[] dataList = GameConfigUtil.getResource(data);
		if(speed < 1){
			RoleBasePublicConfig roleBaseConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_ROLE_BASE);
			if(roleBaseConfig == null){
				ChuanQiLog.error("RoleBasePublicConfig is null.");
				return;
			}
			speed = roleBaseConfig.getSpeed();
		}
		Map<Integer, ExpConfig> configs = new HashMap<>();
		for (Object obj : dataList) {
			Map<String, Object> tmp = (Map<String, Object>)obj;
			if (null != tmp) {
				
				ExpConfig config = createExpConfig(tmp);
				configs.put(config.getLevel(), config);
			}
		}
		this.configs = configs;
		//设置角色最大等级
		ROLE_MAX_LEVEL = dataList.length;
	}
	
	private ExpConfig createExpConfig(Map<String, Object> tmp) {
		ExpConfig config = new ExpConfig();	
						
		int level = CovertObjectUtil.object2int(tmp.get("level"));
		config.setLevel(level);
		config.setNeedexp(CovertObjectUtil.obj2long(tmp.get("needexp")));
		config.setZlValue(CovertObjectUtil.object2int(tmp.get("zplus")));
		
		Map<String, Long> effectMap = ConfigAnalysisUtils.setAttributeVal(tmp);
		effectMap.put(EffectType.x19.name(), speed);
		config.setAttribute(effectMap);
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}
	
	public ExpConfig loadById(int level){
		return configs.get(level);
	}
	
	public Map<String,Long> getLevelAttribute(int level){
		if(level < 1){
			level = 1;
		}else if(level > ROLE_MAX_LEVEL){
			level = ROLE_MAX_LEVEL;
		}
		ExpConfig config = configs.get(level);
		if(config != null){
			return config.getAttribute();
		}else{
			return null;
		}
	}
	
	public int getRoleMaxLevel(){
		return ROLE_MAX_LEVEL;
	}
	
	/**
	 * 根据玩家等级，经验获取配置
	 * @param info	0:等级,1:当前经验
	 * @return
	 */
	public ExpConfig getNowExpConfig(Long[] info){
		int level = info[0].intValue();
		if(level == ROLE_MAX_LEVEL){
			return configs.get(ROLE_MAX_LEVEL);
		}
		long exp = info[1];
		ExpConfig config = null;
		for (int i = level; i <= ROLE_MAX_LEVEL; i++) {
			config = configs.get(i);
			if(config == null){
				ChuanQiLog.error("EXP CONFIG ERROR. LEVEL "+ i + " IS NULL");
				continue;
			}
			if(exp < config.getNeedexp()){
				break;
			}
			exp -= config.getNeedexp();
		}
		if(config == null || config.getLevel() == ROLE_MAX_LEVEL){
			info[1] = 0l;
		}else{
			info[1] = exp;
		}
		return config;
	}
}