/**
 * 
 */
package com.junyou.bus.xiaofei.server;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.email.utils.EmailUtil;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.entity.RoleYuanbaoRecord;
import com.junyou.bus.rfbactivity.service.RoleYuanbaoRecordService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiaofei.configure.export.XiaofeiConfig;
import com.junyou.bus.xiaofei.configure.export.XiaofeiConfigExportService;
import com.junyou.bus.xiaofei.configure.export.XiaofeiConfigGroup;
import com.junyou.bus.xiaofei.dao.RefabuXiaofeiDao;
import com.junyou.bus.xiaofei.entity.RefabuXiaofei;
import com.junyou.bus.xiaofei.filter.SortXFGoldComparator;
import com.junyou.bus.xiaofei.filter.XiaoFeiFilter;
import com.junyou.bus.xiaofei.filter.XiaoFeiManager;
import com.junyou.cmd.ClientCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.KaiFuPaiMingHDLogEvent;
import com.junyou.event.PaiMingLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.email.export.EmailExportService;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;



/**
 * @description
 * @author ZHONGDIAN
 * @created 2011-11-16上午10:29:07
 */
@Service
public class RefabuXiaoFeiService { 
	
	@Autowired
	private RefabuXiaofeiDao refabuXiaofeiDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private SessionManagerExportService sessionManagerExportService;
	@Autowired
	private EmailExportService emailExportService;
	@Autowired
	private RoleYuanbaoRecordService roleYuanbaoRecordService;
	
	public void quartXiaoFei() {
		Map<Integer, XiaofeiConfigGroup> groups = XiaofeiConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		//循环充值礼包配置数据
		for(Map.Entry<Integer, XiaofeiConfigGroup> entry : groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			XiaofeiConfigGroup config = XiaofeiConfigExportService.getInstance().loadByMap(entry.getKey());
			if(config == null){
				continue;
			}
			ChuanQiLog.error("消费活动最大人数："+config.getMaxPeople()+"子活动ID："+entry.getKey());
			List<RefabuXiaofei> list = refabuXiaofeiDao.dbLoadAllByRank(entry.getKey(),config.getMaxPeople());
			if(list != null){
				ChuanQiLog.error("消费活动查出来的人数："+list.size()+"子活动ID："+entry.getKey());
			}else{
				ChuanQiLog.error("消费活动查出来的list是null子活动ID："+entry.getKey());
			}
			if(list != null){
				XiaoFeiManager.setRanks(entry.getKey(),list, true);
			}
			
		}
	}
	
	
	public List<RefabuXiaofei> initRefabuXiaofei(Long userRoleId){
		return refabuXiaofeiDao.initRefabuXiaofei(userRoleId);
	}
	
	private RefabuXiaofei getXiaoFei(Long userRoleId,int subId){
		List<RefabuXiaofei> list = refabuXiaofeiDao.cacheLoadAll(userRoleId, new XiaoFeiFilter(subId));
		if(list == null || list.size() <= 0){
			RefabuXiaofei xiaofei = new RefabuXiaofei();
			xiaofei.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			xiaofei.setUserRoleId(userRoleId);
			xiaofei.setXfGold(0);
			xiaofei.setCreateTime(new Timestamp(System.currentTimeMillis()));
			xiaofei.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			xiaofei.setSubId(subId);
			
			refabuXiaofeiDao.cacheInsert(xiaofei, userRoleId);
			
			return xiaofei;
		}
		return list.get(0);
	}
	
	public Object[] getXiaoFeiInfo(Long userRoleId,Integer version,int subId,int begin,int number){
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

		if(begin < 0 || number <= 0){
			return AppErrorCode.CLIENT_DATA_ERROR;
		}
		
		List<RefabuXiaofei> xfList = XiaoFeiManager.getRanksByNum(subId,begin, number);
		//没有数据
		if(xfList == null || xfList.size() <= 0){
			return new Object[]{
					1,
					subId,
					begin,
					number,
					null,
					0
			};
		}
		
		List<String> userList = new ArrayList<>();
		for (int i = 0; i < xfList.size(); i++) {
			RefabuXiaofei xiaof = xfList.get(i);
			userList.add(getRoleName(xiaof.getUserRoleId()));
		}
		
		return new Object[]{
				1,
				subId,
				begin,
				number,
				userList.toArray(),
				XiaoFeiManager.getRankSize(subId)
		};
	}

	/**
	 * 获取第1名的名字
	 * @return
	 */
	private String getOneName(int subId){
		RefabuXiaofei xiaofei = XiaoFeiManager.getActivityXiaoFeiByNum(subId,1);
		if(xiaofei == null){
			return null;
		}
		return getRoleName(xiaofei.getUserRoleId());
	}
	
	private String getRoleName(Long userRoleId){
		RoleWrapper role = null;
		try {
			
			if(sessionManagerExportService.isOnline(userRoleId)){
				role = roleExportService.getLoginRole(userRoleId);
				return role.getName();
			}else{
				role = roleExportService.getUserRoleFromDb(userRoleId);
				return role.getName();
			}
		} catch (Exception e) {
			role = roleExportService.getUserRoleFromDb(userRoleId);
		}
		return role.getName();
	}
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){

		XiaofeiConfigGroup config = XiaofeiConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		RefabuXiaofei xiaofei = getXiaoFei(userRoleId, subId);
		//检测今天消耗的元宝
		yuanbaoJianCe(xiaofei, config);
		
		List<RefabuXiaofei> list = XiaoFeiManager.getRanks(subId);
		if(xiaofei.getXfGold() > 0 && (list == null || list.size() <= 0)){
			List<RefabuXiaofei> xflist = refabuXiaofeiDao.dbLoadAllByRank(subId,config.getMaxPeople());
			if(xflist != null){
				XiaoFeiManager.setRanks(subId,xflist, true);
			}
		}
		return new Object[]{
				config.getPic(),
				//config.getDes(),
				config.getDataList().toArray(),
				new Object[]{getOneName(subId),XiaoFeiManager.getUserPaiMing(subId,userRoleId),xiaofei.getXfGold()}
		};
	
	}
	
	public Object[] getRefbLingQuStatus(Long userRoleId, Integer subId){
		RefabuXiaofei xiaofei = getXiaoFei(userRoleId, subId);
		
		return new Object[]{
				subId,
				new Object[]{getOneName(subId),XiaoFeiManager.getUserPaiMing(subId,userRoleId),xiaofei.getXfGold()}
		};
	}
	
	
	public void xiaofeiYb(Long userRoleId,Long yb){

		if(yb < 0){
			return;
		}
		Map<Integer, XiaofeiConfigGroup> groups = XiaofeiConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		long curTime = GameSystemTime.getSystemMillTime();
		//循环充值礼包配置数据
		for(Map.Entry<Integer, XiaofeiConfigGroup> entry : groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			//判断活动是否结束
			if(configSong != null){
				long endTime = configSong.getEndTimeByMillSecond();//活动结束时间
				long bigTime = 60*1000;//结算间隔时间
				if(endTime - bigTime <= curTime && endTime + bigTime >= curTime){//否则 当前时间减去结束时间  相差在1分钟之内
					continue;
				}
			}
			XiaofeiConfigGroup config = XiaofeiConfigExportService.getInstance().loadByMap(entry.getKey());
			if(config == null){
				continue;
			}
			RoleWrapper role = roleExportService.getLoginRole(userRoleId);
			RefabuXiaofei xiaofei = getXiaoFei(userRoleId, entry.getKey());
			if(xiaofei != null){
				xiaofei.setXfGold((int) (xiaofei.getXfGold().intValue()+yb));
				xiaofei.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				refabuXiaofeiDao.cacheUpdate(xiaofei, userRoleId);
			}
			if(role != null && role.isGm()){
				return;//GM不参与消费排行
			}
			List<RefabuXiaofei> list = XiaoFeiManager.getRanks(entry.getKey());
			if(list == null){
				list = new ArrayList<>();
				XiaoFeiManager.setRanks(entry.getKey(),list, true);
			}
			RefabuXiaofei managerXF = XiaoFeiManager.getActivityXiaoFeiByRoleId(entry.getKey(),userRoleId);
			if(managerXF != null){
				/*managerXF.setXfGold((int) (managerXF.getXfGold().intValue()+yb));
				managerXF.setUpdateTime(new Timestamp(System.currentTimeMillis()));*/
				XiaoFeiManager.delActivityXiaoFeiByRoleId(entry.getKey(), userRoleId);
				list.add(xiaofei);
			}else{
				if(list.size() < config.getMaxPeople()){
					list.add(xiaofei);
				}else{
					RefabuXiaofei othersXF = XiaoFeiManager.getActivityXiaoFeiByNum(entry.getKey(),config.getMaxPeople());
					if(othersXF != null){
						if(xiaofei.getXfGold() > othersXF.getXfGold()){
							list.remove(config.getMaxPeople() - 1);
							list.add(xiaofei);
						}
					}
				}
			}
			//list重新排行 设置
			Collections.sort(list, new SortXFGoldComparator());
			XiaoFeiManager.setRanks(entry.getKey(),list, true);
		}
	}
	
	
	private void yuanbaoJianCe(RefabuXiaofei xiaofei,XiaofeiConfigGroup config){
		RoleYuanbaoRecord record = roleYuanbaoRecordService.getRoleYuanBaoRecord(xiaofei.getUserRoleId());
		if(record.getXfValue() > xiaofei.getXfGold()){
			xiaofei.setXfGold(record.getXfValue());
			xiaofei.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			refabuXiaofeiDao.cacheUpdate(xiaofei, xiaofei.getUserRoleId());
			
			List<RefabuXiaofei> list = XiaoFeiManager.getRanks(xiaofei.getSubId());
			if(list == null){
				list = new ArrayList<>();
				XiaoFeiManager.setRanks(xiaofei.getSubId(),list, true);
			}
			RefabuXiaofei managerXF = XiaoFeiManager.getActivityXiaoFeiByRoleId(xiaofei.getSubId(),xiaofei.getUserRoleId());
			if(managerXF != null){
				XiaoFeiManager.delActivityXiaoFeiByRoleId(xiaofei.getSubId(), xiaofei.getUserRoleId());
				list.add(xiaofei);
			}else{
				if(list.size() < config.getMaxPeople()){
					list.add(xiaofei);
				}else{
					RefabuXiaofei othersXF = XiaoFeiManager.getActivityXiaoFeiByNum(xiaofei.getSubId(),config.getMaxPeople());
					if(othersXF != null){
						if(xiaofei.getXfGold() > othersXF.getXfGold()){
							list.remove(config.getMaxPeople() - 1);
							list.add(xiaofei);
						}
					}
				}
			}
			//list重新排行 设置
			Collections.sort(list, new SortXFGoldComparator());
			XiaoFeiManager.setRanks(xiaofei.getSubId(),list, true);
		}else if(xiaofei.getXfGold() > 0){
			List<RefabuXiaofei> list = XiaoFeiManager.getRanks(xiaofei.getSubId());
			if(list == null){
				list = new ArrayList<>();
				XiaoFeiManager.setRanks(xiaofei.getSubId(),list, true);
			}
			RefabuXiaofei managerXF = XiaoFeiManager.getActivityXiaoFeiByRoleId(xiaofei.getSubId(),xiaofei.getUserRoleId());
			if(managerXF == null){
				if(list.size() < config.getMaxPeople()){
					list.add(xiaofei);
				}else{
					RefabuXiaofei othersXF = XiaoFeiManager.getActivityXiaoFeiByNum(xiaofei.getSubId(),config.getMaxPeople());
					if(othersXF != null){
						if(xiaofei.getXfGold() > othersXF.getXfGold()){
							list.remove(config.getMaxPeople() - 1);
							list.add(xiaofei);
						}
					}
				}
			}
			//list重新排行 设置
			Collections.sort(list, new SortXFGoldComparator());
			XiaoFeiManager.setRanks(xiaofei.getSubId(),list, true);
		}
	}
	
	/**
	 * 结算邮件
	 */
	public void xfJiangLiEmail() {
		ChuanQiLog.error("消费排行活动结算开始");
		Map<Integer, XiaofeiConfigGroup> groups = XiaofeiConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		ChuanQiLog.error("消费排行活动个数："+groups.size());
		long curTime = GameSystemTime.getSystemMillTime();
		//循环充值礼包配置数据
		for(Map.Entry<Integer, XiaofeiConfigGroup> entry : groups.entrySet()){
			//判断活动是否结束
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if(configSong != null){
				ChuanQiLog.error("活动ID："+entry.getKey()+",结束时间："+new Timestamp(configSong.getEndTimeByMillSecond()));
				long endTime = configSong.getEndTimeByMillSecond();//活动结束时间
				long bigTime = 1*60*1000;//结算间隔时间
				if(endTime - bigTime <= curTime && endTime + bigTime >= curTime){//否则 当前时间减去结束时间  相差在1分钟之内
					//先打印排行日志
					try {
						List<RefabuXiaofei> list = XiaoFeiManager.getRanks(entry.getKey());
						JSONArray consumeItemArray = new JSONArray(); 
						parseJSONArray(list,consumeItemArray);
						GamePublishEvent.publishEvent(new PaiMingLogEvent(LogPrintHandle.PAIMING_XIAOFEI_PAIMING,consumeItemArray));
					} catch (Exception e) {
						ChuanQiLog.error(""+e);
					}
					
					XiaofeiConfigGroup config = XiaofeiConfigExportService.getInstance().loadByMap(entry.getKey());
					Map<Integer, XiaofeiConfig> configMap = config.getConfigMap();
					ChuanQiLog.error("消费排名排行长度："+XiaoFeiManager.getRankSize(entry.getKey())+",子活动ID："+entry.getKey());
					int chang = XiaoFeiManager.getRankSize(entry.getKey());
					if(chang <= 0){
						List<RefabuXiaofei> xflist = refabuXiaofeiDao.dbLoadAllByRank(entry.getKey(),config.getMaxPeople());
						if(xflist == null || xflist.size() <= 0){
							continue;
						}else{
							XiaoFeiManager.setRanks(entry.getKey(),xflist, true);
						}
					}
					for (int i = 1; i <= configMap.size(); i++) {
						XiaofeiConfig xiaofeiConfig =  configMap.get(i);
						if(xiaofeiConfig == null){
							break;
						}
						int start = xiaofeiConfig.getMin();
						int end = xiaofeiConfig.getMax();
						for (int j = start; j <= end; j++) {
							try {
								ChuanQiLog.error("获取第"+j+"名消费信息");
								RefabuXiaofei roleXiaofei = XiaoFeiManager.getActivityXiaoFeiByNum(entry.getKey(), j);
								if(roleXiaofei == null){
									continue;
								}
								long userRoleId = roleXiaofei.getUserRoleId();
								String title = EmailUtil.getCodeEmail(AppErrorCode.XIAOFEI_PAIMING_EMAIL_TITLE);
								String content = EmailUtil.getCodeEmail(AppErrorCode.XIAOFEI_PAIMING_EMAIL,j+"");
								emailExportService.sendEmailToOne(userRoleId,title, content,GameConstants.EMAIL_TYPE_SINGLE, xiaofeiConfig.getEmailItem());
								ChuanQiLog.error("消费排名：给玩家"+userRoleId+"名次"+j+"发送邮件成功!子活动ID："+entry.getKey());
								//roleXiaofei.setXfGold(0);//设置消费元宝为0
								if(sessionManagerExportService.isOnline(userRoleId)){
									refabuXiaofeiDao.cacheUpdate(roleXiaofei, roleXiaofei.getUserRoleId());
								}
								//日志
								GamePublishEvent.publishEvent(new KaiFuPaiMingHDLogEvent(userRoleId, getRoleName(userRoleId), j,xiaofeiConfig.getEmailItem(), LogPrintHandle.XIAOFEI_PAIMING));
							} catch (Exception e) {
								ChuanQiLog.error("",e);
							}
						}
					}
					ChuanQiLog.error("***************消费排名发邮件结束清除数据！****************");
					//删除库里的遗留数据
					refabuXiaofeiDao.dbDeleteBySubId(entry.getKey());
					//清空管理器数据
					XiaoFeiManager.clearRank(entry.getKey());
				}
			}
		}
		ChuanQiLog.error("消费排行活动结算结束");
	}
	
	/**
	 * 转换成日志对应的格式
	 * @param goodsMap
	 * @param receiveItems
	 */
	public void parseJSONArray(List<RefabuXiaofei>  userList,JSONArray receiveItems) {
		if(userList == null || userList.size() == 0){
			return;
		}
		for (int i = 0; i < userList.size();i++) {
			RefabuXiaofei vo =  userList.get(i);
			Map<String,Object> entity = new HashMap<>();
			entity.put("userRoleId", vo.getUserRoleId());
			entity.put("name",getRoleName(vo.getUserRoleId()));
			entity.put("rank",i+1);
			entity.put("numer",vo.getXfGold());
			receiveItems.add(entity);
		}
	}
	
	
}