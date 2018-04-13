package com.junyou.stage.tafang.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.tafang.configure.TaFangMonsterCreateConfig;
import com.junyou.bus.tafang.configure.TaFangPublicConfig;
import com.junyou.bus.tafang.configure.export.TaFangMonsterCreateConfigExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.tafang.stage.TaFangMonster;
import com.junyou.stage.tafang.stage.TaFangStage;
import com.junyou.stage.tunnel.StageMsgSender;
import com.kernel.gen.id.IdFactory;

/**
 * @author LiuYu
 * 2015-10-12 下午6:51:57
 */
@Service
public class TaFangStageService {

	@Autowired
	private TaFangMonsterCreateConfigExportService taFangMonsterCreateConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private MonsterExportService monsterExportService;
	
	
	public void createMonsters(String stageId,int level,int state){
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null){
			return;
		}
		if(!StageType.TAFANG_FUBEN.equals(iStage.getStageType())){
			return;
		}
		List<TaFangMonsterCreateConfig> list = taFangMonsterCreateConfigExportService.getConfigs();
		if(list == null){
			return;
		}
		TaFangStage stage = (TaFangStage)iStage;
		int finish = 0;
		for (TaFangMonsterCreateConfig taFangMonsterCreateConfig : list) {
			String monsterId = taFangMonsterCreateConfig.getMonster(level, state);
			if(monsterId == null){
				finish++;
				continue;
			}
			//刷怪
			MonsterConfig monsterConfig = monsterExportService.load(monsterId);
			if(monsterConfig == null){
				finish++;
				continue;
			}
			TaFangMonster monster = MonsterFactory.createTaFangMonster(IdFactory.getInstance().generateNonPersistentId(), monsterConfig);
			monster.setMoveLine(taFangMonsterCreateConfig.getId());
			monster.setStopZb(taFangMonsterCreateConfig.getStop());
			int[] brith = taFangMonsterCreateConfig.getStart();
			stage.enter(monster, brith[0], brith[1]);
		}
		long time = 0;
		TaFangPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TAFANG);
		if(config == null){
			return;
		}
		if(finish < list.size()){
			state++;
			time = config.getPerMonsterTime();
		}else{
			level++;
			if(level > taFangMonsterCreateConfigExportService.getMaxLevel()){
				Role role = stage.getChallenger();
				if(role != null){
					StageMsgSender.send2One(role.getId(), ClientCmdType.TAFANG_NOTICE_MONSTER_CREATE_OVER, level - 1);
				}
				return;//全部怪已刷完
			}
			Role role = stage.getChallenger();
			if(role != null){
				StageMsgSender.send2One(role.getId(), ClientCmdType.TAFANG_NOTICE_MONSTER_CREATE_OVER, level - 1);
			}
			state = 0;
			time = config.getMonstersTime();
		}
		
		stage.scheduleProductMonster(time, level, state);
		
	}
	
	public Object[] startTafang(Long userRoleId,String stageId){
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.TAFANG_FUBEN.equals(iStage.getStageType())){
			return null;//场景不存在
		}
		TaFangStage stage = (TaFangStage)iStage;
		stage.start();
		return AppErrorCode.OK;
	}
}
