package com.junyou.bus.shenmo.util;

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
import com.junyou.gameconfig.publicconfig.configure.export.ShenMoPublicConfig;
import com.kernel.spring.container.DataContainer;

@Service
public class KuafuMatch4v4Manager {

	@Autowired
	private BusScheduleExportService scheduleExportService;
	@Autowired
	private DataContainer dataContainer;

	@Autowired
	private GongGongShuJuBiaoConfigExportService gongGongShuJuBiaoConfigExportService;

	private Map<Long, KuafuMatch4v4> matchMap = new ConcurrentHashMap<Long, KuafuMatch4v4>();

	private Map<Long, Long> userMatchMap = new ConcurrentHashMap<Long, Long>();

	public Long getMatchId(Long userRoleId) {
		return userMatchMap.get(userRoleId);
	}

	public void removeUserMatch(Long userRoleId) {
		userMatchMap.remove(userRoleId);
	}

	public KuafuMatch4v4 getMatchById(Long matchId) {
		return matchMap.get(matchId);
	}

	public KuafuMatch4v4 removeMatchById(Long matchId) {
		return matchMap.remove(matchId);
	}

	public ShenMoPublicConfig getPublicConfig() {
		return gongGongShuJuBiaoConfigExportService
				.loadPublicConfig(PublicConfigConstants.MOD_SHENMO);
	}

	public void enterMatch(Long userRoleId, String userRoleName, Long matchId,
			Integer jifen, int camp,String serverId, Object[] roleData) {
		KuafuMatch4v4 match = null;
		synchronized (this) {
			match = matchMap.get(matchId);
			if (match == null) {
				match = createMatch(matchId);
				matchMap.put(matchId, match);
			}
		}
		KuafuMatch4v4Member member = new KuafuMatch4v4Member();
		member.setMatchId(matchId);
		member.setRoleId(userRoleId);
		member.setCamp(camp);
		member.setServerId(serverId);
		member.setName(userRoleName);
		member.setJifen(jifen);
		member.setOnline(true);
		match.addMember(member);
		userMatchMap.put(userRoleId, matchId);
		dataContainer.putData(GameConstants.COMPONENET_KUAFU_DATA,
				userRoleId.toString(), roleData);
	}

	private KuafuMatch4v4 createMatch(Long matchId) {
		KuafuMatch4v4 match = new KuafuMatch4v4(matchId);
		matchMap.put(matchId, match);
		// 起定时进入场景
		BusTokenRunable runable = new BusTokenRunable(
				GameConstants.DEFAULT_ROLE_ID,
				InnerCmdType.KUAFU_ARENA_4V4_ENTER_STAGE, matchId);
		ShenMoPublicConfig publicConfig = getPublicConfig();
		scheduleExportService.schedule(
				GameConstants.DEFAULT_ROLE_ID.toString(),
				GameConstants.COMPONENT_KUAFU_ARENA_4V4_ENTER_STAGE + matchId,
				runable, publicConfig.getDaojishi1(), TimeUnit.SECONDS);
		return match;
	}

}
