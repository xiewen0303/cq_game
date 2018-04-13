package com.hehj.easyexecutor.cmd;

import java.util.HashMap;
import java.util.Map;

import com.hehj.easyexecutor.enumeration.EasyKuafuType;
import com.junyou.cmd.InnerCmdType;
import com.junyou.context.GameServerContext;
import com.junyou.module.GameModType;

public class CmdGroupInfo {
	
	public static final String LOGIN_GROUP = "login";
	public static final String PUBLIC_GROUP = "public";
	public static final String NODE_CONTROL_GROUP = "node-control";
	public static final String GUILD_GROUP = "guild";
	
	public static final String BUS_CACHE_GROUP = "bus_cache";
	public static final String BUS_INIT_GROUP = "bus_init";
	public static final String STAGECONTROL_GROUP = "stage_control";
	public static final String STAGE_GROUP = "stage";
	public static final String KUAFU_GROUP = "kuafu";

	// dest_type 目标类型
	public static final int dest_type_client = 0; 
	public static final int dest_type_bus = 1; 
	public static final int dest_type_stage_control = 2; 
	public static final int dest_type_stage = 3; 
	public static final int dest_type_inout = 4; 
	public static final int dest_type_bus_init = 5; 
	public static final int dest_type_public = 6; 
	public static final int dest_type_inner_system = 7; 
	public static final int dest_type_kuafu_server = 8; 

	// broadcast_type
	public static final int broadcast_type_1 = 1; 
	public static final int broadcast_type_2 = 2; 
	public static final int broadcast_type_3 = 3;
	
	// from_type
	public static final int from_type_client = 1;
	public static final int from_type_bus = 2;
	public static final int from_type_stage = 3;
	public static final int from_type_stage_control = 4;
	
	// node_from_type
	public static final int node_from_type_client = 1;
	public static final int node_from_type_public = 2;
	public static final int node_from_type_gs = 3;
	public static final int node_from_type_kuafu_source = 4;
	public static final int node_from_type_kuafu_server = 5;
	
	/**
	 * key:模块组,value:广播类型
	 */
	private static Map<String, Integer> groupDestMap = new HashMap<String, Integer>();
	static{
		groupDestMap.put(BUS_CACHE_GROUP, dest_type_bus);
		groupDestMap.put(STAGECONTROL_GROUP, dest_type_stage_control);
		groupDestMap.put(STAGE_GROUP, dest_type_stage);
		groupDestMap.put(LOGIN_GROUP, dest_type_inout);
		groupDestMap.put(BUS_INIT_GROUP, dest_type_bus_init);
		groupDestMap.put(PUBLIC_GROUP, dest_type_public);
		groupDestMap.put(KUAFU_GROUP, dest_type_kuafu_server);
	}
	
	
	private static Map<String, String> moduleGroupMap = new HashMap<>();
	/**
	 * key:子模块,value:指令
	 */
	private static Map<Short, String> cmdGroupMap = new HashMap<>();
	private static Map<Short, String> cmdModuleMap = new HashMap<>();
	private static Map<Short, Integer> cmdDestMap = new HashMap<>();
	
	/**
	 * 注册指令
	 * @param cmd
	 * @param moduleName
	 * @param groupName
	 * @param kuafuType
	 * @throws Exception 
	 */
	public static void registerCmds(short cmd,String moduleName,String groupName,EasyKuafuType kuafuType) throws Exception{
		
		if(cmdGroupMap.containsKey(cmd)){
			throw new Exception((InnerCmdType.leaveStage_cmd == cmd) + "重复注册指令 ["+cmd+","+moduleName+","+groupName+"]["+cmdGroupMap.get(InnerCmdType.leaveStage_cmd)+","+cmdModuleMap.get(InnerCmdType.leaveStage_cmd)+"]");
		}
		
		//GM指令在gmOpen是关闭时，不注册指定
		if(GameModType.GM_MODULE.equals(moduleName) && !GameServerContext.getServerInfoConfig().isGmOpen()){
			//未开启GM功能
			return;
		}
		
		cmdGroupMap.put(cmd, groupName);
		moduleGroupMap.put(groupName, moduleName);
		cmdModuleMap.put(cmd, moduleName);
		
		cmdDestMap.put(cmd, groupDestMap.get(groupName));
		
		//跨服指令注册
		EasyKuafuCmdInfo.registerKuafuCmds(cmd, kuafuType);
	}
	
	
	public static Integer getCmdDest(Short cmd){
		return cmdDestMap.get(cmd);
	}
	
	/**
	 * 获取指令所属执行分组
	 * @param cmd
	 * @return
	 */
	public static String getCmdGroup(Short cmd){
		return cmdGroupMap.get(cmd);
	}
	
	/**
	 * 获取指令所属模块名
	 * @param cmd
	 * @return
	 */
	public static String getCmdModule(Short cmd){
		return cmdModuleMap.get(cmd);
	}
	
	
	public static boolean isChatModule(Short cmd){
		String module = cmdModuleMap.get(cmd);
		if(null == module) return false;
		return module.equals(GameModType.CHAT_MODULE);
	}

	public static boolean isTsServerModule(Short cmd){
		String module = cmdModuleMap.get(cmd);
		if(null == module) return false;
		return module.equals(GameModType.TSSERVER_MODULE);
	}
	
	public static boolean isPingModule(Short cmd){
		String module = cmdModuleMap.get(cmd);
		if(null == module) return false;
		return module.equals(GameModType.PING_MODULE);
	}
	
	/**
	 * 判定一个指令是否属于某个模块
	 * @param cmd
	 * @param moduleName
	 * @return
	 */
	public static boolean isModule(Short cmd,String moduleName){
		String module = cmdModuleMap.get(cmd);
		if(null == module) return false;
		return module.equals(moduleName);
	}

	/**
	 * 判定一个模块是否属于某个执行分组
	 * @param module
	 * @param groupName
	 * @return
	 */
	public static boolean isGroup(String module,String groupName){
		String group = moduleGroupMap.get(module);
		if(null == group) return false;
		return group.equals(groupName);
	}
}
