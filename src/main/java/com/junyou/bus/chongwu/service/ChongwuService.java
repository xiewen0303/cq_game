package com.junyou.bus.chongwu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.entity.RoleAccountWrapper;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.chongwu.configure.export.ChongWuJiHuoBiaoConfig;
import com.junyou.bus.chongwu.configure.export.ChongWuJiHuoBiaoConfigExportService;
import com.junyou.bus.chongwu.configure.export.ChongWuJiNengBiaoConfig;
import com.junyou.bus.chongwu.configure.export.ChongWuJiNengBiaoConfigExportService;
import com.junyou.bus.chongwu.configure.export.ChongWuShengJiBiaoConfig;
import com.junyou.bus.chongwu.configure.export.ChongWuShengJiBiaoConfigExportService;
import com.junyou.bus.chongwu.configure.export.ChongWuShengJieBiaoConfig;
import com.junyou.bus.chongwu.configure.export.ChongWuShengJieBiaoConfigExportService;
import com.junyou.bus.chongwu.constants.ChongwuConstants;
import com.junyou.bus.chongwu.dao.RoleChongwuDao;
import com.junyou.bus.chongwu.dao.RoleChongwuSkillDao;
import com.junyou.bus.chongwu.entity.RoleChongwu;
import com.junyou.bus.chongwu.entity.RoleChongwuSkill;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgQueue;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.ActivateChongwuLogEvent;
import com.junyou.event.ChongwuSkillLogEvent;
import com.junyou.event.UpgradeChongwuLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.goods.configure.export.helper.BusConfigureHelper;
import com.junyou.gameconfig.publicconfig.configure.export.ChongwuPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.stage.model.element.chongwu.Chongwu;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class ChongwuService {
	@Autowired
	private RoleChongwuDao roleChongwuDao;
    @Autowired
    private RoleChongwuSkillDao roleChongwuSkillDao;
	@Autowired
	private ChongWuJiHuoBiaoConfigExportService chongWuJiHuoBiaoConfigExportService;
	@Autowired
	private ChongWuJiNengBiaoConfigExportService chongWuJiNengBiaoConfigExportService;
	@Autowired
	private ChongWuShengJiBiaoConfigExportService chongWuShengJiBiaoConfigExportService;
	@Autowired
	private ChongWuShengJieBiaoConfigExportService chongWuShengJieBiaoConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private AccountExportService accountExportService;

	public ChongwuPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_CHONGWU);
	}

	public List<RoleChongwu> initRoleChongwu(Long userRoleId) {
		return roleChongwuDao.initRoleChongwu(userRoleId);
	}

	private RoleChongwu getChongwu(Long userRoleId,
			final Integer chongwuConfigId) {
		List<RoleChongwu> list = roleChongwuDao.cacheLoadAll(userRoleId,
				new IQueryFilter<RoleChongwu>() {
					private boolean stop = false;

					@Override
					public boolean check(RoleChongwu chongwu) {
						if (chongwu.getConfigId().equals(chongwuConfigId)) {
							stop = true;
						}
						return stop;
					}

					@Override
					public boolean stopped() {
						return stop;
					}
				});
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private RoleChongwu create(Long userRoleId, Integer configId) {
		RoleChongwu pojo = new RoleChongwu();
		pojo.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		pojo.setUserRoleId(userRoleId);
		pojo.setStatus(ChongwuConstants.CHONGWU_FIGHT_STATUS_NO);
		pojo.setConfigId(configId);
		pojo.setJie(1);
		pojo.setCeng(1);
		pojo.setJieExp(0L);
		pojo.setLevel(1);
		pojo.setLevelExp(0L);
		pojo.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleChongwuDao.cacheInsert(pojo, userRoleId);
		return getChongwu(userRoleId, configId);
	}

	public Object[] getChongwuList(Long userRoleId) {
		List<RoleChongwu> list = roleChongwuDao.cacheLoadAll(userRoleId);
		if (list == null || list.size() == 0) {
			return null;
		}
		Object[] ret = new Object[2];
		Object[] listInfo = new Object[list.size()];
		ret[0] = listInfo;
		for (int i = 0; i < list.size(); i++) {
			RoleChongwu e = list.get(i);
			if (e.getStatus() == ChongwuConstants.CHONGWU_FIGHT_STATUS_YES) {
				ret[1] = e.getConfigId();
			}
			listInfo[i] = new Object[] {
					e.getConfigId(),
					e.getJie(),
					e.getLevel(),
					getChongwuZplus(e.getConfigId(), e.getJie(), e.getCeng(),
							e.getLevel()) };
		}
		return ret;
	}

    public Object[] getChongwuDetail(Long userRoleId, Integer configId) {
        Object[] ret = new Object[8];
        RoleChongwu chongwu = getChongwu(userRoleId, configId);
        if (chongwu == null) {
            ret[0] = configId;
            return ret;
        }
        int index = 0;
        ret[index++] = configId;
        ret[index++] = chongwu.getLevel();
        ret[index++] = chongwu.getLevelExp();
        ret[index++] = chongWuShengJieBiaoConfigExportService.loadByIdJieCeng(configId, chongwu.getJie(), chongwu.getCeng()).getId1();
        ret[index++] = chongwu.getJieExp();
        ret[index++] = roleBagExportService.getAllChongwuEquips(userRoleId, configId);
        ret[index++] = getChongwuAttribute(configId, chongwu.getLevel(), chongwu.getJie(), chongwu.getCeng());
        ret[index]   = getRoleChongwuSkillData(userRoleId, configId);
        return ret;
    }

	public RoleChongwu getFihgtChongwu(Long userRoleId) {
		List<RoleChongwu> list = roleChongwuDao.cacheLoadAll(userRoleId,
				new IQueryFilter<RoleChongwu>() {
					private boolean stop = false;

					@Override
					public boolean check(RoleChongwu chongwu) {
						if (chongwu.getStatus().equals(
								ChongwuConstants.CHONGWU_FIGHT_STATUS_YES)) {
							stop = true;
						}
						return stop;
					}

					@Override
					public boolean stopped() {
						return stop;
					}
				});
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;

	}

	public Chongwu getFightCw(Long userRoleId) {
		RoleChongwu chongwu = getFihgtChongwu(userRoleId);
		if (chongwu == null) {
			return null;
		}
		int speed = getPublicConfig().getSpeed();
		String name = chongWuJiHuoBiaoConfigExportService.loadById(
				chongwu.getConfigId()).getName();
		Chongwu cw = new Chongwu(chongwu.getId(), name);
		cw.setSpeed(speed);
		cw.setConfigId(chongwu.getConfigId());
		return cw;
	}

	public void onlineHandle(Long userRoleId){
		UserRole userRole = roleExportService.getUserRole(userRoleId);
		ChongwuPublicConfig publicConfig = getPublicConfig();
		if (userRole.getLevel() < publicConfig.getOpen()) {
			return;
		}
		RoleChongwu chongwu = getFihgtChongwu(userRoleId);
		Integer configId = 0;
		Long chongwuGuid = 0L;
		if (chongwu != null) {
			configId = chongwu.getConfigId();
			chongwuGuid = chongwu.getId();
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.CHONGWU_FIGHT,
				new Object[]{AppErrorCode.SUCCESS,configId,chongwuGuid});
	}
	private Long getChongwuZplus(int configId, int jie, int ceng, int level) {
		Map<String, Long> attr = new HashMap<String, Long>();
		ChongWuShengJiBiaoConfig shengjiConfig = chongWuShengJiBiaoConfigExportService
				.loadByLevel(level);
		Map<String, Long> shengjiAttr = shengjiConfig.getAttrs();
		ObjectUtil.longMapAdd(attr, shengjiAttr);
		ChongWuShengJieBiaoConfig shengjieConfig = chongWuShengJieBiaoConfigExportService
				.loadByIdJieCeng(configId, jie, ceng);
		Map<String, Long> shengjieAttr = shengjieConfig.getAttrs();
		ObjectUtil.longMapAdd(attr, shengjieAttr);
		return attr.get("zplus");
	}

	public Object[] activateChongwu(Long userRoleId, Integer configId,
			List<Long> itemList) {
		UserRole userRole = roleExportService.getUserRole(userRoleId);
		ChongwuPublicConfig publicConfig = getPublicConfig();
		if (userRole.getLevel() < publicConfig.getOpen()) {
			return AppErrorCode.CHONGWU_NOT_OPEN;
		}
		// 判断是否激活
		RoleChongwu chongwu = getChongwu(userRoleId, configId);
		if (chongwu != null) {
			return AppErrorCode.CHONGWU_ACTIVATED;
		}
		ChongWuJiHuoBiaoConfig chongWuJiHuoBiaoConfig = chongWuJiHuoBiaoConfigExportService
				.loadById(configId);
		if (chongWuJiHuoBiaoConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		// 判断道具是否足够
		String categoryId = chongWuJiHuoBiaoConfig.getNeeditem();
		int count = chongWuJiHuoBiaoConfig.getCount();
		// 验证item
		if (itemList == null || itemList.size() == 0) {
			return AppErrorCode.CHONGWU_ITEM_NOT_ENOUGH;
		}
		int itemCount = 0;
		for (Long e : itemList) {
			RoleItemExport roleItemExport = roleBagExportService
					.getBagItemByGuid(userRoleId, e);
			if (roleItemExport == null) {
				return AppErrorCode.CHONGWU_ITEM_NOT_ENOUGH;
			}
			String goodsId = roleItemExport.getGoodsId();
			GoodsConfig goodsConfig = goodsConfigExportService
					.loadById(goodsId);
			if (goodsConfig == null) {
				return AppErrorCode.CHONGWU_ITEM_ERROR;
			}
			if (!goodsConfig.getId1().equals(categoryId)) {
				return AppErrorCode.CHONGWU_ITEM_ERROR;
			}
			itemCount = itemCount + roleItemExport.getCount();
		}
		if (itemCount < count) {
			return AppErrorCode.CHONGWU_ITEM_NOT_ENOUGH;
		}
		// 扣道具
		int itemNeed = count;
		Map<String, Integer> goods = new HashMap<String, Integer>();
		for (Long e : itemList) {
			if (itemNeed > 0) {
				RoleItemExport roleItemExport = roleBagExportService
						.getBagItemByGuid(userRoleId, e);
				int consumeCount = roleItemExport.getCount() > itemNeed ? itemNeed
						: roleItemExport.getCount();
				roleBagExportService.removeBagItemByGuid(e, consumeCount,
						userRoleId, GoodsSource.GOODS_CHONGWU_ACTIVATE_CONSUME,
						true, true);
				itemNeed = itemNeed - count;
				goods.put(roleItemExport.getGoodsId(), count);
			}
		}
		chongwu = create(userRoleId, configId);
		notifyStageChongwuAttrChange(chongwu);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(goods, null);
		GamePublishEvent.publishEvent(new ActivateChongwuLogEvent(userRoleId,
				configId, jsonArray));
		return new Object[] { AppErrorCode.SUCCESS, configId };
	}

	public Object[] upgradeJieChongwu(Long userRoleId, Integer configId,
			List<Long> itemIds, Boolean useGold) {
		RoleChongwu chongwu = getChongwu(userRoleId, configId);
		if (chongwu == null) {
			return AppErrorCode.CHONGWU_NOT_ACTIVATED;
		}
		int currentJie = chongwu.getJie();
		int currentCeng = chongwu.getCeng();
		long currentJieExp = chongwu.getJieExp();
		ChongWuShengJieBiaoConfig chongWuShengJieBiaoConfig = chongWuShengJieBiaoConfigExportService
				.loadByIdJieCeng(configId, currentJie, currentCeng);
		if (chongWuShengJieBiaoConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		if (currentCeng >= chongWuShengJieBiaoConfigExportService.getMAX_CENG()
				&& currentJie >= chongWuShengJieBiaoConfigExportService
						.getMAX_JIE()) {
			if ((currentJieExp + chongWuShengJieBiaoConfig.getExpone()) >= chongWuShengJieBiaoConfig
					.getExpmax()) {
				return AppErrorCode.CHONGWU_TOP_LEVEL;
			}
		}
		RoleAccountWrapper account = accountExportService
				.getAccountWrapper(userRoleId);
		int needMoney = chongWuShengJieBiaoConfig.getNeedmoney();
		if (account.getJb() < needMoney) {
			return AppErrorCode.CHONGWU_MONEY_NOT_ENOUGH;
		}
		ChongwuPublicConfig publicConfig = getPublicConfig();
		// 判断道具是否足够
		int hasItemNum = 0;
		JSONArray jsonArray = null;
		for (Long e : itemIds) {
			RoleItemExport roleItemExport = roleBagExportService
					.getBagItemByGuid(userRoleId, e);
			if (roleItemExport == null) {
				return AppErrorCode.CHONGWU_ITEM_NOT_ENOUGH;
			}
			String goodsId = roleItemExport.getGoodsId();
			GoodsConfig goodsConfig = goodsConfigExportService
					.loadById(goodsId);
			if (!goodsConfig.getId1().equals(
					chongWuShengJieBiaoConfig.getMallid())) {
				return AppErrorCode.CHONGWU_ITEM_ERROR;
			}
			hasItemNum = hasItemNum + roleItemExport.getCount();
		}
		int goldConsume = 0;
		int activeateItemNum = chongWuShengJieBiaoConfig.getCount();
		Map<String, Integer> goods = new HashMap<String, Integer>();
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
							userRoleId,
							GoodsSource.GOODS_CHONGWU_ACTIVATE_CONSUME, true,
							true);
					goods.put(roleItemExport.getGoodsId(), count);
					activeateItemNum = activeateItemNum - count;
				}
			}
		} else {
			if (!useGold) {
				return AppErrorCode.CHONGWU_ITEM_NOT_ENOUGH;
			} else {
				int needGold = publicConfig.getItemgold();
				if (needGold * (activeateItemNum - hasItemNum) < account
						.getYb()) {
					// 扣道具
					int tempActiveateItemNum = activeateItemNum;
					for (Long e : itemIds) {
						if (tempActiveateItemNum > 0) {
							RoleItemExport roleItemExport = roleBagExportService
									.getBagItemByGuid(userRoleId, e);
							int count = roleItemExport.getCount() > tempActiveateItemNum ? tempActiveateItemNum
									: roleItemExport.getCount();
							roleBagExportService.removeBagItemByGuid(e, count,
									userRoleId,
									GoodsSource.GOODS_CHONGWU_ACTIVATE_CONSUME,
									true, true);
							goods.put(roleItemExport.getGoodsId(), count);
							tempActiveateItemNum = tempActiveateItemNum - count;
						}
					}
					// 扣元宝
					Object[] result = accountExportService
							.decrCurrencyWithNotify(GoodsCategory.GOLD,
									needGold * (activeateItemNum - hasItemNum),
									userRoleId,
									LogPrintHandle.CONSUME_CHONGWU_UPGRADE,
									true, LogPrintHandle.CBZ_CHONGWU_UPGRADE);
					if (result != null) {
						return result;
					}else{
						if(PlatformConstants.isQQ()){
							BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,needGold * (activeateItemNum - hasItemNum),LogPrintHandle.CONSUME_CHONGWU_UPGRADE,QQXiaoFeiType.CONSUME_CHONGWU_UPGRADE,1});
						}
					}
					goldConsume = needGold * (activeateItemNum - hasItemNum);
				} else {
					return AppErrorCode.CHONGWU_ITEM_NOT_ENOUGH;
				}
			}
		}
		accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY,
				needMoney, userRoleId, LogPrintHandle.CONSUME_CHONGWU_UPGRADE,
				true, LogPrintHandle.CBZ_CHONGWU_UPGRADE);
		int expone = chongWuShengJieBiaoConfig.getExpone();
		long expmax = chongWuShengJieBiaoConfig.getExpmax();
		long nextJieExp = currentJieExp + expone;
		int nextCeng = currentCeng;
		int nextJie = currentJie;
		boolean upgrade = false;
		if (nextJieExp >= expmax) {
			upgrade = true;
			List<ChongWuShengJieBiaoConfig> configList = chongWuShengJieBiaoConfigExportService
					.getAfterConfig(configId, currentJie, currentCeng);
			for (ChongWuShengJieBiaoConfig e : configList) {
				if (e.getExpmax() <= nextJieExp) {
					nextJieExp = nextJieExp - e.getExpmax();
					nextCeng = nextCeng + 1;
					if (nextCeng > chongWuShengJieBiaoConfigExportService
							.getMAX_CENG()) {
						nextJie = nextJie + 1;
						nextCeng = 1;
					}
					if (nextJie > chongWuShengJieBiaoConfigExportService
									.getMAX_JIE()) {
						nextCeng = chongWuShengJieBiaoConfigExportService
								.getMAX_CENG();
						nextJie = chongWuShengJieBiaoConfigExportService
								.getMAX_JIE();
						nextJieExp = 0;
						break;
					}
				} else {
					break;
				}
			}
		}
		chongwu.setJie(nextJie);
		chongwu.setCeng(nextCeng);
		chongwu.setJieExp(nextJieExp);
		roleChongwuDao.cacheUpdate(chongwu, userRoleId);
		if (upgrade) {
			notifyStageChongwuAttrChange(chongwu);
		}
		jsonArray = LogPrintHandle.getLogGoodsParam(goods, null);
		GamePublishEvent.publishEvent(new UpgradeChongwuLogEvent(userRoleId,
				configId, jsonArray, goldConsume, needMoney));
		return new Object[] {
				AppErrorCode.SUCCESS,
				configId,
				chongWuShengJieBiaoConfigExportService.loadByIdJieCeng(
						configId, chongwu.getJie(), chongwu.getCeng()).getId1(),
				nextJieExp };
	}

	public Object[] goFight(Long userRoleId, Integer configId) {
		Long guid = 0L;
		if (configId == 0) {
			RoleChongwu chongwu = getFihgtChongwu(userRoleId);
			if (chongwu != null
					&& chongwu.getStatus().equals(
							ChongwuConstants.CHONGWU_FIGHT_STATUS_YES)) {
				chongwu.setStatus(ChongwuConstants.CHONGWU_FIGHT_STATUS_NO);
				roleChongwuDao.cacheUpdate(chongwu, userRoleId);
			}
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.CHONGWU_SHOW_UPDATE,
					null);
		} else {
			RoleChongwu chongwu = getChongwu(userRoleId, configId);
			if (chongwu == null) {
				return AppErrorCode.CHONGWU_NOT_ACTIVATED;
			}
			guid = chongwu.getId();
			if (chongwu.getStatus().equals(
					ChongwuConstants.CHONGWU_FIGHT_STATUS_YES)) {
				return new Object[] { AppErrorCode.SUCCESS, configId,guid };
			}
			RoleChongwu currentFightChongwu = getFihgtChongwu(userRoleId);
			if (currentFightChongwu != null) {
				currentFightChongwu
						.setStatus(ChongwuConstants.CHONGWU_FIGHT_STATUS_NO);
				roleChongwuDao.cacheUpdate(currentFightChongwu, userRoleId);
			}
			chongwu.setStatus(ChongwuConstants.CHONGWU_FIGHT_STATUS_YES);
			roleChongwuDao.cacheUpdate(chongwu, userRoleId);
			int speed = getPublicConfig().getSpeed();
			String name = chongWuJiHuoBiaoConfigExportService
					.loadById(configId).getName();
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.CHONGWU_SHOW_UPDATE,
					new Object[]{chongwu.getId(),name,configId,speed});
		}
		return new Object[] { AppErrorCode.SUCCESS, configId ,guid};
	}

	public Object[] addChongwuExp(Long userRoleId, Long levelExp) {
		RoleChongwu chongwu = getFihgtChongwu(userRoleId);
		if (chongwu == null) {
			return AppErrorCode.CHONGWU_NO_FIGHT;
		}
		int currentLevel = chongwu.getLevel();
		if (currentLevel >= chongWuShengJiBiaoConfigExportService
				.getMAX_LEVEL()) {
			return AppErrorCode.CHONGWU_TOP_LEVEL;
		}
		long currentLevelExp = chongwu.getLevelExp();
		// 判断是否到达顶级 且经验上限
		int nextLevel = currentLevel;
		long nextLevelExp = currentLevelExp + levelExp;
		ChongWuShengJiBiaoConfig chongWuShengJiBiaoConfig = chongWuShengJiBiaoConfigExportService
				.loadByLevel(currentLevel);
		boolean upgrade = false;
		if (nextLevelExp >= chongWuShengJiBiaoConfig.getCwexp()) {
			upgrade = true;
			List<ChongWuShengJiBiaoConfig> configList = chongWuShengJiBiaoConfigExportService
					.getAfterConfig(currentLevel);
			for (ChongWuShengJiBiaoConfig e : configList) {
				if (e.getCwexp() <= nextLevelExp) {
					nextLevelExp = nextLevelExp - e.getCwexp();
					nextLevel = nextLevel + 1;
					if (nextLevel > chongWuShengJiBiaoConfigExportService
							.getMAX_LEVEL()) {
						nextLevel = chongWuShengJiBiaoConfigExportService
								.getMAX_LEVEL();
						nextLevelExp = 0;
						break;
					}
				} else {
					break;
				}
			}
		}
		chongwu.setLevel(nextLevel);
		chongwu.setLevelExp(nextLevelExp);
		roleChongwuDao.cacheUpdate(chongwu, userRoleId);
		BusMsgSender
				.send2One(userRoleId, ClientCmdType.CHONGWU_EXP_CHANGE,
						new Object[] { chongwu.getConfigId(), nextLevel,
								nextLevelExp });
		if (upgrade) {
			notifyStageChongwuAttrChange(chongwu);
		}
		return null;
	}

	private void notifyStageChongwuAttrChange(RoleChongwu chongwu) {
		Map<String, Long> attr = getChongwuAttribute(chongwu.getConfigId(),
				chongwu.getLevel(), chongwu.getJie(), chongwu.getCeng());
		BusMsgSender.send2One(chongwu.getUserRoleId(),
				ClientCmdType.CHONGWU_ATTR_CHANGE,
				new Object[] { chongwu.getConfigId(), attr });
		BusMsgSender.send2Stage(chongwu.getUserRoleId(),
				InnerCmdType.CHONGWU_ATTR_CHANGE,
				getAllChongwuAttribute(chongwu.getUserRoleId()));
	}

	public Map<String, Long> getChongwuAttribute(Integer configId,
			Integer level, Integer jie, Integer ceng) {
		Map<String, Long> ret = new HashMap<String, Long>();
		ChongWuShengJiBiaoConfig shengjiConfig = chongWuShengJiBiaoConfigExportService
				.loadByLevel(level);
		Map<String, Long> shengjiAttr = shengjiConfig.getAttrs();
		ObjectUtil.longMapAdd(ret, shengjiAttr);
		ChongWuShengJieBiaoConfig shengjieConfig = chongWuShengJieBiaoConfigExportService
				.loadByIdJieCeng(configId, jie, ceng);
		Map<String, Long> shengjieAttr = shengjieConfig.getAttrs();
		ObjectUtil.longMapAdd(ret, shengjieAttr);
		return ret;
	}

	public Map<String, Long> getAllChongwuAttribute(Long userRoleId) {
		List<RoleChongwu> list = roleChongwuDao.cacheLoadAll(userRoleId);
		if (list == null) {
			return null;
		}
		Map<String, Long> ret = new HashMap<String, Long>();
		for (RoleChongwu e : list) {
			Map<String, Long> attr = getChongwuAttribute(e.getConfigId(),
					e.getLevel(), e.getJie(), e.getCeng());
			ObjectUtil.longMapAdd(ret, attr);
		}
		return ret;
	}

	// 获取玩家宠物对象数据
    public RoleChongwu getRoleChongwu(Long userRoleId, Integer chongwuConfigId) {
        return getChongwu(userRoleId, chongwuConfigId);
    }
    
    // 获取宠物激活配置信息
	public ChongWuJiHuoBiaoConfig loadChongwuJiHuoConfigById(Integer configId){
	    return chongWuJiHuoBiaoConfigExportService.loadById(configId);
	}
	
    // -------------------------------宠物技能-------------------------------------//
    /**
     * 获取缓存中玩家宠物技能对象数据
     * 
     * @param userRoleId
     * @return
     */
    private RoleChongwuSkill getCacheRoleChongwuSkill(Long userRoleId, int chongwuId) {
        final int chongwu_id = chongwuId;
        List<RoleChongwuSkill> list = roleChongwuSkillDao.cacheLoadAll(userRoleId, new IQueryFilter<RoleChongwuSkill>() {
            private boolean stop = false;

            @Override
            public boolean check(RoleChongwuSkill entity) {
                return entity != null && entity.getChongwuId().equals(chongwu_id) ? stop = true : false;
            }

            @Override
            public boolean stopped() {
                return stop;
            }
        });
        if (ObjectUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取玩家所有宠物数据集合
     * 
     * @param userRoleId
     * @return
     */
    private List<RoleChongwuSkill> getCacheRoleAllChongwuSkill(Long userRoleId) {
        return roleChongwuSkillDao.cacheLoadAll(userRoleId);
    }

    /**
     * 获取宠物技能基础配置信息
     * 
     * @param skill_id
     * @param skill_level
     * @return
     */
    private ChongWuJiNengBiaoConfig getChongwuSkillConfig(String skill_id, int skill_level) {
        return chongWuJiNengBiaoConfigExportService.loadByIdAndLevel(skill_id, skill_level);
    }

    /**
     * 获取宠物技能的最大等级
     * 
     * @param skill_id
     * @return
     */
    private Integer getChongwuSkillMaxLevelById(String skill_id) {
        return chongWuJiNengBiaoConfigExportService.loadMaxLevelById(skill_id);
    }

    /**
     * 解析宠物技能信息数据数组
     * 
     * @param roleChongwuSkill
     * @param index
     * @return
     */
    private String[] getChongwuSkillInfoByIndex(RoleChongwuSkill roleChongwuSkill, int index) {
        if (null == roleChongwuSkill)
            return null;
        String skill_info = null;
        switch (index) {
        case 1:
            skill_info = roleChongwuSkill.getSkillInfo1();
            break;
        case 2:
            skill_info = roleChongwuSkill.getSkillInfo2();
            break;
        case 3:
            skill_info = roleChongwuSkill.getSkillInfo3();
            break;
        }
        if (ObjectUtil.strIsEmpty(skill_info))
            return null;
        return skill_info.split(ChongwuConstants.SPLIT_CHAR);
    }

    /**
     * 设置指定序号的宠物技能信息
     * 
     * @param roleChongwuSkill
     * @param index 技能序号
     * @param skill_info 更新信息
     */
    private void setChongwuSkillInfoByIndex(RoleChongwuSkill roleChongwuSkill, int index, String skill_info) {
        if (null == roleChongwuSkill)
            return;
        switch (index) {
        case 1:
            roleChongwuSkill.setSkillInfo1(skill_info);
            break;
        case 2:
            roleChongwuSkill.setSkillInfo2(skill_info);
            break;
        case 3:
            roleChongwuSkill.setSkillInfo3(skill_info);
            break;
        }
    }

    /**
     * 创建新的宠物技能数据
     * 
     * @param skill_index
     * @param skill_info
     */
    private void createChongwuSkillByIndex(Long userRoleId, int chongwu_id, int skill_index, String skill_info) {
        long now_time = GameSystemTime.getSystemMillTime();
        RoleChongwuSkill roleChongwuSkill = new RoleChongwuSkill();
        roleChongwuSkill.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
        roleChongwuSkill.setUserRoleId(userRoleId);
        roleChongwuSkill.setChongwuId(chongwu_id);
        setChongwuSkillInfoByIndex(roleChongwuSkill, skill_index, skill_info);
        roleChongwuSkill.setCreateTime(now_time);
        roleChongwuSkill.setUpdateTime(now_time);
        roleChongwuSkillDao.cacheInsert(roleChongwuSkill, userRoleId);
    }

    /**
     * 更新宠物技能数据
     * 
     * @param roleChongwuSkill
     * @param skill_index
     * @param skill_info
     */
    private void updateChongwuSkillByIndex(RoleChongwuSkill roleChongwuSkill, Integer skill_index, String skill_info) {
        if (null == roleChongwuSkill)
            return;
        setChongwuSkillInfoByIndex(roleChongwuSkill, skill_index, skill_info);
        roleChongwuSkill.setUpdateTime(GameSystemTime.getSystemMillTime());
        roleChongwuSkillDao.cacheUpdate(roleChongwuSkill, roleChongwuSkill.getUserRoleId());
    }

    /**
     * 获取宠物配置的技能信息
     * 
     * @param chongwuConfig 宠物
     * @param index 技能索引
     * @return
     */
    private String getChongwuSkillIdByIndex(ChongWuJiHuoBiaoConfig chongwuConfig, int index) {
        if (null == chongwuConfig)
            return null;
        switch (index) {
        case 1:
            return chongwuConfig.getSkill1();
        case 2:
            return chongwuConfig.getSkill2();
        case 3:
            return chongwuConfig.getSkill3();
        }
        return null;
    }

    /**
     * 初始化宠物技能数据到缓存
     * 
     * @param userRoleId
     * @return
     */
    public List<RoleChongwuSkill> initRoleChongwuSkill(Long userRoleId) {
        return roleChongwuSkillDao.initRoleChongwuSkill(userRoleId);
    }

    /**
     * 获取所有宠物技能属性加成集合
     * 
     * @param userRoleId
     * @return
     */
    public Map<String, Long> getRoleAllChongwuSkillAttrs(Long userRoleId) {
        List<RoleChongwuSkill> chongwuSkillList = getCacheRoleAllChongwuSkill(userRoleId);
        if (ObjectUtil.isEmpty(chongwuSkillList)) {
            return null;
        }
        Map<String, Long> attrMap = new HashMap<String, Long>();
        for (RoleChongwuSkill roleChongwuSkill : chongwuSkillList) {
            for (int index = 1; index <= ChongwuConstants.MAX_SKILL_COUNT; index++) {
                String[] skill_array = getChongwuSkillInfoByIndex(roleChongwuSkill, index);
                if (null != skill_array) {
                    ChongWuJiNengBiaoConfig config = getChongwuSkillConfig(skill_array[0], Integer.parseInt(skill_array[1]));
                    if (null != config) {
                        ObjectUtil.longMapAdd(attrMap, config.getAttrMap());
                    }
                }
            }
        }
        return attrMap;
    }

    /**
     * 获取宠物技能数据信息
     * 
     * @param userRoleId
     * @return
     */
    public Object[] getRoleChongwuSkillData(Long userRoleId, int chongwuId) {
        RoleChongwuSkill roleChongwuSkill = getCacheRoleChongwuSkill(userRoleId, chongwuId);
        if (null == roleChongwuSkill)
            return null;
        List<Object[]> dataList = new ArrayList<Object[]>();
        for (int index = 1; index <= ChongwuConstants.MAX_SKILL_COUNT; index++) {
            String[] skill_array = getChongwuSkillInfoByIndex(roleChongwuSkill, index);
            if (null != skill_array) {
                dataList.add(new Object[] { String.valueOf(skill_array[0]), index, Integer.parseInt(skill_array[1]) });
            }
        }
        return dataList.toArray();
    }

    /**
     * 升级宠物技能
     * 
     * @param userRoleId
     * @param chongwu_id 宠物编号
     * @param skill_id 技能编号
     * @param skill_index 技能索引
     * @param auto 是否自动购买材料(true=材料不足自动购买;false=不自动购买)
     * @param busMsgQueue 消息队列
     * @return
     */
    public void uplevelChongwuSkill(Long userRoleId, Integer chongwu_id, String skill_id, Integer skill_index, Boolean auto, BusMsgQueue busMsgQueue) {
        short command = ClientCmdType.CHONGWU_SKILL_UPLEVEL;
        if (null == chongwu_id || ObjectUtil.strIsEmpty(skill_id) || (null == skill_index || skill_index <= 0 || skill_index > ChongwuConstants.MAX_SKILL_COUNT) || null == auto) {
            busMsgQueue.addMsg(userRoleId, command, AppErrorCode.PARAMETER_ERROR);
            return;
        }
        /* 校验宠物是否存在 */
        ChongWuJiHuoBiaoConfig chongWuConfig = chongWuJiHuoBiaoConfigExportService.loadById(chongwu_id);
        if (null == chongWuConfig) {
            busMsgQueue.addMsg(userRoleId, command, AppErrorCode.CONFIG_ERROR);
            return;
        }
        /* 校验宠物是否拥有该技能 */
        String chongwu_skill_id = getChongwuSkillIdByIndex(chongWuConfig, skill_index);
        if (null == chongwu_skill_id || !skill_id.equals(chongwu_skill_id)) {
            busMsgQueue.addMsg(userRoleId, command, AppErrorCode.CHONGWU_SKILL_ID_ERROR);
            return;
        }
        /* 校验宠物激活 */
        RoleChongwu roleChongwu = getChongwu(userRoleId, chongwu_id);
        if (null == roleChongwu) {
            busMsgQueue.addMsg(userRoleId, command, AppErrorCode.CHONGWU_NOT_ACTIVATED);
            return;
        }
        int skill_level = 0;
        RoleChongwuSkill roleChongwuSkill = getCacheRoleChongwuSkill(userRoleId, chongwu_id);
        if (null != roleChongwuSkill) {
            String[] skill_array = getChongwuSkillInfoByIndex(roleChongwuSkill, skill_index);
            if (null != skill_array) {
                if (!skill_id.equals(skill_array[0])) {
                    busMsgQueue.addMsg(userRoleId, command, AppErrorCode.CHONGWU_SKILL_ID_ERROR);
                    return;
                }
                skill_level = Integer.parseInt(skill_array[1]);
            }
        }
        /* 校验技能最大等级 */
        Integer max_level = getChongwuSkillMaxLevelById(skill_id);
        if (null == max_level) {
            busMsgQueue.addMsg(userRoleId, command, AppErrorCode.CONFIG_ERROR);
            return;
        }
        if (skill_level >= max_level) {
            busMsgQueue.addMsg(userRoleId, command, AppErrorCode.CHONGWU_SKILL_MAX_LEVEL);
            return;
        }
        /* 校验技能配置 */
        ChongWuJiNengBiaoConfig config = getChongwuSkillConfig(skill_id, skill_level);
        if (null == config) {
            busMsgQueue.addMsg(userRoleId, command, AppErrorCode.CONFIG_ERROR);
            return;
        }
        /* 检验银两消耗 */
        int money = config.getNeedMoney();
        if (money > 0) {
            Object[] moneyError = roleBagExportService.isEnought(GoodsCategory.MONEY, money, userRoleId);
            if (null != moneyError) {
                busMsgQueue.addMsg(userRoleId, command, AppErrorCode.JB_ERROR);
                return;
            }
        }
        /* 检验升级材料消耗 */
        int needCount = config.getItemNum();
        List<String> needGoodIds = BusConfigureHelper.getGoodsConfigExportService().loadIdsById1(config.getItemId());
        if (needCount < 0 || ObjectUtil.isEmpty(needGoodIds)) {
            busMsgQueue.addMsg(userRoleId, command, AppErrorCode.CONFIG_ERROR);
            return;
        }
        Map<String, Integer> resourcesMap = new HashMap<String, Integer>();// 消耗的道具资源
        for (String goodsId : needGoodIds) {
            int owerCount = roleBagExportService.getBagItemCountByGoodsId(goodsId, userRoleId);
            if (owerCount >= needCount) {
                resourcesMap.put(goodsId, needCount);
                needCount = 0;
                break;
            } else if (owerCount > 0) {
                resourcesMap.put(goodsId, resourcesMap.get(goodsId) == null ? owerCount : (owerCount + resourcesMap.get(goodsId)));
                needCount -= owerCount;
            }
        }
        int autoGold = 0;// 自动购买消耗的元宝
        int autoBgold = 0;// 自动购买消耗的绑定元宝
        if (needCount > 0) {// 消耗材料不足
            if (!auto) {
                busMsgQueue.addMsg(userRoleId, command, AppErrorCode.CHONGWU_ITEM_NOT_ENOUGH);
                return;
            }
            // 自动购买,优先消耗绑定元宝
            int itemBgold = config.getItemBgold();// 绑定元宝单价
            if (itemBgold < 0) {
                busMsgQueue.addMsg(userRoleId, command, AppErrorCode.CONFIG_ERROR);
                return;
            }
            int bCount = 0;// 绑定元宝消耗的材料数量
            while (needCount > 0) {
                Object[] bgoldError = roleBagExportService.isEnought(GoodsCategory.BGOLD, itemBgold * (bCount + 1), userRoleId);
                if (null != bgoldError) {
                    break;
                }
                --needCount;
                ++bCount;
            }
            autoBgold = itemBgold * bCount;
            // 自动购买,消耗元宝
            if (needCount > 0) {
                int itemGold = config.getItemGold();// 元宝单价
                if (itemGold < 0) {
                    busMsgQueue.addMsg(userRoleId, command, AppErrorCode.CONFIG_ERROR);
                    return;
                }
                Object[] goldError = roleBagExportService.isEnought(GoodsCategory.GOLD, itemGold * needCount, userRoleId);
                if (null != goldError) {
                    busMsgQueue.addMsg(userRoleId, command, AppErrorCode.YB_ERROR);
                    return;
                }
                autoGold = itemGold * needCount;
                needCount = 0;
            }
        }
        // 扣除银两
        if (money > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.CONSUME_CHONGWU_SKILL_SHENGJI, true, LogPrintHandle.CBZ_CHONGWU_SKILL_SHENGJI);
        }
        // 扣除绑定元宝
        if (autoBgold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, autoBgold, userRoleId, LogPrintHandle.CONSUME_CHONGWU_SKILL_SHENGJI, true, LogPrintHandle.CBZ_CHONGWU_SKILL_SHENGJI);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                busMsgQueue.addBusMsg(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_BYB, autoBgold, String.valueOf(LogPrintHandle.CONSUME_CHONGWU_SKILL_SHENGJI), QQXiaoFeiType.CONSUME_CHONGWU_SKILL_SHENGJI, 1 });
            }
        }
        // 扣除元宝
        if (autoGold > 0) {
            roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, autoGold, userRoleId, LogPrintHandle.CONSUME_CHONGWU_SKILL_SHENGJI, true, LogPrintHandle.CBZ_CHONGWU_SKILL_SHENGJI);
            // 腾讯OSS消费上报
            if (PlatformConstants.isQQ()) {
                busMsgQueue.addBusMsg(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, autoGold, String.valueOf(LogPrintHandle.CONSUME_CHONGWU_SKILL_SHENGJI), QQXiaoFeiType.CONSUME_CHONGWU_SKILL_SHENGJI, 1 });
            }
        }
        if (!ObjectUtil.isEmpty(resourcesMap)) {
            // 扣除道具
            BagSlots errorCode = roleBagExportService.removeBagItemByGoods(resourcesMap, userRoleId, GoodsSource.CHONGWU_SKILL_SHENGJI, true, true);
            if (!errorCode.isSuccee()) {
                busMsgQueue.addMsg(userRoleId, command, errorCode.getErrorCode());
                return;
            }
        }
        /* 更新数据 */
        String skill_info = new StringBuilder().append(skill_id).append(ChongwuConstants.MERGE_CHAR).append(++skill_level).toString();
        if (null != roleChongwuSkill) {
            updateChongwuSkillByIndex(roleChongwuSkill, skill_index, skill_info);
        } else {
            createChongwuSkillByIndex(userRoleId, chongwu_id, skill_index, skill_info);
        }
        /* 通知场景宠物技能属性变化 */
        busMsgQueue.addStageMsg(userRoleId, InnerCmdType.CHONGWU_Skill_ATTR_CHANGE, getRoleAllChongwuSkillAttrs(userRoleId));
        /* 日志记录 */
        RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
        GamePublishEvent.publishEvent(new ChongwuSkillLogEvent(userRoleId, roleWrapper.getName(), chongwu_id, skill_id, skill_index, skill_level, LogPrintHandle.getLogGoodsParam(resourcesMap, null), money, autoGold, autoBgold));
        busMsgQueue.addMsg(userRoleId, command, new Object[] { AppErrorCode.SUCCESS, chongwu_id, skill_id, skill_index, skill_level });
    }


}
