package com.junyou.bus.sync;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import com.junyou.cmd.InnerCmdType;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.share.export.PublicRoleStateExportService;
import com.kernel.pool.executor.Message;
import com.kernel.sync.Lock;
import com.kernel.sync.LockManager;

/**
 * @description 同步切面
 * @author hehj
 * 2011-11-29 上午10:28:18
 */
public class BusSyncAspect {

	private LockManager lockManager = null;

	@Autowired
	private PublicRoleStateExportService publicRoleStateExportService;
	
	public void setLockManager(LockManager lockManager) {
		this.lockManager = lockManager;
	}

	/**
	 * 同步处理通知业务实现
	 */
	public Object sync(ProceedingJoinPoint pjp){
		Message msg = (Message) pjp.getArgs()[0];
		
		Lock lock = lockManager.getLock(GameConstants.COMPONENT_BUS_SHARE, msg.getRoleId().toString());
		synchronized (lock) {
			try {
				
				if(publicRoleStateExportService.isPublicOnline(msg.getRoleId()) || InnerCmdType.BUS_INIT_IN == msg.getCommand() || InnerCmdType.BUS_INIT_OUT == msg.getCommand() ){
					Object ret = pjp.proceed();
					return ret;
				}else{
					ChuanQiLog.error(new StringBuilder("not online,").append(msg.toString()).toString());
					return null;
				}
				
			} catch (Throwable e) {
				throw new RuntimeException("error in sync invoke",e);
			}
		}
	}
	
	private String getLockKey(String component,String roleid){
		
		StringBuilder keyBuilder = new StringBuilder(component);
		keyBuilder.append(roleid);
		
		return keyBuilder.toString();
	}
}
