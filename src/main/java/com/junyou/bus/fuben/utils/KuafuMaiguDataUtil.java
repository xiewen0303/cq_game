package com.junyou.bus.fuben.utils;

import com.junyou.stage.model.element.role.IRole;
import com.junyou.utils.ChuanQiConfigUtil;

/**
 * 跨服数据处理工具
 * @author LiuYu
 * 2015-5-23 下午1:34:35
 */
public class KuafuMaiguDataUtil {
	public static Object[] getRoleData(IRole role){
		return new Object[]{
			role.getId()
			,role.getName()
			,ChuanQiConfigUtil.getServerId()
			,role.getFightAttribute().getZhanLi()
			,role.getLevel()
			,role.getBusinessData().getRoleConfigId()
		};
	}
	
	public static String getRedisTeamListKey(int fubenId){
		return "mgf_teams_"+fubenId;
	}
	
	public static int getRedisTeamId(long teamId){
		return (int)(teamId & Integer.MAX_VALUE);
	}
	public static String REDIS_TEAM_ID_KEY = "mgf_teamid_";
	public static String getRedisTeamIdKey(String teamId){
		return REDIS_TEAM_ID_KEY+teamId;
	}
	public static String getRedisServerKey(){
		return "mgf_teams_server_"+ChuanQiConfigUtil.getServerId();
	}
	
}
