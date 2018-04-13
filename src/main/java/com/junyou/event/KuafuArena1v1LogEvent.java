package com.junyou.event;

import com.junyou.log.LogPrintHandle;

public class KuafuArena1v1LogEvent extends AbsGameLogEvent {

	private static final long serialVersionUID = 1L;

	private long userRoleId;
	private String name;
	private int beforeJifen;
	private int afterJifen;
	private String opponentName;
	
	public KuafuArena1v1LogEvent( long userRoleId, String name,
			int beforeJifen, int afterJifen, String opponentName) {
		super(LogPrintHandle.KUAFU_ARENA_1V1);
		this.userRoleId = userRoleId;
		this.name = name;
		this.beforeJifen = beforeJifen;
		this.afterJifen = afterJifen;
		this.opponentName = opponentName;
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
	public String getOpponentName() {
		return opponentName;
	}
	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}

	
}