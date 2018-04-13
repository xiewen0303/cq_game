package com.junyou.bus.lianchong.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.lianchong.configure.export.LianChongConfig;
import com.junyou.bus.lianchong.configure.export.LianChongConfig.RewardVo;
import com.junyou.bus.lianchong.configure.export.LianChongConfigExportService;
import com.junyou.bus.lianchong.configure.export.LianChongConfigGroup;
import com.junyou.bus.lianchong.dao.RoleLianchongDao;
import com.junyou.bus.lianchong.entity.RoleLianchong;
import com.junyou.bus.lianchong.filter.LianChongFilter;
import com.junyou.bus.lianchong.util.LianchongConstants;
import com.junyou.bus.rfbactivity.configure.export.ActivityAnalysisManager;
import com.junyou.bus.rfbactivity.configure.export.ActivityConfigSon;
import com.junyou.bus.rfbactivity.entity.RoleYuanbaoRecord;
import com.junyou.bus.rfbactivity.export.RefabuActivityExportService;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.RfbActivityPartInLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.log.ChuanQiLog;
import com.junyou.log.LogPrintHandle;
import com.junyou.utils.datetime.DatetimeUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.junyou.utils.math.BitOperationUtil;
import com.kernel.gen.id.IdFactory;
import com.kernel.gen.id.ServerIdType;

@Service
public class LianChongService {

	@Autowired
	private RoleLianchongDao roleLianchongDao;
	@Autowired
	private RoleBagExportService roleBagExportService;

	@Autowired
	private RefabuActivityExportService refabuActivityExportService;

	/**
	 * 登陆后加载到缓存
	 * 
	 * @param userRoleId
	 * @return
	 */
	public List<RoleLianchong> initLeichong(Long userRoleId) {
		
		
		return roleLianchongDao.initRoleLianchong(userRoleId);
	}

	/**
	 * 玩家充值后调用 [ [int(今天是否完成这个档位的充值0|1), int(excel行id）,int(完成天数),[领取过的奖励 [ 3,5,7
	 * ]]
	 */
	public void rechargeYb(Long userRoleId, Long addYb) {
		if (addYb < 0) {
			return;
		}
		// 判断配置
		Map<Integer, LianChongConfigGroup> allActivity = LianChongConfigExportService.getInstance().getAllConfig();
		List<JSONArray> toClientData = new ArrayList<>();
		if (allActivity == null || allActivity.size() == 0) {
			return;
		}
		for (Entry<Integer, LianChongConfigGroup> entry : allActivity.entrySet()) { // 可能配了多个活动
			// 是否在有这个活动或者是否在时间内
			ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(entry.getKey());
			if (configSong == null || !configSong.isRunActivity()) {
				continue;
			}
			JSONArray clientArray = new JSONArray(); // 客户端要的连充数据
			JSONArray clientIds = new JSONArray();
			int subId = entry.getKey();
			LianChongConfigGroup group = entry.getValue();
			// 更新日充数据
			RoleLianchong roleLianchong = this.getLianChong(userRoleId, subId, LianchongConstants.flag_0);
			roleLianchong.setDayRecharge((int) (roleLianchong.getDayRecharge() + addYb));
			roleLianchong.setUpdateTime(GameSystemTime.getSystemMillTime());

			clientArray.add(subId);
			clientArray.add(roleLianchong.getDayRecharge());

			List<LianChongConfig> configs = group.getConfigs();

			// 处理连充的数据
			clientIds = updateLianchongStateData(configs, roleLianchong);

			clientArray.add(clientIds.size() > 0 ? clientIds : null);
			toClientData.add(clientArray);// 组成一个大的数组给客户端
			this.roleLianchongDao.cacheUpdate(roleLianchong, userRoleId);
		}
		BusMsgSender.send2One(userRoleId, ClientCmdType.LIANCHONG_RECHARGE, this.convert2arr2(toClientData));
	}

	/**
	 * 更新连充的记录数据
	 * 
	 * @param configs
	 * @param roleLianchong
	 * @return
	 */
	private JSONArray updateLianchongStateData(List<LianChongConfig> configs, RoleLianchong roleLianchong) {
		JSONArray clientIds = new JSONArray();
		List<JSONArray> oldRewardStates = roleLianchong.getGoldRewardInfoList();
		List<Integer> completeDangweiIds = new ArrayList<>(); // 今日总充值达到完成的档位
		for (LianChongConfig lianChongConfig : configs) {
			// 达到充值档位
			if (lianChongConfig.getType().intValue() == 2 && roleLianchong.getDayRecharge().intValue() >= lianChongConfig.getGold().intValue()) {
				completeDangweiIds.add(lianChongConfig.getId());
			}
		}

		List<Integer> needAddConfigIds = new ArrayList<>(); // 新完成档位数据
		if (completeDangweiIds.size() > 0) {
			if (oldRewardStates != null) {
				// 更新数据
				for (int configId : completeDangweiIds) {
					boolean isContaineId = false; // 老数据是否有这个档位
					for (JSONArray doneJson : oldRewardStates) {
						int doneConfigId = doneJson.getIntValue(1);
						if (doneConfigId == configId) {
							// 更新有存在的数据
							isContaineId = true;
							if (doneJson.getIntValue(0) == 0) {
								doneJson.set(0, 1);// 今天已完成这个档位的充值
								doneJson.set(2, doneJson.getIntValue(2) + 1);// 连充完成天数+1
								clientIds.add(doneJson.get(1)); // 今日已完成id
							}
							break;
						}

					}
					if (!isContaineId) {
						needAddConfigIds.add(configId);
					}
				}

			} else {
				needAddConfigIds.addAll(completeDangweiIds);
			}

			if (!needAddConfigIds.isEmpty()) {
				// 新增数据
				for (int configId : needAddConfigIds) {
					JSONArray addJson = new JSONArray();
					addJson.add(1);
					addJson.add(configId);
					addJson.add(1);
					clientIds.add(configId);// 今日已完成id
					roleLianchong.addGoldRewardInfoArray(addJson);
				}
			}

		}
		return clientIds;
	}

	/**
	 * 获取奖励
	 * 
	 * @param userRoleId
	 * @param subId
	 * @param version
	 * @param configId
	 *            奖励id
	 * @param dayType
	 *            领取几天的连充
	 * @return
	 */
	public Object[] getReward(Long userRoleId, int subId, int version, int configId, int dayType) {

		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadRunByZiId(subId);
		if (configSong == null) {
			return AppErrorCode.NO_SUB_ACTIVITY;
		}
		// 版本不一样
		if (configSong.getClientVersion() != version) {
			// 处理数据变化:
			Object newSubHandleData = getRefbInfo(userRoleId, subId);
			Object[] data = new Object[] { subId, configSong.getClientVersion(), newSubHandleData };
			BusMsgSender.send2One(userRoleId, ClientCmdType.GET_ZHIDINGZIACTIVITY, data);
			return null;
		}
		// 判断配置
		LianChongConfig config = LianChongConfigExportService.getInstance().loadByKeyId(subId, configId);
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		// 连充数据
		RoleLianchong roleLianchong = getLianChong(userRoleId, subId, LianchongConstants.flag_1);

		// 判断是否已完成天数|是否领取
		if (config.getType() == 2) {
			JSONArray json = roleLianchong.getRewardArrayById(configId);
			if (json != null) {
				if (json.getIntValue(1) == configId) {
					int completeDay = json.getIntValue(2);
					if (completeDay < dayType) {// 未完成的天数不能领取
						return AppErrorCode.PARAMETER_ERROR;
					}
					if (json.size() > 3) {// 有领取记录
						JSONArray arr = (JSONArray) json.get(3);
						if (arr.indexOf(dayType) != -1) {
							return AppErrorCode.GET_ALREADY_ERROR; // 已经领取过
						}
					}
				}
			} else {
				return AppErrorCode.PARAMETER_ERROR; // 未来达到充值条件
			}

		} else {
			if (roleLianchong.getDayRecharge() < config.getGold()) {
				return AppErrorCode.PARAMETER_ERROR; // 未来达到充值条件
			}
			if (!BitOperationUtil.calState(roleLianchong.getDayReward(), configId - 1)) {
				return AppErrorCode.GET_ALREADY_ERROR;// 已经领取
			}

		}
		// 奖励
		List<RewardVo> list = config.getRewardVos();
		Map<String, Integer> itemMap = null;
		for (RewardVo rewardVo : list) {
			if (rewardVo.getDayType() == dayType) {
				itemMap = rewardVo.getItemMap();
				break;
			}
		}
		Object[] code = roleBagExportService.checkPutGoodsAndNumberAttr(itemMap, userRoleId);
		// 背包空间不足
		if (code != null) {
			return code;
		}
		// 更新状态
		if (config.getType() == 2) {
			JSONArray jsonArray = roleLianchong.getRewardArrayById(configId);
			if (jsonArray != null && jsonArray.size() < 4) {
				// 表示之前没有领过
				jsonArray.add(new JSONArray());
			}
			JSONArray lingQuArray = (JSONArray) jsonArray.get(3);
			lingQuArray.add(dayType); // 加入一个领取类型
		} else {
			int newState = BitOperationUtil.chanageState(roleLianchong.getDayReward(), configId - 1);
			roleLianchong.setDayReward(newState);
		}
		this.roleLianchongDao.cacheUpdate(roleLianchong, userRoleId);
		roleBagExportService.putGoodsAndNumberAttr(itemMap, userRoleId, GoodsSource.GOODS_LIANCHONG_GIFT, LogPrintHandle.GET_RFB_LIANCHONG, LogPrintHandle.GBZ_RFB_LIANCHONG, true);
		
	 //打印活动参与日志
  GamePublishEvent.publishEvent(
          new RfbActivityPartInLogEvent(
                  LogPrintHandle.REFABU_LIANCHONG,
                  configSong.getActivityId(), 
                  configSong.getSubName(), 
                  configSong.getSubActivityType(), 
                  configSong.getStartTimeByMillSecond(), 
                  configSong.getEndTimeByMillSecond(), 
                  userRoleId
          )
   );
		return new Object[] { 1, subId, configId, dayType };

	}

	/**
	 * 获取某个子活动的热发布某个活动信息
	 * 
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	public Object[] getRefbInfo(Long userRoleId, Integer subId) {
		LianChongConfigGroup group = LianChongConfigExportService.getInstance().loadByMap(subId);
		if (group == null) {
			return null;
		}
		Object[] data = new Object[6];
		data[0] = group.getPic();
		data[1] = group.getDes();
		data[2] = group.configsToArray();
		Object[] doneData = getLianChongStates(userRoleId, subId);
		data[3] = doneData[1];
		data[4] = doneData[2];
		data[5] = doneData[3];
		return data;
	}

	/**
	 * 获取状态数据,处理数据
	 * 
	 * @param userRoleId
	 * @param subId
	 * @return
	 */

	public Object[] getLianChongStates(Long userRoleId, int subId) {
		Object[] data = new Object[4];
		// 处理数据
		RoleLianchong roleLianchong = getLianChong(userRoleId, subId, LianchongConstants.flag_1);
		data[0] = subId;
		data[1] = roleLianchong.getDayRecharge();
		data[2] = roleLianchong.getDayReward();
		data[3] = this.convert2arr1(roleLianchong.getGoldRewardInfoList());

		return data;
	}

	/**
	 * [ [int(今天是否完成0|1), int(excel行id）,int(完成天数),[领取过的奖励3,5,7 ] ],... ]
	 * 
	 * @param data
	 * @return
	 */
	private Object[] convert2arr1(List<JSONArray> data) {
		if (data != null) {
			Object[] objects = new Object[data.size()];
			for (int i = 0; i < data.size(); i++) {
				JSONArray json = data.get(i);
				Object[] obj2 = new Object[] { json.get(0), json.get(1), json.get(2), null };
				if (json.size() > 3) {// 领取过的奖励
					JSONArray json2 = (JSONArray) json.get(3);
					Object[] awardArr = new Object[json2.size()];
					for (int j = 0; j < json2.size(); j++) {
						awardArr[j] = json2.get(j);
					}
					obj2[3] = awardArr;
				}
				objects[i] = obj2;
			}
			return objects;
		}
		return null;
	}

	private Object[] convert2arr2(List<JSONArray> toClientData) {
		if (toClientData != null) {
			Object[] data = new Object[toClientData.size()];
			for (int j = 0; j < toClientData.size(); j++) {
				JSONArray jsonArray = toClientData.get(j);
				Object[] obj = new Object[] { jsonArray.get(0), jsonArray.get(1), null };
				if (jsonArray.get(2) != null) {
					JSONArray json = (JSONArray) jsonArray.get(2);
					Object[] ids = new Object[json.size()];
					for (int i = 0; i < json.size(); i++) {
						ids[i] = json.get(i);
					}
					obj[2] = ids;
				}
				data[j] = obj;
			}
			return data;
		}
		return null;
	}

	private RoleLianchong createRoleLianchong(Long userRoleId, int subId) {
		RoleLianchong lianchong = new RoleLianchong();
		lianchong.setId(IdFactory.getInstance().generateId(ServerIdType.COMMON));
		lianchong.setUserRoleId(userRoleId);
		lianchong.setDayRecharge(0);
		lianchong.setDayReward(0);
		lianchong.setUpdateTime(GameSystemTime.getSystemMillTime());
		lianchong.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
		lianchong.setSubId(subId);
		roleLianchongDao.cacheInsert(lianchong, userRoleId);

		return lianchong;
	}

	/**
	 * 获取实体对象 同时纠正跨天数据 type=0玩家充值调用，其他type=1
	 * 
	 * @param userRoleId
	 * @param subId
	 * @return
	 */
	private RoleLianchong getLianChong(Long userRoleId, int subId, int type) {
		RoleLianchong lianchong = null;
		ActivityConfigSon configSong = ActivityAnalysisManager.getInstance().loadByZiId(subId);
		if (configSong == null) {
			// 可能为空 容错而已
			lianchong = createRoleLianchong(userRoleId, subId);
			return lianchong;
		}

		List<RoleLianchong> list = roleLianchongDao.cacheLoadAll(userRoleId, new LianChongFilter(subId));
		if (list == null || list.size() <= 0) {
			lianchong = createRoleLianchong(userRoleId, subId);
		} else {
			lianchong = list.get(0);
		}

//		int hefuNum = ServerInfoServiceManager.getInstance().getServerHefuTimes();
//		long dayTime = configSong.getEndTimeByMillSecond() / GameConstants.DAY_TIME;// 天为单位的时间戳
		// 服务器合服次数>1次时，要重置基于合服类型的连充活动的数据
		// if (hefuNum > 1 && configSong.getTimeType().intValue() ==
		// ActivityTimeType.TIME_3_HE_FU && lianchong.getActiveEndTime() /
		// GameConstants.DAY_TIME != dayTime) {
		
		long startTime = configSong.getStartTimeByMillSecond();// 活动开始时间
		long upTime = lianchong.getUpdateTime();
		long dTime = GameSystemTime.getSystemMillTime();
		if (startTime > upTime && startTime < dTime) {// 如果活动开始时间大于业务上次更新时间，清理业务数据
			lianchong.setDayRecharge(0);
			lianchong.setDayReward(0);
//			lianchong.setActiveEndTime(configSong.getEndTimeByMillSecond());
			if (lianchong.getGoldRewardInfoList() != null) {
				lianchong.getGoldRewardInfoList().clear();// 清空数据
			}
			lianchong.setGoldRewardInfo(null);
			lianchong.setUpdateTime(GameSystemTime.getSystemMillTime());
			roleLianchongDao.cacheUpdate(lianchong, userRoleId);
		}
		
		// 跨天了，重置当日领取数据状态
		if (!DatetimeUtil.dayIsToday(lianchong.getUpdateTime())) {
			lianchong.setDayRecharge(0);
			lianchong.setDayReward(0);
			List<JSONArray> all = lianchong.getGoldRewardInfoList();
			if (all != null) {
				for (JSONArray json : all) {
					if (json != null && json.size() > 0) {
						json.set(0, 0); // 连充状态全部更新为今天未完成目标
					}
				}
			}
			lianchong.setUpdateTime(GameSystemTime.getSystemMillTime());
			roleLianchongDao.cacheUpdate(lianchong, userRoleId);
		}
		// 当天活动上之前可能玩家已经有充值记录，检查下充值记录,更新的连充活动数据
		RoleYuanbaoRecord roleYuanbaoRecord = refabuActivityExportService.getRoleYuanbaoRecord(userRoleId);
		// roleYuanbaoRecord.setCzValue(5000); // ****测试***
		if (roleYuanbaoRecord != null && roleYuanbaoRecord.getCzValue() > lianchong.getDayRecharge()) {
			// 替换当日充值总额
			lianchong.setDayRecharge(roleYuanbaoRecord.getCzValue());
			if (type == LianchongConstants.flag_1) {
				// 更新连充状态数据
				LianChongConfigGroup group = LianChongConfigExportService.getInstance().loadByMap(subId);
				if (group != null) {
					List<LianChongConfig> configs = group.getConfigs();
					updateLianchongStateData(configs, lianchong);
				} else {
					ChuanQiLog.error("LianChongConfigGroup is null");
				}
			}
			roleLianchongDao.cacheUpdate(lianchong, userRoleId);
		}
		return lianchong;
	}
}
