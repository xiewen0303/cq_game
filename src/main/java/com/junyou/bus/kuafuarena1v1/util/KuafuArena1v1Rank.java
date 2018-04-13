package com.junyou.bus.kuafuarena1v1.util;

public class KuafuArena1v1Rank {
	public static final String FIELD_USER_ROLE_ID = "1";
	private Long userRoleId;
	public static final String FIELD_CONFIG_ID = "2";
	private Integer configId;
	public static final String FIELD_NAME = "3";
	private String name;
	public static final String FIELD_GUILD_NAME = "4";
	private String guildName;
	public static final String FIELD_LEVEL = "5";
	private Integer level;
	public static final String FIELD_ZPLUS = "6";
	private Long zplus;
	public static final String FIELD_ZUOQI_ID = "7";
	private Integer zuoqiId;
	public static final String FIELD_CHIBANG_ID = "8";
	private Integer chibangId;
	private int duan;
	private int rank;

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getZplus() {
		return zplus;
	}

	public void setZplus(Long zplus) {
		this.zplus = zplus;
	}

	public Integer getZuoqiId() {
		return zuoqiId;
	}

	public void setZuoqiId(Integer zuoqiId) {
		this.zuoqiId = zuoqiId;
	}

	public Integer getChibangId() {
		return chibangId;
	}

	public void setChibangId(Integer chibangId) {
		this.chibangId = chibangId;
	}

	public int getDuan() {
		return duan;
	}

	public void setDuan(int duan) {
		this.duan = duan;
	}

}
