package com.junyou.bus.flower.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.bag.BagSlots;
import com.junyou.bus.bag.GoodsSource;
import com.junyou.bus.bag.export.RoleBagExportService;
import com.junyou.bus.bag.export.RoleItemExport;
import com.junyou.bus.flower.configure.FlowerConfig;
import com.junyou.bus.flower.configure.FlowerConfigExportService;
import com.junyou.bus.flower.constants.FlowerConstants;
import com.junyou.bus.flower.dao.RoleSendFlowerDao;
import com.junyou.bus.flower.entity.RoleSendFlower;
import com.junyou.bus.rfbflower.export.FlowerCharmRankExportService;
import com.junyou.bus.role.export.RoleExportService;
import com.junyou.bus.role.export.RoleWrapper;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.event.FlowerSendLogEvent;
import com.junyou.event.publish.GamePublishEvent;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.goods.configure.export.GoodsConfig;
import com.junyou.gameconfig.goods.configure.export.GoodsConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.FlowerSendPublicConfig;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class FlowerService {

	@Autowired
	private FlowerConfigExportService flowerConfigExportService;
	@Autowired
	private RoleExportService roleExportService;
	@Autowired
	private GoodsConfigExportService goodsConfigExportService;

	@Autowired
	private RoleBagExportService roleBagExportService;
	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;
	@Autowired
	private SessionManagerExportService sessionManagerExportService;

	@Autowired
	private RoleSendFlowerDao roleSendFlowerDao;
	@Autowired
	private FlowerCharmRankExportService flowerCharmRankExportService;

	/**
	 * 初始化
	 * 
	 * @param userRoleId
	 * @return
	 */
	public List<RoleSendFlower> initData(long userRoleId) {
		return roleSendFlowerDao.initRoleSendFlower(userRoleId);
	}
	
	private Map<Long,Object> locks = new ConcurrentHashMap<Long, Object>();
	
	//  登陆时候调用
	public void handleUserLogin(Long userRoleId){
		locks.put(userRoleId, new Object());
		
	}
	//  离线的时候调用
	public void handleOffline(Long userRoleId){
		locks.remove(userRoleId);
	}

	/**
	 * 送花
	 * 
	 * @param userRoleId
	 * @param configId
	 * @return
	 */
	public  Object[] sendFlower(Long userRoleId, long guid, Long receiverRoleId) {

		RoleItemExport item = roleBagExportService.getBagItemByGuid(userRoleId, guid);
		if (item == null) {
			return AppErrorCode.GOODS_NOT_ENOUGH;// 物品不存在
		}
		FlowerConfig config = flowerConfigExportService.loadByConfigId(item.getGoodsId());
		if (config == null) {
			return AppErrorCode.CONFIG_ERROR;
		}

		if (userRoleId.longValue() == receiverRoleId.longValue()) {
			return AppErrorCode.FLOWER_SEND_NO_MYSELF;// 不能送自己
		}

		RoleWrapper receiveRoleWrapper = null;
		if (!sessionManagerExportService.isOnline(receiverRoleId)) {
			receiveRoleWrapper = roleExportService.getUserRoleFromDb(receiverRoleId);
			if (receiveRoleWrapper == null) {
				return AppErrorCode.FLOWER_SEND_NO_KUAFU;// 不能跨服送花
			} else {
				return AppErrorCode.ROLE_NOT_LOGIN;
			}
		} else {
			receiveRoleWrapper = roleExportService.getLoginRole(receiverRoleId);
		}
		if (receiveRoleWrapper == null) {
			return AppErrorCode.ROLE_NOT_LOGIN;
		}

		FlowerSendPublicConfig flowerSendPublicConfig = gongGongShuJuBiaoConfigExportService.loadPublicConfig(PublicConfigConstants.MOD_FLOWER);
		if (flowerSendPublicConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		RoleWrapper roleWrapper = roleExportService.getLoginRole(userRoleId);
		if (roleWrapper.getLevel() < flowerSendPublicConfig.getOpen()) {
			return AppErrorCode.LEVEL_NOT_ENOUGH;
		}

		GoodsConfig goodsConfig = goodsConfigExportService.loadById(item.getGoodsId());
		if (goodsConfig == null) {
			return AppErrorCode.GOODS_NOT_ENOUGH;
		}
		Object lock = locks.get(receiverRoleId);
		if(lock == null){
			ChuanQiLog.error("send flower lock objec is null");
			return AppErrorCode.ERR;
		}
		synchronized (lock) {
			BagSlots bagSlots = roleBagExportService.removeBagItemByGuid(guid, 1, userRoleId, GoodsSource.FLOWER_SEND, true, true);
			if (!bagSlots.isSuccee()) {
				return bagSlots.getErrorCode();
			}
			RoleSendFlower receiverRoleSendFlower = getRoleSendFlower(receiverRoleId);
			receiverRoleSendFlower.setUpateTime(GameSystemTime.getSystemMillTime());
			receiverRoleSendFlower.setCharmValue(receiverRoleSendFlower.getCharmValue() + config.getCharmValue().longValue());
			roleSendFlowerDao.cacheUpdate(receiverRoleSendFlower, receiverRoleId);
			
			// 更新redis个人排行榜数据
			flowerCharmRankExportService.updateRedisFlowerCharmRank(receiverRoleId, config.getCharmValue());
			// 日志
			GamePublishEvent.publishEvent(new FlowerSendLogEvent(userRoleId, receiverRoleId, config.getConfigId(), config.getGoodId()));
			// 通知收花人
			BusMsgSender.send2One(receiverRoleId, ClientCmdType.FLOWER_RECEIVE, new Object[] { roleWrapper.getName(), 
					roleWrapper.getId(), config.getConfigId(),receiverRoleSendFlower.getCharmValue() });
			// 全服公告
			if (config.getNotice() == FlowerConstants.NOTICE_FLAG) {
//				BusMsgSender.send2All(ClientCmdType.NOTIFY_CLIENT_ALERT4, new Object[] { AppErrorCode.FLOWER_SEND_NOTICE_ALL,
//						new Object[] { roleWrapper.getName(), receiveRoleWrapper.getName(), goodsConfig.getName() } });
				// 通知client  
				BusMsgSender.send2All(ClientCmdType.FLOWER_NOTICE_ALL, 
						new Object[]{ config.getConfigId(),roleWrapper.getName(), receiveRoleWrapper.getName(), goodsConfig.getName()});
			}
		}

		return new Object[] { 1, config.getConfigId() };
	}

	public RoleSendFlower getRoleSendFlower(long receiverRoleId) {
		RoleSendFlower roleSendFlower = roleSendFlowerDao.cacheLoad(receiverRoleId, receiverRoleId);
		if (roleSendFlower == null) {
			roleSendFlower = new RoleSendFlower();
			roleSendFlower.setUserRoleId(receiverRoleId);
			roleSendFlower.setCharmValue(0L);
			roleSendFlower.setUpateTime(GameSystemTime.getSystemMillTime());
			roleSendFlower.setCreateTime(new Timestamp(GameSystemTime.getSystemMillTime()));
			roleSendFlowerDao.cacheInsert(roleSendFlower, receiverRoleId);
		}
		return roleSendFlower;
	}

}
