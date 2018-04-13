package com.junyou.bus.equip.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.ContainerType;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.equip.configure.export.ShengJiBiaoConfigExportService;
import com.junyou.bus.equip.configure.export.TaoZhuangXiangWeiConfig;
import com.junyou.bus.equip.configure.export.TaoZhuangXiangWeiConfigExportService;
import com.junyou.bus.equip.configure.export.TaoZhuangXiangWeiJieDuanConfigExportService;
import com.junyou.bus.equip.dao.RoleXiangweiDao;
import com.junyou.bus.equip.entity.RoleXiangwei;
import com.junyou.bus.equip.filter.RoleXiangweiFilter;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.EquipXiangWeiLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.event.util.LogFormatUtils;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.SuitXiangweiPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * 
 * @description:套装象位业务层 
 *
 *	@author ChuBin
 *
 * @date 2016-12-13
 */
@Service
public class RoleXiangweiService {
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private TaoZhuangXiangWeiConfigExportService taoZhuangXiangWeiConfigExportService;
	@Autowired
	private ShengJiBiaoConfigExportService shengJiBiaoConfigExportService;
	@Autowired
	private RoleXiangweiDao roleXiangweiDao;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private TaoZhuangXiangWeiJieDuanConfigExportService taoZhuangXiangWeiJieDuanConfigExportService;

	public List<RoleXiangwei> initRoleXiangwei(Long userRoleId) {
		return roleXiangweiDao.initRoleXiangwei(userRoleId);
	}

	/**
	 * 套装象位升星
	 * @param userRoleId
	 * @param guid
	 * @param busMsgQueue
	 * @return
	 */
	public Object[] taozhuangXiangWeiUp(Long userRoleId, long guid, BusMsgQueue busMsgQueue) {
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
			return AppErrorCode.BODY_NO_ITEM;
		}

		Object[] result = modelOpenCheck(userRoleId);
		if (result != null) {
			return result;
		}

		GoodsConfig goods = goodsConfigExportService.loadById(roleItem.getGoodsId());
		if (goods == null) {
			return AppErrorCode.CONFIG_ERROR;
		}

		// 判断是否套装
		if (goods.getSuit() == 0) {
			return AppErrorCode.EQUIP_TYPE_ERROR;
		}

		int buwei = goods.getEqpart();
		RoleXiangwei roleXiangwei = getRoleXiangwei(userRoleId, buwei);
		int xiangwei = roleXiangwei.getXiangweiId() == null ? 0 : roleXiangwei.getXiangweiId();
		int star = roleXiangwei.getXiangweiStar() == null ? 0 : roleXiangwei.getXiangweiStar();

		TaoZhuangXiangWeiConfig nextConfig = taoZhuangXiangWeiConfigExportService.getNextConfigByCondition(buwei,
				xiangwei, star);
		if (nextConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}

		int needZsLevel = nextConfig.getNeedZsLevel();
		Integer zhushenLevel = roleItem.getZhushenLevel() == null ? 0 : roleItem.getZhushenLevel();
		if (zhushenLevel < needZsLevel) {
			// 铸神等级不足
			return AppErrorCode.EQUIP_ZHUSHEN_LEVEL_ERROR;
		}

		List<String> needGoodsIds = shengJiBiaoConfigExportService.getConsumeIds(nextConfig.getConsumeId());
		int needCount = nextConfig.getNeedCount();
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
			int bPrice = nextConfig.getBgold();// 绑定元宝的价格
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
			int price = nextConfig.getGold();
			int nowNeedGold = needCount * price;

			Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, nowNeedGold, userRoleId);
			if (null != goldError) {
				return goldError;
			}

			tempResources.put(GoodsCategory.GOLD + "", nowNeedGold);
			needCount = 0;
		}

		if (needCount > 0) {
			return AppErrorCode.ITEM_NOT_ENOUGH;
		}

		Integer newNeedGold = tempResources.remove(GoodsCategory.GOLD + "");
		Integer newNeedBgold = tempResources.remove(GoodsCategory.BGOLD + "");

		// 扣除元宝
		if (newNeedGold != null && newNeedGold > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, newNeedGold, userRoleId,
					LogPrintHandle.CONSUME_XIANGWEI_SJ, true, LogPrintHandle.CBZ_XIANGWEI_SJ);
			// 腾讯OSS消费上报
			if (PlatformConstants.isQQ()) {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] {
						QqConstants.ZHIFU_YB, newNeedGold, LogPrintHandle.CONSUME_XIANGWEI_SJ,
						QQXiaoFeiType.CONSUME_XIANGWEI_SJ, 1 });
			}
		}
		// 扣除绑定元宝
		if (newNeedBgold != null && newNeedBgold > 0) {
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, newNeedBgold, userRoleId,
					LogPrintHandle.CONSUME_XIANGWEI_SJ, true, LogPrintHandle.CBZ_XIANGWEI_SJ);
			// 腾讯OSS消费上报
			if (PlatformConstants.isQQ()) {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] {
						QqConstants.ZHIFU_BYB, newNeedBgold, LogPrintHandle.CONSUME_XIANGWEI_SJ,
						QQXiaoFeiType.CONSUME_XIANGWEI_SJ, 1 });
			}
		}

		Integer nextJd = nextConfig.getJd();
		Integer nextStar = nextConfig.getStar();
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoods(tempResources, userRoleId,
				GoodsSource.CONSUME_SUIT_XIANGWEI_SJ, true, true);

		// 更新信息
		roleXiangwei.setXiangweiId(nextJd);
		roleXiangwei.setXiangweiStar(nextStar);
		roleXiangwei.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleXiangweiDao.cacheUpdate(roleXiangwei, userRoleId);

		// 属性变更通知
		notifyAttrChange(userRoleId);

		try {
			// 记录日志
			JSONArray consumeItemArray = new JSONArray();
			LogFormatUtils.parseJSONArray(bagSlots, consumeItemArray);
			GamePublishEvent.publishEvent(new EquipXiangWeiLogEvent(userRoleId//
					, buwei//
					, roleItem.getGoodsId()//
					, consumeItemArray//
					, newNeedBgold == null ? 0 : newNeedGold//
					, newNeedGold == null ? 0 : newNeedGold//
					, xiangwei//
					, nextJd//
					, star//
					, nextStar));
		} catch (Exception e) {
			ChuanQiLog.error("套装象位升星日志报错!! userRoleId={},guid={}", userRoleId, guid);
		}

		// 成功:[0:int(1),1:Number(装备guid),1:int(星象id),2:int(当前星数)]
		return new Object[] { AppErrorCode.SUCCESS, guid, nextJd, nextStar };
	}

	public Map<String, Long> getRoleAttrs(Long userRoleId) {
		Map<String, Long> result = new HashMap<>();
		List<RoleXiangwei> list = roleXiangweiDao.cacheLoadAll(userRoleId);
		if (list == null || list.size() == 0) {
			return result;
		}

		for (RoleXiangwei roleXiangwei : list) {
			Integer jd = roleXiangwei.getXiangweiId();
			Integer star = roleXiangwei.getXiangweiStar();
			Integer buwei = roleXiangwei.getBuwei();

			// 容错
			if (jd == null || star == null || buwei == null) {
				continue;
			}

			// 添加象位基础属性
			TaoZhuangXiangWeiConfig baseConfig = taoZhuangXiangWeiConfigExportService.getConfigByCondition(buwei, jd,
					star);
			if (baseConfig != null && baseConfig.getAttrs() != null) {
				ObjectUtil.longMapAdd(result, baseConfig.getAttrs());
			}

			// 添加象位固定阶段特殊属性
			Map<String, Long> specilAttrs = taoZhuangXiangWeiJieDuanConfigExportService.getTotalAttrs(buwei, jd, star);
			ObjectUtil.longMapAdd(result, specilAttrs);
		}

		return result;
	}

	private void notifyAttrChange(Long userRoleId) {
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_XIANGWEI_CHANGE, getRoleAttrs(userRoleId));
	}

	/**
	 * 
	 * @param userRoleId
	 *@description:套装象位模块开启条件检查 
	 * @return
	 */
	private Object[] modelOpenCheck(Long userRoleId) {
		UserRole user = roleExportService.getUserRole(userRoleId);
		SuitXiangweiPublicConfig publicConfig = gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_TAOZHUANG_XIANGWEI);

		if (user == null || publicConfig == null || user.getLevel() < publicConfig.getNeedLevel()) {
			return AppErrorCode.LEVEL_NOT_ENOUGH;
		}

		return null;
	}

	private RoleXiangwei getRoleXiangwei(Long userRoleId, int buwei) {
		List<RoleXiangwei> list = roleXiangweiDao.cacheLoadAll(userRoleId, new RoleXiangweiFilter(buwei));
		if (list != null && list.size() > 0) {
			return list.get(0);
		}

		RoleXiangwei entity = new RoleXiangwei();
		entity.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		entity.setBuwei(buwei);
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());
		entity.setUserRoleId(userRoleId);
		entity.setXiangweiId(1);
		entity.setXiangweiStar(0);
		roleXiangweiDao.cacheInsert(entity, userRoleId);

		return entity;
	}

	public Object[] getRoleXiangweiInfo(Long userRoleId) {
		Set<Integer> buweiIds = taoZhuangXiangWeiConfigExportService.loadAllBuweiIdSet();
		if (buweiIds == null || buweiIds.size() == 0) {
			return null;
		}

		List<Object[]> result = new ArrayList<>();
		for (Integer buweiId : buweiIds) {
			RoleXiangwei info = getRoleXiangwei(userRoleId, buweiId);
			result.add(new Object[] { buweiId, info.getXiangweiId(), info.getXiangweiStar() });
		}

		return result.toArray();
	}
}
