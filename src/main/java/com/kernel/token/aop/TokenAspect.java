package com.kernel.token.aop;

import org.aspectj.lang.ProceedingJoinPoint;

import com.junyou.log.ChuanQiLog;
import com.kernel.pool.executor.Message;
import com.kernel.token.TokenManager;
import com.kernel.token.annotation.TokenCheck;

public class TokenAspect {

	private TokenManager tokenManager;
	
	public void setTokenManager(TokenManager tokenManager) {
		this.tokenManager = tokenManager;
	}

	/**
	 * 校验token值是否合法
	 * @param pjp
	 * @param tokenCheck
	 */
	public void check(ProceedingJoinPoint pjp,TokenCheck tokenCheck){
		
		try {
			
			String component = tokenCheck.component();
			Message msg = (Message) pjp.getArgs()[0];
			Object[] token = msg.getToken();
			if(null != token){
				
				Object tokenIdentity = token[0];
				Long tokenValue = (Long) token[1];

				if(tokenManager.checkToken(tokenValue, component, tokenIdentity)){
					tokenManager.removeToken(component, tokenIdentity);
					pjp.proceed();
				}
				
			}else{
				ChuanQiLog.error("no token to be checked.");
				throw new RuntimeException("no token to be checked.");
			}
			
			
		} catch (Throwable e) {
			
			ChuanQiLog.error("error in token check invoke log ",e);
			throw new RuntimeException("error in token check invoke");
		}

	}
	
}
