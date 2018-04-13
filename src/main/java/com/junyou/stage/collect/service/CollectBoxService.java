package com.junyou.stage.collect.service;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.kuafu_qunxianyan.configure.QunXianYanJiFenConfig;
import com.junyou.bus.kuafu_qunxianyan.configure.QunXianYanJiFenConfigExportService;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuQunXianYanPublicConfig;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.collect.configure.DingShiShuaXiangConfig;
import com.junyou.stage.collect.element.ActivityBox;
import com.junyou.stage.collect.element.BoxProduceTeam;
import com.junyou.stage.collect.export.DingShiShuaXiangConifgExport;
import com.junyou.stage.collect.manage.CollectBoxActivityManager;
import com.junyou.stage.configure.export.impl.ZiYuanConfig;
import com.junyou.stage.configure.export.impl.ZiYuanConfigExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.goods.Collect;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.kuafuquanxianyan.KuafuQunXianYanStage;
import com.junyou.stage.schedule.StageTokenRunable;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.spring.container.DataContainer;




@Service
public class CollectBoxService {
	@Autowired
	private DingShiActiveConfigExportService dingShiActiveConfigExportService;
	@Autowired
	private DingShiShuaXiangConifgExport dingShiShuaXiangConifgExport;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private QunXianYanJiFenConfigExportService qunXianYanJiFenConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private ZiYuanConfigExportService ziYuanConfigExportService;
	
	private DingShiActiveConfig dingShiActiveConfig;
	private DingShiShuaXiangConfig dingShiShuaXiangConfig;
	/**活动管理器*/
	private CollectBoxActivityManager activityManager;
	
	

	/**宝箱采集活动开始*/
	public void CollectBoxActivityStart(int activityId){

		this.dingShiActiveConfig = dingShiActiveConfigExportService.getConfig(activityId);
		this.dingShiShuaXiangConfig = dingShiShuaXiangConifgExport.getDingShiShuaXiangConfig();
		this. activityManager=CollectBoxActivityManager.getManager();
		initManager(activityId);
		
		//开启开关
		activityManager.activityStart();
		
		//开始通知场景刷新
		StageMsgSender.send2StageInner(GameConstants.DEFAULT_ROLE_ID, this.activityManager.getStageId(), InnerCmdType.DINGSHI_FLUSH_BOX, activityManager.getStageId());	
		
		//活动结束定时
		launchStopTimer(activityManager.getStageId());
		
		ChuanQiLog.debug("宝箱采集活动开始。。。");		
	}
	
	/**开启结束定时*/
	private void launchStopTimer(String stageId){
		//活动结束定时
		Long delay =  activityManager.getEndTime() - GameSystemTime.getSystemMillTime();
		if(delay > 0){
			StageTokenRunable stageTokenRunableEnd = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, this.activityManager.getStageId(), InnerCmdType.COLLECT_BOX_STOP, stageId);
			activityManager.getScheduleExecutor().schedule(stageTokenRunableEnd, delay, TimeUnit.MILLISECONDS);
		}
	}
	
	/**活动结束*/
	public void collectBoxActivityEnd(){
		ChuanQiLog.debug("宝箱采集活动结束。。。");
		//关闭开关
		activityManager.activityEnd();
		
		long interval =activityManager.getVanishTime() - activityManager.getEndTime();
		if(interval > 0){
			//全局回收定时
			StageTokenRunable stageTokenRunableClear = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, this.activityManager.getStageId(), InnerCmdType.DINGSHI_CLEAR_ALL_BOX, activityManager.getStageId());
			activityManager.getScheduleExecutor().schedule(stageTokenRunableClear, interval, TimeUnit.MILLISECONDS);
		}else{
			clearAllBox(activityManager.getStageId());	
		}
		
	}
	
	/**回收所有箱子*/
	public void clearAllBox(String stageId){
		IStage stage = StageManager.getStage(stageId);
		if(stage != null && activityManager.getBoxTeam() != null){
			ChuanQiLog.debug("开始回收场景箱子。。。");
			activityManager.getBoxTeam().clear();
			stage.getStageProduceManager().removeElementProduceTeam(ElementType.COLLECT, activityManager.getBoxTeam().getId());
		}
	}
	
	/**初始活动数据管理器*/
	private void initManager(int activityId){
		//将相关配置数据统一加入管理器
		this.activityManager.setActivityId(activityId);
		
		List<Integer[]> points=new ArrayList<>();
		points.addAll(this.dingShiShuaXiangConfig.getZuobiaoList());
		this.activityManager.setCanPutPoints(points);
		
		this.activityManager.setBoxLimit(this.dingShiShuaXiangConfig.getCount());
		long curTime = GameSystemTime.getSystemMillTime();
		
		String[] data1 =  this.dingShiActiveConfig.getData1().split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		long vanishTime = DatetimeUtil.getTheDayTheTime(Integer.parseInt(data1[0]),Integer.parseInt(data1[1]),curTime);
		this.activityManager.setVanishTime(vanishTime);
		
		this.activityManager.setFlushInterval(this.dingShiShuaXiangConfig.getJiange());
		
		String[] endTime =  this.dingShiActiveConfig.getEndTime().split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		long endTimeL = DatetimeUtil.getTheDayTheTime(Integer.parseInt(endTime[0]),Integer.parseInt(endTime[1]),curTime);
		this.activityManager.setEndTime(endTimeL);

		String stageId = StageUtil.getStageId(this.dingShiShuaXiangConfig.getMap(), this.dingShiShuaXiangConfig.getLine());
		activityManager.setStageId(stageId);
	}
	
	/**创建一个能采集的箱子*/
	public ActivityBox createActivityBox(){
		Long boxId = IdFactory.getInstance().generateNonPersistentId() * 1L; 
		String goodsId = this.dingShiShuaXiangConfig.getJiangItem();		
		//long curTime = GameSystemTime.getSystemMillTime();
		
		ActivityBox activityBox = new ActivityBox(boxId, goodsId);
		activityBox.setConfigId(dingShiShuaXiangConfig.getId1());
		activityBox.setStageId(StageUtil.getStageId(this.dingShiShuaXiangConfig.getMap(), this.dingShiShuaXiangConfig.getLine()));
		activityBox.setNeedRoleLevel(this.dingShiActiveConfig.getMinLevel());
	/*	Integer[] point = this.activityManager.randomPoint();
		activityBox.setBornPosition(point[0],point[1]);*/
		activityBox.setVanishTime(this.activityManager.getVanishTime());
		activityBox.setNeedRoleLevel(this.dingShiActiveConfig.getMinLevel());
		activityBox.setCollectDuration(this.dingShiShuaXiangConfig.getTime3());
		
		return activityBox;
	}
	
/*	*//**批量创建活动的箱子送入场景*//*
	public void batchCreate(){
		if(!activityManager.activityIsUnderway()){
			return;
		}
		
		//需创建数量=上限-当前数量
		int needAmount = this.activityManager.getBoxLimit()-this.activityManager.currExistAmount();
		
		for (int i = 0; i < needAmount; i++) {
			ActivityBox activityBox = createActivityBox();

			boxEnterStage(activityBox);
			
		}
		//TODO 开启下次定
		
		StageTokenRunable runable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID,this.manager.getStageId(), InnerCmdType.DINGSHI_FLUSH_BOX, null);
		scheduleExportService.schedule(GameConstants.DEFAULT_ROLE_ID.toString(), 
				GameConstants.COMPONENET_DINGSHI_BOX + this.manager.getActivityId(), 
				runable,
				this.manager.getFlushInterval(),
				TimeUnit.MILLISECONDS);StageScheduleExecutor

	}*/
	
	
/*	*//**箱子进入场景*//*
	public void  boxEnterStage(ActivityBox activityBox){
		IStage stage = StageManager.getStage(activityBox.getStageId());
		
		stage.enter(activityBox, activityBox.getBornPosition().getX(), activityBox.getBornPosition().getY());
		//记录到管理器
		this.activityManager.addBox(activityBox);
	}*/
	
	/**定时刷新箱子*/
	public void flushBox(String stageId){
		if(!this.activityManager.activityIsUnderway()){
			return;
		}
		
		IStage stage = StageManager.getStage(stageId);
		if(stage != null){
			String teamId ="" + IdFactory.getInstance().generateNonPersistentId(); 
			String goodsId= dingShiShuaXiangConfig.getJiangItem();	
			if(activityManager.getBoxTeam()==null){
				BoxProduceTeam boxTeam = new BoxProduceTeam(teamId, ElementType.COLLECT, activityManager.getBoxLimit(), goodsId,activityManager.getCanPutPoints() , activityManager.getFlushInterval());
				activityManager.setBoxTeam(boxTeam);
				stage.getStageProduceManager().addElementProduceTeam(boxTeam);	
			}
			activityManager.getBoxTeam().produceAll();
			
		}
		ChuanQiLog.info("宝箱刷新了");
		//开启下次定时
		StageTokenRunable stageTokenRunable = new StageTokenRunable(GameConstants.DEFAULT_ROLE_ID, stageId, InnerCmdType.DINGSHI_FLUSH_BOX, stageId);
		activityManager.getScheduleExecutor().schedule(stageTokenRunable, activityManager.getFlushInterval(), TimeUnit.MILLISECONDS);
	}
	
	
	
	/**开始采集*/
	public Object[] startCollect(String stageId, Long roleId,Long guid ){
	/*	if(!activityManager.activityIsUnderway()){
			return AppErrorCode.ACTIVITY_END;//活动已结束
		}*/
		
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return  AppErrorCode.COLLECT_FAIL;// 采集失败
		}else if(stage.getStageType() == StageType.XIANGONG){//转发探宝活动采集
			StageMsgSender.send2Stage(roleId, ClientCmdType.TANBAO_COLLECT_START, guid);
			return null;
		}else if(stage.getStageType() == StageType.KUAFUQUNXIANYAN){
			return qunXianCaiji(stage, roleId, guid);
		}
		
		IRole role = (IRole)stage.getElement(roleId,ElementType.ROLE);
		if(role == null){
			return AppErrorCode.COLLECT_FAIL;// 采集失败
		}		
			
		ActivityBox box =	(ActivityBox)stage.getElement(guid,ElementType.COLLECT);
		if(box == null){
			return AppErrorCode.BOX_NULL;// 宝箱已被采完
		}
		
		if(role.getLevel() < box.getNeedRoleLevel()){
			return AppErrorCode.LEVEL_LACK; //等级不足，不能采集
		}
		
		long finishTime = GameSystemTime.getSystemMillTime() + box.getCollectDuration();
		
		role.startCollect(guid, finishTime);
		
		return new Object[]{AppErrorCode.SUCCESS,finishTime}; 
	}

	
	public Object[] qunXianCaiji(IStage stage,Long roleId,Long guid ){
		IRole role = (IRole)stage.getElement(roleId,ElementType.ROLE);
		if(role == null){
			return AppErrorCode.COLLECT_FAIL;// 采集失败
		}
		Collect box =	(Collect)stage.getElement(guid,ElementType.COLLECT);
		if(box == null){
			return AppErrorCode.BOX_NULL;// 宝箱已被采完
		}
		long finishTime = GameSystemTime.getSystemMillTime() + box.getCollectTime();
		role.startCollect(guid, finishTime);
		return new Object[]{AppErrorCode.SUCCESS,finishTime}; 
	}
	/**完成采集*/
	public Object[] completeCollect(String stageId, Long roleId){
		/*if(!activityManager.activityIsUnderway()){
			return AppErrorCode.ACTIVITY_END;//活动已结束
		}*/
		IStage stage = StageManager.getStage(stageId);
		if(stage == null) {
			return AppErrorCode.COLLECT_FAIL;// 采集失败;
		}else if(stage.getStageType() == StageType.XIANGONG){//转发探宝活动采集
			StageMsgSender.send2Stage(roleId, ClientCmdType.TANBAO_COLLECT_STOP, null);
			return null;
		}else if(stage.getStageType() == StageType.KUAFUQUNXIANYAN){
			return qunXianYanCaijiWan(stage, roleId);
		}
		
		IRole role = (IRole)stage.getElement(roleId,ElementType.ROLE);
		if(role == null){
			return AppErrorCode.COLLECT_FAIL;// 采集失败
		}	
		
		
		Long guid = role.getCollectId();
		if(guid == null){
			return AppErrorCode.COLLECT_FAIL;// 采集失败
		}
		
		ActivityBox box = (ActivityBox)stage.getElement(guid,ElementType.COLLECT);
		
		if(box == null){
			return AppErrorCode.BOX_NULL;// 宝箱已被采完
		}
		
		role.clearCollect();
		Object [] checkBag =  roleBagExportService.checkPutInBag(box.getAwardId(),1, role.getId());
		if(checkBag!=null){
			return checkBag;
		}
		//物品进背包
		RoleItemInput item = BagUtil.createItem(box.getAwardId(), 1, 0);
		roleBagExportService.putInBag(item, roleId, GoodsSource.COLLECT_BOX, true);
		
		//回收
		activityManager.getBoxTeam().retrieve(box);
		return AppErrorCode.OK;
	}
	
	private Object[] qunXianYanCaijiWan(IStage stage, Long roleId){
		IRole role = (IRole)stage.getElement(roleId,ElementType.ROLE);
		if(role == null){
			return AppErrorCode.COLLECT_FAIL;// 采集失败
		}	
		
		
		Long guid = role.getCollectId();
		if(guid == null){
			return AppErrorCode.COLLECT_FAIL;// 采集失败
		}
		
		Collect box = (Collect)stage.getElement(guid,ElementType.COLLECT);
		
		if(box == null){
			return AppErrorCode.KUAFU_QUNXIANYAN_CAIJI_BEI_CAIWAN;// 宝箱已被采完
		}
		ZiYuanConfig zyConfig = ziYuanConfigExportService.loadById(box.getConfigId());
		if(zyConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}	
		if(zyConfig.getCollectItem() != null && !"".equals(zyConfig.getCollectItem())){
			KuafuMsgSender.send2KuafuSource(roleId,InnerCmdType.KUAFUQUNXIANYAN_CJ_ITEM,new Object[]{zyConfig.getCollectItem(), zyConfig.getCollectItemCount()});
		}
		role.clearCollect();
		//增加积分 
		StageMsgSender.send2StageInner(roleId, stage.getId(), InnerCmdType.KUAFUQUNXIANYAN_ADD_JIFEN, box.getConfigId().toString());
		//回收
		stage.leave(box);
		//采完还是要刷出来的
		KuafuQunXianYanStage kStage = (KuafuQunXianYanStage) stage;
		kStage.schedulerRefreshZiYuan(box.getRefreshTime(), box.getConfigId(),box.getPosition().getX(), box.getPosition().getY());
		//公告
		if(zyConfig.isNotice()){
			KuafuQunXianYanPublicConfig pConfig = getPublicConfig();
			BusMsgSender.send2Many(kStage.getAllRoleIds(),ClientCmdType.NOTIFY_CLIENT_ALERT6, new Object[]{pConfig.getCjCode(),new Object[]{role.getName()}});
		}
		return AppErrorCode.OK;
	}
	
	private KuafuQunXianYanPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.KUAFU_QUNXIANYAN);
	}
}
