package com.junyou.bus.zhuansheng.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.zhuansheng.configure.ZhuanShengConfig;
import com.junyou.bus.zhuansheng.configure.ZhuanShengConfigExportService;
import com.junyou.bus.zhuansheng.dao.ZhuanshengDao;
import com.junyou.bus.zhuansheng.entity.Zhuansheng;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.ZhuanShengDuiHuanLogEvent;
import com.junyou.event.ZhuanShengLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.ZhuanshengPublicConfig;

/**
 * @author LiuYu
 * 2015-11-2 下午3:08:44
 */
@Service
public class ZhuanshengService {
	
	@Autowired
	private ZhuanshengDao zhuanshengDao;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private ZhuanShengConfigExportService zhuanShengConfigExportService;
	
	
	public List<Zhuansheng> initZhuansheng(Long userRoleId) {
		return zhuanshengDao.initZhuansheng(userRoleId);
	}
	
	public Map<String,Long> getZhuangShengAttribute(Long userRoleId){
		Zhuansheng zhuansheng = zhuanshengDao.cacheLoad(userRoleId, userRoleId);
		if(zhuansheng == null){
			return null;
		}
		ZhuanShengConfig config = zhuanShengConfigExportService.loadByLevel(zhuansheng.getSzLevel());
		if(config == null){
			return null;
		}
		return config.getAttribute();
	}
	
	public Integer getZhuanshengLevel(Long userRoleId){
		Zhuansheng zhuansheng = zhuanshengDao.cacheLoad(userRoleId, userRoleId);
		if(zhuansheng == null){
			return null;
		}
		return zhuansheng.getSzLevel();
	}
	
	public Object[] duihuan(Long userRoleId,long value){
		ZhuanshengPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_ZHUANSHENG);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(value < config.getMinDuihuan()){
			return AppErrorCode.ZHUANSHENG_VALUE_LOW_MIN;//低于最低兑换要求
		}
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null || role.getLevel() < config.getOpen()){
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;//等级不足
		}
		long exp = value * GameConstants.DUIHUAN_PER_EXP;
		Object[] result = roleExportService.costExp(userRoleId, exp);
		if(result != null){
			return result;
		}
		long xiuwei = value * config.getPerXiuwei();
		roleBusinessInfoExportService.addXiuwei(userRoleId, xiuwei);
		GamePublishEvent.publishEvent(new ZhuanShengDuiHuanLogEvent(userRoleId, role.getName(), exp, xiuwei));
		return AppErrorCode.OK;
	}
	
	public Object[] zhuansheng(Long userRoleId){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper == null){
			return AppErrorCode.ROLE_IS_NOT_EXIST;
		}
		Zhuansheng zhuansheng = zhuanshengDao.cacheLoad(userRoleId, userRoleId);
		int level = zhuanShengConfigExportService.getMinLevel();
		boolean create = true;
		if(zhuansheng != null){
			create = false;
			level = zhuansheng.getSzLevel() + 1;
		}
		ZhuanShengConfig config = zhuanShengConfigExportService.loadByLevel(level);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(roleWrapper.getLevel() < config.getNeedLv()){
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		
		if(!roleBusinessInfoExportService.isEnoughXiuwei(userRoleId, config.getXiuwei())){
			return AppErrorCode.ZHUANSHENG_NO_ENOUGH_XIUWEI;
		}
		Object[] result = roleBagExportService.checkRemoveBagItemByGoodsType(config.getId1(), config.getCount(), userRoleId);
		if(result != null){
			return result;
		}
		roleBusinessInfoExportService.costXiuwei(userRoleId, config.getXiuwei());
		roleBagExportService.removeBagItemByGoodsType(config.getId1(), config.getCount(), userRoleId, GoodsSource.GOODS_ZHUANSHENG, true, true);
		if(create){
			zhuansheng = new Zhuansheng();
			zhuansheng.setSzLevel(level);
			zhuansheng.setUserRoleId(userRoleId);
			zhuanshengDao.cacheInsert(zhuansheng, userRoleId);
		}else{
			zhuansheng.setSzLevel(level);
			zhuanshengDao.cacheUpdate(zhuansheng, userRoleId);
		}
		
		//通知场景属性变化
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.ZHUANSHENG_LEVEL_CHANGE, level);
		GamePublishEvent.publishEvent(new ZhuanShengLogEvent(userRoleId, roleWrapper.getName(), level,config.getId1(), config.getCount()));
		
		return new Object[]{1,level};
	}
	
}
