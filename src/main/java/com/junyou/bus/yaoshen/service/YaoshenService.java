package com.junyou.bus.yaoshen.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.utils.common.CovertObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.entity.RoleAccountWrapper;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.skill.export.RoleSkillExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.yaoshen.configure.export.YaoshenJichuConfig;
import com.junyou.bus.yaoshen.configure.export.YaoshenJichuConfigExportService;
import com.junyou.bus.yaoshen.configure.export.YaoshenMoWenConfigExportService;
import com.junyou.bus.yaoshen.configure.export.YaoshenQianNengBiaoConfig;
import com.junyou.bus.yaoshen.configure.export.YaoshenQianNengBiaoConfigExportService;
import com.junyou.bus.yaoshen.constants.YaoshenConstants;
import com.junyou.bus.yaoshen.dao.RoleYaoshenDao;
import com.junyou.bus.yaoshen.dao.RoleYaoshenMowenDao;
import com.junyou.bus.yaoshen.entity.RoleYaoshen;
import com.junyou.bus.yaoshen.entity.RoleYaoshenMowen;
import com.junyou.bus.yaoshen.vo.YaoShenMowenRankVo;
import com.junyou.bus.yaoshen.vo.YaoShenRankVo;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.ActivateYaoshenLogEvent;
import com.junyou.event.UpgradeYaoshenLogEvent;
import com.junyou.event.UpgradeYaoshenMowenLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.YaoshenPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.spring.container.DataContainer;

@Service
public class YaoshenService implements IFightVal {

	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		switch(fightPowerType){
			case FightPowerType.EM_XMSX :
				RoleYaoshen roleYaoshen = getRoleYaoshen(userRoleId);
				if(roleYaoshen == null){
					return 0;
				}
				Map<String, Long> attrs1 = getYaoshenAttribute(roleYaoshen.getJieLevel(), roleYaoshen.getCengLevel(),roleYaoshen.getQndNum(), roleYaoshen.getCzdNum());
				if(attrs1 == null){
					return 0;
				}
				return CovertObjectUtil.getZplus(attrs1);
			case FightPowerType.EM_WZSX :
				RoleYaoshenMowen roleYaoshenMowen = getRoleYaoshenMowen(userRoleId);
				Map<String, Long> attrs2 =getYaoshenMowenAttribute(userRoleId);
				if(attrs2 == null){
					return 0;
				}
				return  CovertObjectUtil.getZplus(attrs2);
				default:
					ChuanQiLog.error("fightPowerType is not exist!");
		}
		return 0;
	}

	@Autowired
	private YaoshenJichuConfigExportService yaoshenJichuConfigExportService;
	@Autowired
	private YaoshenMoWenConfigExportService yaoshenMoWenConfigExportService;
	@Autowired
	private RoleYaoshenDao roleYaoshenDao;
	@Autowired
	private RoleYaoshenMowenDao roleYaoshenMowenDao;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private PublicRoleStateService publicRoleStateService;
	
	
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;

	@Autowired
	private GoodsConfigExportService goodsConfigExportService;

	@Autowired
	private YaoshenQianNengBiaoConfigExportService yaoshenQianNengBiaoConfigExportService;
	
	@Autowired
	private DataContainer dataContainer;

	@Autowired
	private RoleSkillExportService roleSkillExportService;
	
	
	
	public Object[] batiUseCzdNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService
				.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService
				.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_CZD
				&& goodsConfig.getCategory() != GoodsCategory.YS_WANNENG_CZD) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = useCZD(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.YAOSHEN_BATI_CZ, true, true);
		}
		return ret;
	}
	public Object[] batiUseQndNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService
				.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService
				.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_QND
				&& goodsConfig.getCategory() != GoodsCategory.YS_WANNENG_QND) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = useQND(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.YAOSHEN_BATI_QN, true, true);
		}
		return ret;
	}
	
	public Object[] mowenUseCzdNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService
				.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService
				.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_MOWEN_CZD
				&& goodsConfig.getCategory() != GoodsCategory.YS_WANNENG_CZD) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = useMowenCZD(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.YAOSHEN_MOWEN_CZ, true, true);
		}
		return ret;
	}
	public Object[] mowenUseQndNew(Long userRoleId,Long guid){
		RoleItemExport roleItemExport = roleBagExportService
				.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.NO_YB_GOODS;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService
				.loadById(goodsId);
		if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_MOWEN_QND
				&& goodsConfig.getCategory() != GoodsCategory.YS_WANNENG_QND) {
			return AppErrorCode.NO_YB_GOODS;
		}
		Object[] ret = useMowenQND(userRoleId, 1);
		if(ret == null){
			ret = AppErrorCode.OK;
			roleBagExportService.removeBagItemByGuid(guid,1, userRoleId, GoodsSource.YAOSHEN_MOWEN_QN, true, true);
		}
		return ret;
	}

	public YaoshenPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_YAOSHEN);
	}

	public RoleYaoshen initRoleYaoshen(Long userRoleId) {
		List<RoleYaoshen> list = roleYaoshenDao.initRoleYaoshen(userRoleId);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}
	public RoleYaoshenMowen initRoleYaoshenMowen(Long userRoleId) {
		List<RoleYaoshenMowen> list = roleYaoshenMowenDao.initRoleYaoshenMowen(userRoleId);
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	public RoleYaoshen getRoleYaoshen(Long userRoleId) {
		RoleYaoshen ret = roleYaoshenDao.cacheLoad(userRoleId, userRoleId);
		return ret;
	}

	public void createRoleYaoshen(Long userRoleId) {
		RoleYaoshen roleYaoshen = new RoleYaoshen();
		roleYaoshen.setUserRoleId(userRoleId);
		roleYaoshen.setIsYaoshenShow(YaoshenConstants.YAOSHEN_SHOW_NO);
		roleYaoshen.setJieLevel(1);
		roleYaoshen.setCengLevel(1);
		roleYaoshen.setQndNum(0);
		roleYaoshen.setCzdNum(0);
		roleYaoshen.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleYaoshenDao.cacheInsert(roleYaoshen, userRoleId);
	}

	public Object[] getYaoshenInfo(Long userRoleId) {
		RoleYaoshen roleYaoshen = getRoleYaoshen(userRoleId);
		if (roleYaoshen == null) {
			return new Object[] { 0, 0 };
		} else {
			return new Object[] { roleYaoshen.getCzdNum(),
					roleYaoshen.getQndNum() };

		}
	}

	public Object[] activeYaoshen(Long userRoleId, List<Long> itemIds,
			boolean useGold) {
		RoleYaoshen roleYaoshen = getRoleYaoshen(userRoleId);
		if (roleYaoshen != null) {
			return AppErrorCode.YAOSHEN_IS_ACTIVATED;
		}
		UserRole userRole = roleExportService.getUserRole(userRoleId);
		YaoshenPublicConfig publicConfig = getPublicConfig();
		if (userRole.getLevel() < publicConfig.getOpen()) {
			return AppErrorCode.YAOSHEN_NOT_OPEN;
		}
		int hasItemNum = 0;
		JSONArray jsonArray = null;
		for (Long e : itemIds) {
			RoleItemExport roleItemExport = roleBagExportService
					.getBagItemByGuid(userRoleId, e);
			if (roleItemExport == null) {
				return AppErrorCode.YAOSHEN_ITEM_NOT_ENOUGH;
			}
			String goodsId = roleItemExport.getGoodsId();
			GoodsConfig goodsConfig = goodsConfigExportService
					.loadById(goodsId);
			if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_SJ_ITEM
					&& goodsConfig.getCategory() != GoodsCategory.YAOSHEN_WANNENG_SJ_ITEM) {
				return AppErrorCode.YAOSHEN_ITEM_ERROR;
			}
			hasItemNum = hasItemNum + roleItemExport.getCount();
		}
		Integer[] consume = new Integer[2];
		consume[0] =0;
		consume[1] =0;
		int activeateItemNum = yaoshenJichuConfigExportService
				.getACTIVATE_ITEM_NUM();
		Map<String,Integer> goods = new HashMap<String,Integer>();
		if (hasItemNum >= activeateItemNum) {
			// 道具足够
			// 扣道具
			for (Long e : itemIds) {
				if (activeateItemNum > 0) {
					RoleItemExport roleItemExport = roleBagExportService
							.getBagItemByGuid(userRoleId, e);
					int count = roleItemExport.getCount() > activeateItemNum ? activeateItemNum
							: roleItemExport.getCount();
					roleBagExportService.removeBagItemByGuid(e, count,
							userRoleId, GoodsSource.ACTIVATE_YAOSHEN, true,
							true);
					goods.put(roleItemExport.getGoodsId(), count);
					activeateItemNum = activeateItemNum - count;
				}
			}
		} else {
			if (!useGold) {
				return AppErrorCode.YAOSHEN_ITEM_NOT_ENOUGH;
			} else {
				RoleAccountWrapper account = accountExportService
						.getAccountWrapper(userRoleId);
				int needBGold = publicConfig.getItembgold();
				if (needBGold * (activeateItemNum - hasItemNum) < account
						.getBindYb()) {
					// 扣道具
					int tempActiveateItemNum = activeateItemNum;
					for (Long e : itemIds) {
						if (tempActiveateItemNum > 0) {
							RoleItemExport roleItemExport = roleBagExportService
									.getBagItemByGuid(userRoleId, e);
							int count = roleItemExport.getCount() > tempActiveateItemNum ? tempActiveateItemNum
									: roleItemExport.getCount();
							roleBagExportService.removeBagItemByGuid(e, count,
									userRoleId, GoodsSource.ACTIVATE_YAOSHEN, true,
									true);
							goods.put(roleItemExport.getGoodsId(), count);
							tempActiveateItemNum = tempActiveateItemNum - count;
						}
					}
					// 绑元就够了
					// 扣绑元
					Object[] result = accountExportService
							.decrCurrencyWithNotify(
									GoodsCategory.BGOLD,
									needBGold * (activeateItemNum - hasItemNum),
									userRoleId,
									LogPrintHandle.CONSUME_YAOSHEN_ACTIVATE,
									true, LogPrintHandle.CBZ_YAOSHEN_ACTIVATE);
					if (result != null) {
						return result;
					}else{
						if(PlatformConstants.isQQ()){
							BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB, needBGold * (activeateItemNum - hasItemNum),LogPrintHandle.CONSUME_YAOSHEN_ACTIVATE,QQXiaoFeiType.CONSUME_YAOSHEN_ACTIVATE,1});
						}
					}
					consume[0] = needBGold * (activeateItemNum - hasItemNum) ;
				} else {
					// 绑元不够
					int needGold = publicConfig.getItemgold();
					int bgoldNum = (int) (account.getBindYb() / needBGold);
					int goldNum = activeateItemNum - hasItemNum - bgoldNum;
					if (needGold * goldNum > account.getYb()) {
						// 元宝不够
						return AppErrorCode.YAOSHEN_ITEM_NOT_ENOUGH;
					}
					// 扣道具
					int tempActiveateItemNum = activeateItemNum;
					for (Long e : itemIds) {
						if (tempActiveateItemNum > 0) {
							RoleItemExport roleItemExport = roleBagExportService
									.getBagItemByGuid(userRoleId, e);
							int count = roleItemExport.getCount() > tempActiveateItemNum ? tempActiveateItemNum
									: roleItemExport.getCount();
							roleBagExportService.removeBagItemByGuid(e, count,
									userRoleId, GoodsSource.ACTIVATE_YAOSHEN, true,
									true);
							goods.put(roleItemExport.getGoodsId(), count);
							tempActiveateItemNum = tempActiveateItemNum - count;
						}
					}
					// 扣绑元
					Object[] result = accountExportService
							.decrCurrencyWithNotify(GoodsCategory.BGOLD,
									bgoldNum*needBGold, userRoleId,
									LogPrintHandle.CONSUME_YAOSHEN_ACTIVATE,
									true, LogPrintHandle.CBZ_YAOSHEN_ACTIVATE);
					if (result != null) {
						return result;
					}else{
						if(PlatformConstants.isQQ()){
							BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB, bgoldNum*needBGold,LogPrintHandle.CONSUME_YAOSHEN_ACTIVATE,QQXiaoFeiType.CONSUME_YAOSHEN_ACTIVATE,1});
						}
					}
					consume[0] =  (int) account.getBindYb();
					// 扣元宝
					result = accountExportService.decrCurrencyWithNotify(
							GoodsCategory.GOLD, needGold * goldNum, userRoleId,
							LogPrintHandle.CONSUME_YAOSHEN_ACTIVATE, true,
							LogPrintHandle.CBZ_YAOSHEN_ACTIVATE);
					if (result != null) {
						return result;
					}else{
						if(PlatformConstants.isQQ()){
							BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,needGold * goldNum,LogPrintHandle.CONSUME_YAOSHEN_ACTIVATE,QQXiaoFeiType.CONSUME_YAOSHEN_ACTIVATE,1});
						}
					}
					consume[1] =  needGold * goldNum;
				}
			}
		}
		createRoleYaoshen(userRoleId);
		roleSkillExportService.initYaoShenSkills(userRoleId);
		roleYaoshen = roleYaoshenDao.cacheLoad(userRoleId, userRoleId);
		notifyStageYaoshenAttrChange(roleYaoshen);
		YaoshenJichuConfig jichuConfig = yaoshenJichuConfigExportService.load(
				roleYaoshen.getJieLevel(), roleYaoshen.getCengLevel());
		jsonArray = LogPrintHandle.getLogGoodsParam(
				goods, null);
		GamePublishEvent.publishEvent(new ActivateYaoshenLogEvent(userRoleId,jsonArray,consume[0],consume[1]));
		return new Object[] { AppErrorCode.SUCCESS, jichuConfig.getId(),consume };
	}

	public Object[] upgrade(Long userRoleId, List<Long> itemIds) {
		RoleYaoshen roleYaoshen = getRoleYaoshen(userRoleId);
		if (roleYaoshen == null) {
			return AppErrorCode.YAOSHEN_NOT_OPEN;
		}
		int currentJie = roleYaoshen.getJieLevel();
		int currentCeng = roleYaoshen.getCengLevel();
		if (currentCeng == yaoshenJichuConfigExportService.getMAX_CENG()
				&& currentJie == yaoshenJichuConfigExportService.getMAX_JIE()) {
			return AppErrorCode.YAOSHEN_TOP_LEVEL;
		}
		YaoshenJichuConfig jichuConfig = yaoshenJichuConfigExportService.load(
				currentJie, currentCeng);
		if (jichuConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		int hasItemNum = 0;
		for (Long e : itemIds) {
			RoleItemExport roleItemExport = roleBagExportService
					.getBagItemByGuid(userRoleId, e);
			if (roleItemExport == null) {
				return AppErrorCode.YAOSHEN_ITEM_NOT_ENOUGH;
			}
			String goodsId = roleItemExport.getGoodsId();
			GoodsConfig goodsConfig = goodsConfigExportService
					.loadById(goodsId);
			if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_SJ_ITEM  
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
		Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, money, userRoleId,
				LogPrintHandle.CONSUME_YAOSHEN_SJ, true,
				LogPrintHandle.CBZ_YAOSHEN_SJ);
		if (result != null) {
			return result;
		}
		// 扣道具
		Map<String,Integer> goods = new HashMap<String,Integer>();
		for (Long e : itemIds) {
			if (activeateItemNum > 0) {
				RoleItemExport roleItemExport = roleBagExportService
						.getBagItemByGuid(userRoleId, e);
				int count = roleItemExport.getCount() > activeateItemNum ? activeateItemNum
						: roleItemExport.getCount();
				roleBagExportService.removeBagItemByGuid(e, count, userRoleId,
						GoodsSource.UPDATE_YAOSHEN, true, true);
				goods.put(roleItemExport.getGoodsId(), count);
				activeateItemNum = activeateItemNum - count;
			}
		}
		int nextCeng = currentCeng >= yaoshenJichuConfigExportService
				.getMAX_CENG() ? 1 : currentCeng + 1;
		int nextJie = currentCeng >= yaoshenJichuConfigExportService
				.getMAX_CENG() ? currentJie + 1 : currentJie;
		YaoshenJichuConfig nextJichuConfig = yaoshenJichuConfigExportService.load(
				nextJie, nextCeng);
		roleYaoshen.setJieLevel(nextJie);
		roleYaoshen.setCengLevel(nextCeng);
		roleYaoshen.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleYaoshenDao.cacheUpdate(roleYaoshen, userRoleId);
		notifyStageYaoshenAttrChange(roleYaoshen);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(
				goods, null);
		GamePublishEvent.publishEvent(new UpgradeYaoshenLogEvent(userRoleId,jsonArray,nextJie,nextCeng));
		//公告通知
		if(nextJichuConfig.getGgopen()==1){
			 BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { AppErrorCode.YAOSHEN_BATI_UP_NOTICE1, 
					 new Object[] { this.getRoleWrapper(userRoleId).getName(),  roleYaoshen.getJieLevel(),roleYaoshen.getCengLevel()} });
			 /*BusMsgSender.send2One(userRoleId,ClientCmdType.NOTIFY_CLIENT_ALERT3,new Object[]{AppErrorCode.YAOSHEN_BATI_UP_NOTICE2,
					 new Object[] { nextJichuConfig.getName() }	 
			 });*/
		}
		return new Object[] { AppErrorCode.SUCCESS, nextJichuConfig.getId() };
	}

	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer addCeng){
		RoleYaoshen info = getRoleYaoshen(userRoleId);
		if (info == null) {
			return AppErrorCode.YAOSHEN_NOT_OPEN;
		}else{
			int currentJie = info.getJieLevel();
			int currentCeng = info.getCengLevel();
			//最高级
			if (currentCeng == yaoshenJichuConfigExportService.getMAX_CENG()&& currentJie == yaoshenJichuConfigExportService.getMAX_JIE()) {
				return AppErrorCode.YAOSHEN_TOP_LEVEL;
			}
			if(currentJie < minLevel){
				return AppErrorCode.YAOSHEN_LEVEL_LIMIT_CAN_NOT_USE;
			}else if(currentJie > maxLevel){
				return AppErrorCode.YAOSHEN_SHENGXING_MAX_CHAOCHU;
			}else{
				boolean isGongGao = false;
				int configId = 0;
				for (int i = 1; i <= addCeng; i++) {
					int nextCeng = currentCeng >= yaoshenJichuConfigExportService.getMAX_CENG() ? 1 : currentCeng + 1;
					int nextJie = currentCeng >= yaoshenJichuConfigExportService.getMAX_CENG() ? currentJie + 1 : currentJie;
					YaoshenJichuConfig nextJichuConfig = yaoshenJichuConfigExportService.load(nextJie, nextCeng);
					info.setJieLevel(nextJie);
					info.setCengLevel(nextCeng);
					if(nextJichuConfig.getGgopen()==1){
						isGongGao = true;
					}
					configId = nextJichuConfig.getId();
				}
				info.setUpdateTime(GameSystemTime.getSystemMillTime());
				roleYaoshenDao.cacheUpdate(info, userRoleId);
				notifyStageYaoshenAttrChange(info);
				//通知前端
				BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_UPGRADE, new Object[] { AppErrorCode.SUCCESS, configId });
				//公告通知
				if(isGongGao){
					 BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { AppErrorCode.YAOSHEN_BATI_UP_NOTICE1, 
							 new Object[] { this.getRoleWrapper(userRoleId).getName(),  info.getJieLevel(),info.getCengLevel()} });
				}
				
			}
			
		}
		return null;
	}
	 
	
	/**
	 * 使用潜能丹
	 * 
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] useQND(long userRoleId, int count) {
		RoleYaoshen info = getRoleYaoshen(userRoleId);
		if (info == null) {
			return AppErrorCode.YAOSHEN_NOT_OPEN;
		}
		int currentJie = info.getJieLevel();
		int currentCeng = info.getCengLevel();
		YaoshenJichuConfig jichuConfig = yaoshenJichuConfigExportService.load(
				currentJie, currentCeng);
		if (jichuConfig.getQndopen() != YaoshenConstants.QND_OPEN_FLAG) {
			return AppErrorCode.YAOSHEN_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if (info.getQndNum().intValue() + count > jichuConfig.getQndmax()) {
			return AppErrorCode.YAOSHEN_QND_MAX_NUM;
		}
		info.setQndNum(count + info.getQndNum());
		roleYaoshenDao.cacheUpdate(info, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_QND_NUM,
				info.getQndNum() != null ? info.getQndNum() : 0);
		notifyStageYaoshenAttrChange(info);
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
		RoleYaoshen info = getRoleYaoshen(userRoleId);
		if (info == null) {
			return AppErrorCode.YAOSHEN_NOT_OPEN;
		}
		int currentJie = info.getJieLevel();
		int currentCeng = info.getCengLevel();
		YaoshenJichuConfig jichuConfig = yaoshenJichuConfigExportService.load(
				currentJie, currentCeng);
		if (jichuConfig.getQndopen() != YaoshenConstants.CZD_OPEN_FLAG) {
			return AppErrorCode.YAOSHEN_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if (info.getCzdNum().intValue() + count > jichuConfig.getCzdmax()) {
			return AppErrorCode.YAOSHEN_CZD_MAX_NUM;
		}
		info.setCzdNum(count + info.getCzdNum());
		roleYaoshenDao.cacheUpdate(info, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_CZD_NUM,
				info.getCzdNum() != null ? info.getCzdNum() : 0);
		notifyStageYaoshenAttrChange(info);
		return null;
	}

	public void notifyStageYaoshenAttrChange(RoleYaoshen yaoshen) {
		int currentJie = yaoshen.getJieLevel();
		int currentCeng = yaoshen.getCengLevel();
		BusMsgSender.send2Stage(yaoshen.getUserRoleId(),
				InnerCmdType.INNER_YAOSHEN_ATTR_CHANGE,
				new Object[] { currentJie, currentCeng, yaoshen.getQndNum(),
						yaoshen.getCzdNum() });
	}

	public Object[] changeYaoshenShow(Long userRoleId) {
		String stageId = publicRoleStateExportService.getRolePublicStageId(userRoleId);
		if(stageId == null){
			return AppErrorCode.ERR;
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null){
			return AppErrorCode.ERR;
		}
		if(!stage.isCanChangeYaoShen()){
			return AppErrorCode.WENQUAN_NOT_CHANGE;
		}
		RoleYaoshen info = getRoleYaoshen(userRoleId);
		if (info == null) {
			return AppErrorCode.YAOSHEN_NOT_ACTIVATE;
		}
		YaoshenPublicConfig publicConfig = getPublicConfig();
		if (publicConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		Long cd = publicConfig.getYaoshencd();
		Long endTimStamp = 0L;
		if (info.getIsYaoshenShow() == YaoshenConstants.YAOSHEN_SHOW_YES) {
			// 校验cd
			Long lastChangeTime = dataContainer.getData(GameConstants.YAOSHEN_SHOW_CHANGE, userRoleId.toString());
			if (lastChangeTime != null) {
				Long currentTime = GameSystemTime.getSystemMillTime();
				if (currentTime < lastChangeTime + cd) {
					return AppErrorCode.YAOSHEN_SHOW_CHANGE_CD;
				}
			}
			info.setIsYaoshenShow(YaoshenConstants.YAOSHEN_SHOW_NO);
		} else {
			info.setIsYaoshenShow(YaoshenConstants.YAOSHEN_SHOW_YES);
			dataContainer.putData(GameConstants.YAOSHEN_SHOW_CHANGE,userRoleId.toString(), GameSystemTime.getSystemMillTime());
			endTimStamp = cd + GameSystemTime.getSystemMillTime();
		}
		roleYaoshenDao.cacheUpdate(info, userRoleId);
		roleSkillExportService.changeSkill(userRoleId, info.getIsYaoshenShow() == YaoshenConstants.YAOSHEN_SHOW_YES);
		BusMsgSender.send2Stage(info.getUserRoleId(),InnerCmdType.INNER_YAOSHEN_SHOW_CHANGE,info.getIsYaoshenShow());
		return new Object[] { AppErrorCode.SUCCESS,info.getIsYaoshenShow() == YaoshenConstants.YAOSHEN_SHOW_YES ,endTimStamp};
	}

	public void onlineHandle(Long userRoleId) {
		RoleYaoshen info = getRoleYaoshen(userRoleId);
		if (info == null) {
			return;
		}
		YaoshenJichuConfig jichuConfig = yaoshenJichuConfigExportService.load(
				info.getJieLevel(), info.getCengLevel());
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_CONFIG_ID,
				jichuConfig.getId());
		Long endTimeStamp = 0L;
		if (info.getIsYaoshenShow() == YaoshenConstants.YAOSHEN_SHOW_YES){
			Long lastChangeTime = dataContainer.getData(
					GameConstants.YAOSHEN_SHOW_CHANGE, userRoleId.toString());
			if (lastChangeTime != null) {
				Long currentTime = GameSystemTime.getSystemMillTime();
				YaoshenPublicConfig publicConfig = getPublicConfig();
				if (publicConfig != null) {
					Long cd = publicConfig.getYaoshencd();
					if (currentTime < lastChangeTime + cd) {
						endTimeStamp = lastChangeTime + cd;
					}
				}
			}
		}
		BusMsgSender
				.send2One(
						userRoleId,
						ClientCmdType.YAOSHEN_CHANGE_SHOW,
						new Object[] {
								AppErrorCode.SUCCESS,
								info.getIsYaoshenShow().intValue() == YaoshenConstants.YAOSHEN_SHOW_YES,endTimeStamp});
//		if (info.getIsYaoshenShow().intValue() == YaoshenConstants.YAOSHEN_SHOW_YES) {
//			roleSkillExportService.changeSkill(userRoleId, true);
//		}
	}
	public Map<String, Long> getYaoshenAttribute(Long userRoleId,boolean show) {
		RoleYaoshen info = getRoleYaoshen(userRoleId);
		if (info == null) {
			if(show){
				return yaoshenJichuConfigExportService.getYaoshenAttribute(1, 1);
			}
			return null;
		}
		return getYaoshenAttribute(info.getJieLevel(), info.getCengLevel(),info.getQndNum(), info.getCzdNum());
	}

	public Map<String, Long> getYaoshenAttribute(Integer jie, Integer ceng,Integer qndNum, Integer czdNum) {
		Map<String, Long> ret = new HashMap<String, Long>();
		Map<String, Long> jichuAttr = yaoshenJichuConfigExportService
				.getYaoshenAttribute(jie, ceng);
		if (jichuAttr != null && jichuAttr.size() > 0) {
			ObjectUtil.longMapAdd(ret, jichuAttr);
		}
		Long speed = ret.remove(EffectType.x19.name());
		if (czdNum > 0) {
			String czdId = yaoshenJichuConfigExportService.getCZD_ID();
			float multi = getCzdMulti(czdId) * czdNum / 100f + 1f;
			ObjectUtil.longMapTimes(ret, multi);
		}
		if (qndNum > 0) {
			YaoshenQianNengBiaoConfig qiannengConfig = yaoshenQianNengBiaoConfigExportService
					.getConfig(YaoshenConstants.BATI_QND);
			for (int i = 0; i < qndNum; i++) {
				ObjectUtil.longMapAdd(ret, qiannengConfig.getAttrs());
			}
		}
		if (speed != null) {
			ret.put(EffectType.x19.name(), speed);
		}	
		return ret;
	}
	/**
	 * 玩家登陆后统计属性
	 */
	public Map<String, Long> getYaoshenMowenAttributeAfterLogin(Long userRoleId) {
		//先初始化一次
		 RoleWrapper  roleWrapper = roleExportService.getLoginRole(userRoleId);
		 if(roleWrapper!=null){
			 checkLevelAndActiveMowen(userRoleId,roleWrapper.getLevel());
		 }
		 Map<String, Long> attrMap  =   getYaoshenMowenAttribute(userRoleId);
		//通知客户端 用来显示脚下光圈
		 RoleYaoshenMowen roleYaoshenMowen = getRoleYaoshenMowen(userRoleId);
		 if(roleYaoshenMowen!=null){
			 BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOWEN_JIE_NOTICE_CLIENT,roleYaoshenMowen.getJieLevel());
		 }
		 return attrMap;
	}
	
	/**
	 * 妖神魔纹属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getYaoshenMowenAttribute(Long userRoleId) {
		RoleYaoshenMowen info = getRoleYaoshenMowen(userRoleId);
		if (info == null) {
			return null;
		}
		return getYaoshenMowenAttribute(info.getJieLevel(), info.getCengLevel(),
				info.getQndNum(), info.getCzdNum());
	}
	/**
	 * 客户端拉取某一阶的属性
	 */
	public Map<String, Long> getYaoshenMowenAttributeByJie(Long userRoleId,int jie){
		 RoleWrapper  roleWrapper = roleExportService.getLoginRole(userRoleId);
		 if(roleWrapper==null|| jie<=0){
			 return  null;
		 }
		RoleYaoshenMowen  roleYaoshenMowen  = getRoleYaoshenMowen(userRoleId);
		if(roleYaoshenMowen==null||jie>roleYaoshenMowen.getJieLevel()){
			return null;
		}
		int cengLevel =0;
		//历史阶属性，算出满层数
		Map<Integer, YaoshenJichuConfig> allMap = yaoshenMoWenConfigExportService.loadByJie(jie);
		if(roleYaoshenMowen.getJieLevel()==jie){
			cengLevel  = roleYaoshenMowen.getCengLevel();
		}else{
			int maxCeng = 0;
			for (Entry<Integer, YaoshenJichuConfig> entry : allMap.entrySet()) {
				int ceng = entry.getValue().getId2(); //获得层
				if(ceng>maxCeng){
					maxCeng=ceng;
				}
			}
			cengLevel  = maxCeng;
		}
		return getYaoshenMowenAttribute(jie,cengLevel,roleYaoshenMowen.getQndNum(),roleYaoshenMowen.getCzdNum());
	}
	
	/**
	 * 妖神魔纹属性
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getYaoshenMowenAttribute(Integer jie, Integer ceng,
			Integer qndNum, Integer czdNum) {
		Map<String, Long> ret = new HashMap<String, Long>();
		Map<String, Long> jichuAttr = yaoshenMoWenConfigExportService
				.getYaoshenAttribute(jie, ceng);
		if (jichuAttr != null && jichuAttr.size() > 0) {
			ObjectUtil.longMapAdd(ret, jichuAttr);
		}
		Long speed = ret.remove(EffectType.x19.name());
		if (czdNum > 0) {
			String czdId = yaoshenMoWenConfigExportService.getCZD_ID();
			float multi = getCzdMulti(czdId) * czdNum / 100f + 1f;
			ObjectUtil.longMapTimes(ret, multi);
		}
		if (qndNum > 0) {
			YaoshenQianNengBiaoConfig qiannengConfig = yaoshenQianNengBiaoConfigExportService
					.getConfig(YaoshenConstants.MOWEN_QND);
			for (int i = 0; i < qndNum; i++) {
				ObjectUtil.longMapAdd(ret, qiannengConfig.getAttrs());
			}
		}
		if (speed != null) {
			ret.put(EffectType.x19.name(), speed);
		}	
		return ret;
	}
	private int multi = 0;

	public int getCzdMulti(String czdId) {
		if (multi == 0) {
			List<String> goodsIds = goodsConfigExportService
					.loadIdsById1(czdId);
			if (goodsIds.size() > 0) {
				GoodsConfig config = goodsConfigExportService.loadById(goodsIds
						.get(0));
				multi = config.getData1();
			}
		}
		return multi;
	}

	public List<YaoShenRankVo> getYaoShenRankVo(int limit) {
		return roleYaoshenDao.getYaoShenRankVo(limit);
	}
	public List<YaoShenMowenRankVo> getMowenRankVo(int limit) {
		return roleYaoshenMowenDao.getMowenRankVo(limit);
	}
	
	
	public int getYaoshenId(Long userRoleId) {
		RoleYaoshen yaoshen = getRoleYaoshen(userRoleId);
		if (yaoshen == null) {
			return -1;
		} else {
			YaoshenJichuConfig config = yaoshenJichuConfigExportService.load(yaoshen.getJieLevel(), yaoshen.getCengLevel());
			if(config == null){
				return -1;
			}
			return config.getId();
		}
	}
	/**
	 * 魔纹公共配置
	 * @return
	 */
	public YaoshenPublicConfig getMowenPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_YAOSHEN_MOWEN);
	}
	/**
	 * 获得魔纹Entity对象
	 * @param userRoleId
	 * @return
	 */
	public RoleYaoshenMowen getRoleYaoshenMowen(Long userRoleId) {
		//玩家登陆后要先放进缓存
		RoleYaoshenMowen ret = roleYaoshenMowenDao.cacheLoad(userRoleId, userRoleId);
		return ret;
	}
	/**
	 * 创建魔纹entity
	 */
	private RoleYaoshenMowen createMowen(Long userRoleId){
		RoleYaoshenMowen  roleYaoshenMowen  = new RoleYaoshenMowen();
		roleYaoshenMowen.setUserRoleId(userRoleId);
		roleYaoshenMowen.setCzdNum(0);
		roleYaoshenMowen.setQndNum(0);
		roleYaoshenMowen.setCengLevel(1);
		roleYaoshenMowen.setJieLevel(1);
		roleYaoshenMowen.setUpdateTime(GameSystemTime.getSystemMillTime());
		notifyStageYaoshenMowenAttrChange(roleYaoshenMowen);//通知加属性
		return roleYaoshenMowen;
	}
	/**
	 * 获取魔纹阶数
	 */
	public int getMowenJie(Long userRoleId){
		RoleYaoshenMowen  roleYaoshenMowen  = getRoleYaoshenMowen(userRoleId);
		if(roleYaoshenMowen!=null){
			return roleYaoshenMowen.getJieLevel();
		}
		return 0;
	}
	/**
	 * 等级到了自动激活魔纹
	 */
	public void checkLevelAndActiveMowen(Long userRoleId,int level){
		try {
			YaoshenPublicConfig yaoshenPublicConfig  = getMowenPublicConfig();
			if(yaoshenPublicConfig==null){
				ChuanQiLog.error("***公共配置表中妖神魔纹信息不存在***");
				return;
			}
			RoleYaoshenMowen roleYaoshenMowen = getRoleYaoshenMowen(userRoleId);
			if(level>=yaoshenPublicConfig.getOpen() && roleYaoshenMowen==null){
				roleYaoshenMowen = this.createMowen(userRoleId);
				roleYaoshenMowenDao.cacheInsert(roleYaoshenMowen, userRoleId);
			} 
		} catch (Exception e) {
			ChuanQiLog.error("激活妖神魔纹失败：{}",e);
		}
	}
	/**
	 * 获取妖神魔纹信息
	 */
	public Object[] getMowenInfo(Long userRoleId){
		RoleYaoshenMowen roleYaoshenMowen = getRoleYaoshenMowen(userRoleId);
		if(roleYaoshenMowen==null){
			return  AppErrorCode.YAOSHEN_MOWEN_NOT_OPEN;
		}
		YaoshenJichuConfig yaoshenJichuConfig = yaoshenMoWenConfigExportService.load(roleYaoshenMowen.getJieLevel(), roleYaoshenMowen.getCengLevel());
		if (yaoshenJichuConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<String, Long> mowenAttrMap  = this.getYaoshenMowenAttribute(userRoleId);
		return new Object[]{yaoshenJichuConfig.getId(),roleYaoshenMowen.getQndNum(),roleYaoshenMowen.getCzdNum(),mowenAttrMap};
	}
	/**
	 * 魔纹升级
	 * @param userRoleId
	 * @param itemIds
	 * @return
	 */
	public Object[] mowenUpgrade(Long userRoleId, List<Long> itemIds) {
		 RoleWrapper  roleWrapper = roleExportService.getLoginRole(userRoleId);
		 
		 if(roleWrapper==null){
			 return  AppErrorCode.ROLE_IS_NOT_ONLINE;
		 }
		YaoshenPublicConfig yaoshenPublicConfig  = getMowenPublicConfig();
		
		if(roleWrapper.getLevel()<yaoshenPublicConfig.getOpen()){
			 return  AppErrorCode.ROLE_LEVEL_ERROR;
		}
		RoleYaoshenMowen roleYaoshenMowen = getRoleYaoshenMowen(userRoleId);
		if (roleYaoshenMowen == null) {
			return AppErrorCode.YAOSHEN_MOWEN_NOT_OPEN;
		}
		int currentJie = roleYaoshenMowen.getJieLevel();
		int currentCeng = roleYaoshenMowen.getCengLevel();
		if (currentCeng == yaoshenMoWenConfigExportService.getMAX_CENG()
				&& currentJie == yaoshenMoWenConfigExportService.getMAX_JIE()) {
			return AppErrorCode.YAOSHEN_TOP_LEVEL;
		}
		YaoshenJichuConfig jichuConfig = yaoshenMoWenConfigExportService.load(
				currentJie, currentCeng);
		if (jichuConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		int hasItemNum = 0;
		for (Long e : itemIds) {
			RoleItemExport roleItemExport = roleBagExportService
					.getBagItemByGuid(userRoleId, e);
			if (roleItemExport == null) {
				return AppErrorCode.YAOSHEN_ITEM_NOT_ENOUGH;
			}
			String goodsId = roleItemExport.getGoodsId();
			GoodsConfig goodsConfig = goodsConfigExportService
					.loadById(goodsId);
			if (goodsConfig.getCategory() != GoodsCategory.YAOSHEN_MOWEN_ITEM 
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
		Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, money, userRoleId,
				LogPrintHandle.CONSUME_YAOSHEN_MOWEN_SJ, true,
				LogPrintHandle.CBZ_YAOSHEN_MOWEN_SJ);
		if (result != null) {
			return result;
		}
		// 扣道具
		Map<String,Integer> goods = new HashMap<String,Integer>();
		for (Long e : itemIds) {
			if (activeateItemNum > 0) {
				RoleItemExport roleItemExport = roleBagExportService
						.getBagItemByGuid(userRoleId, e);
				int count = roleItemExport.getCount() > activeateItemNum ? activeateItemNum
						: roleItemExport.getCount();
				roleBagExportService.removeBagItemByGuid(e, count, userRoleId,
						GoodsSource.UPDATE_YAOSHEN_MOWEN, true, true);
				goods.put(roleItemExport.getGoodsId(), count);
				activeateItemNum = activeateItemNum - count;
			}
		}
		int nextCeng = currentCeng >= yaoshenMoWenConfigExportService
				.getMAX_CENG() ? 1 : currentCeng + 1;
		int nextJie = currentCeng >= yaoshenMoWenConfigExportService
				.getMAX_CENG() ? currentJie + 1 : currentJie;
		YaoshenJichuConfig nextJichuConfig = yaoshenMoWenConfigExportService.load(nextJie, nextCeng);
		roleYaoshenMowen.setJieLevel(nextJie);
		roleYaoshenMowen.setCengLevel(nextCeng);
		roleYaoshenMowen.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleYaoshenMowenDao.cacheUpdate(roleYaoshenMowen, userRoleId);
		notifyStageYaoshenMowenAttrChange(roleYaoshenMowen);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(goods, null);
		GamePublishEvent.publishEvent(new UpgradeYaoshenMowenLogEvent(userRoleId,jsonArray,nextJie,nextCeng));
		//公告通知
		if(nextJichuConfig.getGgopen()==1){
			 BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { AppErrorCode.YAOSHEN_MOWEN_UP_NOTICE1, 
					 new Object[] { roleWrapper.getName(), 
					 				roleYaoshenMowen.getJieLevel(),roleYaoshenMowen.getCengLevel() } });
			 
//			 BusMsgSender.send2One(userRoleId,ClientCmdType.NOTIFY_CLIENT_ALERT3,new Object[]{AppErrorCode.YAOSHEN_MOWEN_UP_NOTICE2,
//					 new Object[] {nextJichuConfig.getName() }	 
//			 });
		}
		return new Object[] { AppErrorCode.SUCCESS, nextJichuConfig.getId() };
	}
	
	public Object[] sjByItemMoWen(Long userRoleId,Integer minLevel,Integer maxLevel,Integer addCeng){
		RoleYaoshenMowen info = getRoleYaoshenMowen(userRoleId);
		if (info == null) {
			return AppErrorCode.YAOSHEN_NOT_OPEN;
		}else{
			int currentJie = info.getJieLevel();
			int currentCeng = info.getCengLevel();
			//最高级
			if (currentCeng == yaoshenMoWenConfigExportService.getMAX_CENG()&& currentJie == yaoshenMoWenConfigExportService.getMAX_JIE()) {
				return AppErrorCode.YAOSHEN_TOP_LEVEL;
			}
			if(currentJie < minLevel){
				return AppErrorCode.YAOSHEN_MOWEN_LEVEL_LIMIT_CAN_NOT_USE;
			}else if(currentJie > maxLevel){
				return AppErrorCode.YAOSHEN_SHENGXING_MAX_CHAOCHU;
			}else{
				boolean isGongGao = false;
				int configId = 0;
				for (int i = 1; i <= addCeng; i++) {
					int nextCeng = currentCeng >= yaoshenMoWenConfigExportService.getMAX_CENG() ? 1 : currentCeng + 1;
					int nextJie = currentCeng >= yaoshenMoWenConfigExportService.getMAX_CENG() ? currentJie + 1 : currentJie;
					YaoshenJichuConfig nextJichuConfig = yaoshenMoWenConfigExportService.load(nextJie, nextCeng);
					info.setJieLevel(nextJie);
					info.setCengLevel(nextCeng);
					if(nextJichuConfig.getGgopen()==1){
						isGongGao = true;
					}
					configId = nextJichuConfig.getId();
				}
				info.setUpdateTime(GameSystemTime.getSystemMillTime());
				roleYaoshenMowenDao.cacheUpdate(info, userRoleId);
				notifyStageYaoshenMowenAttrChange(info);
				//通知前端
				BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOWEN_UP, new Object[] { AppErrorCode.SUCCESS, configId });
				//公告通知
				if(isGongGao){
					 BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { AppErrorCode.YAOSHEN_MOWEN_UP_NOTICE1, 
							 new Object[] { this.getRoleWrapper(userRoleId).getName(), 
							 				info.getJieLevel(),info.getCengLevel() } });
				}
				
			}
			
		}
		return null;
	}
	
	private RoleWrapper getRoleWrapper(long userRoleId){
		RoleWrapper role =null;
		if(publicRoleStateService.isPublicOnline(userRoleId)){
			 role = roleExportService.getLoginRole(userRoleId);
		}else{
			 role = roleExportService.getUserRoleFromDb(userRoleId);
		}
		return role;
	}
	/**
	 * 魔纹属性变化
	 * @param roleYaoshenMowen
	 */
	public void notifyStageYaoshenMowenAttrChange(RoleYaoshenMowen roleYaoshenMowen) {
		int currentJie = roleYaoshenMowen.getJieLevel();
		int currentCeng = roleYaoshenMowen.getCengLevel();
		BusMsgSender.send2Stage(roleYaoshenMowen.getUserRoleId(),
				InnerCmdType.INNER_YAOSHEN_MOWEN_CHANGE,
				new Object[] { currentJie, currentCeng, roleYaoshenMowen.getQndNum(),
						roleYaoshenMowen.getCzdNum() });
	}
	
	/**
	 * 使用潜能丹
	 * 
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] useMowenQND(long userRoleId, int count) {
		RoleYaoshenMowen roleYaoshenMowen = getRoleYaoshenMowen(userRoleId);
		if (roleYaoshenMowen == null) {
			return AppErrorCode.YAOSHEN_MOWEN_NOT_OPEN;
		}
		int currentJie = roleYaoshenMowen.getJieLevel();
		int currentCeng = roleYaoshenMowen.getCengLevel();
		YaoshenJichuConfig jichuConfig = yaoshenMoWenConfigExportService.load(
				currentJie, currentCeng);
		if (jichuConfig.getQndopen() != YaoshenConstants.QND_OPEN_FLAG) {
			return AppErrorCode.YAOSHEN_MOWEN_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if (roleYaoshenMowen.getQndNum().intValue() + count > jichuConfig.getQndmax()) {
			return AppErrorCode.YAOSHEN_MOWEN_QND_MAX_NUM;
		}
		roleYaoshenMowen.setQndNum(count + roleYaoshenMowen.getQndNum());
		roleYaoshenMowenDao.cacheUpdate(roleYaoshenMowen, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOWEN_QND_NUM,
				roleYaoshenMowen.getQndNum() != null ? roleYaoshenMowen.getQndNum() : 0);
		notifyStageYaoshenMowenAttrChange(roleYaoshenMowen);
		return null;
	}

	/**
	 * 使用成长丹
	 * 
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] useMowenCZD(long userRoleId, int count) {
		RoleYaoshenMowen roleYaoshenMowen = getRoleYaoshenMowen(userRoleId);
		if (roleYaoshenMowen == null) {
			return AppErrorCode.YAOSHEN_MOWEN_NOT_OPEN;
		}
		int currentJie = roleYaoshenMowen.getJieLevel();
		int currentCeng = roleYaoshenMowen.getCengLevel();
		YaoshenJichuConfig jichuConfig = yaoshenMoWenConfigExportService.load(
				currentJie, currentCeng);
		if (jichuConfig.getQndopen() != YaoshenConstants.CZD_OPEN_FLAG) {
			return AppErrorCode.YAOSHEN_MOWEN_LEVEL_LIMIT_CAN_NOT_USE;
		}
		if (roleYaoshenMowen.getCzdNum().intValue() + count > jichuConfig.getCzdmax()) {
			return AppErrorCode.YAOSHEN_MOWEN_CZD_MAX_NUM;
		}
		roleYaoshenMowen.setCzdNum(count + roleYaoshenMowen.getCzdNum());
		roleYaoshenMowenDao.cacheUpdate(roleYaoshenMowen, userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.YAOSHEN_MOWEN_CZD_NUM,
				roleYaoshenMowen.getCzdNum() != null ? roleYaoshenMowen.getCzdNum() : 0);
		notifyStageYaoshenMowenAttrChange(roleYaoshenMowen);
		return null;
	}
	
}
