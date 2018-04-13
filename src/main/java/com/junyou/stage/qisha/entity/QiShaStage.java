package com.junyou.stage.qisha.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.tunnel.StageMsgSender;

/**
 * @author LiuYu
 * 2015-8-20 下午3:02:51
 */
public class QiShaStage extends PublicFubenStage{

	private Map<String,QiShaBossVo> boss = new ConcurrentHashMap<>();
	private Object[] enterData;
	private Map<Long,IRole> roles = new ConcurrentHashMap<>();
	
	public QiShaStage(String id, Integer mapId, Integer lineNo,AOIManager aoiManager, PathInfoConfig pathInfoConfig) {
		super(id, mapId, lineNo, aoiManager, pathInfoConfig, StageType.QISHA);
	}
	
	public void initBoss(List<String> bossIds){
		Object[] bossData = new Object[bossIds.size()];
		for (int i = 0; i < bossIds.size(); i++) {
			String bossId = bossIds.get(i);
			QiShaBossVo bossVo = new QiShaBossVo(bossId);
			boss.put(bossVo.getMonsterId(), bossVo);
			bossData[i] = bossVo.getBossData();
		}
		enterData = new Object[]{1,bossData};
	}

	@Override
	public void enterNotice(Long userRoleId) {
		StageMsgSender.send2One(userRoleId, ClientCmdType.ENTER_QISHA, enterData);
	}

	@Override
	public void exitNotice(Long userRoleId) {
		roles.remove(userRoleId);
		StageMsgSender.send2One(userRoleId, ClientCmdType.EXIT_QISHA, AppErrorCode.OK);
	}

	@Override
	public boolean isFubenMonster() {
		return true;
	}

	@Override
	public boolean isAddPk() {
		return false;
	}

	@Override
	public void enter(IStageElement element, int x, int y) {
		super.enter(element, x, y);
		if(ElementType.isRole(element.getElementType())){
			IRole role = (IRole)element;
			roles.put(role.getId(), role);
		}else if(ElementType.isMonster(element.getElementType())){
			Monster monster = (Monster)element;
			if(boss.containsKey(monster.getMonsterId())){
				QiShaBossVo bossVo = boss.get(monster.getMonsterId());
				bossVo.setState(GameConstants.BOSS_STATE_REFRESH);
				//通知场景内玩家BOSS入场
				StageMsgSender.send2Many(roles.keySet().toArray(), ClientCmdType.QISHA_BOSS_STATE_CHANGE, bossVo.getBossData());
			}
		}
	}
	
	@Override
	public void leave(IStageElement element) {
		super.leave(element);
		if(ElementType.isMonster(element.getElementType()) && isOpen()){
			Monster monster = (Monster)element;
			if(boss.containsKey(monster.getMonsterId())){
				QiShaBossVo bossVo = boss.get(monster.getMonsterId());
				bossVo.setState(GameConstants.BOSS_STATE_DEAD);
				//通知场景内玩家BOSS死亡
				StageMsgSender.send2Many(roles.keySet().toArray(), ClientCmdType.QISHA_BOSS_STATE_CHANGE, bossVo.getBossData());
			}
		}
	}

	@Override
	public boolean isCanRemove() {
		return !isOpen() && roles.size() < 1;
	}
	@Override
	public void start(){
		super.start();
		StageMsgSender.send2Bus(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.QISHA_EXP_ADD, getId());
	}
	
	public String getGuildRoleIds(Long guildId){
		StringBuilder builder = new StringBuilder();
		for (IRole role : new ArrayList<>(roles.values())) {
			if(guildId.equals(role.getBusinessData().getGuildId())){
				builder.append(",").append(role.getId());
			}
		}
		if(builder.length() > 0){
			return builder.substring(1);
		}
		return null;
	}
	
}
