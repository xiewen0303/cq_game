package com.junyou.bus.kuafuluandou.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Tuple;

import com.alibaba.fastjson.JSON;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.kuafu_dianfeng.constants.KuaFuDianFengConstants;
import com.junyou.bus.kuafu_dianfeng.vo.DianFengPlayerVO;
import com.junyou.bus.kuafuluandou.configure.DaLuanDouHuoDongBiaoConfig;
import com.junyou.bus.kuafuluandou.configure.DaLuanDouHuoDongBiaoConfigExportService;
import com.junyou.bus.kuafuluandou.configure.DaLuanDouJiangLiBiaoConfig;
import com.junyou.bus.kuafuluandou.configure.DaLuanDouJiangLiBiaoConfigExportService;
import com.junyou.bus.kuafuluandou.constants.KuaFuDaLuanDouConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafu.manager.KuafuServerInfoManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.RoleFactory;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.model.state.NoBeiAttack;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;

/**
 * @author zhongdian
 * 2016-2-17 下午3:34:33
 */
@Service
public class KuaFuDaLuanDouService {

	
	@Autowired
    private DaLuanDouHuoDongBiaoConfigExportService daLuanDouHuoDongBiaoConfigExportService;
	@Autowired
	private DaLuanDouJiangLiBiaoConfigExportService daLuanDouJiangLiBiaoConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	
	/**
	 * 请求报名状态
	 * @param userRoleId
	 * @return
	 */
	public boolean getBaoMingInfo(Long userRoleId){
		Redis redis = GameServerContext.getRedis();
		String member = userRoleId.toString();
		Long isBaoMing = redis.zrevrank(RedisKey.KUAFU_LUANDOU_BAOMING_DATA_KEY, member);
		if(isBaoMing != null){//玩家存在排名，说明已经报过名了
			return true;
		}
		return false;
	}
	/**
	 * 请求海选状态
	 * @param userRoleId
	 * @return
	 */
	public int getHaiXuanInfo(Long userRoleId){
		Redis redis = GameServerContext.getRedis();
		return getKuaFuFangJianId(redis, userRoleId.toString());
	}
	
	/**
	 * 报名参加大乱斗
	 * @param userRoleId
	 * @return
	 */
	public Object[] baoMingDaLuanDou(Long userRoleId){
		DaLuanDouHuoDongBiaoConfig bmConfig = daLuanDouHuoDongBiaoConfigExportService.loadById(KuaFuDaLuanDouConstants.BAOMING_ID);
		if(bmConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//判断是否在报名时间内
		if(!isHdTime(bmConfig)){
			return AppErrorCode.LD_HD_TIME_BAOMING_TIME_NO;
		}
		//判断玩家等级和战力是否达到报名的条件
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		RoleBusinessInfoWrapper roleInfo = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
		if(role == null || roleInfo == null){
			return AppErrorCode.ROLE_IS_NOT_ONLINE;
		}
		if(role.getLevel() < bmConfig.getNeedlevel() || roleInfo.getCurFighter() < bmConfig.getNeedzplus()){
			return AppErrorCode.LD_HD_BAOMING_TIAOJIAN_NO;
		}
		Redis redis = GameServerContext.getRedis();
		String member = userRoleId.toString();
		Long isBaoMing = redis.zrevrank(RedisKey.KUAFU_LUANDOU_BAOMING_DATA_KEY, member);
		if(isBaoMing != null){//玩家存在排名，说明已经报过名了
			return AppErrorCode.LD_HD_BAOMING_YI_BAO;
		}
		redis.zadd(RedisKey.KUAFU_LUANDOU_BAOMING_DATA_KEY, roleInfo.getCurFighter(), member);
		
		return AppErrorCode.OK ;
	}
	
	/**
	 * 海选赛，给入选的和淘汰的分别发送邮件
	 */
	public void haixuansai(){
		DaLuanDouHuoDongBiaoConfig hxConfig = daLuanDouHuoDongBiaoConfigExportService.loadById(KuaFuDaLuanDouConstants.HAIXUANSAI_ID);
		if(hxConfig == null){
			return;
		}
		//判断是否在报名时间内
		if(!isHdTime(hxConfig)){
			return;
		}
		DaLuanDouJiangLiBiaoConfig slConfig = daLuanDouJiangLiBiaoConfigExportService.loadById(KuaFuDaLuanDouConstants.HAIXUAN_SHENGLI);
		DaLuanDouJiangLiBiaoConfig sbConfig = daLuanDouJiangLiBiaoConfigExportService.loadById(KuaFuDaLuanDouConstants.HAIXUAN_SHIBAI);
		if(slConfig == null || sbConfig == null){
			return;
		}
		String slItem = slConfig.getJiangitem()+":1";//海选胜利奖励
		String sbItem = sbConfig.getJiangitem()+":1";//海选失败奖励
		Redis redis = GameServerContext.getRedis();
		//报名总人数
		long count = redis.zcount(RedisKey.KUAFU_LUANDOU_BAOMING_DATA_KEY);
		String title = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_LUANDOU_HAIXUAN_SHENGLI_TITLE);
		if(count > KuaFuDaLuanDouConstants.HAIXUAN_MINGE){
			Set<Tuple> slSet = redis.zrevrangeWithScore(RedisKey.KUAFU_LUANDOU_BAOMING_DATA_KEY, 0, KuaFuDaLuanDouConstants.HAIXUAN_MINGE-1);
			
			for (Tuple tuple : slSet) {//遍历集合，发送海选胜利邮件
				long userRoleId = Long.parseLong(tuple.getElement());
				try {
					RoleWrapper roleWrapper = roleExportService.getUserRoleFromDb(userRoleId);
					ChuanQiLog.info("kuafu daluandou haixuan shengli userRolId={}  isBenfu={}", new Object[] { userRoleId, roleWrapper != null });
					if (roleWrapper != null) {
						// 是本服玩家发邮件
						emailExportService.sendEmailToOne(userRoleId, title,EmailUtil.getCodeEmail(AppErrorCode.KUAFU_LUANDOU_HAIXUAN_SHENGLI), GameConstants.EMAIL_TYPE_SINGLE, slItem);
					}
				} catch (Exception ex) {
					ChuanQiLog.info("kuafu daluandou haixuan shengli send reward error userRolId={} ", userRoleId);
				}
			}
			
			Set<Tuple> sbSet = redis.zrevrangeWithScore(RedisKey.KUAFU_LUANDOU_BAOMING_DATA_KEY, KuaFuDaLuanDouConstants.HAIXUAN_MINGE, -1);
			for (Tuple tuple : sbSet) {//遍历集合，发送海选失败邮件
				long userRoleId = Long.parseLong(tuple.getElement());
				try {
					RoleWrapper roleWrapper = roleExportService.getUserRoleFromDb(userRoleId);
					ChuanQiLog.info("kuafu daluandou haixuan shibai userRolId={}  isBenfu={}", new Object[] { userRoleId, roleWrapper != null });
					if (roleWrapper != null) {
						// 是本服玩家发邮件
						emailExportService.sendEmailToOne(userRoleId, title, EmailUtil.getCodeEmail(AppErrorCode.KUAFU_LUANDOU_HAIXUAN_SHIBAI), GameConstants.EMAIL_TYPE_SINGLE, sbItem);
					}
				} catch (Exception ex) {
					ChuanQiLog.info("kuafu daluandou haixuan shibai send reward error userRolId={} ", userRoleId);
				}
			}
		}else{//如果小于800个人，则全部入选，发胜利奖励
			Set<Tuple> set = redis.zrevrangeWithScore(RedisKey.KUAFU_LUANDOU_BAOMING_DATA_KEY, 0, -1);
			for (Tuple tuple : set) {//遍历集合，发送海选胜利邮件
				long userRoleId = Long.parseLong(tuple.getElement());
				try {
					RoleWrapper roleWrapper = roleExportService.getUserRoleFromDb(userRoleId);
					ChuanQiLog.info("kuafu daluandou haixuan shengli userRolId={}  isBenfu={}", new Object[] { userRoleId, roleWrapper != null });
					if (roleWrapper != null) {
						// 是本服玩家发邮件
						emailExportService.sendEmailToOne(userRoleId, title, EmailUtil.getCodeEmail(AppErrorCode.KUAFU_LUANDOU_HAIXUAN_SHENGLI), GameConstants.EMAIL_TYPE_SINGLE, slItem);
					}
				} catch (Exception ex) {
					ChuanQiLog.info("kuafu daluandou haixuan shengli send reward error userRolId={} ", userRoleId);
				}
			}
		}
		//根据跨服服务器个数，分配乱斗房间所在的跨服服务器
		createFangJian();
		return;
	}
	
	/**
	 * 海选结束之后，根据跨服服务器的个数不同，创建打架房间(只在1服执行)
	 */
	public void createFangJian(){
		//上正服的时候要解掉注释 
		String pServerId = ChuanQiConfigUtil.getPlatformServerId();
		if(PlatformConstants.isQQ()){
			if(Integer.parseInt(pServerId) != 1 && Integer.parseInt(pServerId) != 118){
				return;
			}
		}else{
			if(Integer.parseInt(pServerId) != 1){
				return;
			}
		}
		
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("redis 没有开启");
			return;
		}
		Set<String> serverIdSets = redis.zrange(RedisKey.KUAFU_SERVER_LIST_KEY,	0, -1);
		if(serverIdSets!=null){
			//8组跨服，每个跨服分配一个房间
			if(serverIdSets.size() >= KuaFuDaLuanDouConstants.KUAFU_COUNT_8){
				int id = 1;
				for(String e:serverIdSets){
					if(id > KuaFuDaLuanDouConstants.KUAFU_COUNT_8){
						break;
					}
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
				}
			}else if(serverIdSets.size() < KuaFuDaLuanDouConstants.KUAFU_COUNT_8 && serverIdSets.size() >= KuaFuDaLuanDouConstants.KUAFU_COUNT_4){
				//多于4组少于8组跨服，取前4个跨服，每个跨服分配二个房间
				int id = 1;
				for(String e:serverIdSets){
					if(id > KuaFuDaLuanDouConstants.KUAFU_COUNT_8){
						break;
					}
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
				}
			
			}else if(serverIdSets.size() < KuaFuDaLuanDouConstants.KUAFU_COUNT_4 && serverIdSets.size() >= KuaFuDaLuanDouConstants.KUAFU_COUNT_2){
				//多于2组少于4组跨服，取前2个跨服，每个跨服分配四个房间
				int id = 1;
				for(String e:serverIdSets){
					if(id > KuaFuDaLuanDouConstants.KUAFU_COUNT_8){
						break;
					}
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
				}
			}else{//只有1个跨服,在1个服务上创建8个房间
				int id = 1;
				for(String e:serverIdSets){
					if(id > KuaFuDaLuanDouConstants.KUAFU_COUNT_8){
						break;
					}
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
					redis.set(RedisKey.getRedisKuafuLuanDouFangJianKey(id), e);
					id++;
				}
			}
			
		}
		
		
		
	}
	/**
	 * 申请进入跨服乱斗房间
	 * @param userRoleId
	 * @return
	 */
	public Object[] enterKuafuLuanDou(Long userRoleId) {
		//判断时间
		DaLuanDouHuoDongBiaoConfig hxConfig = daLuanDouHuoDongBiaoConfigExportService.loadById(KuaFuDaLuanDouConstants.LUANDOU_ID);
		if(hxConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//判断是否在活动时间内
		if(!isHdTime(hxConfig)){
			return AppErrorCode.LD_HD_DALUANDOU_TIME_NO;
		}
		// 判断是否在副本中
		if (stageControllExportService.inFuben(userRoleId)) {
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
		}
		// 判断当前跨服是否可用
		if (!KuafuConfigUtil.isKuafuAvailable()) {
			return AppErrorCode.KUAFU_NO_CONNECTION;// 跨服连接失败
		}
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("redis 没有开启");
			return AppErrorCode.CONFIG_ERROR;
		}
		//获取玩家的房间号1-8
		int fj = getKuaFuFangJianId(redis, userRoleId.toString());
		if(fj <= 0){
			return AppErrorCode.LD_HD_DALUANDOU_FANGJIAN_NO;
		}
		//获取房间所在跨服服务器
		String kuafuLuanDouFangJianKey = RedisKey.getRedisKuafuLuanDouFangJianKey(fj);
		String serverId = redis.get(kuafuLuanDouFangJianKey);
		if (serverId == null) {
			return AppErrorCode.KUAFU_NO_CONNECTION;// 跨服连接失败
		}
		return readyEnterKuafu(userRoleId, redis, serverId,fj);
	}
	
	private Object[] readyEnterKuafu(Long userRoleId, Redis redis,String serverId,int fangjian) {
		// 为玩家绑定跨服
		KuafuServerInfo serverInfo = KuafuServerInfoManager.getInstance().getKuafuServerInfo(serverId);
		if (serverInfo == null) {
			serverInfo = KuafuServerInfoManager.getInstance().getKuafuServerInfo(serverId, redis);
			if (serverInfo == null) {
				return AppErrorCode.CONFIG_ERROR;
			}
		}
		// 校验连接是否可用
		KuafuNetTunnel tunnel = KuafuConfigUtil.getConnection(serverInfo);
		if (tunnel == null || !tunnel.isConnected()) {
			KuafuConfigUtil.returnBrokenConnection(tunnel);
			String errorKey = RedisKey.buildKuafuServerErrorKey(serverId);
			redis.hset(errorKey, ChuanQiConfigUtil.getServerId(),String.valueOf(1));
			Map<String, String> timesMap = redis.hgetAll(errorKey);
			if (timesMap != null && timesMap.size() >= KuafuConfigUtil.CAN_NOT_CONNECT_CLEAN_TIMES) {
				redis.zrem(RedisKey.KUAFU_SERVER_LIST_KEY, serverId);
				redis.hset(RedisKey.KUAFU_DELETE_SERVER_LIST, serverId,
				DatetimeUtil.formatTime(GameSystemTime.getSystemMillTime(),DatetimeUtil.FORMART3));
				ChuanQiLog.error("跨服服务器与源服连接不通超过{}个，将被从跨服候选列表中删除，id:{}",KuafuConfigUtil.CAN_NOT_CONNECT_CLEAN_TIMES, serverId);
			}
			return AppErrorCode.KUAFU_NO_CONNECTION;
		} else {
			redis.del(RedisKey.buildKuafuServerErrorKey(serverId));
		}
		KuafuConfigUtil.returnConnection(tunnel);
		KuafuRoleServerManager.getInstance().bindServer(userRoleId, serverInfo);

		String stageId = stageControllExportService.getCurStageId(userRoleId);
		if (ObjectUtil.strIsEmpty(stageId)) {
			return AppErrorCode.ERR;
		}
		IStage stage = StageManager.getStage(stageId);
		if (stage == null || stage.isCopy()) {
			return AppErrorCode.ERR;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return AppErrorCode.ERR;
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_LUANDOU_SHOW_LOADING,null);
		// 向跨服发送绑定RoleId serverId
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId, InnerCmdType.BIND_ROLE_ID_SERVERID, new Object[] {ChuanQiConfigUtil.getServerId(), userRoleId });

		Object roleData = RoleFactory.createKuaFuRoleData(role);
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId,InnerCmdType.KUAFULUANDOU_SEND_ROLE_DATA,
						new Object[] {
								fangjian,
								userRoleId,
								role.getName(),
								new Object[] { roleData, userRoleId,role.getName() } });
		return null;
	}
	
	
	
	/**
	 * 获取玩家的乱斗房间ID
	 * @param userRoleId
	 * @return
	 */
	private int getKuaFuFangJianId(Redis redis,String userRoleId){
		Long rank = redis.zrevrank(RedisKey.KUAFU_LUANDOU_BAOMING_DATA_KEY, userRoleId);
		if(rank != null && rank < KuaFuDaLuanDouConstants.HAIXUAN_MINGE){
			rank = rank + 1;
			int fangjian = rank.intValue() % 8;
			if(fangjian == 0){
				fangjian = 8;
			}
			return fangjian;
		}else{
			return -1;
		}
	}
	
	
	/**
	 * 是否在时间内
	 * @return
	 */
	private boolean isHdTime(DaLuanDouHuoDongBiaoConfig config){
		//今天星期几
		int toDayWeek = DatetimeUtil.getCurWeek();
		//是否是同一天
		if(toDayWeek != config.getWeek()){
			return false;
		}
		//判断时间点
		String[] start = config.getStarttime().split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		String[] end = config.getEndtime().split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		long time = System.currentTimeMillis();
		long startTime = DatetimeUtil.getCalcTimeCurTime(time, Integer.parseInt(start[0]), Integer.parseInt(start[1]), Integer.parseInt(start[2]));
		long endTime = DatetimeUtil.getCalcTimeCurTime(time, Integer.parseInt(end[0]), Integer.parseInt(end[1]), Integer.parseInt(end[2]));
		if(time < startTime || time > endTime){
			return false;
		}
		return true;
	}
	
	
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
		// 标记当前为副本状态
		stageControllExportService.changeFuben(userRoleId, true);
		// 发送到场景控制中心进入小黑屋地图，以便从跨服服务器出来时走完整流程
		DiTuConfig config = diTuConfigExportService.loadSafeDiTu();
		int[] birthXy = config.getRandomBirth();
		Object[] applyEnterData = new Object[] { config.getId(), birthXy[0],
				birthXy[1], MapType.KUAFU_SAFE_MAP };
		// 传送前加一个无敌状态
		role.getStateManager().add(new NoBeiAttack());
		role.setChanging(true);
		StageMsgSender.send2StageControl(userRoleId,
				InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		ChuanQiLog.info("userRoleId={} enter xiaoheiwu in kuafudaluandou",
				userRoleId);

	}
	
	public Object[] exitKuafuDaLuanDou(Long userRoleId) {
		KuafuMsgSender.send2KuafuServer(userRoleId, userRoleId,InnerCmdType.KUAFULUANDOU_EXIT, null);
		return null;
	}
	
	public void leaveFb(Long userRoleId) {
		KuafuManager.removeKuafu(userRoleId);
		BusMsgSender.send2BusInner(userRoleId,InnerCmdType.S_APPLY_LEAVE_STAGE, null);
		
		stageControllExportService.changeFuben(userRoleId, false);
		KuafuRoleServerManager.getInstance().removeBind(userRoleId);
	}
	
	public Object[] kuafuLuanDouFuhuo(Long userRoleId) {
		KuafuMsgSender.send2KuafuServer(userRoleId, userRoleId,InnerCmdType.KUAFULUANDOU_FUHUO, null);
		return null;
	}

	
	/**
	 * 活动结束，根据排名发送奖励
	 */
	public void jiesuanEmail(){
		DaLuanDouHuoDongBiaoConfig config = daLuanDouHuoDongBiaoConfigExportService.loadById(KuaFuDaLuanDouConstants.LUANDOU_ID);
		if(config == null){
			return;
		}
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("redis 没有开启");
			return;
		}
		//判断活动是否已经结束
		String[] end = config.getEndtime().split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		long time = System.currentTimeMillis();
		long endTime = DatetimeUtil.getCalcTimeCurTime(time, Integer.parseInt(end[0]), Integer.parseInt(end[1]), Integer.parseInt(end[2]));
		//今天星期几
		int toDayWeek = DatetimeUtil.getCurWeek();
		if(toDayWeek > config.getWeek()){
			//活动过期超过1天了，清空redis中关于跨服乱斗的数据 
			redis.del(RedisKey.KUAFU_LUANDOU_BAOMING_DATA_KEY);
			for (int i = 1; i <= KuaFuDaLuanDouConstants.KUAFU_LUANDOU_FANGJIAN_COUNT; i++) {
				redis.del(RedisKey.getRedisKuafuLuanDouFangJianJiFenKey(i));
				redis.del(RedisKey.getRedisKuafuLuanDouFangJianKey(i));
			}
		}
		//是否是同一天
		if(toDayWeek != config.getWeek()){
			return;
		}
		if(time < endTime){
			return;
		}
		//获取每个房间的排行数据，然后发奖励
		for (int i = 1; i <= KuaFuDaLuanDouConstants.KUAFU_LUANDOU_FANGJIAN_COUNT; i++) {
			String xiaozu = getEnglishByXiaoZu(i);
			Set<Tuple> set = redis.zrevrangeWithScore(RedisKey.getRedisKuafuLuanDouFangJianJiFenKey(i), 0, -1);
			for (Tuple tuple : set) {//遍历集合，发送海选胜利邮件
				long userRoleId = Long.parseLong(tuple.getElement());
				try {
					RoleWrapper roleWrapper = roleExportService.getUserRoleFromDb(userRoleId);
					ChuanQiLog.info("kuafu daluandou luandou paiming userRolId={}  isBenfu={}", new Object[] { userRoleId, roleWrapper != null });
					if (roleWrapper != null) {
						//获取排名
						Long rank = redis.zrevrank(RedisKey.getRedisKuafuLuanDouFangJianJiFenKey(i), userRoleId+"");
						if(rank == null){
							return;
						}
						rank = rank + 1;
						//取每个房间的前2名存入到redis中,作为巅峰之战的参赛者
						if(0 < rank && rank <= KuaFuDaLuanDouConstants.KUAFU_LUANDOU_FANGJIAN_RANK){
						    long zhanli = 0;
						    RoleBusinessInfoWrapper roleBusinessInfoWrapper = roleBusinessInfoExportService.getRoleBusinessInfoForDB(userRoleId);
						    if(null != roleBusinessInfoWrapper){
						        zhanli = roleBusinessInfoWrapper.getCurFighter();
						    }
                            DianFengPlayerVO vo = new DianFengPlayerVO((xiaozu + rank), userRoleId, roleWrapper.getConfigId(), roleWrapper.getName(), roleWrapper.getLevel(), roleWrapper.getServerId(), zhanli, KuaFuDianFengConstants.STATE_NOT);
						    redis.hset(RedisKey.getRedisKuafuLuanDouRoomRankKey(i), String.valueOf(rank), JSON.toJSONString(vo));
						    //巅峰之战参赛通知
						    sendDianFengNoticeEmail(userRoleId, rank);
						    ChuanQiLog.info("******KuafuDengFeng******send role join kuafu dianfeng email notice, roleId={}, room={}. rank={}*******KuafuDengFeng*****", userRoleId, i, rank);
						}
						//根据排名获取奖励配置
						DaLuanDouJiangLiBiaoConfig jlConfig = daLuanDouJiangLiBiaoConfigExportService.getJiangItemByMingCi(rank.intValue());
						if(jlConfig == null){
							continue;
						}
						// 是本服玩家发邮件
						String slItem = jlConfig.getJiangitem() +":" + 1;
						String title = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_LUANDOU_JIESUAN_EMAIL_TITLE);
						String content = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_LUANDOU_JIESUAN_EMAIL,xiaozu,rank+"");
						emailExportService.sendEmailToOne(userRoleId, title,content,GameConstants.EMAIL_TYPE_SINGLE, slItem);
					}
				} catch (Exception ex) {
					ChuanQiLog.info("kuafu daluandou luandou paiming send reward error userRolId={} ", userRoleId);
				}
			}
			
		}
		return;
	}
	
	/**
	 * 根据小组获取英文译名
	 * @param rank
	 * @return
	 */
	private String getEnglishByXiaoZu(int xiaozu){
		switch (xiaozu) {
		case 1:
			 return "A";
		case 2:
			
			 return "B";
		case 3:
			
			 return "C";
		case 4:
			
			 return "D";
		case 5:
			
			 return "E";
		case 6:
			
			 return "F";
		case 7:
			
			 return "G";
		case 8:
			 return "H";
			 
		default:
			break;
		}
		return "";
	}
    
	/**
	 * 发送巅峰之战参赛通知邮件
	 * @param userRoleId
	 * @param rank
	 */
	private void sendDianFengNoticeEmail(Long userRoleId, long rank){
		String title = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_DIANFENG_NOTICE_EMAIL_TITLE);
        String content = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_DIANFENG_NOTICE_EMAIL, String.valueOf(rank));
	    emailExportService.sendEmailToOne(userRoleId, title,content,GameConstants.EMAIL_TYPE_SINGLE, null);
	}
	
	
    /**
     * 获取大乱斗冠亚军排名数据
     * 
     * @return
     */
    public Object[] getRankData() {
        Redis redis = GameServerContext.getRedis();
        if (redis == null) {
            ChuanQiLog.error("redis 没有开启");
            return null;
        }
        List<Object[]> rsList = new ArrayList<>();
        for (int i = 1; i <= KuaFuDaLuanDouConstants.KUAFU_LUANDOU_FANGJIAN_COUNT; i++) {
            Map<String, String> map = redis.hgetAll(RedisKey.getRedisKuafuLuanDouRoomRankKey(i));
            if (ObjectUtil.isEmpty(map)) {
                continue;
            }
            Object[] data = new Object[3];
            data[0] = getEnglishByXiaoZu(i);
            String json1 = map.get(String.valueOf(1));
            if (!ObjectUtil.strIsEmpty(json1)) {
                try {
                    DianFengPlayerVO vo = JSON.parseObject(json1, DianFengPlayerVO.class);
                    data[1] = vo.getNickName();
                } catch (Exception e) {
                    ChuanQiLog.error("解析大乱斗vo错误:data:{}",json1);
                }
            }
            String json2 = map.get(String.valueOf(2));
            if (!ObjectUtil.strIsEmpty(json2)) {
                try {
                    DianFengPlayerVO vo2 = JSON.parseObject(json2, DianFengPlayerVO.class);
                    data[2] = vo2.getNickName();
                } catch (Exception e) {
                    ChuanQiLog.error("解析大乱斗vo错误:data:{}",json2);
                }
            }
            rsList.add(data);
        }
        return ObjectUtil.isEmpty(rsList) ? null : rsList.toArray();
    }
    
    //清除大乱斗排名数据
    public void clearDaLuanDouRankData(){
        Redis redis = GameServerContext.getRedis();
        if (redis == null) {
            ChuanQiLog.error("redis 没有开启");
            return ;
        }
        for (int room = 1; room <= KuaFuDaLuanDouConstants.KUAFU_LUANDOU_FANGJIAN_COUNT; room++) {
            redis.del(RedisKey.getRedisKuafuLuanDouRoomRankKey(room));
        }
    }
}
