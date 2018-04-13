package com.junyou.gameconfig.goods.configure.export;

 
import org.springframework.stereotype.Component;

import com.junyou.configure.parser.impl.AbsClasspathConfigureParser;
import com.junyou.context.GameServerContext;
import com.junyou.log.ChuanQiLog;
 

/**
 * 非常重要的配置文件 
 * @author DaoZheng Yuan
 * 2015年10月22日 下午8:27:53
 */
@Component
public class GameSecdictConfigExportService extends AbsClasspathConfigureParser{
	 
	/**
	  * configFileName
	 */
	private String configureName = "gameSecdict.jat";
	
	protected String getConfigureName() {
		return configureName;
	}
	
	protected void configureDataResolve(byte[] data) {
		if(data == null){
			ChuanQiLog.error("=========缺少非常重要的配置文件 [secdict],服务器不能起动，强制停止！！！=========");
			System.exit(0);
			return;
		}
		
		//数据放入上下文件管理器里
		GameServerContext.setSecdictBytes(data);
	}
	
}