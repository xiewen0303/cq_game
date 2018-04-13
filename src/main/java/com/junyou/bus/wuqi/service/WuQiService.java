package com.junyou.bus.wuqi.service;

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
import com.junyou.bus.rfbactivity.service.ActivityServiceFacotry;
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.shenqi.export.ShenQiExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wuqi.WuQiConstants;
import com.junyou.bus.wuqi.WuQiUtil;
import com.junyou.bus.wuqi.configure.export.XinShengJianJiChuConfig;
import com.junyou.bus.wuqi.configure.export.XinShengJianJiChuConfigExportService;
import com.junyou.bus.wuqi.configure.export.XinShengJianQianNengBiaoConfig;
import com.junyou.bus.wuqi.configure.export.XinShengJianQianNengBiaoConfigExportService;
import com.junyou.bus.wuqi.configure.export.XinShengJianZhuJueJiaChengConfig;
import com.junyou.bus.wuqi.configure.export.XinShengJianZhuJueJiaChengConfigExportService;
import com.junyou.bus.wuqi.dao.WuQiInfoDao;
import com.junyou.bus.wuqi.entity.WuQiInfo;
import com.junyou.bus.wuqi.manage.WuQi;
import com.junyou.bus.wuqi.vo.WuQiRankVo;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.WuQiLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.XinShengJianPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.lottery.RandomUtil;

/**
 * 武器
 *@author: wind
 *@email: 18221610336@163.com
 *@version: 2014-11-27下午3:38:12
 */

@Service
public class WuQiService implements IFightVal {
	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		long result = 0;
		WuQiInfo wuQiInfo = getWuQiInfo(userRoleId);
		if(wuQiInfo == null){
		    return result;
        }

		switch (fightPowerType)
		{
			case FightPowerType.WUQI_JIE:
                XinShengJianJiChuConfig  sjConfig = xinShengJianConfigExportService.loadById(wuQiInfo.getZuoqiLevel());
                if(sjConfig == null){
                    ChuanQiLog.error("XinShengJianJiChuConfig is null,level=" + wuQiInfo.getZuoqiLevel());
                    return 0;
                }
                return CovertObjectUtil.getZplus(sjConfig.getAttrs());
			case FightPowerType.WUQI_ROLE_LEVE:
				RoleWrapper roleWrapper = getRoleWrapper(userRoleId);
				if(roleWrapper == null){
					ChuanQiLog.error("user is not exist,userRoleId="+userRoleId);
					return 0;
				}
				//主角加成
				XinShengJianZhuJueJiaChengConfig  yjsxConfig = xinShengJianJCConfigExportService.getXinShengJianJiaChengConfig(wuQiInfo.getZuoqiLevel(), roleWrapper.getLevel());
				if(yjsxConfig == null){
					ChuanQiLog.error("XinShengJianZhuJueJiaChengConfig is null,wuqiLevel="+wuQiInfo.getZuoqiLevel()+"\trolelevel:"+roleWrapper.getLevel());
					return 0;
				}
				return CovertObjectUtil.getZplus(yjsxConfig.getDatas());

			default:
				ChuanQiLog.error("not exist type:"+fightPowerType);
		}
		return result;
	}

	@Autowired
	private WuQiInfoDao wuqiInfoDao;
	@Autowired
	private XinShengJianJiChuConfigExportService xinShengJianConfigExportService; 
	@Autowired
	private XinShengJianZhuJueJiaChengConfigExportService xinShengJianJCConfigExportService; 
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;

	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private XinShengJianQianNengBiaoConfigExportService wuQiQianNengBiaoConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired 
	private HuoYueDuExportService huoYueDuExportService;
//	@Autowired
//	private EmailExportService emailExportService;
	@Autowired
	private ShenQiExportService shenQiExportService;
	@Autowired
	private YuJianHuanHuaBiaoConfigExportService yuJianHuanHuaBiaoConfigExportService;
	@Autowired
	private HuanhuaExportService huanhuaExportService;
	@Autowired
	private TaskBranchService taskBranchService;
	/**
	 * 万能成长丹使用
	 * @param userRoleId
	 * @param guid
	 * @return
	 */
	public Object[] useCzdNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.WQ_CZD&& goodsConfig.getCategory() != GoodsCategory.WANNENG_CZD) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = zqUseCZD(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.WUQI_CZ, true, true);
		}
		return ret;
	}
	
	/**
	 * 万能潜能丹使用
	 * @param userRoleId
	 * @param guid
	 * @return
	 */
	public Object[] useQndNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.WQ_QND && goodsConfig.getCategory() != GoodsCategory.WANNENG_QND) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = wqUseQND(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.WUQI_QN, true, true);
		}
		return ret;
	}
	
	/**
	 * 使用潜能丹
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] wqUseQND(long userRoleId,int count){
		WuQiInfo wuqiInfo = getWuQiInfo(userRoleId);
		if(wuqiInfo == null){
			return AppErrorCode.WQ_NO_OPEN;
		}
		XinShengJianJiChuConfig wuqiConfig = xinShengJianConfigExportService.loadById(wuqiInfo.getZuoqiLevel());
		if(wuqiConfig.getQndopen().intValue() != WuQiConstants.QND_OPEN_FLAG){
			return AppErrorCode.WQ_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if(wuqiInfo.getQndCount().intValue()+count > wuqiConfig.getQndmax()){
			return AppErrorCode.WQ_QND_MAX_NUM;
		}
		wuqiInfo.setQndCount(count + wuqiInfo.getQndCount());
		wuqiInfoDao.cacheUpdate(wuqiInfo, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_QND_NUM, wuqiInfo.getQndCount()!=null?wuqiInfo.getQndCount():0);
		//通知场景里面圣剑属性变化
		notifyStageWuqiChange(userRoleId);
		return null;
	}
	
	/**
	 * 使用成长丹
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] zqUseCZD(long userRoleId,int count){
		WuQiInfo wuqiInfo = getWuQiInfo(userRoleId);
		if(wuqiInfo == null){
			return AppErrorCode.WQ_NO_OPEN;
		}
		XinShengJianJiChuConfig yujianConfig = xinShengJianConfigExportService.loadById(wuqiInfo.getZuoqiLevel());
		if(yujianConfig.getCzdopen() != WuQiConstants.CZD_OPEN_FLAG){
			return AppErrorCode.WQ_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if(wuqiInfo.getCzdcount().intValue()+count > yujianConfig.getCzdmax()){
			return AppErrorCode.WQ_CZD_MAX_NUM;
		}
		wuqiInfo.setCzdcount(count + wuqiInfo.getCzdcount());
		wuqiInfoDao.cacheUpdate(wuqiInfo, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_CZD_NUM, wuqiInfo.getCzdcount()!=null?wuqiInfo.getCzdcount():0);
		//通知场景里面圣剑属性变化
		notifyStageWuqiChange(userRoleId);
		
		return null;
	}
	
	
	/**
	 * 圣剑手动升阶
	 * @param userRoleId
	 * @param busMsgQueue
	 * @param isAutoGM
	 * @return
	 */
	public Object[] wuQiSj(long userRoleId,BusMsgQueue busMsgQueue,boolean isAutoGM){
		
		WuQiInfo wuqiInfo = getWuQiInfo(userRoleId);
		if(wuqiInfo == null){
			return AppErrorCode.WQ_NO_OPEN;
		}
		return wuQiSJ(wuqiInfo,userRoleId, busMsgQueue, isAutoGM, wuqiInfo.getZuoqiLevel() + 1, false); 
	}

	/**
	 *  领取促销付费奖励直升圣剑
	 * @param newLevel 御剑直接升级到这个等级 
	 * @return
	 */
	public Object[] sjByCuxiao(Long userRoleId, int newLevel) {
		WuQiInfo  info = getWuQiInfo(userRoleId);
		if(info == null){
			return AppErrorCode.WQ_NO_OPEN;
		}
		XinShengJianJiChuConfig config = xinShengJianConfigExportService.loadById(info.getZuoqiLevel());
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		int level = config.getLevel();
		
		int showId = xinShengJianConfigExportService.getShowId(newLevel);
		
		if(newLevel <= level){
			//要升级的等级不能<=当前等级
			return AppErrorCode.WQ_TARGET_LEVEL_ERROR;
		}
		if(level >= xinShengJianConfigExportService.getMaxLevel()){
			return AppErrorCode.IS_MAX_LEVEL;//已满级
		}
		
		XinShengJianJiChuConfig newConfig = xinShengJianConfigExportService.loadById(showId-1);
		info.setZfzVal(newConfig.getRealNeedZfz());
		info.setZuoqiLevel(showId);
		info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		XinShengJianJiChuConfig  shenJianConfig = xinShengJianConfigExportService.loadById(showId);
		wuqiInfoDao.cacheUpdate(info, userRoleId);
		//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
		notifyStageWuqiChange(userRoleId);
		wuqiUpdateShow(userRoleId, showId,true);
		if(shenJianConfig.isGgopen()){
			UserRole userRole =	roleExportService.getUserRole(userRoleId);
			BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{ AppErrorCode.WQ_SJ_NOTICE,new Object[]{userRole.getName(), newLevel}});
		}
		//通知client变身形象
		BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_JJ_UP,new Object[]{1,getShowZfz(info.getZuoqiLevel(), info.getZfzVal()),showId,0,0,0});

		//排行升级提醒活动角标
		ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.SHENG_JIAN_RANK);

		return null;

	}
	
	/**
	 * 圣剑消耗道具直升 
	 * @param userRoleId
	 * @param minLevel
	 * @param maxLevel
	 * @param zfz
	 * @return
	 */
	public Object[] sjByItem(Long userRoleId, Integer minLevel, Integer maxLevel, Integer zfz) {
		WuQiInfo  info = getWuQiInfo(userRoleId);
		if(info == null){
			return AppErrorCode.WQ_NO_OPEN;
		}
		
		int currentLevel = info.getZuoqiLevel();
		if(currentLevel >= xinShengJianConfigExportService.getMaxConfigId()){
			return AppErrorCode.IS_MAX_LEVEL;//已满级
		}
		
		XinShengJianJiChuConfig config = xinShengJianConfigExportService.loadById(currentLevel);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Integer showId = xinShengJianConfigExportService.getShowId(config.getLevel() + 1);
		if(showId == null){
			return AppErrorCode.IS_MAX_LEVEL;//已满级
		}
		
		int newLevel = config.getLevel() + 1;
		
		
		if (newLevel < minLevel) {
			// 等级不够
			return AppErrorCode.WQ_LEVEL_LIMIT_CAN_NOT_USE;
		} else {
			if(newLevel >= maxLevel){
				// 增加祝福值
				info.setZfzVal(info.getZfzVal()+zfz);
				XinShengJianJiChuConfig  wuqiSjConfig = xinShengJianConfigExportService.loadById(currentLevel);
				if(info.getLastSjTime() == 0 &&  wuqiSjConfig.getCztime() != 0){
					float clearTime = wuqiSjConfig.getCztime();
					long lastTime = GameSystemTime.getSystemMillTime() +(int)(clearTime*60*60*1000);
					info.setLastSjTime(lastTime);
				}
				//如果祝福值大于了最大值，算强化成功
				int maxzf = wuqiSjConfig.getZfzmax();
				int minzf = wuqiSjConfig.getZfzmin();
				if(info.getZfzVal() > minzf && info.getZfzVal() >= maxzf ){
					info.setZfzVal(0);
					info.setLastSjTime(0l);
					info.setZuoqiLevel(currentLevel+1);
					info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
					//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
					notifyStageWuqiChange(userRoleId);
					wuqiUpdateShow(userRoleId, info.getZuoqiLevel(),true);
					if(wuqiSjConfig.isGgopen()){
						UserRole userRole =	roleExportService.getUserRole(userRoleId);
						BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
								AppErrorCode.WQ_SJ_NOTICE, 
								new Object[]{userRole.getName(),showId}
						});
					}
					//通知client变身形象
					BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_JJ_UP,new Object[]{1,info.getZfzVal(),info.getZuoqiLevel(),0,0});
				}else{
					//祝福值变化通知前端
					BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_SHOW,new Object[]{info.getZfzVal(),info.getZuoqiLevel(),info.getShowId(),info.getLastSjTime()});
				}
				wuqiInfoDao.cacheUpdate(info, userRoleId);
			}else{
				info.setZuoqiLevel(showId);
				info.setZfzVal(0);
				info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
				XinShengJianJiChuConfig  wuqiSjConfig = xinShengJianConfigExportService.loadById(showId);
				float clearTime = wuqiSjConfig.getCztime();
				long zfzCdTime=0l;
				if(clearTime == 0 || info.getZfzVal() == 0){
					zfzCdTime=0l; 
				}else{
					zfzCdTime = GameSystemTime.getSystemMillTime() +(int)(clearTime*60*60*1000);
				}
				info.setLastSjTime(zfzCdTime);
				wuqiInfoDao.cacheUpdate(info, userRoleId);
				//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
				notifyStageWuqiChange(userRoleId);
				wuqiUpdateShow(userRoleId, showId,true);
				if(wuqiSjConfig.isGgopen()){
					UserRole userRole =	roleExportService.getUserRole(userRoleId);
					BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
							AppErrorCode.WQ_SJ_NOTICE, 
							new Object[]{userRole.getName(), showId}
					});
				}
				//通知client变身形象
				BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_JJ_UP,new Object[]{1,info.getZfzVal(),showId,0,0});

				//排行升级提醒活动角标
				ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.SHENG_JIAN_RANK);
			}
		}
		return null;

	}
	
	/**
	 * 圣剑自动升阶
	 * @param userRoleId
	 * @param busMsgQueue
	 * @param isAutoGM
	 * @return
	 */
	public Object[] autoWuQiSj(long userRoleId,BusMsgQueue busMsgQueue,boolean isAutoGM){
		
		WuQiInfo zuoqiInfo = getWuQiInfo(userRoleId);
		if(zuoqiInfo == null){
			return AppErrorCode.WQ_NO_OPEN;
		}
		int maxJjLevel = xinShengJianConfigExportService.getMaxConfigId();
		return wuQiSJ(zuoqiInfo,userRoleId, busMsgQueue, isAutoGM, maxJjLevel, true); 
	}
	
	
	/**
	 * 圣剑进阶
	 * @param userRoleId
	 * @return
	 */
	private Object[] wuQiSJ(WuQiInfo wuqiInfo, Long userRoleId, BusMsgQueue busMsgQueue ,boolean isAutoGM , int targetLevel,boolean isAuto) {
				
		int wuQiLevel = wuqiInfo.getZuoqiLevel();
		int maxLevel = xinShengJianConfigExportService.getMaxConfigId();
		if(wuQiLevel >= maxLevel){
			return AppErrorCode.WQ_LEVEL_MAX;
		}
		
		if(targetLevel <= wuQiLevel || targetLevel >maxLevel){
			return AppErrorCode.WQ_TARGET_LEVEL_ERROR;
		}
		
		XinShengJianJiChuConfig  wuQiSjConfig = xinShengJianConfigExportService.loadById(wuQiLevel);
	 
		if(wuQiSjConfig == null){
			return AppErrorCode.WQ_CONFIG_ERROR;
		}
		
		Map<String,Integer> needResource = new HashMap<>();
		Map<Integer,Integer> beilvRecords = new HashMap<>();
		
		int zfzVal = wuqiInfo.getZfzVal();//getZfzValue(userRoleId);
		
//		Object[] result = wuQiSj(wuQiLevel,needResource, userRoleId, true, maxJjLevel,false,targetLevel,zfzVal,isAuto,wuQiSjConfig.getGold(),wuQiSjConfig.getBgold(),0,beilvRecords,isAutoGM);
		Object[] result = wuQiSjOther(needResource, userRoleId, false, maxLevel, targetLevel, isAuto, beilvRecords, isAutoGM);//(wuQiLevel,needResource, userRoleId, true, maxJjLevel,false,targetLevel,zfzVal,isAuto,wuQiSjConfig.getGold(),wuQiSjConfig.getBgold(),0,beilvRecords,isAutoGM);
		
		//	result:{errorCode,qhlevel,zfzVal}
		Object[] erroCode=(Object[])result[0];
		if(erroCode!=null){
			return erroCode;
		}
		 
		int newlevel=(Integer)result[1];
		int newZfz=(Integer)result[2];
		int upCount =(int)result[3];
		Integer newNeedMoney = needResource.remove(GoodsCategory.MONEY+"");
		Integer newNeedGold = needResource.remove(GoodsCategory.GOLD+"");
		Integer newNeedBgold = needResource.remove(GoodsCategory.BGOLD+"");
		
		// 扣除金币
		if(newNeedMoney != null && newNeedMoney>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, newNeedMoney, userRoleId,  LogPrintHandle.CONSUME_WUQI_SJ, true, LogPrintHandle.CBZ_WUQI_SJ);
		}
		// 扣除元宝
		if(newNeedGold != null && newNeedGold>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId, LogPrintHandle.CONSUME_WUQI_SJ, true,LogPrintHandle.CBZ_WUQI_SJ); 
//			//腾讯OSS消费上报
//			if(PlatformConstants.isQQ()){
//				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,newNeedGold,LogPrintHandle.CONSUME_ZUOQI_SJ,QQXiaoFeiType.CONSUME_ZUOQI_SJ,1});
//			}
		}
		// 扣除绑定元宝
		if(newNeedBgold != null && newNeedBgold>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId, LogPrintHandle.CONSUME_WUQI_SJ, true,LogPrintHandle.CBZ_WUQI_SJ); 
//			//腾讯OSS消费上报
//			if(PlatformConstants.isQQ()){
//				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,newNeedBgold,LogPrintHandle.CONSUME_ZUOQI_SJ,QQXiaoFeiType.CONSUME_ZUOQI_SJ,1});
//			}
		}
		
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(needResource, userRoleId, GoodsSource.WQ_SJ, true, true);
		
		
		
		wuqiInfo.setZfzVal(newZfz);
		wuqiInfo.setZuoqiLevel(newlevel);
		XinShengJianJiChuConfig  wuQiSJConfig = xinShengJianConfigExportService.loadById(newlevel);
		if(newZfz > 0 && (newlevel != wuQiLevel || zfzVal ==0)) {
			long zfzCdTime=0l;
			
			float clearTime = wuQiSJConfig.getCztime();
			if(clearTime == 0){
				zfzCdTime=0l; 
			}else{
				zfzCdTime = GameSystemTime.getSystemMillTime() +(int)(clearTime*60*60*1000);
			}
			wuqiInfo.setLastSjTime(zfzCdTime);
		}else if(newZfz == 0){
			wuqiInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			wuqiInfo.setLastSjTime(0l);
			//成就推送
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_WUQI,  wuQiSJConfig.getLevel()});
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			}
		}
		
		wuqiInfoDao.cacheUpdate(wuqiInfo, userRoleId);
		
		//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
		if(newlevel > wuQiLevel){
			notifyStageWuqiChange(userRoleId);
			
			//星级
//			XinShengJianJiChuConfig nextWQConfig = xinShengJianConfigExportService.loadById(newlevel);
			if(wuQiSJConfig != null && wuQiSJConfig.getLevel() != wuQiSjConfig.getLevel()){
				
				wuqiUpdateShow(userRoleId, wuQiSJConfig.getShowId(),true);
				
	//			//等级发生变化，检测圣剑升阶返利
	//			wuQiFanli(userRoleId,newlevel);
				
				//是否聊天窗口通知
				if(wuQiSJConfig.isGgopen()){
					UserRole userRole =	roleExportService.getUserRole(userRoleId);
					BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{AppErrorCode.WQ_SJ_NOTICE, 
							new Object[]{userRole.getName(), wuQiSJConfig.getLevel()}
					});
				}
	
				//排行升级提醒活动角标
				ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.SHENG_JIAN_RANK);
			}
		}
		
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A28);
		//支线
		taskBranchService.completeBranch(userRoleId, BranchEnum.B2, upCount);
		
		// 记录操作日志
		JSONArray consumeItemArray = new JSONArray(); 
		LogFormatUtils.parseJSONArray(bagSlots,consumeItemArray);
		GamePublishEvent.publishEvent(new WuQiLogEvent(LogPrintHandle.WUQI_SJ, userRoleId, newNeedMoney, newNeedGold, consumeItemArray, wuQiLevel, newlevel, zfzVal, newZfz));
		
		return new Object[]{1,getShowZfz(newlevel, newZfz),newlevel,CovertObjectUtil.object2int(beilvRecords.get(GameConstants.RATIO_2)),CovertObjectUtil.object2int(beilvRecords.get(GameConstants.RATIO_1)),newZfz - zfzVal};
	}
	
	private int getShowZfz(int configId,int newZfz){
		if(configId >0){
			XinShengJianJiChuConfig config = xinShengJianConfigExportService.loadById(configId -1);
			return newZfz - config.getRealNeedZfz();
		}
		return newZfz;
	}
	
//	/**
//	 * 返利统计 
//	 * @param userRoleId
//	 * @param newlevel
//	 */
//	private void wuQiFanli(Long userRoleId,int newlevel){
//		WuQiFanLiPublicConfig config =  gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_WUQIFANLI);
//		if(config == null){
//			return ;
//		}
//		long expireTime = DatetimeUtil.getNextTime(0, 0);
//		expireTime += GameConstants.DAY_TIME;//过一天
//		
//		if(DatetimeUtil.twoDaysDiffence(ServerInfoServiceManager.getInstance().getServerStartTime().getTime()) < config.getKfTime() && newlevel == config.getFlid()){
//			String content = EmailUtil.getCodeEmail(GameConstants.WUQI_FANLI);
//			String title = EmailUtil.getCodeEmail(GameConstants.WUQI_FANLI_TITLE);
//			emailExportService.sendEmailToOneExpire(userRoleId,title, content,GameConstants.EMAIL_TYPE_SINGLE, config.getFlitem(),expireTime);
//		}
//	}
	
//	@Autowired
//	private EmailExportService emailExportService;
	
//	/**
//	 *  圣剑升阶
//	 * @param zqLevel
//	 * @param needResource
//	 * @param userRoleId
//	 * @param isSendErrorCode
//	 * @param maxLevel
//	 * @param isAutoGM
//	 * @param targetLevel
//	 * @param zfzVal
//	 * @param isAuto
//	 * @param beilvRecords
//	 * @param isUseWND
//	 * @return
//	 */
//	private Object[] wuQiSj(int zqLevel,Map<String,Integer> needResource,
//			long userRoleId,boolean isSendErrorCode,int maxLevel,
//			boolean isAutoGM,int targetLevel,int zfzVal,boolean isAuto,
//			int yb,int byb,int upCount
//			,Map<Integer,Integer> beilvRecords,boolean isUseWND){
//		
//		if(zqLevel >= targetLevel){
//			 return new Object[]{null,zqLevel,upCount};
//		}
//		
//		XinShengJianJiChuConfig wqConfig = xinShengJianConfigExportService.loadById(zqLevel);
//		if(wqConfig==null){
//			Object[] errorCode = isSendErrorCode?AppErrorCode.WQ_CONFIG_ERROR:null;
//			return new Object[]{errorCode,zqLevel,zfzVal,upCount};
//		}
//		
//		//如果祝福值大于了最大值，算强化成功
//		int maxzf = wqConfig.getRealNeedZfz();
//		if(zfzVal < maxzf ){
//			Map<String,Integer> tempResources = new HashMap<>();
//			
//			int money = wqConfig.getMoney();
//			int oldMoney = needResource.get(GoodsCategory.MONEY)== null ? 0 :needResource.get(GoodsCategory.MONEY);
//			Object[] isOb=roleBagExportService.isEnought(GoodsCategory.MONEY, money + oldMoney, userRoleId);
//			if(null != isOb){
//				Object[] errorCode = isSendErrorCode ? isOb : null;
//				return new Object[]{errorCode,zqLevel,zfzVal,upCount};
//			}
//			tempResources.put(GoodsCategory.MONEY+"",money);
//			
//			List<String> needGoodsIds = xinShengJianConfigExportService.getConsumeIds(wqConfig.getConsumeItems(),isUseWND);
//			int needCount = wqConfig.getNumber();
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
//				
//				int price = yb;
//				
//				int nowNeedGold = needCount * price;
//				
//				Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD,nowNeedGold, userRoleId);
//				if(null != goldError){ 
//					Object[] errorCode=isSendErrorCode?goldError:null;
//					return new Object[]{errorCode,zqLevel,zfzVal,upCount};
//				}
//				
//				tempResources.put(GoodsCategory.GOLD+"", nowNeedGold);
//				needCount = 0;
//			}
//			
//			if(needCount > 0){
//				Object[] errorCode = isSendErrorCode ? AppErrorCode.WQ_GOODS_BZ : null;
//				return new Object[]{errorCode,zqLevel,zfzVal,upCount};
//			}
//			ObjectUtil.mapAdd(needResource, tempResources);
//		
//		
//	//		boolean flag = isSJSuccess(zfzVal, wqConfig);
//	//		if(!flag){
//			//星级
//			int[] targetVal = getAddZfzBL(wqConfig);
//			if(targetVal == null){
//				ChuanQiLog.error("wuqi beilv up weight is null,configId = "+ wqConfig.getId());
//				targetVal = new int[]{GameConstants.RATIO_3,1};
//			}
//			beilvRecords.put(targetVal[0], CovertObjectUtil.object2int(beilvRecords.get(targetVal[0]))+ 1);
//			zfzVal += RandomUtil.getIntRandomValue(wqConfig.getZfzmin2(), wqConfig.getZfzmin3()+1) * targetVal[1];
//			
//			//修炼任务
//	  		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.JINJIE_SHENGJIAN, null});
//		}
//		if(zfzVal >= maxzf ){
//			++zqLevel;
//		}
//		upCount ++;
//		//如果不是自动,成功与否都退出
//		if(!isAuto){
//			return new Object[]{null,zqLevel,zfzVal,upCount};
//		}
//		
//		//成功之后达到了指定的目标等级就停止
//		if(targetLevel <= zqLevel) {
//			return new Object[]{null,zqLevel,zfzVal,upCount};
//		}
//		
//		return wuQiSj(zqLevel, needResource, userRoleId, false, maxLevel, isAutoGM, targetLevel, zfzVal, isAuto,wqConfig.getGold(),wqConfig.getBgold(),upCount,beilvRecords,isUseWND);
//	}
	
	
	public Object[] wuQiSjOther(Map<String,Integer> needResource,
			long userRoleId,boolean isSendErrorCode,int maxLevel,
			int targetLevel,
			boolean isAuto,Map<Integer,Integer> beilvRecords,boolean isUseWND){
		
		WuQiInfo info = getWuQiInfo(userRoleId); 
		int zqLevel = info.getZuoqiLevel();
		int zfzVal = info.getZfzVal();
		
		for(int upCount=0;upCount<10000000;upCount++){
			if(zqLevel >= targetLevel){
				 return new Object[]{null,zqLevel,upCount};
			}
			
			XinShengJianJiChuConfig wqConfig = xinShengJianConfigExportService.loadById(zqLevel);
			if(wqConfig==null){
				Object[] errorCode = isSendErrorCode?AppErrorCode.WQ_CONFIG_ERROR:null;
				return new Object[]{errorCode,zqLevel,zfzVal,upCount};
			}
			//如果祝福值大于了最大值，算强化成功
			int maxzf = wqConfig.getRealNeedZfz();
			if(zfzVal < maxzf ){
				Map<String,Integer> tempResources = new HashMap<>();
				
				int money = wqConfig.getMoney();
				int oldMoney = needResource.get(GoodsCategory.MONEY)== null ? 0 :needResource.get(GoodsCategory.MONEY);
				Object[] isOb=roleBagExportService.isEnought(GoodsCategory.MONEY, money + oldMoney, userRoleId);
				if(null != isOb){
					Object[] errorCode = isSendErrorCode ? isOb : null;
					return new Object[]{errorCode,zqLevel,zfzVal,upCount};
				}
				tempResources.put(GoodsCategory.MONEY+"",money);
				
				List<String> needGoodsIds = xinShengJianConfigExportService.getConsumeIds(wqConfig.getConsumeItems(),isUseWND);
				int needCount = wqConfig.getNumber();
				
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
					Object[] errorCode = isSendErrorCode ? AppErrorCode.WQ_GOODS_BZ : null;
					return new Object[]{errorCode,zqLevel,zfzVal,upCount};
				}
				ObjectUtil.mapAdd(needResource, tempResources);
			
				//星级
				int[] targetVal = getAddZfzBL(wqConfig);
				if(targetVal == null){
					ChuanQiLog.error("wuqi beilv up weight is null,configId = "+ wqConfig.getId());
					targetVal = new int[]{GameConstants.RATIO_3,1};
				}
				beilvRecords.put(targetVal[0], CovertObjectUtil.object2int(beilvRecords.get(targetVal[0]))+ 1);
				zfzVal += RandomUtil.getIntRandomValue(wqConfig.getZfzmin2(), wqConfig.getZfzmin3()+1) * targetVal[1];
				
				//修炼任务
		  		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.JINJIE_SHENGJIAN, null});
			}
			
			
			if(zfzVal >= maxzf ){
				++zqLevel;
			}
			//如果不是自动,成功与否都退出
			if(!isAuto){
				return new Object[]{null,zqLevel,zfzVal,upCount};
			}
			
			//成功之后达到了指定的目标等级就停止
			if(targetLevel <= zqLevel) {
				return new Object[]{null,zqLevel,zfzVal,upCount};
			}
		}
		return new Object[]{AppErrorCode.DATA_ERROR,zqLevel,zfzVal,0};
		
		
		
		
				
		
//		if(zfzVal < maxzf ){
//			Map<String,Integer> tempResources = new HashMap<>();
//			
//			int money = wqConfig.getMoney();
//			int oldMoney = needResource.get(GoodsCategory.MONEY)== null ? 0 :needResource.get(GoodsCategory.MONEY);
//			Object[] isOb=roleBagExportService.isEnought(GoodsCategory.MONEY, money + oldMoney, userRoleId);
//			if(null != isOb){
//				Object[] errorCode = isSendErrorCode ? isOb : null;
//				return new Object[]{errorCode,zqLevel,zfzVal,upCount};
//			}
//			tempResources.put(GoodsCategory.MONEY+"",money);
//			
//			List<String> needGoodsIds = xinShengJianConfigExportService.getConsumeIds(wqConfig.getConsumeItems(),isUseWND);
//			int needCount = wqConfig.getNumber();
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
//				
//				int price = yb;
//				
//				int nowNeedGold = needCount * price;
//				
//				Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD,nowNeedGold, userRoleId);
//				if(null != goldError){ 
//					Object[] errorCode=isSendErrorCode?goldError:null;
//					return new Object[]{errorCode,zqLevel,zfzVal,upCount};
//				}
//				
//				tempResources.put(GoodsCategory.GOLD+"", nowNeedGold);
//				needCount = 0;
//			}
//			
//			if(needCount > 0){
//				Object[] errorCode = isSendErrorCode ? AppErrorCode.WQ_GOODS_BZ : null;
//				return new Object[]{errorCode,zqLevel,zfzVal,upCount};
//			}
//			ObjectUtil.mapAdd(needResource, tempResources);
//		
//		
//	//		boolean flag = isSJSuccess(zfzVal, wqConfig);
//	//		if(!flag){
//			//星级
//			int[] targetVal = getAddZfzBL(wqConfig);
//			if(targetVal == null){
//				ChuanQiLog.error("wuqi beilv up weight is null,configId = "+ wqConfig.getId());
//				targetVal = new int[]{GameConstants.RATIO_3,1};
//			}
//			beilvRecords.put(targetVal[0], CovertObjectUtil.object2int(beilvRecords.get(targetVal[0]))+ 1);
//			zfzVal += RandomUtil.getIntRandomValue(wqConfig.getZfzmin2(), wqConfig.getZfzmin3()+1) * targetVal[1];
//			
//			//修炼任务
//	  		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.JINJIE_SHENGJIAN, null});
//		}
//		if(zfzVal >= maxzf ){
//			++zqLevel;
//		}
//		upCount ++;
//		//如果不是自动,成功与否都退出
//		if(!isAuto){
//			return new Object[]{null,zqLevel,zfzVal,upCount};
//		}
//		
//		//成功之后达到了指定的目标等级就停止
//		if(targetLevel <= zqLevel) {
//			return new Object[]{null,zqLevel,zfzVal,upCount};
//		}
		
//		return wuQiSj(zqLevel, needResource, userRoleId, false, maxLevel, isAutoGM, targetLevel, zfzVal, isAuto,wqConfig.getGold(),wqConfig.getBgold(),upCount,beilvRecords,isUseWND);
	}
	
	
	
	
	
	
	private int[] getAddZfzBL(XinShengJianJiChuConfig wqConfig){
		return Lottery.getRandomKeyByInteger(wqConfig.getWeights());
	}
	
	
//	/**
//	 * 圣剑升阶
//	 * @param zfzValue
//	 * @param qiLastTime
//	 * @param qhConfig
//	 * @return
//	 */
//	public boolean isSJSuccess(int zfzValue,XinShengJianJiChuConfig zqConfig){
//		 
//		int minzf = zqConfig.getZfzmin(); 
//		
//		if(zfzValue < minzf ){
//			return false;
//		}
//		
//		int pro = zqConfig.getPro();
//		
//		if(RandomUtil.getIntRandomValue(1, 101) > pro){
//			return false;
//		}
//		return true;
//	}
	 
	
	/**
	 * 获取御剑公共配置
	 * @return
	 */
	private XinShengJianPublicConfig getXinShengJianPublicConfig(){
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.XIN_SHENG_JIAN);
	}
	
	/**
	 * 开启圣剑模块
	 * @param userRoleId
	 */
	public WuQiInfo openWuQi(long userRoleId){
		//判定人物等级是否已经在配置表中获得了开启的条件 
		int beginLevel = getXinShengJianPublicConfig().getOpen();
		RoleWrapper roleWrapper = getRoleWrapper(userRoleId);
		if(roleWrapper.getLevel() < beginLevel){
			return null;
		}
		
		WuQiInfo  wuQiInfo = create(userRoleId);
		
		notifyStageWuqiChange(userRoleId);
		
		return wuQiInfo;
	}
	
	/**
	 * 通知场景里面属性变化
	 */
	public void notifyStageWuqiChange(long userRoleId){
		Object[] obj = getWuQiEquips(userRoleId);
		
		WuQiInfo wuQiInfo = getWuQiInfo(userRoleId);
		
		// 推送内部场景圣剑属性变化 
		BusMsgSender.send2Stage(userRoleId,InnerCmdType.INNER_WUQI_CHANGE, new Object[]{wuQiInfo,obj,shenQiExportService.getActivatedShenqiNum(userRoleId),huanhuaExportService.getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_7)});
	}
	
	public Object[] getWuQiEquips(long userRoleId){ 
		List<RoleItemExport> items = roleBagExportService.getEquip(userRoleId,ContainerType.WUQI);
		return  EquipOutputWrapper.getRoleEquipAttribute(items);
	}
	
	
	private RoleWrapper getRoleWrapper(long userRoleId){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper != null){
			return roleWrapper;
		}
		return null;
	}
	
	public WuQiInfo create(long userRoleId){
		WuQiInfo wuqiInfo =new WuQiInfo(); 
		wuqiInfo.setUserRoleId(userRoleId);
		wuqiInfo.setQndCount(0);
		wuqiInfo.setShowId(0);
		wuqiInfo.setZuoqiLevel(0);
		wuqiInfo.setCzdcount(0);
		wuqiInfo.setZfzVal(0);
		wuqiInfo.setZplus(0L);
		wuqiInfo.setLastSjTime(0l);
		wuqiInfo.setIsGetOn(0);
		wuqiInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		//加入缓存
		wuqiInfoDao.cacheInsert(wuqiInfo, userRoleId);
		
		return wuqiInfo;
	}
	
	public WuQiInfo getWuQiInfo(long userRoleId){
		WuQiInfo wuqiInfo = wuqiInfoDao.cacheLoad(userRoleId, userRoleId);
		if(wuqiInfo!=null){
			XinShengJianJiChuConfig  config = xinShengJianConfigExportService.loadById(wuqiInfo.getZuoqiLevel());
			boolean isClear = config.isZfztime();
			long qiLastTime = wuqiInfo.getLastSjTime();
			if(qiLastTime <= GameSystemTime.getSystemMillTime() && isClear){
				wuqiInfo.setZfzVal(0);
				wuqiInfo.setLastSjTime(0L);
				wuqiInfoDao.cacheUpdate(wuqiInfo, userRoleId);
			}
		}
		return wuqiInfo; 
	}

	public WuQiInfo getWuQiDB(long userRoleId) {
		return wuqiInfoDao.dbLoadwuqi(userRoleId);
	}
	
	
	/**
	 * 获得圣剑的属性
	 */
	public Map<String, Long> getWuQiAttrs(Long userRoleId,WuQi wuqi,int dj) {
		if(wuqi == null) return null;
		int zqLevel = wuqi.getWuqiLevel();
		XinShengJianJiChuConfig  sjConfig = xinShengJianConfigExportService.loadById(zqLevel);
		
		Map<String,Long> attrs = sjConfig.getAttrs(); 
		if(attrs == null){
			return null;
		}
		
		attrs = new HashMap<>(attrs);
//		Long speed = attrs.remove(EffectType.x19.name());
		
		List<Integer> huanhuaConfigList = wuqi.getHuanhuaList();
		for(Integer e:huanhuaConfigList){
			YuJianHuanHuaBiaoConfig config = yuJianHuanHuaBiaoConfigExportService.loadById(e);
			if(null == config){
			    continue;
			}
			if(config.isCzd()){
				ObjectUtil.longMapAdd(attrs, config.getAttrs());
			}
		}
		
		if(wuqi.getCzdCount() > 0){
			String czdId = xinShengJianConfigExportService.getCzdId();
			float multi = getCzdMulti(czdId)*wuqi.getCzdCount()/100f + 1f;
			ObjectUtil.longMapTimes(attrs, multi);
		}
		
		//主角加成
		XinShengJianZhuJueJiaChengConfig  yjsxConfig = xinShengJianJCConfigExportService.getXinShengJianJiaChengConfig(zqLevel, dj);
		if(yjsxConfig  != null){
			ObjectUtil.longMapAdd(attrs, yjsxConfig.getDatas());
		}
		
		//潜能丹加成
		if(wuqi.getQndCount() > 0){
			XinShengJianQianNengBiaoConfig qiannengConfig = wuQiQianNengBiaoConfigExportService.getConfig();
			for(int i=0;i<wuqi.getQndCount();i++){
				ObjectUtil.longMapAdd(attrs, qiannengConfig.getAttrs());
			}
		}
		
		//幻化加成
		for(Integer e : huanhuaConfigList){
			YuJianHuanHuaBiaoConfig config = yuJianHuanHuaBiaoConfigExportService.loadById(e);
			if(null == config){
                continue;
            }
			if(!config.isCzd()){
				ObjectUtil.longMapAdd(attrs, config.getAttrs());
			}
		}
		
//		if(speed != null ){
//			attrs.put(EffectType.x19.name(), speed);
//		}
		
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
	 * 更新圣剑战斗力
	 * @param zuoqi
	 */
	public void updateWuQiZplus(Long userRoleId,Long zplus){
		WuQiInfo wuQiInfo = getWuQiInfo(userRoleId);
		if(wuQiInfo != null && zplus != null){
			//变化更新
			if(wuQiInfo.getZplus() != zplus){
				wuQiInfo.setZplus(zplus);
				wuqiInfoDao.cacheUpdate(wuQiInfo, wuQiInfo.getUserRoleId());
			}
		}
	}
	
//	/**
//	 * 获得圣剑移动速度属性
//	 * @param zuoqi
//	 * @return
//	 */
//	public Map<String, Long> getZuoQiSeedAttr(ZuoQi zuoqi) {
//		if(zuoqi == null) return null;
//		
//		XinShengJianJiChuConfig  config = xinShengJianConfigExportService.loadById(zuoqi.getZuoQiLevel());
//		if(config == null ) return null;
//		Map<String,Long> result = new HashMap<>();
//		result.put(EffectType.x20.name(), config.getMoveAttrVal());
//		return result;
//	}
	
//	TODO 坐骑换装
//	public Object[] zuoqiChangeEquip(Long userRoleId, long guid,int targetSlot, int containerType) {
//		//如是脱下 检查身上是否有该装备
//		RoleItemExport roleItemExport = roleBagExportService.getZuoQiEquip(userRoleId, guid,ContainerType.ZUOQIITEM);
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
//		notifyStageWuqiChange(userRoleId);
//		
//		Object[] result=new Object[3];
//		result[0]=1;
//		List<RoleItemOperation> roleItemVos=bagSlots.getRoleItemVos();
//		for (int i=0;i<roleItemVos.size();i++) {
//			result[i+1]=BagOutputWrapper.getOutWrapperData(OutputType.MOVESLOT, roleItemVos.get(i));
//		}
//		return result; 
//	}
	
//	private int getZfzValue(long userRoleId){
//		WuQiInfo  wuQiInfo = getWuQiInfo(userRoleId);
//		if(wuQiInfo == null){
//			return 0;
//		}
//		XinShengJianJiChuConfig  config = xinShengJianConfigExportService.loadById(wuQiInfo.getZuoqiLevel());
//		int zfzValue = wuQiInfo.getZfzVal();
//		boolean isClear = config.isZfztime();
//		long qiLastTime = wuQiInfo.getLastSjTime();
//		if(qiLastTime <= GameSystemTime.getSystemMillTime() && isClear){
//			return 0;
//		}
//		return zfzValue;
//	}
	
	public Object[] wuqiShow(Long userRoleId) {
		WuQiInfo wuqiInfo = getWuQiInfo(userRoleId);
		if(wuqiInfo == null){
			wuqiInfo = openWuQi(userRoleId);//create(userRoleId);
			if(wuqiInfo == null){
				return AppErrorCode.WQ_LEVEL_NO;
			}
		}
		
		return new Object[]{getShowZfz(wuqiInfo.getZuoqiLevel(), wuqiInfo.getZfzVal()),wuqiInfo.getZuoqiLevel(),wuqiInfo.getShowId()};
	}

	public WuQiInfo initWuQi(Long userRoleId) {
		return wuqiInfoDao.initwuqiInfo(userRoleId);
	}

//	public void zuoqiDown(Long userRoleId, boolean state) {
//		WuQiInfo  zuoqiInfo = getWuQiInfo(userRoleId);
//		if(zuoqiInfo == null) {
//			return ;
//		}
//		
//		zuoqiInfo.setIsGetOn(state?GameConstants.ZQ_UP:GameConstants.ZQ_DOWN);
//		wuqiInfoDao.cacheUpdate(zuoqiInfo, userRoleId);
//		
//		//通知场景修改圣剑状态
//		if(KuafuManager.kuafuIng(userRoleId)){
//			KuafuMsgSender.send2KuafuServer(userRoleId,userRoleId, InnerCmdType.ZUOQI_STATE, state); 
//		}else{
//			BusMsgSender.send2Stage(userRoleId, InnerCmdType.ZUOQI_STATE, state); 
//		}
//	}

	/**
	 * 切换圣剑外显示
	 * @param userRoleId
	 * @param showId
	 */
	public Object[] wuqiUpdateShow(Long userRoleId, int showId,boolean checkLevel) {
		WuQiInfo  wuqiInfo = getWuQiInfo(userRoleId);
		if(wuqiInfo == null) {
			return AppErrorCode.WQ_NO_OPEN;
		}
		if(checkLevel){
			if(showId > wuqiInfo.getZuoqiLevel() && showId != wuqiInfo.getShowId().intValue()){
				return AppErrorCode.WQ_NO_SHOW;
			}
		}
		
		wuqiInfo.setShowId(showId);
		wuqiInfoDao.cacheUpdate(wuqiInfo, userRoleId);
		
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.WUQI_SHOW_UPDATE, showId);
		
		return new Object[]{1,showId};
	}
	
//	TODO wind
//	/**
//	 * 持久化圣剑状态到db中
//	 * @param userRoleId
//	 * @param state
//	 */
//	public void zuoqiBusState(Long userRoleId, int state) {
//		ZuoQiInfo zuoqiInfo = getZuoQiInfo(userRoleId);
//		zuoqiInfo.setIsGetOn(state);
//		wuqiInfoDao.cacheUpdate(zuoqiInfo, userRoleId);
//	}

	public List<WuQiRankVo> getWuqiRankVo(int limit) {
		return wuqiInfoDao.getWuQiRankVo(limit);
	}
	
	public Map<String,Long> getWuqiAttr(Long userRoleId){
		WuQiInfo wuqiInfo = getWuQiInfo(userRoleId);
		if(wuqiInfo == null){
			return null;
		}
		List<Integer> huanhuanConfigList = huanhuaExportService.getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_7);
		
		Object[] zqEquips = getWuQiEquips(userRoleId);
		WuQi wuQi =  WuQiUtil.coverToWuQi(wuqiInfo,huanhuanConfigList, zqEquips);
		return getWuQiAttrs(userRoleId, wuQi, getRoleWrapper(userRoleId).getLevel());
	}
	
	
	public void onlineHandle(Long userRoleId){
		WuQiInfo wuqi = getWuQiInfo(userRoleId);
		if(wuqi == null){
			return;
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_QND_NUM, wuqi.getQndCount()!=null?wuqi.getQndCount():0);
		BusMsgSender.send2One(userRoleId, ClientCmdType.WUQI_CZD_NUM, wuqi.getCzdcount()!=null?wuqi.getCzdcount():0);
	}
	
	
	public int getSkillMaxCount(Long userRoleId){
		WuQiInfo wuqi = getWuQiInfo(userRoleId);
		if(wuqi == null){
			return 0 ;
		}
		XinShengJianJiChuConfig sjConfig = xinShengJianConfigExportService.loadById(wuqi.getZuoqiLevel());
		if(sjConfig == null){
			return 0;
		}
		return sjConfig.getJinengge();
	}
   
	/**
     * 玩家下线处理:下圣剑
     * @param userRoleId
     */
    public void offlineHandle(Long userRoleId) {
    	
//        WuQiInfo  zuoqiInfo = getWuQiInfo(userRoleId);
//        if(zuoqiInfo == null) {
//            return ;
//        }
//        zuoqiInfo.setIsGetOn(GameConstants.ZQ_DOWN);
//        wuqiInfoDao.cacheUpdate(zuoqiInfo, userRoleId);
    	
    }
}