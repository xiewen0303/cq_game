package com.junyou.bus.activityboss.manage;

import com.junyou.bus.activityboss.util.ActivityBossUtil;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.shuaguai.configure.export.DingShiShuaGuaiConfig;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.model.hatred.BossMonsterHatredManager;
import com.junyou.stage.model.hatred.MonsterHatredRule;
import com.kernel.gen.id.IdFactory;

/**
 * 生产定时活动怪
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-25 下午2:50:57
 */
public class DingShiMonsterFactory {
	  
	
	public static ActivityBoss createActivityMonster(DingShiShuaGuaiConfig config,int line,int mapId){ 
		
		Long id = IdFactory.getInstance().generateNonPersistentId() * 1L; 
		String monsterId = config.getMonsterId1();
		
		MonsterConfig monsterConfig = StageConfigureHelper.getMonsterExportService().load(monsterId);
		ActivityBoss boss = new ActivityBoss(id, null, monsterConfig);
		MonsterFactory.createHandle(boss, monsterConfig);
		boss.setLine(line);
		boss.setDsMonsterConfigId(ActivityBossUtil.getKey(mapId, config.getId()));
		boss.setHatredManger(new BossMonsterHatredManager(boss,new MonsterHatredRule(boss)));
		boss.setHuodongType(config.getType());
		boss.setMapId(mapId);
		return boss;
	}
	
}