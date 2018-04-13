package com.junyou.bus.xianjian.service;

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
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rfbactivity.service.ActivityServiceFacotry;
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.shenqi.export.ShenQiExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wuqi.configure.export.XinShengJianJiChuConfig;
import com.junyou.bus.xianjian.XianjianConstants;
import com.junyou.bus.xianjian.configure.export.XianJianJiChuConfig;
import com.junyou.bus.xianjian.configure.export.XianJianJiChuConfigExportService;
import com.junyou.bus.xianjian.configure.export.XianJianQianNengBiaoConfig;
import com.junyou.bus.xianjian.configure.export.XianJianQianNengBiaoConfigExportService;
import com.junyou.bus.xianjian.dao.XianJianInfoDao;
import com.junyou.bus.xianjian.entity.XianJianInfo;
import com.junyou.bus.xianjian.manage.XianJian;
import com.junyou.bus.xianjian.util.XianjianUtil;
import com.junyou.bus.xianjian.vo.XianJianRankVo;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.XianJianLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.XianJianPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.utils.StageHelper;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.lottery.RandomUtil;

/**
 * 宠物的剑
 */
@Service
public class XianjianService implements IFightVal {

	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		long result = 0;
		XianJianInfo xianJianInfo = getXianJianInfo(userRoleId);
		if(xianJianInfo == null){
			ChuanQiLog.error("xianjianInfo is null userRoleId={}",userRoleId);
			return 0;
		}

		if(fightPowerType ==  FightPowerType.MOJIAN_JIE) {
			int xianjianLevel = xianJianInfo.getXianjianLevel();
			XianJianJiChuConfig sjConfig = xianJianJiChuConfigExportService.loadById(xianjianLevel);

			Map<String, Long> attrs = sjConfig.getAttrs();
			if (attrs == null) {
				ChuanQiLog.error("xianjianInfo zplus config is null");
				return 0;
			}
			return CovertObjectUtil.getZplus(attrs);
		}else if(fightPowerType == FightPowerType.HH_MOJIAN){
			List<Integer> huanhuaConfigList = StageHelper.getHuanhuaExportService().getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_3);
			for(Integer e:huanhuaConfigList){
				YuJianHuanHuaBiaoConfig config = yuJianHuanHuaBiaoConfigExportService.loadById(e);
				if(null == config){
					continue;
				}
				result += CovertObjectUtil.getZplus(config.getAttrs());
			}
		}else{
			ChuanQiLog.error("type is not exist!type={}",fightPowerType);
		}

		return result;
	}

	@Autowired
	private XianJianInfoDao xianJianInfoDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private XianJianJiChuConfigExportService xianJianJiChuConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private XianJianQianNengBiaoConfigExportService xianJianQianNengBiaoConfigExportService;

	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
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
		if (goodsConfig.getCategory() != GoodsCategory.XIANJIAN_CZD
				&& goodsConfig.getCategory() != GoodsCategory.WANNENG_CZD) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = xjUseCZD(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.XIANJIAN_CZ, true, true);
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
		if (goodsConfig.getCategory() != GoodsCategory.XIANJIAN_QND
				&& goodsConfig.getCategory() != GoodsCategory.WANNENG_QND) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = xjUseQND(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.XIANJIAN_QN, true, true);
		}
		return ret;
	}

	/**
	 * 使用潜能丹
	 * 
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] xjUseQND(long userRoleId, int count) {
		XianJianInfo info = getXianJianInfo(userRoleId);
		if (info == null) {
			return AppErrorCode.XJ_NO_OPEN;
		}
		XianJianJiChuConfig xianjianConfig = xianJianJiChuConfigExportService
				.loadById(info.getXianjianLevel());
		if (xianjianConfig.getQndopen().intValue() != XianjianConstants.QND_OPEN_FLAG) {
			return AppErrorCode.XJ_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if (info.getQndCount().intValue() + count > xianjianConfig.getQndmax()) {
			return AppErrorCode.XJ_QND_MAX_NUM;
		}
		info.setQndCount(count + info.getQndCount());
		xianJianInfoDao.cacheUpdate(info, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_QND_NUM,info.getQndCount()!=null?info.getQndCount():0);
		notifyStageXianJianChange(userRoleId);
		return null;
	}

	/**
	 * 使用成长丹
	 * 
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] xjUseCZD(long userRoleId, int count) {
		XianJianInfo info = getXianJianInfo(userRoleId);
		if (info == null) {
			return AppErrorCode.XJ_NO_OPEN;
		}
		XianJianJiChuConfig xianjianConfig = xianJianJiChuConfigExportService
				.loadById(info.getXianjianLevel());
		if (xianjianConfig.getCzdopen() != XianjianConstants.CZD_OPEN_FLAG) {
			return AppErrorCode.XJ_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if (info.getCzdcount().intValue() + count > xianjianConfig.getCzdmax()) {
			return AppErrorCode.XJ_CZD_MAX_NUM;
		}
		info.setCzdcount(count + info.getCzdcount());
		xianJianInfoDao.cacheUpdate(info, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_CZD_NUM,info.getCzdcount() != null?info.getCzdcount() :0);
		// 通知场景里面 属性变化
		notifyStageXianJianChange(userRoleId);

		return null;
	}

	/**
	 * 手动升阶
	 * 
	 * @param userRoleId
	 * @param busMsgQueue
	 * @param isAutoGM
	 * @return
	 */
	public Object[] xianJianSj(long userRoleId, BusMsgQueue busMsgQueue,
			boolean isAutoGM,boolean isAuto) {
		XianJianInfo info = getXianJianInfo(userRoleId);
		if (info == null) {
			return AppErrorCode.XJ_NO_OPEN;
		}
		int targetLevel = isAuto?xianJianJiChuConfigExportService.getMaxConfigId():info.getXianjianLevel()+1;
		return sj(info, userRoleId, busMsgQueue, isAutoGM,targetLevel, isAuto);
	}
	

	/**
	 * 直接消耗道具进阶
	 * @param userRoleId
	 * @return
	 */
	public Object[] sjByItem(Long userRoleId, Integer minLevel, Integer maxLevel, Integer zfz) {
		XianJianInfo info = getXianJianInfo(userRoleId);
		if (info == null) {
			return AppErrorCode.XJ_NO_OPEN;
		}
		int currentLevel = info.getXianjianLevel();
		int newlevel = currentLevel + 1;
		if(newlevel > xianJianJiChuConfigExportService.getMaxConfigId()){
			return AppErrorCode.IS_MAX_LEVEL;//已满级
		}
		if (newlevel < minLevel) {
			// 等级不够
			return AppErrorCode.XJ_LEVEL_LIMIT_CAN_NOT_USE;
		} else {
			if(newlevel >= maxLevel){
				// 增加祝福值
				info.setZfzVal(info.getZfzVal()+zfz);
				XianJianJiChuConfig  yuJianJJConfig = xianJianJiChuConfigExportService.loadById(currentLevel);
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
					info.setXianjianLevel(currentLevel+1);
					info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
					//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
					notifyStageXianJianChange(userRoleId);
					xianJianUpdateShow(userRoleId, currentLevel,true);
					if(yuJianJJConfig.isGgopen()){
						UserRole userRole =	roleExportService.getUserRole(userRoleId);
						BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
								AppErrorCode.XJ_SJ_NOTICE, 
								new Object[]{userRole.getName(), newlevel+1}
						});
					}
					//通知client变身形象
					BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_JJ_UP_COMMON, 
							new Object[]{1,info.getZfzVal(),currentLevel,info.getLastSjTime()});
				}else{
					//祝福值变化通知前端
					BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_SHOW, 
							new Object[]{info.getZfzVal(),info.getXianjianLevel(),info.getLastSjTime()});
				}
				//info.setLastSjTime(0l);
				//info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
				xianJianInfoDao.cacheUpdate(info, userRoleId);
			}else{
				info.setXianjianLevel(newlevel);
				info.setZfzVal(0);
				info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
				XianJianJiChuConfig  yuJianJJConfig = xianJianJiChuConfigExportService.loadById(newlevel);
				float clearTime = yuJianJJConfig.getCztime();
				long zfzCdTime=0l;
				if(clearTime == 0 || info.getZfzVal() == 0){
					zfzCdTime=0l; 
				}else{
					zfzCdTime = GameSystemTime.getSystemMillTime() +(int)(clearTime*60*60*1000);
				}
				info.setLastSjTime(zfzCdTime);
				xianJianInfoDao.cacheUpdate(info, userRoleId);
				//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
				notifyStageXianJianChange(userRoleId);
				xianJianUpdateShow(userRoleId, newlevel,true);
				if(yuJianJJConfig.isGgopen()){
					UserRole userRole =	roleExportService.getUserRole(userRoleId);
					BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
							AppErrorCode.XJ_SJ_NOTICE, 
							new Object[]{userRole.getName(), newlevel+1}
					});
				}
				//通知client变身形象
				BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_JJ_UP_COMMON, new Object[]{1,info.getZfzVal(),newlevel,info.getLastSjTime()});

				//排行升级提醒活动角标
				ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.TANG_BAO_TYPE);
			}
		}
		return null;

	}

	/**
	 * 进阶
	 * 
	 * @param userRoleId
	 * @return
	 */
	private Object[] sj(XianJianInfo info, Long userRoleId,
			BusMsgQueue busMsgQueue, boolean isAutoGM, int targetLevel,
			boolean isAuto) {

		int currentLevel = info.getXianjianLevel();
		int maxLevel = xianJianJiChuConfigExportService.getMaxConfigId();
		if (currentLevel >= maxLevel) {
			return AppErrorCode.XJ_LEVEL_MAX;
		}

		if (targetLevel <= currentLevel || targetLevel > maxLevel) {
			return AppErrorCode.XJ_TARGET_LEVEL_ERROR;
		}

		XianJianJiChuConfig zuoqiSjConfig = xianJianJiChuConfigExportService.loadById(currentLevel);
		if (zuoqiSjConfig == null) {
			return AppErrorCode.XJ_CONFIG_ERROR;
		}

		Map<String, Integer> needResource = new HashMap<String, Integer>();
		Map<Integer,Integer> beilvRecords = new HashMap<>();
		
		int zfzVal = info.getZfzVal();//getZfzValue(userRoleId);

//		Object[] result = xianjianSj(currentLevel, needResource, userRoleId,
//				true, maxLevel, false, targetLevel, zfzVal, isAuto,
//				zuoqiSjConfig.getGold(), zuoqiSjConfig.getBgold(),beilvRecords,isAutoGM);
		
		Object[] result = xianjianSjOther(needResource, userRoleId, false, maxLevel, targetLevel, isAuto, beilvRecords, isAutoGM);

		// result:{errorCode,qhlevel,zfzVal}
		Object[] erroCode = (Object[]) result[0];
		if (erroCode != null) {
			return erroCode;
		}

		int newLevel = (Integer) result[1];
		int newZfz = (Integer) result[2];
		Integer newNeedMoney = needResource.remove(GoodsCategory.MONEY + "");
		Integer newNeedGold = needResource.remove(GoodsCategory.GOLD + "");
		Integer newNeedBgold = needResource.remove(GoodsCategory.BGOLD + "");

		// 扣除金币
		if (newNeedMoney != null && newNeedMoney > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY,
					newNeedMoney, userRoleId,
					LogPrintHandle.CONSUME_XIANJIAN_SJ, true,
					LogPrintHandle.CBZ_XIANJIAN_SJ);
		}
		// 扣除元宝
		if (newNeedGold != null && newNeedGold > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD,
					newNeedGold, userRoleId,
					LogPrintHandle.CONSUME_XIANJIAN_SJ, true,
					LogPrintHandle.CBZ_XIANJIAN_SJ);
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,newNeedGold,LogPrintHandle.CONSUME_XIANJIAN_SJ,QQXiaoFeiType.CONSUME_XIANJIAN_SJ,1});
			}
		}
		// 扣除绑定元宝
		if (newNeedBgold != null && newNeedBgold > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD,
					newNeedBgold, userRoleId,
					LogPrintHandle.CONSUME_XIANJIAN_SJ, true,
					LogPrintHandle.CBZ_XIANJIAN_SJ);
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,newNeedBgold,LogPrintHandle.CONSUME_XIANJIAN_SJ,QQXiaoFeiType.CONSUME_XIANJIAN_SJ,1});
			}
		}

		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(needResource, userRoleId, GoodsSource.XJ_SJ, true, true);

		info.setZfzVal(newZfz);
		info.setXianjianLevel(newLevel);
		XianJianJiChuConfig yuJianJJConfig = xianJianJiChuConfigExportService.loadById(newLevel);
		if (newZfz > 0 && (newLevel != currentLevel || zfzVal == 0)) {
			long zfzCdTime = 0l;

			float clearTime = yuJianJJConfig.getCztime();
			if (clearTime == 0) {
				zfzCdTime = 0l;
			} else {
				zfzCdTime = GameSystemTime.getSystemMillTime()
						+ (int) (clearTime * 60 * 60 * 1000);
			}
			info.setLastSjTime(zfzCdTime);

		} else if (newZfz == 0) {
			info.setUpdateTime(new Timestamp(System.currentTimeMillis())); 
			info.setLastSjTime(0l);
			//成就推送
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_XIANJIAN,  yuJianJJConfig.getLevel()});
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			}
		}

		xianJianInfoDao.cacheUpdate(info, userRoleId);

		// 如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象
		if (newLevel > currentLevel) {
			notifyStageXianJianChange(userRoleId);
			if(yuJianJJConfig != null && yuJianJJConfig.getLevel() != zuoqiSjConfig.getLevel()){
				xianJianUpdateShow(userRoleId, yuJianJJConfig.getShowId(),true);
				if(yuJianJJConfig.isGgopen()){
					UserRole userRole =	roleExportService.getUserRole(userRoleId);
					BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
							AppErrorCode.XJ_SJ_NOTICE, 
							new Object[]{userRole.getName(), yuJianJJConfig.getLevel()}
					});
				}
	
				//排行升级提醒活动角标
				ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.TANG_BAO_TYPE);
			}
		}
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A5);
		// 记录操作日志
		JSONArray consumeItemArray = new JSONArray();
		LogFormatUtils.parseJSONArray(bagSlots, consumeItemArray);
        GamePublishEvent.publishEvent(new XianJianLogEvent(LogPrintHandle.XIANJIAN_SJ, userRoleId, newNeedMoney, newNeedGold, consumeItemArray, currentLevel, newLevel, zfzVal, newZfz));
        
		return new Object[]{1,getShowZfz(newLevel, newZfz),newLevel,CovertObjectUtil.object2int(beilvRecords.get(GameConstants.RATIO_2)),CovertObjectUtil.object2int(beilvRecords.get(GameConstants.RATIO_1)),newZfz - zfzVal};
	}
	
	private int getShowZfz(int configId,int newZfz){
		if(configId >0){
			XianJianJiChuConfig config = xianJianJiChuConfigExportService.loadById(configId -1);
			return newZfz - config.getRealNeedZfz();
		}
		return newZfz;
	}

//	/**
//	 * 坐骑升阶
//	 * 
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
//	private Object[] xianjianSj(int zqLevel, Map<String, Integer> needResource,
//			long userRoleId, boolean isSendErrorCode, int maxLevel,
//			boolean isAutoGM, int targetLevel, int zfzVal, boolean isAuto,
//			int yb, int byb
//			,Map<Integer,Integer> beilvRecords,boolean isUseWND){
//
//		if (zqLevel >= targetLevel) {
//			return new Object[] { null, zqLevel };
//		}
//
//		XianJianJiChuConfig zqConfig = xianJianJiChuConfigExportService.loadById(zqLevel);
//		if (zqConfig == null) {
//			Object[] errorCode = isSendErrorCode ? AppErrorCode.XJ_CONFIG_ERROR : null;
//			return new Object[] { errorCode, zqLevel, zfzVal };
//		}
//		
//		
//		int maxzf = zqConfig.getRealNeedZfz();
//		if(zfzVal < maxzf){
//		
//
//				Map<String, Integer> tempResources = new HashMap<>();
//		
//				int money = zqConfig.getMoney();
//				int oldMoney = needResource.get(GoodsCategory.MONEY) == null ? 0 : needResource.get(GoodsCategory.MONEY);
//				Object[] isOb = roleBagExportService.isEnought(GoodsCategory.MONEY, money + oldMoney, userRoleId);
//				if (null != isOb) { Object[] errorCode = isSendErrorCode ? isOb : null;
//					return new Object[] { errorCode, zqLevel, zfzVal };
//				}
//				tempResources.put(GoodsCategory.MONEY + "", money);
//		
//				List<String> needGoodsIds = xianJianJiChuConfigExportService.getConsumeIds(zqConfig.getConsumeItems(),isUseWND);
//				int needCount = zqConfig.getNumber();
//		
//				for (String goodsId : needGoodsIds) {
//					int oldNeedCount = needResource.get(goodsId) == null ? 0 : needResource.get(goodsId);
//		
//					int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
//					if (owerCount >= oldNeedCount + needCount) {
//						tempResources.put(goodsId, needCount);
//						needCount = 0;
//						break;
//					}
//					needCount = oldNeedCount + needCount - owerCount;
//					tempResources.put(goodsId, owerCount - oldNeedCount);
//				}
//		
//				if (isAutoGM && needCount > 0) {
//					int bPrice = byb;// 绑定元宝的价格
//					int bCount = 0;
//					int nowNeedBgold = 0;
//					for (int i = 0; i < needCount; i++) {
//						nowNeedBgold = (bCount + 1) * bPrice;
//						Object[] bgoldError = roleBagExportService.isEnought(
//								GoodsCategory.BGOLD, nowNeedBgold, userRoleId);
//						if (null != bgoldError) {
//							break;
//						}
//						bCount++;
//					}
//					nowNeedBgold = bCount * bPrice;
//					tempResources.put(GoodsCategory.BGOLD + "", nowNeedBgold);
//		
//					needCount = needCount - bCount;
//					int price = yb;
//					int nowNeedGold = needCount * price;
//		
//					Object[] goldError = roleBagExportService.isEnought(
//							GoodsCategory.GOLD, nowNeedGold, userRoleId);
//					if (null != goldError) {
//						Object[] errorCode = isSendErrorCode ? goldError : null;
//						return new Object[] { errorCode, zqLevel, zfzVal };
//					}
//		
//					tempResources.put(GoodsCategory.GOLD + "", nowNeedGold);
//					needCount = 0;
//				}
//		
//				if (needCount > 0) {
//					Object[] errorCode = isSendErrorCode ? AppErrorCode.XJ_GOODS_BZ
//							: null;
//					return new Object[] { errorCode, zqLevel, zfzVal };
//				}
//				ObjectUtil.mapAdd(needResource, tempResources);
//		
//				boolean flag = isSJSuccess(zfzVal, zqConfig);
//				if (!flag) {
//					
//					//星级
//					int[] targetVal = getAddZfzBL(zqConfig);
//					if(targetVal == null){
//						ChuanQiLog.error("wuqi beilv up weight is null,configId = "+ zqConfig.getId());
//						targetVal = new int[]{GameConstants.RATIO_3,1};
//					}
//					beilvRecords.put(targetVal[0], CovertObjectUtil.object2int(beilvRecords.get(targetVal[0]))+ 1);
//					
//					zfzVal += RandomUtil.getIntRandomValue(zqConfig.getZfzmin2(),zqConfig.getZfzmin3() + 1) * targetVal[1];
//				}
//			//修炼任务
//	  		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.JINJIE_TIANGONG, null});
//		}
//  		// 如果祝福值大于了最大值，算强化成功
//		
//		if (zfzVal >= maxzf) {
//			++zqLevel;
//		}
//		
//		// 如果不是自动,成功与否都退出
//		if (!isAuto) {
//			return new Object[] { null, zqLevel, zfzVal };
//		}
//
//		// 成功之后达到了指定的目标等级就停止
//		if (targetLevel <= zqLevel) {
//			return new Object[] { null, zqLevel, zfzVal };
//		}
//
//		return xianjianSj(zqLevel, needResource, userRoleId, false, maxLevel,
//				isAutoGM, targetLevel, zfzVal, isAuto, zqConfig.getGold(),
//				zqConfig.getBgold(),beilvRecords,isUseWND);
//	}
	
	/**
	 * 坐骑升阶
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
	private Object[] xianjianSjOther(Map<String, Integer> needResource,
			long userRoleId, boolean isSendErrorCode, int maxLevel,
			int targetLevel, boolean isAuto,
			Map<Integer,Integer> beilvRecords,boolean isUseWND){

		
		XianJianInfo info = getXianJianInfo(userRoleId); 
		int zqLevel = info.getXianjianLevel();
		int zfzVal = info.getZfzVal();
		
		for(int upCount=0;upCount<10000000;upCount++){
			if (zqLevel >= targetLevel) {
				return new Object[] { null, zqLevel };
			}
	
			XianJianJiChuConfig zqConfig = xianJianJiChuConfigExportService.loadById(zqLevel);
			if (zqConfig == null) {
				Object[] errorCode = isSendErrorCode ? AppErrorCode.XJ_CONFIG_ERROR : null;
				return new Object[] { errorCode, zqLevel, zfzVal };
			}
			
			
			int maxzf = zqConfig.getRealNeedZfz();
			if(zfzVal < maxzf){
			 		Map<String, Integer> tempResources = new HashMap<>();
			
					int money = zqConfig.getMoney();
					int oldMoney = needResource.get(GoodsCategory.MONEY) == null ? 0 : needResource.get(GoodsCategory.MONEY);
					Object[] isOb = roleBagExportService.isEnought(GoodsCategory.MONEY, money + oldMoney, userRoleId);
					if (null != isOb) { Object[] errorCode = isSendErrorCode ? isOb : null;
						return new Object[] { errorCode, zqLevel, zfzVal };
					}
					tempResources.put(GoodsCategory.MONEY + "", money);
			
					List<String> needGoodsIds = xianJianJiChuConfigExportService.getConsumeIds(zqConfig.getConsumeItems(),isUseWND);
					int needCount = zqConfig.getNumber();
			
					for (String goodsId : needGoodsIds) {
						int oldNeedCount = needResource.get(goodsId) == null ? 0 : needResource.get(goodsId);
			
						int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
						if (owerCount >= oldNeedCount + needCount) {
							tempResources.put(goodsId, needCount);
							needCount = 0;
							break;
						}
						needCount = oldNeedCount + needCount - owerCount;
						tempResources.put(goodsId, owerCount - oldNeedCount);
					}
			
					if (needCount > 0) {
						Object[] errorCode = isSendErrorCode ? AppErrorCode.XJ_GOODS_BZ
								: null;
						return new Object[] { errorCode, zqLevel, zfzVal };
					}
					ObjectUtil.mapAdd(needResource, tempResources);
			
//					boolean flag = isSJSuccess(zfzVal, zqConfig);
//					if (!flag) {
					//星级
					int[] targetVal = getAddZfzBL(zqConfig);
					if(targetVal == null){
						ChuanQiLog.error("wuqi beilv up weight is null,configId = "+ zqConfig.getId());
						targetVal = new int[]{GameConstants.RATIO_3,1};
					}
					beilvRecords.put(targetVal[0], CovertObjectUtil.object2int(beilvRecords.get(targetVal[0]))+ 1);
					
					zfzVal += RandomUtil.getIntRandomValue(zqConfig.getZfzmin2(),zqConfig.getZfzmin3() + 1) * targetVal[1];
//					}
				//修炼任务
		  		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.JINJIE_TIANGONG, null});
			}
	  		// 如果祝福值大于了最大值，算强化成功
			
			if (zfzVal >= maxzf) {
				++zqLevel;
			}
			
			// 如果不是自动,成功与否都退出
			if (!isAuto) {
				return new Object[] { null, zqLevel, zfzVal };
			}
	
			// 成功之后达到了指定的目标等级就停止
			if (targetLevel <= zqLevel) {
				return new Object[] { null, zqLevel, zfzVal };
			}
		}

		return new Object[]{AppErrorCode.DATA_ERROR,zqLevel,zfzVal,0};
	}
	
	
	
	
	
	private int[] getAddZfzBL(XianJianJiChuConfig wqConfig){
		return Lottery.getRandomKeyByInteger(wqConfig.getWeights());
	}

	/**
	 * 坐骑升阶
	 * 
	 * @param zfzValue
	 * @param qiLastTime
	 * @param qhConfig
	 * @return
	 */
	public boolean isSJSuccess(int zfzValue, XianJianJiChuConfig zqConfig) {

		int minzf = zqConfig.getZfzmin();

		if (zfzValue < minzf) {
			return false;
		}

		int pro = zqConfig.getPro();

		if (RandomUtil.getIntRandomValue(1, 101) > pro) {
			return false;
		}
		return true;
	}

	/**
	 * 获取御剑公共配置
	 * 
	 * @return
	 */
	private XianJianPublicConfig getXianJianPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_XIANJIAN);
	}

	/**
	 * 开启仙剑模块
	 * 
	 * @param userRoleId
	 */
	public XianJianInfo openXianJian(long userRoleId) {
		// 判定人物等级是否已经在配置表中获得了开启的条件
		int beginLevel = getXianJianPublicConfig().getOpen();
		RoleWrapper roleWrapper = getRoleWrapper(userRoleId);
		if (roleWrapper.getLevel() < beginLevel) {
			return null;
		}

		XianJianInfo xianjianInfo = create(userRoleId);

		notifyStageXianJianChange(userRoleId);
		return xianjianInfo;
	}

	/**
	 * 通知场景里面属性变化
	 */
	public void notifyStageXianJianChange(long userRoleId) {
		Object[] obj = getZuoQiEquips(userRoleId);

		XianJianInfo info = getXianJianInfo(userRoleId);

		// 推送内部场景坐骑属性变化
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_XIANJIAN_CHANGE,
				new Object[] { info, obj,shenQiExportService.getActivatedShenqiNum(userRoleId),huanhuaExportService.getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_3) });
	}

	public Object[] getZuoQiEquips(long userRoleId) {
		List<RoleItemExport> items = roleBagExportService
				.getEquip(userRoleId,ContainerType.TIANGONGITEM);
		return EquipOutputWrapper.getRoleEquipAttribute(items);
	}

	private RoleWrapper getRoleWrapper(long userRoleId) {
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper != null) {
			return roleWrapper;
		}
		return null;
	}

	public XianJianInfo create(long userRoleId) {
		XianJianInfo xianjianInfo = new XianJianInfo();
		xianjianInfo.setUserRoleId(userRoleId);
		xianjianInfo.setQndCount(0);
		xianjianInfo.setShowId(0);
		xianjianInfo.setXianjianLevel(0);
		xianjianInfo.setCzdcount(0);
		xianjianInfo.setZfzVal(0);
		xianjianInfo.setZplus(0L);
		xianjianInfo.setLastSjTime(0l);
		xianjianInfo.setIsGetOn(1);
		xianjianInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));

		// 加入缓存
		xianJianInfoDao.cacheInsert(xianjianInfo, userRoleId);

		return xianjianInfo;
	}

	public XianJianInfo getXianJianInfo(long userRoleId) {
		XianJianInfo xianjianInfo = xianJianInfoDao.cacheLoad(userRoleId,
				userRoleId);
		if(xianjianInfo!=null){
			XianJianJiChuConfig config = xianJianJiChuConfigExportService.loadById(xianjianInfo.getXianjianLevel());
			boolean isClear = config.isZfztime();
			long qiLastTime = xianjianInfo.getLastSjTime();
			if(qiLastTime <= GameSystemTime.getSystemMillTime() && isClear){
				xianjianInfo.setZfzVal(0);
				xianjianInfo.setLastSjTime(0L);
				xianJianInfoDao.cacheUpdate(xianjianInfo, userRoleId);
			}
		}
		return xianjianInfo;
	}

	public XianJianInfo getXianJianInfoDB(long userRoleId) {
		return xianJianInfoDao.dbLoadXianJianInfo(userRoleId);
	}

	/**
	 * 获得翅膀骑的属性
	 */
	public Map<String, Long> getXianJianAttrs(Long userRoleId, XianJian xianJian) {
		if (xianJian == null)
			return null;
		int xianjianLevel = xianJian.getXianjianLevel();
		XianJianJiChuConfig sjConfig = xianJianJiChuConfigExportService
				.loadById(xianjianLevel);

		Map<String, Long> attrs = sjConfig.getAttrs();
		if(attrs == null){
			return null;
		}
		attrs = new HashMap<>(attrs);
		
		List<Integer> huanhuaConfigList = xianJian.getHuanhuaList();
		for(Integer e:huanhuaConfigList){
			YuJianHuanHuaBiaoConfig config = yuJianHuanHuaBiaoConfigExportService.loadById(e);
			if(null == config){
                continue;
            }
			if(config.isCzd()){
				ObjectUtil.longMapAdd(attrs, config.getAttrs());
			}
		}
		
		if (xianJian.getCzdCount() > 0) {
			String czdId = xianJianJiChuConfigExportService.getCzdId();
			float multi = getCzdMulti(czdId) * xianJian.getCzdCount() / 100f
					+ 1f;
			ObjectUtil.longMapTimes(attrs, multi);
		}
		if (xianJian.getQndCount() > 0) {
			XianJianQianNengBiaoConfig qiannengConfig = xianJianQianNengBiaoConfigExportService
					.getConfig();
			for (int i = 0; i < xianJian.getQndCount(); i++) {
				ObjectUtil.longMapAdd(attrs, qiannengConfig.getAttrs());
			}
		}
		for(Integer e:huanhuaConfigList){
			YuJianHuanHuaBiaoConfig config = yuJianHuanHuaBiaoConfigExportService.loadById(e);
            if(null == config){
                continue;
            }
			if(!config.isCzd()){
				ObjectUtil.longMapAdd(attrs, config.getAttrs());
			}
		}
		return attrs;
	}

	private int multi = 0;

	public int getCzdMulti(String czdId) {
		if (multi == 0) {
			List<String> goodsIds = goodsConfigExportService
					.loadIdsById1(czdId);
			if (goodsIds.size() > 0) {
				GoodsConfig config = goodsConfigExportService.loadById(goodsIds
						.get(0));
				multi = config.getData1();
			}
		}
		return multi;
	}

	/**
	 * 翅膀换装备 TODO
	 * 
	 * @param userRoleId
	 * @param guid
	 * @param targetSlot
	 * @param containerType
	 * @return
	 */
	private int getZfzValue(long userRoleId) {
		XianJianInfo info = getXianJianInfo(userRoleId);
		if (info == null) {
			return 0;
		}
		XianJianJiChuConfig config = xianJianJiChuConfigExportService
				.loadById(info.getXianjianLevel());
		int zfzValue = info.getZfzVal();
		boolean isClear = config.isZfztime();
		long qiLastTime = info.getLastSjTime();
		if (qiLastTime <= GameSystemTime.getSystemMillTime() && isClear) {
			return 0;
		}
		return zfzValue;
	}

	public Object[] xianJianShow(Long userRoleId) {
		XianJianInfo info = getXianJianInfo(userRoleId);
		if (info == null) {
			info = openXianJian(userRoleId);
			if (info == null) {
				return AppErrorCode.XIANJIAN_LEVEL_NO;
			}
		}

		return new Object[] { getShowZfz(info.getXianjianLevel(), info.getZfzVal()), info.getXianjianLevel()};
	}

	public XianJianInfo initXianJian(Long userRoleId) {
		return xianJianInfoDao.initXianJianInfo(userRoleId);
	}

	/**
	 * 切换翅膀外显示
	 * 
	 * @param userRoleId
	 * @param showId
	 */
	public Object[] xianJianUpdateShow(Long userRoleId, int showId,boolean checkLevel) {
		XianJianInfo info = getXianJianInfo(userRoleId);
		if (info == null) {
			return AppErrorCode.XJ_NO_OPEN;
		}
		if(checkLevel){
			if (showId < info.getXianjianLevel()
					&& showId == info.getShowId().intValue()) {
				return AppErrorCode.XJ_NO_SHOW;
			}
		}

		info.setShowId(showId);
		xianJianInfoDao.cacheUpdate(info, userRoleId);

		BusMsgSender.send2Stage(userRoleId, InnerCmdType.XIANJIAN_SHOW_UPDATE,
				showId);
		return new Object[] { 1, showId };
	}

	/**
	 * 更新仙剑战斗力
	 * 
	 * @param userRoleId
	 * @param zplus
	 */
	public void updateXianJianZplus(Long userRoleId, Long zplus) {
		XianJianInfo info = getXianJianInfo(userRoleId);
		if (info != null && zplus != null) {

			if (!info.getZplus().equals(zplus)) {
				info.setZplus(zplus);
				xianJianInfoDao.cacheUpdate(info, userRoleId);
			}
		}
	}

	public List<XianJianRankVo> getXianJianRankVo(int limit) {
		return xianJianInfoDao.getXianJianRankVo(limit);
	}

	public Map<String, Long> getXianJianAttr(Long userRoleId) {
		XianJianInfo xianjianInfo = getXianJianInfo(userRoleId);
		if (xianjianInfo == null) {
			return null;
		}
		List<Integer> huanhuanConfigList = huanhuaExportService.getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_3);
		Object[] zqEquips = getZuoQiEquips(userRoleId);
		XianJian xianjian = XianjianUtil
				.coverToXianjian(xianjianInfo,huanhuanConfigList, zqEquips);
		return getXianJianAttrs(userRoleId, xianjian);
	}

	public void onlineHandle(Long userRoleId) {
		XianJianInfo xianjian = getXianJianInfo(userRoleId);
		if (xianjian == null) {
			return;
		}
		BusMsgSender.send2One(
				userRoleId,
				ClientCmdType.XIANJIAN_QND_NUM,xianjian.getQndCount() != null ? xianjian
						.getQndCount() : 0 );
		BusMsgSender.send2One(
				userRoleId,
				ClientCmdType.XIANJIAN_CZD_NUM,xianjian.getCzdcount() != null ? xianjian
						.getCzdcount() : 0 );

		if (xianjian.getShowId() != null) {
			BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_SHOW_ID,
					new Object[]{xianjian.getXianjianLevel(),xianjian.getShowId()});
		}
	}
	
	public int getSkillMaxCount(Long userRoleId){
		XianJianInfo xianjian = getXianJianInfo(userRoleId);
		if(xianjian==null){
			return 0;
		}
		XianJianJiChuConfig config = xianJianJiChuConfigExportService.loadById(xianjian.getXianjianLevel());
		if(config == null){
			return 0;
		}
		return config.getJinengge();
	}


	/**
	 *  领取促销付费奖励直升圣剑
	 * @param newLevel 御剑直接升级到这个等级
	 * @return
	 */
	public Object[] sjByCuxiao(Long userRoleId, int newLevel) {
		XianJianInfo info = getXianJianInfo(userRoleId);
		if(info == null){
			return AppErrorCode.XIANJIAN_LEVEL_NO;
		}
		
		XianJianJiChuConfig config = xianJianJiChuConfigExportService.loadById(info.getXianjianLevel());
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		int level = config.getLevel();
		
		int showId = xianJianJiChuConfigExportService.getShowId(newLevel);
		
		if(newLevel <= level){
			//要升级的等级不能<=当前等级
			return AppErrorCode.WQ_TARGET_LEVEL_ERROR;
		}
		if(level >= xianJianJiChuConfigExportService.getMaxLevel()){
			return AppErrorCode.IS_MAX_LEVEL;//已满级
		}
		XianJianJiChuConfig newConfig = xianJianJiChuConfigExportService.loadById(showId-1);
		info.setZfzVal(newConfig.getRealNeedZfz());
		
		info.setXianjianLevel(showId);
		info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));

		XianJianJiChuConfig  shenJianConfig = xianJianJiChuConfigExportService.loadById(newLevel);

		xianJianInfoDao.cacheUpdate(info, userRoleId);

		//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象
		notifyStageXianJianChange(userRoleId);
		xianJianUpdateShow(userRoleId, showId,true);

		if(shenJianConfig.isGgopen()){
			UserRole userRole =	roleExportService.getUserRole(userRoleId);
			BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{ AppErrorCode.XJ_SJ_NOTICE, new Object[]{userRole.getName(), newLevel}});
		}

		//通知client变身形象
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIANJIAN_JJ_UP_COMMON,new Object[]{1,getShowZfz(info.getXianjianLevel(), info.getZfzVal()),showId,0,0,0});

		//排行升级提醒活动角标
		ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.TANG_BAO_TYPE);

		return null;
	}


//	/**
//	 * 促销奖励直升翅膀
//	 * @param userRoleId
//	 * @param minLevel
//	 * @param maxLevel
//	 * @param zfz
//	 * @return
//	 */
//	public Object[] sjByCuxiao(Long userRoleId,int newlevel){
//		ChiBangInfo info = getChiBangInfo(userRoleId);
//		if(info== null){
//			return AppErrorCode.CB_NO_OPEN;
//		}else{
//			if(newlevel > chiBangJiChuConfigExportService.getMaxJjLevel()){
//				return AppErrorCode.IS_MAX_LEVEL;//已满级
//			}
//			if(newlevel<=info.getChibangLevel()){
//				//要升级的等级不能<=当前等级
//				return AppErrorCode.CB_TARGET_LEVEL_ERROR;
//			}
//			info.setChibangLevel(newlevel);
//			info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
//			ChiBangJiChuConfig  yuJianJJConfig = chiBangJiChuConfigExportService.loadById(newlevel);
//			chiBangInfoDao.cacheUpdate(info, userRoleId);
//			//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象
//			notifyStageChiBangChange(userRoleId);
//			chiBangUpdateShow(userRoleId, newlevel,true);
//			if(yuJianJJConfig.isGgopen()){
//				UserRole userRole =	roleExportService.getUserRole(userRoleId);
//				BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
//						AppErrorCode.CB_SJ_NOTICE,
//						new Object[]{userRole.getName(), newlevel+1}
//				});
//			}
//			//通知client变身形象
//			BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_JJ_UP,
//					new Object[]{1,info.getZfzVal(),newlevel,info.getLastSjTime()});
//
//			//排行升级提醒活动角标
//			ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.XIANJIE_YUYI_TYPE);
//
//		}
//		return null;
//	}

}
