package com.junyou.bus.daomoshouzha.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.bus.daomoshouzha.entity.DaoMoShouZhaLog;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.kernel.data.filedb.Filedb;


@Component
public class DaoMoShouZhaLogDao {

	//获得服务器Id
	private String getServerId(){
		return ChuanQiConfigUtil.getServerId();
	}
	
	@SuppressWarnings("unchecked")
	public void insertDb(DaoMoShouZhaLog xunbaoLog) {
		
		String serverId = getServerId();
		
		synchronized (GameConstants.DM_LOG_LOCK) {
			
			try {
				
				File file = Filedb.getFile(GameConstants.DM_COMPONENET_NAME+xunbaoLog.getUserRoleId(),serverId);
				if(null == file){
					
					ObjectOutputStream out = null;
					try{
						file = Filedb.mkFile(GameConstants.DM_COMPONENET_NAME+xunbaoLog.getUserRoleId(), serverId);
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
					List<DaoMoShouZhaLog> inData = null;
					try{
						in = new ObjectInputStream(new FileInputStream(file));
						inData = (List<DaoMoShouZhaLog>)in.readObject();
						inData.add(inData.size(), xunbaoLog);
					}catch(Exception e){
						ChuanQiLog.error("",e);
					}finally{
						if(null != in){
							in.close();
						}
					}
					
					//数据上限验证
					if(inData.size() > GameConstants.DM_INFO_MAX_COUNT){
						inData.remove(0);
					}
					out = new ObjectOutputStream(new FileOutputStream(file));
					out.writeObject(inData);
					
				}catch(Exception e){
					ChuanQiLog.error("",e);
				}finally{
					if(null != out){
						out.close();
					}
				}
				
			} catch (Exception e) {
				ChuanQiLog.error("",e);
			}
		}
		
	}


	@SuppressWarnings("unchecked")
	public List<DaoMoShouZhaLog> getXunbaonfoByIdDb(Long userRoleId) {
		String serverId = getServerId();
		
		File file = Filedb.getFile(GameConstants.DM_COMPONENET_NAME+userRoleId, serverId);
		if(null != file){
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				List<DaoMoShouZhaLog> inData = (List<DaoMoShouZhaLog>)in.readObject();
				return inData;
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