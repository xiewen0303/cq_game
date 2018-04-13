package com.junyou.event;

import com.alibaba.fastjson.JSONArray;
import com.junyou.log.LogPrintHandle;

/**
 * 排名活动日志
 * 
 * @author wind
 * 
 */
public class PaiMingLogEvent extends AbsGameLogEvent {
 

	private static final long serialVersionUID = 1L;
	
	private Integer pmType; //排名类型
	private JSONArray paiming; //排名
	
	
	
	public PaiMingLogEvent(Integer pmType,JSONArray paiming) {
		super(LogPrintHandle.YUNYIN_ACTITY_PAIMING);
		
		this.pmType = pmType;
		this.paiming = paiming;
	}



	public Integer getPmType() {
		return pmType;
	}



	public void setPmType(Integer pmType) {
		this.pmType = pmType;
	}



	public JSONArray getPaiming() {
		return paiming;
	}



	public void setPaiming(JSONArray paiming) {
		this.paiming = paiming;
	}
	
	
}