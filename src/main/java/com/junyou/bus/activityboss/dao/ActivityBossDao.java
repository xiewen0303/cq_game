package com.junyou.bus.activityboss.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.junyou.bus.activityboss.manage.ActivityBossInfo;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.kernel.data.filedb.Filedb;


@Component
public class ActivityBossDao {

	//获得服务器Id
	private String getServerId(){
		return ChuanQiConfigUtil.getServerId();
	}
	
	/**
	 * 将数据持久化到db中
	 * @param activityBossInfos
	 */
	public void modifyDb(Map<String, ActivityBossInfo> activityBossInfos) {
		
		String serverId = getServerId(); 
		synchronized (GameConstants.ACTIVITY_BOSS_LOCK) { 
			ObjectOutputStream out = null; 
			
			try {
				
				File file = Filedb.getFile(GameConstants.DSBOSS_COMPONENET_NAME,serverId);
				
				if(null == file){
					file = Filedb.mkFile(GameConstants.DSBOSS_COMPONENET_NAME, serverId);
				}
				out = new ObjectOutputStream(new FileOutputStream(file));
				
				out.writeObject(activityBossInfos);
				out.flush();
				}catch(Exception e){
					ChuanQiLog.error(e.getMessage());
				}finally{ 
						try {
							out.close();
						} catch (Exception e) { 
						}
				} 
		}
		
	}

	public void deleteAllDb() {
		Filedb.removeFile(GameConstants.DSBOSS_COMPONENET_NAME, getServerId());
	}

	@SuppressWarnings("unchecked")
	public Map<String,ActivityBossInfo> getAllBossInfoDb() {
		String serverId = getServerId();
		
		File file = Filedb.getFile(GameConstants.DSBOSS_COMPONENET_NAME, serverId);
		if(null != file){
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				Map<String,ActivityBossInfo> inData = (Map<String,ActivityBossInfo>)in.readObject();
				return inData;
			} catch (Exception e) {
				try{
					file.delete();
				}catch (Exception ex) {
				}
			}finally{
				if(null != in){
					try {
						in.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return null;
	}
	
}