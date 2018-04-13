package com.junyou.bus.xianqi.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.qiling.export.QiLingExportService;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.tianyu.export.TianYuExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.service.RoleVipInfoService;
import com.junyou.bus.wuxing.export.WuXingMoShenExportService;
import com.junyou.bus.xianjian.export.XianJianExportService;
import com.junyou.bus.xianqi.configure.XianYuanFeiHuaConfig;
import com.junyou.bus.xianqi.configure.XianYuanFeiHuaConfigExportService;
import com.junyou.bus.xianqi.dao.RoleXianyuanFeihuaDao;
import com.junyou.bus.xianqi.entity.RoleXianyuanFeihua;
import com.junyou.bus.xianqi.export.XianqiServiceExport;
import com.junyou.bus.xinmo.export.XinmoExportService;
import com.junyou.bus.zhanjia.export.ZhanJiaExportService;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.XianYuanFeiHuaLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.RandomUtil;

/**
 * 
 * @description:仙缘飞化 
 *
 *	@author ChuBin
 *
 * @date 2016-11-14
 */
@Service
public class XianYuanFeiHuaService {
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private ChiBangExportService chiBangExportService;
	@Autowired
	private XianJianExportService xianJianExportService;
	@Autowired
	private ZhanJiaExportService zhanJiaExportService;
	@Autowired
	private QiLingExportService qiLingExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private WuXingMoShenExportService wuXingMoShenExportService;
	@Autowired
	private XianqiServiceExport xianqiServiceExport;
	@Autowired
	private XinmoExportService xinmoExportService;
	@Autowired
	private RoleXianyuanFeihuaDao roleXianyuanFeihuaDao;
	@Autowired
	private TianYuExportService tianYuExportService;
	@Autowired
	private RoleVipInfoService roleVipInfoService;
	@Autowired
	private XianYuanFeiHuaConfigExportService xianYuanFeiHuaConfigExportService;
	
	
	public Object[] startUpgrade(long userRoleId,BusMsgQueue busMsgQueue,boolean isAutoGM){
		RoleXianyuanFeihua info = getRoleXianyuanFeihua(userRoleId);
		if(info == null){
			return AppErrorCode.XYFH_NOT_OPEN;
		}
		return upgrade(info,userRoleId, busMsgQueue, isAutoGM, info.getFeihuaLevel()+1, false); 
	}
	
	
	private Object[] upgrade(RoleXianyuanFeihua info, long userRoleId, BusMsgQueue busMsgQueue, boolean isAutoGM,
			Integer targetLevel, boolean isAuto) {
		
		int feihuaLevel = info.getFeihuaLevel();
		int maxJjLevel = xianYuanFeiHuaConfigExportService.getMaxLevel();
		if(feihuaLevel >= maxJjLevel){
			return AppErrorCode.XYFH_LEVEL_MAX;
		}
		
		if(targetLevel <= feihuaLevel || targetLevel >maxJjLevel){
			return AppErrorCode.XYFH_TARGET_LEVEL_ERROR;
		}
		
		XianYuanFeiHuaConfig  config = xianYuanFeiHuaConfigExportService.loadByLevel(feihuaLevel);
		if(config ==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		//不满足升阶的前提条件
		if(!checkCondition(userRoleId, config)){
			return AppErrorCode.XYFH_CANNOT_SJ;
		}
		
		Map<String,Integer> needResource = new HashMap<String,Integer>();
		int zfzVal = getZfzValue(userRoleId);
		
		Object[] result = upgradeResult(feihuaLevel,needResource, userRoleId, true, maxJjLevel,isAutoGM,targetLevel,zfzVal,isAuto,config.getGold(),config.getBgold());
		
		//	result:{errorCode,qhlevel,zfzVal}
		Object[] erroCode=(Object[])result[0];
		if(erroCode!=null){
			return erroCode;
		}
		
		int newlevel=(Integer)result[1];
		int newZfz=(Integer)result[2];
		Integer newNeedMoney = needResource.remove(GoodsCategory.MONEY+"");
		Integer newNeedGold = needResource.remove(GoodsCategory.GOLD+"");
		Integer newNeedBgold = needResource.remove(GoodsCategory.BGOLD+"");
		
		// 扣除金币
		if(newNeedMoney != null && newNeedMoney>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, newNeedMoney, userRoleId,  LogPrintHandle.CONSUME_XIANYUAN_FEIHUA_SJ, true, LogPrintHandle.CBZ_XIANYUAN_FEIHUA_SJ);
		}
		// 扣除元宝
		if(newNeedGold != null && newNeedGold>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId, LogPrintHandle.CONSUME_XIANYUAN_FEIHUA_SJ, true,LogPrintHandle.CBZ_XIANYUAN_FEIHUA_SJ); 
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,newNeedGold,LogPrintHandle.CONSUME_XIANYUAN_FEIHUA_SJ,QQXiaoFeiType.CONSUME_XIANYUAN_FEIHUA_SJ,1});
			}
		}
		// 扣除绑定元宝
		if(newNeedBgold != null && newNeedBgold>0){
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId, LogPrintHandle.CONSUME_XIANYUAN_FEIHUA_SJ, true,LogPrintHandle.CBZ_XIANYUAN_FEIHUA_SJ); 
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,newNeedBgold,LogPrintHandle.CONSUME_XIANYUAN_FEIHUA_SJ,QQXiaoFeiType.CONSUME_XIANYUAN_FEIHUA_SJ,1});
			}
		}
		
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(needResource, userRoleId, GoodsSource.CONSUME_XIANYUAN_FEIHUA, true, true);
		
		info.setZfzVal(newZfz);
		info.setFeihuaLevel(newlevel);
		XianYuanFeiHuaConfig  newConfig = xianYuanFeiHuaConfigExportService.loadByLevel(newlevel);
		if(newZfz > 0 && (newlevel != feihuaLevel || zfzVal ==0)) {
			long zfzCdTime=0l;
			float clearTime = newConfig.getCzTime();
			if(clearTime == 0){
				zfzCdTime=0l; 
			}else{
				zfzCdTime = GameSystemTime.getSystemMillTime() +(int)(clearTime*60*60*1000);
			}
			info.setLastSjTime(zfzCdTime);
			
		}else if(newZfz == 0){
			info.setUpdateTime(new Timestamp(System.currentTimeMillis())); 
			info.setLastSjTime(0l);
		}
		
		roleXianyuanFeihuaDao.cacheUpdate(info, userRoleId);
		
		if(newlevel > feihuaLevel){
			//通知属性变化
			notifyStageChange(userRoleId, busMsgQueue);
			if(newConfig.isSendNotice()){
				UserRole userRole =	roleExportService.getUserRole(userRoleId);
				BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT3, new Object[]{
						AppErrorCode.XYFH_SJ_NOTICE, 
						new Object[]{userRole.getName(), newlevel}
				});
			}
		}
		
		// 记录操作日志
		JSONArray consumeItemArray = new JSONArray(); 
		LogFormatUtils.parseJSONArray(bagSlots,consumeItemArray);
		GamePublishEvent.publishEvent(new XianYuanFeiHuaLogEvent(LogPrintHandle.XIANYUAN_FEIHUA_LOG, userRoleId, newNeedMoney, newNeedGold, consumeItemArray, feihuaLevel, newlevel, zfzVal, newZfz));
		
		return new Object[]{1,newZfz,newlevel,info.getLastSjTime()};
	}
	
	private void notifyStageChange(long userRoleId,BusMsgQueue busMsgQueue) {
		busMsgQueue.addStageMsg(userRoleId, InnerCmdType.INNER_XIANYUAN_FEIHUA_CHANGE, getXianYuanFeiHuaAttr(userRoleId));
	}
	
	/**
	 * 通知场景里面属性变化
	 */
	private void notifyStageChange(long userRoleId){
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_XIANYUAN_FEIHUA_CHANGE, getXianYuanFeiHuaAttr(userRoleId));
	}


	/**
	 * @param curLevel
	 * @param needResource
	 * @param userRoleId
	 * @param isSendErrorCode
	 * @param maxLevel
	 * @param isAutoGM
	 * @param targetLevel
	 * @param zfzVal
	 * @param isAuto
	 * @return
	 */
	private Object[] upgradeResult(int curLevel,Map<String,Integer> needResource,long userRoleId,boolean isSendErrorCode,int maxLevel,boolean isAutoGM,int targetLevel,int zfzVal,boolean isAuto,int yb,int byb){
		
		if(curLevel >= targetLevel){
			 return new Object[]{null,curLevel};
		}
		
		XianYuanFeiHuaConfig zqConfig = xianYuanFeiHuaConfigExportService.loadByLevel(curLevel);
		if(zqConfig==null){
			Object[] errorCode=isSendErrorCode?AppErrorCode.CONFIG_ERROR:null;
			return new Object[]{errorCode,curLevel,zfzVal};
		}
		
		Map<String,Integer> tempResources = new HashMap<>();
		 
		int money = zqConfig.getMoney();
		int oldMoney = needResource.get(GoodsCategory.MONEY)== null ? 0 :needResource.get(GoodsCategory.MONEY);
		Object[] isOb=roleBagExportService.isEnought(GoodsCategory.MONEY, money+oldMoney, userRoleId);
		if(null != isOb){
			Object[] errorCode = isSendErrorCode ? isOb : null;
			return new Object[]{errorCode,curLevel,zfzVal};
		}
		tempResources.put(GoodsCategory.MONEY+"",money);
		
		List<String> needGoodsIds = xianYuanFeiHuaConfigExportService.getConsumeIds(zqConfig.getGoodsId());
		int needCount = zqConfig.getCount();
		
		for (String goodsId : needGoodsIds) {
			int oldNeedCount = needResource.get(goodsId) == null ? 0 : needResource.get(goodsId);
			
			int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
			if(owerCount >= oldNeedCount + needCount){
				tempResources.put(goodsId, needCount);
				needCount = 0;
				break;
			}
			needCount = oldNeedCount + needCount - owerCount;
			tempResources.put(goodsId, owerCount - oldNeedCount);
		}
		
		if(isAutoGM && needCount > 0){
			int bPrice = byb;//绑定元宝的价格
			int bCount = 0;
			int nowNeedBgold = 0;
			for (int i = 0; i < needCount; i++) {
				nowNeedBgold = (bCount + 1) * bPrice;
				Object[] bgoldError =  roleBagExportService.isEnought(GoodsCategory.BGOLD,nowNeedBgold, userRoleId);
				if(null != bgoldError){ 
					break;
				}
				bCount++;
			}
			nowNeedBgold = bCount * bPrice;
			tempResources.put(GoodsCategory.BGOLD+"", nowNeedBgold);
			
			needCount = needCount - bCount;
			int price = yb;
			int nowNeedGold = needCount * price;
			
			Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD,nowNeedGold, userRoleId);
			if(null != goldError){
				Object[] errorCode=isSendErrorCode?goldError:null;
				return new Object[]{errorCode,curLevel,zfzVal};
			}
			
			tempResources.put(GoodsCategory.GOLD+"", nowNeedGold);
			needCount = 0;
		}
		
		if(needCount > 0){
			Object[] errorCode = isSendErrorCode ? AppErrorCode.ITEM_NOT_ENOUGH : null;
			return new Object[]{errorCode,curLevel,zfzVal};
		}
		ObjectUtil.mapAdd(needResource, tempResources);
		
		
		boolean flag = isSJSuccess(zfzVal, zqConfig);
		if(!flag){
			zfzVal += RandomUtil.getIntRandomValue(zqConfig.getZfzMinAdd(), zqConfig.getZfzMaxAdd()+1);
		}

		//如果祝福值大于了最大值，算强化成功
		int maxzf = zqConfig.getZfzMax();
		if(flag || zfzVal >= maxzf ){
			zfzVal=0;
			++curLevel;
		}
		
		//如果不是自动,成功与否都退出
		if(!isAuto){
			return new Object[]{null,curLevel,zfzVal};
		}
		
		//成功之后达到了指定的目标等级就停止
		if(targetLevel <= curLevel ) {
			return new Object[]{null,curLevel,zfzVal};
		}
		
		return upgradeResult(curLevel, needResource, userRoleId, false, maxLevel, isAutoGM, targetLevel, zfzVal, isAuto,zqConfig.getGold(),zqConfig.getBgold());
	}
	
	public boolean isSJSuccess(int zfzValue,XianYuanFeiHuaConfig zqConfig){
		 
		int minzf = zqConfig.getZfzMin(); 
		
		if(zfzValue < minzf ){
			return false;
		}
		
		int pro = zqConfig.getSuccessRate();
		
		if(RandomUtil.getIntRandomValue(1, 101) > pro){
			return false;
		}
		return true;
	}


	private int getZfzValue(long userRoleId){
		RoleXianyuanFeihua  info = getRoleXianyuanFeihua(userRoleId);
		if(info == null){
			return 0;
		}
		XianYuanFeiHuaConfig  config = xianYuanFeiHuaConfigExportService.loadByLevel(info.getFeihuaLevel());
		int zfzValue = info.getZfzVal();
		boolean isClear = config.isResetZfzTime();
		long qiLastTime = info.getLastSjTime();
		if(qiLastTime <= GameSystemTime.getSystemMillTime() && isClear){
			return 0;
		}
		return zfzValue;
	}


	/**
	 * 
	 *@description:检查是否满足进阶条件 
	 * @param userRoleId
	 * @param config
	 * @return
	 */
	private boolean checkCondition(Long userRoleId, XianYuanFeiHuaConfig config) {
		
		
		return (config.getNeedChibang() ==0 || (chiBangExportService.getChibangLevel(userRoleId) !=null &&config.getNeedChibang() <= chiBangExportService.getChibangLevel(userRoleId) + 1))
				&& (config.getNeedLevel() ==0 || config.getNeedLevel() <= roleExportService.getLoginRole(userRoleId).getLevel())
				&& (config.getNeedQianghua() ==0 || config.getNeedQianghua() <= roleBagExportService.getAllEquipsQHLevel(userRoleId))
				&& (config.getNeedQiling()==0 || config.getNeedQiling() <= qiLingExportService.getQiLingLevel(userRoleId) + 1)
				&& (config.getNeedTiangong()  ==0 || config.getNeedTiangong() <= xianJianExportService.getXianJianLevel(userRoleId) + 1)
				&& (config.getNeedTianshang()==0 || config.getNeedTianshang() <= zhanJiaExportService.getXianJianLevel(userRoleId) + 1)
				&& (config.getNeedTianyu() ==0 || config.getNeedTianyu() <= tianYuExportService.getTianYuLevel(userRoleId) + 1)
				&& (config.getNeedVip()==0 || ( roleVipInfoService.getRoleVipInfo(userRoleId) !=null &&config.getNeedVip() <= roleVipInfoService.getRoleVipInfo(userRoleId).getVipLevel()))
				&& (config.getNeedWuxing()==0 || config.getNeedWuxing() <= wuXingMoShenExportService.getRoleWuXingTotallZplus(userRoleId))
				&& (config.getNeedXianjue() ==0 || config.getNeedXianjue() <= xianqiServiceExport.getRoleXianqiJuexingTotalZplus(userRoleId))
				&& (config.getNeedXinmo()==0 || config.getNeedXinmo() <= xinmoExportService.getRoleXinmoTotalZplus(userRoleId))
				&& (config.getNeedYujian()==0 || config.getNeedYujian() <= zuoQiExportService.getZuoQiInfoLevel(userRoleId) + 1);
	}

	public RoleXianyuanFeihua getRoleXianyuanFeihua(long userRoleId){
		RoleXianyuanFeihua info = roleXianyuanFeihuaDao.cacheLoad(userRoleId, userRoleId);
		if(info!=null){
			XianYuanFeiHuaConfig  config = xianYuanFeiHuaConfigExportService.loadByLevel(info.getFeihuaLevel());
			if(config ==null){
				return null;
			}
			boolean isClear = config.isResetZfzTime();
			long qiLastTime = info.getLastSjTime();
			if(qiLastTime <= GameSystemTime.getSystemMillTime() && isClear){
				info.setZfzVal(0);
				info.setLastSjTime(0L);
				roleXianyuanFeihuaDao.cacheUpdate(info, userRoleId);
			}
		}
		return info; 
	}
	
	public List<RoleXianyuanFeihua> getRoleXianyuanFeihuaDB(long userRoleId) {
		return roleXianyuanFeihuaDao.dbLoadRoleXianyuanFeihua(userRoleId);
	}
	
	public Map<String,Long> getXianYuanFeiHuaAttr(Long userRoleId){
		RoleXianyuanFeihua info = getRoleXianyuanFeihua(userRoleId);
		if(info !=null){
			XianYuanFeiHuaConfig  config = xianYuanFeiHuaConfigExportService.loadByLevel(info.getFeihuaLevel());
			if(config !=null && config.getAttrs() !=null){
				return config.getAttrs();
			}
		}
		
		return null;
	}


	/**
	 *@description: 
	 * @param userRoleId
	 * @return
	 */
	public Object[] getRoleXianyuanFeihuaInfo(Long userRoleId) {
		RoleXianyuanFeihua entity = getRoleXianyuanFeihua(userRoleId);
		if(entity == null){
			entity = createRoleXianyuanFeihua(userRoleId);
			roleXianyuanFeihuaDao.cacheInsert(entity, userRoleId);
			notifyStageChange(userRoleId);
		}
		
		/**
		 * s->c
		  Array[ 
		        0:int(仙缘飞化等级),
		        1:int(当前祝福值),
		        2:Number(祝福值清空时间戳),
		        3:int(玩家强化等级总和),
		        4:int(五行魔神战斗力总和),
		        5:int(心魔战斗力总和),
		        6:int(仙器觉醒战斗力总和)
		       ]
		 */
		return new Object[]{entity.getFeihuaLevel()
				,getZfzValue(userRoleId)
				,entity.getLastSjTime()
				,roleBagExportService.getAllEquipsQHLevel(userRoleId)
				,wuXingMoShenExportService.getRoleWuXingTotallZplus(userRoleId)
				,xinmoExportService.getRoleXinmoTotalZplus(userRoleId)
				,xianqiServiceExport.getRoleXianqiJuexingTotalZplus(userRoleId)};
	}


	private RoleXianyuanFeihua createRoleXianyuanFeihua(Long userRoleId) {
		RoleXianyuanFeihua entity;
		entity = new RoleXianyuanFeihua();
		long nowTime = GameSystemTime.getSystemMillTime();
		entity.setUserRoleId(userRoleId);
		entity.setFeihuaLevel(1);
		entity.setLastSjTime(0l);
		entity.setZfzVal(0);
		entity.setUpdateTime(new Timestamp(nowTime));
		return entity;
	}
	
}
