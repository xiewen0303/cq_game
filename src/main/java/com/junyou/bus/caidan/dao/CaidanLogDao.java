package com.junyou.bus.caidan.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.junyou.bus.caidan.entity.CaidanLog;
import com.junyou.constants.GameConstants;
import com.junyou.utils.common.ObjectUtil;
import com.kernel.data.filedb.Filedb;

/**
 * @author LiuYu
 * 2015-9-16 下午3:08:57
 */
@Component
public class CaidanLogDao {

	private Map<Integer,List<CaidanLog>> allLogs = new HashMap<>();
	private Map<Integer,Object[]> allLogsOut = new HashMap<>();
	
	
	public void insert(CaidanLog caidanLog,Integer subId) {
		List<CaidanLog> logs = allLogs.get(subId);
		if(logs == null){
			logs = initLogs(subId);
		}
		synchronized (this) {
			allLogsOut.remove(subId);
			if(logs.size() > GameConstants.CAIDAN_LOG_MAX_COUNT){
				logs.remove(0);
			}
			logs.add(caidanLog);
			Filedb.writeFile(GameConstants.COMPONENET_CAIDAN_NAME,subId.toString(), JSON.toJSONString(logs));
		}
	}

	private List<CaidanLog> getCaidanLogs(Integer subId) {
		List<CaidanLog> logs = allLogs.get(subId);
		if(logs == null){
			logs = initLogs(subId);
		}
		return new ArrayList<>(logs);
	}
	
	private List<CaidanLog> initLogs(Integer subId){
		List<CaidanLog> logs = null;
		synchronized (this) {
			String info = Filedb.readFile(GameConstants.COMPONENET_CAIDAN_NAME, subId.toString());
			if(!ObjectUtil.strIsEmpty(info)){
				logs = new ArrayList<>();
				try{
					for (Object json : (JSONArray)JSON.parse(info)) {
						logs.add(JSON.parseObject(json.toString(), CaidanLog.class));
					}
				}catch (Exception e) {
				}
			}else{
				logs = new ArrayList<>();
			}
			allLogs.put(subId, logs);
		}
		return logs;
	}
	
	public Object[] getLogsOut(Integer subId){
		Object[] logsOut = allLogsOut.get(subId);
		if(logsOut == null){
			List<Object[]> list = new ArrayList<Object[]>();
			List<CaidanLog> logs = getCaidanLogs(subId);
			for (CaidanLog caidanLog : logs) {
				list.add(caidanLog.outMsg());
			}
			logsOut = new Object[]{subId,list.toArray()};
			allLogsOut.put(subId, logsOut);
		}
		return logsOut;
	}
	
}
