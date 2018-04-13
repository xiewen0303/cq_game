package com.junyou.bus.yaoshen.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.utils.common.CovertObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.yaoshen.configure.export.YaoshenFumoBaseConfig;
import com.junyou.bus.yaoshen.configure.export.YaoshenFumoBaseConfigExportService;
import com.junyou.bus.yaoshen.configure.export.YaoshenFumoJiachengConfigExportService;
import com.junyou.bus.yaoshen.constants.YaoshenConstants;
import com.junyou.bus.yaoshen.dao.RoleYaoshenFumoDao;
import com.junyou.bus.yaoshen.entity.RoleYaoshen;
import com.junyou.bus.yaoshen.entity.RoleYaoshenFumo;
import com.junyou.bus.yaoshen.service.export.YaoshenExportService;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.YaoshenFumoSjLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.YaoshenFumoConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.LogPrintHandle;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.junyou.stage.model.core.attribute.BaseAttributeType;
import com.junyou.stage.model.core.attribute.IBaseAttribute;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class YaoshenFumoService implements IFightVal{
	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		if(fightPowerType == FightPowerType.EM_YSSX) {
			Map<String, Long>  datas = countAllAttr(userRoleId);
			return CovertObjectUtil.getZplus(datas);
		}
		return 0;
	}

	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService; 
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private  RoleYaoshenFumoDao roleYaoshenFumoDao;
	@Autowired
	private  YaoshenExportService yaoshenExportService;
	@Autowired
	private  YaoshenFumoBaseConfigExportService yaoshenFumoBaseConfigExportService;
	@Autowired
	private  YaoshenFumoJiachengConfigExportService yaoshenFumoJiachengConfigExportService;
	@Autowired
	private  PublicRoleStateService publicRoleStateService;
	
	
	 
	/**
	 * 初始化
	 * @param userRoleId
	 * @return
	 */
	public RoleYaoshenFumo initRoleYaoshenFumo(Long userRoleId) {
		List<RoleYaoshenFumo> list = roleYaoshenFumoDao.initRoleYaoshenFumo(userRoleId);
		if (list == null || list.size() == 0) {
			return null;
		}
		RoleYaoshenFumo roleYaoshenMoyin  = list.get(0);
		return roleYaoshenMoyin;
	}
	/**
	 * 玩家登陆后统计属性
	 */
	public Map<String, Long> getYaoshenFumoAttributeAfterLogin(Long userRoleId) {
		 
		return countAllAttr(userRoleId);
	}
	/**
	 * 统计附魔增加的总属性
	 */
	public Map<String, Long> countAllAttr(Long userRoleId){
		 Map<String, Long> allAttrMap  = new HashMap<>();
		 RoleYaoshenFumo entity = getRoleYaoshenFumo(userRoleId);
			 //基础属性
		   if(entity.getCaoweiMapInfo()!=null && !entity.getCaoweiMapInfo().isEmpty()){
			   Map<String, Long> baseAttr = yaoshenFumoBaseConfigExportService.getAllBaseAttr(entity.getCaoweiMapInfo());
			   ObjectUtil.longMapAdd(allAttrMap, baseAttr);
		   }
		   //额外加成属性
		   if(entity.getCountLevel()>0){
			   Map<String, Long> extraAttr  = yaoshenFumoJiachengConfigExportService.countAttrByLevel(entity.getCountLevel());
			   ObjectUtil.longMapAdd(allAttrMap, extraAttr);
		   }
		return allAttrMap;
		
	}
	/**
	 * 获取当前所有面板所有信息
	 */
	public Object[] getPanelInfo(Long userRoleId){
		YaoshenFumoConfig publicConfig =  getPublicConfig();
		if(publicConfig==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		RoleWrapper  roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper.getLevel()<publicConfig.getOpen()){
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		RoleYaoshenFumo entity =  getRoleYaoshenFumo(userRoleId);
		Map<Integer, Integer[]>  map  = null;
		if(entity.getCaoweiMapInfo()!=null){
			map  = entity.getCaoweiMapInfo();
		}
		//JSON.toJSONString(map)
		return new Object[]{1,entity.getCountLevel(), map};
	}
	
	/**
	 * 获取当前信息by槽位
	 */
	public Object[] getInfoByCaowei(Long userRoleId,int caowei){
		YaoshenFumoConfig publicConfig =  getPublicConfig();
		if(publicConfig==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		RoleWrapper  roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper.getLevel()<publicConfig.getOpen()){
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		RoleYaoshenFumo entity =  getRoleYaoshenFumo(userRoleId);
		
		Object[] data  = null;
		if(entity.getCaoweiMapInfo()!=null){
			data  = entity.getCaoweiMapInfo().get(caowei);
		}
		return new Object[]{1, data};
	}
	
	/**
	 * 客户端请求属性总和
	 */
	public Map<String, Long> getAllAttrToClient(Long userRoleId){
		String stageId = publicRoleStateService.getRolePublicStageId(userRoleId);
		IStage stage = StageManager.getStage(stageId);
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		IBaseAttribute baseAttribute = role.getFightAttribute().getBaseAttributeMap(BaseAttributeType.YAOSHEN_FUMO);
		Map<String,Long> map = null;
		if(baseAttribute!=null){
			map  = baseAttribute.toMap();
		}else{
			map  = this.countAllAttr(userRoleId);
		}
		return map;
	}
	/**
	 * 
	 * @param userRoleId
	 * @param itemIds
	 * @param caowei 槽位  有10个,索引从1开始
	 * @param index 格位从1开始，只有三个格位
	 * @param isAutoGM 是否自动购买
	 * @return
	 */
	public Object[] sj(Long userRoleId,List<Long> itemIds,int caowei,int index){
		if(index>YaoshenConstants.RIGHT_INDEX_NUM ||caowei>YaoshenConstants.CAOWEI_NUM){
			return  null;//容错 
		}
		YaoshenFumoConfig publicConfig =  getPublicConfig();
		RoleWrapper  roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper.getLevel()<publicConfig.getOpen()){
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		RoleYaoshenFumo entity =  getRoleYaoshenFumo(userRoleId);
		int indexLevel  = 0;//当前格位的等级
		Map<Integer, Integer[]>  infoMap = entity.getCaoweiMapInfo();
		if(infoMap!=null && infoMap.get(caowei)!=null){
			indexLevel = infoMap.get(caowei)[index-1];
		}
		Map<Integer, Integer> maxLevelMap = yaoshenFumoBaseConfigExportService.getMaxLevelMap();
		if(maxLevelMap==null ||maxLevelMap.get(caowei)==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(indexLevel+1>maxLevelMap.get(caowei)){
			return AppErrorCode.YAOSHEN_FUMO_MAX_LEVEL; //超过最大等级
		}
		
		Map<Integer, Integer> activeLevelMap = yaoshenFumoBaseConfigExportService.getActiveLevelMap();
		RoleYaoshen roleYaoshen = yaoshenExportService.getRoleYaoshen(userRoleId);
		if(roleYaoshen==null){
			return  null;
		}
		int minBatiLevel  = activeLevelMap.get(1);	//取到激活第一个格位的霸体的最低等级
		if(roleYaoshen.getJieLevel()<minBatiLevel){
			return  AppErrorCode.YAOSHEN_FUMO_CAOWEI_SJ_ERROR_1; 
		}
		int activeLevel  = activeLevelMap.get(caowei);
		if(roleYaoshen.getJieLevel()<activeLevel){
			return  AppErrorCode.YAOSHEN_FUMO_CAOWEI_SJ_NOT_ACTIVE;
		}
		if(activeLevel!=activeLevelMap.get(caowei)){
			return  AppErrorCode.YAOSHEN_FUMO_CAOWEI_SJ_ERROR_2;  
		}
		YaoshenFumoBaseConfig baseConfig = yaoshenFumoBaseConfigExportService.getBaseConfigById(caowei, indexLevel);
		if(baseConfig==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		int needCounsumNum = baseConfig.getConsumeCount(); //这次升级需要消耗的总个数
		int needMoney  = baseConfig.getNeedMoney(); //这次升级需要消耗的银两
		

		Object[] moneyError =  roleBagExportService.isEnought(GoodsCategory.MONEY,needMoney, userRoleId);
		if(moneyError!=null){
			return moneyError; //银两不足
		}
		
		int hasItemNum = 0; //玩家拥有个数
		for (Long e : itemIds) {
			RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
			if (roleItemExport != null) {
				String goodsId = roleItemExport.getGoodsId();
				GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
				if (goodsConfig.getCategory() != GoodsCategory.YS_FUMO_STONE) {
					return AppErrorCode.YAOSHEN_ITEM_ERROR;
				}
				hasItemNum = hasItemNum + roleItemExport.getCount();
			}
		}
		if(hasItemNum<needCounsumNum){
			//数量不足
			return AppErrorCode.YAOSHEN_ITEM_NOT_ENOUGH; 
		}
		
		// 扣除金币
		roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, needMoney, userRoleId,  LogPrintHandle.CONSUME_FUMO_SJ, true, LogPrintHandle.CBZ_FUMO_SJ);
		
		Integer[] arr = null;
		Map<Integer, Integer[]> info = entity.getCaoweiMapInfo();
		if(info==null){
			info  = new HashMap<>();
		}
		if(info.get(caowei)==null){
			arr =  new Integer[]{0,0,0};
			info.put(caowei, arr);
		}else{
			arr = info.get(caowei);
		}
		arr[index-1] = indexLevel +1;
		entity.setCaoweiMapInfo(info);
		entity.setCaoweiInfo(JSON.toJSONString(info));
		entity.setCountLevel(entity.getCountLevel()+1);
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleYaoshenFumoDao.cacheUpdate(entity, userRoleId);
		// 扣道具
		Map<String, Integer> goods = new HashMap<String, Integer>();
		for (Long e : itemIds) {
			if (needCounsumNum > 0) {
				RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
				if(roleItemExport!=null){
					int count = roleItemExport.getCount() > needCounsumNum ? needCounsumNum : roleItemExport.getCount();
					roleBagExportService.removeBagItemByGuid(e, count, userRoleId, GoodsSource.YAOSHEN_FUMO_SJ, true, true);
					goods.put(roleItemExport.getGoodsId(), count);
					needCounsumNum = needCounsumNum - count;
				}
				
			}
		}
		notifyStageAttrChange(userRoleId); //通知属性变更
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(goods, null);
		GamePublishEvent.publishEvent(new YaoshenFumoSjLogEvent(userRoleId,roleYaoshen.getJieLevel(), caowei,index,indexLevel+1,jsonArray,needMoney));
		
		return  new Object[]{1,caowei,index,indexLevel+1 };
	}
	/**
	 * 
	 * @param userRoleId
	 * @param itemIds
	 * @param caowei 槽位  有10个,索引从1开始
	 * @param index 格位从1开始，只有三个格位
	 * @param isAutoGM 是否自动购买
	 * @return
	 */
	@Deprecated
	public Object[] sj_old(Long userRoleId,List<Long> itemIds,int caowei,int index,boolean isAutoGM){
		if(index>YaoshenConstants.RIGHT_INDEX_NUM ||caowei>YaoshenConstants.CAOWEI_NUM){
			return  null;//容错 
		}
		YaoshenFumoConfig publicConfig =  getPublicConfig();
		RoleWrapper  roleWrapper = roleExportService.getLoginRole(userRoleId);
		if(roleWrapper.getLevel()<publicConfig.getOpen()){
			return AppErrorCode.ROLE_LEVEL_NOT_ENOUGH;
		}
		RoleYaoshenFumo entity =  getRoleYaoshenFumo(userRoleId);
		int indexLevel  = 0;//当前格位的等级
		Map<Integer, Integer[]>  infoMap = entity.getCaoweiMapInfo();
		if(infoMap!=null && infoMap.get(caowei)!=null){
			indexLevel = infoMap.get(caowei)[index-1];
		}
		Map<Integer, Integer> maxLevelMap = yaoshenFumoBaseConfigExportService.getMaxLevelMap();
		if(maxLevelMap==null ||maxLevelMap.get(caowei)==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		if(indexLevel+1>maxLevelMap.get(caowei)){
			return AppErrorCode.YAOSHEN_FUMO_MAX_LEVEL; //超过最大等级
		}
		
		Map<Integer, Integer> activeLevelMap = yaoshenFumoBaseConfigExportService.getActiveLevelMap();
		RoleYaoshen roleYaoshen = yaoshenExportService.getRoleYaoshen(userRoleId);
		if(roleYaoshen==null){
			return  null;
		}
		int minBatiLevel  = activeLevelMap.get(1);	//取到激活第一个格位的霸体的最低等级
		if(roleYaoshen.getJieLevel()<minBatiLevel){
			return  AppErrorCode.YAOSHEN_FUMO_CAOWEI_SJ_ERROR_1; 
		}
		int activeLevel  = activeLevelMap.get(caowei);
		if(roleYaoshen.getJieLevel()<activeLevel){
			return  AppErrorCode.YAOSHEN_FUMO_CAOWEI_SJ_NOT_ACTIVE;
		}
		if(activeLevel!=activeLevelMap.get(caowei)){
			return  AppErrorCode.YAOSHEN_FUMO_CAOWEI_SJ_ERROR_2;  
		}
		YaoshenFumoBaseConfig baseConfig = yaoshenFumoBaseConfigExportService.getBaseConfigById(caowei, indexLevel);
		if(baseConfig==null){
			return AppErrorCode.CONFIG_ERROR;
		}
		
		int needCounsumNum = baseConfig.getConsumeCount(); //这次升级需要消耗的总个数
		int needMoney  = baseConfig.getNeedMoney(); //这次升级需要消耗的银两
		int bgoldPrice  = publicConfig.getNeedBgold(); //绑元价格
		int goldPrice  = publicConfig.getNeedGold(); //元宝价格
		

		Object[] moneyError =  roleBagExportService.isEnought(GoodsCategory.MONEY,needMoney, userRoleId);
		if(moneyError!=null){
			return moneyError; //银两不足
		}
		
		int hasItemNum = 0; //玩家拥有个数
		for (Long e : itemIds) {
			RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
			if (roleItemExport != null) {
				String goodsId = roleItemExport.getGoodsId();
				GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
				if (goodsConfig.getCategory() != GoodsCategory.YS_FUMO_STONE) {
					return AppErrorCode.YAOSHEN_ITEM_ERROR;
				}
				hasItemNum = hasItemNum + roleItemExport.getCount();
			}
		}
		if(!isAutoGM && hasItemNum<needCounsumNum){
			//不自动购买 数量不足
			return AppErrorCode.YAOSHEN_ITEM_NOT_ENOUGH; 
		}
		
		int nowNeedBgold = 0; //消耗绑定元宝
		int nowNeedGold = 0; //消耗元宝
		//自动购买
		if(isAutoGM && hasItemNum<needCounsumNum){
			
			int bCount = 0; //绑定元宝可以买的个数
			int rest  = needCounsumNum  - hasItemNum;
			if(rest>0){
				for (int i = 0; i < rest; i++) {
					int checkValue = (bCount + 1) * bgoldPrice;
					Object[] bgoldError =  roleBagExportService.isEnought(GoodsCategory.BGOLD,checkValue, userRoleId);
					if(null != bgoldError){ 
						break;
					}
					bCount++; //统计绑元可以买多少个
				}
				nowNeedBgold  = bCount*bgoldPrice; 
			}
			//计算元宝是否需要消耗
			rest = needCounsumNum-hasItemNum-bCount;
			if(rest>0){
				nowNeedGold  = rest*goldPrice;
				Object[] goldError =  roleBagExportService.isEnought(GoodsCategory.GOLD,nowNeedGold, userRoleId);
				if(null != goldError){ 
					return goldError; //元宝不足
				}
			}
		}
		
		// 扣除金币
		roleBagExportService.decrNumberWithNotify(GoodsCategory.MONEY, needMoney, userRoleId,  LogPrintHandle.CONSUME_FUMO_SJ, true, LogPrintHandle.CBZ_FUMO_SJ);
		
		// 扣除元宝
		if(nowNeedGold>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.GOLD, nowNeedGold, userRoleId, LogPrintHandle.CONSUME_FUMO_SJ, true,LogPrintHandle.CBZ_FUMO_SJ); 
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_YB,nowNeedGold,LogPrintHandle.CONSUME_FUMO_SJ,QQXiaoFeiType.CONSUME_FUMO_SJ,1});
			}
		}
		// 扣除绑定元宝
		if( nowNeedBgold>0){ 
			roleBagExportService.decrNumberWithNotify(GoodsCategory.BGOLD, nowNeedBgold, userRoleId, LogPrintHandle.CONSUME_FUMO_SJ, true,LogPrintHandle.CBZ_FUMO_SJ); 
			//腾讯OSS消费上报
			if(PlatformConstants.isQQ()){
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[]{QqConstants.ZHIFU_BYB,nowNeedBgold,LogPrintHandle.CONSUME_FUMO_SJ,QQXiaoFeiType.CONSUME_FUMO_SJ,1});
			}
		}
		
		Integer[] arr = null;
		Map<Integer, Integer[]> info = entity.getCaoweiMapInfo();
		if(info==null){
			info  = new HashMap<>();
		}
		if(info.get(caowei)==null){
			arr =  new Integer[]{0,0,0};
			info.put(caowei, arr);
		}else{
			arr = info.get(caowei);
		}
		arr[index-1] = indexLevel +1;
		entity.setCaoweiMapInfo(info);
		entity.setCaoweiInfo(JSON.toJSONString(info));
		entity.setCountLevel(entity.getCountLevel()+1);
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleYaoshenFumoDao.cacheUpdate(entity, userRoleId);
		// 扣道具
		Map<String, Integer> goods = new HashMap<String, Integer>();
		for (Long e : itemIds) {
			if (needCounsumNum > 0) {
				RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
				if(roleItemExport!=null){
					int count = roleItemExport.getCount() > needCounsumNum ? needCounsumNum : roleItemExport.getCount();
					roleBagExportService.removeBagItemByGuid(e, count, userRoleId, GoodsSource.YAOSHEN_FUMO_SJ, true, true);
					goods.put(roleItemExport.getGoodsId(), count);
					needCounsumNum = needCounsumNum - count;
				}
				
			}
		}
		notifyStageAttrChange(userRoleId); //通知属性变更
//		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(goods, null);
//		GamePublishEvent.publishEvent(new YaoshenFumoSjLogEvent(userRoleId,roleYaoshen.getJieLevel(), caowei,index,indexLevel+1,jsonArray,
//				needMoney,nowNeedBgold,nowNeedGold));
		
		return  new Object[]{1,caowei,index,indexLevel+1,new Object[]{nowNeedBgold,nowNeedGold}};
	}
	/**
	 * 魔印属性变化
	 * 
	 * @param entity
	 */
	public void notifyStageAttrChange( Long userRoleId) {
		
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_YAOSHEN_HUMO_ATTR_CHANGE,countAllAttr(userRoleId));
	}
	/**
	 * 获得Entity对象
	 * @param userRoleId
	 * @return
	 */
	public RoleYaoshenFumo getRoleYaoshenFumo(Long userRoleId) {
		RoleYaoshenFumo entity = roleYaoshenFumoDao.cacheLoad(userRoleId, userRoleId);
			if(entity==null){
				entity = new RoleYaoshenFumo();
				entity.setUserRoleId(userRoleId);
				entity.setCountLevel(0);
				entity.setCaoweiInfo(null);
				entity.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
				entity.setUpdateTime(GameSystemTime.getSystemMillTime());
				roleYaoshenFumoDao.cacheInsert(entity, userRoleId);
			} 
		return entity;
	}
	/**
	 * 公共配置
	 * @return
	 */
	public YaoshenFumoConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_YAOSHEN_FUMO);
	}

}
