package com.junyou.bus.yaoshen.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.utils.common.CovertObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.yaoshen.configure.export.YaoshenMoYinConfigExportService;
import com.junyou.bus.yaoshen.configure.export.YaoshenMoYinJichuConfig;
import com.junyou.bus.yaoshen.configure.export.YaoshenMoYinQNDConfigExportService;
import com.junyou.bus.yaoshen.constants.YaoshenConstants;
import com.junyou.bus.yaoshen.dao.RoleYaoshenMoyinDao;
import com.junyou.bus.yaoshen.entity.RoleYaoshenMoyin;
import com.junyou.bus.yaoshen.vo.YaoShenMoyinRankVo;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.UpgradeYaoshenMoYinLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.YaoshenMoYinConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class YaoshenMoYinService implements IFightVal{
	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		if(fightPowerType == FightPowerType.EM_YJSX){
			Map<String, Long> data = getYaoshenMoYinAttribute(userRoleId);
			return CovertObjectUtil.getZplus(data);
		}
		return 0;
	}

	@Autowired
	private RoleYaoshenMoyinDao roleYaoshenMoYinDao;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private YaoshenMoYinConfigExportService yaoshenMoYinConfigExportService;
	@Autowired
	private YaoshenMoYinQNDConfigExportService YaoshenMoYinQNDConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;

	
	public Object[] moyingUseCzdNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService
				.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService
				.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_MOYIN_CZD
				&& goodsConfig.getCategory() != GoodsCategory.YS_WANNENG_CZD) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = useCZD(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.YAOSHEN_MOYING_CZ, true, true);
		}
		return ret;
	}
	public Object[] moyingUseQndNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService
				.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService
				.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_MOYIN_QND
				&& goodsConfig.getCategory() != GoodsCategory.YS_WANNENG_QND) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = useQND(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.YAOSHEN_MOYING_QN, true, true);
		}
		return ret;
	}
	/**
	 * 初始化
	 * @param userRoleId
	 * @return
	 */
	public RoleYaoshenMoyin initRoleYaoshenMoYin(Long userRoleId) {
		List<RoleYaoshenMoyin> list = roleYaoshenMoYinDao.initRoleYaoshenMoyin(userRoleId);
		if (list == null || list.size() == 0) {
			return null;
		}
		RoleYaoshenMoyin roleYaoshenMoyin  = list.get(0);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOYIN_JIE_TO_CLIENT, roleYaoshenMoyin.getJieLevel());
		return roleYaoshenMoyin;
	}
	/**
	 * 玩家登陆后统计属性
	 */
	public Map<String, Long> getYaoshenMoYinAttributeAfterLogin(Long userRoleId) {
		// 先初始化一次
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		Map<String, Long> attrMap = null;
		if (roleWrapper != null) {
			attrMap = getYaoshenMoYinAttribute(userRoleId);
		}
		return attrMap;
	}
	/**
	 * client获取当前属性
	 */
	public Map<String, Long> getCurrentAttribute(Long userRoleId) {
		// 先初始化一次
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		Map<String, Long> attrMap = null;
		if (roleWrapper != null) {
			RoleYaoshenMoyin entity = getRoleYaoshenMoYin(userRoleId);
			if (entity == null) {
				YaoshenMoYinJichuConfig config = yaoshenMoYinConfigExportService.load(1, 1);
				if (config != null) {
					//未激活的时候把一阶一层的属性给客户端
					attrMap = config.getAttrs();
				}
			}else{
				attrMap = getYaoshenMoYinAttribute(userRoleId);
			}
		}
		return attrMap;
	}
	
	/**
	 * 妖神魔印信息
	 */
	public Object[] getMoYinInfo(Long userRoleId) {
		RoleYaoshenMoyin entity = getRoleYaoshenMoYin(userRoleId);
		if (entity == null) {
			return null;
		}
		YaoshenMoYinJichuConfig config = yaoshenMoYinConfigExportService.load(entity.getJieLevel(), entity.getCengLevel());
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		return new Object[] { entity.getCzdNum(),entity.getQndNum(),config.getId() };
	}

	
	
	/**
	 * 升级
	 * @param userRoleId
	 * @param itemIds
	 * @return
	 */
	public Object[] upgrade(Long userRoleId, List<Long> itemIds) {
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper == null) {
			return AppErrorCode.ROLE_IS_NOT_ONLINE;
		}
		if(itemIds!=null && itemIds.size()==0){
			return AppErrorCode.YAOSHEN_ITEM_ERROR;
		}
		YaoshenMoYinConfig config = getPublicConfig();

		if (roleWrapper.getLevel() < config.getOpen()) {
			return AppErrorCode.ROLE_LEVEL_ERROR;
		}
		RoleYaoshenMoyin entity = getRoleYaoshenMoYin(userRoleId);
		boolean  isActive  = false; //是否处于激活升级0升级到1级
		if (entity == null) {
			entity = this.createMoYin(userRoleId);
			isActive = true;
		}
		int currentJie = entity.getJieLevel();
		int currentCeng = entity.getCengLevel();
		if (currentCeng == yaoshenMoYinConfigExportService.getMAX_CENG() && currentJie == yaoshenMoYinConfigExportService.getMAX_JIE()) {
			return AppErrorCode.YAOSHEN_TOP_LEVEL;
		}
		YaoshenMoYinJichuConfig jichuConfig = yaoshenMoYinConfigExportService.load(currentJie, currentCeng);
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
			if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_MOYIN_DAN
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
		//验证银两
		Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, money, userRoleId, 
				LogPrintHandle.CONSUME_YAOSHEN_MOYIN_SJ, true,LogPrintHandle.CBZ_YAOSHEN_MOYIN_SJ);
		if (result != null) {
			return result;
		}
		// 扣道具
		Map<String, Integer> goods = new HashMap<String, Integer>();
		for (Long e : itemIds) {
			if (activeateItemNum > 0) {
				RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
				int count = roleItemExport.getCount() > activeateItemNum ? activeateItemNum : roleItemExport.getCount();
				roleBagExportService.removeBagItemByGuid(e, count, userRoleId, GoodsSource.YAOSHEN_MOYIN_SJ, true, true);
				goods.put(roleItemExport.getGoodsId(), count);
				activeateItemNum = activeateItemNum - count;
			}
		}
		int nextCeng = currentCeng >= yaoshenMoYinConfigExportService.getMAX_CENG() ? 1 : currentCeng + 1;
		int nextJie = currentCeng >= yaoshenMoYinConfigExportService.getMAX_CENG() || currentCeng==0 ? currentJie + 1 : currentJie;
		YaoshenMoYinJichuConfig nextJichuConfig = yaoshenMoYinConfigExportService.load(nextJie, nextCeng);
		entity.setJieLevel(nextJie);
		entity.setCengLevel(nextCeng);
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());
		if(isActive){
			roleYaoshenMoYinDao.cacheInsert(entity, userRoleId);
		}else{
			roleYaoshenMoYinDao.cacheUpdate(entity, userRoleId);	
		}
		notifyStageAttrChange(entity);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(goods, null);
		GamePublishEvent.publishEvent(new UpgradeYaoshenMoYinLogEvent(userRoleId, jsonArray, nextJie, nextCeng));
		// 公告通知
		if (nextJichuConfig.getGgopen() == 1) {
			BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { AppErrorCode.YAOSHEN_MOYIN_UP_NOTICE1,
					new Object[] { roleWrapper.getName(),  entity.getJieLevel() , entity.getCengLevel()  } });

//			BusMsgSender.send2One(userRoleId, ClientCmdType.NOTIFY_CLIENT_ALERT3, new Object[] { AppErrorCode.YAOSHEN_MOYIN_UP_NOTICE2,
//					new Object[] {  nextJichuConfig.getName() } });
		}
		return new Object[] { AppErrorCode.SUCCESS, nextJichuConfig.getId() };
	}

	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer addCeng){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper == null) {
			return AppErrorCode.ROLE_IS_NOT_ONLINE;
		}
		RoleYaoshenMoyin info = getRoleYaoshenMoYin(userRoleId);
		if (info == null) {
			return AppErrorCode.YAOSHEN_NOT_OPEN;
		}else{
			int currentJie = info.getJieLevel();
			int currentCeng = info.getCengLevel();
			//最高级
			if (currentCeng == yaoshenMoYinConfigExportService.getMAX_CENG()&& currentJie == yaoshenMoYinConfigExportService.getMAX_JIE()) {
				return AppErrorCode.YAOSHEN_TOP_LEVEL;
			}
			if(currentJie < minLevel){
				return AppErrorCode.YAOSHEN_MOYIN_LEVEL_LIMIT_CAN_NOT_USE;
			}else if(currentJie > maxLevel){
				return AppErrorCode.YAOSHEN_SHENGXING_MAX_CHAOCHU;
			}else{
				boolean isGongGao = false;
				int configId = 0;
				for (int i = 1; i <= addCeng; i++) {
					int nextCeng = currentCeng >= yaoshenMoYinConfigExportService.getMAX_CENG() ? 1 : currentCeng + 1;
					int nextJie = currentCeng >= yaoshenMoYinConfigExportService.getMAX_CENG() ? currentJie + 1 : currentJie;
					YaoshenMoYinJichuConfig nextJichuConfig = yaoshenMoYinConfigExportService.load(nextJie, nextCeng);
					info.setJieLevel(nextJie);
					info.setCengLevel(nextCeng);
					if(nextJichuConfig.getGgopen()==1){
						isGongGao = true;
					}
					configId = nextJichuConfig.getId();
				}
				info.setUpdateTime(GameSystemTime.getSystemMillTime());
				roleYaoshenMoYinDao.cacheUpdate(info, userRoleId);
				notifyStageAttrChange(info);
				//通知前端
				BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOYIN_SJ, new Object[] { AppErrorCode.SUCCESS, configId });
				//公告通知
				if(isGongGao){
					BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { AppErrorCode.YAOSHEN_MOYIN_UP_NOTICE1,
							new Object[] { roleWrapper.getName(),  info.getJieLevel() , info.getCengLevel()  } });

				}
				
			}
			
		}
		return null;
	}
	 
	
	/**
	 * 获得Entity对象
	 * @param userRoleId
	 * @return
	 */
	public RoleYaoshenMoyin getRoleYaoshenMoYin(Long userRoleId) {
		RoleYaoshenMoyin ret = roleYaoshenMoYinDao.cacheLoad(userRoleId, userRoleId);
		
		return ret;
	}

	/**
	 * 妖神魔印属性
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getYaoshenMoYinAttribute(Long userRoleId) {
		RoleYaoshenMoyin info = getRoleYaoshenMoYin(userRoleId);
		if (info == null) {
			return null;
		}
		return getYaoshenMoYinAttribute(info.getJieLevel(), info.getCengLevel(), info.getQndNum(), info.getCzdNum());
	}

	/**
	 * 妖神魔印属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getYaoshenMoYinAttribute(Integer jie, Integer ceng, Integer qndNum, Integer czdNum) {
		Map<String, Long> ret = new HashMap<String, Long>();
		Map<String, Long> jichuAttr = yaoshenMoYinConfigExportService.getAttribute(jie, ceng);
		if (jichuAttr != null && jichuAttr.size() > 0) {
			ObjectUtil.longMapAdd(ret, jichuAttr);
		}
		Long speed = ret.remove(EffectType.x19.name());
		if (czdNum > 0) {
			String czdId = yaoshenMoYinConfigExportService.getCZD_ID();
			float multi = getCzdMulti(czdId) * czdNum / 100f + 1f;
			ObjectUtil.longMapTimes(ret, multi);
		}
		if (qndNum > 0) {
			Map<String, Map<String, Long>> map = YaoshenMoYinQNDConfigExportService.getConfigMap();
			for (int i = 0; i < qndNum; i++) {
				ObjectUtil.longMapAdd(ret, map.get(YaoshenConstants.MOYIN_QND));
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
		RoleYaoshenMoyin entity = getRoleYaoshenMoYin(userRoleId);
		if (entity == null) {
			return AppErrorCode.YAOSHEN_MOYIN_NOT_OPEN;
		}
		int currentJie = entity.getJieLevel();
		int currentCeng = entity.getCengLevel();
		YaoshenMoYinJichuConfig jichuConfig = yaoshenMoYinConfigExportService.load(currentJie, currentCeng);
		if (jichuConfig.getQndopen() != YaoshenConstants.QND_OPEN_FLAG) {
			return AppErrorCode.YAOSHEN_MOYIN_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if (entity.getQndNum().intValue() + count > jichuConfig.getQndmax()) {
			return AppErrorCode.YAOSHEN_MOYIN_QND_MAX_NUM;
		}
		entity.setQndNum(count + entity.getQndNum());
		roleYaoshenMoYinDao.cacheUpdate(entity, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOYIN_QND_NUM, entity.getQndNum() != null ? entity.getQndNum() : 0);
		notifyStageAttrChange(entity);
		return null;
	}

	/**
	 * 使用成长丹
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] useCZD(long userRoleId, int count) {
		RoleYaoshenMoyin entity = getRoleYaoshenMoYin(userRoleId);
		if (entity == null) {
			return AppErrorCode.YAOSHEN_MOYIN_NOT_OPEN;
		}
		int currentJie = entity.getJieLevel();
		int currentCeng = entity.getCengLevel();
		YaoshenMoYinJichuConfig jichuConfig = yaoshenMoYinConfigExportService.load(currentJie, currentCeng);
		if (jichuConfig.getQndopen() != YaoshenConstants.CZD_OPEN_FLAG) {
			return AppErrorCode.YAOSHEN_MOYIN_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if (entity.getCzdNum().intValue() + count > jichuConfig.getCzdmax()) {
			return AppErrorCode.YAOSHEN_MOYIN_CZD_MAX_NUM;
		}
		entity.setCzdNum(count + entity.getCzdNum());
		roleYaoshenMoYinDao.cacheUpdate(entity, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOYIN_CZD_NUM, entity.getCzdNum() != null ? entity.getCzdNum() : 0);
		notifyStageAttrChange(entity);
		return null;
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
	private RoleYaoshenMoyin createMoYin(Long userRoleId) {
		RoleYaoshenMoyin entity = new RoleYaoshenMoyin();
		entity.setUserRoleId(userRoleId);
		entity.setCzdNum(0);
		entity.setQndNum(0);
		entity.setCengLevel(0);
		entity.setJieLevel(0);
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());
		return entity;
	}
	public List<YaoShenMoyinRankVo> getMoyinRankVo(int limit) {
		return roleYaoshenMoYinDao.getMoyinRankVo(limit);
	}

	/**
	 * 魔印属性变化
	 * 
	 * @param entity
	 */
	public void notifyStageAttrChange(RoleYaoshenMoyin entity) {
		int currentJie = entity.getJieLevel();
		int currentCeng = entity.getCengLevel();
		BusMsgSender.send2Stage(entity.getUserRoleId(), InnerCmdType.INNER_YAOSHEN_MOYIN_ATTR_CHANGE,
				new Object[] { currentJie, currentCeng, entity.getQndNum(), entity.getCzdNum() });
	}

	/**
	 * 公共配置
	 * 
	 * @return
	 */
	public YaoshenMoYinConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YAOSHEN_MOYIN);
	}

}
