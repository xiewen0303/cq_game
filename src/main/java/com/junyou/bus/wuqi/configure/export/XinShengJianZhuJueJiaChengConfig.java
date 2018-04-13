package com.junyou.bus.wuqi.configure.export;

 
import java.util.HashMap;
import java.util.Map;

import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 新圣剑主角加层表
 *
 * @author wind
 * @date 2015-04-01 17:50:13
 */
public class XinShengJianZhuJueJiaChengConfig extends AbsVersion {

	private Integer id;
	
	private Integer level;
 
	private Map<String,Long> datas = new HashMap<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Map<String, Long> getDatas() {
		return datas;
	}

	public void setDatas(Map<String, Long> datas) {
		this.datas = datas;
	} 
}
