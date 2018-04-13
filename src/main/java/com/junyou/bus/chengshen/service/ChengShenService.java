package com.junyou.bus.chengshen.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.chenghao.service.export.ChenghaoExportService;
import com.junyou.bus.chengshen.configure.export.ChengShenTitleConfig;
import com.junyou.bus.chengshen.configure.export.ChengShenTitleConfigExportService;
import com.junyou.bus.chengshen.constants.ChenShenConstants;
import com.junyou.bus.chengshen.dao.RoleChengShenDao;
import com.junyou.bus.chengshen.entity.RoleChengShen;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.ShenHunValueInfoLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.ChengShenConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class ChengShenService {

	@Autowired
	private RoleChengShenDao roleChengShenDao;
	@Autowired
	private ChengShenTitleConfigExportService chengShenTitleConfigExportService; 
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private ChenghaoExportService chenghaoExportService;
	
	/***
	 *初始化
	 */
	public RoleChengShen  initRoleChengShen(Long userRoleId){
		List<RoleChengShen> data =  roleChengShenDao.initRoleChengShen(userRoleId);
		return data!=null && data.size()>0?data.get(0):null;
	}
	/**
	 * 初始化属性
	 */
	public Map<String, Long> initAttrMap(long userRoleId){
		RoleChengShen  roleChengShen  = roleChengShenDao.cacheLoad(userRoleId, userRoleId);
		if(roleChengShen==null){
			return  null;
		}
		return getAttrMapByLevel(roleChengShen.getLevel());
	}
	/**
	 * 请求成神信息
	 * @param userRoleId
	 * @return
	 */
	public Integer getInfo(Long userRoleId){
		ChengShenConfig  config  = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_CHENG_SHEN);
		if(config==null){
			ChuanQiLog.error("***成神系统配置不存在***");
			return null;
		}
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper.getLevel()<config.getOpen()){
			ChuanQiLog.error("***等级不足不能访问成神系统数据***");
			return  null;
		}
		return  getChengShen(userRoleId).getLevel();
	}
	/**
	 * 升级
	 * @param userRoleId
	 * activeLevel 要激活的等级
	 * @return
	 */
	public Object[] upgrade(Long userRoleId,int activeLevel){
		
		ChengShenConfig  config  = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_CHENG_SHEN);
		if(config==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper.getLevel()<config.getOpen()){
			return  AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		 
		RoleChengShen  roleChengShen  = getChengShen(userRoleId);
		
		if(activeLevel<roleChengShen.getLevel()+1){
			 //此等级已经激活
			return AppErrorCode.CHENG_SHEN_LEVEL_SJ_ALREADY;
		}
		if(activeLevel>roleChengShen.getLevel()+1){
			 //不能越级激活
			return AppErrorCode.CHENG_SHEN_LEVEL_SJ_ERROR;
		}
		if(roleChengShen.getLevel()==chengShenTitleConfigExportService.getMaxJjLevel()){
			
			return AppErrorCode.CHENG_SHEN_MAX_LIMIT_ERROR;
		}
		
		ChengShenTitleConfig nextChengShenTitleConfig = loadConfig(roleChengShen.getLevel()+1);
		int needValue = nextChengShenTitleConfig.getNeedValue();
		if(roleChengShen.getShenhunValue()<needValue){
			return AppErrorCode.CHENG_SHEN_VALUE_NOT_ENOUGH;
		}
		
		roleChengShen.setShenhunValue(roleChengShen.getShenhunValue()-needValue);
		roleChengShen.setUpdateShenhunTime(GameSystemTime.getSystemMillTime());
		roleChengShen.setLevel(roleChengShen.getLevel()+1);
		roleChengShen.setUpdateLevelTime(GameSystemTime.getSystemMillTime());
		roleChengShenDao.cacheUpdate(roleChengShen, userRoleId);
		
		//加属性
		notifyStageAttrChange(userRoleId,roleChengShen.getLevel());
		//更新称号
		chenghaoExportService.activateChenghao(userRoleId,nextChengShenTitleConfig.getChengHaoId());
		//日志
		GamePublishEvent.publishEvent(new ShenHunValueInfoLogEvent(userRoleId, ChenShenConstants.SHZ_CONSUME,needValue,ChenShenConstants.SHZ_SJ_CONSUME,roleChengShen.getLevel()));
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHENG_SHEN_SHZ_GET, roleChengShen.getShenhunValue());
		return new Object[]{1,roleChengShen.getLevel()};
	}
	/**
	 * 玩家登陆推送神魂值给client
	 */
	public void onlineHandlerToClientSHZ(long userRoleId){
		RoleChengShen  roleChengShen  = roleChengShenDao.cacheLoad(userRoleId, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHENG_SHEN_SHZ_GET, roleChengShen==null?0:roleChengShen.getShenhunValue());
	}
	/**
	 * 推送神魂值
	 */
	public void toClientSHZChange(){
		
	}
	/**
	 * 使用神魂丹获得神魂值
	 * @param userRoleId
	 * @return
	 */
	public Object[] useShenHunDan(long userRoleId, int data1,int count) {
		int addValue =  data1*count;
		RoleChengShen roleChengShen = addShenHunZhi(userRoleId,addValue);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHENG_SHEN_SHZ_GET, roleChengShen.getShenhunValue());
		//日志
		GamePublishEvent.publishEvent(new ShenHunValueInfoLogEvent(userRoleId, ChenShenConstants.SHZ_GET,addValue,
				ChenShenConstants.SHZ_USE_SHD_GET,roleChengShen.getLevel()));
		return  null;
	}
	
	/**
	 *增加神魂值 --对外方法 
	 */
	public void addSHZ(long userRoleId, int addValue){
		RoleChengShen roleChengShen = addShenHunZhi(userRoleId,addValue);
		GamePublishEvent.publishEvent(new ShenHunValueInfoLogEvent(userRoleId, ChenShenConstants.SHZ_GET,addValue,
				ChenShenConstants.SHZ_JJC_GET,roleChengShen.getLevel()));
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHENG_SHEN_SHZ_GET, roleChengShen.getShenhunValue());
	}

	
	/**更新神魂值**/
	private RoleChengShen addShenHunZhi(long userRoleId, int value){
		RoleChengShen roleChengShen  =  getChengShen(userRoleId);
//		if(roleChengShen.getLevel()==chengShenTitleConfigExportService.getMaxJjLevel()){
			//如果是到了最高级了就不加神魂值
//			return  roleChengShen;
//		}
		roleChengShen.setShenhunValue(roleChengShen.getShenhunValue()+value);
		roleChengShen.setUpdateShenhunTime(GameSystemTime.getSystemMillTime());
		roleChengShenDao.cacheUpdate(roleChengShen, userRoleId);
		return roleChengShen;
	}
	/**
	 * 获得对应称号的属性
	 */
	public Map<String, Long> getAttrMapByLevel(int level){
		ChengShenTitleConfig config  = loadConfig(level);
		if(config==null){
			return  null;
		}
		return config.getAttrMap();
	}
	/**
	 * 通知场景加属性
	 */
	private void notifyStageAttrChange(long userRoleId,int level){
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHENG_SHEN_SJ_ATTR_CHANGE, level);
	}

	/**
	 * 获得玩家的神魂值
	 * @param userRoleId
	 * @return
	 */
	public long getShenHunZhiByUserRoleId(long userRoleId){
		RoleChengShen  roleChengShen  = roleChengShenDao.cacheLoad(userRoleId, userRoleId);
		return roleChengShen==null?0:roleChengShen.getShenhunValue();
	}
	
	//获取entity
	private RoleChengShen getChengShen(Long userRoleId){
		RoleChengShen  roleChengShen  = roleChengShenDao.cacheLoad(userRoleId, userRoleId);
		if(roleChengShen==null){
			roleChengShen  = new RoleChengShen();
			roleChengShen.setUserRoleId(userRoleId);
			roleChengShen.setLevel(0);
			roleChengShen.setShenhunValue(0L);
			roleChengShen.setUpdateLevelTime(GameSystemTime.getSystemMillTime());
			roleChengShen.setUpdateShenhunTime(GameSystemTime.getSystemMillTime());
			roleChengShen.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			roleChengShenDao.cacheInsert(roleChengShen, userRoleId);
		}
		return roleChengShen;
	}
	//获取配置信息
	private ChengShenTitleConfig loadConfig(int level){
		
		return chengShenTitleConfigExportService.loadByLevel(level);
	}
}
