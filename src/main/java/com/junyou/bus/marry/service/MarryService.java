package com.junyou.bus.marry.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.entity.RoleAccount;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.marry.configure.export.DingHunConfigServiceExport;
import com.junyou.bus.marry.configure.export.JieHunConfigServiceExport;
import com.junyou.bus.marry.configure.export.LoveLevelConfigServiceExport;
import com.junyou.bus.marry.dao.RoleMarryInfoDao;
import com.junyou.bus.marry.entity.DingHunConfig;
import com.junyou.bus.marry.entity.JieHunConfig;
import com.junyou.bus.marry.entity.LoveLevelConfig;
import com.junyou.bus.marry.entity.RoleMarryInfo;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.export.JueSeDuiZhaoBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.MarryPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.export.TeamExportService;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.accessor.AccessType;

/**
 * @author LiuYu
 * 2015-8-10 下午3:58:43
 */
@Service
public class MarryService {
	
	@Autowired
	private RoleMarryInfoDao roleMarryInfoDao;
	@Autowired
	private TeamExportService teamExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private JueSeDuiZhaoBiaoConfigExportService jueSeDuiZhaoBiaoConfigExportService;
	@Autowired
	private SessionManagerExportService sessionManagerExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private DingHunConfigServiceExport dingHunConfigServiceExport;
	@Autowired
	private JieHunConfigServiceExport jieHunConfigServiceExport;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private LoveLevelConfigServiceExport loveLevelConfigServiceExport;
	@Autowired
	private BusScheduleExportService busScheduleExportService;
	@Autowired
	private AccountExportService accountExportService;
	
	private Map<Long,Long> dingMap = new ConcurrentHashMap<>();//订婚map<目标id，发起人id>
	
	public List<RoleMarryInfo> initRoleMarryInfo(Long userRoleId) {
		return roleMarryInfoDao.initRoleMarryInfo(userRoleId);
	}
	
	private void startAddSchedule(Long userRoleId){
		MarryPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MARRY);
		BusTokenRunable runable = new BusTokenRunable(userRoleId, InnerCmdType.MARRY_SCHEDULE_ADD,null);
		busScheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPONENT_MARRY, runable, config.getAddLoveTime(), TimeUnit.MINUTES);
	}
	private void cancelAddSchedule(Long userRoleId){
		busScheduleExportService.cancelSchedule(userRoleId.toString(), GameConstants.COMPONENT_MARRY);
	}
	
	private void startDivorceSchedule(Long userRoleId,int time){
		BusTokenRunable runable = new BusTokenRunable(userRoleId, InnerCmdType.MARRY_SCHEDULE_DIVOCRE,null);
		busScheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPONENT_DIVORCE, runable, time, TimeUnit.SECONDS);
	}
	private void cancelDivorceSchedule(Long userRoleId){
		busScheduleExportService.cancelSchedule(userRoleId.toString(), GameConstants.COMPONENT_DIVORCE);
	}
	
	public void onlineHandle(Long userRoleId){
		//上线启动定时
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || info.getState() == GameConstants.MARRY_STATE_NO){
			return;
		}
		boolean online = sessionManagerExportService.isOnline(info.getTargetRoleId());
		if(online){
			RoleMarryInfo target = roleMarryInfoDao.cacheLoad(info.getTargetRoleId(), info.getTargetRoleId());
			if(target != null){
				target.setTargetInfo(null);
			}
		}
		if(info.getState() == GameConstants.MARRY_STATE_DING){
			//启动缘分增加定时
			if(online){
				startAddSchedule(userRoleId);
			}
		}else if(info.getState() == GameConstants.MARRY_STATE_MARRY){
			//启动亲密度定时
			if(online){
				startAddSchedule(userRoleId);
				startAddSchedule(info.getTargetRoleId());
			}
		}else if(info.getState() == GameConstants.MARRY_STATE_DIVORCE){
			MarryPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MARRY);
			long endTime = info.getUpdateTime() + config.getReadyDivorceCd() * 3600 * 1000;
			long cur = GameSystemTime.getSystemMillTime();
			if(endTime > cur){
				//启动离婚定时
				int scheduleTime = (int)((endTime - cur)/1000);
				startDivorceSchedule(userRoleId, scheduleTime);
			}else{
				//离婚到了
				successDivorce(userRoleId);
			}
		}
	}
	
	public void offlineHandle(Long userRoleId){
		dingMap.remove(userRoleId);
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || info.getState() == GameConstants.MARRY_STATE_NO){
			return;
		}
		if(info.getState() == GameConstants.MARRY_STATE_DING || info.getState() == GameConstants.MARRY_STATE_MARRY){
			//取消亲密度定时
			cancelAddSchedule(userRoleId);
			cancelAddSchedule(info.getTargetRoleId());
		}
	}
	
	private RoleMarryInfo createRoleMarryInfo(Long userRoleId){
		RoleMarryInfo info = new RoleMarryInfo();
		info.setUserRoleId(userRoleId);
		info.setLoveVal(0l);
		info.setLfLevel(1);
		info.setXinwu(0);
		info.setMarryTime(0l);
		info.setState(GameConstants.MARRY_STATE_NO);
		info.setTargetRoleId(0l);
		info.setUpdateTime(0l);
		return info;
	}
	
	/**
	 * 订婚
	 * @param userRoleId	订婚操作人
	 * @return
	 */
	public Object[] dinghun(Long userRoleId){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		long curTime = GameSystemTime.getSystemMillTime();
		MarryPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MARRY);
		if(info != null){
			if(info.getState() != GameConstants.MARRY_STATE_NO){
				return AppErrorCode.JIEHUN_NOT_NO_MARRY;//当前非单身状态
			}
			long endTime = info.getUpdateTime() + config.getDivorceCd() * 3600 * 1000;//离婚CD结束时间
			if(curTime < endTime){
				return AppErrorCode.JIEHUN_IN_DIVORCE_CD;//当前处于离婚CD中
			}
			if(dingMap.containsKey(userRoleId) || dingMap.containsValue(userRoleId)){
				return AppErrorCode.JIEHUN_HAS_DING_APPLY;//当前处于订婚申请中
			}
		}else{
			info = createRoleMarryInfo(userRoleId);
			roleMarryInfoDao.cacheInsert(info, userRoleId);
		}
		Long[] roleIds = teamExportService.getTeamMemberIds(userRoleId);
		if(roleIds == null || roleIds.length != 2){
			return AppErrorCode.JIEHUN_BOTH_IN_TEAM;//结婚需要男女双方组队
		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null){
			return AppErrorCode.ROLE_IS_NOT_EXIST;//角色不存在
		}
		RoleWrapper target = null;
		for (Long roleId : roleIds) {
			if(userRoleId.equals(roleId)){
				continue;
			}
			target = roleExportService.getLoginRole(roleId);
		}
		
		if(target == null){
			return AppErrorCode.ROLE_IS_NOT_ONLINE;//对方不在线
		}
		
		if(jueSeDuiZhaoBiaoConfigExportService.getSex(role.getConfigId()) == jueSeDuiZhaoBiaoConfigExportService.getSex(target.getConfigId())){
			return AppErrorCode.JIEHUN_BOTH_IN_TEAM;//不支持同性恋
		}
		RoleMarryInfo targetInfo = roleMarryInfoDao.cacheLoad(target.getId(), target.getId());
		if(targetInfo != null){
			if(targetInfo.getState() != GameConstants.MARRY_STATE_NO){
				return AppErrorCode.JIEHUN_TARGET_NOT_NO_MARRY;// 当前非单身状态
			}
			long endTime = targetInfo.getUpdateTime() + config.getDivorceCd() * 3600 * 1000;//离婚CD结束时间
			if(curTime < endTime){
				return AppErrorCode.JIEHUN_HAS_DIVORCE;//当前处于离婚CD中
			}
			if(dingMap.containsKey(userRoleId) || dingMap.containsValue(userRoleId)){
				return AppErrorCode.JIEHUN_HAS_DING_APPLY;//当前处于订婚申请中
			}
		}
		dingMap.put(target.getId(),userRoleId);
		//发送订婚请求
		BusMsgSender.send2One(target.getId(), ClientCmdType.NOTICE_RECIVE_DING_APPLY, new Object[]{role.getId(),role.getName(),role.getConfigId()});
		return null;//成功
	}
	
	/**
	 * 答复订婚
	 * @param userRoleId
	 * @return
	 */
	public Object[] answerDinghun(Long userRoleId,Boolean agree){
		Long targetRoleId = dingMap.get(userRoleId);
		if(targetRoleId == null){
			return AppErrorCode.JIEHUN_NO_DING_APPLY;//当前没有订婚请求
		}
		
		if(!agree){
			//通知对方被拒
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			BusMsgSender.send2One(targetRoleId, ClientCmdType.NOTICE_TARGET_REFUSE_DINGHUN, role.getName());
		}else{
			RoleWrapper role = roleExportService.getLoginRole(targetRoleId);
			if(role == null){
				return AppErrorCode.ROLE_IS_NOT_ONLINE;// 对方不在线
			}
			DingHunConfig dingHunConfig = dingHunConfigServiceExport.getConfig();
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, dingHunConfig.getCostMoney(), targetRoleId, LogPrintHandle.CONSUME_MARRY, true, LogPrintHandle.CBZ_DINGHUN);
			if(result != null){
				return AppErrorCode.JIEHUN_TARGET_NO_ENOUGH_MONEY;//对方剩余银两不足
			}
			RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
			boolean insert = false;
			if(info == null){
				info = createRoleMarryInfo(userRoleId);
				insert = true;
			}
			info.setTargetRoleId(targetRoleId);
			info.setState(GameConstants.MARRY_STATE_DING);
			info.setYuanfen(0);
			info.setXinqing("");
			if(insert){
				roleMarryInfoDao.cacheInsert(info, userRoleId);
			}else{
				roleMarryInfoDao.cacheUpdate(info, userRoleId);
			}
			
			RoleMarryInfo target = roleMarryInfoDao.cacheLoad(targetRoleId, targetRoleId);
			target.setTargetRoleId(userRoleId);
			target.setState(GameConstants.MARRY_STATE_DING);
			target.setYuanfen(0);
			target.setXinqing("");
			roleMarryInfoDao.cacheUpdate(target, targetRoleId);
			
			//通知双方订婚成功
			startAddSchedule(userRoleId);
			startAddSchedule(targetRoleId);
			BusMsgSender.send2One(userRoleId, ClientCmdType.NOTICE_DING_SUCCESS, null);
			BusMsgSender.send2One(targetRoleId, ClientCmdType.NOTICE_DING_SUCCESS, null);
		}
		
		dingMap.remove(userRoleId);
		return null;//成功
	}
	
	/**
	 * 取消订婚
	 * @return
	 */
	public Object[] cancelDing(Long userRoleId){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || info.getState() != GameConstants.MARRY_STATE_DING){
			return AppErrorCode.JIEHUN_NOT_DING;//当前未订婚
		}
		DingHunConfig dingHunConfig = dingHunConfigServiceExport.getConfig();
		int cost = dingHunConfig.getQxCost();//取消订婚消耗
		Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, cost, userRoleId, LogPrintHandle.CONSUME_MARRY, true, LogPrintHandle.CBZ_CANCEL_DINGHUN);
		if(result != null){
			return result;
		}
		Long targetRoleId = info.getTargetRoleId();
		info.setState(GameConstants.MARRY_STATE_NO);
		info.setTargetRoleId(0l);
		roleMarryInfoDao.cacheUpdate(info, userRoleId);
		
		RoleMarryInfo target = null;
		boolean online = sessionManagerExportService.isOnline(targetRoleId);
		if(online){
			target = roleMarryInfoDao.cacheLoad(targetRoleId, targetRoleId);
		}else{
			target = roleMarryInfoDao.load(targetRoleId, targetRoleId, AccessType.getDirectDbType());
		}
		if(target == null){
			ChuanQiLog.error("玩家[{}]分手时对象未找到，对象id:{}",userRoleId,targetRoleId);
		}else{
			target.setState(GameConstants.MARRY_STATE_NO);
			target.setTargetRoleId(0l);
			if(online){
				roleMarryInfoDao.cacheUpdate(target, targetRoleId);
			}else{
				roleMarryInfoDao.update(target, targetRoleId, AccessType.getDirectDbType());
			}
		}

		cancelAddSchedule(userRoleId);
		if(online){
			cancelAddSchedule(targetRoleId);
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			//通知前任分手了
			BusMsgSender.send2One(targetRoleId, ClientCmdType.TARGET_CANCEL_DING, role.getName());
		}
		return AppErrorCode.OK;//分手成功
	}
	
	/**
	 * 结婚
	 * @param userRoleId	结婚发起人
	 * @param xinwu		信物
	 * @return
	 */
	public Object[] marry(Long userRoleId,Integer xinwu){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || info.getState() != GameConstants.MARRY_STATE_DING){
			return AppErrorCode.JIEHUN_NOT_DING;//当前未订婚
		}
		Long targetRoleId = info.getTargetRoleId();
		if(!sessionManagerExportService.isOnline(targetRoleId)){
			return AppErrorCode.ROLE_IS_NOT_ONLINE;
		}
		DingHunConfig dingHunConfig = dingHunConfigServiceExport.getConfig();
		if(info.getYuanfen() < dingHunConfig.getMaxYF()){
			return AppErrorCode.JIEHUN_NO_ENOUGH_YUANFEN;//缘分值不足
		}
		JieHunConfig config = jieHunConfigServiceExport.getConfig(xinwu);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;//信物错误
		}
		if(config.getCount() > 0){
			Object[] result = roleBagExportService.checkRemoveBagItemByGoodsType(config.getItemId1(), config.getCount(), userRoleId);
			if(result != null){
				return result;
			}
		}
		
		if(config.getGold() > 0){
			Object[] result = accountExportService.isEnought(GoodsCategory.GOLD, config.getGold(), userRoleId);
			if(result != null){
				return result;
			}
		}
		
		info.setQiuhunXinwu(xinwu);
		//发送求婚申请
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		BusMsgSender.send2One(info.getTargetRoleId(), ClientCmdType.NOTICE_RECIVE_MARRY_APPLY, new Object[]{role.getId(),role.getName(),xinwu,role.getConfigId()});
		
		return AppErrorCode.OK;
	}
	
	public Object[] answerMarry(Long userRoleId,Boolean agree){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || info.getState() != GameConstants.MARRY_STATE_DING){
			return AppErrorCode.JIEHUN_NOT_DING;//当前未订婚
		}
		long targetRoleId = info.getTargetRoleId();
		if(!sessionManagerExportService.isOnline(targetRoleId)){
			return AppErrorCode.ROLE_IS_NOT_ONLINE;//对方已下线
		}
		RoleMarryInfo target = roleMarryInfoDao.cacheLoad(targetRoleId, targetRoleId);
		if(target == null){
			return AppErrorCode.ROLE_IS_NOT_EXIST;//异常
		}
		Integer xinwu = target.getQiuhunXinwu();
		if(xinwu == null || xinwu < 1){
			return AppErrorCode.JIEHUN_NO_MARRY_APPLY;//对方未求婚
		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(!agree){
			//通知拒绝
			BusMsgSender.send2One(targetRoleId, ClientCmdType.NOTICE_TARGET_REFUSE_MARRY, role.getName());
		}else{
			JieHunConfig config = jieHunConfigServiceExport.getConfig(xinwu);
			if(config == null){
				return AppErrorCode.CONFIG_ERROR;//信物错误
			}
			if(config.getCount() > 0){
				Object[] result = roleBagExportService.checkRemoveBagItemByGoodsType(config.getItemId1(), config.getCount(), targetRoleId);
				if(result != null){
					return AppErrorCode.JIEHUN_TARGET_NO_ENOUGH_ITEM;//对方道具不足
				}
			}
			
			if(config.getGold() > 0){
				Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, config.getGold(), targetRoleId, LogPrintHandle.CONSUME_MARRY, true, LogPrintHandle.CBZ_MARRY);
				if(result != null){
					return AppErrorCode.JIEHUN_TARGET_NO_ENOUGH_GOLD;//对方元宝不足
				}else{
					if(PlatformConstants.isQQ()){
						BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,config.getGold(),LogPrintHandle.CONSUME_MARRY,QQXiaoFeiType.CONSUME_MARRY,1});
					}
				}
			}
			if(config.getCount() > 0){
				roleBagExportService.removeBagItemByGoodsType(config.getItemId1(), config.getCount(), targetRoleId, GoodsSource.GOODS_MARRY_CONSUME, true, true);
			}
			long cur = GameSystemTime.getSystemMillTime();
			target.setMarryTime(cur);
			target.setXinwu(xinwu);
			target.setState(GameConstants.MARRY_STATE_MARRY);
			roleMarryInfoDao.cacheUpdate(target, targetRoleId);
			info.setMarryTime(cur);
			info.setXinwu(xinwu);
			info.setState(GameConstants.MARRY_STATE_MARRY);
			roleMarryInfoDao.cacheUpdate(info, userRoleId);
			//通知双方结婚成功
			BusMsgSender.send2One(userRoleId, ClientCmdType.NOTICE_MARRY_SUCCESS, null);
			BusMsgSender.send2One(targetRoleId, ClientCmdType.NOTICE_MARRY_SUCCESS, null);
			//全服广告结婚成功
			MarryPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MARRY);
			RoleWrapper targetRole = roleExportService.getLoginRole(targetRoleId);
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.MARRY_CHANGE_PEIOU, targetRole.getName());
			BusMsgSender.send2Stage(targetRoleId, InnerCmdType.MARRY_CHANGE_PEIOU, role.getName());
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.MARRY_CHANGE_XINWU, xinwu);
			BusMsgSender.send2Stage(targetRoleId, InnerCmdType.MARRY_CHANGE_XINWU, xinwu);
			Object[] msg = new Object[]{publicConfig.getCode(),null};
			if(jueSeDuiZhaoBiaoConfigExportService.getSex(role.getConfigId()) == GameConstants.SEX_MAN){
				msg[1] = new Object[]{role.getName(),targetRole.getName()};
			}else{
				msg[1] = new Object[]{targetRole.getName(),role.getName()};
			}
			BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT6, msg);
			//通知场景增加结婚属性
			synStageXinWuAttChange(userRoleId, config);
			synStageLongFengAtt(userRoleId);
			synStageXinWuAttChange(targetRoleId, config);
			synStageLongFengAtt(targetRoleId);
			//重启定时
			cancelAddSchedule(userRoleId);
			startAddSchedule(userRoleId);
			cancelAddSchedule(targetRoleId);
			startAddSchedule(targetRoleId);
		}
		
		return null;
	}
	
	/**
	 * 离婚
	 * @param userRoleId
	 * @param type
	 * @return
	 */
	public Object[] divorce(Long userRoleId,Integer type){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || info.getState() != GameConstants.MARRY_STATE_MARRY){
			return AppErrorCode.JIEHUN_NO_IN_MARRY;//当前未结婚
		}
		
		Long targetRoleId = info.getTargetRoleId();
		boolean online = sessionManagerExportService.isOnline(targetRoleId);
		long cur = GameSystemTime.getSystemMillTime();
		if(type == GameConstants.DIVORCE_HEPING){
			if(!online){
				return AppErrorCode.ROLE_IS_NOT_ONLINE;//对方不在线
			}
			if(info.isAskDivorce()){
				return AppErrorCode.JIEHUN_HAS_SEND_DIVORCE_APPLY;//已发送离婚请求
			}
			info.setAskDivorce(true);
			//通知对方我要离婚
			BusMsgSender.send2One(targetRoleId, ClientCmdType.NOTICE_TARGET_DIVORCE, null);
		}else{
			MarryPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MARRY);
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, publicConfig.getDivorceCost(), userRoleId, LogPrintHandle.CONSUME_MARRY, true, LogPrintHandle.CBZ_DIVOICE);
			if(result != null){
				return result;
			}
			
			info.setState(GameConstants.MARRY_STATE_NO);
			info.setTargetRoleId(0l);
			info.setXinwu(0);
			info.setYuanfen(0);
			info.setUpdateTime(cur);
			roleMarryInfoDao.cacheUpdate(info, userRoleId);
			//通知场景减属性
			synStageLongFengAtt(userRoleId, null);
			synStageXinWuAttChange(userRoleId, null);
			cancelAddSchedule(userRoleId);
			//通知玩家已离婚
			BusMsgSender.send2One(userRoleId, ClientCmdType.NOTICE_TARGET_DIVORCE_SUCCESS, null);
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.MARRY_CHANGE_PEIOU, null);
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.MARRY_CHANGE_XINWU, null);
			RoleMarryInfo target = null;
			if(online){
				cancelAddSchedule(targetRoleId);
				target = roleMarryInfoDao.cacheLoad(targetRoleId, targetRoleId);
			}else{
				target = roleMarryInfoDao.load(targetRoleId, targetRoleId, AccessType.getDirectDbType());
			}
			if(target != null){
				target.setState(GameConstants.MARRY_STATE_NO);
				target.setTargetRoleId(0l);
				target.setXinwu(0);
				target.setYuanfen(0);
				target.setUpdateTime(cur);
				if(online){
					roleMarryInfoDao.cacheUpdate(target, targetRoleId);
					//通知场景减属性
					synStageLongFengAtt(targetRoleId, null);
					synStageXinWuAttChange(targetRoleId, null);
					//通知玩家已离婚
					BusMsgSender.send2One(targetRoleId, ClientCmdType.NOTICE_TARGET_DIVORCE_SUCCESS, null);
					BusMsgSender.send2Stage(targetRoleId, InnerCmdType.MARRY_CHANGE_PEIOU, null);
					BusMsgSender.send2Stage(targetRoleId, InnerCmdType.MARRY_CHANGE_XINWU, null);
				}else{
					roleMarryInfoDao.update(target, targetRoleId, AccessType.getDirectDbType());
				}
			}
		}
		
		return new Object[]{1,type};
	}
	
	public Object[] answerDivorce(Long userRoleId,boolean agree){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || info.getState() != GameConstants.MARRY_STATE_MARRY){
			return AppErrorCode.JIEHUN_NO_IN_MARRY;//当前未结婚
		}
		
		Long targetRoleId = info.getTargetRoleId();
		boolean online = sessionManagerExportService.isOnline(targetRoleId);
		if(!online){
			return AppErrorCode.ROLE_IS_NOT_ONLINE;//对象不在线
		}
		long cur = GameSystemTime.getSystemMillTime();
		
		RoleMarryInfo target = roleMarryInfoDao.cacheLoad(targetRoleId, targetRoleId);
		if(!target.isAskDivorce()){
			return AppErrorCode.JIEHUN_TARGET_NOT_SEND_DIVORCE;//对方未发起离婚协议
		}
		
		target.setAskDivorce(false);
		if(!agree){
			//通知拒绝离婚
			BusMsgSender.send2One(targetRoleId, ClientCmdType.NOTICE_TARGET_ANSWER_DIVORCE, AppErrorCode.JIEHUN_REFUSE_DIVORCE);
		}else{
			info.setState(GameConstants.MARRY_STATE_DIVORCE);
			info.setUpdateTime(cur);
			roleMarryInfoDao.cacheUpdate(info, userRoleId);
			
			target.setState(GameConstants.MARRY_STATE_DIVORCE);
			target.setUpdateTime(cur);
			roleMarryInfoDao.cacheUpdate(target, targetRoleId);
			
			//通知玩家协议离婚
			MarryPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MARRY);
			int time = config.getReadyDivorceCd() * 3600;
			startDivorceSchedule(userRoleId, time);
			startDivorceSchedule(targetRoleId, time);
			cancelAddSchedule(userRoleId);
			cancelAddSchedule(targetRoleId);
			
			BusMsgSender.send2One(targetRoleId, ClientCmdType.NOTICE_TARGET_ANSWER_DIVORCE, new Object[]{0,cur} );
		}
		
		return new Object[]{1,cur};
	}
	
	/**
	 * 成功离婚（协议离婚到点）
	 * @param userRoleId
	 */
	public void successDivorce(Long userRoleId){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || info.getState() != GameConstants.MARRY_STATE_DIVORCE){
			return ;//当前不是协议离婚状态
		}
		MarryPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MARRY);
		info.setState(GameConstants.MARRY_STATE_NO);
		info.setTargetRoleId(0l);
		info.setXinwu(0);
		info.setYuanfen(0);
		info.setUpdateTime(info.getUpdateTime() + publicConfig.getReadyDivorceCd() * 3600 * 1000);//根据协议时间，计算离婚时间
		roleMarryInfoDao.cacheUpdate(info, userRoleId);
		//通知场景减属性
		synStageLongFengAtt(userRoleId, null);
		synStageXinWuAttChange(userRoleId, null);
		//通知玩家已离婚
		BusMsgSender.send2One(userRoleId, ClientCmdType.NOTICE_TARGET_DIVORCE_SUCCESS, null);
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.MARRY_CHANGE_PEIOU, null);
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.MARRY_CHANGE_XINWU, null);
	}
	
	/**
	 * 取消协议离婚
	 * @param userRoleId
	 * @return
	 */
	public Object[] cancelDivorce(Long userRoleId){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || info.getState() != GameConstants.MARRY_STATE_DIVORCE){
			return AppErrorCode.JIEHUN_NOT_IN_DIVORCE;//当前不是协议离婚状态
		}
		if(info.getUpdateTime() >= GameSystemTime.getSystemMillTime()){
			return AppErrorCode.JIEHUN_HAS_DIVORCE;//已离婚
		}
		Long targetRoleId = info.getTargetRoleId();
		boolean online = sessionManagerExportService.isOnline(targetRoleId);
		//取消双方离婚定时
		cancelDivorceSchedule(userRoleId);
		info.setState(GameConstants.MARRY_STATE_MARRY);
		roleMarryInfoDao.cacheUpdate(info, userRoleId);
		if(online){
			cancelDivorceSchedule(targetRoleId);
			RoleMarryInfo target = roleMarryInfoDao.cacheLoad(targetRoleId, targetRoleId);
			target.setState(GameConstants.MARRY_STATE_MARRY);
			roleMarryInfoDao.cacheUpdate(target, targetRoleId);
			BusMsgSender.send2One(targetRoleId, ClientCmdType.NOTICE_TARGET_CANCEL_DIVORCE, null);
			startAddSchedule(userRoleId);
			startAddSchedule(targetRoleId);
		}else{
			RoleMarryInfo target = roleMarryInfoDao.load(targetRoleId, targetRoleId, AccessType.getDirectDbType());
			target.setState(GameConstants.MARRY_STATE_MARRY);
			roleMarryInfoDao.update(target, targetRoleId, AccessType.getDirectDbType());
		}
		
		return AppErrorCode.OK;
	}
	
	/**
	 * 增加缘分值
	 * @param userRoleId
	 * @param auto
	 * @return
	 */
	public Object[] addYuanfen(Long userRoleId,boolean auto){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || info.getState() != GameConstants.MARRY_STATE_DING){
			return AppErrorCode.JIEHUN_NOT_DING;//当前未订婚
		}
		DingHunConfig config = dingHunConfigServiceExport.getConfig();
		int curYuanfen = info.getYuanfen();
		if(curYuanfen >= config.getMaxYF()){
			return AppErrorCode.JIEHUN_IS_MAX_YUANFEN;//缘分值已满
		}
		curYuanfen += config.getYfAdd();
		Object[] result = accountExportService.isEnought(GoodsCategory.MONEY, config.getMoney(), userRoleId);
		if(result != null){
			return result;
		}
		
		int count = roleBagExportService.getBagItemCountByGoodsType(config.getItemId1(), userRoleId);
		int bGold = 0;
		int gold = 0;
		int costCount = config.getCount();
		if(count < config.getCount()){
			if(!auto){
				return AppErrorCode.GOODS_NOT_ENOUGH;//数量不足
			}else{
				int need = config.getCount() - count;
				RoleAccount account = accountExportService.getRoleAccount(userRoleId);
				bGold = need * config.getItemBGold();
				if(bGold > account.getBindYb()){
					int bcount = (int)(account.getBindYb() / config.getItemBGold());
					bGold = bcount * config.getItemBGold();
					need -= bcount;
					
					gold = need * config.getItemGold();
					if(gold > account.getYb()){
						return AppErrorCode.YUANBAO_NOT_ENOUGH;//元宝不足
					}
				}
				costCount = count;
			}
		}
		roleBagExportService.removeBagItemByGoodsType(config.getItemId1(), costCount, userRoleId, GoodsSource.GOODS_YUANFEN_CONSUME, true, true);
		if(bGold > 0){
			accountExportService.decrCurrencyWithNotify(GoodsCategory.BGOLD, bGold, userRoleId, LogPrintHandle.CONSUME_MARRY, true, LogPrintHandle.CBZ_YUANFEN);
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,bGold,LogPrintHandle.CONSUME_MARRY,QQXiaoFeiType.CONSUME_MARRY,1});
			}
		}
		if(gold > 0){
			accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, gold, userRoleId, LogPrintHandle.CONSUME_MARRY, true, LogPrintHandle.CBZ_YUANFEN);
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,gold,LogPrintHandle.CONSUME_MARRY,QQXiaoFeiType.CONSUME_MARRY,1});
			}
		}
		accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, config.getMoney(), userRoleId,LogPrintHandle.CONSUME_MARRY, true, LogPrintHandle.CBZ_YUANFEN);
		
		
		info.setYuanfen(curYuanfen);
		roleMarryInfoDao.cacheUpdate(info, userRoleId);
		//通知缘分变化
		BusMsgSender.send2One(userRoleId, ClientCmdType.NOTICE_YUANFEN, curYuanfen);
		
		if(sessionManagerExportService.isOnline(info.getTargetRoleId())){
			RoleMarryInfo target = roleMarryInfoDao.cacheLoad(info.getTargetRoleId(), info.getTargetRoleId());
			target.setYuanfen(curYuanfen);
			roleMarryInfoDao.cacheUpdate(target, info.getTargetRoleId());
			//通知缘分变化
			BusMsgSender.send2One(info.getTargetRoleId(), ClientCmdType.NOTICE_YUANFEN, curYuanfen);
		}else{
			RoleMarryInfo target = roleMarryInfoDao.load(info.getTargetRoleId(), info.getTargetRoleId(),AccessType.getDirectDbType());
			target.setYuanfen(curYuanfen);
			roleMarryInfoDao.update(target, info.getTargetRoleId(),AccessType.getDirectDbType());
		}
		
		return null;
	}
	/**
	 * 增加亲密度
	 * @param userRoleId
	 * @param auto
	 * @return
	 */
	public Object[] addQinmidu(Long userRoleId,boolean auto){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || info.getState() != GameConstants.MARRY_STATE_MARRY){
			return AppErrorCode.JIEHUN_NO_IN_MARRY;// 当前未结婚
		}
		int level = info.getLfLevel();
		LoveLevelConfig config = loveLevelConfigServiceExport.getConfig(level);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;//配置异常
		}
		long curLove = info.getLoveVal();
		if(curLove >= loveLevelConfigServiceExport.getMaxLove()){
			return AppErrorCode.IS_MAX_LEVEL;//已到最大等级
		}
		Object[] result = accountExportService.isEnought(GoodsCategory.MONEY, config.getNeedMoney(), userRoleId);
		if(result != null){
			return result;
		}
		
		int count = roleBagExportService.getBagItemCountByGoodsType(config.getItemId1(), userRoleId);
		int bGold = 0;
		int gold = 0;
		int costCount = config.getCount();
		if(count < config.getCount()){
			if(!auto){
				return AppErrorCode.GOODS_NOT_ENOUGH;//数量不足
			}else{
				int need = config.getCount() - count;
				RoleAccount account = accountExportService.getRoleAccount(userRoleId);
				bGold = need * config.getItemBGold();
				if(bGold > account.getBindYb()){
					int bcount = (int)(account.getBindYb() / config.getItemBGold());
					bGold = bcount * config.getItemBGold();
					need -= bcount;
					
					gold = need * config.getItemGold();
					if(gold > account.getYb()){
						return AppErrorCode.YUANBAO_NOT_ENOUGH;//元宝不足
					}
				}
				costCount = count;
			}
		}
		roleBagExportService.removeBagItemByGoodsType(config.getItemId1(), costCount, userRoleId, GoodsSource.GOODS_YUANFEN_CONSUME, true, true);
		if(bGold > 0){
			accountExportService.decrCurrencyWithNotify(GoodsCategory.BGOLD, bGold, userRoleId, LogPrintHandle.CONSUME_MARRY, true, LogPrintHandle.CBZ_YUANFEN);
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,bGold,LogPrintHandle.CONSUME_MARRY,QQXiaoFeiType.CONSUME_MARRY,1});
			}
		}
		if(gold > 0){
			accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, gold, userRoleId, LogPrintHandle.CONSUME_MARRY, true, LogPrintHandle.CBZ_YUANFEN);
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,gold,LogPrintHandle.CONSUME_MARRY,QQXiaoFeiType.CONSUME_MARRY,1});
			}
		}
		accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, config.getNeedMoney(), userRoleId, LogPrintHandle.CONSUME_MARRY, true, LogPrintHandle.CBZ_YUANFEN);
		
		curLove += config.getAddLove();
		if(curLove >= config.getMaxLove()){
			level++;
			config = loveLevelConfigServiceExport.getConfig(level);
			if(config == null){
				level--;//已达到最大等级
			}
		}
		
		info.setLoveVal(curLove);
		BusMsgSender.send2One(userRoleId, ClientCmdType.NOTICE_QINMIDU, curLove);
		if(info.getLfLevel() < level){
			info.setLfLevel(level);
			//通知场景龙凤等级变化
			synStageLongFengAtt(userRoleId, config);
			BusMsgSender.send2One(userRoleId, ClientCmdType.NOTICE_LOVE_LEVEL_CHANGE, level);
		}
		roleMarryInfoDao.cacheUpdate(info, userRoleId);
		
		return null;
	}
	
	/**
	 * 定时增加缘分值或亲密度
	 * @param userRoleId
	 */
	public void addYuanfenOrLove(Long userRoleId){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null){
			return;
		}
		if(info.getTargetRoleId() == null || !sessionManagerExportService.isOnline(info.getTargetRoleId())){
			return;
		}
		boolean next = false;
		if(info.getState() == GameConstants.MARRY_STATE_DING){
			next = addYuanfen(userRoleId, info);
		}else if(info.getState() == GameConstants.MARRY_STATE_MARRY){
			next = addQinmidu(userRoleId, info);
		}
		if(next){
			//开启下次定时
			startAddSchedule(userRoleId);
		}
	}
	
	/**
	 * 定时增加缘分值
	 * @param userRoleId
	 * @param info
	 */
	private boolean addYuanfen(Long userRoleId,RoleMarryInfo info){
		DingHunConfig config = dingHunConfigServiceExport.getConfig();
		MarryPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MARRY);
		int curYuanfen = info.getYuanfen();
		if(curYuanfen >= config.getMaxYF()){
			return false;//缘分已满
		}
		curYuanfen += publicConfig.getAddLove();
		
		Long targetRoleId = info.getTargetRoleId();
		if(!sessionManagerExportService.isOnline(targetRoleId)){
			return false;//对象不在线
		}
		
		RoleMarryInfo target = roleMarryInfoDao.cacheLoad(targetRoleId, targetRoleId);
		if(target == null || target.getState() != GameConstants.MARRY_STATE_DING){
			return false;//对象不存在
		}
		
		info.setYuanfen(curYuanfen);
		roleMarryInfoDao.cacheUpdate(info, userRoleId);
		target.setYuanfen(curYuanfen);
		roleMarryInfoDao.cacheUpdate(target, targetRoleId);
		//通知玩家
		BusMsgSender.send2One(userRoleId, ClientCmdType.NOTICE_YUANFEN, curYuanfen);
		BusMsgSender.send2One(targetRoleId, ClientCmdType.NOTICE_YUANFEN, curYuanfen);
		return true;
	}
	
	/**
	 * 定时增加亲密度
	 * @param userRoleId
	 * @param info
	 */
	private boolean addQinmidu(Long userRoleId,RoleMarryInfo info){
		LoveLevelConfig config = loveLevelConfigServiceExport.getConfig(info.getLfLevel());
		if(config == null){
			return false;//配置异常
		}
		long curLove = info.getLoveVal();
		
		if(curLove >= loveLevelConfigServiceExport.getMaxLove()){
			return false;//已到最大等级
		}
		MarryPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MARRY);
		curLove += publicConfig.getAddLove() ;
		
		info.setLoveVal(curLove);
		if(curLove >= config.getMaxLove()){
			int level = info.getLfLevel() + 1;
			config = loveLevelConfigServiceExport.getConfig(level);
			if(config != null){
				info.setLfLevel(level);
				//通知场景属性变化
				synStageLongFengAtt(userRoleId, config);
				BusMsgSender.send2One(userRoleId, ClientCmdType.NOTICE_LOVE_LEVEL_CHANGE, level);
			}
		}
		roleMarryInfoDao.cacheUpdate(info, userRoleId);
		//通知玩家
		BusMsgSender.send2One(userRoleId, ClientCmdType.NOTICE_QINMIDU, curLove);
		return true;
	}
	
	public Object[] changeXinWu(Long userRoleId,Integer xinwu){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || info.getState() != GameConstants.MARRY_STATE_MARRY){
			return AppErrorCode.JIEHUN_NO_IN_MARRY;//当前未结婚
		}
		JieHunConfig oldConfig = jieHunConfigServiceExport.getConfig(info.getXinwu());
		JieHunConfig config = jieHunConfigServiceExport.getConfig(xinwu);
		if(oldConfig == null || config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(oldConfig.getLevel() >= config.getLevel()){
			return AppErrorCode.JIEHUN_ONLY_CAN_CHANGE_BETTER;//更换的信物只能更高级
		}
		if(config.getCount() > 0){
			Object[] result = roleBagExportService.checkRemoveBagItemByGoodsType(config.getItemId1(), config.getCount(), userRoleId);
			if(result != null){
				return result;
			}
		}
		
		if(config.getGold() > 0){
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, config.getGold(), userRoleId, LogPrintHandle.CONSUME_MARRY, true, LogPrintHandle.CBZ_MARRY);
			if(result != null){
				return result;
			}else{
				if(PlatformConstants.isQQ()){
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,config.getGold(),LogPrintHandle.CONSUME_MARRY,QQXiaoFeiType.CONSUME_MARRY,1});
				}
			}
		}
		if(config.getCount() > 0){
			roleBagExportService.removeBagItemByGoodsType(config.getItemId1(), config.getCount(), userRoleId, GoodsSource.GOODS_MARRY_CONSUME, true, true);
		}
		info.setXinwu(xinwu);
		roleMarryInfoDao.cacheUpdate(info, userRoleId);
		synStageXinWuAttChange(userRoleId, config);
		BusMsgSender.send2One(userRoleId, ClientCmdType.NOTICE_XINWU_CHANGED, xinwu);
		
		if(sessionManagerExportService.isOnline(info.getTargetRoleId())){
			RoleMarryInfo target = roleMarryInfoDao.cacheLoad(info.getTargetRoleId(), info.getTargetRoleId());
			target.setXinwu(xinwu);
			roleMarryInfoDao.cacheUpdate(target, info.getTargetRoleId());
			synStageXinWuAttChange(info.getTargetRoleId(), config);
			//通知信物变化
			BusMsgSender.send2One(info.getTargetRoleId(), ClientCmdType.NOTICE_XINWU_CHANGED, xinwu);
		}else{
			RoleMarryInfo target = roleMarryInfoDao.load(info.getTargetRoleId(), info.getTargetRoleId(),AccessType.getDirectDbType());
			target.setXinwu(xinwu);
			roleMarryInfoDao.update(target, info.getTargetRoleId(),AccessType.getDirectDbType());
		}
		
		return null;
	}
	
	/**
	 * 同步场景信物属性
	 * @param userRoleId
	 * @param config
	 */
	private void synStageXinWuAttChange(Long userRoleId,JieHunConfig config){
		Map<String,Long> atts = null;
		if(config != null){
			atts = config.getAtts();
		}
		//通知场景
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_ROLE_ATT, new Object[]{BaseAttributeType.XINWU.val,atts});
	}
	
//	private void synStageXinWuAttChange(Long userRoleId){
//		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
//		JieHunConfig config = null;
//		if(info != null && info.getState() == GameConstants.MARRY_STATE_MARRY || info.getState() == GameConstants.MARRY_STATE_DIVORCE){
//			config = jieHunConfigServiceExport.getConfig(info.getXinwu());
//		}
//		synStageXinWuAttChange(userRoleId, config);
//	}
	
	private void synStageLongFengAtt(Long userRoleId,LoveLevelConfig config){
		Map<String,Long> atts = null;
		if(config != null){
			atts = config.getAtts();
		}
		//通知场景
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_ROLE_ATT, new Object[]{BaseAttributeType.LONGFENG.val,atts});
	}
	
	private void synStageLongFengAtt(Long userRoleId){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		LoveLevelConfig config = null;
		if(info != null && info.getState() == GameConstants.MARRY_STATE_MARRY || info.getState() == GameConstants.MARRY_STATE_DIVORCE){
			config = loveLevelConfigServiceExport.getConfig(info.getLfLevel());
		}
		synStageLongFengAtt(userRoleId, config);
	}
	
	/**
	 * 填充结婚属性
	 * @param userRoleId
	 * @param xinwuAtt	信物属性
	 * @param lfAtt		龙凤属性
	 */
	public void putJieHunAtt(Long userRoleId,Map<String,Long> xinwuAtt,Map<String,Long> lfAtt){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info != null && (info.getState() == GameConstants.MARRY_STATE_MARRY || info.getState() == GameConstants.MARRY_STATE_DIVORCE)){
			JieHunConfig jieHunConfig = jieHunConfigServiceExport.getConfig(info.getXinwu());
			if(jieHunConfig != null){
				xinwuAtt.putAll(jieHunConfig.getAtts());
			}
			LoveLevelConfig loveLevelConfig = loveLevelConfigServiceExport.getConfig(info.getLfLevel());
			if(loveLevelConfig != null){
				lfAtt.putAll(loveLevelConfig.getAtts());
			}
		}
	}
	
	public Object[] getSelfMarryInfo(Long userRoleId){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null){
			return new Object[]{1,0};
		}
		Long targetRoleId = info.getTargetRoleId();
		Object[] target = null;
		if(targetRoleId != null){
			RoleWrapper role = null;
			if(sessionManagerExportService.isOnline(targetRoleId)){
				role = roleExportService.getLoginRole(targetRoleId);
			}else{
				role = roleExportService.getUserRoleFromDb(targetRoleId);
			}
			if(role != null){
				target = new Object[]{role.getConfigId(),role.getId(),role.getName(),info.getXinwu()};
			}
		}
		return new Object[]{1,info.getState(),target,info.getUpdateTime()};
	}
	
	public Object[] getLongfengInfo(Long userRoleId){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null){
			return null;
		}
		return new Object[]{info.getLfLevel(),info.getLoveVal()};
	}
	
	public Object[] getDinghunInfo(Long userRoleId){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null){
			return null;
		}
		if(info.getState() != GameConstants.MARRY_STATE_DING){
			return null;
		}
		Long targetRoleId = info.getTargetRoleId();
		if(sessionManagerExportService.isOnline(targetRoleId)){
			RoleWrapper role = roleExportService.getLoginRole(targetRoleId);
			return new Object[]{role.getId(),role.getConfigId(),role.getName(),info.getYuanfen()};
		}else if(info.getTargetInfo() == null){
			RoleWrapper role = roleExportService.getUserRoleFromDb(targetRoleId);
			Object[] targetInfo = new Object[]{role.getId(),role.getConfigId(),role.getName(),info.getYuanfen()};
			info.setTargetInfo(targetInfo);
		}else{
			Object[] targetInfo = info.getTargetInfo();
			targetInfo[3] = info.getYuanfen();
		}
		return info.getTargetInfo();
	}
	
	public Object[] getMarryInfo(Long userRoleId){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null){
			return null;
		}
		if(info.getState() != GameConstants.MARRY_STATE_MARRY && info.getState() != GameConstants.MARRY_STATE_DIVORCE){
			return null;
		}
		Long divorceTime = null;
		if(info.getState() == GameConstants.MARRY_STATE_DIVORCE){
			MarryPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_MARRY);
			divorceTime = info.getUpdateTime() + config.getReadyDivorceCd() * 3600 * 1000;
		}
		Long targetRoleId = info.getTargetRoleId();
		if(sessionManagerExportService.isOnline(targetRoleId)){
			RoleWrapper role = roleExportService.getLoginRole(targetRoleId);
			RoleMarryInfo target = roleMarryInfoDao.cacheLoad(targetRoleId, targetRoleId);
			return new Object[]{role.getId(),role.getConfigId(),role.getName(),target.getMarryTime(),info.getLoveVal(),target.getLoveVal(),info.getXinqing(),info.getXinwu(),divorceTime};
		}else if(info.getTargetInfo() == null){
			RoleWrapper role = roleExportService.getUserRoleFromDb(targetRoleId);
			RoleMarryInfo target = roleMarryInfoDao.load(targetRoleId, targetRoleId,AccessType.getDirectDbType());
			Object[] targetInfo = new Object[]{role.getId(),role.getConfigId(),role.getName(),target.getMarryTime(),info.getLoveVal(),target.getLoveVal(),info.getXinqing(),info.getXinwu(),divorceTime};
			info.setTargetInfo(targetInfo);
		}else{
			Object[] targetInfo = info.getTargetInfo();
			targetInfo[4] = info.getLoveVal();
			targetInfo[6] = info.getXinqing();
			targetInfo[7] = info.getXinwu();
		}
		return info.getTargetInfo();
	}
	
	public Object[] openCloseDinghunPanel(Long userRoleId,boolean open){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null){
			return null;
		}
		if(open && sessionManagerExportService.isOnline(info.getTargetRoleId())){
			RoleMarryInfo target = roleMarryInfoDao.cacheLoad(info.getTargetRoleId(), info.getTargetRoleId());
			if(target.isOpenDinghun()){
				return AppErrorCode.JIEHUN_TARGET_IS_OPEN_DINGHUN;
			}
		}
		info.setOpenDinghun(open);
		return AppErrorCode.OK;
	}
	public Object[] openCloseXinwuPanel(Long userRoleId,boolean open){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null){
			return null;
		}
		if(open && sessionManagerExportService.isOnline(info.getTargetRoleId())){
			RoleMarryInfo target = roleMarryInfoDao.cacheLoad(info.getTargetRoleId(), info.getTargetRoleId());
			if(target.isOpenChange()){
				return AppErrorCode.JIEHUN_TARGET_IS_OPEN_CHANGE;
			}
		}
		info.setOpenChange(open);
		return AppErrorCode.OK;
	}
	
	public Object[] changeTodayXinqing(Long userRoleId,String xinqing){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null){
			return AppErrorCode.JIEHUN_NO_IN_MARRY;
		}
		if(info.getState() != GameConstants.MARRY_STATE_MARRY){
			return AppErrorCode.JIEHUN_NO_IN_MARRY;
		}
		if(xinqing.length() > GameConstants.XINQING_MAX_LENGTH){
			return AppErrorCode.JIEHUN_XINQING_TOO_LENGTH;//字数超过上限
		}
		info.setXinqing(xinqing);
		roleMarryInfoDao.cacheUpdate(info, userRoleId);
		
		Long targetRoleId = info.getTargetRoleId();
		if(sessionManagerExportService.isOnline(targetRoleId)){
			RoleMarryInfo target = roleMarryInfoDao.cacheLoad(targetRoleId, targetRoleId);
			target.setXinqing(xinqing);
			roleMarryInfoDao.cacheUpdate(target, targetRoleId);
		}else if(info.getTargetInfo() == null){
			RoleMarryInfo target = roleMarryInfoDao.load(targetRoleId, targetRoleId,AccessType.getDirectDbType());
			target.setXinqing(xinqing);
			roleMarryInfoDao.update(target, targetRoleId, AccessType.getDirectDbType());
		}
		
		return AppErrorCode.OK;
	}
	
	/**
	 * 获取配偶userRoleId，没有为null
	 * @param userRoleId
	 * @return
	 */
	public Long getPeiouUserRoleId(Long userRoleId){
		RoleMarryInfo info = null;
		if(sessionManagerExportService.isOnline(userRoleId)){
			info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		}else{
			info = roleMarryInfoDao.load(userRoleId, userRoleId,AccessType.getDirectDbType());
		}
		if(info == null){
			return null;
		}
		if(info.getState() != GameConstants.MARRY_STATE_MARRY && info.getState() != GameConstants.MARRY_STATE_DIVORCE){
			return null;
		}
		return info.getTargetRoleId();
	}
	/**
	 * 配偶改名
	 * @param userRoleId
	 * @param name
	 * @return
	 */
	public void peiouChangeName(Long userRoleId,String name){
		RoleMarryInfo info = null;
		if(sessionManagerExportService.isOnline(userRoleId)){
			info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		}else{
			info = roleMarryInfoDao.load(userRoleId, userRoleId,AccessType.getDirectDbType());
		}
		if(info == null){
			return;
		}
		if(info.getState() != GameConstants.MARRY_STATE_MARRY && info.getState() != GameConstants.MARRY_STATE_DIVORCE){
			return;
		}
		if(sessionManagerExportService.isOnline(info.getTargetRoleId())){
			BusMsgSender.send2Stage(info.getTargetRoleId(), InnerCmdType.MARRY_CHANGE_PEIOU, name);
		}
	}
	
	/**
	 * 转职强制离婚
	 * @param userRoleId
	 */
	public void zhuanZhiJieChuJieHun(Long userRoleId){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || (info.getState() != GameConstants.MARRY_STATE_MARRY && info.getState() != GameConstants.MARRY_STATE_DIVORCE)){
			return;//当前未结婚
		}
		Long targetRoleId = info.getTargetRoleId();
		boolean online = sessionManagerExportService.isOnline(targetRoleId);
		long cur = GameSystemTime.getSystemMillTime();

		info.setState(GameConstants.MARRY_STATE_NO);
		info.setTargetRoleId(0l);
		info.setXinwu(0);
		info.setYuanfen(0);
		info.setUpdateTime(cur);
		roleMarryInfoDao.cacheUpdate(info, userRoleId);
		//通知场景减属性
		synStageLongFengAtt(userRoleId, null);
		synStageXinWuAttChange(userRoleId, null);
		cancelAddSchedule(userRoleId);
		//通知玩家已离婚
		BusMsgSender.send2One(userRoleId, ClientCmdType.NOTICE_TARGET_DIVORCE_SUCCESS, null);
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.MARRY_CHANGE_PEIOU, null);
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.MARRY_CHANGE_XINWU, null);
		RoleMarryInfo target = null;
		if(online){
			cancelAddSchedule(targetRoleId);
			target = roleMarryInfoDao.cacheLoad(targetRoleId, targetRoleId);
		}else{
			target = roleMarryInfoDao.load(targetRoleId, targetRoleId, AccessType.getDirectDbType());
		}
		if(target != null){
			target.setState(GameConstants.MARRY_STATE_NO);
			target.setTargetRoleId(0l);
			target.setXinwu(0);
			target.setYuanfen(0);
			target.setUpdateTime(cur);
			if(online){
				roleMarryInfoDao.cacheUpdate(target, targetRoleId);
				//通知场景减属性
				synStageLongFengAtt(targetRoleId, null);
				synStageXinWuAttChange(targetRoleId, null);
				//通知玩家已离婚
				BusMsgSender.send2One(targetRoleId, ClientCmdType.NOTICE_TARGET_DIVORCE_SUCCESS, null);
				BusMsgSender.send2Stage(targetRoleId, InnerCmdType.MARRY_CHANGE_PEIOU, null);
				BusMsgSender.send2Stage(targetRoleId, InnerCmdType.MARRY_CHANGE_XINWU, null);
			}else{
				roleMarryInfoDao.update(target, targetRoleId, AccessType.getDirectDbType());
			}
		}
	}
	
	/**
	 * 转职强制取消订婚
	 * @return
	 */
	public void zhuanZhiCancelDing(Long userRoleId){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null || info.getState() != GameConstants.MARRY_STATE_DING){
			return;//当前未订婚
		}
		
		Long targetRoleId = info.getTargetRoleId();
		info.setState(GameConstants.MARRY_STATE_NO);
		info.setTargetRoleId(0l);
		roleMarryInfoDao.cacheUpdate(info, userRoleId);
		
		RoleMarryInfo target = null;
		boolean online = sessionManagerExportService.isOnline(targetRoleId);
		if(online){
			target = roleMarryInfoDao.cacheLoad(targetRoleId, targetRoleId);
		}else{
			target = roleMarryInfoDao.load(targetRoleId, targetRoleId, AccessType.getDirectDbType());
		}
		if(target == null){
			ChuanQiLog.error("玩家[{}]分手时对象未找到，对象id:{}",userRoleId,targetRoleId);
		}else{
			target.setState(GameConstants.MARRY_STATE_NO);
			target.setTargetRoleId(0l);
			if(online){
				roleMarryInfoDao.cacheUpdate(target, targetRoleId);
			}else{
				roleMarryInfoDao.update(target, targetRoleId, AccessType.getDirectDbType());
			}
		}

		cancelAddSchedule(userRoleId);
		if(online){
			cancelAddSchedule(targetRoleId);
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			//通知前任分手了
			BusMsgSender.send2One(targetRoleId, ClientCmdType.TARGET_CANCEL_DING, role.getName());
		}
		return;//分手成功
	}
	
	/**
	 * 获取角色结婚状态
	 * @param userRoleId
	 * @return
	 */
	public Integer getUserRoleJieHunStat(Long userRoleId){
		RoleMarryInfo info = roleMarryInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info == null){
			return 0;
		}else{
			return info.getState();
		}
	}
	
}
