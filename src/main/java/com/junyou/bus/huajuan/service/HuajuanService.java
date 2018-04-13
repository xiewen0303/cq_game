package com.junyou.bus.huajuan.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.entity.RoleItem;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.huajuan.configure.HuaJuanBiaoConfig;
import com.junyou.bus.huajuan.configure.HuaJuanBiaoConfigExportService;
import com.junyou.bus.huajuan.configure.HuaJuanDengJiConfig;
import com.junyou.bus.huajuan.configure.HuaJuanDengJiConfigExportService;
import com.junyou.bus.huajuan.configure.HuaJuanHeChengConfig;
import com.junyou.bus.huajuan.configure.HuaJuanHeChengConfigExportService;
import com.junyou.bus.huajuan.configure.HuaJuanShengJiBiaoConfig;
import com.junyou.bus.huajuan.configure.HuaJuanShengJiBiaoConfigExportService;
import com.junyou.bus.huajuan.configure.HuaJuanShouJiConfig;
import com.junyou.bus.huajuan.configure.HuaJuanShouJiConfigExportService;
import com.junyou.bus.huajuan.configure.HuaJuanZhuanShuBiaoConfig;
import com.junyou.bus.huajuan.configure.HuaJuanZhuanShuBiaoConfigExportService;
import com.junyou.bus.huajuan.constants.HuajuanConstants;
import com.junyou.bus.huajuan.dao.RoleHuajuanDao;
import com.junyou.bus.huajuan.dao.RoleHuajuanExpDao;
import com.junyou.bus.huajuan.entity.RoleHuajuan;
import com.junyou.bus.huajuan.entity.RoleHuajuanExp;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.HuajuanFenjieLogEvent;
import com.junyou.event.HuajuanHechengLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.HuajuanPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class HuajuanService {
	@Autowired
	private RoleHuajuanDao roleHuajuanDao;
	@Autowired
	private RoleHuajuanExpDao roleHuajuanExpDao;
	@Autowired
	private HuaJuanBiaoConfigExportService huaJuanBiaoConfigExportService;
	@Autowired
	private HuaJuanDengJiConfigExportService huaJuanDengJiConfigExportService;
	@Autowired
	private HuaJuanHeChengConfigExportService huaJuanHeChengConfigExportService;
	@Autowired
	private HuaJuanShouJiConfigExportService huaJuanShouJiConfigExportService;
	@Autowired
	private HuaJuanZhuanShuBiaoConfigExportService huaJuanZhuanShuBiaoConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private HuaJuanShengJiBiaoConfigExportService huaJuanShengJiBiaoConfigExportService;
	/**
	 * 获取公共数据配置
	 * 
	 * @return
	 */
	private HuajuanPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_HUAJUAN);
	}

	public void onlineHandle(Long userRoleId) {

	}

	public List<RoleHuajuan> initRoleHuajuan(Long userRoleId) {
		return roleHuajuanDao.initRoleHuajuan(userRoleId);
	}

	public RoleHuajuan createRoleHuajuan(Long userRoleId, Integer huajuanId) {
		RoleHuajuan roleHuajuan = new RoleHuajuan();
		roleHuajuan.setId(IdFactory.getInstance().generateId(
				ServerIdType.COMMON));
		roleHuajuan.setHuanjuanId(huajuanId);
		roleHuajuan.setUserRoleId(userRoleId);
		roleHuajuan.setIsUp(HuajuanConstants.HUAJUAN_DOWN);
		roleHuajuan.setCreateTime(GameSystemTime.getSystemMillTime());
		roleHuajuan.setLevelId(0);
		roleHuajuan.setExp(0);
		roleHuajuan.setSjUpdateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		roleHuajuanDao.cacheInsert(roleHuajuan, userRoleId);
		return roleHuajuan;
	}

	public RoleHuajuan getRoleHuajuan(Long userRoleId, final Integer huajuanId) {
		List<RoleHuajuan> list = roleHuajuanDao.cacheLoadAll(userRoleId,
				new IQueryFilter<RoleHuajuan>() {
					private boolean stop = false;

					@Override
					public boolean check(RoleHuajuan roleHuajuan) {
						if (roleHuajuan.getHuanjuanId().equals(huajuanId)) {
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

	public List<RoleHuajuanExp> initRoleHuajuanExp(Long userRoleId) {
		return roleHuajuanExpDao.initRoleHuajuanExp(userRoleId);
	}

	public RoleHuajuanExp createRoleHuajuanExp(Long userRoleId) {
		RoleHuajuanExp roleHuajuanExp = new RoleHuajuanExp();
		roleHuajuanExp.setUserRoleId(userRoleId);
		roleHuajuanExp.setExp(0);
		roleHuajuanExp.setKucunExp(0);
		roleHuajuanExp.setCreateTime(GameSystemTime.getSystemMillTime());
		roleHuajuanExp.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleHuajuanExpDao.cacheInsert(roleHuajuanExp, userRoleId);
		return roleHuajuanExp;
	}

	public RoleHuajuanExp getRoleHuajuanExp(Long userRoleId) {
		return roleHuajuanExpDao.cacheLoad(userRoleId, userRoleId);
	}

	public Map<String, Long> getHuajuanAttr(Long userRoleId) {
		Map<String, Long> ret = new HashMap<String, Long>();
		List<RoleHuajuan> huajuanList = roleHuajuanDao.cacheLoadAll(userRoleId);
		if (huajuanList == null || huajuanList.size() == 0) {
			return null;
		}
		Map<Integer, Map<Integer, Integer>> shoujiAttrMap = new HashMap<Integer, Map<Integer, Integer>>();
		for (RoleHuajuan e : huajuanList) {
			int huajuanId = e.getHuanjuanId();
			HuaJuanBiaoConfig huaJuanBiaoConfig = huaJuanBiaoConfigExportService
					.loadById(huajuanId);
			if (e.getIsUp() == HuajuanConstants.HUAJUAN_UP) {
				// 装备属性
				ObjectUtil.longMapAdd(ret, huaJuanBiaoConfig.getAttrs());
			}
			int exclusive = huaJuanBiaoConfig.getExclusive();
			if (exclusive != 0) {
				HuaJuanZhuanShuBiaoConfig huaJuanZhuanShuBiaoConfig = huaJuanZhuanShuBiaoConfigExportService
						.loadById(exclusive);
				if (huaJuanZhuanShuBiaoConfig != null) {
					// 专属属性
					ObjectUtil.longMapAdd(ret,huaJuanZhuanShuBiaoConfig.getAttrs());
				}
			}
			//等级属性
			HuaJuanShengJiBiaoConfig config = huaJuanShengJiBiaoConfigExportService.loadById(e.getLevelId());
			if(config != null){
				ObjectUtil.longMapAdd(ret,config.getAttrs());
			}
			Integer liebiaoId = huaJuanBiaoConfig.getLiebiaoid();
			Integer type = huaJuanBiaoConfig.getType();
			Map<Integer, Integer> liebiaoMap = shoujiAttrMap.get(liebiaoId);
			if (liebiaoMap == null) {
				liebiaoMap = new HashMap<Integer, Integer>();
				shoujiAttrMap.put(liebiaoId, liebiaoMap);
			}
			Integer num = liebiaoMap.get(type);
			if (num == null) {
				liebiaoMap.put(type, 1);
			} else {
				liebiaoMap.put(type, num + 1);
			}
		}
		for (Integer liebiaoId : shoujiAttrMap.keySet()) {
			Map<Integer, Integer> typeMap = shoujiAttrMap.get(liebiaoId);
			for (Integer type : typeMap.keySet()) {
				Integer num = typeMap.get(type);
				HuaJuanShouJiConfig huaJuanShouJiConfig = huaJuanShouJiConfigExportService
						.get(liebiaoId, type, num);
				if (huaJuanShouJiConfig != null) {
					// 收集属性
					ObjectUtil.longMapAdd(ret, huaJuanShouJiConfig.getAttrs());
				}
			}
		}
		RoleHuajuanExp roleHuajuanExp = getRoleHuajuanExp(userRoleId);
		if (roleHuajuanExp != null) {
			int level = huaJuanDengJiConfigExportService
					.calcLevel(roleHuajuanExp.getExp());
			Long speed = ret.remove(EffectType.x19.name());
			HuaJuanDengJiConfig dengjiConfig = huaJuanDengJiConfigExportService
					.loadByLevel(level);
			// 等级 比例加成
			ObjectUtil.longMapTimes(ret,
					(dengjiConfig.getPercentage() + 10000) / 10000f);
			if (speed != null) {
				ret.put(EffectType.x19.name(), speed);
			}
		}
		return ret;
	}

	public void noticeStageAttrChange(Long userRoleId) {
		Map<String, Long> attr = getHuajuanAttr(userRoleId);
		// 推送内部场景 属性变化
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.HUAJUAN_ATTR_CHANGE,
				attr);
	}

	public Object[] hecheng(Long userRoleId, Integer id, Integer num) {
		if (num == null || num <= 0) {
			return AppErrorCode.PARAMETER_ERROR;
		}
		HuaJuanHeChengConfig config = huaJuanHeChengConfigExportService
				.loadById(id);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		//升级需要消耗的银两
		int yl = config.getNeedmoney()*num;
		Object[] isOb=accountExportService.isEnought(GoodsCategory.MONEY,yl, userRoleId);
		if(null != isOb){
			return AppErrorCode.JB_ERROR;
		}
		Map<String, Integer> itemMap = new HashMap<>();
		itemMap.put(config.getItem(), num);
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(
				itemMap, userRoleId);
		// 背包空间不足 请先清理背包
		if (code != null) {
			return code;
		}
		for (String goodId : config.getNeeditem().keySet()) {
			List<RoleItem> roleItem = roleBagExportService.getBagItemByGoodId(
					goodId, userRoleId);
			if (roleItem == null) {
				return AppErrorCode.HUAJUAN_ITEM_NOT_ENOUGH;
			} else {
				int count = 0;
				for (RoleItem e : roleItem) {
					count = e.getCount() + count;
				}
				if (count < config.getNeeditem().get(goodId)*num) {
					return AppErrorCode.HUAJUAN_ITEM_NOT_ENOUGH;
				}
			}
		}
		// 扣除金币
		if( yl>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, yl, userRoleId,  LogPrintHandle.CONSUME_HUAJUAN_HECHENG, true, LogPrintHandle.CBZ_HUAJUAN_HECHENG);
		}
		for (String goodId : config.getNeeditem().keySet()) {
			roleBagExportService.removeBagItemByGoodsId(goodId, config
					.getNeeditem().get(goodId)*num, userRoleId,
					GoodsSource.HECHENG_HUAJUAN, true, true);
		}
		roleBagExportService.putInBag(itemMap, userRoleId,
				GoodsSource.HUAJUAN_HECHENG_GET, true);
		Map<String, Integer> consumeMap = new HashMap<String,Integer>();
		for(String e:config.getNeeditem().keySet()){
			consumeMap.put(e, config.getNeeditem().get(e)*num);
		}
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(
				consumeMap, null);
		GamePublishEvent.publishEvent(new HuajuanHechengLogEvent(userRoleId,
				jsonArray, config.getItem(), num));
		return new Object[] { AppErrorCode.SUCCESS, config.getItem(), num };
	}

	public Object[] getHuajuanInfo(Long userRoleId) {
		int exp = 0;
		int level = huaJuanDengJiConfigExportService.getMinLevel();
		RoleHuajuanExp roleHuajuanExp = getRoleHuajuanExp(userRoleId);
		if (roleHuajuanExp != null) {
			exp = roleHuajuanExp.getExp();
			level = huaJuanDengJiConfigExportService.calcLevel(exp);
		}
		List<RoleHuajuan> huajuanList = roleHuajuanDao.cacheLoadAll(userRoleId);
		Object[] vo = toVO(huajuanList);
		Map<String, Long> attr = getHuajuanAttr(userRoleId);
		BusMsgSender.send2One(userRoleId, ClientCmdType.HUAJUAN_ATTR, attr);
		return new Object[] { level, exp, vo };
	}

	public Object[] toVO(List<RoleHuajuan> huajuanList) {
		if (huajuanList == null || huajuanList.size() == 0) {
			return null;
		}
		Object[] ret = new Object[huajuanList.size()];
		for (int i = 0; i < huajuanList.size(); i++) {
			RoleHuajuan huajuan = huajuanList.get(i);
			Object[] info = new Object[6];
			info[0] = huajuan.getHuanjuanId();
			info[1] = huajuan.getId();
			info[2] = huajuan.getIsUp() == HuajuanConstants.HUAJUAN_UP;
			info[3] = null;// 随机属性 TODO
			info[4] = huajuan.getLevelId();
			info[5] = huajuan.getExp();
			ret[i] = info;
		}
		return ret;
	}

	public Object[] up(Long userRoleId, Long huajuanId) {
		RoleHuajuan roleHuajuan = roleHuajuanDao.cacheLoad(huajuanId,
				userRoleId);
		if (roleHuajuan == null) {
			return AppErrorCode.HUAJUAN_NOT_EXIT;
		}
		List<RoleHuajuan> list = roleHuajuanDao.cacheLoadAll(userRoleId);
		int equipCount = 0;
		if (list != null && list.size() > 0) {
			for (RoleHuajuan e : list) {
				if (e.getIsUp() == HuajuanConstants.HUAJUAN_UP) {
					equipCount++;
				}
			}
		}
		if (getPublicConfig().getNum() < equipCount) {
			return AppErrorCode.HUAJUAN_EQUIP_NUM_LIMIT;
		}
		RoleHuajuan minZplusHuajuan = null;
		if (getPublicConfig().getNum() == equipCount) {
			long minZplus = Long.MAX_VALUE;
			for (RoleHuajuan e : list) {
				if (e.getIsUp() == HuajuanConstants.HUAJUAN_UP) {
					HuaJuanBiaoConfig huajuanConfig = huaJuanBiaoConfigExportService
							.loadById(e.getHuanjuanId());
					if (huajuanConfig.getAttrs().get("zplus") < minZplus) {
						minZplus = huajuanConfig.getAttrs().get("zplus");
						minZplusHuajuan = e;
					}
				}
			}

		}
		if (minZplusHuajuan != null) {
			minZplusHuajuan.setIsUp(HuajuanConstants.HUAJUAN_DOWN);
			roleHuajuanDao.cacheUpdate(minZplusHuajuan, userRoleId);
			BusMsgSender.send2One(
					userRoleId,
					ClientCmdType.HUAJUAN_DOWN,
					new Object[] { AppErrorCode.SUCCESS,
							minZplusHuajuan.getId() });
			ChuanQiLog.info("userRoleId={} down huajuan={}", userRoleId,
					minZplusHuajuan.getId());
		}
		roleHuajuan.setIsUp(HuajuanConstants.HUAJUAN_UP);
		roleHuajuanDao.cacheUpdate(roleHuajuan, userRoleId);
		ChuanQiLog.info("userRoleId={} up huajuan={}", userRoleId, huajuanId);
		noticeStageAttrChange(userRoleId);
		return new Object[] { AppErrorCode.SUCCESS, huajuanId };
	}

	public Object[] down(Long userRoleId, Long huajuanId) {
		RoleHuajuan roleHuajuan = roleHuajuanDao.cacheLoad(huajuanId,
				userRoleId);
		if (roleHuajuan == null) {
			return AppErrorCode.HUAJUAN_NOT_EXIT;
		}
		roleHuajuan.setIsUp(HuajuanConstants.HUAJUAN_DOWN);
		roleHuajuanDao.cacheUpdate(roleHuajuan, userRoleId);
		ChuanQiLog.info("userRoleId={} down huajuan={}", userRoleId, huajuanId);
		noticeStageAttrChange(userRoleId);
		return new Object[] { AppErrorCode.SUCCESS, huajuanId };
	}

	public Object[] fenjie(Long userRoleId, List<Long> itemList) {
		if (itemList == null || itemList.size() == 0) {
			return AppErrorCode.PARAMETER_ERROR;
		}
		int beforeExp = 0;
		RoleHuajuanExp roleHuajuanExp = getRoleHuajuanExp(userRoleId);
		if (roleHuajuanExp == null) {
			roleHuajuanExp = createRoleHuajuanExp(userRoleId);
		} else {
			beforeExp = roleHuajuanExp.getExp();
		}
		int maxExp = huaJuanDengJiConfigExportService.getMaxExp();
		if (beforeExp >= maxExp) {
			return AppErrorCode.HUAJUAN_LEVEL_TOP;
		}
		int fenjieValue = 0;
		Map<Long, Integer> consumeMap = new HashMap<Long, Integer>();
		Map<String, Integer> consumeItemMap = new HashMap<String, Integer>();
		for (Long e : itemList) {
			RoleItemExport roleItem = roleBagExportService.getBagItemByGuid(
					userRoleId, e);
			if (roleItem == null) {
				return AppErrorCode.PARAMETER_ERROR;
			}
			String goodsId = roleItem.getGoodsId();
			GoodsConfig config = goodsConfigExportService.loadById(goodsId);
			if (config == null) {
				return AppErrorCode.PARAMETER_ERROR;
			} else {
				if (config.getCategory() != GoodsCategory.HUAJUAN) {
					return AppErrorCode.PARAMETER_ERROR;
				}
			}
			boolean full = false;
			int count = roleItem.getCount();
			int consumeCount = 0;
			for (int i = 0; i < count; i++) {
				consumeCount++;
				fenjieValue = fenjieValue + config.getData1();
				if ((beforeExp + fenjieValue) >= maxExp) {
					full = true;
					break;
				}
			}
			consumeMap.put(e, consumeCount);
			consumeItemMap.put(goodsId, consumeCount);
			if (full) {
				break;
			}
		}
		if (fenjieValue <= 0) {
			return AppErrorCode.PARAMETER_ERROR;
		}
		for (Long guid : consumeMap.keySet()) {
			Integer consumeCount = consumeMap.get(guid);
			if (consumeCount > 0) {
				BagSlots bagSlots = roleBagExportService.removeBagItemByGuid(
						guid, consumeCount, userRoleId,
						GoodsSource.HUAJUAN_FENJIE, true, true);
				if (!bagSlots.isSuccee()) {
					return bagSlots.getErrorCode();
				}
			}
		}
		int afterExp = beforeExp + fenjieValue;

		roleHuajuanExp.setExp(afterExp);
		roleHuajuanExp.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleHuajuanExpDao.cacheUpdate(roleHuajuanExp, userRoleId);

		int beforeLevel = huaJuanDengJiConfigExportService.calcLevel(beforeExp);
		int afterLevel = huaJuanDengJiConfigExportService.calcLevel(afterExp);
		boolean levelUp = afterLevel > beforeLevel;

		ChuanQiLog.info("userRoleId={} fenjie exp from={} to={} levelUp={}",
				userRoleId, beforeExp, afterExp, levelUp);
		if (levelUp) {
			noticeStageAttrChange(userRoleId);
		}
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(consumeItemMap,
				null);
		GamePublishEvent.publishEvent(new HuajuanFenjieLogEvent(userRoleId,
				jsonArray, beforeExp, beforeLevel, afterExp, afterLevel));
		return new Object[] { AppErrorCode.SUCCESS, afterLevel, afterExp };
	}

	public Object[] activateHuajuan(Long userRoleId, Integer huajuanId) {
		RoleHuajuan roleHuajuan = getRoleHuajuan(userRoleId, huajuanId);
		if (roleHuajuan != null) {
			return AppErrorCode.HUAJUAN_ALREADY_HAS;
		}
		createRoleHuajuan(userRoleId, huajuanId);
		ChuanQiLog.info("userRoleId={} get huajuan={}", userRoleId, huajuanId);
		noticeStageAttrChange(userRoleId);
		return null;
	}
	
	public Object[] sjHuaJuan(Long userRoleId, Long huajuanId,List<Long> itemIds) {
		//RoleHuajuan roleHuajuan = getRoleHuajuan(userRoleId, huajuanId);
		RoleHuajuan roleHuajuan = roleHuajuanDao.cacheLoad(huajuanId,userRoleId);
		if (roleHuajuan == null) {
			return AppErrorCode.HUAJUAN_WEIJIHUO_NO_SHENGJI;
		}
		RoleHuajuanExp roleHuajuanExp = getRoleHuajuanExp(userRoleId);
		if (roleHuajuanExp == null) {
			roleHuajuanExp = createRoleHuajuanExp(userRoleId);
		}
		boolean isNoticeAttrChange = false;
		int jingyanzhi = 0;
		Map<Long, Integer> cuilianGoods = new HashMap<Long, Integer>();
		Map<String, Integer> goodIdMap = new HashMap<String, Integer>();
		if(itemIds != null && itemIds.size() > 0){
			for (Long e : itemIds) {
				RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
				if (roleItemExport == null) {
					return AppErrorCode.ITEM_NOT_ENOUGH;
				}
				String goodsId = roleItemExport.getGoodsId();
				GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
				if (goodsConfig.getCuilian() <= 0) {
					// 不能淬炼,移除
					return AppErrorCode.HUAJUAN_ITEM_NO_SHENGJI;
				}
				goodIdMap.put(roleItemExport.getGoodsId(), roleItemExport.getCount());
				cuilianGoods.put(roleItemExport.getGuid(), roleItemExport.getCount()); // 需要淬炼的道具

				jingyanzhi += goodsConfig.getCuilian() * roleItemExport.getCount();
			}
		}
		HuaJuanShengJiBiaoConfig sjConfig = huaJuanShengJiBiaoConfigExportService.loadById(roleHuajuan.getLevelId());
		if(sjConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(sjConfig.getNeedexp() == null || sjConfig.getNeedexp() == 0){
			return AppErrorCode.HUAJUAN_SHENGJI_DAODING;
		}
		jingyanzhi = jingyanzhi + roleHuajuanExp.getKucunExp();
		roleHuajuanExp.setKucunExp(0);
		if(jingyanzhi > 0){
			roleHuajuan.setExp(roleHuajuan.getExp() + jingyanzhi);
			for (int i = 0; i < 100; i++) {
				if(roleHuajuan.getExp() > sjConfig.getNeedexp()){
					roleHuajuan.setLevelId(roleHuajuan.getLevelId() + 1);
					roleHuajuan.setExp(roleHuajuan.getExp() - sjConfig.getNeedexp());
					roleHuajuan.setSjUpdateTime(new Timestamp(System.currentTimeMillis()));
					isNoticeAttrChange = true;//ID变化，需要同步属性
				}else{
					break;
				}
				sjConfig = huaJuanShengJiBiaoConfigExportService.loadById(roleHuajuan.getLevelId());
				if(sjConfig == null){
					return AppErrorCode.CONFIG_ERROR;
				}
				if(sjConfig.getNeedexp() == null || sjConfig.getNeedexp() == 0){
					roleHuajuanExp.setKucunExp(roleHuajuan.getExp());
					roleHuajuan.setExp(0);//到顶级设置经验为0
					break;
				}
			}
		}
		// 扣道具
		for (Entry<Long, Integer> item : cuilianGoods.entrySet()) {
			roleBagExportService.removeBagItemByGuid(item.getKey(), item.getValue(), userRoleId, GoodsSource.TONGTIAN_ROAD_CUILIAN, true, true);
		}
		//更新数据
		roleHuajuanDao.cacheUpdate(roleHuajuan, userRoleId);
		roleHuajuanExpDao.cacheUpdate(roleHuajuanExp, userRoleId);
		BusMsgSender.send2One(userRoleId,ClientCmdType.TUISONG_HUAJUAN_KUCUN_EXP,roleHuajuanExp.getKucunExp());
		//属性变化
		if (isNoticeAttrChange) {
			noticeStageAttrChange(userRoleId);
		}
		// 日志
		/*JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(goodIdMap, null);
		GamePublishEvent.publishEvent(new TongtianRoadCuilianLogEvent(userRoleId, jsonArray, entity.getClientMap(), jingyanzhi,totalCuilian,consumeCuilian));
			*/	 
		return new Object[]{1,huajuanId,roleHuajuan.getLevelId(),roleHuajuan.getExp()};
	}
	
	public Integer getKuCunExp(Long userRoleId){
		RoleHuajuanExp roleHuajuanExp = getRoleHuajuanExp(userRoleId);
		if (roleHuajuanExp == null) {
			roleHuajuanExp = createRoleHuajuanExp(userRoleId);
		}
		
		return roleHuajuanExp.getKucunExp();
	}
}
