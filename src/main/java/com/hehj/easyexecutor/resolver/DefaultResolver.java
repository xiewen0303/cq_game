package com.hehj.easyexecutor.resolver;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hehj.easyexecutor.Interceptor.IInterceptor;
import com.junyou.log.ChuanQiLog;

public class DefaultResolver implements IEasyResolver {

	private Method m;
	private Class<?> targetCls;
	private Class<?>[] paramTypes;
	private Object target;
	
	private List<IInterceptor> globalInterceptors;
	private List<IInterceptor> classInterceptors;
	private List<IInterceptor> methodInterceptors;
	
	public DefaultResolver(Method m,Class<?>[] paramTypes,Class<?> targetCls,Object target,List<IInterceptor> globalInterceptors,List<IInterceptor> classInterceptors,List<IInterceptor> methodInterceptors){
		this.m = m;
		this.paramTypes = paramTypes;
		this.targetCls = targetCls;
		this.target = target;
		this.globalInterceptors = globalInterceptors;
		this.classInterceptors = classInterceptors;
		this.methodInterceptors = methodInterceptors;
	}
	

	@Override
	public void execute(Object message) {
		try {
//			com.kernel.pool.executor.Message msg = (com.kernel.pool.executor.Message)message;
			// global interceptor
			if(null !=globalInterceptors){
				for(IInterceptor interceptor : globalInterceptors){
					if(!interceptor.before(message)) return;
				}
			}
			// class interceptor
			if(null !=classInterceptors){
				for(IInterceptor interceptor : classInterceptors){
					if(!interceptor.before(message)) return;
				}
			}
			// method interceptor
			if(null !=methodInterceptors){
				for(IInterceptor interceptor : methodInterceptors){
					if(!interceptor.before(message)) return;
				}
			}
			// invoke
			m.invoke(target, new Object[] {message});

			if(null !=methodInterceptors){
				for(IInterceptor interceptor : methodInterceptors){
					if(!interceptor.after(message)) return;
				}
			}
			if(null !=classInterceptors){
				for(IInterceptor interceptor : classInterceptors){
					if(!interceptor.after(message)) return;
				}
			}
			if(null !=globalInterceptors){
				for(IInterceptor interceptor : globalInterceptors){
					if(!interceptor.after(message)) return;
				}
			}
		
		} catch (Exception e) {
			ChuanQiLog.error("",e);
			throw new RuntimeException("",e);
		
		}
		
		
	}

}
