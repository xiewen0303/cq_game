package com.junyou.public_.guild.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.junyou.constants.GameConstants;
import com.junyou.log.ChuanQiLog;
import com.junyou.public_.guild.entity.Guild;
import com.kernel.data.filedb.Filedb;

/**   
 * 公会日志
 * @author LiuYu  
 * @version  2014-8-23
 * 
 */
@Repository
public class GuildLogDao{

	public void writeLogFile(Guild guild) {
		ObjectOutputStream out = null;
		try {
			JSONArray fileSource = guild.getGuildLogManager().getFileSource();
			if(fileSource == null){
				return;
			}
			
			File file = Filedb.getFile(GameConstants.LOG_FILE_COMPONENET_NAME, guild.getId()+"");
			if(null == file){
				file = Filedb.mkFile(GameConstants.LOG_FILE_COMPONENET_NAME, guild.getId()+"");
			}
			out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(fileSource.toJSONString());
		} catch (Exception e) {
			ChuanQiLog.error("",e);
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					ChuanQiLog.error("",e);
				}
			}
		}
	}

	public void initLogs(Guild guild) {
		File file = Filedb.getFile(GameConstants.LOG_FILE_COMPONENET_NAME, guild.getId()+"");
		if(null != file){
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				
				JSONArray array = JSONArray.parseArray(in.readObject().toString());
				guild.initLog(array);
			} catch (Exception e) {
				ChuanQiLog.error("",e);
				guild.initLog(null);
				file.delete();
			}finally{
				if(null != in){
					try {
						in.close();
					} catch (IOException e) {
						ChuanQiLog.error("",e);
					}
				}
			}
			
		}else{
			guild.initLog(null);
		}
	}
	
	
}
