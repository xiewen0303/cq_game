package com.junyou.bus.xinwen.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.junyou.bus.rfbactivity.service.ActivityServiceFacotry;
import com.junyou.bus.rfbactivity.util.ReFaBuUtil;
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
import com.junyou.bus.xinwen.configure.export.XinwenConfigExportService;
import com.junyou.bus.xinwen.configure.export.XinwenJichuConfig;
import com.junyou.bus.xinwen.configure.export.XinwenQNDConfigExportService;
import com.junyou.bus.xinwen.constants.XinwenConstants;
import com.junyou.bus.xinwen.dao.RoleTangbaoXinwenDao;
import com.junyou.bus.xinwen.entity.RoleTangbaoXinwen;
import com.junyou.bus.xinwen.vo.XinwenRankVo;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.UpgradeXinwenLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.XinwenPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class XinwenService {

	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private AccountExportService accountExportService;

	@Autowired
	private XinwenConfigExportService xinwenConfigExportService;
	@Autowired
	private RoleTangbaoXinwenDao roleTangbaoXinwenDao;
	@Autowired
	private XinwenQNDConfigExportService xinwenQNDConfigExportService;

	/**
	 * 初始化
	 * 
	 * @param userRoleId
	 * @return
	 */
	public RoleTangbaoXinwen initRoleTangbaoXinwen(Long userRoleId) {
		List<RoleTangbaoXinwen> list = roleTangbaoXinwenDao.initRoleTangbaoXinwen(userRoleId);
		if (list == null || list.size() == 0) {
			return null;
		}
		RoleTangbaoXinwen entity = list.get(0);
		return entity;
	}
	public List<XinwenRankVo> getXinwenRankVo(int limit) {
		return roleTangbaoXinwenDao.getXinwenRankVo(limit);
	}
	/**
	 * 玩家登陆后统计属性
	 */
	public Map<String, Long> getXinwenAttributeAfterLogin(Long userRoleId) {
		// 先初始化一次
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper != null) {
			checkLevelAndActiveXinwen(userRoleId, roleWrapper.getLevel());
		}
		Map<String, Long> attrMap = null;
		if (roleWrapper != null) {
			attrMap = getXinwenAttribute(userRoleId);
		}
		return attrMap;
	}

	/**
	 * 获取 信息
	 */
	public Object[] getInfo(Long userRoleId) {
		RoleTangbaoXinwen roleTangbaoXinwen = getRoleTangbaoXinwen(userRoleId);
		if (roleTangbaoXinwen == null) {
			return AppErrorCode.XINWEN_NOT_OPEN;
		}
		XinwenJichuConfig xinwenJichuConfig = xinwenConfigExportService.load(roleTangbaoXinwen.getJieLevel(), roleTangbaoXinwen.getCengLevel());
		if (xinwenJichuConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<String, Long> attrMap = getXinwenAttribute(userRoleId);
		return new Object[] {1, xinwenJichuConfig.getId(), roleTangbaoXinwen.getQndNum(), roleTangbaoXinwen.getCzdNum(), attrMap };
	}

	/**
	 * 客户端拉取某一阶的属性
	 */
	public Map<String, Long> getXinwenAttributeByJie(Long userRoleId, int jie) {
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper == null || jie <= 0) {
			return null;
		}
		RoleTangbaoXinwen entity = getRoleTangbaoXinwen(userRoleId);
		if (entity == null || jie > entity.getJieLevel()) {
			return null;
		}
		int cengLevel = 0;
		// 历史阶属性，算出满层数
		Map<Integer, XinwenJichuConfig> allMap = xinwenConfigExportService.loadByJie(jie);
		if (entity.getJieLevel() == jie) {
			cengLevel = entity.getCengLevel();
		} else {
			int maxCeng = 0;
			for (Entry<Integer, XinwenJichuConfig> entry : allMap.entrySet()) {
				int ceng = entry.getValue().getId2(); // 获得层
				if (ceng > maxCeng) {
					maxCeng = ceng;
				}
			}
			cengLevel = maxCeng;
		}
		return getXinwenAttribute(jie, cengLevel, entity.getQndNum(), entity.getCzdNum());
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
		XinwenPublicConfig publicConfig = getPublicConfig();

		if (roleWrapper.getLevel() < publicConfig.getOpen()) {
			return AppErrorCode.ROLE_LEVEL_ERROR;
		}
		RoleTangbaoXinwen roleTangbaoXinwen = getRoleTangbaoXinwen(userRoleId);
		if (roleTangbaoXinwen == null) {
			return AppErrorCode.XINWEN_NOT_OPEN;
		}
		int currentJie = roleTangbaoXinwen.getJieLevel();
		int currentCeng = roleTangbaoXinwen.getCengLevel();
		if (currentCeng == xinwenConfigExportService.getMAX_CENG() && currentJie == xinwenConfigExportService.getMAX_JIE()) {
			return AppErrorCode.XINWEN_TOP_LEVEL;
		}
		XinwenJichuConfig jichuConfig = xinwenConfigExportService.load(currentJie, currentCeng);
		if (jichuConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		int hasItemNum = 0;
		for (Long e : itemIds) {
			RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
			if (roleItemExport == null) {
				return AppErrorCode.XINWEN_ITEM_NOT_ENOUGH;
			}
			String goodsId = roleItemExport.getGoodsId();
			GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
			if (goodsConfig.getCategory() != GoodsCategory.XINWEN_ITEM) {
				return AppErrorCode.XINWEN_ITEM_ERROR;
			}
			hasItemNum = hasItemNum + roleItemExport.getCount();
		}
		int activeateItemNum = jichuConfig.getCount();
		if (hasItemNum < jichuConfig.getCount()) {
			return AppErrorCode.XINWEN_ITEM_NOT_ENOUGH;
		}
		int money = jichuConfig.getMoney();
		// 验证银两
		Object[] result = accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, money, userRoleId, LogPrintHandle.CONSUME_TANGBAO_XINWEN_SJ, true,
				LogPrintHandle.CBZ_TANGBAO_XINWEN_SJ);
		if (result != null) {
			return result;
		}
		// 扣道具
		Map<String, Integer> goods = new HashMap<String, Integer>();
		for (Long e : itemIds) {
			if (activeateItemNum > 0) {
				RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, e);
				int count = roleItemExport.getCount() > activeateItemNum ? activeateItemNum : roleItemExport.getCount();
				roleBagExportService.removeBagItemByGuid(e, count, userRoleId, GoodsSource.TANGBAO_XINWEN_SJ, true, true);
				goods.put(roleItemExport.getGoodsId(), count);
				activeateItemNum = activeateItemNum - count;
			}
		}
		int nextCeng = currentCeng >= xinwenConfigExportService.getMAX_CENG() ? 1 : currentCeng + 1;
		int nextJie = currentCeng >= xinwenConfigExportService.getMAX_CENG() ? currentJie + 1 : currentJie;
		XinwenJichuConfig nextJichuConfig = xinwenConfigExportService.load(nextJie, nextCeng);
		roleTangbaoXinwen.setJieLevel(nextJie);
		roleTangbaoXinwen.setCengLevel(nextCeng);
		roleTangbaoXinwen.setUpdateTime(GameSystemTime.getSystemMillTime());
		roleTangbaoXinwenDao.cacheUpdate(roleTangbaoXinwen, userRoleId);
		notifyStageAttrChange(roleTangbaoXinwen);
		JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(goods, null);
		GamePublishEvent.publishEvent(new UpgradeXinwenLogEvent(userRoleId, jsonArray, nextJie, nextCeng));
		// 公告通知
		if (nextJichuConfig.getGgopen() == 1) {
			BusMsgSender.send2All(
							ClientCmdType.NOTIFY_CLIENT_ALERT4,
							new Object[] { AppErrorCode.TANGBAO_XINWEN_NOTICE_1,
									new Object[] { roleWrapper.getName(), roleTangbaoXinwen.getJieLevel(),
									roleTangbaoXinwen.getCengLevel() } });
			BusMsgSender.send2One(userRoleId, ClientCmdType.NOTIFY_CLIENT_ALERT3,
					new Object[] { AppErrorCode.TANGBAO_XINWEN_NOTICE_2, new Object[] { roleTangbaoXinwen.getJieLevel() } });
		}

		//排行升级提醒活动角标
		ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.TANGBAOXINWEN_TYPE);

		return new Object[] { AppErrorCode.SUCCESS, nextJichuConfig.getId() };
	}
	
	public Object[] sjByItem(Long userRoleId,Integer minLevel,Integer maxLevel,Integer addCeng){
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);

		if (roleWrapper == null) {
			return AppErrorCode.ROLE_IS_NOT_ONLINE;
		}
		RoleTangbaoXinwen info = getRoleTangbaoXinwen(userRoleId);
		if (info == null) {
			return AppErrorCode.XINWEN_NOT_OPEN;
		}else{
			int currentJie = info.getJieLevel();
			int currentCeng = info.getCengLevel();
			//最高级
			if (currentCeng == xinwenConfigExportService.getMAX_CENG()&& currentJie == xinwenConfigExportService.getMAX_JIE()) {
				return AppErrorCode.YAOSHEN_TOP_LEVEL;
			}
			if(currentJie < minLevel){
				return AppErrorCode.XINWEN_LEVEL_LIMIT_CAN_NOT_USE;
			}else if(currentJie > maxLevel){
				return AppErrorCode.YAOSHEN_SHENGXING_MAX_CHAOCHU;
			}else{
				boolean isGongGao = false;
				int configId = 0;
				for (int i = 1; i <= addCeng; i++) {
					int nextCeng = currentCeng >= xinwenConfigExportService.getMAX_CENG() ? 1 : currentCeng + 1;
					int nextJie = currentCeng >= xinwenConfigExportService.getMAX_CENG() ? currentJie + 1 : currentJie;
					XinwenJichuConfig nextJichuConfig = xinwenConfigExportService.load(nextJie, nextCeng);
					info.setJieLevel(nextJie);
					info.setCengLevel(nextCeng);
					if(nextJichuConfig.getGgopen()==1){
						isGongGao = true;
					}
					configId = nextJichuConfig.getId();
				}
				info.setUpdateTime(GameSystemTime.getSystemMillTime());
				roleTangbaoXinwenDao.cacheUpdate(info, userRoleId);
				notifyStageAttrChange(info);
				//通知前端
				BusMsgSender.send2One(userRoleId, ClientCmdType.XINWEN_SJ, new Object[] { AppErrorCode.SUCCESS, configId });
				//公告通知
				if(isGongGao){
					BusMsgSender.send2All(
									ClientCmdType.NOTIFY_CLIENT_ALERT4,
									new Object[] { AppErrorCode.TANGBAO_XINWEN_NOTICE_1,
											new Object[] { roleWrapper.getName(), info.getJieLevel(),
											info.getCengLevel() } });
				}

				//排行升级提醒活动角标
				ActivityServiceFacotry.checkRankIconFlag(userRoleId, ReFaBuUtil.TANGBAOXINWEN_TYPE);
			}
			
		}
		return null;
	}
	/**
	 * 等级到了自动激活糖宝心纹
	 */
	public void checkLevelAndActiveXinwen(Long userRoleId, int level) {
		try {
			XinwenPublicConfig publicConfig = getPublicConfig();
			if(publicConfig==null){
				ChuanQiLog.error("***公共配置表糖宝心纹信息不存在***");
				return ;
			}
			RoleTangbaoXinwen entity = getRoleTangbaoXinwen(userRoleId);
			if (level >= publicConfig.getOpen() && entity == null) {
				entity = this.createEntity(userRoleId);
				roleTangbaoXinwenDao.cacheInsert(entity, userRoleId);
			}
		} catch (Exception e) {
			ChuanQiLog.error("激活糖宝心纹失败：{}", e);
		}
	}

	/**
	 * 通知场景属性变化
	 * 
	 * @param entity
	 */
	public void notifyStageAttrChange(RoleTangbaoXinwen entity) {
		int currentJie = entity.getJieLevel();
		int currentCeng = entity.getCengLevel();
		BusMsgSender.send2Stage(entity.getUserRoleId(), InnerCmdType.INNER_TANGBAO_XINWEN_ATTR_CHANGE,
				new Object[] { currentJie, currentCeng, entity.getQndNum(), entity.getCzdNum() });
	}

	/**
	 * 获得心纹属性
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getXinwenAttribute(Long userRoleId) {
		RoleTangbaoXinwen info = getRoleTangbaoXinwen(userRoleId);
		if (info == null) {
			return null;
		}
		return getXinwenAttribute(info.getJieLevel(), info.getCengLevel(), info.getQndNum(), info.getCzdNum());
	}

	/**
	 * 获得心纹属性
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Map<String, Long> getXinwenAttribute(Integer jie, Integer ceng, Integer qndNum, Integer czdNum) {
		Map<String, Long> ret = new HashMap<String, Long>();
		Map<String, Long> jichuAttr = xinwenConfigExportService.getAttribute(jie, ceng);
		if (jichuAttr != null && jichuAttr.size() > 0) {
			ObjectUtil.longMapAdd(ret, jichuAttr);
		}
		Long speed = ret.remove(EffectType.x19.name());
		if (czdNum > 0) {
			String czdId = xinwenConfigExportService.getCZD_ID();
			float multi = getCzdMulti(czdId) * czdNum / 100f + 1f;
			ObjectUtil.longMapTimes(ret, multi);
		}
		if (qndNum > 0) {
			Map<String, Map<String, Long>> map = xinwenQNDConfigExportService.getConfigMap();
			for (int i = 0; i < qndNum; i++) {
				ObjectUtil.longMapAdd(ret, map.get(XinwenConstants.XINWEN_QND));
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
			List<String> goodsIds = goodsConfigExportService.loadIdsById1(czdId);
			if (goodsIds.size() > 0) {
				GoodsConfig config = goodsConfigExportService.loadById(goodsIds.get(0));
				multi = config.getData1();
			}
		}
		return multi;
	}

	/**
	 * 使用各种丹的时候检测
	 * type=1表示检测潜能丹，type=2表示检测成长丹
	 */
	private Object[] checkDan(long userRoleId,long guid, int count,int type){
		RoleTangbaoXinwen entity = getRoleTangbaoXinwen(userRoleId);
		if (entity == null) {
			return AppErrorCode.XINWEN_NOT_OPEN;
		}
		XinwenJichuConfig jichuConfig = xinwenConfigExportService.load(entity.getJieLevel(), entity.getCengLevel());
		if (jichuConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		RoleItemExport roleItemExport = roleBagExportService.getBagItemByGuid(userRoleId, guid);
		if (roleItemExport == null) {
			return AppErrorCode.XINWEN_ITEM_NOT_ENOUGH;
		}
		String goodsId = roleItemExport.getGoodsId();
		GoodsConfig goodsConfig = goodsConfigExportService.loadById(goodsId);
		
		if(type==XinwenConstants.QND_USE_TYPE){
			//使用潜能丹
			if (goodsConfig.getCategory() != GoodsCategory.XINWEN_QND) {
				return AppErrorCode.XINWEN_ITEM_ERROR;
			}
//			&& goodsConfig.getCategory() != GoodsCategory.WANNENG_QND
			if (jichuConfig.getQndopen() != XinwenConstants.QND_OPEN_FLAG) {
				return AppErrorCode.XINWEN_LEVEL_LIMIT_CAN_NOT_USE;
			}
			if (entity.getQndNum().intValue() + count > jichuConfig.getQndmax()) {
				return AppErrorCode.XINWEN_QND_MAX_NUM;
			}
		}else{
			//使用成长丹
			if (goodsConfig.getCategory() != GoodsCategory.XINWEN_CZD ) {
//				&& goodsConfig.getCategory() != GoodsCategory.WANNENG_CZD
				return AppErrorCode.XINWEN_ITEM_ERROR;
			}
			if (jichuConfig.getCzdopen() != XinwenConstants.CZD_OPEN_FLAG) {
				return AppErrorCode.XINWEN_LEVEL_LIMIT_CAN_NOT_USE;
			}
			if (entity.getCzdNum().intValue() + count > jichuConfig.getCzdmax()) {
				return AppErrorCode.XINWEN_CZD_MAX_NUM;
			}
			
		}
		return null;
	}
	
	/**
	 * 使用潜能丹
	 * 
	 * @param userRoleId
	 * @param count=1 默认使用一个
	 * @return
	 */
	public Object[] useQND(long userRoleId,long guid, int count) {
		Object[] ret = checkDan(userRoleId,guid,count,XinwenConstants.QND_USE_TYPE);
		if(ret!=null){
			return ret;
		}
		// 背包减掉一个道具
		BagSlots bagSlots = roleBagExportService.removeBagItemByGuid(guid, 1, userRoleId, GoodsSource.TANGBAO_XINWEN_QND_USE, true, true);
		if (!bagSlots.isSuccee()) {
			return bagSlots.getErrorCode();
		}
		RoleTangbaoXinwen entity = getRoleTangbaoXinwen(userRoleId);
		entity.setQndNum(count + entity.getQndNum());
		roleTangbaoXinwenDao.cacheUpdate(entity, userRoleId);
		notifyStageAttrChange(entity);
		return new Object[]{1,entity.getQndNum()};
	}

	/**
	 * 使用成长丹
	 * count=1 默认使用一个
	 * @param userRoleId
	 * @param count
	 * @return
	 */
	public Object[] useCZD(long userRoleId,long guid, int count) {
		Object[] ret = checkDan(userRoleId,guid,count,XinwenConstants.CZD_USE_TYPE);
		if(ret!=null){
			return ret;
		}
		// 背包减掉一个道具
		BagSlots bagSlots = roleBagExportService.removeBagItemByGuid(guid, 1, userRoleId, GoodsSource.TANGBAO_XINWEN_CZD_USE, true, true);
		if (!bagSlots.isSuccee()) {
			return bagSlots.getErrorCode();
		}
		RoleTangbaoXinwen entity = getRoleTangbaoXinwen(userRoleId);
		entity.setCzdNum(count + entity.getCzdNum());
		roleTangbaoXinwenDao.cacheUpdate(entity, userRoleId);
		notifyStageAttrChange(entity);
		return new Object[]{1,entity.getCzdNum()};
	}

	/**
	 * 获取心纹的阶数
	 */
	public int getXinwenJie(Long userRoleId) {
		RoleTangbaoXinwen roleTangbaoXinwen = getRoleTangbaoXinwen(userRoleId);
		if (roleTangbaoXinwen == null) {
			return 0;
		}
		return roleTangbaoXinwen.getJieLevel();
	}

	/**
	 * 获得魔纹Entity对象
	 * 
	 * @param userRoleId
	 * @return
	 */
	public RoleTangbaoXinwen getRoleTangbaoXinwen(Long userRoleId) {
		// 玩家登陆后要先放进缓存
		RoleTangbaoXinwen ret = roleTangbaoXinwenDao.cacheLoad(userRoleId, userRoleId);
		return ret;
	}

	/**
	 * 创建entity
	 */
	private RoleTangbaoXinwen createEntity(Long userRoleId) {
		RoleTangbaoXinwen entity = new RoleTangbaoXinwen();
		entity.setUserRoleId(userRoleId);
		entity.setCzdNum(0);
		entity.setQndNum(0);
		entity.setCengLevel(1);
		entity.setJieLevel(1);
		entity.setUpdateTime(GameSystemTime.getSystemMillTime());
		notifyStageAttrChange(entity);// 通知加属性
		return entity;
	}

	/**
	 * 公共配置
	 * 
	 * @return
	 */
	public XinwenPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_XINWEN);
	}

}
