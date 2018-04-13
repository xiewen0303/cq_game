package com.junyou.bus.rolestage.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.junyou.constants.GameConstants;
import com.junyou.stage.model.skill.PublicCdManager;
import com.kernel.data.filedb.Filedb;

/**
 * @author LiuYu
 * 2015-6-4 下午8:34:41
 */
@Repository
public class PublicCdManagerDao {
	public void writeFile(PublicCdManager publicCdManager,String userRoleId) {
		ObjectOutputStream out = null;
		try {
			String jsonStr = JSON.toJSONString(publicCdManager);
			if(jsonStr == null){
				return;
			}
			
			File file = Filedb.getFile(GameConstants.PUBLIC_CD_MANAGER_FILE_NAME, userRoleId);
			if(null == file){
				file = Filedb.mkFile(GameConstants.PUBLIC_CD_MANAGER_FILE_NAME, userRoleId);
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

	public PublicCdManager loadFile(String userRoleId) {
		PublicCdManager publicCdManager = null;
		File file = Filedb.getFile(GameConstants.PUBLIC_CD_MANAGER_FILE_NAME, userRoleId);
		if(null != file){
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				publicCdManager = JSON.parseObject(in.readObject().toString(), PublicCdManager.class);
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
		return publicCdManager;
	}
}
