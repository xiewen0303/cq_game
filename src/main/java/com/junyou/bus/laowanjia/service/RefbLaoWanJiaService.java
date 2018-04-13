package com.junyou.bus.laowanjia.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.laowanjia.configue.LaoWanJiaConfig;
import com.junyou.bus.laowanjia.configue.LaoWanJiaConfigExportService;
import com.junyou.bus.laowanjia.configue.LaoWanJiaConfigGroup;
import com.junyou.bus.laowanjia.dao.RefbLaowanjiaDao;
import com.junyou.bus.laowanjia.entity.RefbLaowanjia;
import com.junyou.bus.laowanjia.filter.RefbLaoWanJiaFilter;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * @author zhongdian
 * 2016-3-21 下午2:16:15
 */
@Service
public class RefbLaoWanJiaService {

	@Autowired
	private RefbLaowanjiaDao refbLaowanjiaDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	
	public List<RefbLaowanjia> initRefbLaowanjias(Long userRoleId){
		return refbLaowanjiaDao.initRefbLaowanjia(userRoleId);
	}
	public boolean isXianShiActity(Long userRoleId,int subId){
		List<RefbLaowanjia> list = refbLaowanjiaDao.cacheLoadAll(userRoleId, new RefbLaoWanJiaFilter(subId));
		if(list == null || list.size() <= 0){
			return false;
		}
		return true;
	}
	
	/**
	 *  上线处理
	 * @param userRoleId
	 * @return
	 */
	public void onlineHandle(Long userRoleId) {
		Map<Integer, LaoWanJiaConfigGroup> groups = LaoWanJiaConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		
		//循环充值礼包配置数据
		for(Map.Entry<Integer, LaoWanJiaConfigGroup> entry : groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			LaoWanJiaConfigGroup config = LaoWanJiaConfigExportService.getInstance().loadByMap(entry.getKey());
			if(config == null){
				continue;
			}
			List<RefbLaowanjia> list = refbLaowanjiaDao.cacheLoadAll(userRoleId, new RefbLaoWanJiaFilter(entry.getKey()));
			if(list != null && list.size() > 0){
				//推送通知客户端显示活动显示
				BusMsgSender.send2One(userRoleId, ClientCmdType.LAOWANJIA_TUISONG, 1);
				//如果跨天  则加一天登录时间
				RefbLaowanjia  laowanjia = list.get(0);
				if(!DateUtils.isSameDay(laowanjia.getUpdateTime(), new Timestamp(GameSystemTime.getSystemMillTime()))){
					laowanjia.setLoginDay(laowanjia.getLoginDay() + 1);
					laowanjia.setUpdateTime(new Timestamp(System.currentTimeMillis()));
					refbLaowanjiaDao.cacheUpdate(laowanjia, userRoleId);
				}
				continue;
			}
			//获取玩家上次离线时间
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			if(role == null){
				return;
			}
			long time =  DatetimeUtil.addDays(role.getOfflineTime(), config.getWeiLogin());
			long dTime = GameSystemTime.getSystemMillTime();
			if(time <= dTime){
				RefbLaowanjia laowanjia = new RefbLaowanjia();
				laowanjia.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
				laowanjia.setUserRoleId(userRoleId);
				laowanjia.setLoginDay(1);
				laowanjia.setRechargeVal(0);
				laowanjia.setSubId(entry.getKey());
				laowanjia.setLingquStatus("");
				laowanjia.setCreateTime(new Timestamp(System.currentTimeMillis()));
				laowanjia.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				refbLaowanjiaDao.cacheInsert(laowanjia, userRoleId);
				//推送通知客户端显示活动显示
				BusMsgSender.send2One(userRoleId, ClientCmdType.LAOWANJIA_TUISONG, 1);
			}
		}
	}
	
	private RefbLaowanjia getLaowanjia(Long userRoleId,int subId){
		List<RefbLaowanjia> list = refbLaowanjiaDao.cacheLoadAll(userRoleId, new RefbLaoWanJiaFilter(subId));
		if(list == null || list.size() <= 0){
			/*RefbLaowanjia laowanjia = new RefbLaowanjia();
			
			laowanjia.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			laowanjia.setUserRoleId(userRoleId);
			laowanjia.setLoginDay(1);
			laowanjia.setRechargeVal(0);
			laowanjia.setSubId(subId);
			laowanjia.setLingquStatus("");
			laowanjia.setCreateTime(new Timestamp(System.currentTimeMillis()));
			laowanjia.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			refbLaowanjiaDao.cacheInsert(laowanjia, userRoleId);
			
			return laowanjia;*/
			return null;
		}
		return list.get(0);
	}
	
	public Object[] getLaoWanJiaInfo(Long userRoleId,Integer version,int subId){
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
		RefbLaowanjia laowanjia = getLaowanjia(userRoleId, subId);
		if(laowanjia== null){
			return AppErrorCode.LAOWANJIA_IS_NO;
		}
		LaoWanJiaConfigGroup config = LaoWanJiaConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		return new Object[]{1,subId,laowanjia.getRechargeVal(),laowanjia.getLoginDay(),getLingQuStatus(laowanjia, config.getConfigMap())};
	}
	
	
	public Object[] lingqu(Long userRoleId,Integer version,int subId,int configId,int type){
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
		LaoWanJiaConfig config = LaoWanJiaConfigExportService.getInstance().loadByKeyId(subId,configId);
		if(config== null){
			return AppErrorCode.CONFIG_ERROR;
		}
		RefbLaowanjia laowanjia = getLaowanjia(userRoleId, subId);
		if(laowanjia== null){
			return AppErrorCode.LAOWANJIA_IS_NO;
		}
		if(type == 0){
			//判断是否已经领取过了
			if(isLingQu(laowanjia.getLingquStatus(), getJitemAIdById(configId)) == 1){
				return AppErrorCode.GET_ALREADY;
			}
			//判断登陆天数
			if(laowanjia.getLoginDay() < config.getLoginDay()){
				return AppErrorCode.LOGIN_DAYS_LESS;
			}
			//检查物品是否可以进背包
			Object[] bagCheck = roleBagExportService.checkPutInBagVo(config.getJianLiMapA(), userRoleId);
			if(bagCheck != null){
				return bagCheck;
			}
			//更新玩家领取状态
			laowanjia.setLingquStatus(updateLingQuStatus(laowanjia.getLingquStatus(), getJitemAIdById(configId)));
			laowanjia.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			refbLaowanjiaDao.cacheUpdate(laowanjia, userRoleId);
			
			//发放奖励
			roleBagExportService.putGoodsVoAndNumberAttr(config.getJianLiMapA(), userRoleId, GoodsSource.LAOWANJIA_HUIGUI, LogPrintHandle.GET_RFB_LAOWANJIA_HUIGUI, LogPrintHandle.GBZ_RFB_LAOWANJIA_HUIGUI, true);
		}else{
			//判断是否已经领取过了
			if(isLingQu(laowanjia.getLingquStatus(), getJitemBIdById(configId)) == 1){
				return AppErrorCode.GET_ALREADY;
			}
			//判断充值的元宝数
			if(laowanjia.getRechargeVal() < config.getReGold()){
				return AppErrorCode.LAOWANJIA_RE_NO;
			}
			//检查物品是否可以进背包
			Object[] bagCheck = roleBagExportService.checkPutInBagVo(config.getJianLiMapB(), userRoleId);
			if(bagCheck != null){
				return bagCheck;
			}
			//更新玩家领取状态
			laowanjia.setLingquStatus(updateLingQuStatus(laowanjia.getLingquStatus(), getJitemBIdById(configId)));
			laowanjia.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			refbLaowanjiaDao.cacheUpdate(laowanjia, userRoleId);
			//发放奖励
			roleBagExportService.putGoodsVoAndNumberAttr(config.getJianLiMapB(), userRoleId, GoodsSource.LAOWANJIA_HUIGUI, LogPrintHandle.GET_RFB_LAOWANJIA_HUIGUI, LogPrintHandle.GBZ_RFB_LAOWANJIA_HUIGUI, true);
		}
		
		return new Object[]{1,subId,configId,type};
	}
	
	private Object[] getLingQuStatus(RefbLaowanjia laowanjia,Map<Integer, LaoWanJiaConfig> configMap){
		List<Object[]> list = new ArrayList<>();
		if(configMap == null || configMap.size() <= 0){
			return null;
		}
		for(Map.Entry<Integer, LaoWanJiaConfig> entry : configMap.entrySet()){
			int id = entry.getKey();
			list.add(new Object[]{id,
					isLingQu(laowanjia.getLingquStatus(), getJitemAIdById(id)),
					isLingQu(laowanjia.getLingquStatus(), getJitemBIdById(id)),
					}
			);
		}
		return list.toArray();
	}
	
	private String updateLingQuStatus(String lingquStatus,String id){
		if(lingquStatus == null || "".equals(lingquStatus)){
			return id;
		}else{
			return lingquStatus + "," + id;
		}
	}
	
	/**
	 * 判断奖励是否已经领取
	 * @param lingquStatus
	 * @param id
	 * @return 0：未领取,1：已领取
	 */
	private int isLingQu(String lingquStatus,String id){
		if(lingquStatus == null || "".equals(lingquStatus)){
			return 0;
		}
		String[] str = lingquStatus.split(",");
		for (int i = 0; i < str.length; i++) {
			if(str[i].equals(id)){
				return 1;
			}
		}
		return 0;
	}
	
	/**
	 * 获取登陆奖励的ID
	 * @param id
	 * @return
	 */
	private String getJitemAIdById(int id){
		return id + "-" + GameConstants.LAOWANJIA_LOGIN;
	}
	/**
	 * 获取充值奖励的ID
	 * @param id
	 * @return
	 */
	private String getJitemBIdById(int id){
		return id + "-" + GameConstants.LAOWANJIA_RECHARGE;
	}
	
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		LaoWanJiaConfigGroup config = LaoWanJiaConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		//处理数据
		RefbLaowanjia laowanjia = getLaowanjia(userRoleId, subId);		
		if(laowanjia== null){
			return null;
		}
		Map<Integer, LaoWanJiaConfig> configMap = config.getConfigMap();
		
		return new Object[]{
				config.getPic(),
				config.getDes(),
				config.getDengLuVo().toArray(),
				getLingQuStatus(laowanjia, configMap),
				laowanjia.getRechargeVal(),
				laowanjia.getLoginDay()
				
		};
	}
	
	
	public void rechargeYb(Long userRoleId,Long addVal){
		if(addVal < 0){
			return;
		}
		Map<Integer, LaoWanJiaConfigGroup> groups = LaoWanJiaConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		//循环充值礼包配置数据
		for(Map.Entry<Integer, LaoWanJiaConfigGroup> entry : groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			
			RefbLaowanjia laowanjia = getLaowanjia(userRoleId, entry.getKey());		
			if(laowanjia== null){
				continue;
			}
			laowanjia.setRechargeVal((int) (laowanjia.getRechargeVal()+addVal));
			laowanjia.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			refbLaowanjiaDao.cacheUpdate(laowanjia, userRoleId);
		}
	}
	
}
