package com.junyou.gameconfig.publicconfig.configure.export;


/**
 * PK相关配置
 *@author  LiNing
 *@created 2013-10-24上午6:22:26
 */
public class PKPublicConfig extends AdapterPublicConfig{
	
	private Integer needRed;//变成红名需要的PK点数
	
	private Integer needYellow;//变成黄名需要的PK点数
	
	private int pkChiXu;//灰名持续时间（单位：秒）
	
	private int pkCleanTime;//消除1点PK值需要的时间（单位：分钟)
	
	private Integer pkVal;//杀人增加的pk值
	
	private int pkMaxVal;//PK值的最大值
	
	/**
	 * 获取PK值的最大值
	 * @return
	 */
	public int getPkMaxVal() {
		return pkMaxVal;
	}

	/**
	 * 设置PK值的最大值
	 * @param pkMaxVal
	 */
	public void setPkMaxVal(int pkMaxVal) {
		this.pkMaxVal = pkMaxVal;
	}

	/**
	 * 获取杀人增加的pk值
	 * @return
	 */
	public Integer getPkVal() {
		return pkVal;
	}

	/**
	 * 设置杀人增加的pk值
	 * @param pkVal
	 */
	public void setPkVal(Integer pkVal) {
		this.pkVal = pkVal;
	}

	/**
	 * 获取消除1点PK值需要的时间（单位：分钟)
	 * @return
	 */
	public int getPkCleanTime() {
		return pkCleanTime;
	}

	/**
	 * 设置消除1点PK值需要的时间（单位：分钟)
	 * @param pkCleanTime
	 */
	public void setPkCleanTime(int pkXiaoChu) {
		this.pkCleanTime = pkXiaoChu;
	}

	/**
	 * 获取灰名持续时间（单位：秒）
	 * @return
	 */
	public int getPkChiXu() {
		return pkChiXu;
	}

	/**
	 * 设置灰名持续时间（单位：秒）
	 * @param pkChiXu
	 */
	public void setPkChiXu(int pkChiXu) {
		this.pkChiXu = pkChiXu;
	}

	/**
	 * 获取变成黄名需要的PK点数
	 * @return
	 */
	public Integer getNeedYellow() {
		return needYellow;
	}

	/**
	 * 设置变成黄名需要的PK点数
	 * @param needYellow
	 */
	public void setNeedYellow(Integer needYellow) {
		this.needYellow = needYellow;
	}

	/**
	 * 获取变成红名需要的PK点数
	 * @return
	 */
	public Integer getNeedRed() {
		return needRed;
	}

	/**
	 * 设置变成红名需要的PK点数
	 * @param needred
	 */
	public void setNeedRed(Integer needRed) {
		this.needRed = needRed;
	}

}
