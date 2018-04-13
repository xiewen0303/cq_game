package com.junyou.bus.tianyu.service;

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
import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.shenqi.export.ShenQiExportService;
import com.junyou.bus.tianyu.TianYuConstants;
import com.junyou.bus.tianyu.configure.export.TianYuJiChuConfig;
import com.junyou.bus.tianyu.configure.export.TianYuJiChuConfigExportService;
import com.junyou.bus.tianyu.configure.export.TianYuQianNengBiaoConfig;
import com.junyou.bus.tianyu.configure.export.TianYuQianNengBiaoConfigExportService;
import com.junyou.bus.tianyu.dao.TianYuInfoDao;
import com.junyou.bus.tianyu.entity.TianYuInfo;
import com.junyou.bus.tianyu.manage.TianYu;
import com.junyou.bus.tianyu.util.TianYuUtil;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.TianYuLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.TianYuPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.lottery.RandomUtil;

/**
 *	天羽
 */
@Service
public class TianYuService implements IFightVal {

	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		TianYuInfo tianYuInfo = getTianYuInfo(userRoleId);

		if(tianYuInfo == null) {
			ChuanQiLog.debug("tianyu is null,userRoleId={}",userRoleId);
			return 0;
		}
//		if (fightPowerType == FightPowerType.MOYI_JIE) {
			int zqLevel = tianYuInfo.getTianyuLevel();
			TianYuJiChuConfig  sjConfig = tianYuJiChuConfigExportService.loadById(zqLevel);
			if(sjConfig == null){
				ChuanQiLog.debug("sjConfig is null,zqLevel={}",zqLevel);
				return 0;
			}
			return CovertObjectUtil.getZplus(sjConfig.getAttrs());
//		}else if(fightPowerType == FightPowerType.HH_MOJIAN){
//			List<Integer> huanhuaConfigList = StageHelper.getHuanhuaExportService().getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_5);
//			for(Integer e:huanhuaConfigList){
//				YuJianHuanHuaBiaoConfig config = yuJianHuanHuaBiaoConfigExportService.loadById(e);
//				if(null == config){
//					continue;
//				}
//				result += config.getAttrs().get(EffectType.zplus.name());
//			}
//			return result;
//		}

//		return 0;


	}

	@Autowired
	private TianYuInfoDao tianYuInfoDao;
	@Autowired
	private TianYuJiChuConfigExportService tianYuJiChuConfigExportService; 
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private TianYuQianNengBiaoConfigExportService  tianYuQianNengBiaoConfigExportService; 
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
		if (goodsConfig.getCategory() != GoodsCategory.TIANYU_CZD
				&& goodsConfig.getCategory() != GoodsCategory.WANNENG_CZD) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = cbUseCZD(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.TIANYU_CZ, true, true);
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
		if (goodsConfig.getCategory() != GoodsCategory.TIANYU_QND
				&& goodsConfig.getCategory() != GoodsCategory.WANNENG_QND) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = cbUseQND(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.TIANYU_QN, true, true);
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
		TianYuInfo info = getTianYuInfo(userRoleId);
		if(info == null){
			return AppErrorCode.TY_NO_OPEN;
		}
		TianYuJiChuConfig chibangConfig = tianYuJiChuConfigExportService.loadById(info.getTianyuLevel());
		if(chibangConfig.getQndopen().intValue() != TianYuConstants.QND_OPEN_FLAG){
			return AppErrorCode.TY_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if(info.getQndCount().intValue()+count > chibangConfig.getQndmax()){
			return AppErrorCode.TY_QND_MAX_NUM;
		}
		info.setQndCount(count+info.getQndCount());
		tianYuInfoDao.cacheUpdate(info, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TIANYU_QND_NUM,info.getQndCount()!=null?info.getQndCount():0);
		//通知场景里面器灵属性变化
		notifyStageTianYuChange(userRoleId);
		return null;
	}
	
	/**
	 * 使用成长丹
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] cbUseCZD(long userRoleId,int count){
		TianYuInfo	info = getTianYuInfo(userRoleId);
		if(info == null){
			return AppErrorCode.TY_NO_OPEN;
		}
		TianYuJiChuConfig chibangConfig = tianYuJiChuConfigExportService.loadById(info.getTianyuLevel());
		if(chibangConfig.getCzdopen() != TianYuConstants.CZD_OPEN_FLAG){
			return AppErrorCode.TY_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if(info.getCzdcount().intValue()+count > chibangConfig.getCzdmax()){
			return AppErrorCode.TY_CZD_MAX_NUM;
		}
		info.setCzdcount(count+info.getCzdcount());
		tianYuInfoDao.cacheUpdate(info, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TIANYU_CZD_NUM,info.getCzdcount()!=null?info.getCzdcount():0);
		//通知场景里面器灵属性变化
		notifyStageTianYuChange(userRoleId);
		return null;
	}
	
	
	/**
	 * 器灵手动升阶
	 * @param userRoleId
	 * @param busMsgQueue
	 * @param isAutoGM
	 * @return
	 */
	public Object[] tianYuSj(long userRoleId,BusMsgQueue busMsgQueue,boolean isAutoGM,boolean isAuto){
		TianYuInfo info = getTianYuInfo(userRoleId);
		if(info == null){
			return AppErrorCode.TY_NO_OPEN;
		}
		int maxConfigId = isAuto?tianYuJiChuConfigExportService.getMaxConfigId():info.getTianyuLevel()+1;
		return sj(info,userRoleId, busMsgQueue, isAutoGM, maxConfigId, isAuto); 
	}
	
	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer zfz){
		TianYuInfo info = getTianYuInfo(userRoleId);
		if(info== null){
			return AppErrorCode.TY_NO_OPEN;
		}else{
			int newlevel = info.getTianyuLevel()+1;
			if(newlevel > tianYuJiChuConfigExportService.getMaxConfigId()){
				return AppErrorCode.IS_MAX_LEVEL;//已满级
			}
			if(newlevel < minLevel){
				return AppErrorCode.TY_LEVEL_LIMIT_CAN_NOT_USE;
			}else{
				if(newlevel >= maxLevel){
					// 增加祝福值
					info.setZfzVal(info.getZfzVal()+zfz);
					TianYuJiChuConfig  yuJianJJConfig = tianYuJiChuConfigExportService.loadById(info.getTianyuLevel());
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
						info.setTianyuLevel(info.getTianyuLevel()+1);
						info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
						//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
						notifyStageTianYuChange(userRoleId);
						tianYuUpdateShow(userRoleId, info.getTianyuLevel());
						if(yuJianJJConfig.isGgopen()){
							UserRole userRole =	roleExportService.getUserRole(userRoleId);
							BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
									AppErrorCode.TY_SJ_NOTICE, 
									new Object[]{userRole.getName(), newlevel+1}
							});
						}
						//通知client变身形象
						BusMsgSender.send2One(userRoleId, ClientCmdType.TIANYU_JJ_UP, 
								new Object[]{1,info.getZfzVal(),info.getTianyuLevel(),info.getLastSjTime()});
					} else{
						//祝福值变化通知前端
						BusMsgSender.send2One(userRoleId, ClientCmdType.TIANYU_SHOW, 
								new Object[]{info.getZfzVal(),info.getShowId(),info.getLastSjTime()});
					}
					//info.setLastSjTime(0l);
					//info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
					tianYuInfoDao.cacheUpdate(info, userRoleId);
				}else{
					info.setTianyuLevel(newlevel);
					info.setZfzVal(0);
					info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
					TianYuJiChuConfig  yuJianJJConfig = tianYuJiChuConfigExportService.loadById(newlevel);
					float clearTime = yuJianJJConfig.getCztime();
					long zfzCdTime=0l;
					if(clearTime == 0 || info.getZfzVal() == 0){
						zfzCdTime=0l; 
					}else{
						zfzCdTime = GameSystemTime.getSystemMillTime() +(int)(clearTime*60*60*1000);
					}
					info.setLastSjTime(zfzCdTime);
					tianYuInfoDao.cacheUpdate(info, userRoleId);
					//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
					notifyStageTianYuChange(userRoleId);
					tianYuUpdateShow(userRoleId, newlevel);
					if(yuJianJJConfig.isGgopen()){
						UserRole userRole =	roleExportService.getUserRole(userRoleId);
						BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
								AppErrorCode.TY_SJ_NOTICE, 
								new Object[]{userRole.getName(), newlevel+1}
						});
					}
					//通知client变身形象
					BusMsgSender.send2One(userRoleId, ClientCmdType.TIANYU_JJ_UP, 
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
	private Object[] sj(TianYuInfo info, Long userRoleId, BusMsgQueue busMsgQueue ,boolean isAutoGM , int targetLevel,boolean isAuto) {
				
		int tianYuLevel = info.getTianyuLevel();
		int maxLevel = tianYuJiChuConfigExportService.getMaxConfigId();
		if(tianYuLevel >= maxLevel){
			return AppErrorCode.TY_LEVEL_MAX;
		}
		
		if(targetLevel <= tianYuLevel || targetLevel >maxLevel){
			return AppErrorCode.TY_TARGET_LEVEL_ERROR;
		}
		  
		TianYuJiChuConfig  tianyuSjConfig = tianYuJiChuConfigExportService.loadById(tianYuLevel);
	 
		if(tianyuSjConfig == null){
			return AppErrorCode.TY_CONFIG_ERROR;
		}
		
		Map<String,Integer> needResource = new HashMap<String,Integer>();
		Map<Integer,Integer> beilvRecords = new HashMap<>();
		int zfzVal = info.getZfzVal();//getZfzValue(userRoleId);
		
//		Object[] result = tianYuSj(tianYuLevel,needResource, userRoleId, true, maxJjLevel,false,targetLevel,zfzVal,isAuto,tianyuSjConfig.getGold(),tianyuSjConfig.getBgold(),beilvRecords,isAutoGM);
		Object[] result = tianYuSjOther(needResource, userRoleId, false, maxLevel, targetLevel, isAuto, beilvRecords, isAutoGM);
		
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
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, newNeedMoney, userRoleId,  LogPrintHandle.CONSUME_TIANYU_SJ, true, LogPrintHandle.CBZ_TIANYU_SJ);
		}
		// 扣除元宝
		if(newNeedGold != null && newNeedGold>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId, LogPrintHandle.CONSUME_TIANYU_SJ, true,LogPrintHandle.CBZ_TIANYU_SJ); 
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,newNeedGold,LogPrintHandle.CONSUME_TIANYU_SJ,QQXiaoFeiType.CONSUME_TIANYU_SJ,1});
			}
		}
		// 扣除绑定元宝
		if(newNeedBgold != null && newNeedBgold>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId, LogPrintHandle.CONSUME_TIANYU_SJ, true,LogPrintHandle.CBZ_TIANYU_SJ); 
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,newNeedBgold,LogPrintHandle.CONSUME_TIANYU_SJ,QQXiaoFeiType.CONSUME_TIANYU_SJ,1});
			}
		}
		
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(needResource, userRoleId, GoodsSource.TY_SJ, true, true);
		
		
		
		info.setZfzVal(newZfz);
		info.setTianyuLevel(newlevel);
		TianYuJiChuConfig  yuJianJJConfig = tianYuJiChuConfigExportService.loadById(newlevel);
		if(newZfz > 0 && (newlevel != tianYuLevel || zfzVal ==0)) {
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
			/*try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_QILING,  yuJianJJConfig.getLevel()});
				//roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_CHIBANG, yuJianJJConfig.getLevel());
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			}*/
		}
		
		tianYuInfoDao.cacheUpdate(info, userRoleId);
		
		//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
		if(newlevel > tianYuLevel){
			notifyStageTianYuChange(userRoleId);
			if(yuJianJJConfig != null && yuJianJJConfig.getLevel() != tianyuSjConfig.getLevel()){
				tianYuUpdateShow(userRoleId, yuJianJJConfig.getShowId());
				if(yuJianJJConfig.isGgopen()){
					UserRole userRole =	roleExportService.getUserRole(userRoleId);
					BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
							AppErrorCode.TY_SJ_NOTICE, 
							new Object[]{userRole.getName(), yuJianJJConfig.getLevel()}
					});
				}
			}
			
		}
		
		// 记录操作日志
		JSONArray consumeItemArray = new JSONArray(); 
		LogFormatUtils.parseJSONArray(bagSlots,consumeItemArray);
		GamePublishEvent.publishEvent(new TianYuLogEvent(LogPrintHandle.TIANYU_SJ, userRoleId, newNeedMoney, newNeedGold, consumeItemArray, tianYuLevel, newlevel, zfzVal, newZfz));
		//活跃度lxn TODO 是否记录到活跃度？？
//		HuoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A4);
		
		return new Object[]{1,getShowZfz(newlevel, newZfz),newlevel,CovertObjectUtil.object2int(beilvRecords.get(GameConstants.RATIO_2)),CovertObjectUtil.object2int(beilvRecords.get(GameConstants.RATIO_1)),newZfz - zfzVal};
	}
	
	private int getShowZfz(int configId,int newZfz){
		if(configId >0){
			TianYuJiChuConfig config = tianYuJiChuConfigExportService.loadById(configId -1);
			return newZfz - config.getRealNeedZfz();
		}
		return newZfz;
	}
	
//	/**
//	 *  天羽升阶
//	 * @param zqLevel
//	 * @param needResource
//	 * @param userRoleId
//	 * @param isSendErrorCode
//	 * @param maxLevel
//	 * @param isAutoGM
//	 * @param targetLevel
//	 * @param zfzVal
//	 * @param isAuto
//	 * @return
//	 */
//	private Object[] tianYuSj(int zqLevel,Map<String,Integer> needResource,long userRoleId,boolean isSendErrorCode,int maxLevel,boolean isAutoGM,int targetLevel,int zfzVal,boolean isAuto,int yb,int byb
//			,Map<Integer,Integer> beilvRecords,boolean isUseWND){
//		
//		if(zqLevel >= targetLevel){
//			 return new Object[]{null,zqLevel};
//		}
//		
//		TianYuJiChuConfig zqConfig = tianYuJiChuConfigExportService.loadById(zqLevel);
//		if(zqConfig==null){
//			Object[] errorCode=isSendErrorCode?AppErrorCode.TY_CONFIG_ERROR:null;
//			return new Object[]{errorCode,zqLevel,zfzVal};
//		}
//		
//		//如果祝福值大于了最大值，算强化成功
//		int maxzf = zqConfig.getRealNeedZfz();
//		if(zfzVal < maxzf ){
//			Map<String,Integer> tempResources = new HashMap<>();
//			 
//			int money = zqConfig.getMoney();
//			int oldMoney = needResource.get(GoodsCategory.MONEY)== null ? 0 :needResource.get(GoodsCategory.MONEY);
//			Object[] isOb=roleBagExportService.isEnought(GoodsCategory.MONEY, money+oldMoney, userRoleId);
//			if(null != isOb){
//				Object[] errorCode = isSendErrorCode ? isOb : null;
//				return new Object[]{errorCode,zqLevel,zfzVal};
//			}
//			tempResources.put(GoodsCategory.MONEY+"",money);
//			
//			List<String> needGoodsIds = tianYuJiChuConfigExportService.getConsumeIds(zqConfig.getConsumeItems(),isUseWND);
//			int needCount = zqConfig.getNumber();
//			
//			for (String goodsId : needGoodsIds) {
//				int oldNeedCount = needResource.get(goodsId) == null ? 0 : needResource.get(goodsId);
//				
//				int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
//				if(owerCount >= oldNeedCount + needCount){
//					tempResources.put(goodsId, needCount);
//					needCount = 0;
//					break;
//				}
//				needCount = oldNeedCount + needCount - owerCount;
//				tempResources.put(goodsId, owerCount - oldNeedCount);
//			}
//			
//			if(isAutoGM && needCount > 0){
//				int bPrice = byb;//绑定元宝的价格
//				int bCount = 0;
//				int nowNeedBgold = 0;
//				for (int i = 0; i < needCount; i++) {
//					nowNeedBgold = (bCount + 1) * bPrice;
//					Object[] bgoldError =  roleBagExportService.isEnought(GoodsCategory.BGOLD,nowNeedBgold, userRoleId);
//					if(null != bgoldError){ 
//						break;
//					}
//					bCount++;
//				}
//				nowNeedBgold = bCount * bPrice;
//				tempResources.put(GoodsCategory.BGOLD+"", nowNeedBgold);
//				
//				needCount = needCount - bCount;
//				int price = yb;
//				int nowNeedGold = needCount * price;
//				
//				Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD,nowNeedGold, userRoleId);
//				if(null != goldError){ 
//					Object[] errorCode=isSendErrorCode?goldError:null;
//					return new Object[]{errorCode,zqLevel,zfzVal};
//				}
//				
//				tempResources.put(GoodsCategory.GOLD+"", nowNeedGold);
//				needCount = 0;
//			}
//			
//			if(needCount > 0){
//				Object[] errorCode = isSendErrorCode ? AppErrorCode.TY_GOODS_BZ : null;
//				return new Object[]{errorCode,zqLevel,zfzVal};
//			}
//			ObjectUtil.mapAdd(needResource, tempResources);
//			
//			//星级
//			int[] targetVal = getAddZfzBL(zqConfig);
//			if(targetVal == null){
//				ChuanQiLog.error("tianYu beilv up weight is null,configId = "+ zqConfig.getId());
//				targetVal = new int[]{GameConstants.RATIO_3,1};
//			}
//			beilvRecords.put(targetVal[0], CovertObjectUtil.object2int(beilvRecords.get(targetVal[0]))+ 1);
//			zfzVal += RandomUtil.getIntRandomValue(zqConfig.getZfzmin2(), zqConfig.getZfzmin3()+1)* targetVal[1];
//		}
//		
//		if( zfzVal >= maxzf ){
//			++zqLevel;
//		}
//		
//		//如果不是自动,成功与否都退出
//		if(!isAuto){
//			return new Object[]{null,zqLevel,zfzVal};
//		}
//		
//		//成功之后达到了指定的目标等级就停止
//		if(targetLevel <= zqLevel ) {
//			return new Object[]{null,zqLevel,zfzVal};
//		}
//		
//		return tianYuSj(zqLevel, needResource, userRoleId, false, maxLevel, isAutoGM, targetLevel, zfzVal, isAuto,zqConfig.getGold(),zqConfig.getBgold(),beilvRecords,isUseWND);
//	}
	
	
	/**
	 *  天羽升阶
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
	private Object[] tianYuSjOther(Map<String,Integer> needResource,long userRoleId,boolean isSendErrorCode,int maxLevel,
			int targetLevel,boolean isAuto
			,Map<Integer,Integer> beilvRecords,boolean isUseWND){
		
		TianYuInfo info = getTianYuInfo(userRoleId); 
		int zqLevel = info.getTianyuLevel();
		int zfzVal = info.getZfzVal();
		
		for(int upCount=0;upCount<10000000;upCount++){
			if(zqLevel >= targetLevel){
				 return new Object[]{null,zqLevel};
			}
			
			
			TianYuJiChuConfig zqConfig = tianYuJiChuConfigExportService.loadById(zqLevel);
			if(zqConfig==null){
				Object[] errorCode=isSendErrorCode?AppErrorCode.TY_CONFIG_ERROR:null;
				return new Object[]{errorCode,zqLevel,zfzVal};
			}
			
			//如果祝福值大于了最大值，算强化成功
			int maxzf = zqConfig.getRealNeedZfz();
			if(zfzVal < maxzf ){
				Map<String,Integer> tempResources = new HashMap<>();
				 
				int money = zqConfig.getMoney();
				int oldMoney = needResource.get(GoodsCategory.MONEY)== null ? 0 :needResource.get(GoodsCategory.MONEY);
				Object[] isOb=roleBagExportService.isEnought(GoodsCategory.MONEY, money+oldMoney, userRoleId);
				if(null != isOb){
					Object[] errorCode = isSendErrorCode ? isOb : null;
					return new Object[]{errorCode,zqLevel,zfzVal};
				}
				tempResources.put(GoodsCategory.MONEY+"",money);
				
				List<String> needGoodsIds = tianYuJiChuConfigExportService.getConsumeIds(zqConfig.getConsumeItems(),isUseWND);
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
				
				if(needCount > 0){
					Object[] errorCode = isSendErrorCode ? AppErrorCode.TY_GOODS_BZ : null;
					return new Object[]{errorCode,zqLevel,zfzVal};
				}
				ObjectUtil.mapAdd(needResource, tempResources);
				
				//星级
				int[] targetVal = getAddZfzBL(zqConfig);
				if(targetVal == null){
					ChuanQiLog.error("tianYu beilv up weight is null,configId = "+ zqConfig.getId());
					targetVal = new int[]{GameConstants.RATIO_3,1};
				}
				beilvRecords.put(targetVal[0], CovertObjectUtil.object2int(beilvRecords.get(targetVal[0]))+ 1);
				zfzVal += RandomUtil.getIntRandomValue(zqConfig.getZfzmin2(), zqConfig.getZfzmin3()+1)* targetVal[1];
			}
			
			if( zfzVal >= maxzf ){
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
		}
		return new Object[]{AppErrorCode.DATA_ERROR,zqLevel,zfzVal,0};
	}
	
	
	private int[] getAddZfzBL(TianYuJiChuConfig wqConfig){
		return Lottery.getRandomKeyByInteger(wqConfig.getWeights());
	}
	
	/**
	 * 升阶
	 * @param zfzValue
	 * @param qiLastTime
	 * @param qhConfig
	 * @return
	 */
	public boolean isSJSuccess(int zfzValue,TianYuJiChuConfig zqConfig){
		 
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
	private TianYuPublicConfig getPublicConfig(){
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TIANYU);
	}

	/**
	 * 开启器灵模块
	 * @param userRoleId
	 */
	public TianYuInfo openChiBang(long userRoleId){
		//判定人物等级是否已经在配置表中获得了开启的条件 
		int beginLevel = getPublicConfig().getOpen();
		RoleWrapper roleWrapper = getRoleWrapper(userRoleId);
		if(roleWrapper.getLevel() <beginLevel){
			return null;
		}
		
		TianYuInfo qlInfo = create(userRoleId); 
		
		notifyStageTianYuChange(userRoleId);
		return qlInfo;
	}
	
	/**
	 * 通知场景里面属性变化
	 */
	private void notifyStageTianYuChange(long userRoleId){
		Object[] obj = getEquips(userRoleId);
		
		TianYuInfo info = getTianYuInfo(userRoleId);
		
		// 推送内部场景 属性变化
		BusMsgSender.send2Stage(userRoleId,InnerCmdType.INNER_TIANYU_CHANGE, new Object[]{info,obj,shenQiExportService.getActivatedShenqiNum(userRoleId),huanhuaExportService.getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_5)});
	}
	
	public Object[] getEquips(long userRoleId){ 
		List<RoleItemExport> items = roleBagExportService.getEquip(userRoleId,ContainerType.TIANYUITEM);
		return  EquipOutputWrapper.getRoleEquipAttribute(items);
	}
	
	
	private RoleWrapper getRoleWrapper(long userRoleId){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper != null){
			return roleWrapper;
		}
		return null;
	}
	
	/**
	 * @param userRoleId
	 * @return
	 */
	public TianYuInfo create(long userRoleId){
		TianYuInfo info =new TianYuInfo(); 
		info.setUserRoleId(userRoleId);
		info.setQndCount(0);
		info.setShowId(0);
		info.setTianyuLevel(0);
		info.setCzdcount(0);
		info.setZfzVal(0);
		info.setLastSjTime(0l);
		info.setIsGetOn(1);
		info.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		info.setZplus(0l);
		//加入缓存
		tianYuInfoDao.cacheInsert(info, userRoleId);
		
		return info;
	}
	
	public TianYuInfo getTianYuInfo(long userRoleId){
		TianYuInfo info = tianYuInfoDao.cacheLoad(userRoleId, userRoleId);
		if(info!=null){
			TianYuJiChuConfig  config = tianYuJiChuConfigExportService.loadById(info.getTianyuLevel());
			boolean isClear = config.isZfztime();
			long qiLastTime = info.getLastSjTime();
			if(qiLastTime <= GameSystemTime.getSystemMillTime() && isClear){
				info.setZfzVal(0);
				info.setLastSjTime(0L);
				tianYuInfoDao.cacheUpdate(info, userRoleId);
			}
		}
		return info; 
	}
	public TianYuInfo getTianYuInfoDB(long userRoleId) {
		return tianYuInfoDao.dbLoadTianYuInfo(userRoleId);
	}
	/**
	 * 获得器灵的属性
	 */
	public Map<String, Long> getTianYuAttrs(Long userRoleId,TianYu tianyu) {
		if(tianyu == null) return null;
		int zqLevel = tianyu.getTianYuLevel();
		TianYuJiChuConfig  sjConfig = tianYuJiChuConfigExportService.loadById(zqLevel);
		
		Map<String,Long> attrs = sjConfig.getAttrs(); 
		if(attrs == null){
			return null;
		}
		attrs = new HashMap<>(attrs);
		Long speed = attrs.remove(EffectType.x19.name());
		
		List<Integer> huanhuaConfigList = tianyu.getHuanhuaList();
		for(Integer e:huanhuaConfigList){
			YuJianHuanHuaBiaoConfig config = yuJianHuanHuaBiaoConfigExportService.loadById(e);
			if(null == config){
			    continue;
			}
			ObjectUtil.longMapAdd(attrs, config.getAttrs());
		}
		
		if(tianyu.getCzdCount() > 0){
			String czdId = tianYuJiChuConfigExportService.getCzdId();
			float multi = getCzdMulti(czdId)*tianyu.getCzdCount()/100f + 1f;
			ObjectUtil.longMapTimes(attrs, multi);
		}
		if(tianyu.getQndCount() > 0){
			TianYuQianNengBiaoConfig qiannengConfig = tianYuQianNengBiaoConfigExportService.getConfig();
			for(int i=0;i<tianyu.getQndCount();i++){
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
	

	
//	private int getZfzValue(long userRoleId){
//		TianYuInfo  info = getTianYuInfo(userRoleId);
//		if(info == null){
//			return 0;
//		}
//		TianYuJiChuConfig  config = tianYuJiChuConfigExportService.loadById(info.getTianyuLevel());
//		int zfzValue = info.getZfzVal();
//		boolean isClear = config.isZfztime();
//		long qiLastTime = info.getLastSjTime();
//		if(qiLastTime <= GameSystemTime.getSystemMillTime() && isClear){
//			return 0;
//		}
//		return zfzValue;
//	}
	
	public Object[] tianYuShow(Long userRoleId) {
		TianYuInfo info = getTianYuInfo(userRoleId);
		if(info == null){
			info =	openChiBang(userRoleId);
			if(info == null) {
				return AppErrorCode.CHIBANG_LEVEL_NO;
			}
		}
		return new Object[]{getShowZfz(info.getTianyuLevel(), info.getZfzVal()),info.getTianyuLevel(),info.getShowId()};
	}
	

	public TianYuInfo initTianYu(Long userRoleId) {
		return tianYuInfoDao.initTianYuInfo(userRoleId);
	}
	

	/**
	 * 切换器灵外显示
	 * @param userRoleId
	 * @param showId
	 */
	public Object[] tianYuUpdateShow(Long userRoleId, int showId) {
		TianYuInfo  info = getTianYuInfo(userRoleId);
		if(info == null) {
			return AppErrorCode.TY_NO_OPEN;
		}
		
		if(showId < info.getTianyuLevel() && showId == info.getShowId().intValue()){
			return AppErrorCode.TY_NO_SHOW;
		}
		
		info.setShowId(showId);
		tianYuInfoDao.cacheUpdate(info, userRoleId);
		
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.TIANYU_SHOW_UPDATE, showId);
		
		return new Object[]{1,showId};
	}

	/**
	 * 更新器灵战斗力
	 * @param userRoleId
	 * @param zplus
	 */
	public void updateTianYuZplus(Long userRoleId,Long zplus){
		TianYuInfo  info = getTianYuInfo(userRoleId);
		if(info != null && zplus != null){
			if(!info.getZplus().equals(zplus)){
				info.setZplus(zplus);
				tianYuInfoDao.cacheUpdate(info, userRoleId);
			}
		}
	}
	
	
	public Map<String,Long> getTianYuAttr(Long userRoleId){
		TianYuInfo qlInfo = getTianYuInfo(userRoleId);
		if(qlInfo==null){
			return null;
		}
		List<Integer> huanhuanConfigList = huanhuaExportService.getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_5);
		Object[] zqEquips = getEquips(userRoleId);
		TianYu chibang = TianYuUtil.coverToTianYu(qlInfo,huanhuanConfigList, zqEquips);
		return getTianYuAttrs(userRoleId, chibang);
	}
	
	public void onlineHandle(Long userRoleId){
		TianYuInfo qlInfo = getTianYuInfo(userRoleId);
		if(qlInfo == null){
			return;
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.TIANYU_QND_NUM,qlInfo.getQndCount()!=null?qlInfo.getQndCount():0);
		BusMsgSender.send2One(userRoleId, ClientCmdType.TIANYU_CZD_NUM,qlInfo.getCzdcount()!=null?qlInfo.getCzdcount():0);
	}
	public int getSkillMaxCount(Long userRoleId){
		TianYuInfo qlInfo = getTianYuInfo(userRoleId);
		if(qlInfo==null){
			return 0;
		}
		TianYuJiChuConfig config = tianYuJiChuConfigExportService.loadById(qlInfo.getTianyuLevel());
		if(config == null){
			return 0;
		}
		return config.getJinengge();
	}
}