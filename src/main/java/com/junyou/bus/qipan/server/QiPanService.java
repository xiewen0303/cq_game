/**
 * 
 */
package com.junyou.bus.qipan.server;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.qipan.configure.export.QiPanConfig;
import com.junyou.bus.qipan.configure.export.QiPanConfigExportService;
import com.junyou.bus.qipan.configure.export.QiPanConfigGroup;
import com.junyou.bus.qipan.dao.QipanDao;
import com.junyou.bus.qipan.entity.Qipan;
import com.junyou.bus.qipan.filter.QiPanFilter;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.service.AbstractActivityService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.QinPanLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * 棋盘
 */
@Service
public class QiPanService extends AbstractActivityService {

	@Override
	public boolean getChildFlag(long userRoleId,int subId) {
		//棋盘数据
		Qipan qipan = getQiPan(userRoleId, subId);		
		//判断转盘次数
		return qipan == null? false : qipan.getCount() > 0;
	}
	
	@Autowired
	private QipanDao qipanDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	public List<Qipan> initQiPan(Long userRoleId){
		return qipanDao.initQipan(userRoleId);
	}
	
	private Qipan getQiPan(Long userRoleId,int subId){
		List<Qipan> list = qipanDao.cacheLoadAll(userRoleId, new QiPanFilter(subId));
		if(list == null || list.size() <= 0){
			Qipan qipan = new Qipan();
			qipan.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			qipan.setUserRoleId(userRoleId);
			qipan.setQipanId(1);
			qipan.setQipanStep(0);
			qipan.setLingquStatus("");
			qipan.setRechargeVal(0);
			qipan.setCount(0);
			qipan.setYihuoCount(0);
			qipan.setCreateTime(new Timestamp(System.currentTimeMillis()));
			qipan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			qipan.setSubId(subId);
			
			qipanDao.cacheInsert(qipan, userRoleId);
			
			return qipan;
		}
		Qipan qipan = list.get(0);
		//不是同一天，清理数据
		if(!DateUtils.isSameDay(qipan.getUpdateTime(), new Timestamp(GameSystemTime.getSystemMillTime()))){
			qipan.setRechargeVal(0);
			qipan.setYihuoCount(0);
			qipan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			qipanDao.cacheUpdate(qipan, userRoleId);
			
		}
		return qipan;
	}
	
	
	public Object[] zhuan(Long userRoleId,Integer version,int subId){
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
		//棋盘数据
		Qipan qipan = getQiPan(userRoleId, subId);		
		//判断转盘次数
		if(qipan.getCount() <= 0){
			return AppErrorCode.NO_QIPAN_CPUNT;
		}
		//判断配置
		QiPanConfig config = QiPanConfigExportService.getInstance().loadByKeyId(subId,qipan.getQipanId());
		if(config== null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Map<Integer, Float> zhuanMap = config.getZhuanMap();//转盘MAP  1-6
		//随机1个步数
		int bu = Lottery.getRandomKey(zhuanMap);
		//奖励
		Map<String, Integer> itemMap = getJiangItem(qipan.getQipanStep(), bu, config.getGeWeiMap());
		
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(itemMap, userRoleId);
		if(bagCheck != null){
			return bagCheck;
		}
		
		Object[] gezi = null;
		//判断是否已经达到最大步数
		int dBu = qipan.getQipanStep() + bu;//当前步数
		if(dBu >= config.getMaxGe()){
			//是否有下一级棋盘
			QiPanConfig nextConfig = QiPanConfigExportService.getInstance().loadByKeyId(subId,qipan.getQipanId()+1);
			if(nextConfig != null){
				qipan.setQipanId(nextConfig.getId());
				qipan.setQipanStep(0);
				gezi = nextConfig.getClientGw().toArray();
			}else{
				qipan.setQipanStep(0);
			}
		}else{
			qipan.setQipanStep(dBu);
		}
		qipan.setCount(qipan.getCount() - 1);
		qipan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		qipanDao.cacheUpdate(qipan, userRoleId);
		
		//发放奖励
		roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.GOODS_GET_QP, LogPrintHandle.GET_RFB_QIPAN, LogPrintHandle.GBZ_RFB_QIPAN, true);
		
		//检测是否要消失角标提示
		super.checkIconFlag(userRoleId, subId);
		
		//日志打印 
		try {
			JSONArray receiveItems = new JSONArray();
			LogFormatUtils.parseJSONArray(itemMap, receiveItems);
			GamePublishEvent.publishEvent(new QinPanLogEvent(userRoleId, getRoleName(userRoleId), receiveItems));
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}	
		
		return new Object[]{1,subId,bu,gezi};
			
	}
	private String getRoleName(Long userRoleId){
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		return role.getName();
	}
	/**
	 *  根据步数获得这次应得的奖励
	 * @param lingQuStatus
	 * @param laoBu
	 * @param bu
	 * @param geWeiMap
	 * @return
	 */
	private Map<String, Integer> getJiangItem(int laoBu,int bu,Map<Integer, Object[]> geWeiMap){
		bu = laoBu + bu;//新步数
		
		Map<String, Integer> itemMap = new HashMap<>();
		
		for (int i = laoBu+1; i <= bu; i++) {
			Object[] obj = geWeiMap.get(i);
			if(obj == null || obj.length <= 0){
				continue;
			}
			for (int j = 0; j < obj.length; j++) {
				Object[] item = (Object[]) obj[j];
				if(itemMap.containsKey(item[0].toString())){
					itemMap.put(item[0].toString(),itemMap.get(item[0])+Integer.parseInt(item[1].toString()));
				}else{
					itemMap.put(item[0].toString(),Integer.parseInt(item[1].toString()));
				}
			}
		}
		
		return itemMap;
	}
	
	/**
	 * 获取某个子活动的热发布某个活动信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId){
		QiPanConfigGroup config = QiPanConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		//处理数据
		Qipan qipan = getQiPan(userRoleId, subId);		
		QiPanConfig ziConfig = QiPanConfigExportService.getInstance().loadByKeyId(subId,qipan.getQipanId());
		
		return new Object[]{
				config.getDes(),
				config.getPic(),
				ziConfig.getClientGw().toArray(),
				new Object[]{qipan.getCount(),qipan.getQipanStep(),qipan.getYihuoCount(),config.getXfValue(),config.getMaxCount()}
				
		};
	}
	
	public Object[] getRefbLingQuStatus(Long userRoleId, Integer subId){
		Qipan qipan = getQiPan(userRoleId, subId);	
		QiPanConfigGroup config = QiPanConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			ChuanQiLog.error("QiPanConfigGroup is NULL subId :" + subId);
			return null;
		}
		return new Object[]{subId,qipan.getCount(),qipan.getQipanStep(),qipan.getYihuoCount(),config.getXfValue(),config.getMaxCount()};
	}
	
	
	public void rechargeYb(Long userRoleId,Long addVal){
		if(addVal < 0){
			return;
		}
		Map<Integer, QiPanConfigGroup> groups = QiPanConfigExportService.getInstance().getAllConfig();
		if(groups.size() == 0){
			return;
		}
		
		//循环充值礼包配置数据
		for(Map.Entry<Integer, QiPanConfigGroup> entry : groups.entrySet()){
			//是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if(configSong == null || !configSong.isRunActivity()){
				continue;
			}
			
			Qipan qipan = getQiPan(userRoleId, entry.getKey());	
			if(qipan.getYihuoCount() == 0){//今日第1次充值 次数+1
				qipan.setCount(qipan.getCount()+1);
				qipan.setYihuoCount(qipan.getYihuoCount() + 1);
			}
			int yb = (int) (qipan.getRechargeVal() + addVal);
			//if(qipan.getYihuoCount() == 1){//今日已或次数为1  才进
			QiPanConfigGroup config = QiPanConfigExportService.getInstance().loadByMap(entry.getKey());
			if(config != null){
				while(yb >= config.getXfValue() && qipan.getYihuoCount() < config.getMaxCount()){//达到条件 再次+1
					qipan.setCount(qipan.getCount()+1);
					qipan.setYihuoCount(qipan.getYihuoCount()+1);
					yb = yb - config.getXfValue();
				}
			}
			//}
			qipan.setRechargeVal(yb);
			qipan.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			qipanDao.cacheUpdate(qipan, userRoleId);

			super.checkIconFlag(userRoleId,configSong.getId());
		}
	}
	
}