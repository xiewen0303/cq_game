package com.junyou.bus.shizhuang.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.shizhuang.configure.export.ShiZhuangConfigExportService;
import com.junyou.bus.shizhuang.configure.export.ShiZhuangDuiYingBiaoConfig;
import com.junyou.bus.shizhuang.configure.export.ShiZhuangDuiYingBiaoConfigExportService;
import com.junyou.bus.shizhuang.configure.export.ShiZhuangJieDuanConfig;
import com.junyou.bus.shizhuang.configure.export.ShiZhuangJieDuanConfigExportService;
import com.junyou.bus.shizhuang.configure.export.ShiZhuangJinJieConfig;
import com.junyou.bus.shizhuang.configure.export.ShiZhuangJinJieConfigExportService;
import com.junyou.bus.shizhuang.configure.export.ShiZhuangLevelConfigExportService;
import com.junyou.bus.shizhuang.dao.RoleShizhuangDao;
import com.junyou.bus.shizhuang.dao.RoleShizhuangJinJieDao;
import com.junyou.bus.shizhuang.entity.RoleShiZhuangJinJie;
import com.junyou.bus.shizhuang.entity.RoleShizhuang;
import com.junyou.bus.shizhuang.entity.ShiZhuangConfig;
import com.junyou.bus.shizhuang.entity.ShiZhuangLevelConfig;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.gameconfig.export.JueSeDuiZhaoBiaoConfigExportService;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

/**
 * @author LiuYu
 * 2015-8-7 下午1:46:35
 */
@Service
public class RoleShiZhuangService {

	@Autowired
	private RoleShizhuangDao roleShizhuangDao;
	@Autowired
	private ShiZhuangConfigExportService shiZhuangConfigExportService;
	@Autowired
	private ShiZhuangLevelConfigExportService shiZhuangLevelConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private JueSeDuiZhaoBiaoConfigExportService jueSeDuiZhaoBiaoConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private BusScheduleExportService busScheduleExportService;
	@Autowired
	private ShiZhuangDuiYingBiaoConfigExportService shiZhuangDuiYingBiaoConfigExportService;
	@Autowired
	private RoleShizhuangJinJieDao roleShizhuangJinJieDao;
	@Autowired
	private ShiZhuangJinJieConfigExportService shiZhuangJinJieConfigExportService;
	@Autowired
	private ShiZhuangJieDuanConfigExportService shiZhuangJieDuanConfigExportService;
	
	public List<RoleShizhuang> initRoleShizhuang(Long userRoleId) {
		return roleShizhuangDao.initRoleShizhuang(userRoleId);
	}
	
	public List<RoleShiZhuangJinJie> initRoleShizhuangJinJie(Long userRoleId) {
		return roleShizhuangJinJieDao.initRoleShizhuang(userRoleId);
	}
	
	public void onlineHandle(Long userRoleId){
		Long nextTime = Long.MAX_VALUE;
		List<RoleShizhuang> list = roleShizhuangDao.cacheLoadAll(userRoleId);
		for (RoleShizhuang roleShizhuang : list) {
			if(roleShizhuang.getExpireTime() > 0 && roleShizhuang.getExpireTime() < nextTime){
				nextTime = roleShizhuang.getExpireTime();
			}
		}
		if(nextTime < Long.MAX_VALUE){
			Long schedule = nextTime - GameSystemTime.getSystemMillTime();
			if(schedule > 0){
				schedule = schedule / 1000 + 20;
				BusTokenRunable runable = new BusTokenRunable(userRoleId, InnerCmdType.SHIZHUANG_EXPIRE,null);
				busScheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPONENT_SHIZHUANG, runable, schedule.intValue(), TimeUnit.SECONDS);
			}
		}
	}
	
	private RoleShizhuang getRoleShizhuang(Long userRoleId,Integer id){
		RoleShizhuang roleShizhuang = null;
		final Integer szId = id;
		List<RoleShizhuang> list = roleShizhuangDao.cacheLoadAll(userRoleId,new IQueryFilter<RoleShizhuang>(){
			boolean stop;
			@Override
			public boolean check(RoleShizhuang entity) {
				if(entity.getShizhuangId().equals(szId)){
					stop = true;
				}
				return stop;
			}

			@Override
			public boolean stopped() {
				return stop;
			}
		});
		if(list != null && list.size() > 0){
			roleShizhuang = list.get(0);
		}
		return roleShizhuang;
	}
	
	private RoleShiZhuangJinJie getRoleShizhuangJj(Long userRoleId,Integer id){
		RoleShiZhuangJinJie roleShiZhuangJj = null;
		final Integer szId = id;
		List<RoleShiZhuangJinJie> list = roleShizhuangJinJieDao.cacheLoadAll(userRoleId,new IQueryFilter<RoleShiZhuangJinJie>(){
			boolean stop;
			@Override
			public boolean check(RoleShiZhuangJinJie entity) {
				if(entity.getShizhuangId().equals(szId)){
					stop = true;
				}
				return stop;
			}

			@Override
			public boolean stopped() {
				return stop;
			}
		});
		if(list != null && list.size() > 0){
			roleShiZhuangJj = list.get(0);
		}
		return roleShiZhuangJj;
	}
	
	private RoleShizhuang getNowRoleShizhuang(Long userRoleId){
		RoleShizhuang roleShizhuang = null;
		List<RoleShizhuang> list = roleShizhuangDao.cacheLoadAll(userRoleId,new IQueryFilter<RoleShizhuang>(){
			boolean stop;
			@Override
			public boolean check(RoleShizhuang entity) {
				if(entity.getIsShow() == GameConstants.BOOLEAN_TRUE_TO_INT){
					stop = true;
				}
				return stop;
			}
			
			@Override
			public boolean stopped() {
				return stop;
			}
		});
		if(list != null && list.size() > 0){
			roleShizhuang = list.get(0);
		}
		return roleShizhuang;
	}
	
	private RoleShizhuang createShiZhuang(Long userRoleId,Integer id,Long limitTime){
		RoleShizhuang roleShizhuang = new RoleShizhuang();
		roleShizhuang.setId(IdFactory.getInstance().generateId(ServerIdType.GOODS_OTHER));
		roleShizhuang.setIsShow(GameConstants.BOOLEAN_FALSE_TO_INT);
		roleShizhuang.setLevel(0);
		roleShizhuang.setShizhuangId(id);
		roleShizhuang.setUserRoleId(userRoleId);
		roleShizhuang.setExpireTime(limitTime);
		roleShizhuangDao.cacheInsert(roleShizhuang, userRoleId);
		return roleShizhuang;
	}
	
	private RoleShiZhuangJinJie createRoleShiZhuangJinJie(Long userRoleId,Integer id){
		RoleShiZhuangJinJie roleShizhuang = new RoleShiZhuangJinJie();
		roleShizhuang.setId(IdFactory.getInstance().generateId(ServerIdType.GOODS_OTHER));
		roleShizhuang.setJieLevel(1);
		roleShizhuang.setShizhuangId(id);
		roleShizhuang.setUserRoleId(userRoleId);
		roleShizhuang.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleShizhuangJinJieDao.cacheInsert(roleShizhuang, userRoleId);
		return roleShizhuang;
	}
	
	private void synStageAttributeActiveChange(Long userRoleId){
		Map<String,Long> activeAtt = new HashMap<>();
		List<RoleShizhuang> list = roleShizhuangDao.cacheLoadAll(userRoleId);
		Long cur = GameSystemTime.getSystemMillTime();
		for (RoleShizhuang roleShizhuang : list) {
			if(roleShizhuang.isExpire(cur)){
				continue;
			}
			ShiZhuangConfig shiZhuangConfig = shiZhuangConfigExportService.getConfig(roleShizhuang.getShizhuangId());
			if(shiZhuangConfig != null){
				ObjectUtil.longMapAdd(activeAtt, shiZhuangConfig.getAttribute());
			}else{
				ChuanQiLog.error("时装id{}未找到配置",roleShizhuang.getShizhuangId());
			}
		}
		if(activeAtt.size() > 0){
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_SHIZHUANG_ACTIVE_ATT, activeAtt);
		}
	}
	
	private void synStageAttributeShengJiChange(Long userRoleId){
		Map<String,Long> levelAtt = new HashMap<>();
		List<RoleShizhuang> list = roleShizhuangDao.cacheLoadAll(userRoleId);
		Long cur = GameSystemTime.getSystemMillTime();
		for (RoleShizhuang roleShizhuang : list) {
			if(roleShizhuang.isExpire(cur)){
				continue;
			}
			ShiZhuangLevelConfig config = shiZhuangLevelConfigExportService.getConfig(roleShizhuang.getLevel());
			if(config != null){
				ObjectUtil.longMapAdd(levelAtt, config.getAtts());
			}
		}
		if(levelAtt.size() > 0){
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_SHIZHUANG_SHENGJI_ATT, levelAtt);
		}
	}
	
	
	
	private void synStageAttributeJinJiChange(Long userRoleId){
		Map<String,Long> allAtt = new HashMap<>(); 
		Map<String,Long> JieAtt = new HashMap<>(); 
		List<RoleShizhuang> list = roleShizhuangDao.cacheLoadAll(userRoleId);
		Long cur = GameSystemTime.getSystemMillTime();
		//添加时装的激活升级属性
		for (RoleShizhuang roleShizhuang : list) {
			ShiZhuangConfig shiZhuangConfig = shiZhuangConfigExportService.getConfig(roleShizhuang.getShizhuangId());
			if(roleShizhuang.isExpire(cur)){
				continue;
			}
			if(shiZhuangConfig != null){
				ObjectUtil.longMapAdd(allAtt, shiZhuangConfig.getAttribute());
			}else{
				ChuanQiLog.error("时装id{}未找到配置",roleShizhuang.getShizhuangId());
			}
			ShiZhuangLevelConfig config = shiZhuangLevelConfigExportService.getConfig(roleShizhuang.getLevel());
			if(config != null){
				ObjectUtil.longMapAdd(allAtt, config.getAtts());
			}
		}
		//时装升阶属性
		List<RoleShiZhuangJinJie> jinJieList = roleShizhuangJinJieDao.cacheLoadAll(userRoleId);
		int jieCount = 0;
		if(jinJieList != null){
			for (RoleShiZhuangJinJie roleShizhuang : jinJieList) {
				ShiZhuangJinJieConfig config = shiZhuangJinJieConfigExportService.getConfig(roleShizhuang.getShizhuangId()+"_"+roleShizhuang.getJieLevel());
				if(config == null){
					continue;
				}
				jieCount = jieCount+roleShizhuang.getJieLevel();
				//添加升阶属性
				if(config != null){
					ObjectUtil.longMapAdd(JieAtt, config.getAttrs());
					ObjectUtil.longMapAdd(allAtt, config.getAttrs());
				}
			}
		}
		
		//升阶阶段属性
		ShiZhuangJieDuanConfig jieDuanConfig = getJieDuanConfigId(jieCount);
		if(jieDuanConfig != null){
			//添加进阶阶段属性
			ObjectUtil.longMapAdd(JieAtt, jieDuanConfig.getAttrs());
			ObjectUtil.longMapAdd(allAtt, jieDuanConfig.getAttrs());
			if(jieDuanConfig.getShizhuangshuxing() > 0){
				Map<String,Long> jieDuanAttr = new HashMap<>();
				for(Entry<String, Long> map:allAtt.entrySet()){
//					ChuanQiLog.error("error  {},{},{},{}", map.getKey(),map.getValue(),"======",Math.round(map.getValue() * (jieDuanConfig.getShizhuangshuxing()/100f)));
					jieDuanAttr.put(map.getKey(), Math.round(map.getValue() * (jieDuanConfig.getShizhuangshuxing()/100d)));
				}
				//添加进阶阶段属性百分比
				ObjectUtil.longMapAdd(JieAtt, jieDuanAttr);
			}
		}
		
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_SHIZHUANG_JINJIE_ATT, JieAtt);
	}	
	public Object[] getRoleShiZhuangInfo(Long userRoleId){
		List<RoleShizhuang> list = roleShizhuangDao.cacheLoadAll(userRoleId);
		if(list == null || list.size() < 1){
			return null;
		}
		List<Object[]> out = new ArrayList<>();
		Integer showId = null;
		Long cur = GameSystemTime.getSystemMillTime();
		for (RoleShizhuang roleShizhuang : list) {
			out.add(new Object[]{roleShizhuang.getShizhuangId(),roleShizhuang.getLevel(),roleShizhuang.getExpireTime()});
			if(!roleShizhuang.isExpire(cur) && roleShizhuang.getIsShow() == GameConstants.BOOLEAN_TRUE_TO_INT){
				showId = roleShizhuang.getShizhuangId();
			}
		}
		return new Object[]{out.toArray(),showId};
	}
	
	/**
	 * 获取时装进阶信息
	 * @param userRoleId
	 * @return
	 */
	public Object[] getRoleShiZhuangJinJieInfo(Long userRoleId){
		
		List<RoleShizhuang> list = roleShizhuangDao.cacheLoadAll(userRoleId);
		if(list == null || list.size() < 1){
			return null;
		}
		List<RoleShiZhuangJinJie> jieJieList = roleShizhuangJinJieDao.cacheLoadAll(userRoleId);
		Map<Integer,Integer> jieJieMap = new HashMap<>();
		if(jieJieList != null){
			for (RoleShiZhuangJinJie roleShiZhuangJinJie : jieJieList) {
				jieJieMap.put(roleShiZhuangJinJie.getShizhuangId(), roleShiZhuangJinJie.getJieLevel());
			}
		}
		List<Object[]> out = new ArrayList<>();
		for (RoleShizhuang roleShizhuang : list) {
			if(jieJieMap.containsKey(roleShizhuang.getShizhuangId())){
				out.add(new Object[]{roleShizhuang.getShizhuangId(),jieJieMap.get(roleShizhuang.getShizhuangId())});
			}else{
				out.add(new Object[]{roleShizhuang.getShizhuangId(),1});
				RoleShiZhuangJinJie jinjie = new RoleShiZhuangJinJie();
				jinjie = createRoleShiZhuangJinJie(userRoleId, roleShizhuang.getShizhuangId());
				jieJieList.add(jinjie);
			}
		}
		int jieCount = 0;
		for (RoleShiZhuangJinJie roleShiZhuangJinJie : jieJieList) {
			jieCount = jieCount + roleShiZhuangJinJie.getJieLevel();
		}
		int jieId = 0;
		ShiZhuangJieDuanConfig config = getJieDuanConfigId(jieCount);
		if(config != null){
			jieId = config.getId();
		}
		return new Object[]{out.toArray(),jieId,jieCount};
	}
	
	
	/**
	 * 获取阶段配置
	 * @param jieCount
	 * @return
	 */
	public ShiZhuangJieDuanConfig getJieDuanConfigId(int jieCount){
		if(jieCount <= 0){
			return null;
		}
		ShiZhuangJieDuanConfig config = shiZhuangJieDuanConfigExportService.getConfig(jieCount);
		if(config != null){
			return config;
		}
		jieCount--;
		return getJieDuanConfigId(jieCount);
		
	}
	
	public Integer getAttribute(Long userRoleId,Map<String,Long> activeAtt,Map<String,Long> levelAtt,Map<String,Long> jinJieAtt){
		Map<String,Long> allAtt = new HashMap<>(); 
		List<RoleShizhuang> list = roleShizhuangDao.cacheLoadAll(userRoleId);
		Integer showId = null;
		Long cur = GameSystemTime.getSystemMillTime();
		for (RoleShizhuang roleShizhuang : list) {
			ShiZhuangConfig shiZhuangConfig = shiZhuangConfigExportService.getConfig(roleShizhuang.getShizhuangId());
			if(roleShizhuang.isExpire(cur)){
				continue;
			}
			if(shiZhuangConfig != null){
				ObjectUtil.longMapAdd(activeAtt, shiZhuangConfig.getAttribute());
				ObjectUtil.longMapAdd(allAtt, shiZhuangConfig.getAttribute());
			}else{
				ChuanQiLog.error("时装id{}未找到配置",roleShizhuang.getShizhuangId());
			}
			ShiZhuangLevelConfig config = shiZhuangLevelConfigExportService.getConfig(roleShizhuang.getLevel());
			if(config != null){
				ObjectUtil.longMapAdd(levelAtt, config.getAtts());
				ObjectUtil.longMapAdd(allAtt, config.getAtts());
			}
			if(roleShizhuang.getIsShow() == GameConstants.BOOLEAN_TRUE_TO_INT){
				showId = roleShizhuang.getShizhuangId();
			}
		}
		List<RoleShiZhuangJinJie> jinJieList = roleShizhuangJinJieDao.cacheLoadAll(userRoleId);
		int jieCount = 0;
		if(jinJieList != null){
			for (RoleShiZhuangJinJie roleShizhuang : jinJieList) {
				ShiZhuangJinJieConfig config = shiZhuangJinJieConfigExportService.getConfig(roleShizhuang.getShizhuangId()+"_"+roleShizhuang.getJieLevel());
				if(config == null){
					continue;
				}
				jieCount = jieCount+roleShizhuang.getJieLevel();
				//添加升阶属性
				if(config != null){
					ObjectUtil.longMapAdd(jinJieAtt, config.getAttrs());
					ObjectUtil.longMapAdd(allAtt, config.getAttrs());
				}
			}
		}
		
		ShiZhuangJieDuanConfig jieDuanConfig = getJieDuanConfigId(jieCount);
		if(jieDuanConfig != null){
			//添加进阶阶段属性
			ObjectUtil.longMapAdd(jinJieAtt, jieDuanConfig.getAttrs());
			ObjectUtil.longMapAdd(allAtt, jieDuanConfig.getAttrs());
			if(jieDuanConfig.getShizhuangshuxing() > 0){
				Map<String,Long> jieDuanAttr = new HashMap<>();
				for(Entry<String, Long> map:allAtt.entrySet()){
//					ChuanQiLog.error("error  {},{},{}", map.getValue(),"======",map.getValue()+(Math.round(map.getValue() * (jieDuanConfig.getShizhuangshuxing()/100f))));
					jieDuanAttr.put(map.getKey(), Math.round(map.getValue() * (jieDuanConfig.getShizhuangshuxing()/100d)));
				}
				//添加进阶阶段属性百分比
				ObjectUtil.longMapAdd(jinJieAtt, jieDuanAttr);
			}
		}
		return showId;
	}
	
	public Object[] activeShiZhuang(Long userRoleId,Integer id){
		ShiZhuangConfig shiZhuangConfig = shiZhuangConfigExportService.getConfig(id);
		if(shiZhuangConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		RoleShizhuang roleShizhuang = getRoleShizhuang(userRoleId, id);
		if(roleShizhuang != null && roleShizhuang.getExpireTime() < 1){
			return AppErrorCode.SHIZHUANG_IS_ACTIVED;//时装已激活
		}
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null || jueSeDuiZhaoBiaoConfigExportService.getSex(role.getConfigId()) != shiZhuangConfig.getSex()){
			return AppErrorCode.SHIZHUANG_SEX_ERROR;
		}
		
		int gold = shiZhuangConfig.getGold();
		if(gold > 0){
			Object[] ret = accountExportService.isEnought(GoodsCategory.GOLD, gold, userRoleId);
			if(ret != null){
				return ret;
			}
		}
		Map<String, Integer> items = shiZhuangConfig.getCostItem();
		if(items != null && items.size() > 0){
			Object[] ret = roleBagExportService.checkRemoveBagItemByGoodsId(items, userRoleId);
			if(ret != null){
				return ret;
			}
		}
		
		if(gold > 0){
			accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, gold, userRoleId, LogPrintHandle.CONSUME_SHIZHUANG, true, LogPrintHandle.CBZ_ACTIVE_SHIZHUANG);
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,gold,LogPrintHandle.CONSUME_SHIZHUANG,QQXiaoFeiType.CONSUME_SHIZHUANG,1});
			}
		}
		if(items != null && items.size() > 0){
			roleBagExportService.removeBagItemByGoods(items, userRoleId, GoodsSource.GOODS_SHIZHUANG_ACTIVE, true, true);
		}
		boolean expire = true;
		if(roleShizhuang == null){
			createShiZhuang(userRoleId, id,0l);
		}else{
			expire = roleShizhuang.isExpire(GameSystemTime.getSystemMillTime());
			roleShizhuang.setExpireTime(0l);//限时变永久
			roleShizhuangDao.cacheUpdate(roleShizhuang, userRoleId);
		}
		
		if(expire){
		    shizhuangExpire(userRoleId, 1);
		}
		createRoleShiZhuangJinJie(userRoleId, id);
		synStageAttributeJinJiChange(userRoleId);
		return new Object[]{1,id};
	}
	
	public Object[] levelUpShiZhuang(Long userRoleId,Integer id){
		RoleShizhuang roleShizhuang = getRoleShizhuang(userRoleId, id);
		if(roleShizhuang == null || roleShizhuang.isExpire(GameSystemTime.getSystemMillTime())){
			return AppErrorCode.SHIZHUANG_NOT_ACTIVE;//时装未激活
		}
		int nextLevel = roleShizhuang.getLevel() + 1 ;
		ShiZhuangLevelConfig shiZhuangLevelConfig = shiZhuangLevelConfigExportService.getConfig(nextLevel);
		if(shiZhuangLevelConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		Object[] ret = roleBagExportService.removeBagItemByGoodsIdAndNumber(shiZhuangLevelConfig.getItems(), userRoleId,GoodsSource.GOODS_SHIZHUANG_SHENGJI,true,true,LogPrintHandle.CONSUME_SHIZHUANG,true,LogPrintHandle.CBZ_SHENGJI_SHIZHUANG);
		if(ret != null){
			return ret;
		}
		
		roleShizhuang.setLevel(nextLevel);
		roleShizhuangDao.cacheUpdate(roleShizhuang, userRoleId);
		
		synStageAttributeShengJiChange(userRoleId);
		return new Object[]{1,nextLevel,id};
	}
	
	
	/**
	 * 时装进阶
	 * @param userRoleId
	 * @param id
	 * @return
	 */
	public Object[] shiZhuangJinJie(Long userRoleId,Integer id,Integer level){
		RoleShiZhuangJinJie entity = getRoleShizhuangJj(userRoleId, id);
		if(entity == null){
			entity = createRoleShiZhuangJinJie(userRoleId, id);
		}
		if(level.intValue() != entity.getJieLevel()){
			return AppErrorCode.SHIZHUANG_JJLEVEL_ERROR;
		}
		
		int maxLevel = shiZhuangJinJieConfigExportService.getMaxLevel(level);
		if(level.intValue() > maxLevel){
			return AppErrorCode.SHIZHUANG_MAX_LEVEL;
		}
		int nextLevel = entity.getJieLevel() + 1 ;
		ShiZhuangJinJieConfig shiZhuangJjConfig = shiZhuangJinJieConfigExportService.getConfig(id+"_"+entity.getJieLevel());
		if(shiZhuangJjConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		int needMoney =  shiZhuangJjConfig.getNeedmoney();
		// 扣除金币
		if(needMoney > 0){
			Object[] ret1 = roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, needMoney, userRoleId,  LogPrintHandle.CONSUME_SHIZHUANG_SJ, true, LogPrintHandle.CBZ_SHIZHUANG_SJ);
			if(ret1 != null){
				return ret1;
			}
		}
		//扣除道具
		Object[] ret = roleBagExportService.removeBagItemByGoodsIdAndNumber(shiZhuangJjConfig.getCostItem(), userRoleId,GoodsSource.GOODS_SHIZHUANG_JINJIE,true,true,LogPrintHandle.CONSUME_SHIZHUANG_JINJIE,true,LogPrintHandle.CBZ_JINJIE_SHIZHUANG);
		if(ret != null){
			return ret;
		}
		entity.setJieLevel(nextLevel);
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleShizhuangJinJieDao.cacheUpdate(entity, userRoleId);
		
		List<RoleShiZhuangJinJie> jieJieList = roleShizhuangJinJieDao.cacheLoadAll(userRoleId);
		int jieCount = 0;
		for (RoleShiZhuangJinJie roleShiZhuangJinJie : jieJieList) {
			jieCount = jieCount + roleShiZhuangJinJie.getJieLevel();
		}
		synStageAttributeJinJiChange(userRoleId);
		int jieId = 0;
		ShiZhuangJieDuanConfig config = getJieDuanConfigId(jieCount);
		if(config != null){
			jieId = config.getId();
		}
		return new Object[]{1,id,nextLevel,jieId,jieCount};
	}
	
	public void changeShizhuang(Long userRoleId,Integer id){
		String stageId = publicRoleStateExportService.getRolePublicStageId(userRoleId);
		if(stageId == null){
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		if(stage == null || !stage.isCanChangeShow()){
			return;
		}
		RoleShizhuang nowShizhuang = getNowRoleShizhuang(userRoleId);
		if(nowShizhuang != null){
			nowShizhuang.setIsShow(GameConstants.BOOLEAN_FALSE_TO_INT);
			roleShizhuangDao.cacheUpdate(nowShizhuang, userRoleId);
		}
		if(id > 0){
			RoleShizhuang roleShizhuang = getRoleShizhuang(userRoleId, id);
			if(roleShizhuang == null || roleShizhuang.isExpire(GameSystemTime.getSystemMillTime())){
				return;//时装未激活
			}
			roleShizhuang.setIsShow(GameConstants.BOOLEAN_TRUE_TO_INT);
			roleShizhuangDao.cacheUpdate(roleShizhuang, userRoleId);
		}
		
		//通知场景
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_SHIZHUANG, id);
	}
	
	/**
	 * 激活限时时装
	 * @param userRoleId
	 * @param time	有效时间（单位小时）
	 * @param id
	 * @return
	 */
	public Object[] activeXianshiShizhuang(Long userRoleId,int time,int id){
		ShiZhuangConfig shiZhuangConfig = shiZhuangConfigExportService.getConfig(id);
		if(shiZhuangConfig == null){
			return AppErrorCode.CONFIG_ERROR;
		}
		Long cur = GameSystemTime.getSystemMillTime();
		RoleShizhuang roleShizhuang = getRoleShizhuang(userRoleId, id);
		if(roleShizhuang != null && !roleShizhuang.isExpire(cur)){
			return AppErrorCode.SHIZHUANG_IS_ACTIVED;//时装已激活
		}
		
		RoleWrapper role = roleExportService.getLoginRole(userRoleId);
		if(role == null || jueSeDuiZhaoBiaoConfigExportService.getSex(role.getConfigId()) != shiZhuangConfig.getSex()){
			return AppErrorCode.SHIZHUANG_SEX_ERROR;
		}
		Long limitTime = cur + time * 3600000L;
		if(roleShizhuang == null){
			roleShizhuang = createShiZhuang(userRoleId, id,limitTime);
		}else{
			roleShizhuang.setExpireTime(limitTime);//时限
			roleShizhuangDao.cacheUpdate(roleShizhuang, userRoleId);
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.XIANSHI_SHIZHUANG_ACTIVE, new Object[]{id,roleShizhuang.getLevel(),roleShizhuang.getExpireTime()});
		busScheduleExportService.cancelSchedule(userRoleId.toString(), GameConstants.COMPONENT_SHIZHUANG);
		shizhuangExpire(userRoleId, 1);
		createRoleShiZhuangJinJie(userRoleId, id);
		synStageAttributeJinJiChange(userRoleId);
//		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHANGE_SHIZHUANG_JINJIE, id);
		/*send2Stage(userRoleId, InnerCmdType.CHANGE_SHIZHUANG_JINJIE, id);*/
		return null;
	}
	
	/**
	 * 时装过期处理
	 * @param userRoleId
	 * @param time	深度，防止无限循环
	 */
	public void shizhuangExpire(Long userRoleId,int time){
		//时装过期
		Map<String,Long> activeAtt = new HashMap<>();
		Map<String,Long> levelAtt = new HashMap<>();
		List<RoleShizhuang> list = roleShizhuangDao.cacheLoadAll(userRoleId);
		Long cur = GameSystemTime.getSystemMillTime();
		Long nextTime = Long.MAX_VALUE;
		for (RoleShizhuang roleShizhuang : list) {
			if(roleShizhuang.isExpire(cur)){
				if(roleShizhuang.getIsShow() == GameConstants.BOOLEAN_TRUE_TO_INT){
					roleShizhuang.setIsShow(GameConstants.BOOLEAN_FALSE_TO_INT);
					roleShizhuangDao.cacheUpdate(roleShizhuang, userRoleId);
					BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_SHIZHUANG, 0);
				}
				continue;
			}else if(roleShizhuang.getExpireTime() > 0 && roleShizhuang.getExpireTime() < nextTime){
				nextTime = roleShizhuang.getExpireTime();
			}
			ShiZhuangConfig shiZhuangConfig = shiZhuangConfigExportService.getConfig(roleShizhuang.getShizhuangId());
			if(shiZhuangConfig != null){
				ObjectUtil.longMapAdd(activeAtt, shiZhuangConfig.getAttribute());
			}else{
				ChuanQiLog.error("时装id{}未找到配置",roleShizhuang.getShizhuangId());
			}
			ShiZhuangLevelConfig config = shiZhuangLevelConfigExportService.getConfig(roleShizhuang.getLevel());
			if(config != null){
				ObjectUtil.longMapAdd(levelAtt, config.getAtts());
			}
			
		}
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_SHIZHUANG_ACTIVE_ATT, activeAtt);
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_SHIZHUANG_SHENGJI_ATT, levelAtt);
		if(nextTime < Long.MAX_VALUE){
			Long schedule = nextTime - GameSystemTime.getSystemMillTime();
			if(schedule <= 0 && time < 10){
				shizhuangExpire(userRoleId,time++);
			}else{
				schedule = schedule / 1000 + 20;
				BusTokenRunable runable = new BusTokenRunable(userRoleId, InnerCmdType.SHIZHUANG_EXPIRE,null);
				busScheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPONENT_SHIZHUANG, runable, schedule.intValue(), TimeUnit.SECONDS);
			}
		}
	}
	
	public Object[] xufei(Long userRoleId,Integer id){
		ShiZhuangConfig shiZhuangConfig = shiZhuangConfigExportService.getConfig(id);
		if(shiZhuangConfig == null || shiZhuangConfig.getXfgold() < 1){
			return AppErrorCode.CONFIG_ERROR;
		}
		RoleShizhuang roleShizhuang = getRoleShizhuang(userRoleId, id);
		if(roleShizhuang == null){
			return AppErrorCode.SHIZHUANG_NOT_ACTIVE;//时装已激活
		}
		if(roleShizhuang.getExpireTime() < 1){
			return AppErrorCode.SHIZHUANG_IS_ACTIVED;//时装已激活
		}
		Object[] ret = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, shiZhuangConfig.getXfgold(), userRoleId, LogPrintHandle.CONSUME_SHIZHUANG, true, LogPrintHandle.CBZ_XUFEI_SHIZHUANG);
		if(ret != null){
			return ret;
		}else{
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,shiZhuangConfig.getXfgold(),LogPrintHandle.CONSUME_SHIZHUANG,QQXiaoFeiType.CONSUME_SHIZHUANG,1});
			}
		}
		
		Long cur = GameSystemTime.getSystemMillTime();
		Long expireTime = null;
		if(roleShizhuang.isExpire(cur)){
			expireTime = cur + shiZhuangConfig.getXftime() * 3600000L; 
		}else{
			expireTime = roleShizhuang.getExpireTime() + shiZhuangConfig.getXftime() * 3600000L;
		}
		roleShizhuang.setExpireTime(expireTime);
		roleShizhuangDao.cacheUpdate(roleShizhuang, userRoleId);
		
		busScheduleExportService.cancelSchedule(userRoleId.toString(), GameConstants.COMPONENT_SHIZHUANG);
		shizhuangExpire(userRoleId, 1);
		return new Object[]{1,id,expireTime};
	}
	
	public Object[] zhuanZhiShiZhuangZhuanHuan(Long userRoleId){
		List<RoleShizhuang> list = roleShizhuangDao.cacheLoadAll(userRoleId);
		if(list.size() <= 0){
			return null;
		}
		for (RoleShizhuang roleShizhuang : list) {
			//对应的要转换时装ID
			ShiZhuangDuiYingBiaoConfig sz = shiZhuangDuiYingBiaoConfigExportService.loadById(roleShizhuang.getShizhuangId());
			if(null == sz){
			    return AppErrorCode.SHIZHUANG_CONFIG_ERROR;
			}
			if(sz.getId1().intValue() > 0){
				roleShizhuang.setShizhuangId(sz.getId1().intValue());
				roleShizhuangDao.cacheUpdate(roleShizhuang, userRoleId);
				if(roleShizhuang.getIsShow() == GameConstants.BOOLEAN_TRUE_TO_INT){
					//通知场景脱掉身上穿戴的时装
					BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHANGE_SHIZHUANG, roleShizhuang.getShizhuangId());
				}
			}
		}
		return null;
		
	}
	
}
