package com.junyou.public_.shichang.configure.export;
 
import com.kernel.data.dao.AbsVersion;

/**
 * 
 * @description 市场 
 *
 * @author wind
 * @date 2015-04-10 15:27:13
 */
public class ShiChangFenLeiConfig extends AbsVersion  {

	private Integer id;

	private Integer needjob;

	private Integer eqpart;

	private Integer type1;

	private String type2;

	private Integer type;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNeedjob() {
		return needjob;
	}

	public void setNeedjob(Integer needjob) {
		this.needjob = needjob;
	}
	public Integer getEqpart() {
		return eqpart;
	}

	public void setEqpart(Integer eqpart) {
		this.eqpart = eqpart;
	}
	public Integer getType1() {
		return type1;
	}

	public void setType1(Integer type1) {
		this.type1 = type1;
	} 
	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
