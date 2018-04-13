package com.junyou.bus.activityboss.manage;

import java.util.concurrent.TimeUnit;

import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.stage.model.core.skill.IHarm;
import com.junyou.stage.model.element.monster.AbsHurtRankMonster;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.schedule.StageTokenRunable;

/**
 * 定时活动boss
 * @author wind
 * @email  18221610336@163.com
 * @date  2015-4-25 下午2:42:30
 */
public class ActivityBoss extends AbsHurtRankMonster {


	private int line;//几号线boss
	
	private String dsMonsterConfigId;//定时刷怪表中的配置ID(排序字段)
	private int huodongType; //活动类型
	private int mapId;
	
	public ActivityBoss(Long id, String teamId, MonsterConfig monsterConfig) {
		super(id, teamId, monsterConfig); 
	}
	
	@Override
	protected void scheduleDisappearHandle(IHarm harm){
		//启动消失
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.AI_DISAPPEAR, new Object[]{getId(),getElementType().getVal()});
		this.getScheduler().schedule(getId().toString(), GameConstants.COMPONENT_AI_RETRIEVE, runable, disappearDuration, TimeUnit.SECONDS);
		
		//取消排行榜刷新定时器
		this.getScheduler().cancelSchedule(this.getId().toString(), GameConstants.DSBOSS_REFRESH_RANK);
	}
	
	/**
	 * 定时刷排行榜
	 */
	public void schedulerRefreshRank(){
		StageTokenRunable runable = new StageTokenRunable(getId(), getStage().getId(), InnerCmdType.INNER_DSBOSS_RANK, getId());
		this.getScheduler().schedule(this.getId().toString(), GameConstants.DSBOSS_REFRESH_RANK, runable, GameConstants.DSBOSS_RANK_DELAY, TimeUnit.MILLISECONDS);
	}
	
	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}
	
	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public String getDsMonsterConfigId() {
		return dsMonsterConfigId;
	}

	public void setDsMonsterConfigId(String dsMonsterConfigId) {
		this.dsMonsterConfigId = dsMonsterConfigId;
	}

	/**
	 * 新增伤害值
	 * @param roleId
	 * @param addHurt
	 */
	public void addHurt(Long roleId,long addHurt) {
		BossHurtRank  bossHurtRank = bossHurts.get(roleId);
		if(bossHurtRank == null){
			bossHurtRank = new BossHurtRank();
			bossHurtRank.setHurt(addHurt);
			bossHurtRank.setRoleName(getRoleName(roleId));
			bossHurtRank.setUserRoleId(roleId);
			
			bossHurts.put(roleId, bossHurtRank);
			ranks.add(bossHurtRank);
			//野外boss抛指令处理活跃度
			BusMsgSender.send2BusInner(roleId, InnerCmdType.INNER_HUOYUEDU, new Object[] {ActivityEnum.A17});
			//修炼任务
			BusMsgSender.send2BusInner(roleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.SHIJIE_BOSS, null});
		}
		bossHurtRank.setHurt(bossHurtRank.getHurt() + addHurt);
	}
	
	private String getRoleName(long userRoleId){
		//打印日志
		RoleWrapper rw = BusConfigureHelper.getRoleExportService().getLoginRole(userRoleId);
		if(rw == null){
			return "";
		}
		return rw.getName();
	}
	
	public RoleWrapper getRoleWrapper(long userRoleId){
		//打印日志
		return BusConfigureHelper.getRoleExportService().getLoginRole(userRoleId);
	}
	
	@Override
	public Long getId(IRole role) {
		return role.getId();
	}

	@Override
	public short getNoticeCmd() {
		return ClientCmdType.YW_BOSS_RANK;
	}
	@Override
	public short getCloseRankCmd() {
		return ClientCmdType.YW_BOSS_CLOSE_HURT_RANK;
	}

	public int getHuodongType() {
		return huodongType;
	}

	public void setHuodongType(int huodongType) {
		this.huodongType = huodongType;
	}
}