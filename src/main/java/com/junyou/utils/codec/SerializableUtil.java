package com.junyou.utils.codec;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import com.junyou.log.ChuanQiLog;

import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;

public class SerializableUtil {
	
	
	/**
	 * java自带序列化
	 * 
	 * @param o
	 * @return
	 */
	public static byte[] javaSerialize(Object o) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(bout));
			out.writeObject(o);
			out.flush();
			return bout.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException("can not covert from object to bytes", e);
		} finally {
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(bout);
		}

	}
	
	/**
	 * 使用java自带的反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object javaDeserialize(byte[] bytes) {
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new BufferedInputStream(bin));
			Object o = in.readObject();
			return o;
		} catch (Exception e) {
			ChuanQiLog.error("can not covert from bytes to object", e);
			return null;
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(bin);
		}
	}
	
	
	public static byte[] fstSerialize(Object obj) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FSTObjectOutput fout = new FSTObjectOutput(out);
		try {
			fout.writeObject(obj);
			fout.flush();
		} catch (IOException e) {
			ChuanQiLog.error("fstSerialize", e);
		}finally{
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(fout);
		}
		return out.toByteArray();
	}

	public static Object fstDeserialize(byte[] bytes) {
		FSTObjectInput in = null;
		try {
			in = new FSTObjectInput(new ByteArrayInputStream(bytes));
			return in.readObject();
		} catch (Exception e) {
			ChuanQiLog.error("fstDeserialize", e);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return null;
	}
	

	public static boolean object2SwapFile(Object o,String fileName,String basePath){
		
		try {
			File swapFile = new File(basePath+fileName);
			
			boolean isCreate = false;
			if(!swapFile.exists()){
				isCreate = swapFile.createNewFile();
			}
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(swapFile));
			out.writeObject(o);
			out.close();
			
			return isCreate;
		} catch (Exception e) {
			throw new RuntimeException("can not covert from object to swap file",e);
		}
		
	}

	public static Object swapFile2Object(String fileName,String basePath){
		
		try {
			File swapFile = new File(basePath+fileName);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(swapFile));
			Object o = in.readObject();
			in.close();
			return o;
		} catch (Exception e) {
			throw new RuntimeException("can not covert from  swap file to object",e);
		}
		
	}
	
	
}
