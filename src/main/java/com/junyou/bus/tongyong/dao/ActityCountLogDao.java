package com.junyou.bus.tongyong.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.bus.tongyong.entity.ActityCountLog;
import com.junyou.constants.GameConstants;
import com.junyou.utils.ChuanQiConfigUtil;
import com.junyou.utils.datetime.GameSystemTime;
import com.kernel.data.filedb.Filedb;
import com.kernel.sync.annotation.Sync;

/**
 * 保存活动次数
 * @author Administrator
 *
 */
@Component
public class ActityCountLogDao {

	//获得服务器Id
	private String getServerId(){
		return ChuanQiConfigUtil.getServerId();
	}
	
	@SuppressWarnings("unchecked")
	@Sync(component = GameConstants.COMPONENT_ACTITY_SHARE,indexes = {0})
	public void insertDb(ActityCountLog xunbaoLog,int subId) {
		String serverId = getServerId();
		
		synchronized (GameConstants.ACTIVITY_LOG_LOCK) {
			
			try {
				
				File file = Filedb.getFile(GameConstants.ACTIVITY_COMPONENET_NAME,subId+"_"+serverId);
				if(null == file){
					
					ObjectOutputStream out = null;
					try{
						file = Filedb.mkFile(GameConstants.ACTIVITY_COMPONENET_NAME, subId+"_"+serverId);
						out = new ObjectOutputStream(new FileOutputStream(file));
						out.writeObject(new ArrayList<String>());
					}catch(Exception e){
						throw new RuntimeException(e);
					}finally{
						if(null != out){
							out.close();
						}
					}
				}
				ObjectOutputStream out = null;
				try{
					
					ObjectInputStream in = null;
					List<ActityCountLog> inData = null;
					try{
						in = new ObjectInputStream(new FileInputStream(file));
						inData = (List<ActityCountLog>)in.readObject();
						inData.add(inData.size(), xunbaoLog);
					}catch(Exception e){
						throw new RuntimeException(e);
					}finally{
						if(null != in){
							in.close();
						}
					}
					out = new ObjectOutputStream(new FileOutputStream(file));
					out.writeObject(inData);
					
				}catch(Exception e){
					throw new RuntimeException(e);
				}finally{
					if(null != out){
						out.close();
					}
				}
				
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	/**
	 * 增加次数
	 * @param subId
	 * @param userRoleId
	 */
	@Sync(component = GameConstants.COMPONENT_ACTITY_SHARE,indexes = {0})
	public void addActivityCount(int subId,long userRoleId,int count){
		String serverId = getServerId();
		
		//synchronized (GameConstants.ACTIVITY_LOG_LOCK_ADD) {
			File file = Filedb.getFile(GameConstants.ACTIVITY_COMPONENET_NAME, subId+"_"+serverId);
			if(null != file){
				ObjectInputStream in = null;
				try {
					in = new ObjectInputStream(new FileInputStream(file));
					List<ActityCountLog> inData = (List<ActityCountLog>)in.readObject();
					for (int i = 0; i < inData.size(); i++) {
						ActityCountLog log = inData.get(i);
						if(log.getUserRoleId() == userRoleId){
							inData.remove(i);
							log.setCount(log.getCount() + count);
							log.setUpdateTime(GameSystemTime.getSystemMillTime());
							inData.add(inData.size(), log);
							ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
							out.writeObject(inData);
							break;
						}
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
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
		//}
	}
	/**
	 * 清空次数
	 * @param subId
	 * @param userRoleId
	 */
	@Sync(component = GameConstants.COMPONENT_ACTITY_SHARE,indexes = {0})
	public void cleanActivityCount(int subId,long userRoleId){
		String serverId = getServerId();
		//synchronized (GameConstants.ACTIVITY_LOG_LOCK_CLEAN) {
		
			File file = Filedb.getFile(GameConstants.ACTIVITY_COMPONENET_NAME, subId+"_"+serverId);
			if(null != file){
				ObjectInputStream in = null;
				try {
					in = new ObjectInputStream(new FileInputStream(file));
					List<ActityCountLog> inData = (List<ActityCountLog>)in.readObject();
					for (int i = 0; i < inData.size(); i++) {
						ActityCountLog log = inData.get(i);
						if(log.getUserRoleId() == userRoleId){
							inData.remove(i);
							ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
							out.writeObject(inData);
						}
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
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
		
		//}
	}
	/**
	 * 获取子活动ID参与次数
	 * @param subId
	 * @param userRoleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActityCountLog getActivityCountBySubIdAndUser(int subId,long userRoleId){
		String serverId = getServerId();
		File file = Filedb.getFile(GameConstants.ACTIVITY_COMPONENET_NAME, subId+"_"+serverId);
		if(null != file){
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				List<ActityCountLog> inData = (List<ActityCountLog>)in.readObject();
				for (int i = 0; i < inData.size(); i++) {
					ActityCountLog log = inData.get(i);
					if(log.getUserRoleId() == userRoleId){
						return log;
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
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