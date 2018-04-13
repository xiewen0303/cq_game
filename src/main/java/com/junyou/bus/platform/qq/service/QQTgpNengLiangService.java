package com.junyou.bus.platform.qq.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.configure.export.PlatformGongGongShuJuBiaoConfigExportService;
import com.junyou.bus.platform.configure.export.PlatformPublicConfigConstants;
import com.junyou.bus.platform.qq.confiure.export.QqTgpDuiHuanPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqTgpDuiHuanXHPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqTgpNengLiangPublicConfig;
import com.junyou.bus.platform.qq.confiure.export.QqTgpZhuanPanPublicConfig;
import com.junyou.bus.platform.qq.dao.RoleQqTgpDao;
import com.junyou.bus.platform.qq.entity.RoleQqTgp;
import com.junyou.bus.platform.qq.vo.UserRoleVo;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;

/**
 * @author zhongdian
 * 2015-9-16 下午2:45:39
 */
@Service
public class QQTgpNengLiangService {

	@Autowired
	private RoleQqTgpDao roleQqTgpDao;
	@Autowired
	private PlatformGongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	
	public List<RoleQqTgp> initRoleQqTgps(Long userRoleId){
		return roleQqTgpDao.initRoleQqTgp(userRoleId);
	}
	
	private RoleQqTgp getRoleQqTgp(Long userRoleId){
		List<RoleQqTgp> list = roleQqTgpDao.cacheAsynLoadAll(userRoleId);
		if(list == null || list.size() <= 0){
			RoleQqTgp tgp = new RoleQqTgp();
			tgp.setUserRoleId(userRoleId);
			tgp.setHdCount(0);
			tgp.setZnlVal(1);
			tgp.setDuihuanCount(0);
			tgp.setCreateTime(GameSystemTime.getSystemMillTime());
			tgp.setUpdateTime(GameSystemTime.getSystemMillTime());
			
			roleQqTgpDao.cacheInsert(tgp, userRoleId);
			
			return tgp;
		}
		RoleQqTgp tgp = list.get(0);
		//不是同一天，清理数据
		if(!DateUtils.isSameDay(new Timestamp(tgp.getUpdateTime()), new Timestamp(GameSystemTime.getSystemMillTime()))){
			tgp.setHdCount(0);
			tgp.setDuihuanCount(0);
			if(isXingQiOne()){
				tgp.setZnlVal(0);
			}
			if(tgp.getZnlVal() <= 0){
				tgp.setZnlVal(1);
			}
			tgp.setUpdateTime(GameSystemTime.getSystemMillTime());
			
			roleQqTgpDao.cacheUpdate(tgp, userRoleId);
		}
		return tgp;
	}
	
	/**
	 * 判断是否是星期1
	 * @return
	 */
	private boolean isXingQiOne(){
		Calendar calendarMonday = Calendar.getInstance();
		calendarMonday.setTimeInMillis(GameSystemTime.getSystemMillTime());
		int monday = calendarMonday.get(Calendar.DAY_OF_WEEK) == 1 ? 7 :calendarMonday.get(Calendar.DAY_OF_WEEK) - 1 ;
		return monday == 1;
	}
	
	private QqTgpNengLiangPublicConfig getQqTgpNengLiangPublicConfig(){
		QqTgpNengLiangPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.TGP_NL_CFG);
		return config;
	}
	private QqTgpZhuanPanPublicConfig getQqTgpZhuanPanPublicConfig(){
		QqTgpZhuanPanPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.TGP_NL_ZUANPAN);
		return config;
	}
	private QqTgpDuiHuanPublicConfig getQqTgpDuiHuanPublicConfig(){
		QqTgpDuiHuanPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.TGP_NL_DUIHUAN);
		return config;
	}
	private QqTgpDuiHuanXHPublicConfig getQqTgpDuiHuanXHPublicConfig(){
		QqTgpDuiHuanXHPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PlatformPublicConfigConstants.TGP_NL_DUIHUAN_XH);
		return config;
	}
	
	
	public Object[] getTgpInfo(Long userRoleId){
		RoleQqTgp tgp = getRoleQqTgp(userRoleId);
		QqTgpNengLiangPublicConfig nlConfig = getQqTgpNengLiangPublicConfig();
		if(nlConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		int zsCount = nlConfig.getTgpMap().get("jieshou");
		return new Object[]{1,tgp.getZnlVal(),zsCount-tgp.getHdCount()};
	}
	
	/**
	 * 根据角色名赠送能量值
	 * @param userName
	 * @return
	 */
	public Object[] zengSongHaoYou(Long userRoleId,String userName){
		RoleQqTgp wodeTgp = getRoleQqTgp(userRoleId);
		if(wodeTgp.getZnlVal() <= 0){
			return AppErrorCode.QQ_TGP_NO_NLVALUE;
		}
		UserRoleVo vo = roleQqTgpDao.getUserRoleVoByParams(userName);
		if(vo == null){
			return AppErrorCode.QQ_TGP_NO_ZAIXIAN;
		}
		QqTgpNengLiangPublicConfig nlConfig = getQqTgpNengLiangPublicConfig();
		if(nlConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		int keCount = nlConfig.getTgpMap().get("jieshou");
		RoleQqTgp tadeTgp = getRoleQqTgp(vo.getId());
		if(tadeTgp.getHdCount() >= keCount){
			return AppErrorCode.QQ_TGP_NO_ZSONG;
		}
		//没问题的话就是我的能量减1 他的能量加1
		wodeTgp.setZnlVal(wodeTgp.getZnlVal() - 1);
		wodeTgp.setUpdateTime(GameSystemTime.getSystemMillTime());
		
		tadeTgp.setZnlVal(tadeTgp.getZnlVal() + 1);
		tadeTgp.setHdCount(tadeTgp.getHdCount() + 1);
		tadeTgp.setUpdateTime(GameSystemTime.getSystemMillTime());
		
		roleQqTgpDao.cacheUpdate(wodeTgp, wodeTgp.getUserRoleId());
		roleQqTgpDao.cacheUpdate(tadeTgp, tadeTgp.getUserRoleId());
		//通知对方获得赠送
		BusMsgSender.send2One(tadeTgp.getUserRoleId(), ClientCmdType.GET_TGP_NL_TUISONG,new Object[]{tadeTgp.getZnlVal(),keCount-tadeTgp.getHdCount()} );
		
		return new Object[]{1,wodeTgp.getZnlVal()};
	}
	
	
	public Object[] zhuan(Long userRoleId){
		RoleQqTgp tgp = getRoleQqTgp(userRoleId);
		if(tgp.getZnlVal() <= 0){
			return AppErrorCode.QQ_TGP_NO_NLVALUE;
		}
		QqTgpNengLiangPublicConfig nlConfig = getQqTgpNengLiangPublicConfig();
		if(nlConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		int zhuanXH = nlConfig.getTgpMap().get("zhuanpan");
		if(tgp.getZnlVal() < zhuanXH){
			return AppErrorCode.QQ_TGP_NO_NLVALUE;
		}
		QqTgpZhuanPanPublicConfig zpConfig = getQqTgpZhuanPanPublicConfig();
		if(zpConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//抽奖
		Map<String, Integer> jiang = Lottery.getRandomKeyByInteger(zpConfig.getTgpMap());
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(jiang, userRoleId);
		if(bagCheck != null){
			return AppErrorCode.BAG_NOEMPTY;
		}else{
			//物品进背包
			roleBagExportService.putGoodsAndNumberAttr(jiang, userRoleId, GoodsSource.GOODS_TGP_ZHUANPAN, LogPrintHandle.GET_TGP_ZNL_ZHUANPAN, LogPrintHandle.GBZ_TGP_ZNL_ZHUANPAN, true);
		}
		//消耗能量值
		tgp.setZnlVal(tgp.getZnlVal() - zhuanXH);
		tgp.setUpdateTime(GameSystemTime.getSystemMillTime());
		
		roleQqTgpDao.cacheUpdate(tgp, userRoleId);
		
		int gezi = zpConfig.getGeziMap().get(jiang);
		System.out.println(gezi);
		return new Object[]{1,tgp.getZnlVal(),gezi};
	}
	
	/**
	 * 兑换
	 * @param userRoleId
	 * @param version
	 * @param subId
	 * @param configId
	 * @param busMsgQueue
	 * @return
	 */
	public Object[] duihuan(Long userRoleId,int id){
		RoleQqTgp tgp = getRoleQqTgp(userRoleId);
		if(tgp.getZnlVal() <= 0){
			return AppErrorCode.QQ_TGP_NO_NLVALUE;
		}
		QqTgpDuiHuanPublicConfig dhConfig = getQqTgpDuiHuanPublicConfig();
		QqTgpDuiHuanXHPublicConfig xhConfig = getQqTgpDuiHuanXHPublicConfig();
		if(dhConfig == null || xhConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		//兑换要的能量
		int dhnl = xhConfig.getTgpMap().get(id+"");
		if(dhnl == 0){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(tgp.getZnlVal() < dhnl){
			return AppErrorCode.QQ_TGP_NO_NLVALUE;
		}
		Map<String, Integer> goodsMap = dhConfig.getTgpMap().get(id+"");
		//检查物品是否可以进背包
		Object[] bagCheck = roleBagExportService.checkPutInBag(goodsMap, userRoleId);
		if(bagCheck != null){
			return AppErrorCode.BAG_NOEMPTY;
		}else{
			//物品进背包
			roleBagExportService.putGoodsAndNumberAttr(goodsMap, userRoleId, GoodsSource.GOODS_TGP_DUIHUAN, LogPrintHandle.GET_TGP_ZNL_DUIHUAN, LogPrintHandle.GBZ_TGP_ZNL_DUIHUAN, true);
		}
		//消耗能量值
		tgp.setZnlVal(tgp.getZnlVal() - dhnl);
		tgp.setUpdateTime(GameSystemTime.getSystemMillTime());
		
		roleQqTgpDao.cacheUpdate(tgp, userRoleId);
		
		return new Object[]{1,tgp.getZnlVal()};
	}
	
}
