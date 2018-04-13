package com.junyou.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.junyou.analysis.ServerInfoConfigManager;
import com.junyou.context.GameServerContext;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.common.ConfigureUtil;

public class ChuanQiConfigUtil {

	private static final String VERSION = "config/version.properties";
	private static final String GAMECONFIG_INFO = "config/gameconfig-info.properties";
	private static final String TENCENT_INFO = "config/platform/tencent.properties";
	
	
	public static int getClientIoPort(){
		return GameServerContext.getGameAppConfig().getGameServerPort();
	}

	public static int getClientIoConcurrent(){
		return GameServerContext.getServerInfoConfig().getClientIoConcurrent();
	}
	
	
	//-------------------原run-info配置值 (暂时保留但不读配置了)-----start--------------------------------
	public static int getUseCacheInfo(){
		return 1;
	}

	public static boolean isSwapNode(){
		return true;
	}
	
	public static boolean isClientIoNode(){
		return true;
	}

	public static boolean isPublicNode(){
		return true;
	}
	
	public static boolean isGsNode(){
		return true;
	}
	
	//-----------------------原run-info配置值 -----end----------------------------
	
	/**
	 * 获取http监听端口(可以理解为webservice对外的端口)
	 * @return
	 */
	public static int getWsOutPort(){
		return GameServerContext.getGameAppConfig().getGameHttpPort();
	}
	
	/**
	 * 获取服务器id
	 */
	public static String getServerId(){ 
		return GameServerContext.getGameAppConfig().getServerId();
	}
	/**
	 * 获取服务器名字
	 */
	public static String getServerName(){ 
		return GameServerContext.getGameAppConfig().getServerName();
	}
	
	/**
	 * 获取当前平台的服务器id
	 */
	public static String getPlatformServerId(){ 
		return GameServerContext.getGameAppConfig().getPlatformServerId();
	}
	
	/**
	 * 获取服务器所在平台的id
	 */
	public static String getPlatfromId(){ 
		return GameServerContext.getGameAppConfig().getPlatformId();
	}

	/**
	 * 获取游戏服务器版本号
	 */
	public static String getGsVersion() {
		return ConfigureUtil.getProp(VERSION, "version");
	}
	
	/**
	 * 获取服务器IP地址
	 * @return
	 */
	public static String getServerIp(){
		
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostAddress().toString();//获得本机IP 
		} catch (UnknownHostException e) {
			ChuanQiLog.error("", e);
		}
		return null; 
		
	}
	
	/**
	 * 腾讯url地址
	 * @return
	 */
	public static String getTencentUrl(){
		return ConfigureUtil.getPropAllowNull(TENCENT_INFO, "tencent_url");
	}
	/**
	 * 腾讯管家url地址
	 * @return
	 */
	public static String getTencentGuanjiaUrl(){
		return ConfigureUtil.getPropAllowNull(TENCENT_INFO, "tencent_guanjia");
	}
	
	/**
	 * 腾讯码类型（标识开始字符）
	 * @return
	 */
	public static String getTencentCodeType(){
		return ConfigureUtil.getPropAllowNull(TENCENT_INFO, "code_type");
	}
	
	/**
	 * 腾讯码奖励
	 * @return
	 */
	public static String getTencentCodeReward(){
		return ConfigureUtil.getPropAllowNull(TENCENT_INFO, "code_reward");
	}
	
	/**
	 * 获取后台管理地址的url
	 * @return
	 */
	public static String getManageToolUrl(){
		return ConfigureUtil.getProp(GAMECONFIG_INFO, "config_server_base_url")+"/";
	}
	
	/**
	 * 获取腾讯罗盘数据上传的url
	 * @return
	 */
	public static String getTencentLuoPanUrl(){
		return ConfigureUtil.getProp(GAMECONFIG_INFO, "config_tencent_luopan_url");
	}
	
	/**
	 * 获取腾讯罗盘OSS数据上传的url
	 * @return
	 */
	public static String getTencentLuoPanOssUrl(){
		return ConfigureUtil.getProp(GAMECONFIG_INFO, "config_tencent_luopan_oss_url");
	}
	
	/**
	 * 获取腾讯联盟罗盘数据上传的url
	 * @return
	 */
	public static String getTencentLuoPanLMUrl(){
		return ConfigureUtil.getProp(GAMECONFIG_INFO, "config_tencent_luopan_lm_url");
	}
	
	/**
	 * 获取后台管理下载动态配置目录 地址
	 * @return
	 */
	public static String getLoadDirectoryUrl(){
		return ConfigureUtil.getProp(GAMECONFIG_INFO, "config_load_directory_url");
	}
	/**
	 * 获取全局redis的开关
	 * @return true:开,false：关
	 */
	public static boolean getGlobalRedisOn(){
		try{
			String tmp = ConfigureUtil.getProp(GAMECONFIG_INFO, "global_redis_on");
			return "1".equals(tmp);
		}catch (Exception e) {
			ChuanQiLog.error("no global_redis_on config");
			return false;
		}
	}
	/**
	 * 获取全局redis的ip
	 * @return
	 */
	public static String getGlobalRedisIp(){
		return ConfigureUtil.getProp(GAMECONFIG_INFO, "global_redis_ip");
	}
	/**
	 * 获取全局redis的port
	 * @return
	 */
	public static String getGlobalRedisPort(){
		return ConfigureUtil.getProp(GAMECONFIG_INFO, "global_redis_port");
	}
	public static boolean getIsGlobalMatchServer(){
		try {
			String str = ConfigureUtil.getProp(GAMECONFIG_INFO, "is_global_match_server");
			return str.equals("1");
		}catch (Exception e) {
			ChuanQiLog.error("no is_global_match_server config");
			return false;
		}
	}
	public static boolean getIsGlobalCompetitionMatchServer(){
		try {
			String str = ConfigureUtil.getProp(GAMECONFIG_INFO, "is_global_competition_match_server");
			return str.trim().equals("1");
		}catch (Exception e) {
			return false;
		}
	}
	/**
	 * 获取全局redis的db
	 * @return
	 */
	public static int getGlobalRedisDb(){
		try {
			String dbStr = ConfigureUtil.getProp(GAMECONFIG_INFO, "global_redis_db");
			return Integer.parseInt(dbStr);
		}catch (Exception e) {
			ChuanQiLog.error("no global_redis_db config");
		}
		return 0;
	}
	/**
	 * 获取全局redis的password
	 * @return
	 */
	public static String getGlobalRedisPassword(){
		return ConfigureUtil.getProp(GAMECONFIG_INFO, "global_redis_password");
	}
	
	
	/**
	 * 获取跨服下载配置目录 地址
	 * @return
	 */
	public static String getKuafuConfigRemoteUrl(){
		StringBuffer value = new StringBuffer();
		value.append(getLoadDirectoryUrl()).append("/");
		
		String kuafuRemoteDir = ServerInfoConfigManager.getInstance().getKuafuAppConfig().getKuafuConfigRemoteDir();
		try {
			value.append(kuafuRemoteDir).append("/");
		} catch (Exception e) {
			return null;
		}
		
		return value.toString();
	}
	
	
	/**
	 * 获取游戏的充值兑换比例
	 * @return
	 */
	public static int getGameYbOdds(){
		return GameServerContext.getServerInfoConfig().getYbodds();
	}
	
	/**
	 * 是否需要验证中文字符区间
	 */
	public static boolean isCheckCharsetRegion(){
		return GameServerContext.getServerInfoConfig().isZwChar();
	}
	
	/**
	 * 获取服务器语言环境
	 */
	public static String getServerLanguage(){ 
		return GameServerContext.getServerInfoConfig().getLanguage();
	}
	/**
	 * 获取服务器国家
	 * @return
	 */
	public static String getServerCountry(){ 
		return GameServerContext.getServerInfoConfig().getCountry();
	}
	
	public static boolean isPrintJyLog() {
		return GameServerContext.getGameAppConfig().isPrintJyLog();
	}
	
	public static String getJyLogUrl(){
		return GameServerContext.getGameAppConfig().getJyLogUrl();
	}
	
	/**
	 * 获取后台管理地址的url
	 * @return
	 */
	public static String getGmRootName(){
		return ConfigureUtil.getProp(GAMECONFIG_INFO, "game_web_root_name");
	}
	
	/**
	 * 获取源码部署地址
	 * @return
	 */
	public static String getSourceDir(){
		return ConfigureUtil.getProp(GAMECONFIG_INFO, "game_source_dir");
	}
	
	/**
	 * 获取游戏名字
	 * @return
	 */
	public static String getProjectName(){
		return ConfigureUtil.getProp(GAMECONFIG_INFO, "game_project_name");
	}
}
