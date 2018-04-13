package com.junyou.event;

import com.junyou.log.LogPrintHandle;

public class KuafuArena4v4LogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private String name;
	private int beforeJifen;
	private int afterJifen;
	private String shenbinNames;//神兵
	private String xuemoNames;//血魔

	public KuafuArena4v4LogEvent(long userRoleId, String name, int beforeJifen,
			int afterJifen, String shenbinNames, String xuemoNames) {
		super(LogPrintHandle.KUAFU_ARENA_4V4);
		this.userRoleId = userRoleId;
		this.name = name;
		this.beforeJifen = beforeJifen;
		this.afterJifen = afterJifen;
		this.shenbinNames = shenbinNames;
		this.xuemoNames = xuemoNames;
	}

	public long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getBeforeJifen() {
		return beforeJifen;
	}

	public void setBeforeJifen(int beforeJifen) {
		this.beforeJifen = beforeJifen;
	}

	public int getAfterJifen() {
		return afterJifen;
	}

	public void setAfterJifen(int afterJifen) {
		this.afterJifen = afterJifen;
	}

	public String getShenbinNames() {
		return shenbinNames;
	}

	public void setShenbinNames(String shenbinNames) {
		this.shenbinNames = shenbinNames;
	}

	public String getXuemoNames() {
		return xuemoNames;
	}

	public void setXuemoNames(String xuemoNames) {
		this.xuemoNames = xuemoNames;
	}

}