package com.junyou.bus.huajuan.configure;

import java.util.Map;

public class HuaJuanBiaoConfig {
	private int id;
	private String name;
	private int type;
	// 品质
	private int rarelevel;
	// 分解经验
	private int huajuanexp;
	// 专属
	// 收集即激活
	private int exclusive;
	private Map<String, Long> attrs;
	// 所属列表id
	private int liebiaoid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRarelevel() {
		return rarelevel;
	}

	public void setRarelevel(int rarelevel) {
		this.rarelevel = rarelevel;
	}

	public int getHuajuanexp() {
		return huajuanexp;
	}

	public void setHuajuanexp(int huajuanexp) {
		this.huajuanexp = huajuanexp;
	}

	public int getExclusive() {
		return exclusive;
	}

	public void setExclusive(int exclusive) {
		this.exclusive = exclusive;
	}

	public Map<String, Long> getAttrs() {
		return attrs;
	}

	public void setAttrs(Map<String, Long> attrs) {
		this.attrs = attrs;
	}

	public int getLiebiaoid() {
		return liebiaoid;
	}

	public void setLiebiaoid(int liebiaoid) {
		this.liebiaoid = liebiaoid;
	}

}
