package com.junyou.bus.kuafu_qunxianyan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.kuafu_qunxianyan.configure.KuafuQuanXianYanRankRewardConfig;
import com.junyou.bus.kuafu_qunxianyan.configure.KuafuQuanXianYanRankRewardConfigExportService;
import com.junyou.bus.kuafu_qunxianyan.utils.KuafuQunXianYanUtils;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuQunXianYanPublicConfig;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.manager.KuafuRoleServerManager;
import com.junyou.kuafu.manager.KuafuServerInfoManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.kuafu.share.tunnel.KuafuNetTunnel;
import com.junyou.kuafu.share.util.KuafuConfigUtil;
import com.junyou.kuafu.share.util.KuafuServerInfo;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
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

@Service
public class KuafuQunXianYanService {
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private KuafuQuanXianYanRankRewardConfigExportService kuafuQuanXianYanRankRewardConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private EmailExportService emailExportService;
	/**
	 * 获取跨服世界boss的公共数据配置
	 * 
	 * @return
	 */
	private KuafuQunXianYanPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.KUAFU_QUNXIANYAN);
	}
	/**
	 * 是否在时间内
	 * @return
	 */
	private boolean isHdTime(KuafuQunXianYanPublicConfig config){
		//判断时间点
		String[] start = config.getStartTime().split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		String[] end = config.getEndTime().split(GameConstants.CONFIG_SUB_SPLIT_CHAR);
		long time = System.currentTimeMillis();
		long startTime = DatetimeUtil.getCalcTimeCurTime(time, Integer.parseInt(start[0]), Integer.parseInt(start[1]), Integer.parseInt(start[2]));
		long endTime = DatetimeUtil.getCalcTimeCurTime(time, Integer.parseInt(end[0]), Integer.parseInt(end[1]), Integer.parseInt(end[2]));
		if(time < startTime || time > endTime){
			return false;
		}
		return true;
	}

	public Object[] enterKuafuBoss(Long userRoleId) {
		KuafuQunXianYanPublicConfig publicConfig = getPublicConfig();
		// 判断等级是否满足
		if (publicConfig.getOpen() > roleExportService.getUserRole(userRoleId)
				.getLevel()) {
			return AppErrorCode.ROLE_LEVEL_ERROR;
		}

		// 判断是否在副本中
		if (stageControllExportService.inFuben(userRoleId)) {
			return AppErrorCode.FUBEN_IS_IN_FUBEN;
		}
		// 判断当前是否在活动期间内
		if (!isHdTime(publicConfig)) {
			return AppErrorCode.KUAFU_BOSS_ACTIVE_END;
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
		// 判断是否有绑定跨服，如果绑定跨服了，判断该世界boss是否结束
		String kuafuQunXianYanBindServerIdKey = RedisKey.getKuafuQunXianYanBindServerIdKey(userRoleId);
		String bindServerId = redis.get(kuafuQunXianYanBindServerIdKey);
		if (bindServerId != null) {
			Long bool = redis.zrank(RedisKey.KUAFU_SERVER_LIST_KEY, bindServerId);
			if(bool != null && bool>= 0){
				return readyEnterKuafu(userRoleId, redis, bindServerId);
			}
		}

		// 判断所有跨服是否已满
		Set<String> keySet = redis.keys(RedisKey.KUAFU_QUNXIANYAN_STAGE_NUM_KEY_PREFIX + "*");
		String[] keys = new String[keySet.size()];
		int count = 0;
		for (String e : keySet) {
			keys[count] = e;
			count++;
		}
		List<String> list = redis.mget(keys);
		Map<String, String> kuafuBossStageRoleNumMap = new HashMap<String, String>();
		for (int i = 0; i < keys.length; i++) {
			String numV = list.get(i);
			String tmpServerId = keys[i].substring(keys[i].indexOf(RedisKey.KUAFU_QUNXIANYAN_STAGE_NUM_KEY_PREFIX)
					+ RedisKey.KUAFU_QUNXIANYAN_STAGE_NUM_KEY_PREFIX.length());
			if(redis.zrank(RedisKey.KUAFU_SERVER_LIST_KEY, tmpServerId)!=null){
				kuafuBossStageRoleNumMap.put(tmpServerId,numV == null ? "0" : numV);
			}
		}
		boolean full = true;
		for (String serverId : kuafuBossStageRoleNumMap.keySet()) {
			Integer num = Integer.parseInt(kuafuBossStageRoleNumMap.get(serverId));
			if (num < publicConfig.getMaxpople()) {
				full = false;
				break;
			}
		}
		if (full) {
			return AppErrorCode.KUAFU_BOSS_ACTIVE_FULL;
		}
		// 筛选一个当前人数最多的，且boss还没有被打死的跨服进去
		String targeServerId = null;
		Integer maxNum = -1;
		for (String serverId : kuafuBossStageRoleNumMap.keySet()) {
			Integer num = Integer.parseInt(kuafuBossStageRoleNumMap.get(serverId));
			if (num < publicConfig.getMaxpople()) {
				if (num > maxNum) {
					targeServerId = serverId;
					maxNum = num;
				}
			}
		}
		if (targeServerId == null) {
			return AppErrorCode.KUAFU_BOSS_ACTIVE_FULL;
		}
		return readyEnterKuafu(userRoleId, redis, targeServerId);
	}

	private Object[] readyEnterKuafu(Long userRoleId, Redis redis,
			String serverId) {
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
				redis.hset(RedisKey.KUAFU_DELETE_SERVER_LIST, serverId,DatetimeUtil.formatTime(GameSystemTime.getSystemMillTime(),DatetimeUtil.FORMART3));
				ChuanQiLog.error("跨服服务器与源服连接不通超过{}个，将被从跨服候选列表中删除，id:{}",
						KuafuConfigUtil.CAN_NOT_CONNECT_CLEAN_TIMES, serverId);
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
		BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_QUNXIANYAN_SHOW_LOADING,null);
		// 向跨服发送绑定RoleId serverId
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,
				userRoleId, InnerCmdType.BIND_ROLE_ID_SERVERID, new Object[] {
						ChuanQiConfigUtil.getServerId(), userRoleId });

		Object roleData = RoleFactory.createKuaFuRoleData(role);
		KuafuMsgSender.send2KuafuServer(GameConstants.DEFAULT_ROLE_ID,userRoleId,InnerCmdType.KUAFUQUNXIANYAN_SEND_ROLE_DATA,
						new Object[] {
								userRoleId,
								role.getName(),
								new Object[] { roleData, userRoleId,
								role.getName() } });
		return null;
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
		Object[] applyEnterData = new Object[] { config.getId(), birthXy[0],birthXy[1], MapType.KUAFU_SAFE_MAP };
		// 传送前加一个无敌状态
		role.getStateManager().add(new NoBeiAttack());
		role.setChanging(true);
		StageMsgSender.send2StageControl(userRoleId,
				InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		ChuanQiLog.info("userRoleId={} enter xiaoheiwu in kuafuqunxianyan",
				userRoleId);

	}

	public Object[] exitKuafuBoss(Long userRoleId) {
		KuafuMsgSender.send2KuafuServer(userRoleId, userRoleId,	InnerCmdType.KUAFUQUNXIANYAN_EXIT, null);
		return null;
	}

	public void leaveFb(Long userRoleId) {
		KuafuManager.removeKuafu(userRoleId);
		BusMsgSender.send2BusInner(userRoleId,InnerCmdType.S_APPLY_LEAVE_STAGE, null);
		stageControllExportService.changeFuben(userRoleId, false);
		KuafuRoleServerManager.getInstance().removeBind(userRoleId);
	}

	public Object[] kuafuBossFuhuo(Long userRoleId) {
		KuafuMsgSender.send2KuafuServer(userRoleId, userRoleId,InnerCmdType.KUAFUQUNXIANYAN_FUHUO, getPublicConfig().getMapid());
		return null;
	}
	
	/**
	 * 请求排行榜
	 * @param stageId
	 * @param userRoleId
	 * @param jifen
	 */
	public void getRank(Long userRoleId){
		KuafuMsgSender.send2KuafuServer(userRoleId, userRoleId, InnerCmdType.KUAFUQUNXIANYAN_GET_RANK, null);
		return;
	}
	
	
	/**
	 * 领取奖励
	 * @param userRoleId
	 * @return
	 */
	public Object[] lingQuJiangLi(Long userRoleId,int rank){
		KuafuQunXianYanPublicConfig publicConfig = getPublicConfig();
		// 判断当前是否在活动期间内
		if (isHdTime(publicConfig)) {
			return AppErrorCode.KUAFU_QUNXIANYAN_WEI_JIESHU;
		}
		//判断是否领取过奖励
		if(KuafuQunXianYanUtils.getJmapByUserRoleId(userRoleId)){
			return AppErrorCode.KUAFU_QUNXIANYAN_LINGQU;
		}
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("redis 没有开启");
			return AppErrorCode.CONFIG_ERROR;
		}
		//String member = userRoleId.toString();
		//Long rank = redis.zrevrank(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN, member);
		
		/*if(rank == null){
			return AppErrorCode.KUAFU_QUNXIANYAN_PAIMING_NO;
		}*/
		//奖励配置
		KuafuQuanXianYanRankRewardConfig jconfig = kuafuQuanXianYanRankRewardConfigExportService.getConfigByRank(rank);
		if(jconfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(jconfig.getJiangitem(), userRoleId);
		if(bagCheck != null){
			String title = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_EMAIL_JIANGLI_TITLE);
			String content = EmailUtil.getCodeEmail(AppErrorCode.ZHUANPAN_EAMIL);
			emailExportService.sendEmailToOne(userRoleId,title, content,GameConstants.EMAIL_TYPE_SINGLE, jconfig.getJiangitemStr());
		}else{
			//物品进背包
			roleBagExportService.putGoodsAndNumberAttr(jconfig.getJiangitem(), userRoleId, GoodsSource.QUNXIANYAN_JT, LogPrintHandle.GET_QUNXIANYAN, LogPrintHandle.GBZ_QUNXIANYAN, true);
		}
		//记录领取状态
		KuafuQunXianYanUtils.serJmapByUserRoleId(userRoleId);
		
		return new Object[]{1,rank};
	}
	/**
	 * 邮件奖励
	 * @param userRoleId
	 * @return
	 */
	public void emailJiangli(Long userRoleId,int rank){
		//奖励配置
		KuafuQuanXianYanRankRewardConfig jconfig = kuafuQuanXianYanRankRewardConfigExportService.getConfigByRank(rank);
		if(jconfig == null){
			return ;
		}
		String title = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_EMAIL_JIANGLI_TITLE);
		String content = EmailUtil.getCodeEmail(AppErrorCode.KUAFU_EMAIL_JIANGLI,rank+"");
		emailExportService.sendEmailToOne(userRoleId, title,content,GameConstants.EMAIL_TYPE_SINGLE, jconfig.getJiangitemStr());
		
		return ;
	}
	
	public void clearQunXianYan(){
		KuafuQunXianYanUtils.clearJmap();
		Redis redis = GameServerContext.getRedis();
		if (redis == null) {
			ChuanQiLog.error("redis 没有开启");
			return;
		}
		redis.del(RedisKey.KUAFU_QUNXIANYAN_RANK_JIFEN);
	}
	
	public void caiJiGood(Long userRoleId,String item,Integer count){
		Map<String, Integer> goodMap = new HashMap<>();
		goodMap.put(item, count);
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(goodMap, userRoleId);
		if(bagCheck != null){
			String title = EmailUtil.getCodeEmail(AppErrorCode.ZHUANPAN_EAMIL_TITLE);
			String content = EmailUtil.getCodeEmail(AppErrorCode.ZHUANPAN_EAMIL);
			emailExportService.sendEmailToOne(userRoleId,title, content,GameConstants.EMAIL_TYPE_SINGLE, item+":"+count);
		}else{
			//物品进背包
			roleBagExportService.putGoodsAndNumberAttr(goodMap, userRoleId, GoodsSource.QUNXIANYAN_CJ, LogPrintHandle.GET_QUNXIANYAN_CJ, LogPrintHandle.GBZ_QUNXIANYAN_CJ, true);
		}
	}
	
}
