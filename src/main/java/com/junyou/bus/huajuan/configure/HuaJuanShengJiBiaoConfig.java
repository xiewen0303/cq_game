package com.junyou.bus.huajuan.configure;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 画卷升级表 
 *
 * @author ZHONGDIAN
 * @date 2016-03-08 14:55:20
 */
public class HuaJuanShengJiBiaoConfig extends AbsVersion {

	private Integer id;

	private Map<String, Long> attrs;

	private Integer needexp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}


	public Integer getNeedexp() {
		return needexp;
	}

	public void setNeedexp(Integer needexp) {
		this.needexp = needexp;
	}

	


}
