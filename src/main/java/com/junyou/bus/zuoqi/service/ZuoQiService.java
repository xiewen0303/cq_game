package com.junyou.bus.zuoqi.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.BagOutputWrapper;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.OutputType;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.bag.vo.RoleItemOperation;
import com.junyou.bus.branchtask.BranchEnum;
import com.junyou.bus.branchtask.service.TaskBranchService;
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
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.bus.zuoqi.ZuoQiUtil;
import com.junyou.bus.zuoqi.ZuoqiConstants;
import com.junyou.bus.zuoqi.configure.export.YuJianJiChuConfig;
import com.junyou.bus.zuoqi.configure.export.YuJianJiChuConfigExportService;
import com.junyou.bus.zuoqi.configure.export.YuJianQianNengBiaoConfig;
import com.junyou.bus.zuoqi.configure.export.YuJianQianNengBiaoConfigExportService;
import com.junyou.bus.zuoqi.dao.ZuoQiInfoDao;
import com.junyou.bus.zuoqi.entity.ZuoQiInfo;
import com.junyou.bus.zuoqi.manage.ZuoQi;
import com.junyou.bus.zuoqi.vo.ZuoqiRankVo;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.ZuoQiLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.YuJianPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.kuafu.manager.KuafuManager;
import com.junyou.kuafu.share.tunnel.KuafuMsgSender;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.utils.StageHelper;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.lottery.RandomUtil;

/**
 *	坐骑
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午3:38:12
 */

@Service
public class ZuoQiService implements IFightVal{

	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		long result = 0;
		ZuoQiInfo zuoQiInfo = getZuoQiInfo(userRoleId);
		if(zuoQiInfo == null){
			return 0;
		}

		if(fightPowerType == FightPowerType.ZUOQI_JIE){
			int zqLevel = zuoQiInfo.getZuoqiLevel();
			YuJianJiChuConfig  sjConfig = zuoqiJjConfigExportService.loadById(zqLevel);
			if(sjConfig == null){
				ChuanQiLog.error("YuJianJiChuConfig is null,level="+zqLevel);
				return 0;
			}
			Map<String,Long> attrs = sjConfig.getAttrs();
			return CovertObjectUtil.getZplus(attrs);
		}else if(fightPowerType == FightPowerType.HH_WUQI){
			List<Integer> huanhuaConfigList = StageHelper.getHuanhuaExportService().getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_1);
			if(huanhuaConfigList == null){
				return 0;
			}
			for(Integer e:huanhuaConfigList){
				YuJianHuanHuaBiaoConfig config = yuJianHuanHuaBiaoConfigExportService.loadById(e);
				if(null == config){
					continue;
				}
				result +=  CovertObjectUtil.getZplus(config.getAttrs());
			}
			return result;
		}
		return 0;
	}

	@Autowired
	private ZuoQiInfoDao zuoqiInfoDao;
	@Autowired
	private YuJianJiChuConfigExportService zuoqiJjConfigExportService; 
//	@Autowired
//	private YuJianShuXingConfigExportService zuoqiDjConfigExportService; 
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
//	@Autowired
//	private EmailExportService emailExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	
	@Autowired
	private YuJianQianNengBiaoConfigExportService yuJianQianNengBiaoConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
	@Autowired
	private ShenQiExportService shenQiExportService;
	@Autowired
	private YuJianHuanHuaBiaoConfigExportService yuJianHuanHuaBiaoConfigExportService;
	@Autowired
	private HuanhuaExportService huanhuaExportService;
	
	public Object[] useCzdNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService
				.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService
				.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.ZQ_CZD
				&& goodsConfig.getCategory() != GoodsCategory.WANNENG_CZD) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = zqUseCZD(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.ZUOQI_CZ, true, true);
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
		if (goodsConfig.getCategory() != GoodsCategory.ZQ_QND
				&& goodsConfig.getCategory() != GoodsCategory.WANNENG_QND) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = zqUseQND(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.ZUOQI_QN, true, true);
		}
		return ret;
	}
	
	/**
	 * 使用潜能丹
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] zqUseQND(long userRoleId,int count){
		ZuoQiInfo zuoqiInfo = getZuoQiInfo(userRoleId);
		if(zuoqiInfo == null){
			return AppErrorCode.ZQ_NO_OPEN;
		}
		YuJianJiChuConfig yujianConfig = zuoqiJjConfigExportService.loadById(zuoqiInfo.getZuoqiLevel());
		if(yujianConfig.getQndopen().intValue() != ZuoqiConstants.QND_OPEN_FLAG){
			return AppErrorCode.ZUOQI_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if(zuoqiInfo.getQndCount().intValue()+count > yujianConfig.getQndmax()){
			return AppErrorCode.ZUOQI_QND_MAX_NUM;
		}
		zuoqiInfo.setQndCount(count+zuoqiInfo.getQndCount());
		zuoqiInfoDao.cacheUpdate(zuoqiInfo, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_QND_NUM, zuoqiInfo.getQndCount()!=null?zuoqiInfo.getQndCount():0);
		//通知场景里面坐骑属性变化
		notifyStageZuoqiChange(userRoleId);
		return null;
	}
	
	/**
	 * 使用成长丹
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] zqUseCZD(long userRoleId,int count){
		ZuoQiInfo zuoqiInfo = getZuoQiInfo(userRoleId);
		if(zuoqiInfo == null){
			return AppErrorCode.ZQ_NO_OPEN;
		}
		YuJianJiChuConfig yujianConfig = zuoqiJjConfigExportService.loadById(zuoqiInfo.getZuoqiLevel());
		if(yujianConfig.getCzdopen() != ZuoqiConstants.CZD_OPEN_FLAG){
			return AppErrorCode.ZUOQI_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if(zuoqiInfo.getCzdcount().intValue()+count > yujianConfig.getCzdmax()){
			return AppErrorCode.ZUOQI_CZD_MAX_NUM;
		}
		zuoqiInfo.setCzdcount(count+zuoqiInfo.getCzdcount());
		zuoqiInfoDao.cacheUpdate(zuoqiInfo, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_CZD_NUM, zuoqiInfo.getCzdcount()!=null?zuoqiInfo.getCzdcount():0);
		//通知场景里面坐骑属性变化
		notifyStageZuoqiChange(userRoleId);
		
		return null;
	}
	
	
	/**
	 * 坐骑手动升阶
	 * @param userRoleId
	 * @param busMsgQueue
	 * @param isAutoGM
	 * @return
	 */
	public Object[] zuoQiSj(long userRoleId,BusMsgQueue busMsgQueue,boolean isAutoGM){
		
		ZuoQiInfo zuoqiInfo = getZuoQiInfo(userRoleId);
		if(zuoqiInfo == null){
			return AppErrorCode.ZQ_NO_OPEN;
		}
		return zuoQiSJ(zuoqiInfo,userRoleId, busMsgQueue, isAutoGM, zuoqiInfo.getZuoqiLevel() + 1, false); 
	}

	/**
	 *  领取促销付费奖励直升坐骑
	 * @param newLevel 御剑直接升级到这个等级 
	 * @return
	 */
	public Object[] sjByCuxiao(Long userRoleId, int newLevel) {
		ZuoQiInfo  info = getZuoQiInfo(userRoleId);
		if(info == null){
			return AppErrorCode.ZQ_NO_OPEN;
		}
		
		YuJianJiChuConfig config = zuoqiJjConfigExportService.loadById(info.getZuoqiLevel());
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		int level = config.getLevel();
		
		int showId = zuoqiJjConfigExportService.getShowId(newLevel);
		
		if(newLevel <= level){
			//要升级的等级不能<=当前等级
			return AppErrorCode.ZQ_TARGET_LEVEL_ERROR;
		}
		if(newLevel > zuoqiJjConfigExportService.getMaxLevel()){
			return AppErrorCode.IS_MAX_LEVEL;//已满级
		}
		
		YuJianJiChuConfig newConfig = zuoqiJjConfigExportService.loadById(showId-1);
		info.setZfzVal(newConfig.getRealNeedZfz());
		
		info.setZuoqiLevel(showId);
		info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		YuJianJiChuConfig  zuoqiSjConfig = zuoqiJjConfigExportService.loadById(newLevel);
		zuoqiInfoDao.cacheUpdate(info, userRoleId);
		//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
		notifyStageZuoqiChange(userRoleId);
		zuoqiUpdateShow(userRoleId, showId,true);
		if(zuoqiSjConfig.isGgopen()){
			UserRole userRole =	roleExportService.getUserRole(userRoleId);
			BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
					AppErrorCode.ZUOQI_SJ_NOTICE, 
					new Object[]{userRole.getName(), newLevel}
			});
		}
		//通知client变身形象
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_JJ_UP,new Object[]{1,getShowZfz(info.getZuoqiLevel(), info.getZfzVal()),showId,0,0,0});

		//排行升级提醒活动角标
		ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.YUJIAN_FEIXING_TYPE);

		return null;

	}
	
	/**
	 * 坐骑消耗道具直升
	 * @param userRoleId
	 * @param minLevel
	 * @param maxLevel
	 * @param zfz
	 * @return
	 */
	public Object[] sjByItem(Long userRoleId, Integer minLevel, Integer maxLevel, Integer zfz) {
		ZuoQiInfo  info = getZuoQiInfo(userRoleId);
		if(info == null){
			return AppErrorCode.ZQ_NO_OPEN;
		}
		int currentLevel = info.getZuoqiLevel();
		int newLevel = currentLevel + 1;
		if(newLevel > zuoqiJjConfigExportService.getMaxConfigId()){
			return AppErrorCode.IS_MAX_LEVEL;//已满级
		}
		if (newLevel < minLevel) {
			// 等级不够
			return AppErrorCode.ZUOQI_LEVEL_LIMIT_CAN_NOT_USE;
		} else {
			if(newLevel >= maxLevel){
				// 增加祝福值
				info.setZfzVal(info.getZfzVal()+zfz);
				YuJianJiChuConfig  zuoqiSjConfig = zuoqiJjConfigExportService.loadById(currentLevel);
				if(info.getLastSjTime() == 0 &&  zuoqiSjConfig.getCztime() != 0){
					float clearTime = zuoqiSjConfig.getCztime();
					long lastTime = GameSystemTime.getSystemMillTime() +(int)(clearTime*60*60*1000);
					info.setLastSjTime(lastTime);
				}
				//如果祝福值大于了最大值，算强化成功
				int maxzf = zuoqiSjConfig.getZfzmax();
				int minzf = zuoqiSjConfig.getZfzmin();
				if(info.getZfzVal() > minzf && info.getZfzVal() >= maxzf ){
					info.setZfzVal(0);
					info.setLastSjTime(0l);
					info.setZuoqiLevel(currentLevel+1);
					info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
					//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
					notifyStageZuoqiChange(userRoleId);
					zuoqiUpdateShow(userRoleId, info.getZuoqiLevel(),true);
					if(zuoqiSjConfig.isGgopen()){
						UserRole userRole =	roleExportService.getUserRole(userRoleId);
						BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
								AppErrorCode.ZUOQI_SJ_NOTICE, 
								new Object[]{userRole.getName(), newLevel+1}
						});
					}
					//通知client变身形象
					BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_JJ_UP, 
							new Object[]{1,info.getZfzVal(),info.getZuoqiLevel(),info.getLastSjTime()});
				}else{
					//祝福值变化通知前端
					BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_SHOW, 
							new Object[]{info.getZfzVal(),info.getZuoqiLevel(),info.getShowId(),info.getLastSjTime()});
				}
				zuoqiInfoDao.cacheUpdate(info, userRoleId);
			}else{
				info.setZuoqiLevel(newLevel);
				info.setZfzVal(0);
				info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
				YuJianJiChuConfig  zuoqiSjConfig = zuoqiJjConfigExportService.loadById(newLevel);
				float clearTime = zuoqiSjConfig.getCztime();
				long zfzCdTime=0l;
				if(clearTime == 0 || info.getZfzVal() == 0){
					zfzCdTime=0l; 
				}else{
					zfzCdTime = GameSystemTime.getSystemMillTime() +(int)(clearTime*60*60*1000);
				}
				info.setLastSjTime(zfzCdTime);
				zuoqiInfoDao.cacheUpdate(info, userRoleId);
				//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
				notifyStageZuoqiChange(userRoleId);
				zuoqiUpdateShow(userRoleId, newLevel,true);
				if(zuoqiSjConfig.isGgopen()){
					UserRole userRole =	roleExportService.getUserRole(userRoleId);
					BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
							AppErrorCode.ZUOQI_SJ_NOTICE, 
							new Object[]{userRole.getName(), newLevel+1}
					});
				}
				//通知client变身形象
				BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_JJ_UP, 
						new Object[]{1,info.getZfzVal(),newLevel,info.getLastSjTime()});
			}

			//排行升级提醒活动角标
			ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.YUJIAN_FEIXING_TYPE);
		}
		return null;

	}
	
	/**
	 * 坐骑自动升阶
	 * @param userRoleId
	 * @param busMsgQueue
	 * @param isAutoGM
	 * @return
	 */
	public Object[] autoZuoQiSj(long userRoleId,BusMsgQueue busMsgQueue,boolean isAutoGM){
		
		ZuoQiInfo zuoqiInfo = getZuoQiInfo(userRoleId);
		if(zuoqiInfo == null){
			return AppErrorCode.ZQ_NO_OPEN;
		}
		int maxConfigId = zuoqiJjConfigExportService.getMaxConfigId();
		return zuoQiSJ(zuoqiInfo,userRoleId, busMsgQueue, isAutoGM, maxConfigId, true); 
	}
	
	
	/**
	 * 坐骑进阶
	 * @param userRoleId
	 * @return
	 */
	private Object[] zuoQiSJ(ZuoQiInfo zuoqiInfo, Long userRoleId, BusMsgQueue busMsgQueue ,boolean isAutoGM , int targetLevel,boolean isAuto) {
				
		int zuoQiLevel = zuoqiInfo.getZuoqiLevel();
		int maxLevel = zuoqiJjConfigExportService.getMaxConfigId();
		if(zuoQiLevel >= maxLevel){
			return AppErrorCode.ZQ_LEVEL_MAX;
		}
		
		if(targetLevel <= zuoQiLevel || targetLevel >maxLevel){
			return AppErrorCode.ZQ_TARGET_LEVEL_ERROR;
		}
		  
		YuJianJiChuConfig  sjConfig = zuoqiJjConfigExportService.loadById(zuoQiLevel);
	 
		if(sjConfig == null){
			return AppErrorCode.ZQ_CONFIG_ERROR;
		}
		
		Map<String,Integer> needResource = new HashMap<>();
		Map<Integer,Integer> beilvRecords = new HashMap<>();
		
		int zfzVal = zuoqiInfo.getZfzVal();//getZfzValue(userRoleId);
		
//		Object[] result = zuoQiSj(zuoQiLevel,needResource, userRoleId, true, maxJjLevel,false,targetLevel,zfzVal,isAuto,sjConfig.getGold(),sjConfig.getBgold(),beilvRecords,isAutoGM);
		Object[] result = zuoQiSjOther(needResource, userRoleId, false, maxLevel, targetLevel, isAuto, beilvRecords, isAutoGM);
		
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
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, newNeedMoney, userRoleId,  LogPrintHandle.CONSUME_ZUOQI_SJ, true, LogPrintHandle.CBZ_ZUOQI_SJ);
		}
		// 扣除元宝
		if(newNeedGold != null && newNeedGold>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId, LogPrintHandle.CONSUME_ZUOQI_SJ, true,LogPrintHandle.CBZ_ZUOQI_SJ); 
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,newNeedGold,LogPrintHandle.CONSUME_ZUOQI_SJ,QQXiaoFeiType.CONSUME_ZUOQI_SJ,1});
			}
		}
		// 扣除绑定元宝
		if(newNeedBgold != null && newNeedBgold>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId, LogPrintHandle.CONSUME_ZUOQI_SJ, true,LogPrintHandle.CBZ_ZUOQI_SJ); 
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,newNeedBgold,LogPrintHandle.CONSUME_ZUOQI_SJ,QQXiaoFeiType.CONSUME_ZUOQI_SJ,1});
			}
		}
		
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(needResource, userRoleId, GoodsSource.ZQ_SJ, true, true);
		
		
		
		zuoqiInfo.setZfzVal(newZfz);
		zuoqiInfo.setZuoqiLevel(newlevel);
		YuJianJiChuConfig  newConfig = zuoqiJjConfigExportService.loadById(newlevel);
		if(newZfz > 0 && (newlevel != zuoQiLevel || zfzVal ==0)) {
			long zfzCdTime=0l;
			
			float clearTime = newConfig.getCztime();
			if(clearTime == 0){
				zfzCdTime=0l; 
			}else{
				zfzCdTime = GameSystemTime.getSystemMillTime() +(int)(clearTime*60*60*1000);
			}
			zuoqiInfo.setLastSjTime(zfzCdTime);
		}else if(newZfz == 0){
			zuoqiInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			zuoqiInfo.setLastSjTime(0l);
			//成就推送
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_ZUOQI,  newConfig.getLevel()});
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			}
		}
		
		zuoqiInfoDao.cacheUpdate(zuoqiInfo, userRoleId);
		
		//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
		if(newlevel > zuoQiLevel){
			notifyStageZuoqiChange(userRoleId);
			
			if(newConfig != null && newConfig.getLevel() != sjConfig.getLevel()){
				zuoqiUpdateShow(userRoleId, newConfig.getShowId(),true);
	//			//等级发生变化，检测坐骑升阶返利
	//			zuoqiFanli(userRoleId,newlevel);
				if(newConfig.isGgopen()){
					UserRole userRole =	roleExportService.getUserRole(userRoleId);
					BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
							AppErrorCode.ZUOQI_SJ_NOTICE, 
							new Object[]{userRole.getName(), newConfig.getLevel()}
					});
				}
	
				//排行升级提醒活动角标
				ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.YUJIAN_FEIXING_TYPE);
			}
		}
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A3);
		
		//支线
		taskBranchService.completeBranch(userRoleId, BranchEnum.B3,1);
		
		// 记录操作日志
		JSONArray consumeItemArray = new JSONArray(); 
		LogFormatUtils.parseJSONArray(bagSlots,consumeItemArray);
		GamePublishEvent.publishEvent(new ZuoQiLogEvent(LogPrintHandle.ZUOQI_SJ, userRoleId, newNeedMoney, newNeedGold, consumeItemArray, zuoQiLevel, newlevel, zfzVal, newZfz));
		
		return new Object[]{1,getShowZfz(newlevel, newZfz),newlevel,CovertObjectUtil.object2int(beilvRecords.get(GameConstants.RATIO_2)),CovertObjectUtil.object2int(beilvRecords.get(GameConstants.RATIO_1)),newZfz - zfzVal};
	}
	
	private int getShowZfz(int configId,int newZfz){
		if(configId >0){
			YuJianJiChuConfig config = zuoqiJjConfigExportService.loadById(configId -1);
			return newZfz - config.getRealNeedZfz();
		}
		return newZfz;
	}
	
	@Autowired
	private TaskBranchService taskBranchService;
	
//	private void zuoqiFanli(Long userRoleId,int newlevel){
//		YuJianFanLiPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_ZUOQIFANLI);
//		if(config == null){
//			return ;
//		}
//		long expireTime = DatetimeUtil.getNextTime(0, 0);
//		expireTime += GameConstants.DAY_TIME;//过一天
//		
//		if(DatetimeUtil.twoDaysDiffence(ServerInfoServiceManager.getInstance().getServerStartTime().getTime()) < config.getKfTime() && newlevel == config.getFlid()){
//			String content = EmailUtil.getCodeEmail(GameConstants.YUJIAN_FANLI);
//			String title = EmailUtil.getCodeEmail(GameConstants.YUJIAN_FANLI_TITLE);
//			emailExportService.sendEmailToOneExpire(userRoleId,title, content,GameConstants.EMAIL_TYPE_SINGLE, config.getFlitem(),expireTime);
//		}
//	}
	
//	/**
//	 *  坐骑升阶
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
//	private Object[] zuoQiSj(int zqLevel,Map<String,Integer> needResource,
//			long userRoleId,boolean isSendErrorCode,int maxLevel,boolean isAutoGM,
//			int targetLevel,int zfzVal,boolean isAuto,int yb,int byb
//			,Map<Integer,Integer> beilvRecords,boolean isUseWND){
//		
//		if(zqLevel >= targetLevel){
//			 return new Object[]{null,zqLevel};
//		}
//		
//		YuJianJiChuConfig zqConfig = zuoqiJjConfigExportService.loadById(zqLevel);
//		if(zqConfig==null){
//			Object[] errorCode=isSendErrorCode?AppErrorCode.ZQ_CONFIG_ERROR:null;
//			return new Object[]{errorCode,zqLevel,zfzVal};
//		}
//		//如果祝福值大于了最大值，算强化成功
//		int maxzf = zqConfig.getRealNeedZfz();
//		if(zfzVal < maxzf){
//						Map<String,Integer> tempResources = new HashMap<>();
//						 
//						int money = zqConfig.getMoney();
//						int oldMoney = needResource.get(GoodsCategory.MONEY)== null ? 0 :needResource.get(GoodsCategory.MONEY);
//						Object[] isOb=roleBagExportService.isEnought(GoodsCategory.MONEY, money+oldMoney, userRoleId);
//						if(null != isOb){
//							Object[] errorCode = isSendErrorCode ? isOb : null;
//							return new Object[]{errorCode,zqLevel,zfzVal};
//						}
//						tempResources.put(GoodsCategory.MONEY+"",money);
//						List<String> needGoodsIds = zuoqiJjConfigExportService.getConsumeIds(zqConfig.getConsumeItems(),isUseWND);
//						int needCount = zqConfig.getNumber();
//						
//						for (String goodsId : needGoodsIds) {
//							int oldNeedCount = needResource.get(goodsId) == null ? 0 : needResource.get(goodsId);
//							
//							int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
//							if(owerCount >= oldNeedCount + needCount){
//								tempResources.put(goodsId, needCount);
//								needCount = 0;
//								break;
//							}
//							needCount = oldNeedCount + needCount - owerCount;
//							tempResources.put(goodsId, owerCount - oldNeedCount);
//						}
//						
//						if(isAutoGM && needCount > 0){
//							int bPrice = byb;//绑定元宝的价格
//							int bCount = 0;
//							int nowNeedBgold = 0;
//							for (int i = 0; i < needCount; i++) {
//								nowNeedBgold = (bCount + 1) * bPrice;
//								Object[] bgoldError =  roleBagExportService.isEnought(GoodsCategory.BGOLD,nowNeedBgold, userRoleId);
//								if(null != bgoldError){ 
//									break;
//								}
//								bCount++;
//							}
//							nowNeedBgold = bCount * bPrice;
//							tempResources.put(GoodsCategory.BGOLD+"", nowNeedBgold);
//							
//							needCount = needCount - bCount;
//							
//							int price = yb;
//							
//							int nowNeedGold = needCount * price;
//							
//							Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD,nowNeedGold, userRoleId);
//							if(null != goldError){ 
//								Object[] errorCode=isSendErrorCode?goldError:null;
//								return new Object[]{errorCode,zqLevel,zfzVal};
//							}
//							
//							tempResources.put(GoodsCategory.GOLD+"", nowNeedGold);
//							needCount = 0;
//						}
//						
//						if(needCount > 0){
//							Object[] errorCode = isSendErrorCode ? AppErrorCode.ZQ_GOODS_BZ : null;
//							return new Object[]{errorCode,zqLevel,zfzVal};
//						}
//						ObjectUtil.mapAdd(needResource, tempResources);
//						//星级
//						int[] targetVal = getAddZfzBL(zqConfig);
//						if(targetVal == null){
//							ChuanQiLog.error("zuoQi beilv up weight is null,configId = "+ zqConfig.getId());
//							targetVal = new int[]{GameConstants.RATIO_3,1};
//						}
//						beilvRecords.put(targetVal[0], CovertObjectUtil.object2int(beilvRecords.get(targetVal[0]))+ 1);
//						
//						zfzVal += RandomUtil.getIntRandomValue(zqConfig.getZfzmin2(), zqConfig.getZfzmin3()+1) * targetVal[1];
//						//修炼任务
//						BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.JINJIE_ZUOQI, null});
//		}
//		if(zfzVal >= maxzf){
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
//		return zuoQiSj(zqLevel, needResource, userRoleId, false, maxLevel, isAutoGM, targetLevel, zfzVal, isAuto,zqConfig.getGold(),zqConfig.getBgold(),beilvRecords,isUseWND);
//	}
	
	
	private Object[] zuoQiSjOther(Map<String,Integer> needResource,
			long userRoleId,boolean isSendErrorCode,int maxLevel,
			int targetLevel,boolean isAuto,
			Map<Integer,Integer> beilvRecords,boolean isUseWND){
		
		ZuoQiInfo info = getZuoQiInfo(userRoleId); 
		int zqLevel = info.getZuoqiLevel();
		int zfzVal = info.getZfzVal();
		
		for(int upCount=0;upCount<10000000;upCount++){
			if(zqLevel >= targetLevel){
				 return new Object[]{null,zqLevel};
			}
			
			YuJianJiChuConfig zqConfig = zuoqiJjConfigExportService.loadById(zqLevel);
			if(zqConfig==null){
				Object[] errorCode=isSendErrorCode?AppErrorCode.ZQ_CONFIG_ERROR:null;
				return new Object[]{errorCode,zqLevel,zfzVal};
			}
			//如果祝福值大于了最大值，算强化成功
			int maxzf = zqConfig.getRealNeedZfz();
			if(zfzVal < maxzf){
				Map<String,Integer> tempResources = new HashMap<>();
				 
				int money = zqConfig.getMoney();
				int oldMoney = needResource.get(GoodsCategory.MONEY)== null ? 0 :needResource.get(GoodsCategory.MONEY);
				Object[] isOb=roleBagExportService.isEnought(GoodsCategory.MONEY, money+oldMoney, userRoleId);
				if(null != isOb){
					Object[] errorCode = isSendErrorCode ? isOb : null;
					return new Object[]{errorCode,zqLevel,zfzVal};
				}
				tempResources.put(GoodsCategory.MONEY+"",money);
				List<String> needGoodsIds = zuoqiJjConfigExportService.getConsumeIds(zqConfig.getConsumeItems(),isUseWND);
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
					Object[] errorCode = isSendErrorCode ? AppErrorCode.ZQ_GOODS_BZ : null;
					return new Object[]{errorCode,zqLevel,zfzVal};
				}
				ObjectUtil.mapAdd(needResource, tempResources);
				//星级
				int[] targetVal = getAddZfzBL(zqConfig);
				if(targetVal == null){
					ChuanQiLog.error("zuoQi beilv up weight is null,configId = "+ zqConfig.getId());
					targetVal = new int[]{GameConstants.RATIO_3,1};
				}
				beilvRecords.put(targetVal[0], CovertObjectUtil.object2int(beilvRecords.get(targetVal[0]))+ 1);
				
				zfzVal += RandomUtil.getIntRandomValue(zqConfig.getZfzmin2(), zqConfig.getZfzmin3()+1) * targetVal[1];
				//修炼任务
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.JINJIE_ZUOQI, null});
			}
			if(zfzVal >= maxzf){
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
	
	private int[] getAddZfzBL(YuJianJiChuConfig wqConfig){
		return Lottery.getRandomKeyByInteger(wqConfig.getWeights());
	}
	
	
	/**
	 * 坐骑升阶
	 * @param zfzValue
	 * @param qiLastTime
	 * @param qhConfig
	 * @return
	 */
	public boolean isSJSuccess(int zfzValue,YuJianJiChuConfig zqConfig){
		 
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
	
	public static void main(String[] args) {
		System.out.println(RandomUtil.getIntRandomValue(1, 101)> 100);
	}
	 
	
	/**
	 * 获取御剑公共配置
	 * @return
	 */
	private YuJianPublicConfig getYuJianPublicConfig(){
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YUJIAN);
	}
	/**
	 * 开启坐骑模块
	 * @param userRoleId
	 */
	public ZuoQiInfo openZuoQi(long userRoleId){
		//判定人物等级是否已经在配置表中获得了开启的条件 
		int beginLevel = getYuJianPublicConfig().getOpen();
		RoleWrapper roleWrapper = getRoleWrapper(userRoleId);
		if(roleWrapper.getLevel() < beginLevel){
			return null;
		}
		
		ZuoQiInfo  zuoQiInfo = create(userRoleId);
		
		notifyStageZuoqiChange(userRoleId);
		
		return zuoQiInfo;
	}
	
	/**
	 * 通知场景里面属性变化
	 */
	public void notifyStageZuoqiChange(long userRoleId){
		Object[] obj = getZuoQiEquips(userRoleId);
		
		ZuoQiInfo ZuoQiInfo = getZuoQiInfo(userRoleId);
		
		// 推送内部场景坐骑属性变化
		BusMsgSender.send2Stage(userRoleId,InnerCmdType.INNER_ZUOQI_CHANGE, new Object[]{ZuoQiInfo,obj,shenQiExportService.getActivatedShenqiNum(userRoleId),huanhuaExportService.getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_1)});
	}
	
	public Object[] getZuoQiEquips(long userRoleId){ 
		List<RoleItemExport> items = roleBagExportService.getEquip(userRoleId,ContainerType.ZUOQIITEM);
		return  EquipOutputWrapper.getRoleEquipAttribute(items);
	}
	
	
	private RoleWrapper getRoleWrapper(long userRoleId){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper != null){
			return roleWrapper;
		}
		return null;
	}
	
	public ZuoQiInfo create(long userRoleId){
		ZuoQiInfo zuoqiInfo =new ZuoQiInfo(); 
		zuoqiInfo.setUserRoleId(userRoleId);
		zuoqiInfo.setQndCount(0);
		zuoqiInfo.setShowId(0);
		zuoqiInfo.setZuoqiLevel(0);
		zuoqiInfo.setCzdcount(0);
		zuoqiInfo.setZfzVal(0);
		zuoqiInfo.setZplus(0L);
		zuoqiInfo.setLastSjTime(0l);
		zuoqiInfo.setIsGetOn(0);
		zuoqiInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		//加入缓存
		zuoqiInfoDao.cacheInsert(zuoqiInfo, userRoleId);
		
		return zuoqiInfo;
	}
	
	public ZuoQiInfo getZuoQiInfo(long userRoleId){
		ZuoQiInfo zuoqiInfo = zuoqiInfoDao.cacheLoad(userRoleId, userRoleId);
		if(zuoqiInfo!=null){
			YuJianJiChuConfig  config = zuoqiJjConfigExportService.loadById(zuoqiInfo.getZuoqiLevel());
			boolean isClear = config.isZfztime();
			long qiLastTime = zuoqiInfo.getLastSjTime();
			if(qiLastTime <= GameSystemTime.getSystemMillTime() && isClear){
				zuoqiInfo.setZfzVal(0);
				zuoqiInfo.setLastSjTime(0L);
				zuoqiInfoDao.cacheUpdate(zuoqiInfo, userRoleId);
			}
		}
		return zuoqiInfo; 
	}

	public ZuoQiInfo getZuoQiDB(long userRoleId) {
		return zuoqiInfoDao.dbLoadZuoQi(userRoleId);
	}
	/**
	 * 获得坐骑的属性
	 */
	public Map<String, Long> getZuoQiAttrs(Long userRoleId,ZuoQi zuoqi,int dj) {
		if(zuoqi == null) return null;
		int zqLevel = zuoqi.getZuoQiLevel();
		YuJianJiChuConfig  sjConfig = zuoqiJjConfigExportService.loadById(zqLevel);
		
		Map<String,Long> attrs = sjConfig.getAttrs(); 
		if(attrs == null){
			return null;
		}
		attrs = new HashMap<>(attrs);
		Long speed = attrs.remove(EffectType.x19.name());
		
		List<Integer> huanhuaConfigList = zuoqi.getHuanhuaList();
		for(Integer e:huanhuaConfigList){
			YuJianHuanHuaBiaoConfig config = yuJianHuanHuaBiaoConfigExportService.loadById(e);
			if(null == config){
			    continue;
			}
			if(config.isCzd()){
				ObjectUtil.longMapAdd(attrs, config.getAttrs());
			}
		}
		
		if(zuoqi.getCzdCount() > 0){
			String czdId = zuoqiJjConfigExportService.getCzdId();
			float multi = getCzdMulti(czdId)*zuoqi.getCzdCount()/100f + 1f;
			ObjectUtil.longMapTimes(attrs, multi);
		}
		
//		//御剑属性加成表
//		YuJianShuXingConfig  yjsxConfig = zuoqiDjConfigExportService.getYuJianShuXingConfig(zqLevel, dj);
//		if(yjsxConfig  != null){
//			ObjectUtil.longMapAdd(attrs, yjsxConfig.getDatas());
//		}
		
		if(zuoqi.getQndCount() > 0){
			YuJianQianNengBiaoConfig qiannengConfig = yuJianQianNengBiaoConfigExportService.getConfig();
			for(int i=0;i<zuoqi.getQndCount();i++){
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

	/**
	 * 更新坐骑战斗力
	 * @param zuoqi
	 */
	public void updateZuoQiZplus(Long userRoleId,Long zplus){
		ZuoQiInfo zuoQiInfo = getZuoQiInfo(userRoleId);
		if(zuoQiInfo != null && zplus != null){
			//变化更新
			if(zuoQiInfo.getZplus() != zplus){
				zuoQiInfo.setZplus(zplus);
				zuoqiInfoDao.cacheUpdate(zuoQiInfo, zuoQiInfo.getUserRoleId());
			}
		}
	}
	
	/**
	 * 获得坐骑移动速度属性
	 * @param zuoqi
	 * @return
	 */
	public Map<String, Long> getZuoQiSeedAttr(ZuoQi zuoqi) {
		if(zuoqi == null) return null;
		
		YuJianJiChuConfig  config = zuoqiJjConfigExportService.loadById(zuoqi.getZuoQiLevel());
		if(config == null ) return null;
		Map<String,Long> result = new HashMap<>();
		result.put(EffectType.x20.name(), config.getMoveAttrVal());
		return result;
	}

	public Object[] zuoqiChangeEquip(Long userRoleId, long guid,int targetSlot, int containerType) {
		//如是脱下 检查身上是否有该装备
		RoleItemExport roleItemExport = roleBagExportService.getOtherEquip(userRoleId, guid,ContainerType.ZUOQIITEM);
		if(targetSlot>0 && roleItemExport == null){
			return AppErrorCode.BODY_NO_ITEM;
		}
		
		BagSlots bagSlots=roleBagExportService.moveSlot(guid, targetSlot,containerType, userRoleId);
		if(!bagSlots.isSuccee()){
			return bagSlots.getErrorCode();
		}
		
		//通知场景
		notifyStageZuoqiChange(userRoleId);
		
		Object[] result=new Object[3];
		result[0]=1;
		List<RoleItemOperation> roleItemVos=bagSlots.getRoleItemVos();
		for (int i=0;i<roleItemVos.size();i++) {
			result[i+1]=BagOutputWrapper.getOutWrapperData(OutputType.MOVESLOT, roleItemVos.get(i));
		}
		return result; 
	}
	
//	private int getZfzValue(long userRoleId){
//		ZuoQiInfo  zuoQiInfo = getZuoQiInfo(userRoleId);
//		if(zuoQiInfo == null){
//			return 0;
//		}
//		YuJianJiChuConfig  config = zuoqiJjConfigExportService.loadById(zuoQiInfo.getZuoqiLevel());
//		int zfzValue = zuoQiInfo.getZfzVal();
//		boolean isClear = config.isZfztime();
//		long qiLastTime = zuoQiInfo.getLastSjTime();
//		if(qiLastTime <= GameSystemTime.getSystemMillTime() && isClear){
//			return 0;
//		}
//		return zfzValue;
//	}
	
	public Object[] zuoqiShow(Long userRoleId) {
		ZuoQiInfo zuoqiInfo = getZuoQiInfo(userRoleId);
		if(zuoqiInfo == null){
			zuoqiInfo = openZuoQi(userRoleId);//create(userRoleId);
			if(zuoqiInfo == null){
				return AppErrorCode.ZUOQI_LEVEL_NO;
			}
		}
		
		return new Object[]{getShowZfz(zuoqiInfo.getZuoqiLevel(), zuoqiInfo.getZfzVal()),zuoqiInfo.getZuoqiLevel(),zuoqiInfo.getShowId(),zuoqiInfo.getLastSjTime()};
	}

	public ZuoQiInfo initZuoQi(Long userRoleId) {
		return zuoqiInfoDao.initZuoQiInfo(userRoleId);
	}

	public void zuoqiDown(Long userRoleId, boolean state) {
		ZuoQiInfo  zuoqiInfo = getZuoQiInfo(userRoleId);
		if(zuoqiInfo == null) {
			return ;
		}
		
		zuoqiInfo.setIsGetOn(state?GameConstants.ZQ_UP:GameConstants.ZQ_DOWN);
		zuoqiInfoDao.cacheUpdate(zuoqiInfo, userRoleId);
		
		//通知场景修改坐骑状态
		if(KuafuManager.kuafuIng(userRoleId)){
			KuafuMsgSender.send2KuafuServer(userRoleId,userRoleId, InnerCmdType.ZUOQI_STATE, state); 
		}else{
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.ZUOQI_STATE, state); 
		}
	}

	/**
	 * 切换坐骑外显示
	 * @param userRoleId
	 * @param showId
	 */
	public Object[] zuoqiUpdateShow(Long userRoleId, int showId,boolean checkLevel) {
		ZuoQiInfo  zuoqiInfo = getZuoQiInfo(userRoleId);
		if(zuoqiInfo == null) {
			return AppErrorCode.ZQ_NO_OPEN;
		}
		if(checkLevel){
			if(showId > zuoqiInfo.getZuoqiLevel() && showId != zuoqiInfo.getShowId().intValue()){
				return AppErrorCode.ZQ_NO_SHOW;
			}
		}
		
		zuoqiInfo.setShowId(showId);
		zuoqiInfoDao.cacheUpdate(zuoqiInfo, userRoleId);
		
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_SHOW_UPDATE, showId);
		
		return new Object[]{1,showId};
	}

	/**
	 * 持久化坐骑状态到db中
	 * @param userRoleId
	 * @param state
	 */
	public void zuoqiBusState(Long userRoleId, int state) {
		ZuoQiInfo zuoqiInfo = getZuoQiInfo(userRoleId);
		zuoqiInfo.setIsGetOn(state);
		zuoqiInfoDao.cacheUpdate(zuoqiInfo, userRoleId);
	}

	public List<ZuoqiRankVo> getZuoqiRankVo(int limit) {
		return zuoqiInfoDao.getZuoqiRankVo(limit);
	}
	
	public Map<String,Long> getZuoqiAttr(Long userRoleId){
		ZuoQiInfo zuoqiInfo = getZuoQiInfo(userRoleId);
		if(zuoqiInfo==null){
			return null;
		}
		List<Integer> huanhuanConfigList = huanhuaExportService.getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_1);
		Object[] zqEquips = getZuoQiEquips(userRoleId);
		ZuoQi zuoQi =  ZuoQiUtil.coverToZuoQi(zuoqiInfo,huanhuanConfigList, zqEquips);
		return getZuoQiAttrs(userRoleId, zuoQi, getRoleWrapper(userRoleId).getLevel());
	}
	
	public void onlineHandle(Long userRoleId){
		ZuoQiInfo zuoqi = getZuoQiInfo(userRoleId);
		if(zuoqi == null){
			return;
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_QND_NUM, zuoqi.getQndCount()!=null?zuoqi.getQndCount():0);
		BusMsgSender.send2One(userRoleId, ClientCmdType.ZUOQI_CZD_NUM, zuoqi.getCzdcount()!=null?zuoqi.getCzdcount():0);
	}
	
	public int getSkillMaxCount(Long userRoleId){
		ZuoQiInfo zuoqi = getZuoQiInfo(userRoleId);
		if(zuoqi == null){
			return 0 ;
		}
		YuJianJiChuConfig sjConfig = zuoqiJjConfigExportService.loadById(zuoqi.getZuoqiLevel());
		if(sjConfig == null){
			return 0;
		}
		return sjConfig.getJinengge();
	}
   
	/**
     * 玩家下线处理:下坐骑
     * @param userRoleId
     */
    public void offlineHandle(Long userRoleId) {
        ZuoQiInfo  zuoqiInfo = getZuoQiInfo(userRoleId);
        if(zuoqiInfo == null) {
            return ;
        }
        zuoqiInfo.setIsGetOn(GameConstants.ZQ_DOWN);
        zuoqiInfoDao.cacheUpdate(zuoqiInfo, userRoleId);
    }
}