package com.junyou.bus.chibang.service;

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
import com.junyou.bus.chibang.ChibangConstants;
import com.junyou.bus.chibang.configure.export.ChiBangJiChuConfig;
import com.junyou.bus.chibang.configure.export.ChiBangJiChuConfigExportService;
import com.junyou.bus.chibang.configure.export.ChiBangQianNengBiaoConfig;
import com.junyou.bus.chibang.configure.export.ChiBangQianNengBiaoConfigExportService;
import com.junyou.bus.chibang.dao.ChiBangInfoDao;
import com.junyou.bus.chibang.entity.ChiBangInfo;
import com.junyou.bus.chibang.manage.ChiBang;
import com.junyou.bus.chibang.util.ChiBangUtil;
import com.junyou.bus.chibang.vo.ChiBangRankVo;
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
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.ChiBangLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.ChiBangPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
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
 *	翅膀
 */
@Service
public class ChiBangService implements IFightVal{

	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		long result = 0;
		ChiBangInfo chiBangInfo = getChiBangInfo(userRoleId);
		if (chiBangInfo == null) {
			ChuanQiLog.debug("chibangInfo is null,userRoleId=" + userRoleId);
			return 0;
		}
		if (fightPowerType == FightPowerType.SWING_JIE) {
			int zqLevel = chiBangInfo.getChibangLevel();
			ChiBangJiChuConfig sjConfig = chiBangJiChuConfigExportService.loadById(zqLevel);

			Map<String, Long> attrs = sjConfig.getAttrs();
			if (attrs == null) {
				ChuanQiLog.debug("getZplus chibang config null,zqLevel=" + zqLevel);
				return 0;
			}
			return CovertObjectUtil.getZplus(attrs);
		} else if (fightPowerType == FightPowerType.HH_SWING) {
			List<Integer> huanhuaConfigList = StageHelper.getHuanhuaExportService().getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_2);
			for (Integer e : huanhuaConfigList) {
				YuJianHuanHuaBiaoConfig config = yuJianHuanHuaBiaoConfigExportService.loadById(e);
				if (null == config) {
					continue;
				}
				result += CovertObjectUtil.getZplus(config.getAttrs());
			}
			return result;
		}
		return 0;
	}

	@Autowired
	private ChiBangInfoDao chiBangInfoDao;
	@Autowired
	private ChiBangJiChuConfigExportService chiBangJiChuConfigExportService; 
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private ChiBangQianNengBiaoConfigExportService  chiBangQianNengBiaoConfigExportService; 
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
		if (goodsConfig.getCategory() != GoodsCategory.CHIBANG_CZD
				&& goodsConfig.getCategory() != GoodsCategory.WANNENG_CZD) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = cbUseCZD(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.CHIBANG_CZ, true, true);
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
		if (goodsConfig.getCategory() != GoodsCategory.CHIBANG_QND
				&& goodsConfig.getCategory() != GoodsCategory.WANNENG_QND) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = cbUseQND(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.CHIBANG_QN, true, true);
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
		ChiBangInfo info = getChiBangInfo(userRoleId);
		if(info == null){
			return AppErrorCode.CB_NO_OPEN;
		}
		ChiBangJiChuConfig chibangConfig = chiBangJiChuConfigExportService.loadById(info.getChibangLevel());
		if(chibangConfig.getQndopen().intValue() != ChibangConstants.QND_OPEN_FLAG){
			return AppErrorCode.CB_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if(info.getQndCount().intValue()+count > chibangConfig.getQndmax()){
			return AppErrorCode.CB_QND_MAX_NUM;
		}
		info.setQndCount(count+info.getQndCount());
		chiBangInfoDao.cacheUpdate(info, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_QND_NUM,info.getQndCount()!=null?info.getQndCount():0);
		//通知场景里面翅膀属性变化
		notifyStageChiBangChange(userRoleId);
		return null;
	}
	
	/**
	 * 使用成长丹
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] cbUseCZD(long userRoleId,int count){
		ChiBangInfo	info = getChiBangInfo(userRoleId);
		if(info == null){
			return AppErrorCode.CB_NO_OPEN;
		}
		ChiBangJiChuConfig chibangConfig = chiBangJiChuConfigExportService.loadById(info.getChibangLevel());
		if(chibangConfig.getCzdopen() != ChibangConstants.CZD_OPEN_FLAG){
			return AppErrorCode.CB_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if(info.getCzdcount().intValue()+count > chibangConfig.getCzdmax()){
			return AppErrorCode.CB_CZD_MAX_NUM;
		}
		info.setCzdcount(count+info.getCzdcount());
		chiBangInfoDao.cacheUpdate(info, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_CZD_NUM,info.getCzdcount()!=null?info.getCzdcount():0);
		//通知场景里面翅膀属性变化
		notifyStageChiBangChange(userRoleId);
		return null;
	}
	
	/**
	 * 翅膀手动升阶
	 * @param userRoleId
	 * @param busMsgQueue
	 * @param isAutoGM
	 * @return
	 */
	public Object[] chiBangSj(long userRoleId,BusMsgQueue busMsgQueue,boolean isAutoGM,boolean isAuto){
		ChiBangInfo info = getChiBangInfo(userRoleId);
		if(info == null){
			return AppErrorCode.CB_NO_OPEN;
		}
		int maxJjLevel = isAuto?chiBangJiChuConfigExportService.getMaxConfigId():info.getChibangLevel()+1;
		return sj(info,userRoleId, busMsgQueue, isAutoGM, maxJjLevel, isAuto); 
	}
	
	/**
	 * 促销奖励直升翅膀
	 * @param userRoleId
	 * @param minLevel
	 * @param maxLevel
	 * @param zfz
	 * @return
	 */
	public Object[] sjByCuxiao(Long userRoleId,int newlevel){
		ChiBangInfo info = getChiBangInfo(userRoleId);
		if(info== null){
			return AppErrorCode.CB_NO_OPEN;
		} 
		
		ChiBangJiChuConfig config = chiBangJiChuConfigExportService.loadById(info.getChibangLevel());
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		int level = config.getLevel();
		
		int showId = chiBangJiChuConfigExportService.getShowId(newlevel);
		
		ChiBangJiChuConfig newConfig = chiBangJiChuConfigExportService.loadById(showId - 1);
		
		if(newlevel > chiBangJiChuConfigExportService.getMaxLevel()){
			return AppErrorCode.IS_MAX_LEVEL;//已满级
		}
		if(newlevel <= level){
			//要升级的等级不能<=当前等级
			return AppErrorCode.CB_TARGET_LEVEL_ERROR;
		}
		info.setChibangLevel(showId);
		info.setZfzVal(newConfig.getRealNeedZfz());
		
		info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		ChiBangJiChuConfig  yuJianJJConfig = chiBangJiChuConfigExportService.loadById(showId);
		chiBangInfoDao.cacheUpdate(info, userRoleId);
		//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
		notifyStageChiBangChange(userRoleId);
		chiBangUpdateShow(userRoleId, showId,true);
		if(yuJianJJConfig.isGgopen()){
			UserRole userRole =	roleExportService.getUserRole(userRoleId);
			BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
					AppErrorCode.CB_SJ_NOTICE, 
					new Object[]{userRole.getName(), newlevel}
			});
		}
		//通知client变身形象
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_JJ_UP,new Object[]{1,getShowZfz(info.getChibangLevel(), info.getZfzVal()),showId,0,0,0});

		//排行升级提醒活动角标
		ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.XIANJIE_YUYI_TYPE);
 
		return null;
	}
	
	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer zfz){
		ChiBangInfo info = getChiBangInfo(userRoleId);
		if(info== null){
			return AppErrorCode.CB_NO_OPEN;
		}else{
			int newlevel = info.getChibangLevel()+1;
			if(newlevel > chiBangJiChuConfigExportService.getMaxConfigId()){
				return AppErrorCode.IS_MAX_LEVEL;//已满级
			}
			if(newlevel < minLevel){
				return AppErrorCode.CB_LEVEL_LIMIT_CAN_NOT_USE;
			}else{
				if(newlevel >= maxLevel){
					// 增加祝福值
					info.setZfzVal(info.getZfzVal()+zfz);
					ChiBangJiChuConfig  yuJianJJConfig = chiBangJiChuConfigExportService.loadById(info.getChibangLevel());
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
						info.setChibangLevel(info.getChibangLevel()+1);
						info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
						//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
						notifyStageChiBangChange(userRoleId);
						chiBangUpdateShow(userRoleId, info.getChibangLevel(),true);
						if(yuJianJJConfig.isGgopen()){
							UserRole userRole =	roleExportService.getUserRole(userRoleId);
							BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
									AppErrorCode.CB_SJ_NOTICE, 
									new Object[]{userRole.getName(), newlevel+1}
							});
						}
						//通知client变身形象
						BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_JJ_UP, 
								new Object[]{1,info.getZfzVal(),info.getChibangLevel(),info.getLastSjTime()});
					}else{
						//祝福值变化通知前端
						BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_SHOW, 
								new Object[]{info.getZfzVal(),info.getChibangLevel(),info.getShowId(),info.getLastSjTime()});
					}
					//info.setLastSjTime(0l);
					//info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
					chiBangInfoDao.cacheUpdate(info, userRoleId);
				}else{
					info.setChibangLevel(newlevel);
					info.setZfzVal(0);
					info.setUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
					ChiBangJiChuConfig  yuJianJJConfig = chiBangJiChuConfigExportService.loadById(newlevel);
					float clearTime = yuJianJJConfig.getCztime();
					long zfzCdTime=0l;
					if(clearTime == 0 || info.getZfzVal() == 0){
						zfzCdTime=0l; 
					}else{
						zfzCdTime = GameSystemTime.getSystemMillTime() +(int)(clearTime*60*60*1000);
					}
					info.setLastSjTime(zfzCdTime);
					chiBangInfoDao.cacheUpdate(info, userRoleId);
					//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
					notifyStageChiBangChange(userRoleId);
					chiBangUpdateShow(userRoleId, newlevel,true);
					if(yuJianJJConfig.isGgopen()){
						UserRole userRole =	roleExportService.getUserRole(userRoleId);
						BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
								AppErrorCode.CB_SJ_NOTICE, 
								new Object[]{userRole.getName(), newlevel+1}
						});
					}
					//通知client变身形象
					BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_JJ_UP, 
							new Object[]{1,info.getZfzVal(),newlevel,info.getLastSjTime()});
				}

				//排行升级提醒活动角标
				ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.XIANJIE_YUYI_TYPE);
			}
		}
		return null;
	}
	 
	
	/**
	 * 进阶
	 * @param userRoleId
	 * @return
	 */
	private Object[] sj(ChiBangInfo info, Long userRoleId, BusMsgQueue busMsgQueue ,boolean isAutoGM , int targetLevel,boolean isAuto) {
				
		int chibangConfigId = info.getChibangLevel();
		ChiBangJiChuConfig config = chiBangJiChuConfigExportService.loadById(chibangConfigId);
		if(config == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		int maxLevel = chiBangJiChuConfigExportService.getMaxConfigId();
		if(chibangConfigId >= maxLevel){
			return AppErrorCode.CB_LEVEL_MAX;
		}
		
		if(targetLevel <= chibangConfigId || targetLevel >maxLevel){
			return AppErrorCode.CB_TARGET_LEVEL_ERROR;
		}
		  
		ChiBangJiChuConfig  chibangSjConfig = chiBangJiChuConfigExportService.loadById(chibangConfigId);
	 
		if(chibangSjConfig == null){
			return AppErrorCode.CB_CONFIG_ERROR;
		}
		
		Map<String,Integer> needResource = new HashMap<String,Integer>();
		Map<Integer,Integer> beilvRecords = new HashMap<>();
		
		int zfzVal = info.getZfzVal();//getZfzValue(userRoleId);
		
//		Object[] result = chibangSj(chibangConfigId,needResource, userRoleId, true, maxJjLevel,false,targetLevel,zfzVal,isAuto,chibangSjConfig.getGold(),chibangSjConfig.getBgold(),beilvRecords,isAutoGM);
		Object[] result = chibangSjOther(needResource, userRoleId, false, maxLevel, targetLevel, isAuto, beilvRecords, isAutoGM);
		
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
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, newNeedMoney, userRoleId,  LogPrintHandle.CONSUME_CHIBANG_SJ, true, LogPrintHandle.CBZ_CHIBANG_SJ);
		}
		// 扣除元宝
		if(newNeedGold != null && newNeedGold>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId, LogPrintHandle.CONSUME_CHIBANG_SJ, true,LogPrintHandle.CBZ_CHIBANG_SJ); 
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,newNeedGold,LogPrintHandle.CONSUME_CHIBANG_SJ,QQXiaoFeiType.CONSUME_CHIBANG_SJ,1});
			}
		}
		// 扣除绑定元宝
		if(newNeedBgold != null && newNeedBgold>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId, LogPrintHandle.CONSUME_CHIBANG_SJ, true,LogPrintHandle.CBZ_CHIBANG_SJ); 
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,newNeedBgold,LogPrintHandle.CONSUME_CHIBANG_SJ,QQXiaoFeiType.CONSUME_CHIBANG_SJ,1});
			}
		}
		
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(needResource, userRoleId, GoodsSource.CB_SJ, true, true);
		
		
		
		info.setZfzVal(newZfz);
		info.setChibangLevel(newlevel);
		ChiBangJiChuConfig  yuJianJJConfig = chiBangJiChuConfigExportService.loadById(newlevel);
		if(newZfz > 0 && (newlevel != chibangConfigId || zfzVal ==0)) {
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
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[]{GameConstants.CJ_CHIBANG,  yuJianJJConfig.getLevel()});
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			}
		}
		
		chiBangInfoDao.cacheUpdate(info, userRoleId);
		
		//如果阶段等级发生变化，需要通知属性变化 ， 直接替换获得的新形象 
		if(newlevel > chibangConfigId){
			notifyStageChiBangChange(userRoleId);
			
			//星级
//			ChiBangJiChuConfig nextConfig = chiBangJiChuConfigExportService.loadById(newlevel);
			
			if(yuJianJJConfig != null && yuJianJJConfig.getLevel() != config.getLevel()){
				chiBangUpdateShow(userRoleId, yuJianJJConfig.getShowId(),true);
				if(yuJianJJConfig.isGgopen()){
					UserRole userRole =	roleExportService.getUserRole(userRoleId);
					BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[]{
							AppErrorCode.CB_SJ_NOTICE, 
							new Object[]{userRole.getName(), yuJianJJConfig.getLevel()}
					});
				}
				
				//排行升级提醒活动角标
				ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.XIANJIE_YUYI_TYPE);
			}
		}
		
		// 记录操作日志
		JSONArray consumeItemArray = new JSONArray(); 
		LogFormatUtils.parseJSONArray(bagSlots,consumeItemArray);
		GamePublishEvent.publishEvent(new ChiBangLogEvent(LogPrintHandle.CHIBANG_SJ, userRoleId, newNeedMoney, newNeedGold, consumeItemArray, chibangConfigId, newlevel, zfzVal, newZfz));
		//活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A4);
		//子任务
		taskBranchService.completeBranch(userRoleId, BranchEnum.B4,1);
		
		return new Object[]{1,getShowZfz(newlevel, newZfz),newlevel,CovertObjectUtil.object2int(beilvRecords.get(GameConstants.RATIO_2)),CovertObjectUtil.object2int(beilvRecords.get(GameConstants.RATIO_1)),newZfz - zfzVal};
	}
	
	private int getShowZfz(int configId,int newZfz){
		if(configId >0){
			ChiBangJiChuConfig config = chiBangJiChuConfigExportService.loadById(configId -1);
			return newZfz - config.getRealNeedZfz();
		}
		return newZfz;
	}
	
	@Autowired
	private TaskBranchService taskBranchService;
	
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
//	private Object[] chibangSj(int zqLevel,Map<String,Integer> needResource,
//			long userRoleId,boolean isSendErrorCode,int maxLevel,boolean isAutoGM,
//			int targetLevel,int zfzVal,boolean isAuto,int yb,int byb
//			,Map<Integer,Integer> beilvRecords,boolean isUseWND){
//		
//		if(zqLevel >= targetLevel){
//			 return new Object[]{null,zqLevel};
//		}
//		
//		ChiBangJiChuConfig zqConfig = chiBangJiChuConfigExportService.loadById(zqLevel);
//		if(zqConfig==null){
//			Object[] errorCode=isSendErrorCode?AppErrorCode.CB_CONFIG_ERROR:null;
//			return new Object[]{errorCode,zqLevel,zfzVal};
//		}
//		
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
//			List<String> needGoodsIds = chiBangJiChuConfigExportService.getConsumeIds(zqConfig.getConsumeItems(),isUseWND);
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
//				Object[] errorCode = isSendErrorCode ? AppErrorCode.CB_GOODS_BZ : null;
//				return new Object[]{errorCode,zqLevel,zfzVal};
//			}
//			ObjectUtil.mapAdd(needResource, tempResources);
//			//星级
//			int[] targetVal = getAddZfzBL(zqConfig);
//			if(targetVal == null){
//				ChuanQiLog.error("wuqi beilv up weight is null,configId = "+ zqConfig.getId());
//				targetVal = new int[]{GameConstants.RATIO_3,1};
//			}
//			beilvRecords.put(targetVal[0], CovertObjectUtil.object2int(beilvRecords.get(targetVal[0]))+ 1);
//			zfzVal += RandomUtil.getIntRandomValue(zqConfig.getZfzmin2(), zqConfig.getZfzmin3()+1) * targetVal[1];
//			
//			//修炼任务
//			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.JINJIE_CHIBANG, null});
//		}
//		if(zfzVal >= maxzf ){
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
//		return chibangSj(zqLevel, needResource, userRoleId, false, maxLevel, isAutoGM, targetLevel, zfzVal, isAuto,zqConfig.getGold(),zqConfig.getBgold(),beilvRecords,isUseWND);
//	}
	
	
	
	/**
	 *  坐骑升阶
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
	public Object[] chibangSjOther(Map<String,Integer> needResource,
			long userRoleId,boolean isSendErrorCode,int maxLevel,
			int targetLevel,
			boolean isAuto,Map<Integer,Integer> beilvRecords,boolean isUseWND){
		
		ChiBangInfo info = getChiBangInfo(userRoleId); 
		int zqLevel = info.getChibangLevel();
		int zfzVal = info.getZfzVal();
		
		for(int upCount=0;upCount<10000000;upCount++){
			if(zqLevel >= targetLevel){
				 return new Object[]{null,zqLevel};
			}
			
			ChiBangJiChuConfig zqConfig = chiBangJiChuConfigExportService.loadById(zqLevel);
			if(zqConfig==null){
				Object[] errorCode=isSendErrorCode?AppErrorCode.CB_CONFIG_ERROR:null;
				return new Object[]{errorCode,zqLevel,zfzVal};
			}
			
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
				
				List<String> needGoodsIds = chiBangJiChuConfigExportService.getConsumeIds(zqConfig.getConsumeItems(),isUseWND);
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
					Object[] errorCode = isSendErrorCode ? AppErrorCode.CB_GOODS_BZ : null;
					return new Object[]{errorCode,zqLevel,zfzVal};
				}
				
				ObjectUtil.mapAdd(needResource, tempResources);
				//星级
				int[] targetVal = getAddZfzBL(zqConfig);
				if(targetVal == null){
					ChuanQiLog.error("chibang beilv up weight is null,configId = "+ zqConfig.getId());
					targetVal = new int[]{GameConstants.RATIO_3,1};
				}
				beilvRecords.put(targetVal[0], CovertObjectUtil.object2int(beilvRecords.get(targetVal[0]))+ 1);
				zfzVal += RandomUtil.getIntRandomValue(zqConfig.getZfzmin2(), zqConfig.getZfzmin3()+1) * targetVal[1];
				
				//修炼任务
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.JINJIE_CHIBANG, null});
			}
			
			if(zfzVal >= maxzf ){
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

	
	private int[] getAddZfzBL(ChiBangJiChuConfig wqConfig){
		return Lottery.getRandomKeyByInteger(wqConfig.getWeights());
	}

	/**
	 * 升阶
	 * @param zfzValue
	 * @param qiLastTime
	 * @param qhConfig
	 * @return
	 */
	public boolean isSJSuccess(int zfzValue,ChiBangJiChuConfig zqConfig){
		 
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
	 * 获取翅膀公共配置
	 * @return
	 */
	private ChiBangPublicConfig getChiBangPublicConfig(){
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_CHIBANG);
	}

	/**
	 * 开启翅膀模块
	 * @param userRoleId
	 */
	public ChiBangInfo openChiBang(long userRoleId){
		//判定人物等级是否已经在配置表中获得了开启的条件 
		int beginLevel = getChiBangPublicConfig().getOpen();
		RoleWrapper roleWrapper = getRoleWrapper(userRoleId);
		if(roleWrapper.getLevel() <beginLevel){
			return null;
		}
		
		ChiBangInfo chibangInfo = create(userRoleId); 
		
		notifyStageChiBangChange(userRoleId);
		return chibangInfo;
	}
	
	/**
	 * 通知场景里面属性变化
	 */
	public void notifyStageChiBangChange(long userRoleId){
		Object[] obj = getEquips(userRoleId);
		
		ChiBangInfo info = getChiBangInfo(userRoleId);
		
		// 推送内部场景 属性变化
		BusMsgSender.send2Stage(userRoleId,InnerCmdType.INNER_CHIBANG_CHANGE, new Object[]{info,obj,shenQiExportService.getActivatedShenqiNum(userRoleId),huanhuaExportService.getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_2)});
	}
	
	public Object[] getEquips(long userRoleId){ 
		List<RoleItemExport> items = roleBagExportService.getEquip(userRoleId,ContainerType.CHIBANGITEM);
		return  EquipOutputWrapper.getRoleEquipAttribute(items);
	}
	
	
	private RoleWrapper getRoleWrapper(long userRoleId){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper != null){
			return roleWrapper;
		}
		return null;
	}
	
	public ChiBangInfo create(long userRoleId){
		ChiBangInfo chibangInfo =new ChiBangInfo(); 
		chibangInfo.setUserRoleId(userRoleId);
		chibangInfo.setQndCount(0);
		chibangInfo.setShowId(0);
		chibangInfo.setChibangLevel(0);
		chibangInfo.setCzdcount(0);
		chibangInfo.setZfzVal(0);
		chibangInfo.setLastSjTime(0l);
		chibangInfo.setIsGetOn(1);
		chibangInfo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		//加入缓存
		chiBangInfoDao.cacheInsert(chibangInfo, userRoleId);
		
		return chibangInfo;
	}
	
	public ChiBangInfo getChiBangInfo(long userRoleId){
		ChiBangInfo zuoqiInfo = chiBangInfoDao.cacheLoad(userRoleId, userRoleId);
		if(zuoqiInfo!=null){
			ChiBangJiChuConfig  config = chiBangJiChuConfigExportService.loadById(zuoqiInfo.getChibangLevel());
			boolean isClear = config.isZfztime();
			long qiLastTime = zuoqiInfo.getLastSjTime();
			if(qiLastTime <= GameSystemTime.getSystemMillTime() && isClear){
				zuoqiInfo.setZfzVal(0);
				zuoqiInfo.setLastSjTime(0L);
				chiBangInfoDao.cacheUpdate(zuoqiInfo, userRoleId);
			}
		}
		return zuoqiInfo; 
	
	}
	public ChiBangInfo getChiBangInfoDB(long userRoleId) {
		return chiBangInfoDao.dbLoadChiBangInfo(userRoleId);
	}
	/**
	 * 获得翅膀的属性
	 */
	public Map<String, Long> getChiBangAttrs(Long userRoleId,ChiBang chiBang) {
		if(chiBang == null) return null;
		int zqLevel = chiBang.getZuoQiLevel();
		ChiBangJiChuConfig  sjConfig = chiBangJiChuConfigExportService.loadById(zqLevel);
		
		Map<String,Long> attrs = sjConfig.getAttrs(); 
		if(attrs == null){
			return null;
		}
		attrs = new HashMap<>(attrs);
		Long speed = attrs.remove(EffectType.x19.name());
		
		List<Integer> huanhuaConfigList = chiBang.getHuanhuaList();
		for(Integer e:huanhuaConfigList){
			YuJianHuanHuaBiaoConfig config = yuJianHuanHuaBiaoConfigExportService.loadById(e);
			if(null == config){
                continue;
            }
			if(config.isCzd()){
				ObjectUtil.longMapAdd(attrs, config.getAttrs());
			}
		}
		
		if(chiBang.getCzdCount() > 0){
			String czdId = chiBangJiChuConfigExportService.getCzdId();
			float multi = getCzdMulti(czdId)*chiBang.getCzdCount()/100f + 1f;
			ObjectUtil.longMapTimes(attrs, multi);
		}
		if(chiBang.getQndCount() > 0){
			ChiBangQianNengBiaoConfig qiannengConfig = chiBangQianNengBiaoConfigExportService.getConfig();
			for(int i=0;i<chiBang.getQndCount();i++){
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
	

//	/**
//	 * 获得坐骑移动速度属性
//	 * @param zuoqi
//	 * @return
//	 */
//	public Map<String, Integer> getZuoQiSeedAttr(ChiBang zuoqi) {
//		if(zuoqi == null) return null;
//		
//		ChiBangJiChuConfig  config = chiBangJiChuConfigExportService.loadById(zuoqi.getZuoQiLevel());
//		if(config == null ) return null;
//		Map<String,Integer> result = new HashMap<>();
//		result.put(EffectType.x20.name(), config.getMoveAttrVal());
//		return result;
//	}
	
	/**
	 * 翅膀换装备  TODO
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
	
//	private int getZfzValue(long userRoleId){
//		ChiBangInfo  info = getChiBangInfo(userRoleId);
//		if(info == null){
//			return 0;
//		}
//		ChiBangJiChuConfig  config = chiBangJiChuConfigExportService.loadById(info.getChibangLevel());
//		int zfzValue = info.getZfzVal();
//		boolean isClear = config.isZfztime();
//		long qiLastTime = info.getLastSjTime();
//		if(qiLastTime <= GameSystemTime.getSystemMillTime() && isClear){
//			return 0;
//		}
//		return zfzValue;
//	}
	
	public Object[] chiBangShow(Long userRoleId) {
		ChiBangInfo info = getChiBangInfo(userRoleId);
		if(info == null){
			info =	openChiBang(userRoleId);
			if(info == null) {
				return AppErrorCode.CHIBANG_LEVEL_NO;
			}
		}
		
		return new Object[]{getShowZfz(info.getChibangLevel(), info.getZfzVal()),info.getChibangLevel(),info.getShowId(),info.getLastSjTime()};
	}

	public ChiBangInfo initChiBang(Long userRoleId) {
		return chiBangInfoDao.initChiBangInfo(userRoleId);
	}
	

	/**
	 * 切换翅膀外显示
	 * @param userRoleId
	 * @param showId
	 */
	public Object[] chiBangUpdateShow(Long userRoleId, int showId,boolean checkLevel) {
		ChiBangInfo  info = getChiBangInfo(userRoleId);
		if(info == null) {
			return AppErrorCode.CB_NO_OPEN;
		}
		if(checkLevel){
			if(showId < info.getChibangLevel() && showId == info.getShowId().intValue()){
				return AppErrorCode.CB_NO_SHOW;
			}
		}
		
		info.setShowId(showId);
		chiBangInfoDao.cacheUpdate(info, userRoleId);
		
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.CHIBANG_SHOW_UPDATE, showId);
		
		return new Object[]{1,showId};
	}

	/**
	 * 更新翅膀战斗力
	 * @param userRoleId
	 * @param zplus
	 */
	public void updateChiBangZplus(Long userRoleId,Long zplus){
		ChiBangInfo  info = getChiBangInfo(userRoleId);
		if(info != null && zplus != null){
			
			if(!info.getZplus().equals(zplus)){
				info.setZplus(zplus);
				chiBangInfoDao.cacheUpdate(info, userRoleId);
			}
		}
	}
	
	
	public List<ChiBangRankVo> getChiBangRankVo(int limit) {
		return chiBangInfoDao.getChiBangRankVo(limit);
	}
	
	public Map<String,Long> getChiBangAttr(Long userRoleId){
		ChiBangInfo chibangInfo = getChiBangInfo(userRoleId);
		if(chibangInfo==null){
			return null;
		}
		List<Integer> huanhuanConfigList = huanhuaExportService.getRoleHuanhuaConfigList(userRoleId, HuanhuaConstants.HUANHUA_TYPE_2);
		Object[] zqEquips = getEquips(userRoleId);
		ChiBang chibang = ChiBangUtil.coverToChiBang(chibangInfo,huanhuanConfigList, zqEquips);
		return getChiBangAttrs(userRoleId, chibang);
	}
	
	public void onlineHandle(Long userRoleId){
		ChiBangInfo chibang = getChiBangInfo(userRoleId);
		if(chibang == null){
			return;
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_QND_NUM,chibang.getQndCount()!=null?chibang.getQndCount():0);
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHIBANG_CZD_NUM,chibang.getCzdcount()!=null?chibang.getCzdcount():0);
	}
	public int getSkillMaxCount(Long userRoleId){
		ChiBangInfo chibangInfo = getChiBangInfo(userRoleId);
		if(chibangInfo==null){
			return 0;
		}
		ChiBangJiChuConfig config = chiBangJiChuConfigExportService.loadById(chibangInfo.getChibangLevel());
		if(config == null){
			return 0;
		}
		return config.getJinengge();
	}
}