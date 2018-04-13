package com.junyou.bus.offlineexp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.offlineexp.configure.LiXianJingYanConfigExportService;
import com.junyou.bus.offlineexp.dao.OfflineExpDao;
import com.junyou.bus.offlineexp.entity.OfflineExp;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.OfflineExpLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class OfflineExpService {

	@Autowired
	private OfflineExpDao offlineExpDao;
	@Autowired
	private LiXianJingYanConfigExportService liXianJingYanConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private AccountExportService accountExportService;

	/**
	 * 离线经验初始化
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] getOfflineExpInfo(Long userRoleId) {
		OfflineExp exp = getOfflineExp(userRoleId);
		return new Object[] { exp.getOfflineTotal(), exp.getOfflineExp() };
	}
	
	/**
	 * 根据类型获取倍率
	 * @param type
	 * @return
	 */
	private int getRateByType(int type){
		if(type == 1){
			return 2;
		}else if(type == 2){
			return 5;
		}
		return 1;
	}

	/**
	 * 根据客户端传递奖励类型，领取离线经验奖励
	 * 离线达到10分钟才可以领取
	 * 离线最多时间达到12小时
	 * @param userRoleId
	 * @param type
	 *            (经验倍数)
	 * @return
	 */
	public Object[] getOfflineExpReward(Long userRoleId, Integer type) {
		if (type < 0 || type > 2) {
			return AppErrorCode.PARAMETER_ERROR;
		}
		
		OfflineExp offlineExp = getOfflineExp(userRoleId);
		
//		// 离线时间是否达到10分钟 
//		if (offlineExp.getOfflineTotal() < GameConstants.OFFLINE_EXP_LESS_TIME * 60 * 1000l) {
//			return AppErrorCode.OFFLINE_TIME_LESS;
//		}
				
		// 判断领取条件是否满足
		if (type == 1 && roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_OFFLINE_LIXIAN2) < 1) {
			return AppErrorCode.VIP_NOT_ENOUGH_LEVEL;
		}else if (type == 2) {
			if (roleVipInfoExportService.getVipTequan(userRoleId, GameConstants.VIP_OFFLINE_LIXIAN3) < 1) {
				return AppErrorCode.VIP_NOT_ENOUGH_LEVEL;
			}
			if(null != accountExportService.isEnought(GoodsCategory.GOLD, GameConstants.OFFLINE_GOLD_CONSUME, userRoleId)){
				return AppErrorCode.YB_ERROR;
			} 
		}
		
		long thisOfflineExp = offlineExp.getOfflineExp()*getRateByType(type);
		offlineExp.setOfflineTotal(0l);
		offlineExp.setOfflineExp(0l);
		offlineExpDao.cacheUpdate(offlineExp, userRoleId);
		
		//扣除元宝
		if(type == 2){
			accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, GameConstants.OFFLINE_GOLD_CONSUME, userRoleId, LogPrintHandle.CONSUME_OFFLINE_EXP, true, LogPrintHandle.CBZ_OFFLINE_EXP);
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,GameConstants.OFFLINE_GOLD_CONSUME,LogPrintHandle.CONSUME_OFFLINE_EXP,QQXiaoFeiType.CONSUME_OFFLINE_EXP,1});
			}
		}
		
		roleBagExportService.incrNumberWithNotify(GoodsCategory.EXP, thisOfflineExp, userRoleId, LogPrintHandle.GET_OFFLINE_EXP, LogPrintHandle.GBZ_OFFLINE_EXP);
		//打印日志
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		GamePublishEvent.publishEvent(new OfflineExpLogEvent(userRoleId,role.getName(),type,offlineExp.getOfflineTotal()));
		return new Object[]{AppErrorCode.SUCCESS,type};
	}

	/**
	 * 玩家登陆操作
	 * 
	 * @param userRoleId
	 */
	public void login(Long userRoleId) {
		RoleWrapper roleW = roleExportService.getLoginRole(userRoleId);
		if(roleW.getOfflineTime().longValue() == 0l){
			return;
		}
		
		OfflineExp offlineExp = getOfflineExp(userRoleId);
		long offlineTotalTime = GameConstants.OFFLINE_EXP_TIME_LIMIT * 3600 * 1000l;
		if (offlineExp.getOfflineTotal() >= offlineTotalTime){			
			return;
		}
		
		// 本次离线时间
		long d = GameSystemTime.getSystemMillTime() - roleW.getOfflineTime();
		// 不足一分钟不做记录
		if(d < 60*1000l){
			return;
		}
		
		// 加载配置出错
		Long exp = liXianJingYanConfigExportService.getExpByLvl(roleW.getLevel());
		if(exp == null){
			return;
		}
		
		long currOfflineTime = offlineExp.getOfflineTotal() + d;
		if (currOfflineTime >= offlineTotalTime){
			d = offlineTotalTime - offlineExp.getOfflineTotal();
			currOfflineTime = offlineTotalTime;
		}
		// 离线总时间
		offlineExp.setOfflineTotal(currOfflineTime);
		// 离线登陆时的经验
		int minute = (int) (d / (60 * 1000));
		exp = exp * minute;
		offlineExp.setOfflineExp(offlineExp.getOfflineExp()+exp);
		// 上次领取奖励时间重置
		offlineExpDao.cacheUpdate(offlineExp, userRoleId);
	}

	/**
	 * 获取离线经验实体
	 * 
	 * @param userRoleId
	 * @return
	 */
	private OfflineExp getOfflineExp(Long userRoleId) {
		OfflineExp offlineExp = offlineExpDao.cacheAsynLoad(userRoleId,userRoleId);
		if (offlineExp == null) {
			offlineExp = new OfflineExp();
			offlineExp.setOfflineTotal(0l);
			offlineExp.setOfflineExp(0l);
			offlineExp.setUserRoleId(userRoleId);
			offlineExpDao.cacheInsert(offlineExp, userRoleId);
		}
		return offlineExp;
	}

	
	/**
	 * 获取离线是否有奖励
	 * @param userRoleId
	 * @return 返回状态值：没有奖励 = 0 ，有奖励 = 2的2次方
	 */
	public int getOfflineStateValue(Long userRoleId){
		//int(0) 二进制   奖励 有为1，无为0  顺序为  签到(0)，在线(1)，离线(2)，七登(3)
		int state = 0;
		OfflineExp offlineExp = getOfflineExp(userRoleId);
		if (offlineExp.getOfflineExp() <= 0 && offlineExp.getOfflineTotal() < GameConstants.OFFLINE_EXP_LESS_TIME * 60 * 1000l) {
			//离线时间不到10分钟，直接返回无奖励
			return state;
		}else{
			return 4;//离线(2) 2的2次方
		}
	}
}
