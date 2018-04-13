package com.junyou.bus.rolebusiness.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.chengjiu.export.RoleChengJiuExportService;
import com.junyou.bus.rolebusiness.dao.RoleBusinessInfoDao;
import com.junyou.bus.rolebusiness.entity.RoleBusinessInfo;
import com.junyou.bus.rolebusiness.vo.FightingRankVo;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.PKPublicConfig;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.accessor.AccessType;
import com.kernel.sync.annotation.Sync;

/**
 * 角色业务数据Service
 * 
 * @author LiNing
 * @email anne_0520@foxmail.com
 * @date 2015-1-12 下午4:48:08 
 */
@Service
public class RoleBusinessInfoService {

	@Autowired
	private RoleBusinessInfoDao roleBusinessInfoDao;
	@Autowired
	private BusScheduleExportService busScheduleExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private RoleChengJiuExportService roleChengJiuExportService;
	
	public RoleBusinessInfo createRoleBusinessInfo(Long userRoleId){
		RoleBusinessInfo roleBusinessInfo = new RoleBusinessInfo();
		
		roleBusinessInfo.setUserRoleId(userRoleId);
		
		//战斗力
		roleBusinessInfo.setCurFighter(0l);
		roleBusinessInfo.setMaxFighter(0l);
		
		roleBusinessInfo.setPkVal(0);
		roleBusinessInfo.setZhenqi(0l);
		roleBusinessInfo.setRongluValue(0); 
		roleBusinessInfo.setJumpVal(GameConstants.TIAOSHAN_MAX_VAL);//设置跳闪值
		roleBusinessInfo.setRongyu(0); 
		
		roleBusinessInfo.setXuanTieValue(0);
		
		
		roleBusinessInfoDao.insert(roleBusinessInfo, userRoleId, AccessType.getDirectDbType());
		return roleBusinessInfo;
	}
	
	public RoleBusinessInfo getRoleBusinessInfo(Long userRoleId){
		return roleBusinessInfoDao.cacheLoad(userRoleId, userRoleId);
	}
	
	private void updateRoleBusinessInfo(RoleBusinessInfo roleBusinessInfo){
		roleBusinessInfoDao.cacheUpdate(roleBusinessInfo, roleBusinessInfo.getUserRoleId());
	}
	
	/**
	 * 获取角色战斗力[在线拉缓存，不在线的拉库]
	 * @param userRoleId
	 * @return
	 */
	public long getRoleBusOutFromDb4Catche(long userRoleId){
		if(publicRoleStateExportService.isPublicOnline(userRoleId)){
			//在线处理
			return roleBusinessInfoDao.cacheLoad(userRoleId, userRoleId).getCurFighter();
			
		}else{
			//不在线处理
			return roleBusinessInfoDao.getRoleBusinessInfoForDB(userRoleId).getCurFighter();
		}
	}	
	/**
	 * 获取玩家Pk公共配置数据
	 * @return
	 */
	private PKPublicConfig getPKPublicConfig(){
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_PK);
	}
	
	/**
	 * 红名定时开启
	 * @param userRoleId
	 * @param millTime
	 */
	private void schedulePkValue(Long userRoleId,int millTime){
		BusTokenRunable task = new BusTokenRunable(userRoleId, InnerCmdType.HONGMING_DERC, null);
		busScheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPONENET_HM, task, millTime, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 灰名定时开启
	 * @param userRoleId
	 * @param millTime
	 */
	private void scheduleHuiMing(Long userRoleId,int millTime){
		BusTokenRunable task = new BusTokenRunable(userRoleId, InnerCmdType.HUI_XS, null);
		busScheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPONENET_HUIM, task, millTime, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Pk上线业务
	 * @param roleBusinessInfo
	 * @param isChange
	 */
	private void onlinePKHandle(RoleBusinessInfo roleBusinessInfo, boolean isChange){
		//红名定时处理
		if(roleBusinessInfo.getPkVal() > 0){
			isChange = true;
			roleBusinessInfo.setHmStartTime(GameSystemTime.getSystemMillTime());
			schedulePkValue(roleBusinessInfo.getUserRoleId(), roleBusinessInfo.getHmTime().intValue());
		}
		
		//灰名定时处理
		if(roleBusinessInfo.getHuiTime() > 0){
			isChange = true;
			roleBusinessInfo.setHuiStartTime(GameSystemTime.getSystemMillTime());
			scheduleHuiMing(roleBusinessInfo.getUserRoleId(), roleBusinessInfo.getHuiTime().intValue());
		}
	}
	
	/**
	 * pk下线业务
	 * @param roleBusinessInfo
	 */
	private void offlinePKHandle(RoleBusinessInfo roleBusinessInfo){
		PKPublicConfig pkPublicConfig = getPKPublicConfig();
		if(pkPublicConfig == null){
			return;
		}
		
		int hmConfigTime = pkPublicConfig.getPkCleanTime() * 60 * 1000;
		int huimConfigTime = pkPublicConfig.getPkChiXu() * 1000;
		
		if(roleBusinessInfo.getHuiStartTime() > 0){
			long huiCalcMill = GameSystemTime.getSystemMillTime() - roleBusinessInfo.getHuiStartTime();
			//灰名
			if(huimConfigTime - huiCalcMill > 0){
				roleBusinessInfo.setHuiTime(huimConfigTime - huiCalcMill);
			}else{
				roleBusinessInfo.setHuiTime(0L);
				roleBusinessInfo.setHuiStartTime(0L);
			}
		}else{
			roleBusinessInfo.setHuiTime(0L);
			roleBusinessInfo.setHuiStartTime(0L);
		}
		
		long hmCalcMill = GameSystemTime.getSystemMillTime() - roleBusinessInfo.getHmStartTime();
		//红名
		if(hmConfigTime - hmCalcMill > 0){
			roleBusinessInfo.setHmTime(hmConfigTime - hmCalcMill);
		}
		
		updateRoleBusinessInfo(roleBusinessInfo);
	}
	
	/**
	 * 判断是否超过最大值
	 * @param accountValue
	 * @param addValue
	 * @return true 是
	 */
	private Boolean isOverMax(int accountValue,int addValue){
		
		if(GameConstants.MAX_VALUE - accountValue < addValue){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取该玩家的业务数据直接访问库
	 * @param userRoleId
	 * @return
	 */
	public RoleBusinessInfo getRoleBusinessInfoForDB(Long userRoleId){
		return roleBusinessInfoDao.getRoleBusinessInfoForDB(userRoleId);
	}
	
	/**
	 * 玩家Pk处理
	 * @param userRoleId
	 */
	public void rolePkAdd(Long userRoleId){
		RoleBusinessInfo roleBusinessInfo = getRoleBusinessInfo(userRoleId);
		
		if(roleBusinessInfo == null){
			return;
		}
		
		PKPublicConfig pkPublicConfig = getPKPublicConfig();
		if(pkPublicConfig == null){
			return;
		}
		
		// ---------------------添加红名---------------------------
		int hmConfigTime = pkPublicConfig.getPkCleanTime() * 60 * 1000;
		//pk值大于0则之前的红名减Pk值定时取消
		if(roleBusinessInfo.getPkVal() > 0){
			busScheduleExportService.cancelSchedule(userRoleId.toString(), GameConstants.COMPONENET_HM);
		}
		
		//红名Data
		int pkVal = roleBusinessInfo.getPkVal() + pkPublicConfig.getPkVal();
		roleBusinessInfo.setHmStartTime(GameSystemTime.getSystemMillTime());
		roleBusinessInfo.setHmTime(hmConfigTime * 1l);
		roleBusinessInfo.setPkVal(pkVal > pkPublicConfig.getPkMaxVal() ? pkPublicConfig.getPkMaxVal() : pkVal);
		
		//启动消除PK点定时
		schedulePkValue(userRoleId, hmConfigTime);
		
		updateRoleBusinessInfo(roleBusinessInfo);
		
		//通知场景PK值变化和灰名时间
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.ADD_PK_CHANGE, roleBusinessInfo.getPkVal());
		//成就
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_PKVALUE, roleBusinessInfo.getPkVal()});
			//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_PKVALUE, roleBusinessInfo.getPkVal());
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	/**
	 * 玩家Pk和灰名处理
	 * @param userRoleId
	 */
	public void rolePkAddHm(Long userRoleId){
		RoleBusinessInfo roleBusinessInfo = getRoleBusinessInfo(userRoleId);
		
		if(roleBusinessInfo == null){
			return;
		}
		
		PKPublicConfig pkPublicConfig = getPKPublicConfig();
		if(pkPublicConfig == null){
			return;
		}
		
		// ---------------------灰名处理---------------------------
		//之前的灰名消失定时取消
		busScheduleExportService.cancelSchedule(userRoleId.toString(), GameConstants.COMPONENET_HUIM);
		
		int huimConfigTime = pkPublicConfig.getPkChiXu() * 1000;
		roleBusinessInfo.setHuiStartTime(GameSystemTime.getSystemMillTime());//灰名开始时间改为现在
		roleBusinessInfo.setHuiTime(huimConfigTime * 1l);//玩家灰名时间
		
		//之前的灰名消失定时取消
		scheduleHuiMing(userRoleId, huimConfigTime);
		
		updateRoleBusinessInfo(roleBusinessInfo);
		
		//通知场景PK值变化和灰名时间
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.PK_HUI, huimConfigTime);
	}
	
	/**
	 * 定时减少 PK值
	 * @param userRoleId
	 */
	public void quertzDecreasePkValue(Long userRoleId){
		RoleBusinessInfo roleBusinessInfo = getRoleBusinessInfo(userRoleId);
		if(roleBusinessInfo == null){
			return;
		}
		
		PKPublicConfig pkPublicConfig = getPKPublicConfig();
		if(pkPublicConfig == null){
			return;
		}
		
		int beforePkValue = roleBusinessInfo.getPkVal();
		if(beforePkValue > 0){
			//减pk值
			roleBusinessInfo.setPkVal(beforePkValue - 1);
			int hmConfigTime = 0;
			
			//在启减少Pk值定时
			if(roleBusinessInfo.getPkVal() > 0){
				roleBusinessInfo.setHmStartTime(GameSystemTime.getSystemMillTime());
				hmConfigTime = pkPublicConfig.getPkCleanTime() * 60 * 1000;
				schedulePkValue(userRoleId, hmConfigTime);
			}else{
				roleBusinessInfo.setHmStartTime(0l);
			}
			
			roleBusinessInfo.setHmTime(hmConfigTime * 1l);
			updateRoleBusinessInfo(roleBusinessInfo);
			
			//通知场景PK值变化
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.ADD_PK_CHANGE, roleBusinessInfo.getPkVal());
		}
	}
	
	/**
	 * 定时 灰名消失
	 * @param userRoleId
	 */
	public void quertzDissolveHuiMing(Long userRoleId){
		RoleBusinessInfo roleBusinessInfo = getRoleBusinessInfo(userRoleId);
		if(roleBusinessInfo == null){
			return;
		}
		
		roleBusinessInfo.setHuiStartTime(0l);
		roleBusinessInfo.setHuiTime(0l);
		updateRoleBusinessInfo(roleBusinessInfo);
		
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.PK_HUI, 0);
	}
	
	/**
	 * 上线业务
	 * @param userRoleId
	 */
	public void onlineHandle(Long userRoleId){
		RoleBusinessInfo roleBusinessInfo = getRoleBusinessInfo(userRoleId);
		if(roleBusinessInfo == null){
			return;
		}
		
		boolean isChange = false;
		
		//Pk
		onlinePKHandle(roleBusinessInfo, isChange);
		
		if(isChange){
			updateRoleBusinessInfo(roleBusinessInfo);
		}
	}
	
	/**
	 * 下线业务
	 * @param userRoleId
	 */
	public void offlineHandle(Long userRoleId){
		RoleBusinessInfo roleBusinessInfo = getRoleBusinessInfo(userRoleId);
		if(roleBusinessInfo == null){
			return;
		}
		
		//Pk TODO 需要测试一下
		offlinePKHandle(roleBusinessInfo);
	}
	/**
	 * 获取战斗力排行数据
	 * @return
	 */
	public List<Object> getZhanLiRanks(){
		return roleBusinessInfoDao.getZhanLiRanks();
	}
	/**
	 * 保存角色战斗力
	 * @param userRoleId
	 * @param fighterValue
	 */
	@Sync(component = GameConstants.COMPONENT_BUS_SHARE,indexes = {0})
	public void roleSaveFighter(Long userRoleId,Long fighterValue){
		if(fighterValue == null || fighterValue == 0) return;
		
		if(!publicRoleStateExportService.isPublicOnline(userRoleId)){
			return;
		}
		
		try {
			RoleBusinessInfo roleBusinessInfo = getRoleBusinessInfo(userRoleId);
			roleBusinessInfo.setCurFighter(fighterValue);
			if(roleBusinessInfo.getMaxFighter() < fighterValue){
				roleBusinessInfo.setMaxFighter(fighterValue);
			}
			
			roleBusinessInfoDao.cacheUpdate(roleBusinessInfo, userRoleId);
			
			//成就推送
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_ZPLUS, roleBusinessInfo.getCurFighter().intValue()});
			//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_ZPLUS, roleBusinessInfo.getCurFighter().intValue());
		} catch (Exception e) {
			ChuanQiLog.error(""+e);
		}
	}
	/**
	 * 增加真气
	 * @param userRoleId
	 * @param value
	 */
	public void addZhenqi(long userRoleId,long value){
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		long zhenqi = business.getZhenqi() + value;
		business.setZhenqi(zhenqi);
		updateRoleBusinessInfo(business);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZQ_CHANGE, zhenqi);
	}
	
	/**
	 * 真气是否足够
	 * @param userRoleId
	 * @param value
	 */
	public boolean isEnoughZhenqi(long userRoleId,long value){
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		return business.getZhenqi() >= value;
	}
	/**
	 * 消耗真气
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public boolean costZhenqi(long userRoleId, long value){
		if(value < 0){
			return false;
		}
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		long zhenqi = business.getZhenqi() - value;
		if(zhenqi < 0)return false;
		business.setZhenqi(zhenqi);
		updateRoleBusinessInfo(business);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZQ_CHANGE, zhenqi);
		return true;
	}
	/**
	 * 增加熔炼值
	 * @param userRoleId
	 * @param value
	 */
	public Integer addRongLianVal(long userRoleId,int value){
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		int ronglian = business.getRongluValue() + value;
		business.setRongluValue(ronglian);
		updateRoleBusinessInfo(business);
		//BusMsgSender.send2One(userRoleId, ClientCmdType.ZQ_CHANGE, ronglian);
		return ronglian;
	}
	
	/**
	 * 熔炼值是否足够
	 * @param userRoleId
	 * @param value
	 */
	public boolean isEnoughRongLianVal(long userRoleId,int value){
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		return business.getRongluValue() >= value;
	}
	/**
	 * 消耗熔炼值
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public boolean costRongLianVal(long userRoleId, int value){
		if(value < 0){
			return false;
		}
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		int ronglian = business.getRongluValue() - value;
		if(ronglian < 0)return false;
		business.setRongluValue(ronglian);
		updateRoleBusinessInfo(business);
		//BusMsgSender.send2One(userRoleId, ClientCmdType.ZQ_CHANGE, ronglian);
		return true;
	}
	/**
	 * 增加玄铁值
	 * @param userRoleId
	 * @param value
	 */
	public void addXuanTieVal(long userRoleId,int value){
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		int ronglian = business.getXuanTieValue() + value;
		business.setXuanTieValue(ronglian);
		updateRoleBusinessInfo(business);
		//BusMsgSender.send2One(userRoleId, ClientCmdType.ZQ_CHANGE, ronglian);
	}
	
	/**
	 * 熔炼值是否足够
	 * @param userRoleId
	 * @param value
	 */
	public boolean isEnoughXuanTieVal(long userRoleId,int value){
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		return business.getXuanTieValue() >= value;
	}
	/**
	 * 消耗熔炼值
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public boolean costXuanTieVal(long userRoleId, int value){
		if(value < 0){
			return false;
		}
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		int ronglian = business.getXuanTieValue() - value;
		if(ronglian < 0)return false;
		business.setXuanTieValue(ronglian);
		updateRoleBusinessInfo(business);
		//BusMsgSender.send2One(userRoleId, ClientCmdType.ZQ_CHANGE, ronglian);
		return true;
	}
	/**
	 * 增加荣誉
	 * @param userRoleId
	 * @param value
	 */
	public void addRongyu(long userRoleId,int value){
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		int rongyu = business.getRongyu() + value;
		business.setRongyu(rongyu);
		updateRoleBusinessInfo(business);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RONGYU_CHANGE, rongyu);
	}
	
	/**
	 * 荣誉是否足够
	 * @param userRoleId
	 * @param value
	 */
	public boolean isEnoughRongyu(long userRoleId,int value){
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		return business.getRongyu() >= value;
	}
	/**
	 * 消耗荣誉
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public boolean costRongyu(long userRoleId, int value){
		if(value < 0){
			return false;
		}
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		int rongyu = business.getRongyu() - value;
		if(rongyu < 0)return false;
		business.setRongyu(rongyu);
		updateRoleBusinessInfo(business);
		BusMsgSender.send2One(userRoleId, ClientCmdType.RONGYU_CHANGE, rongyu);
		return true;
	}
	/**
	 * 增加修为
	 * @param userRoleId
	 * @param value
	 */
	public void addXiuwei(long userRoleId,long value){
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		value += business.getXiuwei();
		business.setXiuwei(value);
		updateRoleBusinessInfo(business);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIUWEI_CHARGE, value);
	}
	
	/**
	 * 修为是否足够
	 * @param userRoleId
	 * @param value
	 */
	public boolean isEnoughXiuwei(long userRoleId,int value){
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		return business.getXiuwei() >= value;
	}
	/**
	 * 消耗修为
	 * @param userRoleId
	 * @param value
	 * @return
	 */
	public boolean costXiuwei(long userRoleId, long value){
		if(value < 0){
			return false;
		}
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		value = business.getXiuwei() - value;
		if(value < 0)return false;
		business.setXiuwei(value);
		updateRoleBusinessInfo(business);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIUWEI_CHARGE, value);
		return true;
	}
	
	
	/**
	 * 添加跳闪值
	 * @param userRoleId
	 * @param value
	 */
	public void addTiaoShan(long userRoleId,int value){
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		int tiaoShan = business.getJumpVal() +value;
		business.setJumpVal(tiaoShan);
	}

	/**
	 * 消耗跳闪值
	 * @param userRoleId
	 * @param value：  若为null表示成功，其他的是错误码
	 */
	public Object[] costTiaoShan(long userRoleId, int value) {
		
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		if(value <0 || value <  business.getJumpVal()){
			return AppErrorCode.TIAOSHAN_ERROR;
		}
		
		int tiaoShan = business.getJumpVal() - value;
		business.setJumpVal(tiaoShan); 
		BusMsgSender.send2One(userRoleId, ClientCmdType.TIAO_SHAN, tiaoShan);
		return null;
	}

	/**
	 * 更新吆喝时间
	 * @param userRoleId
	 * @param systemMillTime
	 */
	public void updateYHLastTime(long userRoleId,long systemMillTime) {
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		business.setLastYhTime(systemMillTime);
		updateRoleBusinessInfo(business);
	}
	
	public List<FightingRankVo> getFightingRankVo(int limit) {
	
		return 	roleBusinessInfoDao.getFightingRankVo(limit);
	}
	public long getUserServerRank(String userId,String serverId){
		return roleBusinessInfoDao.getUserServerRank(userId, serverId);
	}
	
	public boolean clearPkValue(Long userRoleId,int value,boolean notice){
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		int pkValue = business.getPkVal();
		if(pkValue == 0){
			return false;
		}
		pkValue -= value;
		if(pkValue < 0){
			pkValue = 0;
		}else if(pkValue == 0){
			business.setHmStartTime(0l);
			busScheduleExportService.cancelSchedule(userRoleId.toString(), GameConstants.COMPONENET_HM);
		}
		business.setPkVal(pkValue);
		updateRoleBusinessInfo(business);
		if(pkValue == 0 || notice){
			//通知场景PK值变化
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.ADD_PK_CHANGE, pkValue);
		}
		return true;
	}
	
	public void changeKeepTime(Long userRoleId,Long userTypeTime,boolean isOnline){
		RoleBusinessInfo business = null;
		if(isOnline){
			business = getRoleBusinessInfo(userRoleId);
		}else{
			business = getRoleBusinessInfoForDB(userRoleId);
		}
		business.setUserTypeTime(userTypeTime + GameSystemTime.getSystemMillTime());
		if(isOnline){
			roleBusinessInfoDao.cacheUpdate(business, userRoleId);
		}else{
			roleBusinessInfoDao.update(business, userRoleId, AccessType.getDirectDbType());
		}
	}
	
	/**
	 * 检测时间是否已到期
	 * @param userRoleId
	 * @return
	 */
	public boolean checkKeepTime(Long userRoleId){
		RoleBusinessInfo business = getRoleBusinessInfo(userRoleId);
		if(business == null){
			return false;
		}
		if(business.getUserTypeTime() != null && business.getUserTypeTime() < GameSystemTime.getSystemMillTime()){
			business.setUserTypeTime(0l);
			roleBusinessInfoDao.cacheUpdate(business, userRoleId);
			return true;
		}
		return false;
	}
}
