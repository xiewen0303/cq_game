package com.junyou.bus.yaoshen.configure.export;

import java.util.Map;

import com.kernel.data.dao.AbsVersion;

public class YaoshenMoWenConfig extends AbsVersion {
	private int id;
	private int id1;
	private int id2;
	private String name;
	private int money;
	private int count;
	private String mallid;
	private int ggopen;
	private int qndopen;
	private String qndid;
	private int qndmax;
	private int czdopen;
	private String czdid;
	private int czdmax;
	private Map<String, Long> attrs;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId1() {
		return id1;
	}

	public void setId1(int id1) {
		this.id1 = id1;
	}

	public int getId2() {
		return id2;
	}

	public void setId2(int id2) {
		this.id2 = id2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getMallid() {
		return mallid;
	}

	public void setMallid(String mallid) {
		this.mallid = mallid;
	}

	public int getGgopen() {
		return ggopen;
	}

	public void setGgopen(int ggopen) {
		this.ggopen = ggopen;
	}

	public String getQndid() {
		return qndid;
	}

	public void setQndid(String qndid) {
		this.qndid = qndid;
	}

	public int getQndmax() {
		return qndmax;
	}

	public void setQndmax(int qndmax) {
		this.qndmax = qndmax;
	}

	public String getCzdid() {
		return czdid;
	}

	public void setCzdid(String czdid) {
		this.czdid = czdid;
	}

	public int getCzdmax() {
		return czdmax;
	}

	public void setCzdmax(int czdmax) {
		this.czdmax = czdmax;
	}

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}

	public int getQndopen() {
		return qndopen;
	}

	public void setQndopen(int qndopen) {
		this.qndopen = qndopen;
	}

	public int getCzdopen() {
		return czdopen;
	}

	public void setCzdopen(int czdopen) {
		this.czdopen = czdopen;
	}

}
