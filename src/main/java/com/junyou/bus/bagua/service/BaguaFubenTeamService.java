package com.junyou.bus.bagua.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bagua.configure.export.BaguaZhenMenExportService;
import com.junyou.bus.bagua.configure.export.DuoRenTongYongBiaoConfig;
import com.junyou.bus.bagua.configure.export.DuoRenTongYongBiaoExportService;
import com.junyou.bus.bagua.constants.BaguaConstant;
import com.junyou.bus.bagua.dao.RoleBaguaDao;
import com.junyou.bus.bagua.entity.RoleBagua;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.fuben.entity.BaguaFubenTeam;
import com.junyou.bus.fuben.entity.BaguaFubenTeamManage;
import com.junyou.bus.fuben.entity.BaguaFubenTeamMember;
import com.junyou.bus.fuben.entity.BaguaZhenmenInfo;
import com.junyou.bus.fuben.utils.KuafuBaguaDataUtil;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.maigu.entity.RoleMaigu;
import com.junyou.bus.maigu.service.export.MaiguExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.RoleState;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.event.BaguaFubenRewardLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.BaguaPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafu.manager.KuafuServerInfoManager;
import com.junyou.kuafu.manager.KuafuServerManager;
import com.junyou.kuafu.manager.KuafuSessionManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.kuafuio.util.KuafuOnlineUtil;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.module.GameModType;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.KuafuConfigPropUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.number.LongUtils;
import com.kernel.cache.redis.Redis;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;
import com.kernel.spring.container.DataContainer;

@Service
public class BaguaFubenTeamService {
	@Autowired
	private RoleBaguaDao roleBaguaDao;

	@Autowired
	private BaguaZhenMenExportService baguaZhenMenExportService;
	@Autowired
	private DuoRenTongYongBiaoExportService duoRenTongYongBiaoExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private BusScheduleExportService busScheduleExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private DataContainer dataContainer;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private MaiguExportService maiguExportService;

	public List<RoleBagua> initRoleBagua(Long userRoleId) {
		return roleBaguaDao.initRoleBagua(userRoleId);
	}

	/**
	 * 创建成员
	 * 
	 * @param roleData
	 * @return
	 */
	private BaguaFubenTeamMember createTeamMember(Object[] roleData) {
		BaguaFubenTeamMember teamMember = new BaguaFubenTeamMember();
		teamMember.setRoleId(LongUtils.obj2long(roleData[0]));
		teamMember.setLevel((Integer) roleData[4]);
		teamMember.setMemberName((String) roleData[1]);
		teamMember.setStrength(LongUtils.obj2long(roleData[3]));
		teamMember.setConfigId((Integer) roleData[5]);
		teamMember.setServerId((String) roleData[2]);
		return teamMember;
	}
	public Object[] quickJoin(Long userRoleId,Integer fubenId){
		// 是否已经在副本中
		if(stageControllExportService.inFuben(userRoleId)){
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
		}
		// 所属副本ID是否存在
		DuoRenTongYongBiaoConfig dyConfig = duoRenTongYongBiaoExportService
				.loadByOrder(BaguaConstant.BUAGUA_FUBEN, fubenId);
		if (dyConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		RoleBagua baguaInfo = getRoleBagua(userRoleId, fubenId);
		if(baguaInfo!=null){
			if(baguaInfo.getTimes() >= dyConfig.getCount()){
				return AppErrorCode.FUBEN_NO_COUNT;
			}
		}
		String stageId = stageControllExportService.getCurStageId(userRoleId);
		if(stageId == null){
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		Redis redis = GameServerContext.getRedis();
		if(redis == null){
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}
		List<String> teamids = redis.lrange(KuafuBaguaDataUtil.getRedisTeamListKey(fubenId), 0, -1);
		if(teamids == null || teamids.isEmpty()){
			return null;
		}
		Integer targetTeamId= null;
		Map<String,String> targetTeam = null;
		for(String teamId : teamids){
			Map<String,String> team = redis.hgetAll(KuafuBaguaDataUtil.getRedisTeamIdKey(teamId));
			String countStr = team.get(GameConstants.REDIS_BAGUA_FUBEN_COUNT_KEY);
			DuoRenTongYongBiaoConfig config = duoRenTongYongBiaoExportService
					.loadByOrder(BaguaConstant.BUAGUA_FUBEN,
							fubenId);
			if(Integer.parseInt(countStr)==config.getTuijian()){
				continue;
			}
			long zplus = role.getFightAttribute().getZhanLi();
			String zplusStr = team.get(GameConstants.REDIS_BAGUA_FUBEN_ZPLUS_KEY);
			if(zplus<Long.valueOf(zplusStr)){
				continue;
			}
			targetTeamId=Integer.parseInt(teamId);
			targetTeam = team;
			break;
		}
		
		if (targetTeamId == null) {
			return AppErrorCode.BAGUA_FUBEN_NO_AVAILABLE_TEAM;
		}
		//标记当前为副本状态
		KuafuServerInfo kuafuServerInfo = KuafuServerInfoManager.getInstance().getKuafuServerInfo(targetTeam.get(GameConstants.REDIS_BAGUA_FUBEN_SERVERID_KEY), redis);
		if(kuafuServerInfo==null){
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}
		stageControllExportService.changeFuben(userRoleId, true);
		KuafuRoleServerManager.getInstance().bindServer(userRoleId, kuafuServerInfo);
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,role.getId(), InnerCmdType.BAGUA_FUBEN_APPLY_ENTER_TEAM, new Object[]{targetTeamId,KuafuBaguaDataUtil.getRoleData(role),ChuanQiConfigUtil.getServerId()});
		return null;
	}

	/**
	 * 创建八卦副本队伍
	 * 
	 * @param teamLeader
	 * @return
	 */
	public Object[] createTeam(Long userRoleId,
			Integer fubenId, Long strength, Boolean isAuto) {
		
		RoleBagua fuben = getRoleBagua(userRoleId, fubenId);
		// 判断副本完成状态
		if (fuben != null
				&& fuben.getStatus() == GameConstants.BAGUA_FUBEN_FINISH_STATUS) {
			return AppErrorCode.FUBEN_NO_COUNT;
		}
		
		// 所属副本ID是否存在
		DuoRenTongYongBiaoConfig config = duoRenTongYongBiaoExportService
				.loadByOrder(BaguaConstant.BUAGUA_FUBEN, fubenId);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		RoleBagua baguaInfo = getRoleBagua(userRoleId, fubenId);
		if(baguaInfo!=null){
			if(baguaInfo.getTimes() >= config.getCount()){
				return AppErrorCode.FUBEN_NO_COUNT;
			}
		}
		// 是否已经在副本中
		if (stageControllExportService.inFuben(userRoleId)) {
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
		}

		String stageId = stageControllExportService.getCurStageId(userRoleId);
		if (stageId == null) {
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}

		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		// 玩家等级是否满足
		if (role.getLevel() < config.getLevel()) {
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}

		if (!KuafuConfigUtil.isKuafuAvailable()) {
			return AppErrorCode.KUAFU_NO_CONNECTION;// 跨服连接失败
		}
		// 标记当前为副本状态
		KuafuServerInfo kuafuServerInfo = KuafuServerManager.getInstance()
				.getMostIdleKuafuServer(0);
		if (kuafuServerInfo == null) {
			return AppErrorCode.KUAFU_NO_CONNECTION;// 跨服连接失败
		}
		stageControllExportService.changeFuben(userRoleId, true);
		KuafuRoleServerManager.getInstance().bindServer(userRoleId,
				kuafuServerInfo);
		KuafuMsgSender.send2KuafuServer(
				GameConstants.DEFAULT_ROLE_ID,
				role.getId(),
				InnerCmdType.BAGUA_CREATE_TEAM_KF,
				new Object[] { fubenId, strength, isAuto,
						KuafuBaguaDataUtil.getRoleData(role),
						ChuanQiConfigUtil.getServerId() });
		return null;
	}

	/**
	 * 加入八卦副本队伍
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] joinTeam(Long userRoleId, Integer teamId) {
		// 是否已经在副本中
		if (stageControllExportService.inFuben(userRoleId)) {
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
		}
		String stageId = stageControllExportService.getCurStageId(userRoleId);
		if (stageId == null) {
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}
		Map<String, String> team = redis.hgetAll(KuafuBaguaDataUtil
				.getRedisTeamIdKey(teamId.toString()));
		if (team == null || team.isEmpty()) {
			return AppErrorCode.BAGUA_TEAM_NOT_EXISTS;
		}
		String fubenIdStr = team.get(GameConstants.REDIS_BAGUA_FUBEN_FUBENID_KEY);
		if(fubenIdStr == null ){
			return AppErrorCode.CONFIG_ERROR;
		}
		int fubenId = Integer.parseInt(fubenIdStr);
		RoleBagua fuben = getRoleBagua(userRoleId, fubenId);
		// 判断副本完成状态
		if (fuben != null
				&& fuben.getStatus() == GameConstants.BAGUA_FUBEN_FINISH_STATUS) {
			return AppErrorCode.FUBEN_NO_COUNT;
		}
		// 所属副本ID是否存在
		DuoRenTongYongBiaoConfig config = duoRenTongYongBiaoExportService
				.loadByOrder(BaguaConstant.BUAGUA_FUBEN, fubenId);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		RoleBagua baguaInfo = getRoleBagua(userRoleId, fubenId);
		if(baguaInfo!=null){
			if(baguaInfo.getTimes() >= config.getCount()){
				return AppErrorCode.FUBEN_NO_COUNT;
			}
		}
		// 标记当前为副本状态
		KuafuServerInfo kuafuServerInfo = KuafuServerInfoManager.getInstance()
				.getKuafuServerInfo(
						team.get(GameConstants.REDIS_BAGUA_FUBEN_SERVERID_KEY),
						redis);
		if (kuafuServerInfo == null) {
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}
		stageControllExportService.changeFuben(userRoleId, true);
		KuafuRoleServerManager.getInstance().bindServer(userRoleId,
				kuafuServerInfo);
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,
				role.getId(), InnerCmdType.BAGUA_FUBEN_APPLY_ENTER_TEAM,
				new Object[] { teamId, KuafuBaguaDataUtil.getRoleData(role),
						ChuanQiConfigUtil.getServerId() });
		return null;
	}

	/**
	 * 退出队伍
	 * 
	 * @param userRoleId
	 * @return
	 */
	public void leaveTeam(Long userRoleId) {
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,
				userRoleId, InnerCmdType.BAGUA_EXIT_FUBEN, new Object[] {
						userRoleId, ChuanQiConfigUtil.getServerId() });
	}

	/**
	 * 踢出八卦副本队伍
	 * 
	 * @param userRoleId
	 * @param targetRoleId
	 * @return
	 */
	public void kick(Long userRoleId, Long targetRoleId) {
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,
				userRoleId, InnerCmdType.BAGUA_KICK, new Object[] {
						userRoleId, ChuanQiConfigUtil.getServerId(),
						targetRoleId });
	}

	// /**
	// * 快速加入(客户端做)
	// * @param userRoleId
	// * @param fubenId
	// * @return
	// */
	// public Object[] quicklyAdd(Long userRoleId,Integer fubenId){
	// String stageId = stageControllExportService.getCurStageId(userRoleId);
	// if(stageId == null){
	// return AppErrorCode.STAGE_IS_NOT_EXIST;
	// }
	// IStage stage = StageManager.getStage(stageId);
	// if(stage == null){
	// return AppErrorCode.STAGE_IS_NOT_EXIST;
	// }
	//
	// Integer teamId = MoreFubenTeamManage.getTeamIdMap().get(userRoleId);
	// if(teamId != null){
	// return AppErrorCode.MORE_TEAM_EXISTS;
	// }
	//
	// HuanJingFubenConfig config = moreFubenConfigService.loadById(fubenId);
	// if(config == null){
	// return AppErrorCode.CONFIG_ERROR;
	// }
	//
	// IRole role = stage.getElement(userRoleId, ElementType.ROLE);
	// // 玩家等级是否满足
	// if(role.getLevel() < config.getLevel()){
	// return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
	// }
	// List<MoreFubenTeam> teams =
	// MoreFubenTeamManage.getTeamFubenMap().get(teamId);
	// List<MoreFubenTeam> finalTeams = null;
	// if(teams != null && !teams.isEmpty()){
	// finalTeams = new ArrayList<>();
	// for(MoreFubenTeam team : teams){
	// // 密码暂时不做处理
	// // if(team.getPwd() > 0){
	// // continue;
	// // }
	// // 战斗力
	// if(team.getStrength() > 0 && role.getFightAttribute().getZhanLi() <
	// team.getStrength()){
	// continue;
	// }
	// // 成员是否满员
	// if(team.getMembers().size() >= config.getTuijian()){
	// continue;
	// }
	// finalTeams.add(team);
	// }
	// }
	// if(finalTeams != null && !finalTeams.isEmpty()){
	// int index = Lottery.roll(0, finalTeams.size());
	// // 最终加入的多人副本队伍
	// MoreFubenTeam item = finalTeams.get(index);
	// MoreFubenTeamMember teamMember = new MoreFubenTeamMember();
	// teamMember.setRoleId(userRoleId);
	// teamMember.setMemberName(role.getName());
	// teamMember.setTeam(item);
	// item.addMembers(teamMember,config.getTuijian());
	// MoreFubenTeamManage.addTeamIdMap(userRoleId, item.getTeamId());
	// return new Object[]{item.getTeamId()};
	// }
	// return AppErrorCode.MORE_TEAM_JOIN_ERROR;
	// }

	/**
	 * 队员准备
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] changePrepareStatusKf(Long userRoleId) {
		// 队伍是否存在
		Integer teamId = BaguaFubenTeamManage.getTeamId(userRoleId);
		if (teamId == null) {
			return AppErrorCode.BAGUA_TEAM_NOT_EXISTS;
		}

		BaguaFubenTeam team = BaguaFubenTeamManage.getTeamMap().get(teamId);
		if (team == null || team.getLeader() == null) {
			// 队伍不存在
			BaguaFubenTeamManage.removeTeam(userRoleId, true);
			return AppErrorCode.BAGUA_TEAM_NOT_EXISTS;
		}
		// 是否队长
		if (userRoleId.equals(team.getLeader().getRoleId())) {
			return AppErrorCode.BAGUA_TEAM_NO_NEED;
		}

		// 多人副本是否存在
		DuoRenTongYongBiaoConfig config = duoRenTongYongBiaoExportService
				.loadByOrder(BaguaConstant.BUAGUA_FUBEN,
						team.getBelongFubenId());
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}

		BaguaFubenTeamMember mftm = team.getMember(userRoleId);
		if (mftm.isPrepare()) {
			mftm.setPrepare(false);
		} else {
			mftm.setPrepare(true);
		}

		// 如果设置自动，满员自动进入副本场景
		if (team.isAuto() && team.getMembers().size() == config.getTuijian()) {
			boolean isAllPrepare = true;
			Long leaderRoleId = team.getLeader().getRoleId();
			for (BaguaFubenTeamMember member : team.getMembers()) {
				if (!leaderRoleId.equals(member.getRoleId())
						&& !member.isPrepare()) {
					isAllPrepare = false;
					break;
				}
			}
			if (isAllPrepare) {
				// 全部准备好
				// 副本进入互斥
				// for(Long tmpRoleId : team.getRoleIdList()){
				// if(stageControllExportService.inFuben(tmpRoleId)){
				// return AppErrorCode.FUBEN_IS_IN_FUBEN;
				// }
				// }
				startDjs(team);
				return null;
			}
		}
		Object[] obj = new Object[] { teamId, userRoleId, mftm.isPrepare() };
		for (BaguaFubenTeamMember member : team.getMembers()) {
			Object[] msg = new Object[] { ClientCmdType.BAGUA_CHANGE_PREPARE_STATUS,
					obj, member.getRoleId() };
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(),
					InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
		return null;
	}

	/**
	 * 队长开始多人副本操作
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] startTeamKf(Long userRoleId) {
		// 副本队伍是否存在
		Integer teamId = BaguaFubenTeamManage.getTeamId(userRoleId);
		if (teamId == null) {
			return AppErrorCode.BAGUA_TEAM_NOT_EXISTS;
		}

		BaguaFubenTeam team = BaguaFubenTeamManage.getTeamMap().get(teamId);
		if (team == null) {
			return AppErrorCode.BAGUA_TEAM_NOT_EXISTS;
		}

		// 是否是队伍领导
		if (!userRoleId.equals(team.getLeader().getRoleId())) {
			return AppErrorCode.TEAM_IS_NOT_LEADER;
		}

		// 是否有玩家未准备好
		for (BaguaFubenTeamMember member : team.getMembers()) {
			if (!userRoleId.equals(member.getRoleId()) && !member.isPrepare()) {
				return AppErrorCode.BAGUA_TEAM_NOT_PREPARED;
			}
		}

		// 副本进入互斥
		// for(Long tmpRoleId : team.getRoleIdList()){
		// if(stageControllExportService.inFuben(tmpRoleId)){
		// return AppErrorCode.FUBEN_IS_IN_FUBEN;
		// }
		// }

		// 预开启多人副本定时
		startDjs(team);

		return null;
	}

	/**
	 * 5s副本倒计时后正式进入多人副本
	 * 
	 * @param teamId
	 * @return
	 */
	private void startDjs(BaguaFubenTeam team) {
		// 已经进入倒计时，其他玩家不能进入多人副本队伍
		team.setDjs(true);
		BusTokenRunable runable = new BusTokenRunable(
				GameConstants.DEFAULT_ROLE_ID,
				InnerCmdType.BAGUA_AFTER_DJS, new Object[]{team.getTeamId(),1});
		busScheduleExportService.schedule(team.getTeamId() + "",
				GameConstants.BAGUA_DJS, runable,
				GameConstants.BAGUA_FUBEN_DJS_TIME + 500, TimeUnit.MILLISECONDS);
		Object[] obj = new Object[] { AppErrorCode.SUCCESS,
				GameConstants.BAGUA_FUBEN_DJS_TIME };
		for (BaguaFubenTeamMember member : team.getMembers()) {
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(),
					InnerCmdType.BAGUA_FUBEN_ASK_ROLE_DATA, member.getRoleId());
			Object[] msg = new Object[] { ClientCmdType.BAGUA_START_TEAM,
					obj, member.getRoleId() };
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(),
					InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
	}

	/**
	 * 获取八卦副本业务数据
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] getMyInfo(Long userRoleId) {
		List<Object> objs = new ArrayList<>();
		long now = GameSystemTime.getSystemMillTime();
		List<RoleBagua> list = roleBaguaDao.cacheLoadAll(userRoleId);
		if (list != null && !list.isEmpty()) {
			for (RoleBagua fuben : list) {
				if (!DatetimeUtil.dayIsToday(fuben.getUpdateTime())) {
					fuben.setTimes(0);
					fuben.setUpdateTime(now);
					roleBaguaDao.cacheUpdate(fuben, userRoleId);
				}
				if(fuben.getTimes()>0){
					objs.add(new Object[]{fuben.getFubenId(),fuben.getTimes()});
				}
			}	
		}
		List<RoleMaigu> maiguList =maiguExportService.getRoleMaiguList(userRoleId);
		if(maiguList!=null && maiguList.size()>0){
			for (RoleMaigu fuben : maiguList) {
				if(fuben.getTimes()>0){
					objs.add(new Object[]{fuben.getFubenId(),fuben.getTimes()});
				}
			}
		}
		if(objs.isEmpty()){
			return null;
		}
		return objs.toArray();
	}

	/**
	 * 获取八卦副本队伍信息
	 * 
	 * @param fubenId
	 * @return
	 */
	public Object[] selectTeamByFubenId(Integer fubenId,int beginIndex,int endIndex) {
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}
		Long sizeL = redis.llen(KuafuBaguaDataUtil.getRedisTeamListKey(fubenId));
		int size = sizeL.intValue();
		if(beginIndex>size-1 || beginIndex > endIndex){
			return new Object[]{fubenId,null,beginIndex,size};
		}
		if(endIndex>size-1){
			endIndex = size-1;
		}
		List<String> teamids = redis.lrange(
				KuafuBaguaDataUtil.getRedisTeamListKey(fubenId), beginIndex, endIndex);
		if (teamids == null || teamids.isEmpty()) {
			return new Object[]{fubenId,null,beginIndex,size};
		}
		List<Object[]> objs = new ArrayList<>();
		for (String teamId : teamids) {
			// 暂时服务器ID不做处理
			Map<String, String> team = redis.hgetAll(KuafuBaguaDataUtil
					.getRedisTeamIdKey(teamId));
			if (team == null || team.isEmpty()) {
				redis.lrem(KuafuBaguaDataUtil.getRedisTeamListKey(fubenId), teamId+"");
				continue;
			}
			String ing = team.get(GameConstants.REDIS_BAGUA_FUBEN_ING);
			if(ing==null || ing.equals("1")){
				continue;
			}
			objs.add(new Object[] {
					team.get(GameConstants.REDIS_BAGUA_FUBEN_TEAMID_KEY), 0,
					team.get(GameConstants.REDIS_BAGUA_FUBEN_TEAMLEADER_KEY),
					team.get(GameConstants.REDIS_BAGUA_FUBEN_ZPLUS_KEY),
					team.get(GameConstants.REDIS_BAGUA_FUBEN_COUNT_KEY) });
			// objs.add(new
			// Object[]{team.getTeamId(),0,team.getName(),team.getStrength(),team.getMembers().size()});
		}
		return new Object[]{fubenId,objs.toArray(),beginIndex,size};
	}

	// public Object[] initTeamInfo(Integer fubenId){//旧版跨服用方法，暂时废弃
	// List<MoreFubenTeam> teams =
	// MoreFubenTeamManage.getTeamFubenMap().get(fubenId);
	// if(teams == null || teams.isEmpty()){
	// return null;
	// }
	// // List<MoreFubenTeam> subTeams = (List<MoreFubenTeam>)
	// getTeamInfo(currPage,perPage,teams);
	// // if(subTeams == null || subTeams.isEmpty()){
	// // return null;
	// // }
	// List<Object[]> objs = new ArrayList<>();
	// for(MoreFubenTeam team : teams){
	// // 暂时服务器ID不做处理
	// objs.add(new
	// Object[]{team.getTeamId(),0,team.getName(),team.getStrength(),team.getMembers().size()});
	// }
	// return new Object[]{fubenId,objs.toArray()};
	// }

	/**
	 * 获取分页组队信息
	 * 
	 * @param currPage
	 * @param perPage
	 * @param data
	 * @return
	 */
	public List<?> getTeamInfo(int currPage, int perPage, List<?> data) {
		currPage = currPage - 1;
		int pageCount = getTotalPageCount(data, perPage);
		if (currPage > pageCount) {
			return null;
		}
		int startIndex = currPage * perPage;
		int totalCount = data.size();
		int lastPageRecord = totalCount % perPage;
		int endIndex = (startIndex + perPage) > totalCount ? (startIndex + lastPageRecord)
				: (startIndex + perPage);
		if (endIndex > totalCount) {
			return null;
		}
		return data.subList(startIndex, endIndex);
	}

	/**
	 * 获取累计页数
	 * 
	 * @param data
	 * @param perPage
	 * @return
	 */
	private int getTotalPageCount(List<?> data, int perPage) {
		int totalCount = data.size();
		int pageCount = totalCount / perPage;
		return ((totalCount % perPage) != 0 ? 1 : 0) + pageCount;
	}

	/**
	 * 通过副本ID和玩家ID获取副本单个对象
	 * 
	 * @param userRoleId
	 * @param fubenId
	 * @return
	 */
	public RoleBagua getRoleBagua(Long userRoleId, int fubenId) {
		final Integer id = fubenId;
		List<RoleBagua> fubens = roleBaguaDao.cacheLoadAll(userRoleId,
				new IQueryFilter<RoleBagua>() {
					private boolean stop = false;

					@Override
					public boolean check(RoleBagua fuben) {
						if (fuben.getFubenId().equals(id)) {
							stop = true;
							return true;
						} else {
							return false;
						}
					}

					@Override
					public boolean stopped() {
						return stop;
					}
				});
		if (fubens != null && fubens.size() > 0) {
			RoleBagua fuben = fubens.get(0);
			if (!DatetimeUtil.dayIsToday(fuben.getUpdateTime())) {
				fuben.setTimes(0);
				fuben.setUpdateTime(GameSystemTime.getSystemMillTime());
				roleBaguaDao.cacheUpdate(fuben, userRoleId);
			}
			return fuben;
		}
		return null;
	}

	/**
	 * 倒计时结束后进入副本正式进入八卦副本场景
	 * 
	 * @param teamId
	 * @return
	 */
	public void afterDjsKf(Integer teamId,Integer ceng) {

		// 进入的副本队伍是否存在
		BaguaFubenTeam team = BaguaFubenTeamManage.getTeamByTeamID(teamId);
		if (team == null) {
			return;
		}

		synchronized (team) {
			team = BaguaFubenTeamManage.getTeamByTeamID(teamId);
			if (team == null) {
				return;
			}

			// 配置出错
			DuoRenTongYongBiaoConfig config = duoRenTongYongBiaoExportService
					.loadByOrder(BaguaConstant.BUAGUA_FUBEN,
							team.getBelongFubenId());
			if (config == null) {
				return;
			}

			// 发送到场景进入地图
			DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config
					.getMapId());
			BaguaPublicConfig publicConfig = getPublicConfig();
			int[] zuobiao = null;
			if(ceng<8){
				zuobiao = publicConfig.getZuobiao();
			}else{
				zuobiao = publicConfig.getFuhuo();
			}
			Object[] applyEnterData = new Object[] { dituCoinfig.getId(),
					zuobiao[0], zuobiao[1], MapType.BAGUA_FUBEN_MAP,
					team.getBelongFubenId(), team.getTeamId(),null,ceng };

			Object[] obj = new Object[] {
					AppErrorCode.SUCCESS,
					team.getBelongFubenId(),
					GameSystemTime.getSystemMillTime() + config.getTime()
							* 1000,team.getOpenDoorInfo(ceng, baguaZhenMenExportService) };
			for (BaguaFubenTeamMember member : team.getMembers()) {
//				BaguaFubenTeamManage.removeTeam(member.getRoleId(), false);
				if (!checkRoleData(member)) {
					continue;
				}
				Object[] msg = new Object[] { ClientCmdType.BAGUA_ENTER_FUBEN,
						obj, member.getRoleId() };
				KuafuMsgSender.send2OneKuafuSource(member.getServerId(),
						InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
				BaguaFubenTeamManage.memberReadyEnter(member.getRoleId(),
						applyEnterData);
				// 活跃度
				KuafuMsgSender.send2OneKuafuSource(member.getServerId(),
						InnerCmdType.INNER_HUOYUEDU_BAGUA, new Object[] {
								ActivityEnum.A26, member.getRoleId() });
				// 通知原服玩家已跨服
				KuafuMsgSender.send2OneKuafuSource(member.getServerId(),
						InnerCmdType.BAGUA_ENTER_XIAO_HEI_WU,
						member.getRoleId());
			}
			Redis redis = GameServerContext.getRedis();
			if(redis!=null){
				redis.hmset(
						KuafuBaguaDataUtil.getRedisTeamIdKey(teamId
								+ ""), team.getSaveInfo());
			}
			// 清除组队信息
//			BaguaFubenTeamManage.getTeamMap().remove(team.getTeamId());
//			BaguaFubenTeamManage.getTeamFubenMap().get(team.getBelongFubenId())
//					.remove(team);
//			Redis redis = GameServerContext.getRedis();
//			if (redis != null) {
//				redis.del(KuafuBaguaDataUtil.getRedisTeamIdKey(teamId + ""));
//				redis.lrem(KuafuBaguaDataUtil.getRedisTeamListKey(team
//						.getBelongFubenId()), teamId + "");
//				redis.lrem(KuafuBaguaDataUtil.getRedisServerKey(), teamId + "");
//			}

		}
	}

	/**
	 * 成员跨服处理
	 * 
	 * @param member
	 */
	private boolean checkRoleData(BaguaFubenTeamMember member) {
		Object roleData = member.getRoleData();
		Long userRoleId = member.getRoleId();
		if (roleData == null) {// 如果跨服数据未同步过来，视为进入失败
			KuafuMsgSender.send2KuafuSource(userRoleId,
					InnerCmdType.BAGUA_ENTER_FUBEN_FAIL, null);
			return false;
		}
		KuafuOnlineUtil.changeSomeoneOnline(userRoleId);
		dataContainer.putData(GameConstants.COMPONENET_KUAFU_DATA, userRoleId
				+ "", roleData);
		return true;
	}

	/**
	 * 玩家进入跨服
	 * 
	 * @param userRoleId
	 */
	public void enterXiaoheiwu(Long userRoleId) {
		String stageId = stageControllExportService.getCurStageId(userRoleId);
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		KuafuManager.startKuafu(userRoleId);
		// 发送到场景控制中心进入小黑屋地图，以便从跨服服务器出来时走完整流程
		DiTuConfig config = diTuConfigExportService.loadSafeDiTu();
		int[] birthXy = config.getRandomBirth();
		Object[] applyEnterData = new Object[] { config.getId(), birthXy[0],
				birthXy[1], MapType.KUAFU_SAFE_MAP};
		// 传送前加一个无敌状态
		role.getStateManager().add(new NoBeiAttack());
		role.setChanging(true);
		StageMsgSender.send2StageControl(userRoleId,
				InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		// 通知跨服可以进入场景了
		KuafuMsgSender.send2KuafuServer(userRoleId, userRoleId,
				InnerCmdType.BAGUA_ENTER_FUBEN, null);
	}

	/**
	 * 领取副本奖励 玩家可手动领取，客户端也可在时间倒计时向服务端发送请求
	 * 
	 * @param userRoleId
	 * @param fubenId
	 * @return
	 */
	public Object[] pickReward(Long userRoleId, Integer fubenId) {

		RoleBagua fuben = getRoleBagua(userRoleId, fubenId);
		// 判断副本完成状态
		if (fuben == null
				|| fuben.getStatus() != GameConstants.BAGUA_FUBEN_FINISH_STATUS) {
			return AppErrorCode.FUBEN_NOT_FINISH;
		}

		String stageId = stageControllExportService.getCurStageId(userRoleId);
		if (stageId == null) {
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return AppErrorCode.STAGE_IS_NOT_EXIST;
		}

		IRole role = stage.getElement(userRoleId, ElementType.ROLE);

		// 验证传递参数
		DuoRenTongYongBiaoConfig config = duoRenTongYongBiaoExportService
				.loadByOrder(BaguaConstant.BUAGUA_FUBEN, fubenId);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}

		// 是否第一次通关
		boolean isFirst = fuben.getTimes() < 1;

		// 验证背包
		// if(isFirst && roleBagExportService.checkPutInBag(config.getReward(),
		// userRoleId) != null){
		// return AppErrorCode.BAG_NOEMPTY;
		// }
		fuben.setStatus(0);
		fuben.setTimes(fuben.getTimes() + 1);
		roleBaguaDao.cacheUpdate(fuben, userRoleId);
		if (isFirst) {
			String content = EmailUtil.getCodeEmail(
					GameConstants.BAGUA_FUBEN_EMAIL_REWARD, config.getName());
			roleBagExportService.putInBagOrEmail(config.getJiangliMap(),
					userRoleId, GoodsSource.BAGUA_FUBEN_GET, true, content);
		}
		if (config.getJiangexp() > 0) {
			roleBagExportService.incrNumberWithNotify(GoodsCategory.EXP,
					config.getJiangexp(), userRoleId,
					LogPrintHandle.GET_BAGUA_FUBEN,
					LogPrintHandle.GBZ_BAGUA_FUBEN);
		}
		if (config.getJiangmoney() > 0) {
			roleBagExportService.incrNumberWithNotify(GoodsCategory.MONEY,
					config.getJiangmoney(), userRoleId,
					LogPrintHandle.GET_BAGUA_FUBEN,
					LogPrintHandle.GBZ_BAGUA_FUBEN);
		}
		// 打印日志
		if (isFirst) {
			JSONArray goods = LogPrintHandle.getLogGoodsParam(
					config.getJiangliMap(), null);
			GamePublishEvent.publishEvent(new BaguaFubenRewardLogEvent(
					userRoleId, role.getName(), goods, config.getJiangexp(),
					config.getJiangmoney(), fubenId));
		} else {
			GamePublishEvent.publishEvent(new BaguaFubenRewardLogEvent(
					userRoleId, role.getName(), null, config.getJiangexp(),
					config.getJiangmoney(), fubenId));
		}
		// TODO 成就
		// if(fubenId.intValue() == 3){
		// //成就
		// try {
		// BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE,
		// new Object[]{GameConstants.CJ_DIHUOFUBENCOUNT, 1});
		// //roleChengJiuExportService.tuisongChengJiu(userRoleId,
		// GameConstants.CJ_DIHUOFUBENCOUNT, 1);
		// } catch (Exception e) {
		// ChuanQiLog.error("",e);
		// }
		// }

		return AppErrorCode.OK;
	}

	/**
	 * 上线处理
	 * 
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId) {
		List<RoleBagua> list = roleBaguaDao.cacheLoadAll(userRoleId);
		for (RoleBagua baguaFuben : list) {
			if (baguaFuben.getStatus() == GameConstants.BAGUA_FUBEN_FINISH_STATUS) {
				baguaFuben.setStatus(0);
				baguaFuben.setTimes(baguaFuben.getTimes() + 1);
				roleBaguaDao.cacheUpdate(baguaFuben, userRoleId);

				DuoRenTongYongBiaoConfig config = duoRenTongYongBiaoExportService
						.loadByOrder(BaguaConstant.BUAGUA_FUBEN,
								baguaFuben.getFubenId());
				String title = EmailUtil.getCodeEmail(GameConstants.FUBEN_EMAIL_REWARD_TITLE);
				String content = EmailUtil.getCodeEmail(GameConstants.BAGUA_FUBEN_EMAIL_REWARD_RESEND,
						config.getName());
				Map<String, Integer> gift = new HashMap<>();
				if (baguaFuben.getTimes() <= 1) {
					ObjectUtil.mapAdd(gift, config.getJiangliMap());
				}
				if (config.getJiangexp() > 0) {
					Long value = config.getJiangexp();
					if (gift.containsKey(ModulePropIdConstant.EXP_GOODS_ID)) {
						value += gift.get(ModulePropIdConstant.EXP_GOODS_ID);
					}
					gift.put(ModulePropIdConstant.EXP_GOODS_ID,
							value.intValue());
				}
				if (config.getJiangmoney() > 0) {
					int value = config.getJiangmoney();
					if (gift.containsKey(ModulePropIdConstant.MONEY_GOODS_ID)) {
						value += gift.get(ModulePropIdConstant.MONEY_GOODS_ID);
					}
					gift.put(ModulePropIdConstant.MONEY_GOODS_ID, value);
				}
				// 发送奖励邮件
				String[] attachments = EmailUtil.getAttachments(gift);
				for (String attachment : attachments) {
					emailExportService.sendEmailToOne(userRoleId, title,content,GameConstants.EMAIL_TYPE_SINGLE, attachment);
				}
			}
		}
	}

	/**
	 * 离线处理
	 * 
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId) {
		BaguaFubenTeamManage.leaveTeam(userRoleId);
		BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID,
				InnerCmdType.BAGUA_FUBEN_OFFLINE_HANDLE_SYN, userRoleId);
	}

	/**
	 * 异步离线处理
	 * 
	 * @param userRoleId
	 */
	public void offlineHandleSyn(Long userRoleId) {
		KuafuMsgSender
				.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID, userRoleId,
						InnerCmdType.BAGUA_FUBEN_OFFLINE_HANDLE, userRoleId);
	}

	/**
	 * 离线处理(跨服)
	 * 
	 * @param userRoleId
	 */
	public void offlineHandleKF(Long userRoleId) {
		// dataContainer.removeData(GameModType.STAGE_CONTRAL,
		// userRoleId.toString());
		Integer teamId = BaguaFubenTeamManage.getTeamId(userRoleId);
		if (teamId == null) {
			return;
		}
		// 多人副本未开始，清除多人副本管理对象中的对象数据
		BaguaFubenTeam mft = BaguaFubenTeamManage.getTeamMap().get(teamId);
		if (mft == null) {
			BaguaFubenTeamManage.removeTeam(userRoleId, false);
		} else {
			mft.removeMember(userRoleId);
			Redis redis = GameServerContext.getRedis();
			if (redis != null) {
				if (mft.getMembers().isEmpty()) {
					if (mft.isDjs()) {
						busScheduleExportService.cancelSchedule(mft.getTeamId()
								+ "", GameConstants.BAGUA_DJS);
					}
					BaguaFubenTeamManage.getTeamMap().remove(teamId);
					if (redis != null) {
						redis.del(KuafuBaguaDataUtil.getRedisTeamIdKey(teamId
								+ ""));
						redis.lrem(KuafuBaguaDataUtil.getRedisTeamListKey(mft
								.getBelongFubenId()), teamId + "");
						redis.lrem(KuafuBaguaDataUtil.getRedisServerKey(),
								teamId + "");
					}
				} else {
					if (redis != null) {
						redis.hmset(
								KuafuBaguaDataUtil.getRedisTeamIdKey(teamId
										+ ""), mft.getSaveInfo());
					}
				}
			}
		}
	}

	/**
	 * 退出多人副本
	 * 
	 * @param stageId
	 */
	public Object[] leaveFubenKf(Long userRoleId) {
		if (!KuafuConfigPropUtil.isKuafuServer()) {
			return null;// 非跨服不处理该指令
		}
		// 是否在多人副本中
		if (!stageControllExportService.inFuben(userRoleId)) {
			return AppErrorCode.FUBEN_NOT_IN_FUBEN;
		}
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.BAGUA_SELF_LEAVE_FUBEN,
				null);
		return null;
	}

	/**
	 * 完成副本清场
	 * 
	 * @param stageId
	 * @param type
	 */
	public void fubenFinishHandle(Long userRoleId, Integer fubenId) {
		// 更改副本状态
		RoleBagua baguaFuben = getRoleBagua(userRoleId, fubenId);
		if (baguaFuben == null) {
			baguaFuben = createRoleBagua(userRoleId, fubenId);
		} else {
			baguaFuben.setStatus(GameConstants.BAGUA_FUBEN_FINISH_STATUS);
			baguaFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
			roleBaguaDao.cacheUpdate(baguaFuben, userRoleId);
		}
	}

	/**
	 * 创建多人副本
	 * 
	 * @param userRoleId
	 * @param fubenId
	 * @return
	 */
	private RoleBagua createRoleBagua(Long userRoleId, Integer fubenId) {
		RoleBagua fuben = new RoleBagua();
		fuben.setUserRoleId(userRoleId);
		fuben.setId(IdFactory.getInstance().generateId(ServerIdType.FUBEN));
		fuben.setFubenId(fubenId);
		fuben.setTimes(0);
		fuben.setStatus(GameConstants.BAGUA_FUBEN_FINISH_STATUS);
		fuben.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleBaguaDao.cacheInsert(fuben, userRoleId);
		return fuben;
	}

	/**
	 * 创建八卦副本队伍
	 * 
	 * @param teamLeader
	 * @return
	 */
	public Object[] createTeamKf(Integer belongFubenId,
			Long strength, Boolean isAuto, Long userRoleId, Object[] roleData) {
		// 是否已经有队伍
		if (BaguaFubenTeamManage.getTeamId(userRoleId) != null) {
			return AppErrorCode.BAGUA_TEAM_EXISTS;
		}

		// 是否已经在副本中
		// if(stageControllExportService.inFuben(userRoleId)){
		// return AppErrorCode.FUBEN_IS_IN_FUBEN;
		// }

		// 所属副本ID是否存在
		DuoRenTongYongBiaoConfig config = duoRenTongYongBiaoExportService
				.loadByOrder(BaguaConstant.BUAGUA_FUBEN, belongFubenId);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}

		// 当前副本队伍已达上限
		List<BaguaFubenTeam> teams = BaguaFubenTeamManage.getTeamFubenMap()
				.get(belongFubenId);
		if (teams != null && teams.size() > GameConstants.BAGUA_FUBEN_LIMIT) {
			return AppErrorCode.BAGUA_FUBEN_TEAM_LIMIT_ERROR;
		}
		
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}

		BaguaFubenTeamMember teamLeader = createTeamMember(roleData);
		BaguaFubenTeam team = new BaguaFubenTeam(teamLeader);
		//随机每一层的阵门
		Map<Integer, Map<Integer, BaguaZhenmenInfo>> zhenmenInfo = initZhenmenInfo();
		team.setZhenmenInfo(zhenmenInfo);
		int teamId = KuafuBaguaDataUtil.getRedisTeamId(redis.generateId(
				GameConstants.REDIS_BAGUA_FUBEN_TEAM_ID,
				GameConstants.REDIS_BAGUA_FUBEN_TEAM_ID));
		team.setTeamId(teamId);
		if (strength != null) {
			team.setStrength(strength);
		}
		// if(pwd != null){
		// team.setPwd(pwd);
		// }
		if (belongFubenId != null) {
			team.setBelongFubenId(belongFubenId);
		}
		team.setAuto(isAuto);
		teamLeader.setTeam(team);
		BaguaFubenTeamManage.addBaguaFubenTeamMap(belongFubenId, team);
		BaguaFubenTeamManage.addTeamMap(teamId, team);
		BaguaFubenTeamManage.addTeamIdMap(userRoleId, teamLeader);

		redis.hmset(KuafuBaguaDataUtil.getRedisTeamIdKey(teamId + ""),
				team.getSaveInfo());
		redis.lpush(KuafuBaguaDataUtil.getRedisTeamListKey(belongFubenId),
				teamId + "");
		redis.lpush(KuafuBaguaDataUtil.getRedisServerKey(), teamId + "");
		ChuanQiLog.info("userRoleId={} create bagua fubenId={} teamId={}", userRoleId,belongFubenId,teamId);
		return new Object[] { AppErrorCode.SUCCESS, belongFubenId, teamId,
				strength, isAuto };
	}

	private BaguaPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_BAGUA);
	}
	
	private Random random = new Random();
	/**
	 * 层是从1开始 位置也是从1开始
	 * @return
	 */
	private Map<Integer, Map<Integer, BaguaZhenmenInfo>> initZhenmenInfo() {
		BaguaPublicConfig publicConfig = getPublicConfig();
		int ceng = publicConfig.getCeng();
		Map<Integer, Map<Integer, BaguaZhenmenInfo>> ret = new HashMap<Integer, Map<Integer, BaguaZhenmenInfo>>();
		for(int i=1;i<ceng;i++){
			//最后一层没有阵门
			Map<Integer, BaguaZhenmenInfo> tmp = new HashMap<Integer,BaguaZhenmenInfo>();
			ret.put(i, tmp);
			//随机一个位置，放置生门
			int firstIndex = random.nextInt(8);
			int zhenmenId=1;
			for(int j=firstIndex;j<firstIndex+8;j++){
				int position = j;
				if(position>7){
					position = position - 8;
				}
				tmp.put(position, new BaguaZhenmenInfo(zhenmenId));
				zhenmenId++;
			}
		}
		return ret;
	}
	
	/**
	 * 加入多人副本队伍(跨服)
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] joinTeamKf(Long userRoleId, Integer teamId,
			Object[] roleData) {
		// 是否已经加入其它队伍
		Integer tmpTeamId = BaguaFubenTeamManage.getTeamId(userRoleId);
		if (tmpTeamId != null) {
			return AppErrorCode.BAGUA_TEAM_OTHER_IN;
		}

		// 是否已经在副本中
		// if(stageControllExportService.inFuben(userRoleId)){
		// return AppErrorCode.FUBEN_IS_IN_FUBEN;
		// }

		// 加入的多人副本队伍是否存在
		BaguaFubenTeam mftTarget = BaguaFubenTeamManage.getTeamByTeamID(teamId);
		if (mftTarget == null) {
			return AppErrorCode.BAGUA_TEAM_NOT_EXISTS;
		}

		// 加入的多人副本队伍是否进入倒计时阶段
		if (mftTarget.isDjs()) {
			return AppErrorCode.BAGUA_FUBEN_DJS;
		}

		// 玩家是否在目标队伍中
		if (mftTarget.isTeamMember(userRoleId)) {
			return AppErrorCode.BAGUA_TEAM_IN;
		}

		// 玩家等级是否满足
		DuoRenTongYongBiaoConfig config = duoRenTongYongBiaoExportService
				.loadByOrder(BaguaConstant.BUAGUA_FUBEN,
						mftTarget.getBelongFubenId());
		if ((Integer) roleData[4] < config.getLevel()) {
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}

		// 队伍人数是否达到上限
		if (mftTarget.getMembers().size() >= config.getTuijian()) {
			return AppErrorCode.BAGUA_TEAM_PEOPLE_COUNT;
		}
		// 密码是否正确
		// if(mftTarget.getPwd() > 0 && mftTarget.getPwd() != pwd){
		// return AppErrorCode.BAGUA_TEAM_PWD_ERROR;
		// }
		long zplus = LongUtils.obj2long(roleData[3]);
		// 战力是否达到
		if (mftTarget.getStrength() > 0 && zplus < mftTarget.getStrength()) {
			return AppErrorCode.BAGUA_TEAM_STRENGTH_LESS;
		}
		// 加入队伍
		List<BaguaFubenTeamMember> members = mftTarget.getMembers();// 原先队员

		BaguaFubenTeamMember teamMember = createTeamMember(roleData);
		teamMember.setTeam(mftTarget);
		mftTarget.addMembers(teamMember, config.getTuijian());
		BaguaFubenTeamManage.addTeamIdMap(userRoleId, teamMember);
		Object[] obj = new Object[] { teamId, teamMember.getMemberVo() };
		for (BaguaFubenTeamMember member : members) {
			Object[] msg = new Object[] { ClientCmdType.BAGUA_JOIN_TEAM_NOTICE,
					obj, member.getRoleId() };// 封装转发数据
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(),
					InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
		Redis redis = GameServerContext.getRedis();
		if (redis != null) {
			redis.hmset(KuafuBaguaDataUtil.getRedisTeamIdKey(teamId + ""),
					mftTarget.getSaveInfo());
		}
		ChuanQiLog.info("userRoleId={} join fubenId={} teamId={}", userRoleId,mftTarget.getBelongFubenId(),teamId);
		return new Object[] { AppErrorCode.SUCCESS, teamId,
				mftTarget.getLeader().getRoleId(),
				mftTarget.getBelongFubenId(), mftTarget.getStrength(),
				mftTarget.isAuto(), mftTarget.getTeamInfoArr() };
	}

	/**
	 * 退出幻境历练副本队伍(跨服)
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] leaveTeamKf(Long userRoleId) {
		// 队伍是否存在
		Integer teamId = BaguaFubenTeamManage.getTeamId(userRoleId);
		if (teamId == null) {
			return AppErrorCode.TEAM_IS_NOT_EXIST;
		}
		BaguaFubenTeam mft = BaguaFubenTeamManage.getTeamMap().get(teamId);
		if (mft == null) {
			BaguaFubenTeamManage.removeTeam(userRoleId, true);
			return AppErrorCode.TEAM_IS_NOT_EXIST;
		}

		// 主动离开
		mft.removeMember(userRoleId);
		Redis redis = GameServerContext.getRedis();
		if (mft.getMembers().isEmpty()) {
			if (mft.isDjs()) {
				busScheduleExportService.cancelSchedule(mft.getTeamId() + "",
						GameConstants.BAGUA_DJS);
			}
			BaguaFubenTeamManage.getTeamMap().remove(teamId);
			if (redis != null) {
				redis.del(KuafuBaguaDataUtil.getRedisTeamIdKey(teamId + ""));
				redis.lrem(KuafuBaguaDataUtil.getRedisTeamListKey(mft
						.getBelongFubenId()), teamId + "");
				redis.lrem(KuafuBaguaDataUtil.getRedisServerKey(), teamId + "");
			}
		} else {
			if (redis != null) {
				redis.hmset(KuafuBaguaDataUtil.getRedisTeamIdKey(teamId + ""),
						mft.getSaveInfo());
			}
		}
		return null;
	}

	/**
	 * 踢出幻境副本队伍(跨服)
	 * 
	 * @param userRoleId
	 * @param targetRoleId
	 * @return
	 */
	public Object[] kickKf(Long userRoleId, Long targetRoleId) {
		// 队伍是否存在
		Integer teamId = BaguaFubenTeamManage.getTeamId(userRoleId);
		if (teamId == null) {
			return AppErrorCode.BAGUA_TEAM_NOT_EXISTS;
		}
		BaguaFubenTeam mft = BaguaFubenTeamManage.getTeamMap().get(teamId);
		// 领队踢出
		if (!userRoleId.equals(mft.getLeader().getRoleId())) {
			return AppErrorCode.TEAM_IS_NOT_LEADER;
		}
		if (!mft.isTeamMember(targetRoleId)) {
			// 没有该玩家，不允许踢出
			return AppErrorCode.BAGUA_TEAM_NOT_IN;
		}
		BaguaFubenTeamMember member = mft.removeMember(targetRoleId);
		Redis redis = GameServerContext.getRedis();
		if (redis != null) {
			redis.hmset(KuafuBaguaDataUtil.getRedisTeamIdKey(teamId + ""),
					mft.getSaveInfo());
		}
		return new Object[] { AppErrorCode.SUCCESS, teamId, member.getRoleId() };
	}

	/**
	 * 队长更改幻境历练副本队伍进入需求战力
	 * 
	 * @param userRoleId
	 * @param strength
	 * @return
	 */
	public Object[] changeStrengthKf(Long userRoleId, Long strength) {
		// 队伍是否存在
		Integer teamId = BaguaFubenTeamManage.getTeamId(userRoleId);
		if (teamId == null) {
			return AppErrorCode.TEAM_IS_NOT_EXIST;
		}
		BaguaFubenTeam mft = BaguaFubenTeamManage.getTeamMap().get(teamId);
		if (!userRoleId.equals(mft.getLeader().getRoleId())) {
			return AppErrorCode.TEAM_IS_NOT_LEADER;
		}
		mft.setStrength(strength);
		Object[] obj = new Object[] { teamId, strength };
		for (BaguaFubenTeamMember member : mft.getMembers()) {
			Object[] msg = new Object[] {
					ClientCmdType.BAGUA_CHANGE_STRENGTH, obj,
					member.getRoleId() };
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(),
					InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
		return null;
	}

	/**
	 * 队长更改幻境历练副本队伍是否满员自动挑战
	 * 
	 * @param userRoleId
	 * @param isAuto
	 * @return
	 */
	public Object[] changeTeamAutoStartKf(Long userRoleId) {
		// 队伍是否存在
		Integer teamId = BaguaFubenTeamManage.getTeamId(userRoleId);
		if (teamId == null) {
			return AppErrorCode.TEAM_IS_NOT_EXIST;
		}
		BaguaFubenTeam mft = BaguaFubenTeamManage.getTeamMap().get(teamId);
		if (!userRoleId.equals(mft.getLeader().getRoleId())) {
			return AppErrorCode.TEAM_IS_NOT_LEADER;
		}
		if (mft.isAuto()) {
			mft.setAuto(false);
		} else {
			mft.setAuto(true);
		}
		Object[] obj = new Object[] { teamId, mft.isAuto() };
		for (BaguaFubenTeamMember member : mft.getMembers()) {
			Object[] msg = new Object[] { ClientCmdType.BAGUA_CHANGE_TEAM_AUTO_START,
					obj, member.getRoleId() };
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(),
					InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
		return null;
	}

	/**
	 * 发送玩家数据到跨服
	 * 
	 * @param userRoleId
	 */
	public void sendRoleData(Long userRoleId) {
		String stageId = stageControllExportService.getCurStageId(userRoleId);
		if (ObjectUtil.strIsEmpty(stageId)) {
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		if (stage == null || stage.isCopy()) {
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		Object roleData = RoleFactory.createKuaFuRoleData(role);
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,
				userRoleId, InnerCmdType.BAGUA_SEND_ROLE_DATA,
				new Object[] { roleData, userRoleId });
	}

	/**
	 * 接收玩家数据到跨服
	 * 
	 * @param userRoleId
	 */
	public void recieveRoleDataKf(Object[] roleData) {
		Long userRoleId = LongUtils.obj2long(roleData[1]);
		BaguaFubenTeamMember member = BaguaFubenTeamManage
				.getTeamMember(userRoleId);
		member.setRoleData(roleData);
	}

	/**
	 * 离开了队伍
	 * 
	 * @param userRoleId
	 */
	public void leaveTeam1(Long userRoleId) {
		KuafuManager.removeKuafu(userRoleId);
		KuafuRoleServerManager.getInstance().removeBind(userRoleId);
		stageControllExportService.changeFuben(userRoleId, false);
		if (BaguaFubenTeamManage.leaveTeam(userRoleId)) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.BAGUA_LEAVE_FUBEN,
					AppErrorCode.OK);
		}
	}

	/**
	 * 可以进入跨服场景了
	 * 
	 * @param userRoleId
	 */
	public void enterFubenKf(Long userRoleId) {
		Object[] data = BaguaFubenTeamManage.getEnterData(userRoleId);
		if (data == null) {
			KuafuMsgSender.send2KuafuSource(userRoleId,
					InnerCmdType.BAGUA_ENTER_FUBEN_FAIL, null);
			return;
		}

		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL,
				userRoleId.toString());
		if (null == roleState) {
			roleState = new RoleState(userRoleId);
			dataContainer.putData(GameModType.STAGE_CONTRAL,
					userRoleId.toString(), roleState);
		}
		// 发送到场景进入地图
		BusMsgSender.send2BusInner(userRoleId,
				InnerCmdType.S_APPLY_CHANGE_STAGE, data);
	}

	/**
	 * 离开副本
	 * 
	 * @param userRoleId
	 */
	public void leaveFubenYf(Long userRoleId) {
		leaveTeam1(userRoleId);
		// BusMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_EXIT,
		// AppErrorCode.OK);
		BusMsgSender.send2BusInner(userRoleId,
				InnerCmdType.S_APPLY_LEAVE_STAGE, null);
	}

	/**
	 * 进入多人本失败
	 * 
	 * @param userRoleId
	 */
	public void enterFubenFail(Long userRoleId) {
		BusMsgSender.send2BusInner(userRoleId,
				InnerCmdType.S_APPLY_LEAVE_STAGE, null);
		BusMsgSender.send2One(userRoleId, ClientCmdType.BAGUA_ENTER_FUBEN,
				AppErrorCode.KUAFU_ENTER_FAIL);
		leaveTeam1(userRoleId);
		ChuanQiLog.error("进入多人本失败，id：{}", userRoleId);
	}

	public void joinTeamSuccess(Long userRoleId, Integer teamId,
			Integer fubenId) {
		if (userRoleId == null || teamId == null || fubenId == null) {
			return;
		}
		BaguaFubenTeamManage.enterTeam(userRoleId, teamId, fubenId);
	}

	// /**
	// * 邀请
	// * @param userRoleId
	// */
	// public void baguaFubenYaoqing(Long userRoleId){
	// Object[] msg = BaguaFubenTeamManage.getYaoQingMsg(userRoleId);
	// if(msg != null){
	// RoleWrapper role = roleExportService.getLoginRole(userRoleId);
	// if(role != null){
	// msg[0] = role.getName();
	// BusMsgSender.send2All(ClientCmdType.MORE_FUBEN_YAOQING_OTHER, msg);
	// }
	// }
	// }

	/**
	 * 离开副本(跨服)
	 * 
	 * @param userRoleId
	 */
	public void exitKuafu(Long userRoleId) {
		dataContainer.removeData(GameModType.STAGE_CONTRAL,
				userRoleId.toString());

		KuafuSessionManager.removeUserRoleId(userRoleId);
		KuafuOnlineUtil.changeSomeoneOffline(userRoleId);
		
		//新增逻辑 
		Integer teamId = BaguaFubenTeamManage.getTeamId(userRoleId);
		if (teamId == null) {
			return;
		}
		// 多人副本未开始，清除多人副本管理对象中的对象数据
		BaguaFubenTeam mft = BaguaFubenTeamManage.getTeamMap().get(teamId);
		if (mft == null) {
			BaguaFubenTeamManage.removeTeam(userRoleId, false);
		} else {
			mft.removeMember(userRoleId);
			Redis redis = GameServerContext.getRedis();
			if (redis != null) {
				if (mft.getMembers().isEmpty()) {
					if (mft.isDjs()) {
						busScheduleExportService.cancelSchedule(mft.getTeamId()
								+ "", GameConstants.BAGUA_DJS);
					}
					BaguaFubenTeamManage.getTeamMap().remove(teamId);
					if (redis != null) {
						redis.del(KuafuBaguaDataUtil.getRedisTeamIdKey(teamId
								+ ""));
						redis.lrem(KuafuBaguaDataUtil.getRedisTeamListKey(mft
								.getBelongFubenId()), teamId + "");
						redis.lrem(KuafuBaguaDataUtil.getRedisServerKey(),
								teamId + "");
					}
				} else {
					if (redis != null) {
						redis.hmset(
								KuafuBaguaDataUtil.getRedisTeamIdKey(teamId
										+ ""), mft.getSaveInfo());
					}
				}
			}
		}
	}

	/** 跨服启动处理 */
	public void kfStartHandle() {
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			return;
		}
		List<String> teamids = redis.lrange(
				KuafuBaguaDataUtil.getRedisServerKey(), 0, -1);
		if (teamids == null || teamids.isEmpty()) {
			return;
		}
		for (String teamId : teamids) {
			redis.del(KuafuBaguaDataUtil.getRedisTeamIdKey(teamId));
		}
		redis.del(KuafuBaguaDataUtil.getRedisServerKey());
	}
	
	/**
	 * 邀请
	 * @param userRoleId
	 */
	public void yaoqing(Long userRoleId){
		Object[] msg = BaguaFubenTeamManage.getYaoQingMsg(userRoleId);
		if(msg != null){
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			if(role != null){
				msg[0] = role.getName();
				BusMsgSender.send2All(ClientCmdType.BAGUA_FUBEN_YAOQING_OTHER, msg);
			}
		}
	}
	
	public void startHandleOutTimeTeamSchedule(){
		BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.HANDLE_BAGUA_FUBEN_TIME_OUT_TEAM,null);
		busScheduleExportService.schedule("bf_"+ChuanQiConfigUtil.getServerId(),GameConstants.BAGUA_FUBEN_TIME_OUT_TEAM, runable, GameConstants.BAGUA_FUBEN_TIME_OUT_TEAM_TIME,TimeUnit.SECONDS);
	}
	
	public void handleOutTimeTeam(){
		Redis redis = GameServerContext.getRedis();
		if(redis == null){
			return ;
		}
		Set<String> teamIdKeys = redis.keys(KuafuBaguaDataUtil.REDIS_TEAM_ID_KEY+"*");
		if(teamIdKeys == null || teamIdKeys.isEmpty()){
			return ;
		}
		for(String teamIdKey : teamIdKeys){
			try{
				String teamId = teamIdKey.substring(KuafuBaguaDataUtil.REDIS_TEAM_ID_KEY.length(), teamIdKey.length());
				Map<String,String> teamInfo = redis.hgetAll(KuafuBaguaDataUtil.getRedisTeamIdKey(teamId));
				if (teamInfo == null || teamInfo.isEmpty()) {
					continue;
				}
				String serverId = teamInfo.get(GameConstants.REDIS_BAGUA_FUBEN_SERVERID_KEY);
				if(serverId!=null && serverId.equals(ChuanQiConfigUtil.getServerId())){
					BaguaFubenTeam team = BaguaFubenTeamManage.getTeamByTeamID(Integer.parseInt(teamId));
					if(team==null){
						redis.del(KuafuBaguaDataUtil.getRedisTeamIdKey(String.valueOf(teamId)));
						String fubenId =  teamInfo.get(GameConstants.REDIS_BAGUA_FUBEN_FUBENID_KEY);
						redis.lrem(KuafuBaguaDataUtil.getRedisTeamListKey(Integer.parseInt(fubenId)), String.valueOf(teamId));
						redis.lrem(KuafuBaguaDataUtil.getRedisServerKey(), String.valueOf(teamId));
					}else{
						long liveTime  = GameSystemTime.getSystemMillTime()-team.getStartTime();
						if(liveTime>60*60*1000){
							for(BaguaFubenTeamMember member : team.getMembers()){	
								team.removeMember(member.getRoleId());
							}
							redis.del(KuafuBaguaDataUtil.getRedisTeamIdKey(String.valueOf(teamId)));
							redis.lrem(KuafuBaguaDataUtil.getRedisTeamListKey(team.getBelongFubenId()), teamId+"");
							redis.lrem(KuafuBaguaDataUtil.getRedisServerKey(), String.valueOf(teamId));
						}
					}
				}
			}catch (Exception e) {
				ChuanQiLog.error("handleOutTimeTeam baguafuben error teamIdKey ={}",teamIdKey);
			}
		}
		startHandleOutTimeTeamSchedule();
	}
}
