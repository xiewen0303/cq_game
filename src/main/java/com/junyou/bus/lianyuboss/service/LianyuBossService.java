package com.junyou.bus.lianyuboss.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.lianyuboss.configure.LianyuBossConfig;
import com.junyou.bus.lianyuboss.configure.LianyuBossConfigExportService;
import com.junyou.bus.lianyuboss.dao.GuildBossLianyuDao;
import com.junyou.bus.lianyuboss.entity.GuildBossLianyu;
import com.junyou.bus.lianyuboss.entity.LianyuBossPublicVo;
import com.junyou.bus.lianyuboss.util.LianyuBossComparator;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.LianyuBossRewardLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.map.configure.export.DiTuConfig;
import com.junyou.gameconfig.map.configure.export.DiTuConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GuildPublicConfig;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.public_.guild.entity.GuildMember;
import com.junyou.public_.guild.service.GuildService;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class LianyuBossService {

	@Autowired
	private GuildBossLianyuDao guildBossLianyuDao;
	@Autowired
	private GuildService guildService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private LianyuBossConfigExportService lianyuBossConfigExportService;
	
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private DiTuConfigExportService diTuConfigExportService;
	@Autowired
	private SessionManagerExportService sessionManagerExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private EmailExportService emailExportService;

	/** 服务器帮派boss公共数据 **/
	private ConcurrentMap<Long, ConcurrentMap<Integer, LianyuBossPublicVo>> lianyuBossPublicMap = new ConcurrentHashMap<>();

	private ReentrantLock lock = new ReentrantLock();
	private static int TRY_LOCK_TIME = 1;

	/**
	 * 服务器启动|凌晨 统计所有帮派boss公共数据
	 */
	public void initAllGuildLianyuBossData() {
		lianyuBossPublicMap.clear();
		List<LianyuBossPublicVo> data = loadLianyuBossPublicData();
		if (data != null && !data.isEmpty()) {
			ConcurrentMap<Integer, LianyuBossPublicVo> guildMap = null;
			for (LianyuBossPublicVo lianyuBossPublicVo : data) {
				if(lianyuBossPublicMap.get(lianyuBossPublicVo.getGuildId())==null){
					guildMap = new ConcurrentHashMap<>();
					lianyuBossPublicMap.put(lianyuBossPublicVo.getGuildId(), guildMap);
				}
				guildMap.put(lianyuBossPublicVo.getConfigId(), lianyuBossPublicVo);
				
			}
		}
	}
	/** 凌晨过一秒清理 **/
	public void clearData() {
		lianyuBossPublicMap.clear();
		clearKuatianData();
		ChuanQiLog.info("执行凌晨清空门派炼狱boss战数据成功！");
	}
	
	/** 清理DB过期数据 **/
	public void clearKuatianData() {
		try {
			//清理表跨天数据
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
			String time = sdf.format(new Date(GameSystemTime.getSystemMillTime()));
			guildBossLianyuDao.deleteKuatianShujuByLingceng(time);
		} catch (Exception e) {
			ChuanQiLog.error("凌晨时间到，清理表跨天数据报错"+e);
		}
	}
	
	/**
	 * 查看boss属性是否减半
	 */
	public boolean isReduceHalfBossHarm(long userRoleId,int configId){
		boolean bl = false;
		GuildMember guildMember = guildService.getGuildMember(userRoleId);
		if(guildMember!=null){
			ConcurrentMap<Integer, LianyuBossPublicVo> guildMap =lianyuBossPublicMap.get(guildMember.getGuildId());
			if(guildMap==null){
				return false;
			}else{
				if(guildMap.get(configId)!=null){
					return true;
				}
			}
		}
		return bl;
		
	}
	
	/**
	 * 初始化个人数据到缓存
	 * @param userRoleId
	 * @return
	 */
	public List<GuildBossLianyu> initGuildBossLianyu(Long userRoleId) {
		List<GuildBossLianyu> data = null;
		GuildMember guildMember = guildService.getGuildMember(userRoleId);
		if (guildMember != null) {
			data = loadGuildBossLianyuById(guildMember.getGuildId(), userRoleId);
		}
		return data;
	}

	/**
	 * 获取个人boss当天通关记录
	 */
	private List<GuildBossLianyu> loadGuildBossLianyuById(Long guildId, Long userRoleId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
		String time = sdf.format(new Date(GameSystemTime.getSystemMillTime()));
		List<GuildBossLianyu> list = guildBossLianyuDao.initGuildBossLianyuById(guildId, userRoleId,time);
		return list;
	}

	/**
	 * 请求玩家当前通关数据
	 */
	public Object[] getCurrentTonguanInfo(Long userRoleId) {

		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);

		GuildPublicConfig guildPublicConfig = getGonggongShujuConfig();
		if (roleWrapper.getLevel() < guildPublicConfig.getLianyuBossOpen()) {
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}

		GuildMember guildMember = guildService.getGuildMember(userRoleId);
		if (guildMember == null) {
			return AppErrorCode.ROLE_NO_GUILD;
		}

		GuildBossLianyu guildBossLianyu = getCurrentBossByUserId(userRoleId,guildMember.getGuildId());
		if (guildBossLianyu == null) {
			return new Object[] { 1, 0 };
		}
		return new Object[] { 1, guildBossLianyu.getConfigId() };
	}

	/**
	 * 请求关卡信息
	 */
	public Object[] getInfoByConfigId(int configId, Long userRoleId) {
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);

		GuildPublicConfig guildPublicConfig = getGonggongShujuConfig();
		if (roleWrapper.getLevel() < guildPublicConfig.getLianyuBossOpen()) {
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}

		GuildMember guildMember = guildService.getGuildMember(userRoleId);
		if (guildMember == null) {
			return AppErrorCode.ROLE_NO_GUILD;
		}
		Object[] ret = new Object[] { 1, configId, 0, null, 0, 0 };

		GuildBossLianyu guildBossLianyu = getGuildBossLianyuById(configId, userRoleId,guildMember.getGuildId());

		if (guildBossLianyu != null) {
			ret[5] = guildBossLianyu.getTongGuanTime();
		}
		ConcurrentMap<Integer, LianyuBossPublicVo> guildMap = lianyuBossPublicMap.get(guildMember.getGuildId());
		if (guildMap != null) {
			LianyuBossPublicVo vo = guildMap.get(configId);
			if (vo != null) {
				ret[2] = vo.getCountNum();
				ret[3] = getRoleWrapper(vo.getUserRoleId()).getName();
				ret[4] = vo.getFastestTime();
			}
		}
		return ret;
		
	}
	/**
	 * 玩家主动点击退出炼狱
	 * @param userRoleId
	 */
	public void exitLianyu(Long userRoleId){
		if(!stageControllExportService.inFuben(userRoleId)){
			return;
		}
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.S_EXIT_FUBEN, null);
	}
	/**2分钟倒计时强退
	 * 副本时间到退出炼狱通知客户端
	 * @param userRoleId
	 */
	public void hasExitPata(Long userRoleId){

		BusMsgSender.send2One(userRoleId, ClientCmdType.LIANYU_BOSS_EXIT, null);
	}
	public void enterTiaozhan(Long userRoleId, Integer configId, BusMsgQueue busMsgQueue) {

		LianyuBossConfig config = lianyuBossConfigExportService.loadById(configId);
		if (config == null) {
			// 配置异常
			busMsgQueue.addMsg(userRoleId, ClientCmdType.LIANYU_BOSS_ENTER, AppErrorCode.CONFIG_ERROR);
			return;
		}
		if (stageControllExportService.inFuben(userRoleId)) {
			// 在副本中
			busMsgQueue.addMsg(userRoleId, ClientCmdType.LIANYU_BOSS_ENTER, AppErrorCode.FUBEN_IS_IN_FUBEN);
			return;
		}
		
		GuildMember guildMember = guildService.getGuildMember(userRoleId);
		if(guildMember==null){
			//角色没有门派
			busMsgQueue.addMsg(userRoleId, ClientCmdType.LIANYU_BOSS_ENTER, AppErrorCode.ROLE_NO_GUILD);
			return;
		}else{
			//当天加入帮派不能挑战
			if(DatetimeUtil.dayIsToday(guildMember.getEnterTime())){
				busMsgQueue.addMsg(userRoleId, ClientCmdType.LIANYU_BOSS_ENTER, AppErrorCode.LIANYU_BOSS_DAY_ENTTER);
				return;
			}
		}
		 GuildBossLianyu guildBossLianyuDone = getGuildBossLianyuById(configId,userRoleId,guildMember.getGuildId());
			if (guildBossLianyuDone!=null) {
				//副本已打
				busMsgQueue.addMsg(userRoleId, ClientCmdType.LIANYU_BOSS_ENTER, AppErrorCode.FUBEN_FINISHED_ALREADY);
				return;
			}
		 GuildBossLianyu guildBossLianyu = getCurrentBossByUserId(userRoleId,guildMember.getGuildId());
		 if (guildBossLianyu!=null && configId - 1 > guildBossLianyu.getConfigId()) {
			// 当前层不可挑战 不能跳级挑战
			busMsgQueue.addMsg(userRoleId, ClientCmdType.LIANYU_BOSS_ENTER, AppErrorCode.FUBEN_NOW_CENG_CANNOT_TIAOZHAN);
			return;
		 }
		GuildPublicConfig guildPublicConfig = getGonggongShujuConfig(); 
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper.getLevel()<guildPublicConfig.getLianyuBossOpen()){
			//等级不足
			busMsgQueue.addMsg(userRoleId, ClientCmdType.LIANYU_BOSS_ENTER, AppErrorCode.ROLE_LEVEL_NOT_ENOUGH);
		}
		// 发送到场景进入地图
		DiTuConfig dituCoinfig = diTuConfigExportService.loadDiTu(guildPublicConfig.getLianyuBossMapId());
		int[] birthXy = dituCoinfig.getRandomBirth();
		Object[] applyEnterData = new Object[] { dituCoinfig.getId(), birthXy[0], birthXy[1], MapType.GUILD_LIANYU_BOSS, configId };
		busMsgQueue.addBusMsg(userRoleId, InnerCmdType.S_APPLY_CHANGE_STAGE, applyEnterData);
		// 推送给客户端进入成功
		busMsgQueue.addMsg(userRoleId, ClientCmdType.LIANYU_BOSS_ENTER, new Object[] { AppErrorCode.SUCCESS, configId });
	}

	/**
	 *  通关成功
	 * 挑战
	 * @param enterBossTime
	 *            进入boss战时间
	 * @return
	 */
	public void tongguanSuccess(Long userRoleId,int configId, Long enterBossTime, int completeTime) {
		completeTime = completeTime/1000;
		// 通知客户端挑战成功
		BusMsgSender.send2One(userRoleId, ClientCmdType.LIANYU_BOSS_SUCCESS,new Object[]{1, completeTime});

		LianyuBossConfig config = lianyuBossConfigExportService.loadById(configId);
		if (config == null) {
			ChuanQiLog.error("门派炼狱boss奖励配置表不存在");
			return;
		}
		GuildMember guildMember = guildService.getGuildMember(userRoleId);
		if (guildMember == null) {
			ChuanQiLog.error("门派炼狱通关成功，但是找不到玩家所属的帮派，userRoleId={}", userRoleId);//容错记录
		}
		// 一直打到跨天的情况不记录挑战数据
		if (DatetimeUtil.dayIsToday(enterBossTime)) {
			if (getGuildBossLianyuById(configId, userRoleId,guildMember.getGuildId()) == null && guildMember != null) {
				// 创建个人数据
				createGuildBossLianyu(configId, userRoleId, completeTime, guildMember.getGuildId());
			} else {
				ChuanQiLog.error("重复通关，userRoleID={},configId={}", userRoleId, configId);
				return;
			}
		}
		// 发奖励
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(config.getRewards(), userRoleId);
		// 背包空间不足,发邮件
		if (code != null) {
			String[] attachmentArray = EmailUtil.getAttachments(config.getRewards());
			String title = EmailUtil.getCodeEmail(AppErrorCode.LIANYU_BOSS_REWARD_EMAIL_TITLE);
			String content = EmailUtil.getCodeEmail(AppErrorCode.LIANYU_BOSS_REWARD_EMAIL);
			emailExportService.sendEmailToOne(userRoleId,title, content, GameConstants.EMAIL_TYPE_SINGLE, attachmentArray[0]);
		} else {
			// 背包新增一个道具
			roleBagExportService.putInBag(config.getRewards(), userRoleId, GoodsSource.GUILD_BOSS_LIANYU, true);
		}
		// 日志
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(config.getRewards(), null);
		GamePublishEvent.publishEvent(new LianyuBossRewardLogEvent(userRoleId, jsonArray, completeTime, configId));

	}

	/**
	 * 创建 GuildBossLianyu
	 * 
	 * @param e
	 * @return
	 */
	private void createGuildBossLianyu(int configId, Long userRoleId, int completeTime, long guildId) {
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			GuildBossLianyu guildBossLianyu = new GuildBossLianyu();
			guildBossLianyu.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			guildBossLianyu.setConfigId(configId);
			guildBossLianyu.setRewardState(1);// 1领了，0未领
			guildBossLianyu.setTongGuanTime(completeTime);
			guildBossLianyu.setUserRoleId(userRoleId);
			guildBossLianyu.setGuildId(guildId);
			guildBossLianyu.setUpdateTime(GameSystemTime.getSystemMillTime());
			guildBossLianyu.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			guildBossLianyuDao.cacheInsert(guildBossLianyu, userRoleId);
			// 检查公共缓存
			ConcurrentMap<Integer, LianyuBossPublicVo> guildMap = lianyuBossPublicMap.get(guildId);
			LianyuBossPublicVo vo = null;
			if (guildMap == null) {
				guildMap = new ConcurrentHashMap<>();
				vo = new LianyuBossPublicVo();
				vo.setConfigId(configId);
				vo.setCountNum(1);
				vo.setFastestTime(completeTime);
				vo.setGuildId(guildId);
				vo.setUserRoleId(userRoleId);
				vo.setcTime(guildBossLianyu.getCreateTime());
				guildMap.put(configId, vo);
				lianyuBossPublicMap.put(guildId, guildMap);
			} else {
				 if(guildMap.get(configId) == null){
					vo = new LianyuBossPublicVo();
					vo.setConfigId(configId);
					vo.setCountNum(1);
					vo.setFastestTime(completeTime);
					vo.setGuildId(guildId);
					vo.setUserRoleId(userRoleId);
					vo.setcTime(guildBossLianyu.getCreateTime());
					guildMap.put(configId, vo);
				 }else{
					vo = guildMap.get(configId);
					vo.setCountNum(vo.getCountNum() + 1);// 完成人数+1
					if (completeTime < vo.getFastestTime()) {
						vo.setFastestTime(completeTime);
						vo.setUserRoleId(userRoleId);// 更新新记录
					} 
				 }
				
			}

		} catch (InterruptedException e) {
			ChuanQiLog.error("创建门派炼狱boss个人数据或者更新公共缓存报错，userRoleId={},config={},error={}", userRoleId, configId, e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
	private RoleWrapper getRoleWrapper(long e) {
		RoleWrapper role = null;
		if (sessionManagerExportService.isOnline(e)) {
			role = roleExportService.getLoginRole(e);
		} else {
			role = roleExportService.getUserRoleFromDb(e);
		}
		return role;
	}

	/**
	 * 当前通关到第几关数据
	 */
	private GuildBossLianyu getCurrentBossByUserId(Long userRoleId,long currentGuildId) {
		List<GuildBossLianyu> list = guildBossLianyuDao.cacheLoadAll(userRoleId);
		if (list != null && !list.isEmpty()) {
			Collections.sort(list, new LianyuBossComparator());
			// 获取已通关关卡数据
			if (!checkKuatian(list.get(0),currentGuildId)) {
				
				return list.get(0);
			}

		}
		return null;
	}

	/**
	 * 获取指定关卡的数据
	 */
	private GuildBossLianyu getGuildBossLianyuById(int configId, Long userRoleId,long currentGuildId) {
		List<GuildBossLianyu> list = guildBossLianyuDao.cacheLoadAll(userRoleId);
		if (list != null && !list.isEmpty()) {
			for (GuildBossLianyu guildBossLianyu : list) {
				if (guildBossLianyu.getConfigId().intValue() == configId) {
					if (!checkKuatian(guildBossLianyu,currentGuildId)) {
						return guildBossLianyu;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 检查数据是否跨天
	 * 换了帮派也要清空之前在旧的帮派打的缓存数据
	 */
	private boolean checkKuatian(GuildBossLianyu entity,long currentGuildId) {
		boolean bl = false;
		if (!DatetimeUtil.dayIsToday(entity.getCreateTime().getTime()) || entity.getGuildId().longValue()!=currentGuildId) {
			// 跨天了清理数据
			try {
				bl = true;
				List<GuildBossLianyu> list = guildBossLianyuDao.cacheLoadAll(entity.getUserRoleId());
				//清理缓存跨天数据
				if(list!=null && !list.isEmpty()){
					for (GuildBossLianyu guildBossLianyu : list) {
						if(!DatetimeUtil.dayIsToday(guildBossLianyu.getCreateTime().getTime()) || entity.getGuildId().longValue()!=currentGuildId){
							guildBossLianyuDao.cacheDelete(guildBossLianyu.getId(), guildBossLianyu.getUserRoleId());
						}
					}
					//在线跨天清理DB
					deleteDataByUserRoleId(entity.getUserRoleId());
				}
				
			} catch (Exception e) {
				ChuanQiLog.error("门派boss删除个人通关,跨天数据出错，userRoleId={},guildId={},error={}", entity.getUserRoleId(), entity.getGuildId(),e);
			}
		}
		return bl;
	}

	/**
	 * 删除个人通关数据 根据userRoleId哪怕是换了帮派的玩家也会同时删除之前在别的帮派的记录
	 */
	private void deleteDataByUserRoleId(Long userRoleId) {
		guildBossLianyuDao.deleteDataByUserRoleId(userRoleId);
	}

	/**
	 * 初始化炼狱boss公共数据
	 */
	private List<LianyuBossPublicVo> loadLianyuBossPublicData() {

		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
		String startTime = sdf.format(new Date(GameSystemTime.getSystemMillTime()));

		return guildBossLianyuDao.initLianyuBossPublicData(startTime);
	}

	private GuildPublicConfig getGonggongShujuConfig() {
		GuildPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_GUILD);
		return config;
	}

}
