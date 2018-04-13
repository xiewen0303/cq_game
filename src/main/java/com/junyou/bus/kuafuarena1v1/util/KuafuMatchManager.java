package com.junyou.bus.kuafuarena1v1.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junyou.bus.share.export.BusScheduleExportService;
import com.junyou.bus.share.schedule.BusTokenRunable;
import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.gameconfig.constants.PublicConfigConstants;
import com.junyou.gameconfig.publicconfig.configure.export.GongGongShuJuBiaoConfigExportService;
import com.junyou.gameconfig.publicconfig.configure.export.KuafuArena1v1PublicConfig;
import com.kernel.spring.container.DataContainer;

@Service
public class KuafuMatchManager {

	@Autowired
	private BusScheduleExportService scheduleExportService;
	@Autowired
	private DataContainer dataContainer;

	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;

	private Map<Long, KuafuMatch> matchMap = new ConcurrentHashMap<Long, KuafuMatch>();

	private Map<Long, Long> userMatchMap = new ConcurrentHashMap<Long, Long>();

	public Long getMatchId(Long userRoleId) {
		return userMatchMap.get(userRoleId);
	}

	public void removeUserMatch(Long userRoleId) {
		userMatchMap.remove(userRoleId);
	}

	public KuafuMatch getMatchById(Long matchId) {
		return matchMap.get(matchId);
	}

	public KuafuMatch removeMatchById(Long matchId) {
		return matchMap.remove(matchId);
	}

	public KuafuArena1v1PublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_KUAFU_ARENA_1v1);
	}

	public void enterMatch(Long userRoleId, String userRoleName,Long matchId, Integer jifen,
			Object[] roleData) {
		KuafuMatch match = null;
		synchronized (this) {
			match = matchMap.get(matchId);
			if (match == null) {
				match = createMatch(matchId);
				matchMap.put(matchId, match);
			}
		}
		KuafuMatchMember member = new KuafuMatchMember();
		member.setMatchId(matchId);
		member.setRoleId(userRoleId);
		member.setName(userRoleName);
		member.setJifen(jifen);
		member.setOnline(true);
		match.addMember(member);
		userMatchMap.put(userRoleId, matchId);
		dataContainer.putData(GameConstants.COMPONENET_KUAFU_DATA,
				userRoleId.toString(), roleData);
	}

	private KuafuMatch createMatch(Long matchId) {
		KuafuMatch match = new KuafuMatch(matchId);
		matchMap.put(matchId, match);
		// 起定时进入场景
		BusTokenRunable runable = new BusTokenRunable(
				GameConstants.DEFAULT_ROLE_ID,
				InnerCmdType.KUAFU_ARENA_1V1_ENTER_STAGE, matchId);
		KuafuArena1v1PublicConfig publicConfig = getPublicConfig();
		scheduleExportService.schedule(
				GameConstants.DEFAULT_ROLE_ID.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_ENTER_STAGE + matchId,
				runable, publicConfig.getDaojishi1(), TimeUnit.SECONDS);
		return match;
	}

}
