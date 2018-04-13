package com.junyou.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kernel.pool.executor.Message;

public class MsgPrintUtil {

	private static final Log LOG = LogFactory.getLog(MsgPrintUtil.class);

	public static final boolean PUBLIC_PRINT = false;
	public static final String PUBLIC_PREFIX = "public";

	public static final boolean BUS_PRINT = false;
	public static final String BUS_PREFIX = "bus";
	
	public static final boolean STAGE_PRINT = false;
	public static final String STAGE_PREFIX = "stage";
	
	public static final boolean KUAFU_PRINT = false;
	public static final String KUAFU_PREFIX = "kuafu";
	
	public static final boolean IO_PRINT = false;
	public static final String IO_PREFIX = "io";
	
	//忽略打印的部分指指令
	private static Map<Short,Object> ignoreCmds = new HashMap<Short, Object>();
//	static{
//		
//	}
	
	
	private static String isNull(String group){
		if(null == group) return "";
		return " {"+group+"}";
	}
	
	public static boolean needPrint(){
		return LOG.isDebugEnabled();
	}
	
	public static void printMsg(Message msg,boolean print,String prefix,String group){
		if(print){
			if(LOG.isDebugEnabled() && !ignoreCmds.containsKey(msg.getCommand())){
				String msgStr = "c:"+msg.getCommand()
								+",sid:"+msg.getSessionid()
								+",rid:"+msg.getRoleId()
								+",stid:"+msg.getStageId()
								+",d:"+getMsgData(msg.getData());
								;
				
				LOG.error(prefix + isNull(group) + " rec { "+msgStr +" }");
			}
		}
	}
	
	public static void printOutMsg(Object[] sourceMsg,boolean print,String prefix){
		if(print){
			Message msg = new Message(sourceMsg);
			if(LOG.isDebugEnabled() && !ignoreCmds.containsKey(msg.getCommand())){
				Object roleid = sourceMsg[7];
				if(roleid instanceof Object[]){
					roleid = convertArrayToString((Object[])roleid);
				}
				String msgStr = "c:"+msg.getCommand()
								+",sid:"+msg.getSessionid()
								+",rid:"+roleid
								+",stid:"+msg.getStageId()
								+",d:"+getMsgData(msg.getData());
								;
				
				LOG.error(prefix + " sent { "+msgStr +" }");
			}
		}
	}
	
	private static String getMsgData(Object data){
		if(data instanceof Object[]){
			return convertArrayToString((Object[])data);
		}else{
			return "["+data+"]";
		}
	}
	
	private static String convertArrayToString(Object[] objects){
		String s = "[";
		int i = 0;
		for(Object o : objects){

			if(o instanceof Object[]){
				if( i == 0 ){
					s += i + ":" + convertArrayToString((Object[]) o);
				}else{
					s += "," + i + ":" + convertArrayToString((Object[]) o);
				}
			}else{
				if( i == 0 ){
					s += i + ":" + o;
				}else{
					s += "," + i + ":" + o;
				}
			}
			
			i++;
		}
		return s += "]";

	}
	
}
