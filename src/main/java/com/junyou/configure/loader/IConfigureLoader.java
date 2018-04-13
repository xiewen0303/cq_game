/**
 * 
 */
package com.junyou.configure.loader;


/**
 * @description
 * @author ShiJie Chi
 * @created 2011-12-8下午5:38:23
 */
public interface IConfigureLoader {

	/**
	 * 加载文件
	 * @param configureFileName 文件名
	 */
	byte[] load(String configureFileName);
	
	/**
	 * 读取标志
	 * @param filename 文件名
	 * @param dirType 路径类型
	 * @return 
	 */
	Object[] loadSign(String filename,DirType dirType);

}
