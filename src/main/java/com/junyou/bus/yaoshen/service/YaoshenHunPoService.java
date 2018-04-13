package com.junyou.bus.yaoshen.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.bus.yaoshen.entity.RoleYaoshen;
import com.junyou.bus.yaoshen.entity.RoleYaoshenMowen;
import com.junyou.gameconfig.constants.AttributeType;
import com.junyou.utils.common.CovertObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.yaoshen.configure.export.YaoshenHunPoConfigExportService;
import com.junyou.bus.yaoshen.configure.export.YaoshenHunPoPoShenConfigExportService;
import com.junyou.bus.yaoshen.configure.export.YaoshenHunPoPoshenConfig;
import com.junyou.bus.yaoshen.configure.export.YaoshenHunPoQNDConfigExportService;
import com.junyou.bus.yaoshen.configure.export.YaoshenJichuConfig;
import com.junyou.bus.yaoshen.constants.YaoshenConstants;
import com.junyou.bus.yaoshen.dao.RoleYaoshenHunpoDao;
import com.junyou.bus.yaoshen.entity.RoleYaoshenHunpo;
import com.junyou.bus.yaoshen.vo.YaoShenHunpoRankVo;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.UpgradeYaoshenHunpoLogEvent;
import com.junyou.event.YaoshenPoshenLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.YaoshenHunpoPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class YaoshenHunPoService  implements IFightVal {

	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		switch(fightPowerType){
			case FightPowerType.EM_HLSX :
				Map<String, Long>  datas = getYaoshenHunpoAttribute(userRoleId);
				return CovertObjectUtil.getZplus(datas);
			case FightPowerType.EM_LHSX :
				long result = 0;
				RoleYaoshenHunpo entity = getRoleYaoshenHunpo(userRoleId);
				if(entity == null){
					return 0;
				}

				for(int i=  entity.getJieLevel();i>0;i--){
					Map<String, Long> map = this.getHunpoTaiGuangAttributeById(userRoleId, i);
					if(map != null){
						result += CovertObjectUtil.getZplus(map);
					}
				}
				return result;
			default:
				ChuanQiLog.error("fightPowerType is not exist!");
		}
		return 0;
	}

	@Autowired
	private RoleYaoshenHunpoDao roleYaoshenHunpoDao;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private YaoshenHunPoConfigExportService yaoshenHunPoConfigExportService;
	@Autowired
	private YaoshenHunPoPoShenConfigExportService yaoshenHunPoPoShenConfigExportService;
	@Autowired
	private YaoshenHunPoQNDConfigExportService yaoshenHunPoQNDConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	private static final int MAX_INDEX = 4;
	private static final int MIN_INDEX = 0;
	
	public Object[] hunpoUseCzdNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService
				.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService
				.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_HUNPO_CZD
				&& goodsConfig.getCategory() != GoodsCategory.YS_WANNENG_CZD) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = useCZD(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.YAOSHEN_HUNPO_CZ, true, true);
		}
		return ret;
	}
	public Object[] hunpoUseQndNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService
				.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService
				.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_HUNPO_QND
				&& goodsConfig.getCategory() != GoodsCategory.YS_WANNENG_QND) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = useQND(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.YAOSHEN_HUNPO_QN, true, true);
		}
		return ret;
	}

	/**
	 * 初始化
	 * 
	 * @param userRoleId
	 * @return
	 */
	public RoleYaoshenHunpo initRoleYaoshenHunpo(Long userRoleId) {
		List<RoleYaoshenHunpo> list = roleYaoshenHunpoDao.initRoleYaoshenHunpo(userRoleId);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	
	/**
	 * 玩家登陆后统计属性--魂魄
	 */
	public Map<String, Long> getYaoshenHunpoAttributeAfterLogin(Long userRoleId) {
		//先初始化一次
		 RoleWrapper  roleWrapper = roleExportService.getLoginRole(userRoleId);
		 if(roleWrapper!=null){
			 checkLevelAndActiveHunpo(userRoleId,roleWrapper.getLevel());
		 }
		 Map<String, Long> attrMap  =  getYaoshenHunpoAttribute(userRoleId);
		 return attrMap;
	}

	/**
	 * 玩家登陆后统计属性--魄神
	 */
	public Map<String, Long> getYaoshenHunpoPoshenAttributeAfterLogin(Long userRoleId) {
		
		return getHunpoAllPoshenAttribute(userRoleId);
	}
	/**
	 * 等级到了激活
	 */
	public void checkLevelAndActiveHunpo(Long userRoleId, int level) {
		try {
			YaoshenHunpoPublicConfig yaoshenPublicConfig = getPublicConfig();
			if(yaoshenPublicConfig==null){
				ChuanQiLog.error("***公共配置表妖神魂魄信息不存在***");
				return ;
			}
			RoleYaoshenHunpo entity = getRoleYaoshenHunpo(userRoleId);
			if (level >= yaoshenPublicConfig.getOpen() && entity == null) {
				entity = this.createHunpo(userRoleId);
				roleYaoshenHunpoDao.cacheInsert(entity, userRoleId);
			}
		} catch (Exception e) {
			ChuanQiLog.error("激活妖神魂魄失败：{}", e);
		}
	}

	/**
	 * 妖神魂魄信息
	 */
	public Object[] getHunpoInfo(Long userRoleId) {
		RoleYaoshenHunpo entity = getRoleYaoshenHunpo(userRoleId);
		if (entity == null) {
			return AppErrorCode.YAOSHEN_HUNPO_NOT_OPEN;
		}
		YaoshenJichuConfig yaoshenJichuConfig = yaoshenHunPoConfigExportService.load(entity.getJieLevel(), entity.getCengLevel());
		if (yaoshenJichuConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<String, Long> attrMap = getYaoshenHunpoAttribute(entity.getJieLevel(), entity.getCengLevel(), entity.getQndNum(), entity.getCzdNum());

		return new Object[] { yaoshenJichuConfig.getId(), entity.getQndNum(), entity.getCzdNum(), attrMap };
	}

	/**
	 * 升级
	 * 
	 * @param userRoleId
	 * @param itemIds
	 * @return
	 */
	public Object[] upgrade(Long userRoleId, List<Long> itemIds) {
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper == null) {
			return AppErrorCode.ROLE_IS_NOT_ONLINE;
		}
		YaoshenHunpoPublicConfig yaoshenPublicConfig = getPublicConfig();

		if (roleWrapper.getLevel() < yaoshenPublicConfig.getOpen()) {
			return AppErrorCode.ROLE_LEVEL_ERROR;
		}
		RoleYaoshenHunpo entity = getRoleYaoshenHunpo(userRoleId);
		if (entity == null) {
			return AppErrorCode.YAOSHEN_HUNPO_NOT_OPEN;
		}
		int currentJie = entity.getJieLevel();
		int currentCeng = entity.getCengLevel();
		if (currentCeng == yaoshenHunPoConfigExportService.getMAX_CENG() && currentJie == yaoshenHunPoConfigExportService.getMAX_JIE()) {
			return AppErrorCode.YAOSHEN_TOP_LEVEL;
		}
		YaoshenJichuConfig jichuConfig = yaoshenHunPoConfigExportService.load(currentJie, currentCeng);
		if (jichuConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		int hasItemNum = 0;
		for (Long e : itemIds) {
			RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
			if (roleItemExport == null) {
				return AppErrorCode.YAOSHEN_ITEM_NOT_ENOUGH;
			}
			String goodsId = roleItemExport.getGoodsId();
			GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
			if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_HUNPO_ITEM 
					&& goodsConfig.getCategory() != GoodsCategory.YAOSHEN_WANNENG_SJ_ITEM) {
				return AppErrorCode.YAOSHEN_ITEM_ERROR;
			}
			hasItemNum = hasItemNum + roleItemExport.getCount();
		}
		int activeateItemNum = jichuConfig.getCount();
		if (hasItemNum < jichuConfig.getCount()) {
			return AppErrorCode.YAOSHEN_ITEM_NOT_ENOUGH;
		}
		int money = jichuConfig.getMoney();
		// 验证银两
		Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.CONSUME_YAOSHEN_HUNPO_SJ, true,
				LogPrintHandle.CBZ_YAOSHEN_HUNPO_SJ);
		if (result != null) {
			return result;
		}
		// 扣道具
		Map<String, Integer> goods = new HashMap<String, Integer>();
		for (Long e : itemIds) {
			if (activeateItemNum > 0) {
				RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
				int count = roleItemExport.getCount() > activeateItemNum ? activeateItemNum : roleItemExport.getCount();
				roleBagExportService.removeBagItemByGuid(e, count, userRoleId, GoodsSource.YAOSHEN_HUNPO_UPDATE, true, true);
				goods.put(roleItemExport.getGoodsId(), count);
				activeateItemNum = activeateItemNum - count;
			}
		}
		int nextCeng = currentCeng >= yaoshenHunPoConfigExportService.getMAX_CENG() ? 1 : currentCeng + 1;
		int nextJie = currentCeng >= yaoshenHunPoConfigExportService.getMAX_CENG() ? currentJie + 1 : currentJie;
		YaoshenJichuConfig nextJichuConfig = yaoshenHunPoConfigExportService.load(nextJie, nextCeng);
		entity.setJieLevel(nextJie);
		entity.setCengLevel(nextCeng);
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleYaoshenHunpoDao.cacheUpdate(entity, userRoleId);
		notifyStageYaoshenHunpoAttrChange(entity);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(goods, null);
		GamePublishEvent.publishEvent(new UpgradeYaoshenHunpoLogEvent(userRoleId, jsonArray, nextJie, nextCeng));
		// 公告通知
		if (nextJichuConfig.getGgopen() == 1) {
			BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { AppErrorCode.YAOSHEN_HUNPO_UP_NOTICE1,
					new Object[] { roleWrapper.getName(), entity.getJieLevel() , entity.getCengLevel() } });

//			BusMsgSender.send2One(userRoleId, ClientCmdType.NOTIFY_CLIENT_ALERT3, new Object[] { AppErrorCode.YAOSHEN_HUNPO_UP_NOTICE2,
//					new Object[] { nextJichuConfig.getName() } });
		}
		return new Object[] { AppErrorCode.SUCCESS, nextJichuConfig.getId() };
	}

	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer addCeng){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper == null) {
			return AppErrorCode.ROLE_IS_NOT_ONLINE;
		}
		RoleYaoshenHunpo info = getRoleYaoshenHunpo(userRoleId);
		if (info == null) {
			return AppErrorCode.YAOSHEN_NOT_OPEN;
		}else{
			int currentJie = info.getJieLevel();
			int currentCeng = info.getCengLevel();
			//最高级
			if (currentCeng == yaoshenHunPoConfigExportService.getMAX_CENG()&& currentJie == yaoshenHunPoConfigExportService.getMAX_JIE()) {
				return AppErrorCode.YAOSHEN_TOP_LEVEL;
			}
			if(currentJie < minLevel){
				return AppErrorCode.YAOSHEN_HUNPO_LEVEL_LIMIT_CAN_NOT_USE;
			}else if(currentJie > maxLevel){
				return AppErrorCode.YAOSHEN_SHENGXING_MAX_CHAOCHU;
			}else{
				boolean isGongGao = false;
				int configId = 0;
				for (int i = 1; i <= addCeng; i++) {
					int nextCeng = currentCeng >= yaoshenHunPoConfigExportService.getMAX_CENG() ? 1 : currentCeng + 1;
					int nextJie = currentCeng >= yaoshenHunPoConfigExportService.getMAX_CENG() ? currentJie + 1 : currentJie;
					YaoshenJichuConfig nextJichuConfig = yaoshenHunPoConfigExportService.load(nextJie, nextCeng);
					info.setJieLevel(nextJie);
					info.setCengLevel(nextCeng);
					if(nextJichuConfig.getGgopen()==1){
						isGongGao = true;
					}
					configId = nextJichuConfig.getId();
				}
				info.setUpdateTime(GameSystemTime.getSystemMillTime());
				roleYaoshenHunpoDao.cacheUpdate(info, userRoleId);
				notifyStageYaoshenHunpoAttrChange(info);
				//通知前端
				BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_SJ, new Object[] { AppErrorCode.SUCCESS, configId });
				//公告通知
				if(isGongGao){
					BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { AppErrorCode.YAOSHEN_HUNPO_UP_NOTICE1,
						new Object[] { roleWrapper.getName(), info.getJieLevel() , info.getCengLevel() } });}
				}
			
		}
		return null;
	}
	
	/**
	 * 魄神兑换
	 */
	public Object[]  poshenDuiHuan(Long userRoleId,long itemGuid){
		 
		RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, itemGuid);
		if (roleItemExport == null) {
			return AppErrorCode.YAOSHEN_ITEM_NOT_ENOUGH;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_HUNPO_ITEM) {
			return AppErrorCode.YAOSHEN_ITEM_ERROR;
		}
		int count = roleBagExportService.getBagItemCountByGoodsType(goodsConfig.getId1(), userRoleId);
		YaoshenHunpoPublicConfig yaoshenHunpoPublicConfig = this.getPublicConfig();
		if(count<yaoshenHunpoPublicConfig.getJingQi()){
			return AppErrorCode.ITEM_COUNT_ENOUGH;
		}
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(yaoshenHunpoPublicConfig.getItem(), userRoleId);
		// 背包空间不足 请先清理背包
		if (code != null) {
			return code;
		}
		//减道具
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoodsType(goodsConfig.getId1(), yaoshenHunpoPublicConfig.getJingQi(), userRoleId, GoodsSource.YAOSHEN_HUNPO_ON, true, true);
		if (!bagSlots.isSuccee()) {
			return bagSlots.getErrorCode();
		}
		// 背包新增道具
	    roleBagExportService.putInBag(yaoshenHunpoPublicConfig.getItem(), userRoleId, GoodsSource.GOODS_POSHEN_DUIHUAN, true);
		return new Object[]{1};
	}
	
	/**
	 * 获得魔纹Entity对象
	 * 
	 * @param userRoleId
	 * @return
	 */
	public RoleYaoshenHunpo getRoleYaoshenHunpo(Long userRoleId) {
		// 玩家登陆后要先放进缓存
		RoleYaoshenHunpo ret = roleYaoshenHunpoDao.cacheLoad(userRoleId, userRoleId);
		return ret;
	}

	/**
	 * 获取某个魂魄的镶嵌的所有魄神的属性和
	 */
	public Map<String, Long> getHunpoTaiGuangAttributeById(Long userRoleId, int id) {
		RoleYaoshenHunpo entity = getRoleYaoshenHunpo(userRoleId);
		if (entity.getTaiGuang() != null && !entity.equals("")) {
			Map<Integer, Map<Integer,String>> map = entity.getTaiGuangMap();
			if (map != null) {
				Map<String, Long> ret = new HashMap<String, Long>();
				Map<String, YaoshenHunPoPoshenConfig> config = yaoshenHunPoPoShenConfigExportService.getConfigMap();
				GoodsConfig goodsConfig = null;
				Map<String, Long> attrMap =null;
				for (Entry<Integer, Map<Integer,String>> entry : map.entrySet()) {
					int _id = entry.getKey();// 魂魄id
					if (id == _id) {
						Map<Integer,String> data = entry.getValue();
						// 遍历所有的镶嵌的宝石
						for (Entry<Integer,String> dataEntry : data.entrySet()) {
							 goodsConfig = goodsConfigExportService.loadById(dataEntry.getValue());
							 if(goodsConfig==null){
								 continue;
							 }
							 attrMap = config.get(goodsConfig.getId1()).getAttMap();
							 ObjectUtil.longMapAdd(ret, attrMap);
						}
						return ret;
					}
				}
			}
		}
		return null;
	}
	
	public List<YaoShenHunpoRankVo> getYaoShenHunpoRankVo(int limit) {
		return roleYaoshenHunpoDao.getYaoShenHunpoRankVo(limit);
	}
	/**
	 * 所有魄神属性总和
	 * @return
	 */
	public Map<String, Long> getHunpoAllPoshenAttribute(Long userRoleId) {
		RoleYaoshenHunpo entity = getRoleYaoshenHunpo(userRoleId);
		if (entity!=null && entity.getTaiGuang() != null && !entity.getTaiGuang().equals("")) {
			Map<Integer, Map<Integer,String>> map = entity.getTaiGuangMap();
			if (map != null) {
				Map<String, Long> ret = new HashMap<String, Long>();
				Map<String, YaoshenHunPoPoshenConfig> configMap = yaoshenHunPoPoShenConfigExportService.getConfigMap();
				GoodsConfig goodsConfig = null;
				Map<String, Long> attrMap =null;
				for (Entry<Integer, Map<Integer,String>> entry : map.entrySet()) {
					Map<Integer,String> data = entry.getValue();
					// 遍历所有的镶嵌的宝石
					for (Entry<Integer,String> dataEntry : data.entrySet()) {
						 goodsConfig = goodsConfigExportService.loadById(dataEntry.getValue());
						 if(goodsConfig==null){
							 continue;
						 }
						 attrMap = configMap.get(goodsConfig.getId1()).getAttMap();
						 ObjectUtil.longMapAdd(ret, attrMap);
					}
				}
				return ret;
			}
		}
		return null;
	}
	/**
	 * 妖神魂魄属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getYaoshenHunpoAttribute(Long userRoleId) {
		RoleYaoshenHunpo info = getRoleYaoshenHunpo(userRoleId);
		if (info == null) {
			return null;
		}
		return getYaoshenHunpoAttribute(info.getJieLevel(), info.getCengLevel(),
				info.getQndNum(), info.getCzdNum());
	}
	/**
	 * 妖神魂魄属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getYaoshenHunpoAttribute(Integer jie, Integer ceng, Integer qndNum, Integer czdNum) {
		Map<String, Long> ret = new HashMap<String, Long>();
		Map<String, Long> jichuAttr = yaoshenHunPoConfigExportService.getYaoshenHunpoAttribute(jie, ceng);
		if (jichuAttr != null && jichuAttr.size() > 0) {
			ObjectUtil.longMapAdd(ret, jichuAttr);
		}
		Long speed = ret.remove(EffectType.x19.name());
		if (czdNum > 0) {
			String czdId = yaoshenHunPoConfigExportService.getCZD_ID();
			float multi = getCzdMulti(czdId) * czdNum / 100f + 1f;
			ObjectUtil.longMapTimes(ret, multi);
		}
		if (qndNum > 0) {
			Map<String, Map<String, Long>> map = yaoshenHunPoQNDConfigExportService.getConfigMap();
			for (int i = 0; i < qndNum; i++) {
				ObjectUtil.longMapAdd(ret, map.get(YaoshenConstants.HUNPO_QND));
			}
		}
		if (speed != null) {
			ret.put(EffectType.x19.name(), speed);
		}
		return ret;
	}

	/**
	 * 使用潜能丹
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] useQND(long userRoleId, int count) {
		RoleYaoshenHunpo entity = getRoleYaoshenHunpo(userRoleId);
		if (entity == null) {
			return AppErrorCode.YAOSHEN_HUNPO_NOT_OPEN;
		}
		int currentJie = entity.getJieLevel();
		int currentCeng = entity.getCengLevel();
		YaoshenJichuConfig jichuConfig = yaoshenHunPoConfigExportService.load(currentJie, currentCeng);
		if (jichuConfig.getQndopen() != YaoshenConstants.QND_OPEN_FLAG) {
			return AppErrorCode.YAOSHEN_HUNPO_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if (entity.getQndNum().intValue() + count > jichuConfig.getQndmax()) {
			return AppErrorCode.YAOSHEN_HUNPO_QND_MAX_NUM;
		}
		entity.setQndNum(count + entity.getQndNum());
		roleYaoshenHunpoDao.cacheUpdate(entity, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_QND_NUM, entity.getQndNum() != null ? entity.getQndNum() : 0);
		notifyStageYaoshenHunpoAttrChange(entity);
		return null;
	}

	/**
	 * 使用成长丹
	 * 
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] useCZD(long userRoleId, int count) {
		RoleYaoshenHunpo entity = getRoleYaoshenHunpo(userRoleId);
		if (entity == null) {
			return AppErrorCode.YAOSHEN_HUNPO_NOT_OPEN;
		}
		int currentJie = entity.getJieLevel();
		int currentCeng = entity.getCengLevel();
		YaoshenJichuConfig jichuConfig = yaoshenHunPoConfigExportService.load(currentJie, currentCeng);
		if (jichuConfig.getQndopen() != YaoshenConstants.CZD_OPEN_FLAG) {
			return AppErrorCode.YAOSHEN_HUNPO_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if (entity.getCzdNum().intValue() + count > jichuConfig.getCzdmax()) {
			return AppErrorCode.YAOSHEN_HUNPO_CZD_MAX_NUM;
		}
		entity.setCzdNum(count + entity.getCzdNum());
		roleYaoshenHunpoDao.cacheUpdate(entity, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_CZD_NUM, entity.getCzdNum() != null ? entity.getCzdNum() : 0);
		notifyStageYaoshenHunpoAttrChange(entity);
		return null;
	}

	/**
	 * 某魂魄信息
	 */
	public Object[] getHunpoInfoById(long userRoleId, int id1){
		RoleYaoshenHunpo entity = getRoleYaoshenHunpo(userRoleId);
		if (entity == null) {
			return AppErrorCode.YAOSHEN_HUNPO_NOT_OPEN;
		}
		if(entity.getJieLevel()<id1){
			return AppErrorCode.YAOSHEN_HUNPO_NOT_ACTIVE;
		}
		Map<String, Long> map = this.getHunpoTaiGuangAttributeById(userRoleId, id1);
		return new Object[]{id1,entity.getTaiGuangMap().get(id1),map};
	}
	/**
	 * 请求镶嵌魄神
	 */
	public Object[] poshenOn(long userRoleId, int hunpoId1,int index,long poshenGuid){
		if(index>MAX_INDEX || index<MIN_INDEX){
			return AppErrorCode.YAOSHEN_HUNPO_POSHEN_ON_INDEX_ERROR;
		}
		RoleYaoshenHunpo entity = getRoleYaoshenHunpo(userRoleId);
		if (entity == null) {
			return AppErrorCode.YAOSHEN_HUNPO_NOT_OPEN;
		}
		if(hunpoId1>entity.getJieLevel()){
			return AppErrorCode.YAOSHEN_HUNPO_NOT_ACTIVE;
		}
		Map<String, YaoshenHunPoPoshenConfig> config = yaoshenHunPoPoShenConfigExportService.getConfigMap();
		if(config==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, poshenGuid);
		if (roleItemExport == null) {
			return AppErrorCode.YAOSHEN_ITEM_NOT_ENOUGH;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_HUNPO_POSHEN_ITEM) {
			return AppErrorCode.YAOSHEN_ITEM_ERROR;
		}
		int count = roleBagExportService.getBagItemCountByGoodsType(goodsConfig.getId1(), userRoleId);
		if(count<1){
			return AppErrorCode.ITEM_COUNT_ENOUGH;
		}
		String poshenId = goodsConfig.getId();
		String oldPoshenId  =null;
		if(entity.getTaiGuangMap().get(hunpoId1)!=null){
			oldPoshenId =entity.getTaiGuangMap().get(hunpoId1).get(index); 
		}
		if(poshenId.equals(oldPoshenId)){
			//重复镶嵌魄神
			return  AppErrorCode.YAOSHEN_HUNPO_ON_SAME;
		}
		
		// 背包减掉一个道具
		BagSlots bagSlots = roleBagExportService.removeBagItemByGoodsType(goodsConfig.getId1(), 1, userRoleId, GoodsSource.YAOSHEN_HUNPO_ON, true, true);
		if (!bagSlots.isSuccee()) {
			return bagSlots.getErrorCode();
		}
		
		if(oldPoshenId!=null){
			Map<String, Integer> items = new HashMap<>();
			items.put(oldPoshenId, 1);
			//老的进背包
			Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(items, userRoleId);
			// 背包空间不足 请先清理背包
			if (code != null) {
				return code;
			}
			// 背包新增一个道具
			roleBagExportService.putInBag(items, userRoleId, GoodsSource.YAOSHEN_HUNPO_ON_OLD, true);
		}
		 
		if(entity.getTaiGuangMap().get(hunpoId1)==null){
			Map<Integer, String> poshenInfo  = new HashMap<>();
			poshenInfo.put(index, poshenId);
			entity.getTaiGuangMap().put(hunpoId1,  poshenInfo);
		}else{
			entity.getTaiGuangMap().get(hunpoId1).put(index, poshenId);
		}
		entity.updateTaiguang(); //更新字段
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());
		notifyStageYaoPoshenAttrChange(userRoleId);
		this.roleYaoshenHunpoDao.cacheUpdate(entity, userRoleId);
		Map<String, Long> attrMap  = this.getHunpoTaiGuangAttributeById(userRoleId, hunpoId1);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_TAIGUANG_ATTR_CHANGE,new Object[]{hunpoId1, attrMap});
		//记录日志  
		GamePublishEvent.publishEvent(new YaoshenPoshenLogEvent(userRoleId,1,oldPoshenId,poshenId));
		return  new Object[]{1,hunpoId1,index,poshenId};
	}
	/**
	 * 请求卸下魄神
	 */
	public Object[] poshenOff(long userRoleId, int hunpoId,int index){
		RoleYaoshenHunpo entity = getRoleYaoshenHunpo(userRoleId);
		if (entity == null) {
			return AppErrorCode.YAOSHEN_HUNPO_NOT_OPEN;
		}
		if(hunpoId>entity.getJieLevel()){
			return AppErrorCode.YAOSHEN_HUNPO_NOT_ACTIVE;
		}
		
		Map<Integer,String> poshenInfo = entity.getTaiGuangMap().get(hunpoId);
		if(poshenInfo==null || poshenInfo.get(index)==null){
			return AppErrorCode.YAOSHEN_HUNPO_POSHEN_POSITION_NO;
		}
		
		String poshenId  = poshenInfo.get(index);
		Map<String, YaoshenHunPoPoshenConfig> config = yaoshenHunPoPoShenConfigExportService.getConfigMap();
		if(config==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Map<String, Integer> items = new HashMap<>();
		items.put(poshenId, 1);
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(items, userRoleId);
		// 背包空间不足 请先清理背包
		if (code != null) {
			return code;
		}
		poshenInfo.remove(index);
		entity.updateTaiguang(); //更新字段
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());
		this.roleYaoshenHunpoDao.cacheUpdate(entity, userRoleId);
		notifyStageYaoPoshenAttrChange(userRoleId);
		Map<String, Long> attrMap  = this.getHunpoTaiGuangAttributeById(userRoleId, hunpoId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_HUNPO_TAIGUANG_ATTR_CHANGE,new Object[]{hunpoId, attrMap}  );
		roleBagExportService.putInBag(items, userRoleId, GoodsSource.YAOSHEN_HUNPO_OFF, true);
		//记录日志  
		GamePublishEvent.publishEvent(new YaoshenPoshenLogEvent(userRoleId,2,null,poshenId));
		return new Object[]{1,hunpoId,index};
	}
	private int multi = 0;

	public int getCzdMulti(String czdId) {
		if (multi == 0) {
			List<String> goodsIds = goodsConfigExportService.loadIdsById1(czdId);
			if (goodsIds.size() > 0) {
				GoodsConfig config = goodsConfigExportService.loadById(goodsIds.get(0));
				multi = config.getData1();
			}
		}
		return multi;
	}

	/**
	 * 创建entity
	 */
	private RoleYaoshenHunpo createHunpo(Long userRoleId) {
		RoleYaoshenHunpo entity = new RoleYaoshenHunpo();
		entity.setUserRoleId(userRoleId);
		entity.setCzdNum(0);
		entity.setQndNum(0);
		entity.setCengLevel(1);
		entity.setJieLevel(1);
		entity.setTaiGuang("{}");
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());
		notifyStageYaoshenHunpoAttrChange(entity);// 通知加属性
		return entity;
	}

	/**
	 *魂魄属性变化
	 * 
	 * @param entity
	 */
	public void notifyStageYaoshenHunpoAttrChange(RoleYaoshenHunpo entity) {
		int currentJie = entity.getJieLevel();
		int currentCeng = entity.getCengLevel();
		BusMsgSender.send2Stage(entity.getUserRoleId(), InnerCmdType.INNER_YAOSHEN_HUNPO_CHANGE, new Object[] { currentJie, currentCeng, entity.getQndNum(), entity.getCzdNum() });
	}
	/**
	 * 魄神变化
	 * 
	 * @param entity
	 */
	public void notifyStageYaoPoshenAttrChange(Long userRoleId) {
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_YAOSHEN_HUNPO_POSHEN_CHANGE, null);
	}

	/**
	 * 公共配置
	 * 
	 * @return
	 */
	public YaoshenHunpoPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YAOSHEN_HUNPO);
	}

}
