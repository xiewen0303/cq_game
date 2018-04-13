package com.junyou.bus.chenghao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.junyou.bus.jingji.service.IFightVal;
import com.junyou.gameconfig.constants.EffectType;
import com.junyou.utils.common.CovertObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.chenghao.configure.export.ChengHaoPeiZhiConfig;
import com.junyou.bus.chenghao.configure.export.ChengHaoPeiZhiConfigExportService;
import com.junyou.bus.chenghao.constants.ChenghaoConstants;
import com.junyou.bus.chenghao.dao.RoleChenghaoDao;
import com.junyou.bus.chenghao.entity.DingZhiChenghao;
import com.junyou.bus.chenghao.entity.RoleChenghao;
import com.junyou.bus.role.entity.UserRole;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.err.AppErrorCode;
import com.junyou.event.ActivateChenghaoLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.ChenghaoPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.junyou.stage.model.core.stage.ElementType;
import com.junyou.stage.model.core.stage.IStage;
import com.junyou.stage.model.element.role.IRole;
import com.junyou.stage.model.stage.StageManager;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.common.ObjectUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.http.HttpClientMocker;
import com.junyou.utils.md5.Md5Utils;
import com.kernel.data.accessor.AccessType;
import com.kernel.data.dao.IQueryFilter;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class ChenghaoService implements IFightVal {

	@Override
	public long getZplus(long userRoleId, int fightPowerType) {
		Map<String, Long> data = getChenghaoAttribute(userRoleId);
		return CovertObjectUtil.getZplus(data);
	}

	@Autowired
	private RoleChenghaoDao roleChenghaoDao;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private ChengHaoPeiZhiConfigExportService chengHaoPeiZhiConfigExportService;
	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;
	@Autowired
	private RoleBagExportService roleBagExportService;
    @Autowired
    private BusScheduleExportService busScheduleExportService;
	
	private ReentrantLock lock = new ReentrantLock();
	private static int TRY_LOCK_TIME = 1;
	private static String lastMD5=null;
	private List<DingZhiChenghao> dingzhichenghaoList = new ArrayList<DingZhiChenghao>();
	
	/**
	 * 称号配置文件名  TODO wind
	 */
	private static String TITLE_FILE = "title_hqg.jat"; 
	

	public ChenghaoPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_CHENGHAO);
	}

	public List<RoleChenghao> initRoleChenghao(Long userRoleId) {
		return roleChenghaoDao.initRoleChenghao(userRoleId);
	}

	public void filterExpired(Long userRoleId) {
		List<RoleChenghao> list = roleChenghaoDao.cacheLoadAll(userRoleId);
		if (list != null) {
		    long nowTime = GameSystemTime.getSystemMillTime();
			for (RoleChenghao e : list) {
			    Long expireTime = e.getExpireTime();
				if (null != expireTime && expireTime.longValue() > 0) {
				    long delay = expireTime.longValue() - nowTime;
				    if(delay > 0){
				        BusTokenRunable runable = new BusTokenRunable(userRoleId, InnerCmdType.INNER_CHENGHAO_EXPIRE, e.getChenghaoId());
				        busScheduleExportService.schedule(userRoleId.toString(), GameConstants.COMPONENT_CHENGHAO_EXPIRE + e.getChenghaoId(), runable, (int)delay, TimeUnit.MILLISECONDS);
				    } else {
				        roleChenghaoDao.cacheDelete(e.getId(), userRoleId);
				    }
				}
			}
		}
	}

	public Map<Integer, String> getWearingChenghao(Long userRoleId) {
		List<RoleChenghao> list = roleChenghaoDao.cacheLoadAll(userRoleId,
				new IQueryFilter<RoleChenghao>() {

					@Override
					public boolean check(RoleChenghao roleChenghao) {
						if (roleChenghao.getExpireTime() == 0L
								|| roleChenghao.getExpireTime() > GameSystemTime
										.getSystemMillTime()) {
							return roleChenghao.getWearStatus().intValue() == ChenghaoConstants.CHENGHAP_WEAR_STATUS_1;
						}
						return false;
					}

					@Override
					public boolean stopped() {
						return false;
					}
				});
		if (list != null && list.size() > 0) {
			Map<Integer, String> ret = new HashMap<Integer, String>();
			for (int i = 0; i < list.size(); i++) {
				Integer chenghaoId = list.get(i).getChenghaoId();
				String chenghaoRes = list.get(i).getRes();
				ret.put(chenghaoId, chenghaoRes);
			}
			return ret;
		}
		return null;
	}

	private void startChanghaoExpire(RoleChenghao chenghao) {
		Long userRoleId = chenghao.getUserRoleId();
		String stageId = publicRoleStateExportService
				.getRolePublicStageId(userRoleId);
		if (stageId == null) {
			return;
		}
		IStage stage = StageManager.getStage(stageId);
		if (stage == null) {
			return;
		}
		IRole role = stage.getElement(userRoleId, ElementType.ROLE);
		if (role == null) {
			return;
		}
		Long delay = chenghao.getExpireTime()
				- GameSystemTime.getSystemMillTime();
		if (delay > 0) {
			role.startChenghaoExpireSchedule(chenghao.getChenghaoId(), delay);
		}
	}

    private Object[] createChenghaoRefreshData(Long userRoleId) {
        Object[] data = null;
        Map<Integer, String> wearingChenghaoMap = getWearingChenghao(userRoleId);
        if (!ObjectUtil.isEmpty(wearingChenghaoMap)) {
            int index = 0;
            data = new Object[wearingChenghaoMap.size()];
            for (Entry<Integer, String> entry : wearingChenghaoMap.entrySet()) {
                data[index++] = new Object[] { entry.getKey(), entry.getValue() };
            }
        }
        return data == null ? new Object[] { userRoleId } : new Object[] { userRoleId, data };
    }
	
	public void chenghaoExpire(Long userRoleId, Integer chenghaoId) {
		RoleChenghao chenghao = getRoleChenghao(userRoleId, chenghaoId);
		if (chenghao == null) {
			return;
		}
        if (chenghao.getExpireTime() != 0 && chenghao.getExpireTime() <= GameSystemTime.getSystemMillTime()) {
            roleChenghaoDao.cacheDelete(chenghao.getId(), userRoleId);
            BusMsgSender.send2One(userRoleId, ClientCmdType.REFRESH_CHENGHAO_DELETE, new Object[] { chenghaoId });
            BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHENGHAO_REMOVE, chenghaoId);
        }
	}

    public void noticeClient(Long userRoleId) {
        BusMsgSender.send2One(userRoleId, ClientCmdType.REFRESH_CHENGHAO, createChenghaoRefreshData(userRoleId));
    }

	public void createRoleChenghao(Long userRoleId, Integer chenghaoId,
			String name, String res, Long expireTime,boolean toDb) {
		RoleChenghao chenghao = new RoleChenghao();
		chenghao.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		chenghao.setChenghaoId(chenghaoId);
		chenghao.setName(name);
		chenghao.setRes(res);
		chenghao.setUserRoleId(userRoleId);
		chenghao.setExpireTime(expireTime);
		chenghao.setUpdateTime(GameSystemTime.getSystemMillTime());
		chenghao.setWearStatus(ChenghaoConstants.CHENGHAP_WEAR_STATUS_0);
		if (!toDb && expireTime.longValue() != 0L) {
			startChanghaoExpire(chenghao);
		}
		if(toDb){
			roleChenghaoDao.insert(chenghao, userRoleId, AccessType.getDirectDbType());
		}else{
			roleChenghaoDao.cacheInsert(chenghao, userRoleId);
		}
	}

	public RoleChenghao getRoleChenghao(Long userRoleId,
			final Integer chenghaoId) {
		List<RoleChenghao> list = roleChenghaoDao.cacheLoadAll(userRoleId,
				new IQueryFilter<RoleChenghao>() {
					private boolean stop = false;

					@Override
					public boolean check(RoleChenghao roleChenghao) {
						if (roleChenghao.getChenghaoId().equals(chenghaoId)) {
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

	public Object[] getChenghaoInfo(Long userRoleId) {
		List<RoleChenghao> chenghaoList = roleChenghaoDao
				.cacheLoadAll(userRoleId);
		if (chenghaoList != null) {
			int size = 0;
			for (int i = 0; i < chenghaoList.size(); i++) {
				RoleChenghao e = chenghaoList.get(i);
				if (e.getExpireTime() == 0L
						|| e.getExpireTime() > GameSystemTime
								.getSystemMillTime()) {
					size++;
				}
			}
			Object[] ret = new Object[size];
			for (int i = 0; i < chenghaoList.size(); i++) {
				RoleChenghao e = chenghaoList.get(i);
				if (e.getExpireTime() == 0L
						|| e.getExpireTime() > GameSystemTime
								.getSystemMillTime()) {
					Integer chenghaoId = e.getChenghaoId();
					String chenghaoName = e.getName();
					String res = e.getRes();
					Long expireTime = e.getExpireTime();
					Integer jihuoStatus = 1;
					Integer peidaiStatus = 0;
					if (e.getWearStatus() == ChenghaoConstants.CHENGHAP_WEAR_STATUS_1) {
						peidaiStatus = 1;
					}
					ret[i] = new Object[] { chenghaoId, jihuoStatus,
							peidaiStatus, chenghaoName, res, expireTime };
				}
			}
			return ret;
		}
		return null;
	}

	public Object[] activeChenghaoByDingZhi(Long userRoleId,
			Integer chenghaoId, String name, String res) {
		// 检测是否已激活
		RoleChenghao roleChenghao = getRoleChenghao(userRoleId, chenghaoId);
		if (roleChenghao != null) {
			return AppErrorCode.CHENHGAO_IS_ACTIVATED;
		}
		// 检测需求等级
		ChengHaoPeiZhiConfig config = chengHaoPeiZhiConfigExportService
				.loadById(chenghaoId);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		UserRole userRole = roleExportService.getUserRole(userRoleId);
		if (userRole == null) {
			return AppErrorCode.ERR;
		}
		if (userRole.getLevel() < config.getNeedlevel()) {
			return AppErrorCode.CHENGHAO_ROLE_LEVEL_ERROR;
		}
		long expireTime = 0L;
		if (config.getTime() != 0) {
		    expireTime = GameSystemTime.getSystemMillTime() + config.getTime() * 60 * 1000L;
		}
		chenghaoActivate(userRoleId, chenghaoId, name, res, expireTime);
		return null;
	}

	public Object[] activateChenghaoByItem(Long userRoleId, Integer chenghaoId) {
		// 检测是否已激活
		RoleChenghao roleChenghao = getRoleChenghao(userRoleId, chenghaoId);
		if (roleChenghao != null) {
			return AppErrorCode.CHENHGAO_IS_ACTIVATED;
		}
		// 检测需求等级
		ChengHaoPeiZhiConfig config = chengHaoPeiZhiConfigExportService
				.loadById(chenghaoId);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		if(config.getType1() == GameConstants.CHENGHAO_TYPE_CHENGJIU){
			return AppErrorCode.CHENGJIU_CHENGHAO_CANNOT_ACTIVE;
		}
		UserRole userRole = roleExportService.getUserRole(userRoleId);
		if (userRole == null) {
			return AppErrorCode.ERR;
		}
		if (userRole.getLevel() < config.getNeedlevel()) {
			return AppErrorCode.CHENGHAO_ROLE_LEVEL_ERROR;
		}
		long expireTime = 0L;
		if (config.getTime() != 0) {
		    expireTime = GameSystemTime.getSystemMillTime() + config.getTime() * 60 * 1000L;
		}
		List<String> goodsIds = goodsConfigExportService.loadIdsById1(config
				.getNeeditem());
		String goodsId = null;
		Object[] checkResult = AppErrorCode.CONFIG_ERROR;
		for (String e : goodsIds) {
			checkResult = roleBagExportService.checkRemoveBagItemByGoodsId(e,
					1, userRoleId);
			if (checkResult == null) {
				goodsId = e;
				break;
			}
		}
		if (goodsId == null) {
			return AppErrorCode.CHENHGAO_ITEM_LIMIT;
		} else {
			roleBagExportService.removeBagItemByGoodsId(goodsId, 1, userRoleId,
					GoodsSource.ACTIVATE_CHENGHAO, true, true);
		}
		chenghaoActivate(userRoleId, chenghaoId, null, null, expireTime);
		return null;
	}

	public Object[] activateChenghao(Long userRoleId, Integer chenghaoId) {
		// 检测是否已激活
		RoleChenghao roleChenghao = getRoleChenghao(userRoleId, chenghaoId);
		if (roleChenghao != null) {
			return AppErrorCode.CHENHGAO_IS_ACTIVATED;
		}
		// 检测需求等级
		ChengHaoPeiZhiConfig config = chengHaoPeiZhiConfigExportService
				.loadById(chenghaoId);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		UserRole userRole = roleExportService.getUserRole(userRoleId);
		if (userRole == null) {
			return AppErrorCode.ERR;
		}
		if (userRole.getLevel() < config.getNeedlevel()) {
			return AppErrorCode.CHENGHAO_ROLE_LEVEL_ERROR;
		}
		long expireTime = 0L;
		if (config.getTime() != 0) {
            expireTime = GameSystemTime.getSystemMillTime() + config.getTime() * 60 * 1000L;
		}
		chenghaoActivate(userRoleId, chenghaoId, null, null, expireTime);
		return null;
	}

	private void chenghaoActivate(Long userRoleId, Integer chenghaoId,
			String name, String res, Long expireTime) {
        createRoleChenghao(userRoleId, chenghaoId, name, res, expireTime, false);
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHENGHAO_ACTIVATE, chenghaoId);
        BusMsgSender.send2One(userRoleId, ClientCmdType.REFRESH_CHENGHAO_ACTIVATE, new Object[] { chenghaoId, name, res, expireTime });
        GamePublishEvent.publishEvent(new ActivateChenghaoLogEvent(userRoleId, chenghaoId));
    }

	public Object[] wearChenghao(Long userRoleId, Integer chenghaoId) {
		RoleChenghao roleChenghao = getRoleChenghao(userRoleId, chenghaoId);
		if (roleChenghao == null) {
			return AppErrorCode.CHENHGAO_NOT_EXIST;
		}
		if (roleChenghao.getExpireTime() != 0
				&& roleChenghao.getExpireTime() <= GameSystemTime
						.getSystemMillTime()) {
			return AppErrorCode.CHENHGAO_IS_EXPIRE;
		}
		if (roleChenghao.getWearStatus() == ChenghaoConstants.CHENGHAP_WEAR_STATUS_1) {
			return AppErrorCode.CHENHGAO_IS_WEARING;
		}
		ChenghaoPublicConfig publicConfig = getPublicConfig();
		if (publicConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		Map<Integer, String> wearingChenghaoId = getWearingChenghao(userRoleId);
		Integer oldChenghaoId = null;
		if (publicConfig.getToudingnum() == 1) {
			if (wearingChenghaoId != null) {
				for (Integer e : wearingChenghaoId.keySet()) {
					RoleChenghao tmpChenghao = getRoleChenghao(userRoleId, e);
					tmpChenghao
							.setWearStatus(ChenghaoConstants.CHENGHAP_WEAR_STATUS_0);
					roleChenghaoDao.cacheUpdate(tmpChenghao, userRoleId);
					oldChenghaoId = e;
					break;
				}
			}
		} else {
			if (wearingChenghaoId != null
					&& wearingChenghaoId.size() >= publicConfig.getToudingnum()) {
				return AppErrorCode.CHENHGAO_WEARING_NUM_LIMIT;
			}
		}
		roleChenghao.setWearStatus(ChenghaoConstants.CHENGHAP_WEAR_STATUS_1);
		roleChenghaoDao.cacheUpdate(roleChenghao, userRoleId);
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHENGHAO_REFRESH, new Object[] { true, oldChenghaoId, chenghaoId, roleChenghao.getRes(), createChenghaoRefreshData(userRoleId) });
		return new Object[] { AppErrorCode.SUCCESS, chenghaoId };
	}

	public Object[] unWearChenghao(Long userRoleId, Integer chenghaoId) {
		RoleChenghao roleChenghao = getRoleChenghao(userRoleId, chenghaoId);
		if (roleChenghao == null) {
			return AppErrorCode.CHENHGAO_NOT_EXIST;
		}
		if (roleChenghao.getExpireTime() != 0
				&& roleChenghao.getExpireTime() <= GameSystemTime
						.getSystemMillTime()) {
			return AppErrorCode.CHENHGAO_IS_EXPIRE;
		}
		if (roleChenghao.getWearStatus() == ChenghaoConstants.CHENGHAP_WEAR_STATUS_0) {
			return AppErrorCode.CHENHGAO_IS_NOT_WEARING;
		}
		roleChenghao.setWearStatus(ChenghaoConstants.CHENGHAP_WEAR_STATUS_0);
		roleChenghaoDao.cacheUpdate(roleChenghao, userRoleId);
        BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_CHENGHAO_REFRESH, new Object[] { false, null, chenghaoId, roleChenghao.getRes(), createChenghaoRefreshData(userRoleId) });
		return new Object[] { AppErrorCode.SUCCESS, chenghaoId };
	}

	public Map<String, Long> getChenghaoAttribute(Long userRoleId) {
		List<RoleChenghao> list = roleChenghaoDao.cacheLoadAll(userRoleId);
		if (list == null) {
			return null;
		}
		Map<String, Long> ret = new HashMap<String, Long>();
		for (RoleChenghao e : list) {
			if (e.getExpireTime() != 0) {
				if (e.getExpireTime() <= GameSystemTime.getSystemMillTime()) {
					continue;
				}
			}
			Integer chenghaoId = e.getChenghaoId();
			ChengHaoPeiZhiConfig config = chengHaoPeiZhiConfigExportService
					.loadById(chenghaoId);
			if (config == null) {
				ChuanQiLog.error("称号配置异常，称号id:{}", chenghaoId);
				continue;
			}
			ObjectUtil.longMapAdd(ret, config.getAttrs());
			if (e.getWearStatus().intValue() == ChenghaoConstants.CHENGHAP_WEAR_STATUS_1) {
				if (config.getExtraAttrs() != null) {
					ObjectUtil.longMapAdd(ret, config.getExtraAttrs());
				}
			}
		}
		return ret;
	}
	/**
	 *1:成功 -1 :配置不存在 -2:玩家不存在 -3：玩家等级不足  -4:该id称号已激活
	 * @param serverId
	 * @param userId
	 * @param chenghaoId
	 * @param chenghaoName
	 * @param chenghaoRes
	 * @return
	 */
	public Integer dingshiChenghao(String serverId, String userId,
			Integer chenghaoId, String chenghaoName, String chenghaoRes) {
		UserRole userRole = roleExportService.getRoleFromDb(userId, serverId);
		if(userRole == null){
			return -2;
		}
		// 检测需求等级
		ChengHaoPeiZhiConfig config = chengHaoPeiZhiConfigExportService
				.loadById(chenghaoId);
		if (config == null) {
			return -1;
		}
		if (userRole.getLevel() < config.getNeedlevel()) {
			return -3;
		}
		boolean isOnline = publicRoleStateExportService.isPublicOnline(userRole.getId());
		if(isOnline){
			// 检测是否已激活
			RoleChenghao roleChenghao = getRoleChenghao(userRole.getId(), chenghaoId);
			if (roleChenghao != null) {
				return -4;
			}
			activeChenghaoByDingZhi(userRole.getId(), chenghaoId, chenghaoName, chenghaoRes);
		}else{
			List<RoleChenghao> chenghaoList = roleChenghaoDao.getChenghao(userRole.getId(), chenghaoId);
			if(chenghaoList!=null && chenghaoList.size()>0){
				return -4;
			}
            long expireTime = 0L;
            if (config.getTime() != 0) {
                expireTime = GameSystemTime.getSystemMillTime() + config.getTime() * 60 * 1000L;
            }
			createRoleChenghao(userRole.getId(), chenghaoId, chenghaoName, chenghaoRes, expireTime, true);
		}
		return 1;
	}

	/**
	 * 获取跨服下载配置目录 地址
	 * 
	 * @return
	 */
	public static String getDingZhiChenghaoFileRemoteUrl() {
		StringBuffer value = new StringBuffer();
		value.append(ChuanQiConfigUtil.getLoadDirectoryUrl()).append("/title/");
		value.append(ChuanQiConfigUtil.getPlatfromId()).append("/");
		return value.toString();
	}
	
	
	public void fetchDingZhiChenghao() {
		String url = getDingZhiChenghaoFileRemoteUrl();
		String resultStr = HttpClientMocker.requestRemoteFileData(url,TITLE_FILE, 2000);
		if(resultStr==null){
			return ;
		}
		String md5Value = Md5Utils.md5Bytes(resultStr.getBytes());
		if(lastMD5!=null && lastMD5.equals(md5Value)){
			return ;
		}
		
		List<DingZhiChenghao> list = new ArrayList<DingZhiChenghao>();
		JSONArray jsonArray = JSON.parseArray(resultStr);
		if(jsonArray ==null){
			return;
		}
		lastMD5 = md5Value;
		for (Object e : jsonArray) {
			JSONObject jsonObj = (JSONObject) e;
			int titleId = jsonObj.getIntValue("titleId");
			String titleName = jsonObj.getString("titleName");
			String titleRes = jsonObj.getString("titleRes");
			String userName = jsonObj.getString("userName");
			DingZhiChenghao dingzhiChenghao = new DingZhiChenghao();
			dingzhiChenghao.setTitleId(titleId);
			dingzhiChenghao.setTitleName(titleName);
			dingzhiChenghao.setTitleRes(titleRes);
			dingzhiChenghao.setUserName(userName);
			list.add(dingzhiChenghao);
		}
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			dingzhichenghaoList = list;
			ChuanQiLog.info("fetch dingzhi chenghao finish");
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}

	}

	public Object[] getAllDingzhiChenghao(Long userRoleId) {
		try {
			lock.tryLock(TRY_LOCK_TIME, TimeUnit.SECONDS);
			Object[] ret = new Object[dingzhichenghaoList.size()];
			for (int i = 0; i < dingzhichenghaoList.size(); i++) {
				DingZhiChenghao dingZhiChenghao = dingzhichenghaoList.get(i);
				ret[i] = new Object[] { dingZhiChenghao.getTitleId(),
						dingZhiChenghao.getTitleName(),
						dingZhiChenghao.getTitleRes(),
						dingZhiChenghao.getUserName() };
			}
			return ret;
		} catch (InterruptedException e) {
			ChuanQiLog.error("", e);
		} finally {
			if(lock.isHeldByCurrentThread()){
				lock.unlock();
			}
		}
		return null;
	}
}
