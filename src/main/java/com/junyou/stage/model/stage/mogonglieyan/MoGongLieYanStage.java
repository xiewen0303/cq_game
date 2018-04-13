package com.junyou.stage.model.stage.mogonglieyan;

import java.util.HashSet;
import java.util.Set;

import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.PathInfoConfig;
import com.junyou.gameconfig.publicconfig.configure.export.MoGongLieYanPublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AOIManager;
import com.junyou.stage.model.element.monster.Monster;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.common.ObjectUtil;

public class MoGongLieYanStage extends PublicFubenStage{
    
    /**
     * 场景中活着的boss怪物id集合
     */
    private Set<String> bossMonsterIdSet;
    
    
    /**
     * 场景配置数据
     */
    private MoGongLieYanPublicConfig publicConfig;

	public MoGongLieYanStage(String id, Integer mapId,Integer lineNo, AOIManager aoiManager, PathInfoConfig pathInfoConfig, MoGongLieYanPublicConfig publicConfig) {
		super(id, mapId, lineNo, aoiManager, pathInfoConfig,StageType.MGLY_STAGE);
		this.publicConfig = publicConfig;
		this.bossMonsterIdSet = new HashSet<String>();
		
	}

	/**
	 * 获取公共场景配置数据
	 * 
	 * @return
	 */
	public MoGongLieYanPublicConfig getPublicConfig(){
	    return publicConfig;
	}
	
	/**
     * 推送刷新场景中存活的boss怪物信息
     * 
     * @param userRoleId
     */
    public void refreStageBossMonsterInfo(){
        StageMsgSender.send2Many(this.getAllRoleIds(), ClientCmdType.MGLY_REFRESH_MONSTER_INFO, bossMonsterIdSet.toArray(new String[0]));
    }
	
    /**
     * 移除被击杀的boss怪物Id
     * 
     * @param monsterId
     * @return
     */
	public boolean removeBossMonsterId(String monsterId){
	    return bossMonsterIdSet.remove(monsterId);
	}
	
    @Override
 	public void enter(IStageElement element, int x, int y) {
        super.enter(element, x, y);
        if (null != element) {
            if (ElementType.isRole(element.getElementType())) {
                Role role = (Role) element;
                role.startMglyAddExpZhenqiSchedule(publicConfig.getTime());
                role.startMglyCutYumoValSchedule(publicConfig.getTime());
            } else if (ElementType.isMonster(element.getElementType())) {
                Monster monster = (Monster) element;
                if(monster.isMglyBossMonster()){
                    bossMonsterIdSet.add(monster.getMonsterId());
                    refreStageBossMonsterInfo();
                    ChuanQiLog.info("mogonglieyanStage, stageId={}, boss monster name ={}, enter stage", getId(), monster.getName());                    
                }
            }
        }
	}
	
	public void leave(IStageElement element) {
        super.leave(element);
        if (null != element) {
            if (ElementType.isRole(element.getElementType())) {
                Role role = (Role) element;
                role.cancelMglyAddExpZhenqiSchedule();
                role.cancelMglyCutYumoValSchedule();
                role.cancelMglyDelayExitStageSchedule();
            }  
            else if (ElementType.isMonster(element.getElementType())) {
                Monster monster = (Monster) element;
                if(monster.isMglyBossMonster()){
                    bossMonsterIdSet.remove(monster.getMonsterId());
                    refreStageBossMonsterInfo();
                }
            }
        }
	}
	
	public void enterNotice(Long userRoleId) {
	    StageMsgSender.send2One(userRoleId, ClientCmdType.MGLY_ENTER_STAGE, AppErrorCode.OK);
	    if(!ObjectUtil.isEmpty(bossMonsterIdSet)){
	        StageMsgSender.send2One(userRoleId, ClientCmdType.MGLY_REFRESH_MONSTER_INFO, bossMonsterIdSet.toArray(new String[0]));
	    }
	}
	
	public void exitNotice(Long userRoleId) {
		StageMsgSender.send2One(userRoleId, ClientCmdType.MGLY_EXIT_STAGE, AppErrorCode.OK);
	}
	
	@Override
	public boolean isAddPk() {
		return true;
	}
	
	public boolean isCanPk() {
	    return true;
	}
	
	public boolean isFubenMonster() {
		return false;
	}
	
	
}