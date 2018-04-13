/**
 * 
 */
package com.junyou.bus.kaifuactivity.service;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuQuanMingXiuXianConfig;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuQuanMingXiuXianConfigExportService;
import com.junyou.bus.kaifuactivity.configure.export.KaiFuQuanMingXiuXianGroupConfig;
import com.junyou.bus.kaifuactivity.dao.QiriLevelDao;
import com.junyou.bus.kaifuactivity.dao.QiriLevelLibaoDao;
import com.junyou.bus.kaifuactivity.entity.QiriLevel;
import com.junyou.bus.kaifuactivity.entity.QiriLevelLibao;
import com.junyou.bus.kaifuactivity.filter.QiriLevelLibaoFilter;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.service.AbstractActivityService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.KaiFuPaiMingLingQuLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.log.LogPrintHandle;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;



/**
 *  全民修仙冲级活动(等级榜)
 */
@Service
public class RfbLevelHuoDongService  extends AbstractActivityService {

	@Override
	public boolean getChildFlag(long userRoleId,int subId) {
		
		KaiFuQuanMingXiuXianGroupConfig configs = KaiFuQuanMingXiuXianConfigExportService.getInstance().loadByMap(subId);
		if(configs == null){
			return false;
		}
		
		//处理数据
		Map<Integer, KaiFuQuanMingXiuXianConfig> configMap = configs.getConfigMap();
		
		//如果这两个同时存在,则不可领取奖励
		KaiFuQuanMingXiuXianConfig zhanliConfig =  configMap.get(1);//ID为1规定为阶段邮件奖励
		KaiFuQuanMingXiuXianConfig paihengConfig =  configMap.get(21);//判断是否有排行配置
		if(paihengConfig != null && zhanliConfig != null){
			return false;
		}
		
		//玩家等级
		RoleWrapper loginRole = roleExportService.getLoginRole(userRoleId);
		
		for (KaiFuQuanMingXiuXianConfig config : configMap.values()) {
			if(config.getId() < 0){
				continue;
			}
			
			if(loginRole.getLevel() < config.getValue1()){
				continue;
			}
			
			//领取数量
			QiriLevel qiriLevel = getQiriLevel(config.getId(), subId);
			if(config.getShuliang() != 0 && qiriLevel.getLingquNumber() >= config.getShuliang()){
				continue;
			}
			
			QiriLevelLibao qiriLevelLibao = getQiriLevelLibao(userRoleId, subId);
			//判断是否已经领取奖励
			if(isCanRecevieState(qiriLevelLibao.getLevelStatus(), config.getId())){
				return true;
			}
		}
		return false;
	}
	
	
	@Autowired
	private QiriLevelLibaoDao qiriLevelLibaoDao;
	@Autowired
	private QiriLevelDao qiriLevelDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	
	public List<QiriLevelLibao> initAll(long userRoleId){
		return qiriLevelLibaoDao.initAll(userRoleId);
	}
	
	
	public Object[] lingQuJiangLi(long userRoleId,Integer version, Integer configId,int subId) {
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
		KaiFuQuanMingXiuXianConfig config = KaiFuQuanMingXiuXianConfigExportService.getInstance().loadByKeyId(subId,configId);
		if(config== null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//玩家等级
		RoleWrapper loginRole = roleExportService.getLoginRole(userRoleId);
		if(loginRole.getLevel() < config.getValue1()){
			return AppErrorCode.KAIFU_TIAOJIAN_ERROR;
		}
		
		//领取数量
		QiriLevel qiriLevel = getQiriLevel(configId, subId);
		if(config.getShuliang() != 0 && qiriLevel.getLingquNumber() >= config.getShuliang()){
			return AppErrorCode.KAIFU_NUMBER_ERROR;
		}
		
		QiriLevelLibao qiriLevelLibao = getQiriLevelLibao(userRoleId, subId);
		//判断是否已经领取奖励
		if(!isCanRecevieState(qiriLevelLibao.getLevelStatus(), configId)){
			return AppErrorCode.KAIFU_YI_LINGQU;
		}
		
		//check进背包
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(config.getJiangitem(), userRoleId);
		if(code != null){
			return code;
		}else{
			roleBagExportService.putInBag(config.getJiangitem(), userRoleId, GoodsSource.RFB_QMXX, true);
		}
		
		//更新玩家领取状态
		qiriLevelLibao.setLevelStatus(chanageState(qiriLevelLibao.getLevelStatus(), configId));
		updateQiRiLevelLiBao(qiriLevelLibao);
		//更新礼包领取状态
		qiriLevel.setLingquNumber(qiriLevel.getLingquNumber() + 1);
		updateQiRiLevel(qiriLevel);
		
		super.checkIconFlag(userRoleId, subId);
		//日志
		JSONArray receiveItems = new JSONArray();
		LogFormatUtils.parseJSONArray(config.getJiangitem(), receiveItems);
		GamePublishEvent.publishEvent(new KaiFuPaiMingLingQuLogEvent(userRoleId, loginRole.getName(), receiveItems, LogPrintHandle.KAIFU_ACTITY_LQ_LEVEL));
		
		return new Object[]{1,subId,configId};
	}
	
	
	private QiriLevel getQiriLevel(int configId,int subId){
		List<QiriLevel> list = qiriLevelDao.dbLoadAllById(configId,subId);
		if(list == null || list.size() <= 0){
			QiriLevel level = new QiriLevel();
			level.setConfigId(configId);
			level.setSubId(subId);
			level.setCreateTime(new Timestamp(System.currentTimeMillis()));
			level.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			level.setLingquNumber(0);
			
			qiriLevelDao.dbInsert(level);
			
			return level;
		}
		return list.get(0);
	}
	
	private QiriLevelLibao getQiriLevelLibao(Long userRoleId,int subId){
		List<QiriLevelLibao> libaoList = qiriLevelLibaoDao.cacheLoadAll(userRoleId, new QiriLevelLibaoFilter(subId));
		if(libaoList != null && libaoList.size() > 0){
			QiriLevelLibao  libao  = libaoList.get(0);
			return libao;
		}else{
			QiriLevelLibao libao = new QiriLevelLibao();
			libao.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
			libao.setLevelStatus(0);
			libao.setSubId(subId);
			libao.setUserRoleId(userRoleId);
			libao.setCreateTime(new Timestamp(System.currentTimeMillis()));
			libao.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			
			qiriLevelLibaoDao.cacheInsert(libao, userRoleId);
			
			return libao;
			
		}
	}
	
	private void updateQiRiLevel(QiriLevel qiriLevel){
		qiriLevelDao.dbUpdate(qiriLevel);
	}
	
	private void updateQiRiLevelLiBao(QiriLevelLibao levelLiBao){
		levelLiBao.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		qiriLevelLibaoDao.cacheUpdate(levelLiBao, levelLiBao.getUserRoleId());
	}
	
	
	
	/**
	 * 修改状态
	 * @param state
	 * @return
	 */
	public static Integer chanageState(Integer state, Integer day){
		day = day.intValue() - 1;
		
		return (1 << day) | state;
	}
	
	/**
	 * 判断是否已经领取奖励
	 * @param state
	 * @param id
	 * @return true:可领取奖励
	 */
	public static Boolean isCanRecevieState(Integer state, Integer id){
		if(!state.equals(0)){
			id = id.intValue() - 1;
			
			return (state >> id & 1) == 0;
		}
		return true;
	}
	/**
	 * 判断是否已经领取奖励
	 * @param state
	 * @param id
	 * @return 0:未领取奖励，1已领
	 */
	public static int isCanRecevieStateRint(Integer state, Integer id){
		if(!state.equals(0)){
			id = id.intValue() - 1;
			
			if((state >> id & 1) == 0){
				return 0;
			}else{
				return 1;
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
		KaiFuQuanMingXiuXianGroupConfig config = KaiFuQuanMingXiuXianConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		
		
		//处理数据
		Map<Integer, KaiFuQuanMingXiuXianConfig> configMap = config.getConfigMap();
		List<Object[]> data = null;
		if(configMap != null && configMap.size() > 0){
			
			data = new ArrayList<>();
			Map<Integer, KaiFuQuanMingXiuXianConfig> dataMap = new HashMap<>(configMap);//未处理的数据（没有玩家领取）
			List<QiriLevel> list = qiriLevelDao.dbLoadAll(subId);;//获取活动的数据
			//玩家数据
			if(list != null && list.size() > 0){
				List<QiriLevelLibao> libaoList = qiriLevelLibaoDao.cacheLoadAll(userRoleId, new QiriLevelLibaoFilter(subId));
				if(libaoList != null && libaoList.size() > 0){
					QiriLevelLibao  libao  = libaoList.get(0);
					
					for (QiriLevel qirilevel : list) {
						
							KaiFuQuanMingXiuXianConfig qmxxConfig =  configMap.get(qirilevel.getConfigId());
								Object[] lvData =  new Object[]{
										qmxxConfig.getValue1()//0 int 需要到达等级 
										,qirilevel.getLingquNumber() == null ? 0 : qirilevel.getLingquNumber()//1 int 已领取个数 
										,qmxxConfig.getShuliang()//2 int 最大可领取个数 
										,qmxxConfig.getItemClientMap()//3 array [ [0:String(对应职业奖励物品),1:(数量)]…] 
										,isCanRecevieStateRint(libao.getLevelStatus(), qirilevel.getConfigId())//4 int 领取状态(0:未领,1:已领)
										,qmxxConfig.getId()//5 int 礼包id 
								};
							
							data.add(lvData);
							dataMap.remove(qirilevel.getConfigId());
						}
					}else{
						for (QiriLevel qirilevel : list) {
							
							KaiFuQuanMingXiuXianConfig qmxxConfig =  configMap.get(qirilevel.getConfigId());
								Object[] lvData =  new Object[]{
										qmxxConfig.getValue1()//0 int 需要到达等级 
										,qirilevel.getLingquNumber() == null ? 0 : qirilevel.getLingquNumber()//1 int 已领取个数 
										,qmxxConfig.getShuliang()//2 int 最大可领取个数 
										,qmxxConfig.getItemClientMap()//3 array [ [0:String(对应职业奖励物品),1:(数量)]…] 
										,0//4 int 领取状态(0:未领,1:已领)
										,qmxxConfig.getId()//5 int 礼包id 
								};
							
							data.add(lvData);
							dataMap.remove(qirilevel.getConfigId());
						}
					}
				}
			
			//拉取配置
			if(dataMap != null && dataMap.size() > 0){
				for (KaiFuQuanMingXiuXianConfig zbConfig : dataMap.values()) {
					Object[] lvData =  new Object[]{
							zbConfig.getValue1()//0 int 需要到达等级 
							,0//1 int 已领取个数 
							,zbConfig.getShuliang()//2 int 最大可领取个数 
							,zbConfig.getItemClientMap()//3 array [ [0:String(对应职业奖励物品),1:(数量)]…] 
							,0//4 int 领取状态(0:未领,1:已领)
							,zbConfig.getId()//5 int 礼包id 
					};
				
				data.add(lvData);
				}
			}
		}
		
		return new Object[]{
				config.getDes()
				,data == null ? null : data.toArray()
		};
	}
	/**
	 * 获取礼包信息
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbLingQuStatus(Integer subId){
		KaiFuQuanMingXiuXianGroupConfig config = KaiFuQuanMingXiuXianConfigExportService.getInstance().loadByMap(subId);
		if(config == null){
			return null;
		}
		
		
		//处理数据
		Map<Integer, KaiFuQuanMingXiuXianConfig> configMap = config.getConfigMap();
		List<Object[]> data = new ArrayList<>();
		if(configMap != null && configMap.size() > 0){
			
			Map<Integer, KaiFuQuanMingXiuXianConfig> dataMap = new HashMap<>(configMap);//未处理的数据（没有玩家领取）
			List<QiriLevel> list = qiriLevelDao.dbLoadAll(subId);;//获取活动的数据
			//玩家数据
			if(list != null && list.size() > 0){
					
					for (QiriLevel qirilevel : list) {
						
						KaiFuQuanMingXiuXianConfig qmxxConfig =  configMap.get(qirilevel.getConfigId());
						Object[] lvData =  new Object[]{
								qmxxConfig.getId(),
								qirilevel.getLingquNumber()
						};
						
						data.add(lvData);
						dataMap.remove(qirilevel.getConfigId());
				}
			}
			
			//拉取配置
			if(dataMap != null && dataMap.size() > 0){
				for (KaiFuQuanMingXiuXianConfig zbConfig : dataMap.values()) {
					Object[] lvData =  new Object[]{
							zbConfig.getId(),//礼包ID
							0				//已领取数量
					};
					
					data.add(lvData);
				}
			}
		}
		
		return  new Object[]{subId,data.toArray()};
	}
	
	
}