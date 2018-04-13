package com.junyou.gameconfig.publicconfig.configure.export;

import com.kernel.data.dao.AbsVersion;

/**
 * 共公配置实体基类
 * @author DaoZheng Yuan
 * 2013-11-8 上午11:11:23
 */
public class AdapterPublicConfig  extends AbsVersion{

	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
