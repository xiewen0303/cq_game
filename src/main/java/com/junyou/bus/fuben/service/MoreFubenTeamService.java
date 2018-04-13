package com.junyou.bus.fuben.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.fuben.dao.MoreFubenDao;
import com.junyou.bus.fuben.entity.HuanJingFubenConfig;
import com.junyou.bus.fuben.entity.MoreFuben;
import com.junyou.bus.fuben.entity.MoreFubenTeam;
import com.junyou.bus.fuben.entity.MoreFubenTeamManage;
import com.junyou.bus.fuben.entity.MoreFubenTeamMember;
import com.junyou.bus.fuben.utils.KuafuDataUtil;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.RoleState;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.event.MoreFubenRewardLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.ModulePropIdConstant;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
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
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.core.stage.StageType;
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
public class MoreFubenTeamService {
	
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private MoreFubenDao moreFubenDao;
	@Autowired
	private MoreFubenConfigService moreFubenConfigService;
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
	private PublicRoleStateExportService publicRoleStateExportService;
	/**
	 * 初始化
	 * @param userRoleId
	 */
	public List<MoreFuben> initMoreFuben(Long userRoleId){
		return moreFubenDao.initMoreFuben(userRoleId);
	}
	
	/**
	 * 创建成员
	 * @param roleData
	 * @return
	 */
	private MoreFubenTeamMember createTeamMember(Object[] roleData){
		MoreFubenTeamMember teamMember = new MoreFubenTeamMember();
		teamMember.setRoleId(LongUtils.obj2long(roleData[0]));
		teamMember.setLevel((Integer)roleData[4]);
		teamMember.setMemberName((String)roleData[1]);
		teamMember.setStrength(LongUtils.obj2long(roleData[3]));
		teamMember.setConfigId((Integer)roleData[5]);
		teamMember.setServerId((String)roleData[2]);
		return teamMember;
	}
	
	/**
	 * 创建多人副本队伍
	 * @param teamLeader
	 * @return
	 */
	public Object[] createMoreFubenTeam(Long userRoleId,Integer belongFubenId,Long strength,Boolean isAuto){
		// 所属副本ID是否存在 
		HuanJingFubenConfig config = moreFubenConfigService.loadById(belongFubenId);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		// 是否已经在副本中
		if(stageControllExportService.inFuben(userRoleId)){
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
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
		// 玩家等级是否满足
		if(role.getLevel() < config.getLevel()){
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		
		if(!KuafuConfigUtil.isKuafuAvailable()){
			return AppErrorCode.KUAFU_NO_CONNECTION;//跨服连接失败
		}
		//标记当前为副本状态
		KuafuServerInfo kuafuServerInfo = KuafuServerManager.getInstance().getMostIdleKuafuServer(0);
		if(kuafuServerInfo==null){
			return AppErrorCode.KUAFU_NO_CONNECTION;//跨服连接失败
		}
		stageControllExportService.changeFuben(userRoleId, true);
		KuafuRoleServerManager.getInstance().bindServer(userRoleId, kuafuServerInfo);
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,
				userRoleId, InnerCmdType.BIND_ROLE_ID_SERVERID, new Object[] {
						ChuanQiConfigUtil.getServerId(), userRoleId });
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,role.getId(), InnerCmdType.APPLY_CREATE_MORE_FUBEN_TEAM, new Object[]{belongFubenId,strength,isAuto,KuafuDataUtil.getRoleData(role),ChuanQiConfigUtil.getServerId()});
		return null;
	}
	
	public Object[] quickJoin(Long userRoleId,Integer fubenId){
		// 是否已经在副本中
		if(stageControllExportService.inFuben(userRoleId)){
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
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
		List<String> teamids = redis.lrange(KuafuDataUtil.getRedisTeamListKey(fubenId), 0, -1);
		if(teamids == null || teamids.isEmpty()){
			return null;
		}
		Integer targetTeamId= null;
		Map<String,String> targetTeam = null;
		for(String teamId : teamids){
			Map<String,String> team = redis.hgetAll(KuafuDataUtil.getRedisTeamIdKey(teamId));
			String countStr = team.get(GameConstants.REDIS_MORE_FUBEN_COUNT_KEY);
			HuanJingFubenConfig config = moreFubenConfigService.loadById(fubenId);
			if(Integer.parseInt(countStr)==config.getTuijian()){
				continue;
			}
			long zplus = role.getFightAttribute().getZhanLi();
			String zplusStr = team.get(GameConstants.REDIS_MORE_FUBEN_ZPLUS_KEY);
			if(zplus<Long.valueOf(zplusStr)){
				continue;
			}
			targetTeamId=Integer.parseInt(teamId);
			targetTeam = team;
			break;
		}
		
		if (targetTeamId == null) {
			return AppErrorCode.MORE_FUBEN_NO_AVAILABLE_TEAM;
		}
		//标记当前为副本状态
		KuafuServerInfo kuafuServerInfo = KuafuServerInfoManager.getInstance().getKuafuServerInfo(targetTeam.get(GameConstants.REDIS_MORE_FUBEN_SERVERID_KEY), redis);
		if(kuafuServerInfo==null){
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}
		stageControllExportService.changeFuben(userRoleId, true);
		KuafuRoleServerManager.getInstance().bindServer(userRoleId, kuafuServerInfo);
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,
				userRoleId, InnerCmdType.BIND_ROLE_ID_SERVERID, new Object[] {
						ChuanQiConfigUtil.getServerId(), userRoleId });
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,role.getId(), InnerCmdType.MORE_FUBEN_APPLY_ENTER_TEAM, new Object[]{targetTeamId,KuafuDataUtil.getRoleData(role),ChuanQiConfigUtil.getServerId()});
		return null;
	}
	
	/**
	 * 加入多人副本队伍
	 * @param userRoleId
	 * @return
	 */
	public Object[] applyMoreFubenTeam(Long userRoleId,Integer teamId){
		// 是否已经在副本中
		if(stageControllExportService.inFuben(userRoleId)){
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
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
		Map<String,String> team = redis.hgetAll(KuafuDataUtil.getRedisTeamIdKey(teamId.toString()));
		if (team == null || team.isEmpty()) {
			return AppErrorCode.MORE_TEAM_NOT_EXISTS;
		}
		//标记当前为副本状态
		KuafuServerInfo kuafuServerInfo = KuafuServerInfoManager.getInstance().getKuafuServerInfo(team.get(GameConstants.REDIS_MORE_FUBEN_SERVERID_KEY), redis);
		if(kuafuServerInfo==null){
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}
		stageControllExportService.changeFuben(userRoleId, true);
		KuafuRoleServerManager.getInstance().bindServer(userRoleId, kuafuServerInfo);
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,
				userRoleId, InnerCmdType.BIND_ROLE_ID_SERVERID, new Object[] {
						ChuanQiConfigUtil.getServerId(), userRoleId });
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,role.getId(), InnerCmdType.MORE_FUBEN_APPLY_ENTER_TEAM, new Object[]{teamId,KuafuDataUtil.getRoleData(role),ChuanQiConfigUtil.getServerId()});
		return null;
	}
	
	/**
	 * 退出幻境历练副本队伍
	 * @param userRoleId
	 * @return
	 */
	public void leaveMoreFubenTeam(Long userRoleId){
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId, InnerCmdType.MORE_FUBEN_EXIT_FUBEN, new Object[]{userRoleId,ChuanQiConfigUtil.getServerId()});
	}
	
	/**
	 * 踢出幻境副本队伍
	 * @param userRoleId
	 * @param targetRoleId
	 * @return
	 */
	public void removeMoreFubenTeam(Long userRoleId,Long targetRoleId){
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID, userRoleId,InnerCmdType.MORE_FUBEN_KICK_FUBEN, new Object[]{userRoleId,ChuanQiConfigUtil.getServerId(),targetRoleId});
	}
	
//	/**
//	 * 快速加入(客户端做)
//	 * @param userRoleId
//	 * @param fubenId
//	 * @return
//	 */
//	public Object[] quicklyAdd(Long userRoleId,Integer fubenId){
//		String stageId = stageControllExportService.getCurStageId(userRoleId);
//		if(stageId == null){
//			return AppErrorCode.STAGE_IS_NOT_EXIST;
//		}
//		IStage stage = StageManager.getStage(stageId);
//		if(stage == null){
//			return AppErrorCode.STAGE_IS_NOT_EXIST;
//		}
//		
//		Integer teamId = MoreFubenTeamManage.getTeamIdMap().get(userRoleId);
//		if(teamId != null){
//			return AppErrorCode.MORE_TEAM_EXISTS;
//		}
//		
//		HuanJingFubenConfig config = moreFubenConfigService.loadById(fubenId);
//		if(config == null){
//			return AppErrorCode.CONFIG_ERROR;
//		}
//		
//		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
//		// 玩家等级是否满足
//		if(role.getLevel() < config.getLevel()){
//			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
//		}
//		List<MoreFubenTeam> teams = MoreFubenTeamManage.getTeamFubenMap().get(teamId);
//		List<MoreFubenTeam> finalTeams = null;
//		if(teams != null && !teams.isEmpty()){
//			finalTeams = new ArrayList<>();
//			for(MoreFubenTeam team : teams){
//				// 密码暂时不做处理
////				if(team.getPwd() > 0){
////					continue;
////				}
//				// 战斗力
//				if(team.getStrength() > 0 && role.getFightAttribute().getZhanLi() < team.getStrength()){
//					continue;
//				}
//				// 成员是否满员
//				if(team.getMembers().size() >= config.getTuijian()){
//					continue;
//				}
//				finalTeams.add(team);
//			}
//		}
//		if(finalTeams != null && !finalTeams.isEmpty()){
//			int index = Lottery.roll(0, finalTeams.size());
//			// 最终加入的多人副本队伍
//			MoreFubenTeam item = finalTeams.get(index);
//			MoreFubenTeamMember teamMember = new MoreFubenTeamMember();
//			teamMember.setRoleId(userRoleId);
//			teamMember.setMemberName(role.getName());
//			teamMember.setTeam(item);
//			item.addMembers(teamMember,config.getTuijian());
//			MoreFubenTeamManage.addTeamIdMap(userRoleId, item.getTeamId());
//			return new Object[]{item.getTeamId()};
//		}
//		return AppErrorCode.MORE_TEAM_JOIN_ERROR;
//	}
	
	/**
	 * 队员准备
	 * @param userRoleId
	 * @return
	 */
	public Object[] prepareTeamState(Long userRoleId){
		// 队伍是否存在
		Integer teamId = MoreFubenTeamManage.getTeamId(userRoleId);
		if(teamId == null){
			return AppErrorCode.MORE_TEAM_NOT_EXISTS;
		}
		
		MoreFubenTeam team = MoreFubenTeamManage.getTeamMap().get(teamId);
		if(team == null || team.getLeader() == null){
			//队伍不存在
			MoreFubenTeamManage.removeTeam(userRoleId, true);
			return AppErrorCode.MORE_TEAM_NOT_EXISTS;
		}
		// 是否队长
		if(userRoleId.equals(team.getLeader().getRoleId())){
			return AppErrorCode.MORE_TEAM_NO_NEED;
		}
		
		// 多人副本是否存在
		HuanJingFubenConfig config = moreFubenConfigService.loadById(team.getBelongFubenId());
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		MoreFubenTeamMember mftm = team.getMember(userRoleId);
		if(mftm.isPrepare()){
			mftm.setPrepare(false);
		}else{
			mftm.setPrepare(true);
		}
		
		// 如果设置自动，满员自动进入副本场景
		if(team.isAuto() && team.getMembers().size() == config.getTuijian()){
			boolean isAllPrepare = true;
			Long leaderRoleId = team.getLeader().getRoleId();
			for(MoreFubenTeamMember member : team.getMembers()){
				if(!leaderRoleId.equals(member.getRoleId()) && !member.isPrepare()){
					isAllPrepare = false;
					break;
				}
			}
			if(isAllPrepare){
				//全部准备好
				// 副本进入互斥
//				for(Long tmpRoleId : team.getRoleIdList()){
//					if(stageControllExportService.inFuben(tmpRoleId)){
//						return AppErrorCode.FUBEN_IS_IN_FUBEN;
//					}			
//				}
				djs(team);
				return null;
			}
		}
		Object[] obj = new Object[]{teamId,userRoleId,mftm.isPrepare()};
		for (MoreFubenTeamMember member : team.getMembers()) {
			Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_PREPARE, obj,member.getRoleId()};
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(), InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
		return null;
	}
	
	/**
	 * 队长开始多人副本操作
	 * @param userRoleId
	 * @return
	 */
	public Object[] startMoreFuben(Long userRoleId){
		// 副本队伍是否存在
		Integer teamId = MoreFubenTeamManage.getTeamId(userRoleId);
		if(teamId == null){
			return AppErrorCode.MORE_TEAM_NOT_EXISTS;
		}
		
		MoreFubenTeam team = MoreFubenTeamManage.getTeamMap().get(teamId);
		if(team == null){
			return AppErrorCode.MORE_TEAM_NOT_EXISTS;
		}
		
		// 是否是队伍领导
		if(!userRoleId.equals(team.getLeader().getRoleId())){
			return AppErrorCode.TEAM_IS_NOT_LEADER;
		}
		
		// 是否有玩家未准备好
		for(MoreFubenTeamMember member : team.getMembers()){
			if(!userRoleId.equals(member.getRoleId()) && !member.isPrepare()){
				return AppErrorCode.MORE_TEAM_NOT_PREPARED;
			}
		}
		
		// 副本进入互斥
//		for(Long tmpRoleId : team.getRoleIdList()){
//			if(stageControllExportService.inFuben(tmpRoleId)){
//				return AppErrorCode.FUBEN_IS_IN_FUBEN;
//			}			
//		}
		
		// 预开启多人副本定时
		djs(team);
		
		return null;
	}
	
	/**
	 * 5s副本倒计时后正式进入多人副本
	 * @param teamId
	 * @return
	 */
	private void djs(MoreFubenTeam team){
		// 已经进入倒计时，其他玩家不能进入多人副本队伍
		team.setDjs(true);
		BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.B_ENTER_MORE_FUBEN,team.getTeamId());
		busScheduleExportService.schedule(team.getTeamId() + "",GameConstants.MORE_FUBEN_PRE_ENTER, runable, GameConstants.MORE_FUBEN_PRE_DJS + 500,TimeUnit.MILLISECONDS);
		Object[] obj = new Object[]{AppErrorCode.SUCCESS,GameConstants.MORE_FUBEN_PRE_DJS};
		for(MoreFubenTeamMember member : team.getMembers()){
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(), InnerCmdType.MORE_FUBEN_ASK_ROLE_DATA, member.getRoleId());
			Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_PRE_START, obj,member.getRoleId()};
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(), InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
	}
	
	/**
	 * 获取多人副本业务数据
	 * @param userRoleId
	 * @return
	 */
	public Object[] getMoreFubenInfo(Long userRoleId){
		List<MoreFuben> list = moreFubenDao.cacheLoadAll(userRoleId);
		if(list == null || list.isEmpty()){
			return null;
		}
		List<Object> objs = new ArrayList<>();
		long now = GameSystemTime.getSystemMillTime();
		for(MoreFuben fuben : list){
			if(!DatetimeUtil.dayIsToday(fuben.getUpdateTime())){
				fuben.setCount(0);
				fuben.setUpdateTime(now);
				moreFubenDao.cacheUpdate(fuben, userRoleId);
			}
			if(fuben.getCount() > 0){				
				objs.add(fuben.getMoreFubenId());
			}
		}
		if(objs.isEmpty()){
			return null;
		}
		return objs.toArray();
	}
	
	/**
	 * 获取多人副本队伍信息
	 * @param fubenId
	 * @return
	 */
	public Object[] initTeamInfo(Integer fubenId,int beginIndex,int endIndex){
		Redis redis = GameServerContext.getRedis();
		if(redis == null){
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}
		Long sizeL = redis.llen(KuafuDataUtil.getRedisTeamListKey(fubenId));
		int size = sizeL.intValue();
		if(beginIndex>size-1 || beginIndex > endIndex){
			return new Object[]{fubenId,null,beginIndex,size};
		}
		if(endIndex>size-1){
			endIndex = size-1;
		}
		List<String> teamids = redis.lrange(KuafuDataUtil.getRedisTeamListKey(fubenId), beginIndex, endIndex);
		if(teamids == null || teamids.isEmpty()){
			return new Object[]{fubenId,null,beginIndex,size};
		}
		List<Object[]> objs = new ArrayList<>();
		for(String teamId : teamids){
			// 暂时服务器ID不做处理
			Map<String,String> team = redis.hgetAll(KuafuDataUtil.getRedisTeamIdKey(teamId));
			if (team == null || team.isEmpty()) {
				redis.lrem(KuafuDataUtil.getRedisTeamListKey(fubenId), teamId+"");
				continue;
			}
			objs.add(new Object[]{
					team.get(GameConstants.REDIS_MORE_FUBEN_TEAMID_KEY),
					0,
					team.get(GameConstants.REDIS_MORE_FUBEN_TEAMLEADER_KEY),
					team.get(GameConstants.REDIS_MORE_FUBEN_ZPLUS_KEY),
					team.get(GameConstants.REDIS_MORE_FUBEN_COUNT_KEY),
					team.get(GameConstants.REDIS_MORE_FUBEN_LEADER_KEY)});
//			objs.add(new Object[]{team.getTeamId(),0,team.getName(),team.getStrength(),team.getMembers().size()});
		}
		return new Object[]{fubenId,objs.toArray(),beginIndex,size};
	}
//	public Object[] initTeamInfo(Integer fubenId){//旧版跨服用方法，暂时废弃
//		List<MoreFubenTeam> teams = MoreFubenTeamManage.getTeamFubenMap().get(fubenId);
//		if(teams == null || teams.isEmpty()){
//			return null;
//		}
////		List<MoreFubenTeam> subTeams = (List<MoreFubenTeam>) getTeamInfo(currPage,perPage,teams);
////		if(subTeams == null || subTeams.isEmpty()){
////			return null;
////		}
//		List<Object[]> objs = new ArrayList<>();
//		for(MoreFubenTeam team : teams){
//			// 暂时服务器ID不做处理
//			objs.add(new Object[]{team.getTeamId(),0,team.getName(),team.getStrength(),team.getMembers().size()});
//		}
//		return new Object[]{fubenId,objs.toArray()};
//	}
	
	/**
	 * 获取分页组队信息
	 * @param currPage
	 * @param perPage
	 * @param data
	 * @return
	 */
	public List<?> getTeamInfo(int currPage,int perPage,List<?> data){
		currPage = currPage-1;
		int pageCount = getTotalPageCount(data, perPage);
		if(currPage > pageCount){
			return null;
		}
		int startIndex = currPage * perPage;
		int totalCount = data.size();
		int lastPageRecord = totalCount % perPage;
		int endIndex = (startIndex + perPage) > totalCount ? (startIndex + lastPageRecord) : (startIndex + perPage);
		if(endIndex > totalCount){
			return null;
		}
		return data.subList(startIndex, endIndex);
	}
	
	/**
	 * 获取累计页数
	 * @param data
	 * @param perPage
	 * @return
	 */
	private int getTotalPageCount(List<?> data,int perPage){
		int totalCount = data.size();
		int pageCount = totalCount/perPage;
		return ((totalCount % perPage) != 0 ? 1 : 0) + pageCount;
	}
	
	/**
	 * 通过副本ID和玩家ID获取副本单个对象
	 * @param userRoleId
	 * @param fubenId
	 * @return
	 */
	public MoreFuben getMoreFuben(Long userRoleId,int fubenId){
		final Integer id = fubenId;
		List<MoreFuben> fubens = moreFubenDao.cacheLoadAll(userRoleId, new IQueryFilter<MoreFuben>() {
			private boolean stop = false;
			@Override
			public boolean check(MoreFuben fuben) {
				if(fuben.getMoreFubenId().equals(id)){
					stop = true;
					return true;
				}else{
					return false;
				}
			}
			@Override
			public boolean stopped() {
				return stop;
			}
		});
		if(fubens != null && fubens.size() > 0){
			MoreFuben fuben = fubens.get(0);
			if(!DatetimeUtil.dayIsToday(fuben.getUpdateTime())){
				fuben.setCount(0);
				fuben.setUpdateTime(GameSystemTime.getSystemMillTime());
				moreFubenDao.cacheUpdate(fuben, userRoleId);
			}
			return fuben;
		}
		return null;
	}
	
	/**
	 * 正式进入多人副本场景
	 * @param teamId
	 * @return
	 */
	public void enterMoreFuben(Integer teamId){
		
		// 进入的副本队伍是否存在
		MoreFubenTeam team = MoreFubenTeamManage.getTeamByTeamID(teamId);
		if(team == null){
			return;
		}
		
		synchronized (team) {
			team = MoreFubenTeamManage.getTeamByTeamID(teamId);
			if(team == null){
				return;
			}
			
			// 配置出错
			HuanJingFubenConfig config = moreFubenConfigService.loadById(team.getBelongFubenId());
			if(config == null){
				return;
			}
			
			//发送到场景进入地图
			DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(config.getMapId());
			int[] birthXy = dituCoinfig.getRandomBirth();
			Object[] applyEnterData = new Object[]{dituCoinfig.getId(),birthXy[0],birthXy[1], MapType.MORE_FUBEN_MAP, team.getBelongFubenId() ,team.getTeamId()};
			
			Object[] obj = new Object[]{AppErrorCode.SUCCESS,team.getBelongFubenId(),GameSystemTime.getSystemMillTime()+config.getTime()*1000}; 
			for(MoreFubenTeamMember member : team.getMembers()){	
				MoreFubenTeamManage.removeTeam(member.getRoleId(),false);
				if(!memberKuafu(member)){
					continue;
				}
				Object[] msg = new Object[]{ClientCmdType.ENTER_MORE_FUBEN, obj,member.getRoleId()};
				KuafuMsgSender.send2OneKuafuSource(member.getServerId(), InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
				MoreFubenTeamManage.memberReadyEnter(member.getRoleId(), applyEnterData);
				 //活跃度
				KuafuMsgSender.send2OneKuafuSource(member.getServerId(), InnerCmdType.INNER_HUOYUEDU_HUANJING, new Object[]{ ActivityEnum.A18,member.getRoleId()});
				//通知原服玩家已跨服
				KuafuMsgSender.send2OneKuafuSource(member.getServerId(), InnerCmdType.MORE_FUBEN_ENTER_KUAFU, member.getRoleId());
			}
			
			//清除组队信息
			MoreFubenTeamManage.getTeamMap().remove(team.getTeamId());
			MoreFubenTeamManage.getTeamFubenMap().get(team.getBelongFubenId()).remove(team);
			Redis redis = GameServerContext.getRedis();
			if(redis != null){
				redis.del(KuafuDataUtil.getRedisTeamIdKey(teamId+""));
				redis.lrem(KuafuDataUtil.getRedisTeamListKey(team.getBelongFubenId()), teamId+"");
				redis.lrem(KuafuDataUtil.getRedisServerKey(), teamId+"");
			}
			
		}
	}
	/**
	 * 成员跨服处理
	 * @param member
	 */
	private boolean memberKuafu(MoreFubenTeamMember member){
		Object roleData = member.getRoleData();
		Long userRoleId = member.getRoleId();
		if(roleData == null){//如果跨服数据未同步过来，视为进入失败
			KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.MORE_FUBEN_ENTER_FAIL, null);
			ChuanQiLog.error("member userRoleId={} enter more fuben error data is null",userRoleId);
			return false;
		}
		KuafuOnlineUtil.changeSomeoneOnline(userRoleId);
		dataContainer.putData(GameConstants.COMPONENET_KUAFU_DATA, userRoleId+"", roleData);
		return true;
	}
	
	/**
	 * 玩家进入跨服
	 * @param userRoleId
	 */
	public void enterKuafu(Long userRoleId){
		String stageId = stageControllExportService.getCurStageId(userRoleId);
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		KuafuManager.startKuafu(userRoleId);
		// 发送到场景控制中心进入小黑屋地图，以便从跨服服务器出来时走完整流程
		DiTuConfig config = diTuConfigExportService.loadSafeDiTu();
		int[] birthXy = config.getRandomBirth();
		Object[] applyEnterData = new Object[]{config.getId(),birthXy[0],birthXy[1], MapType.KUAFU_SAFE_MAP};
		//传送前加一个无敌状态
		role.getStateManager().add(new NoBeiAttack());
		role.setChanging(true);
		StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		//通知跨服可以进入场景了
		KuafuMsgSender.send2KuafuServer(userRoleId,userRoleId, InnerCmdType.MORE_FUBEN_CAN_ENTER_STAGE, null);
	}
	
	public void syncToSourceServer(Long userRoleId){
		String stageId = publicRoleStateExportService.getRolePublicStageId(userRoleId);
		if(stageId!=null){
			IStage stage = StageManager.getStage(stageId);
			if(stage!=null){
				if(stage.getStageType()==StageType.KUAFU_SAFE_STAGE){
					KuafuManager.startKuafu(userRoleId);
				}else{
					ChuanQiLog.error("error stageType={}",stage.getStageType().toString());
					KuafuMsgSender.send2KuafuServer(userRoleId,userRoleId, InnerCmdType.MORE_FUBEN_REMOVE_FROM_KUAFU, null);
				}	
			}
			
		}
	}
	
	/**
	 * 领取多人副本奖励 
	 * 玩家可手动领取，客户端也可在时间倒计时向服务端发送请求
	 * @param userRoleId
	 * @param fubenId
	 * @return
	 */
	public Object[] getMoreFubenReward(Long userRoleId,Integer fubenId){
		
		MoreFuben fuben = getMoreFuben(userRoleId, fubenId);
		// 判断副本完成状态
		if(fuben == null || fuben.getStatus() != GameConstants.MORE_FUBEN_FINISH_STATUS){
			return AppErrorCode.FUBEN_NOT_FINISH;
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
		
		// 验证传递参数
		HuanJingFubenConfig config = moreFubenConfigService.loadById(fubenId);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		// 是否第一次通关
		boolean isFirst = fuben.getCount() < 1;
		
		// 验证背包
//		if(isFirst && roleBagExportService.checkPutInBag(config.getReward(), userRoleId) != null){
//			return AppErrorCode.BAG_NOEMPTY;
//		}
		fuben.setStatus(0);
		fuben.setCount(fuben.getCount() + 1);
		moreFubenDao.cacheUpdate(fuben, userRoleId);
		if(isFirst){			
			String content = EmailUtil.getCodeEmail(GameConstants.MORE_FUBEN_EMAIL_REWARD, config.getFubenName());
			roleBagExportService.putInBagOrEmail(config.getReward(), userRoleId, GoodsSource.MORE_FUBEN_GET, true,content);
		}
		if(config.getJiangExp() > 0){
			roleBagExportService.incrNumberWithNotify(GoodsCategory.EXP, config.getJiangExp(), userRoleId, LogPrintHandle.GET_MORE_FUBEN, LogPrintHandle.GBZ_MORE_FUBEN);
		} 
		if(config.getJiangMoney() > 0){
			roleBagExportService.incrNumberWithNotify(GoodsCategory.MONEY, config.getJiangMoney(), userRoleId, LogPrintHandle.GET_MORE_FUBEN, LogPrintHandle.GBZ_MORE_FUBEN);
		} 
		// 打印日志
		if(isFirst){			
			JSONArray goods = LogPrintHandle.getLogGoodsParam(config.getReward(), null);
			GamePublishEvent.publishEvent(new MoreFubenRewardLogEvent(userRoleId,role.getName(),goods,config.getJiangExp(),config.getJiangMoney(),fubenId));
		}else{
			GamePublishEvent.publishEvent(new MoreFubenRewardLogEvent(userRoleId,role.getName(),null,config.getJiangExp(),config.getJiangMoney(),fubenId));
		}
		if(fubenId.intValue() == 3){
			//成就
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_DIHUOFUBENCOUNT, 1});
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.FUBEN_ZUDUI, null});
		
		return AppErrorCode.OK;
	}
	
	/**
	 * 上线处理
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		List<MoreFuben> list = moreFubenDao.cacheLoadAll(userRoleId);
		for (MoreFuben moreFuben : list) {
			if(moreFuben.getStatus() == GameConstants.MORE_FUBEN_FINISH_STATUS){
				moreFuben.setStatus(0);
				moreFuben.setCount(moreFuben.getCount() + 1);
				moreFubenDao.cacheUpdate(moreFuben, userRoleId);
				
				HuanJingFubenConfig config = moreFubenConfigService.loadById(moreFuben.getMoreFubenId());
				String title = EmailUtil.getCodeEmail(GameConstants.FUBEN_EMAIL_REWARD_TITLE);
				String content = EmailUtil.getCodeEmail(GameConstants.MORE_FUBEN_EMAIL_REWARD_RESEND, config.getFubenName());
				Map<String,Integer> gift = new HashMap<>();
				if(moreFuben.getCount() <= 1){
					ObjectUtil.mapAdd(gift, config.getReward());
				}
				if(config.getJiangExp() > 0){
					int value = config.getJiangExp();
					if(gift.containsKey(ModulePropIdConstant.EXP_GOODS_ID)){
						value += gift.get(ModulePropIdConstant.EXP_GOODS_ID);
					}
					gift.put(ModulePropIdConstant.EXP_GOODS_ID, value);
				} 
				if(config.getJiangMoney() > 0){
					int value = config.getJiangMoney();
					if(gift.containsKey(ModulePropIdConstant.MONEY_GOODS_ID)){
						value += gift.get(ModulePropIdConstant.MONEY_GOODS_ID);
					}
					gift.put(ModulePropIdConstant.MONEY_GOODS_ID, value);
				} 
				//发送奖励邮件
				String[] attachments = EmailUtil.getAttachments(gift);
				for (String attachment : attachments) {
					emailExportService.sendEmailToOne(userRoleId,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachment);
				}
			}
		}
	}
	/**
	 * 离线处理
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId){
		MoreFubenTeamManage.leaveTeam(userRoleId);
		BusMsgSender.send2BusInner(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.MORE_FUBEN_OFFLINE_HANDLE_SYN, userRoleId);
	}
	
	/**
	 * 异步离线处理
	 * @param userRoleId
	 */
	public void offlineHandleSyn(Long userRoleId){
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId, InnerCmdType.MORE_FUBEN_OFFLINE_HANDLE, userRoleId);
	}
	/**
	 * 离线处理(跨服)
	 * @param userRoleId
	 */
	public void offlineHandleKF(Long userRoleId){
//		dataContainer.removeData(GameModType.STAGE_CONTRAL, userRoleId.toString());
		Integer teamId = MoreFubenTeamManage.getTeamId(userRoleId);
		if(teamId == null){
			return;
		}
		// 多人副本未开始，清除多人副本管理对象中的对象数据
		MoreFubenTeam mft = MoreFubenTeamManage.getTeamMap().get(teamId);
		if(mft == null){
			MoreFubenTeamManage.removeTeam(userRoleId, false);
		}else{
			mft.removeMember(userRoleId);
			Redis redis = GameServerContext.getRedis();
			if(redis != null){
				if(mft.getMembers().isEmpty()){
					if(mft.isDjs()){
						busScheduleExportService.cancelSchedule(mft.getTeamId() + "", GameConstants.MORE_FUBEN_PRE_ENTER);
					}
					MoreFubenTeamManage.getTeamMap().remove(teamId);
					if(redis != null){
						redis.del(KuafuDataUtil.getRedisTeamIdKey(teamId+""));
						redis.lrem(KuafuDataUtil.getRedisTeamListKey(mft.getBelongFubenId()), teamId+"");
						redis.lrem(KuafuDataUtil.getRedisServerKey(), teamId+"");
					}
				}else{
					if(redis != null){
						redis.hmset(KuafuDataUtil.getRedisTeamIdKey(teamId+""), mft.getSaveInfo());
					}
				}
			}
		}
	}
	
	/**
	 * 退出多人副本
	 * @param stageId
	 */
	public Object[] exitFuben(Long userRoleId){
		if(!KuafuConfigPropUtil.isKuafuServer()){
			return null;//非跨服不处理该指令
		}
		// 是否在多人副本中
		if(!stageControllExportService.inFuben(userRoleId)){
			return AppErrorCode.FUBEN_NOT_IN_FUBEN;
		}
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.S_EXIT_MORE_FUBEN, null);
		return null;
	}
	
	/**
	 * 完成副本清场
	 * @param stageId
	 * @param type
	 */
	public void challengeOver(Long userRoleId,Integer fubenId){
		// 更改副本状态
		MoreFuben moreFuben = getMoreFuben(userRoleId, fubenId);
		if(moreFuben == null){
			moreFuben = createMoreFuben(userRoleId, fubenId);
		}else{
			moreFuben.setStatus(GameConstants.MORE_FUBEN_FINISH_STATUS);
			moreFuben.setUpdateTime(GameSystemTime.getSystemMillTime());
			moreFubenDao.cacheUpdate(moreFuben, userRoleId);
		}
	}
	
	/**
	 * 创建多人副本
	 * @param userRoleId
	 * @param fubenId
	 * @return
	 */
	private MoreFuben createMoreFuben(Long userRoleId,Integer fubenId){
		MoreFuben fuben = new MoreFuben();
		fuben.setUserRoleId(userRoleId);
		fuben.setId(IdFactory.getInstance().generateId(ServerIdType.FUBEN));
		fuben.setMoreFubenId(fubenId);
		fuben.setCount(0);
		fuben.setStatus(GameConstants.MORE_FUBEN_FINISH_STATUS);
		fuben.setUpdateTime(GameSystemTime.getSystemMillTime());
		moreFubenDao.cacheInsert(fuben, userRoleId);
		return fuben;
	}
	
	/**
	 * 创建多人副本队伍
	 * @param teamLeader
	 * @return
	 */
	public Object[] createMoreFubenTeamKuafu(Integer belongFubenId,Long strength,Boolean isAuto,Long userRoleId,Object[] roleData){
		// 是否已经有队伍
		if(MoreFubenTeamManage.getTeamId(userRoleId) != null){
			return AppErrorCode.MORE_TEAM_EXISTS;
		}
		
		// 是否已经在副本中
//		if(stageControllExportService.inFuben(userRoleId)){
//			return AppErrorCode.FUBEN_IS_IN_FUBEN;
//		}
		
		// 所属副本ID是否存在 
		HuanJingFubenConfig config = moreFubenConfigService.loadById(belongFubenId);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
				
		// 当前副本队伍已达上限
		List<MoreFubenTeam> teams = MoreFubenTeamManage.getTeamFubenMap().get(belongFubenId);
		if(teams != null && teams.size() > GameConstants.MORE_FUBEN_LIMIT){
			return AppErrorCode.MORE_FUBEN_TEAM_LIMIT_ERROR;
		}
		
		MoreFubenTeamMember teamLeader = createTeamMember(roleData);
		MoreFubenTeam team = new MoreFubenTeam(teamLeader);
		Redis redis = GameServerContext.getRedis();
		if(redis == null){
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}
		int teamId = KuafuDataUtil.getRedisTeamId(redis.generateId(GameConstants.REDIS_MORE_FUBEN_TEAM_ID, GameConstants.REDIS_MORE_FUBEN_TEAM_ID));
		team.setTeamId(teamId);
		if(strength != null){
			team.setStrength(strength);
		}
//		if(pwd != null){
//			team.setPwd(pwd);
//		}
		if(belongFubenId != null){
			team.setBelongFubenId(belongFubenId);
		}
		team.setAuto(isAuto);
		teamLeader.setTeam(team);
		MoreFubenTeamManage.addMoreFubenTeamMap(belongFubenId, team);
		MoreFubenTeamManage.addTeamMap(teamId, team);
		MoreFubenTeamManage.addTeamIdMap(userRoleId, teamLeader);
		
		redis.hmset(KuafuDataUtil.getRedisTeamIdKey(teamId+""), team.getSaveInfo());
		redis.lpush(KuafuDataUtil.getRedisTeamListKey(belongFubenId), teamId+"");
		redis.lpush(KuafuDataUtil.getRedisServerKey(), teamId+"");
		return new Object[]{AppErrorCode.SUCCESS,belongFubenId,teamId,strength,isAuto};
	}
	
	/**
	 * 加入多人副本队伍(跨服)
	 * @param userRoleId
	 * @return
	 */
	public Object[] applyMoreFubenTeamKF(Long userRoleId,Integer teamId,Object[] roleData){
		// 是否已经加入其它队伍
		Integer tmpTeamId = MoreFubenTeamManage.getTeamId(userRoleId);
		if(tmpTeamId != null){
			return AppErrorCode.MORE_TEAM_OTHER_IN;
		}
		
		// 是否已经在副本中
//		if(stageControllExportService.inFuben(userRoleId)){
//			return AppErrorCode.FUBEN_IS_IN_FUBEN;
//		}

		// 加入的多人副本队伍是否存在
		MoreFubenTeam mftTarget = MoreFubenTeamManage.getTeamByTeamID(teamId);
		if(mftTarget == null){
			return AppErrorCode.MORE_TEAM_NOT_EXISTS;
		}
			
		// 加入的多人副本队伍是否进入倒计时阶段
		if(mftTarget.isDjs()){
			return AppErrorCode.MORE_FUBEN_DJS;
		}
		
		// 玩家是否在目标队伍中
		if(mftTarget.isTeamMember(userRoleId)){
			return AppErrorCode.MORE_TEAM_IN;
		}
		
		// 玩家等级是否满足
		HuanJingFubenConfig config = moreFubenConfigService.loadById(mftTarget.getBelongFubenId());
		if((Integer)roleData[4] < config.getLevel()){
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		
		// 队伍人数是否达到上限
		if(mftTarget.getMembers().size() >= config.getTuijian()){
			return AppErrorCode.MORE_TEAM_PEOPLE_COUNT;
		}
		// 密码是否正确
//		if(mftTarget.getPwd() > 0 && mftTarget.getPwd() != pwd){
//			return AppErrorCode.MORE_TEAM_PWD_ERROR;
//		}
		long zplus = LongUtils.obj2long(roleData[3]);
		// 战力是否达到
		if(mftTarget.getStrength() > 0 &&zplus < mftTarget.getStrength()){
			return AppErrorCode.MORE_TEAM_STRENGTH_LESS;
		}
		// 加入队伍
		List<MoreFubenTeamMember> members = mftTarget.getMembers();//原先队员

		MoreFubenTeamMember teamMember = createTeamMember(roleData);
		teamMember.setTeam(mftTarget);
		mftTarget.addMembers(teamMember,config.getTuijian());
		MoreFubenTeamManage.addTeamIdMap(userRoleId, teamMember);
		Object[] obj = new Object[]{teamId,teamMember.getMemberVo()};
		for (MoreFubenTeamMember member : members) {
			Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_TEAM_ENTER,obj,member.getRoleId()};//封装转发数据
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(), InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
		Redis redis = GameServerContext.getRedis();
		if(redis != null){
			redis.hmset(KuafuDataUtil.getRedisTeamIdKey(teamId+""), mftTarget.getSaveInfo());
		}
		
		return new Object[]{AppErrorCode.SUCCESS,teamId,mftTarget.getLeader().getRoleId(),mftTarget.getBelongFubenId(),mftTarget.getStrength(),mftTarget.isAuto(),mftTarget.getTeamInfoArr()};
	}
	
	/**
	 * 退出幻境历练副本队伍(跨服)
	 * @param userRoleId
	 * @return
	 */
	public Object[] leaveMoreFubenTeamKF(Long userRoleId){
		// 队伍是否存在
		Integer teamId = MoreFubenTeamManage.getTeamId(userRoleId);
		if(teamId == null){
			return AppErrorCode.TEAM_IS_NOT_EXIST;
		}
		MoreFubenTeam mft = MoreFubenTeamManage.getTeamMap().get(teamId);
		if(mft == null){
			MoreFubenTeamManage.removeTeam(userRoleId, true);
			return AppErrorCode.TEAM_IS_NOT_EXIST;
		}
		
		// 主动离开
		mft.removeMember(userRoleId);
		Redis redis = GameServerContext.getRedis();
		if(mft.getMembers().isEmpty()){
			if(mft.isDjs()){
				busScheduleExportService.cancelSchedule(mft.getTeamId() + "", GameConstants.MORE_FUBEN_PRE_ENTER);
			}
			MoreFubenTeamManage.getTeamMap().remove(teamId);
			if(redis != null){
				redis.del(KuafuDataUtil.getRedisTeamIdKey(teamId+""));
				redis.lrem(KuafuDataUtil.getRedisTeamListKey(mft.getBelongFubenId()), teamId+"");
				redis.lrem(KuafuDataUtil.getRedisServerKey(), teamId+"");
			}
		}else{
			if(redis != null){
				redis.hmset(KuafuDataUtil.getRedisTeamIdKey(teamId+""), mft.getSaveInfo());
			}
		}
		return null;
	}
	
	/**
	 * 踢出幻境副本队伍(跨服)
	 * @param userRoleId
	 * @param targetRoleId
	 * @return
	 */
	public Object[] removeMoreFubenTeamKF(Long userRoleId,Long targetRoleId){
		// 队伍是否存在
		Integer teamId = MoreFubenTeamManage.getTeamId(userRoleId);
		if(teamId == null){
			return AppErrorCode.MORE_TEAM_NOT_EXISTS;
		}
		MoreFubenTeam mft = MoreFubenTeamManage.getTeamMap().get(teamId);
		// 领队踢出
		if(!userRoleId.equals(mft.getLeader().getRoleId())){
			return AppErrorCode.TEAM_IS_NOT_LEADER;
		}
		if(!mft.isTeamMember(targetRoleId)){
			// 没有该玩家，不允许踢出
			return AppErrorCode.MORE_TEAM_NOT_IN;
		}
		MoreFubenTeamMember member = mft.removeMember(targetRoleId);
		Redis redis = GameServerContext.getRedis();
		if(redis != null){
			redis.hmset(KuafuDataUtil.getRedisTeamIdKey(teamId+""), mft.getSaveInfo());
		}
		return new Object[]{AppErrorCode.SUCCESS,teamId,member.getRoleId()};
	}
	
	/**
	 * 队长更改幻境历练副本队伍进入需求战力
	 * @param userRoleId
	 * @param strength
	 * @return
	 */
	public Object[] teamChangeStrength(Long userRoleId,Long strength){
		// 队伍是否存在
		Integer teamId = MoreFubenTeamManage.getTeamId(userRoleId);
		if(teamId == null){
			return AppErrorCode.TEAM_IS_NOT_EXIST;
		}
		MoreFubenTeam mft = MoreFubenTeamManage.getTeamMap().get(teamId);
		if(!userRoleId.equals(mft.getLeader().getRoleId())){
			return AppErrorCode.TEAM_IS_NOT_LEADER;
		}
		mft.setStrength(strength);
		Object[] obj = new Object[]{teamId,strength};
		for(MoreFubenTeamMember member : mft.getMembers()){	
			Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_CHANGE_STRENGTH, obj,member.getRoleId()};
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(), InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
		return null;
	}
	
	/**
	 * 队长更改幻境历练副本队伍是否满员自动挑战
	 * @param userRoleId
	 * @param isAuto
	 * @return
	 */
	public Object[] teamSetAuto(Long userRoleId){
		// 队伍是否存在
		Integer teamId = MoreFubenTeamManage.getTeamId(userRoleId);
		if(teamId == null){
			return AppErrorCode.TEAM_IS_NOT_EXIST;
		}
		MoreFubenTeam mft = MoreFubenTeamManage.getTeamMap().get(teamId);
		if(!userRoleId.equals(mft.getLeader().getRoleId())){
			return AppErrorCode.TEAM_IS_NOT_LEADER;
		}
		if(mft.isAuto()){
			mft.setAuto(false);
		}else{
			mft.setAuto(true);
		}
		Object[] obj = new Object[]{teamId,mft.isAuto()};
		for(MoreFubenTeamMember member : mft.getMembers()){			
			Object[] msg = new Object[]{ClientCmdType.MORE_FUBEN_SET_AUTO, obj,member.getRoleId()};
			KuafuMsgSender.send2OneKuafuSource(member.getServerId(), InnerCmdType.INNER_KF_TO_ONE_CLIENT, msg);
		}
		return null;
	}
	/**
	 * 发送玩家数据到跨服
	 * @param userRoleId
	 */
	public void sendRoleData(Long userRoleId){
		String stageId = stageControllExportService.getCurStageId(userRoleId);
		if(ObjectUtil.strIsEmpty(stageId)){
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null || stage.isCopy()){
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		Object roleData = RoleFactory.createKuaFuRoleData(role);
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId, InnerCmdType.MORE_FUBEN_SEND_ROLE_DATA, new Object[]{roleData,userRoleId});
	}
	
	/**
	 * 接收玩家数据到跨服
	 * @param userRoleId
	 */
	public void reciveRoleData(Object[] roleData){
		Long userRoleId = LongUtils.obj2long(roleData[1]);
		MoreFubenTeamMember member = MoreFubenTeamManage.getTeamMember(userRoleId);
		if(member!=null){
			member.setRoleData(roleData);
		}
	}
	
	/**
	 * 离开了队伍
	 * @param userRoleId
	 */
	public void leaveTeam(Long userRoleId){
		KuafuManager.removeKuafu(userRoleId);
		KuafuRoleServerManager.getInstance().removeBind(userRoleId);
		stageControllExportService.changeFuben(userRoleId, false);
		if(MoreFubenTeamManage.leaveTeam(userRoleId)){
			BusMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_EXIT, AppErrorCode.OK);
		}
	}
	
	/**
	 * 可以进入跨服场景了
	 * @param userRoleId
	 */
	public void enterKuafuMap(Long userRoleId){
		Object[] data = MoreFubenTeamManage.getEnterData(userRoleId);
		if(data == null){
			KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.MORE_FUBEN_ENTER_FAIL, null);
			ChuanQiLog.error("member userRoleId={} enter more fuben map error data is null",userRoleId);
			return;
		}

		RoleState roleState = dataContainer.getData(GameModType.STAGE_CONTRAL, userRoleId.toString());
		if(null == roleState){
			roleState = new RoleState(userRoleId);
			dataContainer.putData(GameModType.STAGE_CONTRAL, userRoleId.toString(), roleState);
		}
		//发送到场景进入地图
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, data);
		KuafuMsgSender.send2KuafuSource(userRoleId, InnerCmdType.MORE_FUBEN_AFTER_ENTER_STAGE, null);
	}
	
	/**
	 * 离开副本
	 * @param userRoleId
	 */
	public void leaveFuben(Long userRoleId){
		leaveTeam(userRoleId);
//		BusMsgSender.send2One(userRoleId, ClientCmdType.MORE_FUBEN_EXIT, AppErrorCode.OK);
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_APPLY_LEAVE_STAGE, null);
	}
	
	/**
	 * 进入多人本失败
	 * @param userRoleId
	 */
	public void enterFubenFail(Long userRoleId){
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.S_APPLY_LEAVE_STAGE, null);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ENTER_MORE_FUBEN, AppErrorCode.KUAFU_ENTER_FAIL);
		leaveTeam(userRoleId);
		ChuanQiLog.error("进入多人本失败，id：{}",userRoleId);
	}
	
	public void enterTeamSuccess(Long userRoleId,Integer teamId,Integer fubenId){
		if(userRoleId == null || teamId == null || fubenId == null){
			return;
		}
		MoreFubenTeamManage.enterTeam(userRoleId, teamId, fubenId);
	}
	/**
	 * 邀请
	 * @param userRoleId
	 */
	public void moreFubenYaoqing(Long userRoleId){
		Object[] msg = MoreFubenTeamManage.getYaoQingMsg(userRoleId);
		if(msg != null){
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			if(role != null){
				msg[0] = role.getName();
				BusMsgSender.send2All(ClientCmdType.MORE_FUBEN_YAOQING_OTHER, msg);
			}
		}
	}
	
	/**
	 * 离开副本(跨服)
	 * @param userRoleId
	 */
	public void leaveFubenKF(Long userRoleId){
		dataContainer.removeData(GameModType.STAGE_CONTRAL, userRoleId.toString());
		
		KuafuSessionManager.removeUserRoleId(userRoleId);
		KuafuOnlineUtil.changeSomeoneOffline(userRoleId);
	}
	
	/**跨服启动处理*/
	public void kfStartHandle(){
		Redis redis = GameServerContext.getRedis();
		if(redis == null){
			return;
		}
		List<String> teamids = redis.lrange(KuafuDataUtil.getRedisServerKey(), 0, -1);
		if(teamids == null || teamids.isEmpty()){
			return;
		}
		for (String teamId : teamids) {
			redis.del(KuafuDataUtil.getRedisTeamIdKey(teamId));
		}
		redis.del(KuafuDataUtil.getRedisServerKey());
	}
	
	public void startHandleOutTimeTeamSchedule(){
		BusTokenRunable runable = new BusTokenRunable(GameConstants.DEFAULT_ROLE_ID, InnerCmdType.HANDLE_MORE_FUBEN_TIME_OUT_TEAM,null);
		busScheduleExportService.schedule("mf_"+ChuanQiConfigUtil.getServerId(),GameConstants.MORE_FUBEN_TIME_OUT_TEAM, runable, GameConstants.MORE_FUBEN_TIME_OUT_TEAM_TIME,TimeUnit.SECONDS);
	}
	
	public void handleOutTimeTeam(){
		Redis redis = GameServerContext.getRedis();
		if(redis == null){
			return ;
		}
		Set<String> teamIdKeys = redis.keys(KuafuDataUtil.REDIS_TEAM_ID_KEY+"*");
		if(teamIdKeys == null || teamIdKeys.isEmpty()){
			return ;
		}
		for(String teamIdKey : teamIdKeys){
			try{
				String teamId = teamIdKey.substring(KuafuDataUtil.REDIS_TEAM_ID_KEY.length(), teamIdKey.length());
				Map<String,String> teamInfo = redis.hgetAll(KuafuDataUtil.getRedisTeamIdKey(teamId));
				if (teamInfo == null || teamInfo.isEmpty()) {
					continue;
				}
				String serverId = teamInfo.get(GameConstants.REDIS_MORE_FUBEN_SERVERID_KEY);
				if(serverId!=null && serverId.equals(ChuanQiConfigUtil.getServerId())){
					MoreFubenTeam team = MoreFubenTeamManage.getTeamByTeamID(Integer.parseInt(teamId));
					if(team==null){
						redis.del(KuafuDataUtil.getRedisTeamIdKey(String.valueOf(teamId)));
						String fubenId =  teamInfo.get(GameConstants.REDIS_MORE_FUBEN_FUBENID_KEY);
						redis.lrem(KuafuDataUtil.getRedisTeamListKey(Integer.parseInt(fubenId)), String.valueOf(teamId));
						redis.lrem(KuafuDataUtil.getRedisServerKey(), String.valueOf(teamId));
					}else{
						long liveTime  = GameSystemTime.getSystemMillTime()-team.getStartTime();
						if(liveTime>60*60*1000){
							for(MoreFubenTeamMember member : team.getMembers()){	
								team.removeMember(member.getRoleId());
							}
							redis.del(KuafuDataUtil.getRedisTeamIdKey(String.valueOf(teamId)));
							redis.lrem(KuafuDataUtil.getRedisTeamListKey(team.getBelongFubenId()), teamId+"");
							redis.lrem(KuafuDataUtil.getRedisServerKey(), String.valueOf(teamId));
						}
					}
				}
			}catch (Exception e) {
				ChuanQiLog.error("handleOutTimeTeam morefuben error teamIdKey ={}",teamIdKey);
			}
		}
		startHandleOutTimeTeamSchedule();
	}
}
