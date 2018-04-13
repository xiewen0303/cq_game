package com.junyou.bus.qiling.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.equip.EquipOutputWrapper;
import com.junyou.bus.huanhua.configure.YuJianHuanHuaBiaoConfig;
import com.junyou.bus.huanhua.configure.YuJianHuanHuaBiaoConfigExportService;
import com.junyou.bus.huanhua.constants.HuanhuaConstants;
import com.junyou.bus.huanhua.service.export.HuanhuaExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.qiling.QilingConstants;
import com.junyou.bus.qiling.configure.export.QiLingJiChuConfig;
import com.junyou.bus.qiling.configure.export.QiLingJiChuConfigExportService;
import com.junyou.bus.qiling.configure.export.QiLingQianNengBiaoConfig;
import com.junyou.bus.qiling.configure.export.QiLingQianNengBiaoConfigExportService;
import com.junyou.bus.qiling.dao.QiLingInfoDao;
import com.junyou.bus.qiling.entity.QiLingInfo;
import com.junyou.bus.qiling.manage.QiLing;
import com.junyou.bus.qiling.util.QiLingUtil;
import com.junyou.bus.qiling.vo.QiLingRankVo;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.shenqi.export.ShenQiExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.QiLingLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.QiLingPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.RandomUtil;

/**
 *	器灵
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午3:38:12
 */

@Service
public class QiLingService{
	
	@Autowired
	private QiLingInfoDao qiLingInfoDao;
	@Autowired
	private QiLingJiChuConfigExportService qiLingJiChuConfigExportService; 
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private QiLingQianNengBiaoConfigExportService  qiLingQianNengBiaoConfigExportService; 
	@Autowired
	private ShenQiExportService shenQiExportService;
	@Autowired
	private HuanhuaExportService huanhuaExportService;
	@Autowired
	private YuJianHuanHuaBiaoConfigExportService yuJianHuanHuaBiaoConfigExportService;
	
	public Object[] useCzdNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService
				.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService
				.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.QILING_CZD
				&& goodsConfig.getCategory() != GoodsCategory.WANNENG_CZD) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = cbUseCZD(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.QILING_CZ, true, true);
		}
		return ret;
	}
	public Object[] useQndNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService
				.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService
				.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.QILING_QND
				&& goodsConfig.getCategory() != GoodsCategory.WANNENG_QND) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = cbUseQND(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.QILING_QN, true, true);
		}
		return ret;
	}
	/**
	 * 使用潜能丹
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] cbUseQND(long userRoleId,int count){
		QiLingInfo info = getQiLingInfo(userRoleId);
		if(info == null){
			return AppErrorCode.QL_NO_OPEN;
		}
		QiLingJiChuConfig chibangConfig = qiLingJiChuConfigExportService.loadById(info.getQilingLevel());
		if(chibangConfig.getQndopen().intValue() != QilingConstants.QND_OPEN_FLAG){
			return AppErrorCode.QL_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if(info.getQndCount().intValue()+count > chibangConfig.getQndmax()){
			return AppErrorCode.QL_QND_MAX_NUM;
		}
		info.setQndCount(count+info.getQndCount());
		qiLingInfoDao.cacheUpdate(info, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_QND_NUM,info.getQndCount()!=null?info.getQndCount():0);
		//通知场景里面器灵属性变化
		notifyStageQiLingChange(userRoleId);
		return null;
	}
	
	/**
	 * 使用成长丹
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] cbUseCZD(long userRoleId,int count){
		QiLingInfo	info = getQiLingInfo(userRoleId);
		if(info == null){
			return AppErrorCode.QL_NO_OPEN;
		}
		QiLingJiChuConfig chibangConfig = qiLingJiChuConfigExportService.loadById(info.getQilingLevel());
		if(chibangConfig.getCzdopen() != QilingConstants.CZD_OPEN_FLAG){
			return AppErrorCode.QL_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if(info.getCzdcount().intValue()+count > chibangConfig.getCzdmax()){
			return AppErrorCode.QL_CZD_MAX_NUM;
		}
		info.setCzdcount(count+info.getCzdcount());
		qiLingInfoDao.cacheUpdate(info, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_CZD_NUM,info.getCzdcount()!=null?info.getCzdcount():0);
		//通知场景里面器灵属性变化
		notifyStageQiLingChange(userRoleId);
		return null;
	}
	
	
	/**
	 * 器灵手动升阶
	 * @param userRoleId
	 * @param busMsgQueue
	 * @param isAutoGM
	 * @return
	 */
	public Object[] qiLingSj(long userRoleId,BusMsgQueue busMsgQueue,boolean isAutoGM){
		QiLingInfo info = getQiLingInfo(userRoleId);
		if(info == null){
			return AppErrorCode.QL_NO_OPEN;
		}
		return sj(info,userRoleId, busMsgQueue, isAutoGM, info.getQilingLevel() + 1, false); 
	}
	
	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer zfz){
		QiLingInfo info = getQiLingInfo(userRoleId);
		if(info== null){
			return AppErrorCode.QL_NO_OPEN;
		}else{
			int newlevel = info.getQilingLevel()+1;
			if(newlevel > qiLingJiChuConfigExportService.getMaxJjLevel()){
				return AppErrorCode.IS_MAX_LEVEL;//已满级
			}
			if(newlevel < minLevel){
				return AppErrorCode.QL_LEVEL_LIMIT_CAN_NOT_USE;
			}else{
				if(newlevel >= maxLevel){
					// 增加祝福值
					info.setZfzVal(info.getZfzVal()+zfz);
					QiLingJiChuConfig  yuJianJJConfig = qiLingJiChuConfigExportService.loadById(info.getQilingLevel());
					if(info.getLastSjTime() == 0  &&  yuJianJJConfig.getCztime() != 0){
						float clearTime = yuJianJJConfig.getCztime();
						long lastTime = GameSystemTime.getSystemMillTime() +(int)(clearTime*60*60*1000);
						info.setLastSjTime(lastTime);
					}
					//如果祝福值大于了最大值，算强化成功
					int maxzf = yuJianJJConfig.getZfzmax();
					int minzf = yuJianJJConfig.getZfzmin();
					if(info.getZfzVal() > minzf && info.getZfzVal() >= maxzf ){
						info.setZfzVal(0);
						info.setLastSjTime(0l);
						info.setQilingLevel(info.getQilingLevel()+1);
						info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
						//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
						notifyStageQiLingChange(userRoleId);
						qiLingUpdateShow(userRoleId, info.getQilingLevel());
						if(yuJianJJConfig.isGgopen()){
							UserRole userRole =	roleExportService.getUserRole(userRoleId);
							BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
									AppErrorCode.QL_SJ_NOTICE, 
									new Object[]{userRole.getName(), newlevel+1}
							});
						}
						//通知client变身形象
						BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_JJ_UP, 
								new Object[]{1,info.getZfzVal(),info.getQilingLevel(),info.getLastSjTime()});
					} else{
						//祝福值变化通知前端
						BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_SHOW, 
								new Object[]{info.getZfzVal(),info.getShowId(),info.getLastSjTime()});
					}
					//info.setLastSjTime(0l);
					//info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
					qiLingInfoDao.cacheUpdate(info, userRoleId);
				}else{
					info.setQilingLevel(newlevel);
					info.setZfzVal(0);
					info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
					QiLingJiChuConfig  yuJianJJConfig = qiLingJiChuConfigExportService.loadById(newlevel);
					float clearTime = yuJianJJConfig.getCztime();
					long zfzCdTime=0l;
					if(clearTime == 0 || info.getZfzVal() == 0){
						zfzCdTime=0l; 
					}else{
						zfzCdTime = GameSystemTime.getSystemMillTime() +(int)(clearTime*60*60*1000);
					}
					info.setLastSjTime(zfzCdTime);
					qiLingInfoDao.cacheUpdate(info, userRoleId);
					//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
					notifyStageQiLingChange(userRoleId);
					qiLingUpdateShow(userRoleId, newlevel);
					if(yuJianJJConfig.isGgopen()){
						UserRole userRole =	roleExportService.getUserRole(userRoleId);
						BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
								AppErrorCode.QL_SJ_NOTICE, 
								new Object[]{userRole.getName(), newlevel+1}
						});
					}
					//通知client变身形象
					BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_JJ_UP, 
							new Object[]{1,info.getZfzVal(),newlevel,info.getLastSjTime()});
				}
			}
		}
		return null;
	}
	 
	
	/**
	 * 进阶
	 * @param userRoleId
	 * @return
	 */
	private Object[] sj(QiLingInfo info, Long userRoleId, BusMsgQueue busMsgQueue ,boolean isAutoGM , int targetLevel,boolean isAuto) {
				
		int qilingLevel = info.getQilingLevel();
		int maxJjLevel = qiLingJiChuConfigExportService.getMaxJjLevel();
		if(qilingLevel >= maxJjLevel){
			return AppErrorCode.QL_LEVEL_MAX;
		}
		
		if(targetLevel <= qilingLevel || targetLevel >maxJjLevel){
			return AppErrorCode.QL_TARGET_LEVEL_ERROR;
		}
		  
		QiLingJiChuConfig  qilingSjConfig = qiLingJiChuConfigExportService.loadById(qilingLevel);
	 
		if(qilingSjConfig == null){
			return AppErrorCode.QL_CONFIG_ERROR;
		}
		
		Map<String,Integer> needResource = new HashMap<String,Integer>();
		 
		int zfzVal = getZfzValue(userRoleId);
		
		Object[] result = qiLingSj(qilingLevel,needResource, userRoleId, true, maxJjLevel,isAutoGM,targetLevel,zfzVal,isAuto,qilingSjConfig.getGold(),qilingSjConfig.getBgold());
		
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
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, newNeedMoney, userRoleId,  LogPrintHandle.CONSUME_QILING_SJ, true, LogPrintHandle.CBZ_QILING_SJ);
		}
		// 扣除元宝
		if(newNeedGold != null && newNeedGold>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId, LogPrintHandle.CONSUME_QILING_SJ, true,LogPrintHandle.CBZ_QILING_SJ); 
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,newNeedGold,LogPrintHandle.CONSUME_QILING_SJ,QQXiaoFeiType.CONSUME_QILING_SJ,1});
			}
		}
		// 扣除绑定元宝
		if(newNeedBgold != null && newNeedBgold>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId, LogPrintHandle.CONSUME_QILING_SJ, true,LogPrintHandle.CBZ_QILING_SJ); 
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,newNeedBgold,LogPrintHandle.CONSUME_QILING_SJ,QQXiaoFeiType.CONSUME_QILING_SJ,1});
			}
		}
		
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(needResource, userRoleId, GoodsSource.QL_SJ, true, true);
		
		
		
		info.setZfzVal(newZfz);
		info.setQilingLevel(newlevel);
		QiLingJiChuConfig  yuJianJJConfig = qiLingJiChuConfigExportService.loadById(newlevel);
		if(newZfz > 0 && (newlevel != qilingLevel || zfzVal ==0)) {
			long zfzCdTime=0l;
			float clearTime = yuJianJJConfig.getCztime();
			if(clearTime == 0){
				zfzCdTime=0l; 
			}else{
				zfzCdTime = GameSystemTime.getSystemMillTime() +(int)(clearTime*60*60*1000);
			}
			info.setLastSjTime(zfzCdTime);
			
		}else if(newZfz == 0){
			info.setUpdateTime(new Timestamp(System.currentTimeMillis())); 
			info.setLastSjTime(0l);
			//成就推送
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_QILING,  yuJianJJConfig.getLevel()});
				//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_CHIBANG, yuJianJJConfig.getLevel());
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			}
		}
		
		qiLingInfoDao.cacheUpdate(info, userRoleId);
		
		//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
		if(newlevel > qilingLevel){
			notifyStageQiLingChange(userRoleId);
			qiLingUpdateShow(userRoleId, newlevel);
			if(yuJianJJConfig.isGgopen()){
				UserRole userRole =	roleExportService.getUserRole(userRoleId);
				BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
						AppErrorCode.QL_SJ_NOTICE, 
						new Object[]{userRole.getName(), newlevel+1}
				});
			}
		}
		
		// 记录操作日志
		JSONArray consumeItemArray = new JSONArray(); 
		LogFormatUtils.parseJSONArray(bagSlots,consumeItemArray);
		GamePublishEvent.publishEvent(new QiLingLogEvent(LogPrintHandle.QILING_SJ, userRoleId, newNeedMoney, newNeedGold, consumeItemArray, qilingLevel, newlevel, zfzVal, newZfz));
		//活跃度lxn TODO 是否记录到活跃度？？
//		HuoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A4);
		
		return new Object[]{1,newZfz,newlevel,info.getLastSjTime()};
	}
	
	/**
	 *  器灵升阶
	 * @param zqLevel
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
	private Object[] qiLingSj(int zqLevel,Map<String,Integer> needResource,long userRoleId,boolean isSendErrorCode,int maxLevel,boolean isAutoGM,int targetLevel,int zfzVal,boolean isAuto,int yb,int byb){
		
		if(zqLevel >= targetLevel){
			 return new Object[]{null,zqLevel};
		}
		
		QiLingJiChuConfig zqConfig = qiLingJiChuConfigExportService.loadById(zqLevel);
		if(zqConfig==null){
			Object[] errorCode=isSendErrorCode?AppErrorCode.QL_CONFIG_ERROR:null;
			return new Object[]{errorCode,zqLevel,zfzVal};
		}
		
		Map<String,Integer> tempResources = new HashMap<>();
		 
		int money = zqConfig.getMoney();
		int oldMoney = needResource.get(GoodsCategory.MONEY)== null ? 0 :needResource.get(GoodsCategory.MONEY);
		Object[] isOb=roleBagExportService.isEnought(GoodsCategory.MONEY, money+oldMoney, userRoleId);
		if(null != isOb){
			Object[] errorCode = isSendErrorCode ? isOb : null;
			return new Object[]{errorCode,zqLevel,zfzVal};
		}
		tempResources.put(GoodsCategory.MONEY+"",money);
		
		List<String> needGoodsIds = qiLingJiChuConfigExportService.getConsumeIds(zqConfig.getConsumeItems());
		int needCount = zqConfig.getNumber();
		
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
				return new Object[]{errorCode,zqLevel,zfzVal};
			}
			
			tempResources.put(GoodsCategory.GOLD+"", nowNeedGold);
			needCount = 0;
		}
		
		if(needCount > 0){
			Object[] errorCode = isSendErrorCode ? AppErrorCode.QL_GOODS_BZ : null;
			return new Object[]{errorCode,zqLevel,zfzVal};
		}
		ObjectUtil.mapAdd(needResource, tempResources);
		
		
		boolean flag = isSJSuccess(zfzVal, zqConfig);
		if(!flag){
			zfzVal += RandomUtil.getIntRandomValue(zqConfig.getZfzmin2(), zqConfig.getZfzmin3()+1);
		}

		//如果祝福值大于了最大值，算强化成功
		int maxzf = zqConfig.getZfzmax();
		if(flag || zfzVal >= maxzf ){
			zfzVal=0;
			++zqLevel;
		}
		
		//如果不是自动,成功与否都退出
		if(!isAuto){
			return new Object[]{null,zqLevel,zfzVal};
		}
		
		//成功之后达到了指定的目标等级就停止
		if(targetLevel <= zqLevel ) {
			return new Object[]{null,zqLevel,zfzVal};
		}
		
		return qiLingSj(zqLevel, needResource, userRoleId, false, maxLevel, isAutoGM, targetLevel, zfzVal, isAuto,zqConfig.getGold(),zqConfig.getBgold());
	}
	
	
	/**
	 * 升阶
	 * @param zfzValue
	 * @param qiLastTime
	 * @param qhConfig
	 * @return
	 */
	public boolean isSJSuccess(int zfzValue,QiLingJiChuConfig zqConfig){
		 
		int minzf = zqConfig.getZfzmin(); 
		
		if(zfzValue < minzf ){
			return false;
		}
		
		int pro = zqConfig.getPro();
		
		if(RandomUtil.getIntRandomValue(1, 101) > pro){
			return false;
		}
		return true;
	}
	 
	/**
	 * 获取器灵公共配置
	 * @return
	 */
	private QiLingPublicConfig getPublicConfig(){
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_QILING);
	}

	/**
	 * 开启器灵模块
	 * @param userRoleId
	 */
	public QiLingInfo openChiBang(long userRoleId){
		//判定人物等级是否已经在配置表中获得了开启的条件 
		int beginLevel = getPublicConfig().getOpen();
		RoleWrapper roleWrapper = getRoleWrapper(userRoleId);
		if(roleWrapper.getLevel() <beginLevel){
			return null;
		}
		
		QiLingInfo qlInfo = create(userRoleId); 
		
		notifyStageQiLingChange(userRoleId);
		return qlInfo;
	}
	
	/**
	 * 通知场景里面属性变化
	 */
	private void notifyStageQiLingChange(long userRoleId){
		Object[] obj = getEquips(userRoleId);
		
		QiLingInfo info = getQiLingInfo(userRoleId);
		
		// 推送内部场景 属性变化
		BusMsgSender.send2Stage(userRoleId,InnerCmdType.INNER_QILING_CHANGE, new Object[]{info,obj,shenQiExportService.getActivatedShenqiNum(userRoleId),huanhuaExportService.getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_6)});
	}
	
	public Object[] getEquips(long userRoleId){ 
		List<RoleItemExport> items = roleBagExportService.getEquip(userRoleId,ContainerType.QILINGITEM);
		return  EquipOutputWrapper.getRoleEquipAttribute(items);
	}
	
	
	private RoleWrapper getRoleWrapper(long userRoleId){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper != null){
			return roleWrapper;
		}
		return null;
	}
	
	public QiLingInfo create(long userRoleId){
		QiLingInfo info =new QiLingInfo(); 
		info.setUserRoleId(userRoleId);
		info.setQndCount(0);
		info.setShowId(0);
		info.setQilingLevel(0);
		info.setCzdcount(0);
		info.setZfzVal(0);
		info.setLastSjTime(0l);
		info.setIsGetOn(1);
		info.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		//加入缓存
		qiLingInfoDao.cacheInsert(info, userRoleId);
		
		return info;
	}
	
	public QiLingInfo getQiLingInfo(long userRoleId){
		QiLingInfo info = qiLingInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info!=null){
			QiLingJiChuConfig  config = qiLingJiChuConfigExportService.loadById(info.getQilingLevel());
			boolean isClear = config.isZfztime();
			long qiLastTime = info.getLastSjTime();
			if(qiLastTime <= GameSystemTime.getSystemMillTime() && isClear){
				info.setZfzVal(0);
				info.setLastSjTime(0L);
				qiLingInfoDao.cacheUpdate(info, userRoleId);
			}
		}
		return info; 
	}
	public QiLingInfo getQiLingInfoDB(long userRoleId) {
		return qiLingInfoDao.dbLoadQiLingInfo(userRoleId);
	}
	/**
	 * 获得器灵的属性
	 */
	public Map<String, Long> getQiLingAttrs(Long userRoleId,QiLing qiling) {
		if(qiling == null) return null;
		int zqLevel = qiling.getQiLingLevel();
		QiLingJiChuConfig  sjConfig = qiLingJiChuConfigExportService.loadById(zqLevel);
		
		Map<String,Long> attrs = sjConfig.getAttrs(); 
		if(attrs == null){
			return null;
		}
		attrs = new HashMap<>(attrs);
		Long speed = attrs.remove(EffectType.x19.name());
		
		List<Integer> huanhuaConfigList = qiling.getHuanhuaList();
		for(Integer e:huanhuaConfigList){
			YuJianHuanHuaBiaoConfig config = yuJianHuanHuaBiaoConfigExportService.loadById(e);
			if(null == config){
                continue;
            }
			ObjectUtil.longMapAdd(attrs, config.getAttrs());
		}
		
		if(qiling.getCzdCount() > 0){
			String czdId = qiLingJiChuConfigExportService.getCzdId();
			float multi = getCzdMulti(czdId)*qiling.getCzdCount()/100f + 1f;
			ObjectUtil.longMapTimes(attrs, multi);
		}
		if(qiling.getQndCount() > 0){
			QiLingQianNengBiaoConfig qiannengConfig = qiLingQianNengBiaoConfigExportService.getConfig();
			for(int i=0;i<qiling.getQndCount();i++){
				ObjectUtil.longMapAdd(attrs, qiannengConfig.getAttrs());
			}
		}
		if(speed != null ){
			attrs.put(EffectType.x19.name(), speed);
		}
		return attrs;
	}
	private int multi = 0;
	public int getCzdMulti(String czdId){
		if(multi == 0){
			List<String> goodsIds = goodsConfigExportService.loadIdsById1(czdId);
			if(goodsIds.size() > 0){
				GoodsConfig config = goodsConfigExportService.loadById(goodsIds.get(0));
				multi =  config.getData1();
			}
		}
		return multi;
	}
	

//	/**
//	 * 获得坐骑移动速度属性
//	 * @param zuoqi
//	 * @return
//	 */
//	public Map<String, Integer> getZuoQiSeedAttr(ChiBang zuoqi) {
//		if(zuoqi == null) return null;
//		
//		QiLingJiChuConfig  config = QiLingJiChuConfigExportService.loadById(zuoqi.getZuoQiLevel());
//		if(config == null ) return null;
//		Map<String,Integer> result = new HashMap<>();
//		result.put(EffectType.x20.name(), config.getMoveAttrVal());
//		return result;
//	}
	
	/**
	 * 器灵换装备  TODO
	 * @param userRoleId
	 * @param guid
	 * @param targetSlot
	 * @param containerType
	 * @return
	 */
//	public Object[] chiBangChangeEquip(Long userRoleId, long guid,int targetSlot, int containerType) {
//		//如是脱下 检查身上是否有该装备
//		RoleItemExport roleItemExport = roleBagExportService.getZuoQiEquip(userRoleId, guid);
//		if(targetSlot>0 && roleItemExport == null){
//			return AppErrorCode.BODY_NO_ITEM;
//		}
//		
//		BagSlots bagSlots=roleBagExportService.moveSlot(guid, targetSlot,containerType, userRoleId);
//		if(!bagSlots.isSuccee()){
//			return bagSlots.getErrorCode();
//		}
//		
//		//通知场景
//		notifyStageChiBangChange(userRoleId);
//		
//		Object[] result=new Object[3];
//		result[0]=1;
//		List<RoleItemOperation> roleItemVos=bagSlots.getRoleItemVos();
//		for (int i=0;i<roleItemVos.size();i++) {
//			result[i+1]=BagOutputWrapper.getOutWrapperData(OutputType.MOVESLOT, roleItemVos.get(i));
//		}
//		return result; 
//	}
	
	private int getZfzValue(long userRoleId){
		QiLingInfo  info = getQiLingInfo(userRoleId);
		if(info == null){
			return 0;
		}
		QiLingJiChuConfig  config = qiLingJiChuConfigExportService.loadById(info.getQilingLevel());
		int zfzValue = info.getZfzVal();
		boolean isClear = config.isZfztime();
		long qiLastTime = info.getLastSjTime();
		if(qiLastTime <= GameSystemTime.getSystemMillTime() && isClear){
			return 0;
		}
		return zfzValue;
	}
	
	public Object[] qiLingShow(Long userRoleId) {
		QiLingInfo info = getQiLingInfo(userRoleId);
		if(info == null){
			info =	openChiBang(userRoleId);
			if(info == null) {
				return AppErrorCode.CHIBANG_LEVEL_NO;
			}
		}
		
		return new Object[]{getZfzValue(userRoleId),info.getShowId(),info.getLastSjTime()};
	}

	public QiLingInfo initQiLing(Long userRoleId) {
		return qiLingInfoDao.initQiLingInfo(userRoleId);
	}
	

	/**
	 * 切换器灵外显示
	 * @param userRoleId
	 * @param showId
	 */
	public Object[] qiLingUpdateShow(Long userRoleId, int showId) {
		QiLingInfo  info = getQiLingInfo(userRoleId);
		if(info == null) {
			return AppErrorCode.QL_NO_OPEN;
		}
		
		if(showId < info.getQilingLevel() && showId == info.getShowId().intValue()){
			return AppErrorCode.QL_NO_SHOW;
		}
		
		info.setShowId(showId);
		qiLingInfoDao.cacheUpdate(info, userRoleId);
		
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.QILING_SHOW_UPDATE, showId);
		
		return new Object[]{1,showId};
	}

	/**
	 * 更新器灵战斗力
	 * @param userRoleId
	 * @param zplus
	 */
	public void updateQiLingZplus(Long userRoleId,Long zplus){
		QiLingInfo  info = getQiLingInfo(userRoleId);
		if(info != null && zplus != null){
			if(!info.getZplus().equals(zplus)){
				info.setZplus(zplus);
				qiLingInfoDao.cacheUpdate(info, userRoleId);
			}
		}
	}
	
	
	public List<QiLingRankVo> getChiBangRankVo(int limit) {
		return qiLingInfoDao.getQiLingRankVo(limit);
	}
	
	public Map<String,Long> getQiLingAttr(Long userRoleId){
		QiLingInfo qlInfo = getQiLingInfo(userRoleId);
		if(qlInfo==null){
			return null;
		}
		List<Integer> huanhuanConfigList = huanhuaExportService.getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_6);
		Object[] zqEquips = getEquips(userRoleId);
		QiLing chibang = QiLingUtil.coverToQiLing(qlInfo,huanhuanConfigList, zqEquips);
		return getQiLingAttrs(userRoleId, chibang);
	}
	
	public void onlineHandle(Long userRoleId){
		QiLingInfo qlInfo = getQiLingInfo(userRoleId);
		if(qlInfo == null){
			return;
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_QND_NUM,qlInfo.getQndCount()!=null?qlInfo.getQndCount():0);
		BusMsgSender.send2One(userRoleId, ClientCmdType.QILING_CZD_NUM,qlInfo.getCzdcount()!=null?qlInfo.getCzdcount():0);
	}
	public int getSkillMaxCount(Long userRoleId){
		QiLingInfo qlInfo = getQiLingInfo(userRoleId);
		if(qlInfo==null){
			return 0;
		}
		QiLingJiChuConfig config = qiLingJiChuConfigExportService.loadById(qlInfo.getQilingLevel());
		if(config == null){
			return 0;
		}
		return config.getJinengge();
	}
}