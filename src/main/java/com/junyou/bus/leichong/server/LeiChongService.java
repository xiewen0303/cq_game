/**
 * 
 */
package com.junyou.bus.leichong.server;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.leichong.configue.export.LeiChong53Config;
import com.junyou.bus.leichong.configue.export.LeiChong53ConfigExportService;
import com.junyou.bus.leichong.configue.export.LeiChong53ConfigGroup;
import com.junyou.bus.leichong.configue.export.LeiChongConfig;
import com.junyou.bus.leichong.configue.export.LeiChongConfigExportService;
import com.junyou.bus.leichong.configue.export.LeiChongConfigGroup;
import com.junyou.bus.leichong.dao.LeichongDao;
import com.junyou.bus.leichong.entity.Leichong;
import com.junyou.bus.leichong.filter.LeiChongFilter;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.entity.RoleYuanbaoRecord;
import com.junyou.bus.rfbactivity.service.AbstractActivityService;
import com.junyou.bus.rfbactivity.service.RoleYuanbaoRecordService;
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.configure.vo.GoodsConfigureVo;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.RfbActivityPartInLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;


/**
 * 累计充值
 */
@Service
public class LeiChongService extends AbstractActivityService {

	@Override
	public boolean getChildFlag(long userRoleId,int subId) {
		
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			ChuanQiLog.error("");
			return false;
		}
		
		if(configSong.getSubActivityType() == ReFaBuUtil.LEI_CHONG_TYPE){
			if(checkA1(userRoleId, subId) || checkA2(userRoleId, subId)){
				return true;
			}
		}else if(configSong.getSubActivityType() == ReFaBuUtil.CZFZ_TYPE ){
			return checkLb53(userRoleId, subId);
		}
		return false;
	}
	
	
	private boolean checkLb53(long userRoleId, int subId){
		
		LeiChong53ConfigGroup config = LeiChong53ConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			ChuanQiLog.error("LeiChong53ConfigGroup is not exist!subId="+subId);
			return false;
		}
		//处理数据
		Leichong leichong = getLeiChong(userRoleId, subId);		
		//判断活动循环数据
		updateJianCe(subId, leichong);
		//活动迟上线，记录今天已经充值的元宝数
		yuanbaoJianCe(leichong);
		
		Map<Integer, LeiChong53Config> configMap = config.getConfigMap();
		
		List<Object[]> fanhuiStateVOList = new ArrayList<>();
		
		Map<Integer, Integer>  ljStatus = leichong.getLingquFl53();
		int rechargeVal = leichong.getRechargeVal();
		
		for(Entry<Integer, LeiChong53Config> entry : configMap.entrySet()){
			LeiChong53Config leiChong53Config = entry.getValue();
			int id = leiChong53Config.getId();
			if(getChargeType(id) == 2){
				int statusF = 0 ;
				if(ObjectUtil.isEmpty(ljStatus)){
					if( rechargeVal >= leiChong53Config.getReturncharge()){
						return true;
					}
				}else{
					statusF = CovertObjectUtil.object2int(ljStatus.get(leiChong53Config.getId())) == 1 ? 2: rechargeVal >= leiChong53Config.getReturncharge()?1:0;
					if(statusF == 1){
						return true;
					}
				}
				fanhuiStateVOList.add(new Object[]{leiChong53Config.getId(),statusF});
			}
			if(getChargeType(id) == 1){
				if(leiChong53Config.getItemreward() == null){
					continue ;
				}
				
				if(ObjectUtil.isEmpty(ljStatus)){
					if(rechargeVal >= leiChong53Config.getItemcharge()){
						return true;
					}
				}else{
					if(CovertObjectUtil.object2int(ljStatus.get(leiChong53Config.getId())) == 0){
						return true;
					}
				}
			}
		}
		return false;
	}


	private boolean checkA1(long userRoleId,int subId){
		//判断配置
		LeiChongConfigGroup configs = LeiChongConfigExportService.getInstance().loadByMap(subId);
		if(configs == null){
			return false;
		}
		Leichong leichong = getLeiChong(userRoleId, subId);
		//玩家已领取次数
		if(leichong == null){
			return false;
		}
		
		for (LeiChongConfig config : configs.getConfigMap().values()) {
			int configId = config.getId();
			
			int yiCount = getLingQuCount(leichong.getLingquStatus(), configId);
			//判断是否次数已经领完
			if(yiCount >= config.getCount()){
				continue;
			}
			//领取奖励需要充值的元宝
			int xfYb = (yiCount+1) * config.getXfValue();
			//是否达到充值领取条件
			if(leichong.getRechargeVal() < xfYb){
				return false;
			}
			return true;
		}
		return false;
	}
	
	private boolean checkA2(long userRoleId,int subId){
		//判断配置
		LeiChongConfigGroup configs = LeiChongConfigExportService.getInstance().loadByMap(subId);
		if(configs == null){
			return false;
		}
		Leichong leichong = getLeiChong(userRoleId, subId);
		if(leichong == null){
			return false;
		}
		//玩家已领取次数
		for (Integer day : configs.getDayMap().keySet()) {
			//是否充值
			if(!isStatus(leichong.getRechargeDay(), day) && isStatus(leichong.getDayLingqu(), day)){
				return true;
			}
//			//是否领取过奖励
//			if(isStatus(leichong.getDayLingqu(), day)){
//				return true;
//			}
		}
		return false;
	}
	
	
	@Autowired
	private LeichongDao leichongDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleYuanbaoRecordService roleYuanbaoRecordService;
	@Autowired
	private EmailExportService emailExportService;
	
	public List<Leichong> initLeichong(Long userRoleId){
		return leichongDao.initLeichong(userRoleId);
	}
	
	
	public Leichong getLeiChong(Long userRoleId,int subId){
		List<Leichong> list = leichongDao.cacheLoadAll(userRoleId, new LeiChongFilter(subId));
		if(list == null || list.size() <= 0){
			Leichong leichong = new Leichong();
			
			leichong.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			leichong.setUserRoleId(userRoleId);
			leichong.setRechargeVal(0);
			leichong.setSubId(subId);
			leichong.setLingquStatus("");
			leichong.setDayRechargeVal(0);
			leichong.setBuqianCount(0);
			leichong.setRechargeDay("");
			leichong.setCreateTime(new Timestamp(System.currentTimeMillis()));
			leichong.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			leichongDao.cacheInsert(leichong, userRoleId);
			
			return leichong;
		}
		
		Leichong result = list.get(0); 
		updateJianCe(subId, result);
		
		return result;
	}
	
	public Object[] lingqu(Long userRoleId,Integer version,int subId,int configId){
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
		LeiChongConfig config = LeiChongConfigExportService.getInstance().loadByKeyId(subId,configId);
		if(config== null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Leichong leichong = getLeiChong(userRoleId, subId);
		//玩家已领取次数
		int yiCount = getLingQuCount(leichong.getLingquStatus(), config.getId());
		//判断是否次数已经领完
		if(yiCount >= config.getCount()){
			return AppErrorCode.NO_LEICHONG_COUNT;
		}
		//领取奖励需要充值的元宝
		int xfYb = (yiCount+1) * config.getXfValue();
		//是否达到充值领取条件
		if(leichong.getRechargeVal() < xfYb){
			return AppErrorCode.NO_LEICHONG_TIAOJIAN;
		}
		Map<String,GoodsConfigureVo> jiangli = config.getJianLiMapA();
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBagVo(jiangli, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		
		//更新玩家领取状态
		leichong.setLingquStatus(getLingQuStatus(leichong.getLingquStatus(), configId, yiCount));
		leichong.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		leichongDao.cacheUpdate(leichong, userRoleId);
		
		//发放奖励
		roleBagExportService.putGoodsVoAndNumberAttr(jiangli, userRoleId, GoodsSource.GOODS_GET_LC, LogPrintHandle.GET_RFB_LEICHONG, LogPrintHandle.GBZ_RFB_LEICHONG, true);
		
		super.checkIconFlag(userRoleId, subId);
		
		//打印活动参与日志
		GamePublishEvent.publishEvent(
		        new RfbActivityPartInLogEvent(
		                LogPrintHandle.REFABU_LEICHONG,
		                configSong.getActivityId(), 
		                configSong.getSubName(), 
		                configSong.getSubActivityType(), 
		                configSong.getStartTimeByMillSecond(), 
		                configSong.getEndTimeByMillSecond(), 
		                userRoleId
		        )
	  );
		
		return new Object[]{1,subId,configId};
	}
	
	private String getLingQuStatus(String lingquStatus,Integer configId,int count){
		int yiCount = count + 1;
		if(lingquStatus == null || "".equals(lingquStatus)){
			return configId+","+yiCount;
		}else{
			if(lingquStatus.contains(configId+","+count )){
				lingquStatus = lingquStatus.replace(configId+","+count, configId+","+yiCount);
			}else{
				lingquStatus = lingquStatus+"|"+configId+","+yiCount;
			}
			return lingquStatus;
		}
		
	}
	
	/**
	 * 获取领取次数
	 * @return
	 */
	private int getLingQuCount(String lingquStatus,Integer configId){
		String[] lingqu = lingquStatus.split("\\|");
		if(lingqu == null || lingqu.length <= 0){
			return 0;
		}
		for (int i = 0; i < lingqu.length; i++) {
			if(lingqu[i] == null || "".equals(lingqu[i])){
				continue;
			}
			String[] status = lingqu[i].split(",");
			if(Integer.parseInt(status[0]) == configId.intValue()){
				return Integer.parseInt(status[1]);
			}
		}
		return 0;
	}
	
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){

		LeiChongConfigGroup config = LeiChongConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		//处理数据
		Leichong leichong = getLeiChong(userRoleId, subId);		
		//判断活动循环数据
		updateJianCe(subId, leichong);
		//活动迟上线，记录今天已经充值的元宝数
		yuanbaoJianCe(leichong);
		
		Map<Integer, LeiChongConfig> configMap = config.getConfigMap();
		List<Object[]> voList = new ArrayList<>();
		List<Object[]> czList = new ArrayList<>();
		for(Map.Entry<Integer, LeiChongConfig> entry : configMap.entrySet()){
			voList.add(entry.getValue().getVo());
			
			czList.add(new Object[]{entry.getValue().getId(),getLingQuCount(leichong.getLingquStatus(), entry.getValue().getId())});
		}
		
		return new Object[]{
				config.getPic(),
				config.getDes(),
				voList.toArray(),
				czList.toArray(),
				leichong.getRechargeVal(),
				config.getBcGold(),
				leichong.getDayRechargeVal(),
				config.getDayClient(),
				leichong.getBuqianCount(),
				getDayStatus(leichong),
				config.getSeverClient(),
				config.getBcGold()
		};
		
	}
	
	private Object[] getDayStatus(Leichong leichong){
		if(leichong == null || leichong.getRechargeDay().isEmpty()){
			return null;
		}
		List<Object[]> list = new ArrayList<>();
		String[] str = leichong.getRechargeDay().split(",");
		
		for (int i = 0; i < str.length; i++) {
			if(!isStatus(leichong.getDayLingqu(), Integer.parseInt(str[i]))){
				list.add(new Object[]{str[i],2});
			}else{
				list.add(new Object[]{str[i],1});
			}
		}
		return list.toArray();
	}
	
	private void yuanbaoJianCe(Leichong leichong){
		RoleYuanbaoRecord record = roleYuanbaoRecordService.getRoleYuanBaoRecord(leichong.getUserRoleId());
		if(record.getCzValue() > leichong.getRechargeVal()){
			leichong.setRechargeVal(record.getCzValue());
			leichong.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			leichongDao.cacheUpdate(leichong, leichong.getUserRoleId());
		}
		if(record.getCzValue() > leichong.getDayRechargeVal()){
			leichong.setDayRechargeVal(record.getCzValue());
			leichong.setDayRechargeTime(System.currentTimeMillis());
			
			leichongDao.cacheUpdate(leichong, leichong.getUserRoleId());
		}
	}
	
	private void updateJianCe(int subId,Leichong leichong){
		//判断活动是否结束
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		/*if(configSong.getTimeType() != ActivityTimeType.TIME_4_KAI_FU_LOOP && configSong.getTimeType() != ActivityTimeType.TIME_5_HE_FU_LOOP){
			return;
		}*/
		long startTime = configSong.getStartTimeByMillSecond();//活动开始时间
		long upTime = leichong.getUpdateTime().getTime();
		long dTime = GameSystemTime.getSystemMillTime();
		if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
			leichong.setRechargeVal(0);
			leichong.setLingquStatus("");
			leichong.setDayRechargeVal(0);
			leichong.setRechargeDay("");
			leichong.setDayLingqu("");
			leichong.setBuqianCount(0);
			leichong.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			leichongDao.cacheUpdate(leichong, leichong.getUserRoleId());
		}
	}
	public Object[] getRefbLingQuStatus(Long userRoleId, Integer subId){
		LeiChongConfigGroup config = LeiChongConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		//处理数据
		Leichong leichong = getLeiChong(userRoleId, subId);		
		Map<Integer, LeiChongConfig> configMap = config.getConfigMap();
		List<Object[]> czList = new ArrayList<>();
		for(Map.Entry<Integer, LeiChongConfig> entry : configMap.entrySet()){
			
			czList.add(new Object[]{entry.getValue().getId(),getLingQuCount(leichong.getLingquStatus(), entry.getValue().getId())});
		}
			
		return new Object[]{subId,czList.toArray()};
				
	}
	
	
	public void rechargeYb(Long userRoleId,Long addVal){
		if(addVal < 0){
			return;
		}
		Map<Integer, LeiChongConfigGroup> groups = LeiChongConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		//循环充值礼包配置数据
		for(Map.Entry<Integer, LeiChongConfigGroup> entry: groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			
			Leichong leichong = getLeiChong(userRoleId, entry.getKey());	
			//检测
			updateJianCe(entry.getKey(), leichong);
			
			leichong.setRechargeVal((int) (leichong.getRechargeVal()+addVal));
			leichong.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			
			if(!DatetimeUtil.dayIsToday(leichong.getDayRechargeTime())){
				leichong.setDayRechargeVal(addVal.intValue());
				leichong.setDayRechargeTime(GameSystemTime.getSystemMillTime());
				
				if(leichong.getRechargeDay() == null ||leichong.getRechargeDay().equals("")){
					leichong.setRechargeDay((DatetimeUtil.twoDaysDiffence(configSong.getStartTimeByMillSecond()) + 1)+"");
				}else{
					leichong.setRechargeDay(leichong.getRechargeDay()+","+(DatetimeUtil.twoDaysDiffence(configSong.getStartTimeByMillSecond()) + 1));
				}
			}else{//同一天
				leichong.setDayRechargeVal((int) (leichong.getDayRechargeVal()+addVal));
				leichong.setDayRechargeTime(GameSystemTime.getSystemMillTime());
			}
			leichongDao.cacheUpdate(leichong, userRoleId);
			
			BusMsgSender.send2One(userRoleId, ClientCmdType.TUI_SONG_RECHARGE_YB, new Object[]{entry.getKey(),leichong.getRechargeVal(),leichong.getDayRechargeVal()});

			//检查通知客服端关闭掉Icon提示
			checkIconFlag(userRoleId, configSong.getId());
		}
	}
	
	
	public void rechargeYb53(Long userRoleId,Long addVal){
		if(addVal < 0){
			return;
		}
		Map<Integer, LeiChong53ConfigGroup> groups = LeiChong53ConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		//循环充值礼包配置数据
		for(Map.Entry<Integer, LeiChong53ConfigGroup> entry: groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			
			Leichong leichong = getLeiChong(userRoleId, entry.getKey());	
			//检测
			updateJianCe(entry.getKey(), leichong);
			
			leichong.setRechargeVal((int) (leichong.getRechargeVal()+addVal));
			leichong.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			
//			if(!DatetimeUtil.dayIsToday(leichong.getDayRechargeTime())){
//				leichong.setDayRechargeVal(addVal.intValue());
//				leichong.setDayRechargeTime(GameSystemTime.getSystemMillTime());
//				
//				if(leichong.getRechargeDay() == null ||leichong.getRechargeDay().equals("")){
//					leichong.setRechargeDay((DatetimeUtil.twoDaysDiffence(configSong.getStartTimeByMillSecond()) + 1)+"");
//				}else{
//					leichong.setRechargeDay(leichong.getRechargeDay()+","+(DatetimeUtil.twoDaysDiffence(configSong.getStartTimeByMillSecond()) + 1));
//				}
//			}else{//同一天
//				leichong.setDayRechargeVal((int) (leichong.getDayRechargeVal()+addVal));
//				leichong.setDayRechargeTime(GameSystemTime.getSystemMillTime());
//			}
			leichongDao.cacheUpdate(leichong, userRoleId);
			
			BusMsgSender.send2One(userRoleId, ClientCmdType.TUI_SONG_RECHARGE53_YB, new Object[]{entry.getKey(),leichong.getRechargeVal()});

			//检查通知客服端关闭掉Icon提示
			checkIconFlag(userRoleId, configSong.getId());
		}
	}
	
	
	public Object[] lingquDay(Long userRoleId,Integer version,int subId,int day){
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
		LeiChongConfigGroup config = LeiChongConfigExportService.getInstance().loadByMap(subId);
		if(config== null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<String, Integer> item = config.getDayMap().get(day);
		if(item == null || item.size() <= 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		Leichong leichong = getLeiChong(userRoleId, subId);
		//是否充值
		if(isStatus(leichong.getRechargeDay(), day)){
			return AppErrorCode.RECHARGE_NORE_NOLINGQU;
		}
		//是否领取过奖励
		if(!isStatus(leichong.getDayLingqu(), day)){
			return AppErrorCode.LEIHAO_CAN_NOT_PICK;
		}
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(item, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		
		//更新玩家领取状态
		if(leichong.getDayLingqu() == null ||leichong.getDayLingqu().equals("")){
			leichong.setDayLingqu(day+"");
		}else{
			leichong.setDayLingqu(leichong.getDayLingqu()+","+day);
		}
		leichong.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		leichongDao.cacheUpdate(leichong, userRoleId);
		
		//发放奖励
		roleBagExportService.putGoodsAndNumberAttr(item, userRoleId, GoodsSource.GOODS_GET_LC, LogPrintHandle.GET_RFB_LEICHONG, LogPrintHandle.GBZ_RFB_LEICHONG, true);
		
		//判断连冲奖励是否全部领取完（领完了邮件发7天大奖）
		if(isSevenLingqu(config.getDayMap(), leichong.getDayLingqu())){
			emailExportService.sendEmailToOne(userRoleId,EmailUtil.getCodeEmail(AppErrorCode.LEICHONG_EMAIL_TITLE),EmailUtil.getCodeEmail(AppErrorCode.LEICHONG_EMAIL_CONTENT),GameConstants.EMAIL_TYPE_SINGLE, config.getSevenItem());
		}
		
		super.checkIconFlag(userRoleId, subId);
		//打印活动参与日志
		GamePublishEvent.publishEvent(
		        new RfbActivityPartInLogEvent(
		                LogPrintHandle.REFABU_LEICHONG,
		                configSong.getActivityId(), 
		                configSong.getSubName(), 
		                configSong.getSubActivityType(), 
		                configSong.getStartTimeByMillSecond(), 
		                configSong.getEndTimeByMillSecond(), 
		                userRoleId
		        )
	  );
		
		return new Object[]{1,subId,day};
	}
	
	public Object[] lingquFl53(Long userRoleId,Integer version,int subId,Object[] targetIds){
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if(configSong == null){
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		
		//版本不一样
		if(configSong.getClientVersion() != version){
			//处理数据变化:
			Object newSubHandleData = getFanLi53Info(userRoleId, subId);
			Object[] data = new Object[]{subId,configSong.getClientVersion(),newSubHandleData};
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY,data );
			return null;
		}
		
		//判断配置
		LeiChong53ConfigGroup config = LeiChong53ConfigExportService.getInstance().loadByMap(subId);
		if(config== null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		if(ObjectUtil.isEmpty(targetIds)){
			return AppErrorCode.DATA_ERROR;
		}
		
		Leichong leichong = getLeiChong(userRoleId, subId);
		
		int totalGold = 0;
		Map<String,Integer> resultItems = new HashMap<String, Integer>();
		Map<Integer,Integer> lqFl53s =  leichong.getLingquFl53();
		
		for (Object t11 : targetIds) {
			int configId = CovertObjectUtil.object2int(t11);
			//判断配置
			LeiChong53Config c53 = LeiChong53ConfigExportService.getInstance().loadByKeyId(subId,configId);
			if(c53 == null){
				continue;
			}
			
			if(lqFl53s != null && CovertObjectUtil.object2int(lqFl53s.get(configId)) == 1){
				return AppErrorCode.VIP_GIFT_HAS_REVICED;
			}
			
			if(getChargeType(configId) == 2){
				if(leichong.getRechargeVal() < c53.getReturncharge()){
					return AppErrorCode.LIMIT_BUY_CZBZ;
				}
				
				totalGold += c53.getReturngold();
				 
			}else if(getChargeType(configId) == 1){
				if(leichong.getRechargeVal() < c53.getItemcharge()){
					return AppErrorCode.LIMIT_BUY_CZBZ;
				}
				
				ObjectUtil.mapAdd(resultItems, c53.getItemreward());
			}
		}
		 
		//检查物品是否可以进背包
		if(!ObjectUtil.isEmpty(resultItems)){
			Object[] bagCheck = roleBagExportService.checkPutInBag(resultItems, userRoleId);
			if(bagCheck != null){
				return bagCheck;
			}
			roleBagExportService.putGoodsAndNumberAttr(resultItems, userRoleId, GoodsSource.GOODS_GET_LC, LogPrintHandle.GET_RFB_LEICHONG, LogPrintHandle.GBZ_RFB_LEICHONG, true);
		}
		
		if(totalGold > 0){
			roleBagExportService.incrNumberWithNotify(GoodsCategory.GOLD, totalGold, userRoleId, LogPrintHandle.GET_RFB_LEICHONG, LogPrintHandle.GBZ_RFB_LEICHONG);
		}
		
		
		if(lqFl53s == null){
			lqFl53s = new HashMap<>();
		}
		
		for (Object successId : targetIds) {
			lqFl53s.put(CovertObjectUtil.object2int(successId), 1);
		}
		
		leichong.setLingquStatus(JSONObject.toJSONString(lqFl53s));
		
		leichong.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		leichongDao.cacheUpdate(leichong, userRoleId);
		
		super.checkIconFlag(userRoleId, subId);
		
		//打印活动参与日志
		GamePublishEvent.publishEvent(
		        new RfbActivityPartInLogEvent(
		                LogPrintHandle.REFABU_LEICHONG,
		                configSong.getActivityId(),
		                configSong.getSubName(),
		                configSong.getSubActivityType(),
		                configSong.getStartTimeByMillSecond(),
		                configSong.getEndTimeByMillSecond(),
		                userRoleId
		        )
	  );
		
		return new Object[]{1,subId};
	}
	
	/**
	 * 是否领取了全部的奖励
	 * @return
	 */
	private boolean isSevenLingqu(Map<Integer, Map<String,Integer>> dayMap,String lingqu){
		if(dayMap == null || lingqu == null ||"".equals(lingqu)){
			return false;
		}
		for (Integer day : dayMap.keySet()) {
			//是否领取过奖励
			if(isStatus(lingqu, day)){
				return false;
			}
		}
		return true;
	}
	
	public Object[] buqian(Long userRoleId,Integer version,int subId,int day){
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
		LeiChongConfigGroup config = LeiChongConfigExportService.getInstance().loadByMap(subId);
		if(config== null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Leichong leichong = getLeiChong(userRoleId, subId);
		//是否充值
		if(!isStatus(leichong.getRechargeDay(), day)){
			return AppErrorCode.RECHARGE_NO_BUQIAN;
		}
		//补签次数是否足够
		int dayBc = leichong.getDayRechargeVal() / config.getBcGold();
		if(dayBc <= leichong.getBuqianCount()){
			return AppErrorCode.RECHARGE_NO_BUQIAN_RE;
		}
		//更新玩家充值状态
		if(leichong.getRechargeDay() == null ||leichong.getRechargeDay().equals("")){
			leichong.setRechargeDay(day+"");
		}else{
			leichong.setRechargeDay(leichong.getRechargeDay()+","+day);
		}
		leichong.setBuqianCount(leichong.getBuqianCount() + 1);
		leichong.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		leichongDao.cacheUpdate(leichong, userRoleId);
		
		return new Object[]{1,subId,day};
	}
	
	private boolean isStatus(String status,int day){
		if(status == null || status.equals("")){
			return true;
		}
		String[] str = status.split(",");
		for (int i = 0; i < str.length; i++) {
			if(day == Integer.parseInt(str[i])){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * 充值返钻
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getFanLi53Info(Long userRoleId, Integer subId){
		LeiChong53ConfigGroup config = LeiChong53ConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		//处理数据
		Leichong leichong = getLeiChong(userRoleId, subId);		
		//判断活动循环数据
		updateJianCe(subId, leichong);
		//活动迟上线，记录今天已经充值的元宝数
		yuanbaoJianCe(leichong);
		
		Map<Integer, LeiChong53Config> configMap = config.getConfigMap();
		List<Object[]> goldList = new ArrayList<>();
		List<Object[]> itemList = new ArrayList<>();
		List<Object[]> fanhuiStateVOList = new ArrayList<>();
		
		List<Object[]> stateVOList = new ArrayList<>();
		
		Map<Integer, Integer>  ljStatus = leichong.getLingquFl53();
		int rechargeVal = leichong.getRechargeVal();
		
		for(Entry<Integer, LeiChong53Config> entry : configMap.entrySet()){
			LeiChong53Config leiChong53Config = entry.getValue();
			int id = leiChong53Config.getId();
			if(getChargeType(id) == 2){
				goldList.add(new Object[]{leiChong53Config.getId(),leiChong53Config.getReturncharge(),leiChong53Config.getReturngold(),leiChong53Config.getReturnpercent()});
				int statusF = 0 ;
				if(ObjectUtil.isEmpty(ljStatus)){
					statusF = rechargeVal >= leiChong53Config.getReturncharge()?1:0;
				}else{
					statusF = CovertObjectUtil.object2int(ljStatus.get(leiChong53Config.getId())) == 1 ? 2: rechargeVal >= leiChong53Config.getReturncharge()?1:0;
				}
				fanhuiStateVOList.add(new Object[]{leiChong53Config.getId(),statusF});
			}
			
			if(getChargeType(id) == 1){
				if(leiChong53Config.getItemreward() == null){
					continue ;
				}
				List<Object[]> jiangliVOs = new ArrayList<>();
				for (Entry<String,Integer> e : leiChong53Config.getItemreward().entrySet()) {
					jiangliVOs.add(new Object[]{e.getKey(),e.getValue()});
				}
				itemList.add(new Object[]{leiChong53Config.getId(),leiChong53Config.getItemcharge(),jiangliVOs.toArray()});	
				
				int statusF = 0 ;
				if(!ObjectUtil.isEmpty(ljStatus)){
//					statusF = rechargeVal >= leiChong53Config.getItemcharge()?1:0;
//				}else{
					statusF = CovertObjectUtil.object2int(ljStatus.get(leiChong53Config.getId()));// == 1 ? 1:0;
				}
				stateVOList.add(new Object[]{leiChong53Config.getId(),statusF});
			}
		}
		
		return new Object[]{
				config.getPic(),
				config.getDes(), 
				
				goldList.toArray(),
				fanhuiStateVOList.toArray(),
				
				itemList.toArray(),
				stateVOList.toArray(),
				leichong.getRechargeVal(),
				config.getMax()
				
		};
	}
	
	/**
	 * 
	 * @param id  1:道具  2:钻石
	 * @return
	 */
	private int getChargeType(int id){
		if(id > 0 && id < 20){
			return 1;
		}else if(id >= 20 && id < 30){
			return 2;
		}
		return 0;
	}


	public Object[] getRefb53Status(Long userRoleId, int subId) {
		LeiChong53ConfigGroup config = LeiChong53ConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		//处理数据
		Leichong leichong = getLeiChong(userRoleId, subId);
		int rechargeVal = 0;
		if(leichong != null){
			rechargeVal = 	leichong.getRechargeVal();
		}
		return new Object[]{subId,rechargeVal};
	}
}