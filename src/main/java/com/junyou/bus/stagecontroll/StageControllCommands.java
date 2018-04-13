/**
 * 
 */
package com.junyou.bus.stagecontroll;

/**
 * @author DaoZheng Yuan
 * 2014年11月21日 下午3:23:31
 */
public class StageControllCommands {
	
	public static final String APPLY_CHANGE_STAGE = "20000";
	public static final String APPLY_CHANGE_LINE = "20004";
	
	public static final String CHANGE_STAGE = "21000";
	public static final String INNER_ENTER_STAGE = "S:ENTER";
	public static final String INNER_APPLY_CHANGE_MAP = "SC:APPLY_CHANG_MAP";
	

	public static final String INNER_TELEPORT = "40100";

	public static final String GET_MAP_LINES = "20005";
	
	
	/**
	 * 进入跨服副本的本地地图
	 */
	public static final String INNER_ENTER_KUAFU_LOCAL_COPY = "SC:ENTER_KUAFU_LOCAL_COPY";
	public static final String INNER_SC_KUAFU_COPY = "sc:e_kaf_cp";
	
	/**
	 * 通用离开副本回到正常场景
	 */
	public static final String INNER_T_LEAVEL_COPY = "sc:all_l_cp";
	
	/**
	 * 跨服离线指令(第一步，离开地图)
	 */
	public static final String KUAFU_OFFLINE1 = "sc:le_kaf_1";
	/**
	 * 跨服离线指令(第二步，离开跨服服务器)
	 */
	public static final String KUAFU_OFFLINE2 = "sc:le_kaf_2";

}
