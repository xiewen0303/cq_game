package com.junyou.bus.notice.configure.export;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import com.junyou.bus.notice.NoticeConstants;


public class NoticeCategoryConfig  {
	/**
	 *	全局公告
	 */
	private ConcurrentMap<Integer,NoticeConfig> gNotices;
	/**
	 *	平台公告
	 */
	private ConcurrentMap<Integer,NoticeConfig> pNotices;
	/**
	 *	单服公告
	 */
	private ConcurrentMap<Integer,NoticeConfig> sNotices;
	private ConcurrentMap<Integer,NoticeConfig> lastNotices;
	
	private Object[] msg = new Object[0];
	private boolean update = false;

	public Object[] getMsg() {
		if(update){
			refreshMsg();
		}
		return msg;
	}

	public List<Object> setNotices(ConcurrentMap<Integer, NoticeConfig> notices,int type) {
		if(type == NoticeConstants.GLOBAL_TYPE){
			lastNotices = this.gNotices;
			this.gNotices = notices;
		}else if(type == NoticeConstants.PLATFORM_TYPE){
			lastNotices = this.pNotices;
			this.pNotices = notices;
		}else if(type == NoticeConstants.SERVER_TYPE){
			lastNotices = this.sNotices;
			this.sNotices = notices;
		}
		return checkNotices(type);
	}
	
	private List<Object> checkNotices(int type){
		ConcurrentMap<Integer,NoticeConfig> notices = null;
		if(type == NoticeConstants.GLOBAL_TYPE){
			notices = this.gNotices;
		}else if(type == NoticeConstants.PLATFORM_TYPE){
			notices = this.pNotices;
		}else if(type == NoticeConstants.SERVER_TYPE){
			notices = this.sNotices;
		}else{
			return null;
		}
		
		update = true;
		
		List<Object> change = new ArrayList<Object>();
		for (Integer id : notices.keySet()) {
			NoticeConfig notice = notices.get(id);
			Object[] msg = new Object[]{id,notice.getText(),notice.getStep(),notice.getStartTime(),notice.getEndTime(),notice.getLocation()};
			if(lastNotices != null){//第一次加载公告不比较变化
				NoticeConfig ln = lastNotices.get(id);
				if(ln == null){
					change.add(msg);
				}else{
					if(!notice.equals(ln)){
						change.add(msg);
					}
					lastNotices.remove(id);
				}
			}
		}
		if(lastNotices == null){
			return null;
		}
		for (Integer id : lastNotices.keySet()) {
			change.add(new Object[]{id,"",0});
		}
		return change;
	}
	
	private void refreshMsg(){
		synchronized (msg) {
			if(update){
				update = false;
			}else{
				return;
			}
			List<Object> msgList = new ArrayList<Object>();
			if(gNotices != null && gNotices.size() > 0){
				for (NoticeConfig notice : gNotices.values()) {
					Object[] msg = new Object[]{notice.getId(),notice.getText(),notice.getStep(),notice.getStartTime(),notice.getEndTime(),notice.getLocation()};
					msgList.add(msg);
				}
			}
			if(pNotices != null && pNotices.size() > 0){
				for (NoticeConfig notice : pNotices.values()) {
					Object[] msg = new Object[]{notice.getId(),notice.getText(),notice.getStep(),notice.getStartTime(),notice.getEndTime(),notice.getLocation()};
					msgList.add(msg);
				}
			}
			if(sNotices != null && sNotices.size() > 0){
				for (NoticeConfig notice : sNotices.values()) {
					Object[] msg = new Object[]{notice.getId(),notice.getText(),notice.getStep(),notice.getStartTime(),notice.getEndTime(),notice.getLocation()};
					msgList.add(msg);
				}
			}
			msg = msgList.toArray();
		}
	}
}
