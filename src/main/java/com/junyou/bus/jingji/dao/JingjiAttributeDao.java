package com.junyou.bus.jingji.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.junyou.constants.GameConstants;
import com.junyou.state.jingji.entity.JingjiAttribute;
import com.junyou.state.jingji.entity.JingjiAttribute2;
import com.kernel.data.filedb.Filedb;

/**
 * 角色竞技属性
 * @author LiuYu
 * @date 2015-3-20 下午2:55:31
 */
@Repository
public class JingjiAttributeDao{

	public void writeFile(JingjiAttribute jingjiAttribute,String userRoleId) {
		ObjectOutputStream out = null;
		try {
			String jsonStr = JSON.toJSONString(jingjiAttribute);
			if(jsonStr == null){
				return;
			}
			
			File file = Filedb.getFile(GameConstants.JINGJI_ATTRIBUTE_FILE_NAME, userRoleId);
			if(null == file){
				file = Filedb.mkFile(GameConstants.JINGJI_ATTRIBUTE_FILE_NAME, userRoleId);
			}
			out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(jsonStr);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			if(null != out){
				try {
					out.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	public void writeFile(JingjiAttribute2 jingjiAttribute,String userRoleId) {
		FileOutputStream fos = null;
		try {
			String jsonStr = JSON.toJSONString(jingjiAttribute);
			if(jsonStr == null){
				return;
			}
			
			File file = Filedb.getFile(GameConstants.JINGJI_ATTRIBUTE_FILE_NAME_TWO, userRoleId);
			if(null == file){
				file = Filedb.mkFile(GameConstants.JINGJI_ATTRIBUTE_FILE_NAME_TWO, userRoleId);
			}
			fos = new FileOutputStream(file);
			fos.write(jsonStr.getBytes("iso-8859-1"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			if(null != fos){
				try {
					fos.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public JingjiAttribute loadFile(String userRoleId) {
		JingjiAttribute attribute = null;
		File file = Filedb.getFile(GameConstants.JINGJI_ATTRIBUTE_FILE_NAME, userRoleId);
		if(null != file){
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				attribute = JSON.parseObject(in.readObject().toString(), JingjiAttribute.class);
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
		return attribute;
	}
	public JingjiAttribute2 loadFile2(String userRoleId) {
		JingjiAttribute2 attribute = null;
		File file = Filedb.getFile(GameConstants.JINGJI_ATTRIBUTE_FILE_NAME_TWO, userRoleId);
		if(null != file){
			FileInputStream fis = null;
			try {
//				in = new ObjectInputStream(new FileInputStream(file));
//				String info = in.readObject().toString();
				
				fis = new FileInputStream(file);
				byte[] array = new byte[fis.available()];
				fis.read(array);
				String info = new String(array,"iso-8859-1");
				info = info.substring(info.indexOf("{"));
				attribute = JSON.parseObject(info, JingjiAttribute2.class);
			} catch (Exception e) {
				if(null != fis){
					try {
						fis.close();
					} catch (IOException e1) {
					}
				}
				Filedb.removeFile(GameConstants.JINGJI_ATTRIBUTE_FILE_NAME_TWO, userRoleId);
				return null;
			}finally{
				if(null != fis){
					try {
						fis.close();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return attribute;
	}
}
