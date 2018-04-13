package com.junyou.stage.model.stage.quartz;


/**
 * close 
 * @author DaoZheng Yuan
 * 2014-7-22 下午4:09:27
 */
public class CloseSessionHandle {

	/**
	 * 连上时的时间key
	 */
	private static final String L_TIME_KEY = "login_s_key";
	
	public void doIt(){
//		ConcurrentMap<Long, IoSession>  gateSessions = IoSessionManager.getGateSessions();
//		
//		//calc close
//		List<Long> rms = new ArrayList<Long>();
//		for (Long sId : gateSessions.keySet()) {
//			IoSession session = gateSessions.get(sId);
//			
//			Long lT = (Long) session.getAttribute(L_TIME_KEY);
//			Long cvalue = 1 * 60 * 60 * 1000L;
//			
//			if(GameSystemTime.getSystemMillTime() - lT >= cvalue){
//				session.close(true);
//				rms.add(session.getId());
//			}
//		}
//		
//		//remove
//		for (Long sid : rms) {
//			gateSessions.remove(sid);
//		}
	}
}
