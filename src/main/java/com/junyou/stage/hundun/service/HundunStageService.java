package com.junyou.stage.hundun.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.HundunPublicConfig;
import com.junyou.stage.hundun.configure.export.HunDunWarGuiZeBiaoConfig;
import com.junyou.stage.hundun.configure.export.HunDunWarGuiZeBiaoConfigExportService;
import com.junyou.stage.hundun.entity.HundunRank;
import com.junyou.stage.hundun.entity.HundunStage;
import com.junyou.stage.model.core.skill.IBuff;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.skill.buff.BuffFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.BufferedMsgWriter;
import com.junyou.stage.tunnel.DirectMsgWriter;
import com.junyou.stage.tunnel.IMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;

/**
 * @author LiuYu
 * 2015-9-6 下午3:41:34
 */
@Service
public class HundunStageService {

	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private HunDunWarGuiZeBiaoConfigExportService hunDunWarGuiZeBiaoConfigExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	/**
	 * 混沌排名
	 * @param stageId
	 */
	public void chaosWarCheck(String stageId){
		
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.CHAOS.equals(iStage.getStageType())){
			return;
		}
		HundunStage stage = (HundunStage)iStage;
		if(!stage.isOpen()){
			return;
		}
		
		//获取混沌战公共配置
		HunDunWarGuiZeBiaoConfig hunDunWarConfig = stage.getHdConfig();
		//混沌战配置，启动下一个定时
		stage.scheduleCheck(hunDunWarConfig.getSecond());
		
		List<HundunRank> list = stage.reRankIngChaos(true);
		if(list != null && list.size() > 0){
			
			int size = hunDunWarConfig.getNumber() < list.size() ? hunDunWarConfig.getNumber() : list.size();
			Set<Long> roleIds = new HashSet<>();
			for (int i = 0; i < size; i++) {
				roleIds.add(list.get(i).getUserRoleId());
			}
			stage.setCanEnterUsers(roleIds);
			
			//下一层的过期时间限制
			HundunPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_HUNDUN);
			long nextExpTime = GameSystemTime.getSystemMillTime() + publicConfig.getEnterTime();
			stage.setNextExpiredTime(nextExpTime);
			StageMsgSender.send2Many(roleIds.toArray(), ClientCmdType.CHAOS_NOTICE_ENTER_NEXT, nextExpTime);
			
		}
		
		//发送检测的过期时间
		Map<Long,IStageElement> roleMaps = stage.getBaseStageRoles();
		if(roleMaps != null && roleMaps.size() > 0){
			StageMsgSender.send2Many(roleMaps.keySet().toArray(), ClientCmdType.CHAOS_END_TIME, stage.getCengExpiredTime());
		}
	}
	
	public Object[] enterNext(Long userRoleId,String stageId){
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.CHAOS.equals(iStage.getStageType())){
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}
		HundunStage stage = (HundunStage)iStage;
		if(!stage.isOpen()){
			return AppErrorCode.NOT_IN_TIME_CANNOT_CHANGE_MAP;
		}
		
		if(!stage.isOpenEnterNext()){
			return AppErrorCode.HUNDUN_NOT_IN_ENTER_NEXT_TIME;
		}
		
		if(!stage.isCanEnter(userRoleId)){
			return AppErrorCode.HUNDUN_CANNOT_ENTER_NEXT;
		}
		
		HunDunWarGuiZeBiaoConfig nextHdConfig = hunDunWarGuiZeBiaoConfigExportService.getHdConfigByNextId(stage.getHdConfig().getId());
		if(nextHdConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		//地图配置
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(nextHdConfig.getMapId());
		if(dituCoinfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		//离开场景
		int birthPoint[] = dituCoinfig.getRandomBirth();
		int x = birthPoint[0];
		int y = birthPoint[1];
		List<int[]> birthPoints = dituCoinfig.getBirthRandomPoints();
		if(birthPoints != null){
			int randome = Lottery.roll(birthPoints.size());
			int[] birthXy = birthPoints.get(randome);
			
			x = birthXy[0];
			y = birthXy[1];
		}
		//发送到场景进入地图
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, new Object[]{dituCoinfig.getId(), x, y,MapType.HUNDUN_MAP_TYPE});
		
		return null;
	}
	
	public int getSelfRank(Long userRoleId,String stageId){
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.CHAOS.equals(iStage.getStageType())){
			return -1;
		}
		HundunStage stage = (HundunStage)iStage;
		if(!stage.isOpen()){
			return -1;
		}
		return stage.getRank(userRoleId);
	}
	
	public Object[] getCurRank(String stageId){
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null || !StageType.CHAOS.equals(iStage.getStageType())){
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}
		HundunStage stage = (HundunStage)iStage;
		if(!stage.isOpen()){
			return AppErrorCode.NOT_IN_TIME_CANNOT_CHANGE_MAP;
		}
		List<HundunRank> list = stage.reRankIngChaos(false);
		List<Object[]> roles = new ArrayList<>();
		if(list != null && list.size() > 0){
			for (HundunRank rank : list) {
				roles.add(rank.getData());
			}
		}
		return roles.toArray();
	}
	

	public void putongRevive(String stageId, Long roleId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null || !StageType.CHAOS.equals(stage.getStageType())){
			return;
		}
		
		IRole role = stage.getElement(roleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		if(!role.getStateManager().isDead()){
			return;
		}
		
		
		//去除死亡状态
		role.getStateManager().remove(StateType.DEAD);
		HundunPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_HUNDUN);
		role.getFightAttribute().setCurHp((int)(role.getFightAttribute().getMaxHp() * publicConfig.getResetRate()));
		IMsgWriter writer = BufferedMsgWriter.getInstance();
		role.getFightStatistic().flushChanges(writer);
		writer.flush();
		StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.TOWN_REVIVE, roleId);
		changeMapAddWuDiState(role);
		
		DiTuConfig config = diTuConfigExportService.loadDiTu(stage.getMapId());
		int[] birthXy = config.getRandomBirth();
		int x = birthXy[0];
		int y = birthXy[1];
		
		//1.给切换的人推送 100
		StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x,y,stage.getLineNo(), GameConstants.IS_CLEAR_BUFF});
		
		stage.teleportTo(role, x,y);
		
		removeMapAddWuDiState(role);
		
		//添加复活的buff
		IBuff buff = BuffFactory.create(role, role, GameConstants.SF_BUFF);
		role.getBuffManager().addBuff(buff);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
		
		//给当前AOI区域内的其它人推送702 单位瞬间移动
		StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{roleId, role.getPosition().getX(),role.getPosition().getY()});
	}
	
	/**
	 * 传送前加一个无敌状态
	 * @param role
	 */
	private void changeMapAddWuDiState(IRole role){
		//传送前加一个无敌状态
		role.getStateManager().add(new NoBeiAttack());
	}
	/**
	 * 瞬移后删除无敌状态
	 * @param role
	 */
	private void removeMapAddWuDiState(IRole role){
		role.getStateManager().remove(StateType.NO_ATTACKED);
	}
}
