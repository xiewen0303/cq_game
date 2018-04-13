package com.junyou.stage.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.tanbao.entity.TanBaoBaoXiangConfig;
import com.junyou.bus.tanbao.entity.XianGongXunLuoMonsterConfig;
import com.junyou.bus.tanbao.export.TanBaoBaoXiangConfigExportService;
import com.junyou.bus.tanbao.export.TanBaoExpConfigExportService;
import com.junyou.bus.tanbao.export.XianGongXunLuoMonsterConfigExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.DropIdType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.DropRule;
import com.junyou.gameconfig.export.ZuBaoConfigExportService;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfigExportService;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.monster.configure.export.MonsterConfig;
import com.junyou.gameconfig.monster.configure.export.MonsterExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.TanBaoPublicConfig;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.stage.configure.export.impl.ZiYuanConfig;
import com.junyou.stage.configure.export.impl.ZiYuanConfigExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.core.stage.aoi.AoiPoint;
import com.junyou.stage.model.core.state.StateType;
import com.junyou.stage.model.element.goods.CollectFacory;
import com.junyou.stage.model.element.goods.Goods;
import com.junyou.stage.model.element.goods.TanBaoBox;
import com.junyou.stage.model.element.monster.IMonster;
import com.junyou.stage.model.element.monster.MonsterFactory;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.model.stage.tanbao.TanBaoManager;
import com.junyou.stage.model.stage.tanbao.TanBaoRoleVo;
import com.junyou.stage.model.stage.tanbao.TanBaoStage;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.BufferedMsgWriter;
import com.junyou.stage.tunnel.IMsgWriter;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.gen.id.IdFactory;

@Service
public class TanBaoStageService {
	@Autowired
	private DingShiActiveConfigExportService dingShiActiveConfigExportService;
	@Autowired
	private PublicFubenStageFactory publicFubenStageFactory;
	@Autowired
	private MapConfigExportService mapConfigExportService;
	@Autowired
	private TanBaoBaoXiangConfigExportService tanBaoBaoXiangConfigExportService;
	@Autowired
	private ZiYuanConfigExportService ziYuanConfigExportService;
	@Autowired
	private XianGongXunLuoMonsterConfigExportService xianGongXunLuoMonsterConfigExportService;
	@Autowired
	private MonsterExportService monsterExportService;	
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private ZuBaoConfigExportService zuBaoConfigExportService;
	@Autowired
	private TanBaoExpConfigExportService tanBaoExpConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	
	
	public void activeStart(Integer id){
		DingShiActiveConfig config = dingShiActiveConfigExportService.getConfig(id);
		if(config == null){
			return;
		}
		TanBaoManager.getManager().clear();
		
		Integer endLine = Integer.parseInt(config.getData1());
		Integer mapId = config.getMapId();
		createTanBaoStage(mapId, 1, endLine);
		TanBaoManager.getManager().activeStart(config.getMinLevel());
	}
	
	public void createTanBaoStage(Integer mapId,Integer startLine,Integer endLine){
		MapConfig mapConfig = mapConfigExportService.load(mapId);
		Map<Integer, TanBaoBaoXiangConfig> boxConfigs = tanBaoBaoXiangConfigExportService.getConfigs();
		List<XianGongXunLuoMonsterConfig> xunluoConfigs = xianGongXunLuoMonsterConfigExportService.getAllConfigs();
		for (int i = startLine; i <= endLine; i++) {
			PublicFubenStage stage = publicFubenStageFactory.create(StageUtil.getStageId(mapId, i), i, mapConfig);
			if(stage != null){
				TanBaoStage tanbaoStage = (TanBaoStage)stage;
				for (TanBaoBaoXiangConfig boxConfig : boxConfigs.values()) {
					ZiYuanConfig ziYuanConfig = ziYuanConfigExportService.loadById(boxConfig.getId());
					for (Integer[] zuobiao : boxConfig.getZuobiao()) {
						TanBaoBox box = CollectFacory.create(IdFactory.getInstance().generateNonPersistentId(), ziYuanConfig);
						stage.enter(box, zuobiao[0], zuobiao[1]);
					}
				}
				for (XianGongXunLuoMonsterConfig xianGongXunLuoMonsterConfig : xunluoConfigs) {
					MonsterConfig monsterConfig = monsterExportService.load(xianGongXunLuoMonsterConfig.getMonsterId());
					IMonster monster = MonsterFactory.createXianGongMonster(IdFactory.getInstance().generateNonPersistentId(), xianGongXunLuoMonsterConfig, monsterConfig);
					AoiPoint born = xianGongXunLuoMonsterConfig.getLujing().get(0);
					stage.enter(monster, born.getX(), born.getY());
				}
				TanBaoManager.getManager().addStage(tanbaoStage);
				stage.start();
			}
		}
	}
	
	public void productBox(Object[] data){
		Integer configId = (Integer)data[0];
		String stageId = (String)data[1];
		Integer x = (Integer)data[2];
		Integer y = (Integer)data[3];
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		ZiYuanConfig ziYuanConfig = ziYuanConfigExportService.loadById(configId);
		TanBaoBox box = CollectFacory.create(IdFactory.getInstance().generateNonPersistentId(), ziYuanConfig);
		Object[] roleIds = stage.getAllRoleIds();
		TanBaoBaoXiangConfig config = tanBaoBaoXiangConfigExportService.getConfigById(box.getCollectConfigId());
		if(config != null && config.getGonggao() != null){
			if(roleIds != null){
				StageMsgSender.send2Many(roleIds, ClientCmdType.NOTIFY_CLIENT_ALERT6, new Object[]{config.getGonggao(),new Object[]{box.getName()}});
			}
		}
		stage.enter(box, x,y);
	}
	
	/**
	 * 开始开箱
	 * @param userRoleId
	 * @param stageId
	 * @param guid
	 */
	public Object[] startBox(Long userRoleId,String stageId,Long guid){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}
		TanBaoBox box = stage.getElement(guid, ElementType.COLLECT);
		if(box == null){
			return AppErrorCode.BOX_NULL;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		long finishTime = GameSystemTime.getSystemMillTime() + box.getCollectTime();
		role.startCollect(guid, finishTime);
		return box.getSuccessMsg();
	}
	
	/**
	 * 开启箱子
	 * @param userRoleId
	 * @param stageId
	 */
	public Object[] openBox(Long userRoleId,String stageId){
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		Long guid = role.getCollectId();
		if(guid == null){
			return AppErrorCode.BOX_NULL;
		}
		role.clearCollect();
		TanBaoBox box = stage.getElement(guid, ElementType.COLLECT);
		if(box == null){
			return AppErrorCode.BOX_NULL;
		}
		stage.leave(box);
		TanBaoBaoXiangConfig config = tanBaoBaoXiangConfigExportService.getConfigById(box.getCollectConfigId());
		if(config != null){
			List<Goods> dropGoodsSet = new ArrayList<>();
			
			List<DropRule> dropRules = config.getDropRule();
			if(dropRules != null && dropRules.size() > 0){
				for(DropRule dropRule : dropRules){
					List<Goods> goods = dropCal(dropRule, role.getLevel());
					dropGoodsSet.addAll(goods);
				}
			}
			if(dropGoodsSet.size() > 0){
				Object[] data = new Object[]{userRoleId, null, box.getPosition().getX(), box.getPosition().getY(), dropGoodsSet.toArray(), config.getDropProtectDuration(), config.getDropGoodsDuration(), null, guid};
				StageMsgSender.send2StageInner(userRoleId,stageId,InnerCmdType.DROP_GOODS, data);
			}
			if(config.getGonggao1() != null){
				Object[] roleIds = stage.getAllRoleIds();
				if(roleIds != null){
					StageMsgSender.send2Many(roleIds, ClientCmdType.NOTIFY_CLIENT_ALERT6, new Object[]{config.getGonggao1(),new Object[]{box.getName(),role.getName()}});
				}
			}
		}
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.GET_MOGONG_BAOXIANG, null});
		return null;
	}
	
	private List<Goods> dropCal(DropRule dropRule,Integer level){
		
		List<Goods> dropGoodsSet = new ArrayList<>();
		
		Goods generateGoods = null;
		
		//掉落规则
		int dropCount = dropRule.getDropCount() > 0 ? dropRule.getDropCount() : 1;
		for(int i = 0 ; i < dropCount ; i++){
			generateGoods = calDropRule(dropRule);
			if(null != generateGoods){
				dropGoodsSet.add(generateGoods);
			}
		}
		return dropGoodsSet;
		
	}
	/**
	 * 基本掉落规则
	 */
	private Goods calDropRule(DropRule dropRule) {
		String goodsId = null;
		int goodsCount = 1;
		
		//随机几率掉落算法
		boolean mingZhong = Lottery.roll(dropRule.getDropRate(), Lottery.TENTHOUSAND);
		if(mingZhong){
			//掉落计算
			if(DropIdType.GOODSID.equals(dropRule.getDropIdType())){
				//物品编号
				
				goodsId = dropRule.getDropId();
				
			}else{
				//组包编号
				Object[] goods = zuBaoConfigExportService.componentRoll(dropRule.getComponentDropId());
				if (goods != null) {					
					goodsId = (String) goods[0];
					goodsCount = (Integer)goods[1];
				}
			}
			if(null != goodsId){
				GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
				if(null != goodsConfig){
					//创建物品
					Goods goods = new Goods(IdFactory.getInstance().generateNonPersistentId(), goodsId, goodsCount, GameSystemTime.getSystemMillTime());
					return goods;
				}
			}
		}
		return null;
	}
	
	public void leaveTanBao(Long userRoleId){
		StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_LEAVE_STAGE, userRoleId);
	}

	/**
	 * 增加积分
	 * @param userRoleId
	 * @param score
	 */
	public void addScore(Long userRoleId,Integer score){
		if(score == null || score < 0){
			return;
		}
		TanBaoRoleVo roleVo = TanBaoManager.getManager().getRoleVo(userRoleId);
		if(roleVo == null){
			return;
		}
		roleVo.setScore(roleVo.getScore() + score);
		if(KuafuConfigPropUtil.isKuafuServer()){
			KuafuMsgSender.send2One(userRoleId, ClientCmdType.GET_TANBAO_GOLD, roleVo.getScore());
		}else{
			StageMsgSender.send2One(userRoleId, ClientCmdType.GET_TANBAO_GOLD, roleVo.getScore());
		}
	}
	
	/**
	 * 获取探宝经验
	 * @param userRoleId
	 */
	public void getTanBaoExp(Long userRoleId){
		TanBaoRoleVo roleVo = TanBaoManager.getManager().getRoleVo(userRoleId);
		if(roleVo == null){
			return;
		}
		if(KuafuConfigPropUtil.isKuafuServer()){
			KuafuMsgSender.send2One(userRoleId, ClientCmdType.GET_TANBAO_EXP, roleVo.getExp());
		}else{
			StageMsgSender.send2One(userRoleId, ClientCmdType.GET_TANBAO_EXP, roleVo.getExp());
		}
	}
	
	/**
	 * 获取前五名
	 */
	public void getTopFive(Long userRoleId){
		Object[] result = TanBaoManager.getManager().getRank(0, 5);
		if(result == null){
			return;
		}
		if(KuafuConfigPropUtil.isKuafuServer()){
			KuafuMsgSender.send2One(userRoleId, ClientCmdType.SEND_TANBAO_TOP_FIVE, result);
		}else{
			StageMsgSender.send2One(userRoleId, ClientCmdType.SEND_TANBAO_TOP_FIVE, result);
		}
	}
	
	/**
	 * 获取自己信息
	 */
	public void getSelfInfo(Long userRoleId){
		TanBaoRoleVo roleVo = TanBaoManager.getManager().getRoleVo(userRoleId);
		if(roleVo == null){
			return;
		}
		if(KuafuConfigPropUtil.isKuafuServer()){
			KuafuMsgSender.send2One(userRoleId, ClientCmdType.SELF_TANBAO_INFO, roleVo.getMsgData());
		}else{
			StageMsgSender.send2One(userRoleId, ClientCmdType.SELF_TANBAO_INFO, roleVo.getMsgData());
		}
	}
	
	/**
	 * 获取排行信息
	 */
	public void getRankInfo(Long userRoleId,Integer start,Integer count){
		Object[] rank = TanBaoManager.getManager().getRank(start, count);
		if(rank == null){
			return;
		}
		Object[] result = new Object[]{TanBaoManager.getManager().getSize(),rank,start};
		if(KuafuConfigPropUtil.isKuafuServer()){
			KuafuMsgSender.send2One(userRoleId, ClientCmdType.GET_RANK_INFO, result);
		}else{
			StageMsgSender.send2One(userRoleId, ClientCmdType.GET_RANK_INFO, result);
		}
	}
	
	public void addTanBaoExp(Long userRoleId,String stageId){
		if(!TanBaoManager.getManager().isStarting()){
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		if(stage.getStageType() != StageType.XIANGONG){
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		TanBaoRoleVo roleVo = TanBaoManager.getManager().getRoleVo(userRoleId);
		if(roleVo == null){
			return;
		}
		if(!role.getStateManager().isDead()){//死了不加经验
			int exp = tanBaoExpConfigExportService.getExp(role.getLevel());
			if(exp > 0){
				roleVo.setExp(roleVo.getExp() + exp);
				Object result = new Object[]{exp,null};
				if(KuafuConfigPropUtil.isKuafuServer()){
					KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.INNER_ADD_EXP, result);
					KuafuMsgSender.send2One(userRoleId, ClientCmdType.GET_TANBAO_EXP, roleVo.getExp());
				}else{
					StageMsgSender.send2Bus(userRoleId, InnerCmdType.INNER_ADD_EXP, result);
					StageMsgSender.send2One(userRoleId, ClientCmdType.GET_TANBAO_EXP, roleVo.getExp());
				}
			}
		}
		TanBaoPublicConfig tanBaoPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TANBAO);
		if(tanBaoPublicConfig != null){
			role.tanBaoExpSchedule(tanBaoPublicConfig.getAddExpTime());
		}
	}
	
	/**
	 * 复活点复活
	 * @param stageId
	 * @param roleId
	 */
	public void pointRevive(Long roleId,String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		
		IRole attacker = (IRole)stage.getElement(roleId, ElementType.ROLE);
		if(attacker == null){
			return;
		}
		//取消强制复活定时
		attacker.cancelBackFuhuoSchedule();
		//复活前加一个无敌状态
		attacker.getStateManager().add(new NoBeiAttack());
		
		if(attacker.getStateManager().remove(StateType.DEAD)){
			
			//按比率加血和蓝
			attacker.getFightAttribute().resetHp();
			
			IMsgWriter writer = BufferedMsgWriter.getInstance();
			attacker.getFightStatistic().flushChanges(writer);
			writer.flush();
			
			DiTuConfig config = diTuConfigExportService.loadDiTu(stage.getMapId());
			
			int[] birthXy = config.getRandomBirth();
			int x = birthXy[0];
			int y = birthXy[1];
			
			stage.teleportTo(attacker, x,y);
			StageMsgSender.send2Many(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.BEHAVIOR_TELEPORT, new Object[]{roleId,attacker.getPosition().getX(),attacker.getPosition().getY()});
			
			StageMsgSender.send2Many(stage.getSurroundRoleIds(attacker.getPosition()), ClientCmdType.TOWN_REVIVE, roleId);
			
		}
		//复活后取消无敌状态
		attacker.getStateManager().remove(StateType.NO_ATTACKED);
		
	}
}
