package com.junyou.stage.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.junyou.gameconfig.constants.EffectType;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.tunnel.DirectMsgWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.yabiao.configure.export.YaBiaoConfig;
import com.junyou.bus.yabiao.configure.export.YaBiaoConfigExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.YabiaoPublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.stage.model.core.element.IFighter;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.stage.ScopeType;
import com.junyou.stage.model.core.state.StateEventType;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.biaoche.BiaoCheFactory;
import com.junyou.stage.model.element.biaoche.Biaoche;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.state.AiBackState;
import com.junyou.stage.model.state.AiXunLuoState;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.stage.utils.BStarUtil;
import com.kernel.spring.container.DataContainer;
/**
 * 镖车
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-3-16 下午3:08:00 
 */
@Service
public class BiaocheService {
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private YaBiaoConfigExportService yaBiaoConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	
	/**
	 * 获取镖车配置
	 * @param configId
	 * @return
	 */
	private YaBiaoConfig getYaBiaoConfig(Integer configId){
		return yaBiaoConfigExportService.getYabiaoConfigById(configId);
	}
	
	/**
	 * 获取镖车公共配置
	 * @return
	 */
	private YabiaoPublicConfig getYabiaoPublicConfig(){
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YABIAO);
	}
	
	private int calNextOptDelay(Biaoche biaoche,YaBiaoConfig ybConfig){
		//巡逻状态
		if(biaoche.getStateManager().contains(StateType.XUNLUO)){
			return 600;
		}
		if(ybConfig != null && ybConfig.getXintiao() > 0){
			return ybConfig.getXintiao();
		}else{
			return 150;
		}
		
	}
	
	private void refreshState(IStage stage, Biaoche biaoche, IFighter role) {
		//异常处理,不在场景内
		if(biaoche.getStage() == null){
			return;
		}
		
		if(!stage.inScope(biaoche.getPosition(), role.getPosition(), 2,ScopeType.GRID)){
			biaoche.getStateManager().add(new AiBackState());
		}else{
			biaoche.getStateManager().add(new AiXunLuoState());
		}

		//弹提示
		if(!stage.inScope(biaoche.getPosition(), role.getPosition(), 12,ScopeType.GRID)){
			biaoche.setYuanLi(true);
		}else{
			biaoche.setYuanLi(false);
		}
	}
	
	private int[] getBstarFind(Point from,Point target,IStage stage){
		Point[] points = BStarUtil.findPints(from, target);
		
		int[] finalPint = new int[]{from.getX(),from.getY()};
		for (Point point : points) {
			int faX = from.getX() + point.getX();
			int faY = from.getY()+point.getY();
			
			if(stage.checkCanUseStagePoint(faX ,faY, PointTakeupType.BEING)){
				finalPint = new int[]{faX,faY};
				break;
			}
		}
		
		return finalPint;
	}
	
	
	/**
	 * 移动操作
	 */
	private void back(Biaoche biaoche, IFighter role, IStage stage) {
		
		if(biaoche.getStateManager().isForbidden(StateEventType.MOVE)){
			return;
		}
		
		if(biaoche.getStage() == null || role.getStage() == null){
			return;
		}
		
		//不在同一地图不处理
		if(!role.getStage().getId().equals(biaoche.getStage().getId())){
			return;
		}
		
		YabiaoPublicConfig YaBiaoPublicConfig = getYabiaoPublicConfig();
		if(!stage.isScopeGeZi(biaoche.getPosition(), role.getPosition(), YaBiaoPublicConfig.getMaxCell())){
			return;
		}
		
		
		if(stage.isScopeGeZi(biaoche.getPosition(), role.getPosition(), 3)){
			biaoche.getStateManager().add(new AiXunLuoState());
			return;
		}
		
		int[] moveXy = getBstarFind(biaoche.getPosition(), role.getPosition(), stage);
		stage.moveTo(biaoche, moveXy[0], moveXy[1]);
		StageMsgSender.send2Many(stage.getSurroundRoleIds(biaoche.getPosition()), ClientCmdType.BEHAVIOR_MOVE, biaoche.getMoveData());
		
	}
	
	/**
	 * 巡逻操作
	 */
	private void xunluo(Biaoche biaoche,IFighter role, IStage stage) {
		
		if(biaoche.getStateManager().isForbidden(StateEventType.MOVE)){
			return;
		}
		
		if(biaoche.getStage() == null || role.getStage() == null){
			return;
		}
		
		//不在同一地图不处理
		if(!role.getStage().getId().equals(biaoche.getStage().getId())){
			return;
		}
		
		YabiaoPublicConfig YaBiaoPublicConfig = getYabiaoPublicConfig();
		if(!stage.isScopeGeZi(biaoche.getPosition(), role.getPosition(), YaBiaoPublicConfig.getMaxCell())){
			return;
		}
		
		if(!stage.isScopeGeZi(biaoche.getPosition(), role.getPosition(), 4)){
			int[] moveXy = getBstarFind(biaoche.getPosition(), role.getPosition(), stage);
			stage.moveTo(biaoche, moveXy[0], moveXy[1]);
			StageMsgSender.send2Many(stage.getSurroundRoleIds(biaoche.getPosition()), ClientCmdType.BEHAVIOR_MOVE, biaoche.getMoveData());
		}
	}
	
	/**
	 * 创建镖车
	 * @param userRoleId
	 * @param stageId
	 */
	public void createBiaoche(Long userRoleId, String stageId, Integer configId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null || userRoleId == null || configId == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		
		Biaoche biaoche = dataContainer.getData(GameConstants.COMPONENT_BIAOCHE_NMAE, userRoleId+"");
		if(biaoche != null){
			stage.leave(biaoche);
		}
		
		//获取镖车配置
		YaBiaoConfig ybConfig = getYaBiaoConfig(configId);
		if(ybConfig == null){
			ChuanQiLog.error("YaBiaoConfig is null, id:"+configId);
			return;
		}
		
		YabiaoPublicConfig ybPublicConfig = getYabiaoPublicConfig();
		//创建镖车
		biaoche = BiaoCheFactory.createBiaoche(role, ybConfig, ybPublicConfig== null ? 0 : ybPublicConfig.getMaxTime());
		dataContainer.putData(GameConstants.COMPONENT_BIAOCHE_NMAE, userRoleId+"", biaoche);
		role.setBiaoche(biaoche);
		
		//进入场景
		Point point = role.getStage().getSurroundValidPoint(role.getPosition(), false, PointTakeupType.BEING);
		stage.enter(biaoche, point.getX(), point.getY());

		long charnewspeed =  (long)ybPublicConfig.getCharnewspeed();
		addMoveEffect(role,charnewspeed);
	}


	/**
	 * 镖车心跳
	 * @param stageId
	 * @param bcId
	 */
	public void biaocheHeart(String stageId, Long bcId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		Biaoche biaoche = (Biaoche)stage.getElement(bcId, ElementType.BIAOCHE);
		if(biaoche == null){
			return;
		}
		
		IFighter role = biaoche.getOwner();
		try {
			refreshState(stage,biaoche,role);
			
			if(!biaoche.getStateManager().isDead() && biaoche.getStateManager().contains(StateType.BACK)){
				back(biaoche,role,stage);
			}else{
				xunluo(biaoche, role, stage);
			}
		} catch (Exception e) {
			ChuanQiLog.error("biaoChe heart beat error",e);
		}finally{
			//计算下次心跳间隔,不在(战斗，巡逻，返回)状态则无心跳
			YaBiaoConfig ybConfig = getYaBiaoConfig(biaoche.getBcConfigId());
			int delay = calNextOptDelay(biaoche,ybConfig);
			biaoche.getAi().schedule(delay,TimeUnit.MILLISECONDS);
		}
	}
	
	/**
	 * 镖车清除
	 * @param userRoleId
	 * @param jbName 劫镖名字（过期则为null）
	 */
	public void biaocheClean(Long userRoleId, String jbName){
		Biaoche biaoche = dataContainer.getData(GameConstants.COMPONENT_BIAOCHE_NMAE, userRoleId+"");
		if(biaoche == null){
			return;
		}
		
		IRole role = biaoche.getOwner();
		
		//通知押镖者镖车被劫（-1则为任务正常完成，不推送客户端镖车消失）
		if(jbName == null || !jbName.equals("-1")){
			BusMsgSender.send2One(userRoleId, ClientCmdType.YA_BIAOCHE, jbName);
		}
		//镖车离开场景
		biaoche.leaveStageHandle(biaoche.getStage());
		//删除镖车
		dataContainer.removeData(GameConstants.COMPONENT_BIAOCHE_NMAE, userRoleId+"");
		
		//通知业务修改状态
		StageMsgSender.send2Bus(userRoleId, InnerCmdType.B_BIAOCHE_STATUS, null);
		
		biaoche.getStage().leave(biaoche);
		if(role == null){
			return;
		}
		role.setBiaoche(null);

		removeMoveEffer(role);
	}

	/**
	 * 清除押镖移动影响
	 * @param role
	 */
	private void removeMoveEffer(IRole role){
		role.getFightAttribute().clearBaseAttribute(BaseAttributeType.JB_BIAOCE_ATTR,true);
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}

	private void addMoveEffect(IRole role,long charnewspeed){
		//押镖修改移动属性
		Map<String,Long> valMap = new HashMap<>();
		valMap.put(EffectType.x80.name(),charnewspeed);
		role.getFightAttribute().setBaseAttribute(BaseAttributeType.JB_BIAOCE_ATTR,valMap);
		role.getFightAttribute().refresh();
		role.getFightStatistic().flushChanges(DirectMsgWriter.getInstance());
	}
}
