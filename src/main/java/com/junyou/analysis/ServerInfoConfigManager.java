package com.junyou.analysis;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.junyou.log.ChuanQiLog;
import com.junyou.utils.exception.JunYouCustomException;
import com.junyou.utils.xml.XMLUtil;


public class ServerInfoConfigManager {

	private static final Logger logger = LogManager.getLogger(ServerInfoConfig.class);
	
	/**游戏程序必要端口标识配置**/
	private GameAppConfig gameAppConfig = new GameAppConfig();
	
	/**跨服游戏基础配置**/
	private KuafuAppConfig kuafuAppConfig = new KuafuAppConfig();
	
	/**游戏基础配置信息**/
	private ServerInfoConfig serverInfoConfig = new ServerInfoConfig();
	
	/**游戏场景基础配置信息**/
	private StageInfoConfig stageInfoConfig = new StageInfoConfig();
	
	
	private static final ServerInfoConfigManager INSTANCE = new ServerInfoConfigManager();
	
	private ServerInfoConfigManager(){}
	
	public static ServerInfoConfigManager getInstance() {
		return INSTANCE;
	}

	private Element root;
	
	public void load(String fileName) throws JunYouCustomException {
		String pathBase = ClassLoader.getSystemResource("").getFile()+"/config/" + fileName;
		File file = new File(pathBase);
		if (!file.exists()) {
			throw new JunYouCustomException("config file not found");
		}
		try {
			load(file);
		} catch (Exception e) {
			throw new JunYouCustomException("加载配置文件失败: " + file.getAbsolutePath(), e);
		}
	}
	
	public void loadCheck2out(String fileName){
		String pathBase = "cq-config/config/"+ fileName;

		File file = new File(pathBase);
		if (!file.exists()) {
			throw new JunYouCustomException("config file not found");
		}
		try {
			load(file);
		} catch (Exception e) {
			throw new JunYouCustomException("加载配置文件失败: " + file.getAbsolutePath(), e);
		}
	}
	
	public void load(File file) throws JunYouCustomException {
		logger.info("加载服务器配置文件开始: {}", file.getAbsolutePath());
		SAXReader reader = new SAXReader();
		Document doc;
		try {
			doc = reader.read(file);
			
			root = doc.getRootElement();
//			gameXmlConfig.setContent(root.asXML());
			
		} catch (DocumentException e) {
			throw new JunYouCustomException("failed to load config file", e);
		}
		
		parse4GameXmlConfig();
		
		logger.info("加载服务器配置文件完成: {}", file.getAbsolutePath());
	}
	
	
	private void parse4GameXmlConfig() {
		
		int clientIoConcurrent = XMLUtil.attributeValueInt(root, "clientIoConcurrent");
		serverInfoConfig.setClientIoConcurrent(clientIoConcurrent);
		
		boolean openRoleNamePrefix = XMLUtil.attributeValueBoolean(root, "openRoleNamePrefix");
		serverInfoConfig.setOpenRoleNamePrefix(openRoleNamePrefix);
		String roleNamePrefix = XMLUtil.attributeValueString(root, "roleNamePrefix");
		serverInfoConfig.setRoleNamePrefix(roleNamePrefix);
		
		boolean gmOpen = XMLUtil.attributeValueBoolean(root, "gmOpen");
		serverInfoConfig.setGmOpen(gmOpen);
		
		boolean needFlashSafe = XMLUtil.attributeValueBoolean(root, "needFlashSafe");
		serverInfoConfig.setNeedFlashSafe(needFlashSafe);
		
		boolean isTencent = XMLUtil.attributeValueBoolean(root, "isTencent");
		serverInfoConfig.setTencent(isTencent);
		//gameApp
		{
			Element gameApp = XMLUtil.subElement(root, "gameApp");
			
			gameAppConfig.setDebug(XMLUtil.attributeValueBoolean(gameApp, "isDebug"));
			gameAppConfig.setCheckBootDb(XMLUtil.attributeValueBoolean(gameApp, "checkBootDb"));
			gameAppConfig.setDsTiRole(XMLUtil.attributeValueBoolean(gameApp, "dsTiRole"));
			gameAppConfig.setServerId(XMLUtil.attributeValueString(gameApp, "serverId"));
			gameAppConfig.setServerName(XMLUtil.attributeValueString(gameApp, "serverName"));
			gameAppConfig.setPlatformId(XMLUtil.attributeValueString(gameApp, "platformId"));
			gameAppConfig.setGameServerPort(XMLUtil.attributeValueInt(gameApp, "gameServerPort"));
			gameAppConfig.setGameHttpPort(XMLUtil.attributeValueInt(gameApp, "gameHttpPort"));
			gameAppConfig.setPlatformServerId(XMLUtil.attributeValueString(gameApp, "platformServerId"));
			
			try {
				gameAppConfig.setUseLog2db(XMLUtil.attributeValueBoolean(gameApp, "isUseLog2db"));
			} catch (JunYouCustomException e1) {
				ChuanQiLog.debug("isUseLog2db is not exits");
			}
			
			boolean printJyLog = false;
			try{
				printJyLog = XMLUtil.attributeValueBoolean(gameApp, "printJyLog");
			}catch (Exception e) {
			}
			gameAppConfig.setPrintJyLog(printJyLog);
			try{
				String jyLogUrl = XMLUtil.attributeValueString(gameApp, "jyLogUrl");
				gameAppConfig.setJyLogUrl(jyLogUrl);
			}catch (Exception e) {
			}
		}
		
		//kuafu <kuafu isKfServer="false" initSessionSize="3" protocol="fst"/>
		{
			Element kuafu = XMLUtil.subElement(root, "kuafu");
			
			kuafuAppConfig.setKfServer(XMLUtil.attributeValueBoolean(kuafu, "isKfServer"));
			kuafuAppConfig.setInitSessionSize(XMLUtil.attributeValueInt(kuafu, "initSessionSize"));
			kuafuAppConfig.setProtocol(XMLUtil.attributeValueString(kuafu, "protocol"));
			kuafuAppConfig.setKuafuConfigRemoteDir(XMLUtil.attributeValueString(kuafu, "kuafuConfigRemoteDir"));
		}
		
		//stageParams <stageParams aoiWidth = "500" aoiHeight = "400" monsterDeadShow = "true"/>
		{
			Element stageParams = XMLUtil.subElement(root, "stageParams");
			
			stageInfoConfig.setAoiWidth(XMLUtil.attributeValueInt(stageParams, "aoiWidth"));
			stageInfoConfig.setAoiHeight(XMLUtil.attributeValueInt(stageParams, "aoiHeight"));
			stageInfoConfig.setMonsterDeadShow(XMLUtil.attributeValueBoolean(stageParams, "monsterDeadShow"));
		}
		
		
		// download
		{
			Element download = XMLUtil.subElement(root, "download");
			
			boolean isDownload = XMLUtil.attributeValueBoolean(download, "isOpen");
			serverInfoConfig.setDownload(isDownload);
			String remoteSysConfigBaseUrl = XMLUtil.attributeValueString(download, "remoteSysConfigBaseUrl");
			serverInfoConfig.setRemoteSysConfigBaseUrl(remoteSysConfigBaseUrl);
			String remoteMapConfigBaseUrl = XMLUtil.attributeValueString(download, "remoteMapConfigBaseUrl");
			serverInfoConfig.setRemoteMapConfigBaseUrl(remoteMapConfigBaseUrl);
			String remotePbzBaseUrl = XMLUtil.attributeValueString(download, "remotePbzConfigBaseUrl");
			serverInfoConfig.setRemotePbzBaseUrl(remotePbzBaseUrl);
			String remoteMapListConfigBaseUrl = XMLUtil.attributeValueStringOrNull(download, "remoteMapListConfigBaseUrl");
			if(remoteMapListConfigBaseUrl == null){
				remoteMapListConfigBaseUrl = remoteSysConfigBaseUrl;
			}
			serverInfoConfig.setRemoteMapListConfigBaseUrl(remoteMapListConfigBaseUrl);
		}
		
		//localization
		{
			Element localization = XMLUtil.subElement(root, "localization");
			
			serverInfoConfig.setLanguage(XMLUtil.attributeValueString(localization, "language"));
			serverInfoConfig.setCountry(XMLUtil.attributeValueString(localization, "country"));
			serverInfoConfig.setYbodds(XMLUtil.attributeValueInt(localization, "ybodds"));
			serverInfoConfig.setZwChar(XMLUtil.attributeValueBoolean(localization, "isZwChar"));
		}
	}
	
	/**
	 * 获取游戏基础配置数据
	 * @return
	 */
	public ServerInfoConfig getServerInfoConfig(){
		return serverInfoConfig;
	}
	
	/**
	 * 获取游戏场景基础配置数据
	 * @return
	 */
	public StageInfoConfig getStageInfoConfig(){
		return stageInfoConfig;
	}

	/**
	 * 获取游戏程序必要端口标识配置
	 * @return
	 */
	public GameAppConfig getGameAppConfig() {
		return gameAppConfig;
	}

	/**
	 * 获取跨服游戏基础配置
	 * @return
	 */
	public KuafuAppConfig getKuafuAppConfig() {
		return kuafuAppConfig;
	}
	
	
	
}
