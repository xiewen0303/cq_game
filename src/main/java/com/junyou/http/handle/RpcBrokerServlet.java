package com.junyou.http.handle;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.junyou.context.GameServerContext;
import com.junyou.http.MethodWrapper;
import com.junyou.http.codec.TokenConstant;
import com.junyou.http.codec.TokenMsg;
import com.junyou.http.key.KeyEnum;
import com.junyou.http.msg.HttpCallBackMsg;
import com.junyou.http.util.HttpProUtil;
import com.junyou.log.ChuanQiLog;

/**
 * @author DaoZheng Yuan
 * 2014年11月26日 下午1:28:47
 */
public class RpcBrokerServlet {

	public static RpcBrokerServlet getInstance() {
		return InstanceHolder.INSTANCE;
	}

	
	/**
	 * 方法名参数或签名为空
	 */
	private String err1JsonStr;
	/**
	 * 方法无法被调用
	 */
	private String err2JsonStr;
	/**
	 * 加密码签名不通过
	 */
	private String err3JsonStr;
	
	
	private static class InstanceHolder {
		private static final RpcBrokerServlet INSTANCE = new RpcBrokerServlet();
	}
	
	/**
	 * 执行get请求
	 */
	protected final String doGet(TokenMsg token){
		return processRequest(token);
	}
	
	/**
	 * 执行post请求
	 */
	protected final String doPost(TokenMsg token){
		return processRequest(token);
	}
	
	private HttpCallBackMsg getErrorHttpCallBackMsg(){
		HttpCallBackMsg returnValue = new HttpCallBackMsg();
		
		//设置服务器数据
		returnValue.setServerId(GameServerContext.getGameAppConfig().getServerId());   
		returnValue.setServerName(GameServerContext.getGameAppConfig().getServerName());
		returnValue.setPtServerId(GameServerContext.getGameAppConfig().getPlatformServerId());
		returnValue.setPtName(GameServerContext.getGameAppConfig().getPlatformId());
		
		return returnValue;
	}
	
	/**
	 * 实际业务处理
	 * @param token
	 * @return
	 */
	protected String processRequest(TokenMsg token){
		Map<String, Object> paramObject = HttpProUtil.getHttpParams(token.getBusiMessage());
		String functionName = (String) paramObject.remove(TokenConstant.METHOD_PARAM_NAME);
		String sign = (String) paramObject.remove(TokenConstant.SIGN_PARAM_NAME);
		if(functionName == null || sign == null){
			
			if(err1JsonStr == null){
				HttpCallBackMsg httpMsg = getErrorHttpCallBackMsg();
				//方法名参数或签名为空
				httpMsg.setCode(-100);
				err1JsonStr = JSON.toJSONString(httpMsg);
			}
			return err1JsonStr;
		}
		RpcBrokerService rpcBrokerService = RpcBrokerService.getInstance();
		MethodWrapper wrapper = rpcBrokerService.matcherMethod(functionName);
		if(wrapper == null){
			if(err2JsonStr == null){
				HttpCallBackMsg httpMsg = getErrorHttpCallBackMsg();
				//方法无法被调用
				httpMsg.setCode(-200);
				err2JsonStr = JSON.toJSONString(httpMsg);
			}
			return err2JsonStr;
		}
		
		
		boolean canProcess = checkSign(wrapper.getKeyEnum(),sign, paramObject);
		if(canProcess){
			HttpCallBackMsg httpMsg = rpcBrokerService.call(wrapper, paramObject);
			
			ChuanQiLog.error("[HTTP-CALL]=====http remote process methodName[{}] ip[{}]======",functionName,token.getRemoteIp());
			
			return JSON.toJSONString(httpMsg);
		}else{
			if(err3JsonStr == null){
				HttpCallBackMsg httpMsg = getErrorHttpCallBackMsg();
				//加密码签名不通过
				httpMsg.setCode(-300);
				err3JsonStr = JSON.toJSONString(httpMsg);
			}
			return err3JsonStr;
		}
	}
	
	/**
	 * 签名验证
	 * @param keyType http服务key的类型
	 * @param sign 签名
	 * @param paramObject
	 * @return true:验证通过 
	 */
	private boolean checkSign(KeyEnum keyEnum,String sign,Map<String, Object> paramObject){
		String md5Value = HttpProUtil.httpMd5_sign(keyEnum, paramObject);
		return md5Value.equalsIgnoreCase(sign);
	}
}
