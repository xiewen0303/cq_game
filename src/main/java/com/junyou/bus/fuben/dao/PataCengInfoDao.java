package com.junyou.bus.fuben.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.junyou.bus.fuben.entity.PataCengInfo;
import com.junyou.constants.GameConstants;
import com.kernel.data.filedb.Filedb;

/**
 * @author LiuYu
 * 2015-6-11 上午9:39:48
 */
@Repository
public class PataCengInfoDao {
	public void writeFile(Map<Integer,PataCengInfo> map) {
		ObjectOutputStream out = null;
		try {
			if(map == null || map.size() < 1){
				return;
			}
			String jsonStr = JSON.toJSONString(map);
			if(jsonStr == null){
				return;
			}
			
			File file = Filedb.getFile(GameConstants.PATA_INFO_FILE_NAME, GameConstants.PATA_INFO_FILE_NAME);
			if(null == file){
				file = Filedb.mkFile(GameConstants.PATA_INFO_FILE_NAME, GameConstants.PATA_INFO_FILE_NAME);
			}
			out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(jsonStr);
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
	

	public Map<Integer,JSONObject> loadFile() {
		Map<Integer,JSONObject> map = null;
		File file = Filedb.getFile(GameConstants.PATA_INFO_FILE_NAME, GameConstants.PATA_INFO_FILE_NAME);
		if(null != file){
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				map = JSON.parseObject(in.readObject().toString(), Map.class);
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
		return map;
	}
	
}
