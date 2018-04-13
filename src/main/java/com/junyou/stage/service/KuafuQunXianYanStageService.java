package com.junyou.stage.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.kuafu_boss.constants.KuafubossConstants;
import com.junyou.bus.kuafu_qunxianyan.configure.QunXianYanJiFenConfig;
import com.junyou.bus.kuafu_qunxianyan.configure.QunXianYanJiFenConfigExportService;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.RoleState;
import com.junyou.bus.stagecontroll.StageUtil;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuQunXianYanPublicConfig;
import com.junyou.kuafu.manager.KuafuSessionManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.module.GameModType;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.configure.export.impl.ZiYuanConfig;
import com.junyou.stage.configure.export.impl.ZiYuanConfigExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.goods.Collect;
import com.junyou.stage.model.element.goods.CollectFacory;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.PublicFubenStage;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.model.stage.kuafuquanxianyan.KuaFuQunXianYanRank;
import com.junyou.stage.model.stage.kuafuquanxianyan.KuafuQunXianYanStage;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;
import com.kernel.gen.id.IdFactory;
import com.kernel.spring.container.DataContainer;

@Service
public class KuafuQunXianYanStageService {
	@Autowired
	private PublicFubenStageFactory publicFubenStageFactory;
	@Autowired
	private MapConfigExportService mapConfigExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private QunXianYanJiFenConfigExportService qunXianYanJiFenConfigExportService;
	@Autowired
	private ZiYuanConfigExportService ziYuanConfigExportService;
	
	public void kuafuBossSendRoleData(Long userRoleId,String name,Object roleData) {
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("no redis config");
			return;
		}
		KuafuQunXianYanPublicConfig publicConfig = getPublicConfig();
		String stageId = StageUtil.getStageId(publicConfig.getMapid(), 1);
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			synchronized (this) {
				stage = StageManager.getStage(stageId);
				if (stage == null) {
					stage = createKuafuQunXianYanStage(publicConfig, stageId);
					KuafuQunXianYanStage kStage = (KuafuQunXianYanStage) stage;
					kStage.startForceKickSchedule();
					kStage.schedulerRefreshRank();
				}
			}
		}
		if (stage == null) {
			ChuanQiLog.error("can not create or get stage in kuafuqunxianyan");
			return;
		}
		dataContainer.putData(GameConstants.COMPONENET_KUAFU_DATA,userRoleId.toString(), roleData);
		IRole role = null;
		try {
			role = RoleFactory.createKuaFu(userRoleId, null, dataContainer.getData(GameConstants.COMPONENET_KUAFU_DATA,userRoleId.toString()));
		
		} catch (Exception ex) {
			KuafuMsgSender.send2KuafuSource(userRoleId,InnerCmdType.KUAFUQUNXIANYAN_ENTER_FAIL,KuafubossConstants.ENTER_FAIL_REASON_1);

			BusMsgSender.send2BusInner(userRoleId,InnerCmdType.INNER_KUAFU_LEAVE, null);

			ChuanQiLog.error("error exit when create Kuafu role ", ex);
			return;
		}
		synchronized (stage) {
			KuafuQunXianYanStage kStage = (KuafuQunXianYanStage) stage;
			if (!kStage.isRegister(userRoleId)) {
				// 判断人家是否已经达到上限
				String num = redis.get(RedisKey
						.getKuafuQunXianYanStageRoleNumKey(ChuanQiConfigUtil.getServerId()));
				if (num != null) {
					if (Integer.parseInt(num) >= getPublicConfig().getMaxpople()) {
						KuafuMsgSender.send2KuafuSource(userRoleId,InnerCmdType.KUAFUQUNXIANYAN_ENTER_FAIL,KuafubossConstants.ENTER_FAIL_REASON_2);
						BusMsgSender.send2BusInner(userRoleId,InnerCmdType.INNER_KUAFU_LEAVE, null);
						ChuanQiLog.error("kuafu boss stage max people");
						return;
					}
				}
				//初始化角色积分数据
				KuaFuQunXianYanRank rank = new KuaFuQunXianYanRank();
				rank.setMingci(0);
				rank.setScore(0);
				rank.setUserRoleId(userRoleId);
				rank.setName(name);
				rank.setDeadCount(0);
				rank.setServerId(KuafuSessionManager.getServerId(userRoleId));
				kStage.initUserScore(rank);
				//初始化跨服redis角色积分数据
				String member = userRoleId.toString();
				Long isBaoMing = redis.zrevrank(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN, member);
				if(isBaoMing == null){//玩家存在排名，说明redis里面已经存在数据了,则不再需要再初始化了
					redis.zadd(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN, 0, member);
				}
			}
			publicRoleStateExportService.change2PublicOnline(userRoleId);
			RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, userRoleId.toString());
			if (null == roleState) {
				roleState = new RoleState(userRoleId);
				dataContainer.putData(GameModType.STAGE_CONTRAL,userRoleId.toString(), roleState);
			}
			DiTuConfig dituConfig = diTuConfigExportService.loadDiTu(publicConfig.getMapid());
			List<int[]> birthPointList = dituConfig.getBirthRandomPoints();
			if(birthPointList == null || birthPointList.size() <= 0){
				ChuanQiLog.error("qunxianyan dituConfig is null");
				return;
			}
			int[] birthPoint = birthPointList.get(0);
			Object[] applyEnterData = new Object[] { publicConfig.getMapid(),
					birthPoint[0], birthPoint[1], MapType.KUAFUQUNXIANYAN_FUBEN_MAP,
					null, null, null };
			KuafuMsgSender.send2KuafuSource(role.getId(),
					ClientCmdType.KUAFU_QUNXIANYAN_CANCEL_LOADING, null);
			// 传送前加一个无敌状态
			role.getStateManager().add(new NoBeiAttack());
			role.setChanging(true);
			//开启加经验定时
			kStage.startAddExpSchedule(userRoleId);
			
			StageMsgSender.send2StageControl(role.getId(),InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
			KuafuMsgSender.send2KuafuSource(role.getId(),InnerCmdType.KUAFUQUNXIANYAN_ENTER_XIAOHEIWU, null);
			// 往redis里写数据
			redis.incr(RedisKey.getKuafuQunXianYanStageRoleNumKey(ChuanQiConfigUtil.getServerId()));
			redis.set(RedisKey.getKuafuQunXianYanBindServerIdKey(userRoleId),
					ChuanQiConfigUtil.getServerId());
			ChuanQiLog.info("userRoleId={} enter kuafuboss", userRoleId);
		}
	}

	private PublicFubenStage createKuafuQunXianYanStage(KuafuQunXianYanPublicConfig publicConfig, String stageId) {
		MapConfig mapConfig = mapConfigExportService.load(publicConfig.getMapid());
		if (mapConfig == null) {
			ChuanQiLog.error("no map config mapid={}", publicConfig.getMapid());
			return null;
		}
		PublicFubenStage stage = publicFubenStageFactory.createKuafuQunXianYanStage(stageId, mapConfig);
		StageManager.addStageCopy(stage);
		return stage;
	}

	private KuafuQunXianYanPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.KUAFU_QUNXIANYAN);
	}

	public void addExpDingshi(String stageId, Long userRoleId, Long exp) {
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.KUAFUQUNXIANYAN) {
			return;
		}

		IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;// 角色不存在
		}
		KuafuQunXianYanStage kStage = (KuafuQunXianYanStage) stage;
		KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.INNER_ADD_EXP,
				new Object[] { exp, ClientCmdType.KUAFU_QUNXIANYAN_EXP_ADD });
		kStage.startAddExpSchedule(userRoleId);
	}

	public void kuafuBossExit(Long userRoleId) {
		int mapId = getPublicConfig().getMapid();
		String stageId = StageUtil.getStageId(mapId, 1);
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.KUAFUQUNXIANYAN) {
			return;
		}
		KuafuQunXianYanStage kStage = (KuafuQunXianYanStage) stage;
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role != null) {
			kStage.leave(role);
		} else {
			return;
		}
		KuafuMsgSender.send2KuafuSource(userRoleId,
				InnerCmdType.KUAFUQUNXIANYAN_LEAVE_FB, null);
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_KUAFU_LEAVE,
				null);
		ChuanQiLog.info("userRoleId={} exit kuafuqunxianyan stage", userRoleId);
	}

	public void kuafuBossRank(String stageId) {
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		if (stage.getStageType() != StageType.KUAFUQUNXIANYAN) {
			return;
		}
		KuafuQunXianYanStage kStage = (KuafuQunXianYanStage) stage;
		Object[] rank = kStage.getKuaFuLuanDouRank();
		KuafuMsgSender.send2Many(kStage.getRoleIds().toArray(), ClientCmdType.KUAFU_QUNXIANYAN_PAIHENG, rank);
		kStage.schedulerRefreshRank();
	}

	public void kuafubossForceKick(String stageId) {
		ChuanQiLog.error("qunxianyan forcekick start");
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		if (stage.getStageType() != StageType.KUAFUQUNXIANYAN) {
			return;
		}
		KuafuQunXianYanStage kStage = (KuafuQunXianYanStage) stage;
		Redis redis = GameServerContext.getRedis();
		List<Long> roleIds = kStage.getRoleIds();
		Map<Long, Integer> map = kStage.getStageRankMap();	
		for (Long e : roleIds) {
			String kuafuBossBindServerIdKey = RedisKey.getKuafuQunXianYanBindServerIdKey(e);
			redis.del(kuafuBossBindServerIdKey);
			//获取排名
			/*Long rank = redis.zrevrank(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN, e+"");
			if(rank == null){
				return;
			}*/
			int rank = map.get(e);
			String serverId = kStage.getUserServerId(e);
			if(serverId != null){
				KuafuMsgSender.send2KuafuSource(serverId, InnerCmdType.KUAFUQUNXIANYAN_JIESUAN_EMAIL, new Object[]{e,rank});
			}
			//KuafuMsgSender.send2One(e, ClientCmdType.KUAFU_QUNXIANYAN_JIESUAN, rank);
		}
		Object[] userRoleIds = stage.getAllRoleIds();
		for (Object id : userRoleIds) {
			IRole role = stage.getElement((Long) id, ElementType.ROLE);
			stage.leave(role);
			KuafuMsgSender.send2KuafuSource((Long) id,InnerCmdType.KUAFUQUNXIANYAN_LEAVE_FB, null);
			BusMsgSender.send2BusInner((Long) id,InnerCmdType.INNER_KUAFU_LEAVE, null);
		}
		kStage.stop();
		String serverId = ChuanQiConfigUtil.getServerId();
		redis.set(RedisKey.getKuafuQunXianYanStageRoleNumKey(serverId), "0");
		ChuanQiLog.info("force kick all role stageId={}", stageId);
		if (kStage.isCanRemove()) {
			StageManager.removeCopy(kStage);
		}
		ChuanQiLog.error("qunxianyan forcekick end");
	}

	public void kuafuBossFuhuo(Long userRoleId,String stageId){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}

		if (stage.getStageType() != StageType.KUAFUQUNXIANYAN) {
			return;
		}
		StageMsgSender.send2StageInner( userRoleId, stageId, InnerCmdType.KUAFUQUNXIANYAN_AUTO_FUHUO, null);
	}
	
	/**
	 * 增加积分
	 * @param stageId
	 * @param userRoleId
	 * @param jifen
	 */
	public void kuafuLuanDouAddJiFen(String stageId,Long userRoleId,String zyId){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}
		if (stage.getStageType() != StageType.KUAFUQUNXIANYAN) {
			return;
		}
		QunXianYanJiFenConfig jiFen = qunXianYanJiFenConfigExportService.getConfigByZiYuanId(zyId);
		if(jiFen == null){
			return;
		}
		KuafuQunXianYanStage kStage = (KuafuQunXianYanStage) stage;
		//增加房间排行榜积分
		KuaFuQunXianYanRank rank = kStage.getMyKuaFuLuanDouRank(userRoleId);
		int jifen = jiFen.getJifen();
		rank.setScore(rank.getScore() +jifen);
		KuafuMsgSender.send2One(rank.getUserRoleId(), ClientCmdType.KUAFU_QUNXIANYAN_USER_INFO, new Object[]{rank.getMingci(),rank.getName(),rank.getScore(),rank.getDeadCount()});
		//增加redis结算积分
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("no redis config");
			return;
		}
		String member = userRoleId.toString();
		Long isOn = redis.zrevrank(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN, member);
		if(isOn == null){//玩家存在排名，说明redis里面已经存在数据了,则直接加积分
			redis.zadd(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN, jifen, member);
		}else{
			redis.zincrby(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN, jifen, member);
		}
		
	}
	/**
	 * 死亡积分处理
	 * @param stageId
	 * @param userRoleId
	 * @param jifen
	 */
	public void deadJifen(String stageId,Long userRoleId,Long mbUserRoleId){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}
		if (stage.getStageType() != StageType.KUAFUQUNXIANYAN) {
			return;
		}
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("no redis config");
			return;
		}
		//获得死亡人的积分
		long mbJifen = redis.zscore(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN, mbUserRoleId.toString());
		if(mbJifen <= 0){
			return;
		}
		KuafuQunXianYanPublicConfig pConfig = getPublicConfig();
		int jifen = (int) (mbJifen * pConfig.getKillValue() / 100);
		if(jifen <= 0){
			jifen = 1;
		}else if(jifen > pConfig.getKillValeMax()){
			jifen = pConfig.getKillValeMax();
		}
		
		KuafuQunXianYanStage kStage = (KuafuQunXianYanStage) stage;
		
		//增加房间排行榜积分
		KuaFuQunXianYanRank rank = kStage.getMyKuaFuLuanDouRank(userRoleId);
		rank.setScore(rank.getScore() +jifen);
		//推送每个玩家自己的排行
		KuafuMsgSender.send2One(rank.getUserRoleId(), ClientCmdType.KUAFU_QUNXIANYAN_USER_INFO, new Object[]{rank.getMingci(),rank.getName(),rank.getScore(),rank.getDeadCount()});
	
		//死亡角色减少房间排行榜积分
		KuaFuQunXianYanRank mbRank = kStage.getMyKuaFuLuanDouRank(mbUserRoleId);
		mbRank.setScore(mbRank.getScore() - jifen);
		//推送每个玩家自己的排行
		KuafuMsgSender.send2One(mbRank.getUserRoleId(), ClientCmdType.KUAFU_QUNXIANYAN_USER_INFO, new Object[]{mbRank.getMingci(),mbRank.getName(),mbRank.getScore(),mbRank.getDeadCount()});
	
		//增加redis结算积分
		String member = userRoleId.toString();
		Long isOn = redis.zrevrank(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN, member);
		if(isOn == null){//玩家存在排名，说明redis里面已经存在数据了,则直接加积分
			redis.zadd(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN, jifen, member);
		}else{
			redis.zincrby(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN, jifen, member);
		}
		//减少redis积分
		Long mbIsOn = redis.zrevrank(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN,  mbUserRoleId.toString());
		if(mbIsOn != null){//玩家存在排名，说明redis里面已经存在数据了,则直接加积分
			redis.zincrby(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN, 0-jifen,  mbUserRoleId.toString());
		}
	}
	/**
	 * 刷资源
	 * @param stageId
	 * @param userRoleId
	 * @param jifen
	 */
	public void shuaZiYuan(String stageId,int zyId,int x,int y){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;// 场景不存在
		}
		if (stage.getStageType() != StageType.KUAFUQUNXIANYAN) {
			return;
		}
		
		ZiYuanConfig ziYuanConfig = ziYuanConfigExportService.loadById(zyId);
		if(ziYuanConfig == null){
			return;
		}
		if(ziYuanConfig.getDelaNumber() == 1){
			List<Integer[]> zuobiaoList = ziYuanConfig.getZuobiao();
			Integer[] zuoBiao = zuobiaoList.get((int)(Math.random()*zuobiaoList.size()));
			Collect taozi = CollectFacory.create(IdFactory.getInstance().generateNonPersistentId(),ziYuanConfig.getId().toString(), ziYuanConfig);
			stage.enter(taozi, zuoBiao[0], zuoBiao[1]);
			//公告
			KuafuQunXianYanPublicConfig pConfig = getPublicConfig();
			KuafuQunXianYanStage kStage = (KuafuQunXianYanStage) stage;
			BusMsgSender.send2Many(kStage.getAllRoleIds(),ClientCmdType.NOTIFY_CLIENT_ALERT6, new Object[]{pConfig.getSxCode()});
		}else{
			Collect taozi = CollectFacory.create(IdFactory.getInstance().generateNonPersistentId(),ziYuanConfig.getId().toString(), ziYuanConfig);
			stage.enter(taozi, x, y);
		}
	}
	
	/**
	 * 请求排行榜
	 * @param stageId
	 * @param userRoleId
	 * @param jifen
	 */
	public void getRank(String stageId,Long userRoleId){
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;//场景不存在
		}
		if (stage.getStageType() != StageType.KUAFUQUNXIANYAN) {
			return;
		}
		
		KuafuQunXianYanStage kStage = (KuafuQunXianYanStage) stage;
		BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_QUNXIANYAN_RANK, kStage.getKuaFuLuanDouRankWz());
	}
	
}
