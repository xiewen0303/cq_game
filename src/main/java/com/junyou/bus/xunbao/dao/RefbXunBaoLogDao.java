package com.junyou.bus.xunbao.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.junyou.bus.xunbao.entity.XunBaoLog;
import com.junyou.constants.GameConstants;
import com.junyou.utils.ChuanQiConfigUtil;
import com.kernel.data.filedb.Filedb;


@Component
public class RefbXunBaoLogDao {

	//获得服务器Id
	private String getServerId(){
		return ChuanQiConfigUtil.getServerId();
	}
	
	@SuppressWarnings("unchecked")
	public void insertDb(XunBaoLog xunbaoLog,int subId) {
		
		String serverId = getServerId();
		
		synchronized (GameConstants.XB_LOG_LOCK) {
			
			try {
				
				File file = Filedb.getFile(GameConstants.XB_COMPONENET_NAME,subId+"_"+serverId);
				if(null == file){
					
					ObjectOutputStream out = null;
					try{
						file = Filedb.mkFile(GameConstants.XB_COMPONENET_NAME, subId+"_"+serverId);
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
					List<XunBaoLog> inData = null;
					try{
						in = new ObjectInputStream(new FileInputStream(file));
						inData = (List<XunBaoLog>)in.readObject();
						inData.add(inData.size(), xunbaoLog);
					}catch(Exception e){
						throw new RuntimeException(e);
					}finally{
						if(null != in){
							in.close();
						}
					}
					
					//数据上限验证
					if(inData.size() > GameConstants.XB_INFO_MAX_COUNT){
						inData.remove(0);
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


	public void deleteAllDb(int subId) {
		Filedb.removeFile(GameConstants.XB_COMPONENET_NAME,subId+"_"+ getServerId());
	}

	@SuppressWarnings("unchecked")
	public List<XunBaoLog> getXunbaonfoByIdDb(int subId) {
		String serverId = getServerId();
		
		File file = Filedb.getFile(GameConstants.XB_COMPONENET_NAME, subId+"_"+serverId);
		if(null != file){
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				List<XunBaoLog> inData = (List<XunBaoLog>)in.readObject();
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