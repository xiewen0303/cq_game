package com.junyou.bus.lianyuboss.stage;

import com.junyou.bus.fuben.entity.AbsFubenConfig;
import com.junyou.bus.lianyuboss.configure.LianyuBossConfig;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GuildPublicConfig;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.fuben.SingleFbStage;
import com.junyou.stage.tunnel.StageMsgSender;

public class LianyuBossStage extends SingleFbStage{
	private int expireDelay;
	private short exitCmd;
	private int configId; //关卡id 配置表id
	private  float rate = 1; //怪物属性减至 
	private Long roleId;
	private IMonster monster  =null;
	public LianyuBossStage(String id, Integer mapId, AOIManager aoiManager,AbsFubenConfig fubenConfig,PathInfoConfig pathInfoConfig,StageType stageType) {
		super(id, mapId, aoiManager, pathInfoConfig, stageType, fubenConfig.getWantedMap());
		expireDelay = fubenConfig.getTime() * 1000;
		exitCmd = fubenConfig.getExitCmd();
		configId  = ((LianyuBossConfig)fubenConfig).getConfigId();
		GuildPublicConfig config = StageConfigureHelper.getGongGongShuJuBiaoConfigExportService().loadPublicConfig(PublicConfigConstants.MOD_GUILD);
		rate = config.getLianyuBossAttrReduce()/100f; 
	}
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
 
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	@Override
	public int getExpireDelay() {
		return expireDelay;
	}

	@Override
	public short getExitCmd() {
		return exitCmd;
	}

	@Override
	public int getExpireCheckInterval() {
		return GameConstants.EXPIRE_CHECK_INTERVAL;
	}

	@Override
	public void noticeClientKillInfo(Long roleId) {
//		BusMsgSender.send2One(roleId, ClientCmdType.KILL_MONSTER_COUNT, getWantedCounts());
	}

	@Override
	public short getFinishCmd() {
		return InnerCmdType.LIANYU_BOSS_FINISH_2_STAGE;
	}
	
	public short getFinishNoticeBusCmd(){
		if(getStageType() == StageType.GUILD_LIANYU_BOSS){
			
			return InnerCmdType.LIANYU_BOSS_FINISH_2_BUS;
		}
		return InnerCmdType.S_FUBEN_FINISH;
	}

	@Override
	public void enter(IStageElement element, int x, int y) {
		if (ElementType.isRole(element.getElementType())) {
			IRole role = (IRole)element;
//			role.getFightAttribute().resetHp(); //进副本满血
			boolean half = StageConfigureHelper.getLianyuBossExortService().isReduceHalfBossHarm(role.getId(),configId);
			if(half){
				rate = 1 - rate;
			}else{
				rate = 1; //不减
			}
			if(monster!=null){
				monster.getFightAttribute().setCurHp((long) (monster.getFightAttribute().getCurHp()*rate));
			}
			
		}
		if (ElementType.isMonster(element.getElementType())) {
			monster = (IMonster)element;
			if(roleId!=null){
				monster.getFightAttribute().setCurHp((long) (monster.getFightAttribute().getCurHp()*rate));
			}
			
		}
		super.enter(element, x, y);
	}
	@Override
	public void noticeClientExit(Long userRoleId) {
		//副本时间到
		if(getStageType() == StageType.FUBEN_RC){
			StageMsgSender.send2Bus(userRoleId, InnerCmdType.HAS_EXIT_FUBEN, null);
		}else if(getStageType() == StageType.GUILD_LIANYU_BOSS){
			StageMsgSender.send2Bus(userRoleId, InnerCmdType.LIANYU_BOSS_EXIT, null);
		}
	}

}
