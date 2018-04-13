package com.junyou.stage.campwar.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.CampRankEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfig;
import com.junyou.gameconfig.goods.configure.export.DingShiActiveConfigExportService;
import com.junyou.gameconfig.map.configure.export.MapConfig;
import com.junyou.gameconfig.map.configure.export.MapConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.CampPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.campwar.confiure.export.ZhenGongGaoConfig;
import com.junyou.stage.campwar.confiure.export.ZhenGongGaoConfigExportService;
import com.junyou.stage.campwar.confiure.export.ZhenYinZhanJiangLiConfig;
import com.junyou.stage.campwar.confiure.export.ZhenYinZhanJiangLiConfigExportService;
import com.junyou.stage.campwar.confiure.export.ZhenYinZhanJingYanConfigExportService;
import com.junyou.stage.campwar.entity.CampRank;
import com.junyou.stage.campwar.manage.CampRankComparato;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.IStageElement;
import com.junyou.stage.model.core.stage.StageType;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.stage.fuben.CampStage;
import com.junyou.stage.model.stage.fuben.PublicFubenStageFactory;
import com.junyou.stage.tunnel.StageMsgQueue;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.spring.container.DataContainer;

/**
 * 阵营战场景Service
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-4-8 下午4:59:42
 */
@Service
public class CampWarStageService {
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private ZhenYinZhanJingYanConfigExportService zhenYinZhanJingYanConfigExportService;
	@Autowired
	private DingShiActiveConfigExportService dingShiHuoDongBiaoConfigExportService;
	@Autowired
	private ZhenYinZhanJiangLiConfigExportService zhenYinZhanJiangLiConfigExportService;
	@Autowired
	private ZhenGongGaoConfigExportService zhenGongGaoConfigExportService;
	@Autowired
	private PublicFubenStageFactory publicFubenStageFactory;
	@Autowired
	private MapConfigExportService mapConfigExportService;

	private CampPublicConfig getCampPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_CAMP);
	}

	private RoleWrapper getRoleWrapper(Long userRoleId) {
		RoleWrapper role = null;
		if (publicRoleStateExportService.isPublicOnline(userRoleId)) {
			role = roleExportService.getLoginRole(userRoleId);
		} else {
			role = roleExportService.getUserRoleFromDb(userRoleId);
		}
		return role;
	}

	/**
	 * 获取玩家名字（不在线则拉取库里数据）
	 * 
	 * @param userRoleId
	 * @return
	 */
	private String getRoleName(Long userRoleId) {
		RoleWrapper role = getRoleWrapper(userRoleId);

		if (role == null) {
			return userRoleId + "";
		}
		return role.getName();
	}

	/**
	 * 胜方阵营业务处理
	 * 
	 * @param campStage
	 */
	private void winCampHandle(CampStage campStage) {
		if (campStage == null) {
			return;
		}
		Integer winCamp = campStage.getWinCamp();
		if (winCamp == null) {
			return;
		}

		// 获取排名奖励配置
		Map<Integer, ZhenYinZhanJiangLiConfig> jlMap = zhenYinZhanJiangLiConfigExportService
				.getRankConfig();
		if (jlMap == null || jlMap.size() <= 0) {
			return;
		}
		List<Long> rankedRoleIds = sendRankReward(campStage, jlMap);
		if (winCamp == -1) {
			// 平局
			sendLoseReward(campStage, rankedRoleIds, jlMap, campStage.getCamps());
		} else {
			sendWinReward(campStage, rankedRoleIds, jlMap, winCamp);
			sendLoseReward(campStage, rankedRoleIds, jlMap, new int[]{campStage.getLoseCamp(winCamp)});
		}
	}

	private List<Long> sendRankReward(CampStage campStage,Map<Integer, ZhenYinZhanJiangLiConfig> jlMap) {
		List<Long> rewardedRoleIdList = new ArrayList<Long>();
		// 所有阵营战积分信息
		List<CampRank> list = campStage.getCampRankList();
		if (list == null || list.size() <= 0) {
			return rewardedRoleIdList;
		}
		// 排序
		Collections.sort(list, new CampRankComparato());
		int losePj = zhenYinZhanJiangLiConfigExportService.getRankLosePjId();
		int winPj = zhenYinZhanJiangLiConfigExportService.getRankWinPjId();
		for(Integer e:jlMap.keySet()){
			if(e.intValue() !=losePj && e.intValue()!=winPj ){
				ZhenYinZhanJiangLiConfig config = jlMap.get(e);
				if(list.size() >= e){
					CampRank campRank = list.get(e-1);
					rewardedRoleIdList.add(campRank.getUserRoleId());
					sendSystemEamilForSingle(campRank.getUserRoleId(), config);
				}
			}
		}
		return rewardedRoleIdList;
	}

	private void sendWinReward(CampStage campStage, List<Long> rankRoleIds,
			Map<Integer, ZhenYinZhanJiangLiConfig> jlMap, Integer winCampId) {
		ZhenYinZhanJiangLiConfig pjConfig = jlMap
				.get(zhenYinZhanJiangLiConfigExportService.getRankWinPjId());
		if (pjConfig != null) {
			List<Long> roleIds = new ArrayList<Long>();
			List<Long> allCampRoleIds = campStage.getCampRoles(winCampId);
			for (Long e : allCampRoleIds) {
				if (!rankRoleIds.contains(e)) {
					roleIds.add(e);
				}
			}
			if(roleIds.size()>0){
				sendSystemEamilForAll(roleIds, pjConfig.getJlItem(),AppErrorCode.CAMP_EAMIL_CONTENT_PJ_WIN,AppErrorCode.CAMP_EAMIL_CONTENT_TITLE);
			}
		}
	}

	private void sendLoseReward(CampStage campStage, List<Long> rankRoleIds,
			Map<Integer, ZhenYinZhanJiangLiConfig> jlMap, int[] camps) {
		ZhenYinZhanJiangLiConfig pjConfig = jlMap
				.get(zhenYinZhanJiangLiConfigExportService.getRankLosePjId());
		if (pjConfig != null) {
			for (Integer id : camps) {
				List<Long> roleIds = new ArrayList<Long>();
				List<Long> allCampRoleIds = campStage.getCampRoles(id);
				for (Long e : allCampRoleIds) {
					if (!rankRoleIds.contains(e)) {
						roleIds.add(e);
					}
				}
				if(roleIds.size()>0){
					sendSystemEamilForAll(roleIds, pjConfig.getJlItem(),AppErrorCode.CAMP_EAMIL_CONTENT_PJ_LOSE,AppErrorCode.CAMP_EAMIL_CONTENT_TITLE);
				}
			}
		}
	}

	/**
	 * 发送胜方阵营玩家普奖
	 * 
	 * @param pjRoles
	 *            普奖玩家
	 */
	private void sendSystemEamilForAll(List<Long> pjRoles, String attachment,String code,String title) {
		try {
			String content = EmailUtil.getCodeEmail(code);
			title = EmailUtil.getCodeEmail(title);
			emailExportService.sendEmailToMany(pjRoles, title,content,GameConstants.EMAIL_TYPE_SINGLE, attachment);
		} catch (Exception e) {
			ChuanQiLog.error("CampWar sendSystemEamilForAll error");
		}
	}

	/**
	 * 发送玩家排名奖励
	 * 
	 * @param userRoleId
	 */
	private void sendSystemEamilForSingle(Long userRoleId,ZhenYinZhanJiangLiConfig config) {
		try {
			String title = EmailUtil.getCodeEmail(AppErrorCode.CAMP_EAMIL_CONTENT_TITLE);
			String content = EmailUtil.getCodeEmail(AppErrorCode.CAMP_EAMIL_CONTENT, config.getPaiming().toString());
			emailExportService.sendEmailToOne(userRoleId, title,content,GameConstants.EMAIL_TYPE_SINGLE, config.getJlItem());
		} catch (Exception e) {
			ChuanQiLog.error("CampWar sendSystemEamilForSingle error");
		}
	}

	/**
	 * 阵营战开始
	 * 
	 * @param hdId
	 */
	public void campWarStart(String stageId, Integer hdId) {
		DingShiActiveConfig config = dingShiHuoDongBiaoConfigExportService.getConfig(hdId);
		if (config == null) {
			return;
		}

		ChuanQiLog.info("阵营战开始");
		IStage stage = StageManager.getStage(stageId);

		if (stage == null) {
			MapConfig mapConfig = mapConfigExportService.load(config.getMapId());
			stage = publicFubenStageFactory.create(stageId, 1, mapConfig);
			StageManager.addStageCopy(stage);
			ChuanQiLog.info("阵营战场景创建成功");
		}

		CampStage campStage = (CampStage) stage;
		// 过期检测
		campStage.startEndRankSchedule(config.getCalcEndSecondTime());
		// 定时加经验检测

		// 标记阵营战场开始
		dataContainer.putData(GameConstants.COMPONENT_CAMP_WAR,GameConstants.COMPONENT_CAMP_WAR, hdId);
	}

	/**
	 * 阵营战结束 强制将玩家剔出阵营战副本，并且清除所有此次阵营战的数据
	 * 
	 * @param stageId
	 */
	public void campWarEnd(StageMsgQueue stageMsgQueue, String stageId) {
		Integer hdId = dataContainer.getData(GameConstants.COMPONENT_CAMP_WAR,GameConstants.COMPONENT_CAMP_WAR);
		if (hdId == null) {
			return;
		}
		dataContainer.removeData(GameConstants.COMPONENT_CAMP_WAR,GameConstants.COMPONENT_CAMP_WAR);

		IStage stage = StageManager.getStage(stageId);
		if (stage == null || !StageType.CAMP.equals(stage.getStageType())) {
			return;
		}

		CampStage campStage = (CampStage) stage;
		campStage.clear();// 清除积分
		// StageManager.removeCopy(stage);//摧毁副本

		// 获取场景中所有的玩家
		Map<Long, IStageElement> roleMap = campStage.getBaseStageRoles();
		if (roleMap == null || roleMap.size() <= 0) {
			return;
		}

		for (Map.Entry<Long, IStageElement> entry : roleMap.entrySet()) {
			stageMsgQueue.addStageControllMsg(entry.getKey(),InnerCmdType.S_APPLY_LEAVE_STAGE, null);

			// IRole element = (IRole)stage.getElement(entry.getKey(),
			// ElementType.ROLE);
			// if(element != null)campStage.leave(element);
		}
		ChuanQiLog.info("阵营战结束");
	}

	/**
	 * 阵营战定时加经验
	 * 
	 * @param stageId
	 */
	public void campAddExp(Long userRoleId, String stageId) {
		// 活动结束
		Integer hdId = dataContainer.getData(GameConstants.COMPONENT_CAMP_WAR,
				GameConstants.COMPONENT_CAMP_WAR);
		if (hdId == null) {
			return;
		}

		IStage istage = StageManager.getStage(stageId);
		if (istage == null || !StageType.CAMP.equals(istage.getStageType())) {
			return;
		}

		CampStage stage = (CampStage) istage;
		if (!stage.isOpen()) {
			return;
		}

		// 获取场景中所有的玩家
		IStageElement element = stage.getElement(userRoleId, ElementType.ROLE);
		IRole role = (IRole) element;

		Integer level = role.getLevel();
		if (level == null) {
			return;
		}

		Long addExp = zhenYinZhanJingYanConfigExportService.getCampExpByLevel(level);
		if (addExp == null || addExp.intValue() <= 0) {
			return;
		}
		StageMsgSender.send2Bus(userRoleId, InnerCmdType.INNER_ADD_EXP,new Object[] { addExp, ClientCmdType.NOTICE_EXP });
		role.startCampAddExpSchedule(getCampPublicConfig().getTime());
	}

	/**
	 * 阵营战死亡
	 * 
	 * @param stageId
	 * @param userRoleId
	 *            攻击者
	 */
	public void campDead(String stageId, Long userRoleId, Integer constants,Long deadRoleId) {
		// 活动结束
		Integer hdId = dataContainer.getData(GameConstants.COMPONENT_CAMP_WAR,GameConstants.COMPONENT_CAMP_WAR);
		if (hdId == null) {
			return;
		}

		IStage stage = StageManager.getStage(stageId);
		if (stage == null || constants == null|| !StageType.CAMP.equals(stage.getStageType())) {
			return;
		}

		IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}

		CampPublicConfig campPublicConfig = getCampPublicConfig();
		if (campPublicConfig == null) {
			return;
		}
		CampStage campStage = (CampStage) stage;
		int addJf = 0;
		Integer winCamp = null;
		switch (constants.intValue()) {
		case GameConstants.ROLE_DEAD:// 玩家死亡
			addJf = campPublicConfig.getJifen1();
			campStage.clearRoleBatter(deadRoleId);
			campStage.addRoleBatter(userRoleId);
			Integer batter = campStage.getRoleBatter(userRoleId);
			noticeBatter(userRoleId, batter);
			break;
		case GameConstants.KILL_MONSTER:// 单次雕像
			addJf = campPublicConfig.getJifen2();
			break;
		case GameConstants.MONSTER_DEAD:// 雕像死亡
			addJf = campPublicConfig.getJifen3();
			winCamp = role.getZyCamp();
			break;

		default:
			break;
		}

		if (addJf <= 0) {
			return;
		}

		// 添加积分
		campStage.addRoleJifen(userRoleId, addJf, role.getName());

		// 摧毁雕像推送
		Map<Long, IStageElement> roleMap = campStage.getBaseStageRoles();
		if (roleMap != null && roleMap.size() > 0 && winCamp != null) {
			Object[] roleIds = new Object[roleMap.size()];
			int i = 0;

			for (Long roleId : roleMap.keySet()) {
				roleIds[i++] = roleId;
			}

			StageMsgSender.send2Many(roleIds, ClientCmdType.CMAP_MONSTER_DEAD,winCamp);
		}
	}

	private void noticeBatter(Long userRoleId, int batter) {
		ZhenGongGaoConfig config = zhenGongGaoConfigExportService
				.getConfig(batter);
		if (config != null) {
			UserRole role = roleExportService.getUserRole(userRoleId);
			BusMsgSender.send2All(
					ClientCmdType.NOTIFY_CLIENT_ALERT4,
					new Object[] {
							config.getGonggao(),
							new Object[] {
									new Object[] { 2, userRoleId,
											role.getName() }, batter } });
		}
	}

	/**
	 * 获取阵营战排名信息
	 * 
	 * @param stageId
	 * @param userRoleId
	 * @return
	 */
	public Object[] getCampRank(String stageId, Long userRoleId) {
		// 活动结束
		Integer hdId = dataContainer.getData(GameConstants.COMPONENT_CAMP_WAR,
				GameConstants.COMPONENT_CAMP_WAR);
		if (hdId == null) {
			return null;
		}

		IStage stage = StageManager.getStage(stageId);
		if (stage == null || !StageType.CAMP.equals(stage.getStageType())) {
			return null;
		}

		IRole role = (IRole) stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return null;
		}

		// 获取排名数据
		CampStage campStage = (CampStage) stage;
		Map<Integer, List<CampRank>> map = campStage.getRankMap();
		if (map == null || map.size() <= 0) {
			return null;
		}

		/**
		 * 0:Array[0:int(我的积分),1:int(我在所属阵营内排名)]没有为null 1:Array [
		 * 0:Array(阵营数据)[0:int(所属阵营),1:int(阵营总积分),2:Array(排名数据)[前3名名字...]]...
		 * ]没有为null
		 */
		Integer[] roleData = null;
		List<Object[]> rankData = new ArrayList<>();
		// 处理排行榜数据
		Integer camp = campStage.getRoleCampById(userRoleId);
		for (Map.Entry<Integer, List<CampRank>> entry : map.entrySet()) {

			// 阵营排行数据（推送给客户端的数据）
			int size = entry.getValue().size();
			String[] campData = new String[size > GameConstants.RANK_SIZE ? GameConstants.RANK_SIZE
					: size];
			boolean isRoleData = camp.equals(entry.getKey());// 是否是当前玩家数据

			List<CampRank> ranks = new ArrayList<>();
			int index = 1;
			List<CampRank> list = entry.getValue();
			// 排序
			Collections.sort(list, new CampRankComparato());
			for (CampRank campRank : entry.getValue()) {

				// 前3名排名数据
				if (index <= GameConstants.RANK_SIZE) {
					campData[index - 1] = getRoleName(campRank.getUserRoleId());
				}

				campRank.setOrderNum(index++);
				ranks.add(campRank);

				// 玩家所属排行积分数据
				if (isRoleData && campRank.getUserRoleId().equals(userRoleId)) {
					roleData = campRank.getOutData();
				}
			}

			// 重新设置管理容器
			campStage.setRankList(entry.getKey(), ranks);

			Integer jifens = campStage.getCampJifens(entry.getKey());// 阵营总积分
			rankData.add(new Object[] { entry.getKey(), jifens, campData });
		}

		return new Object[] { roleData,
				rankData.size() <= 0 ? null : rankData.toArray() };
	}

	/**
	 * 阵营战结束，统计排名，发放奖励
	 * 
	 * @param stageId
	 * @param userRoleId
	 */
	public void campRank(String stageId) {
		// 活动结束
		Integer hdId = dataContainer.getData(GameConstants.COMPONENT_CAMP_WAR,
				GameConstants.COMPONENT_CAMP_WAR);
		if (hdId == null) {
			return;
		}

		IStage stage = StageManager.getStage(stageId);
		if (stage == null || !StageType.CAMP.equals(stage.getStageType())) {
			return;
		}

		// 获取排行数据
		CampStage campStage = (CampStage) stage;
		campStage.stop();
		campStage.startEndLevelSchedule();// 活动结束一分钟后 强制踢出

		// 拉取获胜方阵营并发送奖励
		winCampHandle(campStage);

		CampPublicConfig campPublicConfig = getCampPublicConfig();
		Integer paiming = 10;// 拉取总排名前多少
		if (campPublicConfig != null) {
			paiming = campPublicConfig.getPaiming();
		}

		// 获取场景中人数（推送玩家排名数据）
		Map<Long, IStageElement> roleMap = campStage.getBaseStageRoles();
		if (roleMap == null || roleMap.size() <= 0) {
			return;
		}

		// 所有阵营战积分信息
		List<CampRank> list = campStage.getCampRankList();
		if (list == null || list.size() <= 0) {
			return;
		}

		// 排序
		Collections.sort(list, new CampRankComparato());

		int index = 1;// 排名
		List<Object[]> ranks = new ArrayList<>();
		Map<Long, Object[]> roleData = new HashMap<>();
		List<Long> userRoleIds = new ArrayList<>();
		for (CampRank campRank : list) {

			// 前10的排名名次
			if (index <= paiming) {
				/**
				 * 0 String 角色名字 1 int 角色职业（123,战法道） 2 int 所属阵营 3 int 个人积分
				 */
				RoleWrapper role = getRoleWrapper(campRank.getUserRoleId());
				if (role != null) {
					ranks.add(new Object[] { role.getName(),
							role.getConfigId(), campRank.getCamp(),
							campRank.getJifen() });
				}
			}

			// 玩家排名信息
			if (roleMap.containsKey(campRank.getUserRoleId())) {
				// 0:Array[0:int(我的积分),1:int(我活动总排名)]没有为null
				roleData.put(campRank.getUserRoleId(),
						new Object[] { campRank.getJifen(), index });
				userRoleIds.add(campRank.getUserRoleId());
			}

			campRank.setOrderNum(index++);
			campRank.setUpdateTime(GameSystemTime.getSystemMillTime());
		}

		// 通知客户端排名信息
		StageMsgQueue stageMsgQueue = new StageMsgQueue();
		for (Map.Entry<Long, IStageElement> entry : roleMap.entrySet()) {
			Object[] data = new Object[] { roleData.get(entry.getKey()),
					ranks.size() <= 0 ? null : ranks.toArray() };
			stageMsgQueue.addMsg(entry.getKey(), ClientCmdType.GET_CAMP_RANK,
					data);
		}
		stageMsgQueue.flush();
		// lxn 打印排行日志
		GamePublishEvent.publishEvent(new CampRankEvent(list));
	}
}
