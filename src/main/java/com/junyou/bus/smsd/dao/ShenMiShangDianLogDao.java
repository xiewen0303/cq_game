package com.junyou.bus.smsd.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.bus.smsd.entity.ShenMiShangDianLog;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.utils.ChuanQiConfigUtil;
import com.kernel.data.filedb.Filedb;


@Component
public class ShenMiShangDianLogDao {

	//获得服务器Id
	private String getServerId(){
		return ChuanQiConfigUtil.getServerId();
	}
	
	@SuppressWarnings("unchecked")
	public void insertDb(ShenMiShangDianLog xunbaoLog) {
		
		String serverId = getServerId();
		
		synchronized (GameConstants.SMSD_LOG_LOCK) {
			
			try {
				
				File file = Filedb.getFile(GameConstants.SMSD_COMPONENET_NAME,serverId);
				if(null == file){
					
					ObjectOutputStream out = null;
					try{
						file = Filedb.mkFile(GameConstants.SMSD_COMPONENET_NAME, serverId);
						out = new ObjectOutputStream(new FileOutputStream(file));
						out.writeObject(new ArrayList<String>());
					}catch(Exception e){
						ChuanQiLog.error("",e);
					}finally{
						if(null != out){
							out.close();
						}
					}
				}
				
				ObjectOutputStream out = null;
				try{
					
					ObjectInputStream in = null;
					List<ShenMiShangDianLog> inData = null;
					try{
						in = new ObjectInputStream(new FileInputStream(file));
						inData = (List<ShenMiShangDianLog>)in.readObject();
						inData.add(inData.size(), xunbaoLog);
					}catch(Exception e){
						throw new RuntimeException(e);
					}finally{
						if(null != in){
							in.close();
						}
					}
					
					//数据上限验证
					if(inData.size() > GameConstants.SMSD_INFO_MAX_COUNT){
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


	public void deleteAllDb(String guildId) {
		Filedb.removeFile(GameConstants.SMSD_COMPONENET_NAME, getServerId());
	}

	@SuppressWarnings("unchecked")
	public List<ShenMiShangDianLog> getXunbaonfoByIdDb() {
		String serverId = getServerId();
		
		File file = Filedb.getFile(GameConstants.SMSD_COMPONENET_NAME, serverId);
		if(null != file){
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				List<ShenMiShangDianLog> inData = (List<ShenMiShangDianLog>)in.readObject();
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