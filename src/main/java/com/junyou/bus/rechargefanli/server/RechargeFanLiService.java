/**
 * 
 */
package com.junyou.bus.rechargefanli.server;


import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.rechargefanli.configure.export.RechargeFanLiConfigExportService;
import com.junyou.bus.rechargefanli.configure.export.RechargeFanLiConfigGroup;
import com.junyou.bus.rechargefanli.dao.RefabuRefanliDao;
import com.junyou.bus.rechargefanli.entity.RefabuRefanli;
import com.junyou.bus.rechargefanli.filter.RechargeFanLiFilter;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.entity.RoleYuanbaoRecord;
import com.junyou.bus.rfbactivity.service.RoleYuanbaoRecordService;
import com.junyou.bus.rfbactivity.util.ActivityTimeType;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;



/**
 * @description
 * @author ZHONGDIAN
 * @created 2011-11-16上午10:29:07
 */
@Service
public class RechargeFanLiService { 
	
	@Autowired
	private RefabuRefanliDao refabuRefanliDao;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleYuanbaoRecordService roleYuanbaoRecordService;
	
	public List<RefabuRefanli> initRefabuRefanli(Long userRoleId){
		return refabuRefanliDao.initRefabuRefanli(userRoleId);
	}
	
	private RefabuRefanli getRefabuRefanli(Long userRoleId,int subId){
		List<RefabuRefanli> list = refabuRefanliDao.cacheLoadAll(userRoleId, new RechargeFanLiFilter(subId));
		if(list == null || list.size() <= 0){
			RefabuRefanli fanli = new RefabuRefanli();
			fanli.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			fanli.setSubId(subId);
			fanli.setUserRoleId(userRoleId);
			fanli.setFanliGold(0);
			fanli.setLeijiGold(0);
			fanli.setTheGold(0);
			fanli.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			fanli.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			
			refabuRefanliDao.cacheInsert(fanli, userRoleId);
			
			return fanli;
		}
		return list.get(0);
	}
	
	public Object[] lingqu(Long userRoleId,Integer version,int subId){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		
		//版本不一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[]{subId,configSong.getClientVersion(),newSubHandleData};
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
			return null;
		}
		
		//判断配置
		RechargeFanLiConfigGroup config = RechargeFanLiConfigExportService.getInstance().loadByMap(subId);
		if(config== null){
			return AppErrorCode.CONFIG_ERROR;
		}
		RefabuRefanli fanli = getRefabuRefanli(userRoleId, subId);
		//玩家可领取元宝
		if(fanli.getLeijiGold() < config.getMinGold()){
			return AppErrorCode.RECHARGE_FANLI_MINGOLD;
		}
		if(fanli.getFanliGold() <= 0){
			return AppErrorCode.RECHARGE_FANLI_NOGOLD;
		}
		int gold = fanli.getFanliGold();
		//更新玩家领取状态
		fanli.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		fanli.setFanliGold(0);
		fanli.setTheGold(0);
		
		refabuRefanliDao.cacheUpdate(fanli, userRoleId);
		
		//增加元宝
		if(gold > 0){
			accountExportService.incrCurrencyWithNotify(GoodsCategory.GOLD, gold, userRoleId, LogPrintHandle.GET_RFB_CZ_FANLI, LogPrintHandle.GBZ_RFB_CZ_FANLI);
		}	
		
		return new Object[]{1,subId};
	}
	
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){

		RechargeFanLiConfigGroup config = RechargeFanLiConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		//处理数据
		RefabuRefanli fanli = getRefabuRefanli(userRoleId, subId);		
		//判断活动循环数据
		updateJianCe(subId, fanli);
		//活动迟上线，记录今天已经充值的元宝数
		yuanbaoJianCe(fanli,config.getGoldRatio());
		
		return new Object[]{
				config.getPic(),
				config.getDes(),
				config.getMinGold(),
				fanli.getLeijiGold(),
				fanli.getTheGold(),
				config.getGoldRatio()+"",
				fanli.getFanliGold()
				
		};
		
	}
	
	private void yuanbaoJianCe(RefabuRefanli fanli,float ratio){
		RoleYuanbaoRecord record = roleYuanbaoRecordService.getRoleYuanBaoRecord(fanli.getUserRoleId());
		if(record.getCzValue() > fanli.getLeijiGold()){
			fanli.setTheGold(fanli.getTheGold() + (record.getCzValue() - fanli.getLeijiGold()));
			int fanliGold = (int) ((record.getCzValue() - fanli.getLeijiGold()) * ratio);
			fanli.setFanliGold(fanli.getFanliGold() + fanliGold);
			fanli.setLeijiGold(record.getCzValue());
			fanli.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			refabuRefanliDao.cacheUpdate(fanli, fanli.getUserRoleId());
		}
	}
	
	private void updateJianCe(int subId,RefabuRefanli fanli){
		//判断活动是否结束
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		/*if(configSong.getTimeType() != ActivityTimeType.TIME_4_KAI_FU_LOOP && configSong.getTimeType() != ActivityTimeType.TIME_5_HE_FU_LOOP){
			return;
		}*/
		long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
		long upTime = fanli.getUpdateTime().getTime();
		long dTime = GameSystemTime.getSystemMillTime();
		if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
			fanli.setFanliGold(0);
			fanli.setLeijiGold(0);
			fanli.setTheGold(0);
			fanli.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			refabuRefanliDao.cacheUpdate(fanli, fanli.getUserRoleId());
		}
	}
	
	
	public Object[] getRefbLingQuStatus(Long userRoleId, Integer subId){
		RechargeFanLiConfigGroup config = RechargeFanLiConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		//处理数据
		RefabuRefanli fanli = getRefabuRefanli(userRoleId, subId);		

		Object[] obj = new Object[]{
				fanli.getLeijiGold(),
				fanli.getTheGold(),
				fanli.getFanliGold()
				
		};
		
		return new Object[]{subId,obj};
				
	}
	
	
	public void rechargeYb(Long userRoleId,Long addVal){
		if(addVal < 0){
			return;
		}
		Map<Integer, RechargeFanLiConfigGroup> groups = RechargeFanLiConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		//循环充值礼包配置数据
		for(Map.Entry<Integer, RechargeFanLiConfigGroup> entry : groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			RechargeFanLiConfigGroup config = entry.getValue();
			RefabuRefanli fanli = getRefabuRefanli(userRoleId, entry.getKey());		
			//检测
			updateJianCe(entry.getKey(), fanli);
			
			fanli.setLeijiGold((int) (fanli.getLeijiGold() + addVal));
			fanli.setTheGold((int) (fanli.getTheGold() + addVal));
			int yb = (int) (addVal * config.getGoldRatio());
			fanli.setFanliGold(fanli.getFanliGold() + yb);
			fanli.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			refabuRefanliDao.cacheUpdate(fanli, userRoleId);
		}
	}
	
}