package com.junyou.stage.model.element.componentlistener;

import java.util.List;

import com.junyou.bus.branchtask.BranchEnum;
import com.junyou.bus.maigu.configure.constants.MaiguConstant;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.state.IState;
import com.junyou.stage.model.core.state.IStateManagerListener;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.BaguaFbStage;
import com.junyou.stage.model.stage.fuben.MaiguFbStage;
import com.junyou.stage.model.stage.fuben.MoreFbStage;
import com.junyou.stage.model.stage.fuben.SingleFbStage;
import com.junyou.stage.tunnel.BufferedMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;

public class FubenMonsterStateListener implements IStateManagerListener {
	
	private IMonster monster;
	
	public FubenMonsterStateListener(IMonster monster) {
		this.monster = monster;
	}
	

	@Override
	public void addState(IState state) {
		
		if(StateType.DEAD.equals(state.getType())){
			StageType stageType = monster.getStage().getStageType();
			if(stageType == StageType.MORE_FUBEN){
				MoreFbStage moreStage = (MoreFbStage)monster.getStage();
				
				// 死亡状态
				boolean isChedk = moreStage.wantedListCheck(monster.getMonsterId());
				if(isChedk && moreStage.isWantedListComplete()){
					//副本完成
					for(IRole role : moreStage.getChallengers()){						
						StageMsgSender.send2StageInner(role.getId(), moreStage.getId(), InnerCmdType.S_MORE_OVER, null);
						break;
					}
				}
			}else if(stageType == StageType.BAGUA_FUBEN){
				BaguaFbStage baguaStage = (BaguaFbStage)monster.getStage();
				
				// 死亡状态
				boolean isChedk = baguaStage.wantedListCheck(monster.getMonsterId());
				if(isChedk && baguaStage.isWantedListComplete()){
					//副本完成
					String teamId = baguaStage.getTeamId();
					for(int i=1;i<=8;i++){
						String stageId = teamId+"_"+i;
						IStage stage = StageManager.getStage(stageId);
						if(stage==null){
							continue;
						}
						BaguaFbStage bStage = (BaguaFbStage)stage;
						List<IRole> roles = bStage.getChallengers();
						if(roles!=null && roles.size()>0){
							StageMsgSender.send2StageInner(GameConstants.DEFAULT_ROLE_ID, bStage.getId(), InnerCmdType.BAGUA_FUBEN_FINISH, null);
						}
					}
				}
			}else if(stageType == StageType.MAIGU_FUBEN){
				MaiguFbStage maiguStage = (MaiguFbStage)monster.getStage();
				
				if(maiguStage.isNpc(monster.getMonsterId())){
					maiguStage.npcDeadHandle();
				}else{
					boolean isChedk = maiguStage.wantedListCheck(monster.getMonsterId());
					if(isChedk && maiguStage.isWantedListComplete()){
						//副本完成
						List<IRole> roles = maiguStage.getChallengers();
						if(roles!=null && roles.size()>0){
							StageMsgSender.send2StageInner(GameConstants.DEFAULT_ROLE_ID, maiguStage.getId(), InnerCmdType.MAIGU_FUBEN_FINISH, MaiguConstant.RESULT_WIN);
						}
					}
				}
			}else if(StageType.isSingleFuben(stageType)){				
				SingleFbStage fubenStage = (SingleFbStage)monster.getStage();
				
				boolean isChedk = fubenStage.wantedListCheck(monster.getMonsterId());
//			Long userRoleId = fubenStage.getChallenger().getId();
				//有通关条件通知客户端击杀的怪物Id
				if(isChedk){
					//通知客户端击杀某个指定的怪物
//				short cmd = ClientCmdType.KILL_MONSTER_ID;
//				StageMsgSender.send2One(userRoleId, cmd, monster.getMonsterId());
					//副本完成
					if(fubenStage.isWantedListComplete()){
						//副本完成
						StageMsgSender.send2StageInner(fubenStage.getChallenger().getId(), fubenStage.getId(),  fubenStage.getFinishCmd(), null);
						
						//支线任务
						if(stageType == StageType.FUBEN_RC){
							StageMsgSender.send2Bus(fubenStage.getChallenger().getId(), InnerCmdType.INNER_BRANCH_COUNT, new Object[]{BranchEnum.B8,1});	
						}
					}
				}
			}
		}else{
			
			if(StateType.BACK.equals(state.getType())){
				//怪物失去战斗状态回满血和蓝
				monster.getFightAttribute().resetHpMp();
				monster.getFightStatistic().flushChanges(BufferedMsgWriter.getInstance());
			}
		}
	}

	@Override
	public void removeState(IState state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replaceState(IState state) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clearState(IState state) {
		// TODO Auto-generated method stub
		
	}

}
