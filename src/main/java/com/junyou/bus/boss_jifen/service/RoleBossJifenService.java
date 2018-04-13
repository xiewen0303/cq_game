package com.junyou.bus.boss_jifen.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.boss_jifen.configure.RoleBossJifenConfig;
import com.junyou.bus.boss_jifen.configure.RoleBossJifenConfigExportService;
import com.junyou.bus.boss_jifen.dao.RoleBossJifenDao;
import com.junyou.bus.boss_jifen.entity.RoleBossJifen;
import com.junyou.bus.tunnel.BusMsgSender;
import com.junyou.cmd.ClientCmdType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.err.AppErrorCode;
import com.junyou.utils.datetime.GameSystemTime;

@Service
public class RoleBossJifenService {

	@Autowired
	private RoleBossJifenDao bossJifenDao;
	@Autowired
	private RoleBossJifenConfigExportService roleBossJifenConfigExportService;

	public List<RoleBossJifen> initRoleBossJifen(Long userRoleId) {
		return bossJifenDao.initRoleBossJifen(userRoleId);
	}

	public RoleBossJifen getRoleBossJifen(Long userRoleId) {
		RoleBossJifen record = bossJifenDao.cacheLoad(userRoleId, userRoleId);
		if (record == null) {
			long systemMillTime = GameSystemTime.getSystemMillTime();
			RoleBossJifen entity = new RoleBossJifen();
			entity.setConfigId(0);
			entity.setCreateTime(systemMillTime);
			entity.setUpdateTime(systemMillTime);
			entity.setJifen(0l);
			entity.setUserRoleId(userRoleId);
			bossJifenDao.cacheInsert(entity, userRoleId);
			return entity;
		}
		return record;
	}

	public Object[] addJifen(Long userRoleId, long addJifen) {
		if (addJifen < 0) {
			return AppErrorCode.CONFIG_ERROR;
		}

		RoleBossJifen record = getRoleBossJifen(userRoleId);
		record.setJifen(record.getJifen() + addJifen);
		record.setUpdateTime(GameSystemTime.getSystemMillTime());
		bossJifenDao.cacheUpdate(record, userRoleId);
		// 通知前端积分变化
		BusMsgSender.send2One(userRoleId, ClientCmdType.BOSS_JIFEN_CHANGE, record.getJifen());
		return null;
	}

	public Object[] info(Long userRoleId) {
		RoleBossJifen record = getRoleBossJifen(userRoleId);
		
		if(record.getConfigId() >0){
			RoleBossJifenConfig config = roleBossJifenConfigExportService.getConfigByConfigId(record.getConfigId());
			if (config == null) {
				return AppErrorCode.CONFIG_ERROR;
			}
		}

		return new Object[] { AppErrorCode.SUCCESS,record.getConfigId(), record.getJifen() };
	}

	public Map<String, Long> getRoleBossJifenAttrs(Long userRoleId) {
		RoleBossJifen info = bossJifenDao.cacheLoad(userRoleId, userRoleId);
		if (info != null) {
			RoleBossJifenConfig config = roleBossJifenConfigExportService.getConfigByConfigId(info.getConfigId());
			if (config != null) {
				return config.getTotalAttrs();
			}
		}
		return null;
	}

	/**
	 * 通知场景里面属性变化
	 */
	private void notifyStageChange(long userRoleId) {
		BusMsgSender.send2Stage(userRoleId, InnerCmdType.INNER_BOSS_JIFEN_CHANGE, getRoleBossJifenAttrs(userRoleId));
	}

	public Object[] upgrade(Long userRoleId, int targetId) {
		RoleBossJifenConfig nextConfig = roleBossJifenConfigExportService.getConfigByConfigId(targetId);
		if (nextConfig == null) {
			return AppErrorCode.CONFIG_ERROR;
		}

		RoleBossJifen info = getRoleBossJifen(userRoleId);
		if(targetId <= info.getConfigId()){
			return AppErrorCode.BOSS_JIFEN_ALREADY_ACTIVATED;
		}
		if (targetId - info.getConfigId() != 1) {
			return AppErrorCode.CONFIG_ERROR;
		}

		Long consumeJifen = nextConfig.getConsumeJifen();
		if (consumeJifen == null) {
			return AppErrorCode.CONFIG_ERROR;
		}
		if (info.getJifen() < consumeJifen) {
			return AppErrorCode.BOSS_JIFEN_NOT_ENOUGH;
		}

		info.setConfigId(nextConfig.getId());
		info.setJifen(info.getJifen() - consumeJifen);
		info.setUpdateTime(GameSystemTime.getSystemMillTime());
		bossJifenDao.cacheUpdate(info, userRoleId);
		notifyStageChange(userRoleId);
		// 通知前端积分变化
		BusMsgSender.send2One(userRoleId, ClientCmdType.BOSS_JIFEN_CHANGE, info.getJifen());
		return new Object[] { AppErrorCode.SUCCESS, targetId };
	}
}
