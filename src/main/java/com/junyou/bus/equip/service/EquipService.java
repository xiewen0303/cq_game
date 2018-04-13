package com.junyou.bus.equip.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.bus.rfbactivity.service.ActivityServiceFacotry;
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
import com.junyou.gameconfig.constants.AttributeType;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.EquipType;
import com.junyou.gameconfig.constants.EquipTypeSlot;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.entity.RoleAccountWrapper;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.BagOutputWrapper;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.OutputType;
import com.junyou.bus.bag.dao.RoleBagDao;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.bag.export.RoleItemInput;
import com.junyou.bus.bag.utils.BagUtil;
import com.junyou.bus.bag.vo.RoleItemOperation;
import com.junyou.bus.branchtask.BranchEnum;
import com.junyou.bus.branchtask.service.TaskBranchService;
import com.junyou.bus.chibang.export.ChiBangExportService;
import com.junyou.bus.chongwu.configure.export.ChongWuJiHuoBiaoConfig;
import com.junyou.bus.chongwu.entity.RoleChongwu;
import com.junyou.bus.chongwu.service.export.ChongwuExportService;
import com.junyou.bus.equip.EquipContants;
import com.junyou.bus.equip.EquipOutputWrapper;
import com.junyou.bus.equip.configure.export.ChongwuFushuEquipConfigExportService;
import com.junyou.bus.equip.configure.export.ChongwuFushuEquipQianghuaConfig;
import com.junyou.bus.equip.configure.export.ChongwuFushuEquipShengjiConfig;
import com.junyou.bus.equip.configure.export.FuShuEquipShengJiConfig;
import com.junyou.bus.equip.configure.export.FuShuEquipShengJiConfigExportService;
import com.junyou.bus.equip.configure.export.FuShuQiangHuaBiaoConfigExportService;
import com.junyou.bus.equip.configure.export.QiangHuaBiaoConfig;
import com.junyou.bus.equip.configure.export.QiangHuaBiaoConfigExportService;
import com.junyou.bus.equip.configure.export.QiangHuaChuanChengBiaoConfig;
import com.junyou.bus.equip.configure.export.QiangHuaChuanChengBiaoConfigExportService;
import com.junyou.bus.equip.configure.export.RongLianJiChuConfig;
import com.junyou.bus.equip.configure.export.RongLianJiChuConfigExportService;
import com.junyou.bus.equip.configure.export.ShenWuJinJieBiaoConfig;
import com.junyou.bus.equip.configure.export.ShenWuJinJieBiaoConfigExportService;
import com.junyou.bus.equip.configure.export.ShenWuXingZhuBiaoConfig;
import com.junyou.bus.equip.configure.export.ShenWuXingZhuBiaoConfigExportService;
import com.junyou.bus.equip.configure.export.ShengJiBiaoConfig;
import com.junyou.bus.equip.configure.export.ShengJiBiaoConfigExportService;
import com.junyou.bus.equip.configure.export.TaoZhuangZhuShenConfig;
import com.junyou.bus.equip.configure.export.TaoZhuangZhuShenConfigExportService;
import com.junyou.bus.equip.configure.export.TiPinBiaoConfig;
import com.junyou.bus.equip.configure.export.TiPinBiaoConfigExportService;
import com.junyou.bus.equip.configure.export.ZhanLiXiShuConfigExportService;
import com.junyou.bus.huoyuedu.enums.ActivityEnum;
import com.junyou.bus.huoyuedu.export.HuoYueDuExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.qiling.export.QiLingExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoWrapper;
import com.junyou.bus.shenqi.configure.export.ShenQiShuXingConfig;
import com.junyou.bus.shenqi.configure.export.ShenQiShuXingConfigExportService;
import com.junyou.bus.shenqi.entity.ShenQiInfo;
import com.junyou.bus.shenqi.export.ShenQiExportService;
import com.junyou.bus.shenqi.export.ShenQiJinJieExportService;
import com.junyou.bus.tianyu.export.TianYuExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.wuqi.export.WuQiExportService;
import com.junyou.bus.xianjian.export.XianJianExportService;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.bus.zhanjia.export.ZhanJiaExportService;
import com.junyou.bus.zhuansheng.export.ZhuanshengExportService;
import com.junyou.bus.zuoqi.export.ZuoQiExportService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.EquipQHLogEvent;
import com.junyou.event.EquipRecycleLogEvent;
import com.junyou.event.EquipSJLogEvent;
import com.junyou.event.EquipTPLogEvent;
import com.junyou.event.EquipZhuShenLogEvent;
import com.junyou.event.FuShuEquipSJLogEvent;
import com.junyou.event.calculate.AnjinEquipLogEvent;
import com.junyou.event.calculate.ShenwuEquipLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.helper.PublicConfigureHelper;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.configure.export.helper.StageConfigureHelper;
import com.junyou.stage.model.element.role.business.Equip;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.lottery.Lottery;
import com.junyou.utils.lottery.RandomUtil;

/**
 * 装备service
 * @author wind
 * @email 18221610336@163.com
 * @date 2014-12-31 上午10:24:53
 */
@Service
public class EquipService implements IFightVal {

	@Override
	public long getZplus(long userRoleId, int fightPowerType) {

		if(fightPowerType == FightPowerType.EQUIP_JIE){
			List<RoleItemExport> bodysEquip = roleBagExportService.getBodyEquips(userRoleId);
			if(ObjectUtil.isEmpty(bodysEquip)){
				return 0;
			}
			long result = 0;
			for (RoleItemExport  roleItemExport : bodysEquip) {
				String goodsId = roleItemExport.getGoodsId();
				GoodsConfig config =  goodsConfigExportService.loadById(goodsId);
				if(config == null) {
					ChuanQiLog.error("config is not exist !"+goodsId);
					continue;
				}
//				result += CovertObjectUtil.getZplus(config.getEquipBaseAttr());
				result += CovertObjectUtil.getZplus(config.getEquipRealAttr(roleItemExport.getTipinValue()));
			}
			return result;
		}else if(fightPowerType == FightPowerType.EQUIP_QH){

			List<RoleItemExport> bodysEquip = roleBagExportService.getBodyEquips(userRoleId);
			if(ObjectUtil.isEmpty(bodysEquip)){
				return 0;
			}
			long result = 0;
			for (RoleItemExport  roleItemExport : bodysEquip) {
				String goodsId = roleItemExport.getGoodsId();
				GoodsConfig config =  goodsConfigExportService.loadById(goodsId);
				if(config == null) {
					ChuanQiLog.error("config is not exist !"+goodsId);
					continue;
				}
				
				Map<String,Long> tempAttrs = getQHAttrs(roleItemExport.getTipinValue(),config, roleItemExport.getQianhuaLevel(),ContainerType.BODYTITEM);
				if(tempAttrs != null){
					result += CovertObjectUtil.getZplus(tempAttrs);
				}
			}
			return result;
		}else{
			ChuanQiLog.error("not exist "+this.getClass().getSimpleName()+"fightPowerType="+fightPowerType);
		}
		return 0;
	}

	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private QiangHuaBiaoConfigExportService qiangHuaBiaoConfigExportService;
	@Autowired
	private FuShuQiangHuaBiaoConfigExportService fuShuQiangHuaBiaoConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleBagDao roleBagDao;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private ZuoQiExportService zuoQiExportService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;
	@Autowired
	private RongLianJiChuConfigExportService rongLianJiChuConfigExportService;
	@Autowired
	private ShengJiBiaoConfigExportService shengJiBiaoConfigExportService;
	@Autowired
	private HuoYueDuExportService huoYueDuExportService;
	@Autowired
	private TiPinBiaoConfigExportService tiPinBiaoConfigExportService;
	@Autowired
	private ChiBangExportService chiBangExportService;
	@Autowired
	private XianJianExportService xianJianExportService;
	@Autowired
	private ZhanJiaExportService zhanJiaExportService;
	@Autowired
	private QiLingExportService qiLingExportService;
	@Autowired
	private TianYuExportService tianYuExportService;
	@Autowired
	private FuShuEquipShengJiConfigExportService fuShuEquipShengJiConfigExportService;
	@Autowired
	private ShenWuJinJieBiaoConfigExportService shenWuJinJieBiaoConfigExportService;
	@Autowired
	private TaoZhuangZhuShenConfigExportService taoZhuangZhuShenConfigExportService;
	@Autowired
	private ZhuanshengExportService zhuanshengExportService;
	@Autowired
	private ShenWuXingZhuBiaoConfigExportService shenWuXingZhuBiaoConfigExportService;
	@Autowired
	private ChongwuExportService chongwuExportService;
	@Autowired
	private ChongwuFushuEquipConfigExportService chongwuFushuEquipConfigExportService;
	@Autowired
	private ShenQiShuXingConfigExportService shenQiShuXingConfigExportService;
	@Autowired
	private ShenQiExportService shenQiExportService;
	@Autowired
	private ShenQiJinJieExportService shenQiJinJieExportService;
	@Autowired
	private TaskBranchService taskBranchService;
	@Autowired
	private QiangHuaChuanChengBiaoConfigExportService qiangHuaChuanChengBiaoConfigExportService;
	
	@Autowired
	private ZhanLiXiShuConfigExportService zhanLiXiShuConfigExportService;
	
	
	public int getMaxQHLevel(String goodsId) {
		GoodsConfig config = goodsConfigExportService.loadById(goodsId);
		if (config == null) {
			ChuanQiLog.error("物品配置表异常：goodsId=" + goodsId);
			return 0;
		}
		int maxLevel = qiangHuaBiaoConfigExportService.getMaxLevel();
		int goodsMaxQHLevel = config.getMaxIntensify();
		return maxLevel > goodsMaxQHLevel ? goodsMaxQHLevel : maxLevel;
	}
	public ChongwuFushuEquipQianghuaConfig loadChongwuFushuQianghuaConfig(Integer qhLevel) {
		return chongwuFushuEquipConfigExportService.loadQianghuaConfig(qhLevel);
	}
	public ChongwuFushuEquipShengjiConfig loadChongwuFushuShengjieConfig(Integer rank) {
		return chongwuFushuEquipConfigExportService.loadShengjieConfig(rank);
	}
	/**
	 * 对宠物附属装备操作前的基本信息校验
	 * @param userRoleId 玩家编号
	 * @param chongwuConfigId 宠物配置编号
	 * @return
	 */
	private Object[] checkChongwuEquipInfo(Long userRoleId, Integer chongwuConfigId) {
		/* 宠物校验 */
		ChongWuJiHuoBiaoConfig config = chongwuExportService.loadChongwuJiHuoConfigById(chongwuConfigId);
		if (null == config) {
			return new Object[] { false, AppErrorCode.CONFIG_ERROR };
		}
		/* 此宠物附属装备功能尚未开启 */
		if (!config.isEquipOpen()) {
			return new Object[] { false, AppErrorCode.CHONGWU_NOT_OPEN };
		}
		/* 宠物尚未激活 */
		RoleChongwu roleChongwu = chongwuExportService.getRoleChongwuById(userRoleId, chongwuConfigId);
		if (null == roleChongwu) {
			return new Object[] { false, AppErrorCode.CHONGWU_NOT_ACTIVATED };
		}
		return new Object[] { true };
	}
	
	/**
	 * 对神器附属装备操作前的基本信息校验
	 * @param userRoleId 玩家编号
	 * @param shenQiConfigId 宠物配置编号
	 * @return
	 */
	private Object[] checkShenQiEquipInfo(Long userRoleId, Integer shenQiConfigId) {
		/* 宠物校验 */
		ShenQiShuXingConfig config = shenQiShuXingConfigExportService.loadById(shenQiConfigId);
		if (config == null) {
			// 此神器不存在
			return new Object[] { false, AppErrorCode.SHENQI_NOT_EXISTS};
		}
		
		ShenQiInfo entity = shenQiExportService.getShenQiInfo(userRoleId, shenQiConfigId);
		if(entity == null){
			return new Object[] { false, AppErrorCode.SHENQI_IS_NOT_ACTIVATED};
		}
		return new Object[] { true };
	}
	
	/**
	 * 强化
	 * @param userRoleId
	 * @param guid
	 * @param busMsgQueue
	 * @return
	 */
	public Object[] equipQH(Long userRoleId, long guid, BusMsgQueue busMsgQueue, boolean isAutoGM) {
		RoleItemExport roleItem = null;
		ContainerType type = null;
		int qhCount = 0;
		for (ContainerType containerType : ContainerType.values()) {
			if (containerType.isEquip()) {
				roleItem = roleBagExportService.getOtherEquip(userRoleId, guid, containerType);
				if (roleItem != null) {
					type = containerType;
					break;
				}
			}
		}
		if (roleItem == null) {
			return AppErrorCode.NO_QH_EQUIP;
		}
		int qhLevel = roleItem.getQianhuaLevel() == null ? 0 : roleItem.getQianhuaLevel();
		int maxLevel = getMaxQHLevel(roleItem.getGoodsId());
		if (qhLevel >= maxLevel) {
			return AppErrorCode.EQUIP_MAX_LEVEL;
		}
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleItem.getGoodsId());
		if (goodsConfig == null) {
			return AppErrorCode.NO_FIND_CONFIG;
		}
		if (goodsConfig.getRareLevel() < EquipContants.QIANGHUA_MIN_RARELEVEL) {
			return AppErrorCode.NO_QH;
		}
		// int position = goodsConfig.getEqpart();
		int nextQHLevel = qhLevel + 1;
		QiangHuaBiaoConfig qhConfig = null;
		if (ContainerType.BODYTITEM.equals(type)) {
			qhConfig = qiangHuaBiaoConfigExportService.getQHConfig(nextQHLevel);
		} else {
			qhConfig = fuShuQiangHuaBiaoConfigExportService.getQHConfig(nextQHLevel);
		}
		if (qhConfig == null) {
			return AppErrorCode.EQUIP_LEVEL_ERROR;
		}
		List<String> needGoodsIds = shengJiBiaoConfigExportService.getConsumeIds(qhConfig.getNeedItemId());
		int needCount = qhConfig.getNeedItemCount();
		Map<String, Integer> tempResources = new HashMap<>();
		for (String goodsId : needGoodsIds) {
			// int oldNeedCount = needResource.get(goodsId) == null ? 0 : needResource.get(goodsId);
			int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
			if (owerCount >= needCount) {
				tempResources.put(goodsId, needCount);
				needCount = 0;
				break;
			}
			needCount = needCount - owerCount;
			tempResources.put(goodsId, owerCount);
		}
		if (isAutoGM && needCount > 0) {
			int bPrice = qhConfig.getBgold();// 绑定元宝的价格
			if (bPrice < 1) {
				return AppErrorCode.QIANGHUA_GOODS_ERROR;
			}
			int bCount = 0;
			int nowNeedBgold = 0;
			for (int i = 0; i < needCount; i++) {
				nowNeedBgold = (bCount + 1) * bPrice;
				Object[] bgoldError = roleBagExportService.isEnought(GoodsCategory.BGOLD, nowNeedBgold, userRoleId);
				if (null != bgoldError) {
					break;
				}
				bCount++;
			}
			nowNeedBgold = bCount * bPrice;
			tempResources.put(GoodsCategory.BGOLD + "", nowNeedBgold);
			needCount = needCount - bCount;
			int price = qhConfig.getGold();// 需要通过商城配置表获得对应物品的价格
			if (price < 1) {
				return AppErrorCode.CONFIG_ERROR;
			}
			int nowNeedGold = needCount * price;
			Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, nowNeedGold, userRoleId);
			if (null != goldError) {
				return AppErrorCode.YB_ERROR;
			}
			tempResources.put(GoodsCategory.GOLD + "", nowNeedGold);
			needCount = 0;
		} else {
			if (needCount > 0) {
				return AppErrorCode.QIANGHUA_GOODS_ERROR;
			}
		}
		// 升级需要消耗的银两
		int yl = qhConfig.getNeedMoney();
		Object[] isOb = accountExportService.isEnought(GoodsCategory.MONEY, yl, userRoleId);
		if (null != isOb) {
			return AppErrorCode.JB_ERROR;
		}
		Integer newNeedGold = tempResources.remove(GoodsCategory.GOLD + "");
		Integer newNeedBgold = tempResources.remove(GoodsCategory.BGOLD + "");
		// 扣除金币
		if (yl > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, yl, userRoleId,
					LogPrintHandle.CONSUME_EQUIP_QH, true, LogPrintHandle.CBZ_EQUIP_QH);
		}
		// 扣除元宝
		if (newNeedGold != null && newNeedGold > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId,
					LogPrintHandle.CONSUME_EQUIP_QH, true, LogPrintHandle.CBZ_EQUIP_QH);
			// 腾讯OSS消费上报
			if (PlatformConstants.isQQ()) {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] {
						QqConstants.ZHIFU_YB, newNeedGold, LogPrintHandle.CONSUME_EQUIP_QH + "",
						QQXiaoFeiType.CONSUME_EQUIP_QH, 1 });
			}
		} else {
			newNeedGold = 0;
		}
		// 扣除绑定元宝
		if (newNeedBgold != null && newNeedBgold > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId,
					LogPrintHandle.CONSUME_EQUIP_QH, true, LogPrintHandle.CBZ_EQUIP_QH);
			// 腾讯OSS消费上报
			if (PlatformConstants.isQQ()) {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] {
						QqConstants.ZHIFU_BYB, newNeedGold, LogPrintHandle.CONSUME_EQUIP_QH + "",
						QQXiaoFeiType.CONSUME_EQUIP_QH, 1 });
			}
		} else {
			newNeedBgold = 0;
		}
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(tempResources, userRoleId, GoodsSource.EQUIP_QH,
				true, true);
		int successrate = qhConfig.getSuccessrate();
		boolean flag = isQHSuccess(successrate);
		int realLevel = qhLevel;
		if (flag) {
			roleBagExportService.updateQHLevel(nextQHLevel, guid, userRoleId);
			realLevel = qhLevel + 1;
			// 如果穿在身上,需要通知属性变化
			notifyOtherRoleSync(userRoleId, null, type);
			// 强化成功成就推送
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[] {	GameConstants.CJ_QIANGHUA, 0 });
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE_TS, new Object[] {XiuLianConstants.QIANGHUA_ALL_LEVEL, roleBagExportService.getAllEquipsQHLevel(userRoleId)});
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.QIANGHUA_LEVEL, nextQHLevel});

				//排行升级提醒活动角标
				ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.XIANZHUANG_QH_TYPE);
			} catch (Exception e) {
				ChuanQiLog.error("" + e);
			}
		}
		// 活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A2);
		
		taskBranchService.completeBranch(userRoleId, BranchEnum.B1,1);
//		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_BRANCH_COUNT, new Object[]{BranchEnum.A2,1});
		
		// 记录强化操作日志
		JSONArray consumeItemArray = new JSONArray();
		LogFormatUtils.parseJSONArray(bagSlots, consumeItemArray);
		recordEquipQHLog(userRoleId, guid, roleItem.getGoodsId(), consumeItemArray, yl, newNeedGold, newNeedBgold,
				qhLevel, realLevel);
		return new Object[] { 1, guid, realLevel };
	}
	private boolean isQHSuccess(int successrate) {
		int randomValue = RandomUtil.getIntRandomValue(1, 10001);
		if (successrate >= randomValue) {
			return true;
		}
		return false;
	}
	/**
	 * 强化传承
	 * @param userRoleId
	 * @param lordGuid
	 * @param busMsgQueue
	 * @return
	 */
	public Object[] equipQH(Long userRoleId, long lordGuid,long viceGuid, BusMsgQueue busMsgQueue) {
		RoleItem lordRoleItem = roleBagDao.cacheLoad(lordGuid, userRoleId);;//主装备（转移强化等级的装备）
		RoleItem viceRoleItem = roleBagDao.cacheLoad(viceGuid, userRoleId);;//副装备（接收强化等级的装备）
		ContainerType type = null;
		for (ContainerType containerType : ContainerType.values()) {
			if (containerType.isEquip()) {
				RoleItemExport lItem = roleBagExportService.getOtherEquip(userRoleId, lordGuid, containerType);
				RoleItemExport vItem = roleBagExportService.getOtherEquip(userRoleId, viceGuid, containerType);
				if (lItem != null || vItem != null) {
					type = containerType;
					break;
				}
			}
		}
		if (lordRoleItem == null || viceRoleItem == null) {
			return AppErrorCode.EQUIP_NOT;
		}
		int lQhLevel = lordRoleItem.getQianhuaLevel() == null ? 0 : lordRoleItem.getQianhuaLevel();
		int vQhLevel = viceRoleItem.getQianhuaLevel() == null ? 0 : viceRoleItem.getQianhuaLevel();
		if(lQhLevel <= vQhLevel){
			return AppErrorCode.EQUIP_NOT_QIANGHUA_LEVEL;
		}
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(viceRoleItem.getGoodsId());
		if (goodsConfig == null) {
			return AppErrorCode.NO_FIND_CONFIG;
		}
		if (goodsConfig.getRareLevel() < EquipContants.QIANGHUA_MIN_RARELEVEL) {
			return AppErrorCode.EQUIP_NOT_QIANGHUA;
		}
		QiangHuaChuanChengBiaoConfig qhConfig = qiangHuaChuanChengBiaoConfigExportService.getConfig();
		if (qhConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		//需要消耗的银两
		int yl = qhConfig.getNeedMoney();
		Object[] isOb = accountExportService.isEnought(GoodsCategory.MONEY, yl, userRoleId);
		if (null != isOb) {
			return AppErrorCode.JB_ERROR;
		}
		// 扣除金币
		if (yl > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, yl, userRoleId,LogPrintHandle.CONSUME_EQUIP_QH, true, LogPrintHandle.CBZ_EQUIP_QH);
		}
		int successrate = qhConfig.getSuccessrate();
		boolean flag = isQHSuccess(successrate);
		if (flag) {
			//交换强化等级
			lordRoleItem.setQianhuaLevel(vQhLevel);
			viceRoleItem.setQianhuaLevel(lQhLevel);
			roleBagDao.cacheUpdate(lordRoleItem, userRoleId);
			roleBagDao.cacheUpdate(viceRoleItem, userRoleId);
			// 如果穿在身上,需要通知属性变化
			notifyOtherRoleSync(userRoleId, null, type);
			// 成功成就推送
			try {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[] {	GameConstants.CJ_QIANGHUA, 0 });
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE_TS, new Object[] {XiuLianConstants.QIANGHUA_ALL_LEVEL, roleBagExportService.getAllEquipsQHLevel(userRoleId)});
			} catch (Exception e) {
				ChuanQiLog.error("" + e);
			}
		}
		// 记录强化操作日志
		return new Object[] { 1, lordGuid, viceGuid };
	}
	/**
	 * 装备强化日志记录
	 * @param userRoleId
	 * @param type
	 * @param guid
	 * @param goodsId
	 * @param consumeItem
	 * @param consumeMoney
	 * @param beginLevel
	 * @param endLevel
	 */
	private void recordEquipQHLog(long userRoleId, long guid, String goodsId, JSONArray consumeItem, int consumeMoney,
			int consumeYB, int consumeBYB, int beginLevel, int endLevel) {
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		String roleName = "";
		if (roleWrapper != null) {
			roleName = roleWrapper.getName();
		}
		GamePublishEvent.publishEvent(new EquipQHLogEvent(userRoleId, guid, roleName, goodsId, consumeItem,
				consumeMoney, consumeYB, consumeBYB, beginLevel, endLevel));
	}
	/**
	 * 装备铸神日志记录
	 * @param userRoleId
	 * @param type
	 * @param guid
	 * @param goodsId
	 * @param consumeItem
	 * @param consumeMoney
	 * @param beginLevel
	 * @param endLevel
	 */
	private void recordEquipZhuShenLog(long userRoleId, long guid, String goodsId, JSONArray consumeItem,
			int consumeMoney, int beginLevel, int endLevel) {
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		String roleName = "";
		if (roleWrapper != null) {
			roleName = roleWrapper.getName();
		}
		GamePublishEvent.publishEvent(new EquipZhuShenLogEvent(userRoleId, guid, roleName, goodsId, consumeItem,
				consumeMoney, beginLevel, endLevel));
	}
	/**
	 * 装备升级日志记录
	 * @param userRoleId
	 * @param type
	 * @param guid
	 * @param goodsId
	 * @param consumeItem
	 * @param consumeMoney
	 * @param beginLevel
	 * @param endLevel
	 */
	private void recordEquipSJLog(long userRoleId, long guid, String goodsId, JSONArray consumeItem, int consumeMoney,
			int consumeYB, int consumeBYB, String beginGoodsId, String endGoodsId) {
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		String roleName = "";
		if (roleWrapper != null) {
			roleName = roleWrapper.getName();
		}
		GamePublishEvent.publishEvent(new EquipSJLogEvent(userRoleId, guid, roleName, goodsId, consumeItem,
				consumeMoney, consumeYB, consumeBYB, beginGoodsId, endGoodsId));
	}
	/**
	 * 换装业务
	 * @param userRoleId
	 * @param goodsPkId
	 * @return
	 */
	public Object[] changeEquip(Long userRoleId, long guid, int targetSlot) {
		ContainerType containerType = BagUtil.getContainerTypeBySlot(targetSlot);
		if (containerType == null) {
			return AppErrorCode.NO_TARGET_SLOT;
		}
		RoleItem roleItem = roleBagDao.cacheLoad(guid, userRoleId);
		// 是否有这个物品
		if (roleItem == null) {
			return AppErrorCode.POSITION_NO_EQUIP;
		}
		// 移动之前物品所在的格位
		// int sourceSlot=roleItem.getSlot();
		// boolean isZuoQiEquipChange = isZuoqiChangeEquip(sourceSlot, targetSlot);
		// //如果是坐骑换装
		// if(isZuoQiEquipChange){
		// return zuoQiExportService.zuoqiChangeEquip(userRoleId, guid, targetSlot, containerType);
		// }
		return bodyEquipChange(userRoleId, guid, targetSlot, containerType);
	}
	/**
	 * 
	 * @param userRoleId
	 * @param guid			道具guid
	 * @param targetSlot	目标格子
	 * @param containerType	目标容器类型
	 * @return
	 */
	private Object[] bodyEquipChange(Long userRoleId, long guid, int targetSlot, ContainerType containerType) {
		ContainerType source = null;
		// 如是脱下 检查身上是否有该装备
		if (targetSlot >= 0) {
			RoleItemExport roleItemExport = null;
			for (ContainerType type : ContainerType.values()) {
				if (type.isEquip()) {
					roleItemExport = roleBagExportService.getOtherEquip(userRoleId, guid, type);
					if (roleItemExport != null) {
						source = type;
						break;
					}
				}
			}
			if (roleItemExport == null) {
				return AppErrorCode.BODY_NO_ITEM;
			}
		}
		BagSlots bagSlots = roleBagExportService.moveSlot(guid, targetSlot, containerType.getType(), userRoleId);
		if (!bagSlots.isSuccee()) {
			return bagSlots.getErrorCode();
		}
		// 通知场景
		if (source != null) {
			notifyOtherRoleSync(userRoleId, null, source);
		} else {
			notifyOtherRoleSync(userRoleId, null, containerType);
		}
		Object[] result = new Object[3];
		result[0] = 1;
		List<RoleItemOperation> roleItemVos = bagSlots.getRoleItemVos();
		for (int i = 0; i < roleItemVos.size(); i++) {
			result[i + 1] = BagOutputWrapper.getOutWrapperData(OutputType.MOVESLOT, roleItemVos.get(i));
		}
		// 成就推送
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[] {GameConstants.CJ_QIANGHUA, 0 });
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[] {GameConstants.CJ_ZIZHUANG, 0 });
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[] {GameConstants.CJ_CHENGZHUANG, 0 });
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[] {GameConstants.CJ_TAOZHUANG, 0 });
			//修炼任务
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE_TS, new Object[] {XiuLianConstants.QIANGHUA_ALL_LEVEL, roleBagExportService.getAllEquipsQHLevel(userRoleId)});
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		}
		return result;
	}
//	/**
//	 * 是否是坐骑装备换装
//	 * @param sourceSlot
//	 * @param targetSlot
//	 * @return
//	 */
//	private boolean isZuoqiChangeEquip(int sourceSlot, int targetSlot) {
//		if (EquipTypeSlot.isZuoQiEquip(sourceSlot) || EquipTypeSlot.isZuoQiEquip(targetSlot)) {
//			return true;
//		}
//		return false;
//	}
	/**
	 * 通知场景里面属性变化
	 * @param userRoleId
	 * @param obj
	 */
	public void notifyOtherRoleSync(long userRoleId, Object[] obj, ContainerType type) {
		if (null == type)
			return;
		Object[] equips = getRoleEquipAttribute(userRoleId, type);
		if (ContainerType.BODYTITEM.equals(type)) {
			// 推送换装内部指令同步装备
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_EQUIP, new Object[] { equips, obj });
		} else if (ContainerType.ZUOQIITEM.equals(type)) {
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_ZUOQI_EQUIP, equips);
		} else if (ContainerType.CHIBANGITEM.equals(type)) {
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_CHIBANG_EQUIP, equips);
		} else if (ContainerType.TANGBAOITEM.equals(type)) {
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_TANGBAO_EQUIP, equips);
		} else if (ContainerType.TIANGONGITEM.equals(type)) {
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_TIANGONG_EQUIP, equips);
		} else if (ContainerType.TIANSHANGITEM.equals(type)) {
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_TIANSHANG_EQUIP, equips);
		} else if (ContainerType.QILINGITEM.equals(type)) {
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_QILING_EQUIP, equips);
		} else if (ContainerType.TIANYUITEM.equals(type)) {
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_TIANYU_EQUIP, equips);
		} else if (ContainerType.CHONGWUITEM.equals(type)) {
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_CHONGWU_EQUIP, equips);
		}else if (ContainerType.SHENQIITEM.equals(type)) {
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_SHENQI_EQUIP, equips);
		}else if (ContainerType.WUQI.equals(type)) {
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_WUQI_EQUIP, equips);
		}
		
	}
	// /**
	// * 通知场景里面属性变化和外显示
	// * @param userRoleId
	// * @param obj
	// */
	// public void notifyOtherRoleSync(long userRoleId,Object[] obj){
	// Object[] equips = getRoleEquipAttribute(userRoleId);
	// // 推送换装内部指令同步装备
	// BusMsgSender.send2Stage(userRoleId,InnerCmdType.INNER_CHANGE_EQUIP, new
	// Object[]{equips,obj});
	// }
	// 获取身上所有的装备信息,用于传入场景
	public Object[] getRoleEquipAttribute(long userRoleId, ContainerType type) {
		List<RoleItemExport> equips = roleBagExportService.getEquip(userRoleId, type);
		return EquipOutputWrapper.getRoleEquipAttribute(equips);
	}
	public Object[] getRonglianVal(Long userRoleId) {
		RoleBusinessInfoWrapper info = roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId);
		return new Object[] { 1, info.getRongLianVal() };
	}
	
	/**
	 * 装备熔炼
	 * @param userRoleId
	 * @param guid
	 * @return
	 */
	public Object[] equipRecycle(Long userRoleId, Object[] guids) {
		Collection<RoleItemExport> roleItemExports = filterGuid(guids, userRoleId);
		if (roleItemExports == null || roleItemExports.size() == 0) {
			return AppErrorCode.NO_HC_GOODS;
		}
		int bagEmptyCount = roleBagExportService.getContainerEmptyCount(ContainerType.BAGITEM, userRoleId);
		if (bagEmptyCount <= 0) {
			return AppErrorCode.BAG_NOEMPTY;
		}
		int rlz = 0;
		List<Long> goodsGuids = new ArrayList<>();
		for (RoleItemExport roleItemExport : roleItemExports) {
			GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleItemExport.getGoodsId());
			rlz += goodsConfig.getRlz();
			goodsGuids.add(roleItemExport.getGuid());
		}
		BagSlots bagSlots = roleBagExportService.removeBagItemByGuids(goodsGuids, userRoleId,
				GoodsSource.EQUIP_RECYCLE, true, true);
		JSONArray recycleEquips = new JSONArray();
		LogFormatUtils.parseJSONArray(bagSlots, recycleEquips);
		int myRlz = 0;// 我的熔炼值
		// 添加熔炼值
		if (rlz > 0) {
			myRlz = roleBusinessInfoExportService.addRongLianVal(userRoleId, rlz);
			// 判断熔炼值是否达到上限并处理
			isXuanTieVal(userRoleId, myRlz);
		}
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.EQUIP_RONGLIAN,goodsGuids.size()});
		} catch (Exception e) {
			ChuanQiLog.error(""+e);
		}
		// 添加装备回收日志记录
		String roleName = getRoleName(userRoleId);
		GamePublishEvent.publishEvent(new EquipRecycleLogEvent(recycleEquips, userRoleId, roleName, rlz));
		// 活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A9);
		return new Object[] { 1, roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId).getRongLianVal() };
	}
	/**
	 * 熔炼值处理
	 * @param rlz
	 */
	private void isXuanTieVal(Long userRoleId, int rlz) {
		RongLianJiChuConfig config = rongLianJiChuConfigExportService.loadById(1);
		if (config == null) {
			return;
		}
		// 熔炼值小于转换所需的熔炼值则不处理
		if (rlz < config.getRlzmax()) {
			return;
		}
		// 增加玄铁值
		roleBusinessInfoExportService.addXuanTieVal(userRoleId, config.getXuantie());
		// 获得材料
		Object[] itemMap = Lottery.getRandomKey(config.getItemMap());
		if (itemMap != null && itemMap.length >= 2) {
			// 物品入背包
			RoleItemInput item = BagUtil.createItem(itemMap[0].toString(), (int) itemMap[1], 0);
			roleBagExportService.putInBag(item, userRoleId, GoodsSource.ZB_RONGLIAN, true);
		}
		// 消耗熔炼值
		roleBusinessInfoExportService.costRongLianVal(userRoleId, config.getRlzmax());
		isXuanTieVal(userRoleId, roleBusinessInfoExportService.getRoleBusinessInfoWrapper(userRoleId).getRongLianVal());
		return;
	}
	private String getRoleName(long userRoleId) {
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper != null) {
			return roleWrapper.getName();
		}
		return "";
	}
	/**
	 * 过滤物品guid,将不存在或重复的guid给去掉
	 * @param guids
	 * @return
	 */
	private Collection<RoleItemExport> filterGuid(Object[] guids, long userRoleId) {
		if (guids == null)
			return null;
		Set<RoleItemExport> goodsGuids = new HashSet<>();
		for (Object guid : guids) {
			Long goodsGuid = CovertObjectUtil.object2Long(guid);
			RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, goodsGuid);
			if (roleItemExport != null) {
				GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleItemExport.getGoodsId());
				if (goodsConfig == null)
					return null;
				// 熔炉值为0表示不可熔炼
				if (goodsConfig.getRlz() == 0) {
					continue;
				}
				// if(goodsConfig.getJiangexp()==0&&goodsConfig.getJianghun()==0&&goodsConfig.getJiangmoney()==0){
				// //如果回收没有数据，表示该物品不可以回收
				// continue;
				// }
				goodsGuids.add(roleItemExport);
			}
		}
		return goodsGuids;
	}
	/*
	 * public Object[] autoEquipQH(Long userRoleId, long guid, BusMsgQueue busMsgQueue ,boolean
	 * isAutoGM , int targetLevel) { RoleItemExport roleItem=
	 * roleBagExportService.getBodyGoodsByGuid(userRoleId, guid); if(roleItem==null){ return
	 * AppErrorCode.NO_QH_EQUIP; } int qhLevel=roleItem.getQianhuaLevel() ==
	 * null?0:roleItem.getQianhuaLevel(); int maxLevel=getMaxQHLevel(roleItem.getGoodsId());
	 * if(targetLevel<=qhLevel || targetLevel >maxLevel){ return AppErrorCode.TARGET_LEVEL_ERROR; }
	 * if(qhLevel>=maxLevel){ return AppErrorCode.EQUIP_MAX_LEVEL; } GoodsConfig goodsConfig =
	 * goodsConfigExportService.loadById(roleItem.getGoodsId()); if(goodsConfig == null){ return
	 * AppErrorCode.NOT_FOUND_GOOODS; } QiangHuaBiaoConfig
	 * qhConfig=qiangHuaBiaoConfigExportService.getQHConfig(qhLevel+1); if(qhConfig==null){ return
	 * AppErrorCode.EQUIP_LEVEL_ERROR; } Map<String,Integer> needGoods=new HashMap<>(); Object[]
	 * result = autoEquipQH(qhLevel, 0,0, needGoods, userRoleId, true, maxLevel,
	 * goodsConfig.getEqpart(), isAutoGM,targetLevel); //
	 * result:{errorCode,qhlevel,needMoney,needGold} Object[] erroCode=(Object[])result[0];
	 * if(erroCode!=null){ return erroCode; } int newQHlevel=(Integer)result[1]; int
	 * newNeedMoney=(Integer)result[2]; int newNeedGold=(Integer)result[3]; // 扣除金币
	 * if(newNeedMoney>0){ accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY,
	 * newNeedMoney, userRoleId, LogPrintHandle.CONSUME_EQUIP_QH, true,LogPrintHandle.CBZ_EQUIP_QH);
	 * } // 扣除元宝 if(newNeedGold>0){ accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD,
	 * newNeedGold, userRoleId, LogPrintHandle.CONSUME_EQUIP_QH, true,LogPrintHandle.CBZ_EQUIP_QH);
	 * } BagSlots bagSlots = null; //扣除道具 if(needGoods.size()>0){ bagSlots =
	 * roleBagExportService.removeBagItemByGoodsTypes(needGoods, userRoleId, GoodsSource.EQUIP_QH,
	 * true, true); } roleBagExportService.updateQHLevel(newQHlevel,guid,userRoleId);
	 * //如果穿在身上,需要通知属性变化 if(roleItem.getSlot()<0){ notifyOtherRoleSync(userRoleId, null); }
	 * //记录强化操作日志 JSONArray consumeItemArray = new JSONArray();
	 * LogFormatUtils.parseJSONArray(bagSlots,consumeItemArray); recordEquipQHLog(userRoleId, guid,
	 * roleItem.getGoodsId(), consumeItemArray, newNeedMoney, newNeedGold, 0,qhLevel, newQHlevel);
	 * return new Object[]{1,guid,newQHlevel}; }
	 *//**
	 * 自动强化
	 * @param qhLevel
	 * @param needMoney
	 * @param needGold
	 * @param needGoods
	 * @param userRoleId
	 * @param isSendErrorCode
	 * @param maxLevel
	 * @param position
	 * @param isAutoGM
	 * @return {errorCode,qhlevel,needMoney,needGold}
	 */
	/*
	 * private Object[] autoEquipQH(int qhLevel,int needMoney,int needGold,Map<String,Integer>
	 * needGoods,long userRoleId,boolean isSendErrorCode,int maxLevel,int position,boolean
	 * isAutoGM,int targetLevel){ if(qhLevel>=maxLevel){ Object[]
	 * errorCode=isSendErrorCode?AppErrorCode.EQUIP_MAX_LEVEL:null; return new
	 * Object[]{errorCode,qhLevel,needMoney,needGold}; } QiangHuaBiaoConfig
	 * qhConfig=qiangHuaBiaoConfigExportService.getQHConfig(qhLevel+1); if(qhConfig==null){ Object[]
	 * errorCode=isSendErrorCode?AppErrorCode.EQUIP_LEVEL_ERROR:null; return new
	 * Object[]{errorCode,qhLevel,needMoney,needGold}; } Object[]
	 * errorCode=accountExportService.isEnought(GoodsCategory.MONEY,
	 * needMoney+qhConfig.getNeedMoney(), userRoleId); if(null != errorCode){ if(!isSendErrorCode){
	 * errorCode = null; } return new Object[]{errorCode,qhLevel,needMoney,needGold}; } String
	 * goodsId=qhConfig.getNeedItemId(); int count=qhConfig.getNeedItemCount(); int
	 * oldNeedCount=needGoods.get(goodsId) == null?0:needGoods.get(goodsId); int nowCount
	 * =count+oldNeedCount; needGoods.put(goodsId, nowCount); Object[]
	 * needGoodsFlag=roleBagExportService.checkRemoveBagItemByGoodsTypes(needGoods, userRoleId);
	 * if(needGoodsFlag!=null){ if(isAutoGM){ int price = 30;// TODO 需要通过商城配置表获得对应物品的价格 int
	 * owerCount = roleBagExportService.getBagItemCountByGoodsType(goodsId, userRoleId); int
	 * nowNeedGold = (nowCount - owerCount)*price; Object[] code =
	 * accountExportService.isEnought(GoodsCategory.GOLD,nowNeedGold, userRoleId); if(null != code){
	 * if(oldNeedCount==0){ needGoods.remove(goodsId); }else{ needGoods.put(goodsId, oldNeedCount);
	 * } if(!isSendErrorCode){ code = null; } return new
	 * Object[]{errorCode,qhLevel,needMoney,needGold}; } needGold+=nowNeedGold;
	 * needGoods.put(goodsId, owerCount<oldNeedCount?owerCount:oldNeedCount); }else{ Object[]
	 * code=isSendErrorCode?needGoodsFlag:null; if(oldNeedCount==0){ needGoods.remove(goodsId);
	 * }else{ needGoods.put(goodsId, oldNeedCount); } return new
	 * Object[]{code,qhLevel,needMoney,needGold}; } } //金币 needMoney+=qhConfig.getNeedMoney(); int
	 * successrate = qhConfig.getSuccessrate(); boolean flag = isQHSuccess(successrate); if(flag){
	 * if(targetLevel <= ++qhLevel){ return new Object[]{null,qhLevel,needMoney,needGold}; } }
	 * return autoEquipQH( qhLevel, needMoney,needGold,needGoods, userRoleId, false,
	 * maxLevel,position,isAutoGM,targetLevel); }
	 */
	public Object[] getAllEquips(Long userRoleId) {
		return roleBagExportService.getAllEquips(userRoleId);
	}
	/**
	 * 装备升级
	 * @param userRoleId
	 * @param guid
	 * @param busMsgQueue
	 * @param isAutoGM 是否自动购买
	 */
	public Object[] equipLevelUp(Long userRoleId, Long guid, BusMsgQueue busMsgQueue, boolean isAutoGM) {
		RoleItemExport roleItem = roleBagExportService.getBodyGoodsByGuid(userRoleId, guid);
		if (roleItem == null) {
			return AppErrorCode.SJ_ZHUANBEI_NOT;
		}
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleItem.getGoodsId());
		if (goodsConfig == null) {
			return AppErrorCode.SJ_ZHUANBEI_NOT_CONFIG;
		}
		GoodsConfig endGoodsConfig = goodsConfigExportService.loadById(goodsConfig.getNextid());
		// 判断装备是否可以进行进阶
		if (endGoodsConfig == null || null == goodsConfig.getNextid() || "".equals(goodsConfig.getNextid())) {
			return AppErrorCode.ZHUANGBEI_NOT_SJ;
		}
		RoleWrapper loginRole = roleExportService.getLoginRole(userRoleId);// 人物
		// 人物等级小于装备进阶后的等级
		if (loginRole.getLevel() < endGoodsConfig.getLevelReq()) {
			return AppErrorCode.ZHUANGBEI_NOT_LEVEL;
		}
		ShengJiBiaoConfig shengjiConfig = shengJiBiaoConfigExportService.loadById(goodsConfig.getLevelReq());
		if (shengjiConfig == null) {
			return AppErrorCode.SHENGJI_CONFIG_ERROR;
		}
		// 升级需要消耗的材料
		/*
		 * Map<String, Integer> goods = new HashMap<>();
		 * goods.put(shengjiConfig.getProp(),shengjiConfig.getNum()); Object[] code =
		 * roleBagExportService.checkRemoveBagItemByGoodsTypes(goods, userRoleId); if(code != null){
		 * return code; }
		 */
		/*
		 * String goodsType = shengjiConfig.getProp(); int count=shengjiConfig.getNum(); int
		 * needGold = 0; Object[]
		 * errorCode=roleBagExportService.checkRemoveBagItemByGoodsType(goodsType, count,
		 * userRoleId); if(errorCode!=null){ if(!isAutoGM){ //自动购买 return errorCode; }else{ int
		 * price = 30;//TODO 需要从商城中获得物品的价格 int owerCount =
		 * roleBagExportService.getBagItemCountByGoodsId(goodsType, userRoleId); needGold =
		 * (count-owerCount)*price; if(null != accountExportService.isEnought(GoodsCategory.GOLD,
		 * needGold, userRoleId)){ return AppErrorCode.YB_ERROR; } count = owerCount; } }
		 */
		List<String> needGoodsIds = shengJiBiaoConfigExportService.getConsumeIds(shengjiConfig.getProp());
		int needCount = shengjiConfig.getNum();
		Map<String, Integer> tempResources = new HashMap<>();
		for (String goodsId : needGoodsIds) {
			// int oldNeedCount = needResource.get(goodsId) == null ? 0 : needResource.get(goodsId);
			int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
			if (owerCount >= needCount) {
				tempResources.put(goodsId, needCount);
				needCount = 0;
				break;
			}
			needCount = needCount - owerCount;
			tempResources.put(goodsId, owerCount);
		}
		if (isAutoGM && needCount > 0) {
			int bPrice = shengjiConfig.getBgold();// 绑定元宝的价格
			int bCount = 0;
			int nowNeedBgold = 0;
			for (int i = 0; i < needCount; i++) {
				nowNeedBgold = (bCount + 1) * bPrice;
				Object[] bgoldError = roleBagExportService.isEnought(GoodsCategory.BGOLD, nowNeedBgold, userRoleId);
				if (null != bgoldError) {
					break;
				}
				bCount++;
			}
			nowNeedBgold = bCount * bPrice;
			tempResources.put(GoodsCategory.BGOLD + "", nowNeedBgold);
			needCount = needCount - bCount;
			int price = shengjiConfig.getGold();// 需要通过商城配置表获得对应物品的价格
			int nowNeedGold = needCount * price;
			Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, nowNeedGold, userRoleId);
			if (null != goldError) {
				return AppErrorCode.YB_ERROR;
			}
			tempResources.put(GoodsCategory.GOLD + "", nowNeedGold);
			needCount = 0;
		} else {
			if (needCount > 0) {
				return AppErrorCode.SHENGJI_GOODS_ERROR;
			}
		}
		// 升级需要消耗的银两
		int yl = shengjiConfig.getMoney();
		Object[] isOb = accountExportService.isEnought(GoodsCategory.MONEY, yl, userRoleId);
		if (null != isOb) {
			return AppErrorCode.JB_ERROR;
		}
		Integer newNeedGold = tempResources.remove(GoodsCategory.GOLD + "");
		Integer newNeedBgold = tempResources.remove(GoodsCategory.BGOLD + "");
		// 扣除金币
		if (yl > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, yl, userRoleId,
					LogPrintHandle.CONSUME_ZHUANGBEI_SJ, true, LogPrintHandle.CBZ_ZHUANGBEI_SJ);
		}
		// 扣除元宝
		if (newNeedGold != null && newNeedGold > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId,
					LogPrintHandle.CONSUME_ZHUANGBEI_SJ, true, LogPrintHandle.CBZ_ZHUANGBEI_SJ);
			// 腾讯OSS消费上报
			if (PlatformConstants.isQQ()) {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] {
						QqConstants.ZHIFU_YB, newNeedGold, LogPrintHandle.CONSUME_ZHUANGBEI_SJ,
						QQXiaoFeiType.CONSUME_ZHUANGBEI_SJ, 1 });
			}
		} else {
			newNeedGold = 0;
		}
		// 扣除绑定元宝
		if (newNeedBgold != null && newNeedBgold > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId,
					LogPrintHandle.CONSUME_ZHUANGBEI_SJ, true, LogPrintHandle.CBZ_ZHUANGBEI_SJ);
			// 腾讯OSS消费上报
			if (PlatformConstants.isQQ()) {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] {
						QqConstants.ZHIFU_BYB, newNeedBgold, LogPrintHandle.CONSUME_ZHUANGBEI_SJ,
						QQXiaoFeiType.CONSUME_ZHUANGBEI_SJ, 1 });
			}
		} else {
			newNeedBgold = 0;
		}
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(tempResources, userRoleId,
				GoodsSource.ZB_SHENGJI, true, true);
		// //消耗材料
		// BagSlots bagSlots = null;
		// if(count > 0){
		// bagSlots = roleBagExportService.removeBagItemByGoodsType(goodsType, count, userRoleId,
		// GoodsSource.ZB_SHENGJI, true, true);
		//
		// }
		// //消耗银两
		// if(yl>0){
		// accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, yl, userRoleId,
		// LogPrintHandle.CONSUME_ZHUANGBEI_SJ, true,LogPrintHandle.CBZ_ZHUANGBEI_SJ);
		// }
		// //扣除元宝
		// if(needGold > 0){
		// accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, needGold, userRoleId,
		// LogPrintHandle.CONSUME_ZHUANGBEI_SJ, true,LogPrintHandle.CBZ_ZHUANGBEI_SJ);//,
		// LogPrintHandle.CBZ_EQUIP_QH);
		// }
		// 装备升级
		int successrate = shengjiConfig.getSuccessrate();
		boolean flag = isQHSuccess(successrate);
		String endGoodsId = goodsConfig.getId();
		if (flag) {
			endGoodsId = goodsConfig.getNextid();
			roleBagExportService.updateZBSJ(goodsConfig.getNextid(), guid, userRoleId);
			// 如果穿在身上,需要通知属性变化
			if (roleItem.getSlot() < 0) {
				notifyOtherRoleSync(userRoleId, null, ContainerType.BODYTITEM);
			}
		}
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.EQUIP_UP,null});
			//支线
			taskBranchService.completeBranch(userRoleId, BranchEnum.B5, 1);
		} catch (Exception e) {
			ChuanQiLog.error(""+e);
		}
		
		// 记录升级操作日志
		JSONArray consumeItemArray = new JSONArray();
		LogFormatUtils.parseJSONArray(bagSlots, consumeItemArray);
		recordEquipSJLog(userRoleId, guid, roleItem.getGoodsId(), consumeItemArray, yl, newNeedGold, newNeedBgold,
				goodsConfig.getId(), endGoodsId);
		if (flag) {
			return new Object[] { 1, guid, endGoodsId, roleItem.getRandomAttrs() };
		} else {
			return new Object[] { 2, AppErrorCode.SHENGJI_SHIBEI };
		}
	}
	/**
	 * 神武装备升级
	 * @param userRoleId
	 * @param guid
	 * @param busMsgQueue
	 * @param isAutoGM 是否自动购买
	 */
	public Object[] swEquipLevelUp(Long userRoleId, Long guid, BusMsgQueue busMsgQueue, boolean isAutoGM) {
		RoleItemExport roleItem = roleBagExportService.getBodyGoodsByGuid(userRoleId, guid);
		if (roleItem == null) {
			return AppErrorCode.SJ_ZHUANBEI_NOT;
		}
		// GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleItem.getGoodsId());
		ShenWuJinJieBiaoConfig jinjieConfig = shenWuJinJieBiaoConfigExportService.loadById(roleItem.getGoodsId());
		if (jinjieConfig == null || null == jinjieConfig.getNextid() || "".equals(jinjieConfig.getNextid())) {
			return AppErrorCode.SJ_ZHUANBEI_NOT_CONFIG;
		}
		GoodsConfig endGoodsConfig = goodsConfigExportService.loadById(jinjieConfig.getNextid());
		// 判断装备是否可以进行进阶
		if (endGoodsConfig == null) {
			return AppErrorCode.ZHUANGBEI_NOT_SJ;
		}
		// 转身等级
		int zsLevel = zhuanshengExportService.getZhuanshengLevel(userRoleId);
		// 人物转生等级小于装备进阶后的等级
		if (zsLevel < endGoodsConfig.getNeedzhuansheng()) {
			return AppErrorCode.ZHUANGBEI_NOT_LEVEL;
		}
		List<String> needGoodsIds = shenWuJinJieBiaoConfigExportService.getConsumeIds(jinjieConfig.getNeeditem());
		int needCount = jinjieConfig.getCount();
		Map<String, Integer> tempResources = new HashMap<>();
		for (String goodsId : needGoodsIds) {
			int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
			if (owerCount >= needCount) {
				tempResources.put(goodsId, needCount);
				needCount = 0;
				break;
			}
			needCount = needCount - owerCount;
			tempResources.put(goodsId, owerCount);
		}
		if (isAutoGM && needCount > 0) {
			int bPrice = jinjieConfig.getBgold();// 绑定元宝的价格
			if (bPrice < 1) {
				return AppErrorCode.CONFIG_ERROR;
			}
			int bCount = 0;
			int nowNeedBgold = 0;
			for (int i = 0; i < needCount; i++) {
				nowNeedBgold = (bCount + 1) * bPrice;
				Object[] bgoldError = roleBagExportService.isEnought(GoodsCategory.BGOLD, nowNeedBgold, userRoleId);
				if (null != bgoldError) {
					break;
				}
				bCount++;
			}
			nowNeedBgold = bCount * bPrice;
			tempResources.put(GoodsCategory.BGOLD + "", nowNeedBgold);
			needCount = needCount - bCount;
			int price = jinjieConfig.getGold();// 需要通过商城配置表获得对应物品的价格
			if (price < 1) {
				return AppErrorCode.CONFIG_ERROR;
			}
			int nowNeedGold = needCount * price;
			Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, nowNeedGold, userRoleId);
			if (null != goldError) {
				return AppErrorCode.YB_ERROR;
			}
			tempResources.put(GoodsCategory.GOLD + "", nowNeedGold);
			needCount = 0;
		} else {
			if (needCount > 0) {
				return AppErrorCode.SHENGJI_GOODS_ERROR;
			}
		}
		// 升级需要消耗的银两
		int yl = jinjieConfig.getNeedmoney();
		Object[] isOb = accountExportService.isEnought(GoodsCategory.MONEY, yl, userRoleId);
		if (null != isOb) {
			return AppErrorCode.JB_ERROR;
		}
		Integer newNeedGold = tempResources.remove(GoodsCategory.GOLD + "");
		Integer newNeedBgold = tempResources.remove(GoodsCategory.BGOLD + "");
		// 扣除金币
		if (yl > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, yl, userRoleId,
					LogPrintHandle.CONSUME_ZHUANGBEI_SJ_SW, true, LogPrintHandle.CBZ_ZHUANGBEI_SJ_SW);
		}
		// 扣除元宝
		if (newNeedGold != null && newNeedGold > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId,
					LogPrintHandle.CONSUME_ZHUANGBEI_SJ_SW, true, LogPrintHandle.CBZ_ZHUANGBEI_SJ_SW);
			// 腾讯OSS消费上报
			if (PlatformConstants.isQQ()) {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] {
						QqConstants.ZHIFU_YB, newNeedGold, LogPrintHandle.CONSUME_ZHUANGBEI_SJ_SW,
						QQXiaoFeiType.CONSUME_ZHUANGBEI_SJ_SW, 1 });
			}
		} else {
			newNeedGold = 0;
		}
		// 扣除绑定元宝
		if (newNeedBgold != null && newNeedBgold > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId,
					LogPrintHandle.CONSUME_ZHUANGBEI_SJ_SW, true, LogPrintHandle.CBZ_ZHUANGBEI_SJ_SW);
			// 腾讯OSS消费上报
			if (PlatformConstants.isQQ()) {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] {
						QqConstants.ZHIFU_BYB, newNeedBgold, LogPrintHandle.CONSUME_ZHUANGBEI_SJ_SW,
						QQXiaoFeiType.CONSUME_ZHUANGBEI_SJ_SW, 1 });
			}
		} else {
			newNeedBgold = 0;
		}
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(tempResources, userRoleId,
				GoodsSource.SW_ZB_SHENGJI, true, true);
		// 装备升级
		int successrate = jinjieConfig.getSuccessrate();
		boolean flag = isQHSuccess(successrate);
		String endGoodsId = jinjieConfig.getId();
		if (flag) {
			endGoodsId = jinjieConfig.getNextid();
			roleBagExportService.updateZBSJ(jinjieConfig.getNextid(), guid, userRoleId);
			// 如果穿在身上,需要通知属性变化
			if (roleItem.getSlot() < 0) {
				notifyOtherRoleSync(userRoleId, null, ContainerType.BODYTITEM);
			}
		}
		
		//支线
		taskBranchService.completeBranch(userRoleId, BranchEnum.B5, 1);
		
		// 记录升级操作日志
		JSONArray consumeItemArray = new JSONArray();
		LogFormatUtils.parseJSONArray(bagSlots, consumeItemArray);
		recordEquipSJLog(userRoleId, guid, roleItem.getGoodsId(), consumeItemArray, yl, newNeedGold, newNeedBgold,
				jinjieConfig.getId(), endGoodsId);
		if (flag) {
			return new Object[] { 1, guid, endGoodsId, new Object[] { newNeedBgold, newNeedGold } };
		} else {
			return new Object[] { 0, AppErrorCode.SHENGJI_SHIBEI };
		}
	}
	/**
	 * 神武装备星铸(类似神武装备等级强化)
	 * @param userRoleId 玩家游戏编号
	 * @param guid 神武装备唯一编号
	 * @param isAutoGM 材料不足时是否自动购买(true=是;false=否)
	 * @return
	 */
	public Object[] swEquipStarUp(Long userRoleId, Long guid, Boolean isAutoGM) {
		RoleItemExport roleItemExport = roleBagExportService.getBodyGoodsByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_QH_EQUIP;
		}
		int maxQhLevel = shenWuXingZhuBiaoConfigExportService.getMaxQhLevel();
		if (maxQhLevel <= 0) {
			return AppErrorCode.CONFIG_ERROR;
		}
		int beforeQhLevel = roleItemExport.getQianhuaLevel();
		if (beforeQhLevel >= maxQhLevel) {
			return AppErrorCode.ZHUANGBEI_XZ_SW_MAX_LEVEL;
		}
		int afterQhLevel = beforeQhLevel + 1;
		ShenWuXingZhuBiaoConfig swxzConfig = shenWuXingZhuBiaoConfigExportService.getQHConfig(afterQhLevel);
		if (null == swxzConfig) {
			return AppErrorCode.CONFIG_ERROR;
		}
		// 升星需要的道具编号id有序集合
		List<String> needPropIds = shenWuXingZhuBiaoConfigExportService.getGoodsIdsById1(swxzConfig.getNeedItemId());
		if (ObjectUtil.isEmpty(needPropIds)) {
			return AppErrorCode.CONFIG_ERROR;
		}
		// 消耗道具集合
		Map<String, Integer> removeItemMap = new HashMap<String, Integer>();
		int needCount = swxzConfig.getNeedItemCount();
		// 道具消耗
		for (String goodsId : needPropIds) {
			if (needCount <= 0) {
				break;
			}
			int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
			if (owerCount >= needCount) {
				removeItemMap.put(goodsId, needCount);
				needCount = 0;
			} else if (owerCount > 0) {
				removeItemMap.put(goodsId, owerCount);
				needCount -= owerCount;
			}
		}
		// 玩家消耗的绑定元宝数量
		int consumeBdYbCount = 0;
		// 玩家消耗的元宝数量
		int consumeYbCount = 0;
		// 玩家数据
		RoleAccountWrapper roleAccount = accountExportService.getAccountWrapper(userRoleId);
		// 剩余道具富足&&玩家选择道具不足时自动购买选项(优先使用绑定元宝消耗道具，不足再使用元宝消耗道具)
		if (needCount > 0 && isAutoGM) {
			Integer _itemBdYbPrice = swxzConfig.getBgold();
			int itemBdYbPrice = null == _itemBdYbPrice ? 0 : _itemBdYbPrice.intValue();
			if (itemBdYbPrice < 1) {
				return AppErrorCode.CONFIG_ERROR;
			}
			long roleBdYbCount = roleAccount.getBindYb();
			while (needCount > 0) {
				if (roleBdYbCount < itemBdYbPrice) {
					break;
				}
				roleBdYbCount -= itemBdYbPrice;
				consumeBdYbCount += itemBdYbPrice;
				needCount--;
			}
			if (needCount > 0) {
				Integer _itemYbPrice = swxzConfig.getGold();
				int itemYbPrice = null == _itemYbPrice ? 0 : _itemYbPrice.intValue();
				if (itemYbPrice < 1) {
					return AppErrorCode.CONFIG_ERROR;
				}
				int needYbCount = itemYbPrice * needCount;
				if (roleAccount.getYb() < needYbCount) {
					return AppErrorCode.YB_ERROR;
				}
				consumeYbCount = needYbCount;
				needCount = 0;
			}
		}// end if
		if (needCount > 0) {
			return AppErrorCode.ITEM_NOT_ENOUGH;
		}
		// 绑定元宝消耗
		if (consumeBdYbCount > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, consumeBdYbCount, userRoleId,
					LogPrintHandle.CONSUME_ZHUANGBEI_XZ_SW, true, LogPrintHandle.CBZ_ZHUANGBEI_XZ_SW);
			// 腾讯OSS消费上报
			if (PlatformConstants.isQQ()) {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] {
						QqConstants.ZHIFU_BYB, consumeBdYbCount, LogPrintHandle.CONSUME_ZHUANGBEI_XZ_SW,
						QQXiaoFeiType.CONSUME_ZHUANGBEI_XZ_SW, 1 });
			}
		}
		// 元宝消耗
		if (consumeYbCount > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, consumeYbCount, userRoleId,
					LogPrintHandle.CONSUME_ZHUANGBEI_XZ_SW, true, LogPrintHandle.CBZ_ZHUANGBEI_XZ_SW);
			// 腾讯OSS消费上报
			if (PlatformConstants.isQQ()) {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] {
						QqConstants.ZHIFU_YB, consumeYbCount, LogPrintHandle.CONSUME_ZHUANGBEI_XZ_SW,
						QQXiaoFeiType.CONSUME_ZHUANGBEI_XZ_SW, 1 });
			}
		}
		// 道具消耗
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(removeItemMap, userRoleId,
				GoodsSource.SW_ZB_XINGZHU, true, true);
		// 金币消耗
		Integer _consumeMoney = swxzConfig.getNeedMoney();
		int consumeMoney = null == _consumeMoney ? 0 : _consumeMoney.intValue();
		if (roleAccount.getJb() < consumeMoney) {
			return AppErrorCode.GOLD_NOT_ENOUGH;
		}
		// 金币消耗
		if (consumeMoney > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, consumeMoney, userRoleId,
					LogPrintHandle.CONSUME_ZHUANGBEI_XZ_SW, true, LogPrintHandle.CBZ_ZHUANGBEI_XZ_SW);
		}
		// 记录操作日志
		JSONArray consumeItemArray = new JSONArray();
		LogFormatUtils.parseJSONArray(bagSlots, consumeItemArray);
		recordEquipQHLog(userRoleId, guid, roleItemExport.getGoodsId(), consumeItemArray, consumeMoney, consumeYbCount,
				consumeBdYbCount, beforeQhLevel, afterQhLevel);
		// 真实的强化等级
		int realQhLevel = beforeQhLevel;
		int successrate = swxzConfig.getSuccessrate();
		boolean flag = isQHSuccess(successrate);
		// 星铸成功,更新等级
		if (flag) {
			realQhLevel = afterQhLevel;
			roleBagExportService.updateQHLevel(afterQhLevel, guid, userRoleId);
			// 如果穿在身上,需要通知属性变化
			if (roleItemExport.getSlot() < 0) {
				notifyOtherRoleSync(userRoleId, null, ContainerType.BODYTITEM);
			}
		}
		return new Object[] { 1, guid, realQhLevel };
	}
	/**
	 * 装备提品
	 * @param userRoleId
	 * @param equipGuid
	 * @param busMsgQueue
	 * @return
	 */
	public Object[] tipin(Long userRoleId, Long guid, BusMsgQueue busMsgQueue) {
		RoleItemExport roleItem = roleBagExportService.getBodyGoodsByGuid(userRoleId, guid);
		if (roleItem == null) {
			return AppErrorCode.TP_ZHUANBEI_NOT;
		}
		String beforeGoodsId = roleItem.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(beforeGoodsId);
		if (goodsConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		int type = GameConstants.EQUIP_TIPIN_TYPE_NOMAL;
		if (goodsConfig.getSuit() > 0) {
			type = GameConstants.EQUIP_TIPIN_TYPE_TAOZHUANG;
		}
		TiPinBiaoConfig tiPinBiaoConfig = tiPinBiaoConfigExportService.loadById(type, goodsConfig.getRareLevel());
		if (tiPinBiaoConfig == null || ObjectUtil.strIsEmpty(goodsConfig.getTipinId())) {
			return AppErrorCode.EQUIP_CANNOT_TP;// 装备不可提品
		}
		List<String> needGoodsIds = shengJiBiaoConfigExportService.getConsumeIds(tiPinBiaoConfig.getItemId());
		int needCount = tiPinBiaoConfig.getItemCount();
		Map<String, Integer> tempResources = new HashMap<>();
		for (String goodsId : needGoodsIds) {
			int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
			if (owerCount >= needCount) {
				tempResources.put(goodsId, needCount);
				needCount = 0;
				break;
			}
			needCount = needCount - owerCount;
			tempResources.put(goodsId, owerCount);
		}
		if (needCount > 0) {
			return AppErrorCode.TP_ITEM_NOT_ENOUGH;// 物品数量不足
		}
		if (tiPinBiaoConfig.getMoney() > 0) {
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY,
					tiPinBiaoConfig.getMoney(), userRoleId, LogPrintHandle.CONSUME_TIPIN, true,
					LogPrintHandle.CBZ_TIPIN);
			if (result != null) {
				return result;
			}
		}
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(tempResources, userRoleId,
				GoodsSource.GOODS_EQUIP_TIPIN, true, true);
		Integer before = roleItem.getTipinValue();
		if (before == null) {
			before = 0;
		}
		int tipin = Lottery.roll(tiPinBiaoConfig.getMin(), tiPinBiaoConfig.getMax()) + before;
		String goodsId = null;
		Integer rollAtt = null;
		if (tipin >= tiPinBiaoConfig.getMaxXing()) {
			tipin = 0;
			goodsId = goodsConfig.getTipinId();
		}
		roleBagExportService.updateTiPinLevel(guid, userRoleId, tipin, goodsId);
		if (goodsId != null) {
			rollAtt = roleItem.getRandomAttrs();
			notifyOtherRoleSync(userRoleId, null, ContainerType.BODYTITEM);
			try {
				GoodsConfig tpConfig = goodsConfigExportService.loadById(goodsId);
				if(tpConfig != null && tpConfig.getRareLevel() == 4){
					BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.EQUIP_TICHENG,null});
				}
			} catch (Exception e) {
				ChuanQiLog.error(""+e);
			}
		}else{
			if((tipin/tiPinBiaoConfig.getStarMax() > before/tiPinBiaoConfig.getStarMax())){
				notifyOtherRoleSync(userRoleId, null, ContainerType.BODYTITEM);
			}
		}
		JSONArray consumeItemArray = new JSONArray();
		LogFormatUtils.parseJSONArray(bagSlots, consumeItemArray);
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		String roleName = "";
		if (roleWrapper != null) {
			roleName = roleWrapper.getName();
		}
		// 日志
		GamePublishEvent.publishEvent(new EquipTPLogEvent(userRoleId, guid, roleName, beforeGoodsId, consumeItemArray,
				before, tipin, goodsId));
		return new Object[] { 1, guid, tipin, goodsId, rollAtt };
	}
	/**
	 * 附属装备提品
	 * @param userRoleId
	 * @param equipGuid
	 * @param busMsgQueue
	 * @return
	 */
	public Object[] fuShuTiPin(Long userRoleId, Long guid, BusMsgQueue busMsgQueue) {
		RoleItemExport roleItem = null;
		ContainerType conType = null;
		for (ContainerType containerType : ContainerType.values()) {
			if (containerType.isEquip()) {
				roleItem = roleBagExportService.getOtherEquip(userRoleId, guid, containerType);
				if (roleItem != null) {
					conType = containerType;
					break;
				}
			}
		}
		if (roleItem == null || ContainerType.BODYTITEM.equals(conType)) {
			return AppErrorCode.TP_ZHUANBEI_NOT;
		}
		String beforeGoodsId = roleItem.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(beforeGoodsId);
		if (goodsConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		int type = GameConstants.EQUIP_TIPIN_TYPE_FUSHU;
		if (goodsConfig.getSuit() > 0) {
			type = GameConstants.EQUIP_TIPIN_TYPE_TAOZHUANG;
		}
		TiPinBiaoConfig tiPinBiaoConfig = tiPinBiaoConfigExportService.loadById(type, goodsConfig.getRareLevel());
		if (tiPinBiaoConfig == null || ObjectUtil.strIsEmpty(goodsConfig.getTipinId())) {
			return AppErrorCode.EQUIP_CANNOT_TP;// 装备不可提品
		}
		List<String> needGoodsIds = shengJiBiaoConfigExportService.getConsumeIds(tiPinBiaoConfig.getItemId());
		int needCount = tiPinBiaoConfig.getItemCount();
		Map<String, Integer> tempResources = new HashMap<>();
		for (String goodsId : needGoodsIds) {
			int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
			if (owerCount >= needCount) {
				tempResources.put(goodsId, needCount);
				needCount = 0;
				break;
			}
			needCount = needCount - owerCount;
			tempResources.put(goodsId, owerCount);
		}
		if (needCount > 0) {
			return AppErrorCode.TP_ITEM_NOT_ENOUGH;// 物品数量不足
		}
		if (tiPinBiaoConfig.getMoney() > 0) {
			Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY,
					tiPinBiaoConfig.getMoney(), userRoleId, LogPrintHandle.CONSUME_TIPIN, true,
					LogPrintHandle.CBZ_TIPIN);
			if (result != null) {
				return result;
			}
		}
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(tempResources, userRoleId,
				GoodsSource.GOODS_EQUIP_TIPIN, true, true);
		Integer before = roleItem.getTipinValue();
		if (before == null) {
			before = 0;
		}
		int tipin = Lottery.roll(tiPinBiaoConfig.getMin(), tiPinBiaoConfig.getMax()) + before;
		String goodsId = null;
		Integer rollAtt = null;
		if (tipin >= tiPinBiaoConfig.getMaxXing()) {
			tipin = 0;
			goodsId = goodsConfig.getTipinId();
		}
		roleBagExportService.updateTiPinLevel(guid, userRoleId, tipin, goodsId);
		if (goodsId != null) {
			rollAtt = roleItem.getRandomAttrs();
			notifyOtherRoleSync(userRoleId, null, conType);
		}else{
			if((tipin/tiPinBiaoConfig.getStarMax() > before/tiPinBiaoConfig.getStarMax())){
				notifyOtherRoleSync(userRoleId, null, conType);
			}
		}
		JSONArray consumeItemArray = new JSONArray();
		LogFormatUtils.parseJSONArray(bagSlots, consumeItemArray);
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		String roleName = "";
		if (roleWrapper != null) {
			roleName = roleWrapper.getName();
		}
		// 日志
		GamePublishEvent.publishEvent(new EquipTPLogEvent(userRoleId, guid, roleName, beforeGoodsId, consumeItemArray,
				before, tipin, goodsId));
		return new Object[] { 1, guid, tipin, goodsId, rollAtt };
	}
	
	/**
	 * 用强化锤强化装备
	 */
	public Object[] useHammer(Long userRoleId, Long guid, String goodsId) {
		int num = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
		if (num == 0) {
			return AppErrorCode.NO_YB_GOODS;
		}
		RoleItemExport roleItem = roleBagExportService.getBodyGoodsByGuid(userRoleId, guid);
		if (roleItem == null) {
			return AppErrorCode.NO_QH_EQUIP;
		}
		GoodsConfig goodsConfig = this.goodsConfigExportService.loadById(goodsId);
		if (goodsConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		if (goodsConfig.getRareLevel() < 3) {
			return AppErrorCode.NO_QH;
		}
		int minLevel = goodsConfig.getData1();
		int arriveLevel = goodsConfig.getData2().intValue();
		int qhLevel = roleItem.getQianhuaLevel() == null ? 0 : roleItem.getQianhuaLevel();
		int maxLevel = getMaxQHLevel(roleItem.getGoodsId()); // 装备最大强化等级
		if (qhLevel < minLevel || qhLevel >= arriveLevel || arriveLevel > maxLevel) {
			// 不满足强化条件
			return AppErrorCode.REQ_LEVEL_BZ;
		}
		QiangHuaBiaoConfig qhConfig = qiangHuaBiaoConfigExportService.getQHConfig(arriveLevel);
		if (qhConfig == null) {
			return AppErrorCode.EQUIP_LEVEL_ERROR;
		}
		roleBagExportService.updateQHLevel(arriveLevel, guid, userRoleId);
		notifyOtherRoleSync(userRoleId, null, ContainerType.BODYTITEM);
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoodsId(goodsId, 1, userRoleId, GoodsSource.EQUIP_QH,
				true, true);
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE_TS, new Object[] {XiuLianConstants.QIANGHUA_ALL_LEVEL, roleBagExportService.getAllEquipsQHLevel(userRoleId)});
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.QIANGHUA_LEVEL, arriveLevel});

			//排行升级提醒活动角标
			ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.XIANZHUANG_QH_TYPE);
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
		// 记录强化操作日志
		JSONArray consumeItemArray = new JSONArray();
		LogFormatUtils.parseJSONArray(bagSlots, consumeItemArray);
		recordEquipQHLog(userRoleId, guid, goodsId, consumeItemArray, 0, 0, 0, qhLevel, arriveLevel);
		return new Object[] { 1, guid };
	}
	
	private WuQiExportService wuQiExportService;
	/**
	 * 附属装备升级
	 * @param userRoleId
	 * @param guid
	 * @param busMsgQueue
	 */
	public Object[] fushuEquipLevelUp(Long userRoleId, Long guid, BusMsgQueue busMsgQueue) {
		RoleItemExport roleItem = null;
		ContainerType type = null;
		for (ContainerType containerType : ContainerType.values()) {
			if (containerType.isEquip()) {
				roleItem = roleBagExportService.getOtherEquip(userRoleId, guid, containerType);
				if (roleItem != null) {
					type = containerType;
					break;
				}
			}
		}
		if (roleItem == null || ContainerType.BODYTITEM.equals(type)) {
			return AppErrorCode.SJ_ZHUANBEI_NOT;
		}
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleItem.getGoodsId());
		if (goodsConfig == null) {
			return AppErrorCode.SJ_ZHUANBEI_NOT_CONFIG;
		}
		GoodsConfig endGoodsConfig = goodsConfigExportService.loadById(goodsConfig.getNextid());
		// 判断装备是否可以进行进阶
		if (endGoodsConfig == null || null == goodsConfig.getNextid() || "".equals(goodsConfig.getNextid())) {
			return AppErrorCode.ZHUANGBEI_NOT_SJ;
		}
		RoleWrapper loginRole = roleExportService.getLoginRole(userRoleId);// 人物
		// 人物等级小于装备进阶后的等级
		if (loginRole.getLevel() < endGoodsConfig.getLevelReq()) {
			return AppErrorCode.ZHUANGBEI_NOT_LEVEL;
		}
		Integer level = null;
		if (ContainerType.ZUOQIITEM.equals(type)) {
			level = zuoQiExportService.getZuoQiInfoLevelOther(userRoleId);
		} else if (ContainerType.CHIBANGITEM.equals(type)) {
			level = chiBangExportService.getChibangLevelOther(userRoleId);
		} else if (ContainerType.TIANGONGITEM.equals(type)) {
			level = xianJianExportService.getXianJianLevelOther(userRoleId);
		} else if (ContainerType.TIANSHANGITEM.equals(type)) {
			level = zhanJiaExportService.getXianJianLevelOther(userRoleId);
		} else if (ContainerType.QILINGITEM.equals(type)) {
			level = qiLingExportService.getQiLingLevel(userRoleId);
		} else if (ContainerType.TIANYUITEM.equals(type)) {
			level = tianYuExportService.getTianYuLevelOther(userRoleId);
		} else if (ContainerType.WUQI.equals(type)) {
			level = wuQiExportService.getWuQiInfoLevelOther(userRoleId);
		}
		
		if (level != null && level + 1 < endGoodsConfig.getData1()) {
			return AppErrorCode.ZHUANGBEI_NOT_ENOUGH_LEVEL;// 所需等阶不足
		}
		FuShuEquipShengJiConfig shengjiConfig = fuShuEquipShengJiConfigExportService.getConfig(goodsConfig.getData1());
		if (shengjiConfig == null) {
			return AppErrorCode.SHENGJI_CONFIG_ERROR;
		}
		Object[] result = roleBagExportService.checkRemoveBagItemByGoodsType(shengjiConfig.getItemId1(),
				shengjiConfig.getItemCount(), userRoleId);
		if (result != null) {
			return result;
		}
		// 升级需要消耗的银两
		int yl = shengjiConfig.getMoney();
		Object[] isOb = accountExportService.isEnought(GoodsCategory.MONEY, yl, userRoleId);
		if (null != isOb) {
			return isOb;
		}
		// 扣除金币
		roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, yl, userRoleId,
				LogPrintHandle.CONSUME_ZHUANGBEI_SJ, true, LogPrintHandle.CBZ_ZHUANGBEI_SJ);
		roleBagExportService.removeBagItemByGoodsType(shengjiConfig.getItemId1(), shengjiConfig.getItemCount(),
				userRoleId, GoodsSource.ZB_SHENGJI, true, true);
		// 装备升级
		int successrate = shengjiConfig.getSuccess();
		boolean flag = isQHSuccess(successrate);
		String endGoodsId = goodsConfig.getId();
		if (flag) {
			endGoodsId = goodsConfig.getNextid();
			roleBagExportService.updateZBSJ(goodsConfig.getNextid(), guid, userRoleId);
			// 如果穿在身上,需要通知属性变化
			if (roleItem.getSlot() < 0) {
				notifyOtherRoleSync(userRoleId, null, type);
			}
		}
		// 记录升级操作日志
		GamePublishEvent.publishEvent(new FuShuEquipSJLogEvent(userRoleId, guid, loginRole.getName(), goodsConfig
				.getId(), shengjiConfig.getItemId1(), shengjiConfig.getMoney(), shengjiConfig.getItemCount(),
				goodsConfig.getId(), endGoodsId));
		if (flag) {
			return new Object[] { 1, guid, endGoodsId, roleItem.getRandomAttrs() };
		} else {
			return AppErrorCode.FUSHU_SHENGJI_SHIBEI;
		}
	}
	/**
	 * 套装铸神
	 * @param userRoleId
	 * @param guid
	 * @param busMsgQueue
	 * @return
	 */
	public Object[] taozhuangzhushen(Long userRoleId, long guid, BusMsgQueue busMsgQueue, boolean isAutoGM) {
		RoleItemExport roleItem = null;
		for (ContainerType containerType : ContainerType.values()) {
			if (containerType.isEquip()) {
				roleItem = roleBagExportService.getOtherEquip(userRoleId, guid, containerType);
				if (roleItem != null) {
					break;
				}
			}
		}
		if (roleItem == null) {
			return AppErrorCode.NO_ZS_EQUIP;
		}
		int qhLevel = roleItem.getZhushenLevel() == null ? 0 : roleItem.getZhushenLevel();
		int maxLevel = taoZhuangZhuShenConfigExportService.getMaxLevel();
		if (qhLevel >= maxLevel) {
			return AppErrorCode.ZHUSHEN_MAX_LEVEL;
		}
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleItem.getGoodsId());
		if (goodsConfig == null) {
			return AppErrorCode.NO_FIND_CONFIG;
		}
		// 判断是否套装
		if (goodsConfig.getSuit() == 0) {
			return AppErrorCode.NO_ZHUSHEN;
		}
		// int position = goodsConfig.getEqpart();
		int nextQHLevel = qhLevel + 1;
		TaoZhuangZhuShenConfig qhConfig = taoZhuangZhuShenConfigExportService.loadByLevel(nextQHLevel);
		if (qhConfig == null) {
			return AppErrorCode.EQUIP_ZHUSHEN_LEVEL_ERROR;
		}
		List<String> needGoodsIds = shengJiBiaoConfigExportService.getConsumeIds(qhConfig.getNeeditem());
		int needCount = qhConfig.getNeednum();
		Map<String, Integer> tempResources = new HashMap<>();
		for (String goodsId : needGoodsIds) {
			// int oldNeedCount = needResource.get(goodsId) == null ? 0 : needResource.get(goodsId);
			int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
			if (owerCount >= needCount) {
				tempResources.put(goodsId, needCount);
				needCount = 0;
				break;
			}
			needCount = needCount - owerCount;
			tempResources.put(goodsId, owerCount);
		}
		// 升级需要消耗的银两
		int yl = qhConfig.getNeedmoney();
		Object[] isOb = accountExportService.isEnought(GoodsCategory.MONEY, yl, userRoleId);
		if (null != isOb) {
			return AppErrorCode.JB_ERROR;
		}
		// 扣除金币
		if (yl > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, yl, userRoleId,
					LogPrintHandle.CONSUME_EQUIP_ZHUSHEN, true, LogPrintHandle.CBZ_EQUIP_ZHUSHEN);
		}
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(tempResources, userRoleId,
				GoodsSource.EQUIP_ZHUSHEN, true, true);
		Integer failTimes = roleItem.getZhushenFailTimes();
		if (failTimes == null) {
			failTimes = 0;
		}
		boolean flag = isZhushenSuccess(qhConfig, failTimes);
		int realLevel = qhLevel;
		if (flag) {
			roleBagExportService.updateZhushenLevel(nextQHLevel, guid, userRoleId);
			realLevel = qhLevel + 1;
			// 如果穿在身上,需要通知属性变化
			notifyOtherRoleSync(userRoleId, null, ContainerType.BODYTITEM);
			// 强化成功成就推送
			// try {
			// BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new
			// Object[]{GameConstants.CJ_QIANGHUA, 0});
			// //roleChengJiuExportService.tuisongChengJiu(userRoleId, GameConstants.CJ_QIANGHUA,
			// 0);
			// } catch (Exception e) {
			// ChuanQiLog.error(""+e);
			// }
		} else {
			failTimes = failTimes + 1;
			roleBagExportService.updateZhushenFailTimes(failTimes, guid, userRoleId);
		}
		ChuanQiLog.error("userRoleId={} guid ={} zhushen level={} success={},failTimes={}", new Object[] { userRoleId,
				guid, realLevel, flag, failTimes });
		// 活跃度lxn
		// huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A2);
		// 记录强化操作日志
		JSONArray consumeItemArray = new JSONArray();
		LogFormatUtils.parseJSONArray(bagSlots, consumeItemArray);
		recordEquipZhuShenLog(userRoleId, guid, roleItem.getGoodsId(), consumeItemArray, yl, qhLevel, realLevel);
		if (flag) {
			// 成功
			float nextRate = 0;
			if (realLevel != taoZhuangZhuShenConfigExportService.getMaxLevel()) {
				TaoZhuangZhuShenConfig nextConfig = taoZhuangZhuShenConfigExportService.loadByLevel(realLevel + 1);
				nextRate = ((nextConfig.getOdds()) / 100f);
			}
			return new Object[] { 1, guid, realLevel, nextRate };
		} else {
			// 失败
			return new Object[] { 2, guid, Math.floor((qhConfig.getOdds() + qhConfig.getAddodds() * failTimes) / 100f) };
		}
	}
	private boolean isZhushenSuccess(TaoZhuangZhuShenConfig qhConfig, int failTimes) {
		int rate = qhConfig.getOdds() + failTimes * qhConfig.getAddodds();
		int randomValue = RandomUtil.getIntRandomValue(1, 10001);
		if (rate >= randomValue) {
			return true;
		}
		return false;
	}
	/**
	 * 暗金装备统计
	 */
	public void calcEquipRecordLog() {
		List<GoodsConfig> allConfig = goodsConfigExportService.loadAllConfigs();
		if (ObjectUtil.isEmpty(allConfig)) {
			ChuanQiLog.error("goods config is all null");
			return;
		}
		/* 暗金装备物品id */
		List<String> anjinGuIds = new ArrayList<>();
		/* 神武装备物品id */
		List<String> swGuIds = new ArrayList<>();
		for (GoodsConfig config : allConfig) {
			if (config.anjinEquip()) { // 暗金装备
				anjinGuIds.add(config.getId());
			}
			if (config.swEquip()) { // 神武装备
				swGuIds.add(config.getId());
			}
		}
		calcAnjinEquip(anjinGuIds);
		calcSwEquip(swGuIds);
	}
	/**
	 * 记录暗金装备统计日志
	 * @param anjinGuIds
	 */
	private void calcAnjinEquip(List<String> anjinGuIds) {
		if (ObjectUtil.isEmpty(anjinGuIds)) {
			return;
		}
		List<Map<String, Object>> anjinEquipLogList = roleBagDao.queryAnjinEquipCalcList(anjinGuIds);
		if (!ObjectUtil.isEmpty(anjinEquipLogList)) {
			for (Map<String, Object> anjinEquipMap : anjinEquipLogList) {
				int roleLevel = CovertObjectUtil.object2int(anjinEquipMap.get("role_level"));
				int roleCount = CovertObjectUtil.object2int(anjinEquipMap.get("role_cnt"));
				int equipCount = CovertObjectUtil.object2int(anjinEquipMap.get("equip_cnt"));
				GamePublishEvent.publishEvent(new AnjinEquipLogEvent(roleLevel, roleCount, equipCount));
			}
		}
	}
	/**
	 * 记录神武装备统计日志
	 * @param anjinGuIds
	 */
	private void calcSwEquip(List<String> swGuIds) {
		if (ObjectUtil.isEmpty(swGuIds)) {
			return;
		}
		List<Map<String, Object>> swEquipLogList = roleBagDao.querySwEquipCalcList(swGuIds);
		if (!ObjectUtil.isEmpty(swEquipLogList)) {
			for (Map<String, Object> swEquipMap : swEquipLogList) {
				String goodsId = CovertObjectUtil.object2String(swEquipMap.get("goods_id"));
				int goodsCount = CovertObjectUtil.object2int(swEquipMap.get("goods_count"));
				GoodsConfig goods = goodsConfigExportService.loadById(goodsId);
				if (null == goods) {
					continue;
				}
				int equipPart = goods.getEqpart();
				int equipQuality = goods.getNeedzhuansheng();
				GamePublishEvent.publishEvent(new ShenwuEquipLogEvent(equipPart, equipQuality, goodsCount));
			}
		}
	}
	// ----------------------------------宠物附属装备操作------------------------------------------//
	/**
	 * 宠物换装业务
	 * @param userRoleId
	 * @param chongwuConfigId 换装宠物配置编号
	 * @param guid 源物品guid
	 * @param targetSlot 目标格位号
	 * @return
	 */
	public Object[] chongwuEquipChange(Long userRoleId, Integer chongwuConfigId, Long guid, Integer targetSlot) {
		
		if (null == chongwuConfigId || null == guid || null == targetSlot) {
			return AppErrorCode.PAIMAI_ERROR;
		}
		Object[] checkData = checkChongwuEquipInfo(userRoleId, chongwuConfigId);
		if (!(Boolean) checkData[0]) {
			return (Object[]) checkData[1];
		}
		/* 物品校验 */
		RoleItem roleItem = roleBagDao.cacheLoad(guid, userRoleId);
		if (roleItem == null) {
			return AppErrorCode.NOT_FOUND_GOOODS;
		}
		/* 目标容器类型校验 */
		ContainerType containerType = BagUtil.getContainerTypeBySlot(targetSlot);
		if (containerType == null) {
			return AppErrorCode.NO_TARGET_SLOT;
		}
		/* 源容器类型 */
		ContainerType sourceType = null;
		if (targetSlot >= 0) {// 如是脱下 检查身上是否有该装备
			RoleItemExport roleItemExport = null;
			for (ContainerType type : ContainerType.values()) {
				if (type.isEquip()) {
					roleItemExport = roleBagExportService.getOtherEquip(userRoleId, guid, type);
					if (roleItemExport != null) {
						sourceType = type;
						break;
					}
				}
			}
			if (roleItemExport == null) {
				return AppErrorCode.BODY_NO_ITEM;
			}
		}
		/* 装备移动 */
		BagSlots bagSlots = roleBagExportService.moveSlot2(guid, targetSlot, containerType.getType(), userRoleId,
				chongwuConfigId);
		if (!bagSlots.isSuccee()) {
			return bagSlots.getErrorCode();
		}
		/* 通知场景 */
		if (sourceType != null) {// 脱下装备
			notifyOtherRoleSync(userRoleId, null, sourceType);
		} else {// 穿上装备
			notifyOtherRoleSync(userRoleId, null, containerType);
		}
		/* 返回数据 */
		Object[] result = new Object[4];
		int index = 0;
		result[index++] = 1;
		List<RoleItemOperation> roleItemVos = bagSlots.getRoleItemVos();
		for (int i = 0; i < roleItemVos.size(); i++) {
			result[index++] = BagOutputWrapper.getOutWrapperData(OutputType.CHONGWUMOVESLOT, roleItemVos.get(i));
		}
		return result;
	}
	/**
	 * 宠物附属装备强化
	 * @param userRoleId
	 * @param chongwuConfigId 宠物编号
	 * @param guid 装备guid
	 * @return
	 */
	public Object[] chongwuEquipStrong(Long userRoleId, Integer chongwuConfigId, Long guid) {
		/* 参数有效性校验 */
		if (null == chongwuConfigId || null == guid) {
			return AppErrorCode.PAIMAI_ERROR;
		}
		/* 操作前的基本校验 */
		Object[] checkData = checkChongwuEquipInfo(userRoleId, chongwuConfigId);
		if (!(Boolean) checkData[0]) {
			return (Object[]) checkData[1];
		}
		/* 操作的附属装备存在校验 */
		RoleItemExport roleItem = null;
		ContainerType equipContainerType = null;
		for (ContainerType containerType : ContainerType.values()) {
			if (containerType.isEquip()) {
				roleItem = roleBagExportService.getOtherEquip(userRoleId, guid, containerType);
				if (roleItem != null) {
					equipContainerType = containerType;
					break;
				}
			}
		}
		if (roleItem == null) {
			return AppErrorCode.NO_QH_EQUIP;
		}
		if (!ContainerType.CHONGWUITEM.equals(equipContainerType)) {
			return AppErrorCode.NO_QH;
		}
		/* 附属装备强化等级限制校验 */
		int qhLevel = roleItem.getQianhuaLevel() == null ? 0 : roleItem.getQianhuaLevel();
		int maxQhLevel = chongwuFushuEquipConfigExportService.getChongwuFushuEquipMaxQianghuaLevel();
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(roleItem.getGoodsId());
		if (goodsConfig == null) {
			return AppErrorCode.NO_FIND_CONFIG;
		}
		maxQhLevel = maxQhLevel > goodsConfig.getMaxIntensify() ? goodsConfig.getMaxIntensify() : maxQhLevel;
		if (qhLevel >= maxQhLevel) {
			return AppErrorCode.EQUIP_MAX_LEVEL;
		}
		if (goodsConfig.getRareLevel() < EquipContants.CHONGWU_QIANGHUA_MIN_RARELEVEL) {
			return AppErrorCode.NO_QH;
		}
		/* 强化消耗校验 */
		int nextQhLevel = qhLevel + 1;
		ChongwuFushuEquipQianghuaConfig qhConfig = loadChongwuFushuQianghuaConfig(nextQhLevel);
		if (qhConfig == null) {
			return AppErrorCode.NO_FIND_CONFIG;
		}
		/* 检验银两消耗 */
		int money = qhConfig.getNeedMoney();
		if (money > 0) {
			Object[] moneyError = roleBagExportService.isEnought(GoodsCategory.MONEY, money, userRoleId);
			if (null != moneyError) {
				return AppErrorCode.JB_ERROR;
			}
		}
		/* 道具消耗 */
		String itemId1 = qhConfig.getNeedItemId();
		int itemCount = qhConfig.getNeedItemCount();
		Object[] result = roleBagExportService.checkRemoveBagItemByGoodsType(itemId1, itemCount, userRoleId);
		if (result != null) {
			return result;
		}
		// 扣除银两
		if (money > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId,
					LogPrintHandle.CONSUME_CHONGWU_EQUIP_QIANGHUA, true, LogPrintHandle.CBZ_CHONGWU_EQUIP_QIANGHUA);
		}
		// 扣除道具
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoodsType(itemId1, itemCount, userRoleId,
				GoodsSource.CHONGWU_EQUIP_QIANGHUA, true, true);
		if (!bagSlots.isSuccee()) {
			return bagSlots.getErrorCode();
		}
		// 更新数据
		roleBagExportService.updateQHLevel(nextQhLevel, guid, userRoleId);
		// 如果穿在身上,需要通知属性变化
		notifyOtherRoleSync(userRoleId, null, equipContainerType);
		// 活跃度lxn
		huoYueDuExportService.completeActivity(userRoleId, ActivityEnum.A2);
		// 支线
		taskBranchService.completeBranch(userRoleId, BranchEnum.B1,1);
		
		// 记录强化操作日志
		JSONArray consumeItemArray = new JSONArray();
		LogFormatUtils.parseJSONArray(bagSlots, consumeItemArray);
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		GamePublishEvent.publishEvent(new EquipQHLogEvent(userRoleId, guid, roleWrapper.getName(), roleItem
				.getGoodsId(), consumeItemArray, money, 0, 0, qhLevel, nextQhLevel));
		return new Object[] { AppErrorCode.SUCCESS, chongwuConfigId, guid, nextQhLevel };
	}
	/**
	 * 宠物附属装备升阶
	 * @param userRoleId
	 * @param chongwuConfigId 宠物配置id
	 * @param guid
	 * @return
	 */
	public Object[] chongwuEquipUplevel(Long userRoleId, Integer chongwuConfigId, Long guid) {
		/* 参数有效性校验 */
		if (null == chongwuConfigId || null == guid) {
			return AppErrorCode.PAIMAI_ERROR;
		}
		/* 操作前的基本校验 */
		Object[] checkData = checkChongwuEquipInfo(userRoleId, chongwuConfigId);
		if (!(Boolean) checkData[0]) {
			return (Object[]) checkData[1];
		}
		/* 操作的附属装备存在校验 */
		RoleItemExport roleItem = null;
		ContainerType equipContainerType = null;
		for (ContainerType containerType : ContainerType.values()) {
			if (containerType.isEquip()) {
				roleItem = roleBagExportService.getOtherEquip(userRoleId, guid, containerType);
				if (roleItem != null) {
					equipContainerType = containerType;
					break;
				}
			}
		}
		if (roleItem == null) {
			return AppErrorCode.SJ_ZHUANBEI_NOT;
		}
		/* 宠物附属装备升阶,装备位置校验 */
		if (!ContainerType.CHONGWUITEM.equals(equipContainerType)) {
			return AppErrorCode.ZHUANGBEI_NOT_SJ;
		}
		String curGoodsId = roleItem.getGoodsId();// 当前等阶的装备对应的物品id
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(curGoodsId);
		if (null == goodsConfig) {
			return AppErrorCode.SJ_ZHUANBEI_NOT_CONFIG;
		}
		/* 附属装备是否能进阶校验 */
		String nextGoodsId = goodsConfig.getNextid();// 下一等阶的装备对应的物品id
		GoodsConfig nextGoodsConfig = goodsConfigExportService.loadById(nextGoodsId);
		if (null == nextGoodsConfig) {
			return AppErrorCode.ZHUANGBEI_NOT_SJ;
		}
		/* 附属装备升阶后,角色等级校验 */
		RoleWrapper loginRole = roleExportService.getLoginRole(userRoleId);// 人物
		if (loginRole.getLevel() < nextGoodsConfig.getLevelReq()) {
			return AppErrorCode.ZHUANGBEI_NOT_LEVEL;
		}
		/* 附属装备升阶后,宠物等阶校验 */
		int nextLevel = nextGoodsConfig.getData1();// 下一等阶
		RoleChongwu roleChongwu = chongwuExportService.getRoleChongwuById(userRoleId, chongwuConfigId);
		Integer chongwuLevel = null == roleChongwu ? -1 : roleChongwu.getJie();
		if (chongwuLevel != null && chongwuLevel + 1 < nextLevel) {
			return AppErrorCode.ZHUANGBEI_NOT_ENOUGH_LEVEL;
		}
		/* 附属装备升阶消耗 */
		int curLevel = goodsConfig.getData1();// 当前等阶
		ChongwuFushuEquipShengjiConfig shengjiConfig = chongwuFushuEquipConfigExportService
				.loadShengjieConfig(curLevel);
		if (shengjiConfig == null) {
			return AppErrorCode.SHENGJI_CONFIG_ERROR;
		}
		/* 道具消耗是否足够校验 */
		String itemId1 = shengjiConfig.getItemId1();
		int itemCount = shengjiConfig.getItemCount();
		Object[] result = roleBagExportService.checkRemoveBagItemByGoodsType(itemId1, itemCount, userRoleId);
		if (result != null) {
			return result;
		}
		/* 银两消耗是否足够校验 */
		int money = shengjiConfig.getMoney();
		Object[] moneyError = roleBagExportService.isEnought(GoodsCategory.MONEY, money, userRoleId);
		if (null != moneyError) {
			return AppErrorCode.JB_ERROR;
		}
		// 扣除银两
		if (money > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId,
					LogPrintHandle.CONSUME_CHONGWU_EQUIP_SHENGJIE, true, LogPrintHandle.CBZ_CHONGWU_EQUIP_SHENGJIE);
		}
		// 扣除道具
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoodsType(itemId1, itemCount, userRoleId,
				GoodsSource.CHONGWU_EQUIP_SHENGJIE, true, true);
		if (!bagSlots.isSuccee()) {
			return bagSlots.getErrorCode();
		}
		// 装备升级
		String lastGoodsId = curGoodsId;// 最后的物品编号
		int successrate = shengjiConfig.getSuccess();
		boolean flag = isQHSuccess(successrate);
		if (flag) {// 升阶成功
			lastGoodsId = nextGoodsId;
			roleBagExportService.updateZBSJ(nextGoodsId, guid, userRoleId);
			// 需要通知属性变化
			notifyOtherRoleSync(userRoleId, null, equipContainerType);
		}
		// 记录升阶操作日志
		GamePublishEvent.publishEvent(new FuShuEquipSJLogEvent(userRoleId, guid, loginRole.getName(), curGoodsId,
				itemId1, money, itemCount, curGoodsId, lastGoodsId));
		if (flag) {
			return new Object[] { 1, chongwuConfigId, guid, lastGoodsId, roleItem.getRandomAttrs() };
		} else {
			return AppErrorCode.FUSHU_SHENGJI_SHIBEI;
		}
	}
	/**
	 * 宠物附属装备提品
	 * @param userRoleId
	 * @param chongwuConfigId
	 * @param guid
	 * @return
	 */
	public Object[] chongwuEquipTiPin(Long userRoleId, Integer chongwuConfigId, Long guid) {
		/* 参数有效性校验 */
		if (null == chongwuConfigId || null == guid) {
			return AppErrorCode.PAIMAI_ERROR;
		}
		/* 操作前的基本校验 */
		Object[] checkData = checkChongwuEquipInfo(userRoleId, chongwuConfigId);
		if (!(Boolean) checkData[0]) {
			return (Object[]) checkData[1];
		}
		/* 操作的附属装备存在校验 */
		RoleItemExport roleItem = null;
		ContainerType equipContainerType = null;
		for (ContainerType containerType : ContainerType.values()) {
			if (containerType.isEquip()) {
				roleItem = roleBagExportService.getOtherEquip(userRoleId, guid, containerType);
				if (roleItem != null) {
					equipContainerType = containerType;
					break;
				}
			}
		}
		if (roleItem == null) {
			return AppErrorCode.TP_ZHUANBEI_NOT;
		}
		/* 此发放提品操作的装备只能是宠物身上的装备 */
		if (!ContainerType.CHONGWUITEM.equals(equipContainerType)) {
			return AppErrorCode.EQUIP_CANNOT_TP;
		}
		/* 装备是否可以提品校验 */
		String curGoodsId = roleItem.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(curGoodsId);
		if (goodsConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		TiPinBiaoConfig tiPinBiaoConfig = tiPinBiaoConfigExportService.loadById(GameConstants.EQUIP_TIPIN_TYPE_CHONGWU,
				goodsConfig.getRareLevel());
		if (tiPinBiaoConfig == null || ObjectUtil.strIsEmpty(goodsConfig.getTipinId())) {
			return AppErrorCode.EQUIP_CANNOT_TP;// 装备不可提品
		}
		/* 道具消耗是否足够校验 */
		String itemId1 = tiPinBiaoConfig.getItemId();
		int itemCount = tiPinBiaoConfig.getItemCount();
		Object[] result = roleBagExportService.checkRemoveBagItemByGoodsType(itemId1, itemCount, userRoleId);
		if (result != null) {
			return result;
		}
		/* 银两消耗是否足够校验 */
		int money = tiPinBiaoConfig.getMoney();
		Object[] moneyError = roleBagExportService.isEnought(GoodsCategory.MONEY, money, userRoleId);
		if (null != moneyError) {
			return AppErrorCode.JB_ERROR;
		}
		// 扣除银两
		if (money > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId,
					LogPrintHandle.CONSUME_CHONGWU_EQUIP_TIPIN, true, LogPrintHandle.CBZ_CHONGWU_EQUIP_TIPIN);
		}
		// 扣除道具
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoodsType(itemId1, itemCount, userRoleId,
				GoodsSource.CHONGWU_EQUIP_TIPIN, true, true);
		if (!bagSlots.isSuccee()) {
			return bagSlots.getErrorCode();
		}
		// 更新数据
		Integer beforeTipinVal = (beforeTipinVal = roleItem.getTipinValue()) == null ? 0 : beforeTipinVal;
		Integer afterTipinVal = beforeTipinVal + Lottery.roll(tiPinBiaoConfig.getMin(), tiPinBiaoConfig.getMax());
		boolean tipinSuccess = afterTipinVal >= tiPinBiaoConfig.getMaxXing();
		String lasrGoodsId = null;
		if (tipinSuccess) {// 提品成功
			afterTipinVal = 0;
			lasrGoodsId = goodsConfig.getTipinId();
		}
		roleBagExportService.updateTiPinLevel(guid, userRoleId, afterTipinVal, lasrGoodsId);
		Integer randomAttrId = null;
		if (tipinSuccess) {// 提品成功
			randomAttrId = roleItem.getRandomAttrs();
			notifyOtherRoleSync(userRoleId, null, equipContainerType);
		}else{
			if((afterTipinVal/tiPinBiaoConfig.getStarMax() > beforeTipinVal/tiPinBiaoConfig.getStarMax())){
				notifyOtherRoleSync(userRoleId, null, equipContainerType);	
			}
		}
		// 记录提品操作日志
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		String roleName = (roleName = roleWrapper.getName()) == null ? "" : roleName;
		JSONArray consumeItemArray = new JSONArray();
		LogFormatUtils.parseJSONArray(bagSlots, consumeItemArray);
		GamePublishEvent.publishEvent(new EquipTPLogEvent(userRoleId, guid, roleName, curGoodsId, consumeItemArray,
				beforeTipinVal, afterTipinVal, lasrGoodsId));
		return new Object[] { 1, chongwuConfigId, guid, afterTipinVal, lasrGoodsId, randomAttrId };
	}
	
	
	
	// ----------------------------------神器附属装备操作------------------------------------------//
		/**
		 * 神器换装业务
		 * @param userRoleId
		 * @param shenQiConfigId 换装宠物配置编号
		 * @param guid 源物品guid
		 * @param targetSlot 目标格位号
		 * @return
		 */
		public Object[] shenQiEquipChange(Long userRoleId, Integer shenQiConfigId, Long guid,Integer targetSlot) {
			if (null == shenQiConfigId || null == guid || null == targetSlot) {
				return AppErrorCode.PAIMAI_ERROR;
			}
			Object[] checkData = checkShenQiEquipInfo(userRoleId, shenQiConfigId);
			if (!(Boolean) checkData[0]) {
				return (Object[]) checkData[1];
			}
			/* 物品校验 */
			RoleItem roleItem = roleBagDao.cacheLoad(guid, userRoleId);
			if (roleItem == null) {
				return AppErrorCode.NOT_FOUND_GOOODS;
			}
			/* 目标容器类型校验 */
			ContainerType containerType = BagUtil.getContainerTypeBySlot(targetSlot);
			if (containerType == null) {
				return AppErrorCode.NO_TARGET_SLOT;
			}
			/* 源容器类型 */
//			ContainerType sourceType = null;
			if (targetSlot >= 0) {// 如是脱下 检查身上是否有该装备
				RoleItemExport roleItemExport = null;
				for (ContainerType type : ContainerType.values()) {
					if (type.isEquip()) {
						roleItemExport = roleBagExportService.getOtherEquip(userRoleId, guid, type);
						if (roleItemExport != null) {
//							sourceType = type;
							break;
						}
					}
				}
				if (roleItemExport == null) {
					return AppErrorCode.BODY_NO_ITEM;
				}
			}
			/* 装备移动 */
			BagSlots bagSlots = roleBagExportService.moveSlot3(guid, targetSlot, containerType.getType(), userRoleId,
					shenQiConfigId);
			if (!bagSlots.isSuccee()) {
				return bagSlots.getErrorCode();
			}
			
			//通知场景装备属性变化 
			shenQiJinJieExportService.notifyStageChange(userRoleId);
			Object[] result = new Object[4];
			int index = 0;
			result[index++] = 1;
//			List<Object[]> resVo = new ArrayList<>();
			List<RoleItemOperation> roleItemVos = bagSlots.getRoleItemVos();
			for (int i = 0; i < roleItemVos.size(); i++) {
				RoleItemOperation entity = roleItemVos.get(i);
				result[index++] = new Object[]{entity.getGuid(),entity.getSlot(),shenQiConfigId};
			}
			return result;
			
		}
		
		
		 public Map<String, Long> getQHAttrs(int tpVal,GoodsConfig goodsConfig, int level, ContainerType type) {
		        Float qhxs = 0F;
		        if (ContainerType.BODYTITEM.equals(type)) {
		            if (goodsConfig.swEquip()) {
		                qhxs = shenWuXingZhuBiaoConfigExportService.getQhxs(level);
		            } else {
		                qhxs = qiangHuaBiaoConfigExportService.getQhxs(level);
		            }
		        } else if (ContainerType.CHONGWUITEM.equals(type)) {
		            qhxs = chongwuFushuEquipConfigExportService.getQhxs(level);
		        } else{
		            qhxs = fuShuQiangHuaBiaoConfigExportService.getQhxs(level);
		        }
		        if (qhxs == null) {
		            return null;
		        }
		        return calcQhAttributeByQhxs(tpVal,goodsConfig, qhxs);
		    }
		 
		 private Map<String, Long> calcQhAttributeByQhxs(int tpVal,GoodsConfig goodsConfig, float qhxs){
		        Map<String, Long> qhAttrs = new HashMap<>();
		        Map<String, Long> baseAttrs = goodsConfig.getEquipRealAttr(tpVal);
//		        Map<String, Long> baseAttrs = goodsConfig.getEquipBaseAttr();
		        if (!ObjectUtil.isEmpty(baseAttrs)) {
		            List<String> swzbAttr = EffectType.findSwZbQHAttrTypes();// 神武装备增加的效果属性集合
		            for (Entry<String, Long> entry : baseAttrs.entrySet()) {
		                String attr = entry.getKey();
		                if (!EffectType.zplus.name().equals(attr) && !swzbAttr.contains(attr)) {// 排除战力值和神武装备增加的效果属性
		                    qhAttrs.put(attr, Math.round(entry.getValue() * (double) qhxs));
		                }
		            }
		        }
		        // 设置新增的战力值
		        long zhanli = getAddZhanLi(qhAttrs);
		        if (zhanli > 0) {
		            qhAttrs.put(EffectType.zplus.name(), zhanli);
		        }

		        return qhAttrs;
		    }
		 
		 private long getAddZhanLi(Map<String, Long> attrs) {
		        if (attrs == null) {
		            return 0;
		        }
		        double zl = 0;
		        for (Entry<String, Long> entry : attrs.entrySet()) {
		            Float xs = zhanLiXiShuConfigExportService.getZLXS(entry.getKey());
		            if (xs == null) {
		                ChuanQiLog.error("强化计算战斗力增量错误:" + entry.getKey() + "没有找到对应的战力系数!");
		                continue;
		            }
		            zl += (double) xs * entry.getValue();
		        }
		        return Math.round(zl);
		    }
	
}
