package com.junyou.bus.shouchong.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.entity.RoleYuanbaoRecord;
import com.junyou.bus.rfbactivity.export.RefabuActivityExportService;
import com.junyou.bus.rfbactivity.service.AbstractActivityService;
import com.junyou.bus.rfbactivity.util.ActivityTimeType;
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.shouchong.configure.RfbShouChongConfigExportService;
import com.junyou.bus.shouchong.configure.ShouChongActivityConfig;
import com.junyou.bus.shouchong.configure.ShouChongGroupConfig;
import com.junyou.bus.shouchong.dao.RefbRoleShouchongDao;
import com.junyou.bus.shouchong.entity.RefbRoleShouchong;
import com.junyou.bus.shouchong.filter.RefbShouCongFilter;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.err.AppErrorCode;
import com.junyou.event.RfbActivityPartInLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * 类首充活动
 */
@Service
public class RefbRoleShouchongService extends AbstractActivityService {

	@Override
	public boolean getChildFlag(long userRoleId,int subId) {
		ActivityConfigSon configSon = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSon == null){
			return false;
		}
		int subType = configSon.getSubActivityType();
		
		RefbRoleShouchong shoucong = getRefbRoleShouchong(userRoleId, subId);
		if(shoucong == null){
			ChuanQiLog.debug("shoucong is null,subId:{}",subId);
			return false;
		}
		ShouChongGroupConfig group = RfbShouChongConfigExportService.getInstance().loadById(subId);
		if(group == null){
			ChuanQiLog.debug("group is null,subId:{}",subId);
			return false;
		}
		int receiveState = shoucong.getReceiveState();
		
		if(subType == ReFaBuUtil.SOUCHONG_TYPE){
			ShouChongActivityConfig shouChongActivityConfig = group.getShouChongDangByIndex(receiveState);
			if(shouChongActivityConfig == null){
				ChuanQiLog.debug("shouChongActivityConfig is null,subId:{},receiveState:{}",subId,receiveState);
				return false;
			}
			
			//本档首充元宝不够
			if(shouChongActivityConfig.getNeedYb() <= shoucong.getLjYbVal()){
				return true;
			}
			
		}else if(subType == ReFaBuUtil.LOOP_DAY_CHONGZHI_TYPE){
			Map<Integer, ShouChongActivityConfig> shouChongActivityConfigs = group.getDayRechargeMap();
			
			for (Map.Entry<Integer, ShouChongActivityConfig> element : shouChongActivityConfigs.entrySet()) {
				ShouChongActivityConfig config = element.getValue();
				if(config.getConfigId() <0){
					continue;
				}
				//状态判定，是否已经领取
				if(BitOperationUtil.calState(receiveState, element.getKey()) && config.getNeedYb() <= shoucong.getLjYbVal()){
					return true;
				}
			}
		}
		
		return false;
	}


	@Autowired
	private RefbRoleShouchongDao refbRoleShouchongDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RefabuActivityExportService refabuActivityExportService;
	
	
	public List<RefbRoleShouchong> initRefbRoleShouchong(Long userRoleId){
		return refbRoleShouchongDao.initRefbRoleShouchong(userRoleId);
	}
	
	/**
	 * 根据子活动获取累计充值元宝金额
	 * @param userRoleId
	 * @param subId
	 * @return null:没有充值金额
	 */
	public Object getRechargeValueBySubId2(Long userRoleId,int subId){
		RefbRoleShouchong shoucong = getRefbRoleShouchong(userRoleId, subId);
		//检查下充值记录 
		RoleYuanbaoRecord roleYuanbaoRecord =refabuActivityExportService.getRoleYuanbaoRecord(userRoleId);
		if(shoucong == null){
			if(roleYuanbaoRecord!=null && roleYuanbaoRecord.getCzValue()>0){
				 shoucong = createRefbRoleShouchong(userRoleId,Long.valueOf(roleYuanbaoRecord.getCzValue()),subId);
				refbRoleShouchongDao.cacheInsert(shoucong, userRoleId);
				return  new Object[]{subId,shoucong.getLjYbVal()};
			}
			return null;
		}else{
			if(roleYuanbaoRecord!=null && roleYuanbaoRecord.getCzValue()>shoucong.getLjYbVal()){
				shoucong.setLjYbVal(roleYuanbaoRecord.getCzValue());
				refbRoleShouchongDao.cacheUpdate(shoucong, userRoleId);
				return  new Object[]{subId,shoucong.getLjYbVal()};
			}
			return new Object[]{subId,shoucong.getLjYbVal()};
		}
	}
	
	public Object getRechargeValueBySubId(Long userRoleId,int subId){
		RefbRoleShouchong shoucong = getRefbRoleShouchong(userRoleId, subId);
		//检查下充值记录 
		shoucong  = updateRefbRoleShouchong(userRoleId,shoucong,subId);
		if(shoucong == null){
			return  null;
		}else{
			return  new Object[]{subId,shoucong.getLjYbVal(),new int[]{BitOperationUtil.calState(shoucong.getReceiveState(),1)?0:1,BitOperationUtil.calState(shoucong.getReceiveState(), 2)?0:1,BitOperationUtil.calState(shoucong.getReceiveState(), 3)?0:1}};
		}
	}
	/**
	 * 更新当日充值记录 ，如果是充值在上活动之前的话！
	 */
	private RefbRoleShouchong updateRefbRoleShouchong(Long userRoleId,RefbRoleShouchong shoucong,int subId){
		//检查下充值记录 
		RoleYuanbaoRecord roleYuanbaoRecord =refabuActivityExportService.getRoleYuanbaoRecord(userRoleId);
		if(shoucong == null){
			if(roleYuanbaoRecord!=null && roleYuanbaoRecord.getCzValue()>0){
				shoucong = createRefbRoleShouchong(userRoleId,Long.valueOf(roleYuanbaoRecord.getCzValue()),subId);
				refbRoleShouchongDao.cacheInsert(shoucong, userRoleId);
			}
		}else{
			if(roleYuanbaoRecord!=null && roleYuanbaoRecord.getCzValue()>shoucong.getLjYbVal()){
				shoucong.setLjYbVal(roleYuanbaoRecord.getCzValue());
				refbRoleShouchongDao.cacheUpdate(shoucong, userRoleId);
			}
		}
		return shoucong;
	}
	/**
	 * 获取首充数据
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object getShouChongRechargeInfo(Long userRoleId,int subId){
		ShouChongGroupConfig group = RfbShouChongConfigExportService.getInstance().loadById(subId);
		if(group == null){
			return null;
		}
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSong == null){
			return null;
		}
		
		int rechargeYb = 0;
		int rechargeState = 0;
		RefbRoleShouchong shoucong = getRefbRoleShouchong(userRoleId, subId);
		//判断循环
		if(shoucong != null){
			shoucong = updateJianCe(subId, shoucong);
		}
		shoucong =updateRefbRoleShouchong(userRoleId,shoucong,subId);
		if(shoucong != null){
			rechargeYb = shoucong.getLjYbVal();
			rechargeState = shoucong.getReceiveState();
		}
		
		
		ShouChongActivityConfig shouChongConfig = group.getShouChongDangByIndex(rechargeState);
		if(shouChongConfig == null){
			return null;
		}
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		byte job = role.getConfigId().byteValue();
		
		Object[] data = getClientShouChongData(job, shouChongConfig, rechargeYb);
		return data;
	}
	
	/**
	 * 更新首冲活动
	 * @param subId
	 * @param shoucong
	 * @return
	 */
	private RefbRoleShouchong updateJianCe(int subId,RefbRoleShouchong shoucong){
		//判断活动是否结束
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
		long upTime = shoucong.getUpdateTime();
		long dTime = GameSystemTime.getSystemMillTime();
		if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
			shoucong.setLjYbVal(0);
			shoucong.setReceiveState(0);
			shoucong.setUpdateTime(GameSystemTime.getSystemMillTime());
			refbRoleShouchongDao.cacheUpdate(shoucong, shoucong.getUserRoleId());
		}
		return shoucong;
	}
	
	/**
	 * 获取客户端首充数据</br>
	 * 0	Array	包含通用奖励和职业奖励[ [0:String(对应职业奖励物品),1:(数量),2:(如果是装备，则此位为强化等级)],[0:String(对应职业奖励物品),1:(数量),2:(如果是装备，则此位为强化等级)]…]</br>
		1	String	礼包名字(name字段)</br>
		2	int	充值需要的元宝(need字段)</br>
		3	int	已充值元宝数</br>
		4	String	对应职业的bg</br>
		5	String	对应职业的res</br>
		6	String	btn0</br>
		7	String	btn1</br>
		8	String	对应职业的show字段</br>
	 */
	private Object[] getClientShouChongData(byte job,ShouChongActivityConfig shouChongConfig,int rechargeYb){
		
		Object[] data = new Object[]{
			shouChongConfig.getBgStringByJob(job)
			,shouChongConfig.getJianLiClientDataByJob(job)
			,shouChongConfig.getName()
			,shouChongConfig.getNeedYb()
			,rechargeYb
			,shouChongConfig.getResStringByJob(job)
			,shouChongConfig.getBtn0()
			,shouChongConfig.getBtn1()
			,shouChongConfig.getShowMapByJob(job)
		};
		
		return data;
	}
	
	/**
	 * 获取客户端首充数据</br>
	 * 0	Array	包含通用奖励和职业奖励[ [0:String(对应职业奖励物品),1:(数量),2:(如果是装备，则此位为强化等级)],[0:String(对应职业奖励物品),1:(数量),2:(如果是装备，则此位为强化等级)]…]</br>
		1	String	礼包名字(name字段)</br>
		2	int	充值需要的元宝(need字段)</br>
		3	int	 领奖状态（0：未领奖,1:已领奖）</br>
		4	String	对应职业的bg</br>
		5	String	对应职业的res</br>
		6	String	btn0</br>
		7	String	btn1</br>
		8	String	对应职业的show字段</br>
	 */
	private Object[] getClientLoopDayChongZhiData(byte job,Map<Integer, ShouChongActivityConfig> resultMaps ,int rechargeState){
		if(resultMaps == null || resultMaps.size() <= 0 ){
			return null;
		}
		Object[] result = new Object[resultMaps.size()];
		
		for (int i = 0; i < resultMaps.size(); i++) {
			ShouChongActivityConfig  config = resultMaps.get(i+1);
			if(config == null){
				ChuanQiLog.error("loop day config is not exits,id="+(i+1));
				continue;
			}
			int state = BitOperationUtil.calState(rechargeState, i+1) ? 0 :1;
			Object[] data = new Object[]{
					config.getJianLiClientDataByJob(job)
					,config.getName()
					,config.getNeedYb()
					,state
					,config.getBgStringByJob(job)
					,config.getResStringByJob(job)
					,config.getBtn0()
					,config.getBtn1()
					,config.getShowMapByJob(job)
					,config.getConfigId()
				};
			result[i] = data;
		}
		
		return result;
	}
	
	
	/**
	 * 领取首充奖励
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] receiveShouChongJianLi(Long userRoleId,int subId,int version){
		ShouChongGroupConfig group = RfbShouChongConfigExportService.getInstance().loadById(subId);
		if(group == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		
		//版本一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object newSubHandleData = getShouChongRechargeInfo(userRoleId, subId);
			Object[] data = new Object[]{subId,configSong.getClientVersion(),newSubHandleData};
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
			return null;
		}
		
		RefbRoleShouchong refbRole = getRefbRoleShouchong(userRoleId, subId);
		//没有充值记录
		if(refbRole == null){
			return AppErrorCode.NO_RECHARGE_ERROR;
		}
		
		int receiveState = refbRole.getReceiveState();
		ShouChongActivityConfig shouChongActivityConfig = group.getShouChongDangByIndex(receiveState);
		if(shouChongActivityConfig == null){
			//10102 特殊错误推送，礼包已经全部领完 (容错，推送这个指令客户端会关闭，领取按键)
			BusMsgSender.send2One(userRoleId, ClientCmdType.TESHU_SHOUCHONG,subId);
			return null;
		}
		
		//本档首充元宝不够
		if(shouChongActivityConfig.getNeedYb() > refbRole.getLjYbVal()){
			return AppErrorCode.SC_YB_NO_ENOU;
		}
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		byte job = role.getConfigId().byteValue();
		
		Map<String, GoodsConfigureVo> rewardItems = shouChongActivityConfig.getJianLiMapByJob(job);
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBagVo(rewardItems, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		
		//改变状态并保存
		refbRole.setReceiveState(receiveState + 1);
		refbRoleShouchongDao.cacheUpdate(refbRole, userRoleId);
		
		//发放奖励
		int bz = LogPrintHandle.GBZ_RFB_SHOUCHONG1 + receiveState;
		roleBagExportService.putGoodsVoAndNumberAttr(rewardItems, userRoleId, GoodsSource.GOODS_GET_SC, LogPrintHandle.GET_RFB_SHOUCHONG, bz, true);

		//下一个礼包数据
		Object nextData = null;
		ShouChongActivityConfig nextScConfig = group.getShouChongDangByIndex(receiveState + 1);
		if(nextScConfig != null){
			nextData = getClientShouChongData(job, nextScConfig, refbRole.getLjYbVal());
		}
		
		//首充类领取全服公告
		sendAllServer(userRoleId,shouChongActivityConfig, configSong);
		
		//检查通知客服端关闭掉Icon提示
		checkIconFlag(userRoleId, subId);
		
		//打印活动参与日志
		GamePublishEvent.publishEvent(
          new RfbActivityPartInLogEvent(
                  LogPrintHandle.REFABU_SHOUCHONG,
                  configSong.getActivityId(), 
                  configSong.getSubName(), 
                  configSong.getSubActivityType(), 
                  configSong.getStartTimeByMillSecond(), 
                  configSong.getEndTimeByMillSecond(), 
                  userRoleId
          )
		);
		return new Object[]{AppErrorCode.SUCCESS,subId,nextData};
	}
	
	
	/**
	 * 领取每日充值奖励
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] receiveLoopDayChongJianLi(Long userRoleId,int subId,int version,int subConfigId){
		ShouChongGroupConfig group = RfbShouChongConfigExportService.getInstance().loadById(subId);
		if(group == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		
		//版本一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object newSubHandleData = getLoopDayChongZhiRechargeInfo(userRoleId, subId);
			Object[] data = new Object[]{subId,configSong.getClientVersion(),newSubHandleData};
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
			return null;
		}
		
		RefbRoleShouchong refbRole = getRefbRoleShouchong(userRoleId, subId);
		//没有充值记录
		if(refbRole == null){
			return AppErrorCode.NO_RECHARGE_ERROR;
		}
		
		int receiveState = refbRole.getReceiveState();
		ShouChongActivityConfig activityConfig = group.getLoopDayChongZhiConfig(subConfigId);
		if(activityConfig == null){
			//10102 特殊错误推送，礼包已经全部领完 (容错，推送这个指令客户端会关闭，领取按键)
			BusMsgSender.send2One(userRoleId, ClientCmdType.TESHU_SHOUCHONG,subId);
			return null;
		}
		
		//状态判定，是否已经领取
		if(!BitOperationUtil.calState(receiveState, subConfigId)){
			return AppErrorCode.GET_ALREADY;
		}
		
		//本档首充元宝不够
		if(activityConfig.getNeedYb() > refbRole.getLjYbVal()){
			return AppErrorCode.SC_YB_NO_ENOU;
		}
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		byte job = role.getConfigId().byteValue();
		
		Map<String, GoodsConfigureVo> rewardItems = activityConfig.getJianLiMapByJob(job);
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBagVo(rewardItems, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		
		//改变状态并保存
		refbRole.setReceiveState(BitOperationUtil.chanageState(receiveState,subConfigId));
		refbRoleShouchongDao.cacheUpdate(refbRole, userRoleId);
		
		//发放奖励
		int bz = LogPrintHandle.GBZ_RFB_SHOUCHONG1 + receiveState;
		roleBagExportService.putGoodsVoAndNumberAttr(rewardItems, userRoleId, GoodsSource.GOODS_GET_SC, LogPrintHandle.GET_RFB_SHOUCHONG, bz, true);
		
		//每日充值类领取全服公告
		sendAllServerBc(userRoleId,activityConfig, configSong);
		
		//检查通知客服端关闭掉Icon提示
		checkIconFlag(userRoleId, subId);

		
	  //打印活动参与日志
	  GamePublishEvent.publishEvent(
	          new RfbActivityPartInLogEvent(
	                  LogPrintHandle.REFABU_LOOPDAYCHONGZHI,
	                  configSong.getActivityId(), 
	                  configSong.getSubName(), 
	                  configSong.getSubActivityType(), 
	                  configSong.getStartTimeByMillSecond(), 
	                  configSong.getEndTimeByMillSecond(), 
	                  userRoleId
	          )
	   );
		return new Object[]{AppErrorCode.SUCCESS,subId,subConfigId};
	}
	
	
	
	/**
	 * 首充类领取全服公告
	 * @param scConfig
	 * @param configSong
	 */
	private void sendAllServer(Long userRoleId,ShouChongActivityConfig scConfig,ActivityConfigSon configSong){
		//是否有公告
		if(scConfig.isHaveGongGao()){
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			String roleName = "-";
			if(role != null){
				roleName = role.getName();
			}
			
			BusMsgSender.send2All(ClientCmdType.GONGGAO_SHOUCHONG, new Object[]{scConfig.getGongGao(),configSong.getSkey(),roleName});
		}
	}
	
	/**
	 * 首充类领取全服公告
	 * @param scConfig
	 * @param configSong
	 */
	private void sendAllServerBc(Long userRoleId,ShouChongActivityConfig scConfig,ActivityConfigSon configSong){
		//是否有公告
		if(scConfig.isHaveGongGao()){
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			String roleName = "-";
			if(role != null){
				roleName = role.getName();
			}
			
			BusMsgSender.send2All(ClientCmdType.UPDATE_LOOPDAYCHONG_BC, new Object[]{scConfig.getGongGao(),configSong.getSkey(),roleName});
		}
	}
	
	/**
	 * 获取当前是否有首充活动
	 * @return true:有首充,false:无首充
	 */
	public boolean huoquShouchong(Long userRoleId,int subId) {
		ShouChongGroupConfig group = RfbShouChongConfigExportService.getInstance().loadById(subId);
		if(group == null){
			return false;
		}
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSong == null){
			return false;
		}
		
		//没有充值记录
		RefbRoleShouchong refbRole = getRefbRoleShouchong(userRoleId, subId);
		if(refbRole != null){
			refbRole = updateJianCe(subId, refbRole);
			//已全部领取完(配置里的是档位的长度，数据库对象里的是默认从0开始，领取一档自动加上1)
			if(group.getDangSize() - refbRole.getReceiveState() <= 0){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 充值成功后，增加活动的累积元宝金额
	 * @param userRoleId
	 * @param addVal
	 */
	public void changeRecharge(Long userRoleId, Long addVal){
		if(addVal < 0){
			return;
		}
		Map<Integer, ShouChongGroupConfig> groups = RfbShouChongConfigExportService.getInstance().loadAll();
		if(groups.size() == 0){
			return;
		}
		
		//循环充值礼包配置数据
		for(Map.Entry<Integer, ShouChongGroupConfig> entry : groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			int subId = entry.getKey(); 
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			
			RefbRoleShouchong refbRole = getRefbRoleShouchong(userRoleId, subId);
			if(refbRole != null){
				int ljYbVal = refbRole.getLjYbVal();
				//累计值的有限验证
				if((Integer.MAX_VALUE - ljYbVal) > addVal){
					ljYbVal = ljYbVal + addVal.intValue();
				}else{
					ljYbVal = Integer.MAX_VALUE;
				}
				refbRole.setLjYbVal(ljYbVal);
				refbRoleShouchongDao.cacheUpdate(refbRole, userRoleId);
			}else{
				refbRole = createRefbRoleShouchong(userRoleId, addVal, entry.getKey());
				refbRoleShouchongDao.cacheInsert(refbRole, userRoleId);
			}
			
			//主动通知每日充值
			if(configSong.getSubActivityType() == ReFaBuUtil.LOOP_DAY_CHONGZHI_TYPE){
				BusMsgSender.send2One(userRoleId, ClientCmdType.UPDATE_LOOPDAYCHONG, new Object[]{subId,refbRole.getLjYbVal(),new int[]{BitOperationUtil.calState(refbRole.getReceiveState(),1)?0:1,BitOperationUtil.calState(refbRole.getReceiveState(), 2)?0:1,BitOperationUtil.calState(refbRole.getReceiveState(), 3)?0:1}});
			}

			//检查通知客服端关闭掉Icon提示
			checkIconFlag(userRoleId, subId);
		}
	}
	
	/**
	 * 创建首充对象
	 * @param userRoleId
	 * @param addVal
	 * @param subId
	 * @return
	 */
	private RefbRoleShouchong createRefbRoleShouchong(Long userRoleId,Long addVal,int subId){
		RefbRoleShouchong config = new RefbRoleShouchong();
		config.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		config.setSubActivityId(subId);
		config.setUserRoleId(userRoleId);
		if(addVal > Integer.MAX_VALUE){
			addVal = Integer.MAX_VALUE * 1L;
		}
		config.setLjYbVal(addVal.intValue());
		config.setReceiveState(0);
		Long expireTime = 0L;//TODO:过期数据，暂时不处理
		config.setExpireTime(expireTime);
		config.setUpdateTime(GameSystemTime.getSystemMillTime());
		
		return config;
	}
	/**
	 * 根据主角ID和子活动ID获取首充记录
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	private RefbRoleShouchong getRefbRoleShouchong(Long userRoleId,int subId){
		List<RefbRoleShouchong> shouchongs = refbRoleShouchongDao.cacheLoadAll(userRoleId, new RefbShouCongFilter(subId));
		if(shouchongs != null && shouchongs.size() > 0){
			RefbRoleShouchong shouchong = shouchongs.get(0);
			
			//配置处理
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(shouchong.getSubActivityId());
			if(configSong != null && 
					(configSong.getTimeType() == ActivityTimeType.TIME_4_KAI_FU_LOOP ||
					configSong.getTimeType() == ActivityTimeType.TIME_5_HE_FU_LOOP)&& 
					!DatetimeUtil.dayIsToday(shouchong.getUpdateTime())){
				//重置状态数据
				shouchong.setLjYbVal(0);
				shouchong.setReceiveState(0);
				shouchong.setUpdateTime(GameSystemTime.getSystemMillTime());
				refbRoleShouchongDao.cacheUpdate(shouchong, userRoleId);
			}
			return shouchong;
		}else{
			return null;
		}
	}
	
	
	/**
	 * 获取每日充值数据
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object getLoopDayChongZhiRechargeInfo(Long userRoleId,int subId){
		ShouChongGroupConfig group = RfbShouChongConfigExportService.getInstance().loadById(subId);
		if(group == null){
			return null;
		}
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if(configSong == null){
			return null;
		}
		
		int rechargeYb = 0;
		int rechargeState = 0;
		RefbRoleShouchong shoucong = getRefbRoleShouchong(userRoleId, subId);
		//判断循环
		if(shoucong != null){
			shoucong = updateJianCe(subId, shoucong);
		}
		shoucong = updateRefbRoleShouchong(userRoleId,shoucong,subId);
		if(shoucong != null){
			rechargeYb = shoucong.getLjYbVal();
			rechargeState = shoucong.getReceiveState();
		}
		
		Map<Integer, ShouChongActivityConfig> resultMaps = group.getDayRechargeMap();
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		byte job = role.getConfigId().byteValue();
		
		Object[] data = getClientLoopDayChongZhiData(job, resultMaps, rechargeState);
		return new Object[]{group.getBgContent(),rechargeYb,data,group.getDesc()};
	}
}