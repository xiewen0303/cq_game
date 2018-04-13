package com.junyou.bus.dati.configure;

import com.junyou.gameconfig.publicconfig.configure.export.AdapterPublicConfig;

/** 答题公共表配置 */
public class DaTiPublicConfig extends AdapterPublicConfig{

	/** 每次活动总题数 */
	private int tiSum;
	/** 每道题总时间 */
	private int eachSumTime;
	/** 每道题答题时间 */
	private int eachAnswerTime;
	/** 每次活动初始双倍积分卡数量 */
	private int initDoublePointCardNum;
	/** 每次活动初始排除卡数量 */
	private int initExcludeCard;
	/** 显示排名上限 */
	private int showRankLimit;

	/** 排名结算上限 */
	private int settlementLimit;

	public int getTiSum() {
		return tiSum;
	}

	public void setTiSum(int tiSum) {
		this.tiSum = tiSum;
	}

	public int getEachSumTime() {
		return eachSumTime;
	}

	public void setEachSumTime(int eachSumTime) {
		this.eachSumTime = eachSumTime;
	}

	public int getEachAnswerTime() {
		return eachAnswerTime;
	}

	public void setEachAnswerTime(int eachAnswerTime) {
		this.eachAnswerTime = eachAnswerTime;
	}

	public int getInitDoublePointCardNum() {
		return initDoublePointCardNum;
	}

	public void setInitDoublePointCardNum(int initDoublePointCardNum) {
		this.initDoublePointCardNum = initDoublePointCardNum;
	}

	public int getInitExcludeCard() {
		return initExcludeCard;
	}

	public void setInitExcludeCard(int initExcludeCard) {
		this.initExcludeCard = initExcludeCard;
	}

	public int getSettlementLimit() {
		return settlementLimit;
	}

	public void setSettlementLimit(int settlementLimit) {
		this.settlementLimit = settlementLimit;
	}

	public int getShowRankLimit() {
		return showRankLimit;
	}

	public void setShowRankLimit(int showRankLimit) {
		this.showRankLimit = showRankLimit;
	}

}
