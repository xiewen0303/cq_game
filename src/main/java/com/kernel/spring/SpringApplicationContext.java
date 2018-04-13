package com.kernel.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.junyou.bus.client.io.service.IoServiceImpl;
import com.junyou.bus.share.service.RoleStateService;
import com.junyou.context.GameServerContext;
import com.junyou.io.export.SessionManagerExportService;
import com.junyou.public_.nodecontrol.service.NodeControlService;
import com.junyou.public_.share.service.PublicRoleStateService;
import com.kernel.data.write.async_1.AsyncWriteManager;

@Component
public class SpringApplicationContext implements ApplicationContextAware{

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		SpringApplicationContext.applicationContext = context;
		
		/**
		 * 初始化游戏服务上下文
		 */
		initGameServerConext();
	}

	public static ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	
	/**
	 * 初始化游戏服务上下文
	 */
	private void initGameServerConext(){
		/*注册上下文*/
		GameServerContext.setAsyncWriteManager(getApplicationContext().getBean(AsyncWriteManager.class));
		GameServerContext.setNodeControlService(getApplicationContext().getBean(NodeControlService.class));
		GameServerContext.setPublicRoleStateService(getApplicationContext().getBean(PublicRoleStateService.class));
		GameServerContext.setIoService(getApplicationContext().getBean(IoServiceImpl.class));
		GameServerContext.setSessionManagerExportService(getApplicationContext().getBean(SessionManagerExportService.class));
		GameServerContext.setRoleStateService(getApplicationContext().getBean(RoleStateService.class));
	}
}
