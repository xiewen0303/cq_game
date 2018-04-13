package com.junyou.bus.kfjingji.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.chengshen.export.ChengShenExportService;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.kfjingji.configure.KfJingjiConfig;
import com.junyou.bus.kfjingji.configure.KfJingjiConfigExportService;
import com.junyou.bus.kfjingji.configure.KfJingjiMingciZhanshiConfig;
import com.junyou.bus.kfjingji.configure.KfJingjiMingciZhanshiConfigExportService;
import com.junyou.bus.kfjingji.configure.KfPaimingJiangliConfig;
import com.junyou.bus.kfjingji.configure.KfPaimingJiangliConfigExportService;
import com.junyou.bus.kfjingji.configure.KfRobotConfig;
import com.junyou.bus.kfjingji.configure.KfRobotConfigExportService;
import com.junyou.bus.kfjingji.dao.KuafuJingjiDao;
import com.junyou.bus.kfjingji.entity.KuafuJingji;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.skill.entity.RoleSkill;
import com.junyou.bus.skill.export.RoleSkillExportService;
import com.junyou.bus.stagecontroll.MapType;
import com.junyou.bus.stagecontroll.export.StageControllExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.context.GameServerContext;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.configure.SkillFireType;
import com.junyou.stage.model.core.skill.ISkill;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.element.role.KuafuRoleUtil;
import com.junyou.stage.model.element.role.Role;
import com.junyou.stage.model.skill.SkillManager;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.stage.tunnel.StageMsgSender;
import com.junyou.state.jingji.entity.IJingjiFighter;
import com.junyou.state.jingji.entity.JingjiAttribute2;
import com.junyou.state.jingji.entity.JingjiFight;
import com.junyou.state.jingji.entity.JingjiFighter2;
import com.junyou.utils.codec.SerializableUtil;
import com.junyou.utils.collection.ReadOnlyList;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.zip.CompressConfigUtil;
import com.kernel.cache.redis.Redis;
import com.kernel.cache.redis.RedisKey;

/**
 * @author LiuYu
 * 2015-10-27 上午11:06:41
 */
@Service
public class KuafuJingjiService {
	
	@Autowired
	private KuafuJingjiDao kuafuJingjiDao;
	@Autowired
	private KfJingjiMingciZhanshiConfigExportService kfJingjiMingciZhanshiConfigExportService;
	@Autowired
	private KfJingjiConfigExportService kfJingjiConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private KfPaimingJiangliConfigExportService kfPaimingJiangliConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private StageControllExportService stageControllExportService;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private KfRobotConfigExportService kfRobotConfigExportService;
	@Autowired
	private ChengShenExportService chengShenExportService;
	@Autowired
	private ChiBangExportService chiBangExportService;
	@Autowired
	private RoleSkillExportService roleSkillExportService;
	
	public List<KuafuJingji> initKuafuJingji(Long userRoleId) {
		return kuafuJingjiDao.initKuafuJingji(userRoleId);
	}
	
	private KuafuJingji createKuafuJingji(long userRoleId){
		KuafuJingji jingji = new KuafuJingji();
		jingji.setUserRoleId(userRoleId);
		jingji.setCdTime(0l);
		jingji.setChangeCount(0);
		jingji.setGift(0);
		jingji.setRank(kfJingjiMingciZhanshiConfigExportService.getMaxRank());
		jingji.setLastRank(kfJingjiMingciZhanshiConfigExportService.getMaxRank());
		jingji.setTiaozhanCount(0);
		jingji.setUpdateTime(GameSystemTime.getSystemMillTime());
		initFighter(jingji);
		return jingji;
	}
	
	private KuafuJingji getKuafuJingji(long userRoleId){
		KuafuJingji jingji = kuafuJingjiDao.cacheLoad(userRoleId, userRoleId);
		if(jingji == null){
			jingji = createKuafuJingji(userRoleId);
			kuafuJingjiDao.cacheInsert(jingji, userRoleId);
		}else if(!DatetimeUtil.dayIsToday(jingji.getUpdateTime())){
			jingji.setChangeCount(0);
			jingji.setGift(0);
			jingji.setLastRank(0);
			jingji.setTiaozhanCount(0);
			jingji.setUpdateTime(GameSystemTime.getSystemMillTime());
			kuafuJingjiDao.cacheUpdate(jingji, userRoleId);
		}
		return jingji;
	}
	
	/**
	 * 决定战斗对手
	 * @param jingji
	 */
	private void initFighter(KuafuJingji jingji){
		KfJingjiMingciZhanshiConfig config = kfJingjiMingciZhanshiConfigExportService.loadByRank(jingji.getRank());
		int index = 1;
		for (int[] other : config.getOther()) {
			int rank = jingji.getRank() - other[0];
			if(other[1] > 1){
				rank += Lottery.roll(other[1]);
			}
			switch (index) {
			case 1:
				jingji.setFight1(rank);
				break;
			case 2:
				jingji.setFight2(rank);
				break;
			case 3:
				jingji.setFight3(rank);
				break;
			case 4:
				jingji.setFight4(rank);
				break;
			default:
				break;
			}
			index++;
		}
	}
	
	public Object[] getFighter(Long userRoleId){
		KuafuJingji jingji = getKuafuJingji(userRoleId);
		Redis redis = GameServerContext.getRedis();
		if(redis == null){
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}
		if(jingji.getRank() < kfJingjiMingciZhanshiConfigExportService.getMaxRank()){
			//上榜玩家需比较最近排名是否有变动
			Map<String, String> kfjj = redis.hgetAll(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId));
			int rank = kfJingjiMingciZhanshiConfigExportService.getMaxRank();
			if(kfjj != null && kfjj.size() > 0){
				rank = CovertObjectUtil.object2int(kfjj.get(GameConstants.REDIS_KUAFU_JINGJI_RANK));
				if(rank < 1){
					rank = kfJingjiMingciZhanshiConfigExportService.getMaxRank();
					redis.del(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId));
				}
			}
			if(jingji.getRank() != rank){//排名变动
				jingji.setRank(rank);
				initFighter(jingji);
				kuafuJingjiDao.cacheUpdate(jingji, userRoleId);
			}
		}
		refreshAttribute(jingji, redis);
		if(jingji.getFight1() < 1){
			initFighter(jingji);
			kuafuJingjiDao.cacheUpdate(jingji, userRoleId);
		}
		return new Object[]{1,jingji.getRank(),jingji.getTiaozhanCount(),jingji.getCdTime(),jingji.getChangeCount(),getFighters(jingji, redis)};
	}
	
	private Object[] getFighters(KuafuJingji jingji,Redis redis){
		//获取对手信息
		
		return new Object[]{
				getFighterInfo(1, redis),
				getFighterInfo(2, redis),
				getFighterInfo(3, redis),
				getFighterInfo(jingji.getFight1(), redis),
				getFighterInfo(jingji.getFight2(), redis),
				getFighterInfo(jingji.getFight3(), redis),
				getFighterInfo(jingji.getFight4(), redis)
		};
	}
	
	/**
	 * @param map
	 * @param redis
	 * @return
	 */
	private Object[] getFighterInfo(int rank,Redis redis){
		Map<String, String> map = redis.hgetAll(RedisKey.getRedisKuaFuJingJiRankKey(rank));
		if(map == null || map.size() < 1){
			KfRobotConfig config = kfRobotConfigExportService.loadById(rank);
			if(config == null){
				ChuanQiLog.error("跨服竞技异常，排名：{}的机器人无配置",rank);
				return null;
			}
			//机器人
			return config.getInfo();
		}
		long userRoleId = CovertObjectUtil.obj2long(map.get(GameConstants.REDIS_KUAFU_JINGJI_USER_ROLE_ID));
		Map<String, String> role = redis.hgetAll(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId));
		String name = map.get(GameConstants.REDIS_KUAFU_JINGJI_INFO_NAME);
		int level = CovertObjectUtil.object2int(role.get(GameConstants.REDIS_KUAFU_JINGJI_INFO_LEVEL));
		long zplus = CovertObjectUtil.obj2long(role.get(GameConstants.REDIS_KUAFU_JINGJI_ZPLUS));
		int configId = CovertObjectUtil.object2int(role.get(GameConstants.REDIS_KUAFU_JINGJI_INFO_CONFIG));
		int zuoqi = CovertObjectUtil.object2int(role.get(GameConstants.REDIS_KUAFU_JINGJI_INFO_ZUOQI));
		
		return new Object[]{userRoleId,name,level,zplus,rank,configId,zuoqi};
	}
	
	/**
	 * 换一批对手
	 * @param userRoleId
	 * @return
	 */
	public Object[] changeTargets(Long userRoleId){
		KuafuJingji jingji = getKuafuJingji(userRoleId);
		KfJingjiConfig config = kfJingjiConfigExportService.loadConfig();
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Redis redis = GameServerContext.getRedis();
		if(redis == null){
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}
		if(jingji.getChangeCount() >= config.getFreeChange()){
			//本次刷新需要花费元宝
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, config.getChangeGold(), userRoleId, LogPrintHandle.CONSUME_KUAFU_JINGJI, true, LogPrintHandle.CBZ_KUAFU_JINGJI_CHANGE_TARGETS);
			if(result != null){
				return result;
			}
		}else{
			jingji.setChangeCount(jingji.getChangeCount() + 1);
		}
		initFighter(jingji);
		kuafuJingjiDao.cacheUpdate(jingji, userRoleId);
		return new Object[]{1,jingji.getChangeCount(),getFighters(jingji, redis)};
	}
	
	public Object[] reciveGift(Long userRoleId){
		KuafuJingji jingji = getKuafuJingji(userRoleId);
		if(jingji.getGift().equals(GameConstants.BOOLEAN_TRUE_TO_INT)){
			return AppErrorCode.JINGJI_GIFT_IS_RECIVED;//奖励已领取过
		}
		int lastRank = jingji.getLastRank();
		if(lastRank < 1){
			//未记录昨日排名，虚到redis中获取
			Redis redis = GameServerContext.getRedis();
			if(redis == null){
				return AppErrorCode.KUAFU_NO_CONNECTION;
			}
			lastRank = getLastRank(jingji, redis);
			jingji.setLastRank(lastRank);
		}
		jingji.setGift(GameConstants.BOOLEAN_TRUE_TO_INT);
		kuafuJingjiDao.cacheUpdate(jingji, userRoleId);
		KfPaimingJiangliConfig config = kfPaimingJiangliConfigExportService.loadByRank(lastRank);
		if(config == null){
			//没有可领取的奖励 
			return AppErrorCode.JINGJI_NO_GIFT;	
		}
		
		//发放奖励
		accountExportService.incrCurrencyWithNotify(GoodsCategory.MONEY, config.getMoney(), userRoleId, LogPrintHandle.GET_KUAFU_JINGJI, LogPrintHandle.CBZ_KUAFU_JINGJI_CHANGE_TARGETS);
		roleExportService.incrExp(userRoleId, config.getExp()*1l);
		chengShenExportService.addSHZ(userRoleId, config.getShenhun());
		
		return AppErrorCode.OK;
	}
	
	private int getLastRank(KuafuJingji jingji,Redis redis){
		int lastRank = 0;
		if(lastRank < 1){
			Map<String, String> lrMap = redis.hgetAll(RedisKey.getRedisKuaFuJingJiLastRankKey(jingji.getUserRoleId()));
			if(lrMap != null && lrMap.size() > 0){
				if(DatetimeUtil.dayIsToday(CovertObjectUtil.obj2long(lrMap.get(GameConstants.REDIS_KUAFU_JINGJI_UPDATE_TIME)))){
					lastRank = CovertObjectUtil.object2int(lrMap.get(GameConstants.REDIS_KUAFU_JINGJI_RANK));
				}else{
					redis.del(RedisKey.getRedisKuaFuJingJiLastRankKey(jingji.getUserRoleId()));
				}
			}
		}
		if(jingji.getRank() < kfJingjiMingciZhanshiConfigExportService.getMaxRank()){
			Map<String, String> kfjj = redis.hgetAll(RedisKey.getRedisKuaFuJingJiRoleKey(jingji.getUserRoleId()));
			if(kfjj != null && kfjj.size() > 0){
				if(!DatetimeUtil.dayIsToday(CovertObjectUtil.obj2long(kfjj.get(GameConstants.REDIS_KUAFU_JINGJI_UPDATE_TIME)))){
					lastRank = CovertObjectUtil.object2int(kfjj.get(GameConstants.REDIS_KUAFU_JINGJI_RANK));
					Map<String, String> lrMap = new HashMap<>();
					lrMap.put(GameConstants.REDIS_KUAFU_JINGJI_UPDATE_TIME, GameSystemTime.getSystemMillTime()+"");
					lrMap.put(GameConstants.REDIS_KUAFU_JINGJI_RANK, lastRank+"");
					redis.hmset(RedisKey.getRedisKuaFuJingJiLastRankKey(jingji.getUserRoleId()), lrMap);
				}
			}
		}
		if(lastRank < 1){
			lastRank = kfJingjiMingciZhanshiConfigExportService.getMaxRank();
		}
		return lastRank;
	}
	
	/**
	 * 挑战
	 * @param userRoleId
	 * @param rank
	 * @return
	 */
	public Object[] tiaoZhan(Long userRoleId,int rank){
		//判断是否在挑战时间内
		KuafuJingji jingji = getKuafuJingji(userRoleId);
		if(jingji.getRank() == rank){
			return AppErrorCode.JINGJI_TARGET_CANNOT_TIAOZHAN;
		}
		if(rank > 3){
			if(!jingji.getFight1().equals(rank) && !jingji.getFight2().equals(rank) && !jingji.getFight3().equals(rank) && !jingji.getFight4().equals(rank)){
				//目标不可挑战
				return AppErrorCode.JINGJI_TARGET_CANNOT_TIAOZHAN;
			}
		}
		
		KfJingjiConfig config = kfJingjiConfigExportService.loadConfig();
		if(jingji.getCdTime() - GameSystemTime.getSystemMillTime() > config.getMaxCd()){
			return AppErrorCode.JINGJI_NOW_IS_IN_CD;// 正在CD中
		}
		if(jingji.getTiaozhanCount() >= config.getCishu()){
			return AppErrorCode.JINGJI_TODAY_IS_NO_TZ_COUNT;//没有挑战次数
		}
		if(stageControllExportService.inFuben(userRoleId)){
			return AppErrorCode.JINGJI_FUBEN_CANNOT_TIAOZHAN;//在副本中
		}
		Redis redis = GameServerContext.getRedis();
		if(redis == null){
			return AppErrorCode.KUAFU_NO_CONNECTION;
		}
		//获取对手信息
		Long targetRoleId = 0l;
		Map<String, String> target = redis.hgetAll(RedisKey.getRedisKuaFuJingJiRankKey(rank));
		if(target == null || target.size() < 1){
			targetRoleId = -rank*1l;
		}else{
			targetRoleId = CovertObjectUtil.object2Long(target.get(GameConstants.REDIS_KUAFU_JINGJI_USER_ROLE_ID));
		}
		//验证是否在战斗中，并且标记战斗
		if(!redis.setNx(RedisKey.getRedisKuaFuFightKey(targetRoleId), userRoleId.toString())){
			//对手正在战斗中
			return AppErrorCode.JINGJI_TARGET_IS_FIGHTING;
		}
		redis.expire(RedisKey.getRedisKuaFuFightKey(targetRoleId), 30);
		if(jingji.getRank() < kfJingjiMingciZhanshiConfigExportService.getMaxRank()){
			//上榜用户需要标记状态
			if(!redis.setNx(RedisKey.getRedisKuaFuFightKey(userRoleId), targetRoleId.toString())){
				//对手正在战斗中
				redis.del(RedisKey.getRedisKuaFuFightKey(targetRoleId));
				return AppErrorCode.JINGJI_YOU_IS_FIGHTING;
			}
			redis.expire(RedisKey.getRedisKuaFuFightKey(userRoleId), 30);
		}
		
		jingji.setTargetRole(targetRoleId);
		jingji.setTargetRank(rank);
		kuafuJingjiDao.cacheUpdate(jingji, userRoleId);
//		if(targetRoleId == 0){
//			jingji.setTargetRole(targetRoleId);
//		}
		//通知进入小黑屋
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.ENTER_SAFE_MAP_KF, MapType.KF_JINGJI_SAFE_MAP);
		return null;
	}
	
	/**
	 * 场景通知可以进行战斗
	 * @param userRoleId
	 * @param rank
	 */
	public void fight(Long userRoleId){
		KuafuJingji jingji = getKuafuJingji(userRoleId);
		jingji.setWatching(true);
		Redis redis = GameServerContext.getRedis();
		if(redis == null){
			BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_JINGJI_FIGHT_ERROR,1);
			return;
		}
		Long targerRoleId = jingji.getTargetRole();
		if (targerRoleId == null || targerRoleId == 0) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.KUAFU_JINGJI_FIGHT_ERROR,2);
			return;
		}
		jingji.setTargetRole(0);
		IJingjiFighter attFighter = createJingjiFighter(jingji,redis);
		jingji.setTiaozhanCount(jingji.getTiaozhanCount() + 1);
		KfJingjiConfig config = kfJingjiConfigExportService.loadConfig();
		long cdTime = jingji.getCdTime();
		if(cdTime > GameSystemTime.getSystemMillTime()){
			cdTime += config.getFightCd();
		}else{
			cdTime = GameSystemTime.getSystemMillTime() + config.getFightCd();
		}
		jingji.setCdTime(cdTime);
		kuafuJingjiDao.cacheUpdate(jingji, userRoleId);

		int answer = GameConstants.JINGJI_ANSWER_WIN;
		String respotr = null;
		IJingjiFighter defFighter = createJingjiFighter(targerRoleId,redis);
		String defName = "";
		if(defFighter != null){
			defName = defFighter.getName();
			JingjiFight fight = new JingjiFight();
			fight.init(attFighter, defFighter);
			respotr = fight.startFight();
			if(userRoleId.equals(fight.getLoser().getId())){
				answer = GameConstants.JINGJI_ANSWER_LOSE;
			}
		}
		int exp = 0;
		int shenhun = 0;
		if(answer == GameConstants.JINGJI_ANSWER_LOSE){
			exp = config.getLoseExp();
			shenhun = config.getLoseShenhun();
			redis.del(RedisKey.getRedisKuaFuFightKey(userRoleId));
			redis.del(RedisKey.getRedisKuaFuFightKey(targerRoleId));
			String key = RedisKey.getRedisKuaFuFightZhanBaoKey(userRoleId)+redis.generateId(GameConstants.REDIS_KUAFU_JINGJI_FIGHT_ID, GameConstants.REDIS_KUAFU_JINGJI_FIGHT_ID);
			redis.setex(key, GameConstants.REDIS_KUAFU_JINGJI_FIGHT_EXPIRE_TIME, getRedisReport(1, defName, 1, 0, 1));
			String key2 = RedisKey.getRedisKuaFuFightZhanBaoKey(targerRoleId)+redis.generateId(GameConstants.REDIS_KUAFU_JINGJI_FIGHT_ID, GameConstants.REDIS_KUAFU_JINGJI_FIGHT_ID);
			redis.setex(key2, GameConstants.REDIS_KUAFU_JINGJI_FIGHT_EXPIRE_TIME, getRedisReport(0, attFighter.getName(), 0, 0, 1));
		}else{
			exp = config.getWinExp();
			shenhun = config.getWinShenhun();
			int rank = jingji.getTargetRank();
			jingji.setTargetRank(0);
			Map<String,String> target = null;
			if(targerRoleId > 0){
				target = redis.hgetAll(RedisKey.getRedisKuaFuJingJiRoleKey(targerRoleId));
			}
			if(rank < jingji.getRank()){
				String key = RedisKey.getRedisKuaFuFightZhanBaoKey(userRoleId)+redis.generateId(GameConstants.REDIS_KUAFU_JINGJI_FIGHT_ID, GameConstants.REDIS_KUAFU_JINGJI_FIGHT_ID);
				redis.setex(key, GameConstants.REDIS_KUAFU_JINGJI_FIGHT_EXPIRE_TIME, getRedisReport(1, defName, 0, rank, 0));
				if(targerRoleId > 0){
					String key2 = RedisKey.getRedisKuaFuFightZhanBaoKey(targerRoleId)+redis.generateId(GameConstants.REDIS_KUAFU_JINGJI_FIGHT_ID, GameConstants.REDIS_KUAFU_JINGJI_FIGHT_ID);
					redis.setex(key2, GameConstants.REDIS_KUAFU_JINGJI_FIGHT_EXPIRE_TIME, getRedisReport(0, attFighter.getName(), 1, jingji.getRank(), 2));
				}
				//交换名次
				if(target != null){
					long updateTime = CovertObjectUtil.obj2long(target.get(GameConstants.REDIS_KUAFU_JINGJI_UPDATE_TIME));
					if(!DatetimeUtil.dayIsToday(updateTime)){
						// 记录昨日名次
						target.put(GameConstants.REDIS_KUAFU_JINGJI_UPDATE_TIME, GameSystemTime.getSystemMillTime()+"");
						Map<String, String> lrMap = new HashMap<>();
						lrMap.put(GameConstants.REDIS_KUAFU_JINGJI_UPDATE_TIME, GameSystemTime.getSystemMillTime()+"");
						lrMap.put(GameConstants.REDIS_KUAFU_JINGJI_RANK, rank+"");
						redis.hmset(RedisKey.getRedisKuaFuJingJiLastRankKey(targerRoleId), lrMap);
					}
				}
				if(jingji.getRank() < kfJingjiMingciZhanshiConfigExportService.getMaxRank()){
					Map<String,String> self = redis.hgetAll(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId));
					if(targerRoleId > 0){
						redis.hset(RedisKey.getRedisKuaFuJingJiRoleKey(targerRoleId), GameConstants.REDIS_KUAFU_JINGJI_RANK, jingji.getRank().toString());
						target.put(GameConstants.REDIS_KUAFU_JINGJI_RANK, jingji.getRank().toString());
						redis.hmset(RedisKey.getRedisKuaFuJingJiRankKey(jingji.getRank()), target);
					}else{
						redis.del(RedisKey.getRedisKuaFuJingJiRankKey(jingji.getRank()));
					}
					if(!DatetimeUtil.dayIsToday(CovertObjectUtil.obj2long(self.get(GameConstants.REDIS_KUAFU_JINGJI_UPDATE_TIME)))){
						// 记录昨日名次
						self.put(GameConstants.REDIS_KUAFU_JINGJI_UPDATE_TIME, GameSystemTime.getSystemMillTime()+"");
						Map<String, String> lrMap = new HashMap<>();
						lrMap.put(GameConstants.REDIS_KUAFU_JINGJI_UPDATE_TIME, GameSystemTime.getSystemMillTime()+"");
						lrMap.put(GameConstants.REDIS_KUAFU_JINGJI_RANK, jingji.getRank()+"");
						redis.hmset(RedisKey.getRedisKuaFuJingJiLastRankKey(userRoleId), lrMap);
					}
					self.put(GameConstants.REDIS_KUAFU_JINGJI_RANK, rank+"");
					jingji.setRank(rank);
					redis.hmset(RedisKey.getRedisKuaFuJingJiRankKey(rank), self);
					redis.hset(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId), GameConstants.REDIS_KUAFU_JINGJI_RANK, rank+"");
				}else{
					redis.del(RedisKey.getRedisKuaFuJingJiRoleKey(targerRoleId));
					redis.del(RedisKey.getRedisKuaFuJingJiAttributeKey(targerRoleId));
					jingji.setRank(rank);
					Map<String,String> self = createRedisInfo(jingji,attFighter);
					redis.hmset(RedisKey.getRedisKuaFuJingJiRankKey(rank), self);
					redis.hmset(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId), self);
				}
				refreshAttribute(jingji, redis);
				initFighter(jingji);
			}else{
				String key = RedisKey.getRedisKuaFuFightZhanBaoKey(userRoleId)+redis.generateId(GameConstants.REDIS_KUAFU_JINGJI_FIGHT_ID, GameConstants.REDIS_KUAFU_JINGJI_FIGHT_ID);
				redis.setex(key, GameConstants.REDIS_KUAFU_JINGJI_FIGHT_EXPIRE_TIME, getRedisReport(1, defName, 0, 0, 1));
				if(targerRoleId > 0){
					String key2 = RedisKey.getRedisKuaFuFightZhanBaoKey(targerRoleId)+redis.generateId(GameConstants.REDIS_KUAFU_JINGJI_FIGHT_ID, GameConstants.REDIS_KUAFU_JINGJI_FIGHT_ID);
					redis.setex(key2, GameConstants.REDIS_KUAFU_JINGJI_FIGHT_EXPIRE_TIME, getRedisReport(0, attFighter.getName(), 1, 0, 1));
				}
			}
			kuafuJingjiDao.cacheUpdate(jingji, userRoleId);
			redis.del(RedisKey.getRedisKuaFuFightKey(userRoleId));
			redis.del(RedisKey.getRedisKuaFuFightKey(targerRoleId));
		}
		//发放奖励
		roleExportService.incrExp(userRoleId, exp*1l);
		chengShenExportService.addSHZ(userRoleId, shenhun);
		List<Object[]> data = new ArrayList<>();
		if(respotr != null){
			data.add(new Object[]{ClientCmdType.KUAFU_JINGJI_TIAOZHAN, new Object[]{1,defFighter.getId(),defFighter.getConfigId(),defName,defFighter.getLevel(),defFighter.getZuoqi(),defFighter.getFightAttribute().getMaxHp(), respotr,defFighter.getChibang(),attFighter.getFightAttribute().getMaxHp()}});
		}else{
			data.add(new Object[]{ClientCmdType.KUAFU_JINGJI_FIGHT_ERROR, 3});
		}
		data.add(new Object[]{ClientCmdType.KUAFU_JINGJI_FIGHT_ANSWER, new Object[]{answer,jingji.getRank(),exp,shenhun}});
		//打包数据发送
		Object[] obj = CompressConfigUtil.compressAddCheckObject(data.toArray());
		BusMsgSender.send2One(userRoleId, (short)obj[0], obj[1]);
	}
	
	/**
	 * 玩家退出战斗
	 * @param userRoleId
	 */
	public void exitFight(Long userRoleId){
		KuafuJingji jingji = getKuafuJingji(userRoleId);
		if(!jingji.isWatching()){
			return;
		}
		jingji.setWatching(false);
		StageMsgSender.send2StageControl(userRoleId, InnerCmdType.S_APPLY_LEAVE_STAGE, userRoleId);
	}
	
	private String getRedisReport(int bei,String name,int win,int rank,int change){
		StringBuffer sb = new StringBuffer();
		sb.append(bei);
		sb.append(GameConstants.REDIS_KUAFU_JINGJI_REPORT_SPLIT);
		sb.append(name);
		sb.append(GameConstants.REDIS_KUAFU_JINGJI_REPORT_SPLIT);
		sb.append(win);
		sb.append(GameConstants.REDIS_KUAFU_JINGJI_REPORT_SPLIT);
		sb.append(rank);
		sb.append(GameConstants.REDIS_KUAFU_JINGJI_REPORT_SPLIT);
		sb.append(change);
		return sb.toString();
	}
	
	private Map<String,String> createRedisInfo(KuafuJingji jingji,IJingjiFighter fighter){
		Map<String,String> map = new HashMap<>();
		map.put(GameConstants.REDIS_KUAFU_JINGJI_USER_ROLE_ID, jingji.getUserRoleId().toString());
		map.put(GameConstants.REDIS_KUAFU_JINGJI_RANK, jingji.getRank().toString());
		map.put(GameConstants.REDIS_KUAFU_JINGJI_ZPLUS, fighter.getZpuls()+"");
		map.put(GameConstants.REDIS_KUAFU_JINGJI_UPDATE_TIME, GameSystemTime.getSystemMillTime()+"");
		map.put(GameConstants.REDIS_KUAFU_JINGJI_INFO_NAME, fighter.getName());
		map.put(GameConstants.REDIS_KUAFU_JINGJI_INFO_LEVEL, fighter.getLevel()+"");
		map.put(GameConstants.REDIS_KUAFU_JINGJI_INFO_CONFIG, fighter.getConfigId()+"");
		map.put(GameConstants.REDIS_KUAFU_JINGJI_INFO_ZUOQI, zuoQiExportService.getZuoQiInfoLevel(jingji.getUserRoleId()).toString());
		map.put(GameConstants.REDIS_KUAFU_JINGJI_INFO_CHIBANG, chiBangExportService.getChibangLevel(jingji.getUserRoleId()).toString());
		return map;
	}
	
	/**
	 * 创建竞技战斗角色
	 * @param roleJingji
	 * @return
	 */
	private IJingjiFighter createJingjiFighter(KuafuJingji jingji,Redis redis){
		Long userRoleId = jingji.getUserRoleId();
		if(jingji.getRank() < kfJingjiMingciZhanshiConfigExportService.getMaxRank()){
			//在榜玩家，去redis中查询属性
			byte[] attByte = redis.get(RedisKey.getRedisKuaFuJingJiAttributeKey(userRoleId));
			if(attByte != null && attByte.length > 0){
				RoleWrapper role = roleExportService.getLoginRole(userRoleId);
				JingjiAttribute2 attribute = (JingjiAttribute2)SerializableUtil.javaDeserialize(attByte);
				JingjiFighter2 fighter = new JingjiFighter2(userRoleId, role.getName(),role.getConfigId());
				fighter.setJingjiattribute(attribute);
				fighter.setLevel(role.getLevel());
				return fighter;
			}
		}
		String stageId = stageControllExportService.getCurStageId(userRoleId);
		if(stageId == null){
			return null;
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return null;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return null;
		}
		JingjiAttribute2 attribute = new JingjiAttribute2();
		List<RoleSkill> skillList = roleSkillExportService.loadRoleSkill(userRoleId, GameConstants.SKILL_TYPE_ZHUDONG);
		if(skillList != null && skillList.size() > 0){
			for (RoleSkill roleSkill : skillList) {
				ISkill skill = SkillManager.getManager().getSkill(roleSkill.getSkillId(), roleSkill.getLevel());
				if(skill != null && skill.getSkillConfig().getSkillFireType() == SkillFireType.NORMAL){
					attribute.addSkill(skill.getId());
				}
			}
		}
		attribute.setAttribute(KuafuRoleUtil.getRoleAllAttributeExceptBuff(role));
		attribute.setZplus(role.getFightAttribute().getZhanLi());
		JingjiFighter2 fighter = new JingjiFighter2(userRoleId, role.getName(),role.getBusinessData().getRoleConfigId());
		fighter.setJingjiattribute(attribute);
		fighter.setLevel(role.getLevel());
		return fighter;
	}
	/**
	 * 创建竞技战斗角色
	 * @param roleJingji
	 * @return
	 */
	private IJingjiFighter createJingjiFighter(Long userRoleId,Redis redis){
		if(userRoleId < 0){
			//机器人
			KfRobotConfig robot = kfRobotConfigExportService.loadById(0-userRoleId.intValue());
			if(robot == null){
				return null;
			}
			String name = robot.getName();
			Integer configId = robot.getConfigId();
			JingjiAttribute2 attribute = new JingjiAttribute2();
			attribute.setAttribute(robot.getAttribute());
			attribute.setSkills(new ReadOnlyList<>(robot.getSkills()));
			attribute.setZplus(robot.getZplus());
			JingjiFighter2 fighter = new JingjiFighter2(userRoleId, name,configId);
			fighter.setJingjiattribute(attribute);
			fighter.setZuoqi(robot.getZuoqi());
			fighter.setLevel(robot.getLevel());
			return fighter;
		}
		byte[] attByte = redis.get(RedisKey.getRedisKuaFuJingJiAttributeKey(userRoleId));
		if(attByte == null || attByte.length < 1){
			return null;
		}
		String name = redis.hget(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId), GameConstants.REDIS_KUAFU_JINGJI_INFO_NAME);
		Integer configId = CovertObjectUtil.object2int(redis.hget(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId), GameConstants.REDIS_KUAFU_JINGJI_INFO_CONFIG));
		Integer zuoqi = CovertObjectUtil.object2int(redis.hget(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId), GameConstants.REDIS_KUAFU_JINGJI_INFO_ZUOQI));
		Integer chibang = CovertObjectUtil.object2int(redis.hget(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId), GameConstants.REDIS_KUAFU_JINGJI_INFO_CHIBANG));
		Integer level = CovertObjectUtil.object2int(redis.hget(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId), GameConstants.REDIS_KUAFU_JINGJI_INFO_LEVEL));
		JingjiAttribute2 attribute = (JingjiAttribute2)SerializableUtil.javaDeserialize(attByte);
		JingjiFighter2 fighter = new JingjiFighter2(userRoleId, name,configId);
		fighter.setJingjiattribute(attribute);
		fighter.setZuoqi(zuoqi);
		fighter.setChibang(chibang);
		fighter.setLevel(level);
		return fighter;
	}
	
	private void refreshAttribute(KuafuJingji jingji,Redis redis){
		if(jingji.getRank() == kfJingjiMingciZhanshiConfigExportService.getMaxRank()){
			return;
		}
		Long userRoleId = jingji.getUserRoleId();
		String stageId = stageControllExportService.getCurStageId(userRoleId);
		if(stageId == null){
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return;
		}
		Role role = stage.getElement(userRoleId, ElementType.ROLE);
		if(role == null){
			return;
		}
		byte[] attByte = redis.get(RedisKey.getRedisKuaFuJingJiAttributeKey(userRoleId));
		if(attByte != null && attByte.length > 0){
			JingjiAttribute2 attribute = (JingjiAttribute2)SerializableUtil.javaDeserialize(attByte);
			if(attribute.getZplus() >= role.getFightAttribute().getZhanLi()){
				return;
			}
		}
		JingjiAttribute2 attribute = new JingjiAttribute2();
		List<RoleSkill> skillList = roleSkillExportService.loadRoleSkill(userRoleId, GameConstants.SKILL_TYPE_ZHUDONG);
		if(skillList != null && skillList.size() > 0){
			for (RoleSkill roleSkill : skillList) {
				ISkill skill = SkillManager.getManager().getSkill(roleSkill.getSkillId(), roleSkill.getLevel());
				if(skill != null && skill.getSkillConfig().getSkillFireType() == SkillFireType.NORMAL){
					attribute.addSkill(skill.getId());
				}
			}
		}
		attribute.setAttribute(KuafuRoleUtil.getRoleAllAttributeExceptBuff(role));
		attribute.setZplus(role.getFightAttribute().getZhanLi());
		redis.hset(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId),GameConstants.REDIS_KUAFU_JINGJI_ZPLUS,role.getFightAttribute().getZhanLi()+"");
		redis.hset(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId),GameConstants.REDIS_KUAFU_JINGJI_INFO_NAME,role.getName());
		redis.hset(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId),GameConstants.REDIS_KUAFU_JINGJI_INFO_LEVEL,role.getLevel()+"");
		redis.hset(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId),GameConstants.REDIS_KUAFU_JINGJI_INFO_CONFIG,role.getBusinessData().getRoleConfigId()+"");
		redis.hset(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId),GameConstants.REDIS_KUAFU_JINGJI_INFO_ZUOQI,role.getZuoQiLevel()+"");
		redis.hset(RedisKey.getRedisKuaFuJingJiRoleKey(userRoleId),GameConstants.REDIS_KUAFU_JINGJI_INFO_CHIBANG,role.getChiBangShowId()+"");
		redis.set(RedisKey.getRedisKuaFuJingJiAttributeKey(userRoleId), SerializableUtil.javaSerialize(attribute));
	}
	
	public Object[] buyCount(long userRoleId){
		KuafuJingji jingji = getKuafuJingji(userRoleId);
		if(jingji.getTiaozhanCount() < 1){
			return AppErrorCode.TIAOZHAN_COUNT_IS_MAX;//次数已满
		}
		KfJingjiConfig config = kfJingjiConfigExportService.loadConfig();
		Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, config.getCishuGold(), userRoleId, LogPrintHandle.CONSUME_KUAFU_JINGJI, true, LogPrintHandle.CBZ_KUAFU_JINGJI_BUY_COUNT);
		if(result != null){
			return result;//元宝不足
		}else{
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,config.getCishuGold(),LogPrintHandle.CONSUME_KUAFU_JINGJI,QQXiaoFeiType.CONSUME_KUAFU_JINGJI,1});
			}
		}
		jingji.setTiaozhanCount(jingji.getTiaozhanCount() - 1);
		kuafuJingjiDao.cacheUpdate(jingji, userRoleId);
		
		return new Object[]{1,config.getCishu() - jingji.getTiaozhanCount()};
	}
	
	public Object[] miaoCd(Long userRoleId){
		KuafuJingji jingji = getKuafuJingji(userRoleId);
		long time = jingji.getCdTime() - GameSystemTime.getSystemMillTime();
		if(time <= 0){
			return AppErrorCode.JINGJI_NOT_IN_CD;//不需要秒
		}
		int minute = (int)(time / 60000);
		if(time % 60000 > 0){
			minute ++;
		}
		KfJingjiConfig config = kfJingjiConfigExportService.loadConfig();
		int gold = minute * config.getCdGold();
		Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, gold, userRoleId, LogPrintHandle.CONSUME_KUAFU_JINGJI, true, LogPrintHandle.CBZ_KUAFU_JINGJI_MIAO_CD);
		if(result != null){
			return result;//元宝不足
		}else{
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,gold,LogPrintHandle.CONSUME_KUAFU_JINGJI,QQXiaoFeiType.CONSUME_KUAFU_JINGJI,1});
			}
		}
		jingji.setCdTime(GameSystemTime.getSystemMillTime());
		kuafuJingjiDao.cacheUpdate(jingji, userRoleId);
		return AppErrorCode.OK;
	}
	
	public int canRciveGift(Long userRoleId){
		KuafuJingji jingji = getKuafuJingji(userRoleId);
		if(jingji.getGift().equals(GameConstants.BOOLEAN_TRUE_TO_INT)){
			return 0;
		}
		int lastRank = jingji.getLastRank();
		if(lastRank < 1){
			//未记录昨日排名，虚到redis中获取
			Redis redis = GameServerContext.getRedis();
			if(redis == null){
				return 0;
			}
			lastRank = getLastRank(jingji, redis);
			jingji.setLastRank(lastRank);
		}
		if(lastRank < kfJingjiMingciZhanshiConfigExportService.getMaxRank()){
			return 1;
		}
		return 0;
	}
	
	public Object[] giftInfo(Long userRoleId){
		KuafuJingji jingji = getKuafuJingji(userRoleId);
		int lastRank = jingji.getLastRank();
		if(lastRank < 1){
			//未记录昨日排名，虚到redis中获取
			Redis redis = GameServerContext.getRedis();
			if(redis == null){
				return AppErrorCode.KUAFU_NO_CONNECTION;
			}
			lastRank = getLastRank(jingji, redis);
			jingji.setLastRank(lastRank);
		}
		return new Object[]{lastRank,jingji.getGift()};
	}
	
	public Object[] getRankPlayers(int rank){
		KfPaimingJiangliConfig config = kfPaimingJiangliConfigExportService.loadById(rank);
		if(config == null || config.getShow() < 1){
			return null;
		}
		Redis redis = GameServerContext.getRedis();
		if(redis == null){
			return null;
		}
		int min = kfPaimingJiangliConfigExportService.getMinRank(rank);
		List<Object[]> list = new ArrayList<>();
		for (int i = min; i < rank; i++) {
			String name = redis.hget(RedisKey.getRedisKuaFuJingJiRankKey(i), GameConstants.REDIS_KUAFU_JINGJI_INFO_NAME);
			if(name != null && name.length() > 0){
				list.add(new Object[]{i,name});
			}
		}
		return new Object[]{rank,list.toArray()};
	}
	
	public Object[] getFightRecords(Long userRoleId){
		Redis redis = GameServerContext.getRedis();
		if(redis == null){
			return AppErrorCode.EMPTY_OBJECTS;
		}
		Set<String> keys = redis.keys(RedisKey.getRedisKuaFuFightZhanBaoKey(userRoleId)+"*");
		Map<Integer,String> keyMap = new HashMap<>();
		List<Integer> ids = new ArrayList<>();
		for (String key : keys) {
			Integer id = CovertObjectUtil.object2int(key.substring(key.lastIndexOf("_")+1));
			keyMap.put(id, key);
			ids.add(id);
		}
		Collections.sort(ids);
		int max = Math.min(ids.size(), GameConstants.KUAFU_JINGJI_MAX_COUNT);
		if(max < 1){
			return AppErrorCode.EMPTY_OBJECTS;
		}
		String[] keyList = new String[max];
		for (int i = 0; i < max; i++) {
			Integer id = ids.get(ids.size() - i - 1);
			keyList[i] = keyMap.get(id);
		}
		List<String> repotrs = redis.mget(keyList);
		List<Object[]> result = new ArrayList<>();
		for (String report : repotrs) {
			String[] info = report.split(GameConstants.REDIS_KUAFU_JINGJI_REPORT_SPLIT);
			result.add(new Object[]{
					CovertObjectUtil.object2int(info[0]),
					info[1],
					CovertObjectUtil.object2int(info[2]),
					CovertObjectUtil.object2int(info[3]),
					CovertObjectUtil.object2int(info[4])
			});
		}
		
		return result.toArray();
	}
}
