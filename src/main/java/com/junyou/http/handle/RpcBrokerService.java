package com.junyou.http.handle;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.junyou.context.GameServerContext;
import com.junyou.http.MethodWrapper;
import com.junyou.http.key.HttpKeyType;
import com.junyou.http.msg.HttpCallBackMsg;
import com.junyou.http.processor.GameHttpProcessor;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.exception.GameCustomException;

/**
 * @author DaoZheng Yuan
 * 2014年11月25日 下午2:39:44
 */
public class RpcBrokerService {

	private Map<String, MethodWrapper> cachedMethod = new HashMap<String, MethodWrapper>();
	
//	private ServerInfoConfig serverInfoConfig;
	
	public static RpcBrokerService getInstance() {
		return InstanceHanlder.INSTANCE;
	}

	private static class InstanceHanlder {
		private static final RpcBrokerService INSTANCE = new RpcBrokerService();
	}
	
	public void initialize(GameHttpProcessor processor) throws GameCustomException {
		
		ChuanQiLog.debug("================注册Http监听方法开始");
		cachedMethod.putAll(registerMethods(processor));
		ChuanQiLog.debug("================注册Http监听方法结束");
	}
	
	/**
	 * 注册方法
	 * @param instance
	 * @return
	 * @throws GameCustomException
	 */
	private Map<String, MethodWrapper> registerMethods(Object instance) throws GameCustomException {
		Map<String, MethodWrapper> cachedMethod = new HashMap<String, MethodWrapper>();
		Method[] methods = instance.getClass().getDeclaredMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			MethodWrapper methodWrapper = new MethodWrapper(method, instance);
			
			HttpKeyType httpKeyType = method.getAnnotation(HttpKeyType.class);
			if(httpKeyType != null){
				methodWrapper.setKeyEnum(httpKeyType.keyType());
			}
			
			String lowerCaseMethodName = methodName.toLowerCase();
			cachedMethod.put(lowerCaseMethodName, methodWrapper);

		}
		return cachedMethod;
	}
	
	/**
	 * 
	 * @param functionName
	 * @param parameter null，表示是无参数的调用
	 * @return
	 */
	public HttpCallBackMsg call(MethodWrapper wrapper, Object parameter) throws GameCustomException {
		ChuanQiLog.debug("调用======="+wrapper.getMethod().getName());
		HttpCallBackMsg returnValue = null;
		try {
			// 有参数调用
			returnValue = (HttpCallBackMsg) wrapper.getMethod().invoke(wrapper.getInstance(), parameter);

			//设置服务器数据
			returnValue.setServerId(GameServerContext.getGameAppConfig().getServerId());
			returnValue.setServerName(GameServerContext.getGameAppConfig().getServerName());
			returnValue.setPtServerId(GameServerContext.getGameAppConfig().getPlatformServerId());
			returnValue.setPtName(GameServerContext.getGameAppConfig().getPlatformId());
		} catch (Exception e) {
			throw new GameCustomException(e);
		}

		return returnValue;
	}
	
	/**
	 * 匹配执行方法(注意:大小写不敏感)
	 * @param functionName
	 * @param parameter
	 * @return
	 */
	public MethodWrapper matcherMethod(String functionName) {
		String lowerCaseFunctionName = functionName == null ? null : functionName.toLowerCase();
		return cachedMethod.get(lowerCaseFunctionName);
	}
}
