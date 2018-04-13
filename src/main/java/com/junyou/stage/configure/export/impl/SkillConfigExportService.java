package com.junyou.stage.configure.export.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.gameconfig.constants.EnemyHostileType;
import com.junyou.gameconfig.utils.GameConfigUtil;
import com.junyou.stage.configure.SkillFirePathType;
import com.junyou.stage.configure.SkillFireType;
import com.junyou.stage.configure.SkillTargetChooseType;
import com.junyou.utils.collection.ReadOnlyList;
import com.junyou.utils.common.CovertObjectUtil;


/**
 * 技能表
 * @author  作者：wind
 * @version 创建时间：2017-7-5 下午2:27:43
 */
@Service
public class SkillConfigExportService extends AbsClasspathConfigureParser {

	
	/**
	 * 配置名
	 */
	private String configureName = "JiNengBiao.jat";
	private Map<String,SkillConfig> configs;
	/**自动学习技能列表*/
	private Map<Integer,List<SkillConfig>> autoConfig;
	private static List<String> tanbaoSkillIds;
	private static List<String> yaoshenSkillIds;
	
	@SuppressWarnings("unchecked")
	protected void configureDataResolve(byte[] data) {
		Map<String,SkillConfig> configs = new HashMap<>();
		Map<Integer,List<SkillConfig>> autoConfig = new HashMap<>();
		List<String> tanbaoSkillIds = new ArrayList<>();
		List<String> yaoshenSkillIds = new ArrayList<>();
		Object[] skillListData = GameConfigUtil.getResource(data);
		for (Object obj:skillListData) {
			Map<Object, Object> tmp = (Map<Object, Object>)obj;
			if (null != tmp) {
				SkillConfig skillConfig = createSkillCategoryConfig(tmp);
				if(skillConfig == null){
					continue;
				}
				configs.put(skillConfig.getId(), skillConfig);
				
				if(skillConfig.getSkillFireType().equals(SkillFireType.XIANGONG)){
					//注册仙宫探宝技能
					tanbaoSkillIds.add(skillConfig.getId());
				}else if(skillConfig.getSkillFireType().equals(SkillFireType.YAOSHEN)){
					//注册妖神技能
					yaoshenSkillIds.add(skillConfig.getId());
				}else{
					//设置自动学习列表
					Integer job = skillConfig.getNeedJob();
					List<SkillConfig> list = autoConfig.get(job); 
					if(list == null){
						list = new ArrayList<>();
					}
					list.add(skillConfig);
					autoConfig.put(job, list);
				}
			}
		}
		this.configs = configs;
		this.autoConfig = autoConfig;
		SkillConfigExportService.tanbaoSkillIds = tanbaoSkillIds;
		SkillConfigExportService.yaoshenSkillIds = yaoshenSkillIds;
	}
	
	
	/**
	 * 技能信息
	 * @param tmp
	 * @return
	 */
	private SkillConfig createSkillCategoryConfig(Map<Object, Object> tmp) {
		
		String skillId = CovertObjectUtil.object2String(tmp.get("id"));
		if(CovertObjectUtil.isEmpty(skillId)){
			return null;
		}
		SkillConfig config = new SkillConfig();
		config.setId(skillId.trim());

		config.setCd1(CovertObjectUtil.object2String(tmp.get("cd1")));
		config.setCd2(CovertObjectUtil.object2String(tmp.get("cd2")));
		
		config.setGongshi(CovertObjectUtil.object2int(tmp.get("gongshi")));
		config.setNeedJob(CovertObjectUtil.object2int(tmp.get("job")));
		
		config.setNoDef(CovertObjectUtil.object2boolean(tmp.get("wushi")));
		config.setNoMiss(CovertObjectUtil.object2boolean(tmp.get("nomiss")));
		
		config.setHatredN1(CovertObjectUtil.obj2float(tmp.get("chouhen")));
		config.setHatredN2(CovertObjectUtil.obj2float(tmp.get("chouhen1")));
		List<String> effs = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			String eff = CovertObjectUtil.obj2StrOrNull(tmp.get("addeffect" + i));
			if(CovertObjectUtil.isEmpty(eff)){
				break;
			}
			effs.add(eff);
		}
		config.setEffs(effs);
		
		config.setIshostile(CovertObjectUtil.object2int(tmp.get("ishostile")));
		config.setMaxTarget(CovertObjectUtil.object2int(tmp.get("maxtarget")));
		config.setRange(CovertObjectUtil.object2int(tmp.get("range")));
		int skillType = CovertObjectUtil.object2int(tmp.get("skilltype"));
		config.setSkillFireType(SkillFireType.getSkillFireTypeByVal(skillType));
		config.setAttackerMove(CovertObjectUtil.object2int(tmp.get("attackerMove")));
		config.setWeiyi(CovertObjectUtil.object2int(tmp.get("weiyi")));
		Integer targetType = CovertObjectUtil.object2int(tmp.get("targettype"));
		//技能施法路径
		switch (targetType) {
		case 0:
			config.setSkillFirePathType(SkillFirePathType.SELF);
			break;
		case 100:
			config.setSkillFirePathType(SkillFirePathType.DEFAULT);
			break;
		case 101:
			config.setSkillFirePathType(SkillFirePathType.TARGET_CIRCLE);
			break;
		case 102:
			config.setSkillFirePathType(SkillFirePathType.SELF_CIRCLE);
			break;
		case 103:
			config.setSkillFirePathType(SkillFirePathType.SELF);
			break;
		case 105:
			config.setSkillFirePathType(SkillFirePathType.TAFANG);
			break;
		default:
			config.setSkillFirePathType(SkillFirePathType.FOREVER_TRUE);
			break;
		}
		//目标类型
		switch (targetType) {
		case 0:
			config.setTargetType(SkillTargetChooseType.SELF);
			break;
		case 3:
			config.setTargetType(SkillTargetChooseType.ENEMY_AOE);
			break;
		case 4:
			config.setTargetType(SkillTargetChooseType.ENEMY_AOE);
			break;
		case 103:
			config.setTargetType(SkillTargetChooseType.SELF);
			break;
		case 101:
			config.setTargetType(SkillTargetChooseType.ENEMY_AOE);
			break;
		case 102:
			config.setTargetType(SkillTargetChooseType.ENEMY_AOE);
			break;
		case 105:
			config.setTargetType(SkillTargetChooseType.TAFANG);
			break;
		default:
			EnemyHostileType enemyHostileType = EnemyHostileType.getEnemyHostileType(config.getIshostile());
			if(EnemyHostileType.SAME_CAMP.equals(enemyHostileType)){
				config.setTargetType(SkillTargetChooseType.SAME_CAMP);
			}else if(EnemyHostileType.NO_ENEMY.equals(enemyHostileType)){
				config.setTargetType(SkillTargetChooseType.NO_ENEMY);
			}else{
				config.setTargetType(SkillTargetChooseType.ENEMY);
			}
			
			break;
		}
		
		config.setPathRadius(CovertObjectUtil.obj2float(tmp.get("radius")));
		config.setPathSectorRad((float)(CovertObjectUtil.obj2float(tmp.get("rad")) / 180 * Math.PI));
		config.setPathWidth(CovertObjectUtil.obj2float(tmp.get("width")));
		
		return config;
	}
	
	protected String getConfigureName() {
		return configureName;
	}


	public SkillConfig loadById(String skillId) {
		return configs.get(skillId);
	}


	public ReadOnlyList<SkillConfig> getAutoConfig(int job) {
		return new ReadOnlyList<>(autoConfig.get(job));
	}


	public static List<String> getTanbaoSkillIds() {
		return tanbaoSkillIds;
	}
	
	public static List<String> getYaoshenSkillIds() {
		return yaoshenSkillIds;
	}

}
