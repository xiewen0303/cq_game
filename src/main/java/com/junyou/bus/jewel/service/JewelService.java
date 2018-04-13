package com.junyou.bus.jewel.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.jewel.configure.HechengConfig;
import com.junyou.bus.jewel.configure.HechengConfigExportService;
import com.junyou.bus.jewel.configure.JewelConfig;
import com.junyou.bus.jewel.configure.JewelConfigExportService;
import com.junyou.bus.jewel.configure.JewelKaiKongConfig;
import com.junyou.bus.jewel.configure.JewelKaiKongConfigExportService;
import com.junyou.bus.jewel.dao.RoleJewelDao;
import com.junyou.bus.jewel.entity.RoleJewel;
import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.JewelLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class JewelService implements IFightVal {

	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		long result = 0;
		if(fightPowerType == FightPowerType.STONE_XQ){
			RoleJewel roleJewel = getRoleJewel(userRoleId);
			if(roleJewel.getAllData() == null){
				return 0;
			}

			for (Object obj : roleJewel.getAllData()) {
				JSONArray t1 = (JSONArray)obj;
				for (Object o1 : t1) {
					int baoShiId = CovertObjectUtil.object2int(o1);
					JewelConfig config = jewelConfigExportService.loadPublicConfig(baoShiId);
					if(config == null){
						continue;
					}
					result += CovertObjectUtil.getZplus(config.getAttrMap());
				}
			}
		}else{
			ChuanQiLog.error("not exist "+this.getClass().getSimpleName()+"fightPowerType="+fightPowerType);
		}
		return result;
	}


	@Autowired
	private RoleJewelDao roleJewelDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private JewelKaiKongConfigExportService jewelKaiKongConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private JewelConfigExportService jewelConfigExportService;
	@Autowired
	private HechengConfigExportService jewelHechengConfigExportService;
	@Autowired
	private AccountExportService accountExportService;

	/**
	 * 玩家登陆初始化
	 * 
	 * @param userRoleId
	 * @return
	 */
	public List<RoleJewel> initRoleJewel(Long userRoleId) {
		return roleJewelDao.initRoleJewel(userRoleId);
	}
	/**
	 * 初始化宝石面板数据
	 */
	public Object[] initPanelData(Long userRoleId) {
		RoleJewel roleJewel = this.getRoleJewel(userRoleId);
		if (roleJewel.getAllData() != null) {
			Object[] data = this.convertJSONArrayToObject(roleJewel);
			return new Object[] { 1, data };
		} else {
			return new Object[] { 0, AppErrorCode.PARAMETER_ERROR }; 
		}
	}
	/**
	 * 返回给客户端转成原生数组
	 * @param roleJewel
	 * @return
	 */
	private Object[] convertJSONArrayToObject(RoleJewel roleJewel){
		Object[] data = new Object[roleJewel.getAllData().length];
		for (int i = 0; i < roleJewel.getAllData().length; i++) {
		JSONArray jsonArray = (JSONArray) roleJewel.getAllData()[i];
		data[i] = jsonArray.toArray();
	   }
		return data;
	}
	/**
	 * 镶嵌宝石
	 */
	public Object[] jewelUp(Long userRoleId, int gwId, int kId, Long guid) {
		if(gwId>GameConstants.JEWEL_GEZI_NUM || kId>GameConstants.JEWEL_KONG_NUM){
			return AppErrorCode.PARAMETER_ERROR;
		}
		RoleItemExport item = roleBagExportService.getBagItemByGuid(userRoleId, guid);
		if (item == null) {
			return AppErrorCode.GOODS_NOT_ENOUGH;// 物品不存在
		} else {
			GoodsConfig config = goodsConfigExportService.loadById(item.getGoodsId());
			if (config == null) {
				return AppErrorCode.JEWEL_TYPE_ERROR;// 不是宝石类型
			}else{
				if(config.getCategory()!=70){
					return AppErrorCode.JEWEL_TYPE_ERROR;// 不是宝石类型
				}
			}
		}
		// 检查是否可以装在这个孔位
		JewelConfig jewelConfig = jewelConfigExportService.loadPublicConfig(Integer.parseInt(item.getGoodsId()));
		if (jewelConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		if (jewelConfig.getType() != kId) {
			// 不能镶嵌在这个孔位上
			return AppErrorCode.JEWEL_UP_POSITION_ERROR;
		}
		RoleJewel roleJewel = this.getRoleJewel(userRoleId);
		// 孔是否开启
		JSONArray kongData = (JSONArray) roleJewel.getAllData()[gwId - 1];
		if (kId > kongData.size()) {
			return AppErrorCode.PARAMETER_ERROR;
		}
		if ((Integer) kongData.get(kId - 1) == 0) {
			return AppErrorCode.JEWEL_KONG_NOT_OPEN; // 孔还未开启
		}
		
		//镶嵌的位置已经是当前宝石的不能再镶嵌
		if(Integer.parseInt(item.getGoodsId())==(Integer) kongData.get(kId - 1) ){
			return AppErrorCode.JEWEL_UP_ALREADY; //同一颗宝石已经镶嵌了，不要重复镶嵌
		}
		// 背包减掉一个道具
		BagSlots bagSlots = roleBagExportService.removeBagItemByGuid(guid, 1, userRoleId, GoodsSource.GOODS_JEWEL_UP, true, true);
		if (!bagSlots.isSuccee()) {
			return bagSlots.getErrorCode();
		}
		//如果装的孔之前已经镶嵌宝石，老的宝石下放到背包。生成新道具|宝石进背包
		if((Integer) kongData.get(kId - 1)>1){
			Map<String, Integer> items = new HashMap<>();
			String goodsId = String.valueOf(kongData.get(kId - 1));
			items.put(goodsId, 1);
			roleBagExportService.putInBag(items, userRoleId, GoodsSource.GOODS_JEWEL_UP_REPLACE, true);
			noticeStageJewelAttributeChange(userRoleId,0,goodsId);
		}
		kongData.set(kId - 1, Integer.parseInt(item.getGoodsId())); // 更新嵌套宝石id
		updateRoleJewel(userRoleId,roleJewel);
		noticeStageJewelAttributeChange(userRoleId,1,item.getGoodsId());
		//日志
		GamePublishEvent.publishEvent(new JewelLogEvent(userRoleId, 1,item.getGoodsId()));
		return new Object[] { 1, gwId, kId, item.getGoodsId() };
	}

	/**
	 * 宝石卸下
	 */
	public Object[] jewelDown(Long userRoleId, int gwId, int kId) {
		if(gwId>GameConstants.JEWEL_GEZI_NUM || kId>GameConstants.JEWEL_KONG_NUM){
			return AppErrorCode.PARAMETER_ERROR;
		}
		RoleJewel roleJewel = this.getRoleJewel(userRoleId);
		JSONArray kongData = (JSONArray) roleJewel.getAllData()[gwId - 1];
		if ((Integer) kongData.get(kId - 1) <= 1) {
			return AppErrorCode.JEWEL_DOWN_ERROR;// 此位置上没有镶嵌宝石
		}
		Map<String, Integer> items = new HashMap<>();
		items.put(String.valueOf(kongData.get(kId - 1)), 1);
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(items, userRoleId);
		// 背包空间不足 请先清理背包
		if (code != null) {
			return code;
		}
		int goodId = (Integer)kongData.get(kId - 1) ;
		// 更新数据
		kongData.set(kId - 1, 1);
		updateRoleJewel(userRoleId,roleJewel);
		// 背包新增一个道具
		roleBagExportService.putInBag(items, userRoleId, GoodsSource.GOODS_JEWEL_DOWN, true);
		noticeStageJewelAttributeChange(userRoleId,0,String.valueOf(goodId));
		//日志
		GamePublishEvent.publishEvent(new JewelLogEvent(userRoleId, 0,String.valueOf(kongData.get(kId - 1))));
		return new Object[] { 1, gwId, kId };
	}
	/**
	 * 打孔
	 */
	public Object[] jewelBurrow(Long userRoleId, int gwId, int kId) {
		if(gwId>GameConstants.JEWEL_GEZI_NUM || kId>GameConstants.JEWEL_KONG_NUM){
			return AppErrorCode.PARAMETER_ERROR;
		}
		RoleJewel roleJewel = this.getRoleJewel(userRoleId);
		JSONArray kongData = (JSONArray) roleJewel.getAllData()[gwId - 1];
		if((Integer)kongData.get(kId-1)==1){
			//已经开孔不能重复开
			return AppErrorCode.JEWEL_DAKONG_CHONGFU; 
		}
		//不能越级开孔
		if(kId>2){
			if((Integer)kongData.get(kId-2)==0){
				return AppErrorCode.JEWEL_DAKONG_YUEJI; 
			}
		}
		JewelKaiKongConfig kongConfig = jewelKaiKongConfigExportService.loadPublicConfig(gwId);
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		int roleLevel = role.getLevel();
		Object[] configData= kongConfig.getConsumeMap().get(kId);
		int monery = (Integer)configData[2];
		if(roleLevel<(Integer)configData[1]){
			//人物等级不足
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		//银两不足
		Object[] errorCode = accountExportService.isEnought(GoodsCategory.MONEY, monery, userRoleId);
		if(errorCode != null){
			return errorCode;
		}
		kongData.set(kId-1, 1);//开启孔
		this.updateRoleJewel(userRoleId,roleJewel);
		//银两消耗
		accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, monery, userRoleId, LogPrintHandle.CONSUME_JEWEL, true, LogPrintHandle.CBZ_JEWEL_UP);
		return new Object[]{1,gwId,kId};
	}
	/**
	 * 道具合成
	 */
	public Object[] jewelCompound(Long userRoleId, long guid, int num) {
		if(num<=0){
			return AppErrorCode.PARAMETER_ERROR;
		}
		RoleItemExport item = roleBagExportService.getBagItemByGuid(userRoleId, guid);
		GoodsConfig config  =null;
		if (item == null) {
			return AppErrorCode.GOODS_NOT_ENOUGH;// 物品不存在
		} else {
			 config = goodsConfigExportService.loadById(item.getGoodsId());
			if (config == null) {
				return AppErrorCode.GOODS_NOT_ENOUGH;// 物品不存在
			}else{
				if(!jewelHechengConfigExportService.getId1List().contains(config.getId1())){
					return AppErrorCode.JEWEL_TYPE_ERROR;// 不能合成的道具
				}
			}
		}
		int ownerNum = roleBagExportService.getBagItemCountByGoodsType(config.getId1(), userRoleId);
		
		HechengConfig hechengConfig = jewelHechengConfigExportService.loadPublicConfig(config.getId1());
		
		int needNum = num*hechengConfig.getNeednum();
		if(needNum>ownerNum){
			return  AppErrorCode.ITEM_COUNT_ENOUGH;
		}
		if(hechengConfig.getNextLevelid()==null){
			//到顶级了不能再合成
			return  AppErrorCode.JEWEL_LEVEL_MAX;
		}
		//合成后物品
		Map<String, Integer> items = new HashMap<>();
		GoodsConfig newConfig   = goodsConfigExportService.loadById(hechengConfig.getNextLevelid());
		if(newConfig==null){
			return AppErrorCode.GOODS_NOT_ENOUGH;// 物品不存在
		}
		items.put(hechengConfig.getNextLevelid(), num);
		Object[] ret = roleBagExportService.checkPutInBag(items, userRoleId);
		if(ret != null){
			return ret;
		}
		int needMoney = num * hechengConfig.getNeedmoney();
		Object[] result = accountExportService.isEnought(GoodsCategory.MONEY, needMoney, userRoleId);
		if(result != null){
			return result;
		}
		//合成消耗道具
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoodsType(config.getId1(), needNum, userRoleId,GoodsSource.GOODS_JEWEL_DOWN_HECHENG_CONSUME, true, true);
		if (!bagSlots.isSuccee()) {
			return bagSlots.getErrorCode();
		}
		accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, needMoney, userRoleId, LogPrintHandle.CONSUME_JEWEL, true, LogPrintHandle.CBZ_JEWEL_HECHENG);
		//生成新道具 进背包
		roleBagExportService.putInBag(items, userRoleId, GoodsSource.GOODS_JEWEL_HECHENG_GET, true);
		//日志
		GamePublishEvent.publishEvent(
		new JewelLogEvent(userRoleId, 2,String.valueOf(hechengConfig.getNextLevelid()),num,item.getGoodsId(),needNum));
//		return  new Object[]{1,guid,num};
		return  AppErrorCode.OK;
	}
	
	/**
	 * 宝石穿上和卸下导致属性变化
	 */
	private void noticeStageJewelAttributeChange(Long userRoleId,int type,String goodId){
		JewelConfig jewelConfig = jewelConfigExportService.loadPublicConfig(Integer.parseInt(goodId));
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_JEWEL_ATTR_CHANGE,new Object[]{type, jewelConfig.getAttrMap()});
		try {
			//修炼任务
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE_TS, new Object[] {XiuLianConstants.BAOSHI_LEVEL, getAllJewelLevel(userRoleId)});
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	/**
	 * 更新RoleJewel
	 */
	private RoleJewel updateRoleJewel(Long userRoleId,RoleJewel roleJewel) {
		roleJewel.setData(JSON.toJSONString(roleJewel.getAllData())); // 更新数据
		roleJewelDao.cacheUpdate(roleJewel, userRoleId);
		return roleJewel;
	}
	/**
	 * 获取RoleJewel对象
	 */
	private RoleJewel getRoleJewel(Long userRoleId) {
		RoleJewel roleJewel = roleJewelDao.cacheLoad(userRoleId, userRoleId);
		if (roleJewel == null) {
			roleJewel = new RoleJewel();
			roleJewel.setUserRoleId(userRoleId);
			roleJewel.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			Object[] arr = new Object[GameConstants.JEWEL_GEZI_NUM];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = new Object[] { 1, 0, 0, 0, 0 };
			}
			roleJewel.setData(JSON.toJSONString(arr, false));
			roleJewelDao.cacheInsert(roleJewel, userRoleId);
		}
		return roleJewel;
	}

	/**
	 * 获取玩家宝石总等级
	 * @param userRoleId
	 * @return
	 */
	public Integer getAllJewelLevel(Long userRoleId) {
		RoleJewel roleJewel = this.getRoleJewel(userRoleId);
		int level = 0;
		if(roleJewel.getAllData()==null){
			return level;
		}
		for (int i = 0; i < roleJewel.getAllData().length; i++) {
			JSONArray kongData  = (JSONArray)roleJewel.getAllData()[i]; 
			for (int j = 0; j < kongData.size(); j++) {
				if((Integer)kongData.get(j)>1){
					JewelConfig jewelConfig = jewelConfigExportService.loadPublicConfig((Integer)kongData.get(j));
					level += jewelConfig.getLevel();
				}
			}
		}
		return level;
	}
	/**
	 * 统计宝石给玩家加了多少属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> countJewelAttr(Long userRoleId) {
		RoleJewel roleJewel = this.getRoleJewel(userRoleId);
		if(roleJewel.getAllData()==null){
			return null;
		} 
		Map<String, Long> attr= new HashMap<>();
		for (int i = 0; i < roleJewel.getAllData().length; i++) {
			JSONArray kongData  = (JSONArray)roleJewel.getAllData()[i]; 
			for (int j = 0; j < kongData.size(); j++) {
				if((Integer)kongData.get(j)>1){
					JewelConfig jewelConfig = jewelConfigExportService.loadPublicConfig((Integer)kongData.get(j));
					ObjectUtil.longMapAdd(attr, jewelConfig.getAttrMap());
				}
			}
		}
		if(attr.size()==0){
			return null;
		}
		return attr;
	}

}
