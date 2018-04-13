package com.junyou.bus.vip.service;

import java.util.concurrent.TimeUnit;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.bus.laba.service.LaBaService;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.export.ExpConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.dao.RoleVipInfoDao;
import com.junyou.bus.vip.entity.RoleVipInfo;
import com.junyou.bus.vip.entity.VipConfig;
import com.junyou.bus.vip.export.VipConfigExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;
import com.kernel.data.accessor.AccessType;

@Service
public class RoleVipInfoService {

	@Autowired
	private RoleVipInfoDao roleVipInfoDao;
	@Autowired
	private VipConfigExportService vipConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private BusScheduleExportService busScheduleExportService;
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
	@Autowired
	private LaBaService laBaService;
	
	private RoleVipInfo createRoleVipInfo(Long userRoleId){
		RoleVipInfo roleVipInfo = new RoleVipInfo();
		roleVipInfo.setUserRoleId(userRoleId);
		roleVipInfo.setLevelGift(0);
		roleVipInfo.setWeekGift(0);
		roleVipInfo.setVipExp(0l);
		roleVipInfo.setVipLevel(0);
		roleVipInfo.setVipId(0);
		roleVipInfo.setNextWeekTime(DatetimeUtil.getNextWeek(2));
		roleVipInfo.setExpireTime(0l);
		return roleVipInfo;
	}
	
	public void onlineHandle(Long userRoleId){
		RoleVipInfo roleVipInfo = roleVipInfoDao.cacheAsynLoad(userRoleId, userRoleId);
		if(roleVipInfo.getExpireTime() > 0){
			long cur = GameSystemTime.getSystemMillTime();
			long needTime = roleVipInfo.getExpireTime() - cur;
			if(needTime >= 0){
				scheduleVipTimeOut(userRoleId, needTime);
			}else{//限时VIP到期
				roleVipInfo.setExpireTime(0l);
				roleVipInfo.setVipId(0);
				roleVipInfoDao.cacheUpdate(roleVipInfo, userRoleId);
			}
		}
		int vipLevel = roleVipInfo.getVipLevel();
		VipConfig config = vipConfigExportService.getVipConfigByLevel(vipLevel + 1);
		int vipId = 0;
		while(config != null){
			if(config.getNeedExp() > roleVipInfo.getVipExp()){
				break;
			}
			vipLevel = config.getLevel();
			vipId = config.getOrder();
			config = vipConfigExportService.getVipConfigByLevel(vipLevel + 1);
		}
		if(vipLevel > roleVipInfo.getVipLevel()){
			roleVipInfo.setVipId(vipId);
			roleVipInfo.setVipLevel(vipLevel);
			roleVipInfoDao.cacheUpdate(roleVipInfo, userRoleId);
		}
	}
	
	private void scheduleVipTimeOut(Long userRoleId,long time){
		BusTokenRunable runable = new BusTokenRunable(userRoleId, InnerCmdType.VIP_TIME_OUT, userRoleId);
		busScheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPONENET_VIP, runable, (int)time, TimeUnit.MILLISECONDS);
	}
	
	public RoleVipWrapper getRoleVipInfo(Long userRoleId){
		RoleVipInfo roleVipInfo = roleVipInfoDao.cacheLoad(userRoleId, userRoleId);
		if(roleVipInfo != null){
			return new RoleVipWrapper(roleVipInfo);
		}
		return null;
	}
	
	public RoleVipWrapper getRoleVipInfoFromDB(Long userRoleId){
		RoleVipInfo roleVipInfo = roleVipInfoDao.load(userRoleId, userRoleId, AccessType.getDirectDbType());
		if(roleVipInfo != null){
			return new RoleVipWrapper(roleVipInfo);
		}
		return null;
	}
	/**
	 * 创建信息
	 * @param userRoleId
	 */
	public void createRoleVipInfoDB(Long userRoleId){
		RoleVipInfo roleVipInfo = createRoleVipInfo(userRoleId);
		roleVipInfoDao.insert(roleVipInfo, userRoleId, AccessType.getDirectDbType());
	}
	
	/**
	 * 获取角色信息
	 * @param userRoleId
	 * @return
	 */
	private RoleVipInfo getVipInfo(Long userRoleId){
		RoleVipInfo roleVipInfo = roleVipInfoDao.cacheAsynLoad(userRoleId, userRoleId);
		if(roleVipInfo == null){
			roleVipInfo = createRoleVipInfo(userRoleId);
			roleVipInfoDao.cacheInsert(roleVipInfo, userRoleId);
		}else if(roleVipInfo.getNextWeekTime() == null || GameSystemTime.getSystemMillTime() >= roleVipInfo.getNextWeekTime()){
			roleVipInfo.setWeekGift(0);
			roleVipInfo.setNextWeekTime(DatetimeUtil.getNextWeek(2));
			roleVipInfoDao.cacheUpdate(roleVipInfo, userRoleId);
		}
		return roleVipInfo;
	}
	/**
	 * 设置VIP等级
	 * @param userRoleId
	 * @param vipLevel
	 */
	public void setVipLevel(Long userRoleId,int vipLevel){
		RoleVipInfo vipInfo = getVipInfo(userRoleId);
		VipConfig config = vipConfigExportService.getVipConfigByLevel(vipLevel);
		if(config == null){
			return;
		}
		vipInfo.setVipLevel(vipLevel);
		vipInfo.setVipId(config.getOrder());
		vipInfo.setExpireTime(0l);
		roleVipInfoDao.cacheUpdate(vipInfo, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.VIP_LEVEL_CHANGE, vipLevel);
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.VIP_LEVEL_UPGRADE, config.getOrder());
	}
	/**
	 * 获取玩家特权情况
	 * @param userRoleId
	 * @param tequanId
	 * @return
	 */
	public Integer getTequanInfo(Long userRoleId,String tequanId){
		RoleVipInfo roleVipInfo = getVipInfo(userRoleId);
		VipConfig config = null;
		if(roleVipInfo.getVipId() > 0){
			config = vipConfigExportService.getVipConfigById(roleVipInfo.getVipId());
		}else{
			config = vipConfigExportService.getVipConfigByLevel(roleVipInfo.getVipLevel());
		}
		if(config == null || config.getTequanMap() == null){
			//容错配置不存在
			return 0;
		}
		if(config.getTequanMap().containsKey(tequanId)){
			return config.getTequanMap().get(tequanId);
		}
		return 0;
	}
	/**
	 * 获取玩家特权情况(直接访问数据库)
	 * @param userRoleId
	 * @param tequanId
	 * @return
	 */
	public Integer getTequanInfoFromDb(Long userRoleId,String tequanId){
		RoleVipInfo roleVipInfo = roleVipInfoDao.load(userRoleId, userRoleId, AccessType.getDirectDbType());
		VipConfig config = null;
		if(roleVipInfo.getVipId() > 0){
			config = vipConfigExportService.getVipConfigById(roleVipInfo.getVipId());
		}else{
			config = vipConfigExportService.getVipConfigByLevel(roleVipInfo.getVipLevel());
		}
		return config.getTequanMap().get(tequanId);
	}
	/**
	 * 获取玩家VIP礼包领取状态
	 * @param userRoleId
	 * @return
	 */
	public Object[] getRoleVipGiftState(Long userRoleId){
		RoleVipInfo vip = getVipInfo(userRoleId);
		return new Object[]{vip.getWeekGift(),vip.getLevelGift(),vip.getNextWeekTime()};
	}
	
	/**
	 * VIP特权到期
	 * @param userRoleId
	 */
	public void vipTimeOut(Long userRoleId){
		RoleVipInfo roleVipInfo = roleVipInfoDao.cacheLoad(userRoleId, userRoleId);
		long cur = GameSystemTime.getSystemMillTime();
		long needTime = roleVipInfo.getExpireTime() - cur;
		if(needTime >= 0){
			scheduleVipTimeOut(userRoleId, needTime);
		}else{
			roleVipInfo.setExpireTime(0l);
			roleVipInfo.setVipId(0);
			roleVipInfoDao.cacheUpdate(roleVipInfo, userRoleId);
			BusMsgSender.send2One(userRoleId, ClientCmdType.VIP_LEVEL_CHANGE, 0);
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.VIP_LEVEL_UPGRADE, 0);
		}
	}
	
	/**
	 * 玩家领取VIP等级礼包
	 * @param userRoleId
	 * @return
	 */
	public Object[] reciveRoleVipLevelGift(Long userRoleId,Integer id){
		if(id < 1){
			return AppErrorCode.VIP_ASK_DATA_ERROR;//请求异常
		}
		RoleVipInfo vip = getVipInfo(userRoleId);
		if(vip.getVipLevel() < id){
			return AppErrorCode.VIP_NOT_ENOUGH_LEVEL;
		}
		
		int state = vip.getLevelGift();
		if(!BitOperationUtil.calState(state, id - 1)){
			return AppErrorCode.VIP_GIFT_HAS_REVICED;//奖励已领取过
		}
		
		VipConfig config = vipConfigExportService.getVipConfigByLevel(id);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(config.getLevelItem() == null || config.getLevelItem().size() < 1){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Object[] result = roleBagExportService.checkPutInBagVo(config.getLevelItem(), userRoleId);
		if(result != null){
			return result;
		}
		
		state = BitOperationUtil.chanageState(state, id - 1);
		vip.setLevelGift(state);
		roleVipInfoDao.cacheUpdate(vip, userRoleId);
		
		roleBagExportService.putInBagVo(config.getLevelItem(), userRoleId, GoodsSource.VIP_LEVEL_GIFT, true);
		
		return new Object[]{1,id};
	}
	
	/**
	 * 玩家领取VIP周礼包
	 * @param userRoleId
	 * @return
	 */
	public Object[] reciveRoleVipWeekGift(Long userRoleId){
		RoleVipInfo vip = getVipInfo(userRoleId);
		if(vip.getVipLevel() < 1){
			return AppErrorCode.VIP_NOT_ENOUGH_LEVEL;
		}
		
		if(vip.getWeekGift() != GameConstants.BOOLEAN_FALSE_TO_INT){
			return AppErrorCode.VIP_GIFT_HAS_REVICED;//奖励已领取过
		}
		
		VipConfig config = vipConfigExportService.getVipConfigByLevel(vip.getVipLevel());
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Object[] result = roleBagExportService.checkPutInBag(config.getWeekItem(), userRoleId);
		if(result != null){
			return result;
		}
		
		vip.setWeekGift(GameConstants.BOOLEAN_TRUE_TO_INT);
		long nextTime = DatetimeUtil.getNextWeek(2);
		vip.setNextWeekTime(nextTime);
		roleVipInfoDao.cacheUpdate(vip, userRoleId);
		
		roleBagExportService.putInBag(config.getWeekItem(), userRoleId, GoodsSource.VIP_WEEK_GIFT, true);
		
		return new Object[]{1,nextTime};
	}
	
	/**
	 * 增加VIP经验
	 * @param userRoleId
	 * @param vipExp
	 * @param busMsgQueue
	 */
	public void rechargeVipExp(Long userRoleId,long vipExp,BusMsgQueue busMsgQueue){
		if(vipExp < 1){
			return;
		}
		RoleVipInfo vip = getVipInfo(userRoleId);
		long curExp = vip.getVipExp() + vipExp;
		vip.setVipExp(curExp);
		int vipLevel = vip.getVipLevel();
		VipConfig config = vipConfigExportService.getVipConfigByLevel(vip.getVipLevel() + 1);
		int vipId = 0;
		while(config != null){
			if(config.getNeedExp() > curExp){
				break;
			}
			vipLevel = config.getLevel();
			vipId = config.getOrder();
			config = vipConfigExportService.getVipConfigByLevel(vipLevel + 1);
		}
		
		if(vipLevel > vip.getVipLevel()){
			vip.setVipLevel(vipLevel);
			vip.setVipId(vipId);
			busMsgQueue.addMsg(userRoleId, ClientCmdType.VIP_LEVEL_CHANGE, vipLevel);
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.VIP_LEVEL_UPGRADE, vipId);
			//活跃度lxn
			if(vip.getVipLevel()>=5){
				huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A13);
			}
			//成就
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_VIP, vipLevel});
				//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_VIP, vipLevel);
				
				laBaService.checkVipLevelChange(userRoleId);
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
			
		}
		
		vip.setVipExp(curExp);
		busMsgQueue.addMsg(userRoleId, ClientCmdType.VIP_EXP_CHANGE, curExp);
		
		roleVipInfoDao.cacheUpdate(vip, userRoleId);
		
		//TODO 日志
	}
	
	
}
