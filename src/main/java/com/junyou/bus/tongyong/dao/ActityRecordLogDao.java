package com.junyou.bus.tongyong.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.bus.tongyong.entity.ActityCountLog;
import com.junyou.constants.GameConstants;
import com.junyou.utils.ChuanQiConfigUtil;
import com.kernel.data.filedb.Filedb;
import com.kernel.sync.annotation.Sync;

/**
 * 保存活动记录
 */
@Component
public class ActityRecordLogDao {

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
	
	
	private void createFile(int subId){
			String serverId = getServerId();
			ObjectOutputStream out = null;
			try{
				File file = Filedb.mkFile(GameConstants.ACTIVITY_COMPONENET_NAME, subId+"_"+serverId);
				out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(new ArrayList<Object>());
			}catch(Exception e){
				throw new RuntimeException(e);
			}finally{
				if(null != out){
					try {
						out.flush();
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}
	
	/**
	 * 增加次数
	 * @param subId
	 * @param userRoleId
	 */
	@Sync(component = GameConstants.COMPONENT_ACTITY_SHARE,indexes = {0})
	public void addActivityRecord(int subId,Object record,int len){
		String serverId = getServerId();
		
//		synchronized (GameConstants.ACTIVITY_LOG_LOCK_RECORD) {
			File file = Filedb.getFile(GameConstants.ACTIVITY_COMPONENET_NAME, subId+"_"+serverId);
			if(file == null){
				createFile(subId);
				file = Filedb.getFile(GameConstants.ACTIVITY_COMPONENET_NAME, subId+"_"+serverId);
			}
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				ArrayList<Object> inData = (ArrayList<Object>)in.readObject();
				if(inData.size() >= len){
					inData.remove(0);
				}
				inData.add(record);
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(inData);
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
//		}
	}
	
	/**
	 * 清空次数
	 * @param subId
	 * @param userRoleId
	 */
	@Sync(component = GameConstants.COMPONENT_ACTITY_SHARE,indexes = {0})
	public void cleanActivityRecord(int subId,long userRoleId){
		String serverId = getServerId();
			File file = Filedb.getFile(GameConstants.ACTIVITY_COMPONENET_NAME, subId+"_"+serverId);
			if(file == null){
				createFile(subId);
			}
			ObjectInputStream in = null;
			ObjectOutputStream out = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(new ArrayList<>());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}finally{
				if(null != in){
					try {
						out.flush();
						out.close();
						in.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
	}
	
	/**
	 * 获取子活动ID参与次数
	 * @param subId
	 * @param userRoleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Sync(component = GameConstants.COMPONENT_ACTITY_SHARE,indexes = {0})
	public List<Object> getActivityCountBySubIdRecord(int subId){
		List<Object> inData = new ArrayList<>();
		String serverId = getServerId();
		File file = Filedb.getFile(GameConstants.ACTIVITY_COMPONENET_NAME, subId+"_"+serverId);
		if(null != file){
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				inData = (List<Object>)in.readObject();
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
		
		//反序
		Collections.reverse(inData);
		return inData;
	}
	
}