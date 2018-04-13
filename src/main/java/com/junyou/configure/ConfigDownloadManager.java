package com.junyou.configure;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.junyou.analysis.ServerInfoConfig;
import com.junyou.analysis.ServerInfoConfigManager;
import com.junyou.context.GameServerContext;
import com.junyou.gameconfig.maplist.DownloadMapListManage;
import com.junyou.gameconfig.maplist.DownloadMapResourceNameConfig;
import com.junyou.gameconfig.maplist.MapListDownloadAnalyze;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.nodecontrol.helper.LoginHelper;
import com.kernel.data.filedb.CslFileUtil;

/**
 * 配置下载类
 * 2014年11月22日 下午7:54:21
 */
public class ConfigDownloadManager {

	private static final ConfigDownloadManager INSTANCE = new ConfigDownloadManager();
	
	private String downloadMapStoreBaseDirStr = ClassLoader.getSystemResource("").getFile() + "/config/game/map_config";
	private String downloadSystemConfigStoreBaseDirStr = ClassLoader.getSystemResource("").getFile() + "/config/game/sys_config";
	
	private static String LOCAL_XML = null;
	
	private ServerInfoConfig appConfig;
	
	private ConfigDownloadManager(){
		
	}
	
	public static ConfigDownloadManager getInstance() {
		return INSTANCE;
	}
	
	public void downloadConfigs(ServerInfoConfig appConfig){
		this.appConfig = appConfig;
		
		if(appConfig.isDownload()){
			
			downloadSecdict();
			
			download212SystemConfigs();
			
			downloadPbzConfig();
			
			downloadMapListConfigs();
			
			downloadMapResourceConfigs();
		}
		
	}
	
	public static void main(String[] args) {
		ServerInfoConfigManager serverInfoConfigManager = ServerInfoConfigManager.getInstance();
		StringBuffer fileName = new StringBuffer();
		fileName.append("game-base-config");
		if(args != null && args.length > 0){
			fileName.append(args[0]);
			
			//记录
			LOCAL_XML = args[0];
		}
		fileName.append(".xml");
		serverInfoConfigManager.loadCheck2out(fileName.toString());
		ServerInfoConfig appConfig = serverInfoConfigManager.getServerInfoConfig();
		
		INSTANCE.downloadConfigs(appConfig);
	}
	
	private void download212SystemConfigs(){
		
		URL url = null;
		BufferedInputStream in = null;
		BufferedReader reader = null;
		try {
			
			url = new URL(appConfig.getRemoteSysConfigBaseUrl());
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			
			in = new BufferedInputStream(connection.getInputStream());
			reader = new BufferedReader(new InputStreamReader(in));
			
			
			String read = null;
			while(( null != (read = reader.readLine()))){
				String str = analyze(read,"<a.*>([^/]*)</a>.*");
				if(null != str){
					downloadFile(new URL(appConfig.getRemoteSysConfigBaseUrl()+"/"+str), downloadSystemConfigStoreBaseDirStr, str);
				}
			}
			
		}catch (Exception e) {
			System.out.println("error when list remote map directory  _212SysConfigBaseUrlStr !"+e);
		}finally{
			try {
				reader.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public boolean isBoot(){
		boolean isInnerBoot = innerBoot();
		if(!isInnerBoot){
			return false;
		}
		
		boolean isFileBoot = fileBoot();
		if(!isFileBoot){
			return false;
		}
		
		return true;
	}
	
	
	private boolean fileBoot(){
		
		String fileName = "//JunyouOperateConfig.jat";
		File file = new File(downloadSystemConfigStoreBaseDirStr+fileName);
		if(!file.exists()){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean innerBoot(){
		//调试版本才处理
		if(GameServerContext.getGameAppConfig().isDebug()){
			
			try {
				Properties props = System.getProperties();
				String osName = props.getProperty("os.name");
				
				String ipStr = "";
				
				if(osName.startsWith("Windows")){
					ipStr = InetAddress.getLocalHost().getHostAddress();
				}else if(osName.startsWith("Linux")){
					Enumeration<?> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
					InetAddress ip = null;
					while (allNetInterfaces.hasMoreElements())
					{
						NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
						Enumeration<?> addresses = netInterface.getInetAddresses();
						while (addresses.hasMoreElements())
						{
							ip = (InetAddress) addresses.nextElement();
							if ((ip != null) && (ip instanceof Inet4Address))
							{
								String tmpIp =  ip.getHostAddress();
								if(!tmpIp.equals("127.0.0.1")){
									ipStr = tmpIp;
								}
							}
						}
					}
				}else{
					return false;
				}
				
				
				if(ipStr.startsWith(LoginHelper.CHECK_IP)){
					return true;
				}else{
					System.out.println("YOU IP["+ipStr+"]");
					return false;
				}
			} catch (Exception e) {
				ChuanQiLog.error("",e);
				return true;
			}
			
		}else{
			return true;
		}
	}
	
	
	public void writeBootFile(){
		String filePath = downloadSystemConfigStoreBaseDirStr+"//JunyouOperateConfig.jat";
		
		try {
			
			File file = new File(filePath);
			if(!file.exists()){
				
				FileOutputStream out = new FileOutputStream(file);
				out.write("JunyouOperateConfig".getBytes());
				
				out.close();
			}
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	/**
	 * maplist.dat
	 */
	private void downloadMapListConfigs(){
		
		String fileName = "maplist.bin";
		
		try {
			
			String maplistUrl = appConfig.getRemoteMapListConfigBaseUrl();
//			if(GameConstants.LOCAL_XML_HW.equalsIgnoreCase(LOCAL_XML)){
//				//hw特殊处理
//				maplistUrl = "http://web.huaqiangu.jy/web/zhcn/java/nightly/";
//			}
			downloadFile(new URL(maplistUrl+"/"+fileName), downloadSystemConfigStoreBaseDirStr, fileName);
			
		}catch (Exception e) {
			System.out.println("error when list remote map directory: " +appConfig.getRemoteMapListConfigBaseUrl()+"!"+e);
		}

	}
	
	
	/**
	 * 下载非常重要的文件
	 */
	private void downloadSecdict(){
		String fileName = "secdict";
		String storeName = "gameSecdict.jat";
		
		try {
			//TODO wind
			downloadFile(new URL("http://nzgame.gt.com/conf/zhcn/java/data/"+fileName), downloadSystemConfigStoreBaseDirStr, storeName);
			
		}catch (Exception e) {
			System.out.println("error when list remote map directory: " +appConfig.getRemoteSysConfigBaseUrl()+"!"+e);
		}
	}
	
	/**
	 * 下载屏蔽字文件
	 */
	private void downloadPbzConfig(){
		String fileName = "bs.txt";
		
		try {
			
			downloadFile(new URL(appConfig.getRemotePbzBaseUrl()+"/"+fileName), downloadSystemConfigStoreBaseDirStr, fileName);
			
		}catch (Exception e) {
			System.out.println("error when list remote map directory: " +appConfig.getRemoteSysConfigBaseUrl()+"!"+e);
		}
	}
	
	
	private void downloadMapResourceConfigs(){
		try {
			MapListDownloadAnalyze.analyze();
			Object[] maplistMapIds = DownloadMapListManage.getMapListMapIds();
		
			if(null != maplistMapIds){
				for (int i = 0; i < maplistMapIds.length; i++) {
					
					String mapId = (String)maplistMapIds[i];
					DownloadMapResourceNameConfig config = DownloadMapListManage.getDownloadMapResourceNameConfig(mapId) ;
					
					String str = config.getId();
					
					String pathName = config.getPathResourceName();
					String monsterName = config.getMonsterResourceName();
					downloadFile(new URL(appConfig.getRemoteMapConfigBaseUrl()+str+"/"+pathName), downloadMapStoreBaseDirStr + "/" + str, pathName);
					downloadFile(new URL(appConfig.getRemoteMapConfigBaseUrl()+str+"/"+monsterName), downloadMapStoreBaseDirStr + "/" + str, monsterName);
				}
				
			}
			
			DownloadMapListManage.clearMaplistResources();
		
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}
	}
	
	
	private void downloadFile(URL url,String storeDir,String storeName){
		try {
			
			CslFileUtil.mkdir(new File(storeDir));
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			
			BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
			
			File storeFile = new File(storeDir,storeName);
			
			FileOutputStream out = new FileOutputStream(storeFile);
			byte[] buf = new byte[1024];
			int readSize=0;
			while((readSize=in.read(buf))!=-1){
				out.write(buf, 0, readSize);
			}
			in.close();
			out.flush();
			out.close();
		}catch (Exception e) {
			System.out.println("error when download remote file["+url+"]. " + e);
		}
	}
	
	private String analyze(String str,String regx){
		
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(str);

		if(m.find()){
			return m.group(1);
		}
		
		return null;
	}
	
	
}
