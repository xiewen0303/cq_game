package com.junyou.bus.tongtian.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tongtian.configure.export.TongtianRaodConfig;
import com.junyou.bus.tongtian.configure.export.TongtianRoadConfigExportService;
import com.junyou.bus.tongtian.configure.export.TongtianRoadMaxAttrConfigExportService;
import com.junyou.bus.tongtian.dao.RoleTongtianRoadDao;
import com.junyou.bus.tongtian.entity.RoleTongtianRoad;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.yaoshen.entity.RoleYaoshen;
import com.junyou.bus.yaoshen.entity.RoleYaoshenHunpo;
import com.junyou.bus.yaoshen.entity.RoleYaoshenMowen;
import com.junyou.bus.yaoshen.entity.RoleYaoshenMoyin;
import com.junyou.bus.yaoshen.service.export.YaoshenExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenHunpoExportService;
import com.junyou.bus.yaoshen.service.export.YaoshenMoYinExportService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.TongtianRoadCuilianLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.TongtianPublicConfig;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class TongtianRoadService {

	@Autowired
	private TongtianRoadConfigExportService tongtianLoadConfigExportService;
	@Autowired
	private RoleTongtianRoadDao roleTongtianRoadDao;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService ggsjbConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private YaoshenExportService yaoshenExportService; 
	@Autowired
	private YaoshenHunpoExportService yaoshenHunpoExportService;
	@Autowired
	private YaoshenMoYinExportService yaoshenMoYinExportService;
	@Autowired
	private TongtianRoadMaxAttrConfigExportService tongtianRoadMaxAttrConfigExportService;

	/***
	 * 初始化
	 */
	public RoleTongtianRoad initData(Long userRoleId) {
		List<RoleTongtianRoad> data = roleTongtianRoadDao.initRoleTongtianRoad(userRoleId);
		return data != null && data.size() > 0 ? data.get(0) : null;
	}

	/**
	 * 初始化属性
	 */
	public Map<String, Long> initAttrMap(long userRoleId) {
		RoleTongtianRoad entity = roleTongtianRoadDao.cacheLoad(userRoleId, userRoleId);
		if (entity == null || entity.getAttribute() == null) {
			return null;
		}
		if(!PlatformConstants.isQQ()){
			entity.setClientMap(entity.getAttrMap());
		}else if (entity.getClientMap().size() == 0) {
			Map<String, Long> attr = new HashMap<>();
			attr.putAll(entity.getAttrMap());
			// 矫正数据 各个属性超过上限值处理
			Map<Integer, TongtianRaodConfig> allConfig = tongtianLoadConfigExportService.loadAllConfig();
			TongtianRaodConfig tongtianRaodConfig = null;
			boolean isCompareZplus = false;// 是否已经比对过战力
			long maxAttr  = 0;
			long maxZplus = 0;
			Map<String, Long> yaoshenTotalAttr = countYaoshenAddTotalAttr(userRoleId);
			for (Entry<Integer, TongtianRaodConfig> ent : allConfig.entrySet()) {
				tongtianRaodConfig = ent.getValue();
				Long attrValue = attr.get(tongtianRaodConfig.getAttrName());
				Long zplus = attr.get(EffectType.zplus.name());
				if (attrValue != null) {
					maxAttr = tongtianRaodConfig.getMaxAttr()+getAddMaxValueByAttrName(yaoshenTotalAttr, tongtianRaodConfig.getAttrName());
					if (attrValue > maxAttr) {
						attr.put(tongtianRaodConfig.getAttrName(), maxAttr);
					}
				}
				if (zplus != null && !isCompareZplus) {
					maxZplus = tongtianRaodConfig.getMaxZplus()+getAddMaxValueByAttrName(yaoshenTotalAttr,EffectType.zplus.name());
					if (zplus > maxZplus) {
						attr.put(EffectType.zplus.name(),maxZplus);
						isCompareZplus = true;
					}
				}
			}
			entity.setClientMap(attr);
		}
		return entity.getClientMap();
	}

	/**
	 * 获取通天之路的面板信息
	 */
	public Object[] getInfo(long userRoleId) {
		TongtianPublicConfig tongtianPublicConfig = ggsjbConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TONGTIAN);
		if (tongtianPublicConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper.getLevel() < tongtianPublicConfig.getOpen()) {
			return AppErrorCode.ROLE_LEVEL_ERROR;
		}
		RoleTongtianRoad entity = loadEntity(userRoleId);
		Map<String, Long> clientMap = entity.getClientMap();
		if (clientMap != null) {
			if(!PlatformConstants.isQQ()){
				entity.setClientMap(entity.getAttrMap());
			}else  if (entity.getClientMap().size() == 0) {
				// 矫正数据 各个属性超过上限值处理
				Map<Integer, TongtianRaodConfig> allConfig = tongtianLoadConfigExportService.loadAllConfig();
				TongtianRaodConfig tongtianRaodConfig = null;
				boolean isCompareZplus = false;// 是否已经比对过战力
				long maxAttr  = 0;
				long maxZplus = 0;
				Map<String, Long> yaoshenTotalAttr = countYaoshenAddTotalAttr(userRoleId);
				for (Entry<Integer, TongtianRaodConfig> ent : allConfig.entrySet()) {
					tongtianRaodConfig = ent.getValue();
					Long attrValue = clientMap.get(tongtianRaodConfig.getAttrName());
					Long zplus = clientMap.get(EffectType.zplus.name());
					if (attrValue != null) {
						maxAttr = tongtianRaodConfig.getMaxAttr()+getAddMaxValueByAttrName(yaoshenTotalAttr, tongtianRaodConfig.getAttrName());
						if (attrValue > maxAttr) {
							clientMap.put(tongtianRaodConfig.getAttrName(), maxAttr);
						}
					}
					if (zplus != null && !isCompareZplus) {
						maxZplus = tongtianRaodConfig.getMaxZplus()+getAddMaxValueByAttrName(yaoshenTotalAttr,EffectType.zplus.name());
						if (zplus > maxZplus) {
							clientMap.put(EffectType.zplus.name(), maxZplus);
							isCompareZplus = true;
						}
					}
				}
				entity.setClientMap(clientMap);
			}

		}
		return new Object[] { 1, entity.getClientMap().isEmpty()?null:entity.getClientMap(), entity.getValue() };
	}

	/**
	 * 道具淬炼
	 * @param userRoleId
	 * @return
	 */
	public Object[] itemCuilian(long userRoleId, List<Long> itemIds) {
		TongtianPublicConfig tongtianPublicConfig = ggsjbConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_TONGTIAN);
		if (tongtianPublicConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper.getLevel() < tongtianPublicConfig.getOpen()) {
			return AppErrorCode.ROLE_LEVEL_ERROR;
		}

		RoleTongtianRoad entity = loadEntity(userRoleId);
		int position = entity.getPosition();
		int totalCuilian = 0; // 总的淬炼值
		int addCuilian = 0; // 新增淬炼值
		int consumeCuilian = 0; //这次淬炼掉的值
		Map<Long, Integer> cuilianGoods = new HashMap<Long, Integer>();
		Map<String, Integer> goodIdMap = new HashMap<String, Integer>();
		if(itemIds!=null){
			for (Long e : itemIds) {
				RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
				if (roleItemExport == null) {
					return AppErrorCode.ITEM_NOT_ENOUGH;
				}
				String goodsId = roleItemExport.getGoodsId();
				GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
				if (goodsConfig.getCuilian() <= 0) {
					// 不能淬炼,移除
					return AppErrorCode.TONGTIAN_ITEM_NO_CUILIAN;
				}
				goodIdMap.put(roleItemExport.getGoodsId(), roleItemExport.getCount());
				cuilianGoods.put(roleItemExport.getGuid(), roleItemExport.getCount()); // 需要淬炼的道具

				addCuilian += goodsConfig.getCuilian() * roleItemExport.getCount();
			}
		}else{
			if(entity.getValue()< tongtianLoadConfigExportService.getMaxCuilian()){
				return AppErrorCode.TONGTIAN_LINGXIUZHI_NOT_ENOUGH;//剩余朽值不足
			}
		}
		totalCuilian = addCuilian + entity.getValue(); // 玩家总的淬炼值
		boolean isNoticeAttrChange = false; 
		long attrValue = 0;
		long zplusValue = 0;
		long maxAttr = 0;
		long maxZplus = 0;
		int maxLoop = 0; // 记录有几此属性超过最大值(总共八个属性)
		int actualLoop  = 0; //实际加属性的循环次数
		String attrName = null;
		// 达到最大值
		if (totalCuilian >= tongtianLoadConfigExportService.getMaxCuilian()) {
			int loop = (int) Math.floor(totalCuilian / tongtianLoadConfigExportService.getMaxCuilian()); // 计算要加几次
			TongtianRaodConfig config = null;
			Map<String, Long> yaoshenTotalAttr = countYaoshenAddTotalAttr(userRoleId);
			for (int i = 0; i < loop; i++) {
				if (maxLoop == tongtianLoadConfigExportService.getMaxId()+1) {
					break;
				}
				position++;
				if (position > tongtianLoadConfigExportService.getMaxId()) {
					position = 1; // 超过最大值就开始新一轮
				}
				config = tongtianLoadConfigExportService.loadConfig(position);
				attrName = config.getAttrName();
				maxAttr = config.getMaxAttr() + getAddMaxValueByAttrName(yaoshenTotalAttr, config.getAttrName());
				if(maxZplus==0){
					maxZplus = config.getMaxZplus() + getAddMaxValueByAttrName(yaoshenTotalAttr,EffectType.zplus.name());
				}
				
				Long roleAttr = entity.getAttrMap().get(attrName);
				Long roleZplus = entity.getAttrMap().get(EffectType.zplus.name());
				if (roleAttr != null && roleAttr >= maxAttr) {
					// 属性值已经达到上限 不记录循环数
					i--;
					maxLoop++;
					continue;
				}
				if (config != null) {
					isNoticeAttrChange = true;
					attrValue = (roleAttr == null ? 0 : roleAttr) + config.getAttrMap().get(attrName);
					zplusValue = (roleZplus == null ? 0 : roleZplus) + config.getAttrMap().get(EffectType.zplus.name());
					if (attrValue > maxAttr) {
						attrValue = maxAttr;
					}
					if (zplusValue > maxZplus) {
						zplusValue = maxZplus;
					}
					actualLoop ++;
					maxLoop  = 0 ;
					entity.getAttrMap().put(attrName, attrValue);
					entity.getClientMap().put(attrName, attrValue);
					if(roleZplus!=null &&roleZplus>maxZplus){
						//老玩家战力超的情况下
						entity.getClientMap().put(EffectType.zplus.name(),maxZplus);
					}else{
						entity.getClientMap().put(EffectType.zplus.name(),zplusValue);
						entity.getAttrMap().put(EffectType.zplus.name(),zplusValue);
					}
					
				}
			}
			if (!isNoticeAttrChange) {
				// 所有属性都已经加满
				return AppErrorCode.TONGTIAN_ITEM_ATTR_MAX;
			}
			consumeCuilian = tongtianLoadConfigExportService.getMaxCuilian() * actualLoop;
			entity.setAttribute(JSON.toJSONString(entity.getAttrMap()));
			entity.setValue(totalCuilian - consumeCuilian);
		} else {
			boolean isAllAttrMax = true;
			//判断属性是否全部加满
			if(entity.getAttrMap().size()>0){
				TongtianRaodConfig config = null;
				Long roleAttr = 0L;
				Map<Integer, TongtianRaodConfig> allConfig = tongtianLoadConfigExportService.loadAllConfig();
				Map<String, Long> yaoshenTotalAttr = countYaoshenAddTotalAttr(userRoleId);
				for (Entry<Integer, TongtianRaodConfig> entry : allConfig.entrySet()) {
					config  = entry.getValue();
					maxAttr  = config.getMaxAttr() + getAddMaxValueByAttrName(yaoshenTotalAttr, config.getAttrName());
					attrName = config.getAttrName();
					roleAttr = entity.getAttrMap().get(attrName);
					if(roleAttr!=null && roleAttr<maxAttr){
						isAllAttrMax = false;
						break;
					}
				}
			}else{
				isAllAttrMax = false;
			}
			
			if(!isAllAttrMax){
				entity.setValue(totalCuilian);
			}else{
				// 所有属性都已经加满
				return AppErrorCode.TONGTIAN_ITEM_ATTR_MAX;
			}
			
		}
		entity.setPosition(position);
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());

		// 扣道具
		for (Entry<Long, Integer> item : cuilianGoods.entrySet()) {
			roleBagExportService.removeBagItemByGuid(item.getKey(), item.getValue(), userRoleId, GoodsSource.TONGTIAN_ROAD_CUILIAN, true, true);
		}
		// 执行数据更新
		roleTongtianRoadDao.cacheUpdate(entity, userRoleId);
		// 属性变化
		if (isNoticeAttrChange) {
			notifyStageAttrChange(userRoleId, entity.getClientMap());
		}
		// 日志
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(goodIdMap, null);
		GamePublishEvent.publishEvent(new TongtianRoadCuilianLogEvent(userRoleId, jsonArray, entity.getClientMap(), addCuilian,totalCuilian,consumeCuilian));
		 
		if(!PlatformConstants.isQQ()){
			entity.setClientMap(entity.getAttrMap());
		}
		return new Object[] { 1, entity.getClientMap(), entity.getValue() };
	}
	/**
	 * 获取妖神4个模块的单个属性增加上限值
	 * @param userRoleId
	 * @return
	 */
	private long getAddMaxValueByAttrName(Map<String, Long> total,String attrName){
		if(total!=null && total.get(attrName)!=null){
			return total.get(attrName);
		}
		return  0;
	}
	
	/**
	 * 计算妖神四个模块加的属性上限值
	 * @param userRoleId
	 * @return
	 */
	private Map<String, Long> countYaoshenAddTotalAttr(Long userRoleId){
		Map<String, Long> maxAttrTotal  = new HashMap<>();
		Map<String, Long> attrmMap=null; 
		//霸体
		RoleYaoshen  roleYaoshen = 	yaoshenExportService.getRoleYaoshen(userRoleId);
		if(roleYaoshen!=null){
			attrmMap = tongtianRoadMaxAttrConfigExportService.getBatiMap().get(roleYaoshen.getJieLevel());
			if(attrmMap!=null){
				ObjectUtil.longMapAdd(maxAttrTotal, attrmMap);
			}
		}
		//魔纹
		RoleYaoshenMowen roleYaoshenMowen =yaoshenExportService.getRoleYaoshenMowen(userRoleId);
		if(roleYaoshenMowen!=null){
			attrmMap = tongtianRoadMaxAttrConfigExportService.getMowenMap().get(roleYaoshenMowen.getJieLevel());
			if(attrmMap!=null){
				ObjectUtil.longMapAdd(maxAttrTotal, attrmMap);
			}
		}
		//魂魄
		RoleYaoshenHunpo roleYaoshenHunpo = yaoshenHunpoExportService.getRoleYaoshenHunpo(userRoleId);
		if(roleYaoshenHunpo!=null){
			attrmMap = tongtianRoadMaxAttrConfigExportService.getHunpoMap().get(roleYaoshenHunpo.getJieLevel());
			if(attrmMap!=null){
				ObjectUtil.longMapAdd(maxAttrTotal, attrmMap);
			}
		}
		//魔印
		RoleYaoshenMoyin roleYaoshenMoyin = yaoshenMoYinExportService.getRoleYaoshenMoyin(userRoleId);
		if(roleYaoshenMoyin!=null){
			attrmMap = tongtianRoadMaxAttrConfigExportService.getMoyinMap().get(roleYaoshenMoyin.getJieLevel());
			if(attrmMap!=null){
				ObjectUtil.longMapAdd(maxAttrTotal, attrmMap);
			}
		}
//		ChuanQiLog.info("userRoleId={},通天之路妖神四个模块增加的上限值：{}", userRoleId,maxAttrTotal.toString());
		return  maxAttrTotal;
	}

	private RoleTongtianRoad loadEntity(long userRoleId) {
		RoleTongtianRoad entity = roleTongtianRoadDao.cacheLoad(userRoleId, userRoleId);
		if (entity == null) {
			entity = new RoleTongtianRoad();
			entity.setUserRoleId(userRoleId);
			entity.setPosition(0);
			entity.setValue(0);
			entity.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			entity.setUpdateTime(0L);
			entity.setAttribute(null);
			roleTongtianRoadDao.cacheInsert(entity, userRoleId);
		}
		return entity;
	}

	/**
	 * 通知场景加属性
	 */
	private void notifyStageAttrChange(long userRoleId, Map<String, Long> attr) {
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.TONGTIAN_ROAD_GET_ATTR, attr);
	}

}
