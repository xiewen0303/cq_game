/**
 * 
 */
package com.junyou.stage.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.fuben.entity.AbsFubenConfig;
import com.junyou.bus.fuben.entity.FubenConfigManager;
import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.bus.map.export.ActiveMapExportService;
import com.junyou.bus.rolestage.dao.RoleStageDao;
import com.junyou.bus.rolestage.entity.RoleStage;
import com.junyou.bus.skill.entity.RoleSkill;
import com.junyou.bus.skill.export.RoleSkillExportService;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.stagecontroll.position.AbsRolePosition;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.RoleBasePublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.WenquanPublicConfig;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.StageErrorCode;
import com.junyou.stage.StageFightOutputWrapper;
import com.junyou.stage.model.core.element.IElement;
import com.junyou.stage.model.core.stage.DeadDisplay;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.Point;
import com.junyou.stage.model.core.stage.PointTakeupType;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.chongwu.Chongwu;
import com.junyou.stage.model.element.goods.Goods;
import com.junyou.stage.model.element.monster.BossManage;
import com.junyou.stage.model.element.pet.Pet;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.element.role.SyncRoleStageDataUtils;
import com.junyou.stage.model.fight.BattleMode;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.aoi.AoiStage;
import com.junyou.stage.model.stage.fuben.CampStage;
import com.junyou.stage.model.stage.fuben.FubenStageFactory;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.model.stage.fuben.SingleFbStage;
import com.junyou.stage.model.stage.kuafu.KuafuFbStageFactory;
import com.junyou.stage.model.stage.safe.SafeStageFactory;
import com.junyou.stage.model.stage.tanbao.TanBaoManager;
import com.junyou.stage.model.stage.wenquan.WenquanManager;
import com.junyou.stage.tunnel.BufferedMsgWriter;
import com.junyou.stage.tunnel.IMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.stage.utils.StageHelper;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.active.ActiveUtil;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.spring.container.DataContainer;
import com.kernel.sync.GlobalLockManager;
import com.kernel.sync.Lock;

/**
 * @description
 * @author ShiJie Chi
 * @created 2011-11-28下午4:51:53
 */
@Service
public class StageService{
			
	@Autowired
	private RoleStageDao roleStageDao;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private MapConfigExportService mapConfigExportService;
	@Autowired
	private FubenStageFactory fubenStageFactory;
	@Autowired
	private KuafuFbStageFactory kuafuFbStageFactory;
	@Autowired
	private TanBaoStageService tanBaoStageService;
	@Autowired
	private WenquanStageService wenquanStageService;
	@Autowired
	private HcZhengBaSaiStageService hcZhengBaSaiStageService;
	@Autowired
	private ActiveMapExportService activeMapExportService;
	@Autowired
	private RoleSkillExportService roleSkillExportService;
	@Autowired
	private KuafuLuanDouStageService kuafuLuanDouStageService;
	@Autowired
    private KuafuDianFengStageService kuafuDianFengStageService;
	@Autowired
	private KuafuYunGongStageService kuafuYunGongStageService;
	@Autowired
	private MoGongLieYanStageService moGongLieYanStageService;
	@Autowired
	private YunYaoJingMaiStageService yunYaoJingMaiStageService;
	
	public Object leaveStage(String leaveStageId, Long userRoleId) {
		
		IStage stage = StageManager.getStage(leaveStageId);
		if(null == stage){
			return null;
		}
		IRole element = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(element == null){
			return null;
		}
		
		try{
			
			if(element.getFightAttribute().getCurHp() == 0){
				element.getFightAttribute().setCurHp(1);
			}
			
			//退出副本前销毁处理
			leaveCopyBeforeCheckHandle(element,stage);
			
		}catch(Exception e){
			ChuanQiLog.error("leaveStage error,roleId->" + userRoleId, e);
		}finally{
//			//离开
//			leaveTuCheng(userRoleId);Liuyu:花千骨无土城高经验区
			try{
				//离开场景
				stage.leave(element);
			}catch (Exception e) {
				ChuanQiLog.error("离开场景时报错：",e);
			}
			//退出副本后销毁处理
			leaveCopyAfterCheckHandle(element, stage);
		}
		
		return null;
	}

	private void leaveTuCheng(Long userRoleId){
		Map<Long,String> vo = dataContainer.getData(GameConstants.COMPONENT_TUCHEN_EXP, GameConstants.COMPONENT_TUCHEN_EXP);
		if(vo != null && vo.containsKey(userRoleId)){
			vo.remove(userRoleId);
		}
	}
	
	/**
	 * 副本检测并且销毁
	 */
	private void leaveCopyBeforeCheckHandle(IRole role,IStage stage) {
		
	}
	
	
	/**
	 * 副本检测并且销毁
	 */
	private void leaveCopyAfterCheckHandle(IRole role,IStage stage) {
		if(stage.isCanRemove()){//如果场景可回收，则回收掉
			StageManager.removeCopy(stage);
		}
	}
	

	/**
	 * 先离开老的场景后再进入新的场景
	 * @param userRoleId
	 * @param leaveStageId 离开的老场景ID
	 * @param enterStageId 要进入的新场景ID
	 * @param enterX 	要进入的场景x坐标
	 * @param enterY	要进入的场景y坐标
	 */
	public void leaveAfterEnterStage(Long userRoleId,String leaveStageId,String enterStageId,int enterX,int enterY){
		/**
		 * 1.先离开当前场景
		 */
		boolean leaveSuccess = true;
		IStage leaveStage = null;
		if(leaveStageId != null){
			leaveStage = StageManager.getStage(leaveStageId);
		}
		
		IRole element = null;
		if(leaveStage == null){
			leaveSuccess = false;
		}else{
			element = (IRole)leaveStage.getElement(userRoleId, ElementType.ROLE);
			if(element == null){
				leaveSuccess = false;
			}
		}
		
		if(leaveSuccess){
			try{
				//退出副本前销毁处理
				leaveCopyBeforeCheckHandle(element,leaveStage);
				
				//离开
//				leaveTuCheng(userRoleId);Liuyu:花千骨无土城高经验区
				
				leaveCopyCheck(element);
				
				//离开场景
				leaveStage.leave(element);
			}catch (Exception e) {
				ChuanQiLog.error("切换场景时离开报错：",e);
			}
			//退出副本后销毁处理
			leaveCopyAfterCheckHandle(element, leaveStage);
		}
		
		/**
		 * 2.然后进入新场景
		 */
		enterStage(userRoleId, enterStageId, enterX, enterY);
	}
	
	/**
	 * 进入场景
	 * @param userRoleId
	 * @param enterStageId
	 * @param x
	 * @param y
	 * @return
	 */
	public Object enterStage(Long userRoleId,String enterStageId,int x, int y) {
		IStage stage = StageManager.getStage(enterStageId);
		if(stage == null){
			ChuanQiLog.deployInfo("enterStage stage is null stageId="+enterStageId);
			return null;
		}
		
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role != null){
			ChuanQiLog.deployInfo("enterStage stage role is not null roleId="+userRoleId);
		}
		
		if(!KuafuConfigPropUtil.isKuafuServer()){
			role = RoleFactory.create(userRoleId,enterStageId);
			//换场景队伍处理
			StageHelper.getTeamExportService().changeStageHandle(role, stage);
		}else{
			role = RoleFactory.createKuaFu(userRoleId, enterStageId, dataContainer.getData(GameConstants.COMPONENET_KUAFU_DATA,userRoleId+""));
		}

		//跨服小黑屋 不向客户端发送
		if(!stage.getStageType().equals(StageType.KUAFU_SAFE_STAGE)){
			//登陆回馈必先早与所有同步指令
			StageMsgSender.send2One(userRoleId, ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(),x,y,stage.getLineNo()});
		}
		
		//初始化血量
		role.getFightAttribute().setCurHp(role.getFightAttribute().getCurHp());
		
		stage.enter(role, x, y);
		Pet pet = role.getPet();
		if(pet != null && stage.isCanHasTangbao()){
			stage.enter(pet, x, y);
		}
		
		Chongwu chongwu = role.getBusinessData().getChongwu();
		if(chongwu != null && stage.isCanHasChongwu()){
			stage.enter(chongwu, x, y);
		}
		
		campStageHandle(stage, role, x, y);
		wenquanStageHandle(stage, role, x, y);
		enterCopyCheck(role, stage);
		
		clearFubenState(userRoleId);
		//记录角色场景
		publicRoleStateExportService.roleEnter2PublicStage(userRoleId, enterStageId);
		
		if(role.getFightAttribute().getCurHp() == 0){
			role.getFightAttribute().setCurHp(1);
		}
		
		//地图定时关闭业务
		mapDsHandle(role);
		
		//跨服小黑屋 不向客户端发送
		if(!stage.getStageType().equals(StageType.KUAFU_SAFE_STAGE)){
//			推送个人属性
//			StageMsgSender.send2One(userRoleId, ClientCmdType.SELF_ATTRIBUTE, StageOutputWrapper.getAttribute(role));
			//推送个人当前HP MP值
			StageMsgSender.send2One(userRoleId, ClientCmdType.HP_CHANGE, StageFightOutputWrapper.hpChange(role));
			//推送个人BUFF
//			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), StageCommands.ADD_BUFF,new Object[]{role.getId(),role.getBuffManager().getBuffClientMsgs()});
			
			//进入场景开始回血回蓝
//			HpMpPublicConfig hpMpPublicConfig = getHpMpPublicConfig();
//			role.startHpSchedule(hpMpPublicConfig.getHpTimeValue());
//			role.startMpSchedule(hpMpPublicConfig.getMpTimeValue());
//			role.startZySchedule(hpMpPublicConfig.getZyTimeValue());
			//土城高经验区处理
//			TuChenExpPublicConfig tuChenExpPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TUCHEN_EXP);
//			role.startTuChenSchedule(tuChenExpPublicConfig);
		}
		return null;
	}
	/**
	 * 阵营战场景处理（进入后根据自己所在的阵营到指定的坐标）
	 * @param stage
	 * @param role
	 */
	private void campStageHandle(IStage stage, IRole role, int x, int y){
		if(stage.getStageType().equals(StageType.CAMP)){
			CampStage campStage = (CampStage)stage;
			int[] point = campStage.getPoint(role.getZyCamp());
			if(point != null && point.length > 0){
				x = point[0];
				y = point[1];
				//1.给切换的人推送 100
				StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x, y,stage.getLineNo(),1});
				
				stage.teleportTo(role, point[0], point[1]);
				//3.给当前AOI区域内的其它人推送702 单位瞬间移动
				StageMsgSender.send2Many(stage.getNoSelfSurroundRoleIds(role.getPosition(), role.getId()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{role.getId(), x, y});
			}else{
				StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x, y,stage.getLineNo(),1});
			}
		}
	}
	/**
	 * 温泉处理（进入后根据自己是否在高倍区指定的坐标）
	 * @param stage
	 * @param role
	 */
	private void wenquanStageHandle(IStage stage, IRole role, int x, int y){
		if(ActiveUtil.isWenquan() && stage.getStageType().equals(StageType.WENQUAN)){
			if(WenquanManager.getManager().isInHighArea(role.getId())){
				WenquanPublicConfig config = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_WENQUAN);
				int[] highAreaBirthPoint = config.getZuobiao();
				x = highAreaBirthPoint[0];
				y = highAreaBirthPoint[1];
				//1.给切换的人推送 100
				StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x, y,stage.getLineNo()});
				
				stage.teleportTo(role, x, y);
				//3.给当前AOI区域内的其它人推送702 单位瞬间移动
				StageMsgSender.send2Many(stage.getNoSelfSurroundRoleIds(role.getPosition(), role.getId()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{role.getId(), x, y});
			}else{
				StageMsgSender.send2One(role.getId(), ClientCmdType.CHANGE_STAGE, new Object[]{stage.getMapId(), x, y,stage.getLineNo()});
			}
		}
	}
	/**
	 * 地图定时关闭业务
	 * @param role
	 */
	private void mapDsHandle(IRole role){
	}
	
	
	
	/**
	 * 清除副本状态
	 * @param userRoleId
	 */
	private void clearFubenState(Long userRoleId){
		dataContainer.removeData("fuben", userRoleId.toString());
	}
	

	public Object getStageData(Long userRoleId, String curStageId) {
		
		IStage stage = StageManager.getStage(curStageId);
		if(null == stage){
			return null;
		}
		
		IElement element = stage.getElement(userRoleId, ElementType.ROLE);
		if(null != element){
			return element.getStageData();
			
		}
		
		return null;
		
	}

	public void goodsEnterStage(String stageId,Long ownerId, Integer teamId, Integer x,
			Integer y, Object[] goods,Integer protect,Integer disappearDuration,Long monsterGuId,String monsterId){
		
		IStage stage = StageManager.getStage(stageId);
		
		if(null != goods && goods.length > 0){
			
			SearchPointVo searchPointVo = new SearchPointVo();
			for(Object goodsInfo : goods){
				Goods elementResource = (Goods) goodsInfo; 

				//设置掉落人
				elementResource.setDropGuid(monsterGuId);
				//物品掉落
				elementResource.setDuration(disappearDuration);
				
				Point point = calcFinalDropPoint(stage,ownerId,x,y,searchPointVo,1);
				if(point == null){
					ChuanQiLog.error("掉落错误:"+elementResource.getGoodsId() + "没有找到掉落坐标，受益人id："+ownerId);
					return;
				}
				elementResource.setBornPosition(point.getX(), point.getY());
				
				if(protect > 0){
					elementResource.setOwnerId(ownerId);
					elementResource.setOwnTeamId(teamId);
					//保护时间(秒)
					elementResource.setProtect(protect);
				}
				
				elementResource.setDropTime(GameSystemTime.getSystemMillTime());
				stage.enter(elementResource, point.getX(), point.getY());
				searchPointVo.isScanMax();
			}
		}
	}
	
	/**
	 * PK物品掉落
	 * @param stageId
	 * @param attackId
	 * @param targetId
	 * @param targetIsRed
	 */
	public void pkGoodsEnterStage(String stageId,Long attackId,Long targetId,boolean targetIsRed){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		Object[] pkBag = null;//pk爆出的背包中物品
		Object[] pkEquip = null;//pk爆出的装备中物品
//		PKPublicConfig pkPublicConfig = getPKPublicConfig();
//		RoleBusOutInfo targetInfo = roleBusinessInfoExportService.getRoleBusOutInfo(targetId);
//		/**
//		 * ☆ 普通状态：
//		 * 1.背包中的道具装备掉落规则：														
//		 *	1).掉落概率：读取公共数据表中的字段putongdrop，确定是否掉落物品，如果掉落继续2；							0.05						
//		 *	2).掉落个数：读取公共数据表中的字段dropnum(最小值|最大值)，roll出一个值A，此值A为物品掉落的个数，然后随机掉出A个玩家背包和装备栏中所有非绑定的物品。													
//		 * 2.装备栏中的装备掉落规则：														
//		 *	玩家装备栏中的装备掉落概率：读取公共数据表中的字段ptdrop1,确定是否掉落装备，如果掉落，随机roll出玩家装备栏中的一件装备进行掉落。													
//
//		 */
//		if(!targetIsRed){
//			if(Lottery.rollFloat(pkPublicConfig.getPuTongDrop(),Lottery.ONE)){//普通状态下死亡背包的掉落概率
//				//随机背包中的物品和装备栏中的物品掉落物品个数  （最小掉落数|最大掉落数）
//				int dropNum = Lottery.roll(pkPublicConfig.getDropMinNum(), pkPublicConfig.getDropMaxNum());
//				
//				//掉落的物品
//				List<RoleBagSlot> bagList = bagExportServicee.getNoBangDing(targetInfo.getUserRoleId());
//				
//				List<Object> list = randomTopic(getListObject(bagList, null), dropNum);
//				if(list != null && list.size() > 0){
//					List<Object[]> pkBagList = new ArrayList<Object[]>();
//					Map<String, Integer> pkBagMap = new HashMap<String, Integer>();//处理多个物品
//					
//					for (Object object : list) {
//						if(object instanceof RoleBagSlot) {
//							RoleBagSlot bag = (RoleBagSlot) object;
//							int count = 1;
//							//掉落背包中非绑定的物品装备（包括客户端提示），各物品各掉落一个
//							bagExportServicee.pkDiaoLuoGoods(targetInfo.getUserRoleId(), bag.getId(), 1, 0);
//							
//							if(pkBagMap.containsKey(bag.getGoodsId())){
//								count = count + 1;
//							}
//							pkBagMap.put(bag.getGoodsId(), count);
//						}
//					}
//					
//					if(pkBagMap != null && pkBagMap.size() > 0){
//						for (String goodsId : pkBagMap.keySet()) {
//							pkBagList.add(new Object[]{goodsId, pkBagMap.get(goodsId)});
//						}
//						
//						pkBag = pkBagList.toArray();
//					}
//				}
//			}
//			
//			if(Lottery.rollFloat(pkPublicConfig.getPuTongDrop1(),Lottery.ONE)){//普通状态下死亡装备的掉落概率
//				//随机掉落一个装备
//				List<RoleEquipSlot> equipList = roleEquipSlotService.getBagNoBangDingGoods(targetInfo.getUserRoleId());
//				List<Object> list = randomTopic(getListObject(null, equipList), 1);
//				
//				if(list != null && list.size() > 0){
//					RoleEquipSlot roleEquipSlot = (RoleEquipSlot) list.get(0);
//					//掉落装备栏中非绑定的物品装备（包括客户端提示）
//					roleEquipSlotService.pkDiaoLuoEquip(targetInfo.getUserRoleId(), roleEquipSlot.getId());
//					pkEquip = new Object[]{new Object[]{ roleEquipSlot.getGoodsId()}};
//				}
//			}
//		
//		/**
//		 * ☆ 红名状态：
//		 * 	1.背包中的物品装备掉落概率：背包中所有非绑定的物品装备全部掉落；
//		 *  2.玩家装备栏中的装备掉落概率：读取公共数据表中的字段reddrop,确定是否掉落装备，如果掉落，随机roll出玩家装备栏中的一件装备进行掉落。
//		 *
//		 */
//		}else{
//			//掉落背包中非绑定的所有物品装备（包括客户端提示）
//			pkBag = bagExportServicee.pkDiaoLuoGoods(targetInfo.getUserRoleId(), null, null, 1);
//			//红名时，装备栏装备掉落概率
//			if(Lottery.rollFloat(pkPublicConfig.getRedDrop(),Lottery.ONE)){
//				//随机掉落一个装备
//				List<RoleEquipSlot> equipList = roleEquipSlotService.getBagNoBangDingGoods(targetInfo.getUserRoleId());
//				List<Object> list = randomTopic(getListObject(null, equipList), 1);
//				
//				if(list != null && list.size() > 0){
//					RoleEquipSlot roleEquipSlot = (RoleEquipSlot) list.get(0);
//					//掉落装备栏中非绑定的物品装备（包括客户端提示）
//					roleEquipSlotService.pkDiaoLuoEquip(targetInfo.getUserRoleId(), roleEquipSlot.getId());
//					pkEquip = new Object[]{new Object[]{ roleEquipSlot.getGoodsId()}};
//				}
//			}
//		}
		
		if(pkBag != null || pkEquip != null){
			StageMsgSender.send2One(attackId, ClientCmdType.PK_DIAOLUO_GOODS, new Object[]{pkBag, pkEquip});
		}	
	}
	
	
	/**
	 * 临时变量
	 * @author DaoZheng Yuan
	 * 2013-11-2 下午6:20:23
	 */
	private class SearchPointVo{
		
		private int lay = 1;
		
		private int scanIndex = 1;

		
		

		public int getLay() {
			return lay;
		}


		public int getScanIndex() {
			return scanIndex;
		}

		public void setScanIndex(int scanIndex) {
			this.scanIndex = scanIndex;
		}
		
		/**
		 * 是否达到最大值
		 * @return true:最大值
		 */
		public boolean isScanMax(){
			//每一圈的一边格子数  (从3格开始)
			int xbase = lay * 2 + 1;
			
			//每一圈的格子最大个数
			int scanMax = xbase * 2 + (xbase - 2) * 2;
			
			boolean flag = scanIndex > scanMax;
			if(flag){
				lay++;
				scanIndex = 1;
			}
			
			return flag;
		}
		
		
	}
	
	
	private Point calcFinalDropPoint(IStage stage, Long ownerId,Integer x,Integer y,SearchPointVo searchPointVo,int lay) {
		//异常层处理
		IRole role = null;
		if(lay > 10){
			role = stage.getElement(ownerId, ElementType.ROLE);
			if(role == null){
				return null;
			}
			Point tmPoint = role.getPosition();
			x = tmPoint.getX();
			y = tmPoint.getY();
			
			return stage.getSurroundValidPoint(new Point(x, y),false, PointTakeupType.GOODS);
		}
		
		Point point = calcDropPoint(stage, ownerId, x, y, searchPointVo);
		if(point == null){
			searchPointVo.isScanMax();
			point = calcFinalDropPoint(stage, ownerId, x, y, searchPointVo,lay+1);
		}
		
		return point;
	}
	
	/**
	 * 计算物品掉落坐标
	 * @param stage
	 * @param ownerId
	 * @param x
	 * @param y
	 * @return null 当前没有计算到坐标
	 */
	private Point calcDropPoint(IStage stage, Long ownerId,Integer x,Integer y,SearchPointVo searchPointVo) {
		
		IRole role = null;
		if(null == x || null == y){
			role = stage.getElement(ownerId, ElementType.ROLE);
			Point point = role.getPosition();
			
			x = point.getX();
			y = point.getY();
			
			return stage.getSurroundValidPoint(new Point(x, y),false, PointTakeupType.GOODS);
		}
		
		Point dropPoint = null;
		
		int lay = searchPointVo.getLay();
		//每一圈的一边格子数  (从3格开始)
		int xbase = lay * 2 + 1;
		
		//每一圈的格子最大个数
		int scanMax = xbase * 2 + (xbase - 2) * 2;

		
		for (int i = searchPointVo.getScanIndex(); i <= scanMax+1; i++) {
			
			int tmpX = 0;
			int tmpY = 0;
			
			if(i <= xbase){
				//最上面一横排  y坐标一样
				tmpY = y - lay;
				
				tmpX = x - lay + (xbase - (xbase - i + 1) );
				
			}else if(i >= (xbase * 2 - 1) && i <= (xbase * 2 + (xbase - 2))){
				//最下面一横排 y坐标一样
				tmpY = y + lay;
				
				tmpX = x + (xbase / 2) - ((i+1) - (xbase * 2));
				
			}
			
			if(i > xbase && i < xbase * 2 - 1){
				//最右一坚排 x坐标一样
				tmpX = x + lay;
				
				tmpY = y - (xbase / 2 - 1)  + (i - 1 - xbase );
				
			}else if(i > xbase * 3 - 2 && i <= scanMax){
				//最左一坚排 x坐标一样
				tmpX = x - lay;
				
				tmpY = y + (xbase / 2 - 1) - (i - (xbase * 3 - 1));
			}
			
			searchPointVo.setScanIndex(searchPointVo.getScanIndex() + 1);
			
			if(tmpX != 0 && tmpY != 0){
				dropPoint = new Point(tmpX, tmpY);
				boolean canUse = stage.checkCanUseStagePoint(dropPoint);
				if(canUse){
					break;
				}
			}else{
				return null;
			}
			
		}
		
		return dropPoint;
		
	}

	public void goodsEnterStage(String stageId,Long ownerId, Integer teamId, Integer x,
			Integer y, Object[] goods,Integer protect,Integer disappearDuration) {
		
		IStage stage = StageManager.getStage(stageId);
		
		if(null != goods && goods.length > 0){
			for(Object goodsInfo : goods){
				Goods elementResource = (Goods)goodsInfo;
//				Goods elementResource = GoodsFactory.create(goodsInfo,null);
				
				if(ModulePropIdConstant.MONEY_GOODS_ID.equals(elementResource.getGoodsId())){
					//通知客户端
//					StageMsgSender.send2One(ownerId, StageCommands.MONSTER_DROP_MONEY, elementResource.getCount());
					
					//金币掉落
//					accountExportService.incrCurrencyWithNotify(AccountType.TONGQIAN, elementResource.getCount(), ownerId, LogChuanQiUtil.GET_KILL_MONSTER);
				}else{
					//物品掉落
					elementResource.setDuration(disappearDuration);
					
					Point point = createDropPoint(stage,ownerId,x,y);
					elementResource.setBornPosition(point.getX(), point.getY());
					
					if(protect == 1){
						elementResource.setOwnerId(ownerId);
						elementResource.setOwnTeamId(teamId);
					}
					
					elementResource.setDropTime(GameSystemTime.getSystemMillTime());
					
					stage.enter(elementResource, point.getX(), point.getY());
				}

			}
		}
		
		
	}

	/**
	 * 创建掉落坐标
	 */
	private Point createDropPoint(IStage stage, Long ownerId,Integer x,Integer y) {
		
		
		if(null == x || null == y){
			Point point = stage.getElement(ownerId, ElementType.ROLE).getPosition();
			x = point.getX();
			y = point.getY();
		}
		
		Point dropPoint = stage.getSurroundValidPoint(new Point(x, y),false, PointTakeupType.GOODS);
		
		return dropPoint;
	}

	public void goodsDisappear(Long id, String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		Goods goods = (Goods)stage.getElement(id, ElementType.GOODS);
		
		if(null != goods){
			stage.leave(goods);
		}
	}
	
	
	public void innerStageLevelFuben(String stageId, Long userRoleId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role != null && role.getStateManager().isDead()){
			//满血复活
			role.getFightAttribute().setCurHp(role.getFightAttribute().getMaxHp());
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			role.getFightStatistic().flushChanges(writer);
			writer.flush();
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.TOWN_REVIVE, role.getId());
			
		}
		//申请切场景指令
//		StageMsgSender.send2StageControl(userRoleId, StageCommands.INNER_APPLY_LEAVE_FUBEN,null);
		
	}
	public void innerStageLevelTFFuben(String stageId, Long userRoleId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole role = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		if(role != null && role.getStateManager().isDead()){
			//满血复活
			role.getFightAttribute().setCurHp(role.getFightAttribute().getMaxHp());
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			role.getFightStatistic().flushChanges(writer);
			writer.flush();
			StageMsgSender.send2Many(stage.getSurroundRoleIds(role.getPosition()), ClientCmdType.TOWN_REVIVE, role.getId());
			
		}
		
		//申请切场景指令
//		StageMsgSender.send2StageControl(userRoleId, StageCommands.INNER_APPLY_LEAVE_TF_FUBEN,null);
	}

	public Integer[] getPosition(Long roleId, String stageId) {
		
		IStage stage = StageManager.getStage(stageId);
		IElement element = stage.getElement(roleId, ElementType.ROLE);
		if(null != element){
			
			Point point = element.getPosition();
			return new Integer[]{point.getX(),point.getY()};
		}
		
		return null;
	}

	public Object teleportCheck(Long roleId, String stageId) {
		
		IStage stage = StageManager.getStage(stageId);
		IElement element = stage.getElement(roleId, ElementType.ROLE);
		if(null != element){
			
			if(element.getStateManager().isDead()){
				return StageErrorCode.TELEPORT_IN_DEAD_ERROR;
			}
			
//			if(element.getStateManager().contains(StateType.FIGHT)){
//				return StageErrorCode.TELEPORT_IN_FIGHTING_ERROR;
//			}
		}
		
		return null;
	}

	public boolean inFighting(Long roleId, String stageId) {
		
		IStage stage = StageManager.getStage(stageId);
		IElement element = stage.getElement(roleId, ElementType.ROLE);
		
		return element.getStateManager().contains(StateType.FIGHT);
	}
	
	
	public boolean stageIsExist(String stageId) {
		return StageManager.exist(stageId);
	}
	
	public boolean checkAndCreateStage(String stageId,Integer mapId,Integer lineNo) throws Exception {
		
		//是否为新创建的场景
		boolean isNew = StageManager.checkAndCreateStage(stageId,mapId,lineNo); 
		
		if( isNew ){
			
			//新创建的地图，判断是否有新元素进入
			otherElementCreate(stageId);
			
		}
		return isNew;
	}
	
	public boolean checkPublicFubenStageIsExist(String stageId,Integer mapType,Integer activeId) {
		IStage iStage = StageManager.getStage(stageId);
		if(iStage == null){
			if(MapType.XIANGONG_TANBAO_MAP.equals(mapType)){//仙宫探宝需要创建
				if(TanBaoManager.getManager().isStarting()){
					String mapId = StageUtil.getMapId(stageId);
					int line = StageUtil.getLineNo(stageId);
					tanBaoStageService.createTanBaoStage(CovertObjectUtil.object2Integer(mapId),line , line);
					iStage = StageManager.getStage(stageId);
				}
			}
			if(MapType.WENQUAN_MAP_TYPE.equals(mapType)){//温泉
				if(ActiveUtil.isWenquan()){
					String mapId = StageUtil.getMapId(stageId);
					int line = StageUtil.getLineNo(stageId);
					wenquanStageService.createWenquanStage(CovertObjectUtil.object2Integer(mapId),line , line);
					iStage = StageManager.getStage(stageId);
				}
			}
			if(MapType.HC_ZBS_MAP_TYPE.equals(mapType)){//争霸赛
				if(ActiveUtil.isHcZBS()){
					String mapId = StageUtil.getMapId(stageId);
					hcZhengBaSaiStageService.createStage(CovertObjectUtil.object2Integer(mapId));
					iStage = StageManager.getStage(stageId);
				}
			}
			if(MapType.REFB_ACTIVE_MAP_TYPE.equals(mapType)){//热发布活动地图
				if(activeId != null){
					iStage = activeMapExportService.createStage(stageId, activeId);
				}
			}
			if(MapType.MOGONGLIEYAN_MAP.equals(mapType)){//魔宫烈焰
			    String mapId = StageUtil.getMapId(stageId);
			    int lineNo = StageUtil.getLineNo(stageId);
			    iStage = moGongLieYanStageService.createMoGongLieYanStage(stageId, CovertObjectUtil.object2Integer(mapId), lineNo);
			}
			if(MapType.YUNYAOJINGMAI_MAP.equals(mapType)){//云瑶晶脉
                String mapId = StageUtil.getMapId(stageId);
                int lineNo = StageUtil.getLineNo(stageId);
			    iStage = yunYaoJingMaiStageService.createYunYaoJingMaiStage(stageId,CovertObjectUtil.object2Integer(mapId), lineNo);
            }
			if(iStage == null){
				ChuanQiLog.error("活动场景不存在,mapType:{},场景id:{} activeId:{}",mapType,stageId,activeId);
				return false;
			}
		}
		if(StageType.isPublicFuben(iStage.getStageType())){
			PublicFubenStage stage = (PublicFubenStage)iStage;
			return stage.isOpen();
		}
		ChuanQiLog.error("当前活动场景不是公共副本场景,场景id：{},场景类型：{}",stageId,iStage.getStageType());
		return false;
	}
	
	public void createFuben(Long roleId, String stageId, Integer mapId, Integer fubenId, Integer mapType, Object data,DeadDisplay deadDisplay) {
		if(fubenId == null){//部分副本没有副本id：塔防副本
			fubenId = -1;
		}
		AbsFubenConfig config = FubenConfigManager.getConfig(mapType,fubenId);
		
		MapConfig mapConfig = mapConfigExportService.load(mapId);
		
		AoiStage aoiStage = fubenStageFactory.create(roleId, stageId, config, mapConfig,data,deadDisplay);
		
		StageManager.addStageCopy(aoiStage);
		
		if(aoiStage instanceof SingleFbStage){
			SingleFbStage stage = (SingleFbStage)aoiStage;
			stage.noticeClientKillInfo(roleId);
		}
		
	}
	
//	public void checkAndCreateKuafuFuben(Long roleId, String stageId, Integer mapId, Integer mapType) {
//		IStage iStage = StageManager.getStage(stageId);
//		if(iStage != null){
//			KuafuFbStage stage = (KuafuFbStage)iStage;
//			if(stage.isEnd()){
//				stage.reStart();
//			}
//			return;
//		}
//		
//		Lock lock = GlobalLockManager.getInstance().getLock(GameConstants.COMPONENT_STAGE_ASYN_LOCK, stageId);
//		synchronized (lock) {
//			iStage = StageManager.getStage(stageId);
//			if(iStage != null){
//				return;
//			}
//			
//			MapConfig mapConfig = mapConfigExportService.load(mapId);
//			iStage = kuafuFbStageFactory.create(roleId, stageId, mapConfig);
//			StageManager.addStageCopy(iStage);		
//		}
//		
//	}
	public void checkAndCreateSafeStage(Long roleId, String stageId, Integer mapId, Integer mapType) {
		IStage stage = StageManager.getStage(stageId);
		if(stage != null){
			return;
		}
		
		Lock lock = GlobalLockManager.getInstance().getLock(GameConstants.COMPONENT_STAGE_ASYN_LOCK, stageId);
		synchronized (lock) {
			stage = StageManager.getStage(stageId);
			if(stage != null){
				return;
			}
			
			MapConfig mapConfig = mapConfigExportService.load(mapId);
			stage = SafeStageFactory.create(roleId, stageId, mapConfig,mapType);
			StageManager.addStageCopy(stage);		
		}
		
	}
	
	public void checkAndCreateMoreFuben(Long roleId, String stageId, Integer mapId, Integer mapType,Integer fubenId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage != null){
			return;
		}
		
		AbsFubenConfig config = FubenConfigManager.getConfig(mapType,fubenId);
		MapConfig mapConfig = mapConfigExportService.load(mapId);

		Lock lock = GlobalLockManager.getInstance().getLock(GameConstants.COMPONENT_STAGE_ASYN_LOCK, stageId);
		
		synchronized (lock) {
			stage = StageManager.getStage(stageId);
			if(stage != null){
				return;
			}
			
			stage = fubenStageFactory.create(roleId, stageId, config,mapConfig,null);
			StageManager.addStageCopy(stage);		
		}
	}
	public void checkAndCreateBaguaFuben(Long roleId, String stageId, Integer mapId, Integer mapType,Integer fubenId,Integer ceng) {
		IStage stage = StageManager.getStage(stageId);
		if(stage != null){
			return;
		}
		
		AbsFubenConfig config = FubenConfigManager.getConfig(mapType,fubenId);
		MapConfig mapConfig = mapConfigExportService.load(mapId);

		Lock lock = GlobalLockManager.getInstance().getLock(GameConstants.COMPONENT_STAGE_ASYN_LOCK, stageId);
		
		synchronized (lock) {
			stage = StageManager.getStage(stageId);
			if(stage != null){
				return;
			}
			
			stage = fubenStageFactory.create(roleId, stageId, config,mapConfig,ceng);
			StageManager.addStageCopy(stage);		
		}
	}
	
	public void checkAndCreateMaiguFuben(Long roleId, String stageId, Integer mapId, Integer mapType,Integer fubenId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage != null){
			return;
		}
		
		AbsFubenConfig config = FubenConfigManager.getConfig(mapType,fubenId);
		MapConfig mapConfig = mapConfigExportService.load(mapId);

		Lock lock = GlobalLockManager.getInstance().getLock(GameConstants.COMPONENT_STAGE_ASYN_LOCK, stageId);
		
		synchronized (lock) {
			stage = StageManager.getStage(stageId);
			if(stage != null){
				return;
			}
			
			stage = fubenStageFactory.create(roleId, stageId, config,mapConfig,null);
			StageManager.addStageCopy(stage);		
		}
	}
	
	public void checkAndCreateDaLuanDou(Long roleId, String stageId, Integer mapId, Integer mapType) {
		IStage stage = StageManager.getStage(stageId);
		if(stage != null){
			return;
		}
		
		//AbsFubenConfig config = FubenConfigManager.getConfig(mapType,fubenId);
		//MapConfig mapConfig = mapConfigExportService.load(mapId);
		
		Lock lock = GlobalLockManager.getInstance().getLock(GameConstants.COMPONENT_STAGE_ASYN_LOCK, stageId);
		
		synchronized (lock) {
			stage = StageManager.getStage(stageId);
			if(stage != null){
				return;
			}
			stage = kuafuLuanDouStageService.createKuafuLuanDouStage(mapId, stageId);
			//stage = kuafuLuanDouStageService.create(roleId, stageId, config,mapConfig,null);
			StageManager.addStageCopy(stage);		
		}
	}
	
	public void checkAndCreateKuafuArenaSingle(Long roleId, String stageId, Integer mapId, Integer mapType) {
		IStage stage = StageManager.getStage(stageId);
		if(stage != null){
			return;
		}
		
		MapConfig mapConfig = mapConfigExportService.load(mapId);

		Lock lock = GlobalLockManager.getInstance().getLock(GameConstants.COMPONENT_STAGE_ASYN_LOCK, stageId);
		
		synchronized (lock) {
			stage = StageManager.getStage(stageId);
			if(stage != null){
				return;
			}
			
			stage = kuafuFbStageFactory.create(stageId, mapConfig);
			StageManager.addStageCopy(stage);		
		}
	}
	
	public void otherElementCreate(String stageId) {
		
	}
	
	public void syncRoleStageData(Long userRoleId, AbsRolePosition curPosition,Integer targetMapId,boolean exitGameOrNot) {
		
		if(KuafuConfigPropUtil.isKuafuServer()){
			return;//跨服不处理
		}
		
		IStage stage = StageManager.getStage(curPosition.getStageId());
		if(stage == null){
			return;
		}
		
		IRole element = (IRole)stage.getElement(userRoleId, ElementType.ROLE);
		
		if(null == element){
			ChuanQiLog.error("syncRoleStageData error: element is null:" + userRoleId);
			return;
		}
		
		//保存相关需要持久化数据
		Long hp = element.getFightAttribute().getCurHp();
		
		Integer mapId = stage.getMapId();
		Integer x = element.getPosition().getX();
		Integer y = element.getPosition().getY();
		Integer lineNo = stage.getLineNo();
		
		if(!exitGameOrNot){//如果不是退出游戏，更新当前点坐标
			curPosition.setPosition(x, y);
		}
		
		String buffInfo = SyncRoleStageDataUtils.encodeBuffs(element.getBuffManager().getBuffs(),exitGameOrNot);
		
		RoleStage roleStage = null;
		if(KuafuConfigPropUtil.isKuafuServer()){
			roleStage = dataContainer.getData(GameConstants.DATA_CONTAINER_ROLESTAGE, userRoleId.toString());
		}else{
			roleStage = roleStageDao.cacheLoad(userRoleId, userRoleId);
		}
		
		//当前地图是皇城地图
		DiTuConfig curDiTuConfig = diTuConfigExportService.loadDiTu(mapId);
		boolean isHuiChen = curDiTuConfig.getIsHuiChen();
		boolean isDead = element.getStateManager().isDead();
		
		//如果死亡直接设置复活点，回满血量和蓝量
		if(isDead || isHuiChen){
			
			DiTuConfig config = diTuConfigExportService.loadDiTu(roleStage.getLastMainMap());
			if(null != config){
				mapId  = config.getId();
				
				int[] birthXy = config.getRandomBirth();
				x = birthXy[0];
				y = birthXy[1];
				
				if(isDead){
					
					if(stage.isCopy()){
						hp = element.getFightAttribute().getMaxHp();
					}else{
						RoleBasePublicConfig roleBasePublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_ROLE_BASE);
						
						hp = (long)(element.getFightAttribute().getMaxHp() * roleBasePublicConfig.getPtHpBv());
					}
				}
				
			}else{
				//找不到复活地图用默认地图
				config = diTuConfigExportService.loadFirstMainDiTu();
				
				mapId  = config.getId();
				int[] birthXy = config.getRandomBirth();
				x = birthXy[0];
				y = birthXy[1];
				
				if(isDead){
					hp = element.getFightAttribute().getMaxHp();
				}
				
				if(!stage.isCopy()){
					ChuanQiLog.error("no revive config:" + stage.getMapId());
				}
			}
		}
		
		roleStage.setBuff(buffInfo);
		
		if(targetMapId != null){
			DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(targetMapId);
			//记录指定地图
			roleStage.setLastMainMap(diTuConfig.getReviveMapId());
		}
		
		roleStage.setProp( element.getPropModel().format() );
		
		roleStage.setHp(hp);
		roleStage.setNuqi(element.getNuqi());
		
		//副本数据暂时不存
		if(!stage.isCopy()){
			roleStage.setMapId(mapId);
			roleStage.setMapX(x);
			roleStage.setMapY(y);
			roleStage.setLineNo(lineNo);
		}
		
		if(KuafuConfigPropUtil.isKuafuServer()){
			dataContainer.putData(GameConstants.DATA_CONTAINER_ROLESTAGE, userRoleId.toString(), roleStage);
		}else{
			roleStageDao.cacheUpdate(roleStage, userRoleId);
		}
		try{
			for (RoleSkill skill : element.getRoleSkills()) {
				roleSkillExportService.saveSkill(skill);
			}
		}catch (Exception e) {
			ChuanQiLog.error("同步角色技能熟练度时错误。");
		}
	}
	
	
	public void saveExceptionStageData(Long userRoleId, Integer mapId) {
		RoleStage roleStage =  roleStageDao.cacheLoad(userRoleId, userRoleId);
		
		roleStage.setMapId(mapId);
		
		DiTuConfig config = diTuConfigExportService.loadDiTu(mapId);
		int[] birthXy = config.getRandomBirth();
		roleStage.setMapX(birthXy[0]);
		roleStage.setMapY(birthXy[1]);
		
		roleStageDao.cacheUpdate(roleStage, userRoleId);
	}
	
	private void enterCopyCheck(IRole role,IStage stage) {
		//主动下马业务
		unRideHandle(role, stage);
		
		
		Integer mapId = stage.getMapId();
		DiTuConfig diTuConfig = diTuConfigExportService.loadDiTu(mapId);
		if(diTuConfig != null && diTuConfig.getMode() > 0){
			//设置指定模式 
			Object[]  battleMode = dataContainer.getData(GameConstants.BATTLE_MODE, role.getId().toString());
			if(battleMode != null){
				battleMode[0] = 1;
			}
			role.setBattleMode(BattleMode.convert(diTuConfig.getMode()-1));
		}
	}
	
	private void leaveCopyCheck(IRole role) {
		//设置指定模式
		Object[]  battleMode = dataContainer.getData(GameConstants.BATTLE_MODE, role.getId().toString());
		if(battleMode != null && battleMode[0].equals(1)){
			StageMsgSender.send2One(role.getId(), ClientCmdType.BATTLE_MODE, ((BattleMode)battleMode[1]).getVal());
			dataContainer.removeData(GameConstants.BATTLE_MODE, role.getId().toString());
		}else{
			dataContainer.putData(GameConstants.BATTLE_MODE, role.getId().toString(), new Object[]{0,role.getBattleMode()});
		}
	}
	
	/**
	 * 主动下马业务
	 * @param role
	 * @param stage
	 */
	private void unRideHandle(IRole role,IStage stage){
		if(!stage.isCanFeijian() && role.getStateManager().contains(StateType.ZUOQI)){
			//如果场景不允许上坐骑，则自动下坐骑
			StageMsgSender.send2Bus(role.getId(), ClientCmdType.ZUOQI_DOWN, new Object[]{role.getId()});
		}
	}
	
	
	/**
	 * 获取BOSS信息
	 * @param monsterIds
	 * @return
	 */
	public Object getBossesInfo(Object[] monsterIds){
		return BossManage.getBossesData(monsterIds);
	}

    /**
     * 检查巅峰之战场景未创建则new
     * @param dfPosition
     */
    public void checkAndCreateDianFengStage(String stageId, Integer mapId, Integer loop, Integer room, Integer mapType) {
        if (null == mapType || MapType.KUAFU_DIANFENG_MAP != mapType) {
            return;
        }

        IStage stage = StageManager.getStage(stageId);
        if (stage != null) {
            return;
        }
        Lock lock = GlobalLockManager.getInstance().getLock(GameConstants.COMPONENT_STAGE_ASYN_LOCK, stageId);
        synchronized (lock) {
            stage = StageManager.getStage(stageId);
            if (stage != null) {
                return;
            }
            kuafuDianFengStageService.createKuafuDianFengStage(stageId, mapId, loop, room);
        }
    }

    /**
     * 检查云宫之巅场景未创建则new
     * @param roleId
     * @param stageId
     * @param mapId
     * @param mapType 
     */
    public void checkAndCreateYunGongStage(String stageId, Integer mapId, Integer mapType) {
        if(null == mapType || MapType.KUAFU_YUNGONG_MAP != mapType){
            return;
        }
        IStage stage = StageManager.getStage(stageId);
        if(stage != null){
            return;
        }
        Lock lock = GlobalLockManager.getInstance().getLock(GameConstants.COMPONENT_STAGE_ASYN_LOCK, stageId);
        synchronized (lock) {
            stage = StageManager.getStage(stageId);
            if(stage != null){
                return;
            }
            kuafuYunGongStageService.createKuafuYunGongStage(stageId, mapId);
        }
    }

}
