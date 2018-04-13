package com.junyou.gameconfig.checker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.junyou.bus.shizhuang.configure.export.ShiZhuangJinJieConfig;
import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.ObjectUtil;

/**
 * 检测物品配置
 * @author LiuYu
 * @date 2015-1-30 下午4:37:48
 */
@Component
public class GoodsConfigChecker {
	
	private static GoodsConfigExportService goodsConfigExportService;
	
	@Autowired 
	public void setGoodsConfigExportService(GoodsConfigExportService goodsConfigExportService) {
		GoodsConfigChecker.goodsConfigExportService = goodsConfigExportService;
	}
	private static List<IGoodsCheckConfig> configs = new ArrayList<>();
	
	/**
	 * 注册检测
	 * @param config
	 */
	public static void registCheck(IGoodsCheckConfig config){
		configs.add(config);
	}
	/**
	 * 开始检测
	 */
	public static void startCheck(){
		if(GameServerContext.getGameAppConfig().isDebug()){//非debug不启动检测
			ChuanQiLog.debug("===================================================");
			ChuanQiLog.debug("开始检测物品配置");
			for (IGoodsCheckConfig config : configs) {
				List<Map<String,Integer>> list = config.getCheckMap();
				if(list == null)continue;
				for (Map<String,Integer> map : list) {
					for (String goodsId : map.keySet()) {
						GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
						if(goodsConfig == null){
							ChuanQiLog.error(config.getConfigName() + " is error. goodsId not exist:" + goodsId);
						}
					}
				}
			}
			ChuanQiLog.debug("物品配置检测完毕");
			ChuanQiLog.debug("===================================================");
		}
		configs.clear();
	}
	
	/**
	 * 检测物品转换
	 */
	public static void startCheckZhuanHuan(){
		if(GameServerContext.getGameAppConfig().isDebug()){//非debug不启动检测
			boolean checkFail = false;
			ChuanQiLog.debug("===================================================");
			ChuanQiLog.debug("开始检测装备穿戴后转换配置");
			for (GoodsConfig config : goodsConfigExportService.loadAllConfigs()) {
				if(!ObjectUtil.strIsEmpty(config.getEquipId())){
					GoodsConfig equipConfig = goodsConfigExportService.loadById(config.getEquipId());
					if(equipConfig == null){
						checkFail = true;
						ChuanQiLog.debug("id:{}穿戴后转换的装备不存在，穿戴后id:{}",config.getId(),config.getEquipId());
					}
					if(!equipConfig.isBind()){
						checkFail = true;
						ChuanQiLog.debug("id:{}穿戴后转换的装备不是绑定的装备，穿戴后id:{}",config.getId(),config.getEquipId());
					}
					if(!equipConfig.getName().equals(config.getName())){
						checkFail = true;
						ChuanQiLog.debug("id:{}穿戴后转换的装备名字不一致，穿戴前名字：{}，穿戴后名字:{}",config.getId(),config.getName(),equipConfig.getName());
					}
//					if(!equipConfig.getId1().equals(config.getId1())){
//						checkFail = true;
//						ChuanQiLog.debug("id:{}穿戴后转换的装备大类ID不一致，穿戴前大类ID：{}，穿戴后大类ID:{}",config.getId(),config.getId1(),equipConfig.getId1());
//					}
				}
			}
			if(checkFail){
				throw new RuntimeException();
			}
			ChuanQiLog.debug("装备穿戴后转换配置检测完毕");
			ChuanQiLog.debug("===================================================");
		}
	}
	
}
