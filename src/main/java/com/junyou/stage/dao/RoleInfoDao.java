package com.junyou.stage.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Repository;

import com.junyou.constants.GameConstants;
import com.kernel.data.filedb.Filedb;

@Repository
public class RoleInfoDao{

	public Object loadRoleInfoFromFileDb(String userRoleId) {
		Object roleInfo = null;
		try{
			File file = Filedb.getFile(GameConstants.COMPONENET_ROLE_INFO,userRoleId);
			if(null != file){
				ObjectInputStream in = null;
				try{
					in = new ObjectInputStream(new FileInputStream(file));
					roleInfo = in.readObject();
				}catch(Exception e){
					throw new RuntimeException(e);
				}finally{
					if(null != in){
						in.close();
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return roleInfo;
	}

	public void writeRoleInfoFromFileDb(Object roleInfo,String userRoleId) {
		try{
			File file = Filedb.getFile(GameConstants.COMPONENET_ROLE_INFO,userRoleId);
			if(null == file){
				file = Filedb.mkFile(GameConstants.COMPONENET_ROLE_INFO,userRoleId);
			}
			ObjectOutputStream out = null;
			try{
				out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(roleInfo);
			}catch (Exception e) {
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


	public Object loadFightTypeFromFileDb(String userRoleId) {
		Object roleInfo = null;
		try{
			File file = Filedb.getFile(GameConstants.COMPONENET_ROLE_ATTRIBUTE,userRoleId);
			if(null != file){
				ObjectInputStream in = null;
				try{
					in = new ObjectInputStream(new FileInputStream(file));
					roleInfo = in.readObject();
				}catch(Exception e){
					throw new RuntimeException(e);
				}finally{
					if(null != in){
						in.close();
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return roleInfo;
	}

	public void writeFightTypeFromFileDb(Object roleInfo,String userRoleId){
		try{
			File file = Filedb.getFile(GameConstants.COMPONENET_ROLE_ATTRIBUTE,userRoleId);
			if(null == file){
				file = Filedb.mkFile(GameConstants.COMPONENET_ROLE_ATTRIBUTE,userRoleId);
			}
			ObjectOutputStream out = null;
			try{
				out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(roleInfo);
			}catch (Exception e) {
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
