package com.junyou.bus.jingji.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.junyou.constants.GameConstants;
import com.kernel.data.filedb.Filedb;

/**
 * 角色竞技属性
 * @author LiuYu
 * @date 2015-3-20 下午2:55:31
 */
@Repository
public class JingjiReportDao{

	public void writeFile(Object[] report,String userRoleId) {
		ObjectOutputStream out = null;
		try {
			JSONArray json = loadFile(userRoleId);
			if(json == null){
				json = new JSONArray();
			}else{
				while(json.size() > GameConstants.JINGJI_REPORT_MAX){
					json.remove(0);
				}
			}
			json.add(report);
			
			File file = Filedb.getFile(GameConstants.JINGJI_REPORT_FILE_NAME, userRoleId);
			if(null == file){
				file = Filedb.mkFile(GameConstants.JINGJI_REPORT_FILE_NAME, userRoleId);
			}
			out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(json.toJSONString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public JSONArray loadFile(String userRoleId) {
		File file = Filedb.getFile(GameConstants.JINGJI_REPORT_FILE_NAME, userRoleId);
		JSONArray json = null;
		if(null != file){
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				json = JSON.parseArray(in.readObject().toString());
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
		return json;
	}
	
}
