package com.junyou.bus.leihao.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.leihao.configure.export.LeiHaoBGoldConfig;
import com.junyou.bus.leihao.configure.export.LeiHaoConfig;
import com.junyou.bus.leihao.configure.export.LeiHaoConfigExportService;
import com.junyou.bus.leihao.configure.export.LeiHaoGroupConfig;
import com.junyou.bus.leihao.dao.RefabuLeihaoDao;
import com.junyou.bus.leihao.entity.RefabuLeihao;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.entity.RoleYuanbaoRecord;
import com.junyou.bus.rfbactivity.service.AbstractActivityService;
import com.junyou.bus.rfbactivity.service.RoleYuanbaoRecordService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.RfbActivityPartInLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class LeiHaoService  extends AbstractActivityService {

	@Override
	public boolean getChildFlag(long userRoleId,int subId) {
		
		if(pickCheck(userRoleId,subId) || dayPickCheck(userRoleId,subId)){
			return true;
		}
		return false;
	}
	

	private boolean dayPickCheck(long userRoleId, int subId) {
		LeiHaoGroupConfig sConfigs = LeiHaoConfigExportService.getInstance().loadByMap(subId);
		if (sConfigs == null) {
			return false;
		}
		RefabuLeihao leihao = getRefabuLeihao(userRoleId, subId);
		if (leihao == null) {
			return false;
		}
		
		for (LeiHaoBGoldConfig sConfig : sConfigs.getbGoldConfigMap().values()) {
			if(sConfig.getId() < 0){
				continue;
			}
			if(sConfig.getXfvalue() <=0){
				continue;
			}
			if (leihao.getYbDayCount() < sConfig.getXfvalue()) {
				continue;
			}
			//是否领取过奖励
			if(!isLingquDay(leihao, sConfig.getId())){
				continue;
			}
			return true;
		}
		return false;
	}

	private boolean pickCheck(long userRoleId, int subId) {
		LeiHaoGroupConfig sConfigs = LeiHaoConfigExportService.getInstance().loadByMap(subId);
		if (sConfigs == null) {
			return false;
		}
		RefabuLeihao leihao = getRefabuLeihao(userRoleId, subId);
		if (leihao == null) {
			return false;
		}
		
		for (LeiHaoConfig sConfig  : sConfigs.getConfigMap().values()) {
			if(sConfig.getId() < 0){
				continue;
			}
			if(sConfig.getXfvalue() <=0){
				continue;
			}
			
			if (leihao.getYbCount() < sConfig.getXfvalue()) {
				continue;
			}
			
			String pickStr = leihao.getPickStr();
			if(CovertObjectUtil.isEmpty(pickStr)){
				return true;
			}
			
			Map<String,String> data =  CovertObjectUtil.object2Map(pickStr,";",":");
			int times = CovertObjectUtil.object2int(data.get(sConfig.getId()+""));
			
			if(times < leihao.getYbCount()/sConfig.getXfvalue() &&  times < sConfig.getCount()){
				return true;
			}
		}
		return false;
	}

	@Autowired
	private RefabuLeihaoDao refabuLeihaoDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleYuanbaoRecordService roleYuanbaoRecordService;

	public List<RefabuLeihao> initRefabuLeihao(Long userRoleId) {
		return refabuLeihaoDao.initRefabuLeihao(userRoleId);
	}

	public RefabuLeihao createRefabuLeiHao(int subId, Long userRoleId, int yb) {
		RefabuLeihao po = new RefabuLeihao();
		po.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		po.setSubId(subId);
		po.setUserRoleId(userRoleId);
		po.setYbCount(yb);
		po.setPickStr("");
		po.setUpdateTime(GameSystemTime.getSystemMillTime());
		po.setYbDayCount(yb);
		po.setYbDayId("");
		refabuLeihaoDao.cacheInsert(po, userRoleId);
		return po;
	}

	private RefabuLeihao getRefabuLeihao(Long userRoleId, final Integer subId) {
		List<RefabuLeihao> list = refabuLeihaoDao.cacheLoadAll(userRoleId,
				new IQueryFilter<RefabuLeihao>() {
					private boolean stop = false;

					@Override
					public boolean check(RefabuLeihao info) {
						if (subId.equals(info.getSubId())) {
							stop = true;
						}
						return stop;
					}

					@Override
					public boolean stopped() {
						return stop;
					}
				});
		if (list != null && list.size() > 0) {
			RefabuLeihao leihao =  list.get(0);
			if(!DatetimeUtil.dayIsToday(leihao.getUpdateTime())){
				leihao.setUpdateTime(GameSystemTime.getSystemMillTime());
				leihao.setYbDayCount(0);
				leihao.setYbDayId("");
				refabuLeihaoDao.cacheUpdate(leihao, userRoleId);
			}
			return list.get(0);
		}
		return null;
	}

	public void xiaofeiYb(Long userRoleId, Long yb) {
		if (yb < 0) {
			return;
		}
		Map<Integer, LeiHaoGroupConfig> groups = LeiHaoConfigExportService.getInstance().getAllConfig();
		if (groups.size() == 0) {
			return;
		}
		for (Map.Entry<Integer, LeiHaoGroupConfig> entry : groups.entrySet()) {
			// 是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager
					.getInstance().loadByZiId(entry.getKey());
			if (configSong == null || !configSong.isRunActivity()) {
				continue;
			}
			LeiHaoGroupConfig config = LeiHaoConfigExportService.getInstance()
					.loadByMap(entry.getKey());
			if (config == null) {
				continue;
			}
			RefabuLeihao leihao = getRefabuLeihao(userRoleId, entry.getKey());
			if(leihao!=null){
				xunhuanhuodongJiance(configSong, leihao);
			}
			if (leihao != null) {
				leihao.setYbCount((int) (leihao.getYbCount().intValue() + yb));
				leihao.setUpdateTime(GameSystemTime.getSystemMillTime());
				leihao.setYbDayCount((int) (leihao.getYbDayCount().intValue() + yb));
				refabuLeihaoDao.cacheUpdate(leihao, userRoleId);
			} else {
				leihao = createRefabuLeiHao(entry.getKey(), userRoleId, yb.intValue());
			}
			BusMsgSender.send2One(userRoleId, ClientCmdType.PUSH_LEI_HAO_YB,new Object[] { entry.getKey(), leihao.getYbCount() ,leihao.getYbDayCount()});

			this.checkIconFlag(userRoleId,configSong.getId());
		}
	}

	public Object[] getInfo(Long userRoleId, Integer subId) {
		LeiHaoGroupConfig config = LeiHaoConfigExportService.getInstance()
				.loadByMap(subId);
		if (config == null) {
			return null;
		}
		ActivityConfigSon activity = ActivityAnalysisManager.getInstance()
				.loadRunByZiId(subId);
		if (activity == null) {
			return null;
		}
		RefabuLeihao info = getRefabuLeihao(userRoleId, subId);
		if(info!=null){
			xunhuanhuodongJiance(activity, info);
		}
		if (info == null) {
			createRefabuLeiHao(subId, userRoleId, 0);
			info = getRefabuLeihao(userRoleId, subId);
		}
		RoleYuanbaoRecord record = roleYuanbaoRecordService.getRoleYuanBaoRecord(userRoleId);
		if(record.getXfValue() > info.getYbCount()){
			info.setYbCount(record.getXfValue());
			info.setUpdateTime(GameSystemTime.getSystemMillTime());
			info.setYbDayCount(record.getXfValue());
			refabuLeihaoDao.cacheUpdate(info, userRoleId);
		}
		Object[] ret = new Object[8];
		ret[0] = config.getBg();
		ret[1] = config.getDesc();
		ret[2] = info.getYbCount();
		ret[3] = info.getYbDayCount();
		ret[4] = config.getVo();
		ret[5] = getPickState(info);
		ret[6] = config.getDayVo();
		ret[7] = getDayLingQuId(info);
		return ret;
	}

	public Object[] getDayLingQuId(RefabuLeihao info) {
		if (info == null || info.getYbDayId().equals("")) {
			return null;
		}
		String dayId = info.getYbDayId();
		return dayId.split(",");
		
	}
	public Object[] getPickState(RefabuLeihao info) {
		if (info == null || info.getPickStr().equals("")) {
			return null;
		}
		String pickStr = info.getPickStr();
		String[] pickStrArray = pickStr.split(";");
		Object[] ret = new Object[pickStrArray.length];
		for (int i = 0; i < pickStrArray.length; i++) {
			String tmp = pickStrArray[i];
			String[] infoArray = tmp.split(":");
			int id = Integer.parseInt(infoArray[0]);
			int times = Integer.parseInt(infoArray[1]);
			ret[i] = new Object[] { id, times };
		}
		return ret;
	}
	
	public void xunhuanhuodongJiance(ActivityConfigSon activity,RefabuLeihao info){
		/*if(activity.getTimeType() != ActivityTimeType.TIME_4_KAI_FU_LOOP && activity.getTimeType() != ActivityTimeType.TIME_5_HE_FU_LOOP){
			return;
		}*/
		long startTime = activity.getStartTimeByMillSecond();//活动开始时间
		long upTime = info.getUpdateTime();
		long dTime = GameSystemTime.getSystemMillTime();
		if(startTime  > upTime && startTime < dTime){//如果活动开始时间大于业务上次更新时间，清理业务数据
			info.setYbCount(0);
			info.setPickStr("");
			info.setYbDayCount(0);
			info.setYbDayId("");
			info.setUpdateTime(GameSystemTime.getSystemMillTime());
			refabuLeihaoDao.cacheUpdate(info, info.getUserRoleId());
		}
	}

	public Object[] pick(Long userRoleId, Integer subId, Integer version,Integer configId,Integer type) {
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance()
				.loadRunByZiId(subId);
		if (configSong == null) {
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		// 版本不一样
		if (configSong.getClientVersion() != version) {
			// 处理数据变化:
			Object newSubHandleData = getInfo(userRoleId, subId);
			Object[] data = new Object[] { subId,
					configSong.getClientVersion(), newSubHandleData };
			BusMsgSender.send2One(userRoleId,
					ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
			return null;
		}
		LeiHaoConfig sConfig = LeiHaoConfigExportService.getInstance()
				.loadByKeyId(subId, configId);
		if (sConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		RefabuLeihao leihao = getRefabuLeihao(userRoleId, subId);
		if (leihao == null) {
			return AppErrorCode.LEIHAO_CAN_NOT_PICK;
		}
		Object[] checkResult = roleBagExportService.checkPutInBagVo(
				sConfig.getItemMap(), userRoleId);
		if (checkResult != null) {
			return checkResult;
		}
		if (leihao.getYbCount() < sConfig.getXfvalue()) {
			return AppErrorCode.LEIHAO_CAN_NOT_PICK;
		}
		StringBuffer newPickStr = new StringBuffer();
		String pickStr = leihao.getPickStr();
		if (pickStr != null && !pickStr.equals("")) {
			String[] pickStrArray = pickStr.split(";");
			boolean flag = false;
			for (String e : pickStrArray) {
				String[] infoArray = e.split(":");
				if (infoArray[0].equals(String.valueOf(configId))) {
					int times = Integer.parseInt(infoArray[1]);
					if (times >= (leihao.getYbCount()/sConfig.getXfvalue())) {
						return AppErrorCode.LEIHAO_CAN_NOT_PICK;
					}
					if (times >= sConfig.getCount()) {
						return AppErrorCode.LEIHAO_CAN_NOT_PICK;
					}
					newPickStr.append(infoArray[0] + ":"
							+ String.valueOf(times + 1));
					flag = true;
				} else {
					newPickStr.append(e);
				}
				newPickStr.append(";");
			}
			if(!flag){
				newPickStr.append(configId).append(":1;");
			}
			newPickStr.deleteCharAt(newPickStr.length() - 1);
		} else {
			newPickStr.append(configId).append(":1");
		}
		leihao.setPickStr(newPickStr.toString());
		refabuLeihaoDao.cacheUpdate(leihao, userRoleId);
		roleBagExportService.putGoodsVoAndNumberAttr(sConfig.getItemMap(),
				userRoleId, GoodsSource.GOODS_LEIHAO_GET,
				LogPrintHandle.GET_LEI_HAO,
				LogPrintHandle.GBZ_LEIHAO, true);
		ChuanQiLog.info(
				"userRoleId={} pick leihao reward configId={} subId={}",
				userRoleId, configId, subId);
		
		super.checkIconFlag(userRoleId, subId);
	 //打印活动参与日志
  GamePublishEvent.publishEvent(
          new RfbActivityPartInLogEvent(
                  LogPrintHandle.REFABU_XIAOFEI,
                  configSong.getActivityId(), 
                  configSong.getSubName(), 
                  configSong.getSubActivityType(), 
                  configSong.getStartTimeByMillSecond(), 
                  configSong.getEndTimeByMillSecond(), 
                  userRoleId
          )
   );
		return new Object[] { AppErrorCode.SUCCESS,subId, configId,type};
	}
	
	
	
	public Object[] dayPick(Long userRoleId, Integer subId, Integer version,Integer configId,Integer type) {
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if (configSong == null) {
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		// 版本不一样
		if (configSong.getClientVersion() != version) {
			// 处理数据变化:
			Object newSubHandleData = getInfo(userRoleId, subId);
			Object[] data = new Object[] { subId,configSong.getClientVersion(), newSubHandleData };
			BusMsgSender.send2One(userRoleId,ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
			return null;
		}
		LeiHaoBGoldConfig sConfig = LeiHaoConfigExportService.getInstance().loadByKeyDayId(subId, configId);
		if (sConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		RefabuLeihao leihao = getRefabuLeihao(userRoleId, subId);
		if (leihao == null) {
			return AppErrorCode.LEIHAO_CAN_NOT_PICK;
		}
		if (leihao.getYbDayCount() < sConfig.getXfvalue()) {
			return AppErrorCode.LEIHAO_CAN_NOT_PICK;
		}
		//是否领取过奖励
		if(!isLingquDay(leihao, sConfig.getId())){
			return AppErrorCode.LEIHAO_CAN_NOT_PICK;
		}
		if(leihao.getYbDayId() == null ||leihao.getYbDayId().equals("")){
			leihao.setYbDayId(sConfig.getId()+"");
		}else{
			leihao.setYbDayId(leihao.getYbDayId()+","+sConfig.getId());
		}
		
		refabuLeihaoDao.cacheUpdate(leihao, userRoleId);
		accountExportService.incrCurrencyWithNotify(GoodsCategory.BGOLD, sConfig.getBgold(), userRoleId, LogPrintHandle.GET_REFABU_XIAOHAO, LogPrintHandle.GBZ_REFABU_XIAOHAO);
		ChuanQiLog.info(
				"userRoleId={} pick day leihao reward configId={} subId={}",
				userRoleId, configId, subId);
		
		
		super.checkIconFlag(userRoleId, subId);
		
		//打印活动参与日志
		GamePublishEvent.publishEvent(
				new RfbActivityPartInLogEvent(
						LogPrintHandle.REFABU_XIAOFEI,
						configSong.getActivityId(), 
						configSong.getSubName(), 
						configSong.getSubActivityType(), 
						configSong.getStartTimeByMillSecond(), 
						configSong.getEndTimeByMillSecond(), 
						userRoleId
						)
				);
		return new Object[] { AppErrorCode.SUCCESS,subId, configId,type};
	}
	
	private boolean isLingquDay(RefabuLeihao leihao,int id){
		if(leihao == null){
			return false;
		}
		if(leihao.getYbDayId() == null || leihao.getYbDayId().equals("")){
			return true;
		}
		String[] str = leihao.getYbDayId().split(",");
		for (int i = 0; i < str.length; i++) {
			if(id == Integer.parseInt(str[i])){
				return false;
			}
		}
		return true;
	}
}
