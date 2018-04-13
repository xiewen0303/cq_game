package com.junyou.bus.shenqi.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.account.entity.RoleAccountWrapper;
import com.junyou.bus.account.export.AccountExportService;
import com.junyou.bus.assign.export.AssignExportService;
import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.chibang.service.ChiBangService;
import com.junyou.bus.equip.configure.export.ZhanLiXiShuConfigExportService;
import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.bus.jingji.util.FightPowerType;
import com.junyou.bus.online.export.RoleOnlineExportService;
import com.junyou.bus.platform.qq.constants.QQXiaoFeiType;
import com.junyou.bus.platform.qq.constants.QqConstants;
import com.junyou.bus.platform.utils.PlatformConstants;
import com.junyou.bus.rolebusiness.configure.export.RoleBusinessInfoExportService;
import com.junyou.bus.rolestage.export.RoleStageExportService;
import com.junyou.bus.serverinfo.export.ServerInfoServiceManager;
import com.junyou.bus.shenqi.ShenQiConstants;
import com.junyou.bus.shenqi.configure.export.ShenQiShuXingConfig;
import com.junyou.bus.shenqi.configure.export.ShenQiShuXingConfigExportService;
import com.junyou.bus.shenqi.configure.export.ShenQiXiLianConfig;
import com.junyou.bus.shenqi.configure.export.ShenQiXiLianConfigExportService;
import com.junyou.bus.shenqi.configure.export.ShenQiXiLianMaxConfigExportService;
import com.junyou.bus.shenqi.configure.export.ShuXingZhuFuConfig;
import com.junyou.bus.shenqi.configure.export.ShuXingZhuFuConfigExportService;
import com.junyou.bus.shenqi.dao.ShenQiInfoDao;
import com.junyou.bus.shenqi.entity.ShenQiInfo;
import com.junyou.bus.shenqi.filter.ShenQiFilter;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.bus.vip.export.RoleVipInfoExportService;
import com.junyou.bus.vip.util.RoleVipWrapper;
import com.junyou.bus.xianjian.service.XianjianService;
import com.junyou.bus.xiulianzhilu.constants.XiuLianConstants;
import com.junyou.bus.zhanjia.service.ZhanJiaService;
import com.junyou.bus.zuoqi.service.ZuoQiService;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.ActivateShenqiLogEvent;
import com.junyou.event.ShenqiXilianLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.ShenQiPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.ShenQiXiLianPublicConfig;
import com.junyou.gameconfig.utils.GoodsCategory;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.common.CovertObjectUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.lottery.RandomUtil;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class ShenQiService implements IFightVal{
	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		long result = 0;

		if(fightPowerType == FightPowerType.SHENQI_JH){
			List<ShenQiInfo> shenqiS = getRoleActivatedShenqi(userRoleId);
			if(shenqiS == null){
				return 0;
			}
			for (ShenQiInfo info: shenqiS) {
				ShenQiShuXingConfig config = shenQiShuXingConfigExportService.loadById(info.getShenQiId());
				if(config == null){
					continue;
				}
				result += CovertObjectUtil.getZplus(config.getAttrs());
			}
		}else if(fightPowerType == FightPowerType.SHENQI_XL){
			List<ShenQiInfo> shenqiS = getRoleActivatedShenqi(userRoleId);
			if(shenqiS == null){
				return 0;
			}
			float zplus = 0f;
			for (ShenQiInfo shenqiInfo : shenqiS) {
				Map<String, Long>  att = shenqiInfo.getCurrentInfoMap();
				if(att == null){
					continue;
				}
				for (Entry<String, Long> entry : att.entrySet()) {
					Float xs = zhanLiXiShuConfigExportService.getZLXS(entry.getKey());
					if(xs != null){
						zplus += xs * entry.getValue();
					}
				}
			}
			return CovertObjectUtil.obj2long(zplus);
		}else if(fightPowerType == FightPowerType.SHENQI_ZF){

			//神器祝福属性
			Integer zuoqiLevel = zuoQiService.getZuoQiInfo(userRoleId) == null?null:zuoQiService.getZuoQiInfo(userRoleId).getZuoqiLevel();
			Integer chibangLevel = chiBangService.getChiBangInfo(userRoleId)==null?null:chiBangService.getChiBangInfo(userRoleId).getChibangLevel();
			Integer xianjianLevel = xianjianService.getXianJianInfo(userRoleId)==null?null:xianjianService.getXianJianInfo(userRoleId).getXianjianLevel();
			Integer zhanjiaLevel = zhanJiaService.getXianJianInfo(userRoleId)==null?null:zhanJiaService.getXianJianInfo(userRoleId).getXianjianLevel();

			Map<Integer,Integer> typeLevels = new HashMap<>();
			typeLevels.put(ShenQiConstants.ZHUFU_TYPE_ZUOQI, zuoqiLevel);
			typeLevels.put(ShenQiConstants.ZHUFU_TYPE_CHIBANG, chibangLevel);
			typeLevels.put(ShenQiConstants.ZHUFU_TYPE_XIANJIAN, xianjianLevel);
			typeLevels.put(ShenQiConstants.ZHUFU_TYPE_ZHANJIA, zhanjiaLevel);

			//TODO  等待加模块
			// Integer wuQiLevel = role.getBusinessData().getZhanjia()==null?null:role.getBusinessData().getZhanjia().getXianjianLevel();
			// typeLevels.put(ShenQiConstants.ZHUFU_TYPE_WUQI, wuQiLevel);

			Map<String,Long> shenqiZhufuAttribute = getActivatedShenqiZhufuAttr(userRoleId,typeLevels);
			if(shenqiZhufuAttribute != null){
				result = CovertObjectUtil.getZplus(shenqiZhufuAttribute);
			}
		}else{
			ChuanQiLog.error("not exist "+this.getClass().getSimpleName()+"fightPowerType="+fightPowerType);
		}

		return result;
	}


	@Autowired
	private ZhanJiaService zhanJiaService;

	@Autowired
	private XianjianService xianjianService;

	@Autowired
	private ChiBangService chiBangService;

	@Autowired
	private ZuoQiService zuoQiService;
	@Autowired
	private RoleBusinessInfoExportService roleBusinessInfoExportService;

	@Autowired
	private ShenQiInfoDao shenqiInfoDao;

	@Autowired
	private AssignExportService assignExportService;

	@Autowired
	private RoleBagExportService roleBagExportService;

	@Autowired
	private RoleOnlineExportService roleOnlineExportService;
	

	@Autowired
	private ShenQiShuXingConfigExportService shenQiShuXingConfigExportService;

	@Autowired
	private RoleStageExportService roleStageExportService;

	@Autowired
	private ShuXingZhuFuConfigExportService shuXingZhuFuConfigExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private AccountExportService accountExportService;
	@Autowired
	private RoleVipInfoExportService roleVipInfoExportService;
	@Autowired
	private ShenQiXiLianConfigExportService shenQiXiLianConfigExportService;
	@Autowired
	private ShenQiXiLianMaxConfigExportService shenQiXiLianMaxConfigExportService;
	@Autowired
	private ZhanLiXiShuConfigExportService zhanLiXiShuConfigExportService;
	private Map<String, Long> map  = new HashMap<>();
	
	
	public  List<ShenQiInfo> getRoleActivatedShenqi(Long userRoleId){
		return shenqiInfoDao.cacheLoadAll(userRoleId);
	}
	
	public ShenQiInfo getRoleShenQiInfo(Long userRoleId,Integer shenQiId){
		List<ShenQiInfo> entity = shenqiInfoDao.cacheLoadAll(userRoleId, new ShenQiFilter(shenQiId));
		if(entity != null && entity.size() > 0){
			return entity.get(0);
		}
		return null;
	}
	
	
	/**
	 * 获得玩家神器信息
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] getShenqiInfo(Long userRoleId) {
		List<ShenQiShuXingConfig> configList = shenQiShuXingConfigExportService.getAll();
		Object[] ret = new Object[configList.size()];
		Integer wearingShenqi = roleStageExportService.getRoleWearingShenqi(userRoleId);
		List<ShenQiInfo> activatedShenqi = shenqiInfoDao.cacheLoadAll(userRoleId);
		
		
		for (int i = 0; i < configList.size(); i++) {
			ShenQiShuXingConfig config = configList.get(i);
			int id = config.getId().intValue();
			int status = 0;
			if (wearingShenqi != null && wearingShenqi.intValue() == id) {
				status = ShenQiConstants.SHENQI_STATUS_WEARING;
			} else {
				boolean activated = false;
				if (activatedShenqi != null) {
					for (ShenQiInfo shenqi : activatedShenqi) {
						if (shenqi.getShenQiId().intValue() == config.getId().intValue()) {
							activated = true;
							break;
						}
					}
				}
				if (!activated) {
					boolean canActivate = canActivate(userRoleId, config, activatedShenqi);
					if (canActivate) {
						status = ShenQiConstants.SHENQI_STATUS_CAN_ACTIVATE;
					} else {
						status = ShenQiConstants.SHENQI_STATUS_CAN_NOT_ACTIVATE;
					}
				} else {
					status = ShenQiConstants.SHENQI_STATUS_ACTIVATED;
				}
			}
			//洗练属性
			Map<String, Long> attrMap =null; //this.initAttrMap();
			for (ShenQiInfo shenqi : activatedShenqi) {
				if (shenqi.getShenQiId().intValue() == config.getId().intValue()) {
					attrMap =shenqi.getCurrentInfoMap();
					break;
				}
			}
			 if(attrMap==null){
				 attrMap  = initAttrMap();
			 }
			ret[i] = new Object[] { id, status, attrMap.get("x2"),  attrMap.get("x5"), 
					 attrMap.get("x8"),  attrMap.get("x16"),
					 attrMap.get("x11"),  attrMap.get("x14"), attrMap.get("x18") };
		}
		return ret;
	}
	private Map<String, Long> initAttrMap(){
		if(map.size()==0){
			map.put("x2", 0L); 
			map.put("x5", 0L); 
			map.put("x8", 0L); 
			map.put("x16", 0L); 
			map.put("x11", 0L); 
			map.put("x14", 0L); 
			map.put("x18", 0L); 
		}
		return map;
	}

	public Object[] getTotalAssignDays(Long userRoleId) {
		int assignAll = assignExportService.getAssignAll(userRoleId);
		return new Object[] { assignAll };
	}

	public long getTotalOnlineTime(Long userRoleId) {
		long totlaOnlineTime = roleOnlineExportService.getTotalOnlineTime(userRoleId);
		return totlaOnlineTime;
	}

	/**
	 * 判断神器是否可激活
	 * 
	 * @param userRoleId
	 * @param config
	 * @param activatedShenqi
	 * @return
	 */
	public boolean canActivate(Long userRoleId, ShenQiShuXingConfig config, List<ShenQiInfo> activatedShenqi) {
		List<Integer> conditionList = config.getConditionList();
		List<Integer> needCountList = config.getNeedCountList();
		for (int i = 0; i < conditionList.size(); i++) {
			int condition = conditionList.get(i).intValue();
			if (condition == ShenQiConstants.CONDITION_TYPE_ASSIGN) {
				int assignAll = assignExportService.getAssignAll(userRoleId);
				int needCount = needCountList.get(i).intValue();
				if (needCount <= assignAll) {
					return true;
				}
			} else if (condition == ShenQiConstants.CONDITION_TYPE_ONLINE) {
				long totlaOnlineTime = roleOnlineExportService.getTotalOnlineTime(userRoleId) / 60000L;
				int needCount = needCountList.get(i).intValue();
				if (needCount <= totlaOnlineTime) {
					return true;
				}
			} else if (condition == ShenQiConstants.CONDITION_TYPE_ITEM) {
				// 判断有无道具
				Object[] checkResult = roleBagExportService.checkRemoveBagItemByGoodsId(config.getNeedItem(), userRoleId);
				if (checkResult == null) {
					return true;
				}
			} else if (condition == ShenQiConstants.CONDITION_TYPE_SPECIAL) {
				// 先判断是否已经获得其他所有的神器
				if (activatedShenqi != null && activatedShenqi.size() == shenQiShuXingConfigExportService.getAll().size() - 1) {
					Object[] checkResult = roleBagExportService.checkRemoveBagItemByGoodsId(config.getNeedItem(), userRoleId);
					if (checkResult == null) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public List<ShenQiInfo> initShenQiInfo(Long userRoleId) {
		return shenqiInfoDao.initShenQiInfo(userRoleId);
	}

	/**
	 * 激活神器
	 * 
	 * @param userRoleId
	 * @param shenqiId
	 * @return
	 */
	public Object[] activateShenqi(Long userRoleId, Integer shenqiId) {
		ShenQiInfo shenqiInfo = getShenQiInfo(userRoleId, shenqiId);
		if (shenqiInfo != null) {
			// 此神器已经激活
			return AppErrorCode.SHENQI_IS_ACTIVATED;
		}
		ShenQiShuXingConfig config = shenQiShuXingConfigExportService.loadById(shenqiId);
		if (config == null) {
			// 此神器不存在
			return AppErrorCode.SHENQI_NOT_EXISTS;
		}
		List<ShenQiInfo> activatedShenqi = null;
		if (config.getConditionList().contains(ShenQiConstants.CONDITION_TYPE_SPECIAL)) {
			activatedShenqi = shenqiInfoDao.cacheLoadAll(userRoleId);
		}
		// 校验是否满足激活条件
		boolean canActivate = canActivate(userRoleId, config, activatedShenqi);
		if (!canActivate) {
			return AppErrorCode.SHENQI_CAN_NOT_ACTIVATE;
		}
		JSONArray jsonArray = null;
		if (config.getConditionList().contains(ShenQiConstants.CONDITION_TYPE_ITEM) || config.getConditionList().contains(ShenQiConstants.CONDITION_TYPE_SPECIAL)) {
			// 如果激活条件为第三种或者第四种，扣除道具
			// 当为第三种情况 无论在线满足与否，道具都扣掉
			if (config.getNeedItem() != null) {
				BagSlots removeResult = roleBagExportService.removeBagItemByGoods(config.getNeedItem(), userRoleId, GoodsSource.ACTIVATE_SHENQI, true, true);
				if (removeResult.getErrorCode() == null) {
					jsonArray = LogPrintHandle.getLogGoodsParam(config.getNeedItem(), null);
				}
			}
		}
		activateShenqiWithoutCheck(userRoleId, shenqiId, jsonArray);
		// 成就
		try {
			BusMsgSender.send2BusInner(userRoleId, InnerCmdType.CHENGJIU_CHARGE, new Object[] { GameConstants.CJ_SHENBING, 0 });
			// roleChengJiuExportService.tuisongChengJiu(userRoleId,
			// GameConstants.CJ_SHENBING, 0);
		} catch (Exception e) {
			ChuanQiLog.error("", e);
		}
		return AppErrorCode.OK;
	}

	public void activateShenqiByItem(Long userRoleId, Integer shenqiId, JSONArray jsonArray) {
		ShenQiInfo shenqiInfo = getShenQiInfo(userRoleId, shenqiId);
		if (shenqiInfo != null) {
			// 此神器已经激活
			return;
		}
		activateShenqiWithoutCheck(userRoleId, shenqiId, jsonArray);
	}

	public void activateShenqiWithoutCheck(Long userRoleId, Integer shenqiId, JSONArray jsonArray) {
		saveShenqi(userRoleId, shenqiId);
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.SHENQI_ACTIVATE, new Object[] { shenqiId, getActivatedShenqiNum(userRoleId) });
		GamePublishEvent.publishEvent(new ActivateShenqiLogEvent(userRoleId, shenqiId, jsonArray));
		//修炼任务
		BusMsgSender.send2BusInner(userRoleId, InnerCmdType.INNER_XIULIAN_TASK_CHARGE, new Object[] {XiuLianConstants.JIHUO_SHENGQI, null});
	}

	/**
	 * 保存神器
	 * 
	 * @param userRoleId
	 * @param shenqiId
	 */
	public void saveShenqi(Long userRoleId, Integer shenqiId) {
		ShenQiInfo pojo = new ShenQiInfo();
		pojo.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		pojo.setUserRoleId(userRoleId);
		pojo.setShenQiId(shenqiId);
		shenqiInfoDao.cacheInsert(pojo, userRoleId);
	}

	/**
	 * 佩戴神器
	 * 
	 * @param userRoleId
	 * @param shenqiId
	 *            shenqiId为0表示脱掉
	 * @return
	 */
	public Object[] wearShenqi(Long userRoleId, Integer shenqiId) {
		Integer wearingShenqi = roleStageExportService.getRoleWearingShenqi(userRoleId);
		if (wearingShenqi.intValue() == shenqiId.intValue()) {
			return null;
		}
		if (shenqiId.intValue() != 0) {
			ShenQiInfo shenqiInfo = getShenQiInfo(userRoleId, shenqiId);
			if (shenqiInfo == null) {
				return AppErrorCode.SHENQI_IS_NOT_ACTIVATED;
			}
		}
		roleStageExportService.updateShenqi(userRoleId, shenqiId);
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.SHENQI_REFRESH, shenqiId);
		return AppErrorCode.OK;
	}

	/**
	 * 获得单个神器
	 * @param userRoleId
	 * @param shenqiId
	 * @return
	 */
	public ShenQiInfo getShenQiInfo(Long userRoleId, final Integer shenqiId) {
		List<ShenQiInfo> list = shenqiInfoDao.cacheLoadAll(userRoleId, new IQueryFilter<ShenQiInfo>() {
			private boolean stop = false;

			@Override
			public boolean check(ShenQiInfo shenQiInfo) {
				if (shenQiInfo.getShenQiId().equals(shenqiId)) {
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

	public void onlineHandle(Long userRoleId) {
		Integer wearingShenqi = roleStageExportService.getRoleWearingShenqi(userRoleId);
		if (wearingShenqi == null) {
			wearingShenqi = 0;
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.REFRESH_SHENQI, new Object[] { userRoleId, wearingShenqi });
	}

	public int getActivatedShenqiNum(Long userRoleId) {
		List<ShenQiInfo> activatedShenqi = shenqiInfoDao.cacheLoadAll(userRoleId);
		if (activatedShenqi == null) {
			return 0;
		} else {
			return activatedShenqi.size();
		}
	}

	public Map<String, Long> getActivatedShenqiAttr(Long userRoleId) {
		Map<String, Long> ret = new HashMap<String, Long>();
		List<ShenQiInfo> activatedShenqi = shenqiInfoDao.cacheLoadAll(userRoleId);
		if (activatedShenqi != null && activatedShenqi.size() > 0) {
			for (ShenQiInfo e : activatedShenqi) {
				int shenqiId = e.getShenQiId();
				ShenQiShuXingConfig config = shenQiShuXingConfigExportService.loadById(shenqiId);
				ObjectUtil.longMapAdd(ret, config.getAttrs());
			}
			return ret;
		} else {
			return null;
		}
	}
	
	public Map<String, Long> getActivatedShenqiZhufuAttr(Long userRoleId, Map<Integer,Integer> typeLevels){// Integer zuoqiLevel, Integer chibangLevel, Integer xianjianLevel, Integer zhanjiaLevel) {
		Map<String, Long> shenqiZhufuMap = new HashMap<String, Long>();
		
		if(typeLevels == null){
			return shenqiZhufuMap;
		}
		
		for (Entry<Integer, Integer> entry : typeLevels.entrySet()) {
			int key = entry.getKey();
			Integer level = entry.getValue();
			if(level  == null){
				continue;
			}
			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndId(key, level);
			if (config != null) {
				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
			}
		}
		
		
//		if (zuoqiLevel != null) {
//			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndLevel(ShenQiConstants.ZHUFU_TYPE_WUQI, zuoqiLevel);
//			if (config != null) {
//				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
//			}
//		}
//		if (chibangLevel != null) {
//			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndLevel(ShenQiConstants.ZHUFU_TYPE_CHIBANG, chibangLevel);
//			if (config != null) {
//				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
//			}
//		}
//		if (xianjianLevel != null) {
//			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndLevel(ShenQiConstants.ZHUFU_TYPE_XIANJIAN, xianjianLevel);
//			if (config != null) {
//				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
//			}
//		}
//		if (zhanjiaLevel != null) {
//			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndLevel(ShenQiConstants.ZHUFU_TYPE_ZHANJIA, zhanjiaLevel);
//			if (config != null) {
//				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
//			}
//		}
//		
//		if (zuoqiLevel != null) {
//			ShuXingZhuFuConfig config = shuXingZhuFuConfigExportService.getByTypeAndLevel(ShenQiConstants.ZHUFU_TYPE_WUQI, zuoqiLevel);
//			if (config != null) {
//				ObjectUtil.longMapAdd(shenqiZhufuMap, config.getAttrs());
//			}
//		}
		
		
		int activatedNum = getActivatedShenqiNum(userRoleId);
		ObjectUtil.longMapTimes(shenqiZhufuMap, activatedNum);
		return shenqiZhufuMap;
	} 

	/**
	 * 开服买神器
	 * 
	 * @param userRoleId
	 * @return
	 */
	public Object[] kfBuyShenQi(Long userRoleId) {
		ShenQiPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_SHENQI);
		ShenQiInfo shenqiInfo = getShenQiInfo(userRoleId, config.getSqId());
		if (shenqiInfo != null) {
			return AppErrorCode.SHENQI_IS_ACTIVATED;// 此神器已经激活
		}
		int kaifuDay = ServerInfoServiceManager.getInstance().getKaifuDays();// 服务器开服天数
		int openDay = config.getDelayDay() + 1;// 活动延迟开启的天数(开服天数)
		int chixuDay = config.getDay();// 活动持续的天数
		if(kaifuDay < openDay || kaifuDay > openDay + chixuDay){
		    return AppErrorCode.KAIFU_TIME_ERROR;// 不在活动时间内
		}
		Object[] reslut = accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, config.getGold(), userRoleId, LogPrintHandle.CONSUME_BUY_SHENQI, true,
				LogPrintHandle.CBZ_BUY_SHENQI);
		if (reslut != null) {
			return reslut;
		} else {
			if (PlatformConstants.isQQ()) {
				BusMsgSender.send2BusInner(userRoleId, InnerCmdType.TENCENT_LUOPAN_OSS_XIAOFEI, new Object[] { QqConstants.ZHIFU_YB, config.getGold(),
						LogPrintHandle.CONSUME_BUY_SHENQI, QQXiaoFeiType.CONSUME_BUY_SHENQI, 1 });
			}
		}

		activateShenqiWithoutCheck(userRoleId, config.getSqId(), config.getConsume());
		return AppErrorCode.OK;
	}

	/**
	 * 是否已激活
	 * @param userRoleId
	 * @return
	 */
	public boolean isAcitveBuyShenQi(Long userRoleId) {
		ShenQiPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_SHENQI);
		ShenQiInfo shenqiInfo = getShenQiInfo(userRoleId, config.getSqId());
		return shenqiInfo != null;
	}

	/**
	 * 洗练
	 * @param userRoleId
	 * @param consumeType=1普通洗练，2元宝洗练，
	 * @param auto=0：材料不足不自动购买，1：材料不足自动购买
	 * @param lockAttr
	 * @return
	 */
	public Object[] xiLian(Long userRoleId, int id, int consumeType, int auto, Object[] lockAttr) {
		ShenQiXiLianPublicConfig config = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_SHENQI_XILIAN);
		if(config==null){
			return  AppErrorCode.CONFIG_ERROR;
		}
		ShenQiInfo shenqiInfo = getShenQiInfo(userRoleId,id);//洗练对象
		if(shenqiInfo==null){
			return  AppErrorCode.SHENQI_IS_NOT_ACTIVATED;
		}
		int needGold  =  config.getNeedgold(); //元宝洗练
		 RoleAccountWrapper roleAccountWrapper =accountExportService.getAccountWrapper(userRoleId);
		if(consumeType==2){
		    if(roleAccountWrapper.getYb()<needGold){
		    	return AppErrorCode.GOLD_NOT_ENOUGH;
		    }
		}
		String goodId  =  (String)config.getNeedshi()[0]; //魔血 大禹石
		int needGoodNum = Integer.parseInt((String)config.getNeedshi()[1]);//魔血数量
		int autoNeedGold = 0;
		int needMoney  = config.getNeedmoney(); //洗炼消耗银两
		int daYuShiNum =  roleBagExportService.getBagItemCountByGoodsId(goodId, userRoleId);
		if(consumeType!=2){
			
		 Object[] result = accountExportService.isEnought(GoodsCategory.MONEY, needMoney, userRoleId);
			if(result != null){
				return result;
			}
		if(auto==0){
			  if(daYuShiNum<needGoodNum){
				  return AppErrorCode.ITEM_COUNT_ENOUGH;  
			  }
			 
			}else{
				//道具不足自动购买
				if(daYuShiNum<needGoodNum){
					autoNeedGold =config.getJiage(); //自动购买，购买材料的消耗元宝
					autoNeedGold = (needGoodNum-daYuShiNum)*autoNeedGold;//减掉已有的 
					Object[] goldRresult = accountExportService.isEnought(GoodsCategory.GOLD, autoNeedGold, userRoleId);
					if(goldRresult != null){
						return goldRresult;
					}
				}
			}
		}
		String lockGoodId  = (String)config.getNeedfu()[0]; //锁定符
		Integer lockGoodNeedNum = Integer.parseInt((String)config.getNeedfu()[1]);
		//有锁定的属性
		if(lockAttr!=null){
			 int lockItemsNum=roleBagExportService.getBagItemCountByGoodsId(lockGoodId, userRoleId);
			 if(lockItemsNum<lockGoodNeedNum*lockAttr.length){
				 return AppErrorCode.ITEM_COUNT_ENOUGH;  //锁定符数量不足,锁定符不能购买
			 }
			RoleVipWrapper roleVipWrapper = roleVipInfoExportService.getRoleVipInfo(userRoleId);
			int lockNum = config.getLockNum(roleVipWrapper.getVipLevel()); //玩家最大锁定条数
			if(lockNum==0){
				return AppErrorCode.SHENQI_XILIAN_LOCK_ERROR_VIP_1;//不能进行此次的锁定洗练，vip等级不足
			}
			if(lockAttr.length>lockNum){
				return AppErrorCode.SHENQI_XILIAN_LOCK_ERROR_VIP_2; //锁定的属性大于当前vip玩家实际拥有的锁定条数
			}
		}
		//开始洗练
		Map<String, Long> maxConfigMap = shenQiXiLianMaxConfigExportService.loadById(id);//各个属性的上限值
		//取基础值
		ShenQiShuXingConfig shenQiShuXingConfig =shenQiShuXingConfigExportService.loadById(id);
		Map<String, Long> countAttrMap = new HashMap<>();//基础+洗练
		if(shenqiInfo.getCurrentInfoMap()!=null){
			//神器当前已洗出的属性
			ObjectUtil.longMapAdd(countAttrMap,shenqiInfo.getCurrentInfoMap());
		}
		//神器基础属性
		ObjectUtil.longMapAdd(countAttrMap, shenQiShuXingConfig.getAttrs());
		ShenQiXiLianConfig newXiLianConfig = new ShenQiXiLianConfig(); //组装新的洗练数据
		newXiLianConfig.setXiLianMap(new HashMap<String,JSONArray>());
		
		for (Entry<String, Long> entry : countAttrMap.entrySet()) {
			String attrName = entry.getKey();
			long value = entry.getValue();
			if(!attrName.contains("x")){
				continue;
			}
			double _value = (double)value/maxConfigMap.get(attrName);
			int rank =(int)(_value*100);//每个属性比例所在的段位
			List<ShenQiXiLianConfig> list =shenQiXiLianConfigExportService.getAll();
			for (ShenQiXiLianConfig target : list) {
				if(rank<=target.getRank()){
					//匹配到
					newXiLianConfig.getXiLianMap().put(attrName,target.getXiLianMap().get(attrName));
					break;
				}
			} 
		}
		//从洗练数据里面随机洗练值
		Map<String, Long> addWashMap = new HashMap<>();//新的洗练属性增量值
		Map<String, JSONArray> jsonMap = newXiLianConfig.getXiLianMap();
		for (Entry<String, JSONArray> entry : jsonMap.entrySet()) {
			String attrName = entry.getKey();
			JSONArray  json = entry.getValue();
			//第二位是元宝洗练值
			JSONArray  data = consumeType==1?(JSONArray)json.get(0):(JSONArray)json.get(1);
			long addValue = RandomUtil.getRondom((Integer)data.get(1), (Integer)data.get(0));
			if(addValue+countAttrMap.get(attrName)>=maxConfigMap.get(attrName)){
				//新洗练出来的值+旧的值总和超过上限
				addValue  = maxConfigMap.get(attrName) - countAttrMap.get(attrName)<0?0:maxConfigMap.get(attrName) - countAttrMap.get(attrName);
			}
			addWashMap.put(attrName, addValue); //洗练完成 OK
		}
		//检查是否有锁定属性
		if(lockAttr!=null && lockAttr.length>0){
			for (int i = 0; i < lockAttr.length; i++) { 
				String lockName  = (String)lockAttr[i];
				if(!"x2,x5,x8,x11,x14,x16,x18".contains(lockName)){
					ChuanQiLog.error("神器洗练：锁定的属性不在传值有问题");
					return AppErrorCode.PARAMETER_ERROR;
				}
				addWashMap.put(lockName, 0L);
			}
		}
		//第一次洗练不能有负数
		if(shenqiInfo.getCurrentInfoMap()==null){
			for (Entry<String, Long> map : addWashMap.entrySet()) {
				if(map.getValue()<0){
					addWashMap.put(map.getKey(), 0L);//第一次洗练负数的值归0
				}
			}
		}
		shenqiInfo.setFreeMap(addWashMap);//洗出来的增量属性寄存
		Map<String, Integer> consumeGoods = new HashMap<String, Integer>(); //日志用
		//统计洗练银子，道具的消耗
		if(lockAttr!=null && lockAttr.length>0){
			int lockNum  = lockAttr.length*lockGoodNeedNum;
			// 背包减掉道具
			BagSlots bagSlots = roleBagExportService.removeBagItemByGoodsId(lockGoodId,lockNum, userRoleId, GoodsSource.GOODS_XILIAN_LOCK_ITEM, true, true);
			if (!bagSlots.isSuccee()) {
				return bagSlots.getErrorCode();
			}
			consumeGoods.put(lockGoodId, lockNum);
		}
		if(auto==0){
			if(consumeType==2){
				accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, needGold, userRoleId, LogPrintHandle.CONSUME_XILIAN, true, LogPrintHandle.CBZ_CONSUME_XILIAN);
			}else{
				accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, needMoney, userRoleId, LogPrintHandle.CONSUME_XILIAN, true, LogPrintHandle.CBZ_CONSUME_XILIAN);
				//消耗魔血
				BagSlots bagSlots = roleBagExportService.removeBagItemByGoodsId(goodId, needGoodNum, userRoleId, GoodsSource.GOODS_XILIAN_MOXUE_ITEM, true, true);
				if (!bagSlots.isSuccee()) {
					return bagSlots.getErrorCode();
				}
			}
			consumeGoods.put(goodId, needGoodNum);
			//日志
			JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(consumeGoods, null);
			GamePublishEvent.publishEvent(new ShenqiXilianLogEvent(userRoleId,id,auto,consumeType,needMoney,needGold,jsonArray));
			
		}else{
			if(consumeType==2){
				return AppErrorCode.PARAMETER_ERROR;
			}else{
				int numMoXue =daYuShiNum;
				if(autoNeedGold>0){
					//材料不够消耗元宝买
					accountExportService.decrCurrencyWithNotify(GoodsCategory.GOLD, autoNeedGold, userRoleId, LogPrintHandle.CONSUME_XILIAN, true, LogPrintHandle.CBZ_CONSUME_XILIAN);
				}else{
					numMoXue = needGoodNum;
				}
				accountExportService.decrCurrencyWithNotify(GoodsCategory.MONEY, needMoney, userRoleId, LogPrintHandle.CONSUME_XILIAN, true, LogPrintHandle.CBZ_CONSUME_XILIAN);
				//消耗魔血
				if(numMoXue>0){
					BagSlots bagSlots = roleBagExportService.removeBagItemByGoodsId(goodId, numMoXue, userRoleId, GoodsSource.GOODS_XILIAN_MOXUE_ITEM, true, true);
					if (!bagSlots.isSuccee()) {
						return bagSlots.getErrorCode();
					}
					consumeGoods.put(goodId, numMoXue);
				}
			}
			//日志
			JSONArray jsonArray = LogPrintHandle.getLogGoodsParam(consumeGoods, null);
			GamePublishEvent.publishEvent(new ShenqiXilianLogEvent(userRoleId,id,auto,consumeType,needMoney,autoNeedGold,jsonArray));
		}
		return new Object[]{AppErrorCode.SUCCESS,convertArrayToClient(addWashMap)};
	}
	/**
	 * type=1保存，0放弃
	 * 保存或者放弃洗练出的属性
	 */
	public Object[] xiLianSaveOrGiveUp(Long userRoleId, int id,int type) {
		ShenQiInfo shenqiInfo = getShenQiInfo(userRoleId,id);//洗练对象
		if(shenqiInfo==null){
			return null;
		}
		if(type==1){
			if(shenqiInfo.getCurrentInfoMap()==null){
				shenqiInfo.setAttrMap(JSON.toJSONString(shenqiInfo.getFreeMap()));//更新属性
			}else{
				ObjectUtil.longMapAdd(shenqiInfo.getCurrentInfoMap(), shenqiInfo.getFreeMap()); //属性相加
				shenqiInfo.setAttrMap(JSON.toJSONString(shenqiInfo.getCurrentInfoMap()));//更新属性
			}
			shenqiInfoDao.cacheUpdate(shenqiInfo, userRoleId);
			 
			//清理
			if(shenqiInfo.getFreeMap() != null){
				shenqiInfo.getFreeMap().clear();
				shenqiInfo.setFreeMap(null);
			}
			if(shenqiInfo.getCurrentInfoMap() != null){
				shenqiInfo.getCurrentInfoMap().clear();
				shenqiInfo.setCurrentInfoMap(null);
			}
			
			//属性变更
			Map<String, Long>  attrMap = countAllAttr(userRoleId);
			BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_SHENQI_WASH_ATTR_CHANGE,attrMap);
		}else{
			if(shenqiInfo.getFreeMap()!=null){ //容错
				shenqiInfo.getFreeMap().clear();
				shenqiInfo.setFreeMap(null);
			}
			
		}
		
		return AppErrorCode.OK;
	}
	//计算战力
	private void calZplus(Map<String,Long> att){
		Float zplus = 0f;
		for (Entry<String, Long> entry : att.entrySet()) {
			Float xs = zhanLiXiShuConfigExportService.getZLXS(entry.getKey());
			if(xs != null){
				zplus += xs * entry.getValue();
			}
		}
		att.put(EffectType.zplus.name(), zplus.longValue());
	}
	/**
	 * 玩家登陆统计洗练这个模块的所有属性跟战力包括基础的属性
	 * @param Max
	 * @param Min
	 * @return
	 */
	public Map<String, Long> countAllAttr(Long userRoleId){
		List<ShenQiInfo> data = shenqiInfoDao.cacheLoadAll(userRoleId);
		Map<String, Long> washAttrMap=null; //洗练出来的属性
		Map<String, Long> attrMap=null;
		if(data!=null && data.size()>0){
			washAttrMap = new HashMap<>();
			attrMap = new HashMap<>();
			for (ShenQiInfo shenQiInfo : data) {
				//洗练出的属性
				if(shenQiInfo.getCurrentInfoMap()!=null){
					ObjectUtil.longMapAdd(washAttrMap, shenQiInfo.getCurrentInfoMap());
				}
				//基础属性
				ShenQiShuXingConfig shenQiShuXingConfig =shenQiShuXingConfigExportService.loadById(shenQiInfo.getShenQiId());
				ObjectUtil.longMapAdd(attrMap, shenQiShuXingConfig.getAttrs());
			}
			//单独计算洗练出来的属性战力
			if(washAttrMap!=null && washAttrMap.size()>0){
				calZplus(washAttrMap);	
			}
			//把两部分的属性包括战力相加
			ObjectUtil.longMapAdd(attrMap, washAttrMap);
		}
		return attrMap;
	}
	
	//把一个属性的map转成客户端要的数组
	private Object[] convertArrayToClient(Map<String, Long> map ){
		Object[] data  = new Object[map.size()];
		data[0] = map.get("x2");
		data[1] = map.get("x5");
		data[2] = map.get("x8");
		data[3] = map.get("x16");
		data[4] = map.get("x11");
		data[5] = map.get("x14");
		data[6] = map.get("x18");
		return data;
	}
}
