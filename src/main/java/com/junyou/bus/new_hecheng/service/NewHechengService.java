package com.junyou.bus.new_hecheng.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.new_hecheng.configure.export.NewHechengConfig;
import com.junyou.bus.new_hecheng.configure.export.NewHechengConfigExportService;
import com.junyou.err.AppErrorCode;
import com.junyou.event.JewelLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.utils.lottery.RandomUtil;

@Service
public class NewHechengService {
	@Autowired
	private NewHechengConfigExportService newHechengConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	// 成功比率的最大值
	private static final int MAX_SUCCESS_RANGE = 1000;

	/**
	 * 
	 *@description: 
	 * @param userRoleId 
	 * @param configId
	 * @param count 单次合成消耗的物品数
	 * @param loop 合成次数
	 * @param clientSuccessRate 客户端显示的成功率
	 * @return
	 */
	public Object[] hecheng(Long userRoleId, int configId, int count, int loop, float clientSuccessRate) {
		NewHechengConfig config = newHechengConfigExportService.loadPublicConfig(configId);
		if (config == null || config.getFromItem() == null || config.getToItem() == null
				|| config.getClientGailv() == null) {
			return AppErrorCode.CONFIG_ERROR;
		}

		GoodsConfig goodsConfig = goodsConfigExportService.loadById(config.getFromItem());
		if (goodsConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}

		// 使用物品数量验证
		if (count <= 0 || loop <= 0 || Integer.MAX_VALUE / count < loop) {
			return AppErrorCode.ITEM_COUNT_ENOUGH;
		}

		int needCount = count * loop;
		String fromGoods = config.getFromItem();
		String toGoods = config.getToItem();

		// 背包物品数量检查
		Object[] checkCountResult = roleBagExportService.checkRemoveBagItemByGoodsId(fromGoods, needCount, userRoleId);
		if (checkCountResult != null) {
			return checkCountResult;
		}

		// 背包空格检查(检查是否能装下全部成功时的物品数量)
		Object[] checkPutResult = roleBagExportService.checkPutInBag(toGoods, loop, userRoleId);
		if (checkPutResult != null) {
			return checkPutResult;
		}

		int successCount = 0;
		int failCount = 0;

		// 客户端显示的成功率值
		float clientSuccessRange = config.getClientGailv() * count;
		if (clientSuccessRate * 10 != clientSuccessRange) {
			return AppErrorCode.CONFIG_ERROR;
		}

		// 服务器实际计算的成功率值
		float realSuccessRange = config.getRealGaiLv() * count;

		if (clientSuccessRange > MAX_SUCCESS_RANGE || realSuccessRange > MAX_SUCCESS_RANGE) {
			return AppErrorCode.CONFIG_ERROR;
		}

		for (int i = 0; i < loop; i++) {
			// 当前端显示的成功率达到100%时,直接成功,否则按照实际成功率判断
			if (clientSuccessRange == MAX_SUCCESS_RANGE || isHeChengSuccess(realSuccessRange)) {
				successCount++;
			} else {
				failCount++;
			}
		} 

		// 消耗物品
		roleBagExportService.removeBagItemByGoodsId(fromGoods, needCount, userRoleId, GoodsSource.CONSUME_NEW_HECHENG,
				true, true);

		if (successCount > 0) {
			Map<String, Integer> goodMap = new HashMap<String, Integer>();
			goodMap.put(toGoods, successCount);
			roleBagExportService.putInBag(goodMap, userRoleId, GoodsSource.GET_NEW_HECHENG, true);
		}

		GamePublishEvent.publishEvent(new JewelLogEvent(userRoleId, 3, toGoods, successCount, fromGoods, needCount));
		return new Object[] { AppErrorCode.SUCCESS, successCount, failCount };
	}

	private static boolean isHeChengSuccess(float successRange) {
		return RandomUtil.getIntRandomValue(1, MAX_SUCCESS_RANGE + 1) <= successRange;
	}

}
