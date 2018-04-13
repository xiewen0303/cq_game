package com.junyou.gameconfig.export;


/**
 * 
 * @description 公共CD 
 *
 * @author LiuJuan
 * @date 2012-3-12 下午4:47:52
 */
public class PublicCdConfig{

	public String cdId;
	public int cdTime;
	
	/**
	 * CD编号
	 * @return
	 */
	public String getCdId() {
		return cdId;
	}

	public void setCdId(String cdId) {
		this.cdId = cdId;
	}
	
	/**
	 * CD时间
	 * @return
	 */
	public int getCdTime() {
		return cdTime;
	}

	public void setCdTime(int cdTime) {
		this.cdTime = cdTime;
	}
}
